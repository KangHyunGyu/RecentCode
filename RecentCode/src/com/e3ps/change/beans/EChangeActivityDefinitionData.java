package com.e3ps.change.beans;

import com.e3ps.change.EChangeActivity;
import com.e3ps.change.EChangeActivityDefinition;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.org.bean.PeopleData;

import wt.org.WTUser;

public class EChangeActivityDefinitionData {
	private String oid;
	private String step;
	private String activityType;
	private String activityName;
	private String activityTypeValue;
	private String departmentName;
	private String name;
	private String activity;
	private String pOid;
	private String departmentCode;
	private String desc;
	private String finishDate;
	private int	span;
	
	public EChangeActivityDefinitionData(EChangeActivityDefinition def, int span) throws Exception {
		this.oid = StringUtil.checkNull(CommonUtil.getOIDString(def));
		this.step = StringUtil.checkNull(def.getStep());
		this.activityType = StringUtil.checkNull(def.getActiveType());
		this.activityName = StringUtil.checkNull(def.getName());
		this.activityTypeValue = StringUtil.checkNull(CodeHelper.service.getValue("EOACTIVETYPE", def.getActiveType()));
		if(this.activityTypeValue == "" || this.activityTypeValue == null) {
			this.activityTypeValue = this.activityType;
		}
		this.activity = StringUtil.checkNull(CommonUtil.getOIDString(def));
		this.span = span;
		this.desc = StringUtil.checkNull(def.getDescription());
		if(def.getWorker()!=null){
			Object owner = (Object)def.getWorker().getObject();
			PeopleData pd = new PeopleData(owner);
			this.pOid = pd.getpOid();
			this.departmentCode = pd.getDepartmentCode();
			this.departmentName = pd.getDepartmentName();
			this.name = pd.getName();
		}
	}
	
	public EChangeActivityDefinitionData(EChangeActivity eca) throws Exception {
		this.oid = StringUtil.checkNull(CommonUtil.getOIDString(eca));
		this.step = StringUtil.checkNull(eca.getStep());
		this.activityType = StringUtil.checkNull(eca.getActiveType());
		this.activityName = StringUtil.checkNull(eca.getName());
		this.activityTypeValue = StringUtil.checkNull(CodeHelper.service.getValue("EOACTIVETYPE", eca.getActiveType()));
		if(this.activityTypeValue == "" || this.activityTypeValue == null) {
			this.activityTypeValue = this.activityType;
		}
		this.activity = StringUtil.checkNull(CommonUtil.getOIDString(eca));
		this.desc = StringUtil.checkNull(eca.getDescription());
		this.finishDate = StringUtil.checkNull(eca.getFinishDate().toString());
		if(eca.getOwner()!=null){
			Object owner = (Object)eca.getOwner().getObject();
			PeopleData pd = new PeopleData(owner);
			this.pOid = pd.getpOid();
			this.departmentCode = pd.getDepartmentCode();
			this.departmentName = pd.getDepartmentName();
			this.name = pd.getName();
		}
	}

	public String getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getpOid() {
		return pOid;
	}

	public void setpOid(String pOid) {
		this.pOid = pOid;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String activityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityTypeValue() {
		return activityTypeValue;
	}

	public void setActivityTypeValue(String activityTypeValue) {
		this.activityTypeValue = activityTypeValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getActivityType() {
		return activityType;
	}

	public int getSpan() {
		return span;
	}

	public void setSpan(int span) {
		this.span = span;
	}
	
	public String getId() {
		return activity;
	}
}
