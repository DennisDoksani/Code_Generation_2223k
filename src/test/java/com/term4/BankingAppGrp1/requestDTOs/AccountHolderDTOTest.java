package com.term4.BankingAppGrp1.requestDTOs;

import com.term4.BankingAppGrp1.responseDTOs.AccountHolderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountHolderDTOTest extends ValidatingConstraints {

    @BeforeEach
    public void setUp() {
        super.setUp(); // parents setup
    }

    @Test
    void whenUserIdIsNullThenThrowsConstraintsViolationExceptionWithMessage() {
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO(null, 50.00,
                50.00, "test", "test");
        assertEquals("User Id cannot be left empty",
                getMessageFromViolations(accountHolderDTO));

    }

    @Test
    void whenUserIdIsValidItReturnsValidDTO() {
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO(1L, 50.00,
                50.00, "test", "test");
        assertEquals(1L, accountHolderDTO.userId());
    }

    @Test
    void whenDayLimitIsNullThenThrowsConstraintsViolationExceptionWithMessage() {
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO(1L, null,
                50.00, "test", "test");
        assertEquals("Day Limit cannot be left empty",
                getMessageFromViolations(accountHolderDTO));

    }

    @Test
    void whenDayLimitIsNegativeThenThrowsConstraintsViolationExceptionWithMessage() {
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO(1L, -50.00,
                50.00, "test", "test");
        assertEquals("The day limit cannot be Negative",
                getMessageFromViolations(accountHolderDTO));

    }

    @Test
    void whenDayLimitIsValidItReturnsValidDTO() {
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO(1L, 50.00,
                50.00, "test", "test");
        assertEquals(50.00, accountHolderDTO.dayLimit());
    }

    @Test
    void whenTransactionLimitIsNullThenThrowsConstraintsViolationExceptionWithMessage() {
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO(1L, 50.00,
                null, "test", "test");
        assertEquals("Transaction Limit cannot be left empty",
                getMessageFromViolations(accountHolderDTO));

    }
    @Test
    void whenTransactionLimitIsNegativeThenThrowsConstraintsViolationExceptionWithMessage() {
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO(1L, 50.00,
                -50.00, "test", "test");
        assertEquals("The transaction limit cannot be Negative",
                getMessageFromViolations(accountHolderDTO));

    }
    @Test
    void whenTransactionLimitIsValidItReturnsValidDTO() {
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO(1L, 50.00,
                50.00, "test", "test");
        assertEquals(50.00, accountHolderDTO.transactionLimit());
    }
    @Test
    void whenFirstNameIsBlankThenThrowsConstraintsViolationExceptionWithMessage() {
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO(1L, 50.00,
                50.00, null, "test");
        assertEquals("First Name cannot be left empty",
                getMessageFromViolations(accountHolderDTO));

    }
    @Test
    void whenFirstNameIsValidItReturnsValidDTO() {
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO(1L, 50.00,
                50.00, "test", "test");
        assertEquals("test", accountHolderDTO.firstName());
    }
    @Test
    void whenLastNameIsBlankThenThrowsConstraintsViolationExceptionWithMessage() {
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO(1L, 50.00,
                50.00, "test", null);
        assertEquals("Last Name cannot be left empty",
                getMessageFromViolations(accountHolderDTO));

    }
    @Test
    void whenLastNameIsValidItReturnsValidDTO() {
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO(1L, 50.00,
                50.00, "test", "test");
        assertEquals("test", accountHolderDTO.lastName());
    }
}