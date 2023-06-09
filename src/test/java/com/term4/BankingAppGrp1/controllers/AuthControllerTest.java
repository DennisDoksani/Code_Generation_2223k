package com.term4.BankingAppGrp1.controllers;

import com.term4.BankingAppGrp1.configuration.ApiTestConfiguration;
import com.term4.BankingAppGrp1.requestDTOs.LoginDTO;
import com.term4.BankingAppGrp1.responseDTOs.LoginResponseDTO;
import com.term4.BankingAppGrp1.services.AuthService;
import io.cucumber.java.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.naming.AuthenticationException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@Import(ApiTestConfiguration.class)
public class AuthControllerTest {

    //@Autowired
    //private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;
    private LoginDTO loginDto;
    private LoginResponseDTO loginResponseDto;

    @BeforeEach
    public void setUp() {
        //mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        loginDto = new LoginDTO("email@email.com", "password");
        loginResponseDto = new LoginResponseDTO("token", 1, "email@email.com", "name");
    }

    @Test
    void login() throws Exception {
        when(authService.login(loginDto.email(), loginDto.password()))
                .thenReturn(loginResponseDto);

        mockMvc.perform(post("/auth/login")
                .contentType("application/json")
                .content("{\"email\":\"" + loginDto.email() + "\",\"password\":\"" + loginDto.password() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "\"jwt\":\"" + loginResponseDto.jwt()
                        + "\",\"id\":" + loginResponseDto.id()
                        + ",\"email\":\"" + loginResponseDto.email()
                        + "\",\"name\":\"" + loginResponseDto.name() + "\"" +
                        "}"
                ));
    }
}
