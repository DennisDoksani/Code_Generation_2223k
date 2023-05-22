package com.term4.BankingAppGrp1.services;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.repositories.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {

        this.accountRepository = accountRepository;
    }

    public void saveAccount(Account newAccount) {
        accountRepository.save(newAccount);
    }

    public List<Account> getAllAccounts(Pageable pageable) {
        Page<Account> accounts = accountRepository.findAll(pageable);
        return accounts.getContent();
    }

    public Account getAccountByIBAN(String iBAN) {
        return accountRepository.findById(iBAN).orElseThrow(() ->
                new EntityNotFoundException("Account with IBAN: " + iBAN + " was not found"));
    }
    public List<Account> searchAccountByCustomerName(String customerName, Pageable pageable) {
//        Page<Account> accounts = accountRepository.
//                findByCustomerNameIgnoreCase(customerName);
//        return accounts.getContent();
        return accountRepository.findByCustomerFirstNameIgnoreCaseOrCustomerLastNameIgnoreCase(customerName);
    }
}
