package com.term4.BankingAppGrp1.repositories;

import static com.term4.BankingAppGrp1.models.ConstantsContainer.DEFAULT_INHOLLAND_BANK_IBAN;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.models.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class AccountSpecifications {

  // this specification will return all accounts that have the given customer name
  // , and it will check both first name and last name
  public static Specification<Account> hasCustomerName(String name) {
    return (root, query, criteriaBuilder) -> {
      Join<Account, User> accountHolderJoin = root.join("customer");
      String lowerCaseFullName = name.toLowerCase();
      return criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.concat(
          criteriaBuilder.concat(accountHolderJoin.get("firstName"), " "),
          accountHolderJoin.get("lastName")
      )), "%" + lowerCaseFullName + "%");
    };
  }

  // this Specification will return all the Current accounts
  public static Specification<Account> isCurrentAccounts() {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("accountType"), AccountType.CURRENT);
  }

  // this Specification will return all the Active Accounts
  public static Specification<Account> isActiveAccounts() {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.isTrue(root.get("isActive"));
  }

  public static Specification<Account> isNotBanksOwnAccount() {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.notEqual(root.get("iban"), DEFAULT_INHOLLAND_BANK_IBAN);
  }

  public static Specification<Account> hasCustomerEmail(String email) {
    return (root, query, criteriaBuilder) -> {
      Join<Account, User> accountHolderJoin = root.join("customer");
      String lowerCaseEmail = email.toLowerCase();
      return criteriaBuilder.equal(
          criteriaBuilder.lower(
              accountHolderJoin.get("email")), lowerCaseEmail

      );
    };
  }

}

