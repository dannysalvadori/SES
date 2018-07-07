package com.fdmgroup.ses.controllerTest;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.anything;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.controller.StockExchangeController;
import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.CompanyRepository;
import com.fdmgroup.ses.service.OwnedSharesService;
import com.fdmgroup.ses.service.TransactionService;
import com.fdmgroup.ses.service.UserService;
import com.fdmgroup.ses.service.UserServiceImpl;
import com.fdmgroup.ses.stockExchange.SaleForm;
import com.fdmgroup.ses.stockExchange.TransactionForm;
import com.fdmgroup.ses.utils.DataFactory;
import com.fdmgroup.ses.validation.SaleValidator;
import com.fdmgroup.ses.validation.SesValidationException;
import com.fdmgroup.ses.validation.TransactionValidator;
import com.fdmgroup.ses.validation.ValidationFactory;

@RunWith(MockitoJUnitRunner.class) // Unlike SpringRunner.class, allows Mock-Autowired injection
@SpringBootTest
public class StockExchangeControllerTest {

	// Controller and stubbed dependencies
	@InjectMocks // Injects Mocks for @Autowired properties
	private StockExchangeController ctrl = new StockExchangeController();
	
	@Mock
	private CompanyRepository companyRepo;
	
	@Mock
	private OwnedSharesService ownedSharesService;
	
	@Mock
	private TransactionService transactionService;
	
	@Mock
	private UserServiceImpl userService;
	private static User stubCurrentUser = new User();
	
	@Mock
	private ValidationFactory validationFactory;
	@Mock
	private TransactionValidator stubTransactionValidator;
	@Mock
	private SaleValidator stubSaleValidator;
	
	// Test data
	private List<Company> stubAllCompanies = new ArrayList<>();
	private List<OwnedShare> stubCurrentUsersShares = new ArrayList<>();
	private ModelAndView mav = new ModelAndView();
	
	@Before
	public void setUp() throws Exception {
	
		Company c1 = new Company();
		c1.setAvailableShares(100l);
		stubAllCompanies.add(c1);
		when(companyRepo.findAll()).thenReturn(stubAllCompanies);
		
		OwnedShare os1 = new OwnedShare();
		os1.setCompany(c1);
		os1.setQuantity(20l);
		stubCurrentUsersShares.add(os1);
		when(ownedSharesService.findAllForCurrentUser()).thenReturn(stubCurrentUsersShares);
		
		// Stub validators
		when(validationFactory.getValidator(any(TransactionForm.class))).thenReturn(stubTransactionValidator);
		when(validationFactory.getValidator(any(SaleForm.class))).thenReturn(stubSaleValidator);
		
		when(userService.findCurrentUser()).thenReturn(stubCurrentUser);
	}
	

	/*******************************************
	*               Tests Start                *
	*******************************************/

	/**
	 * goToStockExchangeTest() adds new TransactionForm with all available companies under "transactionForm", adds all
	 * of the current user's owned shares to a SaleForm ("saleForm"), and sets the view to user/stockExchange.jsp
	 */
	@Test
	public void goToStockExchangeTest() {
		mav = ctrl.goToStockExchange(mav);
		
		// Confirm view
		assertEquals("Wrong view name", "user/stockExchange", mav.getViewName());
		
		// Confirm transactionForm was added with all available companies
		verify(companyRepo, times(1)).findAll();
		Object transactionFormObject = mav.getModel().get("transactionForm");
		assertNotEquals("transactionForm model object shouldn't be null", null, transactionFormObject);
		assertTrue("transactionForm object is wrong type", transactionFormObject instanceof TransactionForm);
		TransactionForm txForm = (TransactionForm) transactionFormObject;
		assertEquals("Wrong number of companies", stubAllCompanies.size(), txForm.getCompanies().size());
		assertEquals("Wrong number of available shares",
				Long.valueOf(100), Long.valueOf(txForm.getCompanies().get(0).getAvailableShares()));
		
		// Confirm saleForm was added with all ownedShares for the logged in user
		verify(ownedSharesService, times(1)).findAllForCurrentUser();		
		Object saleFormObject = mav.getModel().get("saleForm");
		assertNotEquals("saleForm model object shouldn't be null", null, saleFormObject);
		assertTrue("saleForm object is wrong type", saleFormObject instanceof SaleForm);
		SaleForm saleForm = (SaleForm) saleFormObject;
		assertEquals("Wrong number of owned shares", stubCurrentUsersShares.size(), saleForm.getOwnedShares().size());
		assertEquals("Wrong number of owned shares",
				Long.valueOf(20), Long.valueOf(saleForm.getOwnedShares().get(0).getQuantity()));
	}

