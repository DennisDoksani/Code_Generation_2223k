package com.term4.BankingAppGrp1.requestDTOs;

import jakarta.validation.constraints.NotNull;

public record CreatingAccountDTO(
        double dayLimit,
        double transactionLimit,
        @NotNull(message = "accountType cannot be left empty") String accountType,
        @NotNull(message = "accountHolderId cannot be left empty") Long accountHolderId) {


}
