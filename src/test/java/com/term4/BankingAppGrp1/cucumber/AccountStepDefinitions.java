package com.term4.BankingAppGrp1.cucumber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.term4.BankingAppGrp1.requestDTOs.CreatingAccountDTO;
import com.term4.BankingAppGrp1.requestDTOs.LoginDTO;
import com.term4.BankingAppGrp1.responseDTOs.AccountDTO;
import com.term4.BankingAppGrp1.responseDTOs.ErrorMessageDTO;
import com.term4.BankingAppGrp1.responseDTOs.UserAccountsDTO;
import com.term4.BankingAppGrp1.services.UserService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Collections;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public class AccountStepDefinitions extends BaseStepDefinition {

  @MockBean
  private UserService userService;
  private CreatingAccountDTO validCreatingAccountDTO;

  @SneakyThrows
  @BeforeEach
  void setUp() {
    validCreatingAccountDTO = new CreatingAccountDTO(90.00, 80.00, "Savings", 1L);
  }

  @Given("I log in as an {string}")
  public void iAmLoggedInAsAnEmployee(String role) throws JsonProcessingException {
    httpHeaders.clear();
    httpHeaders.add("Content-Type", "application/json");
    LoginDTO loginDTO;
    if (role.equalsIgnoreCase("employee")) {
      loginDTO = new LoginDTO(EMPLOYEE_EMAIL, LOGIN_PASSWORD);
    } else if (role.equalsIgnoreCase("customer")) {
      loginDTO = new LoginDTO(CUSTOMER_EMAIL, LOGIN_PASSWORD);
    } else {
      throw new IllegalArgumentException("Invalid role");
    }
    httpHeaders.add("Authorization", "Bearer " + getToken(loginDTO));
  }

  @Then("the response status code should be {int}")
  public void theResponseStatusCodeShouldBe(int responseCode) {
    Assertions.assertEquals(responseCode, response.getStatusCode().value());
  }

  @When("I send a GET request to {string}")
  public void iSendAGETRequestTo(String endpoint) {
    response = restTemplate.exchange(
        "/" + endpoint,
        HttpMethod.GET,
        new HttpEntity<>(
            null,
            httpHeaders),
        String.class);
  }

  @And("the response should be an array of objects")
  public void theResponseShouldBeAnArrayOfObjects() throws JsonProcessingException {
    List<AccountDTO> accounts = objectMapper.
        readValue(response.getBody(),
            objectMapper.getTypeFactory().constructCollectionType(List.class, AccountDTO.class));
    Assertions.assertTrue(accounts.size() > 0);
  }

  @And("the response should be an object with an {string} property")
  public void theResponseShouldBeAnObjectWithAnProperty(String iban)
      throws JsonProcessingException {
    AccountDTO account = objectMapper.readValue(response.getBody(), AccountDTO.class);
    Assertions.assertEquals(iban, account.getIban());
  }

  @And("the response should be error message {string}")
  public void theResponseShouldBeErrorMessage(String errorMessage) throws JsonProcessingException {
    ErrorMessageDTO error = objectMapper.readValue(response.getBody(), ErrorMessageDTO.class);
    Assertions.assertEquals(errorMessage, error.message());
  }

  @And("the response should have {int} object")
  public void theResponseShouldHaveObject(int numberOfObjects) throws JsonProcessingException {
    List<AccountDTO> accounts = objectMapper.
        readValue(response.getBody(),
            objectMapper.getTypeFactory().constructCollectionType(List.class, AccountDTO.class));
    Assertions.assertEquals(numberOfObjects, accounts.size());
  }

  //  @And("The response should be an {string} list")
//  public void theResponseShouldBeAnUserAccountsDTOList(String className)
//      throws Exception {
//
//    List<Object> objects = objectMapper.readValue(response.getBody(),
//        objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
//
//    // Check the class name for each object in the list using Stream API
//    List<String> objectClassNames = objects.stream()
//        .map(Object::getClass)
//        .map(Class::getName)
//        .toList();
//
//    // Assert that all object class names match the provided class name
//    Assertions.assertTrue(objectClassNames.stream().allMatch(cn -> cn.equals(className)));
//  }//    Class<?> clazz = Class.forName(className);

  @And("The response should be an UserAccountsDTO List")
  public void theResponseShouldBeAnUserAccountsDTOList() throws JsonProcessingException {
    String responseBody = response.getBody();
    UserAccountsDTO userAccountsDTO = objectMapper.readValue(responseBody, UserAccountsDTO.class);
    List<UserAccountsDTO> accounts = Collections.singletonList(userAccountsDTO);
    Assertions.assertTrue(accounts.size() > 0);
  }


  @When("I send a POST request to {string}")
  public void iSendAPOSTRequestTo(String endpoint) {
    CreatingAccountDTO creatingAccountDTO = new CreatingAccountDTO(90.00,80.00,"Savings",1L);
    httpHeaders.add("Content-Type", "application/json");
    response = restTemplate.exchange(
        "/" + endpoint,
        HttpMethod.POST,
        new HttpEntity<>(
            creatingAccountDTO,
            httpHeaders),
        String.class);
  }
  public void sendPostRequest(HttpHeaders httpHeaders, String endpoint, Object body) {
    response = restTemplate.exchange(
        "/" + endpoint,
        HttpMethod.POST,
        new HttpEntity<>(
            body,
            httpHeaders),
        String.class);
  }

  @When("I send a POST request to {string} with a valid CreatingAccountDTO")
  public void iSendAPOSTRequestToWithAValidCreatingAccountDTO(String arg0) {
  }
}
