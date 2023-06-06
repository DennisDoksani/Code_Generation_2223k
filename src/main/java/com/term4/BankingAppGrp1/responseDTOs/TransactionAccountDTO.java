package com.term4.BankingAppGrp1.responseDTOs;

import com.term4.BankingAppGrp1.models.AccountType;

public record TransactionAccountDTO(String iban, AccountType accountType, TransactionUserDTO userDTO) {
}
