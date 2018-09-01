package com.fdmgroup.ses.validation;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.fdmgroup.ses.model.Company;

@Component
public class CompanyValidator extends ModelValidator {
	
	public static final Long CREATIONS_LOWER_QUANTITY_LIMIT = 2000000l;
	public static final Long CREATIONS_UPPER_QUANTITY_LIMIT = 1000000000l;
	public static final BigDecimal CREATIONS_LOWER_VALUE_LIMIT = new BigDecimal(5);
	public static final BigDecimal CREATIONS_UPPER_VALUE_LIMIT = new BigDecimal(550);
	public static final String FAIL_CREATION_QUANTITY_RANGE = "New companies must have a quantity of available stock "
			+ "between " + CREATIONS_LOWER_QUANTITY_LIMIT + " and " + CREATIONS_UPPER_QUANTITY_LIMIT + ", inclusive.";
	public static final String FAIL_CREATION_VALUE_RANGE = "New companies must have a starting stock value between "
			+ CREATIONS_LOWER_VALUE_LIMIT + " and " + CREATIONS_UPPER_VALUE_LIMIT + ", inclusive.";
	public static final String FAIL_NEGATIVE_VALUE = "Stock value cannot be negative.";
	public static final String FAIL_NEGATIVE_QUANTITY = "Stock quantty cannot be negative.";
	
	private Company company;

	/**
	 * Validates Company creations and updates:
	 * Create:
	 * # New stocks must have a value between 5 and 550 inclusive
	 * # New stock must have between 2,000,000 and 1,000,000,000 available shares, inclusive
	 * Update:
	 * # Stock quantity cannot be less than zero
	 * # Stock value cannot be less than zero
	 */
	@Override
	public void validate() throws SesValidationException {
		
		failures.clear();
		
		Boolean isUpdate = company.getId() != null;
		
		// Update
		if (isUpdate) {
			if (company.getAvailableShares() < 0) {
				failures.add(FAIL_NEGATIVE_QUANTITY);
			}
			
			if (company.getCurrentShareValue().compareTo(new BigDecimal(0)) < 0) {
				failures.add(FAIL_NEGATIVE_VALUE);
			}
			
		// Insert
		} else {
			
			if (company.getAvailableShares().compareTo(CREATIONS_LOWER_QUANTITY_LIMIT) < 0
					|| company.getAvailableShares().compareTo(CREATIONS_UPPER_QUANTITY_LIMIT) > 0) {
				failures.add(FAIL_CREATION_QUANTITY_RANGE);
			}
			
			if (company.getCurrentShareValue().compareTo(CREATIONS_LOWER_VALUE_LIMIT) < 0
					|| company.getCurrentShareValue().compareTo(CREATIONS_UPPER_VALUE_LIMIT) > 0) {
				failures.add(FAIL_CREATION_VALUE_RANGE);
			}
		
		}
		throwFailures();
	}

	public void setCompany(Company company) {
		this.company = company;
	}

}