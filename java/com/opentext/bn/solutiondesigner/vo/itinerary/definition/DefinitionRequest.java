package com.opentext.bn.solutiondesigner.vo.itinerary.definition;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.opentext.bn.solutiondesigner.util.SolutionDesignerConstant;

public class DefinitionRequest {

	@NotNull(message = "Task name is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.TASK_NAME_REG_EXP, message = SolutionDesignerConstant.TASK_NAME_REG_EXP_MESSAGE)
	private String taskName;

	@NotNull(message = "Script id is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.UUID_REG_EXP, message = SolutionDesignerConstant.UUID_REG_EXP_MESSAGE)
	private String scriptId;

	@NotNull(message = "Service code is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.SCODE_REG_EXP, message = SolutionDesignerConstant.SCODE_REG_EXP_MESSAGE)
	private String serviceCode;

	@Valid
	private TaskEmbeddedProperties taskEmbeddedProperties;

	public String getScriptId() {
		return scriptId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public TaskEmbeddedProperties getTaskEmbeddedProperties() {
		return taskEmbeddedProperties;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setScriptId(String scriptId) {
		this.scriptId = scriptId;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public void setTaskEmbeddedProperties(TaskEmbeddedProperties taskEmbeddedProperties) {
		this.taskEmbeddedProperties = taskEmbeddedProperties;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

}
