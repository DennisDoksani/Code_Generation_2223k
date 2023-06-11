package com.term4.BankingAppGrp1.controllers;

import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_LIMIT_STRING;
import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_OFFSET_STRING;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.models.Role;
import com.term4.BankingAppGrp1.models.TriFunction;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.models.customValidators.InhollandIBANPattern;
import com.term4.BankingAppGrp1.requestDTOs.AccountStatusDTO;
import com.term4.BankingAppGrp1.requestDTOs.CreatingAccountDTO;
import com.term4.BankingAppGrp1.requestDTOs.UpdatingAccountDTO;
import com.term4.BankingAppGrp1.responseDTOs.AccountDTO;
import com.term4.BankingAppGrp1.responseDTOs.AccountHolderDTO;
import com.term4.BankingAppGrp1.responseDTOs.AccountWithoutAccountHolderDTO;
import com.term4.BankingAppGrp1.responseDTOs.ErrorMessageDTO;
import com.term4.BankingAppGrp1.responseDTOs.SearchingAccountDTO;
import com.term4.BankingAppGrp1.responseDTOs.UserAccountsDTO;
import com.term4.BankingAppGrp1.services.AccountService;
import com.term4.BankingAppGrp1.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.naming.LimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Validated
@RequestMapping("/accounts")
public class AccountController {

  private final AccountService accountService;
  private final UserService userService;

  private final Function<User, AccountHolderDTO> mapUserObjectToDTO = u ->
      new AccountHolderDTO(u.getId(),
          u.getDayLimit(), u.getTransactionLimit()
          , u.getFirstName(), u.getLastName()
      );

  private final Function<Account, AccountDTO> mapAccountObjectToDTO = a ->
      new AccountDTO(
          a.getIban(), a.getBalance(), a.getAbsoluteLimit(), a.getCreationDate(), a.isActive(),
          a.getAccountType(), mapUserObjectToDTO.apply(a.getCustomer())
      );

  private final Function<Account, SearchingAccountDTO> mapAccountObjectToSearchingDTO = a ->
      new SearchingAccountDTO(a.getCustomer().getFullName(), a.getIban());

  private final Function<Account, AccountWithoutAccountHolderDTO>
      mapAccountObjectToWithoutAccountHolderDTO = a ->
      new AccountWithoutAccountHolderDTO(
          a.getIban(), a.getBalance(), a.getAbsoluteLimit(), a.getCreationDate(),
          a.getAccountType()
      );

  private final TriFunction<List<Account>, User, Double, UserAccountsDTO>
      parseListOfAccountAndUserObjectToUserAccountsDTO = (a, u, v) ->
      new UserAccountsDTO(mapUserObjectToDTO.apply(u),
          a.stream().map(mapAccountObjectToWithoutAccountHolderDTO).toList(), v);

  private final Predicate<GrantedAuthority> isEmployee =
      a -> a.getAuthority().equals(Role.ROLE_EMPLOYEE.name());


  public AccountController(AccountService accountService, UserService userService) {
    this.accountService = accountService;
    this.userService = userService;
  }

  //Get all accounts
  @GetMapping
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<Object> getAllAccounts(
      @RequestParam(defaultValue = DEFAULT_LIMIT_STRING, required = false) int limit,
      @RequestParam(defaultValue = DEFAULT_OFFSET_STRING, required = false) int offset,
      @RequestParam(required = false) String accountType)
  //Spring boot is asking for a default value  to be string
  // this Should be Allowed to be null so we cant use @ValidAccountAnnotation
  {
    AccountType passingAccountType = null;
    try {
      if (accountType != null) {
        passingAccountType = AccountType.valueOf(accountType.toUpperCase());
      }
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Account type is not valid"); //this will be caught by RestControllerExceptionHandler
    }
    List<Account> accounts = accountService.getAllAccounts(limit, offset, passingAccountType);
    return ResponseEntity.ok(
        accounts.parallelStream().map(mapAccountObjectToDTO).toList()
        // using Parallel Stream to improve performance
    );
  }

