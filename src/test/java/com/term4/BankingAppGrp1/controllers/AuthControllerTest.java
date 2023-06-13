package com.term4.BankingAppGrp1.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.term4.BankingAppGrp1.configuration.ApiTestConfiguration;
import com.term4.BankingAppGrp1.requestDTOs.LoginDTO;
import com.term4.BankingAppGrp1.responseDTOs.LoginResponseDTO;
import com.term4.BankingAppGrp1.services.AuthService;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectWriter;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.SerializationFeature;
import javax.naming.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@Import(ApiTestConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
@EnableMethodSecurity
public class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthService authService;

  private LoginDTO validLoginDto;
  private LoginDTO wrongPasswordLoginDto;
  private LoginDTO wrongEmailLoginDto;
  private LoginResponseDTO loginResponseDto;
  private ObjectWriter ow;

  @BeforeEach
  public void setUp() throws Exception {
    validLoginDto = new LoginDTO("email@email.com", "password");
    wrongPasswordLoginDto = new LoginDTO("email@email.com", "wrongPassword");
    wrongEmailLoginDto = new LoginDTO("wrong@email.com", "password");

    loginResponseDto = new LoginResponseDTO("token", 1, "email@email.com", "name");

    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ow = mapper.writer().withDefaultPrettyPrinter();
  }

  @Test
  void loginWithCorrectLoginDetailsReturnsOKAndLoginResponseToken() throws Exception {
    when(authService.login(validLoginDto.email(), validLoginDto.password()))
        .thenReturn(loginResponseDto);

    String validRequest = ow.writeValueAsString(validLoginDto);
    String validResponse = ow.writeValueAsString(loginResponseDto);

    mockMvc.perform(post("/auth/login")
            .contentType("application/json")
            .content(validRequest))
        .andExpect(status().isOk())
        .andExpect(content().json(validResponse));
  }

  @Test
  void loginWithWrongPasswordReturnsUnauthorised() throws Exception {
    when(authService.login(wrongPasswordLoginDto.email(), wrongPasswordLoginDto.password()))
        .thenThrow(new AuthenticationException("Invalid username/password"));

    String wrongPasswordRequest = ow.writeValueAsString(wrongPasswordLoginDto);

    mockMvc.perform(post("/auth/login")
            .contentType("application/json")
            .content(wrongPasswordRequest))
        .andDo(print())
        .andExpect(status().isUnauthorized())
        .andExpect(content().json("{" +
            "\"message\":\"Invalid username/password\"" +
            "}"));
  }

  @Test
  void loginWithWrongEmailReturnsUnauthorised() throws Exception {
    when(authService.login(wrongEmailLoginDto.email(), wrongEmailLoginDto.password()))
        .thenThrow(new AuthenticationException("Invalid username/password"));

    String wrongEmailRequest = ow.writeValueAsString(wrongEmailLoginDto);
    String loginResponseToken = ow.writeValueAsString(loginResponseDto);

    mockMvc.perform(post("/auth/login")
            .contentType("application/json")
            .content(wrongEmailRequest))
        .andExpect(status().isUnauthorized())
        .andExpect(content().json("{" +
            "\"message\":\"Invalid username/password\"" +
            "}"));
  }
}
