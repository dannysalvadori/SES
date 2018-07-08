package com.fdmgroup.ses.reports;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.OwnedShare;

public class ReportForm {
	
	private List<String> availableFormats;
	private static final String FORMAT_CSV = "CSV"; 
	private static final String FORMAT_XML = "CSV"; 
	private Map<String, Class<?>> availableTypes;
	private static final String TYPE_COMPANY = "company"; 
	private static final String TYPE_OWNED_SHARE = "ownedshare"; 
	
	private String format;
	
	private Class<?> type;
	
	public ReportForm() {
		availableFormats = new LinkedList<>();
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
		for (String validFormat : availableFormats) {
			if (format.equalsIgnoreCase(validFormat)) {
				this.format = format;
			}
		}
	}
	
	public Class<?> getType() {
		return type;
	}
	
	public void setType(String typeString) {
		if (availableTypes.containsKey(typeString)) {
			this.type = availableTypes.get(typeString);
		}			
	}
	
}
