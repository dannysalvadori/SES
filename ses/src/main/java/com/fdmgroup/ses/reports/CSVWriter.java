package com.fdmgroup.ses.reports;

public class CSVWriter<T> extends ReportWriter<T> {
	
	public CSVWriter(Report<T> report) {
		super(report);
	}
	
	protected String writeOpen(Report<T> report) {
		String opening = report.getTitle() + "\n";
		for (String column : report.getRowDefinition().getColumnValueMap().keySet()) {
			opening += column + ",";
		}
		opening += "\n";
		return opening;
	};
	
	protected String writeRow(T row) {
		String rowOutput = "";
		for (String column : report.getRowDefinition().getColumnValueMap().keySet()) {
			String fieldName = report.getRowDefinition().getColumnValueMap().get(column);
			rowOutput += ReportWriterUtils.getFieldValue(row, fieldName);
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

	@Override
	public String getFileExtension() {
		return ".csv";
	};
}
