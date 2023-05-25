package com.term4.BankingAppGrp1.repositories;

import com.term4.BankingAppGrp1.models.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;


@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long>, JpaRepository<Transaction, Long>, CustomTransactionRepository {

    Page<Transaction> getTransactionsWithFilters(Pageable pageable, String ibanFrom, String ibanTo, Double amountMin, Double amountMax, LocalDate dateBefore, LocalDate dateAfter);
}
