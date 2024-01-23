package com.e3ps.stagegate.bean;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.stagegate.SGObjectMaster;

public class SGObjectMasterData {
	private String oid;
	private String code;
	private int version;
	private String remark;
	private boolean lastVersion;
	private String createDate;
	private String creator;
	private String sgOid;

	public SGObjectMasterData(SGObjectMaster objMaster) {
		this.oid = CommonUtil.getOIDString(objMaster);
		this.code = StringUtil.checkNull(objMaster.getCode());
		this.version = objMaster.getVersion();
		this.remark = StringUtil.checkNull(objMaster.getRemark());
		this.lastVersion = objMaster.isLastVersion();
		this.createDate = DateUtil.getDateString(objMaster.getPersistInfo().getCreateStamp(),"d");
		this.creator = StringUtil.checkNull(objMaster.getOwner().getFullName());
	}

	
	public String getSgOid() {
		return sgOid;
	}


	public void setSgOid(String sgOid) {
		this.sgOid = sgOid;
	}


	public String getCreateDate() {
		return createDate;
	}


	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}


	public String getCreator() {
		return creator;
	}


	public void setCreator(String creator) {
		this.creator = creator;
	}


	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isLastVersion() {
		return lastVersion;
	}

	public void setLastVersion(boolean lastVersion) {
		this.lastVersion = lastVersion;
	}
	
	
}
