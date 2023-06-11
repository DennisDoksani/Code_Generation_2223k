package com.term4.BankingAppGrp1.cucumber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.term4.BankingAppGrp1.requestDTOs.LoginDTO;
import com.term4.BankingAppGrp1.responseDTOs.LoginResponseDTO;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class BaseStepDefinition {
  @Autowired
  protected TestRestTemplate restTemplate;

  protected ResponseEntity<String> response;
  @Autowired
  protected ObjectMapper objectMapper;
  protected HttpHeaders httpHeaders = new HttpHeaders();
  protected final String accountEndpoint = "/accounts";
  protected final String EMPLOYEE_EMAIL = "employeecustomer@seed.com";
  protected final String LOGIN_PASSWORD = "password";
  protected final String CUSTOMER_EMAIL = "customer@seed.com";

  protected String getToken(LoginDTO loginDTO) throws JsonProcessingException {
    response = restTemplate
        .exchange("/auth/login",
            HttpMethod.POST,
            new HttpEntity<>(objectMapper.writeValueAsString(loginDTO), httpHeaders), String.class);
    LoginResponseDTO tokenDTO = objectMapper.readValue(response.getBody(), LoginResponseDTO.class);
    return tokenDTO.jwt();
  }


}
