package com.term4.BankingAppGrp1.models;

import org.springframework.stereotype.Component;

@Component
public interface ConstantsContainer {

    public  final  String DEFAULT_LIMIT_STRING= "50";
    public  final String DEFAULT_OFFSET_STRING= "0";
    public final String DEFAULT_INHOLLAND_BANK_IBAN = "NL01INHO0000000001";

    public final int DEFAULT_CURRENT_ACCOUNT_LIMIT = 3; // TODO : change  limit for user Creating Account
    public final int DEFAULT_SAVINGS_ACCOUNT_LIMIT = 1;

}
