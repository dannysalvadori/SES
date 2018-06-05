package com.fdmgroup.ses.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.User;

@Component
public class ValidationFactory {

	@Autowired
	UserValidator userValidator;
	@Autowired
	CompanyValidator companyValidator;

	/**
	 * Returns the SesValidator implementation correct to the given object's Class
	 */
	public SesValidator getValidator(Object object) {
		
		SesValidator validator = null;

		if (object.getClass() == User.class) {
			userValidator.setUser((User) object);
			validator = userValidator;
		}
		
		if (object.getClass() == Company.class) {
			companyValidator.setCompany((Company) object);
			validator = companyValidator;
		}
		
		return validator;
	}
	
}
