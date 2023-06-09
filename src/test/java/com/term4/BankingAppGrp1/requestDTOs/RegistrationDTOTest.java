package com.term4.BankingAppGrp1.requestDTOs;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationDTOTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void creatingRegistrationDTOWithoutAnEmailShouldResultInAConstraintViolationException() {
        RegistrationDTO registrationDTO = new RegistrationDTO("123456789",
                "",
                "password",
                "firstName",
                "lastName",
                "06-34531077",
                "2000-01-01");

        Set<ConstraintViolation<RegistrationDTO>> violations = this.validator.validate(registrationDTO);
        String message = violations.iterator().next().getMessage();
        assertEquals("Email is required.", message);
    }
}
