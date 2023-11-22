package com.opentext.bn.solutiondesigner.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.opentext.bn.solutiondesigner.util.SolutionDesignerConstant;

/*
 * This class represents the response JSON attribute of Itinerary definition ReST API.
 * Added a new attribute to persist the itinerary ID used while registering. 
 */
public class ItineraryDefinition {

	@NotNull(message = "Message attribute is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.MSG_REG_EXP, message = SolutionDesignerConstant.MSG_REG_EXP_MESSAGE)
	private String message;

	@NotNull(message = "Status code attribute is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.DOC_TYPE_REG_EXP, message = SolutionDesignerConstant.DOC_TYPE_REG_EXP_MESSAGE)
	private String statusCode;

	@Pattern(regexp = SolutionDesignerConstant.UUID_REG_EXP, message = SolutionDesignerConstant.UUID_REG_EXP_MESSAGE)
	private String itineraryId;

	public String getItineraryId() {
		return itineraryId;
	}

	public String getMessage() {
		return message;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setItineraryId(String itineraryId) {
		this.itineraryId = itineraryId;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

}
