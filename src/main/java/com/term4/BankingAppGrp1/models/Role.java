package com.term4.BankingAppGrp1.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority{
    
    ROLE_EMPLOYEE,
    ROLE_CUSTOMER;

    @Override
    public String getAuthority() {
        return name();
    }
}



