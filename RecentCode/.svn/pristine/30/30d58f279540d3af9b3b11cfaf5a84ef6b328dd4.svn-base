package com.e3ps.admin.bean;

import com.e3ps.admin.MasterACLWTUserLink;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.org.People;
import com.e3ps.org.bean.PeopleData;
import com.e3ps.org.service.PeopleHelper;

import wt.org.WTUser;

public class MasterAclWTUserData {
	private String oid = "";
	private String userId = "";
	private String userName = "";
	private String duty = "";
    private String deptName = "";
    private String docOid = "";
    
    // 검색조건
    private int start = 0;
    private int datafetchcnt = 0;
    private long sessionId = 0L;
    
    public MasterAclWTUserData(){
    	super();
    }
    
    public MasterAclWTUserData(MasterACLWTUserLink link) throws Exception{
    	setOid(CommonUtil.getOIDString(link));
    	WTUser user = (WTUser) link.getRoleAObject();
    	People people = PeopleHelper.manager.getPeople(user);
    	PeopleData pData = new PeopleData(people);
    	setDeptName(pData.getDepartmentName());
    	setUserName(pData.getName());
    	setUserId(pData.getId());
    	setDuty(pData.getDuty());
    }
    
    public MasterAclWTUserData(MasterACLWTUserLink link, People people) throws Exception{
    	setOid(CommonUtil.getOIDString(link));
    	PeopleData pData = new PeopleData(people);
    	setDeptName(pData.getDepartmentName());
    	setUserName(pData.getName());
    	setUserId(pData.getId());
    	setDuty(pData.getDuty());
    }

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDocOid() {
		return docOid;
	}

	public void setDocOid(String docOid) {
		this.docOid = docOid;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getDatafetchcnt() {
		return datafetchcnt;
	}

	public void setDatafetchcnt(int datafetchcnt) {
		this.datafetchcnt = datafetchcnt;
	}

	public long getSessionId() {
		return sessionId;
	}

	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}
}
