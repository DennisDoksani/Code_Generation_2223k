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
import com.term4.BankingAppGrp1.responseDTOs.TransactionAccountDTO;
import com.term4.BankingAppGrp1.responseDTOs.TransactionResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_INHOLLAND_BANK_IBAN;


@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
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

    public List<TransactionResponseDTO> getTransactionsWithFilters(Pageable pageable, String ibanFrom, String ibanTo, Double amountMin, Double amountMax, LocalDate dateBefore, LocalDate dateAfter) {
        List<Transaction> transactions = transactionRepository.getTransactionsWithFilters(pageable, ibanFrom, ibanTo, amountMin, amountMax, dateBefore, dateAfter).getContent();
        List<TransactionResponseDTO> responseDTOS = new ArrayList<>();
        transactions.forEach(
                Transaction -> responseDTOS.add(mapTransactionToDto(Transaction))
        );
        return responseDTOS;
    }
    public List<TransactionResponseDTO> getTransactionsOffAccount(Pageable pageable, String iban) {
        List<Transaction> transactions = transactionRepository.getTransactionsOffAccount(pageable, iban).getContent();
        if(transactions.isEmpty())
            throw new EntityNotFoundException("No transactions found for account " + iban);

        List<TransactionResponseDTO> responseDTOS = new ArrayList<>();
        transactions.forEach(
                Transaction -> responseDTOS.add(mapTransactionToDto(Transaction))
        );

        return responseDTOS;
    }
    //This method checks if either the account to or account from belongs to a customer to prevent customers from accessing
    // someone else's transaction data
    public boolean accountBelongsToUser(String iban, String userName) {
        Account account = null;

        if(iban != null) {
            account = accountRepository.findById(iban)
                    .orElseThrow(() -> new EntityNotFoundException("Account " + iban + " not found"));
            return account.getCustomer().getEmail().equals(userName);
        }

        return false;
    }

    public TransactionResponseDTO addTransaction(TransactionDTO transactionDTO, User userPerforming) {
        Transaction newTransaction = transactionRepository.save(mapDtoToTransaction(transactionDTO, userPerforming));

        return mapTransactionToDto(newTransaction);
    }

    public Double getSumOfMoneyTransferred(String user, LocalDate date) {
        return transactionRepository.getSumOfMoneyTransferred(user, date, AccountType.CURRENT).orElse(0.0); //Ask if this orElse is redundant
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
        Account accountTo = accountRepository.findById(dto.accountTo())
                .orElseThrow(() -> new EntityNotFoundException("Account " + dto.accountTo() + " not found"));
        Account accountFrom = accountRepository.findById(dto.accountFrom())
                .orElseThrow(() -> new EntityNotFoundException("Account " + dto.accountFrom() + " not found"));
        
        //Amount must be a positive number
        if (dto.amount() <= 0)
            throw new IllegalArgumentException("Amount must be a positive number");

        if(!accountTo.isActive() || !accountFrom.isActive())
            throw new IllegalArgumentException("You can not transfer to or from an inactive account");
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
        if (accountTo.getAccountType() != AccountType.SAVINGS && dto.amount() + getSumOfMoneyTransferred(accountFrom.getCustomer().getEmail(), LocalDate.now()) > accountFrom.getCustomer().getDayLimit())
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

    private TransactionResponseDTO mapTransactionToDto(Transaction transaction){
        TransactionAccountDTO accountToDTO = new TransactionAccountDTO(
                transaction.getAccountTo().getIban(),
                transaction.getAccountTo().getAccountType(),
                transaction.getAccountTo().getCustomer().getFullName());

        TransactionAccountDTO accountFromDTO = new TransactionAccountDTO(
                transaction.getAccountFrom().getIban(),
                transaction.getAccountFrom().getAccountType(),
                transaction.getAccountFrom().getCustomer().getFullName());

        return new TransactionResponseDTO(
                transaction.getTransactionID(),
                transaction.getAmount(),
                accountFromDTO,
                accountToDTO,
                transaction.getDate(),
                transaction.getTimestamp(),
                transaction.getUserPerforming().getFullName());
    }

    // this method will execute the atm transaction
    @Transactional // making a atomic state
    public TransactionResponseDTO atmDeposit(ATMDepositDTO depositDTO, String userPerformingEmail) {
        User userPerforming = userService.getUserByEmail(userPerformingEmail);
        // when deposit is made, the money will be transferred from  inholland bank account
        TransactionDTO transactionDTO = new TransactionDTO(
                depositDTO.amount(),
                depositDTO.accountTo(),
                DEFAULT_INHOLLAND_BANK_IBAN
        );
        if (validTransaction(transactionDTO)) {
            TransactionResponseDTO transaction = addTransaction(transactionDTO, userPerforming);
            changeBalance(depositDTO.amount(), DEFAULT_INHOLLAND_BANK_IBAN, depositDTO.accountTo());
            return transaction;
        }
        return null;
    }

    // this method will execute the atm withdraw transaction
    @Transactional // making the method transactional so that if any error occurs, the transaction will be rolled back
    public TransactionResponseDTO atmWithdraw(ATMWithdrawDTO withdrawDTO, String userPerformingEmail) {
        // using service layer so that Null pointer exception is avoided
        User userPerforming = userService.getUserByEmail(userPerformingEmail);
        // when withdraw is made, the money will be transferred to  inholland bank account
        TransactionDTO transactionDTO = new TransactionDTO(
                withdrawDTO.amount(),
                DEFAULT_INHOLLAND_BANK_IBAN,
                withdrawDTO.accountFrom()
        );
        if (validTransaction(transactionDTO)) {
            TransactionResponseDTO transaction = addTransaction(transactionDTO, userPerforming);
            changeBalance(withdrawDTO.amount(), withdrawDTO.accountFrom(), DEFAULT_INHOLLAND_BANK_IBAN);
            return transaction;
        }
        return null;
    }
}