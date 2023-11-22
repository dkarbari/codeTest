package com.opentext.bn.solutiondesigner.vo.serviceregistry;

import javax.validation.constraints.Pattern;

import com.opentext.bn.solutiondesigner.util.SolutionDesignerConstant;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class OrchestrationInfo {

	@Pattern(regexp = SolutionDesignerConstant.SERVICE_CLASS_NAME_REG_EXP, message = SolutionDesignerConstant.SERVICE_CLASS_NAME_REG_EXP_MESSAGE)
	private String serviceClassName;

	@Pattern(regexp = SolutionDesignerConstant.GENERAL_REG_EXP_LEN_50, message = SolutionDesignerConstant.ORCH_TYPE
			+ SolutionDesignerConstant.GENERAL_REG_EXP_MESSAGE_LEN_50)
	private String orchestrationType;

	@Pattern(regexp = SolutionDesignerConstant.GENERAL_REG_EXP_LEN_50, message = SolutionDesignerConstant.GENERAL_REG_EXP_MESSAGE_LEN_50)
	private String serviceEnabled;

	private boolean supportsDiagnostics;

	@Pattern(regexp = SolutionDesignerConstant.GENERAL_REG_EXP_LEN_200, message = SolutionDesignerConstant.INVOCATION_MODE
			+ SolutionDesignerConstant.GENERAL_REG_EXP_MESSAGE_LEN_200)
	private String invocationMode;

	@Pattern(regexp = SolutionDesignerConstant.GENERAL_REG_EXP_LEN_200, message = SolutionDesignerConstant.PARENT_LOGICAL_SERVICE
			+ SolutionDesignerConstant.GENERAL_REG_EXP_MESSAGE_LEN_200)
	private String parentLogicalService;

	public String getInvocationMode() {
		return invocationMode;
	}

	public String getOrchestrationType() {
		return orchestrationType;
	}

	public String getParentLogicalService() {
		return parentLogicalService;
	}

	public String getServiceClassName() {
		return serviceClassName;
	}

	public String getServiceEnabled() {
		return serviceEnabled;
	}

	public boolean isSupportsDiagnostics() {
		return supportsDiagnostics;
	}

	public void setInvocationMode(String invocationMode) {
		this.invocationMode = invocationMode;
	}

	public void setOrchestrationType(String orchestrationType) {
		this.orchestrationType = orchestrationType;
	}

	public void setParentLogicalService(String parentLogicalService) {
		this.parentLogicalService = parentLogicalService;
	}

	public void setServiceClassName(String serviceClassName) {
		this.serviceClassName = serviceClassName;
	}

	public void setServiceEnabled(String serviceEnabled) {
		this.serviceEnabled = serviceEnabled;
	}

	public void setSupportsDiagnostics(boolean supportsDiagnostics) {
		this.supportsDiagnostics = supportsDiagnostics;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
