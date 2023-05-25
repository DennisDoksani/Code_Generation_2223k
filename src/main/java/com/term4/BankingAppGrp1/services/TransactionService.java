package com.term4.BankingAppGrp1.services;

import com.term4.BankingAppGrp1.models.Transaction;
import com.term4.BankingAppGrp1.repositories.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
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

    public List<Transaction> getTransactionsWithFilters(Pageable pageable, String ibanFrom, String ibanTo, Double amountMin, Double amountMax, LocalDate dateBefore, LocalDate dateAfter) {
        return transactionRepository.getTransactionsWithFilters(pageable, ibanFrom, ibanTo, amountMin, amountMax, dateBefore, dateAfter).getContent();
    }
//    public List<Transaction> getTransactionsToAccount(String iban) {
//        return transactionRepository.getTransactionByAccountTo(iban).getContent();
//    }
    public Transaction addTransaction(Transaction transaction) { return transactionRepository.save(transaction); }
}