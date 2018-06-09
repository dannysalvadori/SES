package com.fdmgroup.ses.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.stockExchange.TransactionForm;
import com.fdmgroup.ses.validation.SesValidationException;
import com.fdmgroup.ses.validation.ValidationFactory;

@Service("transactionService")
public class TransactionService {
	
	@Autowired
	private ValidationFactory validationFactory;

	@Autowired
    private CompanyService companyService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OwnedSharesService ownedSharesService;
	
	@Autowired
	private TransactionHistoryService txHistoryService;

	public void buyStocks(User user, TransactionForm transactionForm) throws SesValidationException {
		for (Company company : transactionForm.getCompanies()) {
			
			validationFactory.getValidator(transactionForm).validate();
			
			// Deduct purchased shares from those available
			companyService.updateAvailableShares(company);
			
			// Create owned shares record
			OwnedShare ownedShare = ownedSharesService.createOwnedShares(company, user);
			
			// Record transaction history
			txHistoryService.createTransactionHistory(company, user, ownedShare);
			
		}

		// Take credit from user
		userService.updateCredit(user, transactionForm);
	
	}
	
}
