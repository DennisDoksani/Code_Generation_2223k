package com.term4.BankingAppGrp1.repositories;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AccountRepository extends JpaRepository<Account, String>, JpaSpecificationExecutor<Account> {

    Page<Account> findAccountByAccountTypeEqualsAndIbanNot(Pageable pageable, AccountType accountType, String iban);

    Page<Account> findByAndIbanNot(Pageable pageable, String iban);

    List<Account> findByCustomer_IdEquals(long id);


    int countAccountByCustomer_IdEqualsAndAccountTypeEquals(long customerId, AccountType accountType);

    boolean existsAccountByIbanEqualsAndCustomerEmailEqualsIgnoreCase(String iban, String email);


}
