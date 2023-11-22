package com.opentext.bn.solutiondesigner.vo.itinerary.process;

public class ItineraryRequest {
	private String itineraryName = null;
	private String itineraryVersion = null;

	private InputPayload inputPayload = null;
	private ItineraryParameters itineraryParameters = null;

	public InputPayload getInputPayload() {
		return inputPayload;
	}

	public String getItineraryName() {
		return itineraryName;
	}

	public ItineraryParameters getItineraryParameters() {
		return itineraryParameters;
	}

	public String getItineraryVersion() {
		return itineraryVersion;
	}

	public void setInputPayload(InputPayload inputPayload) {
		this.inputPayload = inputPayload;
	}

	public void setItineraryName(String itineraryName) {
		this.itineraryName = itineraryName;
	}

	public void setItineraryParameters(ItineraryParameters itineraryParameters) {
		this.itineraryParameters = itineraryParameters;
	}

	public void setItineraryVersion(String itineraryVersion) {
		this.itineraryVersion = itineraryVersion;
	}

}
