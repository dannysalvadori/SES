package com.fdmgroup.ses.reports;

/**
 * Defines the structure for report writers
 */
public abstract class ReportWriter<T> {
	
	protected Report<T> report;
	public ReportWriter(Report<T> report) {
		this.report = report;
	}
	
	/**
	 * Calls the required private methods to generate the report body
	 */
	public final String writeReport() {
		String body = writeOpen(report);
		body += writeRows();
		body += writeClose(report);
		return body;
	}
	
	/**
	 * Iterates over rows calling writeRow()
	 */
	private String writeRows() {
		String rowOutput = "";
		for (T row : report.getRows()) {
			rowOutput += writeRow(row);
		}
		return rowOutput;
	}
	
	// Abstract methods to be implemented
	protected abstract String writeOpen(Report<T> report);
	protected abstract String writeRow(T row);
	protected abstract String writeClose(Report<T> report);
	public abstract String getFileExtension();
}
