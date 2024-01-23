package com.e3ps.approval.bean;

import com.e3ps.approval.WFHistory;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;

import wt.fc.PersistInfo;

public class WFHistoryData {
	
	private WFHistory history;
	
	private String oid;
	private String activityName;
	private String wfcomment;
	private String departmentName;
	private String objectVersion;
	private String ownerName;
	private String state;
	private String createDate;
	private String createDateFormat;
	
	
	public WFHistoryData(String oid) throws Exception {
		
		
		WFHistory ecn = (WFHistory) CommonUtil.getObject(oid);
		
		_WFHistoryData(ecn);
	}
	
	public WFHistoryData(WFHistory ecn) throws Exception {
		
		
		_WFHistoryData(ecn);
	}
	
	public void _WFHistoryData(WFHistory history) throws Exception{
		
		this.history = history;
		this.oid = CommonUtil.getOIDString(history);
		this.activityName = history.getActivityName();
		this.wfcomment = history.getWfcomment() == null ? "" : (String)history.getWfcomment();
		this.departmentName = history.getDepartmentName();
		this.objectVersion = history.getObjectVersion();
		this.ownerName = history.getOwner().getFullName();
		this.state ="완료됨";
		PersistInfo persistInfo = history.getPersistInfo();
		this.createDate = persistInfo.getCreateStamp().toString();
		this.createDateFormat = DateUtil.getDateString(persistInfo.getCreateStamp(),"a");
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getWfcomment() {
		return wfcomment;
	}

	public void setWfcomment(String wfcomment) {
		this.wfcomment = wfcomment;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getObjectVersion() {
		return objectVersion;
	}

	public void setObjectVersion(String objectVersion) {
		this.objectVersion = objectVersion;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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
	
	

}
