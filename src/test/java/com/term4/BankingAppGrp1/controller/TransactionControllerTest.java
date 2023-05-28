package com.term4.BankingAppGrp1.controller;

import com.term4.BankingAppGrp1.configuration.ApiTestConfiguration;
import com.term4.BankingAppGrp1.controllers.TransactionController;
import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.models.Transaction;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.services.AccountService;
import com.term4.BankingAppGrp1.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TransactionController.class)
@Import(ApiTestConfiguration.class)
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private Transaction testTransaction1;
    private Account testAccount1;
    private Account testAccount2;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private AccountService accountService;

    @BeforeEach
    void Init() {
        testTransaction1 = new Transaction(10.0, "NL01INHO0000000003", "NL01INHO0000000002",
                LocalDate.now(), LocalTime.now(), 1);

        testAccount1 = new Account("NL01INHO0000000003", 100, LocalDate.now(), 0, true, AccountType.SAVINGS,
                new User(11111, "Ruben", "Walkeuter", LocalDate.of(2003, 10, 1), "TestNumber", "test@test.com", "password" ));
        testAccount2 = new Account("NL01INHO0000000002", 100, LocalDate.now(), 0, true, AccountType.CURRENT,
                new User(11111, "Ruben", "Walkeuter", LocalDate.of(2003, 10, 1), "TestNumber", "test@test.com", "password" ));

    }

    @Test
    @WithMockUser()
    void getTransactionsWithoutSpecifyingFiltersShouldReturnAListOfThree() throws Exception {
        when(transactionService.getTransactionsWithFilters(PageRequest.of(0 / 50, 50), null, null, null, null, null, null))
                .thenReturn(List.of(testTransaction1));

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/transactions"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }


}
