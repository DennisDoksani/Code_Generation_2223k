package com.term4.BankingAppGrp1.repositories;

import com.term4.BankingAppGrp1.models.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomTransactionRepository  {
    Iterable<Transaction> getTransactionByAccountFromAndAmountGreaterThanEqual(String iban, Double amount);
}