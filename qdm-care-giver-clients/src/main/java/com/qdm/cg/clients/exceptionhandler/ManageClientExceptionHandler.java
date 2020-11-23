package com.qdm.cg.clients.exceptionhandler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@SuppressWarnings({ "unchecked", "rawtypes" })
@ControllerAdvice
public class ManageClientExceptionHandler extends ResponseEntityExceptionHandler {

//	@ExceptionHandler(Exception.class)
//	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
//		List<String> details = new ArrayList();
//		details.add(ex.getLocalizedMessage());
//		ErrorResponse error = new ErrorResponse("Failed", "500", "");
//		return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
//	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> details = new ArrayList();
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			details.add(error.getDefaultMessage());
		}
		ErrorResponse error = new ErrorResponse("Failed", "400", "Bad request");
		return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		List<String> details = new ArrayList();
		String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
		details.add(error);
		ErrorResponse apiError = new ErrorResponse();
//		apiError.setDetails(details);
		apiError.setStatus("Failed");
		apiError.setStatus_code("404");
		apiError.setMessage("Not Found");
		return new ResponseEntity<Object>(apiError, HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		List<String> details = new ArrayList();
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getMethod());
		builder.append(" method is not supported for this request. Supported methods are ");
		ex.getSupportedHttpMethods();
		ErrorResponse error = new ErrorResponse("Failed", "415", "Method Not Allowed");
		return new ResponseEntity<Object>(error, HttpStatus.METHOD_NOT_ALLOWED);
	}
}
