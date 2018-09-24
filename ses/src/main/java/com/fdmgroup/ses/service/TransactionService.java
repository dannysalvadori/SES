package com.fdmgroup.ses.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.stockExchange.SaleForm;
import com.fdmgroup.ses.stockExchange.PurchaseForm;
import com.fdmgroup.ses.validation.SesValidationException;
import com.fdmgroup.ses.validation.ValidatorFactory;

@Service("transactionService")
public class TransactionService {
	
	@Autowired
	private ValidatorFactory validationFactory;

	@Autowired
    private CompanyService companyService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OwnedSharesService ownedSharesService;
	
	@Autowired
	private TransactionHistoryService txHistoryService;

	/**
	 * Validates a purchase, and throws SesValidationException if it fails. If validation succeeds, the relevant stock
	 * is reduced by the purchased amount, the user's ownedShares record is upserted, and transaction history is
	 * created.
	 * @throws SesValidationException
	 */
	public void buyStocks(User user, PurchaseForm transactionForm) throws SesValidationException {
		
		// First validate if the purchase remains valid
		validationFactory.getValidator(transactionForm).validate();
		
		for (Company company : transactionForm.getCompanies()) {
			
			// Deduct purchased shares from those available
			companyService.updateAvailableShares(company);
			
			// Create owned shares record
			OwnedShare ownedShare = ownedSharesService.upsertOwnedShares(company, user);
			
			// Record transaction history
			txHistoryService.createTransactionHistory(company, user, ownedShare);
			
		}
		// Take credit from user
		userService.updateCredit(user, transactionForm);
	}

	/**
	 * Validates a sale, and throws SesValidationException if it fails. If validation succeeds, the relevant stock
	 * is increased by the sale amount, the user's ownedShares record is updated, and transaction history is
	 * created.
	 * @throws SesValidationException
	 */
	public void sellStocks(User user, SaleForm saleForm) throws SesValidationException {
		
		// First validate the sale is still valid (e.g. user has not sold same stocks twice)
		validationFactory.getValidator(saleForm).validate();
		
		for (OwnedShare share : saleForm.getOwnedShares()) {
			
			// Invert transactionQuantity to make is a sale instead of a purchase
			Company company = share.getCompany();
			company.setTransactionQuantity(company.getTransactionQuantity() * -1);
			
			// Deduct sale shares (negative amount, hence adding them back to company)
			companyService.updateAvailableShares(company);
			
			// Update owned shares record
			OwnedShare ownedShare = ownedSharesService.upsertOwnedShares(company, user);
			
			// Record transaction history
			txHistoryService.createTransactionHistory(company, user, ownedShare);
			
		}
		// Take credit from user
		userService.updateCredit(user, saleForm);
	}
	
}
