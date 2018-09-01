package com.fdmgroup.ses.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.repository.CompanyRepository;
import com.fdmgroup.ses.service.CompanyService;
import com.fdmgroup.ses.validation.SesValidationException;
import com.fdmgroup.ses.validation.ValidationUtils;

@Controller
public class AdminCRUDCompanyController {

	@Autowired
	CompanyRepository companyRepo;
	
	@Autowired
	CompanyService companyService;

	/**
	 * Go to the company management page
	 */
	@RequestMapping(value="/admin/manageCompanies")
    public ModelAndView goToManageCompanies(ModelAndView modelAndView) {
		modelAndView.setViewName("admin/manageCompanies");
		modelAndView.addObject("companies", companyRepo.findAll());
		return modelAndView;
	}
	
	/**
	 * Go to the create new company page
	 */
	@RequestMapping(value="/admin/createCompany")
    public ModelAndView goToCreateCompany(ModelAndView modelAndView) {
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
			companyService.save(company);
			modelAndView = goToManageCompanies(modelAndView);
		} catch (SesValidationException ex) {
			modelAndView.addObject("failures", ValidationUtils.stringifyFailures(ex.getFailures()));
			modelAndView.setViewName("admin/createCompany");
		}
		return modelAndView;
	}
	
	/**
	 * Go to edit company page for a given company
	 */
	@RequestMapping(value="/admin/editCompany")
    public ModelAndView goToEditCompany(
    		ModelAndView modelAndView,
    		@RequestParam(name="cid") int companyId
    ) {
		Company company = companyRepo.findById(companyId);
		modelAndView.setViewName("admin/editCompany");
		company.setId(companyId);
		modelAndView.addObject("company", company);
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
			companyService.save(update);
			modelAndView = goToManageCompanies(modelAndView);
		} catch (SesValidationException ex) {
			modelAndView.addObject("failures", ValidationUtils.stringifyFailures(ex.getFailures()));
			modelAndView = goToEditCompany(modelAndView, company.getId());
		}
		
		return modelAndView;
	}
	
}
