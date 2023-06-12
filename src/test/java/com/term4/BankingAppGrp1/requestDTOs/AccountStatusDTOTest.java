package com.term4.BankingAppGrp1.requestDTOs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AccountStatusDTOTest extends ValidatingConstraints {

  @Test
  void creatingAccountStatusDTOWithoutAnIsActiveShouldResultInAConstraintViolationException() {
    AccountStatusDTO accountStatusDTO = new AccountStatusDTO(null);
    assertEquals("isActive cannot be null in request body in order to update account status",
        getMessageFromViolations(accountStatusDTO));
  }

  @Test
  void creatingAccountStatusDTOWithAnIsActiveShouldResultInAValidObject() {
    AccountStatusDTO accountStatusDTO = new AccountStatusDTO(true);
    Assertions.assertEquals(true, accountStatusDTO.isActive());
  }

}