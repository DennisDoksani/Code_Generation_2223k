package com.term4.BankingAppGrp1.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@Entity(name = "users")
@Inheritance
public abstract class User {

    @Id
    @GeneratedValue
    protected int id;
    protected int bsn;
    protected String firstName;
    protected String lastName;
    protected LocalDate dateOfBirth;
    protected String phoneNumber;
    protected String email;
    protected String password;
    protected boolean isActive=true;
    protected String name ;

    protected User(int bsn, String firstName, String lastName, LocalDate dateOfBirth, String phoneNumber, String email, String password) {
        this.bsn = bsn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.name = firstName + " " + lastName;
    }
}
