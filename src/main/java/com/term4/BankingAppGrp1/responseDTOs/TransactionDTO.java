package com.term4.BankingAppGrp1.responseDTOs;


import com.term4.BankingAppGrp1.models.User;

import java.time.LocalDate;
import java.time.LocalTime;

public record TransactionDTO(Double amount, String accountTo, String accountFrom, LocalDate date, LocalTime timestamp,
                             User userPerforming) { //Ask if this should be user object or username
}
