package com.term4.BankingAppGrp1.responseDTOs;

import com.term4.BankingAppGrp1.models.AccountType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountWithoutAccountHolderDTO {

  private String iban;
  private double accountBalance;
  private double absoluteLimit;
  private LocalDate creationDate;
  private AccountType accountType;
}
