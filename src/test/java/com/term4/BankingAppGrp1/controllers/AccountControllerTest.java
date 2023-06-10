package com.term4.BankingAppGrp1.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.term4.BankingAppGrp1.configuration.ApiTestConfiguration;
import com.term4.BankingAppGrp1.requestDTOs.AccountStatusDTO;
import com.term4.BankingAppGrp1.services.AccountService;
import com.term4.BankingAppGrp1.services.UserService;
import com.term4.BankingAppGrp1.testingData.BankingAppTestData;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
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
  


  @BeforeEach
  void init() {
    super.setupData();

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
                .param("offset", "0")).andDo(print())
        .andExpect(status().isUnauthorized())
    ;
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
    when(accountService.getAccountByIBAN(employeeAccount.getIban())).thenReturn(employeeAccount);
    this.mockMvc.perform(
            MockMvcRequestBuilders.post("/accounts/" + "accountStatus/"+employeeAccount.getIban() ))
        .andDo(print())
        .andExpect(status().isForbidden());

  }
  @Test //TODO check this it is giving 403 instead of 204
  @WithMockUser(roles = {"EMPLOYEE"})
  void whenEmployeeHaveValidJWTAndTryToAccessPOSTAccountStatusWillGetNoContent() throws Exception {
    when(accountService.getAccountByIBAN(employeeAccount.getIban())).thenReturn(employeeAccount);
    AccountStatusDTO accountStatusDTO = new AccountStatusDTO(true);
    this.mockMvc.perform(
            MockMvcRequestBuilders.post("/accounts/accountStatus/" +employeeAccount.getIban() )
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountStatusDTO)))
        .andDo(print())
        .andExpect(status().isNoContent());

  }
  @Test
  @WithMockUser(roles = {"EMPLOYEE"})
  void whenEmployeeHaveValidJWTAndTryToAccessPOSTAccountStatusWithInvalidIBANWillGetBadRequest() throws Exception {
    when(accountService.getAccountByIBAN(employeeAccount.getIban())).thenReturn(employeeAccount);
    AccountStatusDTO accountStatusDTO = new AccountStatusDTO(true);
    this.mockMvc.perform(
            MockMvcRequestBuilders.post("/accounts/accountStatus/" + "invalidIBAN" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountStatusDTO)))
        .andDo(print())
        .andExpect(status().isBadRequest());

  }
  @Test
  @WithMockUser(roles = {"EMPLOYEE"})
  void whenEmployeeHaveValidJWTAndTryToAccessPOSTAccountStatusWithNonExistingAccountReturnsNotFound() throws Exception {
    when(accountService.getAccountByIBAN(employeeAccount.getIban())).thenThrow(new EntityNotFoundException("Account not found"));
    this.mockMvc.perform(
            MockMvcRequestBuilders.post("/accounts/accountStatus/" + employeeAccount.getIban() )
        )
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Account not found"));
  }
  @Test
  @WithMockUser(roles = {"EMPLOYEE"})
  void whenEmployeeHaveValidJWTAndTryToAccessGETAccountsByEmailWithInvalidEmailReturnsBadRequest() throws Exception {
    when(accountService.getAccountsByEmailAddress(employeeAccount.getCustomer().getEmail())).thenReturn(List.of(employeeAccount));
    this.mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts/user/" + "invalidEmail" )
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("The email provided is not a valid email address"));
  }
  @Test
  @WithMockUser(roles={"CUSTOMER"}, username = CUSTOMER_EMAIL)
  void whenCustomerHaveValidJWTAndTryToAccessGETAccountsByEmailWhichIsDifferentEmailThanJWTEmailReturnsForbidden() throws Exception {
    when(accountService.getAccountsByEmailAddress(employeeAccount.getCustomer().getEmail())).thenReturn(List.of(employeeAccount));
    when(accountService.isAccountOwnedByCustomer(employeeAccount.getIban(), customerUser.getEmail())).thenReturn(false);
    this.mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts/user/" + employeeAccount.getCustomer().getEmail()))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.message").value("You are not allowed to access others Accounts Details!"));
  }
  @Test
  @WithMockUser(roles={"CUSTOMER"}, username = CUSTOMER_EMAIL)
  @WithUserDetails(CUSTOMER_EMAIL)
  void whenCustomerHaveAValidJWTAndTryToAccessGETAccountsByEmailSameWithJWTEmailReturnsOkWithAccount()
      throws Exception {
    when(accountService.getAccountsByEmailAddress(CUSTOMER_EMAIL)).thenReturn(List.of(employeeAccount));
    when(accountService.isAccountOwnedByCustomer(employeeAccount.getIban(), CUSTOMER_EMAIL)).thenReturn(true);
    when(userService.getUserByEmail(CUSTOMER_EMAIL)).thenReturn(employeeUser);
    this.mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts/user/" +CUSTOMER_EMAIL))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].iban").value(employeeAccount.getIban()))
        .andExpect(jsonPath("$[0].accountBalance").value(employeeAccount.getBalance()))
        .andExpect(jsonPath("$[0].accountType").value(employeeAccount.getAccountType().name()))
        .andExpect(jsonPath("$[0].absoluteLimit").value(employeeAccount.getAbsoluteLimit()))
        .andExpect(jsonPath("$[0].creationDate").value(employeeAccount.getCreationDate().toString()))
        .andExpect(jsonPath("$[0].accountHolder.userId").value(employeeAccount.getCustomer().getId()))
        .andExpect(jsonPath("$[0].accountHolder.firstName").value(
            employeeAccount.getCustomer().getFirstName()))
        .andExpect(
            jsonPath("$[0].accountHolder.lastName").value(employeeAccount.getCustomer().getLastName()))
        .andExpect(jsonPath("$[0].accountHolder.transactionLimit").value(
            employeeAccount.getCustomer().getTransactionLimit()))
        .andExpect(
            jsonPath("$[0].accountHolder.dayLimit").value(employeeAccount.getCustomer().getDayLimit()))
        .andExpect(jsonPath("$[0].active").value(employeeAccount.isActive()));

  }




}
