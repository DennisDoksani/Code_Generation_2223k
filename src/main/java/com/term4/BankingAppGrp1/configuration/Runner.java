package com.term4.BankingAppGrp1.configuration;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.services.AccountService;
import jakarta.transaction.Transactional;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements ApplicationRunner {
    private final AccountService service;

    public Runner(AccountService service) {
        this.service = service;
    }


    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        Account seedAccount = new Account(AccountType.CURRENT);
        service.SaveAccount(seedAccount);
    }
}
