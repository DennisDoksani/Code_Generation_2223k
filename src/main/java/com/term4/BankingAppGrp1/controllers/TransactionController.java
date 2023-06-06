package com.term4.BankingAppGrp1.controllers;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.requestDTOs.ATMDepositDTO;
import com.term4.BankingAppGrp1.requestDTOs.ATMWithdrawDTO;
import com.term4.BankingAppGrp1.responseDTOs.TransactionDTO;
import com.term4.BankingAppGrp1.services.AccountService;
import com.term4.BankingAppGrp1.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_LIMIT_STRING;
import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_OFFSET_STRING;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private TransactionService transactionService;
    private AccountService accountService;

    public TransactionController(TransactionService transactionService, AccountService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

//    @GetMapping
//    public ResponseEntity<Object> getAllTransactions() {
//        return ResponseEntity.ok().body(transactionService.getAllTransactions());
//    }

    @GetMapping()   //Make dto for the accounts
    public ResponseEntity<Object> getTransactionsWithFilters(@RequestParam(defaultValue = DEFAULT_LIMIT_STRING, required = false) int limit,
                                                             @RequestParam(defaultValue = DEFAULT_OFFSET_STRING, required = false) int offset,
                                                             @RequestParam(required = false) String ibanFrom,
                                                             @RequestParam(required = false) String ibanTo,
                                                             @RequestParam(required = false) Double amountMin,
                                                             @RequestParam(required = false) Double amountMax,
                                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateBefore,
                                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateAfter) {
        return ResponseEntity.ok().body(transactionService.getTransactionsWithFilters(getPageable(limit, offset), ibanFrom, ibanTo, amountMin, amountMax, dateBefore, dateAfter));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'EMPLOYEE')")
    public ResponseEntity<Object> addTransaction(@RequestBody @Valid TransactionDTO transactionDTO) {
        if (validTransaction(transactionDTO)) {
            transactionService.changeBalance(transactionDTO.amount(), transactionDTO.accountFrom().getIban(), transactionDTO.accountTo().getIban());
            return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.addTransaction(transactionDTO));
        }
        return null;
    }

    @PostMapping("/atm/deposit")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'EMPLOYEE')")
    public ResponseEntity<Object> depositByAtm(@RequestBody @Valid ATMDepositDTO depositDTO) {
        Long userId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.atmDeposit(depositDTO, userId));
    }

    @PostMapping("/atm/withdraw")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'EMPLOYEE')")
    public ResponseEntity<Object> withdrawByAtm(@RequestBody @Valid ATMWithdrawDTO withdrawDTO) {
        Long userId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.atmWithdraw(withdrawDTO, userId));
    }

    private Pageable getPageable(int limit, int offset) {
        return PageRequest.of(offset / limit, limit); // offset/limit = page number
    }

    public Boolean validTransaction(TransactionDTO dto) {
        Account accountTo = dto.accountTo();
        Account accountFrom = dto.accountFrom();

        //This statement checks if money is being transferred to or from a savings account that does not belong to the same user
        if (((accountFrom.getAccountType() == AccountType.CURRENT && accountTo.getAccountType() == AccountType.SAVINGS) || (accountFrom.getAccountType() == AccountType.SAVINGS && accountTo.getAccountType() == AccountType.CURRENT)) && accountFrom.getCustomer().getBsn() != accountTo.getCustomer().getBsn())
            throw new IllegalArgumentException("You can not transfer money from or to a savings account that does not belong to the same user");

        if (accountFrom.getBalance() - dto.amount() < accountFrom.getAbsoluteLimit())
            throw new IllegalArgumentException("The amount you are trying to transfer will result in a balance lower than the absolute limit for this account");
        else if (dto.amount() > accountFrom.getBalance()) {
            //Implement some kind of warning for the user
        }

        if (dto.amount() > accountFrom.getCustomer().getTransactionLimit())
            throw new IllegalArgumentException("The amount you are trying to transfer exceeds the transaction limit");
        // Checks first if the accountFrom has transactions made today and then if the amount will exceed the day limit
        if (transactionService.getSumOfMoneyTransferred(accountFrom.getIban(), LocalDate.now()) > 0.0 && dto.amount() + transactionService.getSumOfMoneyTransferred(accountFrom.getIban(), LocalDate.now()) > accountFrom.getCustomer().getDayLimit())
            throw new IllegalArgumentException("The amount you are trying to transfer will exceed the day limit for this account");


        return true;
    }
}
