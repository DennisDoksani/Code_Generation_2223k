package com.term4.BankingAppGrp1.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.term4.BankingAppGrp1.requestDTOs.LoginRequestDTO;
import com.term4.BankingAppGrp1.responseDTOs.TokenDTO;

@RestController
@RequestMapping("/users")
public class AuthController {
    
    
    @PostMapping("/auth/login")
    public Object login(@RequestBody LoginRequestDTO dto) throws Exception {
            return new TokenDTO(userService.login(dto.email(), dto.password())
            );
    }
}