	/**
	 * goToStockExchangeTest() doesn't add companies or owned shares to the purchase/sale forms if the available/owned
	 * quantity is less than 1
	 */
	@Test
	public void goToStockExchangeNoDataTest() {
		
		// Override default data stub
		List<Company> stubCompaniesWithNoShares = new ArrayList<>();
		Company c1 = new Company();
		c1.setAvailableShares(0l);
		stubCompaniesWithNoShares.add(c1);
		when(companyRepo.findAll()).thenReturn(stubCompaniesWithNoShares);
		
		List<OwnedShare> stubOwnedSharesWithNoQuantity = new ArrayList<>();
		OwnedShare os1 = new OwnedShare();
		os1.setCompany(c1);
		os1.setQuantity(0l);
		stubOwnedSharesWithNoQuantity.add(os1);
		when(ownedSharesService.findAllForCurrentUser()).thenReturn(stubOwnedSharesWithNoQuantity);
		
		// Perform test
		mav = ctrl.goToStockExchange(mav);
		
		// Confirm transactionForm was added with all available companies
		verify(companyRepo, times(1)).findAll();
		Object transactionFormObject = mav.getModel().get("transactionForm");
		assertNotEquals("transactionForm model object shouldn't be null", null, transactionFormObject);
		assertTrue("transactionForm object is wrong type", transactionFormObject instanceof TransactionForm);
		TransactionForm txForm = (TransactionForm) transactionFormObject;
		assertEquals("Wrong number of companies", 0, txForm.getCompanies().size());
		
		// Confirm saleForm was added with all ownedShares for the logged in user
		verify(ownedSharesService, times(1)).findAllForCurrentUser();		
		Object saleFormObject = mav.getModel().get("saleForm");
		assertNotEquals("saleForm model object shouldn't be null", null, saleFormObject);
		assertTrue("saleForm object is wrong type", saleFormObject instanceof SaleForm);
		SaleForm saleForm = (SaleForm) saleFormObject;
		assertEquals("Wrong number of owned shares", 0, saleForm.getOwnedShares().size());
	}

	/**
	 * doPlaceOrder() refines the transactionForm to only those companies that have been selected. If validation
	 * succeeds, the view is set to user/confirmPurchase.jsp, the refined form is added under "purchaseForm", and the
	 * transaction total is added under "total" 
	 */
	@Test
	public void doPlaceOrderSuccessTest() {
		/* Generate transaction form with five companies:
			- 2: selected is TRUE and quantity is OK
			- 1: selected is TRUE but quantity is ZERO,
			- 1: selected is FALSE, though quantity is OK
			- 1: selected is FALSE and quantity is ZERO
			- 1: selected is NULL
			- 1: selected is TRUE but quantity is NULL
		*/ 
		Company c1 = DataFactory.createCompany();
		c1.setSelected(true);
		c1.setTransactionQuantity(5l);
		c1.setCurrentShareValue(new BigDecimal(3.1)); // tx value: 15.50
		Company c2 = DataFactory.createCompany();
		c2.setSelected(true);
		c2.setTransactionQuantity(8l);
		c2.setCurrentShareValue(new BigDecimal(5.01)); // tx value: 40.08
		Company c3 = DataFactory.createCompany();
		c3.setSelected(true);
		c3.setTransactionQuantity(0l);
		c3.setCurrentShareValue(new BigDecimal(100)); // Not selected, value is irrelevant
		Company c4 = DataFactory.createCompany();
		c4.setSelected(false);
		c4.setTransactionQuantity(10l);
		c4.setCurrentShareValue(new BigDecimal(100)); // Not selected, value is irrelevant
		Company c5 = DataFactory.createCompany();
		c5.setSelected(false);
		c5.setTransactionQuantity(0l);
		c5.setCurrentShareValue(new BigDecimal(100)); // Not selected, value is irrelevant
		Company c6 = DataFactory.createCompany();
		c6.setSelected(null);
		c6.setTransactionQuantity(0l);
		c6.setCurrentShareValue(new BigDecimal(100)); // Not selected, value is irrelevant
		Company c7 = DataFactory.createCompany();
		c7.setSelected(true);
		c7.setTransactionQuantity(null);
		c7.setCurrentShareValue(new BigDecimal(100)); // Not selected, value is irrelevant
		
		List<Company> testCompanies = new ArrayList<>();
		testCompanies.addAll(new ArrayList<Company>(Arrays.asList(c1, c2, c3, c4, c5, c6, c7)));
		
		TransactionForm txForm = new TransactionForm();
		txForm.setCompanies(testCompanies); // Total tx value: 55.58
		BigDecimal expectedTotal = new BigDecimal(55.58).setScale(2, RoundingMode.HALF_UP);
		
		// Run test
		mav = ctrl.doPlaceOrder(mav, txForm);
		
		// Confirm view
		assertEquals("Wrong view name", "user/confirmPurchase", mav.getViewName());
		
		// Confirm total was added
		Object totalObject = mav.getModel().get("total");
		assertNotEquals("total model object shouldn't be null", null, totalObject);
		assertTrue("total object is wrong type", totalObject instanceof BigDecimal);
		BigDecimal total = ((BigDecimal) totalObject).setScale(2, RoundingMode.HALF_UP);
		assertEquals("Wrong total value", expectedTotal, total);
	}

