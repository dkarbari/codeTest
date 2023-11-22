package com.opentext.bn.solutiondesigner.vo.serviceregistry;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.opentext.bn.solutiondesigner.util.SolutionDesignerConstant;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ServiceInfo {

	@NotNull(message = "Service code is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.SCODE_REG_EXP, message = SolutionDesignerConstant.SCODE_REG_EXP_MESSAGE)
	private String serviceCode;

	@NotNull(message = "Service name is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.SCODE_REG_EXP, message = SolutionDesignerConstant.SCODE_REG_EXP_MESSAGE)
	private String serviceName;

	@NotNull(message = "Entry type is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.GENERAL_REG_EXP_LEN_200, message = SolutionDesignerConstant.ENTRY_TYPE
			+ SolutionDesignerConstant.GENERAL_REG_EXP_MESSAGE_LEN_200)
	private String entryType;

	@Pattern(regexp = SolutionDesignerConstant.GENERAL_REG_EXP_LEN_250, message = SolutionDesignerConstant.DESCRIPTION
			+ SolutionDesignerConstant.GENERAL_REG_EXP_MESSAGE_LEN_250)
	private String description;

	@NotNull(message = "Service version is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.GENERAL_REG_EXP_LEN_15, message = SolutionDesignerConstant.SERVICE_VERSION
			+ SolutionDesignerConstant.GENERAL_REG_EXP_MESSAGE_LEN_15)
	private String serviceVersion;

	@Pattern(regexp = SolutionDesignerConstant.GENERAL_REG_EXP_LEN_20, message = SolutionDesignerConstant.DEPLOYMENT_VERSION
			+ SolutionDesignerConstant.GENERAL_REG_EXP_MESSAGE_LEN_20)
	private String serviceDeploymentVersion;

	@Pattern(regexp = SolutionDesignerConstant.GENERAL_REG_EXP_LEN_200, message = SolutionDesignerConstant.COMPUTE_ZONE
			+ SolutionDesignerConstant.GENERAL_REG_EXP_MESSAGE_LEN_200)
	private String computeZoneId;

	@Pattern(regexp = SolutionDesignerConstant.GENERAL_REG_EXP_LEN_200, message = SolutionDesignerConstant.PROCESSING_CELL
			+ SolutionDesignerConstant.GENERAL_REG_EXP_MESSAGE_LEN_200)
	private String processingCell;

	public String getComputeZoneId() {
		return computeZoneId;
	}

	public String getDescription() {
		return description;
	}

	public String getEntryType() {
		return entryType;
	}

	public String getProcessingCell() {
		return processingCell;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public String getServiceDeploymentVersion() {
		return serviceDeploymentVersion;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getServiceVersion() {
		return serviceVersion;
	}

	public void setComputeZoneId(String computeZoneId) {
		this.computeZoneId = computeZoneId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}

	public void setProcessingCell(String processingCell) {
		this.processingCell = processingCell;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public void setServiceDeploymentVersion(String serviceDeploymentVersion) {
		this.serviceDeploymentVersion = serviceDeploymentVersion;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
