package com.term4.BankingAppGrp1.controllers;

import com.term4.BankingAppGrp1.models.Transaction;
import com.term4.BankingAppGrp1.services.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_LIMIT_STRING;
import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_OFFSET_STRING;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllTransactions() {
        return ResponseEntity.ok().body(transactionService.getAllTransactions());
    }

    @GetMapping("/from")
    public ResponseEntity<Object> getTransactionsFromAccount(@RequestParam(defaultValue = DEFAULT_LIMIT_STRING, required = false) int limit,
                                                             @RequestParam String iban,
                                                             @RequestParam(required = false) Double amount) {
        return ResponseEntity.ok().body(transactionService.getTransactionsFromAccount(iban, amount));
    }
    @GetMapping("/to")
    public ResponseEntity<Object> getTransactionsToAccount(@RequestParam String iban) {
        return ResponseEntity.ok().body(transactionService.getTransactionsToAccount(iban));
    }
    @PostMapping
    public ResponseEntity<Object> addTransaction(@RequestBody Transaction transaction) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.addTransaction(transaction));
    }
}
