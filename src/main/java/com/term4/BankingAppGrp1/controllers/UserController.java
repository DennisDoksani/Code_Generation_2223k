package com.term4.BankingAppGrp1.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.term4.BankingAppGrp1.services.UserService;
import com.term4.BankingAppGrp1.requestDTOs.LoginRequestDTO;
import com.term4.BankingAppGrp1.responseDTOs.TokenDTO;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping
    public Object login(@RequestBody LoginRequestDTO dto) throws Exception {
            return new TokenDTO(
                    userService.login(dto.email(), dto.password())
            );
    }
}

