package com.fdmgroup.ses.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.CompanyRepository;
import com.fdmgroup.ses.stockExchange.TransactionForm;
import com.fdmgroup.ses.validation.SesValidationException;
import com.fdmgroup.ses.validation.ValidationFactory;

public class CompanyService {

	@Autowired
    private CompanyRepository companyRepo;

	@Autowired
	ValidationFactory validationFactory;

	public void buyStocks(User user, TransactionForm transactionForm) throws SesValidationException {
		for (Company company : transactionForm.getCompanies()) {
			
			validationFactory.getValidator(company).validate();
			
			// Deduct purchased shares from those available
			company.setAvailableShares(company.getAvailableShares()-company.getTransactionQuantity());
			companyRepo.save(company);
			
			// Create owned shares record
			
			
			// Record transaction history
			
		}
	}
	
}
