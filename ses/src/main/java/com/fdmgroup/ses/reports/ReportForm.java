package com.fdmgroup.ses.reports;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.OwnedShare;

public class ReportForm {
	
	// Constants
	private Set<String> availableFormats;
	private Map<String, Class<?>> availableTypes;
	private static final String FORMAT_CSV = "CSV"; 
	private static final String FORMAT_XML = "XML"; 
	private static final String TYPE_COMPANY = "company"; 
	private static final String TYPE_OWNED_SHARE = "ownedshare"; 
	
	// Form properties
	private String format;
	private Class<?> type;
	
	public ReportForm() {
		availableFormats = new HashSet<>();
		availableFormats.add(FORMAT_CSV);
		availableFormats.add(FORMAT_XML);
		availableTypes = new LinkedHashMap<>();
		availableTypes.put(TYPE_COMPANY, Company.class);
		availableTypes.put(TYPE_OWNED_SHARE, OwnedShare.class);
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

	public Set<String> getAvailableFormats() {
		return availableFormats;
	}

	public Set<String> getAvailableTypes() {
		return availableTypes.keySet();
	}
	
	
	
}
