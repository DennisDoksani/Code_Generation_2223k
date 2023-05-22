package com.term4.BankingAppGrp1.configuration;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.models.Customer;
import com.term4.BankingAppGrp1.models.Transaction;
import com.term4.BankingAppGrp1.services.AccountService;
import com.term4.BankingAppGrp1.services.TransactionService;
import com.term4.BankingAppGrp1.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;

@Component
public class Runner implements ApplicationRunner {
    private final AccountService accountService;
    private final UserService userService;
    private TransactionService transactionService;

    public Runner(AccountService accountService, UserService userService, TransactionService transactionService) {

        this.accountService = accountService;
        this.userService = userService;
        this.transactionService = transactionService;
    }


    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        Customer joshMf= new Customer(234445,"Joshua","Mf", LocalDate.now(),"680000000000","josh@mf.com","josh",0,0);
        userService.saveUser(joshMf);
        Account seedAccount = new Account(AccountType.CURRENT,joshMf);
        Account seedSavings = new Account(AccountType.SAVINGS, joshMf);
        accountService.SaveAccount(seedAccount);
        accountService.SaveAccount(seedSavings);

        Transaction newTransaction = new Transaction(10.00, seedSavings, seedAccount, new Date(2023, 5, 22), new Time(21, 4, 0), joshMf);
        transactionService.addTransaction(newTransaction);
    }
}
