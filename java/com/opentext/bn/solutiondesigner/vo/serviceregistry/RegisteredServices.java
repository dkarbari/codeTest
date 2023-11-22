package com.opentext.bn.solutiondesigner.vo.serviceregistry;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RegisteredServices {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String link;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String serviceInstanceId;

	@Valid
	@NotNull
	private ServiceInfo serviceInfo;

	@Valid
	private DesignerInfo designerInfo;

	@Valid
	private ProvisioningInfo provisioningInfo;

	@Valid
	@NotNull
	private OrchestrationInfo orchestrationInfo;

	public DesignerInfo getDesignerInfo() {
		return designerInfo;
	}

	public String getLink() {
		return link;
	}

	public OrchestrationInfo getOrchestrationInfo() {
		return orchestrationInfo;
	}

	public ProvisioningInfo getProvisioningInfo() {
		return provisioningInfo;
	}

	public ServiceInfo getServiceInfo() {
		return serviceInfo;
	}

	public String getServiceInstanceId() {
		return serviceInstanceId;
	}

	public void setDesignerInfo(DesignerInfo designerInfo) {
		this.designerInfo = designerInfo;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setOrchestrationInfo(OrchestrationInfo orchestrationInfo) {
		this.orchestrationInfo = orchestrationInfo;
	}

	public void setProvisioningInfo(ProvisioningInfo provisioningInfo) {
		this.provisioningInfo = provisioningInfo;
	}

	public void setServiceInfo(ServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}

	public void setServiceInstanceId(String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
