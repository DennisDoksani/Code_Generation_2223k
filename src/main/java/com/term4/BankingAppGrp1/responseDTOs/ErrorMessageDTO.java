package com.term4.BankingAppGrp1.responseDTOs;

import jakarta.validation.constraints.NotBlank;

public record ErrorMessageDTO(@NotBlank String message) {
}
