package com.term4.BankingAppGrp1.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
@Inheritance
public abstract class User{

    @Id
    @GeneratedValue
    protected long id;
    protected int bsn;
    protected String firstName;
    protected String lastName;
    protected LocalDate dateOfBirth;
    protected String phoneNumber;
    protected String email;
    protected String password;
    protected boolean isActive;
}
