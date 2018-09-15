package com.fdmgroup.ses.reportsTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdmgroup.ses.reports.CSVWriter;
import com.fdmgroup.ses.reports.CompanyReport;
import com.fdmgroup.ses.reports.Report;
import com.fdmgroup.ses.reports.ReportWriter;
import com.fdmgroup.ses.reports.ReportWriterFactory;
import com.fdmgroup.ses.reports.XMLWriter;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ReportWriterFactoryTest {
	
	private ReportWriter<?> reportWriter = null;  
	private Report<?> report = new CompanyReport();
	
	private final String INVALID_FORMAT = "XXX";
	private final String CSV_FORMAT = "CSV";
	private final String XML_FORMAT = "XML";

	/**
	 * If the input format is not recognised, the factory returns null
	 */
	@Test
	public void invalidFormatTest() {
		reportWriter = ReportWriterFactory.getReportWriter(INVALID_FORMAT, report);
		assertEquals("Factory returned a ReportWriter for " + INVALID_FORMAT, null, reportWriter);
	}
	
	/**
	 * If the input format is CSV, a CSVWriter is returned
	 */
	@Test
	public void csvFormatTest() {
		reportWriter = ReportWriterFactory.getReportWriter(CSV_FORMAT, report);
		assertNotEquals("Factory didn't return a ReportWriter for " + CSV_FORMAT, null, reportWriter);
		assertTrue("ReportWriter for " + CSV_FORMAT + " was the wrong type", reportWriter instanceof CSVWriter<?>);
	}
	
	/**
	 * If the input format is XML, an XMLWriter is returned
	 */
	@Test
	public void xmlFormatTest() {
		reportWriter = ReportWriterFactory.getReportWriter(XML_FORMAT, report);
		assertNotEquals("Factory didn't return a ReportWriter for " + XML_FORMAT, null, reportWriter);
		assertTrue("ReportWriter for " + XML_FORMAT + " was the wrong type", reportWriter instanceof XMLWriter<?>);
	}
	
}
