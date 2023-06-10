package com.term4.BankingAppGrp1.services;

import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_INHOLLAND_BANK_IBAN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.term4.BankingAppGrp1.configuration.ApiTestConfiguration;
import com.term4.BankingAppGrp1.generators.IBANGenerator;
import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.models.InhollandIBANPattern;
import com.term4.BankingAppGrp1.models.Role;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.repositories.AccountRepository;
import com.term4.BankingAppGrp1.requestDTOs.UpdatingAccountDTO;
import com.term4.BankingAppGrp1.responseDTOs.AccountHolderDTO;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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
        .isActive(true)
        .roles(List.of(Role.ROLE_EMPLOYEE))
        .build();
    currentAccount = Account.builder().accountType(AccountType.CURRENT).customer(seedEmployee)
        .iban("NL20INHO00000000021")
        .build();
    savingsAccount = Account.builder().accountType(AccountType.SAVINGS).customer(seedEmployee)
        .build();

    accountHolderDTO = new AccountHolderDTO(seedEmployee.getId(),seedEmployee.getDayLimit(),
        seedEmployee.getTransactionLimit(),seedEmployee.getFirstName(), seedEmployee.getLastName());
    updatingAccountDTO = new UpdatingAccountDTO(currentAccount.getAbsoluteLimit(),currentAccount.isActive(),accountHolderDTO);
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
    Exception exception = Assertions.catchException(() -> accountService.getAccountByIBAN("NL234INHO00000000032"));
    Assertions.assertThat(exception)
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("Account with IBAN: " + "NL234INHO00000000032" + " was not found");
  }
  @Test
  void saveAccountMethodShouldSaveAccount() {
    accountService.saveAccount(currentAccount);
    Mockito.verify(accountRepository, Mockito.times(1)).save(currentAccount);
  }
  @Test
  void UpdateAccountDetailsWithNonExistingIBanShouldThrowEntityNotFoundExceptionWithMessage(){
    when(accountRepository.findById(DEFAULT_INHOLLAND_BANK_IBAN)).thenReturn(Optional.of(inhollandBankAccount));
    Exception exception = Assertions.catchException(() ->
        accountService.updateAccountDetails("NL23INHOoooooo1",updatingAccountDTO ));
    Assertions.assertThat(exception)
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("The account with IBAN: " + "NL23INHOoooooo1" + " Which you are " +
            "trying to update does not exist");
  }
  @Test
  void updatingAccountDetailsWithExistingIBanShouldUpdateAccountDetails() {
    // Mock the repository and userService methods
    when(accountRepository.findById(currentAccount.getIban())).thenReturn(Optional.of(currentAccount));
    when(accountRepository.save(currentAccount)).thenReturn(currentAccount); // Mock the save method to return the updatedAccount object
    when(userService.getUser(currentAccount.getCustomer().getId())).thenReturn(seedEmployee);

    // Call the updateAccountDetails method
    Account updatedAccount = accountService.updateAccountDetails(currentAccount.getIban(), updatingAccountDTO);

    // Perform assertions
    assertEquals(currentAccount, updatedAccount);
    Mockito.verify(accountRepository, Mockito.times(1)).save(currentAccount);
  }




}
