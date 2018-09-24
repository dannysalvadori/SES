package com.fdmgroup.ses.validationTest;

import static org.mockito.Mockito.when;
import static com.fdmgroup.ses.testUtils.StockExchangeUtils.*;
import static com.fdmgroup.ses.testUtils.UserUtils.*;
import static com.fdmgroup.ses.validation.SaleFormValidator.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.CompanyRepository;
import com.fdmgroup.ses.repository.OwnedSharesRepository;
import com.fdmgroup.ses.service.UserService;
import com.fdmgroup.ses.stockExchange.SaleForm;
import com.fdmgroup.ses.validation.SaleFormValidator;

/**
 * Test for SaleValidator:
 * # At least one stock must be selected for sale with quantity 1 or greater
 * # You cannot sell more of a stock than you own
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class SaleValidatorTest extends ValidationTest<SaleFormValidator> {
	
	@Mock
	private UserService userService;
	private User u = createUser();
	
	@Mock
	private OwnedSharesRepository ownedSharesRepo;
	
	@Mock
	private CompanyRepository companyRepo;
	
	public SaleValidatorTest() {
		validator = new SaleFormValidator();
	}
	
	@Before
	public void setup() {
		when(userService.findCurrentUser()).thenReturn(u);
	}
	
	/************************************************
	*                 Insert Tests                  *
	************************************************/

	/**
	 * If validation passes all criteria, no failures are thrown
	 */
	@Test
	public void saleSuccessTest() {
		SaleForm f = createSaleForm();
		OwnedShare os = f.getOwnedShares().get(0);
		Company c = os.getCompany();
		when(companyRepo.findBySymbol(c.getSymbol())).thenReturn(c);
		when(ownedSharesRepo.findByOwnerAndCompany(u, c)).thenReturn(os);
		validator.setSaleForm(f);
		// No expected failures
		runTest();
	}
	
	/**
	 * Validation fails if no shares are selected for sale
	 */
	@Test
	public void noSharesSelectedTest() {
		// Create blank SaleForm
		SaleForm f = new SaleForm();
		validator.setSaleForm(f);
		expectedFailures.add(FAIL_NO_STOCK_SELECTED);
		runTest();
	}
	
	/**
	 * Validation fails if no shares are selected with 0 quantity
	 */
	@Test
	public void zeroQuantitySelectedTest() {
		SaleForm f = createSaleForm();
		OwnedShare os = f.getOwnedShares().get(0);
		Company c = os.getCompany();
		c.setTransactionQuantity(0l);
		when(companyRepo.findBySymbol(c.getSymbol())).thenReturn(c);
		when(ownedSharesRepo.findByOwnerAndCompany(u, c)).thenReturn(os);
		validator.setSaleForm(f);
		expectedFailures.add(FAIL_NO_STOCK_SELECTED);
		runTest();
	}
	
	/**
	 * Validation fails if you try to sell more than you own
	 */
	@Test
	public void quantityTooLargeTest() {
		SaleForm f = createSaleForm();
		OwnedShare os = f.getOwnedShares().get(0);
		os.setQuantity(50l); // You own 50
		Company c = os.getCompany();
		c.setTransactionQuantity(100l); // You try to sell 100
		when(companyRepo.findBySymbol(c.getSymbol())).thenReturn(c);
		when(ownedSharesRepo.findByOwnerAndCompany(u, c)).thenReturn(os);
		validator.setSaleForm(f);
		expectedFailures.add(generateInsufficientOwnedStockMessage(c, 50l));
		runTest();
	}
	
}
