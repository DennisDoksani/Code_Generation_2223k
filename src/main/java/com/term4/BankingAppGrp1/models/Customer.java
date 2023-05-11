package com.term4.BankingAppGrp1.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data

public class Customer extends User{

    public Customer(int Id, int bsn, String firstName, String lastName, Date dateOfBirth, String phoneNumber, String email, String password, boolean isActive, double dayLimit, double transactionLimit) {
        super(Id, bsn, firstName, lastName, dateOfBirth, phoneNumber, email, password, isActive);
        this.dayLimit = dayLimit;
        this.transactionLimit = transactionLimit;
    }

    protected double dayLimit;
    protected double transactionLimit;


}
