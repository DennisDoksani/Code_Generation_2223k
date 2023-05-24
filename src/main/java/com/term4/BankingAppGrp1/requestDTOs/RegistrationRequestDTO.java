package com.term4.BankingAppGrp1.requestDTOs;

import java.time.LocalDate;

public record RegistrationRequestDTO(Integer bsn, String email, String password, String firstName, String lastName, String phoneNumber, LocalDate dateOfBirth) {
    
}
