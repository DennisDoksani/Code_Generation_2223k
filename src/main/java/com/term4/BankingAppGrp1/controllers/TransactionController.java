package com.term4.BankingAppGrp1.controllers;

import com.term4.BankingAppGrp1.models.Transaction;
import com.term4.BankingAppGrp1.responseDTOs.TransactionDTO;
import com.term4.BankingAppGrp1.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_LIMIT_STRING;
import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_OFFSET_STRING;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

//    @GetMapping
//    public ResponseEntity<Object> getAllTransactions() {
//        return ResponseEntity.ok().body(transactionService.getAllTransactions());
//    }

    @GetMapping()
    public ResponseEntity<Object> getTransactionsWithFilters(@RequestParam(defaultValue = DEFAULT_LIMIT_STRING, required = false) int limit,
                                                             @RequestParam(defaultValue = DEFAULT_OFFSET_STRING, required = false) int offset,
                                                             @RequestParam(required = false) String ibanFrom,
                                                             @RequestParam(required = false) String ibanTo,
                                                             @RequestParam(required = false) Double amountMin,
                                                             @RequestParam(required = false) Double amountMax,
                                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateBefore,
                                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateAfter ) {
        return ResponseEntity.ok().body(transactionService.getTransactionsWithFilters(getPageable(limit, offset), ibanFrom, ibanTo, amountMin, amountMax, dateBefore, dateAfter));
    }

    @PostMapping
    public ResponseEntity<Object> addTransaction(@RequestBody @Valid TransactionDTO transactionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.addTransaction(transactionDTO));
    }

    private Pageable getPageable(int limit, int offset) {
        return PageRequest.of(offset / limit, limit); // offset/limit = page number
    }

}
