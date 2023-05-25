package com.term4.BankingAppGrp1.services;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.repositories.AccountRepository;
import com.term4.BankingAppGrp1.requestDTOs.CreatingAccountDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.naming.LimitExceededException;
import java.util.List;
import java.util.function.LongFunction;

import static com.term4.BankingAppGrp1.models.ConstantsContainer.*;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserService userService;


    public AccountService(AccountRepository accountRepository, UserService userService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
    }

    // TODO: Delete the method later
    public void saveAccount(Account newAccount) {

        accountRepository.save(newAccount);
    }

    public Account saveAccount(CreatingAccountDTO creatingAccountDTO) throws LimitExceededException {
        if ((AccountType.valueOf(creatingAccountDTO.accountType().toUpperCase()).equals(AccountType.CURRENT)))
            checkIfUserHasReachedAccountLimit(AccountType.CURRENT, creatingAccountDTO.accountHolderId(), DEFAULT_CURRENT_ACCOUNT_LIMIT);
        else
            checkIfUserHasReachedAccountLimit(AccountType.SAVINGS, creatingAccountDTO.accountHolderId(), DEFAULT_SAVINGS_ACCOUNT_LIMIT);
       return accountRepository.save(parseCreatingAccountDTOToAccount(creatingAccountDTO));
    }


    private void checkIfUserHasReachedAccountLimit(AccountType accountType, long userId, int limit) throws LimitExceededException {
        int accountCount = accountRepository.countAccountByCustomer_IdEqualsAndAccountTypeEquals(userId, accountType);
        LongFunction<Boolean> limitFunction = count -> count >= limit; // function
        if (Boolean.TRUE.equals(limitFunction.apply(accountCount))) {
            throw new LimitExceededException("The user has reached the maximum limit for " + accountType + " accounts.");
        }
    }


    public List<Account> getAllAccounts(Pageable pageable, AccountType accountType) {
        Page<Account> accounts;
        if (accountType != null)
            // getting all accounts except the own  bank account when account type is specified
            accounts = accountRepository.findAccountByAccountTypeEqualsAndIbanNot(pageable, accountType, DEFAULT_INHOLLAND_BANK_IBAN);
        else
            // getting all accounts except the own  bank account when account type is not specified
            accounts = accountRepository.findByAndIbanNot(pageable, DEFAULT_INHOLLAND_BANK_IBAN);
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

    private Account parseCreatingAccountDTOToAccount(CreatingAccountDTO creatingAccountDTO) {
        User accountHolder = userService.getUser(creatingAccountDTO.accountHolderId());
        accountHolder.setDayLimit(creatingAccountDTO.dayLimit());
        accountHolder.setTransactionLimit(creatingAccountDTO.transactionLimit());
        // converting the account type to uppercase to match the enum values
        return new Account(AccountType.valueOf(creatingAccountDTO.accountType().toUpperCase()), accountHolder);
    }
}
