package com.fdmgroup.ses.service;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.repository.CompanyRepository;
import com.fdmgroup.ses.validation.CompanyValidator;
import com.fdmgroup.ses.validation.SesValidationException;

@Service("companyService")
public class CompanyService {

	@Autowired
    private CompanyRepository companyRepo;
	
	@Autowired
	private CompanyValidator validator;

	public void updateAvailableShares(Company company) {
		Company dbCompany = companyRepo.findBySymbol(company.getSymbol());
		dbCompany.setAvailableShares(dbCompany.getAvailableShares()-company.getTransactionQuantity());
		dbCompany.setLastTrade(GregorianCalendar.getInstance().getTime());
		companyRepo.save(dbCompany);
	}

	public List<Company> findAll() {
		return companyRepo.findAll();
	}

	public List<Company> findBySymbol(Set<String> symbols) {
		List<Company> companies;
		if (symbols == null || symbols.isEmpty()) {
			companies = findAll();
		} else {
			companies = companyRepo.findBySymbolIn(symbols);
		}
		return companies;
	}
	
	/**
	 * Save company, provided it passes validation
	 * @throws SesValidationException if validation fails
	 */
	public void save(Company c) throws SesValidationException {
		validator.setCompany(c);
		validator.validate();
		companyRepo.save(c);
	}
	
}
