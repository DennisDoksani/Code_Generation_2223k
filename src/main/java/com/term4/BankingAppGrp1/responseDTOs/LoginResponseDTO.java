package com.term4.BankingAppGrp1.responseDTOs;

import jakarta.validation.constraints.NotBlank;

public record LoginResponseDTO(
    @NotBlank String jwt,
    @NotBlank long id,
    @NotBlank String email,
    @NotBlank String name) {

}

