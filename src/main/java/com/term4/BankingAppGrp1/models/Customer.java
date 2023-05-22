package com.term4.BankingAppGrp1.models;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.lang.annotation.Inherited;
import java.time.LocalDate;
import java.util.Date;
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Customer extends User{

    protected double dayLimit;
    protected double transactionLimit;
}
