package com.term4.BankingAppGrp1.configuration;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.models.Role;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.responseDTOs.TransactionDTO;
import com.term4.BankingAppGrp1.services.AccountService;
import com.term4.BankingAppGrp1.services.TransactionService;
import com.term4.BankingAppGrp1.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_INHOLLAND_BANK_IBAN;

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
        User joshMf = User.builder()
                .bsn(234445)
                .firstName("Joshua")
                .lastName("Mf")
                .dateOfBirth(LocalDate.now())
                .phoneNumber("0611111111")
                .email("josh@mf.com")
                .password("josh")
                .isActive(true)
                .roles(List.of(Role.ROLE_EMPLOYEE, Role.ROLE_CUSTOMER))
                .build();
        userService.saveUser(joshMf);

        User ruubio = User.builder()
                .bsn(123456)
                .firstName("Ruubyo")
                .lastName("Gaming")
                .dateOfBirth(LocalDate.of(2003, 10, 1))
                .phoneNumber("0611111121")
                .email("Ruubyo@isgaming.com")
                .password("secretword")
                .isActive(true)
                .roles(List.of(Role.ROLE_EMPLOYEE, Role.ROLE_CUSTOMER))
                .build();
        userService.saveUser(ruubio);

        for (int i = 0; i < 10; i++) {
            Account seedAccount = new Account(AccountType.CURRENT, joshMf);
            accountService.saveAccount(seedAccount);
        }

        TransactionDTO newTransaction = new TransactionDTO(10.00, "NL01INHO0000000003", "NL01INHO0000000002", ruubio.getId());

        transactionService.addTransaction(newTransaction);
        seedBankAccount();
        makeDummyBankaccounts(ruubio, joshMf);
    }

    private void seedBankAccount() {
        User inhollandBank = User.builder()
                .bsn(111111)
                .firstName("Inholland")
                .lastName("Bank")
                .dateOfBirth(LocalDate.now())
                .phoneNumber("680000000000")
                .email("inholland@bank.nl")
                .password("Inholland")
                .isActive(true)
                .transactionLimit(9999999999999999L)
                .dayLimit(9999999999999999L)
                .roles(List.of(Role.ROLE_EMPLOYEE))
                .build();


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
        Account seedHardcodedIban = new Account("NL72INHO0579629781",
                900, LocalDate.now(), 900, true, AccountType.CURRENT, joshMf);
        accountService.saveAccount(seedAccount);
        accountService.saveAccount(seedHardcodedIban);
        accountService.saveAccount(savings);
        accountService.saveAccount(current);
        accountService.saveAccount(seedSavings);
    }
}
