package com.term4.BankingAppGrp1.requestDTOs;

import jakarta.validation.constraints.NotNull; 

public record AccountStatusDTO(@NotNull(message = "isActive cannot be null in request body in order to update account status")
                               Boolean isActive) {
}
