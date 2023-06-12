package com.term4.BankingAppGrp1.requestDTOs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ATMWithdrawDTOTest extends ValidatingConstraints {

  private ATMWithdrawDTO validATMWithdrawDTO;

  @BeforeEach
  public void setUp() {
    validATMWithdrawDTO = new ATMWithdrawDTO(
        "NL61INHO0000000001",
        100.00
    );
  }

  @Test
  void creatingATMWithdrawDTOWithAllValidFieldsShouldResultInAValidObject() {
    Assertions.assertNotNull(validATMWithdrawDTO);
  }

  //TODO: negative amount

  //TODO: nulls

  //TODO: no pattern

  @Test
  void accountFrom() {

  }

  @Test
  void amount() {
  }
}
