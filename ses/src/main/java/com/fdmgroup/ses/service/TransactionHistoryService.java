package com.fdmgroup.ses.service;

import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.model.TransactionHistory;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.CompanyRepository;
import com.fdmgroup.ses.repository.TransactionHistoryRepository;
import com.fdmgroup.ses.validation.ValidatorFactory;

@Service("transactionHistoryService")
public class TransactionHistoryService {

	@Autowired
    private TransactionHistoryRepository txHistoryRepo;
	
	@Autowired
	private CompanyRepository companyRepo;

	@Autowired
	ValidatorFactory validationFactory;
	
	public void createTransactionHistory(Company company, User user, OwnedShare ownedShare) {
		TransactionHistory txHistory = new TransactionHistory();
		Company dbCompany = companyRepo.findBySymbol(company.getSymbol());
		txHistory.setCompany(dbCompany);
		txHistory.setOwner(user);
		txHistory.setExchangeDate(GregorianCalendar.getInstance().getTime());
		txHistory.setUnitPrice(company.getCurrentShareValue()); // TODO: fix this value so it can't change!
		txHistory.setQuantity(company.getTransactionQuantity());
		txHistory.setValue(company.getTransactionValue());
		txHistory.setOwnedShare(ownedShare);
		txHistoryRepo.save(txHistory);
	}
	
}
