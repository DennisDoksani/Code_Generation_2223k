package com.term4.BankingAppGrp1.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority{
    
    ROLE_NEWUSER,
    ROLE_CUSTOMER, 
    ROLE_EMPLOYEE;

    @Override
    public String getAuthority() {
        return name();
    }
}



