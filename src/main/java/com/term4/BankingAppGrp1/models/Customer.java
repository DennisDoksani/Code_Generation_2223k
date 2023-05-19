package com.term4.BankingAppGrp1.models;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.annotation.Inherited;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class Customer extends User{

    public Customer(int id, int bsn, String firstName, String lastName, Date dateOfBirth, String phoneNumber, String email, String password, boolean isActive, double dayLimit, double transactionLimit) {
        super(id, bsn, firstName, lastName, dateOfBirth, phoneNumber, email, password, isActive);
        this.dayLimit = dayLimit;
        this.transactionLimit = transactionLimit;
    }

    protected double dayLimit;
    protected double transactionLimit;


}
