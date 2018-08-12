package com.fdmgroup.ses.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fdmgroup.ses.model.CreditCardDetail;
import com.fdmgroup.ses.repository.CreditCardDetailRepository;
import com.fdmgroup.ses.validation.SesValidationException;
import com.fdmgroup.ses.validation.ValidationFactory;

@Service("creditCardService")
public class CreditCardService {

	@Autowired
	private CreditCardDetailRepository creditCardRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ValidationFactory validationFactory;
	
	public void saveCreditCard(CreditCardDetail creditCardDetail) throws SesValidationException {	
		// Perform validations
		validationFactory.getValidator(creditCardDetail).validate();
		
		// Hash card number, then save details
		hashCardNumber(creditCardDetail);
		creditCardRepo.save(creditCardDetail);
	}
	
	/**
	 * Hashes the credit card number as long as it is not already hashed.
	 * Relies on the hashed length of the number being exactly 60 characters to identify if it's already been hashed 
	 */
	private void hashCardNumber(CreditCardDetail creditCardDetail) {
		// Since PW length is limited to 50, but encrypted length is 60, this checks if PW is already encryped
		if (creditCardDetail.getCardNumber().length() != 60) {
			creditCardDetail.setCardNumber(passwordEncoder.encode(creditCardDetail.getCardNumber()));
		}
	}

}