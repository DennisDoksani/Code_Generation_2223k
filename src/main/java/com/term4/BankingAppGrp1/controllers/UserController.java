package com.term4.BankingAppGrp1.controllers;

import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.requestDTOs.UserUpdateDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.term4.BankingAppGrp1.services.UserService;
import com.term4.BankingAppGrp1.requestDTOs.RegistrationDTO;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userService.deleteUser(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody UserUpdateDTO userUpdateDTO) {
        try {
            User updatedUser = userService.updateUser(userUpdateDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> saveUser(@RequestBody RegistrationDTO user){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(user));
    }
}

