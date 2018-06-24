package com.fdmgroup.ses.validation;

import org.springframework.stereotype.Component;

import com.fdmgroup.ses.model.Company;

@Component
public class CompanyValidator extends ModelValidator {
	
	private Company company;

	/**
	 * Validates Company creations and updates:
	 * # Stock value cannot fall below XXX or rise above YYY
	 * # Stock amount cannot fall to 0 (how about ZZZ amount?)
	 */
	@Override
	public void validate() throws SesValidationException {
		
		failures.clear();
		
		Boolean isUpdate = company.getId() != null;
		
		// Update
		if (isUpdate) {

//			Company oldCompany = companyRepo.findById(company.getId());
//			
//			// Fail if email is changed to a taken address
//			if (!oldCompany.getSymbol().equalsIgnoreCase(company.getSymbol())
//					&& companyRepo.findBySymbol(company.getSymbol()) != null
//			) {
//				failures.add("A user is already registered with this address.");
//			}
			
		// Insert
		} else {
			
//			if (companyRepo.findBySymbol(company.getSymbol()) != null) {
//				failures.add("A user is already registered with this address.");
//			}
		
		}
		throwFailures();
	}

	public void setCompany(Company company) {
		this.company = company;
	}

}