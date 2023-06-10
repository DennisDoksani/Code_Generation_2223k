package com.term4.BankingAppGrp1.services;

import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_CURRENT_ACCOUNT_LIMIT;
import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_INHOLLAND_BANK_IBAN;
import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_SAVINGS_ACCOUNT_LIMIT;
import static com.term4.BankingAppGrp1.repositories.AccountSpecifications.hasCustomerEmail;
import static com.term4.BankingAppGrp1.repositories.AccountSpecifications.hasCustomerName;
import static com.term4.BankingAppGrp1.repositories.AccountSpecifications.isActiveAccounts;
import static com.term4.BankingAppGrp1.repositories.AccountSpecifications.isCurrentAccounts;
import static com.term4.BankingAppGrp1.repositories.AccountSpecifications.isNotBanksOwnAccount;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.repositories.AccountRepository;
import com.term4.BankingAppGrp1.requestDTOs.CreatingAccountDTO;
import com.term4.BankingAppGrp1.requestDTOs.UpdatingAccountDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.LongFunction;
import javax.naming.LimitExceededException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private final AccountRepository accountRepository;
  private final UserService userService;
  private final TransactionService transactionService;
  private final BiFunction<Integer, Integer, Pageable> getPageableByLimitAndOffset = (limit, offset) ->
      PageRequest.of((offset / limit), limit);

  public AccountService(AccountRepository accountRepository, UserService userService,
      TransactionService transactionService) {
    this.accountRepository = accountRepository;
    this.userService = userService;
    this.transactionService = transactionService;
  }

  // this method is used for internal working with app No new account should be made from user side
  // because it doesn't check account creation limit
  public void saveAccount(Account newAccount) {
    accountRepository.save(newAccount);
  }

  @Transactional // to make sure that the transaction is atomic
  public Account updateAccountDetails(String iban, UpdatingAccountDTO account) {
    Account accountToUpdate = accountRepository.findById(iban)
        .orElseThrow(
            () -> new EntityNotFoundException("The account with IBAN: " + iban + " Which you are " +
                "trying to update does not exist"));

    accountToUpdate.setActive(account.isActive()); // updating the account status
    accountToUpdate.setAbsoluteLimit(account.absoluteLimit()); // updating the absolute limit
    User accountHolder = accountToUpdate.getCustomer(); // updating account holder
    accountHolder.setFirstName(account.accountHolder().firstName());
    accountHolder.setLastName(account.accountHolder().lastName());
    accountHolder.setDayLimit(account.accountHolder().dayLimit());
    accountHolder.setTransactionLimit(account.accountHolder().transactionLimit());
    userService.saveUserWithoutHashingPassword(accountHolder); // saving the updated account holder
    // no need to hash the password again
    return accountRepository.save(accountToUpdate); // saving the updated account
  }

  // this method is used to create a new account from user side and
  // it performs the limit check of account creation for the user
  public Account saveAccountWithLimitCheck(CreatingAccountDTO creatingAccountDTO)
      throws LimitExceededException {
    if ((AccountType.valueOf(creatingAccountDTO.accountType().toUpperCase())
        .equals(AccountType.CURRENT))) {
      checkIfUserHasReachedAccountLimit(AccountType.CURRENT,
          creatingAccountDTO.accountHolderId(),
          DEFAULT_CURRENT_ACCOUNT_LIMIT);
    } else {
      checkIfUserHasReachedAccountLimit(AccountType.SAVINGS,
          creatingAccountDTO.accountHolderId(),
          DEFAULT_SAVINGS_ACCOUNT_LIMIT);
    }
    Account account = mapCreatingAccountDTOToAccount(creatingAccountDTO);
    userService.saveUser(account.getCustomer()); // will get update with the new Limits for the user
    return accountRepository.save(account);
  }


  private void checkIfUserHasReachedAccountLimit(AccountType accountType, long userId, int limit)
      throws LimitExceededException {
    int accountCount = accountRepository.countAccountByCustomer_IdEqualsAndAccountTypeEquals(userId,
        accountType);
    LongFunction<Boolean> limitFunction = count -> count >= limit; // function
    if (Boolean.TRUE.equals(limitFunction.apply(accountCount))) {
      throw new LimitExceededException(
          "The user has reached the maximum limit for " + accountType + " accounts.");
    }
  }


  public List<Account> getAllAccounts(int limit, int offset, AccountType accountType) {
    Page<Account> accounts;
    if (accountType != null)
    // getting all accounts except the own  bank account when account type is specified
    {
      accounts = accountRepository.findAccountByAccountTypeEqualsAndIbanNot(
          getPageableByLimitAndOffset.apply(limit, offset), accountType,
          DEFAULT_INHOLLAND_BANK_IBAN);
    } else
    // getting all accounts except the own  bank account when account type is not specified
    {
      accounts = accountRepository.findByAndIbanNot(
          getPageableByLimitAndOffset.apply(limit, offset)
          , DEFAULT_INHOLLAND_BANK_IBAN);
    }
    return accounts.getContent();
  }

  public Account getAccountByIBAN(String iban) {
    return accountRepository.findById(iban).orElseThrow(() ->
        new EntityNotFoundException("Account with IBAN: " + iban + " was not found"));
  }

  public List<Account> searchAccountByCustomerName(String customerName, int limit, int offset) {
    Page<Account> accounts = accountRepository.findAll(
        hasCustomerName(customerName)
            .and(isCurrentAccounts())
            .and(isActiveAccounts())
            .and(isNotBanksOwnAccount()),
        // adding criteria to the query to filter the result to be accessed by apis
        getPageableByLimitAndOffset.apply(limit, offset)
    );

    // getting all the accounts  with default criteria
    return accounts.getContent();
  }

  public void changeAccountStatus(String iban, Boolean isActive) {
    Account updatingAccount = accountRepository.findById(iban).orElseThrow(() ->
        new EntityNotFoundException("The updating account with IBAN: " + iban + " was not found"));
    updatingAccount.setActive(isActive);
    accountRepository.save(updatingAccount);
  }

  private Account mapCreatingAccountDTOToAccount(CreatingAccountDTO creatingAccountDTO) {
    User accountHolder = userService.getUser(creatingAccountDTO.accountHolderId());
    accountHolder.setDayLimit(creatingAccountDTO.dayLimit());
    accountHolder.setTransactionLimit(creatingAccountDTO.transactionLimit());
    // converting the account type to uppercase to match the enum values
    return Account.builder()
        .accountType(AccountType.valueOf(creatingAccountDTO.accountType().toUpperCase()))
        .customer(accountHolder)
        .build();

  }

  // this method will be used to get all the accounts of a customer
  // only the active accounts will be returned
  // the email case is insensitive
  public List<Account> getAccountsByEmailAddress(String email) {
    return accountRepository.findAll(
        hasCustomerEmail(email)
            .and(isActiveAccounts())
    );
  }

  public double getTotalTransactedAmountOfTodayByUserEmail(String userEmail) {
    return transactionService.getSumOfMoneyTransferred(userEmail, LocalDate.now());
  }

  public boolean isAccountOwnedByCustomer(String iban, String email) {
    return accountRepository.existsAccountByIbanEqualsAndCustomerEmailEqualsIgnoreCase(iban, email);
  }

  public List<Account> getAccountsByUserId(long id) {
    return accountRepository.findByCustomer_IdEquals(id);
  }

}
