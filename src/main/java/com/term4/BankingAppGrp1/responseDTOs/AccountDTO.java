package com.term4.BankingAppGrp1.responseDTOs;

import com.term4.BankingAppGrp1.models.AccountType;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)

public class AccountDTO extends AccountWithoutAccountHolderDTO {

  private boolean isActive;
  private AccountHolderDTO accountHolder;

  public AccountDTO(String iban,
      double accountBalance,
      double absoluteLimit,
      LocalDate creationDate,
      boolean isActive,
      AccountType accountType,
      AccountHolderDTO accountHolder) {
    super(iban, accountBalance, absoluteLimit, creationDate, accountType);
    this.isActive = isActive;
    this.accountHolder = accountHolder;
  }
}
