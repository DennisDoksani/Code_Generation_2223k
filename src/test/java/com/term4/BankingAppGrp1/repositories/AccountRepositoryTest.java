package com.term4.BankingAppGrp1.repositories;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.AccountType;
import com.term4.BankingAppGrp1.models.Role;
import com.term4.BankingAppGrp1.models.User;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.FluentQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;


class AccountRepositoryTest {
    private AccountRepository accountRepository = new AccountRepository() {

        User user = User.builder()
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
        Account testingAccount = Account.builder()
                .iban("NL72INHO0579629781")
                .balance(900.0)
                .creationDate(LocalDate.now())
                .accountType(AccountType.CURRENT)
                .customer(user)
                .build();

        @Override
        public List<Account> findAll(Sort sort) {
            return null;
        }

        @Override
        public Page<Account> findAll(Pageable pageable) {
            return null;
        }

        @Override
        public <S extends Account> S save(S entity) {
            return null;
        }

        @Override
        public <S extends Account> List<S> saveAll(Iterable<S> entities) {
            return null;
        }

        @Override
        public Optional<Account> findById(String s) {
            return Optional.empty();
        }

        @Override
        public boolean existsById(String s) {
            return false;
        }

        @Override
        public List<Account> findAll() {
            return null;
        }

        @Override
        public List<Account> findAllById(Iterable<String> strings) {
            return null;
        }

        @Override
        public long count() {
            return 0;
        }

        @Override
        public void deleteById(String s) {

        }

        @Override
        public void delete(Account entity) {

        }

        @Override
        public void deleteAllById(Iterable<? extends String> strings) {

        }

        @Override
        public void deleteAll(Iterable<? extends Account> entities) {

        }

        @Override
        public void deleteAll() {

        }

        @Override
        public Optional<Account> findOne(Specification<Account> spec) {
            return Optional.empty();
        }

        @Override
        public List<Account> findAll(Specification<Account> spec) {
            return null;
        }

        @Override
        public Page<Account> findAll(Specification<Account> spec, Pageable pageable) {
            return null;
        }

        @Override
        public List<Account> findAll(Specification<Account> spec, Sort sort) {
            return null;
        }

        @Override
        public long count(Specification<Account> spec) {
            return 0;
        }

        @Override
        public boolean exists(Specification<Account> spec) {
            return false;
        }

        @Override
        public long delete(Specification<Account> spec) {
            return 0;
        }

        @Override
        public <S extends Account, R> R findBy(Specification<Account> spec, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
            return null;
        }

        @Override
        public void flush() {

        }

        @Override
        public <S extends Account> S saveAndFlush(S entity) {
            return null;
        }

        @Override
        public <S extends Account> List<S> saveAllAndFlush(Iterable<S> entities) {
            return null;
        }

        @Override
        public void deleteAllInBatch(Iterable<Account> entities) {

        }

        @Override
        public void deleteAllByIdInBatch(Iterable<String> strings) {

        }

        @Override
        public void deleteAllInBatch() {

        }

        @Override
        public Account getOne(String s) {
            return null;
        }

        @Override
        public Account getById(String s) {
            return null;
        }

        @Override
        public Account getReferenceById(String s) {
            return null;
        }

        @Override
        public <S extends Account> Optional<S> findOne(Example<S> example) {
            return Optional.empty();
        }

        @Override
        public <S extends Account> List<S> findAll(Example<S> example) {
            return null;
        }

        @Override
        public <S extends Account> List<S> findAll(Example<S> example, Sort sort) {
            return null;
        }

        @Override
        public <S extends Account> Page<S> findAll(Example<S> example, Pageable pageable) {
            return null;
        }

        @Override
        public <S extends Account> long count(Example<S> example) {
            return 0;
        }

        @Override
        public <S extends Account> boolean exists(Example<S> example) {
            return false;
        }

        @Override
        public <S extends Account, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
            return null;
        }

        @Override
        public Page<Account> findAccountByAccountTypeEqualsAndIbanNot(Pageable pageable, AccountType accountType, String iban) {
            return null;
        }

        @Override
        public Page<Account> findByAndIbanNot(Pageable pageable, String iban) {
            return null;
        }

        @Override
        public List<Account> findByCustomer_IdEquals(long id) {
            return null;
        }

        @Override
        public int countAccountByCustomer_IdEqualsAndAccountTypeEquals(long customerId, AccountType accountType) {
            return 0;
        }

        @Override
        public Double getTotalTransactionsDoneTodayByUser(long customerId) {
            return null;
        }

        @Override
        public boolean existsAccountByIbanEqualsAndCustomerEmailEquals(String iban, String email) {
            return false;
        }
    };
}
