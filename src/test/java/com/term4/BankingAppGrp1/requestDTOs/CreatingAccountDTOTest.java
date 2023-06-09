package com.term4.BankingAppGrp1.requestDTOs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CreatingAccountDTOTest extends ValidatingConstraints {

  @Test
  void whenCreatingAccountDTOWithoutADayLimitShouldResultInAConstraintViolationExceptionWithMessage() {
    CreatingAccountDTO creatingAccountDTO =
        new CreatingAccountDTO(null, 1.00, "Savings", 1L);
    assertEquals("Day Limit cannot be left empty", getMessageFromViolations(creatingAccountDTO));
  }

  @Test
  void whenCreatingAccountDTOWithNegativeDayLimitShouldResultInAConstraintViolationExceptionWithMessage() {
    CreatingAccountDTO creatingAccountDTO =
        new CreatingAccountDTO(-1.00, 1.00, "Savings", 1L);
    assertEquals("Day Limit cannot be negative", getMessageFromViolations(creatingAccountDTO));
  }

  @Test
  void whenCreatingAccountDTOWithValidDayLimitShouldResultInAValidObject() {
    CreatingAccountDTO creatingAccountDTO =
        new CreatingAccountDTO(200.00, 1.00, "Savings", 1L);
    assertEquals(200.00, creatingAccountDTO.dayLimit());
  }

  @Test
  void whenCreatingAccountDTOWithoutAnTransactionLimitShouldResultInAConstraintViolationExceptionWithMessage() {
    CreatingAccountDTO creatingAccountDTO =
        new CreatingAccountDTO(1.00, null, "Savings", 1L);
    assertEquals("Transaction Limit cannot be left empty",
        getMessageFromViolations(creatingAccountDTO));
  }

  @Test
  void whenCreatingAccountDTOWithNegativeTransactionLimitShouldResultInAConstraintViolationExceptionWithMessage() {
    CreatingAccountDTO creatingAccountDTO =
        new CreatingAccountDTO(1.00, -1.00, "Savings", 1L);
    assertEquals("Transaction Limit cannot be negative",
        getMessageFromViolations(creatingAccountDTO));
  }

  @Test
  void whenCreatingAccountDTOWithValidTransactionLimitShouldResultInAValidObject() {
    CreatingAccountDTO creatingAccountDTO =
        new CreatingAccountDTO(1.00, 200.00, "Savings", 1L);
    assertEquals(200.00, creatingAccountDTO.transactionLimit());
  }

  @Test
  void whenCreatingAccountDTOWithoutAnAccountTypeShouldResultInAConstraintViolationExceptionWithMessage() {
    CreatingAccountDTO creatingAccountDTO =
        new CreatingAccountDTO(1.00, 1.00, null, 1L);
    assertEquals("The account type is not valid", getMessageFromViolations(creatingAccountDTO));
  }

  @Test
  void whenCreatingAccountDTOWithAnAccountTypeShouldResultInAValidObject() {
    CreatingAccountDTO creatingAccountDTO =
        new CreatingAccountDTO(1.00, 1.00, "Savings", 1L);
    assertEquals("Savings", creatingAccountDTO.accountType());
  }

  @Test
  void whenCreatingAccountDTOWithoutAnAccountHolderIdShouldResultInAConstraintViolationExceptionWithMessage() {
    CreatingAccountDTO creatingAccountDTO =
        new CreatingAccountDTO(1.00, 1.00, "Savings", null);
    assertEquals("accountHolderId cannot be left empty",
        getMessageFromViolations(creatingAccountDTO));
  }

  @Test
  void whenCreatingAccountDTOWithAnAccountHolderIdShouldResultInAValidObject() {
    CreatingAccountDTO creatingAccountDTO =
        new CreatingAccountDTO(1.00, 1.00, "Savings", 1L);
    assertEquals(1L, creatingAccountDTO.accountHolderId());
  }

}