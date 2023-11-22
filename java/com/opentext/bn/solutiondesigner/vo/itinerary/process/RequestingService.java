package com.opentext.bn.solutiondesigner.vo.itinerary.process;

public class RequestingService {
	private String serviceCode = null;
	private String serviceHost = null;
	private String serviceZoneId = null;
	private String proxiedRequestorServiceCode = null;

	public String getProxiedRequestorServiceCode() {
		return proxiedRequestorServiceCode;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public String getServiceHost() {
		return serviceHost;
	}

	public String getServiceZoneId() {
		return serviceZoneId;
	}

	public void setProxiedRequestorServiceCode(String proxiedRequestorServiceCode) {
		this.proxiedRequestorServiceCode = proxiedRequestorServiceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public void setServiceHost(String serviceHost) {
		this.serviceHost = serviceHost;
	}

	public void setServiceZoneId(String serviceZoneId) {
		this.serviceZoneId = serviceZoneId;
	}
}