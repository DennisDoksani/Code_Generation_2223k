package com.term4.BankingAppGrp1.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class UserTest {
    
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().build();
    }

    @Test
    void userBuilderShouldResultInValidUserObject() {
        Assertions.assertNotNull(user);
    }

    @Test
    void userBuilderShouldDefaultTo200AsTransactionLimit() {
        Assertions.assertEquals(200, user.getTransactionLimit());
    }

    @Test
    void userBuilderShouldDefaultTo500AsDayLimit() {
        Assertions.assertEquals(500, user.getDayLimit());
    }

    @Test
    void setDayLimitToNegativeNumberShouldResultInDayLimitBeingSetToZero() {
        user.setDayLimit(-1);

        Assertions.assertEquals(0, user.getDayLimit());
    }

    @Test
    void setDayLimitToPositiveNumberShouldResultInDayLimitBeingSetToThatNumber() {
        user.setDayLimit(1);

        Assertions.assertEquals(1, user.getDayLimit());
    }

    @Test
    void setTransactionLimitToNegativeNumberShouldResultInTransactionLimitBeingSetToZero() {
        user.setTransactionLimit(-1);

        Assertions.assertEquals(0, user.getTransactionLimit());
    }

    @Test
    void setTransactionLimitToPositiveNumberShouldResultInTransactionLimitBeingSetToThatNumber() {
        user.setTransactionLimit(1);

        Assertions.assertEquals(1, user.getTransactionLimit());
    }

    @Test
    void getFullNameShouldReturnFirstNameAndLastName() {
        user.setFirstName("John");
        user.setLastName("Doe");

        Assertions.assertEquals("John Doe", user.getFullName());
    }

    @Test
    void getFullNameWithNullsShouldNotThrowException() {
        Assertions.assertDoesNotThrow(() -> user.getFullName());
    }
}
