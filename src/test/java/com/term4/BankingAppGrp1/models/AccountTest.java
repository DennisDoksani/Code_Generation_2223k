package com.term4.BankingAppGrp1.models;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountTest {
    private Account account;

    @BeforeEach
    void init() {
        account = Account.builder().build();
    }

    @Test
    void createNewAccountShouldResultInAValidObject() {
        Assertions.assertThat(account).isNotNull();
    }

    @Test
    void settingNegativeBalanceShouldThrowIllegalArgumentExceptionWithMessage() {
        Exception exception = Assertions.catchException(() -> account.setBalance(-1));
        Assertions.assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Balance can not be negative");
    }

    @Test
    void settingPositiveBalanceShouldSetBalance() {
        account.setBalance(1.20);
        assertEquals(1.20, account.getBalance());
    }

    @Test
    void newAccountShouldHaveZeroBalance() {
        Assertions.assertThat(account.getBalance()).isZero();
    }

    @Test
    void newAccountShouldHaveIsActiveTrue() {
        Assertions.assertThat(account.isActive()).isTrue();
    }

    @Test
    void newAccountShouldNotHaveDefaultAccountType() {
        Assertions.assertThat(account.getAccountType()).isNull();
    }

    @Test
    void newAccountShouldHaveDefaultCreationDate() {
        Assertions.assertThat(account.getCreationDate())
                .isEqualTo(java.time.LocalDate.now());
    }

    @Test
    void newAccountShouldHaveDefaultAbsoluteLimit() {
        Assertions.assertThat(account.getAbsoluteLimit()).isZero();
    }

    @Test
    void newAccountShouldHaveDefaultNullCustomer() {
        Assertions.assertThat(account.getCustomer()).isNull();
    }

    @Test
    void notValidEnumAccountTypeThrowsIllegalArgumentException() {
        Exception exception = Assertions.catchException(() -> account.setAccountType(AccountType.valueOf("NOTVALID")));
        Assertions.assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No enum constant com.term4.BankingAppGrp1.models.AccountType.NOTVALID");
    }
    @Test
    void ValidStringShouldResultItoValidEnumAccountType() {
        account.setAccountType(AccountType.valueOf("SAVINGS"));
        Assertions.assertThat(account.getAccountType()).isEqualTo(AccountType.SAVINGS);
    }


    @Test
    void getCustomerShouldReturnValidCustomer() {
        User user = User.builder()
                .bsn("582022290")
                .firstName("Ruubyo")
                .lastName("Gaming")
                .dateOfBirth(LocalDate.of(2003, 10, 1))
                .phoneNumber("0611111121")
                .email("Ruubyo@isgaming.com")
                .password("secretword")
                .isActive(true)
                .dayLimit(300)
                .transactionLimit(300)
                .roles(List.of(Role.ROLE_EMPLOYEE))
                .build();
        account.setCustomer(user);
        Assertions.assertThat(account.getCustomer()).isEqualTo(user).hasSameClassAs(user);
    }
}