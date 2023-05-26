package com.term4.BankingAppGrp1.repositories;

import com.term4.BankingAppGrp1.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, CustomTransactionRepository, JpaSpecificationExecutor<Transaction> {

    Iterable<Transaction> getTransactionByAccountFromAndAmountGreaterThanEqual(String iban, Double amount);
    Iterable<Transaction> getTransactionByAccountTo(String iban);


}
