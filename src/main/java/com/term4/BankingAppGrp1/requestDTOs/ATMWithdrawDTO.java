package com.term4.BankingAppGrp1.requestDTOs;

import com.term4.BankingAppGrp1.models.customValidators.InhollandIBANPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ATMWithdrawDTO(
                            @InhollandIBANPattern
                            @NotBlank(message = "The iban cannot be empty.")
                            String accountFrom,
                            @NotNull(message = "The amount cannot be empty.")
                            @Positive(message = "The amount must be greater than 0.")
                            Double amount) {}

