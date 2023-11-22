package com.opentext.bn.solutiondesigner.vo.serviceregistry;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RegisteredServicesList {

	@JsonProperty("im-services")
	@Valid
	private List<RegisteredServices> im_services;

	public List<RegisteredServices> getIm_services() {
		return im_services;
	}

	public void setIm_services(List<RegisteredServices> im_services) {
		this.im_services = im_services;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
