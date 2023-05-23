package com.term4.BankingAppGrp1.models;

import org.springframework.stereotype.Component;

@Component
public interface ConstantsContainer {
    public final int DEFAULT_LIMIT=50;
    public final int DEFAULT_OFFSET=0;

    public  final  String DEFAULT_LIMIT_STRING= "50";
    public  final String DEFAULT_OFFSET_STRING= "0";

}
