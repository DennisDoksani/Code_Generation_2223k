package com.term4.BankingAppGrp1.requestDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
    @NotBlank(message = "Email is required.") @Email(message = "Email is invalid.") String email,
    @NotBlank(message = "Password is required.") String password
) {

}
