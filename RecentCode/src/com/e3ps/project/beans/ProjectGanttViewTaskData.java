package com.e3ps.project.beans;

import java.util.List;

import com.e3ps.common.util.DateUtil;
import com.e3ps.project.ScheduleNode;

public class ProjectGanttViewTaskData extends ProjectGanttTaskData {
	
	private double progress = 0;
	private String real_start;
	private String real_end;
	private List<ProjectRoleData> taskRole;
	
	public ProjectGanttViewTaskData(ScheduleNode node) throws Exception {
		super(node);
		
		this.progress = node.getCompletion() / 100;
		
		this.real_start = DateUtil.getDateString(node.getStartDate(), "d");
		this.real_end = DateUtil.getDateString(node.getEndDate(), "d");
		
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}

	public String getReal_start() {
		return real_start;
	}

	public void setReal_start(String real_start) {
		this.real_start = real_start;
	}

	public String getReal_end() {
		return real_end;
	}

	public void setReal_end(String real_end) {
		this.real_end = real_end;
	}

	public List<ProjectRoleData> getTaskRole() {
		return taskRole;
	}

	public void setTaskRole(List<ProjectRoleData> taskRole) {
		this.taskRole = taskRole;
	}

}
