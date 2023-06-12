package com.term4.BankingAppGrp1.configuration;

import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_INHOLLAND_BANK_IBAN;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.models.Role;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.requestDTOs.TransactionDTO;
import com.term4.BankingAppGrp1.services.AccountService;
import com.term4.BankingAppGrp1.services.TransactionService;
import com.term4.BankingAppGrp1.services.UserService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements ApplicationRunner {

  private final AccountService accountService;
  private final UserService userService;
  private final TransactionService transactionService;

  public Runner(AccountService accountService, UserService userService,
      TransactionService transactionService) {

    this.accountService = accountService;
    this.userService = userService;
    this.transactionService = transactionService;
  }

  @Override
  @Transactional
  public void run(ApplicationArguments args) throws Exception {

    //Seed users and accounts
    User employeeCustomer = seedEmployeeCustomer();
    User customer = seedCustomer();
    seedAccounts(customer, employeeCustomer);
    seedEmployeeWithoutAccounts();
    //Seed the bank's account
    seedBankAccount();
    seedRuubyoUserWithTransactions();
  }

  private void seedRuubyoUserWithTransactions() {
    User ruubio = User.builder()
        .bsn("582022290")
        .firstName("Ruubyo")
        .lastName("Gaming")
        .dateOfBirth(LocalDate.of(2003, 10, 1))
        .phoneNumber("0611111121")
        .email("Ruubyo@isgaming.com")
        .password("secretword")
        .isActive(true)
        .dayLimit(300)
        .transactionLimit(300)
        .roles(List.of(Role.ROLE_EMPLOYEE))
        .build();
    userService.saveUser(ruubio);

    //Seed a transaction
    List<Account> accounts = makeDummyBankAccounts(ruubio, userService.getUser(1L));

    TransactionDTO newTransaction = new TransactionDTO(10.00, accounts.get(0).getIban(),
        accounts.get(1).getIban());
    transactionService.addTransaction(newTransaction, ruubio);

  }


  private void seedBankAccount() {
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

    userService.saveUser(inhollandBank);
    Account seedAccount = Account.builder()
        .iban(DEFAULT_INHOLLAND_BANK_IBAN)
        .balance(9999999999999.0)
        .creationDate(LocalDate.now())
        .absoluteLimit(0)
        .isActive(true)
        .accountType(AccountType.CURRENT)
        .customer(inhollandBank)
        .build();
    accountService.saveAccount(seedAccount);
  }

  private List<Account> makeDummyBankAccounts(User user, User secondSeedCustomer) {

    Account savings = Account.builder()
        .iban("NL01INHO0000000003")
        .balance(9999.0)
        .isActive(true)
        .accountType(AccountType.SAVINGS)
        .customer(user)
        .build();

    Account current = Account.builder()
        .iban("NL01INHO0000000002")
        .balance(2900.0)
        .isActive(true)
        .accountType(AccountType.CURRENT)
        .customer(user)
        .build();

    accountService.saveAccount(savings);
    accountService.saveAccount(current);

    Account seedAccount = Account.builder()
        .accountType(AccountType.CURRENT)
        .customer(secondSeedCustomer)
        .build();
    Account seedSavings = Account.builder()
        .accountType(AccountType.SAVINGS)
        .customer(secondSeedCustomer)
        .build();

    Account seedHardcodedIban = Account.builder()
        .iban("NL72INHO0579629781")
        .balance(900.0)
        .creationDate(LocalDate.now())
        .accountType(AccountType.CURRENT)
        .customer(secondSeedCustomer)
        .build();
    accountService.saveAccount(seedAccount);
    accountService.saveAccount(seedHardcodedIban);
    accountService.saveAccount(savings);
    accountService.saveAccount(current);
    accountService.saveAccount(seedSavings);

    return List.of(savings, current);
  }

  private void seedAccounts(User customer, User employeeCustomer) {
    //Seed some (200) bank current accounts
    accountService.saveAccount(Account.builder().accountType(AccountType.CURRENT).customer(customer)
        .iban("NL72INHO0579639781").build());
    for (int i = 0; i < 100; i++) {
      accountService.saveAccount(
          Account.builder().accountType(AccountType.CURRENT).customer(customer).build());
      accountService.saveAccount(
          Account.builder().accountType(AccountType.CURRENT).customer(employeeCustomer).build());
    }
    //Seed savings accounts
    accountService.saveAccount(
        Account.builder().accountType(AccountType.SAVINGS).customer(customer).build());
    accountService.saveAccount(
        Account.builder().accountType(AccountType.SAVINGS).customer(employeeCustomer).build());
  }

  private User seedEmployeeCustomer() {
    User seedEmployeeCustomer = User.builder()
        .bsn("277545146")
        .firstName("EmployeeCustomer")
        .lastName("Seed")
        .dateOfBirth(LocalDate.of(1990, 1, 1))
        .phoneNumber("0611111111")
        .email("employeecustomer@seed.com")
        .password("password")
        .isActive(true)
        .roles(List.of(Role.ROLE_EMPLOYEE, Role.ROLE_CUSTOMER))
        .dayLimit(1000)
        .transactionLimit(300)
        .build();
    userService.saveUser(seedEmployeeCustomer);
    return seedEmployeeCustomer;
  }

  private User seedCustomer() {
    User seedCustomer = User.builder()
        .bsn("509547989")
        .firstName("Customer")
        .lastName("Seed")
        .dateOfBirth(LocalDate.of(2000, 10, 10))
        .phoneNumber("0611111121")
        .email("customer@seed.com")
        .password("password")
        .isActive(true)
        .roles(List.of(Role.ROLE_CUSTOMER))
        .dayLimit(300)
        .transactionLimit(300)
        .build();
    userService.saveUser(seedCustomer);
    return seedCustomer;
  }

  private void seedEmployeeWithoutAccounts() {
    User seedEmployee = User.builder()
        .bsn("091287662")
        .firstName("Employee")
        .lastName("Seed")
        .dateOfBirth(LocalDate.of(1990, 1, 1))
        .phoneNumber("0611111111")
        .email("employee@seed.com")
        .password("password")
        .isActive(true)
        .roles(List.of(Role.ROLE_EMPLOYEE))
        .build();
    userService.saveUser(seedEmployee);
  }
} 