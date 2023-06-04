package com.term4.BankingAppGrp1.requestDTOs;

import com.term4.BankingAppGrp1.responseDTOs.AccountHolderDTO;
import jakarta.validation.constraints.NotNull;

public record UpdatingAccountDTO(
        double absoluteLimit,
        @NotNull(message = " The active field cannot be left empty") Boolean isActive,
        AccountHolderDTO accountHolder) {
}
