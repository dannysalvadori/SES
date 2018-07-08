package com.fdmgroup.ses.service;

import java.util.List;

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

	public List<Company> findAll() {
		return companyRepo.findAll();
	}
	
}
