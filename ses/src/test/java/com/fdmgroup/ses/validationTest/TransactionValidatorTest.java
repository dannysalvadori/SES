package com.fdmgroup.ses.validationTest;

import static org.mockito.Mockito.when;
import static com.fdmgroup.ses.testUtils.CreditCardUtils.*;
import static com.fdmgroup.ses.testUtils.StockExchangeUtils.*;
import static com.fdmgroup.ses.testUtils.UserUtils.*;
import static com.fdmgroup.ses.validation.PurchaseFormValidator.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.CreditCardDetail;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.CompanyRepository;
import com.fdmgroup.ses.service.CreditCardService;
import com.fdmgroup.ses.service.UserService;
import com.fdmgroup.ses.stockExchange.PurchaseForm;
import com.fdmgroup.ses.validation.PurchaseFormValidator;

/**
 * Test for TransactionValidator:
 * # User must have registered at least one credit card
 * # At least one company must be selected
 * # User must have sufficient credit for the whole transaction
 * # Each company must have sufficient stock
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class TransactionValidatorTest extends ValidationTest<PurchaseFormValidator> {
	
	@Mock
	private UserService userService;
	private User u = createUser();
	
	@Mock
	private CreditCardService cardService;
	private List<CreditCardDetail> cards = Arrays.asList(createCard(u));
	
	@Mock
	private CompanyRepository companyRepo;
	
	public TransactionValidatorTest() {
		validator = new PurchaseFormValidator();
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
	public void transactionSuccessTest() {
		PurchaseForm f = createTransactionForm();
		when(cardService.findAllForCurrentUser()).thenReturn(cards);
		when(companyRepo.findBySymbol(f.getCompanies().get(0).getSymbol())).thenReturn(f.getCompanies().get(0));
		validator.setTransactionForm(f);
		// No expected failures
		runTest();
	}
	
	/**
	 * Validation fails if the user does not have any registered credit cards
	 */
	@Test
	public void noCreditCardTest() {
		PurchaseForm f = createTransactionForm();
		// When requested, return empty list of credit cards
		when(cardService.findAllForCurrentUser()).thenReturn(new ArrayList<CreditCardDetail>());
		when(companyRepo.findBySymbol(f.getCompanies().get(0).getSymbol())).thenReturn(f.getCompanies().get(0));
		validator.setTransactionForm(f);
		expectedFailures.add(FAIL_NO_CREDIT_CARDS);
		runTest();
	}

	/**
	 * Validation fails if the user has not selected any companies
	 */
	@Test
	public void noCompaniesSelectedTest() {
		// Set empty transaction form
		PurchaseForm f = new PurchaseForm();
		when(cardService.findAllForCurrentUser()).thenReturn(cards);
		validator.setTransactionForm(f);
		expectedFailures.add(FAIL_NO_STOCK_SELECTED);
		runTest();
	}

	/**
	 * Validation fails if the user doesn't have enough credit
	 */
	@Test
	public void insufficientCreditTest() {
		// Set empty transaction form
		PurchaseForm f = createTransactionForm(); // transaction value is 50
		u.setCredit(new BigDecimal(10)); // Set insufficient credit
		when(cardService.findAllForCurrentUser()).thenReturn(cards);
		when(companyRepo.findBySymbol(f.getCompanies().get(0).getSymbol())).thenReturn(f.getCompanies().get(0));
		validator.setTransactionForm(f);
		expectedFailures.add(generateInsufficientCreditMessage(new BigDecimal(50), new BigDecimal(10)));
		runTest();
	}

	/**
	 * Validation fails if the requested purchase quantity exceeds the available stock
	 */
	@Test
	public void insufficientStockTest() {
		// Set empty transaction form
		PurchaseForm f = createTransactionForm(); // Available shares is 2,000,000
		u.setCredit(new BigDecimal(20000000)); // 15 million credit required
		Company c = f.getCompanies().get(0);
		c.setTransactionQuantity(3000000l); // Set transaction request to exceed availibility
		when(cardService.findAllForCurrentUser()).thenReturn(cards);
		when(companyRepo.findBySymbol(c.getSymbol())).thenReturn(c);
		validator.setTransactionForm(f);
		expectedFailures.add(generateInsufficientStockMessage(c, c));
		runTest();
	}

}
