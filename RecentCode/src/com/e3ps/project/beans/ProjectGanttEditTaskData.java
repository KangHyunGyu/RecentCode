package com.e3ps.project.beans;

import java.util.List;

import com.e3ps.project.ETaskNode;
import com.e3ps.project.ScheduleNode;
import com.e3ps.project.service.ProjectHelper;

public class ProjectGanttEditTaskData extends ProjectGanttTaskData {
	
	private boolean isOutput = false;	
	private List<ProjectRoleData> taskRole;
	
	public ProjectGanttEditTaskData(ScheduleNode node) throws Exception {
		super(node);
		
		if(node instanceof ETaskNode) {
			List<ProjectOutputData> outputList = ProjectHelper.manager.getTaskOutputList((ETaskNode) node);
			this.isOutput = outputList.size() > 0;
		}
		
	}

	public boolean isOutput() {
		return isOutput;
	}

	public void setOutput(boolean isOutput) {
		this.isOutput = isOutput;
	}

	public List<ProjectRoleData> getTaskRole() {
		return taskRole;
	}

	public void setTaskRole(List<ProjectRoleData> taskRole) {
		this.taskRole = taskRole;
	}

}
