package com.term4.BankingAppGrp1.requestDTOs;

import com.term4.BankingAppGrp1.responseDTOs.AccountHolderDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UpdatingAccountDTO(
        @NotNull(message = "Absolute Limit cannot be Null")
        Double absoluteLimit,
        @NotNull(message = "The active field cannot be left empty") Boolean isActive,
        @NotNull(message = "The AccountHolder Details cannot be empty")
        @Valid AccountHolderDTO accountHolder) {
}
