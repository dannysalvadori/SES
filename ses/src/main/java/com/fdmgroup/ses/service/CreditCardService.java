package com.fdmgroup.ses.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fdmgroup.ses.model.CreditCardDetail;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.CreditCardDetailRepository;
import com.fdmgroup.ses.validation.SesValidationException;
import com.fdmgroup.ses.validation.ValidatorFactory;

@Service("creditCardService")
public class CreditCardService {

	@Autowired
	private CreditCardDetailRepository creditCardRepo;
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ValidatorFactory validationFactory;
	
	/**
	 * Insert/Update a CreditCardDetail instance
	 * @throws SesValidationException
	 */
	public void saveCreditCard(CreditCardDetail creditCardDetail) throws SesValidationException {	
		// Perform validations
		validationFactory.getValidator(creditCardDetail).validate();
		
		// Save final 4 digits as card signature, then hash the card number and save
		creditCardDetail.setCardSignature(creditCardDetail.getCardNumber().substring(12, 16));
		hashCardNumber(creditCardDetail);
		creditCardRepo.save(creditCardDetail);
	}
	
	/**
	 * Gets all credit card details for the currently logged in user 
	 */
	public List<CreditCardDetail> findAllForCurrentUser() {
		User user = userService.findCurrentUser();
		return creditCardRepo.findByUser(user);
	}
	
	/**
	 * Deletes the specified card, provided it belongs to the user trying to delete it!
	 * @throws SesValidationException if the logged in user does not own the card to be deleted
	 */
	public void deleteCreditCard(Integer cardId) throws SesValidationException {
		Boolean belongsToCurrentUser = false;
		for (CreditCardDetail card : findAllForCurrentUser()) {
			if (card.getId() == cardId) {
				belongsToCurrentUser = true;
				break;
			}
		}
		if (belongsToCurrentUser) {
			CreditCardDetail dbCard = creditCardRepo.findById(cardId);
			creditCardRepo.delete(dbCard);
		} else {
			SesValidationException ex = new SesValidationException();
			ex.addFailure("Bad card ID!");
			throw ex;
		}
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