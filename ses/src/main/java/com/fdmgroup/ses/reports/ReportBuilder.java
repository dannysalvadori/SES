package com.fdmgroup.ses.reports;

import org.springframework.stereotype.Component;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.service.CompanyService;
import com.fdmgroup.ses.service.OwnedSharesService;

@Component
public class ReportBuilder {
	
	private OwnedSharesService ownedSharesService;
	private CompanyService companyService;
	
	public ReportBuilder(OwnedSharesService ownedSharesService, CompanyService companyService) {
		this.ownedSharesService = ownedSharesService;
		this.companyService = companyService;
	}
	
	public Report<?> buildReport(Class<?> type) {
		Report<?> report;
		
		if (type == Company.class) {
			report = new CompanyReport();
			((Report<Company>)report)
					.setRows(companyService.findAll()); // TODO: apply filters
		} else {
			report = new OwnedSharesReport();
			((Report<OwnedShare>)report)
					.setRows(ownedSharesService.findAllForCurrentUser());
		}
		
		return report;
	}
}
