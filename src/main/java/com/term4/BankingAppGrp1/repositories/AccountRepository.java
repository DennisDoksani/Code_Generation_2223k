package com.term4.BankingAppGrp1.repositories;

import com.term4.BankingAppGrp1.models.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, String>, PagingAndSortingRepository<Account, String> {

}
