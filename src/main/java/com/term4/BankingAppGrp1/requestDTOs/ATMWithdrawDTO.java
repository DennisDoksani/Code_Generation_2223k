package com.term4.BankingAppGrp1.requestDTOs;

import com.term4.BankingAppGrp1.models.InhollandIBANPattern;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ATMWithdrawDTO(@NotNull(message = "The account FROM cannot be empty!")
                             @InhollandIBANPattern String accountFrom,
                             @NotNull(message = "The account's amount cannot be empty!")
                             @Positive(message = "The Amount must be Greater than 0")
                             Double amount) {
}
