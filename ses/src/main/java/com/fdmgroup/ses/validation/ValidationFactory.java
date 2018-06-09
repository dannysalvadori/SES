package com.fdmgroup.ses.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.stockExchange.TransactionForm;

@Component
public class ValidationFactory {

	@Autowired
	private UserValidator userValidator;
	@Autowired
	private CompanyValidator companyValidator;
	@Autowired
	private TransactionValidator transactionValidator;
	
	/**
	 * Returns the SesValidator implementation correct to the given object's Class
	 */
	public SesValidator getValidator(Object object) {
		
		// TODO: cleverify this, Map<Class, SesValidator> or something
		
		SesValidator validator = null;

		if (object.getClass() == User.class) {
			userValidator.setUser((User) object);
			validator = userValidator;
		
		} else if (object.getClass() == Company.class) {
			companyValidator.setCompany((Company) object);
			validator = companyValidator;
		
		} else if (object.getClass() == TransactionForm.class) {
			transactionValidator.setTransactionForm((TransactionForm) object);
			validator = transactionValidator;
		}
		
		return validator;
	}
	
}
