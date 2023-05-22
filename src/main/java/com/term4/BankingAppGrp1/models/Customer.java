package com.term4.BankingAppGrp1.models;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Inheritance
public class Customer extends User{

    protected double dayLimit;
    protected double transactionLimit;

    public Customer(int bsn, String firstName, String lastName, LocalDate dateOfBirth, String phoneNumber, String email, String password, boolean isActive, double dayLimit, double transactionLimit){
        super(bsn, firstName, lastName, dateOfBirth, phoneNumber, email, password, isActive);
        this.dayLimit = dayLimit;
        this.transactionLimit = transactionLimit;
    }
}
