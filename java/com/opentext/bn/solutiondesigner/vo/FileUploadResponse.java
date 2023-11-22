package com.opentext.bn.solutiondesigner.vo;

import java.util.Date;

public class FileUploadResponse {
	private String owner = null;
	private String message = null;
	private String fileName = null;
	private Date uploadedTime = null;

	public String getFileName() {
		return fileName;
	}

	public String getMessage() {
		return message;
	}

	public String getOwner() {
		return owner;
	}

	public Date getUploadedTime() {
		return uploadedTime;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setUploadedTime(Date uploadedTime) {
		this.uploadedTime = uploadedTime;
	}
}
