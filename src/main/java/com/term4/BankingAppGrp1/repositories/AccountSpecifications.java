package com.term4.BankingAppGrp1.repositories;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class AccountSpecifications {
    // this specification will return all accounts that have the given customer name
    // , and it will check both first name and last name
    public static Specification<Account> hasCustomerName(String name) {
        return (root, query, criteriaBuilder) -> {
            Join<Account, User> accountHolderJoin = root.join("customer");
            return criteriaBuilder.or(
                    criteriaBuilder.like(accountHolderJoin.get("firstName"), "%" + name + "%"),
                    criteriaBuilder.like(accountHolderJoin.get("lastName"), "%" + name + "%")
            );
        };
    }

    // this Specification will return all the Current accounts
    public static Specification<Account> isCurrentAccounts() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("accountType"), "Current");
    }

    // this Specification will return all the Active Accounts
    public static Specification<Account> isActiveAccounts() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("isActive"));
    }

}

