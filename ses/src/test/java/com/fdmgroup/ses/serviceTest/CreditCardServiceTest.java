package com.fdmgroup.ses.serviceTest;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.fdmgroup.ses.utils.UserUtils.*;
import static com.fdmgroup.ses.utils.RoleUtils.*;
import static com.fdmgroup.ses.utils.StockExchangeUtils.*;
import static com.fdmgroup.ses.utils.CreditCardUtils.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.WebRequest;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.CreditCardDetail;
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.model.Role;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.model.VerificationToken;
import com.fdmgroup.ses.repository.CreditCardDetailRepository;
import com.fdmgroup.ses.repository.RoleRepository;
import com.fdmgroup.ses.repository.UserRepository;
import com.fdmgroup.ses.repository.VerificationTokenRepository;
import com.fdmgroup.ses.service.CreditCardService;
import com.fdmgroup.ses.service.UserService;
import com.fdmgroup.ses.stockExchange.SaleForm;
import com.fdmgroup.ses.stockExchange.TransactionForm;
import com.fdmgroup.ses.validation.CreditCardValidator;
import com.fdmgroup.ses.validation.SesValidationException;
import com.fdmgroup.ses.validation.UserValidator;
import com.fdmgroup.ses.validation.ValidatorFactory;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class CreditCardServiceTest {
	
	@Mock
	private UserService userService;
	@Mock
	private User u;
	
	@Mock
	private CreditCardDetailRepository creditCardRepo;
	
	@Mock
	private ValidatorFactory validationFactory;
	@Mock
	private CreditCardValidator cardValidator;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	// Class to be tested
	@InjectMocks
	private CreditCardService creditCardService = new CreditCardService();

	@Before
	public void setUp() throws Exception {
		when(validationFactory.getValidator(any(CreditCardDetail.class))).thenReturn(cardValidator);
		when(passwordEncoder.encode(any(String.class))).thenReturn(VALID_HASHED_PASSWORD);
		when(userService.findCurrentUser()).thenReturn(u);
	}

	/**
	 * Positive creation test scenarios for saveCreditCard(CreditCardDetail) 
	 */
	@Test
	public void saveCreditCardPositiveTest() throws SesValidationException {
		
		CreditCardDetail card = createCard();
		
		try {
			creditCardService.saveCreditCard(card);
		} catch (SesValidationException e) {
			fail("Save failed unexpectedly");
		}
		
		// Confirm card signature was set
		assertEquals("Card signature was not set correctly", VALID_CARD_SIGNATURE, card.getCardSignature());
		assertEquals("Card number was not hashed", 60, card.getCardNumber().length());
		
		// Confirm critical business logic methods were called
		verify(cardValidator, times(1)).validate();
		verify(creditCardRepo, times(1)).save(card);
	}

	/**
	 * Test findAllForCurrentUser()
	 */
	@Test
	public void findAllForCurrentUserTest() throws SesValidationException {
		creditCardService.findAllForCurrentUser();
		verify(creditCardRepo, times(1)).findByUser(u);
	}

	/**
	 * Positive test scenario for deleteCreditCard(Integer) 
	 * # If the card ID belongs to a card which belongs to the current user, it is deleted
	 */
	@Test
	public void deleteCreditCardPositiveTest() throws SesValidationException {
		CreditCardDetail card = createCard();
		
		// Associate card with current user
		when(creditCardRepo.findByUser(u)).thenReturn(Arrays.asList(card));
		
		// Stub query on matching card
		when(creditCardRepo.findById(card.getId())).thenReturn(card);
		
		creditCardService.deleteCreditCard(card.getId());
		verify(creditCardRepo, times(1)).delete(card);
	}

	/**
	 * Negative test scenario for deleteCreditCard(Integer) 
	 * # If the card ID belongs to a card, but doesn't belong to the current user, it is not deleted and an exception
	 * is thrown.
	 */
	@Test
	public void deleteCreditCardNotYourCardTest() {
		CreditCardDetail card = createCard();
		
		// Stub query on matching card
		when(creditCardRepo.findById(card.getId())).thenReturn(card);
		
		try {
			creditCardService.deleteCreditCard(card.getId());
		} catch (SesValidationException ex) {
			assertFalse("No failures were recorded", ex.getFailures().isEmpty());
			assertEquals("Wrong number of failures", 1, ex.getFailures().size());
			for (String failure : ex.getFailures()) {
				assertEquals("Wrong failure", "Bad card ID!", failure);
			}
		}
		verify(creditCardRepo, times(0)).delete(card);
	}

	/**
	 * Negative test scenario for deleteCreditCard(Integer) 
	 * # If the card ID does not belongs to a card, an exception is thrown.
	 */
	@Test
	public void deleteCreditCardInvalidIdTest() {
		CreditCardDetail card = createCard();
		
		// Associate card with current user
		when(creditCardRepo.findByUser(u)).thenReturn(Arrays.asList(card));
		
		Integer wrongId = card.getId() + 1;
		
		try {
			creditCardService.deleteCreditCard(wrongId);
		} catch (SesValidationException ex) {
			assertFalse("No failures were recorded", ex.getFailures().isEmpty());
			assertEquals("Wrong number of failures", 1, ex.getFailures().size());
			for (String failure : ex.getFailures()) {
				assertEquals("Wrong failure", "Bad card ID!", failure);
			}
		}
		verify(creditCardRepo, times(0)).delete(card);
	}

}
