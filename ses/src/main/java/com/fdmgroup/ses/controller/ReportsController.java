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
import org.springframework.web.bind.annotation.ModelAttribute;
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
		ReportForm reportForm = new ReportForm(companyService);
		modelAndView.addObject("reportForm", reportForm);
		modelAndView.addObject("reportFormats", reportForm.getAvailableFormats());
		modelAndView.addObject("reportTypes", reportForm.getAvailableTypes());
		modelAndView.addObject("availableStockSymbols", reportForm.getAvailableStockSymbols());
		return modelAndView;
	}
	
	/**
	 * Generate report download
	 */
	@RequestMapping(value="/user/doRequestReport")
    public ResponseEntity<InputStreamResource> requestReport(
    		ModelAndView modelAndView,
    		HttpServletResponse response,
    		@ModelAttribute("reportForm") ReportForm reportForm
    ) {
		// Set report type and format
		Report<?> report = new ReportBuilder(ownedSharesService, companyService).buildReport(reportForm);
		ReportWriter<?> writer = ReportWriterFactory.getReportWriter(reportForm.getFormat(), report);
		
		// Write downloadable file TODO: extract into fileUtils class
		String reportBody = writer.writeReport();
		InputStream stream = new ByteArrayInputStream(reportBody.getBytes(StandardCharsets.UTF_8));
		InputStreamResource resource = new InputStreamResource(stream);
		MediaType mediaType = new MediaType("text", "html");
		ResponseEntity<InputStreamResource> responseEntity = ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment;filename=" + report.generateFileName() + writer.getFileExtension())
				.contentType(mediaType)
				.contentLength(reportBody.length())
				.body(resource);
		
		return responseEntity;
	}
		
}
