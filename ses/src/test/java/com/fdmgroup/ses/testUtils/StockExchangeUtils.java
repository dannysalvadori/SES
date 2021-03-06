package com.fdmgroup.ses.testUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.stockExchange.SaleForm;
import com.fdmgroup.ses.stockExchange.PurchaseForm;
import com.fdmgroup.ses.validation.CompanyValidator;

public class StockExchangeUtils {

	/**
	 * Creates a Company instance
	 */
	public static Company createCompany() {
		Company c = new Company();
		c.setName("Test Company");
		c.setSymbol("SEUTC");
		c.setAvailableShares(CompanyValidator.CREATIONS_LOWER_QUANTITY_LIMIT); // 2,000,000
		c.setCurrentShareValue(CompanyValidator.CREATIONS_LOWER_VALUE_LIMIT); // 5
		c.setTransactionQuantity(10l); // TX value of 50
		return c;
	}
	
	/**
	 * Create an OwnedShare instance.
	 * @param (Optional) Company instance. Default createCompany() is used.
	 */
	public static OwnedShare createOwnedShare() {
		return createOwnedShare(createCompany());
	}
	public static OwnedShare createOwnedShare(Company c) {
		OwnedShare os = new OwnedShare();
		os.setCompany(c);
		os.setAveragePurchasePrice(new BigDecimal(66));
		os.setQuantity(100l);
		return os;
	}
	
	/**
	 * Create a TransactionForm instance.
	 * @param (Optional) List of Company objects. Default createCompany() is used.
	 */
	public static PurchaseForm createTransactionForm() {
		List<Company> companies = new ArrayList<>();
		companies.add(createCompany());
		return createTransactionForm(companies);
	}
	public static PurchaseForm createTransactionForm(List<Company> companies) {
		PurchaseForm txForm = new PurchaseForm();
		txForm.setCompanies(companies);
		return txForm;
	}

	/**
	 * Create a SaleForm instance.
	 * @param (Optional) Company instance. Default createOwnedShare() is used.
	 */
	public static SaleForm createSaleForm() {
		List<OwnedShare> ownedShares = new ArrayList<>();
		ownedShares.add(createOwnedShare());
		return createSaleForm(ownedShares);
	}
	public static SaleForm createSaleForm(List<OwnedShare> ownedShares) {
		SaleForm saleForm = new SaleForm();
		saleForm.setOwnedShares(ownedShares);
		return saleForm;
	}
	
}
