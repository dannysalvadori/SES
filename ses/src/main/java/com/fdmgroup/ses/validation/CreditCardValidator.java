package com.fdmgroup.ses.validation;

import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.fdmgroup.ses.model.CreditCardDetail;

@Component
public class CreditCardValidator extends ModelValidator {
	
	private CreditCardDetail creditCardDetail;

	/**
	 * Validates new Credit Card properties:
	 * # Card number must be 16 numbers long
	 * # Card holder name must not be blank
	 * # The expiry date must not have already passed
	 */
	@Override
	public void validate() throws SesValidationException {
		
		failures.clear();
		
		if (!creditCardDetail.getCardNumber().matches("^[0-9]{16}$")) {
			failures.add("Card number must be exactly 16 numbers.");
		}
		
		if (creditCardDetail.getCardHolderName().length() == 0) {
			failures.add("Card holder name must not be blank.");
		}
		
		if (creditCardDetail.getExpiryDate().before(Calendar.getInstance().getTime())) {
			failures.add("This card appears to have already expired. Please check the expiry date you entered.");
		}
		
		throwFailures();
	}

	public void setCreditCardDetail(CreditCardDetail creditCardDetail) {
		this.creditCardDetail = creditCardDetail;
	}

}