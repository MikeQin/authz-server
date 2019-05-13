package com.example.authz.server;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalErrorHandler implements ErrorController {

	@RequestMapping("/error")
	public String handleError(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
		return String.format(
				"<html><title>Error | Exception</title>"
				+ "<body><h2>Error | Exception</h2><div>Status code: <b>%s</b></div>"
				+ "<div>Exception Message: <b>%s</b></div>"
				+ "</body></html>",
				statusCode, exception == null ? "N/A" : exception.getMessage());
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

}
