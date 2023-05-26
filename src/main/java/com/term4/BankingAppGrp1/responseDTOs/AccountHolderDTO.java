package com.term4.BankingAppGrp1.responseDTOs;

public record AccountHolderDTO(long userId, double dayLimit ,double transactionLimit, String firstName, String lastName ) {
}
