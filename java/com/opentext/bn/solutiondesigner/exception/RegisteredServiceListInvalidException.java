package com.opentext.bn.solutiondesigner.exception;

import com.opentext.bn.solutiondesigner.vo.RegisteredServiceListErrorResponse;

/*
 * This exception class is used to throw exception from controller to handle invalid registered services use-case .
 */

public class RegisteredServiceListInvalidException extends Exception {

	private static final long serialVersionUID = 1L;
	private RegisteredServiceListErrorResponse errorResponse;
	
	public RegisteredServiceListInvalidException() {
	}

	public RegisteredServiceListInvalidException(RegisteredServiceListErrorResponse errorResponse) {
		this.errorResponse = errorResponse;
	}
	
	public RegisteredServiceListErrorResponse getErrorResponse() {
		return errorResponse;
	}
	
	public void setErrorResponse(RegisteredServiceListErrorResponse errorResponse) {
		this.errorResponse = errorResponse;
	}	
}
