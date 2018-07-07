package com.fdmgroup.ses.reports;

public class XMLWriter<T> extends ReportWriter<T> {
	
	public XMLWriter(Report<T> report, Class<T> type) {
		super(report, type);
	}
	
	protected String writeOpen(Report<T> report) {
		String output = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<report date=" + report.generateTitle() + ">"
				+ "<body>";
		return output;
	};
	
	protected String writeRow(T row) {
		String rowOutput = "<row>";
		for (String fieldName : report.getRowDefinition().getColumnValueMap().values()) {
			rowOutput += "<" + fieldName + ">";
			rowOutput += ReportWriterUtils.getFieldValue(type, row, fieldName);
			rowOutput += "</" + fieldName + ">";
		}
		rowOutput += "</row>";
		return rowOutput;				
	}
	
	protected String writeClose(Report<T> report) {
		return "</body>"
				+ "</report>";
	};
}
