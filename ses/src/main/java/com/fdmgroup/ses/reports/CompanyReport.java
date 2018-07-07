package com.fdmgroup.ses.reports;

import java.util.List;

import com.fdmgroup.ses.model.Company;

public class CompanyReport extends Report<Company> {

	public CompanyReport() {
		super();
	}
	
	public List<Company> getRows() {
		return rows;
	}

	public RowDefinition getRowDefinition() {
		return rowDefinition;
	}

}
