package com.term4.BankingAppGrp1.requestDTOs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoginDTOTest {

    @Test
    void creatingLoginDTOWithoutAValidEmailShouldResultInAnException() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
           new LoginDTO("email", "password");
        });
    }
}