  //Get Account by IBAN
  // when it is customer it can access be his/her own account
  // when it is employee it can access anyone's account
  @GetMapping("/{iban}")
  @PreAuthorize("hasAnyRole('CUSTOMER', 'EMPLOYEE')")
  public ResponseEntity<Object> getAccountByIBAN(@InhollandIBANPattern
  @PathVariable String iban,
      @AuthenticationPrincipal UserDetails jwtUser) {
    if (jwtUser.getAuthorities().stream().noneMatch(isEmployee) &&
        !accountService.isAccountOwnedByCustomer(iban, jwtUser.getUsername())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
          new ErrorMessageDTO("You are not allowed to access this account!")
      );
    }
    return ResponseEntity.ok(
        mapAccountObjectToDTO.apply(
            accountService.getAccountByIBAN(iban))
    );
  }

  //Search for accounts by customer name
  @GetMapping("/searchByCustomerName")
  @PreAuthorize("hasAnyRole('CUSTOMER', 'EMPLOYEE')")
  public ResponseEntity<Object> searchAccountByCustomerName(
      @NotBlank(message = "Customer name cannot be empty in order to search") @RequestParam String customerName,
      @RequestParam(defaultValue = DEFAULT_LIMIT_STRING, required = false) int limit,
      @RequestParam(defaultValue = DEFAULT_OFFSET_STRING, required = false) int offset) {

    List<Account> accounts = accountService.searchAccountByCustomerName(customerName, limit,
        offset);
    if (accounts.isEmpty()) { // when no accounts are found by the input name
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
          new ErrorMessageDTO("No accounts found by this name " + customerName + "!")
      );
    }
    return ResponseEntity.ok(
        accounts.parallelStream().map(mapAccountObjectToSearchingDTO).toList()
        // using Parallel Stream to improve performance
    );
  }

  //Change accounts status by IBAN
  @PostMapping(value = "/accountStatus/{iban}", consumes = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<Object> changeAccountStatus(@InhollandIBANPattern
  @PathVariable String iban,
      @Valid @RequestBody AccountStatusDTO accountStatusDTO) {
    accountService.changeAccountStatus(iban, accountStatusDTO.isActive());
    return ResponseEntity.noContent().build();
  }

  //Post new account
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<Object> createAccount(@Valid @RequestBody CreatingAccountDTO accountDTO)
      throws LimitExceededException {
    return ResponseEntity.status(HttpStatus.CREATED).body(mapAccountObjectToDTO.apply(
        accountService.createAccountWithLimitCheck(accountDTO)));

  }

  //Update account
  // balance cannot be updated by this endpoint
  @PutMapping(value = "/{iban}", consumes = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<Object> updateAccount(
      @InhollandIBANPattern @NotEmpty(message = "The updating account iban must be provided")
      @PathVariable String iban,
      @Valid @RequestBody UpdatingAccountDTO accountDTO) {

    return ResponseEntity.ok(mapAccountObjectToDTO.apply(
        accountService.updateAccountDetails(iban, accountDTO))); // parsing account object to DTO
  }

  // this endpoint will access by both employee and customer
  // if the user is employee, user can access all accounts of any users
  // if the user is customer, user can access only his accounts by verifying with JWT token
  // This endpoint will only return the active accounts only
  @GetMapping("/user/{email}")
  @PreAuthorize("hasAnyRole('EMPLOYEE', 'CUSTOMER')")
  public ResponseEntity<Object> getAccountsOfUserByEmail(
      @Email(message = "The email provided is not a valid email address")
      @PathVariable String email,
      @AuthenticationPrincipal UserDetails jwtUser) {

    if (jwtUser.getAuthorities().stream().noneMatch(isEmployee)
        // not so big list and less computation so Stream is fine
        && !jwtUser.getUsername().equalsIgnoreCase(email)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(new ErrorMessageDTO("You are not allowed to access others Accounts Details!"));
    }
    User requestingUser = userService.getUserByEmail(email);
    double transactionDoneToday = accountService.getTotalTransactedAmountOfTodayByUserEmail(
        requestingUser.getEmail());
    return ResponseEntity.ok(
        parseListOfAccountAndUserObjectToUserAccountsDTO.apply(
            accountService.getAccountsByEmailAddress(email),
            requestingUser,
            transactionDoneToday
        )
    );
  }

}
