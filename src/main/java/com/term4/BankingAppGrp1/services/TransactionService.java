package com.term4.BankingAppGrp1.services;

import com.term4.BankingAppGrp1.models.Transaction;
import com.term4.BankingAppGrp1.repositories.TransactionRepository;
import com.term4.BankingAppGrp1.responseDTOs.TransactionDTO;
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
    public Transaction addTransaction(TransactionDTO transactionDTO) { return transactionRepository.save(mapDtoToTransaction(transactionDTO)); }

    private Transaction mapDtoToTransaction(TransactionDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(dto.accountFrom());
        transaction.setAccountTo(dto.accountTo());
        transaction.setAmount(dto.amount());
        transaction.setDate(dto.date());
        transaction.setTimestamp(dto.timestamp());
        transaction.setUserPerforming(dto.userPerforming());
        return transaction;
    }
}