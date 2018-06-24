package com.fdmgroup.ses.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.repository.CompanyRepository;

@Service("companyService")
public class CompanyService {

	@Autowired
    private CompanyRepository companyRepo;

	public void updateAvailableShares(Company company) {
		Company dbCompany = companyRepo.findBySymbol(company.getSymbol());
		dbCompany.setAvailableShares(dbCompany.getAvailableShares()-company.getTransactionQuantity());
		companyRepo.save(dbCompany);
	}
	
}
