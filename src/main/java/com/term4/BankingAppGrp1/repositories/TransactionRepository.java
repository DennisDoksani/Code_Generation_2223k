package com.term4.BankingAppGrp1.repositories;

import com.term4.BankingAppGrp1.models.Transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, CustomTransactionRepository {

    Page<Transaction> getTransactionsWithFilters(Pageable pageable, String ibanFrom, String ibanTo, Double amountMin, Double amountMax, LocalDate dateBefore, LocalDate dateAfter);

    @Query("SELECT sum(t.amount) FROM Transaction t WHERE t.accountFrom.iban = :iban AND t.date = :date")
    Optional<Double> getSumOfMoneyTransferred(String iban, LocalDate date);
    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.balance = a.balance - :amount WHERE a.iban = :accountFrom")
    void decreaseBalanceByAmount(double amount, String accountFrom);

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.balance = a.balance + :amount WHERE a.iban = :accountTo")
    void increaseBalanceByAmount(double amount, String accountTo);
}
