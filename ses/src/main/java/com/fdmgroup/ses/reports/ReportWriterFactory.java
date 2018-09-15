package com.fdmgroup.ses.reports;

import org.springframework.stereotype.Component;

@Component
public class ReportWriterFactory {
	
	/**
	 * Returns the SesValidator implementation correct to the given object's Class
	 */
	public static ReportWriter<?> getReportWriter(String format, Report<?> report) {
		ReportWriter<?> reportWriter = null;
		
		if (format.equalsIgnoreCase("XML")) {
			reportWriter = new XMLWriter<>(report);
			
		} else if (format.equalsIgnoreCase("CSV")) {
			reportWriter = new CSVWriter<>(report);
		}
		
		return reportWriter;
	}
	
}
