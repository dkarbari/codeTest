package com.opentext.bn.solutiondesigner.vo.itinerary.process;

public class PayloadMetaData {

	private String snrf = null;
	private String sender = null;
	private String docType = null;
	private String receiver = null;
	private String dataType = null;

	public String getDataType() {
		return dataType;
	}

	public String getDocType() {
		return docType;
	}

	public String getReceiver() {
		return receiver;
	}

	public String getSender() {
		return sender;
	}

	public String getSnrf() {
		return snrf;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setSnrf(String snrf) {
		this.snrf = snrf;
	}
}