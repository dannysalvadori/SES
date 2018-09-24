package com.fdmroup.ses.emailTest;

import static com.fdmgroup.ses.testUtils.UserUtils.*;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.Transport;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdmgroup.ses.config.AwsConfig;
import com.fdmgroup.ses.email.Email;
import com.fdmgroup.ses.email.EmailSender;
import com.fdmgroup.ses.email.RegistrationEmail;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.model.VerificationToken;
import com.fdmgroup.ses.registration.OnRegistrationCompleteEvent;
import com.fdmgroup.ses.validation.SesValidationException;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class EmailSenderTest {
	
	@Mock
	private Transport transport;
	
	@Mock
	private OnRegistrationCompleteEvent event;
	
	@Mock
	private AwsConfig awsConfig;
	
	private User u = createUser();
	private String tokenId = "tokenId";
	private String appURL = "appURL";
	
	// Class to be tested
	@InjectMocks
	private EmailSender emailSender = new EmailSender();

	@Before
	public void setUp() throws Exception {
		when(event.getUser()).thenReturn(u);
		when(event.getAppUrl()).thenReturn(appURL);
	}

	/**
	 * Negative test for sendEmail(Email)
	 */
	@Test
	public void sendEmailTest() throws SesValidationException, MessagingException, UnsupportedEncodingException {
		// User email address is not AWS verified
		VerificationToken vt = new VerificationToken(u, tokenId);
		Email registrationEmail = new RegistrationEmail(event, vt);
		emailSender.sendEmail(registrationEmail);
	}

}
