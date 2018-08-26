package com.fdmgroup.ses.controllerTest;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.controller.AdminCRUDCompanyController;
import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.repository.CompanyRepository;
import com.fdmgroup.ses.utils.StockExchangeUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // Allows detached entities (caused by multiple entityManager instances) to be persisted
public class AdminCRUDCompanyControllerTest {

	@Autowired
	private AdminCRUDCompanyController ctrl;
	
	@Autowired
	private CompanyRepository companyRepo;

	@Mock
	WebRequest webRequest;

	private ModelAndView mav = new ModelAndView();
	private final String VALID_TEST_SYMBOL = "T" + String.valueOf(Math.random());

	@Before
	public void setUp() throws Exception {
		Company testCompany = companyRepo.findBySymbol(VALID_TEST_SYMBOL);
		assertEquals("A company already exists with test symbol. Consider re-running test", null, testCompany);
	}

	/**
	 * Delete any DB data persisted during the test
	 */
	@After
	public void revert() {
		Company testCompany = companyRepo.findBySymbol(VALID_TEST_SYMBOL);
		if (testCompany != null) {
			companyRepo.delete(testCompany);
		}
	}

	/*******************************************
	*               Tests Start                *
	*******************************************/

	/**
	 * manageCompanies() adds all companies to model (as "companies") and sets the view to admin/manageCompanies.jsp
	 */
	@Test
	public void goToManageCompaniesTest() {
		mav = ctrl.goToManageCompanies(mav);
		assertEquals("Wrong view name", "admin/manageCompanies", mav.getViewName());
		Object companiesModelObject = mav.getModel().get("companies");
		assertNotEquals(null, companiesModelObject);
		assertTrue("newUser is wrong type", companiesModelObject instanceof Collection<?>);
		Collection<?> companies = (Collection<?>) companiesModelObject;
		assertEquals("Didn't find all users", companies.size(), companyRepo.findAll().size());
		for (Object obj : companies) {
			assertTrue("'Company' is wrong type", obj instanceof Company);
		}
	}

	/**
	 * createCompany() adds a new Company instance to the model (as "company") and sets the view to
	 * admin/createCompany.jsp
	 */
	@Test
	public void goToCreateCompanyTest() {
		mav = ctrl.goToCreateCompany(mav);
		assertEquals("Wrong view name", "admin/createCompany", mav.getViewName());
		assertNotEquals(null, mav.getModel().get("company"));
		assertTrue("newUser is wrong type", mav.getModel().get("company") instanceof Company);
	}

	/**
	 * If validation succeeds, doCreateCompany() inserts the given Company object into the DB
	 */
	@Test
	public void doCreateCompanySuccessTest() {
		Company testCompany = createCompany();
		mav = ctrl.doCreateCompany(mav, testCompany);

		// Confirm validation passes
		Object failures = mav.getModel().get("failures");
		assertEquals("Validation failed", null, (String) failures);

		Company resultCompany = companyRepo.findBySymbol(VALID_TEST_SYMBOL);
		assertTrue("Null user", resultCompany != null);
		assertEquals("Wrong symbol", VALID_TEST_SYMBOL, resultCompany.getSymbol());
		assertEquals("Wrong name", VALID_TEST_SYMBOL, resultCompany.getName());
		assertEquals("Wrong amount of shares", testCompany.getAvailableShares(), resultCompany.getAvailableShares());
		assertEquals("Wrong share value", testCompany.getCurrentShareValue(), resultCompany.getCurrentShareValue());
	}

	/**
	 * editCompany() adds the Company instance to be edited to the model (as "company") and sets the view to
	 * admin/editCompany.jsp
	 */
	@Test
	public void goToEditCompanyTest() {
		Company testCompany = insertTestCompany();
		mav = ctrl.goToEditCompany(mav, testCompany.getId());
		assertEquals("Wrong view name", "admin/editCompany", mav.getViewName());
		Object companyModelObject = mav.getModel().get("company");
		assertNotEquals("Model company object was null", null, companyModelObject);
		assertTrue("Model company object is wrong type", companyModelObject instanceof Company);
		assertEquals("Wrong ID", testCompany.getId(), ((Company)companyModelObject).getId());
	}

	/**
	 * doEditCompany() updates the Company in the DB, and sets the view back to admin/manageCompanies.jsp
	 */
	@Test
	public void doEditUserCompanyTest() {
		Company testCompany = insertTestCompany();
		
		// Edit test user's details
		testCompany.setName("ALTERED_NAME");
		mav = ctrl.doEditCompany(mav, testCompany);
		
		// Get company from DB
		testCompany = companyRepo.findBySymbol(VALID_TEST_SYMBOL);
		
		assertEquals("Wrong view name", "admin/manageCompanies", mav.getViewName());
		assertEquals("Model User had wrong ID", "ALTERED_NAME", testCompany.getName());
	}
	
	// TODO add fail cases after TODO add company validation

	/*******************************************
	 * Helper Methods *
	 *******************************************/

	private Company createCompany() {
		Company c = StockExchangeUtils.createCompany();
		c.setName(VALID_TEST_SYMBOL);
		c.setSymbol(VALID_TEST_SYMBOL);
		c.setAvailableShares(100l);
		c.setCurrentShareValue(new BigDecimal(50));
		return c;
	}

	private Company insertTestCompany() {
		return insertTestCompany(null);
	}
	private Company insertTestCompany(Company c) {
		Company testCompany = c == null ? createCompany() : c;
//		try {
			companyRepo.save(testCompany);
//		} catch (SesValidationException e) {
			// TODO: create company validator!
//			assertTrue("Test user validation failure: " + ValidationUtils.stringifyFailures(e.getFailures()), false);
//		}
		// Confirm successful persist
		Company resultCompany = companyRepo.findBySymbol(testCompany.getSymbol());
		assertTrue("Null company", resultCompany != null);
		assertEquals("Wrong symbol", testCompany.getSymbol(), resultCompany.getSymbol());
		assertEquals("Wrong name", testCompany.getName(), resultCompany.getName());
		assertEquals("Wrong amount of shares", testCompany.getAvailableShares(), resultCompany.getAvailableShares());
		assertEquals("Wrong share value", testCompany.getCurrentShareValue(), resultCompany.getCurrentShareValue());
		return resultCompany;
	}

}
