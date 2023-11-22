package com.opentext.bn.solutiondesigner.vo.serviceregistry;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.opentext.bn.solutiondesigner.util.SolutionDesignerConstant;

public class CMDResponse {

	@NotNull(message = "Unique ID is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.SERVICE_INSTANCE_ID_REG_EXP, message = SolutionDesignerConstant.SERVICE_INSTANCE_ID_REG_EXP_MESSAGE)
	private String uniqueId;

	private String description;

	public String getDescription() {
		return description;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

}
