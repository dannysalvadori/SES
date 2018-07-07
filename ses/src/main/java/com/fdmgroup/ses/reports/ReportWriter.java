package com.fdmgroup.ses.reports;

public abstract class ReportWriter<T> {
	protected Report<T> report;
	protected final Class<T> type;
	public ReportWriter(Report<T> report, Class<T> type) {
		this.report = report;
		this.type = type;
	}
	public final String writeReport() {
		String body = writeOpen(report);
		body += writeRows();
		body += writeClose(report);
		return body;
	}
	private String writeRows() {
		String rowOutput = "";
		for (T row : report.getRows()) {
			rowOutput += writeRow(row);
		}
		return rowOutput;
	}
	protected abstract String writeOpen(Report<T> report);
	protected abstract String writeRow(T row);
	protected abstract String writeClose(Report<T> report);
}