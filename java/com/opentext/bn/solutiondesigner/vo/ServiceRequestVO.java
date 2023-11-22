package com.opentext.bn.solutiondesigner.vo;

import java.util.List;

public class ServiceRequestVO implements RequestVO {
	private List<String> serviceCodeList = null;

	public List<String> getServiceCodeList() {
		return serviceCodeList;
	}

	public void setServiceCodeList(List<String> serviceCodeList) {
		this.serviceCodeList = serviceCodeList;
	}
}
