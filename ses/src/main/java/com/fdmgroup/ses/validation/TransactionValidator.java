package com.fdmgroup.ses.validation;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.service.UserService;
import com.fdmgroup.ses.stockExchange.TransactionForm;

@Component
public class TransactionValidator extends ModelValidator {
	
	@Autowired
	private UserService userService;

	private TransactionForm transactionForm;

	/**
	 * Validates Company creations and updates:
	 * # Stock value cannot fall below XXX or rise above YYY
	 * # Stock amount cannot fall to 0 (how about ZZZ amount?)
	 */
	@Override
	public void validate() throws SesValidationException {
		
		failures.clear();
		
		User user = userService.findCurrentUser();
		BigDecimal userCredit = user.getCredit();
		BigDecimal transactionValue = new BigDecimal(0);
		
		Boolean purchase = transactionValue.compareTo(new BigDecimal(0)) > 0;
		System.out.println("purchase? " + purchase);
		
		if (purchase) {
			
			for (Company company : transactionForm.getCompanies()) {
				transactionValue = transactionValue
				.add(
						company.getCurrentShareValue()
						.multiply(new BigDecimal(company.getTransactionQuantity()))
				);
			}
			
			if (userCredit.compareTo(transactionValue) < 0) {
				failures.add("Insufficient credit. You have £" + userCredit + " available credit; £"
						+ transactionValue + " is required to complete this transaction.");
			}
		}

		System.out.println("failures? : " + ValidationUtils.stringifyFailures(failures));
		throwFailures();
	}

	public void setTransactionForm(TransactionForm form) {
		this.transactionForm = form;
	}

}