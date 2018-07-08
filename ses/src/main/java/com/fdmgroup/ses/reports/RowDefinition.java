package com.fdmgroup.ses.reports;

import java.util.HashMap;
import java.util.Map;

public class RowDefinition {

	private Map<String, String> columnValueMap = new HashMap<>(); // TODO: use ordered map!!
	
	public Map<String, String> getColumnValueMap() {
		return columnValueMap;
	}

	public void putColumnValueMapping(String key, String value) {
		columnValueMap.put(key, value);		
	}

}
