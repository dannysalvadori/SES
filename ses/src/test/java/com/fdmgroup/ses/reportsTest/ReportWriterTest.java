package com.fdmgroup.ses.reportsTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static com.fdmgroup.ses.utils.StockExchangeUtils.*;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.reports.CSVWriter;
import com.fdmgroup.ses.reports.CompanyReport;
import com.fdmgroup.ses.reports.OwnedSharesReport;
import com.fdmgroup.ses.reports.Report;
import com.fdmgroup.ses.reports.ReportWriter;
import com.fdmgroup.ses.reports.XMLWriter;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ReportWriterTest {
	
	private Report<Company> companyReport = new CompanyReport();
	private Report<OwnedShare> ownedSharesReport = new OwnedSharesReport();
	
	private List<OwnedShare> ownedShares = new ArrayList<>();
	private List<Company> companies = new ArrayList<>();
	private Company testCompany1 = createCompany();
	private Company testCompany2 = createCompany();
	private Company testCompany3 = createCompany();
	
	
	private String xmlString;
	private String csvString;
	
	@Before
	public void setUp() throws Exception {
		setupTestData();
	}
	
	/********************************************
	*              XMLWriter tests              *
	********************************************/

	/**
	 * XMLWriter must generate a .xml file extension 
	 */
	@Test
	public void xmlExtensionTest() {
		ReportWriter<Company> xmlWriter = new XMLWriter<>(companyReport);
		assertTrue("Wrong file extension for XMLWriter. Expected: .xml (case-insensitive); "
				+ "actual: " + xmlWriter.getFileExtension(), xmlWriter.getFileExtension().equalsIgnoreCase(".xml"));
	}

	/**
	 * XMLWriter must produce a well-formed XML string
	 * Note: the XML header information is not explicitly tested, but is required to parse it successfully
	 * Expected format:
	 * <report>
	 *     <body>
	 *         <row>
	 *             <Symbol>SYM1</Symbol>
	 *             <Name>Test Company 1</Name>
	 *             <CurrentShareValue>10</CurrentShareValue>
	 *             <AvailableShares>100</AvailableShares>
	 *             <Gains>1000</Gains>
	 *         </row>
	 *         <row> multiple other rows... </row>
	 *     </body>
	 * </report>
	 */
	@Test
	public void xmlWriterTest() throws Exception {
		// Setup report
		companyReport.setRows(companies);
		ReportWriter<Company> xmlWriter = new XMLWriter<>(companyReport);

		// Get XMLString and convert to document for testing
		xmlString = xmlWriter.writeReport();
		Document xmlDoc = loadXMLFromString(xmlString);

		// Confirm DOM structure
		assertEquals("DOM base was not as expected", "#document", xmlDoc.getNodeName());
		
		// Confirm "report" element
		NodeList reportElement = xmlDoc.getChildNodes();
		assertEquals("Wrong number of base elements", 1, reportElement.getLength());
		assertEquals("DOM base element was not as expected", "report", reportElement.item(0).getNodeName());
		
		// Confirm "body" element
		NodeList bodyElement = reportElement.item(0).getChildNodes();
		assertEquals("Wrong number of base elements", 1, bodyElement.getLength());
		assertEquals("DOM base element was not as expected", "body", bodyElement.item(0).getNodeName());
		
		// Confirm "row"s
		NodeList rowElements = bodyElement.item(0).getChildNodes();
		assertEquals("Wrong number of row elements", companies.size(), rowElements.getLength());
		for (Integer i = 0; i < rowElements.getLength(); i++) {
			NodeList rowElement = rowElements.item(i).getChildNodes();
			// Get corresponding test company
			Company c = companies.get(i);
			assertEquals("Wrong symbol", c.getSymbol(),
					rowElement.item(0).getTextContent());
			assertEquals("Wrong name", c.getName(),
					rowElement.item(1).getTextContent());
			assertEquals("Wrong current share value", c.getCurrentShareValue().toString(),
					rowElement.item(2).getTextContent());
			assertEquals("Wrong available shares", c.getAvailableShares().toString(),
					rowElement.item(3).getTextContent());
			assertEquals("Wrong gains", c.getGains().toString(),
					rowElement.item(4).getTextContent());
		}
	}
	
	/********************************************
	*              CSVWriter tests              *
	********************************************/

	/**
	 * XMLWriter must generate a .csv file extension 
	 */
	@Test
	public void csvExtensionTest() {
		ReportWriter<OwnedShare> csvWriter = new CSVWriter<>(ownedSharesReport);
		assertTrue("Wrong file extension for XMLWriter. Expected: .xml (case-insensitive); "
				+ "actual: " + csvWriter.getFileExtension(), csvWriter.getFileExtension().equalsIgnoreCase(".csv"));
	}

	/**
	 * CSVWriter must produce a well-formed CSV string
	 * Expected format:
	 *     Owned Stock Report (DATE)
	 *     Company Symbol,Company Name,Average Purchase Price,Quantity Owned,Gains
	 *     SYM1,Test Company 1,10,100,1000
	 */
	@Test
	public void csvWriterTest() throws Exception {
		// Setup report
		ownedSharesReport.setRows(ownedShares);
		ReportWriter<OwnedShare> csvWriter = new CSVWriter<>(ownedSharesReport);

		// Get XMLString and convert to document for testing
		csvString = csvWriter.writeReport();
		CSVParser csvParser = CSVParser.parse(csvString, CSVFormat.DEFAULT);
		
		List<CSVRecord> csvRecords = csvParser.getRecords();
		
		// Check title row
		CSVRecord titleRow = csvRecords.get(0);
		assertEquals("Title row had the wrong number of columns", 1, titleRow.size());
		assertTrue("Title row doesn't contain expected title", titleRow.get(0).contains("Owned Stocks Report"));
		
		// Check column headers
		List<String> expectedColumnHeaders = Arrays.asList(
				"Company Symbol",
				"Company Name",
				"Average Purchase Price",
				"Quantity Owned",
				"Gains"
		);
		CSVRecord headerRow = csvRecords.get(1);
		for (Integer i = 0; i < headerRow.size(); i++) {
			assertEquals("Wrong header", expectedColumnHeaders.get(i), headerRow.get(i));
		}

		// Check row values
		List<String> expectedRowValues;
		for (Integer i = 2; i < csvRecords.size(); i++) {
			CSVRecord record = csvRecords.get(i);
			OwnedShare os = ownedShares.get(i-2);
			expectedRowValues = Arrays.asList(
					os.getCompany().getSymbol(),
					os.getCompany().getName(),
					os.getAveragePurchasePrice().toString(),
					os.getQuantity().toString(),
					os.getCompany().getGains().toString()
			);
			for (Integer j = 0; j < record.size(); j++) {
				assertEquals("Wrong value", expectedRowValues.get(j), record.get(j));
			}
		}
	}
	
	/********************************************
	*               Helper Methods              *
	********************************************/
	
	/**
	 * Utility method to convert XML string into Document to be parsed
	 */
	public static Document loadXMLFromString(String xml) throws Exception {
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    return builder.parse(is);
	}
	
	/**
	 * Arbitrarily define companies and owned shares to be reported
	 */
	private void setupTestData() {
		companies.addAll(Arrays.asList(testCompany1, testCompany2, testCompany3));
		for (Integer i = 1; i <= companies.size(); i++) {
			Company c = companies.get(i-1);
			c.setSymbol("SYM" + i); 
			c.setName("TEST COMPANY " + i);
			c.setCurrentShareValue(new BigDecimal(i * 10));
			c.setAvailableShares(i * 100l);
			c.setGains(new BigDecimal(i * 1000));
			ownedShares.add(createOwnedShare(c));
		}
	}
	
}
