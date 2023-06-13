package com.term4.BankingAppGrp1.requestDTOs;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;

abstract class ValidatingConstraints {

  private Validator validator;

  @BeforeEach
  public void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  protected String getMessageFromViolations(Object object) {
    Set<ConstraintViolation<Object>> violations = this.validator.validate(object);
    return violations.iterator().next().getMessage();
  }

}
