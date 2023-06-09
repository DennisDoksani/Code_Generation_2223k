package com.term4.BankingAppGrp1.controllers;

import com.term4.BankingAppGrp1.configuration.ApiTestConfiguration;
import com.term4.BankingAppGrp1.configuration.WebSecurityConfiguration;
import com.term4.BankingAppGrp1.jwtFilter.JwtTokenFilter;
import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.models.Role;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.services.AccountService;
import com.term4.BankingAppGrp1.services.UserService;
import com.term4.BankingAppGrp1.util.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({SpringExtension.class})
@WebMvcTest(AccountController.class)
@Import(ApiTestConfiguration.class)
 class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountService accountService;
    @MockBean
    private UserService userService;

    private Account testingAccount;
    private User testingUser;

    @BeforeEach
    void init() {
        testingUser = User.builder()
                .id(1)
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

        testingAccount = Account.builder()
                .iban("NL72INHO0579629781")
                .balance(900.0)
                .creationDate(LocalDate.now())
                .accountType(AccountType.CURRENT)
                .customer(testingUser)
                .build();

    }
    @Test
    @WithMockUser(username = "admin",  roles = {"CUSTOMER"})
    void whenJwtTokenISNotProvidedAndAccessGetAllEndpointsGivesUnauthorized() throws Exception {
        when(accountService.getAllAccounts(
                1,
                0,
                null
        )).thenReturn(List.of(testingAccount));

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/accounts")
                .param("limit", "1")
                .param("offset", "0")).andDo(print())
                .andExpect(status().isOk())
                ;
    }
  @Test
  @WithMockUser(username = "admin", roles = {"CUSTOMER"})
  void whenCustomerTriesToAccessTheEndpointITShouldGiveForbidden() throws Exception {
    when(accountService.getAllAccounts(
        1,
        0,
        null
    )).thenReturn(List.of(testingAccount));

    this.mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts")
                .param("limit", "1")
                .param("offset", "0")).andDo(print())
        .andExpect(status().isForbidden());
  }

}
