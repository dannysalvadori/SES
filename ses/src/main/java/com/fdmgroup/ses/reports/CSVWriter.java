package com.fdmgroup.ses.reports;

public class CSVWriter<T> extends ReportWriter<T> {
	
	public CSVWriter(Report<T> report, Class<T> type) {
		super(report, type);
	}
	
	protected String writeOpen(Report<T> report) {
		return report.generateTitle() + "\n";
	};
	
	protected String writeRow(T row) {
		String rowOutput = "";
		for (String column : report.getRowDefinition().getColumnValueMap().keySet()) {
			String fieldName = report.getRowDefinition().getColumnValueMap().get(column);
			rowOutput += ReportWriterUtils.getFieldValue(type, row, fieldName);
			rowOutput += ",";
		}
		if (!rowOutput.isEmpty()) {
			// Remove trailing comma and add line break
			rowOutput = rowOutput.substring(0, rowOutput.lastIndexOf(",")) + "\n";
		}
		return rowOutput;
				
	}
	
	protected String writeClose(Report<T> report) {
		return "";
	};
}
