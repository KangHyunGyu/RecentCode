/**
 * @(#)	PeopleData.java
 * Copyright (c) e3ps. All rights reserverd
 * 
 * @version 1.00
 * @since jdk 1.8
 * @author shkim
 */

package com.e3ps.org.bean;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.org.Department;
import com.e3ps.org.DepartmentPeopleLink;
import com.e3ps.org.People;
import com.e3ps.org.WTUserPeopleLink;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.org.WTPrincipalReference;
import wt.org.WTUser;
import wt.session.SessionHelper;

public class PeopleData {
	
	/* People */
	private String oid;
	private String pOid;  //결재 라인 people oid 용
	private String name;
	private String nameEn;
	private String duty;
	private String dutyCode;
	private String dutyDisplay;
	private String officeTel;
	private String homeTel;
	private String cellTel;
	private String address;
	private int priority;
	private String password;
	private boolean isDisable;
	private String disableStr;
	private String disableKor;
	private int sortNum;
	private String pwChangeDate;
	private String title;
	private boolean chief;
	private String ccCode;
	private String enterDate;
	private String retrireDate;
	private String gradeCode;
	private String gradeName;
	private String erpid;
	
	/* WTUser */
	private String wtuserOID;
	private String id;
	private String email;
	private boolean wtuserDeleted;
	
	/* Department */
	private String departmentOID;
	private String departmentName;
	private String departmentCode;
	
	/*결재 라인 Role */
	private String roleType;
	private String roleName;
	
	private boolean isDisDepart;
	
	private boolean isChangePwd;
	
	private boolean isAuthSelected;
	
	
	
	public PeopleData() throws Exception {
		this(SessionHelper.manager.getPrincipal());
	}
	
	public PeopleData(String oid) throws Exception {
		this(CommonUtil.getObject(oid));
	}

	public PeopleData(Object obj) throws Exception {

		if (obj instanceof WTPrincipalReference) {
			obj = (WTUser) ((WTPrincipalReference) obj).getObject();
		}

		WTUser user = null;
		People people = null;
		if (obj instanceof WTUser) {
			user = (WTUser) obj;
			
			QueryResult qr = PersistenceHelper.manager.navigate(user, "people", WTUserPeopleLink.class);
			if(qr.hasMoreElements()) {
				
				people = (People) qr.nextElement();
				
			}else {
				people = People.newPeople();
				people.setUser(user);
				people.setName(user.getFullName());
				people.setNameEn(user.getFullName());
				people.setAddress(user.getEMail());
				people = (People) PersistenceHelper.manager.save(people);
			}
			
		} else if (obj instanceof People) {
			people = (People) obj;
			user = people.getUser();
			
		}
		
		this.wtuserOID = CommonUtil.getOIDString(user);
		this.oid = CommonUtil.getOIDString(people);
		this.pOid = CommonUtil.getOIDString(people);
		setWTUserData(user);
		setPeopleData(people);
		setDepartmentData(people);
	}

	private void setWTUserData(WTUser user) throws Exception {
		if (user == null) return;

		this.id = user.getName();
		this.name = user.getFullName();
		this.nameEn = user.getFullName();
		this.email = StringUtil.checkNull(user.getEMail());
	}
	

	private void setPeopleData(People people) throws Exception {
		if (people == null) return;

		this.name = people.getName();
		if (people.isIsDisable()) {
			this.name = this.name + "(미사용)";
		}
		this.nameEn = people.getNameEn();
		this.duty = people.getDuty() == null ? "지정안됨" : people.getDuty();
		this.dutyCode = StringUtil.checkNull(people.getDutyCode());
		this.officeTel = StringUtil.checkNull(people.getOfficeTel());
		this.homeTel = StringUtil.checkNull(people.getHomeTel());
		this.cellTel = StringUtil.checkNull(people.getCellTel());
		this.address = StringUtil.checkNull(people.getAddress());
		this.priority = people.getPriority();
		this.password = StringUtil.checkNull(people.getPassword());
		this.isDisable = people.isIsDisable();
		this.disableStr = people.isIsDisable() ? "N" : "Y";
		//this.disableKor = people.isIsDisable() ? MessageUtil.getMessage("미사용") : MessageUtil.getMessage("사용");
		this.sortNum = people.getSortNum();
		this.pwChangeDate = StringUtil.checkNull(people.getPwChangeDate());
		this.title = StringUtil.checkNull(people.getTitle());
		this.dutyDisplay = this.duty;
//		this.dutyDisplay += this.chief ? "(부서장)" : "";
		
//		this.ccCode = people.getCcCode();
//		this.enterDate = people.getEnterDate();
//		this.retrireDate = people.getRetrireDate();
//		this.gradeCode = people.getGradeCode();
//		this.gradeName = people.getGradeName();
		this.isDisDepart = false;
		/*
		Department department = people.getDepartment();
		if(department != null){
			
			//this.departmentOID = CommonUtil.getOIDString(department);
			this.departmentName = department.getName();
			this.departmentCode = department.getCode();
			this.isDisDepart = true;
		}
		*/
		if(people.isIsDisable()) {
			this.wtuserDeleted = true;
		}else {
			this.wtuserDeleted = false;
		}
		setDepartmentData(people);
		
	}

