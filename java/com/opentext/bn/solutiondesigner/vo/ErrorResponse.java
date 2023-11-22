package com.opentext.bn.solutiondesigner.vo;

public class ErrorResponse {
	private int errorCode = 0;
	private String errorMessage = null;
	
	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public void setErrorMessage(final String errorMessage) {
		this.errorMessage = errorMessage;
	}			
}
