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
    private long transactionID;

    public Transaction(Double amount, String accountTo, String accountFrom, Date date, Time timestamp, User userPerforming) {
        this.amount = amount;
        this.accountTo = accountTo;
        this.accountFrom = accountFrom;
        this.date = date;
        this.timestamp = timestamp;
        this.userPerforming = userPerforming;
    }

    private Double amount;

    private String accountTo;

    private String accountFrom;

    private Date date;

    private Time timestamp;

    @OneToOne
    private User userPerforming;
}
