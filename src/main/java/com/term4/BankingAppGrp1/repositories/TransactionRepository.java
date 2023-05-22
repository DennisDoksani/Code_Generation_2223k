package com.term4.BankingAppGrp1.repositories;

import com.term4.BankingAppGrp1.models.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.accountFrom.IBAN = :iban")
    Iterable<Transaction> getTransactionByAccountFrom(String iban);
}
