package com.fdmgroup.ses.reportsTest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.model.TransactionHistory;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.reports.ReportWriterUtils;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ReportWriterUtilsTest {

	/**
	 * getFieldValue get the value of a valid property of a given object, one object deep
	 */
	@Test
	public void getFieldValueDepth1SuccessTest() {
		Object sourceObject = new User();
		String expectedResult = "testEmail@xyz.com";
		((User)sourceObject).setEmail(expectedResult);
		String fieldPath = "Email";
		String result = ReportWriterUtils.getFieldValue(sourceObject, fieldPath);
		assertEquals("Wrong value", expectedResult, result);
	}

	/**
	 * getFieldValue returns a blank String if the property path is invalid (must be TitleCase).
	 */
	@Test
	public void getFieldValueDepth1BadPropertyPathTest() {
		Object sourceObject = new User();
		String expectedResult = "testEmail@xyz.com";
		((User)sourceObject).setEmail(expectedResult);
		String fieldPath = "email"; // N.B. must be TitleCase, so this property is illegal
		String result = ReportWriterUtils.getFieldValue(sourceObject, fieldPath);
		assertEquals("Wrong value", "", result);
	}

	/**
	 * getFieldValue get the value of a valid property of a given object, two objects deep
	 */
	@Test
	public void getFieldValueDepth2SuccessTest() {
		Object sourceObject = new TransactionHistory();
		User intermediateObject = new User();
		String expectedResult = "testEmail@xyz.com";
		intermediateObject.setEmail(expectedResult);
		((TransactionHistory)sourceObject).setOwner(intermediateObject);
		String fieldPath = "Owner.Email";
		String result = ReportWriterUtils.getFieldValue(sourceObject, fieldPath);
		assertEquals("Wrong value", expectedResult, result);
	}

	/**
	 * getFieldValue returns a blank String if the final value of the property path is invalid
	 */
	@Test
	public void getFieldValueDepth2BadIntermediatePropertyPathTest() {
		Object sourceObject = new TransactionHistory();
		User intermediateObject = new User();
		String expectedResult = "testEmail@xyz.com";
		intermediateObject.setEmail(expectedResult);
		((TransactionHistory)sourceObject).setOwner(intermediateObject);
		String fieldPath = "oooner.Email";
		String result = ReportWriterUtils.getFieldValue(sourceObject, fieldPath);
		assertEquals("Wrong value", "", result);
	}

	/**
	 * getFieldValue returns a blank String if the final value of the property path is invalid
	 */
	@Test
	public void getFieldValueDepth2BadFinalPropertyPathTest() {
		Object sourceObject = new TransactionHistory();
		User intermediateObject = new User();
		String expectedResult = "testEmail@xyz.com";
		intermediateObject.setEmail(expectedResult);
		((TransactionHistory)sourceObject).setOwner(intermediateObject);
		String fieldPath = "Owner.SomeInvalidField";
		String result = ReportWriterUtils.getFieldValue(sourceObject, fieldPath);
		assertEquals("Wrong value", "", result);
	}

	/**
	 * getFieldValue can access paths three properties deep (there is no limit) 
	 */
	@Test
	public void getFieldValueDepth3SuccessTest() {
		Object sourceObject = new TransactionHistory();
		OwnedShare intermediateObject = new OwnedShare();
		Company thirdObject = new Company();
		String expectedResult = "company_name";
		thirdObject.setName(expectedResult);
		((TransactionHistory)sourceObject).setOwnedShare(intermediateObject);
		intermediateObject.setCompany(thirdObject);
		String fieldPath = "OwnedShare.Company.Name";
		String result = ReportWriterUtils.getFieldValue(sourceObject, fieldPath);
		assertEquals("Wrong value", expectedResult, result);
	}

	/**
	 * getFieldValue returns an empty string if any part of the path is invalid 
	 */
	@Test
	public void getFieldValueDepth3FailureTest() {
		Object sourceObject = new TransactionHistory();
		OwnedShare intermediateObject = new OwnedShare();
		Company thirdObject = new Company();
		String expectedResult = "company_name";
		thirdObject.setName(expectedResult);
		((TransactionHistory)sourceObject).setOwnedShare(intermediateObject);
		intermediateObject.setCompany(thirdObject);
		String fieldPath;
		String result;
		
		fieldPath = "XXXXX.Company.Name";
		result = ReportWriterUtils.getFieldValue(sourceObject, fieldPath);
		assertEquals("Wrong value", "", result);
		
		fieldPath = "OwnedShare.XXXXX.Name";
		result = ReportWriterUtils.getFieldValue(sourceObject, fieldPath);
		assertEquals("Wrong value", "", result);
		
		fieldPath = "OwnedShare.Company.XXXXX";
		result = ReportWriterUtils.getFieldValue(sourceObject, fieldPath);
		assertEquals("Wrong value", "", result);
	}
	
}
