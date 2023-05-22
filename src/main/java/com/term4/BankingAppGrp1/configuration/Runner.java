package com.term4.BankingAppGrp1.configuration;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.models.Customer;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.services.AccountService;
import com.term4.BankingAppGrp1.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class Runner implements ApplicationRunner {
    private final AccountService accountService;
    private final UserService userService;

    @Autowired
    public Runner(AccountService accountService, UserService userService) {

        this.accountService = accountService;
        this.userService = userService;
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

        Customer joshMf= new Customer(234445,"Joshua","Mf", LocalDate.now(),
                "680000000000","josh@mf.com","josh",0,0);

        userService.saveUser(joshMf);
//        for (int i = 0; i < 800; i++) {
//            Account seedAccount = new Account(AccountType.CURRENT,joshMf);
//            accountService.saveAccount(seedAccount);
//        }
        Account seedAccount = new Account(AccountType.CURRENT,joshMf);
        accountService.saveAccount(seedAccount);
        seedBankAccount();
    }
    private void seedBankAccount(){
        Customer inhollandBank= new Customer(111111,"Inholland","Bank", LocalDate.now(),
                "680000000000", "inholland@bank.nl","Inholland",
                9999999999999999L,9999999999999999L);
        userService.saveUser(inhollandBank);
        Account seedAccount = new Account("NL01INHO0000000001",9999999999999.0,LocalDate.now(),0,true,AccountType.CURRENT,inhollandBank);
        accountService.saveAccount(seedAccount);
    }
}
