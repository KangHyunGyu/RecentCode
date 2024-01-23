package com.e3ps.project.beans;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.project.EProject;
import com.e3ps.project.ETask;
import com.e3ps.project.ScheduleNode;

public class ProjectGanttTaskData {
	
	private String id;
	private String text;
	private String start_date;
	private String endDate;
	private double duration;
	private String parent;
	private String type;	
	private String state;
	private String description;
	
	//For Set type
	public static final String PROJECT = "project";
	public static final String TASK = "task";
	
	public ProjectGanttTaskData(ScheduleNode node) throws Exception {
		this.id = CommonUtil.getOIDString(node);
		this.text = node.getName();
		this.start_date = DateUtil.getDateString(node.getPlanStartDate(), "d");
		this.endDate = DateUtil.getDateString(node.getPlanEndDate(), "d");
		this.duration = ProjectUtil.getDurationHoliday(node.getPlanStartDate(), node.getPlanEndDate());
		
		if(node instanceof EProject) {
			EProject project = (EProject) node;
			this.state = project.getLifeCycleState().toString();
		} else {
			ETask task = (ETask) node;
			this.state = task.getStatus();
			this.description = task.getDescription();
		}
		
		this.parent = CommonUtil.getOIDString(node.getParent());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
