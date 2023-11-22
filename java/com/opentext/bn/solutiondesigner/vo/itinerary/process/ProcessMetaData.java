package com.opentext.bn.solutiondesigner.vo.itinerary.process;

public class ProcessMetaData {
	private String snrf = null;
	private String orgId = null;
	private String sender = null;
	private String docType = null;
	private String receiver = null;
	private String dataType = null;
	private String rootOrgId = null;
	private String solutionId = null;
	private String ownerIndicator = null;

	public String getDataType() {
		return dataType;
	}

	public String getDocType() {
		return docType;
	}

	public String getOrgId() {
		return orgId;
	}

	public String getOwnerIndicator() {
		return ownerIndicator;
	}

	public String getReceiver() {
		return receiver;
	}

	public String getRootOrgId() {
		return rootOrgId;
	}

	public String getSender() {
		return sender;
	}

	public String getSnrf() {
		return snrf;
	}

	public String getSolutionId() {
		return solutionId;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public void setOwnerIndicator(String ownerIndicator) {
		this.ownerIndicator = ownerIndicator;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public void setRootOrgId(String rootOrgId) {
		this.rootOrgId = rootOrgId;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setSnrf(String snrf) {
		this.snrf = snrf;
	}

	public void setSolutionId(String solutionId) {
		this.solutionId = solutionId;
	}
}
