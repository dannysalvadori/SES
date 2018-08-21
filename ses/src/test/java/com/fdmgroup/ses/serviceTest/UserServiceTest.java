package com.fdmgroup.ses.serviceTest;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import static com.fdmgroup.ses.utils.UserUtils.*;
import static com.fdmgroup.ses.utils.RoleUtils.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.WebRequest;

import com.fdmgroup.ses.model.Role;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.RoleRepository;
import com.fdmgroup.ses.repository.UserRepository;
import com.fdmgroup.ses.service.UserService;
import com.fdmgroup.ses.service.UserServiceImpl;
import com.fdmgroup.ses.validation.SesValidationException;
import com.fdmgroup.ses.validation.UserValidator;
import com.fdmgroup.ses.validation.ValidationFactory;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserServiceTest {
	
	@Mock
	WebRequest webRequest;
	
	@Mock
    private RoleRepository roleRepo;
	private Role stubRole = new Role();
	
	@Mock
	private UserRepository userRepo;
	
	@Mock
	private ValidationFactory validationFactory;
	@Mock
	private UserValidator userValidator;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private ApplicationEventPublisher eventPublisher;
	
	// Class to be tested
	@InjectMocks
	private UserService userService = new UserServiceImpl();

	@Before
	public void setUp() throws Exception {
		stubRole.setRole("ROLE_USER");
		when(roleRepo.findByRole("ROLE_USER")).thenReturn(stubRole);
		when(validationFactory.getValidator(any(User.class))).thenReturn(userValidator);
		when(passwordEncoder.encode(any(String.class))).thenReturn(VALID_HASHED_PASSWORD);
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
		verify(eventPublisher, times(1)).publishEvent(any());;
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

}
