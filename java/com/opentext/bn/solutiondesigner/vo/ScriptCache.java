package com.opentext.bn.solutiondesigner.vo;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.opentext.bn.solutiondesigner.util.SolutionDesignerConstant;

/*
 * This class represents the response JSON attribute of Script cache ReST API.
 * Added a new attribute to persist the script ID used while registering. 
 * 
 */

public class ScriptCache {

	@NotNull(message = "Message attribute is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.MSG_REG_EXP, message = SolutionDesignerConstant.MSG_REG_EXP_MESSAGE)
	private String message;

	@Pattern(regexp = SolutionDesignerConstant.UUID_REG_EXP, message = SolutionDesignerConstant.UUID_REG_EXP_MESSAGE)
	private String scriptId;

	@NotNull(message = "Status is mandatory")
	@Min(200)
	@Max(599)
	private int status;

	public String getMessage() {
		return message;
	}

	public String getScriptId() {
		return scriptId;
	}

	public int getStatus() {
		return status;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setScriptId(String scriptId) {
		this.scriptId = scriptId;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
