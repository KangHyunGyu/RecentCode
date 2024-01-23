package com.e3ps.common.bean;



import com.e3ps.common.impl.OwnPersistable;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;


import wt.org.WTUser;

public class OwnPersitableData extends PersistableData{
	
	private String owner;
	private String ownerFullName;
	
	private String ownerOid;	//creator Oid
	
	public OwnPersitableData(String oid) throws Exception {
		super(oid);
		OwnPersistable ps = (OwnPersistable)CommonUtil.getObject(oid);
		_OwnPersitableData(ps);
	}
	
	public OwnPersitableData(OwnPersistable ps) throws Exception {
		super(ps);
		_OwnPersitableData(ps);
	}
	
	public void _OwnPersitableData(OwnPersistable ps) throws Exception {
		
		WTUser user = (WTUser)ps.getOwner().getPrincipal();
		this.owner = StringUtil.checkNull(user.getName());
		this.ownerFullName = StringUtil.checkNull(user.getFullName());
		this.ownerOid = CommonUtil.getOIDString(user);
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

	public String getOwnerOid() {
		return ownerOid;
	}

	public void setOwnerOid(String ownerOid) {
		this.ownerOid = ownerOid;
	}
	
	

}
