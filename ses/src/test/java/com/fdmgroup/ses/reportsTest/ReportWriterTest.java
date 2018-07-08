package com.fdmgroup.ses.reportsTest;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.reports.CSVWriter;
import com.fdmgroup.ses.reports.CompanyReport;
import com.fdmgroup.ses.reports.Report;
import com.fdmgroup.ses.reports.ReportBuilder;
import com.fdmgroup.ses.reports.ReportWriter;
import com.fdmgroup.ses.reports.ReportWriterFactory;
import com.fdmgroup.ses.reports.XMLWriter;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ReportWriterTest {
	
	@Before
	public void setUp() throws Exception {
		
	}

	/**
	 * TEST 
	 */
	@Test
	public void goToMyAccountTest() {
		Company company = new Company();
		company.setAvailableShares(188l);
		company.setName("A name...");
		company.setSymbol("");
		
//		System.out.println(ReportWriterUtils.getFieldValue(Company.class, company, "AvailableShares"));
//		System.out.println(ReportWriterUtils.getFieldValue(Company.class, company, "Symbol"));
//		System.out.println(ReportWriterUtils.getFieldValue(Company.class, company, "Name"));
//		System.out.println(ReportWriterUtils.getFieldValue(Company.class, company, "TransactionQuantity"));
		
//		CompanyReport report = new CompanyReport();
		Report<?> report = null;// = new ReportBuilder().buildReport(OwnedShare.class);
//		report.getRowDefinition().getColumnValueMap().put("Symbol", "Symbol");
//		report.getRowDefinition().getColumnValueMap().put("Purchase Quantity", "TransactionQuantity");
//		report.getRowDefinition().getColumnValueMap().put("Stock Name", "Name");
//		report.getRowDefinition().getColumnValueMap().put("Shares Available Now", "AvailableShares");
//		report.getRows().add(company);
		
		report.getRowDefinition().getColumnValueMap().put("Company", "Company");
		report.getRowDefinition().getColumnValueMap().put("Average Purchase Price", "AveragePurchasePrice");
		report.getRowDefinition().getColumnValueMap().put("Quantity Owned", "Quantity");
		
		ReportWriter<?> writer = ReportWriterFactory.getReportWriter("CSV", report);
		
//		CSVWriter<Company> csvWriter = new CSVWriter<>(report, Company.class);
//		XMLWriter<Company> xmlWriter = new XMLWriter<>(report, Company.class);
		
		System.out.println("Here comes the report...");
		System.out.println(writer.writeReport());
		
	}
	
}
