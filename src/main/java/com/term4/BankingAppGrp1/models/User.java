package com.term4.BankingAppGrp1.models;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
@Inheritance
public class User{

    @Id
    @GeneratedValue
    private long id;
    private int bsn;
    private String firstName;
    private String lastName;
    private String name;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String email;
    private String password;
    private boolean isActive;
    private double dayLimit = 0;
    private double transactionLimit = 0;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles;

    public User(int bsn, String firstName, String lastName, LocalDate dateOfBirth, String phoneNumber, String email, String password, boolean isActive, double dayLimit, double transactionLimit){
        this.bsn = bsn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.name = firstName + " " + lastName;
        this.isActive = isActive;
        this.dayLimit = dayLimit;
        this.transactionLimit = transactionLimit;
    }
}
