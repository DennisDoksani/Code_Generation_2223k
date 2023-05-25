package com.term4.BankingAppGrp1.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.term4.BankingAppGrp1.requestDTOs.LoginRequestDTO;
import com.term4.BankingAppGrp1.responseDTOs.TokenDTO;
import com.term4.BankingAppGrp1.services.AuthService;

@RestController
@RequestMapping("/users")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/auth/login")
    public Object login(@RequestBody LoginRequestDTO dto) {
            return new TokenDTO(authService.login(dto.email(), dto.password()));
            
    }
}
