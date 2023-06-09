package com.term4.BankingAppGrp1.controllers;

import com.term4.BankingAppGrp1.configuration.ApiTestConfiguration;
import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.models.Role;
import com.term4.BankingAppGrp1.models.Transaction;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.requestDTOs.TransactionDTO;
import com.term4.BankingAppGrp1.responseDTOs.TransactionAccountDTO;
import com.term4.BankingAppGrp1.responseDTOs.TransactionResponseDTO;
import com.term4.BankingAppGrp1.services.AccountService;
import com.term4.BankingAppGrp1.services.TransactionService;

import com.term4.BankingAppGrp1.services.UserService;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectWriter;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TransactionController.class)
@Import(ApiTestConfiguration.class)
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private Transaction testTransaction1;
    private TransactionResponseDTO responseDTO;
    private Account testAccount1;
    private Account testAccount2;
    private User testUser;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private AccountService accountService;

    @MockBean
    private UserService userService;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @BeforeEach
    void Init() {
        testUser = User.builder()
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

        testAccount1 = new Account("NL01INHO0000000003", 100, LocalDate.now(), 0, true, AccountType.SAVINGS, testUser);
        testAccount2 = new Account("NL01INHO0000000002", 100, LocalDate.now(), 0, true, AccountType.CURRENT, testUser);

        testTransaction1 = new Transaction(10.0, testAccount1, testAccount2,
                LocalDate.now(), LocalTime.now(), testUser);

        responseDTO = new TransactionResponseDTO(
                testTransaction1.getTransactionID(),
                testTransaction1.getAmount(),
                new TransactionAccountDTO(testAccount2.getIban(), testAccount2.getAccountType(), testUser.getFullName()),
                new TransactionAccountDTO(testAccount1.getIban(), testAccount1.getAccountType(), testUser.getFullName()),
                testTransaction1.getDate(),
                testTransaction1.getTimestamp(),
                testUser.getFullName());

    }

    @Test
    @WithMockUser()
    void getTransactionsWithoutSpecifyingFiltersShouldReturnAListOfOne() throws Exception {
        when(transactionService.getTransactionsWithFilters(PageRequest.of(0 / 50, 50), null, null, null, null, null, null))
                .thenReturn(List.of(responseDTO));

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/transactions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser()
    void tryingToMakeATransactionWhenTheDayLimitHasBeenReachedShouldResultInIllegalArgumentException() throws Exception {
        when(transactionService.getSumOfMoneyTransferred("NL01INHO0000000002", LocalDate.now()))
                .thenReturn(100.0);

        TransactionDTO dto = new TransactionDTO(100.0, testAccount1.getIban(), testAccount2.getIban());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(dto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/transactions")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson)
        ) .andExpect(status().isForbidden());
    }
}
