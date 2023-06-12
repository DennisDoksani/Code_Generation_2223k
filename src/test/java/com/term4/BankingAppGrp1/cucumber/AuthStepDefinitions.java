package com.term4.BankingAppGrp1.cucumber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.term4.BankingAppGrp1.requestDTOs.LoginDTO;
import com.term4.BankingAppGrp1.responseDTOs.ErrorMessageDTO;
import com.term4.BankingAppGrp1.responseDTOs.LoginResponseDTO;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class AuthStepDefinitions extends BaseStepDefinition {

  private LoginDTO loginDto;

  @Given("I have a valid login object with valid email and valid password")
  public void iHaveAValidLoginObjectWithValidEmailAndValidPassword() {
    loginDto = new LoginDTO(CUSTOMER_EMAIL, LOGIN_PASSWORD);
  }

  @Given("I have an invalid email and invalid password")
  public void iHaveAnInvalidEmailAndInvalidPassword() {
    loginDto = new LoginDTO("invalid@email.com", "invalidPassword");
  }

  @Given("I have a valid email but invalid password")
  public void iHaveAValidEmailAndInvalidPassword() {
    loginDto = new LoginDTO(CUSTOMER_EMAIL, "invalidPassword");
  }

  @Given("I have an invalid email and valid password")
  public void iHaveAnInvalidEmailAndValidPassword() {
    loginDto = new LoginDTO("invalid@email.com", LOGIN_PASSWORD);
  }

  @Given("I have a valid email but empty password")
  public void iHaveAValidEmailButEmptyPassword() {
    loginDto = new LoginDTO(CUSTOMER_EMAIL, "");
  }

  @Given("I have an empty email but valid password")
  public void iHaveAnEmptyEmailButValidPassword() {
    loginDto = new LoginDTO("", LOGIN_PASSWORD);
  }

  @Given("I have an invalid email format but valid password")
  public void iHaveAnInvalidEmailFormatButValidPassword() {
    loginDto = new LoginDTO("invalidEmailFormat", LOGIN_PASSWORD);
  }

  @When("I call the login endpoint")
  public void iCallTheLoginEndpoint() {
    response = restTemplate.postForEntity(
        LOGIN_ENDPOINT,
        loginDto,
        String.class);
  }

  @Then("I get http status {int}")
  public void iGetHttpStatus(int responseCode) {
    Assertions.assertEquals(responseCode, response.getStatusCode().value());
  }

  @And("I receive a login token response")
  public void iReceiveALoginTokenResponse() throws JsonProcessingException {
    LoginResponseDTO tokenResponse = objectMapper.readValue(response.getBody(),
        LoginResponseDTO.class);
    Assertions.assertNotNull(tokenResponse.jwt());
    Assertions.assertNotEquals(0, tokenResponse.id());
  }

  @And("I receive an error message: {string}")
  public void iReceiveAnErrorMessage(String errorMessage) throws JsonProcessingException {
    ErrorMessageDTO errorMessageDTO = objectMapper.readValue(response.getBody(),
        ErrorMessageDTO.class);
    Assertions.assertEquals(errorMessage, errorMessageDTO.message());
  }
}
