package com.term4.BankingAppGrp1.controllers;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.models.Transaction;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.requestDTOs.TransactionDTO;
import com.term4.BankingAppGrp1.responseDTOs.TransactionResponseDTO;
import com.term4.BankingAppGrp1.services.AccountService;
import com.term4.BankingAppGrp1.services.TransactionService;

import com.term4.BankingAppGrp1.services.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_LIMIT_STRING;
import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_OFFSET_STRING;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private TransactionService transactionService;
    private AccountService accountService;
    private UserService userService;
    public TransactionController(TransactionService transactionService, AccountService accountService, UserService userService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
        this.userService = userService;
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
                                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateAfter ) {
        List<Transaction> transactions = transactionService.getTransactionsWithFilters(getPageable(limit, offset), ibanFrom, ibanTo,
                amountMin, amountMax, dateBefore, dateAfter);


        return ResponseEntity.ok().body(transactions);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'EMPLOYEE')")
    public ResponseEntity<Object> addTransaction(@RequestBody @Valid TransactionDTO transactionDTO,
                                                 @AuthenticationPrincipal UserDetails jwtUser) {
        if(transactionService.validTransaction(transactionDTO)) {
            User userPerforming = userService.getUserByEmail(jwtUser.getUsername());
            transactionService.changeBalance(transactionDTO.amount(), transactionDTO.accountFrom(), transactionDTO.accountTo());
            Transaction newTransaction = transactionService.addTransaction(transactionDTO, userPerforming);
            return ResponseEntity.status(HttpStatus.CREATED).body(newTransaction);
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
}
