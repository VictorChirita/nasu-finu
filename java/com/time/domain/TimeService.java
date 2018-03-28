package com.time.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

import javax.annotation.security.PermitAll;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@PermitAll
public class TimeService {
    @PersistenceContext(unitName = "time")
    private EntityManager entityManager;

	public List<?> saveOrUpdate(Object... objects) {
		return saveOrUpdate(Arrays.asList(objects));
	}
    
	public List<?> saveOrUpdate(List<?> objects) {
		if (objects == null || objects.isEmpty()) {
			return Collections.emptyList();
		}

		List<Object> result = new ArrayList<>();

		for (Object object : objects) {
			if (entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(object) == null) {
				entityManager.persist(object);
				result.add(object);
			} else {
				result.add(entityManager.merge(object));
			}
		}

		return result;
	}
	
	public <T> List<T> getAllObjects(Class<T> type, BiFunction<CriteriaBuilder, Root<T>, Predicate>... where){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> query = cb.createQuery(type);
		
		Root<T> root = query.from(type);
		
		if(where!=null) {
			for (BiFunction<CriteriaBuilder, Root<T>, Predicate> expression : where) {
				Predicate predicate = expression.apply(cb, root);
				query.where(predicate);
			}
		}
		
		return entityManager.createQuery(query).getResultList();
	}
	
	public <T> T getObjectById(Class<T> cls, BigDecimal id) {
		return entityManager.find(cls, id);
	}

	public <T> T getRefreshedObjectById(Class<T> cls, Object id) {
		T result = entityManager.find(cls, id);
		refresh(result);
		return result;
	}
	
	public void refresh(Object o) {
		entityManager.refresh(o);
	}

	public void insertSomething() {
		// entityManager operations
	}
}
