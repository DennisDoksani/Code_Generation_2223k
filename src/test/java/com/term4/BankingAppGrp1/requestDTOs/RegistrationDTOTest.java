package com.term4.BankingAppGrp1.requestDTOs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RegistrationDTOTest extends ValidatingConstraints {

  private RegistrationDTO validRegistrationDTO;

  @BeforeEach
  public void setUp() {
    validRegistrationDTO = new RegistrationDTO(
        "123456789",
        "email@email.com",
        "password",
        "firstName",
        "lastName",
        "06-34531077",
        "2000-01-01");
  }

  @Test
  void creatingRegistrationDTOWithAllValidFieldsShouldResultInAValidObject() {
    Assertions.assertNotNull(validRegistrationDTO);
  }

  @Test
  void creatingRegistrationDTOWithoutAnEmailShouldResultInAConstraintViolationException() {
    RegistrationDTO registrationDTO = new RegistrationDTO(
        "123456789",
        "",
        "password",
        "firstName",
        "lastName",
        "06-34531077",
        "2000-01-01");

    Assertions.assertEquals("Email is required.", getMessageFromViolations(registrationDTO));
  }

  @Test
  void creatingRegistrationDTOWithInvalidEmailShouldResultInAConstraintViolationException() {
    RegistrationDTO registrationDTO = new RegistrationDTO(
        "123456789",
        "notAnEmail",
        "password",
        "firstName",
        "lastName",
        "06-34531077",
        "2000-01-01");

    Assertions.assertEquals("Email is invalid.", getMessageFromViolations(registrationDTO));
  }

  @Test
  void creatingRegistrationDTOWithoutAPasswordShouldResultInAConstraintViolationException() {

  }
}
