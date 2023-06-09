package com.term4.BankingAppGrp1.requestDTOs;

import jakarta.validation.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class LoginDTOTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void creatingLoginDTOWithAnEmailAndPasswordShouldResultInAValidObject() {
        LoginDTO loginDTO = new LoginDTO("email@email.com", "password");
        Assertions.assertNotNull(loginDTO);
    }

    @Test
    void creatingLoginDTOWithoutAnEmailShouldResultInAConstraintViolationException() {
        LoginDTO loginDTO = new LoginDTO("", "password");

        Set<ConstraintViolation<LoginDTO>> violations = this.validator.validate(loginDTO);
        String message = violations.iterator().next().getMessage();
        assertEquals("Email is required.", message);
    }

    @Test
    void creatingLoginDTOWithoutAValidEmailStringShouldResultInAConstraintViolationException() {
        LoginDTO loginDTO = new LoginDTO("notAnEmail", "password");

        Set<ConstraintViolation<LoginDTO>> violations = this.validator.validate(loginDTO);
        String message = violations.iterator().next().getMessage();
        assertEquals("Email is invalid.", message);
    }

    @Test
    void creatingLoginDTOWithoutAPasswordShouldResultInAConstraintViolationException() {
        LoginDTO loginDTO = new LoginDTO("email@email.com", "");

        Set<ConstraintViolation<LoginDTO>> violations = this.validator.validate(loginDTO);
        String message = violations.iterator().next().getMessage();
        assertEquals("Password is required.", message);
    }
}


