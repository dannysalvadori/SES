package com.fdmgroup.ses.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.stockExchange.SaleForm;
import com.fdmgroup.ses.stockExchange.TransactionForm;

public class DataFactory {

	/**
	 * Creates a Company instance
	 */
	public static Company createCompany() {
		Company c = new Company();
		c.setName("Test Company");
		c.setSymbol("DFTC");
		c.setAvailableShares(100l);
		c.setCurrentShareValue(new BigDecimal(50));
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
		os.setSelected(true);
		return os;
	}
	
	/**
	 * Create a TransactionForm instance.
	 * @param (Optional) List of Company objects. Default createCompany() is used.
	 */
	public static TransactionForm createTransactionForm() {
		List<Company> companies = new ArrayList<>();
		companies.add(createCompany());
		return createTransactionForm(companies);
	}
	public static TransactionForm createTransactionForm(List<Company> companies) {
		TransactionForm txForm = new TransactionForm();
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
