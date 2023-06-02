package com.term4.BankingAppGrp1.requestDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrationDTO(
    @NotNull(message="BSN is required.") Integer bsn, 
    @NotBlank(message="Email is required.") @Email(message="Email is invalid.") String email, 
    @NotBlank(message="Password is required.") String password,  
    @NotBlank(message="First name is required.") String firstName,  
    @NotBlank(message="Last name is required.") String lastName,  
    @NotBlank(message="Phone number is required.") String phoneNumber,  
    @NotBlank(message="Date of Birth is required.") String dateOfBirth){} 
