package com.term4.BankingAppGrp1.services;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.models.Transaction;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.repositories.AccountRepository;
import com.term4.BankingAppGrp1.repositories.TransactionRepository;
import com.term4.BankingAppGrp1.repositories.UserRepository;
import com.term4.BankingAppGrp1.requestDTOs.ATMDepositDTO;
import com.term4.BankingAppGrp1.requestDTOs.ATMWithdrawDTO;
import com.term4.BankingAppGrp1.requestDTOs.TransactionDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_INHOLLAND_BANK_IBAN;


@Service
public class TransactionService {
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private final UserService userService;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, UserRepository userRepository, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsWithFilters(Pageable pageable, String ibanFrom, String ibanTo, Double amountMin, Double amountMax, LocalDate dateBefore, LocalDate dateAfter) {
        List<Transaction> transactions = transactionRepository.getTransactionsWithFilters(pageable, ibanFrom, ibanTo, amountMin, amountMax, dateBefore, dateAfter).getContent();
        return transactions;
    }

    public Transaction addTransaction(TransactionDTO transactionDTO, User userPerforming) {
        return transactionRepository.save(mapDtoToTransaction(transactionDTO, userPerforming));
    }

    public Double getSumOfMoneyTransferred(String iban, LocalDate date) {
        return transactionRepository.getSumOfMoneyTransferred(iban, date).orElse(0.0); //Ask if this orElse is redundant
    }

    public void changeBalance(double amount, String accountFrom, String accountTo) {
        decreaseBalanceByAmount(amount, accountFrom);
        increaseBalanceByAmount(amount, accountTo);
    }

    public void decreaseBalanceByAmount(double amount, String accountFrom) {
        transactionRepository.decreaseBalanceByAmount(amount, accountFrom);
    }

    public void increaseBalanceByAmount(double amount, String accountFrom) {
        transactionRepository.increaseBalanceByAmount(amount, accountFrom);
    }

    public Boolean validTransaction(TransactionDTO dto) {
        Account accountTo = accountRepository.findById(dto.accountTo()).get();
        Account accountFrom = accountRepository.findById(dto.accountFrom()).get();

        //This statement checks if money is being transferred to or from a savings account that does not belong to the same user
        if (((accountFrom.getAccountType() == AccountType.CURRENT && accountTo.getAccountType() == AccountType.SAVINGS) || (accountFrom.getAccountType() == AccountType.SAVINGS && accountTo.getAccountType() == AccountType.CURRENT)) && accountFrom.getCustomer().getBsn() != accountTo.getCustomer().getBsn())
            throw new IllegalArgumentException("You can not transfer money from or to a savings account that does not belong to the same user");

        if (accountFrom.getBalance() - dto.amount() < accountFrom.getAbsoluteLimit())
            throw new IllegalArgumentException("The amount you are trying to transfer will result in a balance lower than the absolute limit for this account");
        else if (dto.amount() > accountFrom.getBalance()) {
            //Implement some kind of warning for the user
        }

        if (dto.amount() > accountFrom.getCustomer().getTransactionLimit())
            throw new IllegalArgumentException("The amount you are trying to transfer exceeds the transaction limit");
        // Checks first if the accountFrom has transactions made today and then if the amount will exceed the day limit
        if (getSumOfMoneyTransferred(accountFrom.getIban(), LocalDate.now()) > 0.0 && dto.amount() + getSumOfMoneyTransferred(accountFrom.getIban(), LocalDate.now()) > accountFrom.getCustomer().getDayLimit())
            throw new IllegalArgumentException("The amount you are trying to transfer will exceed the day limit for this account");


        return true;
    }

    private Transaction mapDtoToTransaction(TransactionDTO dto, User userPerforming) {
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(accountRepository.findById(dto.accountFrom()).get());
        transaction.setAccountTo(accountRepository.findById(dto.accountTo()).get());
        transaction.setAmount(dto.amount());
        transaction.setDate(LocalDate.now());
        transaction.setTimestamp(LocalTime.now());
        transaction.setUserPerforming(userPerforming);
        return transaction;
    }

    // this method will execute the atm transaction
    @Transactional // making a atomic state
    public Transaction atmDeposit(ATMDepositDTO depositDTO, String userPerformingEmail) {
        User userPerforming = userService.getUserByEmail(userPerformingEmail);
        // when deposit is made, the money will be transferred from  inholland bank account
        TransactionDTO transactionDTO = new TransactionDTO(
                depositDTO.amount(),
                depositDTO.accountTo(),
                DEFAULT_INHOLLAND_BANK_IBAN
        );
        if (validTransaction(transactionDTO)) {
            Transaction transaction = addTransaction(transactionDTO, userPerforming);
            changeBalance(depositDTO.amount(), DEFAULT_INHOLLAND_BANK_IBAN, depositDTO.accountTo());
            return transaction;
        }
        return null;
    }

    // this method will execute the atm withdraw transaction
    @Transactional // making the method transactional so that if any error occurs, the transaction will be rolled back
    public Transaction atmWithdraw(ATMWithdrawDTO withdrawDTO, String userPerformingEmail) {
        // using service layer so that Null pointer exception is avoided
        User userPerforming = userService.getUserByEmail(userPerformingEmail);
        // when withdraw is made, the money will be transferred to  inholland bank account
        TransactionDTO transactionDTO = new TransactionDTO(
                withdrawDTO.amount(),
                DEFAULT_INHOLLAND_BANK_IBAN,
                withdrawDTO.accountFrom()
        );
        if (validTransaction(transactionDTO)) {
            Transaction transaction = addTransaction(transactionDTO, userPerforming);
            changeBalance(withdrawDTO.amount(), withdrawDTO.accountFrom(), DEFAULT_INHOLLAND_BANK_IBAN);
            return transaction;
        }
        return null;
    }
}