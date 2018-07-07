package com.fdmgroup.ses.reports;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

	@Override
	public String generateTitle() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}

}
