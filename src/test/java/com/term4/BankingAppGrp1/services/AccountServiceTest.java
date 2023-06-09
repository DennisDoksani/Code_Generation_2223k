package com.term4.BankingAppGrp1.services;

import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_INHOLLAND_BANK_IBAN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.models.InhollandIBANPattern;
import com.term4.BankingAppGrp1.models.Role;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.repositories.AccountRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
 class AccountServiceTest {

  @MockBean
  private AccountRepository accountRepository;
  @MockBean
  private AccountService accountService;


  private Account currentAccount;

  private Account savingsAccount;
  private Account inhollandBankAccount;

  @BeforeEach
  void main() {
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
    currentAccount = Account.builder().accountType(AccountType.CURRENT).customer(seedEmployee)
        .build();
    savingsAccount = Account.builder().accountType(AccountType.SAVINGS).customer(seedEmployee)
        .build();
  }

  @Test
  void getAccountByIban() {
    when(accountRepository.findById(DEFAULT_INHOLLAND_BANK_IBAN))
        .thenReturn(Optional.of(inhollandBankAccount));

    Account account = accountService.getAccountByIBAN(DEFAULT_INHOLLAND_BANK_IBAN);

    assertEquals(inhollandBankAccount, account);
  }



}
