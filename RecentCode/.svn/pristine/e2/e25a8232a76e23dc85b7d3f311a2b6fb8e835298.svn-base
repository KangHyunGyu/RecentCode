package com.e3ps.project.beans;

import java.util.List;
import java.util.Map;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.project.ETask;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.service.TemplateHelper;

public class TemplateTaskData {
	
	private String oid;
	private String name;
	
	private String manDay;
	
	private String description;
	
	private boolean child;

	private String roleName;
	
	public TemplateTaskData(ETaskNode task) throws Exception {
		
		this.oid = CommonUtil.getOIDString(task);
		this.name = task.getName();
		
		this.manDay = ProjectUtil.numberFormat(task.getManDay());
		
		this.description = task.getDescription();
		
		List<ETask> children = TemplateHelper.manager.getTemplateTaskChildren(this.oid);
		
		this.child = children.size() > 0;
		
		List<Map<String, Object>> roleList = TemplateHelper.manager.getTaskRoleList(task);
		this.roleName = "";
		for(Map<String, Object> roleMap : roleList) {
			if(!"".equals(this.roleName)) {
				roleName += ",";
			}
			roleName += (String) roleMap.get("name");
		}
		
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isChild() {
		return child;
	}

	public void setChild(boolean child) {
		this.child = child;
	}

	public String getManDay() {
		return manDay;
	}

	public void setManDay(String manDay) {
		this.manDay = manDay;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
}
