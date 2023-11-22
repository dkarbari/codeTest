package com.opentext.bn.solutiondesigner.vo.itinerary.definition;

public class Task {
	private String taskName = null;
	private String taskType = null;
	private String description = null;
	private String serviceCode = null;
	private String taskSubtype = null;
	private String invocationMode = null;
	private String taskInstanceId = null;
	private String documentationLink = null;
	private String nextTaskInstanceId = null;
	private String taskImplementation = null;
	private TaskEmbeddedProperties taskEmbeddedProperties = null;

	public String getDescription() {
		return description;
	}

	public String getDocumentationLink() {
		return documentationLink;
	}

	public String getInvocationMode() {
		return invocationMode;
	}

	public String getNextTaskInstanceId() {
		return nextTaskInstanceId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public TaskEmbeddedProperties getTaskEmbeddedProperties() {
		return taskEmbeddedProperties;
	}

	public String getTaskImplementation() {
		return taskImplementation;
	}

	public String getTaskInstanceId() {
		return taskInstanceId;
	}

	public String getTaskName() {
		return taskName;
	}

	public String getTaskSubtype() {
		return taskSubtype;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDocumentationLink(String documentationLink) {
		this.documentationLink = documentationLink;
	}

	public void setInvocationMode(String invocationMode) {
		this.invocationMode = invocationMode;
	}

	public void setNextTaskInstanceId(String nextTaskInstanceId) {
		this.nextTaskInstanceId = nextTaskInstanceId;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public void setTaskEmbeddedProperties(TaskEmbeddedProperties taskEmbeddedProperties) {
		this.taskEmbeddedProperties = taskEmbeddedProperties;
	}

	public void setTaskImplementation(String taskImplementation) {
		this.taskImplementation = taskImplementation;
	}

	public void setTaskInstanceId(String taskInstanceId) {
		this.taskInstanceId = taskInstanceId;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public void setTaskSubtype(String taskSubtype) {
		this.taskSubtype = taskSubtype;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

}