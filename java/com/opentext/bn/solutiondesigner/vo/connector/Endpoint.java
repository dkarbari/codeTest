package com.opentext.bn.solutiondesigner.vo.connector;

import java.util.List;

public class Endpoint {
	private String id = null;
	private String description = null;
	private List<String> baseUrls = null;
	private List<Variables> variables = null;
	private String endpointRequestMethod = null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getBaseUrls() {
		return baseUrls;
	}

	public void setBaseUrls(List<String> baseUrls) {
		this.baseUrls = baseUrls;
	}

	public List<Variables> getVariables() {
		return variables;
	}

	public void setVariables(List<Variables> variables) {
		this.variables = variables;
	}

	public String getEndpointRequestMethod() {
		return endpointRequestMethod;
	}

	public void setEndpointRequestMethod(String endpointRequestMethod) {
		this.endpointRequestMethod = endpointRequestMethod;
	}

}
