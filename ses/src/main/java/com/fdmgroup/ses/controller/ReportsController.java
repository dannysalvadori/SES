package com.fdmgroup.ses.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.reports.Report;
import com.fdmgroup.ses.reports.ReportBuilder;
import com.fdmgroup.ses.reports.ReportForm;
import com.fdmgroup.ses.reports.ReportWriter;
import com.fdmgroup.ses.reports.ReportWriterFactory;
import com.fdmgroup.ses.service.CompanyService;
import com.fdmgroup.ses.service.OwnedSharesService;

@Controller
public class ReportsController {
	
	@Autowired
	private OwnedSharesService ownedSharesService;
	
	@Autowired
	private CompanyService companyService;

	/**
	 * Go to reports home page
	 */
	@RequestMapping(value="/user/reports")
    public ModelAndView goToReports(ModelAndView modelAndView) {
		modelAndView.setViewName("user/reports");
		ReportForm reportForm = new ReportForm();
		modelAndView.addObject("reportForm", reportForm);
		modelAndView.addObject("reportFormats", reportForm.getAvailableFormats());
		modelAndView.addObject("reportTypes", reportForm.getAvailableTypes());
		return modelAndView;
	}
	
	/**
	 * Go to reports home page
	 */
	@RequestMapping(value="/user/doRequestReport")
    public ResponseEntity<InputStreamResource> requestReport(ModelAndView modelAndView, HttpServletResponse response) {
		
		Report<?> report = new ReportBuilder(ownedSharesService, companyService).buildReport(Company.class);
		
		ReportWriter<?> writer = ReportWriterFactory.getReportWriter("XML", report);
		
		System.out.println("Here comes the report...");
		String reportBody = writer.writeReport();
		System.out.println(reportBody);
		modelAndView.setViewName("user/myAccount");
		
		InputStream stream = new ByteArrayInputStream(reportBody.getBytes(StandardCharsets.UTF_8));
		InputStreamResource resource = new InputStreamResource(stream);
		
		MediaType mediaType = new MediaType("text", "html");
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment;filename=" + report.generateFileName() + writer.getFileExtension())
				.contentType(mediaType)
				.contentLength(reportBody.length())
				.body(resource);
	}
		
}
