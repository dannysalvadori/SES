package com.fdmgroup.ses.reportsTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.fdmgroup.ses.testUtils.StockExchangeUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.reports.Report;
import com.fdmgroup.ses.reports.ReportBuilder;
import com.fdmgroup.ses.reports.ReportForm;
import com.fdmgroup.ses.service.CompanyService;
import com.fdmgroup.ses.service.OwnedSharesService;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ReportBuilderTest {

	@InjectMocks
	private ReportBuilder reportBuilder;
	@Mock
	private OwnedSharesService ownedSharesService;
	@Mock
	private CompanyService companyService;
	private ReportForm reportForm = new ReportForm();
	private Report<?> report = null;
	
	private List<Company> companies = new ArrayList<>();
	private List<OwnedShare> ownedShares = new ArrayList<>();
	private Set<String> stockSymbols = new HashSet<>();
	
	public ReportBuilderTest() {
		companies.add(createCompany());
		for (Company c : companies) {
			stockSymbols.add(c.getSymbol());
			ownedShares.add(createOwnedShare(c));
		}
		reportBuilder = new ReportBuilder(ownedSharesService, companyService);
	}
	
	@Before
	public void setUp() throws Exception {
		when(companyService.findBySymbol(stockSymbols)).thenReturn(companies);
		when(ownedSharesService.findBySymbolForCurrentUser(stockSymbols)).thenReturn(ownedShares);
	}

	/**
	 * If the given ReportForm's type is Company.class, the output report uses all Company's with the selected symbols
	 */
	@Test
	public void companyTest() {
		reportForm.setFormat("XML");
		reportForm.setStockSymbols(stockSymbols);
		reportForm.setType(ReportForm.TYPE_COMPANY);
		report = reportBuilder.buildReport(reportForm);
		verify(companyService, times(1)).findBySymbol(stockSymbols);
		verify(ownedSharesService, times(0)).findBySymbolForCurrentUser(stockSymbols);
		assertEquals("Report had the wrong number of rows", stockSymbols.size(), report.getRows().size());
	}

	/**
	 * If the given ReportForm's type isn't Company.class, the output report uses the current user's owned shares,
	 * limited to the selected stock symbols
	 */
	@Test
	public void ownedShareTest() {
		reportForm.setFormat("CSV");
		reportForm.setStockSymbols(stockSymbols);
		reportForm.setType(ReportForm.TYPE_OWNED_SHARE);
		report = reportBuilder.buildReport(reportForm);
		verify(ownedSharesService, times(1)).findBySymbolForCurrentUser(stockSymbols);
		verify(companyService, times(0)).findBySymbol(stockSymbols);
		assertEquals("Report had the wrong number of rows", stockSymbols.size(), report.getRows().size());
	}
	
}
