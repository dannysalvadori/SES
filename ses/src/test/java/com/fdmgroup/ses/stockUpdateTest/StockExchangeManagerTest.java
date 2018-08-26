package com.fdmgroup.ses.stockUpdateTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.repository.CompanyRepository;
import com.fdmgroup.ses.stockUpdate.StockExchangeManager;
import com.fdmgroup.ses.utils.StockExchangeUtils;

@RunWith(MockitoJUnitRunner.class)
public class StockExchangeManagerTest {
	
	@Mock
	private CompanyRepository companyRepo;
	
	private StockExchangeManager manager = new StockExchangeManager();
	private List<Company> testCompanies = new ArrayList<>();
	
	@Before
	public void setUp() throws Exception {
		Company c1 = StockExchangeUtils.createCompany();
		c1.setCurrentShareValue(new BigDecimal(100));
		testCompanies.addAll(new ArrayList<Company>(Arrays.asList(c1)));
		when(companyRepo.findAll()).thenReturn(testCompanies);
	}

	/**
	 * fluctuateValues increases or reduces the current share value of each company by up to 5%
	 */
	@Test
	public void fluctuateValuesTest() {
		manager.fluctuateValues(companyRepo);
		for (Company c : testCompanies) {
			BigDecimal oldValue = new BigDecimal(100);
			BigDecimal newValue = c.getCurrentShareValue();
			// Note: it's possible they are the same value!
			assertTrue("Value too high", newValue.compareTo(oldValue.multiply(new BigDecimal(1.05))) <= 0);
			assertTrue("Value too low", newValue.compareTo(oldValue.multiply(new BigDecimal(0.95))) >= 0);
			BigDecimal openValue = c.getOpenValue() == null ? new BigDecimal(0) : c.getOpenValue();
			BigDecimal expectedGains = c.getCurrentShareValue().subtract(openValue);
			assertTrue("Wrong gains", expectedGains.compareTo(c.getGains()) == 0);
		}
	}
	
	/**
	 * openStockExchange sets all companies' open values to their current value and reset gains to zero
	 */
	@Test
	public void openStockExchangeTest() {
		manager.openStockExchange(companyRepo);
		verify(companyRepo).save(testCompanies);
		for (Company c : testCompanies) {
			assertTrue("Wrong open value", c.getCurrentShareValue().compareTo(c.getOpenValue()) == 0);
			assertTrue("Wrong gains", new BigDecimal(0).compareTo(c.getGains()) == 0);
		}
	}
	
	/**
	 * openStockExchange sets all companies' open values to their current value and reset gains to zero
	 */
	@Test
	public void closeStockExchangeTest() {
		manager.closeStockExchange(companyRepo);
		verify(companyRepo).save(testCompanies);
		for (Company c : testCompanies) {
			assertTrue("Wrong close value", c.getCurrentShareValue().compareTo(c.getCloseValue()) == 0);
			BigDecimal openValue = c.getOpenValue() == null ? new BigDecimal(0) : c.getOpenValue();
			BigDecimal expectedGains = c.getCurrentShareValue().subtract(openValue);
			assertTrue("Wrong gains", expectedGains.compareTo(c.getGains()) == 0);
		}
	}
	
}
