package com.term4.BankingAppGrp1.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Transaction {

    @GeneratedValue
    @Id
    private long TransactionID;

    public Transaction(double amount, Account accountTo, Account accountFrom, Date date, Time timestamp, User userPerforming) {
        Amount = amount;
        AccountTo = accountTo;
        AccountFrom = accountFrom;
        this.date = date;
        this.timestamp = timestamp;
        UserPerforming = userPerforming;
    }

    private double Amount;

    @OneToOne
    private Account AccountTo;

    @OneToOne
    private Account AccountFrom;

    private Date date;

    private Time timestamp;

    @OneToOne
    private User UserPerforming;
}
