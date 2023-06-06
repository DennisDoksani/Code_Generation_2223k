package com.term4.BankingAppGrp1.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor // for seeding bank account
public class Account {
    @Id
    @GeneratedValue(generator = "IBANGenerator", strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "IBANGenerator", strategy = "com.term4.BankingAppGrp1.generators.IBANGenerator")
    private String iban;
    //@Column(columnDefinition = "double default 20.00")
    private double balance;
    //@Column(columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private LocalDate creationDate = LocalDate.now();
    //@Column(columnDefinition = "double default 0.0")
    private double absoluteLimit;
    //@Column(columnDefinition = "boolean DEFAULT TRUE")
    private boolean isActive = true;
    @Enumerated(EnumType.ORDINAL)
    private AccountType accountType;
    @ManyToOne
    private User customer;

    public Account(AccountType accountType, User customer) {
        this.accountType = accountType;
        this.customer = customer;
    }

}
