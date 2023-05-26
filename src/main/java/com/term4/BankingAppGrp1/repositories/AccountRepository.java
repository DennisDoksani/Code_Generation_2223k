package com.term4.BankingAppGrp1.repositories;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account,String> ,PagingAndSortingRepository<Account, String> {
  //TODO : Add active status and current Accounts only
   Page<Account> findByCustomerNameContainingIgnoreCase(String customerName, Pageable pageable);
   Page<Account> findAccountByAccountTypeEqualsAndIbanNot( Pageable pageable,AccountType accountType,String iban);
   Page<Account> findByAndIbanNot(Pageable pageable,String iban);
    int countAccountByCustomer_IdEqualsAndAccountTypeEquals(long customerId, AccountType accountType);

    List<Account> findByCustomer_EmailEquals(String email);
}
