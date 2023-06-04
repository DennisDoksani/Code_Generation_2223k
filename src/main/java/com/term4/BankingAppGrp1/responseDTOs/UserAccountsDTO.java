package com.term4.BankingAppGrp1.responseDTOs;

import java.util.List;

public record UserAccountsDTO(
        AccountHolderDTO accountHolder,

        List<AccountWithoutAccountHolderDTO> accounts
) {
}
