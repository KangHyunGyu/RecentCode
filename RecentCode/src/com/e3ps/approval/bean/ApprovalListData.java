package com.e3ps.approval.bean;

public class ApprovalListData {
	
	
	private String objectType;
	private String objectTypeName;
	private String state;
	private String stateName;
	private String objectName;
	private String title;
	
	//라인 데이터
	private String roleType;
	private String roleName;
	private String owner;
	private String ownerFullName;
	private String startDate;
	private String startDateFormat;
	
	//마스터 데이터
	private String createDate;
	private String createDateFormat;
	private String creator;
	private String createFullName;
	private String completeDate;
	private String completeDateFormat;
	
	private String oid;			 // 결재라인 Oid
	private String objectOid; 	//결재 대상 Oid
	
	private String processStepData;
	
	private String changeOid;
	
	public String getChangeOid() {
		return changeOid;
	}
	public void setChangeOid(String changeOid) {
		this.changeOid = changeOid;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	
	
	public String getObjectTypeName() {
		return objectTypeName;
	}
	public void setObjectTypeName(String objectTypeName) {
		this.objectTypeName = objectTypeName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getOwnerFullName() {
		return ownerFullName;
	}
	public void setOwnerFullName(String ownerFullName) {
		this.ownerFullName = ownerFullName;
	}
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getStartDateFormat() {
		return startDateFormat;
	}
	public void setStartDateFormat(String startDateFormat) {
		this.startDateFormat = startDateFormat;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getCreateDateFormat() {
		return createDateFormat;
	}
	public void setCreateDateFormat(String createDateFormat) {
		this.createDateFormat = createDateFormat;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreateFullName() {
		return createFullName;
	}
	public void setCreateFullName(String createFullName) {
		this.createFullName = createFullName;
	}
	public String getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}
	public String getCompleteDateFormat() {
		return completeDateFormat;
	}
	public void setCompleteDateFormat(String completeDateFormat) {
		this.completeDateFormat = completeDateFormat;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getObjectOid() {
		return objectOid;
	}
	public void setObjectOid(String objectOid) {
		this.objectOid = objectOid;
	}
	public String getProcessStepData() {
		return processStepData;
	}
	public void setProcessStepData(String processStepData) {
		this.processStepData = processStepData;
	}
	@Override
	public String toString() {
		return "ApprovalListData [objectType=" + objectType + ", objectTypeName=" + objectTypeName + ", state=" + state
				+ ", stateName=" + stateName + ", objectName=" + objectName + ", title=" + title + ", roleType="
				+ roleType + ", roleName=" + roleName + ", owner=" + owner + ", ownerFullName=" + ownerFullName
				+ ", startDate=" + startDate + ", startDateFormat=" + startDateFormat + ", createDate=" + createDate
				+ ", createDateFormat=" + createDateFormat + ", creator=" + creator + ", createFullName="
				+ createFullName + ", completeDate=" + completeDate + ", completeDateFormat=" + completeDateFormat
				+ ", oid=" + oid + ", objectOid=" + objectOid + ", processStepData=" + processStepData + ", changeOid="
				+ changeOid + "]";
	}
	
	
}
