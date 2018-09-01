package com.fdmgroup.ses.validationTest;

import static org.mockito.Mockito.when;
import static com.fdmgroup.ses.utils.CreditCardUtils.*;
import static com.fdmgroup.ses.utils.StockExchangeUtils.*;
import static com.fdmgroup.ses.utils.UserUtils.*;
import static com.fdmgroup.ses.validation.TransactionValidator.*;

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
import com.fdmgroup.ses.stockExchange.TransactionForm;
import com.fdmgroup.ses.validation.TransactionValidator;

/**
 * Test for TransactionValidator:
 * # User must have registered at least one credit card
 * # At least one company must be selected
 * # User must have sufficient credit for the whole transaction
 * # Each company must have sufficient stock
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class TransactionValidatorTest extends ValidationTest<TransactionValidator> {
	
	@Mock
	private UserService userService;
	private User u = createUser();
	
	@Mock
	private CreditCardService cardService;
	private List<CreditCardDetail> cards = Arrays.asList(createCard(u));
	
	@Mock
	private CompanyRepository companyRepo;
	
	public TransactionValidatorTest() {
		validator = new TransactionValidator();
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
		TransactionForm f = createTransactionForm();
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
		TransactionForm f = createTransactionForm();
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
		TransactionForm f = new TransactionForm();
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
		TransactionForm f = createTransactionForm(); // transaction value is 500
		u.setCredit(new BigDecimal(100)); // Set insufficient credit
		when(cardService.findAllForCurrentUser()).thenReturn(cards);
		when(companyRepo.findBySymbol(f.getCompanies().get(0).getSymbol())).thenReturn(f.getCompanies().get(0));
		validator.setTransactionForm(f);
		expectedFailures.add(generateInsufficientCreditMessage(new BigDecimal(500), new BigDecimal(100)));
		runTest();
	}

	/**
	 * Validation fails if the requested purchase quantity exceeds the available stock
	 */
	@Test
	public void insufficientStockTest() {
		// Set empty transaction form
		TransactionForm f = createTransactionForm(); // Available shares is 100
		Company c = f.getCompanies().get(0);
		c.setTransactionQuantity(110l); // Set transaction request to exceed 100
		when(cardService.findAllForCurrentUser()).thenReturn(cards);
		when(companyRepo.findBySymbol(c.getSymbol())).thenReturn(c);
		validator.setTransactionForm(f);
		expectedFailures.add(generateInsufficientStockMessage(c, c));
		runTest();
	}

}
