package com.fdmgroup.ses.serviceTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;

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
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.service.CompanyService;
import com.fdmgroup.ses.service.OwnedSharesService;
import com.fdmgroup.ses.service.TransactionHistoryService;
import com.fdmgroup.ses.service.TransactionService;
import com.fdmgroup.ses.service.UserService;
import com.fdmgroup.ses.stockExchange.SaleForm;
import com.fdmgroup.ses.stockExchange.TransactionForm;
import com.fdmgroup.ses.validation.SaleValidator;
import com.fdmgroup.ses.validation.SesValidationException;
import com.fdmgroup.ses.validation.TransactionValidator;
import com.fdmgroup.ses.validation.ValidatorFactory;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class TransactionServiceTest {
	
	// Test data
	private Company c = createCompany();
	private TransactionForm txForm = createTransactionForm(Arrays.asList(c)); // TX: £50 x 5
	private OwnedShare os = createOwnedShare(c);
	private SaleForm saleForm = createSaleForm(Arrays.asList(os));
	private User u;
	
	// Mock services
	@Mock
	private ValidatorFactory validationFactory;
	@Mock
	private TransactionValidator txValidator;
	@Mock
	private SaleValidator saleValidator;
	@Mock
	private CompanyService companyService;
	@Mock
	private OwnedSharesService ownedSharesService;
	@Mock
	private TransactionHistoryService txHistoryService;
	@Mock
	private UserService userService;
	
	// Class to be tested
	@InjectMocks
	private TransactionService txService = new TransactionService();

	@Before
	public void setUp() throws Exception {
		when(validationFactory.getValidator(txForm)).thenReturn(txValidator);
		when(validationFactory.getValidator(saleForm)).thenReturn(saleValidator);
		when(ownedSharesService.upsertOwnedShares(c, u)).thenReturn(os);
	}

	/**
	 * Positive purchase test scenarios for buyStocks(User, TransactionForm) 
	 * # The company's shares are updated
	 * # The user's owned shares are updated/inserted
	 * # Transaction history is created
	 * # User's credit is updated accordingly
	 */
	@Test
	public void buyStocksPositiveTest() throws SesValidationException {
		
		// Buy stocks without errors
		txService.buyStocks(u, txForm);
		
		// Verify expected behaviour
		verify(companyService, times(1)).updateAvailableShares(c);
		verify(ownedSharesService, times(1)).upsertOwnedShares(c, u);
		verify(txHistoryService, times(1)).createTransactionHistory(c, u, os);
		verify(userService, times(1)).updateCredit(u, txForm);
	}

	/**
	 * Negative purchase test for buyStocks(User, TransactionForm) 
	 * If validation fails, none of the positive-case outcomes should occur
	 */
	@Test
	public void buyStocksNegativeTest() throws SesValidationException {
		
		// Setup validator to fail
		doThrow(new SesValidationException()).when(txValidator).validate();
		
		SesValidationException vEx = null;
		
		// buyStocks() with validation failures
		try {
			txService.buyStocks(u, txForm);
		} catch (SesValidationException e) {
			vEx = e;
		}
		
		// Confirm exception wa thrown and services were not called
		assertNotNull("SES Validation Exception was not thrown", vEx);
		verifyNoMoreInteractions(companyService, ownedSharesService, txHistoryService, userService);
	}

	/**
	 * Positive sale test scenarios for sellStocks(User, SaleForm) 
	 * # The company's shares are updated by the reverse of the transaction quantity
	 * # The user's owned shares are updated/inserted
	 * # Transaction history is created
	 * # User's credit is updated accordingly
	 */
	@Test
	public void sellStocksPositiveTest() throws SesValidationException {
		
		// Note tx quantity before sale (should be reversed internally)
		Long txQuantity = c.getTransactionQuantity();
		
		// Sell stocks without error
		txService.sellStocks(u, saleForm);

		// Verify expected behaviour
		assertEquals("Transaction Quantity was not reversed for sale",
				Long.valueOf(-1*txQuantity), Long.valueOf(c.getTransactionQuantity()));
		verify(companyService, times(1)).updateAvailableShares(c);
		verify(ownedSharesService, times(1)).upsertOwnedShares(c, u);
		verify(txHistoryService, times(1)).createTransactionHistory(c, u, os);
		verify(userService, times(1)).updateCredit(u, saleForm);
	}

	/**
	 * Negative purchase test for sellStocks(User, SaleForm) 
	 * If validation fails, none of the positive-case outcomes should occur
	 */
	@Test
	public void sellStocksNegativeTest() throws SesValidationException {
		
		// Setup validator to fail
		doThrow(new SesValidationException()).when(saleValidator).validate();
		
		SesValidationException vEx = null;
		
		// buyStocks() with validation failures
		try {
			txService.sellStocks(u, saleForm);
		} catch (SesValidationException e) {
			vEx = e;
		}
		
		// Confirm exception wa thrown and services were not called
		assertNotNull("SES Validation Exception was not thrown", vEx);
		verifyNoMoreInteractions(companyService, ownedSharesService, txHistoryService, userService);
	}

}
