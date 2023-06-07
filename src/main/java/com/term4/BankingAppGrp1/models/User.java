package com.term4.BankingAppGrp1.models;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "users")
@Inheritance

public class User{

    @Id
    @GeneratedValue
    private long id;
    @Column(unique = true)
    private String bsn;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    @Column(unique = true)
    private String email;
    private String password;
    private boolean isActive;
    private double dayLimit = 0;
    private double transactionLimit = 0;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles;



    public void setDayLimit(double dayLimit) {
        if (dayLimit > 0)
            this.dayLimit = dayLimit;
        else 
            this.dayLimit = 0;
    }

    public void setTransactionLimit(double transactionLimit) {
        if (transactionLimit > 0)
            this.transactionLimit = transactionLimit;
        else 
            this.transactionLimit = 0;
    }
    public String getFullName(){
        return firstName + " " + lastName;
    }
}
