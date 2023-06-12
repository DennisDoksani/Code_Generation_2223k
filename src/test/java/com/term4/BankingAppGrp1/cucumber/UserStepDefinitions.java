package com.term4.BankingAppGrp1.cucumber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.requestDTOs.RegistrationDTO;
import com.term4.BankingAppGrp1.responseDTOs.ErrorMessageDTO;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;

public class UserStepDefinitions extends BaseStepDefinition {

  private RegistrationDTO registrationDTO;
  private final String VALID_DOB = LocalDate.now().minusYears(20).toString();
  private final String TOO_YOUNG_DOB = LocalDate.now().minusYears(13).toString();

  @Given("I am a visitor without credentials")
  public void iAmAVisitorWithoutCredentials() {
    httpHeaders.clear();
  }

  @And("I have filled in the registration form with valid information")
  public void iHaveFilledInTheRegistrationFormWithValidInformation() {
    registrationDTO = new RegistrationDTO(
        "137667024",
        "johndoe@email.com",
        "password123",
        "John",
        "Doe",
        "0634567890",
        VALID_DOB);
  }

  @And("I have filled in the registration form with an invalid BSN")
  public void iHaveFilledInTheRegistrationFormWithAnInvalidBsn() {
    registrationDTO = new RegistrationDTO(
        "123123123",
        "johndoe2@email.com",
        "password123",
        "John",
        "Doe",
        "0634567890",
        VALID_DOB);
  }

  @And("I have filled in the registration form with an invalid email")
  public void iHaveFilledInTheRegistrationFormWithAnInvalidEmail() {
    registrationDTO = new RegistrationDTO(
        "137667024",
        "no-email-com",
        "password123",
        "John",
        "Doe",
        "0634567890",
        VALID_DOB);
  }

  @And("I have filled in the registration form with an invalid phone number")
  public void iHaveFilledInTheRegistrationFormWithAnInvalidPhoneNumber() {
    registrationDTO = new RegistrationDTO(
        "234684513",
        "johndoe2@email.com",
        "password123",
        "John",
        "Doe",
        "123aaa",
        VALID_DOB);
  }

  @And("I have filled in the registration form but I am younger than 18 years old")
  public void iHaveFilledInTheRegistrationButIAmYoungerThan18YearsOld() {
    registrationDTO = new RegistrationDTO(
        "234684513",
        "johndoe2@email.com",
        "password123",
        "John",
        "Doe",
        "06-34567890",
        TOO_YOUNG_DOB);
  }

  @When("I call the registration endpoint")
  public void iCallTheRegistrationEndpoint() {
    response = restTemplate.postForEntity(
        USERS_ENDPOINT,
        registrationDTO,
        String.class);
  }

  @Then("I receive http status {int}")
  public void iReceiveHttpStatus(int responseCode) {
    Assertions.assertEquals(responseCode, response.getStatusCode().value());
  }

  @And("a new user is returned")
  public void aNewUserIsReturned() throws JsonProcessingException {
    User user = objectMapper.readValue(response.getBody(), User.class);
    Assertions.assertNotNull(user);
    Assertions.assertNotEquals(0, user.getId());
    Assertions.assertEquals(registrationDTO.bsn(), user.getBsn());
    Assertions.assertEquals(registrationDTO.email(), user.getEmail());
    Assertions.assertEquals(registrationDTO.firstName(), user.getFirstName());
    Assertions.assertEquals(registrationDTO.lastName(), user.getLastName());
    Assertions.assertEquals(registrationDTO.phoneNumber(), user.getPhoneNumber());
  }

  @And("I receive error message: {string}")
  public void iReceiveAnErrorMessage(String errorMessage) throws JsonProcessingException {
    ErrorMessageDTO errorMessageDTO = objectMapper.readValue(response.getBody(),
        ErrorMessageDTO.class);
    Assertions.assertEquals(errorMessage, errorMessageDTO.message());
  }
}
