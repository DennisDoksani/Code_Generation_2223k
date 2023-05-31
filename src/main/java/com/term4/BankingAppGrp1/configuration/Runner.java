package com.term4.BankingAppGrp1.configuration;

import com.term4.BankingAppGrp1.models.*;
import com.term4.BankingAppGrp1.services.*;
import com.term4.BankingAppGrp1.responseDTOs.TransactionDTO;
import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_INHOLLAND_BANK_IBAN;

import jakarta.transaction.Transactional;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class Runner implements ApplicationRunner {
    private final AccountService accountService;
    private final UserService userService;
    private final TransactionService transactionService;

    public Runner(AccountService accountService, UserService userService, TransactionService transactionService) {

        this.accountService = accountService;
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        User joshMf = new User(234445, "Joshua", "Mf", LocalDate.now(), "680000000000", "josh@mf.com", "josh", 
        true, 0, 0, List.of(Role.ROLE_CUSTOMER, Role.ROLE_EMPLOYEE));
        userService.saveUser(joshMf);

        User ruubio= new User(123456, "Ruubyo", "Gaming", LocalDate.of(2003, 10, 1), "0611111121", "Ruubyo@isgaming.com", "secretword", 
        true, 500, 300, List.of(Role.ROLE_CUSTOMER));
        userService.saveUser(ruubio);

        for (int i = 0; i < 800; i++) {
            Account seedAccount = new Account(AccountType.CURRENT, joshMf);
            accountService.saveAccount(seedAccount);
        }

        TransactionDTO newTransaction = new TransactionDTO(10.00, "NL01INHO0000000003", "NL01INHO0000000002", ruubio.getId());

        transactionService.addTransaction(newTransaction);
        seedBankAccount();
        makeDummyBankaccounts(ruubio, joshMf);
    }

    private void seedBankAccount(){
        User inhollandBank= new User(111111,"Inholland","Bank", LocalDate.now(),
                "680000000000", "inholland@bank.nl","Inholland", true,
                9999999999999999L,9999999999999999L, List.of(Role.ROLE_EMPLOYEE));

        userService.saveUser(inhollandBank);
        Account seedAccount = new Account(DEFAULT_INHOLLAND_BANK_IBAN, 9999999999999.0, LocalDate.now(), 0, true, AccountType.CURRENT, inhollandBank);
        accountService.saveAccount(seedAccount);
    }
  
    private void makeDummyBankaccounts(User user, User joshMf) {
        Account savings = new Account(AccountType.SAVINGS, user);
        Account current = new Account(AccountType.CURRENT, user);
        current.setIban("NL01INHO0000000002");
        savings.setIban("NL01INHO0000000003");
        Account seedAccount = new Account(AccountType.CURRENT, joshMf);
        Account seedSavings = new Account(AccountType.SAVINGS, joshMf);
        Account seedHardcodedIban= new Account("NL72INHO0579629781",
                900,LocalDate.now(),900,true,AccountType.CURRENT,joshMf);
        accountService.saveAccount(seedAccount);
        accountService.saveAccount(seedHardcodedIban);
        accountService.saveAccount(savings);
        accountService.saveAccount(current);
        accountService.saveAccount(seedSavings);
    }
}
