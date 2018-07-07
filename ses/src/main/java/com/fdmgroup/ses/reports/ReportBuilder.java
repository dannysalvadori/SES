package com.fdmgroup.ses.reports;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.service.OwnedSharesService;
import com.fdmgroup.ses.utils.DataFactory;

@Component
public class ReportBuilder {
	
	@Autowired
	private OwnedSharesService ownedSharesService;
	
	public Report<?> buildReport(Class<?> type) {
		Report<?> report;
		
		List<OwnedShare> ownedShares = new ArrayList<>();
		ownedShares.add(DataFactory.createOwnedShare());
		ownedShares.add(DataFactory.createOwnedShare());
		ownedShares.add(DataFactory.createOwnedShare());
		
		if (type == Company.class) {
			System.out.println("Wrong");
			report = new CompanyReport();
		} else {
			System.out.println("Hi kids!");
			report = new OwnedSharesReport();
			((Report<OwnedShare>)report)
//					.setRows(ownedSharesService.findAllForCurrentUser());
					.setRows(ownedShares);
		}
		
		return report;
	}
}
