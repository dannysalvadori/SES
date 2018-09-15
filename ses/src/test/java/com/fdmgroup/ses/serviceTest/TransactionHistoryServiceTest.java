package com.fdmgroup.ses.serviceTest;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import static com.fdmgroup.ses.utils.StockExchangeUtils.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.model.TransactionHistory;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.CompanyRepository;
import com.fdmgroup.ses.repository.TransactionHistoryRepository;
import com.fdmgroup.ses.service.TransactionHistoryService;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class TransactionHistoryServiceTest {
	
	// Test data
	private Company c = createCompany();
	private OwnedShare os = createOwnedShare(c);
	private User u;
	private Date oneMonthFuture;
	
	@Mock
	private CompanyRepository companyRepo;
	@Mock
	private TransactionHistoryRepository txHistoryRepo;
	
	// Class to be tested
	@InjectMocks
	private TransactionHistoryService txHistoryService = new TransactionHistoryService();
	private TransactionHistory txHistory;

	@Before
	public void setUp() throws Exception {
		// Rig last trade so we can see if it's set
		Calendar cal = GregorianCalendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		oneMonthFuture = cal.getTime();
		c = createCompany();
		c.setLastTrade(oneMonthFuture);
		when(companyRepo.findBySymbol(c.getSymbol())).thenReturn(c);
	}

	/**
	 * ...
	 */
	@Test
	public void createTransactionHistoryTest() {
		
		txHistory = txHistoryService.createTransactionHistory(c, u, os);
		
		// Confirm expected behaviour
		verify(txHistoryRepo, times(1)).save((TransactionHistory)any());
		assertEquals("Wrong company", c, txHistory.getCompany());
		assertEquals("Wrong company", u, txHistory.getOwner());
		assertEquals("Wrong owned share", os, txHistory.getOwnedShare());
		assertTrue("Wrong company",
				GregorianCalendar.getInstance().getTime().getTime() > txHistory.getExchangeDate().getTime());
		assertEquals("Wrong unit price", c.getCurrentShareValue(), txHistory.getUnitPrice());
		assertEquals("Wrong quantity", c.getTransactionQuantity(), txHistory.getQuantity());
		assertEquals("Wrong transaction value", c.getTransactionValue(), txHistory.getValue());
	}
	
}
