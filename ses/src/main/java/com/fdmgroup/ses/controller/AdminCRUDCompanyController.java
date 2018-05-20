package com.fdmgroup.ses.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.repository.CompanyRepository;

@Controller
public class AdminCRUDCompanyController {

	@Autowired
	CompanyRepository companyRepo;

	/**
	 * Go to the company management page
	 */
	@RequestMapping(value="/admin/manageCompanies")
    public ModelAndView manageCompanies(ModelAndView modelAndView) {
		modelAndView.setViewName("admin/manageCompanies");
		modelAndView.addObject("companies", companyRepo.findAll());
		return modelAndView;
	}
	
	/**
	 * Go to the create new company page
	 */
	@RequestMapping(value="/admin/createCompany")
    public ModelAndView createCompany(ModelAndView modelAndView) {
		Company company = new Company();
		modelAndView.setViewName("admin/createCompany");
		modelAndView.addObject("company", company);
		return modelAndView;
	}
	
	/**
	 * Save new company
	 */
	@RequestMapping(value="/admin/doCreateCompany")
    public ModelAndView doCreateCompany(
    		ModelAndView modelAndView,
    		@ModelAttribute("company") Company company
    ) {
		try {
			companyRepo.save(company);
		} catch (Exception e) {
			// TODO: exception handling
			System.out.println("doCreateCompany - Exception happened");
		}
		modelAndView.setViewName("admin/manageCompanies");
		modelAndView.addObject("companies", companyRepo.findAll());
		modelAndView.addObject("company", null);
		return modelAndView;
	}
	
	/**
	 * Go to edit company page for a given company
	 */
	@RequestMapping(value="/admin/editCompany")
    public ModelAndView editCompany(
    		ModelAndView modelAndView,
    		@RequestParam(name="cid") int companyId
    ) {
		try {
			Company company = companyRepo.findById(companyId);
			modelAndView.setViewName("admin/editCompany");
			company.setId(companyId);
			modelAndView.addObject("company", company);
		} catch (Exception e) {
			// TODO: exception handling
			System.out.println("editCompany - Exception happened");
		}
		return modelAndView;
	}
	
	/**
	 * Save changes to an edited company
	 */
	@RequestMapping(value="/admin/doEditCompany")
    public ModelAndView doEditCompany(
    		ModelAndView modelAndView,
    		@ModelAttribute("company") Company company
    ) {
		try {
			Company update = companyRepo.findById(company.getId());
			update.setSymbol(company.getSymbol());
			update.setName(company.getName());
			update.setAvailableShares(company.getAvailableShares());
			update.setCurrentShareValue(company.getCurrentShareValue());
			companyRepo.save(update);
		} catch (Exception e) {
			// TODO: exception handling
			System.out.println("doEditCompany - Exception happened");
		}
		modelAndView.setViewName("admin/manageCompanies");
		modelAndView.addObject("companies", companyRepo.findAll());
		modelAndView.addObject("company", null);
		return modelAndView;
	}
	
}