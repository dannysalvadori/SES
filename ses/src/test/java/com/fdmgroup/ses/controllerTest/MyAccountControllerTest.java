package com.fdmgroup.ses.controllerTest;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static com.fdmgroup.ses.utils.CreditCardUtils.*;

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
import com.fdmgroup.ses.model.CreditCardDetail;
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.model.TransactionHistory;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.TransactionHistoryRepository;
import com.fdmgroup.ses.service.CreditCardService;
import com.fdmgroup.ses.service.OwnedSharesService;
import com.fdmgroup.ses.service.UserService;
import com.fdmgroup.ses.stockExchange.SaleForm;
import com.fdmgroup.ses.utils.StockExchangeUtils;
import com.fdmgroup.ses.validation.SesValidationException;
import com.fdmgroup.ses.validation.UserValidator;
import com.fdmgroup.ses.validation.ValidatorFactory;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class MyAccountControllerTest {
	
	@InjectMocks
	private MyAccountController ctrl = new MyAccountController();
	
	@Mock
	private OwnedSharesService ownedSharesService;
	private List<OwnedShare> stubCurrentUsersShares = new ArrayList<>();
	
	@Mock
	private UserService userService;
	private static User currentUser = new User();
	
	@Mock
	private CreditCardService creditCardService;
	private List<CreditCardDetail> currentUsersCreditCards = new ArrayList<>();
	
	@Mock
	private TransactionHistoryRepository txHistoryRepo;
	private List<TransactionHistory> stubCurrentUsersTxHistory = new ArrayList<>();
	
	private ModelAndView mav = new ModelAndView();
	
	@Mock
	private ValidatorFactory validationFactory;
	private UserValidator userValidator;
	

	@Before
	public void setUp() throws Exception {
		// Stub "current user"
		when(userService.findCurrentUser()).thenReturn(currentUser);
		
		// Stub "user's owned stocks"
		stubCurrentUsersShares.add(StockExchangeUtils.createOwnedShare());
		when(ownedSharesService.findAllForCurrentUser()).thenReturn(stubCurrentUsersShares);
		
		// Stub "user's transaction history"
		stubCurrentUsersTxHistory.add(new TransactionHistory());
		when(txHistoryRepo.findByOwner(currentUser)).thenReturn(stubCurrentUsersTxHistory);
		
		// Stub validators
		when(validationFactory.getValidator(any(User.class))).thenReturn(userValidator);
		
		setupCreditCards();
		when(creditCardService.findAllForCurrentUser()).thenReturn(currentUsersCreditCards);
	}
	
	private void setupCreditCards() {
		for (Integer i = 0; i < 2;i++) {
			CreditCardDetail card = createCard(currentUser);
			card.setCardNumber("000000000000000" + String.valueOf(i)); // i.e. 0000 0000 0000 0001 etc.
			currentUsersCreditCards.add(card);
		}
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
		
		// Confirm user's credit cards were added to the model
		Object creditCardsObject = mav.getModel().get("creditCardDetails");
		assertNotEquals("creditCardDetails model object shouldn't be null", null, creditCardsObject);
		assertTrue("creditCardDetails object is wrong type", creditCardsObject instanceof List<?>);
		List<?> creditCards = (List<?>) creditCardsObject;
		assertEquals("Wrong number of credit cards", 2, creditCards.size());
		for (Object cardObject : creditCards) {
			assertTrue("credit card was not of class CreditCardDetail", cardObject instanceof CreditCardDetail);
		}
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
		mav = ctrl.doChangePassword(mav, currentUser);
		
		// Confirm view
		assertEquals("Wrong view name", "user/myAccount", mav.getViewName());
		
		// Confirm current user was added to the model
		Object userObject = mav.getModel().get("user");
		assertNotEquals("user model object shouldn't be null", null, userObject);
		assertTrue("user model object object is wrong type", userObject instanceof User);
	}
	
	/**
	 * If doChangePassword() fails validation it adds failure messages as "failures" and calls goToChangePassword(),
	 * resetting as per goToChangePasswordTest().
	 */
	@Test
	public void doChangePasswordFailureTest() throws SesValidationException {
		// Setup validation failure
		final String VALIDATION_FAILURE = "VALIDATION_FAILURE";
		SesValidationException vEx = new SesValidationException();
		vEx.addFailure(VALIDATION_FAILURE);
		doThrow(vEx).when(userService).saveUser(currentUser);
		
		mav = ctrl.doChangePassword(mav, currentUser);
		
		// Confirm view
		assertEquals("Wrong view name", "user/changePassword", mav.getViewName());
		
		// Confirm failure was added to the model
		Object failuresObject = mav.getModel().get("failures");
		assertNotEquals("failures model object shouldn't be null", null, failuresObject);
		assertTrue("failure object is wrong type", failuresObject instanceof String);
		assertEquals("Wrong failures", VALIDATION_FAILURE, (String) failuresObject);
	}
	
	/**
	 * doEditDetails() calls the userService to update the current user's details. If successful, it calls
	 *  goToMyAccount(), resetting as per goToMyAccountTest().
	 */
	@Test
	public void doEditDetailsSuccessTest() {
		mav = ctrl.doEditDetails(mav, currentUser);
		
		// Confirm view
		assertEquals("Wrong view name", "user/myAccount", mav.getViewName());
		
		// Confirm current user was added to the model
		Object userObject = mav.getModel().get("user");
		assertNotEquals("user model object shouldn't be null", null, userObject);
		assertTrue("saleForm object is wrong type", userObject instanceof User);
	}
	
	/**
	 * If doEditDetails() fails validation it adds failure messages as "failures" and calls goToEditDetails(),
	 *  resetting as per goToEditDetailsTest().
	 */
	@Test
	public void doEditDetailsFailureTest() throws SesValidationException {
		// Setup validation failure
		final String VALIDATION_FAILURE = "VALIDATION_FAILURE";
		SesValidationException vEx = new SesValidationException();
		vEx.addFailure(VALIDATION_FAILURE);
		doThrow(vEx).when(userService).saveUser(currentUser);
		
		mav = ctrl.doEditDetails(mav, currentUser);
		
		// Confirm view
		assertEquals("Wrong view name", "user/editDetails", mav.getViewName());
		
		// Confirm failure was added to the model
		Object failuresObject = mav.getModel().get("failures");
		assertNotEquals("failures model object shouldn't be null", null, failuresObject);
		assertTrue("failure object is wrong type", failuresObject instanceof String);
		assertEquals("Wrong failures", VALIDATION_FAILURE, (String) failuresObject);
	}
	
	/**
	 * goToNewCreditCard() sets the view to user/createCreditCardDetail.jsp and adds a new CreditCardDetail to the
	 * model as "newCreditCardDetail"
	 */
	@Test
	public void goToNewCreditCardTest() {
		mav = ctrl.goToNewCreditCard(mav);
		
		// Confirm view
		assertEquals("Wrong view name", "user/createCreditCardDetail", mav.getViewName());
		
		// Confirm new credit card was added to the model
		Object cardObject = mav.getModel().get("newCreditCardDetail");
		assertNotEquals("user model object shouldn't be null", null, cardObject);
		assertTrue("saleForm object is wrong type", cardObject instanceof CreditCardDetail);
	}
	
	/**
	 * doCreateCreditCardDetail() sets the view back to myAccount and saves the new CreditCardDetail for the current
	 * user
	 */
	@Test
	public void doCreateNewCreditCardDetailSuccessTest() throws SesValidationException {
		// Create new card details
		final String CARD_NUMBER = "1234123456781234";
		CreditCardDetail card = createCard();
		card.setCardNumber(CARD_NUMBER);
		
		// Submit to controller for creation
		mav = ctrl.doCreateCreditCardDetail(mav, card);
		
		// Confirm card owner was set to current user and the card got saved
		assertEquals("Wrong user", currentUser, card.getUser());
		assertEquals("Wrong card number", CARD_NUMBER, card.getCardNumber());
		verify(creditCardService).saveCreditCard(card);
		assertEquals("Wrong view name", "user/myAccount", mav.getViewName());
	}
	
	/**
	 * If validation fails, doCreateCreditCardDetail() sets the view back to createCreditCardDetail and does not save
	 * the new CreditCardDetail
	 */
	@Test
	public void doCreateNewCreditCardDetailFailureTest() throws SesValidationException {
		// Create new card and setup validation failure
		CreditCardDetail card = createCard();
		final String VALIDATION_FAILURE = "VALIDATION_FAILURE";
		SesValidationException vEx = new SesValidationException();
		vEx.addFailure(VALIDATION_FAILURE);
		doThrow(vEx).when(creditCardService).saveCreditCard(card);
		
		// Submit to controller for creation
		mav = ctrl.doCreateCreditCardDetail(mav, card);
		
		// Confirm card owner was set to current user and the card got saved
		assertEquals("Wrong view name", "user/createCreditCardDetail", mav.getViewName());
		verify(creditCardService).saveCreditCard(card);
		
		// Confirm failure was added to the model
		Object failuresObject = mav.getModel().get("failures");
		assertNotEquals("failures model object shouldn't be null", null, failuresObject);
		assertTrue("failure object is wrong type", failuresObject instanceof String);
		assertEquals("Wrong failures", VALIDATION_FAILURE, (String) failuresObject);
	}
	
	/**
	 * doDeleteCard() sets the view back to myAccount and deletes the given CreditCardDetail
	 */
	@Test
	public void doDeleteCardSuccessTest() throws SesValidationException {
		CreditCardDetail card = currentUsersCreditCards.get(0);
		mav = ctrl.doDeleteCard(mav, card.getId());
		verify(creditCardService).deleteCreditCard(card.getId());
		assertEquals("Wrong view name", "user/myAccount", mav.getViewName());
	}
	
	/**
	 * If deleting the card fails, reports errors under "cardFailures"
	 */
	@Test
	public void doDeleteCardFailureTest() throws SesValidationException {
		// Setup failure
		CreditCardDetail card = currentUsersCreditCards.get(0);
		final String VALIDATION_FAILURE = "VALIDATION_FAILURE";
		SesValidationException vEx = new SesValidationException();
		vEx.addFailure(VALIDATION_FAILURE);
		doThrow(vEx).when(creditCardService).deleteCreditCard(card.getId());
		
		// Attempt to delete card
		mav = ctrl.doDeleteCard(mav, card.getId());
		
		// Confirm view and that cardFailures was added to the model
		assertEquals("Wrong view name", "user/myAccount", mav.getViewName());
		Object failuresObject = mav.getModel().get("cardFailures");
		assertNotEquals("failures model object shouldn't be null", null, failuresObject);
		assertTrue("failure object is wrong type", failuresObject instanceof String);
		assertEquals("Wrong failures", VALIDATION_FAILURE, (String) failuresObject);
	}


}
