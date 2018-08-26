package com.fdmroup.ses.emailTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import static com.fdmgroup.ses.utils.UserUtils.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdmgroup.ses.email.RegistrationEmail;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.model.VerificationToken;
import com.fdmgroup.ses.registration.OnRegistrationCompleteEvent;

/**
 * Tests Email implementations construct correctly
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class EmailTest {
	
	@Mock
	private OnRegistrationCompleteEvent event;
	
	private User u = createUser();
	private String tokenId = "tokenId";
	private String appURL = "appURL";
	
	@Before
	public void setUp() throws Exception {
		when(event.getUser()).thenReturn(u);
		when(event.getAppUrl()).thenReturn(appURL);
	}

	/**
	 * Positive test RegistrationEmail
	 */
	@Test
	public void registrationEmailConstructorTest() {
		
		VerificationToken vt = new VerificationToken(u, tokenId);
		
		RegistrationEmail email = new RegistrationEmail(event, vt);
		
		String expectedSubject = "StockSim Registration";
		String expectedConfirmationUrl = "http://localhost:3702" + appURL + "/registationConfirm?token=" + tokenId;
		String expectedBody = String.join(
    		    System.getProperty("line.separator"),
    		    "<h1>Registration is not complete!</h1>",
    		    "<p>",
    		    "You must veify your email address to complete registration.", 
    		    " To do so, simply click the following link or copy-and-paste it into your browser.</p>", 
    		    "<p><a href='" + expectedConfirmationUrl + "'>" + expectedConfirmationUrl + "</a></p>",
    		    "<p>This link expires in 24 hours.</p>",
    		    "<br/>",
    		    "<p>Not expecting this email?</p>",
    		    "<p>This is an automated email sent from <a href='http://localhost:3702'>stockSim.com</a> because",
    		    " somebody tried to register an account using " + u.getEmail() + ".",
    		    " If it wasn't you, please ignore this email and accept our apologies for the inconvenience.</p>"
    	);
		
		assertEquals("Wrong subject", expectedSubject, email.getSubject());
		assertEquals("Wrong body", expectedBody, email.getBody());
		assertEquals("Wrong toAddress", u.getEmail(), email.getToAddress());		
	}

}
