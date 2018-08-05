package com.fdmgroup.ses.reports;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fdmgroup.ses.model.OwnedShare;

public class OwnedSharesReport extends Report<OwnedShare> {

	public OwnedSharesReport() {
		super();
		rowDefinition.putColumnValueMapping("Company Symbol", "Company.Symbol");
		rowDefinition.putColumnValueMapping("Company Name", "Company.Name");
		rowDefinition.putColumnValueMapping("Average Purchase Price", "AveragePurchasePrice");
		rowDefinition.putColumnValueMapping("Quantity Owned", "Quantity");
		rowDefinition.putColumnValueMapping("Gains", "Gains");
	}

	@Override
	protected void setTitle() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		title = "Owned Stocks Report (" + dtf.format(now) + ")";
	}

	@Override
	public String generateFileName() {
		return title;
	}

}
