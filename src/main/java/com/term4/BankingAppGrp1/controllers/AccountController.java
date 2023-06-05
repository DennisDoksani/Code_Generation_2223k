package com.term4.BankingAppGrp1.controllers;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.models.Role;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.requestDTOs.AccountStatusDTO;
import com.term4.BankingAppGrp1.requestDTOs.CreatingAccountDTO;
import com.term4.BankingAppGrp1.requestDTOs.UpdatingAccountDTO;
import com.term4.BankingAppGrp1.responseDTOs.*;
import com.term4.BankingAppGrp1.services.AccountService;
import com.term4.BankingAppGrp1.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.naming.LimitExceededException;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_LIMIT_STRING;
import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_OFFSET_STRING;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Validated
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    private final UserService userService;

    private final Function<User, AccountHolderDTO> parseUserObjectToDTO = u ->
            new AccountHolderDTO(u.getId(),
                    u.getDayLimit(), u.getTransactionLimit()
                    , u.getFirstName(), u.getLastName()
            );

    private final Function<Account, AccountDTO> parseAccountObjectToDTO = a ->
            new AccountDTO(
                    a.getIban(), a.getBalance(), a.getAbsoluteLimit(), a.getCreationDate(), a.isActive(),
                    a.getAccountType(), parseUserObjectToDTO.apply(a.getCustomer())
            );

    private final Function<Account, SearchingAccountDTO> parseAccountObjectToSearchingDTO = a ->
            new SearchingAccountDTO(a.getCustomer().getFullName(), a.getIban());

    private final Function<Account, AccountWithoutAccountHolderDTO>
            parseAccountObjectToWithoutAccountHolderDTO = a ->
            new AccountWithoutAccountHolderDTO(
                    a.getIban(), a.getBalance(), a.getAbsoluteLimit(), a.getCreationDate(),
                    a.getAccountType()
            );

    private final BiFunction<List<Account>, User, UserAccountsDTO>
            parseListOfAccountAndUserObjectToUserAccountsDTO = (a, u) ->
            new UserAccountsDTO(parseUserObjectToDTO.apply(u),
                    a.stream().map(parseAccountObjectToWithoutAccountHolderDTO).toList());

    private final Predicate<GrantedAuthority> isEmployee =
            a -> a.getAuthority().equals(Role.ROLE_EMPLOYEE.name());


    public AccountController(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    //Get all accounts
    @GetMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Object> getAllAccounts(@RequestParam(defaultValue = DEFAULT_LIMIT_STRING, required = false) int limit,
                                                 @RequestParam(defaultValue = DEFAULT_OFFSET_STRING, required = false) int offset,
                                                 @RequestParam(required = false) String accountType)
    //Spring boot is asking for a default value for limit and offset to be string
    {
        List<Account> accounts = accountService.getAllAccounts(limit, offset,
                accountType == null ? null : AccountType.valueOf(accountType.toUpperCase()));
        return ResponseEntity.ok(accounts.stream().map(parseAccountObjectToDTO).toList());
    }

    //Get Account by IBAN
    @GetMapping("/{iban}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'EMPLOYEE')")
    public ResponseEntity<Object> getAccountByIBAN(@PathVariable String iban) {
        return ResponseEntity.ok(
                parseAccountObjectToDTO.apply(
                        accountService.getAccountByIBAN(iban))
        );
    }

    //Search for accounts by customer name
    @GetMapping("/searchByCustomerName")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'EMPLOYEE')")
    public ResponseEntity<Object> searchAccountByCustomerName(
            @NotBlank(message = "Customer name cannot be empty inorder to search") @RequestParam String customerName,
            @RequestParam(defaultValue = DEFAULT_LIMIT_STRING, required = false) int limit,
            @RequestParam(defaultValue = DEFAULT_OFFSET_STRING, required = false) int offset) {

        List<Account> accounts = accountService.searchAccountByCustomerName(customerName, limit, offset);
        if (accounts.isEmpty()) { // when no accounts are found by the input name
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorMessageDTO("No accounts found by this name " + customerName + "!")
            );
        }
        return ResponseEntity.ok(accounts.stream().map(parseAccountObjectToSearchingDTO).toList());
    }

    //Change accounts status by IBAN
    @PostMapping(value = "/accountStatus/{iban}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Object> changeAccountStatus(@NotBlank(message = "Iban must be required inorder to update the status of account")
                                                      @PathVariable String iban,
                                                      @Valid @RequestBody AccountStatusDTO accountStatusDTO) {
        accountService.changeAccountStatus(iban, accountStatusDTO.isActive());
        return ResponseEntity.noContent().build();
    }

    //Post new account
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Object> saveAccount(@Valid @RequestBody CreatingAccountDTO accountDTO) throws LimitExceededException {
        return ResponseEntity.status(HttpStatus.CREATED).body(parseAccountObjectToDTO.apply(
                accountService.saveAccount(accountDTO)));

    }

    //Update account
    // balance cannot be updated by this endpoint
    @PutMapping(value = "/{iban}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Object> updateAccount(@NotEmpty(message = "The updating account iban must be provided")
                                                @PathVariable String iban,
                                                @Valid @RequestBody UpdatingAccountDTO accountDTO) {

        return ResponseEntity.ok(parseAccountObjectToDTO.apply(
                accountService.updateAccount(iban, accountDTO))); // parsing account object to DTO
    }

    // this endpoint will access by both employee and customer
    // if the user is employee, he can access all accounts of any users
    // if the user is customer, he can access only his accounts by verifying with JWOT token
    // This endpoint will only return the active accounts only
    @GetMapping("/user/{email}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'CUSTOMER')")
    public ResponseEntity<Object> getAccountsOfUserByEmail(@Email(message = "The email provided is not a valid email address")
                                                           @PathVariable String email,
                                                           @AuthenticationPrincipal UserDetails jwtUser) {

        if (jwtUser.getAuthorities().stream().noneMatch(isEmployee)
                && !jwtUser.getUsername().equalsIgnoreCase(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorMessageDTO("You are not allowed to access others Accounts Details! "));
        }
        // TODO : add sum of balance of all accounts
        return ResponseEntity.ok(
                parseListOfAccountAndUserObjectToUserAccountsDTO.apply(
                        accountService.getAccountsByEmailAddress(email),
                        userService.getUserByEmail(email)
                )
        );
    }

}
