package com.term4.BankingAppGrp1.controllers;

import com.term4.BankingAppGrp1.services.AccountService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_LIMIT_STRING;
import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_OFFSET_STRING;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // to get All accounts
    // Employee Role is Required
    //TODO: Add Security & Sort by Account Type
    @GetMapping
    public ResponseEntity<Object> getAllAccounts(@RequestParam(defaultValue = DEFAULT_LIMIT_STRING, required = false) int limit,
                                                 @RequestParam(defaultValue = DEFAULT_OFFSET_STRING, required = false) int offset)
    // Spring boot is asking for a default value for limit and offset to be string
    {

        return ResponseEntity.ok(accountService.getAllAccounts(getPageable(limit, offset)));
    }

    // to get Account by IBAN
    @GetMapping("/{iban}")
    public ResponseEntity<Object> getAccountByIBAN(@NotBlank @PathVariable String iban) {
        return ResponseEntity.ok(accountService.getAccountByIBAN(iban));
    }

    // this  endpoint is to search for accounts by customer name and can be accessed by employee and admin
    @GetMapping("/searchByCustomerName")
    public ResponseEntity<Object> searchAccountByCustomerName( @NotBlank @RequestParam String customerName,
                                                               @RequestParam(defaultValue = DEFAULT_LIMIT_STRING, required = false) int limit,
                                                       @RequestParam(defaultValue = DEFAULT_OFFSET_STRING, required = false) int offset) {
        return ResponseEntity.ok(accountService.searchAccountByCustomerName(customerName, getPageable(limit, offset)));
    }

    private Pageable getPageable(int limit, int offset) {
        return PageRequest.of(offset / limit, limit); // offset/limit = page number
    }


}
