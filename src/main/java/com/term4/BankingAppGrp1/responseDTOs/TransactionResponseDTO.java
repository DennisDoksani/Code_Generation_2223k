package com.term4.BankingAppGrp1.responseDTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

//Ask if the @data etc is necessary
public record TransactionResponseDTO(double amount, TransactionAccountDTO accountFrom, TransactionAccountDTO accountTo, LocalDate date, LocalTime time, String userPerforming) {
}
