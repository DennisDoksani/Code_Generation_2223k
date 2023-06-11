package com.term4.BankingAppGrp1.models.customValidators;

import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.models.customValidators.ValidAccountType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidAccountTypeValidator implements ConstraintValidator<ValidAccountType, String> {

  @Override
  public boolean isValid(String stringAccountType, ConstraintValidatorContext constraintValidatorContext) {
    try{
      AccountType.valueOf(stringAccountType.toUpperCase());
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}
