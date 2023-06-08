package com.term4.BankingAppGrp1.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties.Build;

import com.term4.BankingAppGrp1.models.User;

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
}
