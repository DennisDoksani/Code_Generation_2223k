package com.term4.BankingAppGrp1.responseDTOs;


public record TransactionDTO(Double amount, String accountTo, String accountFrom, long userPerforming) {
}
