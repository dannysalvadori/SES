package com.fdmgroup.ses.validation;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fdmgroup.ses.model.CreditCardDetail;
import com.fdmgroup.ses.repository.CreditCardDetailRepository;

@Component
public class CreditCardValidator extends ModelValidator {
	
	@Autowired
	private CreditCardDetailRepository creditCardRepo;

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
		
		Boolean isUpdate = creditCardDetail.getId() != null;
		
		// Update
		if (isUpdate) {

//			User oldUser = userRepo.findById(user.getId());
//			
//			// Fail if email is changed to a taken address
//			if (!oldUser.getEmail().equalsIgnoreCase(user.getEmail())
//					&& userRepo.findByEmail(user.getEmail()) != null
//			) {
//				failures.add("A user is already registered with this address.");
//			}
//			
//			// Fail if password is changed but is too short, or if confirmation doesn't match
//			if (!oldUser.getPassword().equals(user.getPassword())) {
//				if (user.getPassword().length() < 6 || user.getPassword().length() > 50) {
//					failures.add("Password must be 6 to 50 characters long.");
//				}
//				
//				if (!user.getPassword().equals(user.getConfirmationPassword())) {
//					failures.add("Passwords do not match.");
//				}
//			}
			
		// Insert
		} else {
			
			if (!creditCardDetail.getCardNumber().matches("^[0-9]{16}$")) {
				failures.add("Card number must be exactly 16 numbers.");
			}
			
			if (creditCardDetail.getCardHolderName().length() == 0) {
				failures.add("Card holder name must not be blank.");
			}
			
			if (creditCardDetail.getExpiryDate().before(Calendar.getInstance().getTime())) {
				failures.add("This card appears to have already expired. Please check the expiry date you entered.");
			}
		
		}
		throwFailures();
	}

	public void setCreditCardDetail(CreditCardDetail creditCardDetail) {
		this.creditCardDetail = creditCardDetail;
	}

}