package com.fdmgroup.ses.validation;

import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.fdmgroup.ses.model.CreditCardDetail;

@Component
public class CreditCardValidator extends ModelValidator {
	
	public static final String FAIL_CARD_NUMBER_LENGTH = "Card number must be exactly 16 numbers.";
	public static final String FAIL_CARDHOLDER_BLANK = "Card holder name must not be blank.";
	public static final String FAIL_CARD_EXPIRED = "This card appears to have already expired. Please check the expiry"
			+ " date you entered.";
	
	private CreditCardDetail creditCardDetail;

	/**
	 * Validates new Credit Card properties:
	 * # Card number must be 16 numbers long
	 * # Card holder name must not be blank
	 * # The expiration date must not have already passed
	 */
	@Override
	public void validate() throws SesValidationException {
		
		failures.clear();
		
		if (!creditCardDetail.getCardNumber().matches("^[0-9]{16}$")) {
			failures.add(FAIL_CARD_NUMBER_LENGTH);
		}
		
		if (creditCardDetail.getCardHolderName() == null
				|| creditCardDetail.getCardHolderName().length() == 0) {
			failures.add(FAIL_CARDHOLDER_BLANK);
		}
		
		if (creditCardDetail.getExpiryDate().before(Calendar.getInstance().getTime())) {
			failures.add(FAIL_CARD_EXPIRED);
		}
		
		throwFailures();
	}

	public void setCreditCardDetail(CreditCardDetail creditCardDetail) {
		this.creditCardDetail = creditCardDetail;
	}

}