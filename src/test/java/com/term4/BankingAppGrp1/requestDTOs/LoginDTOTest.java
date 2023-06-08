package com.term4.BankingAppGrp1.requestDTOs;

import jakarta.validation.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Validated
public class LoginDTOTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void creatingLoginDTOWithoutAValidEmailShouldResultInAnException() {

        LoginDTO loginDTO = new LoginDTO("", "password");

        Set<ConstraintViolation<LoginDTO>> violations = this.validator.validate(loginDTO);
        String message = violations.iterator().next().getMessage();
        assertEquals("Email is required.", message);
    }

    @Test
    void creatingLoginDTOWithoutAValidPasswordShouldResultInAnException() {

        Exception exception = Assertions.assertThrows(MethodArgumentNotValidException.class, () -> {
            new LoginDTO("email@email.com", "");
        });
    }
}


