package com.fdmgroup.ses.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.CompanyRepository;
import com.fdmgroup.ses.repository.OwnedSharesRepository;
import com.fdmgroup.ses.validation.ValidationFactory;

@Service("ownedSharesService")
public class OwnedSharesService {

	@Autowired
    private OwnedSharesRepository ownedSharesRepo;
	
	@Autowired
	private CompanyRepository companyRepo;
	
	@Autowired
	private UserService userService;

	@Autowired
	ValidationFactory validationFactory;
	
	public OwnedShare createOwnedShares(Company company, User user) {
		Company dbCompany = companyRepo.findBySymbol(company.getSymbol());
		OwnedShare share = ownedSharesRepo.findByOwnerAndCompany(user, dbCompany);
		if (share == null) {
			share = new OwnedShare();
		};
		share.setCompany(dbCompany);
		share.setOwner(user);
		share.setQuantity(share.getQuantity() + company.getTransactionQuantity());
		ownedSharesRepo.save(share);
		return share;
	}

	public List<OwnedShare> findAllForCurrentUser() {
		User user = userService.findCurrentUser();
		return ownedSharesRepo.findByOwner(user);
	}
	
}
