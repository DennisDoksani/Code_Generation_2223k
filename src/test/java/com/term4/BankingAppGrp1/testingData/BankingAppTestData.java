package com.term4.BankingAppGrp1.testingData;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.models.Role;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.responseDTOs.AccountDTO;
import com.term4.BankingAppGrp1.responseDTOs.AccountHolderDTO;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;

public abstract class BankingAppTestData  {
  protected ObjectMapper objectMapper;
  protected Account employeeAccount;
  protected final String CUSTOMER_EMAIL="customer@seed.com";
  protected final String EMPLOYEE_EMAIL="employee@seed.com";
  protected final String EMPLOYEE_CUSTOMER_EMAIL="employeeCustomer@seed.com";
  protected User employeeUser;
  protected  User customerUser;

  protected AccountDTO accountDTO;

  protected AccountHolderDTO accountHolderDTO;
  @BeforeEach
  protected void setupData(){
    objectMapper=new ObjectMapper();
    customerUser= User.builder()
        .id(3L)
        .bsn("091287662")
        .firstName("Employee")
        .lastName("Seed")
        .dateOfBirth(LocalDate.of(1990, 1, 1))
        .phoneNumber("0611111111")
        .email("customer@seed.com")
        .password("password")
        .dayLimit(200.2)
        .transactionLimit(200.2)
        .isActive(true)
        .roles(List.of(Role.ROLE_CUSTOMER))
        .build();
    employeeUser= User.builder()
        .id(1)
        .bsn("277545146")
        .firstName("EmployeeCustomer")
        .lastName("Seed")
        .dateOfBirth(LocalDate.of(1990, 1, 1))
        .phoneNumber("0611111111")
        .email("employeecustomer@seed.com")
        .password("password")
        .isActive(true)
        .roles(List.of(Role.ROLE_EMPLOYEE, Role.ROLE_CUSTOMER))
        .dayLimit(1000)
        .transactionLimit(300)
        .build();
    employeeAccount = Account.builder()
        .iban("NL72INHO0579629781")
        .accountType(AccountType.CURRENT)
        .creationDate(LocalDate.now())
        .balance(1000)
        .isActive(true)
        .customer(employeeUser)
        .build();
    accountHolderDTO= new AccountHolderDTO(employeeUser.getId(),employeeUser.getDayLimit(),employeeUser.getTransactionLimit(),
        employeeUser.getFirstName(),employeeUser.getLastName());
    accountDTO= new AccountDTO(employeeAccount.getIban(),employeeAccount.getBalance(),
        employeeAccount.getAbsoluteLimit(),employeeAccount.getCreationDate()
        ,employeeAccount.isActive(),
        employeeAccount.getAccountType(),accountHolderDTO);
  }

}
