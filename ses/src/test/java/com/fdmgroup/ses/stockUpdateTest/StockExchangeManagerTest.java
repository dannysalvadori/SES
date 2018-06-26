package com.fdmgroup.ses.stockUpdateTest;

import static org.junit.Assert.*;
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
import com.fdmgroup.ses.utils.DataFactory;

@RunWith(MockitoJUnitRunner.class)
public class StockExchangeManagerTest {
	
	@Mock
	private CompanyRepository companyRepo;
	
	private StockExchangeManager manager = new StockExchangeManager();
	private List<Company> testCompanies = new ArrayList<>();
	
	@Before
	public void setUp() throws Exception {
		Company c1 = DataFactory.createCompany();
		c1.setCurrentShareValue(new BigDecimal(100));
		testCompanies.addAll(new ArrayList<Company>(Arrays.asList(c1)));
		when(companyRepo.findAll()).thenReturn(testCompanies);
	}

	@Test
	public void test() {
		manager.fluctuateValues(companyRepo);
		BigDecimal oldValue = new BigDecimal(100);
		BigDecimal newValue = testCompanies.get(0).getCurrentShareValue();
		// Note: it's possible they are the same value!
		assertTrue(newValue.compareTo(oldValue.multiply(new BigDecimal(1.05))) <= 0);
		assertTrue(newValue.compareTo(oldValue.multiply(new BigDecimal(0.95))) >= 0);
	}

}
