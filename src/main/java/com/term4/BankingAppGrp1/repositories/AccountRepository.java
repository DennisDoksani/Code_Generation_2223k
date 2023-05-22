package com.term4.BankingAppGrp1.repositories;

import com.term4.BankingAppGrp1.models.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, String>, PagingAndSortingRepository<Account, String> {

    List<Account> findByCustomerFirstNameIgnoreCaseOrCustomerLastNameIgnoreCase(String firstName, String lastName);
}
