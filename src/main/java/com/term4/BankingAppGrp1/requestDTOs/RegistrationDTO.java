package com.term4.BankingAppGrp1.requestDTOs;

public record RegistrationDTO(Integer bsn, String email, String password, String firstName, String lastName, String phoneNumber, String dateOfBirth) {
    
}
