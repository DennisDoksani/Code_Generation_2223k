package com.term4.BankingAppGrp1.controllers;

import com.term4.BankingAppGrp1.models.Transaction;
import com.term4.BankingAppGrp1.services.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = {"/from/{iban}", "/from/{iban}/{amount}"})
    public ResponseEntity<Object> getTransactionsFromAccount(@PathVariable String iban, @PathVariable(required = false) Double amount) {
        return ResponseEntity.ok().body(transactionService.getTransactionsFromAccount(iban, amount));
    }
    @GetMapping("/to/{iban}")
    public ResponseEntity<Object> getTransactionsToAccount(@PathVariable String iban) {
        return ResponseEntity.ok().body(transactionService.getTransactionsToAccount(iban));
    }
    @PostMapping
    public ResponseEntity<Object> addTransaction(@RequestBody Transaction transaction) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.addTransaction(transaction));
    }
}
