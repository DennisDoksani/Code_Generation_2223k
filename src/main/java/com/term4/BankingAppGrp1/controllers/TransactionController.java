package com.term4.BankingAppGrp1.controllers;

import com.term4.BankingAppGrp1.models.Role;
import com.term4.BankingAppGrp1.models.Transaction;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.requestDTOs.ATMDepositDTO;
import com.term4.BankingAppGrp1.requestDTOs.ATMWithdrawDTO;
import com.term4.BankingAppGrp1.requestDTOs.TransactionDTO;
import com.term4.BankingAppGrp1.responseDTOs.ErrorMessageDTO;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_LIMIT_STRING;
import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_OFFSET_STRING;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/transactions")
public class TransactionController {
    private TransactionService transactionService;
    private AccountService accountService;
    private UserService userService;

    //Authority check by Bijay
    private final Predicate<GrantedAuthority> isEmployee =
            a -> a.getAuthority().equals(Role.ROLE_EMPLOYEE.name());

    public TransactionController(TransactionService transactionService, AccountService accountService, UserService userService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
        this.userService = userService;
    }

//    @GetMapping
//    public ResponseEntity<Object> getAllTransactions() {
//        return ResponseEntity.ok().body(transactionService.getAllTransactions());
//    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('CUSTOMER', 'EMPLOYEE')")
    public ResponseEntity<Object> getTransactionsWithFilters(@RequestParam(defaultValue = DEFAULT_LIMIT_STRING, required = false) int limit,
                                                             @RequestParam(defaultValue = DEFAULT_OFFSET_STRING, required = false) int offset,
                                                             @RequestParam(required = false) String ibanFrom,
                                                             @RequestParam(required = false) String ibanTo,
                                                             @RequestParam(required = false) Double amountMin,
                                                             @RequestParam(required = false) Double amountMax,
                                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateBefore,
                                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateAfter,
                                                             @AuthenticationPrincipal UserDetails jwtUser) throws Exception {
        if(jwtUser.getAuthorities().stream().noneMatch(isEmployee) && ibanFrom == null && ibanTo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Exception(
                            "Standard customers only have access to their own transaction data"
                    ));
        }
        List<Transaction> transactions = transactionService.getTransactionsWithFilters(getPageable(limit, offset), ibanFrom, ibanTo,
                amountMin, amountMax, dateBefore, dateAfter);

        return ResponseEntity.ok().body(transactions);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'EMPLOYEE')")
    public ResponseEntity<Object> addTransaction(@RequestBody @Valid TransactionDTO transactionDTO,
                                                 @AuthenticationPrincipal UserDetails jwtUser) {
        if (transactionService.validTransaction(transactionDTO)) {
            User userPerforming = userService.getUserByEmail(jwtUser.getUsername());
            transactionService.changeBalance(transactionDTO.amount(), transactionDTO.accountFrom(), transactionDTO.accountTo());
            Transaction newTransaction = transactionService.addTransaction(transactionDTO, userPerforming);
            return ResponseEntity.status(HttpStatus.CREATED).body(newTransaction);
        }

        return null;
    }

    @PostMapping("/atm/deposit")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> depositByAtm(@RequestBody @Valid ATMDepositDTO depositDTO
            , @AuthenticationPrincipal UserDetails jwtUser) {
        // checking if the logged user is trying to deposit to his own account or not
        if (!accountService.isAccountOwnedByCustomer(depositDTO.accountTo(), jwtUser.getUsername()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorMessageDTO("You are not allowed to deposit to this account"));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.atmDeposit(depositDTO, jwtUser.getUsername()));
    }

    @PostMapping("/atm/withdraw")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> withdrawByAtm(@RequestBody @Valid ATMWithdrawDTO withdrawDTO,
                                                @AuthenticationPrincipal UserDetails jwtUser
    ) {
        // checking if the logged user is trying to withdraw from his own account or not
        if (!accountService.isAccountOwnedByCustomer(withdrawDTO.accountFrom(), jwtUser.getUsername()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorMessageDTO("You are not allowed to withdraw from this account"));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.atmWithdraw(withdrawDTO, jwtUser.getUsername()));
    }

    private Pageable getPageable(int limit, int offset) {
        return PageRequest.of(offset / limit, limit); // offset/limit = page number
    }
}
