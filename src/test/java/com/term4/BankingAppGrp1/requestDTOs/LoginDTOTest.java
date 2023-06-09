package com.term4.BankingAppGrp1.requestDTOs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginDTOTest extends ValidatingConstraints {
    @Test
    void creatingLoginDTOWithAnEmailAndPasswordShouldResultInAValidObject() {
        LoginDTO loginDTO = new LoginDTO("email@email.com", "password");
        Assertions.assertNotNull(loginDTO);
    }

    @Test
    void creatingLoginDTOWithoutAnEmailShouldResultInAConstraintViolationException() {
        LoginDTO loginDTO = new LoginDTO("", "password");

        assertEquals("Email is required.", getMessageFromViolations(loginDTO));
    }

    @Test
    void creatingLoginDTOWithoutAValidEmailStringShouldResultInAConstraintViolationException() {
        LoginDTO loginDTO = new LoginDTO("notAnEmail", "password");

        assertEquals("Email is invalid.", getMessageFromViolations(loginDTO));
    }

    @Test
    void creatingLoginDTOWithoutAPasswordShouldResultInAConstraintViolationException() {
        LoginDTO loginDTO = new LoginDTO("email@email.com", "");

        assertEquals("Password is required.", getMessageFromViolations(loginDTO));
    }
}


