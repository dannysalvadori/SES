package com.fdmgroup.ses.utils;

import java.util.Calendar;

import com.fdmgroup.ses.model.CreditCardDetail;
import com.fdmgroup.ses.model.User;

public class CreditCardUtils {

	public static final String VALID_CARD_NUMBER = "1234567812345678";
	public static final String VALID_CARD_SIGNATURE = "5678";
	public static final String VALID_CARDHOLDER_NAME = "John Smith";
	
	public static final String SHORT_CARD_NUMBER = "1234567812345";
	public static final String LONG_CARD_NUMBER = "1234567812345678910";
	
	public static CreditCardDetail createCard(User u) {
		CreditCardDetail card = createCard();
		card.setCardHolderName(u.getName());
		card.setUser(u);
		return card;
	}
	
	public static CreditCardDetail createCard() {
		CreditCardDetail card = new CreditCardDetail();
		card.setCardHolderName(VALID_CARDHOLDER_NAME);
		card.setCardNumber(VALID_CARD_NUMBER);
		card.setExpired(0);
		card.setId(1);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 1);
		card.setExpiryDate(c.getTime());
		return card;
	}
	
}
