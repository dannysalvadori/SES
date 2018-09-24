package com.fdmgroup.ses.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.CreditCardDetail;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.stockExchange.SaleForm;
import com.fdmgroup.ses.stockExchange.PurchaseForm;

@Component
public class ValidatorFactory {

	@Autowired
	private UserValidator userValidator;
	@Autowired
	private CompanyValidator companyValidator;
	@Autowired
	private PurchaseFormValidator transactionValidator;
	@Autowired
	private SaleFormValidator saleValidator;
	@Autowired
	private CreditCardValidator creditCardValidator;
	
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
			
		} else if (object.getClass() == PurchaseForm.class) {
			transactionValidator.setTransactionForm((PurchaseForm) object);
			validator = transactionValidator;
		
		} else if (object.getClass() == SaleForm.class) {
			saleValidator.setSaleForm((SaleForm) object);
			validator = saleValidator;
		
		} else if (object.getClass() == CreditCardDetail.class) {
			creditCardValidator.setCreditCardDetail((CreditCardDetail) object);
			validator = creditCardValidator;
		}
		
		return validator;
	}
	
}
