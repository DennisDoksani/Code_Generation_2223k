package com.term4.BankingAppGrp1.responseDTOs;


import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.User;

public record TransactionDTO(Double amount, Account accountTo, Account accountFrom, User userPerforming) {
    //ask if the accounts and user should also be dto
}
