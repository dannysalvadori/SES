package com.fdmgroup.ses.stockUpdate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fdmgroup.ses.repository.CompanyRepository;

@Component
public class ScheduledTasks {
	
	/**
	 * Workaround for limitations on cron expressions: fluctuateStockValues should be every five minutes between 08:00
	 * and 16:25, but is actually scheduled until 16:55. After close, it should fail to execute.
	 */
	private static Boolean isSEOpen = true;

	@Autowired
	private CompanyRepository companyRepo;

	@Scheduled(cron="0 59 7 * * *") // Every day at 07:59
	public void openStockExchange() {
		isSEOpen = true;
		new StockExchangeManager().openStockExchange(companyRepo);
    }
	
	@Scheduled(cron="0 */5 8-16 * * *") // Every five minutes between 08:00 and 16:55
	public void fluctuateStockValues() {
		if (isSEOpen) {
			new StockExchangeManager().fluctuateValues(companyRepo);
		}
    }
	
	@Scheduled(cron="0 30 16 * * *") // Every day at 16:30
	public void closeStockExchange() {
		isSEOpen = false;
		new StockExchangeManager().closeStockExchange(companyRepo);
    }
	
}