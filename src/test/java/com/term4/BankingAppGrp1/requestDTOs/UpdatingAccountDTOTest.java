package com.term4.BankingAppGrp1.requestDTOs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.term4.BankingAppGrp1.responseDTOs.AccountHolderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UpdatingAccountDTOTest extends ValidatingConstraints {

  private AccountHolderDTO testAccountHolderDTO;

  @BeforeEach
  public void setUp() {
    super.setUp(); // parents setup method
    testAccountHolderDTO =
        new AccountHolderDTO(1L, 50.00, 50.00, "test", "test");
  }

  @Test
  void whenCreatingUpdatingAccountDTOWithoutDayLimitShouldResultInAConstraintViolationExceptionWithMessage() {
    UpdatingAccountDTO updatingAccountDTO =
        new UpdatingAccountDTO(null, true, testAccountHolderDTO);
    assertEquals("Absolute Limit cannot be Null", getMessageFromViolations(updatingAccountDTO));
  }

  @Test
  void whenCreatingInstancingUpdatingAccountDTOWithPositiveDayLimitShouldResultInValidDTO() {
    UpdatingAccountDTO updatingAccountDTO =
        new UpdatingAccountDTO(100.00, true, testAccountHolderDTO);
    assertEquals(100.00, updatingAccountDTO.absoluteLimit());
  }

  @Test
  void whenCreatingUpdatingAccountDTOWithNegativeDayLimitShouldResultInValidDTO() {
    UpdatingAccountDTO updatingAccountDTO =
        new UpdatingAccountDTO(-100.00, true, testAccountHolderDTO);
    assertEquals(-100.00, updatingAccountDTO.absoluteLimit());
  }

  @Test
  void whenCreatingUpdatingAccountDTOWithoutIsActiveShouldResultInAConstraintViolationExceptionWithMessage() {
    UpdatingAccountDTO updatingAccountDTO =
        new UpdatingAccountDTO(100.00, null, testAccountHolderDTO);
    assertEquals("The active field cannot be left empty",
        getMessageFromViolations(updatingAccountDTO));
  }

  @Test
  void whenCreatingUpdatingAccountDTOWithIsActiveShouldResultInValidDTO() {
    UpdatingAccountDTO updatingAccountDTO =
        new UpdatingAccountDTO(100.00, true, testAccountHolderDTO);
    assertEquals(true, updatingAccountDTO.isActive());
  }

  @Test
  void whenCreatingUpdatingAccountDTOWithoutAccountHolderShouldResultInAConstraintViolationExceptionWithMessage() {
    UpdatingAccountDTO updatingAccountDTO =
        new UpdatingAccountDTO(100.00, true, null);
    assertEquals("The AccountHolder Details cannot be empty",
        getMessageFromViolations(updatingAccountDTO));
  }

  @Test
  void whenCreatingUpdatingAccountDTOWithAccountHolderShouldResultInValidDTO() {
    UpdatingAccountDTO updatingAccountDTO =
        new UpdatingAccountDTO(100.00, true, testAccountHolderDTO);
    assertEquals(testAccountHolderDTO, updatingAccountDTO.accountHolder());
  }


}