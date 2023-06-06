package com.term4.BankingAppGrp1.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
public class Transaction {

    @GeneratedValue
    @Id
    private long transactionID;

    private Double amount;

    @ManyToOne
    private Account accountTo;
    @ManyToOne
    private Account accountFrom;

    private LocalDate date;

    private LocalTime timestamp;

    @OneToOne
    private User userPerforming;

    public Transaction(Double amount, Account accountTo, Account accountFrom, LocalDate localDate, LocalTime localTime, User userPerforming) {
        this.amount = amount;
        this.accountTo = accountTo;
        this.accountFrom = accountFrom;
        this.date = localDate;
        this.timestamp = localTime;
        this.userPerforming = userPerforming;
    }

    public void setAmount(double amount) {
        if(amount <= 0)
            throw new IllegalArgumentException("Amount can not be zero or under");

        this.amount = amount;
    }
}
