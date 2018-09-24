package com.fdmgroup.ses.testUtils;

import java.math.BigDecimal;

import com.fdmgroup.ses.model.User;

public class EmailUtils {

	public static final String VALID_PASSWORD = "123456";
	public static final String VALID_EMAIL = "valid.example@ses.test";
	public static final String VALID_HASHED_PASSWORD = "$2a$12$PnKx3mPvRXTgpkSEsOSHxupUniY5qha2kklmLWMDTjUNS6xnalcgW";
	public static final String VALID_LASTNAME = "Smith";
	public static final String VALID_NAME = "Jo";
	public static final BigDecimal DEFAULT_CREDIT = new BigDecimal(50000);
	
	public static final String SHORT_PASSWORD = "1234";
	public static final String LONG_PASSWORD = "1111111111222222222233333333334444444444555555555566666666667777777777";
	
	public static final String INVALID_EMAIL = "invalid.email";
	
	public static User createUser() {
		User u = new User();
		u.setActive(0);
		u.setConfirmationPassword(VALID_PASSWORD);
		u.setCredit(DEFAULT_CREDIT);
		u.setEmail(VALID_EMAIL);
		u.setLastName(VALID_LASTNAME);
		u.setName(VALID_NAME);
		u.setPassword(VALID_PASSWORD);
		return u;
	}
	
}
