package com.fdmgroup.ses.utilsTest;


import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdmgroup.ses.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class DateUtilsTest {

	@Test
	public void xMinutesFromNowTest() {
		// Setup "now" and expected result
		Calendar cal = GregorianCalendar.getInstance();
		cal.add(Calendar.MINUTE, 5);
		Date fiveMinutesFromNow = cal.getTime();

		// Test
		Date result = DateUtils.xMinutesFromNow(5);
		
		// Actual result can be a few milliseconds off expected result
		Long diff = result.getTime() - fiveMinutesFromNow.getTime();
		
		assertTrue("Added more than specified amount of time", diff <= 50); // Allow 50 ms for execution delay
		assertTrue("Didn't add enough time", diff >= 0); // Shouldn't be LESS than x minutes from now		
	}

	@Test
	public void isXMinutesOldTest() {
		// Setup "now" and expected result
		Calendar cal = GregorianCalendar.getInstance();
		cal.add(Calendar.MINUTE, -5);
		cal.add(Calendar.MILLISECOND, -50); // Add 50 ms for execution delay
		Date fiveMinutesAgo = cal.getTime();

		// Test
		Boolean posResult = DateUtils.isXMinutesOld(fiveMinutesAgo, 4);
		Boolean equalResult = DateUtils.isXMinutesOld(fiveMinutesAgo, 5);
		Boolean negResult = DateUtils.isXMinutesOld(fiveMinutesAgo, 6);
		
		assertTrue("Did not register as 4 minutes old", posResult);
		assertTrue("Did not register as 5 minutes old", equalResult);
		assertTrue("Erroneously registered as 6 minutes old", !negResult);
	}
	
	@Test
	public void nowTest() {
		Calendar cal = GregorianCalendar.getInstance();
		cal.add(Calendar.MILLISECOND, -50); // Add 50 ms for execution delay
		Date now = cal.getTime();
		
		Date result = DateUtils.now();
		
		Long diff = result.getTime() - now.getTime();
		assertTrue("\"Now\" was in the future!", diff <= 50);
		assertTrue("\"Now\" was in the past!", diff >= 0);
	}

}
