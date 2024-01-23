package com.e3ps.calendar.bean;

import com.e3ps.calendar.DevelopmentStageCalendar;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;

public class DSCData {
	private String oid;
	private String name;
	private String remark;
	private String startDate;
	private String endDate;
	private String creatorFullName;	
	private String createDate;
	private String modifyDate;
	
	public DSCData(DevelopmentStageCalendar dsc) {
		this.oid = CommonUtil.getOIDString(dsc);
		this.name = StringUtil.checkNull(dsc.getName());
		this.remark = StringUtil.checkNull(dsc.getRemark());
		this.startDate = DateUtil.getDateString(dsc.getStartDate(),"d");
		this.endDate = DateUtil.getDateString(dsc.getEndDate(),"d");
		this.creatorFullName = StringUtil.checkNull(dsc.getOwner().getFullName());
		this.createDate = DateUtil.getDateString(dsc.getPersistInfo().getCreateStamp(),"d");
		this.modifyDate = DateUtil.getDateString(dsc.getPersistInfo().getModifyStamp(),"d");
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getCreatorFullName() {
		return creatorFullName;
	}

	public void setCreatorFullName(String creatorFullName) {
		this.creatorFullName = creatorFullName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	
}
