package com.fdmgroup.ses.stockUpdate;

import java.math.BigDecimal;
import java.util.List;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.repository.CompanyRepository;

public class StockExchangeManager {

	/**
	 * Randomises the market value of all trading stocks and set gains
	 */
	public void fluctuateValues(CompanyRepository companyRepo) {
		List<Company> allStocks = companyRepo.findAll();
		for (Company c : allStocks) {
			// Fluctuate value
			BigDecimal currentValue = c.getCurrentShareValue();
			BigDecimal variance = new BigDecimal(1 + ((5 - (Math.random()*10)) / 100)); // 0% <= variance < 5%
			c.setCurrentShareValue(currentValue.multiply(variance));
			
			// Update gains
			BigDecimal openValue = c.getOpenValue() == null ? new BigDecimal(0) : c.getOpenValue();
			c.setGains(c.getCurrentShareValue().subtract(openValue));
		}
		companyRepo.save(allStocks);
	}

	/**
	 * Set companies' open values and reset gains to zero
	 */
	public void openStockExchange(CompanyRepository companyRepo) {
		List<Company> allStocks = companyRepo.findAll();
		for (Company c : allStocks) {
			c.setOpenValue(c.getCurrentShareValue());
			c.setGains(new BigDecimal(0));
		}
		companyRepo.save(allStocks);
	}

	/**
	 * Set companies' close values and gains
	 */
	public void closeStockExchange(CompanyRepository companyRepo) {
		List<Company> allStocks = companyRepo.findAll();
		for (Company c : allStocks) {
			c.setCloseValue(c.getCurrentShareValue());
			BigDecimal openValue = c.getOpenValue() == null ? new BigDecimal(0) : c.getOpenValue();
			c.setGains(c.getCurrentShareValue().subtract(openValue));
		}
		companyRepo.save(allStocks);
	}

}