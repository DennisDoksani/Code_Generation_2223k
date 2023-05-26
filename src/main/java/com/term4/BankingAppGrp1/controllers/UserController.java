package com.term4.BankingAppGrp1.controllers;

import com.term4.BankingAppGrp1.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.term4.BankingAppGrp1.services.UserService;
import com.term4.BankingAppGrp1.requestDTOs.RegistrationDTO;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userService.deleteUser(id));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping("/users")
    public ResponseEntity<User> saveUser(@RequestBody RegistrationDTO user){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(user));
    }
}

