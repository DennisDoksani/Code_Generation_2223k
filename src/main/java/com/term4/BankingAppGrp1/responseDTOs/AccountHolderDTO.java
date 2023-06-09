package com.term4.BankingAppGrp1.responseDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

// This Record is used in both response and request bodies
public record AccountHolderDTO(@NotNull(message = "User Id cannot be left empty") Long userId,
                               @PositiveOrZero(message = "The day limit cannot be Negative")
                               @NotNull(message = "Day Limit cannot be left empty")
                               Double dayLimit,
                               @PositiveOrZero(message = "The transaction limit cannot be Negative")
                               @NotNull(message = "Transaction Limit cannot be left empty")
                               Double transactionLimit,
                               @NotBlank(message = "First Name cannot be left empty") String firstName,
                               @NotBlank(message = "Last Name cannot be left empty") String lastName) {
}
