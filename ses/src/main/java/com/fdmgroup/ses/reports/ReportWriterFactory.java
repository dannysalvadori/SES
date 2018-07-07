package com.fdmgroup.ses.reports;

import org.springframework.stereotype.Component;

@Component
public class ReportWriterFactory {
	
	/**
	 * Returns the SesValidator implementation correct to the given object's Class
	 */
	public static ReportWriter<?> getReportWriter(String format, Class<?> type, Report report) {
		ReportWriter<?> reportWriter;
		
		if (format.equalsIgnoreCase("XML")) {
			reportWriter = new XMLWriter<>(report, type);
		} else {
			reportWriter = new CSVWriter<>(report, type);
		}
		
		return reportWriter;
	}
	
}
