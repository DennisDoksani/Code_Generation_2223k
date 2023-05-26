package com.term4.BankingAppGrp1.controllers;

import javax.naming.AuthenticationException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.term4.BankingAppGrp1.requestDTOs.LoginDTO;
import com.term4.BankingAppGrp1.responseDTOs.TokenDTO;
import com.term4.BankingAppGrp1.services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginDTO dto) throws AuthenticationException{ 
            return new TokenDTO(authService.login(dto.email(), dto.password()));
    }
}