	public void setDepartmentData(People people) throws Exception {
		
		if (people != null) {
			QueryResult qr = PersistenceHelper.manager.navigate(people, "department", DepartmentPeopleLink.class);
			if (qr.hasMoreElements()) {
				Department department = (Department) qr.nextElement();
				this.departmentOID = CommonUtil.getOIDString(department);
				this.departmentName = department.getName();
				this.departmentCode = department.getCode();
			} else {
				this.departmentName = "지정안됨";
			}
		} else {
			this.departmentName = "지정안됨";
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

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getDutyCode() {
		return dutyCode;
	}

	public void setDutyCode(String dutyCode) {
		this.dutyCode = dutyCode;
	}

	public String getOfficeTel() {
		return officeTel;
	}

	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel;
	}

	public String getHomeTel() {
		return homeTel;
	}

	public void setHomeTel(String homeTel) {
		this.homeTel = homeTel;
	}

	public String getCellTel() {
		return cellTel;
	}

	public void setCellTel(String cellTel) {
		this.cellTel = cellTel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isDisable() {
		return isDisable;
	}

	public void setDisable(boolean isDisable) {
		this.isDisable = isDisable;
	}

	public int getSortNum() {
		return sortNum;
	}

	public void setSortNum(int sortNum) {
		this.sortNum = sortNum;
	}

	public String getPwChangeDate() {
		return pwChangeDate;
	}

	public void setPwChangeDate(String pwChangeDate) {
		this.pwChangeDate = pwChangeDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isChief() {
		return chief;
	}

	public String getWtuserOID() {
		return wtuserOID;
	}

	public void setWtuserOID(String wtuserOID) {
		this.wtuserOID = wtuserOID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDepartmentOID() {
		return departmentOID;
	}

	public void setDepartmentOID(String departmentOID) {
		this.departmentOID = departmentOID;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public void setChief(boolean chief) {
		this.chief = chief;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getpOid() {
		return pOid;
	}

	public void setpOid(String pOid) {
		this.pOid = pOid;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getCcCode() {
		return ccCode;
	}

	public void setCcCode(String ccCode) {
		this.ccCode = ccCode;
	}

	public String getEnterDate() {
		return enterDate;
	}

	public void setEnterDate(String enterDate) {
		this.enterDate = enterDate;
	}

	public String getRetrireDate() {
		return retrireDate;
	}

	public void setRetrireDate(String retrireDate) {
		this.retrireDate = retrireDate;
	}

	public String getGradeCode() {
		return gradeCode;
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getDisableStr() {
		return disableStr;
	}

	public void setDisableStr(String disableStr) {
		this.disableStr = disableStr;
	}

	public String getDisableKor() {
		return disableKor;
	}

	public void setDisableKor(String disableKor) {
		this.disableKor = disableKor;
	}

	public String getDutyDisplay() {
		return dutyDisplay;
	}

	public void setDutyDisplay(String dutyDisplay) {
		this.dutyDisplay = dutyDisplay;
	}

	public boolean isDisDepart() {
		return isDisDepart;
	}

	public void setDisDepart(boolean isDisDepart) {
		this.isDisDepart = isDisDepart;
	}

	public boolean isWtuserDeleted() {
		return wtuserDeleted;
	}

	public void setWtuserDeleted(boolean wtuserDeleted) {
		this.wtuserDeleted = wtuserDeleted;
	}

	public boolean isChangePwd() {
		return isChangePwd;
	}

	public void setChangePwd(boolean isChangePwd) {
		this.isChangePwd = isChangePwd;
	}

	public String getErpid() {
		return erpid;
	}

	public void setErpid(String erpid) {
		this.erpid = erpid;
	}
	
	public boolean isAuthSelected() {
		return isAuthSelected;
	}

	public void setAuthSelected(boolean isAuthSelected) {
		this.isAuthSelected = isAuthSelected;
	}
	
}