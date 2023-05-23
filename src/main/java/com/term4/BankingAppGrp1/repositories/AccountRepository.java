package com.term4.BankingAppGrp1.repositories;

import com.term4.BankingAppGrp1.models.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account,String> ,PagingAndSortingRepository<Account, String> {
  //TODO : Add active status and current Accounts only
   Page<Account> findByCustomerNameContainingIgnoreCase(String customerName, Pageable pageable);

}
