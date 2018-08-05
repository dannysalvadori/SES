package com.fdmgroup.ses.reports;

import java.util.LinkedHashMap;
import java.util.Map;

public class RowDefinition {

	private Map<String, String> columnValueMap = new LinkedHashMap<>(); // Maintains FIFO order
	
	public Map<String, String> getColumnValueMap() {
		return columnValueMap;
	}

	public void putColumnValueMapping(String key, String value) {
		columnValueMap.put(key, value);		
	}

}
