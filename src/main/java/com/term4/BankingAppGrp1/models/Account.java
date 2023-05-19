package com.term4.BankingAppGrp1.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(generator = "IBANGenerator", strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "IBANGenerator", strategy = "com.term4.BankingAppGrp1.generators.IBANGenerator")
    private String IBAN;
    @Column(columnDefinition = "double default 0.0")
    private double balance;
    @Column(columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private Date creationDate;
    @Column(columnDefinition = "double default 0.0")
    private double absoluteLimit;
    @Column(columnDefinition = "boolean default true")
    private boolean isActive;
    @Enumerated(EnumType.ORDINAL)
    private AccountType accountType;
}
