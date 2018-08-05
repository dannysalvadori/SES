package com.fdmgroup.ses.reports;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fdmgroup.ses.model.Company;

public class CompanyReport extends Report<Company> {

	public CompanyReport() {
		super();
		rowDefinition.putColumnValueMapping("Company Symbol", "Symbol");
		rowDefinition.putColumnValueMapping("Company Name", "Name");
		rowDefinition.putColumnValueMapping("Value", "CurrentShareValue");
		rowDefinition.putColumnValueMapping("Volume", "AvailableShares");
		rowDefinition.putColumnValueMapping("Gains", "Gains");
	}

	@Override
	protected void setTitle() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		title = "Public Stocks Report (" + dtf.format(now) + ")";
	}

	@Override
	public String generateFileName() {
		return title;
	}

}
