package com.fdmgroup.ses.validation;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.CompanyRepository;
import com.fdmgroup.ses.repository.OwnedSharesRepository;
import com.fdmgroup.ses.service.UserService;
import com.fdmgroup.ses.stockExchange.SaleForm;
import com.fdmgroup.ses.stockExchange.TransactionForm;

@Component
public class SaleValidator extends ModelValidator {

	@Autowired
	private UserService userService;

	@Autowired
	private OwnedSharesRepository ownedSharesRepo;
	
	@Autowired
	private CompanyRepository companyRepo;

	private SaleForm saleForm;

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
		
		failures.clear();
		
		User user = userService.findCurrentUser();
		
		// At least one share must be selected
		if (saleForm.getOwnedShares().size() < 1) {
			failures.add("You must select at least one stock to sell.");
		}
		
		// For each stock...
		for (OwnedShare share : saleForm.getOwnedShares()) {
			
			Company company = share.getCompany();
			Long saleQuantity = company.getTransactionQuantity();
			Company dbCompany = companyRepo.findBySymbol(company.getSymbol());
			Long dbOwnedQuantity = ownedSharesRepo.findByOwnerAndCompany(user, dbCompany).getQuantity();
			
			// ... you must own sufficient stock to be able to sell it 
			if (dbOwnedQuantity < saleQuantity) {
				failures.add("Insufficient stocks for " + company.getSymbol() + ": "
						+ saleQuantity + " to be sold; "
						+ dbOwnedQuantity + " owned.");
			}
			
		}
		throwFailures();
	}

	public void setSaleForm(SaleForm form) {
		this.saleForm = form;
	}

}