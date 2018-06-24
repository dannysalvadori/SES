package com.fdmgroup.ses.validation;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.CompanyRepository;
import com.fdmgroup.ses.service.UserService;
import com.fdmgroup.ses.stockExchange.TransactionForm;

@Component
public class TransactionValidator extends ModelValidator {

	@Autowired
	private UserService userService;

	@Autowired
	private CompanyRepository companyRepo;

	private TransactionForm transactionForm;

	/**
	 * Validates transaction requests:
	 * -- Purchases:
	 * # User must have sufficient credit for the whole transaction
	 * # Each company must have sufficient stock
	 * -- Sales:
	 * # TODO:
	 */
	@Override
	public void validate() throws SesValidationException {

		System.out.println("real txValidator working!...");
		failures.clear();
		
		User user = userService.findCurrentUser();
		BigDecimal userCredit = user.getCredit();
		BigDecimal transactionValue = transactionForm.getTransactionValue();
		
		// At least one company must be selected
		if (transactionForm.getCompanies().size() < 1) {
			failures.add("You must select at least one stock.");
		}
		
		// User must have sufficient credit
		if (userCredit.compareTo(transactionValue) < 0) {
			failures.add("Insufficient credit: " // TODO: format currency
					+ transactionValue + " required; "
					+ "you have " + userCredit + " available.");
		}
		
		// For each stock...
		for (Company company : transactionForm.getCompanies()) {
			
			// ... companies must have sufficient available stock 
			Company dbCompany = companyRepo.findBySymbol(company.getSymbol());
			if (dbCompany.getAvailableShares() < company.getTransactionQuantity()) {
				failures.add("Insufficient stocks for " + company.getSymbol() + ": "
						+ company.getTransactionQuantity() + " requested; "
						+ dbCompany.getAvailableShares() + " available.");
			}
			
		}
		
		throwFailures();
	}

	public void setTransactionForm(TransactionForm form) {
		this.transactionForm = form;
	}

}