package com.term4.BankingAppGrp1.controllers;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.term4.BankingAppGrp1.configuration.ApiTestConfiguration;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.requestDTOs.AccountStatusDTO;
import com.term4.BankingAppGrp1.requestDTOs.CreatingAccountDTO;
import com.term4.BankingAppGrp1.requestDTOs.UpdatingAccountDTO;
import com.term4.BankingAppGrp1.responseDTOs.AccountHolderDTO;
import com.term4.BankingAppGrp1.services.AccountService;
import com.term4.BankingAppGrp1.services.UserService;
import com.term4.BankingAppGrp1.testingData.BankingAppTestData;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import javax.naming.LimitExceededException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@ExtendWith({SpringExtension.class})
@WebMvcTest(controllers = AccountController.class)
@Import(ApiTestConfiguration.class)
@EnableMethodSecurity
class AccountControllerTest extends BankingAppTestData {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private AccountService accountService;
  @MockBean
  private UserService userService;
  private CreatingAccountDTO creatingAccountDTO;
  private UpdatingAccountDTO updatingAccountDTO;

  @BeforeEach
  void init() {
    super.setupData();
    updatingAccountDTO = new UpdatingAccountDTO(customerAccount.getAbsoluteLimit(),
        customerAccount.isActive(), customerAccountHolderDTO);
    creatingAccountDTO = new CreatingAccountDTO(customerUser.getDayLimit(),
        customerUser.getTransactionLimit(),
        AccountType.CURRENT.name(), customerUser.getId());

  }

