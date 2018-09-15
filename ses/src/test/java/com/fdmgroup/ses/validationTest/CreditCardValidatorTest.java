package com.fdmgroup.ses.validationTest;

import static com.fdmgroup.ses.utils.CreditCardUtils.*;
import static com.fdmgroup.ses.validation.CreditCardValidator.*;

import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdmgroup.ses.model.CreditCardDetail;
import com.fdmgroup.ses.validation.CreditCardValidator;

/**
 * Test for CreditCardValidator:
 * # Card number must be 16 numbers long
 * # Card holder name must not be blank
 * # The expiration date must not have already passed
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class CreditCardValidatorTest extends ValidationTest<CreditCardValidator> {
	
	public CreditCardValidatorTest() {
		validator = new CreditCardValidator();
	}
		
	/**
	 * If validation passes all criteria, no failures are thrown
	 */
	@Test
	public void insertSuccessTest() {
		CreditCardDetail c = createCard();
		validator.setCreditCardDetail(c);
		// No expected failures
		runTest();
	}
	
	/**
	 * Validation fails if the provided credit card number is too short
	 */
	@Test
	public void cardNumberTooShortTest() {
		CreditCardDetail c = createCard();
		c.setCardNumber(SHORT_CARD_NUMBER);
		validator.setCreditCardDetail(c);
		expectedFailures.add(FAIL_CARD_NUMBER_LENGTH);
		runTest();
	}
	
	/**
	 * Validation fails if the provided credit card number is too long
	 */
	@Test
	public void cardNumberTooLongTest() {
		CreditCardDetail c = createCard();
		c.setCardNumber(LONG_CARD_NUMBER);
		validator.setCreditCardDetail(c);
		expectedFailures.add(FAIL_CARD_NUMBER_LENGTH);
		runTest();
	}
	
	/**
	 * Validation fails if the no cardholder name is provided
	 */
	@Test
	public void cardholderNameNullTest() {
		CreditCardDetail c = createCard();
		c.setCardHolderName(null);
		validator.setCreditCardDetail(c);
		expectedFailures.add(FAIL_CARDHOLDER_BLANK);
		runTest();
	}
	
	/**
	 * Validation fails if the cardholder name is blank
	 */
	@Test
	public void cardholderNameBlankTest() {
		CreditCardDetail c = createCard();
		c.setCardHolderName("");
		validator.setCreditCardDetail(c);
		expectedFailures.add(FAIL_CARDHOLDER_BLANK);
		runTest();
	}
	
	/**
	 * Validation fails if the card has already expired
	 */
	@Test
	public void cardExpiryIsPastTest() {
		CreditCardDetail c = createCard();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1); // Set expiry to a month ago
		c.setExpiryDate(cal.getTime());
		validator.setCreditCardDetail(c);
		expectedFailures.add(FAIL_CARD_EXPIRED);
		runTest();
	}
	
}
