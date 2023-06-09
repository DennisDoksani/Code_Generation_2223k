package com.term4.BankingAppGrp1.requestDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreatingAccountDTO(
        @NotNull(message="Day Limit cannot be left empty")
         @PositiveOrZero(message = "Day Limit cannot be negative")
        Double dayLimit,
        @NotNull(message="Dat Limit cannot be left empty")
        @PositiveOrZero(message = "Day Limit cannot be negative")
        Double transactionLimit,
        @NotBlank(message = "accountType cannot be left empty") String accountType,
        @NotNull(message = "accountHolderId cannot be left empty") Long accountHolderId) {


}
