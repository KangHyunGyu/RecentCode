package com.e3ps.common.history.bean;

import com.e3ps.change.EChangeOrder2;
import com.e3ps.common.bean.PersistableData;
import com.e3ps.common.history.ERPHistory;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.project.EProject;

import wt.fc.Persistable;
import wt.part.WTPart;

public class ERPHistoryData extends PersistableData{
	private String state;
	private String sendType;
	private String oid;
	private String sender;
	private String msg;
	private String sendTime;
	private String sendObjOid;
	private String sendObjNumber;
	private String sendObjVersion;
	
	public ERPHistoryData(String oid) throws Exception {
		super(oid);
		
		ERPHistory history = (ERPHistory) CommonUtil.getObject(oid);
		
		_ERPHistoryData(history);
	}
	
	public ERPHistoryData(ERPHistory history) throws Exception {
		super(history);
		
		_ERPHistoryData(history);
	}
	
	public void _ERPHistoryData(ERPHistory history) {
		
		
		if(history.getState().indexOf(",") > -1) {
			this.state = history.getState().substring(0, history.getState().indexOf(","));
			this.sendObjVersion = history.getState().substring(history.getState().indexOf(",")+1);
		}else{
			this.state = history.getState();
			this.sendObjVersion = "";
		}
		this.sendType = history.getSendType();
		this.oid = CommonUtil.getOIDString(history);
		this.sender = history.getSender();
		this.msg = history.getMsg();
		this.sendTime = DateUtil.getDateString(history.getPersistInfo().getCreateStamp(), "d");
		this.sendObjOid = history.getOid();
		
		Persistable per = CommonUtil.getObject(this.sendObjOid);
		if (per instanceof EChangeOrder2) {
			this.sendObjNumber = ((EChangeOrder2)per).getOrderNumber();
		} else if (per instanceof WTPart) {
			this.sendObjNumber = ((WTPart)per).getNumber();
		} else if (per instanceof EProject) {
			this.sendObjNumber = ((EProject)per).getCode();
		}
		
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getSendObjOid() {
		return sendObjOid;
	}

	public void setSendObjOid(String sendObjOid) {
		this.sendObjOid = sendObjOid;
	}

	public String getSendObjNumber() {
		return sendObjNumber;
	}

	public void setSendObjNumber(String sendObjNumber) {
		this.sendObjNumber = sendObjNumber;
	}

	public String getSendObjVersion() {
		return sendObjVersion;
	}

	public void setSendObjVersion(String sendObjVersion) {
		this.sendObjVersion = sendObjVersion;
	}
	
	
}
