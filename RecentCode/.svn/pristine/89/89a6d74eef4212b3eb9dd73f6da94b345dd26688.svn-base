package com.e3ps.project.beans;

import com.e3ps.project.ScheduleNode;

public class ProjectTreeData extends NodeTreeData {
	
	private String iconUrl;
	
	public ProjectTreeData(ScheduleNode node) throws Exception {
		super(node);
		
		this.iconUrl = ProjectUtil.getIconUrl(node);
		int manDay = ProjectUtil.getDurationHoliday(node.getPlanStartDate(), node.getPlanEndDate());
		this.setManDay(manDay);
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
}
