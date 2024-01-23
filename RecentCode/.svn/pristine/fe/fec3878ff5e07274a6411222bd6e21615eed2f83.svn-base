package com.e3ps.common.bean;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import wt.fc.Persistable;

public class PersistableData {
	
	private String oid; // oid
	private String createDate; // createDate
	private String createDateFormat; // createDate(YYYY-MM-DD)
	
	private String modifyDate; // modifyDate
	private String modifyDateFormat; // modifyDate(YYYY-MM-DD)
	
	public PersistableData(String oid) throws Exception {
		
		Persistable ps = CommonUtil.getObject(oid);
		
		_PersistableData(ps);
	}
	
	public PersistableData(Persistable ps) throws Exception {
		
		_PersistableData(ps);
	}
	
	public void _PersistableData(Persistable ps) throws Exception {
		this.oid = CommonUtil.getOIDString(ps);
		
		this.createDate = DateUtil.getDateString(ps.getPersistInfo().getCreateStamp(), "all");
		this.createDateFormat = DateUtil.getDateString(ps.getPersistInfo().getCreateStamp(), "d");
		
		this.modifyDate = DateUtil.getDateString(ps.getPersistInfo().getModifyStamp(), "all");
		this.modifyDateFormat = DateUtil.getDateString(ps.getPersistInfo().getModifyStamp(), "d");
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
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

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getModifyDateFormat() {
		return modifyDateFormat;
	}

	public void setModifyDateFormat(String modifyDateFormat) {
		this.modifyDateFormat = modifyDateFormat;
	}
	
	public String getId() {
		return oid;
	}

}
