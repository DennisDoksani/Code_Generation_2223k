package com.term4.BankingAppGrp1.cucumber.transactions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.term4.BankingAppGrp1.services.UserService;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Objects;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

public class AccountStepDefinitions extends BaseStepDefinition {

  @Autowired
  private TestRestTemplate restTemplate;
  HttpHeaders httpHeaders = new HttpHeaders();
  private ResponseEntity<String> response;

  public class AccountsStepDefinitions {

    @MockBean
    private UserService userService;

    @SneakyThrows
    @BeforeEach
    void setUp() {

    }

    private ResponseEntity<String> response;

    @Given("The endpoint for {string} is available for method {string}")
    public void theEndpointForIsAvailableForMethod(String endpoint, String method) {

      response = restTemplate.exchange(
          "/" + endpoint,
          HttpMethod.OPTIONS,
          new HttpEntity<>(
              null,
              httpHeaders),
          String.class);
      List<String> options = Arrays.stream(response.getHeaders()
          .get("Allow")
          .get(0)
          .split(",")).toList();

      Assertions.assertTrue(options.contains(method.toUpperCase()));

    }

    @When("I send a GET request to {string}")
    public void iSendGETRequestToAccounts(String endpoint) {
      response = restTemplate.exchange(
          endpoint,
          HttpMethod.GET,
          new HttpEntity<>(null,
              httpHeaders),
          String.class
      );
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
      assertEquals(expectedStatusCode, response.getStatusCodeValue());
    }

    @Then("the response should be an array of objects")
    public void theResponseShouldBeArrayOfObjects() {
      assertTrue(Objects.requireNonNull(response.getBody()).startsWith("["));
      assertTrue(response.getBody().endsWith("]"));
    }

    @Then("the response should be an array of objects containing all accounts")
    public void theResponseShouldContainAllAccounts() {
      // Implement your logic to validate that the response contains all the expected accounts
    }
  }


}
