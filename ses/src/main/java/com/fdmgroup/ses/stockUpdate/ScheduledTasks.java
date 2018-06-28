package com.fdmgroup.ses.stockUpdate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fdmgroup.ses.repository.CompanyRepository;

@Component
public class ScheduledTasks {

	@Autowired
	private CompanyRepository companyRepo;
	
	@Scheduled(fixedRate = 5*60*1000)
	public void fluctuateStockValues() {
		new StockExchangeManager().fluctuateValues(companyRepo);
    }
}