  @Test
  @WithAnonymousUser
  void whenNoAuthIsProvidedGetAllEndpointGivesUnauthorized() throws Exception {
    when(accountService.getAllAccounts(
        1,
        0,
        null
    )).thenReturn(List.of(employeeAccount));

    this.mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts")
                .param("limit", "1")
                .param("offset", "0"))
            .andDo(print())
            .andExpect(status().isUnauthorized());

  }

  @Test
  @WithMockUser(username = "customer", roles = {"CUSTOMER"})
  void whenCustomerAuthIsProvidedGetAllEndpointGivesForbidden() throws Exception {
    when(accountService.getAllAccounts(
        1,
        0,
        null
    )).thenReturn(List.of(employeeAccount));

    this.mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts")
                .param("limit", "1")
                .param("offset", "0")).andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.message").value("Access Denied"));
  }

  @Test
  @WithMockUser(username = "employee", roles = {"EMPLOYEE"})
  void whenEmployeeAuthIsProvidedGetAllEndpointGivesOKWithAccountDTO() throws Exception {
    when(accountService.getAllAccounts(
        1,
        0,
        null
    )).thenReturn(List.of(employeeAccount));

    this.mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts")
                .param("limit", "1")
                .param("offset", "0")).andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].iban").value(employeeAccount.getIban()))
        .andExpect(jsonPath("$[0].accountBalance").value(employeeAccount.getBalance()))
        .andExpect(jsonPath("$[0].accountType").value(employeeAccount.getAccountType().name()))
        .andExpect(jsonPath("$[0].absoluteLimit").value(employeeAccount.getAbsoluteLimit()))
        .andExpect(
            jsonPath("$[0].creationDate").value(employeeAccount.getCreationDate().toString()))
        .andExpect(
            jsonPath("$[0].accountHolder.userId").value(employeeAccount.getCustomer().getId()))
        .andExpect(jsonPath("$[0].accountHolder.firstName").value(
            employeeAccount.getCustomer().getFirstName()))
        .andExpect(
            jsonPath("$[0].accountHolder.lastName").value(
                employeeAccount.getCustomer().getLastName()))
        .andExpect(jsonPath("$[0].accountHolder.transactionLimit").value(
            employeeAccount.getCustomer().getTransactionLimit()))
        .andExpect(
            jsonPath("$[0].accountHolder.dayLimit").value(
                employeeAccount.getCustomer().getDayLimit()))
        .andExpect(jsonPath("$[0].active").value(employeeAccount.isActive()));
  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE"})
  void whenEmployeeAuthIsProvidedGetAccountByIbanEndPointOKWithAccountJSON() throws Exception {
    when(accountService.getAccountByIBAN(employeeAccount.getIban())).thenReturn(employeeAccount);

    this.mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts/" + employeeAccount.getIban()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.iban").value(employeeAccount.getIban()))
        .andExpect(jsonPath("$.accountBalance").value(employeeAccount.getBalance()))
        .andExpect(jsonPath("$.accountType").value(employeeAccount.getAccountType().name()))
        .andExpect(jsonPath("$.absoluteLimit").value(employeeAccount.getAbsoluteLimit()))
        .andExpect(jsonPath("$.creationDate").value(employeeAccount.getCreationDate().toString()))
        .andExpect(jsonPath("$.accountHolder.userId").value(employeeAccount.getCustomer().getId()))
        .andExpect(jsonPath("$.accountHolder.firstName").value(
            employeeAccount.getCustomer().getFirstName()))
        .andExpect(
            jsonPath("$.accountHolder.lastName").value(employeeAccount.getCustomer().getLastName()))
        .andExpect(jsonPath("$.accountHolder.transactionLimit").value(
            employeeAccount.getCustomer().getTransactionLimit()))
        .andExpect(
            jsonPath("$.accountHolder.dayLimit").value(employeeAccount.getCustomer().getDayLimit()))
        .andExpect(jsonPath("$.active").value(employeeAccount.isActive()));

  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE"})
  void whenEmployeeJWtIsProvidedGETAccountsWithInvalidAccountTypeFilterReturnsBadRequest()
      throws Exception {
    this.mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts")
                .param("limit", "1")
                .param("offset", "0")
                .param("accountType", "INVALID"))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("The account type is not valid"));
  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE"})
  void whenEmployeeOrCustomerAuthIsProvidedToGETOneEndpointIfIbanDoesntExistItReturnsNotFound()
      throws Exception {
    when(accountService.getAccountByIBAN(employeeAccount.getIban())).
        thenThrow(new EntityNotFoundException("Account not found"));

    this.mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts/" + employeeAccount.getIban()))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Account not found"));
  }

  @Test
  @WithMockUser(roles = {"CUSTOMER"}, username = CUSTOMER_EMAIL)
  void whenCustomerAuthIsProvidedToGETOneAccountWhichJWTUserIsNotOwnerOfItReturnsForbidden()
      throws Exception {
    when(accountService.getAccountByIBAN(employeeAccount.getIban())).thenReturn(employeeAccount);
    when(
        accountService.isAccountOwnedByCustomer(employeeAccount.getIban(), customerUser.getEmail()))
        .thenReturn(false);

    this.mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts/" + employeeAccount.getIban()))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.message").value("You are not allowed to access this account!"));
  }

  @Test
  @WithMockUser(roles = {"CUSTOMER"}, username = CUSTOMER_EMAIL)
  void whenCustomerAuthIsProvidedToGETOneAccountWhichJWTUserIsOwnerOfItReturnsOK()
      throws Exception {
    when(accountService.getAccountByIBAN(employeeAccount.getIban())).thenReturn(employeeAccount);
    when(
        accountService.isAccountOwnedByCustomer(employeeAccount.getIban(), customerUser.getEmail()))
        .thenReturn(true);

    this.mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts/" + employeeAccount.getIban()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.iban").value(employeeAccount.getIban()))
        .andExpect(jsonPath("$.accountBalance").value(employeeAccount.getBalance()))
        .andExpect(jsonPath("$.accountType").value(employeeAccount.getAccountType().name()))
        .andExpect(jsonPath("$.absoluteLimit").value(employeeAccount.getAbsoluteLimit()))
        .andExpect(jsonPath("$.creationDate").value(employeeAccount.getCreationDate().toString()))
        .andExpect(jsonPath("$.accountHolder.userId").value(employeeAccount.getCustomer().getId()))
        .andExpect(jsonPath("$.accountHolder.firstName").value(
            employeeAccount.getCustomer().getFirstName()))
        .andExpect(
            jsonPath("$.accountHolder.lastName").value(employeeAccount.getCustomer().getLastName()))
        .andExpect(jsonPath("$.accountHolder.transactionLimit").value(
            employeeAccount.getCustomer().getTransactionLimit()))
        .andExpect(
            jsonPath("$.accountHolder.dayLimit").value(employeeAccount.getCustomer().getDayLimit()))
        .andExpect(jsonPath("$.active").value(employeeAccount.isActive()));
  }

  @Test
  @WithMockUser(roles = {"CUSTOMER"}, username = CUSTOMER_EMAIL)
  void whenCustomerOrEmployeeHaveValidJWTAndTryToAccessGETOneWithInvalidIBanPatternReturnsBadRequest()
      throws Exception {
    when(accountService.getAccountByIBAN(employeeAccount.getIban())).thenReturn(employeeAccount);
    when(
        accountService.isAccountOwnedByCustomer(employeeAccount.getIban(), customerUser.getEmail()))
        .thenReturn(true);

    this.mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts/" + "invalidIBAN"))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Not a valid Iban for Inholland bank"));
  }

  @Test
  @WithMockUser(roles = {"CUSTOMER"}, username = CUSTOMER_EMAIL)
  void whenCustomerHaveValidJWTAndTryToAccessPOSTAccountStatusWillGetForbidden() throws Exception {
    this.mockMvc.perform(
            MockMvcRequestBuilders.post("/accounts/" + "accountStatus/" + employeeAccount.getIban()))
        .andDo(print())
        .andExpect(status().isForbidden());

  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE"})
  void whenEmployeeHaveValidJWTAndTryToAccessPOSTAccountStatusWillGetNoContent() throws Exception {
    when(accountService.getAccountByIBAN(employeeAccount.getIban())).thenReturn(employeeAccount);
    AccountStatusDTO accountStatusDTO = new AccountStatusDTO(true);
    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post("/accounts/accountStatus/" + employeeAccount.getIban())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountStatusDTO))
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isNoContent());

  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE"})
  void whenEmployeeHaveValidJWTAndTryToAccessPOSTAccountStatusWithInvalidIBANWillGetBadRequest()
      throws Exception {
    when(accountService.getAccountByIBAN(employeeAccount.getIban())).thenReturn(employeeAccount);
    AccountStatusDTO accountStatusDTO = new AccountStatusDTO(true);
    this.mockMvc.perform(
            MockMvcRequestBuilders.post("/accounts/accountStatus/" + "invalidIBAN")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountStatusDTO))
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Not a valid Iban for Inholland bank"));

  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE"}, username = EMPLOYEE_EMAIL)
  void whenEmployeeHaveValidJWTAndTryToAccessPOSTAccountStatusWithNonExistingAccountReturnsNotFound()
      throws Exception {
    doThrow(new EntityNotFoundException("Account not found"))
        .when(accountService).changeAccountStatus(employeeAccount.getIban(), true);
    this.mockMvc.perform(
            MockMvcRequestBuilders.post("/accounts/accountStatus/" + employeeAccount.getIban())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatingAccountDTO))
                .with(csrf())
        )
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Account not found"));
  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE"})
  void whenEmployeeHaveValidJWTAndTryToAccessGETAccountsByEmailWithInvalidEmailReturnsBadRequest()
      throws Exception {
    when(accountService.getAccountsByEmailAddress(
        employeeAccount.getCustomer().getEmail())).thenReturn(List.of(employeeAccount));
    this.mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts/user/" + "invalidEmail")
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("The email provided is not a valid email address"));
  }

  @Test
  @WithMockUser(roles = {"CUSTOMER"})
  void whenCustomerHaveValidJWTAndTryToAccessGETAccountsByEmailWhichIsDifferentEmailThanJWTEmailReturnsForbidden()
      throws Exception {
    when(accountService.getAccountsByEmailAddress(
        employeeAccount.getCustomer().getEmail())).thenReturn(List.of(employeeAccount));
    when(accountService.isAccountOwnedByCustomer(employeeAccount.getIban(),
        customerUser.getEmail())).thenReturn(false);
    this.mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts/user/" + employeeAccount.getCustomer().getEmail()))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(
            jsonPath("$.message").value("You are not allowed to access others Accounts Details!"));
  }

  @Test
  @WithMockUser(roles = {"CUSTOMER"}, username = CUSTOMER_EMAIL)
  void whenCustomerHaveAValidJWTAndTryToAccessGETAccountsByEmailSameWithJWTEmailReturnsOkWithAccount()
      throws Exception {
    when(accountService.getAccountsByEmailAddress(CUSTOMER_EMAIL)).thenReturn(
        List.of(employeeAccount));
    when(accountService.isAccountOwnedByCustomer(employeeAccount.getIban(),
        CUSTOMER_EMAIL)).thenReturn(true);
    when(userService.getUserByEmail(CUSTOMER_EMAIL)).thenReturn(employeeUser);
    when(accountService.getTotalTransactedAmountOfTodayByUserEmail(
        employeeUser.getEmail())).thenReturn(50.00);

    this.mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts/user/" + CUSTOMER_EMAIL))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$.accounts[0].iban").value(employeeAccount.getIban()),
            jsonPath("$.accounts[0].accountBalance").value(employeeAccount.getBalance()),
            jsonPath("$.accounts[0].accountType").value(employeeAccount.getAccountType().name()),
            jsonPath("$.accounts[0].absoluteLimit").value(employeeAccount.getAbsoluteLimit()),
            jsonPath("$.accounts[0].creationDate").value(
                employeeAccount.getCreationDate().toString()),
            jsonPath("$.accountHolder.userId").value(employeeAccount.getCustomer().getId()),
            jsonPath("$.accountHolder.firstName").value(
                employeeAccount.getCustomer().getFirstName()),
            jsonPath("$.accountHolder.lastName").value(employeeAccount.getCustomer().getLastName()),
            jsonPath("$.accountHolder.transactionLimit").value(
                employeeAccount.getCustomer().getTransactionLimit()),
            jsonPath("$.accountHolder.dayLimit").value(employeeAccount.getCustomer().getDayLimit()),
            jsonPath("$.totalTransactedAmountToday").value(50.00),
            jsonPath("$.totalBalance").value(1000.0)
        );
  }

  @Test
  @WithAnonymousUser
  void whenAnonymousUserTryToAccessGETAccountsByEmailReturnsUnauthorized() throws Exception {
    when(accountService.getAccountsByEmailAddress(
        employeeAccount.getCustomer().getEmail())).thenReturn(List.of(employeeAccount));
    this.mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts/user/" + employeeAccount.getCustomer().getEmail()))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE", "CUSTOMER"})
  void whenUserHaveValidJWTAndTryToAccessGETSearchingAccountsByCustomerNameReturnsList()
      throws Exception {
    when(accountService.searchAccountByCustomerName(employeeUser.getFirstName(), 2, 0))
        .thenReturn(List.of(employeeAccount, customerAccount));
    this.mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts/searchByCustomerName")
                .param("limit", "2")
                .param("offset", "0")
                .param("customerName", employeeUser.getFirstName()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(2))
        .andExpectAll(
            jsonPath("$[0].accountHolder").value(employeeAccount.getCustomer().getFirstName()
                + " " + employeeAccount.getCustomer().getLastName()),
            jsonPath("$[0].iban").value(employeeAccount.getIban()),
            jsonPath("$[1].accountHolder").value(customerAccount.getCustomer().getFirstName()
                + " " + customerAccount.getCustomer().getLastName()),
            jsonPath("$[1].iban").value(customerAccount.getIban())
        );
  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE", "CUSTOMER"})
  void whenUserHaveValidJWTAndTryToAccessGETSearchingAccountsByCustomerNameReturnsNotFound()
      throws Exception {
    String searchingName = employeeUser.getFirstName();
    when(accountService.searchAccountByCustomerName(searchingName, 2, 0))
        .thenReturn(List.of());
    this.mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts/searchByCustomerName")
                .param("limit", "2")
                .param("offset", "0")
                .param("customerName", searchingName))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(
            jsonPath("$.message").value("No accounts found by this name " + searchingName + "!"));
  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE", "CUSTOMER"})
  void whenUserHaveValidJWTAndTryToAccessGETSearchingAccountsByBlankCustomerNameReturnsBadRequest()
      throws Exception {
    String searchingName = "";
    this.mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts/searchByCustomerName")
                .param("limit", "0")
                .param("offset", "0")
                .param("customerName", searchingName))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Customer name cannot be empty in order to search"));
  }

  @Test
  @WithAnonymousUser
  void whenAnonymousUserTryToAccessGETSearchingAccountsByCustomerNameReturnsUnauthorized()
      throws Exception {
    String searchingName = employeeUser.getFirstName();
    when(accountService.searchAccountByCustomerName(searchingName, 2, 0))
        .thenReturn(List.of());
    this.mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts/searchByCustomerName")
                .param("limit", "2")
                .param("offset", "0")
                .param("customerName", searchingName))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithAnonymousUser
  void whenAnonymousUserTryToAccessPOSTAccountsReturnsUnauthorized() throws Exception {
    this.mockMvc.perform(
            MockMvcRequestBuilders.post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
        )
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(roles = {"CUSTOMER"})
  void whenCustomerTryToAccessPOSTAccountsReturnsForbidden() throws Exception {
    this.mockMvc.perform(
            MockMvcRequestBuilders.post("/accounts")
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE"}, username = EMPLOYEE_EMAIL)
  void whenCustomerTryToAccessPOSTAccountsReturnsCreatedWithNewAccount()
      throws Exception {
    when(accountService.createAccountWithLimitCheck(creatingAccountDTO)).thenReturn(
        customerAccount);
    when(userService.getUserByEmail(customerUser.getEmail())).thenReturn(employeeUser);
    this.mockMvc.perform(
            MockMvcRequestBuilders.post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(creatingAccountDTO))
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpectAll(
            jsonPath("$.iban").value(customerAccount.getIban()),
            jsonPath("$.accountBalance").value(customerAccount.getBalance()),
            jsonPath("$.accountType").value(customerAccount.getAccountType().name()),
            jsonPath("$.absoluteLimit").value(customerAccount.getAbsoluteLimit()),
            jsonPath("$.accountHolder.dayLimit").value(customerAccount.getCustomer().getDayLimit()),
            jsonPath("$.accountHolder.transactionLimit").value(
                customerAccount.getCustomer().getTransactionLimit()),
            jsonPath("$.accountHolder.userId").value(customerAccount.getCustomer().getId()),
            jsonPath("$.active").value(customerAccount.isActive())
        );

  }

  @Test
  @WithMockUser(roles = {"CUSTOMER"}, username = CUSTOMER_EMAIL)
  void whenCustomerTryToAccessPUTAccountsReturnsForbidden() throws Exception {
    this.mockMvc.perform(
            MockMvcRequestBuilders.put("/accounts")
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE"}, username = EMPLOYEE_EMAIL)
  void whenEmployeeTryToAccessPUTAccountsWithNonExistingIBANReturnsNotFound() throws Exception {
    when(accountService.updateAccountDetails(customerAccount.getIban(), updatingAccountDTO))
        .thenThrow(new EntityNotFoundException("Account not found"));
    when(userService.getUserByEmail(customerUser.getEmail())).thenReturn(employeeUser);
    this.mockMvc.perform(
            MockMvcRequestBuilders.put("/accounts/" + customerAccount.getIban())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatingAccountDTO))
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Account not found"));
  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE"}, username = EMPLOYEE_EMAIL)
  void whenEmployeeTryToAccessPUTAccountsWithExistingIBANReturnsUpdatedAccount() throws Exception {
    when(accountService.updateAccountDetails(customerAccount.getIban(), updatingAccountDTO))
        .thenReturn(customerAccount);
    when(userService.getUserByEmail(customerUser.getEmail())).thenReturn(employeeUser);
    this.mockMvc.perform(
            MockMvcRequestBuilders.put("/accounts/" + customerAccount.getIban()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatingAccountDTO))
                .with(csrf())
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$.iban").value(customerAccount.getIban()),
            jsonPath("$.accountBalance").value(customerAccount.getBalance()),
            jsonPath("$.accountType").value(customerAccount.getAccountType().name()),
            jsonPath("$.absoluteLimit").value(customerAccount.getAbsoluteLimit()),
            jsonPath("$.accountHolder.dayLimit").value(customerAccount.getCustomer().getDayLimit()),
            jsonPath("$.accountHolder.transactionLimit").value(
                customerAccount.getCustomer().getTransactionLimit()),
            jsonPath("$.accountHolder.userId").value(customerAccount.getCustomer().getId()),
            jsonPath("$.active").value(customerAccount.isActive()
            ));
  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE"}, username = EMPLOYEE_EMAIL)
  void whenEmployeeTryToAccessPOSTNewAccountsWithNegativeDayLimitReturnBadRequest()
      throws Exception {
    this.mockMvc.perform(
            MockMvcRequestBuilders.post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreatingAccountDTO(-50.0, 80.2,
                    AccountType.CURRENT.name(), employeeUser.getId())))
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Day Limit cannot be negative"));
  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE"}, username = EMPLOYEE_EMAIL)
  void whenEmployeeTryToAccessPOSTNewAccountsWithNegativeTransactionLimitReturnBadRequest()
      throws Exception {
    this.mockMvc.perform(
            MockMvcRequestBuilders.post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreatingAccountDTO(50.0, -80.2,
                    AccountType.CURRENT.name(), employeeUser.getId())))
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Transaction Limit cannot be negative"));
  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE"}, username = EMPLOYEE_EMAIL)
  void whenEmployeeTryToAccessPOSTAccountWithoutDayLimitReturnsBadRequest()
      throws Exception {

    this.mockMvc.perform(
            MockMvcRequestBuilders.post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreatingAccountDTO(null, 50.0,
                    AccountType.CURRENT.name(), employeeUser.getId())))
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Day Limit cannot be left empty"));
  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE"}, username = EMPLOYEE_EMAIL)
  void whenEmployeeTryToAccessPOSTAccountWithoutTransactionLimitReturnsBadRequest()
      throws Exception {

    this.mockMvc.perform(
            MockMvcRequestBuilders.post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreatingAccountDTO(50.0, null,
                    AccountType.CURRENT.name(), employeeUser.getId())))
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Transaction Limit cannot be left empty"));
  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE"}, username = EMPLOYEE_EMAIL)
  void whenEmployeeTryToAccessPOSTAccountWithoutValidAccountTypeReturnsBadRequest()
      throws Exception {

    this.mockMvc.perform(
            MockMvcRequestBuilders.post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreatingAccountDTO(50.0, 50.0,
                    "INVALID", employeeUser.getId())))
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("The account type is not valid"));
  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE"}, username = EMPLOYEE_EMAIL)
  void whenEmployeeTrisToAccessPOSTAccountWithoutUserIdReturnsBadRequest() throws Exception {
    this.mockMvc.perform(
            MockMvcRequestBuilders.post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreatingAccountDTO(50.0, 50.0,
                    AccountType.CURRENT.name(), null)))
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("accountHolderId cannot be left empty"));
  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE"}, username = EMPLOYEE_EMAIL)
  void whenEmployeeTriesToPUTAccountWithEmptyAbsoluteLimitReturnsBadRequest() throws Exception {
    this.mockMvc.perform(
            MockMvcRequestBuilders.put("/accounts/" + customerAccount.getIban())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    new UpdatingAccountDTO(null, customerAccount.isActive(), accountHolderDTO)))
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Absolute Limit cannot be Null"));
  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE"}, username = EMPLOYEE_EMAIL)
  void whenEmployeeTriesToPUTAccountWithoutIsActiveReturnsBadRequest() throws Exception {
    this.mockMvc.perform(
            MockMvcRequestBuilders.put("/accounts/" + customerAccount.getIban())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    new UpdatingAccountDTO(50.0, null, accountHolderDTO)))
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("The active field cannot be left empty"));
  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE"}, username = EMPLOYEE_EMAIL)
  void whenEmployeeTriesToPUTAccountWithoutAccountHolderReturnsBadRequest() throws Exception {
    this.mockMvc.perform(
            MockMvcRequestBuilders.put("/accounts/" + customerAccount.getIban())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    new UpdatingAccountDTO(50.0, customerAccount.isActive(), null)))
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("The AccountHolder Details cannot be empty"));
  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE"}, username = EMPLOYEE_EMAIL)
  void whenEmployeeTriesToPUTAccountWithEmptyAccountHolderUserIdReturnsBadRequest()
      throws Exception {
    this.mockMvc.perform(
            MockMvcRequestBuilders.put("/accounts/" + customerAccount.getIban())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    new UpdatingAccountDTO(50.0, customerAccount.isActive(),
                        new AccountHolderDTO(null, 20.0, 30.00, customerUser.getFirstName(),
                            customerUser.getLastName()))))
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("User Id cannot be left empty"));
  }

  @Test
  @WithMockUser(roles = {"EMPLOYEE"}, username = EMPLOYEE_EMAIL)
  void whenEmployeesTriesToPUTAccountWithNegativeDayLimitReturnBadRequest()
      throws Exception {

    this.mockMvc.perform(
            MockMvcRequestBuilders.put("/accounts/" + customerAccount.getIban())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    new UpdatingAccountDTO(-50.0, customerAccount.isActive(),
                        new AccountHolderDTO(customerUser.getId(), -20.0, 30.00,
                            customerUser.getFirstName(), customerUser.getLastName()))))
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("The day limit cannot be Negative"));
  }
  @Test
  @WithMockUser(roles = {"EMPLOYEE"}, username = EMPLOYEE_EMAIL)
  void whenEmployeesTriesToPUTAccountWithNegativeTransactionLimitReturnBadRequest()
      throws Exception {

    this.mockMvc.perform(
            MockMvcRequestBuilders.put("/accounts/" + customerAccount.getIban())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    new UpdatingAccountDTO(50.0, customerAccount.isActive(),
                        new AccountHolderDTO(customerUser.getId(), 20.0, -30.00,
                            customerUser.getFirstName(), customerUser.getLastName()))))
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("The transaction limit cannot be Negative"));
  }
  @Test
  @WithMockUser(roles = {"EMPLOYEE"}, username = EMPLOYEE_EMAIL)
  void whenEmployeesTriesToPUTAccountWithoutFirstNameReturnBadRequest()
      throws Exception {

    this.mockMvc.perform(
            MockMvcRequestBuilders.put("/accounts/" + customerAccount.getIban())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    new UpdatingAccountDTO(-50.0, customerAccount.isActive(),
                        new AccountHolderDTO(customerUser.getId(), 20.0, 30.00,
                            null, customerUser.getLastName()))))
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("First Name cannot be left empty"));
  }
  @Test
  @WithMockUser(roles = {"EMPLOYEE"}, username = EMPLOYEE_EMAIL)
  void whenEmployeesTriesToPUTAccountWithoutLastNameReturnBadRequest()
      throws Exception {

    this.mockMvc.perform(
            MockMvcRequestBuilders.put("/accounts/" + customerAccount.getIban())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    new UpdatingAccountDTO(-50.0, customerAccount.isActive(),
                        new AccountHolderDTO(customerUser.getId(), 20.0, 30.00,
                            customerUser.getFirstName(), null))))
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Last Name cannot be left empty"));
  }
  @Test
  @WithMockUser(roles = {"EMPLOYEE"}, username = EMPLOYEE_EMAIL)
  void whenEmployeeTriesToPOSTAccountForUserIfLimitExceedReturnsConflict() throws Exception {
    when(accountService.createAccountWithLimitCheck( new CreatingAccountDTO(50.0,40.00, "Savings", 1L))).
        thenThrow(new LimitExceededException("The user has reached the maximum number of accounts"));
    this.mockMvc.perform(
            MockMvcRequestBuilders.post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    new CreatingAccountDTO(50.0,40.00, "Savings", 1L)))
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("The user has reached the maximum number of accounts"));
  }


}



