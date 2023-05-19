package com.term4.BankingAppGrp1.configuration;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.models.Customer;
import com.term4.BankingAppGrp1.services.AccountService;
import com.term4.BankingAppGrp1.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Runner implements ApplicationRunner {
    private final AccountService accountService;
    private final UserService userService;

    public Runner(AccountService accountService, UserService userService) {

        this.accountService = accountService;
        this.userService = userService;
    }


    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        Customer joshMf= new Customer(234445,"Joshua","Mf", LocalDate.now(),"680000000000","josh@mf.com","josh",0,0);
        userService.saveUser(joshMf);
        Account seedAccount = new Account(AccountType.CURRENT,joshMf);
        accountService.SaveAccount(seedAccount);
    }
}
