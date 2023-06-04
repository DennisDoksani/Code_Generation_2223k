package com.term4.BankingAppGrp1.configuration;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.Metamodel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.google.i18n.phonenumbers.PhoneNumberUtil; 

import java.util.List;
import java.util.Map;
import java.util.Random;

@Configuration
public class BeanFactory {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public Random randomizer() {
        return new Random();
    }

    @Bean
    public PhoneNumberUtil phoneNumberUtil() { 
        return PhoneNumberUtil.getInstance(); 
    } 

    @Bean
    public EntityManager em() {
        return new EntityManager() {
            @Override
            public void persist(Object o) {
                throw new UnsupportedOperationException();
            }

            @Override
            public <T> T merge(T t) {
                return null;
            }

            @Override
            public void remove(Object o) {
                throw new UnsupportedOperationException();
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
                throw new UnsupportedOperationException();
            }

            @Override
            public void setFlushMode(FlushModeType flushModeType) {
                throw new UnsupportedOperationException();
            }

            @Override
            public FlushModeType getFlushMode() {
                return null;
            }

            @Override
            public void lock(Object o, LockModeType lockModeType) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void lock(Object o, LockModeType lockModeType, Map<String, Object> map) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void refresh(Object o) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void refresh(Object o, Map<String, Object> map) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void refresh(Object o, LockModeType lockModeType) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void refresh(Object o, LockModeType lockModeType, Map<String, Object> map) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void clear() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void detach(Object o) {
                throw new UnsupportedOperationException();
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
                throw new UnsupportedOperationException("This operation is not supported.");
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
                throw new UnsupportedOperationException();
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
                throw new UnsupportedOperationException();
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
                throw new UnsupportedOperationException();
            }

            @Override
            public CriteriaBuilder getCriteriaBuilder() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Metamodel getMetamodel() {
                throw new UnsupportedOperationException();
            }

            @Override
            public <T> EntityGraph<T> createEntityGraph(Class<T> aClass) {
                throw new UnsupportedOperationException();
            }

            @Override
            public EntityGraph<?> createEntityGraph(String s) {
                throw new UnsupportedOperationException();
            }

            @Override
            public EntityGraph<?> getEntityGraph(String s) {
                throw new UnsupportedOperationException();
            }

            @Override
            public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> aClass) {
                throw new UnsupportedOperationException();
            }
        };
    }
} 
