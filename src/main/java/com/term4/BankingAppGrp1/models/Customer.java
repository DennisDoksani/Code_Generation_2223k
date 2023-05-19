package com.term4.BankingAppGrp1.models;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.annotation.Inherited;
import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class Customer extends User{

    public Customer(int bsn, String firstName, String lastName, LocalDate dateOfBirth, String phoneNumber,
                    String email, String password, double dayLimit, double transactionLimit) {
        super(bsn, firstName, lastName, dateOfBirth, phoneNumber, email, password);
        this.dayLimit = dayLimit;
        this.transactionLimit = transactionLimit;
    }
    protected double dayLimit;
    protected double transactionLimit;
}
