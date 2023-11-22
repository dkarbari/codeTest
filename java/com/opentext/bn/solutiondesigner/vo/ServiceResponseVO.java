package com.opentext.bn.solutiondesigner.vo;

import java.util.HashMap;
import java.util.Map;

import com.opentext.bn.solutiondesigner.vo.serviceregistry.RegisteredServicesList;

public class ServiceResponseVO implements ResponseVO {
	private Map<String, RegisteredServicesList> serviceCodeResponse = null;

	public ServiceResponseVO() {
		this.serviceCodeResponse = new HashMap<String, RegisteredServicesList>();
	}

	public Map<String, RegisteredServicesList> getServiceCodeResponse() {
		return serviceCodeResponse;
	}

	public void setServiceCodeResponse(Map<String, RegisteredServicesList> serviceCodeResponse) {
		this.serviceCodeResponse = serviceCodeResponse;
	}

}
