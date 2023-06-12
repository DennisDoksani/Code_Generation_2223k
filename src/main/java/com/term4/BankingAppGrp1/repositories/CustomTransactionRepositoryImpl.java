package com.term4.BankingAppGrp1.repositories;


import com.term4.BankingAppGrp1.models.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class CustomTransactionRepositoryImpl implements CustomTransactionRepository {

  private EntityManager em;

  public CustomTransactionRepositoryImpl(EntityManager em) {
    this.em = em;
  }

  @Override
  public Page<Transaction> getTransactionsWithFilters(Pageable pageable, String ibanFrom,
      String ibanTo, Double amountMin, Double amountMax, LocalDate dateBefore,
      LocalDate dateAfter) {
    CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
    CriteriaQuery<Transaction> criteriaQuery = criteriaBuilder.createQuery(Transaction.class);

    Root<Transaction> transactionRoot = criteriaQuery.from(Transaction.class);
    List<Predicate> predicates = new ArrayList<>();

    if (ibanFrom != null) {
      predicates.add(criteriaBuilder.equal(transactionRoot.get("accountFrom"), ibanFrom));
    }
    if (ibanTo != null) {
      predicates.add(criteriaBuilder.equal(transactionRoot.get("accountTo"), ibanTo));
    }
    if (amountMin != null) {
      predicates.add(
          criteriaBuilder.greaterThanOrEqualTo(transactionRoot.get("amount"), amountMin));
    }
    if (amountMax != null) {
      predicates.add(criteriaBuilder.lessThanOrEqualTo(transactionRoot.get("amount"), amountMax));
    }
    if (dateAfter != null) {
      predicates.add(criteriaBuilder.greaterThanOrEqualTo(transactionRoot.get("date"), dateAfter));
    }
    if (dateBefore != null) {
      predicates.add(criteriaBuilder.lessThanOrEqualTo(transactionRoot.get("date"), dateBefore));
    }

    criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));

    TypedQuery<Transaction> query = em.createQuery(criteriaQuery);

    query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
    query.setMaxResults(pageable.getPageSize());

    List<Transaction> resultList = query.getResultList();   //Entity manager will not let me re-use a query so this is the workaround
    int totalRows = resultList.size();

    return new PageImpl<Transaction>(resultList, pageable, totalRows);
  }

}
