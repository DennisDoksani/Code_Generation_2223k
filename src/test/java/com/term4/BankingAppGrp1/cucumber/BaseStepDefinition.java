package com.term4.BankingAppGrp1.cucumber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.term4.BankingAppGrp1.requestDTOs.LoginDTO;
import com.term4.BankingAppGrp1.responseDTOs.LoginResponseDTO;
import io.cucumber.spring.CucumberContextConfiguration;
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

  @Autowired
  protected ObjectMapper objectMapper;

  protected ResponseEntity<String> response;
  protected HttpHeaders httpHeaders = new HttpHeaders();
  protected final String accountEndpoint = "/accounts";
  protected final String EMPLOYEE_EMAIL = "employee@seed.com";
  protected final String EMPLOYEE_CUSTOMER_EMAIL = "employeecustomer@seed.com";
  protected final String CUSTOMER_EMAIL = "customer@seed.com";
  protected final String LOGIN_PASSWORD = "password";


  protected String getToken(LoginDTO loginDTO) throws JsonProcessingException {
    response = restTemplate
        .exchange("/auth/login",
            HttpMethod.POST,
            new HttpEntity<>(objectMapper.writeValueAsString(loginDTO), httpHeaders), String.class);
    LoginResponseDTO tokenDTO = objectMapper.readValue(response.getBody(), LoginResponseDTO.class);
    return tokenDTO.jwt();
  }
}
