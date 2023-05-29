package com.term4.BankingAppGrp1.services;

import com.term4.BankingAppGrp1.models.Transaction;
import com.term4.BankingAppGrp1.repositories.TransactionRepository;
import com.term4.BankingAppGrp1.requestDTOs.DepositDTO;
import com.term4.BankingAppGrp1.requestDTOs.WithdrawDTO;
import com.term4.BankingAppGrp1.responseDTOs.TransactionDTO;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_INHOLLAND_BANK_IBAN;


@Service
public class TransactionService {
    private TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getAllTransactions() {
        return (List<Transaction>) transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsWithFilters(Pageable pageable, String ibanFrom, String ibanTo, Double amountMin, Double amountMax, LocalDate dateBefore, LocalDate dateAfter) {
        return transactionRepository.getTransactionsWithFilters(pageable, ibanFrom, ibanTo, amountMin, amountMax, dateBefore, dateAfter).getContent();
    }
    public Transaction addTransaction(TransactionDTO transactionDTO) { return transactionRepository.save(mapDtoToTransaction(transactionDTO)); }

    private Transaction mapDtoToTransaction(TransactionDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(dto.accountFrom());
        transaction.setAccountTo(dto.accountTo());
        transaction.setAmount(dto.amount());
        transaction.setDate(LocalDate.now());
        transaction.setTimestamp(LocalTime.now());
        transaction.setUserPerforming(dto.userPerforming());
        return transaction;
    }

    public Transaction atmDeposit(DepositDTO depositDTO, Long userId){
        return addTransaction(new TransactionDTO(depositDTO.amount(), depositDTO.accountTo(), DEFAULT_INHOLLAND_BANK_IBAN, userId));
    }

    public Transaction atmWithdraw(WithdrawDTO withdrawDTO, Long userId){
        return addTransaction(new TransactionDTO(withdrawDTO.amount(), withdrawDTO.accountFrom(), DEFAULT_INHOLLAND_BANK_IBAN, userId));
    }

}