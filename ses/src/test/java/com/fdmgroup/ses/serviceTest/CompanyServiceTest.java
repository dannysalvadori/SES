package com.fdmgroup.ses.serviceTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import static com.fdmgroup.ses.utils.StockExchangeUtils.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.repository.CompanyRepository;
import com.fdmgroup.ses.service.CompanyService;
import com.fdmgroup.ses.validation.SesValidationException;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class CompanyServiceTest {
	
	// Test data
	private Company c;
	private Date oneMonthAgo;
	
	@Mock
	private CompanyRepository companyRepo;
	
	// Class to be tested
	@InjectMocks
	private CompanyService companyService = new CompanyService();

	@Before
	public void setUp() throws Exception {
		Calendar cal = GregorianCalendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		oneMonthAgo = cal.getTime();
		c = createCompany();
		c.setLastTrade(oneMonthAgo); // set last trade to a month ago
		when(companyRepo.findBySymbol(c.getSymbol())).thenReturn(c);
	}

	/**
	 * Test cases for updateAvailableShares(Company)
	 * # Subtracts the input tx quantity from the available shares of the company in db
	 * # The company's last trade time is set to now
	 */
	@Test
	public void updateAvailableSharesTest() throws SesValidationException {
		
		Long expectedRemainingShares = c.getAvailableShares()-c.getTransactionQuantity();
		
		companyService.updateAvailableShares(c);
		
		// Confirm expected updates and behaviour
		assertNotEquals("Last Trade date was not updated", oneMonthAgo, c.getLastTrade());
		assertEquals("Wrong number of available shares", expectedRemainingShares, c.getAvailableShares());
		verify(companyRepo, times(1)).save(c);
	}

	/**
	 * Test case for findAll()
	 */
	@Test
	public void findAllTest() throws SesValidationException {
		
		companyService.findAll();
		
		// Confirm expected updates and behaviour
		verify(companyRepo, times(1)).findAll();
	}

	/**
	 * Test case for findBySymbol() when symbols match
	 */
	@Test
	public void findBySymbolPositiveTest() throws SesValidationException {
		Set<String> symbols = new HashSet<>();
		symbols.add(c.getSymbol());
		companyService.findBySymbol(symbols);
		verify(companyRepo, times(1)).findBySymbolIn(symbols);
	}

	/**
	 * Test case for findBySymbol() when symbols set is empty
	 */
	@Test
	public void findBySymbolEmptyTest() throws SesValidationException {
		Set<String> symbols = new HashSet<>();
		companyService.findBySymbol(symbols);
		verify(companyRepo, times(0)).findBySymbolIn(symbols);
		verify(companyRepo, times(1)).findAll();
	}

	/**
	 * Test case for findBySymbol() when symbols set is null
	 */
	@Test
	public void findBySymbolNullTest() throws SesValidationException {
		Set<String> symbols = null;
		companyService.findBySymbol(symbols);
		verify(companyRepo, times(0)).findBySymbolIn(symbols);
		verify(companyRepo, times(1)).findAll();
	}
	
}
