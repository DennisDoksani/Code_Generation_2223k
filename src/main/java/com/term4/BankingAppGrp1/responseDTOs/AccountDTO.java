package com.term4.BankingAppGrp1.responseDTOs;

import com.term4.BankingAppGrp1.models.AccountType;

import java.time.LocalDate;

public record AccountDTO(String iban, double accountBalance,double absoluteLimit, LocalDate creationDate ,
                         boolean isActive, AccountType accountType, AccountHolderDTO accountHolder) { 

}
