package com.term4.BankingAppGrp1.services;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.repositories.AccountRepository;
import com.term4.BankingAppGrp1.requestDTOs.CreatingAccountDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserService userService;



    public AccountService(AccountRepository accountRepository, UserService userService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
    }

    public void saveAccount(Account newAccount) {
        accountRepository.save(newAccount);
    }

    public List<Account> getAllAccounts(Pageable pageable, AccountType accountType) {
        Page<Account> accounts;
        if (accountType != null)
            accounts = accountRepository.findAccountByAccountTypeEquals( pageable,accountType);
        else
            accounts = accountRepository.findAll(pageable);
        return accounts.getContent();
    }

    public Account getAccountByIBAN(String iban) {
        return accountRepository.findById(iban).orElseThrow(() ->
                new EntityNotFoundException("Account with IBAN: " + iban + " was not found"));
    }

    public List<Account> searchAccountByCustomerName(String customerName, Pageable pageable) {
        Page<Account> accounts = accountRepository.findByCustomerNameContainingIgnoreCase(customerName, pageable);
        return accounts.getContent();
    }

    public void changeAccountStatus(String iban, boolean isActive) {
        Account updatingAccount = accountRepository.findById(iban).orElseThrow(() ->
                new EntityNotFoundException("The updating account with IBAN: " + iban + " was not found"));
        updatingAccount.setActive(isActive);
        accountRepository.save(updatingAccount);
    }
    private Account parseCreatingAccountDTOToAccount(CreatingAccountDTO creatingAccountDTO){
        return new Account(); // TODO: i have to make it
    }
}
