package com.fdmgroup.ses.validation;

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
import com.fdmgroup.ses.utils.DateUtils;

@Component
public class SaleFormValidator extends ModelValidator {
	
	public static final String FAIL_NO_STOCK_SELECTED = "You must select at least one stock to sell.";

	public static String generateInsufficientOwnedStockMessage(Company company, Long quantityOwned) {
		return "Insufficient stocks for " + company.getSymbol() + ": "
				+ company.getTransactionQuantity() + " to be sold; "
				+ quantityOwned + " owned.";
	}
	
	public static final String FAIL_TRANSACTION_EXPIRED = "Transaction expired! You must complete sales/purchases "
			+ "within " + TransactionForm.EXPIRY_MINUTES_STRING + " minutes.";
	
	@Autowired
	private UserService userService;

	@Autowired
	private OwnedSharesRepository ownedSharesRepo;
	
	@Autowired
	private CompanyRepository companyRepo;

	private SaleForm saleForm;

	/**
	 * Validates sake requests:
	 * # At least one stock must be selected for sale with quantity 1 or greater
	 * # You cannot sell more of a stock than you own
	 */
	@Override
	public void validate() throws SesValidationException {
		
		failures.clear();
		
		User user = userService.findCurrentUser();
		
		// Must be completed within 5 minutes
		if (saleForm.getSubmissionDate() != null) {
			if (DateUtils.isXMinutesOld(saleForm.getSubmissionDate(), TransactionForm.EXPIRY_MINUTES)) {
				failures.add(FAIL_TRANSACTION_EXPIRED);
			}
		}
		
		// At least one share must be selected
		if (saleForm.getOwnedShares().size() < 1) {
			failures.add(FAIL_NO_STOCK_SELECTED);
		} else {
			Boolean containsNonZeroSale = false;
			for (OwnedShare sale : saleForm.getOwnedShares()) {
				if (sale.getCompany().getTransactionQuantity() != 0) {
					containsNonZeroSale = true;
				}
			}
			if (!containsNonZeroSale) {
				failures.add(FAIL_NO_STOCK_SELECTED);
			}
		}
		
		// For each stock...
		for (OwnedShare share : saleForm.getOwnedShares()) {
			
			Company company = share.getCompany();
			Long saleQuantity = company.getTransactionQuantity();
			Company dbCompany = companyRepo.findBySymbol(company.getSymbol());
			Long dbOwnedQuantity = ownedSharesRepo.findByOwnerAndCompany(user, dbCompany).getQuantity();
			
			// ... you must own sufficient stock to be able to sell it 
			if (dbOwnedQuantity < saleQuantity) {
				failures.add(generateInsufficientOwnedStockMessage(company, dbOwnedQuantity));
			}
			
		}
		throwFailures();
	}

	public void setSaleForm(SaleForm form) {
		this.saleForm = form;
	}

}