package com.term4.BankingAppGrp1.responseDTOs;

import java.time.LocalDate;

public record UserDTO(int bsn, String firstName, String lastName, LocalDate dateOfBirth, String phoneNumber, String email,
                      String password, boolean isActive, double dayLimit, double transactionLimit) {
}
