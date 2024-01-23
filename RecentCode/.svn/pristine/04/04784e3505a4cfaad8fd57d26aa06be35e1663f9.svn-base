package com.e3ps.project.beans;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.org.bean.PeopleData;
import com.e3ps.project.ProjectRole;
import com.e3ps.project.RoleUserLink;

import wt.org.WTUser;

public class ProjectRoleData {
	
	private String roleCode; 
	private String roleName; 
	private String userName; 
	private String duty; 
	private String email; 
	private String peopleOid;
	private String userOid;
	
	public ProjectRoleData(Object[] obj) throws Exception {
		
		RoleUserLink link = (RoleUserLink) obj[0];
		ProjectRole role = (ProjectRole) obj[1];
		
		if(link != null) {
			WTUser user = link.getUser();
			
			PeopleData people = new PeopleData(user);
			
			this.userName = user.getFullName();
			this.duty = people.getDuty();
			this.email = people.getEmail();
			this.peopleOid = people.getOid();
			this.userOid = CommonUtil.getOIDString(user);
		}
		
		this.roleCode = role.getCode();
		this.roleName = role.getName();
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPeopleOid() {
		return peopleOid;
	}

	public void setPeopleOid(String peopleOid) {
		this.peopleOid = peopleOid;
	}

	public String getUserOid() {
		return userOid;
	}

	public void setUserOid(String userOid) {
		this.userOid = userOid;
	}
}
