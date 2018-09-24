package com.fdmgroup.ses.serviceTest;

import static com.fdmgroup.ses.testUtils.StockExchangeUtils.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.CompanyRepository;
import com.fdmgroup.ses.repository.OwnedSharesRepository;
import com.fdmgroup.ses.service.OwnedSharesService;
import com.fdmgroup.ses.service.UserService;
import com.fdmgroup.ses.validation.SesValidationException;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class OwnedSharesServiceTest {
	
	// Test data
	private Company c = createCompany();
	private OwnedShare os = createOwnedShare(c);
	private User u;
	
	// Mock services
	@Mock
	private CompanyRepository companyRepo;
	@Mock
	private OwnedSharesRepository ownedSharesRepo;
	@Mock
	private UserService userService;
	
	// Class to be tested
	@InjectMocks
	private OwnedSharesService osService = new OwnedSharesService();

	@Before
	public void setUp() throws Exception {
		when(companyRepo.findBySymbol(c.getSymbol())).thenReturn(c);
	}

	/**
	 * Positive test for upsertOwnedShares() when the OS record already exists
	 */
	@Test
	public void upsertOwnedSharesUpdateTest() throws SesValidationException {
		// Setup such that owned share exists already
		when(ownedSharesRepo.findByOwnerAndCompany(u, c)).thenReturn(os);
		
		// Run
		osService.upsertOwnedShares(c, u);
		
		// Confirm expected behaviour
		verify(ownedSharesRepo).save(os);		
	}

	/**
	 * Positive test for upsertOwnedShares() when the OS is new
	 */
	@Test
	public void upsertOwnedSharesInsertTest() throws SesValidationException {
		// Setup such that owned share is NOT found in db already
		when(ownedSharesRepo.findByOwnerAndCompany(u, c)).thenReturn(null);
		
		// Run
		osService.upsertOwnedShares(c, u);
		
		// Confirm expected behaviour - original OwnedShare is NOT saved, but a new one is
		verify(ownedSharesRepo, times(0)).save(os);
		verify(ownedSharesRepo, times(1)).save((OwnedShare)any());
	}

	/**
	 * findAllForCurrentUser() gets all owned shares owned by the current user
	 */
	@Test
	public void findAllForCurrentUserTest() throws SesValidationException {
		// Stub current user
		when(userService.findCurrentUser()).thenReturn(u);
		
		// Run
		osService.findAllForCurrentUser();
		
		// Confirm expected behaviour - original OwnedShare is NOT saved, but a new one is
		verify(ownedSharesRepo, times(1)).findByOwner(u);
	}

	/**
	 * findBySymbolForCurrentUser() finds shares owned by the current user that match the given symbols
	 */
	@Test
	public void findBySymbolForCurrentUserPositiveTest() throws SesValidationException {
		// Stub current user and setup correct symbols set
		when(userService.findCurrentUser()).thenReturn(u);
		Set<String> symbols = new HashSet<>();
		symbols.add(c.getSymbol());
		
		// Run
		osService.findBySymbolForCurrentUser(symbols);
		
		// Confirm expected behaviour
		verify(ownedSharesRepo, times(1)).findByOwnerAndCompanySymbolIn(u, symbols);
	}

	/**
	 * If the symbols set was empty, findBySymbolForCurrentUser() just finds all for the current user
	 */
	@Test
	public void findBySymbolForCurrentUserUnspecifiedTest() throws SesValidationException {
		// Stub current user and setup empty symbols
		when(userService.findCurrentUser()).thenReturn(u);
		Set<String> symbols = new HashSet<>();
		
		// Run
		osService.findBySymbolForCurrentUser(symbols);
		
		// Confirm expected behaviour
		verify(ownedSharesRepo, times(0)).findByOwnerAndCompanySymbolIn(u, symbols);
		verify(ownedSharesRepo, times(1)).findByOwner(u);
	}

	/**
	 * If the symbols set was null, findBySymbolForCurrentUser() just finds all for the current user
	 */
	@Test
	public void findBySymbolForCurrentUserNullSymbolsTest() throws SesValidationException {
		// Stub current user and setup empty symbols
		when(userService.findCurrentUser()).thenReturn(u);
		Set<String> symbols = null;
		
		// Run
		osService.findBySymbolForCurrentUser(symbols);
		
		// Confirm expected behaviour
		verify(ownedSharesRepo, times(0)).findByOwnerAndCompanySymbolIn(u, symbols);
		verify(ownedSharesRepo, times(1)).findByOwner(u);
	}
	
}
