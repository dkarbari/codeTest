/***
 * Respective exception handler will be invoked by framework.
 */
package com.opentext.bn.solutiondesigner.util;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gxs.orch.rest.client.OrchRestException;
import com.opentext.bn.solutiondesigner.exception.RegisteredServiceListInvalidException;
import com.opentext.bn.solutiondesigner.vo.ErrorResponse;
import com.opentext.bn.solutiondesigner.vo.RegisteredServiceListErrorResponse;

@ControllerAdvice
public class SolutionDesignerControllerAdvice {
	final Logger logger = LoggerFactory.getLogger(SolutionDesignerControllerAdvice.class);

	@ExceptionHandler(value = ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolationException(
			ConstraintViolationException constraintViolationException) {
		final ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		final Set<ConstraintViolation<?>> fieldErrors = constraintViolationException.getConstraintViolations();
		final StringBuffer stringBuffer = new StringBuffer();
		fieldErrors.forEach(contsraint -> {
			stringBuffer.append(contsraint.getMessage() + ". Value passed:" + contsraint.getInvalidValue() + "\r\n");
		});

		errorResponse.setErrorMessage(stringBuffer.toString());
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = HttpClientErrorException.class)
	public ResponseEntity<ErrorResponse> handleHttpClientErrorException(
			HttpClientErrorException httpClientErrorException) {
		logger.error("Http Client Error Exception", httpClientErrorException);

		final ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(httpClientErrorException.getRawStatusCode());
		errorResponse.setErrorMessage(httpClientErrorException.getResponseBodyAsString());
		return new ResponseEntity<ErrorResponse>(errorResponse, httpClientErrorException.getStatusCode());
	}

	@ExceptionHandler(value = HttpServerErrorException.class)
	public ResponseEntity<ErrorResponse> handleHttpServerErrorException(
			HttpServerErrorException httpServerErrorException) {
		logger.error("Http Server Error Exception", httpServerErrorException);
		final ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(httpServerErrorException.getRawStatusCode());
		errorResponse.setErrorMessage(httpServerErrorException.getResponseBodyAsString());
		return new ResponseEntity<ErrorResponse>(errorResponse, httpServerErrorException.getStatusCode());		
	}

	@ExceptionHandler(value = IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
			IllegalArgumentException illegalArgumentException) {

		logger.warn("Orch Exception", illegalArgumentException);
		final ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errorResponse.setErrorMessage(illegalArgumentException.getMessage());
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = JSONException.class)
	public ResponseEntity<ErrorResponse> handleItineraryException(JSONException jsonException) {
		logger.warn("JSON Exception", jsonException);
		final ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errorResponse.setErrorMessage(jsonException.getMessage());
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentException(
			MethodArgumentNotValidException methodArgumentNotValidException) {
		final ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		final List<FieldError> fieldErrors = methodArgumentNotValidException.getBindingResult().getFieldErrors();
		final StringBuffer stringBuffer = new StringBuffer();
		for (FieldError fieldError : fieldErrors) {
			stringBuffer.append(fieldError.getField() + ":" + fieldError.getRejectedValue() + ":"
					+ fieldError.getDefaultMessage() + "\r\n");
		}

		errorResponse.setErrorMessage(stringBuffer.toString());
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = OrchRestException.class)
	public ResponseEntity<ErrorResponse> handleOrchRestException(OrchRestException orchRestException) {
		logger.warn("Orch Exception", orchRestException);
		final ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(Integer.parseInt(orchRestException.getErrorCode()));
		errorResponse.setErrorMessage(orchRestException.getMessage());
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = IOException.class)
	public ResponseEntity<ErrorResponse> handleIOException(IOException ioException) {
		final ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorResponse.setErrorMessage(ioException.getMessage());
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = JsonProcessingException.class)
	public ResponseEntity<ErrorResponse> handleJsonProcessingException(
			JsonProcessingException jsonProcessingException) {
		final ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorResponse.setErrorMessage(jsonProcessingException.getMessage());
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = RestClientException.class)
	public ResponseEntity<ErrorResponse> handleRestClientException(RestClientException restClientException) {
		final ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorResponse.setErrorMessage(restClientException.getMessage());
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = Throwable.class)
	public ResponseEntity<ErrorResponse> handleOtherException(Throwable throwable) {
		final ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorResponse.setErrorMessage(throwable.getMessage());
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/*
	 * Custom Exception handler to handle invalid registered services use-case which is thrown from controller.
	 * The response (RegisteredServiceListErrorResponse) from this method contains errorMessage, errorCode, invalidServices and validServices.
	 * 
	 * @param RegisteredServiceListInvalidException - Exception thrown from controller which contains details for invalid registered list.
	 * @return RegisteredServiceListErrorResponse
	 */
	 
	@ExceptionHandler(value = RegisteredServiceListInvalidException.class)
	public ResponseEntity<RegisteredServiceListErrorResponse> handleRegisteredServiceInvalidException(
			RegisteredServiceListInvalidException serviceException) {
		logger.error("Registered Service Invalid Exception", serviceException);
		final RegisteredServiceListErrorResponse errorResponse = serviceException.getErrorResponse();
		errorResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<RegisteredServiceListErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
