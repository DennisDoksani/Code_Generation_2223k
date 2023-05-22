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
    @ElementCollection(fetch = FetchType.EAGER)
    protected List<Role> roles;

    public User(int bsn, String firstName, String lastName, LocalDate dateOfBirth, String phoneNumber, String email, String password, boolean isActive){
        this.bsn = bsn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.roles = List.of(Role.getRole(this));
    }
}
