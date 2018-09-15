package com.fdmgroup.ses.validationTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.CreditCardDetail;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.stockExchange.SaleForm;
import com.fdmgroup.ses.stockExchange.TransactionForm;
import com.fdmgroup.ses.validation.CompanyValidator;
import com.fdmgroup.ses.validation.CreditCardValidator;
import com.fdmgroup.ses.validation.SaleValidator;
import com.fdmgroup.ses.validation.SesValidator;
import com.fdmgroup.ses.validation.TransactionValidator;
import com.fdmgroup.ses.validation.UserValidator;
import com.fdmgroup.ses.validation.ValidatorFactory;

/**
 * Test for ValidationFactory:
 * When passed an object, the factory returns an instance of the corresponding SesValidator implementation
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ValidatorFactoryTest {
	
	@InjectMocks
	private ValidatorFactory factory = new ValidatorFactory();
	private SesValidator validator = null;
	
	@Mock
	private UserValidator userValidator;
	@Mock
	private CompanyValidator companyValidator;
	@Mock
	private TransactionValidator transactionValidator;
	@Mock
	private SaleValidator saleValidator;
	@Mock
	private CreditCardValidator creditCardValidator;
		
	/**
	 * If the input object is not a recognised class, the factory returns null
	 */
	@Test
	public void invalidClassTest() {
		validator = factory.getValidator(new BigDecimal(7));
		assertEquals("Wrong validator returned for type BigDecimal", null, validator);
	}
	
	/**
	 * If the input object is a User, a UserValidator is returned
	 */
	@Test
	public void userTest() {
		validator = factory.getValidator(new User());
		assertNotEquals("Null validator returned for type User", null, validator);
		assertTrue("Validatory for User was the wrong type", validator instanceof UserValidator);
	}
	
	/**
	 * If the input object is a Company, a CompanyValidator is returned
	 */
	@Test
	public void companyTest() {
		validator = factory.getValidator(new Company());
		assertNotEquals("Null validator returned for type Company", null, validator);
		assertTrue("Validatory for Company was the wrong type", validator instanceof CompanyValidator);
	}
	
	/**
	 * If the input object is a TransactionForm, a TransactionValidator is returned
	 */
	@Test
	public void transactionFormTest() {
		validator = factory.getValidator(new TransactionForm());
		assertNotEquals("Null validator returned for type TransactionForm", null, validator);
		assertTrue("Validatory for TransactionForm was the wrong type", validator instanceof TransactionValidator);
	}
	
	/**
	 * If the input object is a company, a CompanyValidator is returned
	 */
	@Test
	public void saleFormTest() {
		validator = factory.getValidator(new SaleForm());
		assertNotEquals("Null validator returned for type SaleForm", null, validator);
		assertTrue("Validatory for SaleForm was the wrong type", validator instanceof SaleValidator);
	}
	
	/**
	 * If the input object is a company, a CompanyValidator is returned
	 */
	@Test
	public void creditCardDetailTest() {
		validator = factory.getValidator(new CreditCardDetail());
		assertNotEquals("Null validator returned for type CreditCardDetail", null, validator);
		assertTrue("Validatory for CreditCardDetail was the wrong type", validator instanceof CreditCardValidator);
	}
	
}
