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
        setUp();
        Assertions.assertNotNull(user);
    }

    @Test
    void userBuilderShouldDefaultTo200AsTransactionLimit() {
        setUp();
        Assertions.assertEquals(200, user.getTransactionLimit());
    }

    @Test
    void userBuilderShouldDefaultTo500AsDayLimit() {
        setUp();
        Assertions.assertEquals(500, user.getDayLimit());
    }

    @Test
    void setDayLimitToNegativeNumberShouldResultInDayLimitBeingSetToZero() {
        setUp();
        user.setDayLimit(-1);

        Assertions.assertEquals(0, user.getDayLimit());
    }

    @Test
    void setDayLimitToPositiveNumberShouldResultInDayLimitBeingSetToThatNumber() {
        setUp();
        user.setDayLimit(1);

        Assertions.assertEquals(1, user.getDayLimit());
    }

    @Test
    void setTransactionLimitToNegativeNumberShouldResultInTransactionLimitBeingSetToZero() {
        setUp();
        user.setTransactionLimit(-1);

        Assertions.assertEquals(0, user.getTransactionLimit());
    }

    @Test
    void setTransactionLimitToPositiveNumberShouldResultInTransactionLimitBeingSetToThatNumber() {
        setUp();
        user.setTransactionLimit(1);

        Assertions.assertEquals(1, user.getTransactionLimit());
    }

    @Test
    void getFullNameShouldReturnFirstNameAndLastName() {
        setUp();
        user.setFirstName("John");
        user.setLastName("Doe");

        Assertions.assertEquals("John Doe", user.getFullName());
    }

    @Test
    void getFullNameWithNullsShouldNotThrowException() {
        setUp();
        Assertions.assertDoesNotThrow(() -> user.getFullName());
    }

}
