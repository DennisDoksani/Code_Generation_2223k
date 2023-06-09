package com.term4.BankingAppGrp1.models.customValidators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// making a custom annotation for the Inholland bank IBAN pattern in order to remove duplicate code
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Constraint(validatedBy = {})
@Pattern(regexp = "(?i)NL\\d{2}INHO\\d{10}", message = "Not a valid Iban for Inholland bank")
public @interface InhollandIBANPattern {

  String message() default "Invalid IBAN";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
