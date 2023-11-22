package com.opentext.bn.solutiondesigner.vo.itinerary.definition;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.opentext.bn.solutiondesigner.util.SolutionDesignerConstant;

public class ChildServiceCode {

	@NotNull(message = "Endpoint information is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.UUID_REG_EXP, message = SolutionDesignerConstant.UUID_REG_EXP_MESSAGE)
	private String endpoint = null;

	@NotNull(message = "Service code is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.CONN_SCODE_REG_EXP, message = SolutionDesignerConstant.CONN_SCODE_REG_EXP_MESSAGE)
	private String serviceCode = null;

	@NotNull(message = "Type is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.SID_REG_EXP, message = SolutionDesignerConstant.SID_REG_EXP_MESSAGE)
	private String type = null;

	public String getEndpoint() {
		return endpoint;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public String getType() {
		return type;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public void setType(String type) {
		this.type = type;
	}
}
