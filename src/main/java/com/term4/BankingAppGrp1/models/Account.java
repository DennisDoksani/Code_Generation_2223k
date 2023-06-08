package com.term4.BankingAppGrp1.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor // for seeding bank account
public class Account {
    @Id
    @GeneratedValue(generator = "IBANGenerator", strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "IBANGenerator", strategy = "com.term4.BankingAppGrp1.generators.IBANGenerator")
    private String iban;
    private double balance;
    @Builder.Default
    private LocalDate creationDate = LocalDate.now();
    private double absoluteLimit;
    @Builder.Default
    private boolean isActive = true;
    @Enumerated(EnumType.ORDINAL)
    private AccountType accountType;
    @ManyToOne
    private User customer;
    public void setBalance(double balance) {
        if (balance > 0)
            this.balance = balance;
        else
           throw new IllegalArgumentException("Balance can not be negative");
    }


}
