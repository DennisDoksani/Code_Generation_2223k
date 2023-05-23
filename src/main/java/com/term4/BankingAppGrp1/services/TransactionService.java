package com.term4.BankingAppGrp1.services;

import com.term4.BankingAppGrp1.models.Transaction;
import com.term4.BankingAppGrp1.repositories.TransactionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getAllTransactions() {
        return (List<Transaction>) transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsFromAccount(String iban, Double amount) {
        return (List<Transaction>) transactionRepository.getTransactionByAccountFromAndAmountGreaterThanEqual(iban, amount);
    }
    public List<Transaction> getTransactionsToAccount(String iban) {
        return (List<Transaction>) transactionRepository.getTransactionByAccountTo(iban);
    }
    public Transaction addTransaction(Transaction transaction) { return transactionRepository.save(transaction); }
}
