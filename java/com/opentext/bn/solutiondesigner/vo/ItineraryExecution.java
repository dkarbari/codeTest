package com.opentext.bn.solutiondesigner.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.opentext.bn.solutiondesigner.util.SolutionDesignerConstant;

/*
 * This class represents the response JSON attribute of Itinerary process ReST API.
 */

public class ItineraryExecution {

	@NotNull(message = "Itinerary statys attribute is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.MSG_REG_EXP, message = SolutionDesignerConstant.MSG_REG_EXP_MESSAGE)
	private String itineraryStatus;

	@NotNull(message = "Status code attribute is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.DOC_TYPE_REG_EXP, message = SolutionDesignerConstant.DOC_TYPE_REG_EXP_MESSAGE)
	private String statusCode;

	public String getItineraryStatus() {
		return itineraryStatus;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setItineraryStatus(String itineraryStatus) {
		this.itineraryStatus = itineraryStatus;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

}
