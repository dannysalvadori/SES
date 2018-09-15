package com.fdmgroup.ses.reports;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.service.CompanyService;

public class ReportForm {
	
	// Constants
	private Set<String> availableFormats;
	private Map<String, Class<?>> availableTypes;
	private Set<String> availableStockSymbols;
	public static final String FORMAT_CSV = "CSV"; 
	public static final String FORMAT_XML = "XML"; 
	public static final String TYPE_COMPANY = "Public Stocks"; 
	public static final String TYPE_OWNED_SHARE = "My Owned Stocks"; 
	
	// Form properties
	private String format;
	private Class<?> type;
	private Set<String> stockSymbols;
	
	/**
	 * Default constructor required for Spring
	 */
	public ReportForm() {
		availableFormats = new HashSet<>();
		availableFormats.add(FORMAT_CSV);
		availableFormats.add(FORMAT_XML);
		availableTypes = new LinkedHashMap<>();
		availableTypes.put(TYPE_COMPANY, Company.class);
		availableTypes.put(TYPE_OWNED_SHARE, OwnedShare.class);
	}
	
	/**
	 * Populates available file formats, report types and stock symbols
	 */
	public ReportForm(CompanyService companyService) {
		availableFormats = new HashSet<>();
		availableFormats.add(FORMAT_CSV);
		availableFormats.add(FORMAT_XML);
		availableTypes = new LinkedHashMap<>();
		availableTypes.put(TYPE_COMPANY, Company.class);
		availableTypes.put(TYPE_OWNED_SHARE, OwnedShare.class);
		availableStockSymbols = new TreeSet<>();
		for (Company company : companyService.findAll()) {
			availableStockSymbols.add(company.getSymbol());
		}
	}
	
	public String getFormat() {
		return format;
	}
	
	/**
	 * @param Allow format to be set only to available formats
	 */
	public void setFormat(String format) {
		if (availableFormats.contains(format)) {
			this.format = format;
		} else {
			// TODO: throw exception
			System.out.println("Bad report format specified: " + format);
		}
	}
	
	public Class<?> getType() {
		return type;
	}
	
	public void setType(String typeString) {
		if (availableTypes.containsKey(typeString)) {
			this.type = availableTypes.get(typeString);
		} else {
			// TODO: throw exception
			System.out.println("Bad report type specified: " + format);
		}
	}
	
	public Set<String> getStockSymbols() {
		return stockSymbols;
	}
	
	public void setStockSymbols(Set<String> stockSymbols) {
		this.stockSymbols = stockSymbols;
	}

	public Set<String> getAvailableFormats() {
		return availableFormats;
	}

	public Set<String> getAvailableTypes() {
		return availableTypes.keySet();
	}
	
	public Set<String> getAvailableStockSymbols() {
		return availableStockSymbols;
	}
	
}
