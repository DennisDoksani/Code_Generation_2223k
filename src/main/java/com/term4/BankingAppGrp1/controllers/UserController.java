package com.term4.BankingAppGrp1.controllers;

import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.requestDTOs.UserUpdateDTO;
import com.term4.BankingAppGrp1.responseDTOs.AccountHolderDTO;
import com.term4.BankingAppGrp1.responseDTOs.UserDTO;
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
public class UserController {

    private final UserService userService;

    private final Function<User, UserDTO> parseUserObjectToDTO = u ->
            new UserDTO(u.getId(), u.getBsn(), u.getFirstName(), u.getLastName(),
                     u.getDateOfBirth(), u.getEmail(), u.getPhoneNumber(), u.getRoles());

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<String> deleteUser(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userService.deleteUser(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<User> updateUser(@RequestBody UserUpdateDTO userUpdateDTO) {
        User updatedUser = userService.updateUser(userUpdateDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users.stream().map(parseUserObjectToDTO).toList());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> saveUser(@RequestBody RegistrationDTO user) {
        //@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
       // public ResponseEntity<User> saveUser (@Valid @RequestBody RegistrationDTO user){
        //    return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(user));
        return null;
        }
}

