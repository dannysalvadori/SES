package com.fdmgroup.ses.stockUpdate;

import java.math.BigDecimal;
import java.util.List;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.repository.CompanyRepository;

public class StockExchangeManager {

	/**
	 * Randomises the market value of all trading stocks
	 */
	public void fluctuateValues(CompanyRepository companyRepo) {
		List<Company> allStocks = companyRepo.findAll();
		for (Company c : allStocks) {
			BigDecimal currentValue = c.getCurrentShareValue();
			BigDecimal variance = new BigDecimal(1 + ((5 - (Math.random()*10)) / 100)); // 0% <= variance < 5%
			c.setCurrentShareValue(currentValue.multiply(variance));
		}
		companyRepo.save(allStocks);
	}

}