package com.term4.BankingAppGrp1.requestDTOs;

import com.term4.BankingAppGrp1.models.InhollandIBANPattern;
import jakarta.validation.constraints.NotNull;

public record ATMWithdrawDTO(@NotNull(message = "The account FROM cannot be empty!")
@InhollandIBANPattern String accountFrom, @NotNull(message = "The account's amount cannot be empty!") Double amount) {
}
