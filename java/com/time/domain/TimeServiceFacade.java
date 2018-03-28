package com.time.domain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class TimeServiceFacade {
	@EJB
	protected TimeService fill;

	public void insertSomething() {
		fill.insertSomething();
	}
}
