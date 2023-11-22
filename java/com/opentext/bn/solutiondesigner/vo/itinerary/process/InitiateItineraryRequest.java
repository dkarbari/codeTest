package com.opentext.bn.solutiondesigner.vo.itinerary.process;

public class InitiateItineraryRequest {
	private Header header = null;
	private ProcessMetaData processMetaData = null;
	private ItineraryRequest itineraryRequest = null;
	private RequestingService requestingService = null;
	private LegacyItineraryRequestAttributes legacyItineraryRequestAttributes = null;

	public Header getHeader() {
		return header;
	}

	public ItineraryRequest getItineraryRequest() {
		return itineraryRequest;
	}

	public LegacyItineraryRequestAttributes getLegacyItineraryRequestAttributes() {
		return legacyItineraryRequestAttributes;
	}

	public ProcessMetaData getProcessMetaData() {
		return processMetaData;
	}

	public RequestingService getRequestingService() {
		return requestingService;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public void setItineraryRequest(ItineraryRequest itineraryRequest) {
		this.itineraryRequest = itineraryRequest;
	}

	public void setLegacyItineraryRequestAttributes(LegacyItineraryRequestAttributes legacyItineraryRequestAttributes) {
		this.legacyItineraryRequestAttributes = legacyItineraryRequestAttributes;
	}

	public void setProcessMetaData(ProcessMetaData processMetaData) {
		this.processMetaData = processMetaData;
	}

	public void setRequestingService(RequestingService requestingService) {
		this.requestingService = requestingService;
	}

}
