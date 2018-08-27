package com.fdmgroup.ses.controllerTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.fdmgroup.ses.utils.StockExchangeUtils.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.controller.AdminCRUDCompanyController;
import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.repository.CompanyRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class AdminCRUDCompanyControllerTest {

	@InjectMocks
	private AdminCRUDCompanyController ctrl = new AdminCRUDCompanyController();
	private ModelAndView mav = new ModelAndView();
	
	@Mock
	private CompanyRepository companyRepo;

	@Mock
	private WebRequest webRequest;
	
	private List<Company> allCompanies = new  ArrayList<>();

	@Before
	public void setUp() throws Exception {
		setupAllCompanies();
		when(companyRepo.findAll()).thenReturn(allCompanies);
	}
	
	private void setupAllCompanies() {
		for (Integer i = 0; i < 3; i++) {
			Company c = createCompany();
			c.setId(i);
			c.setSymbol(String.valueOf(i));
			allCompanies.add(c);
		}
	}

	/**
	 * manageCompanies() adds all companies to model (as "companies") and sets the view to admin/manageCompanies.jsp
	 */
	@Test
	public void goToManageCompaniesTest() {
		mav = ctrl.goToManageCompanies(mav);
		assertEquals("Wrong view name", "admin/manageCompanies", mav.getViewName());
		Object companiesModelObject = mav.getModel().get("companies");
		assertNotEquals(null, companiesModelObject);
		assertTrue("companies model object is the wrong type", companiesModelObject instanceof Collection<?>);
		Collection<?> companies = (Collection<?>) companiesModelObject;
		assertEquals("Didn't find all companies", allCompanies.size(), companies.size());
		for (Object obj : companies) {
			assertTrue("'Company' is wrong type", obj instanceof Company);
		}
	}

	/**
	 * goToCreateCompany() adds a new Company instance to the model (as "company") and sets the view to
	 * admin/createCompany.jsp
	 */
	@Test
	public void goToCreateCompanyTest() {
		mav = ctrl.goToCreateCompany(mav);
		assertEquals("Wrong view name", "admin/createCompany", mav.getViewName());
		assertNotEquals(null, mav.getModel().get("company"));
		assertTrue("company model object is the wrong type", mav.getModel().get("company") instanceof Company);
	}

	/**
	 * If validation succeeds, doCreateCompany() inserts the given Company object into the DB
	 */
	@Test
	public void doCreateCompanySuccessTest() {
		Company c = createCompany();
		mav = ctrl.doCreateCompany(mav, c);

		// Confirm validation passes
		Object failures = mav.getModel().get("failures");
		assertEquals("Validation failed", null, (String) failures);

		// Confirm save was attempted and view reset to manage companies
		verify(companyRepo, times(1)).save(c);
		assertEquals("Wrong view name", "admin/manageCompanies", mav.getViewName());
		Collection<?> companies = (Collection<?>) mav.getModel().get("companies");
		assertEquals("Didn't find all companies", allCompanies.size(), companies.size());
	}
	
	/**
	 * If validation fails, doCreateCompany() .... TODO: the code does nothing different if it fails! 
	 */
	@Test
	public void doCreateCompanyFailureTest() {
		// implement
	}

	/**
	 * editCompany() adds the Company instance to be edited to the model (as "company") and sets the view to
	 * admin/editCompany.jsp
	 */
	@Test
	public void goToEditCompanyTest() {
		Company c = allCompanies.get(0);
		when(companyRepo.findById(c.getId())).thenReturn(c);
		
		mav = ctrl.goToEditCompany(mav, c.getId());
		
		// Verify view and company object set correctly
		assertEquals("Wrong view name", "admin/editCompany", mav.getViewName());
		Object companyModelObject = mav.getModel().get("company");
		assertNotEquals("Model company object was null", null, companyModelObject);
		assertTrue("Model company object is wrong type", companyModelObject instanceof Company);
		assertEquals("Wrong ID", c.getId(), ((Company)companyModelObject).getId());
	}

	/**
	 * doEditCompany() updates the Company in the DB, and sets the view back to admin/manageCompanies.jsp
	 */
	@Test
	public void doEditCompanySuccessTest() {
		Company c = allCompanies.get(0);
		when(companyRepo.findById(c.getId())).thenReturn(c);
		
		Company c2 = createCompany();
		c2.setId(c.getId());
		c2.setName("NEW_NAME");
		
		mav = ctrl.doEditCompany(mav, c2);
		
		// Verify company object fields set correctly and saved
		verify(companyRepo, times(1)).save(c);
		assertEquals("Saved company's name was not updated", c2.getName(), c.getName());
		
		// Verify view set back to manage companies
		assertEquals("Wrong view name", "admin/manageCompanies", mav.getViewName());
		Collection<?> companies = (Collection<?>) mav.getModel().get("companies");
		assertEquals("Didn't find all companies", allCompanies.size(), companies.size());
	}

	/**
	 * doEditCompany() failure ... TODO: the code does nothing if it fails
	 */
	@Test
	public void doEditCompanyFailureTest() {
		// implement
	}

}
