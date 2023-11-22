package com.opentext.bn.solutiondesigner.vo.itinerary.process;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.opentext.bn.solutiondesigner.util.SolutionDesignerConstant;

public class ProcessRequest {

	@NotNull(message = "Script id is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.UUID_REG_EXP, message = SolutionDesignerConstant.UUID_REG_EXP_MESSAGE)
	private String scriptId = null;

	@NotNull(message = "Itinerary id is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.UUID_REG_EXP, message = SolutionDesignerConstant.UUID_REG_EXP_MESSAGE)
	private String itineraryId = null;

	@Valid
	@NotNull(message = "User Input is mandatory")
	private ProcessUserInputs processUserInputs = null;

	public String getItineraryId() {
		return itineraryId;
	}

	public ProcessUserInputs getProcessUserInputs() {
		return processUserInputs;
	}

	public String getScriptId() {
		return scriptId;
	}

	public void setItineraryId(String itineraryId) {
		this.itineraryId = itineraryId;
	}

	public void setProcessUserInputs(ProcessUserInputs processUserInputs) {
		this.processUserInputs = processUserInputs;
	}

	public void setScriptId(String scriptId) {
		this.scriptId = scriptId;
	}

}