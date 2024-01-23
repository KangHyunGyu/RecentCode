package com.e3ps.approval.bean;

import java.util.List;

import com.e3ps.approval.ApprovalLine;
import com.e3ps.approval.ApprovalMaster;
import com.e3ps.approval.service.ApprovalHelper;
import com.e3ps.approval.util.ApprovalUtil;
import com.e3ps.common.bean.OwnPersitableData;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.org.bean.PeopleData;

import wt.org.WTUser;

public class ApprovalLineData extends OwnPersitableData {

	private ApprovalLine lineObj;
	
	private String roleType;
	private String roleName;
	private String approvalGubun;

	private boolean lastSeqToSameRole;
	private String state;
	private String stateName;
	private boolean last;
	private boolean readcheck;
	private int ver;
	private int seq;
	private int appNum;
	private String approveDate;
	private String approveDateFormat;
	private String startDate;
	private String startDateFormat;
	private String description;

	private String name;// 결재 라인 유저 이름
	private String userId;// 결재 라인 유저 userId;
	private String pOid; // people oid;
	private String departmentOID;
	private String departmentName;
	private String departmentCode;

	private String duty;
	private String dutyCode;

	public ApprovalLineData(String oid) throws Exception {

		super(oid);
		ApprovalLine line = (ApprovalLine) CommonUtil.getObject(oid);
		_ApprovalLineData(line);

	}

	public ApprovalLineData(ApprovalLine line) throws Exception {

		super(line);
		_ApprovalLineData(line);
	}

	public void _ApprovalLineData(ApprovalLine line) throws Exception {

		this.lineObj = line;
		this.roleType = line.getRole().toString();
		this.roleName = line.getRole().getDisplay(MessageUtil.getLocale());
		this.state = line.getState().toString();
		this.stateName = line.getState().getDisplay(MessageUtil.getLocale());
		this.last = line.isLast();
		this.readcheck = line.isReadCheck();
		this.ver = line.getVer();
		this.seq = line.getSeq();
		this.approvalGubun = this.roleName;

		if (this.roleType.equals("APPROVE")) {
			this.appNum = line.getSeq();
			this.approvalGubun = "검토" + appNum;
		}

		if (line.getApproveDate() != null) {
			this.approveDate = DateUtil.getDateString(line.getApproveDate(), "all");
			this.approveDateFormat = DateUtil.getDateString(line.getApproveDate(), "d");
		}

		if (line.getStartDate() != null) {
			this.startDate = DateUtil.getDateString(line.getStartDate(), "all");
			this.startDateFormat = DateUtil.getDateString(line.getStartDate(), "d");
		}

		this.description = StringUtil.checkNull(line.getDescription());
	}

	public String peopleCall() throws Exception {

		WTUser user = (WTUser) CommonUtil.getObject(this.getOwnerOid());
		PeopleData pData = new PeopleData(user);
		this.departmentName = pData.getDepartmentName();
		this.userId = user.getName();
		this.departmentName = pData.getDepartmentName();
		this.duty = pData.getDuty();
		this.name = this.getOwnerFullName();
		this.pOid = pData.getOid();
		return this.userId;
	}

	public String departmentName() throws Exception {
		WTUser user = (WTUser) CommonUtil.getObject(this.getOwnerOid());

		PeopleData pData = new PeopleData(user);
		this.departmentName = pData.getDepartmentName();
		this.userId = user.getName();
		return this.departmentName;

	}
	
	public void checkLastSeqToSameRole() throws Exception{
		
		if(this.lineObj != null) {
			
			ApprovalMaster master = this.lineObj.getMaster();
			List<ApprovalLineData> allLineList = ApprovalHelper.manager.getApprovalLastLine(master, true);
			for(ApprovalLineData data : allLineList) {
				if(data.getOid().equals(CommonUtil.getOIDString(this.lineObj))) {
					this.lastSeqToSameRole = data.isLastSeqToSameRole();
					this.approvalGubun = data.getApprovalGubun();
				}
			}
		}
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

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public boolean isReadcheck() {
		return readcheck;
	}

	public void setReadcheck(boolean readcheck) {
		this.readcheck = readcheck;
	}

	public int getVer() {
		return ver;
	}

	public void setVer(int ver) {
		this.ver = ver;
	}

	public String getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(String approveDate) {
		this.approveDate = approveDate;
	}

	public String getApproveDateFormat() {
		return approveDateFormat;
	}

	public void setApproveDateFormat(String approveDateFormat) {
		this.approveDateFormat = approveDateFormat;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDepartmentOID() {
		return departmentOID;
	}

	public void setDepartmentOID(String departmentOID) {
		this.departmentOID = departmentOID;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getpOid() {
		return pOid;
	}

	public void setpOid(String pOid) {
		this.pOid = pOid;
	}

	public String getId() {
		return pOid;
	}

	public int getAppNum() {
		return appNum;
	}

	public void setAppNum(int appNum) {
		this.appNum = appNum;
	}

	public String getApprovalGubun() {
		return approvalGubun;
	}

	public void setApprovalGubun(String approvalGubun) {
		this.approvalGubun = approvalGubun;
	}

	public boolean isLastSeqToSameRole() {
		return lastSeqToSameRole;
	}

	public void setLastSeqToSameRole(boolean lastSeqToSameRole) {
		this.lastSeqToSameRole = lastSeqToSameRole;
		if(this.lastSeqToSameRole) {
			if(ApprovalUtil.ROLE_APPROVE.equals(this.roleType)) {
				this.approvalGubun = "최종 결재";
			}
		}
	}
	
}
