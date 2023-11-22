package com.opentext.bn.solutiondesigner.vo.itinerary.process;

public class InputPayload {
	private String payloadKey = null;
	private String payloadUri = null;
	private String payloadType = null;
	private String payloadFilename = null;
	private PayloadMetaData payloadMetaData = null;

	public String getPayloadFilename() {
		return payloadFilename;
	}

	public String getPayloadKey() {
		return payloadKey;
	}

	public PayloadMetaData getPayloadMetaData() {
		return payloadMetaData;
	}

	public String getPayloadType() {
		return payloadType;
	}

	public String getPayloadUri() {
		return payloadUri;
	}

	public void setPayloadFilename(String payloadFilename) {
		this.payloadFilename = payloadFilename;
	}

	public void setPayloadKey(String payloadKey) {
		this.payloadKey = payloadKey;
	}

	public void setPayloadMetaData(PayloadMetaData payloadMetaData) {
		this.payloadMetaData = payloadMetaData;
	}

	public void setPayloadType(String payloadType) {
		this.payloadType = payloadType;
	}

	public void setPayloadUri(String payloadUri) {
		this.payloadUri = payloadUri;
	}
}