	/**
	 * If validation fails, doPlaceOrder() sets the view back to the stock exchange and adds an error String under
	 * "purchaseFailures"
	 */
	@Test
	public void doPlaceOrderValidationFailTest() {
		
		TransactionForm txForm = new TransactionForm();
		BigDecimal expectedTotal = new BigDecimal(55.58).setScale(2, RoundingMode.HALF_UP);
		
		SesValidationException stubValidationException = new SesValidationException();
		Set<String> stubFailures = new HashSet<>();
		String testFailureMessage = "Test stub failure";
		stubFailures.add(testFailureMessage);
		stubValidationException.setFailures(stubFailures);
		
		// TODO: how can we make the validator throw a failure?
		try {
			doThrow(stubValidationException).when(stubTransactionValidator).validate();
			// Run test
			mav = ctrl.doPlaceOrder(mav, txForm);
			verify(validationFactory).getValidator(any());
//			verify(stubTransactionValidator).validate();
		} catch (SesValidationException e) {
			assertTrue("Controller did not handle validation exception", false);
		}
		
//		// Confirm view
//		assertEquals("Wrong view name", "user/stockExchange", mav.getViewName());
//		
//		// Confirm errors were added to model
//		Object failuresObject = mav.getModel().get("purchaseFailures");
//		assertNotEquals("purchaseFailures model object shouldn't be null", null, failuresObject);
//		assertTrue("purchaseFailures object is wrong type", failuresObject instanceof Set<?>);
//		Set<String> purchaseFailures = (Set<String>) failuresObject;
//		assertEquals("Wrong number of failures", 1, purchaseFailures.size());
//		for (String failure : purchaseFailures) {
//			assertEquals("Wrong failure", testFailureMessage, failure);
//		}
	}

	/**
	 * goToAuthenticatePurchase() adds TransactionForm with selected shares for purchase under "transactionForm", and
	 * sets the view to user/authenticatePurchase.jsp
	 */
	@Test
	public void goToAuthenticatePurchaseTest() {
		
		TransactionForm txForm = DataFactory.createTransactionForm();
		mav = ctrl.goToAuthenticatePurchase(mav, txForm);
		
		// Confirm view
		assertEquals("Wrong view name", "user/authenticatePurchase", mav.getViewName());
		
		// Confirm transactionForm was added with all available companies
		Object transactionFormObject = mav.getModel().get("transactionForm");
		assertNotEquals("transactionForm model object shouldn't be null", null, transactionFormObject);
		assertTrue("transactionForm object is wrong type", transactionFormObject instanceof TransactionForm);
		txForm = (TransactionForm) transactionFormObject;
		assertEquals("Wrong number of companies", 1, txForm.getCompanies().size());
		assertEquals("Wrong number of available shares",
				Long.valueOf(100), Long.valueOf(txForm.getCompanies().get(0).getAvailableShares()));
	}

	/**
	 * doPurchase() sets the view to user/purchaseComplete.jsp if validation succeeds
	 */
	@Test
	public void doPurchaseSuccessTest() {

		TransactionForm txForm = DataFactory.createTransactionForm();
		mav = ctrl.doPurchase(mav, txForm);
		
		// Confirm view
		assertEquals("Wrong view name", "user/purchaseComplete", mav.getViewName());
		
		try {
			verify(transactionService).buyStocks(stubCurrentUser, txForm);
		} catch (SesValidationException e) {
			assertTrue("Controller didn't handle ValidationException", false);
		}
	}

	/**
	 * doPurchase() sets the view to user/purchaseFailed.jsp if validation fails
	 */
	@Test
	public void doPurchaseFailureTest() {

		// TODO: how to force validator to throw a failure?
		
//		TransactionForm txForm = DataFactory.createTransactionForm();
//		mav = ctrl.doPurchase(mav, txForm);
//		
//		// Confirm view
//		assertEquals("Wrong view name", "user/purchaseComplete", mav.getViewName());
//		
//		try {
//			verify(transactionService).buyStocks(stubCurrentUser, txForm);
//		} catch (SesValidationException e) {
//			assertTrue("Controller didn't handle ValidationException", false);
//		}
	}

