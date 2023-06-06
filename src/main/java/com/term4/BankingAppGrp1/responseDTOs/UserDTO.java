package com.term4.BankingAppGrp1.responseDTOs;

import java.time.LocalDate;

//can bsn be a type LONG? If not then how do I fix the error shown in the User Service class on line 54?
public record UserDTO(long id, long bsn, String firstName, String lastName, LocalDate dateOfBirth, String phoneNumber, String email, boolean isActive, double dayLimit, double transactionLimit) {
}
