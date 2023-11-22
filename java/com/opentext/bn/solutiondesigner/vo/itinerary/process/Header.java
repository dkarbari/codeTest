package com.opentext.bn.solutiondesigner.vo.itinerary.process;

public class Header {
	private String eventId = null;
	private String processId = null;
	private String correlationId = null;
	private String messageVersion = null;
	private String messageCreateTimestamp = null;
	private ParentProcessID parentProcessIds = null;

	public String getCorrelationId() {
		return correlationId;
	}

	public String getEventId() {
		return eventId;
	}

	public String getMessageCreateTimestamp() {
		return messageCreateTimestamp;
	}

	public String getMessageVersion() {
		return messageVersion;
	}

	public ParentProcessID getParentProcessIds() {
		return parentProcessIds;
	}

	public String getProcessId() {
		return processId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public void setMessageCreateTimestamp(String messageCreateTimestamp) {
		this.messageCreateTimestamp = messageCreateTimestamp;
	}

	public void setMessageVersion(String messageVersion) {
		this.messageVersion = messageVersion;
	}

	public void setParentProcessIds(ParentProcessID parentProcessIds) {
		this.parentProcessIds = parentProcessIds;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}
}