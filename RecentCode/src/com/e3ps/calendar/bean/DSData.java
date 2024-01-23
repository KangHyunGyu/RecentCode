package com.e3ps.calendar.bean;

import com.e3ps.calendar.DevelopmentStage;
import com.e3ps.calendar.DevelopmentStageCalendar;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.project.beans.ProjectUtil;

public class DSData {
	private String oid;
	private String name;
	private String remark;
	private String startDate;
	private String endDate;
	private String isDSC;
	private long code;
	private long parent;
	private double duration;
	
	public DSData(DevelopmentStage ds, long parent) {
		this.oid = CommonUtil.getOIDString(ds);
		this.name = StringUtil.checkNull(ds.getName());
		this.remark = StringUtil.checkNull(ds.getRemark());
		this.startDate = DateUtil.getDateString(ds.getStartDate(),"d");
		this.endDate = DateUtil.getDateString(ds.getEndDate(),"d");
		this.isDSC = "false";
		this.code =  CommonUtil.getOIDLongValue(ds);
		this.parent = parent;
		this.duration = ProjectUtil.getDurationHoliday(ds.getStartDate(), ds.getEndDate());
		
	}
	
	public DSData(DevelopmentStageCalendar dsc) {
		this.oid = CommonUtil.getOIDString(dsc);
		this.name = StringUtil.checkNull(dsc.getName());
		this.remark = StringUtil.checkNull(dsc.getRemark());
		this.startDate = DateUtil.getDateString(dsc.getStartDate(),"d");
		this.endDate = DateUtil.getDateString(dsc.getEndDate(),"d");
		this.isDSC = "true";
		this.code =  CommonUtil.getOIDLongValue(dsc);
		this.parent = 0;
		this.duration = ProjectUtil.getDurationHoliday(dsc.getStartDate(), dsc.getEndDate());
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

	public String isDSC() {
		return isDSC;
	}

	public void setDSC(String isDSC) {
		this.isDSC = isDSC;
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public long getParent() {
		return parent;
	}

	public void setParent(long parent) {
		this.parent = parent;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	
}
