package com.term4.BankingAppGrp1.configuration;

import com.term4.BankingAppGrp1.models.*;
import com.term4.BankingAppGrp1.services.*;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class Runner implements ApplicationRunner {
    private final AccountService accountService;
    private final UserService userService;
    private TransactionService transactionService;

    public Runner(AccountService accountService, UserService userService, TransactionService transactionService) {

        this.accountService = accountService;
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @DeleteMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable long id){
        return userService.deleteUser(id);
    }

    @GetMapping("/users/getone/{id}")
        public ResponseEntity<User> getUser(@PathVariable long id){
            Optional<User> user = userService.getUser(id);
            return user.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        User joshMf= new User(234445,"Joshua","Mf", LocalDate.now(),"680000000000","josh@mf.com","josh",true, 0,0);
        userService.saveUser(joshMf);
        for (int i = 0; i < 800; i++) {
            Account seedAccount = new Account(AccountType.CURRENT,joshMf);
            accountService.saveAccount(seedAccount);
        }
        Account seedAccount = new Account(AccountType.CURRENT,joshMf);
        Account seedSavings = new Account(AccountType.SAVINGS, joshMf);
        accountService.saveAccount(seedAccount);
        accountService.saveAccount(seedSavings);
        seedBankAccount();

        Transaction newTransaction = new Transaction(10.00, seedSavings.getIban(), seedAccount.getIban(), new Date(2023, 5, 22), new Time(21, 4, 0), joshMf);
        transactionService.addTransaction(newTransaction);
    }

    @Bean
    public EntityManager em() {
        return new EntityManager() {
            @Override
            public void persist(Object o) {

            }

            @Override
            public <T> T merge(T t) {
                return null;
            }

            @Override
            public void remove(Object o) {

            }

            @Override
            public <T> T find(Class<T> aClass, Object o) {
                return null;
            }

            @Override
            public <T> T find(Class<T> aClass, Object o, Map<String, Object> map) {
                return null;
            }

            @Override
            public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType) {
                return null;
            }

            @Override
            public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType, Map<String, Object> map) {
                return null;
            }

            @Override
            public <T> T getReference(Class<T> aClass, Object o) {
                return null;
            }

            @Override
            public void flush() {

            }

            @Override
            public void setFlushMode(FlushModeType flushModeType) {

            }

            @Override
            public FlushModeType getFlushMode() {
                return null;
            }

            @Override
            public void lock(Object o, LockModeType lockModeType) {

            }

            @Override
            public void lock(Object o, LockModeType lockModeType, Map<String, Object> map) {

            }

            @Override
            public void refresh(Object o) {

            }

            @Override
            public void refresh(Object o, Map<String, Object> map) {

            }

            @Override
            public void refresh(Object o, LockModeType lockModeType) {

            }

            @Override
            public void refresh(Object o, LockModeType lockModeType, Map<String, Object> map) {

            }

            @Override
            public void clear() {

            }

            @Override
            public void detach(Object o) {

            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public LockModeType getLockMode(Object o) {
                return null;
            }

            @Override
            public void setProperty(String s, Object o) {

            }

            @Override
            public Map<String, Object> getProperties() {
                return null;
            }

            @Override
            public Query createQuery(String s) {
                return null;
            }

            @Override
            public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
                return null;
            }

            @Override
            public Query createQuery(CriteriaUpdate criteriaUpdate) {
                return null;
            }

            @Override
            public Query createQuery(CriteriaDelete criteriaDelete) {
                return null;
            }

            @Override
            public <T> TypedQuery<T> createQuery(String s, Class<T> aClass) {
                return null;
            }

            @Override
            public Query createNamedQuery(String s) {
                return null;
            }

            @Override
            public <T> TypedQuery<T> createNamedQuery(String s, Class<T> aClass) {
                return null;
            }

            @Override
            public Query createNativeQuery(String s) {
                return null;
            }

            @Override
            public Query createNativeQuery(String s, Class aClass) {
                return null;
            }

            @Override
            public Query createNativeQuery(String s, String s1) {
                return null;
            }

            @Override
            public StoredProcedureQuery createNamedStoredProcedureQuery(String s) {
                return null;
            }

            @Override
            public StoredProcedureQuery createStoredProcedureQuery(String s) {
                return null;
            }

            @Override
            public StoredProcedureQuery createStoredProcedureQuery(String s, Class... classes) {
                return null;
            }

            @Override
            public StoredProcedureQuery createStoredProcedureQuery(String s, String... strings) {
                return null;
            }

            @Override
            public void joinTransaction() {

            }

            @Override
            public boolean isJoinedToTransaction() {
                return false;
            }

            @Override
            public <T> T unwrap(Class<T> aClass) {
                return null;
            }

            @Override
            public Object getDelegate() {
                return null;
            }

            @Override
            public void close() {

            }

            @Override
            public boolean isOpen() {
                return false;
            }

            @Override
            public EntityTransaction getTransaction() {
                return null;
            }

            @Override
            public EntityManagerFactory getEntityManagerFactory() {
                return null;
            }

            @Override
            public CriteriaBuilder getCriteriaBuilder() {
                return null;
            }

            @Override
            public Metamodel getMetamodel() {
                return null;
            }

            @Override
            public <T> EntityGraph<T> createEntityGraph(Class<T> aClass) {
                return null;
            }

            @Override
            public EntityGraph<?> createEntityGraph(String s) {
                return null;
            }

            @Override
            public EntityGraph<?> getEntityGraph(String s) {
                return null;
            }

            @Override
            public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> aClass) {
                return null;
            }
        };
    }
    
    private void seedBankAccount(){
        User inhollandBank= new User(111111,"Inholland","Bank", LocalDate.now(),
                "680000000000", "inholland@bank.nl","Inholland", true,
                9999999999999999L,9999999999999999L);
        userService.saveUser(inhollandBank);
        Account seedAccount = new Account("NL01INHO0000000001",9999999999999.0,LocalDate.now(),0,true,AccountType.CURRENT,inhollandBank);
        accountService.saveAccount(seedAccount);
    }
}
