package com.term4.BankingAppGrp1.repositories;

import com.term4.BankingAppGrp1.models.Transaction;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    Iterable<Transaction> getTransactionByAccountFrom(String iban);
    Iterable<Transaction> getTransactionByAccountTo(String iban);
}
