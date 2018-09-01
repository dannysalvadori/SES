package com.fdmgroup.ses.validationTest;

import static com.fdmgroup.ses.utils.StockExchangeUtils.*;
import static com.fdmgroup.ses.validation.CompanyValidator.*;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.validation.CompanyValidator;

/**
 * Test for CompanyValidator:
 * Create:
 * # New stocks must have a value between 5 and 550 inclusive
 * # New stock must have between 2,000,000 and 1,000,000,000 available shares, inclusive
 * Update:
 * # Stock quantity cannot be less than zero
 * # Stock value cannot be less than zero
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class CompanyValidatorTest extends ValidationTest<CompanyValidator> {
	
	public CompanyValidatorTest() {
		validator = new CompanyValidator();
	}
	
	/*****************************************
	*              Insert Tests              *
	*****************************************/
		
	/**
	 * If validation passes all criteria, no failures are thrown
	 */
	@Test
	public void insertSuccessTest() {
		Company c = createCompany();
		validator.setCompany(c);
		// No expected failures
		runTest();
	}
	
	/**
	 * Validation fails if the created company's stock value is below 5
	 */
	@Test
	public void creationStockValueTooLowTest() {
		Company c = createCompany();
		c.setCurrentShareValue(new BigDecimal(4));
		validator.setCompany(c);
		expectedFailures.add(FAIL_CREATION_VALUE_RANGE);
		runTest();
	}
	
	/**
	 * Validation fails if the created company's stock value is greater than 550
	 */
	@Test
	public void creationStockValueTooHighTest() {
		Company c = createCompany();
		c.setCurrentShareValue(new BigDecimal(4000));
		validator.setCompany(c);
		expectedFailures.add(FAIL_CREATION_VALUE_RANGE);
		runTest();
	}
	
	/**
	 * Validation fails if the created company's available stock is below 2 million
	 */
	@Test
	public void creationStockAvailabilityTooLowTest() {
		Company c = createCompany();
		c.setAvailableShares(1000000l);
		validator.setCompany(c);
		expectedFailures.add(FAIL_CREATION_QUANTITY_RANGE);
		runTest();
	}
	
	/**
	 * Validation fails if the created company's available stock is above 1 billion
	 */
	@Test
	public void creationStockAvailabilityTooHighTest() {
		Company c = createCompany();
		c.setAvailableShares(2000000000l);
		validator.setCompany(c);
		expectedFailures.add(FAIL_CREATION_QUANTITY_RANGE);
		runTest();
	}
	
	/*****************************************
	*              Update Tests              *
	*****************************************/
		
	/**
	 * If validation passes all criteria, no failures are thrown
	 */
	@Test
	public void updateSuccessTest() {
		Company c = createCompany();
		c.setId(1); // Setting ID identifies this as an update
		validator.setCompany(c);
		// No expected failures
		runTest();
	}
	
	/**
	 * Validation fails if updating a company's value to below zero
	 */
	@Test
	public void negativeValueFailureTest() {
		Company c = createCompany();
		c.setId(1);
		c.setCurrentShareValue(new BigDecimal(-10));
		validator.setCompany(c);
		expectedFailures.add(FAIL_NEGATIVE_VALUE);
		runTest();
	}
	
	/**
	 * Validation fails if updating a company's value to below zero
	 */
	@Test
	public void negativeQuantityFailureTest() {
		Company c = createCompany();
		c.setId(1);
		c.setAvailableShares(-10l);
		validator.setCompany(c);
		expectedFailures.add(FAIL_NEGATIVE_QUANTITY);
		runTest();
	}
	
}
