package com.term4.BankingAppGrp1.responseDTOs;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

//can bsn be a type LONG? If not then how do I fix the error shown in the User Service class on line 54?
public record UserDTO(@NotNull(message = "User Id cannot be left empty!") long id, long bsn, @NotNull(message = "First Name cannot be left empty") String firstName, @NotNull(message = "Last Name cannot be left empty") String lastName, LocalDate dateOfBirth, String phoneNumber, String email, boolean isActive, double dayLimit, double transactionLimit) {
}
