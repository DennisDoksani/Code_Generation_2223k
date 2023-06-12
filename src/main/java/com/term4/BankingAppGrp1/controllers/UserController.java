package com.term4.BankingAppGrp1.controllers;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.requestDTOs.RegistrationDTO;
import com.term4.BankingAppGrp1.responseDTOs.UserDTO;
import com.term4.BankingAppGrp1.services.AccountService;
import com.term4.BankingAppGrp1.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.function.Function;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

  private final UserService userService;
  private final AccountService accountService;

  private final Function<User, UserDTO> parseUserObjectToDTO = u ->
      new UserDTO(u.getId(), u.getBsn(), u.getFirstName(), u.getLastName(), u.getDateOfBirth(),
          u.getPhoneNumber(),
          u.getEmail(), u.isActive(), u.getDayLimit(), u.getTransactionLimit());

  public UserController(UserService userService, AccountService accountService) {
    this.userService = userService;
    this.accountService = accountService;
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<String> deleteUser(@PathVariable long id) {
    List<Account> userAccounts = accountService.getAccountsByUserId(id);
    if (userAccounts.isEmpty()) {
      userService.deleteUser(id);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully!");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("User has active bank accounts and cannot be deleted.");
    }
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<UserDTO> getUser(@PathVariable long id) {
    User user = userService.getUser(id);
    UserDTO userDTO = parseUserObjectToDTO.apply(user);
    return ResponseEntity.ok(userDTO);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<User> updateUser(@RequestBody UserDTO userDTO) {
    User updatedUser = userService.updateUser(userDTO);
    return ResponseEntity.ok(updatedUser);
  }

  @GetMapping
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<Object> getAllUsers() {
    List<User> users = userService.getAllUsers();
    return ResponseEntity.ok(users.stream().map(parseUserObjectToDTO).toList());
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<User> saveUser(@NotNull @Valid @RequestBody RegistrationDTO user) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(user));
  }
}

