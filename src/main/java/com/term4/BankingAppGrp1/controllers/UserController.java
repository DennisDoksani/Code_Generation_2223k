package com.term4.BankingAppGrp1.controllers;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.responseDTOs.UserDTO;
import com.term4.BankingAppGrp1.responseDTOs.AccountHolderDTO;
import com.term4.BankingAppGrp1.responseDTOs.UserDTO;
import com.term4.BankingAppGrp1.services.AccountService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.term4.BankingAppGrp1.services.UserService;
import com.term4.BankingAppGrp1.requestDTOs.RegistrationDTO;

import java.util.List;
import java.util.function.Function;



@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService userService;
    private final AccountService accountService;

    private final Function<User, UserDTO> parseUserObjectToDTO = u ->
            new UserDTO(u.getId(), u.getBsn(), u.getFirstName(), u.getLastName(), u.getDateOfBirth(), u.getPhoneNumber(),
                    u.getEmail(), u.isActive(), u.getDayLimit(), u.getTransactionLimit());

    public UserController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<String> deleteUser(@PathVariable long id){
        List<Account> userAccounts = accountService.getAccountsByUserId(id);
        if (userAccounts.isEmpty()){
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User has active bank accounts and cannot be deleted.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<User> updateUser(@RequestBody UserDTO userDTO) {
        User updatedUser = userService.updateUser(userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users.stream().map(parseUserObjectToDTO).toList());
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> saveUser(@Valid @RequestBody RegistrationDTO user){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(user));
    }
}

