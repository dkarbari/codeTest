package com.opentext.bn.solutiondesigner.vo;

import com.opentext.bn.solutiondesigner.vo.serviceregistry.RegisteredServicesList;

/*
 * This class extends ErrorResponse and captures additional information of valid and invalid registered services.
 * This VO will be returned as response object if service list controller throws HttpServerErrorException for any violations in registered services.
 */

public class RegisteredServiceListErrorResponse extends ErrorResponse {

	private RegisteredServicesList validServices = null;
	private RegisteredServicesList invalidServices = null;
	
	public RegisteredServiceListErrorResponse() {
	}
	
	public RegisteredServiceListErrorResponse(String errorMessage, RegisteredServicesList validServices, RegisteredServicesList invalidServices) {
		this.setErrorMessage(errorMessage);
		this.validServices = validServices;
		this.invalidServices = invalidServices;
	}

	public RegisteredServicesList getValidServices() {
		return validServices;
	}

	public void setValidServices(RegisteredServicesList validServices) {
		this.validServices = validServices;
	}

	public RegisteredServicesList getInvalidServices() {
		return invalidServices;
	}

	public void setInvalidServices(RegisteredServicesList invalidServices) {
		this.invalidServices = invalidServices;
	}

	@Override
	public String toString() {
		return "RegisteredServiceListErrorResponse [validServices=" + validServices + ", invalidServices="
				+ invalidServices + ", ErrorMessage=" + getErrorMessage() + "]";
	}	
}
