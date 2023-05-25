package com.term4.BankingAppGrp1.configuration;

import com.term4.BankingAppGrp1.models.*;
import com.term4.BankingAppGrp1.services.*;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @DeleteMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable User id){
        return userService.deleteUser(id);
    }

    @GetMapping("/users/getone/{id}")
        public ResponseEntity<User> getUser(@PathVariable User id){
            Optional<User> user = userService.getUser(id);
            return user.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        User joshMf= new User(234445,"Joshua","Mf", LocalDate.now(),"680000000000","josh@mf.com","josh",true, 0,0);
        userService.saveUser(joshMf);
        for (int i = 0; i < 800; i++) {
            Account seedAccount = new Account(AccountType.CURRENT,joshMf);
            accountService.saveAccount(seedAccount);
        }
        Account seedAccount = new Account(AccountType.CURRENT,joshMf);
        Account seedSavings = new Account(AccountType.SAVINGS, joshMf);
        accountService.saveAccount(seedAccount);
        accountService.saveAccount(seedSavings);

        Transaction newTransaction = new Transaction(10.00, seedSavings.getIban(), seedAccount.getIban(), LocalDate.now(), new Time(21, 4, 0), joshMf);
        transactionService.addTransaction(newTransaction);
    }

    private void seedBankAccount(){
        User inhollandBank= new User(111111,"Inholland","Bank", LocalDate.now(),
                "680000000000", "inholland@bank.nl","Inholland", true,
                9999999999999999L,9999999999999999L);
        userService.saveUser(inhollandBank);
        Account seedAccount = new Account("NL01INHO0000000001",9999999999999.0,LocalDate.now(),0,true,AccountType.CURRENT,inhollandBank);
        accountService.saveAccount(seedAccount);
    }
}
