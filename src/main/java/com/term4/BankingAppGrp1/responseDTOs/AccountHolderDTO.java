package com.term4.BankingAppGrp1.responseDTOs;

import jakarta.validation.constraints.NotNull;
// This Record is used in both response and request bodies
public record AccountHolderDTO( @NotNull(message = "User Id cannot be left empty") Long userId,
                                double dayLimit ,
                                double transactionLimit,
                               @NotNull(message = "First Name cannot be left empty") String firstName,
                               @NotNull(message = "Last Name cannot be left empty") String lastName) {
}
