package com.opentext.bn.solutiondesigner.vo.itinerary.definition;

import java.util.List;

public class Path {
	private boolean isRootPath = true;
	private String pathId = null;
	private String pathName = null;
	private String parentPathId = null;
	private List<Task> tasks = null;

	public String getParentPathId() {
		return parentPathId;
	}

	public String getPathId() {
		return pathId;
	}

	public String getPathName() {
		return pathName;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public boolean isIsRootPath() {
		return isRootPath;
	}

	public void setParentPathId(String parentPathId) {
		this.parentPathId = parentPathId;
	}

	public void setPathId(String pathId) {
		this.pathId = pathId;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	public void setIsRootPath(boolean isRootPath) {
		this.isRootPath = isRootPath;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

}
