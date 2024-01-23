package com.e3ps.project.beans;

import java.util.ArrayList;
import java.util.List;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.ScheduleNode;
import com.e3ps.project.service.TemplateHelper;

public class TemplateTreeNode {
	
	private ScheduleNode node;
	
	private String oid;
	private double orgNodeManDay;
	private int level;
	
	private boolean children = false;
	
	private TemplateTreeNode parent;
	
	private List<String> preTaskList = new ArrayList<String>();
	
	public TemplateTreeNode(int level, ScheduleNode node, TemplateTreeNode parent) {
		
		this.node = node;
		
		this.level = level;
		this.oid = CommonUtil.getOIDString(node);
		this.orgNodeManDay = node.getManDay();
		
		this.parent = parent;
	}

	public void setPreTaskId()throws Exception{
		
		if(this.node instanceof ETaskNode) {
			List<TemplateTaskData> taskDataList = TemplateHelper.manager.getPreTaskList((ETaskNode) this.node);
			
			for(TemplateTaskData data : taskDataList) {
				this.preTaskList.add(data.getOid());
			}
		}
	}
	
	public boolean checkScheduleEdit(){
		
		if(this.orgNodeManDay != (((ScheduleNode) this.node).getManDay())){
			return true;
		}
		
		return false;
	}
	
	public double getOrgNodeManDay() {
		return orgNodeManDay;
	}

	public void setOrgNodeManDay(double orgNodeManDay) {
		this.orgNodeManDay = orgNodeManDay;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public List<String> getPreTaskList() {
		return preTaskList;
	}

	public void setPreTaskList(List<String> preTaskList) {
		this.preTaskList = preTaskList;
	}

	public boolean isChildren() {
		return children;
	}

	public void setChildren(boolean children) {
		this.children = children;
	}

	public TemplateTreeNode getParent() {
		return parent;
	}

	public void setParent(TemplateTreeNode parent) {
		this.parent = parent;
	}
	
}
