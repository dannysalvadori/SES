package com.fdmgroup.ses.reports;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fdmgroup.ses.model.OwnedShare;

public class OwnedSharesReport extends Report<OwnedShare> {

	public OwnedSharesReport() {
		super();
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
