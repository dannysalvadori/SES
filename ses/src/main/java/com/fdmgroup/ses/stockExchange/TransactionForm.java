package com.fdmgroup.ses.stockExchange;

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
	
}
