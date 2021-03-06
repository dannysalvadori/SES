package com.fdmgroup.ses.reports;

import java.util.ArrayList;
import java.util.List;

public abstract class Report<T> {

	protected String title = "undefined";
	protected List<T> rows;
	protected RowDefinition rowDefinition = new RowDefinition();
	
	public Report() {
		rows = new ArrayList<T>();
		setTitle();
	}
	
	public List<T> getRows() {
		return rows;
	}
	
	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public RowDefinition getRowDefinition() {
		return rowDefinition;
	}

	protected String getTitle() {
		return title;
	};
	
	protected abstract void setTitle();

	public abstract String generateFileName();

}
