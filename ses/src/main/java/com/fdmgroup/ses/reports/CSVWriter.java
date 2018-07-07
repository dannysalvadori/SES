package com.fdmgroup.ses.reports;

public class CSVWriter<T> extends ReportWriter<T> {
	
	public CSVWriter(Report<T> report) {
		super(report);
	}
	
	protected String writeOpen(Report<T> report) {
		return "Report title\n";
	};
	
	protected String writeRow(T row) {
		String rowOutput = "";
		for (String column : report.getRowDefinition().getColumnValueMap().keySet()) {
			String valueField = report.getRowDefinition().getColumnValueMap().get(column);
			rowOutput += valueField + " value,";
		}
		if (!rowOutput.isEmpty()) {
//			rowOutput.removeLast(","); // TODO: implement
			rowOutput += "\n";
		}
		return rowOutput;
				
	}
	
	protected String writeClose(Report<T> report) {
		return "";
	};
}
