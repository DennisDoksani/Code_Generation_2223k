package com.term4.BankingAppGrp1.requestDTOs;

import com.term4.BankingAppGrp1.models.ConstantsContainer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

public record LoginDTO(
    @NotBlank(message="Email is required.") @Email(message="Email is invalid.") String email,
    @NotBlank(message="Password is required.") String password
) {
}
