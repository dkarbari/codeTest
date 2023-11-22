package com.opentext.bn.solutiondesigner.vo.itinerary.definition;

import java.util.List;

public class Itinerary {
	private String name = null;
	private String author = null;
	private String description = null;
	private String processType = null;
	private String engineTarget = null;
	private String restrictions = null;
	private String documentationLink = null;
	private List<Path> paths = null;

	public String getAuthor() {
		return author;
	}

	public String getDescription() {
		return description;
	}

	public String getDocumentationLink() {
		return documentationLink;
	}

	public String getEngineTarget() {
		return engineTarget;
	}

	public String getName() {
		return name;
	}

	public List<Path> getPaths() {
		return paths;
	}

	public String getProcessType() {
		return processType;
	}

	public String getRestrictions() {
		return restrictions;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDocumentationLink(String documentationLink) {
		this.documentationLink = documentationLink;
	}

	public void setEngineTarget(String engineTarget) {
		this.engineTarget = engineTarget;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPaths(List<Path> paths) {
		this.paths = paths;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public void setRestrictions(String restrictions) {
		this.restrictions = restrictions;
	}
}