package com.term4.BankingAppGrp1.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority{
    
    ROLE_EMPLOYEE,
    ROLE_CUSTOMER;

    @Override
    public String getAuthority() {
        return name();
    }

    public static Role getRole(Object object) {
        switch (object.getClass().getSimpleName()) {
            case "Employee":
                return ROLE_EMPLOYEE;
            case "Customer":
                return ROLE_CUSTOMER;
            default:
                throw new IllegalArgumentException("Invalid object type");
        }
    }
}



