package com.fdmgroup.ses.stockUpdate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fdmgroup.ses.repository.CompanyRepository;

@Component
public class ScheduledTasks {

	@Autowired
	private CompanyRepository companyRepo;

	@Scheduled(cron="0 59 7 * * *") // Every day at 07:59
	public void openStockExchange() {
		new StockExchangeManager().openStockExchange(companyRepo);
    }
	
	@Scheduled(cron="0 */5 8-16 * * *") // Every five minutes between 08:00 and 16:55
	public void fluctuateStockValues() {
		new StockExchangeManager().fluctuateValues(companyRepo);
    }
	
	@Scheduled(cron="0 0 17 * * *") // Every day at 17:00
	public void closeStockExchange() {
		new StockExchangeManager().closeStockExchange(companyRepo);
    }
	
}