package com.opentext.bn.solutiondesigner.vo.itinerary.process;

public class LegacyItineraryRequestAttributes {
	private String node = null;
	private String serviceEndPoint = null;
	private String routingReferenceUrl = null;

	public String getNode() {
		return node;
	}

	public String getRoutingReferenceUrl() {
		return routingReferenceUrl;
	}

	public String getServiceEndPoint() {
		return serviceEndPoint;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public void setRoutingReferenceUrl(String routingReferenceUrl) {
		this.routingReferenceUrl = routingReferenceUrl;
	}

	public void setServiceEndPoint(String serviceEndPoint) {
		this.serviceEndPoint = serviceEndPoint;
	}

}