	/**
	 * goToSellSelected() refines the saleForm to only those owned shares that have been selected. If validation
	 * succeeds, the view is set to user/confirmSale.jsp, the refined form is added under "saleForm", and the
	 * transaction total is added under "total" 
	 */
	@Test
	public void goToSellSelectedSuccessTest() {
		/* Generate transaction form with five companies:
			- 2: selected is TRUE and quantity is OK
			- 1: selected is TRUE but quantity is ZERO,
			- 1: selected is FALSE, though quantity is OK
			- 1: selected is FALSE and quantity is ZERO
			- 1: selected is NULL
			- 1: selected is TRUE but quantity is NULL
		*/ 
		Company c1 = DataFactory.createCompany();
		c1.setSelected(true);
		c1.setTransactionQuantity(5l);
		c1.setCurrentShareValue(new BigDecimal(3.1)); // tx value: 15.50
		Company c2 = DataFactory.createCompany();
		c2.setSelected(true);
		c2.setTransactionQuantity(8l);
		c2.setCurrentShareValue(new BigDecimal(5.01)); // tx value: 40.08
		Company c3 = DataFactory.createCompany();
		c3.setSelected(true);
		c3.setTransactionQuantity(0l);
		c3.setCurrentShareValue(new BigDecimal(100)); // Not selected, value is irrelevant
		Company c4 = DataFactory.createCompany();
		c4.setSelected(false);
		c4.setTransactionQuantity(10l);
		c4.setCurrentShareValue(new BigDecimal(100)); // Not selected, value is irrelevant
		Company c5 = DataFactory.createCompany();
		c5.setSelected(false);
		c5.setTransactionQuantity(0l);
		c5.setCurrentShareValue(new BigDecimal(100)); // Not selected, value is irrelevant
		Company c6 = DataFactory.createCompany();
		c6.setSelected(null);
		c6.setTransactionQuantity(0l);
		c6.setCurrentShareValue(new BigDecimal(100)); // Not selected, value is irrelevant
		Company c7 = DataFactory.createCompany();
		c7.setSelected(true);
		c7.setTransactionQuantity(null);
		c7.setCurrentShareValue(new BigDecimal(100)); // Not selected, value is irrelevant
		
		List<Company> testCompanies = new ArrayList<>();
		testCompanies.addAll(new ArrayList<Company>(Arrays.asList(c1, c2, c3, c4, c5, c6, c7)));
		
		List<OwnedShare> ownedShares = new ArrayList<>();
		for (Company c : testCompanies) {
			OwnedShare newOwnedShare = DataFactory.createOwnedShare(c);
			newOwnedShare.setSelected(c.getSelected());
			ownedShares.add(newOwnedShare);
		}
		
		SaleForm saleForm = new SaleForm();
		saleForm.setOwnedShares(ownedShares); // Total tx value: 55.58
		BigDecimal expectedTotal = new BigDecimal(55.58).setScale(2, RoundingMode.HALF_UP);
		
		// Run test
		mav = ctrl.goToSellSelected(mav, saleForm);
		
		// Confirm view
		assertEquals("Wrong view name", "user/confirmSale", mav.getViewName());
		
		// Confirm total was added
		Object totalObject = mav.getModel().get("total");
		assertNotEquals("total model object shouldn't be null", null, totalObject);
		assertTrue("total object is wrong type", totalObject instanceof BigDecimal);
		BigDecimal total = ((BigDecimal) totalObject).setScale(2, RoundingMode.HALF_UP);
		assertEquals("Wrong total value", expectedTotal, total);
	}

	/**
	 * If validation fails, goToSellSelected() returns to the stock exchange and adds failures under "saleFailures" 
	 */
	@Test
	public void goToSellSelectedFailureTest() {
		// TODO: test validation failure
	}

	/**
	 * doSale() sets view to user/saleComplete.jsp on validation success, and calls the transaction service to sell 
	 */
	@Test
	public void doSaleSuccessTest() {
		
		SaleForm saleForm = DataFactory.createSaleForm();
		mav = ctrl.doSale(mav, saleForm);
		
		// Confirm view
		assertEquals("Wrong view name", "user/saleComplete", mav.getViewName());
		
		try {
			verify(transactionService).sellStocks(stubCurrentUser, saleForm);
		} catch (SesValidationException e) {
			assertTrue("Controller didn't handle ValidationException", false);
		}
	}

	/**
	 * If validation fails, doSale() sets the view to user/saleFailed.jsp with failures added under "failures" 
	 */
	@Test
	public void doSaleFailureTest() {
		// TODO: test validation failure
	}	

}