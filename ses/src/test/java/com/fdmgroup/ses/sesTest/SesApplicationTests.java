package com.fdmgroup.ses.sesTest;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fdmgroup.ses.SesApplication;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SesApplicationTests {

	@Test
	public void contextLoadsTest() {
		SesApplication.main(new String[0]);	
		assert(true);
	}
	
	/**
	 * passwordEncoder bean returns an instance of Crypto password encoder
	 */
	@Test
	public void passwordEncoderBeanTest() {
		Object output = new SesApplication().passwordEncoder();
		assertTrue("Passwoord encoder is of the wrong type",
				output instanceof org.springframework.security.crypto.password.PasswordEncoder);
	}

}
