package com.fdmgroup.ses.stockUpdateTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;

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
	
	@Test
	public void testScheduler(){
	    // to test if a cron expression runs only from Monday to Friday
	    org.springframework.scheduling.support.CronTrigger trigger = 
	                                      new CronTrigger("0 */5 8-16 * * *");
	    Calendar today = Calendar.getInstance();
	    today.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);

	    SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss EEEE"); 
	    final Date yesterday = today.getTime();
	    System.out.println("Yesterday was : " + df.format(yesterday));
	    Date nextExecutionTime = trigger.nextExecutionTime(
	        new TriggerContext() {

	            @Override
	            public Date lastScheduledExecutionTime() {
	                return yesterday;
	            }

	            @Override
	            public Date lastActualExecutionTime() {
	                return yesterday;
	            }

	            @Override
	            public Date lastCompletionTime() {
	                return yesterday;
	            }
	        });

	    String message = "Next Execution date: " + df.format(nextExecutionTime);
	    System.out.println(message);

	}

}
