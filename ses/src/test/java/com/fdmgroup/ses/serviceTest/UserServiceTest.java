package com.fdmgroup.ses.serviceTest;

import static com.fdmgroup.ses.testUtils.RoleUtils.*;
import static com.fdmgroup.ses.testUtils.StockExchangeUtils.*;
import static com.fdmgroup.ses.testUtils.UserUtils.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.model.Role;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.model.VerificationToken;
import com.fdmgroup.ses.repository.RoleRepository;
import com.fdmgroup.ses.repository.UserRepository;
import com.fdmgroup.ses.repository.VerificationTokenRepository;
import com.fdmgroup.ses.service.UserService;
import com.fdmgroup.ses.stockExchange.SaleForm;
import com.fdmgroup.ses.stockExchange.PurchaseForm;
import com.fdmgroup.ses.validation.SesValidationException;
import com.fdmgroup.ses.validation.UserValidator;
import com.fdmgroup.ses.validation.ValidatorFactory;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserServiceTest {
	
	@Mock
	WebRequest webRequest;
	
	@Mock
    private RoleRepository roleRepo;
	private Role stubRole = new Role();
	
	@Mock
	private VerificationTokenRepository vtRepo;
	
	@Mock
	private UserRepository userRepo;
	
	@Mock
	private ValidatorFactory validationFactory;
	@Mock
	private UserValidator userValidator;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private ApplicationEventPublisher eventPublisher;
	
	@Mock
	private SecurityContextHolder securityContextHolder;
	@Mock
	private SecurityContext securityContext;
	@Mock
	private Authentication authentication;
	@Mock
	private UserDetails userDetails;
	
	// Class to be tested
	@InjectMocks
	private UserService userService = new UserService();

	@Before
	public void setUp() throws Exception {
		stubRole.setRole("ROLE_USER");
		when(roleRepo.findByRole("ROLE_USER")).thenReturn(stubRole);
		when(validationFactory.getValidator(any(User.class))).thenReturn(userValidator);
		when(passwordEncoder.encode(any(String.class))).thenReturn(VALID_HASHED_PASSWORD);
		SecurityContextHolder.setContext(securityContext);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(authentication.getPrincipal()).thenReturn(userDetails);
	}

	/**
	 * Positive creation test scenarios for saveUser(User, WebRequest) 
	 * # If saveUser is called with a user that doesn't have a role, it assigned ROLE_USER and set inactive
	 * # Calls a UserValidator.validate(), UserRepository.save(User) and ApplicationEventPublisher.publish(any())
	 */
	@Test
	public void saveNewUserPositiveTest() throws SesValidationException {
		
		User u = createUser();
		
		try {
			userService.saveUser(u, webRequest);
		} catch (SesValidationException e) {
			fail();
		}
		
		// Confirm User instance properties were set correctly
		assertEquals("User was not deactivated", 0, u.getActive());
		assertEquals("User was given the wrong number of roles", 1, u.getRoles().size());
		for (Role r : u.getRoles()) {
			assertEquals("User was given the wrong role", ROLE_USER.getRole(), r.getRole());
		}
		
		// Confirm critical business logic methods were called
		verify(userValidator, times(1)).validate();
		verify(userRepo, times(1)).save(u);
		verify(eventPublisher, times(1)).publishEvent(any());
	}

	/**
	 * Positive edit test scenarios for saveUser(User, WebRequest) 
	 * # Since the has a role, it is not set inactive
	 * # Calls a UserValidator.validate(), UserRepository.save(User) and ApplicationEventPublisher.publish(any())
	 */
	@Test
	public void saveExistingUserPositiveTest() throws SesValidationException {
		
		User u = createUser();
		u.setActive(1);
		Set<Role> roles = new HashSet<>();
		roles.add(ROLE_USER);
		u.setRoles(roles);
		
		try {
			userService.saveUser(u, webRequest);
		} catch (SesValidationException e) {
			fail();
		}
		
		// Confirm User instance properties were set correctly
		assertEquals("User was deactivated in error", 1, u.getActive());
		
		// Confirm critical business logic methods were called
		verify(userValidator, times(1)).validate();
		verify(userRepo, times(1)).save(u);
		verify(eventPublisher, times(1)).publishEvent(any());;
	}

	/**
	 * Positive case for activateUser(String). If the token is valid, the user is activated and updated.
	 */
	@Test
	public void activateUserPositiveTest() throws SesValidationException {
		
		User u = createUser();
		assertEquals("Test user was created active to begin with!", 0, u.getActive());
		
		String tokenId = "TESTTOKENVALUE";
		VerificationToken token = new VerificationToken(u);		
		
		when(vtRepo.findByToken(tokenId)).thenReturn(token);
		
		userService.activateUser(tokenId);
		
		// Confirm user was set to active
		assertEquals("User was not set to active", 1, u.getActive());
		
		// Confirm critical business logic methods were called
		verify(userRepo, times(1)).save(u);
	}

	/**
	 * Negative case for activateUser(String). If the token is invalid, an exception is thrown.
	 */
	@Test
	public void activateUserNegativeTest() {
		
		String tokenId = "TESTTOKENVALUE";
		User u = createUser();
		assertEquals("Test user was created active to begin with!", 0, u.getActive());

		// Stub failure to find requested verification token 
		when(vtRepo.findByToken(tokenId)).thenReturn(null);
		
		try {
			userService.activateUser(tokenId);
		} catch (SesValidationException ex) {
			assertEquals("Wrong number of failures", 1, ex.getFailures().size());
			for (String failure : ex.getFailures()) {
				assertEquals("Wrong failure", "That verification code is expired or incorrect!", failure);
			}
		}
		
		// Confirm user was not set to active, and save was not called
		assertEquals("User was set to active despite failure", 0, u.getActive());
		verify(userRepo, times(0)).save(u);
	}

	/**
	 * Positive case for updateCredit(User, SaleForm). The user's credit is reduced by the transaction value of the
	 * sale form.
	 */
	@Test
	public void updateCreditSaleTest() {
		
		// Default credit is 50,000
		User u = createUser();
		assertEquals("Test user has the wrong starting credit", new BigDecimal(50000), u.getCredit());

		// Create an owned share sale (value 5000)
		SaleForm saleForm = new SaleForm();
		List<OwnedShare> ownedShares = new ArrayList<>();
		OwnedShare saleShare = new OwnedShare();
		Company saleCompany = createCompany();
		saleCompany.setTransactionQuantity(-50l);
		saleCompany.setCurrentShareValue(new BigDecimal(100)); // Transaction value: £5,000.00
		saleShare.setCompany(saleCompany);
		ownedShares.add(saleShare);
		saleForm.setOwnedShares(ownedShares);
		
		userService.updateCredit(u, saleForm);
		
		// Confirm user was not set to active, and save was not called
		BigDecimal expectedCredit = new BigDecimal(55000);
		
		assertTrue("Wrong value for user's credit: " + u.getCredit(), expectedCredit.equals((u.getCredit())));
		verify(userRepo, times(1)).save(u);
	}

	/**
	 * Positive case for updateCredit(User, TransactionForm). The user's credit is reduced by the transaction value of the
	 * sale form.
	 */
	@Test
	public void updateCreditPurchaseTest() {
		
		// Default credit is 50,000
		User u = createUser();
		assertEquals("Test user has the wrong starting credit", new BigDecimal(50000), u.getCredit());

		// Create a purchase worth 5000
		PurchaseForm purchaseForm = new PurchaseForm();
		List<Company> purchaseShares = new ArrayList<>();
		Company purchaseCompany = createCompany();
		purchaseCompany.setTransactionQuantity(50l);
		purchaseCompany.setCurrentShareValue(new BigDecimal(100)); // Transaction value: £5,000.00
		purchaseShares.add(purchaseCompany);
		purchaseForm.setCompanies(purchaseShares);
		
		userService.updateCredit(u, purchaseForm);
		
		// Confirm user was not set to active, and save was not called
		BigDecimal expectedCredit = new BigDecimal(45000);
		
		assertTrue("Wrong value for user's credit: " + u.getCredit(), expectedCredit.equals((u.getCredit())));
		verify(userRepo, times(1)).save(u);
	}

	/**
	 * Positive case for deleteUser(User).
	 */
	@Test
	public void deleteUserTest() {
		User u = createUser();
		userService.deleteUser(u);
		verify(userRepo, times(1)).delete(u);
	}

	/**
	 * Positive case for createVerificationToken(User, String).
	 */
	@Test
	public void createVerificationTokenTest() {
		User u = createUser();
		String tokenId = "tokenId";
		userService.createVerificationToken(u, tokenId);
		verify(vtRepo, times(1)).save(any(VerificationToken.class));
	}

	/**
	 * Positive case for findCurrentUser().
	 */
	@Test
	public void findCurrentUserTest() {
		User u = createUser();
		
		when(userDetails.getUsername()).thenReturn(u.getEmail());
		
		userService.findCurrentUser();
		
		verify(userRepo, times(1)).findByEmail(u.getEmail());
	}

}
