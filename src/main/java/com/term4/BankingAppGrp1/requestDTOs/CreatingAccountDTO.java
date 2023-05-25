package com.term4.BankingAppGrp1.requestDTOs;


public record CreatingAccountDTO(double dayLimit, double transactionLimit, String accountType,
                                 Long accountHolderId) {

}
