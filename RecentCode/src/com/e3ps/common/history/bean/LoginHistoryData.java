package com.e3ps.common.history.bean;

import com.e3ps.common.bean.PersistableData;
import com.e3ps.common.history.LoginHistory;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;

public class LoginHistoryData extends PersistableData{

	private String userId;
	private String name;
	private String conTime;
	private String ip;
	private String browser;
	
	public LoginHistoryData(String oid) throws Exception {
		super(oid);
		
		LoginHistory history = (LoginHistory) CommonUtil.getObject(oid);
		
		_LoginHistoryData(history);
	}
	
	public LoginHistoryData(LoginHistory history) throws Exception {
		super(history);
		
		_LoginHistoryData(history);
	}
	
	public void _LoginHistoryData(LoginHistory history) {
		this.userId = history.getId();
		this.name = history.getName();
		this.conTime = DateUtil.getDateString(history.getPersistInfo().getCreateStamp(), "a");
		this.ip = history.getIp();
		this.browser = history.getBrowser();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String id) {
		this.userId = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConTime() {
		return conTime;
	}

	public void setConTime(String conTime) {
		this.conTime = conTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}
	
	
}
