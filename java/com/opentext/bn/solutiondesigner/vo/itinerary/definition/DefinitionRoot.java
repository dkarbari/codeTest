package com.opentext.bn.solutiondesigner.vo.itinerary.definition;

public class DefinitionRoot {
	private Header header = null;
	private Itinerary itinerary = null;
	private VersionControl versionControl = null;

	public Header getHeader() {
		return header;
	}

	public Itinerary getItinerary() {
		return itinerary;
	}

	public VersionControl getVersionControl() {
		return versionControl;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public void setItinerary(Itinerary itinerary) {
		this.itinerary = itinerary;
	}

	public void setVersionControl(VersionControl versionControl) {
		this.versionControl = versionControl;
	}
}