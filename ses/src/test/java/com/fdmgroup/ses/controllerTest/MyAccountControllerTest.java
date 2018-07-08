package com.fdmgroup.ses.controllerTest;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.controller.MyAccountController;
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.model.TransactionHistory;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.TransactionHistoryRepository;
import com.fdmgroup.ses.service.OwnedSharesService;
import com.fdmgroup.ses.service.UserServiceImpl;
import com.fdmgroup.ses.stockExchange.SaleForm;
import com.fdmgroup.ses.utils.DataFactory;
import com.fdmgroup.ses.validation.UserValidator;
import com.fdmgroup.ses.validation.ValidationFactory;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class MyAccountControllerTest {
	
	@InjectMocks
	private MyAccountController ctrl = new MyAccountController();
	
	@Mock
	private OwnedSharesService ownedSharesService;
	private List<OwnedShare> stubCurrentUsersShares = new ArrayList<>();
	
	@Mock
	private UserServiceImpl userService;
	private static User stubCurrentUser = new User();
	
	@Mock
	private TransactionHistoryRepository txHistoryRepo;
	private List<TransactionHistory> stubCurrentUsersTxHistory = new ArrayList<>();
	
	private ModelAndView mav = new ModelAndView();
	
	@Mock
	private ValidationFactory validationFactory;
	private UserValidator userValidator;
	

	@Before
	public void setUp() throws Exception {
		// Stub "current user"
		when(userService.findCurrentUser()).thenReturn(stubCurrentUser);
		
		// Stub "user's owned stocks"
		stubCurrentUsersShares.add(DataFactory.createOwnedShare());
		when(ownedSharesService.findAllForCurrentUser()).thenReturn(stubCurrentUsersShares);
		
		// Stub "user's transaction history"
		stubCurrentUsersTxHistory.add(new TransactionHistory());
		when(txHistoryRepo.findByOwner(stubCurrentUser)).thenReturn(stubCurrentUsersTxHistory);
		
		// Stub validators
		when(validationFactory.getValidator(any(User.class))).thenReturn(userValidator);
	}
	
	/**
	 * TODO: implement more meaningful test 
	 */
	@Test
	public void initBinderTest() {
		ctrl.initBinder(new WebDataBinder(ctrl));
	}

	/**
	 * goToMyAccount() sets the view to user/myAccount.jsp and adds the currently logged in user to the model as "user"
	 * along with his/her owned shares (wrapped) as "saleForm" and his/her transaction history as "userTXHistory" 
	 */
	@Test
	public void goToMyAccountTest() {
		mav = ctrl.goToMyAccount(mav);
		
		// Confirm view
		assertEquals("Wrong view name", "user/myAccount", mav.getViewName());
		
		// Confirm current user was added to the model
		Object userObject = mav.getModel().get("user");
		assertNotEquals("user model object shouldn't be null", null, userObject);
		assertTrue("saleForm object is wrong type", userObject instanceof User);
		
		// Confirm user's owned shares were added to the model
		Object saleFormObject = mav.getModel().get("saleForm");
		assertNotEquals("saleForm model object shouldn't be null", null, saleFormObject);
		assertTrue("saleForm object is wrong type", saleFormObject instanceof SaleForm);
		SaleForm saleForm = (SaleForm) saleFormObject;
		assertEquals("Wrong number of owned shares", stubCurrentUsersShares.size(), saleForm.getOwnedShares().size());
		assertEquals("Wrong quantity for owned share",
				Long.valueOf(100), Long.valueOf(saleForm.getOwnedShares().get(0).getQuantity()));
		
		// Confirm user's transaction history was added to the model
		Object txHistoryObject = mav.getModel().get("userTXHistory");
		assertNotEquals("userTXHistory model object shouldn't be null", null, txHistoryObject);
		assertTrue("userTXHistory object is wrong type", txHistoryObject instanceof List<?>);
		List<?> txHistory = (List<?>) txHistoryObject;
		assertEquals("Wrong number of transaction history lines", 1, txHistory.size());
		assertTrue("TxHistory was not of class TransactionHistory", txHistory.get(0) instanceof TransactionHistory);
	}
	
	/**
	 * goToChangePassword() sets the view to user/changePassword.jsp and adds the current user to the model as "user"
	 */
	@Test
	public void goToChangePasswordTest() {
		mav = ctrl.goToChangePassword(mav);
		
		// Confirm view
		assertEquals("Wrong view name", "user/changePassword", mav.getViewName());
		
		// Confirm current user was added to the model
		Object userObject = mav.getModel().get("user");
		assertNotEquals("user model object shouldn't be null", null, userObject);
		assertTrue("saleForm object is wrong type", userObject instanceof User);
	}
	
	/**
	 * goToEditDetails() sets the view to user/editDetails.jsp and adds the current user to the model as "user"
	 */
	@Test
	public void goToEditDetailsTest() {
		mav = ctrl.goToEditDetails(mav);
		
		// Confirm view
		assertEquals("Wrong view name", "user/editDetails", mav.getViewName());
		
		// Confirm current user was added to the model
		Object userObject = mav.getModel().get("user");
		assertNotEquals("user model object shouldn't be null", null, userObject);
		assertTrue("saleForm object is wrong type", userObject instanceof User);
	}
	
	/**
	 * doChangePassword() calls the userService to update the current user's password. If successful, it calls
	 *  goToMyAccount(), resetting as per goToMyAccountTest().
	 */
	@Test
	public void doChangePasswordSuccessTest() {
		mav = ctrl.doChangePassword(mav, stubCurrentUser);
		
		// Confirm view
		assertEquals("Wrong view name", "user/myAccount", mav.getViewName());
		
		// Confirm current user was added to the model
		Object userObject = mav.getModel().get("user");
		assertNotEquals("user model object shouldn't be null", null, userObject);
		assertTrue("saleForm object is wrong type", userObject instanceof User);
	}
	
	/**
	 * TODO: implement test
	 * If doChangePassword() fails validation it adds failure messages as "failures" and calls goToChangePassword(),
	 *  resetting as per goToChangePasswordTest().
	 */
	@Test
	public void doChangePasswordFailureTest() {
		// TODO: implement
	}
	
	/**
	 * doEditDetails() calls the userService to update the current user's details. If successful, it calls
	 *  goToMyAccount(), resetting as per goToMyAccountTest().
	 */
	@Test
	public void doEditDetailsSuccessTest() {
		mav = ctrl.doEditDetails(mav, stubCurrentUser);
		
		// Confirm view
		assertEquals("Wrong view name", "user/myAccount", mav.getViewName());
		
		// Confirm current user was added to the model
		Object userObject = mav.getModel().get("user");
		assertNotEquals("user model object shouldn't be null", null, userObject);
		assertTrue("saleForm object is wrong type", userObject instanceof User);
	}
	
	/**
	 * TODO: implement test
	 * If doEditDetails() fails validation it adds failure messages as "failures" and calls goToEditDetails(),
	 *  resetting as per goToEditDetailsTest().
	 */
	@Test
	public void doEditDetailsFailureTest() {
		// TODO: implement
	}

}
