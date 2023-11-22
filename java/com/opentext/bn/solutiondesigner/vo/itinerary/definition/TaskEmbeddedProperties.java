package com.opentext.bn.solutiondesigner.vo.itinerary.definition;

import java.util.List;

import javax.validation.Valid;

public class TaskEmbeddedProperties {

	@Valid
	private List<ChildServiceCode> childServiceCodes = null;

	public List<ChildServiceCode> getChildServiceCodes() {
		return childServiceCodes;
	}

	public void setChildServiceCodes(List<ChildServiceCode> childServiceCodes) {
		this.childServiceCodes = childServiceCodes;
	}

}
