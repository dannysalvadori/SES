package com.fdmgroup.ses.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
//		System.out.println("transactionForm company count: " + transactionForm.getCompanies().size());
//		for (Company comp : transactionForm.getCompanies()) {
//			// If getSelected is FALSE or NULL (removed from page by datatables), ignore, else BUY
//			System.out.print("comp: " + (comp == null ? "NULL" : "OK") + " -- ");
//			if (comp != null) {
//				System.out.print(comp.getSymbol());
//				System.out.print(" (" + (comp.getSelected() == null ? null : comp.getSelected() == true ? "CHECK" : "X"));
//				System.out.print("): buying " + comp.getTransactionQuantity() + "\n");
//			}
//		}
		
		List<Company> purchaseCompanies = new ArrayList<>();
		for (Company comp : transactionForm.getCompanies()) {
			if (comp.getSelected() != null && comp.getSelected() == true
					&& comp.getTransactionQuantity() != null && comp.getTransactionQuantity() > 0) {
				purchaseCompanies.add(comp);
			}
		}
		
		BigDecimal total = new BigDecimal(0);
		for (Company c : purchaseCompanies) {

			BigDecimal subTotal = c.getCurrentShareValue().multiply(new BigDecimal(c.getTransactionQuantity()));
			
			total = total.add(subTotal);
			System.out.println("subtotal: " + subTotal);
			System.out.println("total: " + total);
		}
		System.out.println("total done: " + total);
		
		modelAndView.addObject("purchaseCompanies", purchaseCompanies);
		modelAndView.addObject("total", total);
		
		// ---- Debug only; return to SE page
		modelAndView.setViewName("user/buyStocks");
		addCompaniesToModel(modelAndView);
		// ----
		return modelAndView;
	}
	
	private void addCompaniesToModel(ModelAndView mav) {
		TransactionForm transactionForm = new TransactionForm();
		transactionForm.setCompanies(companyRepo.findAll());
		mav.addObject("transactionForm", transactionForm);
	}
	
}
