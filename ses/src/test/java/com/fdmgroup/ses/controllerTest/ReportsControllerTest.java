package com.fdmgroup.ses.controllerTest;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.when;

import static com.fdmgroup.ses.utils.StockExchangeUtils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.controller.ReportsController;
import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.reports.ReportForm;
import com.fdmgroup.ses.service.CompanyService;
import com.fdmgroup.ses.service.OwnedSharesService;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ReportsControllerTest {
	
	@InjectMocks
	private ReportsController ctrl = new ReportsController();
	private ModelAndView mav = new ModelAndView();
	
	@Mock
	private CompanyService companyService;
	private List<Company> allCompanies = new ArrayList<>();
	
	@Autowired
	private OwnedSharesService ownedSharesService;
	
	@Mock
	private HttpServletResponse httpResponse;
	
	@Before
	public void setUp() throws Exception {
		setupCompanies();
		when(companyService.findBySymbol(any())).thenReturn(allCompanies);
	}
	
	private void setupCompanies() {
		for (String symbol : Arrays.asList("LSE", "TKSE", "NYSE")) {
			Company c = createCompany();
			c.setSymbol(symbol);
		}
	}
	
	/**
	 * goToReports() sets the view name to user/Reports.jsp and adds form objects to the model
	 */
	@Test
	public void goToReportsTest() {
		mav = ctrl.goToReports(mav);
		assertEquals("Wrong view name", "user/reports", mav.getViewName());
		assertTrue("", mav.getModel().get("reportForm") instanceof ReportForm);
		assertTrue("", mav.getModel().get("reportFormats") instanceof Set<?>);
		assertTrue("", mav.getModel().get("reportTypes") instanceof Set<?>);
		assertTrue("", mav.getModel().get("availableStockSymbols") instanceof Set<?>);
	}
	
	/**
	 * requestReport() interprets the ReportForm and returns a ResponseEntity
	 */
	@Test
	public void requestReportTest() {
		ReportForm reportForm = new ReportForm(companyService);
		reportForm.setFormat("CSV");
		reportForm.setType("Public Stocks");
		
		ResponseEntity<?> response = ctrl.requestReport(mav, httpResponse, reportForm);
		assertNotEquals("Response was null", null, response);
	}
	
	
	
}
