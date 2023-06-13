package com.term4.BankingAppGrp1.repositories;

import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_INHOLLAND_BANK_IBAN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.term4.BankingAppGrp1.configuration.ApiTestConfiguration;
import com.term4.BankingAppGrp1.generators.IBANGenerator;
import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.testingData.BankingAppTestData;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.extern.java.Log;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) //making issolated for each tests
@Import(ApiTestConfiguration.class)
class AccountRepositoryTesst extends BankingAppTestData {

  @Autowired
  private AccountRepository accountRepository;
  @MockBean
  private IBANGenerator ibanGenerator;
  @Autowired
  private TestEntityManager entityManager;
  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    super.setupData();
  }

  @Test
  void savingAccountShouldReturnSavedAccount() {
    userRepository.save(employeeUser);
    Account savedAccount = accountRepository.save(employeeAccount);
    assertNotNull(savedAccount);
    assertEquals(employeeAccount.getBalance(), savedAccount.getBalance());
    assertEquals(employeeAccount.getAccountType(), savedAccount.getAccountType());
    assertEquals(employeeAccount.getCustomer(), savedAccount.getCustomer());
    assertEquals(employeeAccount.getIban(), savedAccount.getIban());
  }

  @Test
  void savingAccountShouldGenerateIban() {
    userRepository.save(employeeUser);
    Account newAccount = Account.builder().accountType(AccountType.CURRENT)
        .customer(employeeUser).balance(1000.0).build();
    Account savedAccount = accountRepository.save(newAccount);
    assertNotNull(savedAccount);
    assertNotNull(savedAccount.getIban());
  }

  @Test
  void gettingAllAccountsShouldReturnAllAccount() {
    userRepository.save(employeeUser);
    accountRepository.save(employeeAccount);
    accountRepository.save(Account.builder().accountType(AccountType.CURRENT)
        .customer(employeeUser).balance(1000.0).build());

    List<Account> allAccounts = accountRepository.findAll();
    assertNotNull(allAccounts);
    assertEquals(2, allAccounts.size());

  }

  @Test
  void gettingAccountIdShouldReturnAccountObject() {
    userRepository.save(employeeUser);
    accountRepository.save(employeeAccount);
    Optional<Account> foundAccount = accountRepository.findById(employeeAccount.getIban());
    assertNotNull(foundAccount);
  }

  @Test
  void gettingAccountIdShouldReturnEmptyOptional() {
    Optional<Account> foundAccount = accountRepository.findById(employeeAccount.getIban());
    assertEquals(Optional.empty(), foundAccount);
  }

  @Test
  void findByAndIbanNotShouldNotReturnAccountWhereIbanIs() {
    userRepository.save(employeeUser);
    userRepository.save(inhollandBankUser);
    accountRepository.save(employeeAccount);
    accountRepository.save(inhollandBankAccount);
    Page<Account> pageAccounts = accountRepository.findByAndIbanNot(PageRequest.of(0, 3),
        DEFAULT_INHOLLAND_BANK_IBAN);
    List<Account> foundAccounts = pageAccounts.getContent();
    assertEquals(1, foundAccounts.size());
    assertEquals(employeeAccount, foundAccounts.get(0));
    assertNotEquals(inhollandBankAccount, foundAccounts.get(0));
  }

  @Test
  void findAccountByAccountTypeEqualsAndIbanNotShouldNotReturnAccountWhereIbanIsAndTypeOfAccountSpecified() {
    userRepository.save(employeeUser);
    userRepository.save(inhollandBankUser);
    accountRepository.save(employeeAccount);
    accountRepository.save(inhollandBankAccount);
    accountRepository.save(Account.builder().accountType(AccountType.SAVINGS)
        .customer(employeeUser).balance(1000.0).build());
    Page<Account> pageAccounts = accountRepository.findAccountByAccountTypeEqualsAndIbanNot(
        PageRequest.of(0, 3),
        AccountType.SAVINGS, DEFAULT_INHOLLAND_BANK_IBAN);
    List<Account> foundAccounts = pageAccounts.getContent();
    assertEquals(1, foundAccounts.size());
    assertNotEquals(employeeAccount, foundAccounts.get(0));
    assertNotEquals(inhollandBankAccount, foundAccounts.get(0));
    assertEquals(AccountType.SAVINGS, foundAccounts.get(0).getAccountType());
  }

  @Test
  void findByCustomer_IDEqualsShouldReturnAllAccountsOfCustomer() {
    userRepository.save(employeeUser);
    userRepository.save(inhollandBankUser);
    accountRepository.save(employeeAccount);
    accountRepository.save(inhollandBankAccount);
    accountRepository.save(Account.builder().accountType(AccountType.SAVINGS)
        .customer(employeeUser).balance(1000.0).build());
    List<Account> foundAccounts = accountRepository.findByCustomer_IdEquals(employeeUser.getId());
    assertEquals(2, foundAccounts.size());
    assertEquals(employeeAccount, foundAccounts.get(0));
    assertNotEquals(inhollandBankAccount, foundAccounts.get(0));
    assertEquals(employeeUser.getId(), foundAccounts.get(0).getCustomer().getId());
    assertEquals(employeeUser.getId(), foundAccounts.get(1).getCustomer().getId());
  }

  @Test
  void countAccountByCustomer_IdEqualsAndAccountTypeEqualsShouldReturnNumberOfAccountOwned() {
    userRepository.save(employeeUser);
    accountRepository.save(employeeAccount);
    userRepository.save(inhollandBankUser);
    accountRepository.save(inhollandBankAccount);
    accountRepository.save(Account.builder().accountType(AccountType.CURRENT)
        .customer(employeeUser).balance(1000.0).build());
    int numberOfAccounts = accountRepository.countAccountByCustomer_IdEqualsAndAccountTypeEquals(
        employeeUser.getId(), AccountType.CURRENT);
    assertEquals(2, numberOfAccounts);

  }

  @Test
  void existAccountByIbanEqualsAndCustomerEmailEqualsIgnoreCaseShouldReturnTrueWhenTheyHaveAccount() {
    userRepository.save(employeeUser);
    accountRepository.save(employeeAccount);
    boolean accountExists = accountRepository.existsAccountByIbanEqualsAndCustomerEmailEqualsIgnoreCase(
        employeeAccount.getIban(), employeeUser.getEmail());
    assertTrue(accountExists);
  }

  @Test
  void existAccountByIbanEqualsAndCustomerEmailEqualsIgnoreCaseShouldReturnFalseWhenTheyDontHaveAccount() {
    userRepository.save(employeeUser);
    accountRepository.save(employeeAccount);
    boolean accountExists = accountRepository.existsAccountByIbanEqualsAndCustomerEmailEqualsIgnoreCase(
        employeeAccount.getIban(), inhollandBankUser.getEmail());
    assertFalse(accountExists);
  }


}
