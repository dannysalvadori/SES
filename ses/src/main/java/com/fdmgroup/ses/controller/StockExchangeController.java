package com.fdmgroup.ses.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.repository.CompanyRepository;
import com.fdmgroup.ses.stockExchange.TransactionForm;

@Controller
public class StockExchangeController {
	
	@Autowired
	CompanyRepository companyRepo;

	/**
	 * Go to Admin home page
	 */
	@RequestMapping(value="/user/stockExchange")
    public ModelAndView goToAdminHome(ModelAndView modelAndView) {
		modelAndView.setViewName("user/stockExchange");
		addCompaniesToModel(modelAndView);
		return modelAndView;
	}
	
	/**
	 * Request a purchase on a given stock
	 */
	@RequestMapping(value="/user/doBuyStocks")
    public ModelAndView goToBuyStocks(
    		ModelAndView modelAndView,
    		@ModelAttribute("transactionForm") TransactionForm transactionForm
    ) {
		System.out.println("buying stocks!!");
		modelAndView.setViewName("user/stockExchange");
		addCompaniesToModel(modelAndView);
		return modelAndView;
	}
	
	private void addCompaniesToModel(ModelAndView mav) {
		TransactionForm transactionForm = new TransactionForm();
		transactionForm.setCompanies(companyRepo.findAll());
		mav.addObject("transactionForm", transactionForm);
	}
	
}
