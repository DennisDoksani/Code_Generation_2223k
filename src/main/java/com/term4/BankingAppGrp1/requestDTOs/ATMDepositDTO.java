package com.term4.BankingAppGrp1.requestDTOs;
import com.term4.BankingAppGrp1.models.customValidators.InhollandIBANPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ATMDepositDTO(
        @InhollandIBANPattern
        @NotBlank(message = "The iban cannot be empty.")
        String accountTo,
        @NotNull(message = "The account's amount cannot be empty.")
        @Positive(message = "The Amount must be Greater than 0.")
        Double amount ) {


}
