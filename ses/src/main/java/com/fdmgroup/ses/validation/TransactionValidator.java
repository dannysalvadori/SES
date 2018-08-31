package com.fdmgroup.ses.validation;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.CompanyRepository;
import com.fdmgroup.ses.service.CreditCardService;
import com.fdmgroup.ses.service.UserService;
import com.fdmgroup.ses.stockExchange.TransactionForm;

@Component
public class TransactionValidator extends ModelValidator {

	// Failure messages
	public static final String FAIL_NO_CREDIT_CARDS = "Purchases require a valid credit card. Visit \"My Account\" to "
			+ "register a card.";
	
	public static final String FAIL_NO_STOCK_SELECTED = "You must select at least one stock.";
	
	public static String generateInsufficientCreditMessage(BigDecimal transactionValue, BigDecimal userCredit) {
		return "Insufficient credit: " // TODO: format currency
				+ transactionValue + " required; "
				+ "you have " + userCredit + " available.";
	}
	public static String generateInsufficientStockMessage(Company company, Company dbCompany) {
		return "Insufficient stocks for " + company.getSymbol() + ": "
				+ company.getTransactionQuantity() + " requested; "
				+ dbCompany.getAvailableShares() + " available.";
	}
	
	@Autowired
	private UserService userService;

	@Autowired
	private CompanyRepository companyRepo;

	@Autowired
	private CreditCardService creditCardService;

	private TransactionForm transactionForm;

	/**
	 * Validates transaction requests:
	 * Purchases:
	 * # User must have registered at least one credit card
	 * # At least one company must be selected
	 * # User must have sufficient credit for the whole transaction
	 * # Each company must have sufficient stock
	 * Sales:
	 * # None
	 */
	@Override
	public void validate() throws SesValidationException {

		failures.clear();
		
		User user = userService.findCurrentUser();
		BigDecimal userCredit = user.getCredit();
		BigDecimal transactionValue = transactionForm.getTransactionValue();
		
		// Must have at least one registered credit card
		if (creditCardService.findAllForCurrentUser().isEmpty()) {
			failures.add(FAIL_NO_CREDIT_CARDS);
		}
		
		// At least one company must be selected
		if (transactionForm.getCompanies().size() < 1) {
			failures.add(FAIL_NO_STOCK_SELECTED);
		}
		
		// User must have sufficient credit
		if (userCredit.compareTo(transactionValue) < 0) {
			failures.add(generateInsufficientCreditMessage(transactionValue, userCredit));
		}
		
		// For each stock...
		for (Company company : transactionForm.getCompanies()) {
			
			// ... companies must have sufficient available stock 
			Company dbCompany = companyRepo.findBySymbol(company.getSymbol());
			if (dbCompany.getAvailableShares() < company.getTransactionQuantity()) {
				failures.add(generateInsufficientStockMessage(company, dbCompany));
			}
			
		}
		
		throwFailures();
	}

	public void setTransactionForm(TransactionForm form) {
		this.transactionForm = form;
	}

}