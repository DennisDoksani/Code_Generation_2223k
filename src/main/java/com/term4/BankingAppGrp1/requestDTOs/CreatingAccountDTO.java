package com.term4.BankingAppGrp1.requestDTOs;

import com.term4.BankingAppGrp1.models.AccountType;

public record CreatingAccountDTO(double balance, double transactionLimit, AccountType accountType,
                                 Long accountHolderId) {
}