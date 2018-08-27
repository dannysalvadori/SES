package com.fdmgroup.ses.utils;

import java.util.Calendar;

import com.fdmgroup.ses.model.CreditCardDetail;
import com.fdmgroup.ses.model.User;

public class CreditCardUtils {

	public static final String VALID_CARD_NUMBER = "1234567812345678";
	public static final String VALID_CARD_SIGNATURE = "5678";
	
	public static CreditCardDetail createCard(User u) {
		CreditCardDetail card = new CreditCardDetail();
		card.setCardHolderName(u.getName());
		card.setCardNumber(VALID_CARD_NUMBER);
		card.setCardSignature(VALID_CARD_SIGNATURE);
		card.setExpired(0);
		card.setExpiryDate(Calendar.getInstance().getTime());
		card.setUser(u);
		return card;
	}
	
	public static CreditCardDetail createCard() {
		CreditCardDetail card = new CreditCardDetail();
		card.setCardNumber(VALID_CARD_NUMBER);
		card.setCardSignature(VALID_CARD_SIGNATURE);
		card.setExpired(0);
		card.setExpiryDate(Calendar.getInstance().getTime());
		return card;
	}
	
}
