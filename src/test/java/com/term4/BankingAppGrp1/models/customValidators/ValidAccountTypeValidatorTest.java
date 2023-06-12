package com.term4.BankingAppGrp1.models.customValidators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ValidAccountTypeValidatorTest {

  @Test
  void whenValidAccountTypeIsPassedITGivesValidAccountType() {
    ValidAccountTypeValidator validAccountTypeValidator = new ValidAccountTypeValidator();
    assertTrue(validAccountTypeValidator.isValid("Savings", null));
  }
  @Test
  void whenInvalidAccountTypeIPassedWillReturnFalse(){
    ValidAccountTypeValidator validAccountTypeValidator = new ValidAccountTypeValidator();
    assertFalse(validAccountTypeValidator.isValid("Savings1", null));
  }

}