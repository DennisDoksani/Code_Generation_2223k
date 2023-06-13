package com.term4.BankingAppGrp1.services;

import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_INHOLLAND_BANK_IBAN;
import static com.term4.BankingAppGrp1.models.Role.ROLE_CUSTOMER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.term4.BankingAppGrp1.configuration.ApiTestConfiguration;
import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.models.Role;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.repositories.AccountRepository;
import com.term4.BankingAppGrp1.requestDTOs.CreatingAccountDTO;
import com.term4.BankingAppGrp1.requestDTOs.UpdatingAccountDTO;
import com.term4.BankingAppGrp1.responseDTOs.AccountHolderDTO;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.naming.LimitExceededException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import(ApiTestConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class AccountServiceTest {

  @MockBean
  private AccountRepository accountRepository;
  private AccountService accountService;
  @MockBean
  private UserService userService;
  @MockBean
  private TransactionService transactionService;


  private Account currentAccount;

  private Account savingsAccount;
  private Account inhollandBankAccount;
  private User seedEmployee;
  private CreatingAccountDTO creatingAccountDTO;

  private UpdatingAccountDTO updatingAccountDTO;
  private AccountHolderDTO accountHolderDTO;

  @BeforeEach
  void init() {

    accountService = new AccountService(accountRepository, userService, transactionService);
    User inhollandBank = User.builder()
        .bsn("227015277")
        .firstName("Inholland")
        .lastName("Bank")
        .dateOfBirth(LocalDate.now())
        .phoneNumber("680000000000")
        .email("inholland@bank.nl")
        .password("Inholland")
        .isActive(true)
        .transactionLimit(9999999999999999L)
        .dayLimit(9999999999999999L)
        .roles(List.of(Role.ROLE_EMPLOYEE))
        .build();

    inhollandBankAccount = Account.builder()
        .iban(DEFAULT_INHOLLAND_BANK_IBAN)
        .balance(9999999999999.0)
        .creationDate(LocalDate.now())
        .absoluteLimit(0)
        .isActive(true)
        .accountType(AccountType.CURRENT)
        .customer(inhollandBank)
        .build();
    seedEmployee = User.builder()
        .id(1L)
        .bsn("091287662")
        .firstName("Employee")
        .lastName("Seed")
        .dateOfBirth(LocalDate.of(1990, 1, 1))
        .phoneNumber("0611111111")
        .email("employee@seed.com")
        .password("password")
        .dayLimit(200.2)
        .transactionLimit(200.2)
        .isActive(true)
        .roles(List.of(Role.ROLE_EMPLOYEE))
        .build();
    // add customer role to employee
    currentAccount = Account.builder().accountType(AccountType.CURRENT).customer(seedEmployee)
        .iban("NL20INHO00000000021")
        .build();
    savingsAccount = Account.builder().accountType(AccountType.SAVINGS).customer(seedEmployee)
        .build();

    accountHolderDTO = new AccountHolderDTO(seedEmployee.getId(), seedEmployee.getDayLimit(),
        seedEmployee.getTransactionLimit(), seedEmployee.getFirstName(),
        seedEmployee.getLastName());
    updatingAccountDTO = new UpdatingAccountDTO(currentAccount.getAbsoluteLimit(),
        currentAccount.isActive(), accountHolderDTO);
    creatingAccountDTO = new CreatingAccountDTO(seedEmployee.getDayLimit(),
        seedEmployee.getTransactionLimit(),
        currentAccount.getAccountType().name(), seedEmployee.getId());

  }

  @Test
  void getAccountByIbanShouldFindAccount() {
    Mockito.when(accountRepository.findById(DEFAULT_INHOLLAND_BANK_IBAN))
        .thenReturn(Optional.of(inhollandBankAccount));

    Account account = accountService.getAccountByIBAN(DEFAULT_INHOLLAND_BANK_IBAN);
    assertEquals(inhollandBankAccount, account);
  }

  @Test
  void getAccountByIbanShouldThrowAnEntityNotFoundException() {
    Mockito.when(accountRepository.findById(DEFAULT_INHOLLAND_BANK_IBAN))
        .thenReturn(Optional.of(inhollandBankAccount));
    Exception exception = Assertions.catchException(
        () -> accountService.getAccountByIBAN("NL234INHO00000000032"));
    Assertions.assertThat(exception)
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("Account with IBAN: " + "NL234INHO00000000032" + " was not found");
  }

  @Test
  void saveAccountMethodShouldSaveAccount() {
    accountService.saveAccount(currentAccount);
    Mockito.verify(accountRepository, times(1)).save(currentAccount);
  }

  @Test
  void updateAccountDetailsWithNonExistingIBanShouldThrowEntityNotFoundExceptionWithMessage() {
    when(accountRepository.findById(DEFAULT_INHOLLAND_BANK_IBAN)).thenReturn(
        Optional.of(inhollandBankAccount));
    Exception exception = Assertions.catchException(() ->
        accountService.updateAccountDetails("NL23INHOoooooo1", updatingAccountDTO));
    Assertions.assertThat(exception)
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("The account with IBAN: " + "NL23INHOoooooo1" + " Which you are " +
            "trying to update does not exist");
  }

  @Test
  void updatingAccountDetailsWithExistingIBanShouldUpdateAccountDetails() {
    // Mock the repository and userService methods
    when(accountRepository.findById(currentAccount.getIban())).thenReturn(
        Optional.of(currentAccount));
    when(accountRepository.save(currentAccount)).thenReturn(
        currentAccount); // Mock the save method to return the updatedAccount object
    when(userService.getUser(currentAccount.getCustomer().getId())).thenReturn(seedEmployee);

    // Call the updateAccountDetails method
    Account updatedAccount = accountService.updateAccountDetails(currentAccount.getIban(),
        updatingAccountDTO);

    // Perform assertions
    assertEquals(currentAccount, updatedAccount);
    Mockito.verify(accountRepository, times(1)).save(currentAccount);
  }

  @Test
  void updatingAccountDetailsShouldNotUpdatePassword() {
    when(accountRepository.findById(currentAccount.getIban())).thenReturn(
        Optional.of(currentAccount));
    when(accountRepository.save(currentAccount)).thenReturn(
        currentAccount); // Mock the save method to return the updatedAccount object
    Account updatedAccount = accountService.updateAccountDetails(currentAccount.getIban(),
        updatingAccountDTO);
    assertEquals(updatedAccount.getCustomer().getPassword(),
        currentAccount.getCustomer().getPassword());

  }

  @Test
    //private checkIfUser has reachedLimit method which is private will also be tested here with
  void creatingAccountForUserShouldThrowLimitExceededExceptionWhenUserHasReachedTheLimitOfAccounts() {
    when(accountRepository.countAccountByCustomer_IdEqualsAndAccountTypeEquals(seedEmployee.getId(),
        AccountType.CURRENT))
        .thenReturn(3);
    Exception exception = Assertions.catchException(
        () -> accountService.createAccountWithLimitCheck(creatingAccountDTO));
    Assertions.assertThat(exception)
        .isInstanceOf(LimitExceededException.class)
        .hasMessage("The user has reached the maximum limit for " + AccountType.CURRENT.name()
            + " accounts.");
  }

  @Test
  void creatingAccountForUserShouldAddCustomerRoleAfterValidAccountCreation()
      throws LimitExceededException {
    User seedEmployeeCustomer = seedEmployee;
    seedEmployeeCustomer.setRoles(List.of(Role.ROLE_EMPLOYEE, ROLE_CUSTOMER));

    Account updatedCustomerAccount = new Account();
    updatedCustomerAccount.setCustomer(seedEmployeeCustomer);

    when(accountRepository
        .countAccountByCustomer_IdEqualsAndAccountTypeEquals(seedEmployee.getId(),
            AccountType.CURRENT))
        .thenReturn(2);
    when(accountRepository.save(Mockito.any(Account.class))).thenReturn(updatedCustomerAccount);
    when(userService.getUser(seedEmployee.getId())).thenReturn(seedEmployeeCustomer);

    Account createdAccount = accountService.createAccountWithLimitCheck(creatingAccountDTO);

    assertNotNull(createdAccount);
    assertEquals(createdAccount, updatedCustomerAccount);
    assertEquals(seedEmployeeCustomer, createdAccount.getCustomer());
    assertEquals(Arrays.asList(Role.ROLE_EMPLOYEE, ROLE_CUSTOMER),
        new ArrayList<>(createdAccount.getCustomer().getRoles()));
  }

  @Test
  void creatingAnAccountForUserShouldNotChangeTheStoredPassword() throws LimitExceededException {
    User seedEmployeeCustomer = seedEmployee;
    seedEmployeeCustomer.setRoles(List.of(Role.ROLE_EMPLOYEE, ROLE_CUSTOMER));

    Account updatedCustomerAccount = new Account();
    updatedCustomerAccount.setCustomer(seedEmployeeCustomer);

    when(accountRepository
        .countAccountByCustomer_IdEqualsAndAccountTypeEquals(seedEmployee.getId(),
            AccountType.CURRENT))
        .thenReturn(2);
    when(accountRepository.save(Mockito.any(Account.class))).thenReturn(updatedCustomerAccount);
    when(userService.getUser(seedEmployee.getId())).thenReturn(seedEmployeeCustomer);

    Account createdAccount = accountService.createAccountWithLimitCheck(creatingAccountDTO);

    // Assert that the stored password remains unchanged
    assertEquals(seedEmployee.getPassword(), createdAccount.getCustomer().getPassword());

  }

  @Test
  void getAllAccountsWithLimitAndCurrentAccountTypeShouldReturnCurrentAccountAndExceptBankAccount() {
    // Mock the repository method
    when(accountRepository.findAccountByAccountTypeEqualsAndIbanNot(PageRequest.of(0, 1),
        AccountType.CURRENT,
        DEFAULT_INHOLLAND_BANK_IBAN)) // in Service page request is determined by 0/2 which is 0
        .thenReturn(new PageImpl<>(List.of(currentAccount)));
    List<Account> accounts = accountService.getAllAccounts(1, 0, AccountType.CURRENT);

    assertEquals(List.of(currentAccount), accounts);
    assertEquals(1, accounts.size());
  }

  @Test
  void getAllAccountsWithOutAccountTypeShouldReturnAllAccountsExceptBankAccount() {
    when(accountRepository.findByAndIbanNot(PageRequest.of(0, 2),
        DEFAULT_INHOLLAND_BANK_IBAN)).thenReturn(
        new PageImpl<>(List.of(currentAccount, savingsAccount)));
    List<Account> accounts = accountService.getAllAccounts(2, 0, null);
    assertEquals(List.of(currentAccount, savingsAccount), accounts);
    assertEquals(2, accounts.size());
  }

  @Test
  void testSearchAccountByCustomerName() {
    String customerName = "Employee";
    int limit = 2;
    int offset = 0;

    when(accountRepository.findAll(any(Specification.class), eq(PageRequest.of(0, limit))))
        .thenReturn(new PageImpl<>(List.of(currentAccount, savingsAccount)));

    List<Account> result = accountService.searchAccountByCustomerName(customerName, limit, offset);

    assertEquals(List.of(currentAccount, savingsAccount), result);
  }

  @Test
  void changeAccountStatusShouldChangeAccountStatus() {
    when(accountRepository.findById(currentAccount.getIban())).thenReturn(
        Optional.of(currentAccount));
    when(accountRepository.save(currentAccount)).thenReturn(currentAccount);
    accountService.changeAccountStatus(currentAccount.getIban(), true);
    assertTrue(currentAccount.isActive());
  }

  @Test
  void changeAccountStatusShouldThrowEntityNotFoundExceptionWhenAccountDoesNotExist() {
    when(accountRepository.findById(currentAccount.getIban())).thenReturn(
        Optional.of(currentAccount));
    Exception exception = Assertions.catchException(
        () -> accountService.changeAccountStatus("NL62INGO00000", true));
    Assertions.assertThat(exception)
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("The updating account with IBAN: " + "NL62INGO00000" + " was not found");
  }

  @Test
  void getAccountsByEmailAddressShouldReturnOnlyAccountsWithPassedEmailAndActiveAccounts() {
    String email = seedEmployee.getEmail();
    when(accountRepository.findAll(any(Specification.class)))
        .thenReturn(List.of(currentAccount, savingsAccount));
    List<Account> accounts = accountService.getAccountsByEmailAddress(email);
    assertEquals(List.of(currentAccount, savingsAccount), accounts);
    assertEquals(2, accounts.size());
  }

  @Test
  void isAccountOwnedBYCustomerShouldReturnTrueWhenAccountIsOwnedByCustomerAndShouldBeCaseInSensitive() {
    when(accountRepository.existsAccountByIbanEqualsAndCustomerEmailEqualsIgnoreCase(
        currentAccount.getIban(), seedEmployee.getEmail())).thenReturn(true);

    boolean result = accountService.isAccountOwnedByCustomer(currentAccount.getIban(),
        seedEmployee.getEmail().toLowerCase());
    assertTrue(result);
  }

  @Test
  void getTotalTransactedAmountOfTodayShouldReturnTotalTransactedAmountOfToday() {
    when(transactionService.getSumOfMoneyTransferred(currentAccount.getIban(), LocalDate.now()))
        .thenReturn(100.0);
    assertEquals(100.0,
        accountService.getTotalTransactedAmountOfTodayByUserEmail(currentAccount.getIban()));
  }
}
