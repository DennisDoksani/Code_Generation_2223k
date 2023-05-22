package com.term4.BankingAppGrp1.controllers;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    // to get All accounts
    // Employee Role is Required
    @GetMapping
    public ResponseEntity<Object> getAllAccounts(){
        return ResponseEntity.ok(new Account());
    }
}
