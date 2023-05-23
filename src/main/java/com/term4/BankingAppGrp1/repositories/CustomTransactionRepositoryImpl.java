package com.term4.BankingAppGrp1.repositories;


import com.term4.BankingAppGrp1.models.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomTransactionRepositoryImpl implements CustomTransactionRepository{

    private EntityManager em;

    public CustomTransactionRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Iterable<Transaction> getTransactionByAccountFromAndAmountGreaterThanEqual(String iban, Double amount) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Transaction> criteriaQuery = criteriaBuilder.createQuery(Transaction.class);

        Root<Transaction> transactionRoot = criteriaQuery.from(Transaction.class);
        List<Predicate> predicates = new ArrayList<>();

        if (amount != null) {
            predicates.add(criteriaBuilder.equal(transactionRoot.get("amount"), amount));
        }
        if (iban !=null) {
            predicates.add(criteriaBuilder.equal(transactionRoot.get("accountFrom"), iban));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return em.createQuery(criteriaQuery).getResultList();
    }
}
