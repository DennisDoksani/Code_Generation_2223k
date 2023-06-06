package com.term4.BankingAppGrp1.requestDTOs;
import jakarta.validation.constraints.NotNull;

public record ATMDepositDTO( @NotNull(message = "The account TO cannot be empty!")String accountTo,
        @NotNull(message = "The account's amount cannot be empty!")Double amount ) {
}
