package com.term4.BankingAppGrp1.responseDTOs;

import java.time.LocalDate;
import java.time.LocalTime;

public record TransactionResponseDTO(Long id, double amount, TransactionAccountDTO accountFrom, TransactionAccountDTO accountTo, LocalDate date, LocalTime time, String userPerforming) {
}
