package com.term4.BankingAppGrp1.responseDTOs;
// Todo: User id to long after git pull
public record AccountHolderDTO(long userId, double dayLimit ,double transactionLimit, String firstName, String lastName ) {
}
