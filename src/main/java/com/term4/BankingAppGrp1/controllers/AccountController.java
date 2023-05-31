package com.term4.BankingAppGrp1.controllers;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.requestDTOs.AccountStatusDTO;
import com.term4.BankingAppGrp1.requestDTOs.CreatingAccountDTO;
import com.term4.BankingAppGrp1.requestDTOs.UpdatingDTO;
import com.term4.BankingAppGrp1.responseDTOs.AccountDTO;
import com.term4.BankingAppGrp1.responseDTOs.AccountHolderDTO;
import com.term4.BankingAppGrp1.responseDTOs.ErrorMessageDTO;
import com.term4.BankingAppGrp1.responseDTOs.SearchingAccountDTO;
import com.term4.BankingAppGrp1.services.AccountService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.naming.LimitExceededException;
import java.util.List;
import java.util.function.Function;

import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_LIMIT_STRING;
import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_OFFSET_STRING;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    private final Function<User, AccountHolderDTO> parseUserObjectToDTO = u ->
            new AccountHolderDTO(u.getId(),
                    u.getDayLimit(), u.getTransactionLimit()
                    , u.getFirstName(), u.getLastName());

    private final Function<Account, AccountDTO> parseAccountObjectToDTO = a ->
            new AccountDTO(a.getIban(), a.getBalance(), a.getAbsoluteLimit(), a.getCreationDate(), a.isActive(),
                    a.getAccountType(), parseUserObjectToDTO.apply(a.getCustomer()));

    private final Function<Account, SearchingAccountDTO> parseAccountObjectToSearchingDTO = a ->
            new SearchingAccountDTO(a.getCustomer().getName(), a.getIban());

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    //Get all accounts
    //TODO: Sort by Account Type
    @GetMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Object> getAllAccounts(@RequestParam(defaultValue = DEFAULT_LIMIT_STRING, required = false) int limit,
                                                 @RequestParam(defaultValue = DEFAULT_OFFSET_STRING, required = false) int offset,
                                                 @NotBlank @RequestParam(required = false) String accountType)
    //Spring boot is asking for a default value for limit and offset to be string
    {
        List<Account> accounts = accountService.getAllAccounts(limit, offset,
                accountType == null ? null : AccountType.valueOf(accountType.toUpperCase()));
        return ResponseEntity.ok(accounts.stream().map(parseAccountObjectToDTO).toList());
    }

    //Get Account by IBAN
    @GetMapping("/{iban}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'EMPLOYEE')")
    public ResponseEntity<Object> getAccountByIBAN(@NotBlank @PathVariable String iban) {
        return ResponseEntity.ok(parseAccountObjectToDTO.apply(accountService.getAccountByIBAN(iban)));
    }

    //Search for accounts by customer name
    @GetMapping("/searchByCustomerName")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'EMPLOYEE')")
    public ResponseEntity<Object> searchAccountByCustomerName(
            @NotEmpty(message = "cannot be left empty") @RequestParam String customerName,
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

    //Change accounts status
    @PostMapping(value = "/accountStatus/{iban}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Object> changeAccountStatus(@NotBlank @PathVariable String iban,
                                                      @Valid @NotBlank @RequestBody AccountStatusDTO accountStatusDTO) {
        accountService.changeAccountStatus(iban, accountStatusDTO.isActive());
        return ResponseEntity.noContent().build();
    }

    //Post new account
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Object> saveAccount(@Valid @RequestBody CreatingAccountDTO accountDTO) throws LimitExceededException {
        return ResponseEntity.status(HttpStatus.CREATED).body(parseAccountObjectToDTO.apply(
                accountService.saveAccount(accountDTO)));
        //TODO: Look out for Saving account and Absolute Limit
    }

    //Update account
    @PutMapping(value = "/{iban}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Object> updateAccount(@NotBlank @PathVariable String iban,
                                                @Valid @RequestBody UpdatingDTO accountDTO) {

        return ResponseEntity.ok(parseAccountObjectToDTO.apply(
                accountService.updateAccount(iban, accountDTO))); // parsing account object to DTO
    }

    //Get account by email
    @GetMapping("/user/{email}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'EMPLOYEE')")
    public ResponseEntity<Object> getAccountsByEmail(@NotBlank @PathVariable String email) {
        return ResponseEntity.ok(accountService.getAccountsByEmailAddress(email).stream().map(parseAccountObjectToDTO).toList());
    }
}
