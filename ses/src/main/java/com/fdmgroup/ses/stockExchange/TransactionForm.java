package com.fdmgroup.ses.stockExchange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fdmgroup.ses.model.Company;

public class TransactionForm {

	List<Company> companies;

	public List<Company> getCompanies() {
		if (companies == null) {
			companies = new ArrayList<>();
		}
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}
	
	/**
	 * Sums (shareValue * transactionQuantity) for each company in the transaction
	 */
	public BigDecimal getTransactionValue() {
		BigDecimal txValue = new BigDecimal(0);
		for (Company company : companies) {
			txValue = txValue.add(company.getTransactionValue());
		}
		return txValue;
	}
	
}
