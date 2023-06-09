package com.term4.BankingAppGrp1.repositories;

import com.term4.BankingAppGrp1.models.Transaction;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomTransactionRepository {

  Page<Transaction> getTransactionsWithFilters(Pageable pageable, String ibanFrom, String ibanTo,
      Double amountMin, Double amountMax, LocalDate dateBefore, LocalDate dateAfter);

}
