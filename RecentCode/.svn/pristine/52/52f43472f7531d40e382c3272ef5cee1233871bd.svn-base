package com.e3ps.project.beans;

import java.util.Date;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.project.EProjectTemplate;

import wt.org.WTUser;
import wt.session.SessionHelper;

public class TemplateData {
	public String oid;
	public String code;
	public String name;
	public String manDay;
	public String createDate;
	public String modifyDate;
	public String creator;
	public String description;
	
	public String productGubunCode;
	public String productGubunName;
	
	public int duration;
	
	public String output;
	public String outputDisplay;
	public boolean manager;
	public boolean editable;
	public boolean enable;
	public boolean isManager;
	
	public TemplateData(EProjectTemplate template) throws Exception {
		this.oid = template.getPersistInfo().getObjectIdentifier().toString();
		this.code = template.getCode();
		this.name = template.getName();
		this.manDay = ProjectUtil.numberFormat(template.getManDay());
		this.output = template.getOutputType().toString();
		this.outputDisplay = template.getOutputType().getDisplay();
		
		this.createDate = DateUtil.getDateString(template.getPersistInfo().getCreateStamp(),"d");
		this.modifyDate = DateUtil.getDateString(template.getPersistInfo().getModifyStamp(),"d");
		this.creator = template.getCreator().getFullName();
		WTUser user = (WTUser)SessionHelper.manager.getPrincipal();
		this.manager = user.getName().equals(template.getCreator().getName());
		this.editable = template.isLastVersion();
		if(CommonUtil.isAdmin() == true || manager == true) {
			this.isManager = true;
		}else {
			this.isManager = false;
		}
		
		this.description = template.getDescription();
		
		//this.productGubunCode = StringUtil.checkNull(template.getProductGubun());
		//this.productGubunName = StringUtil.checkNull(NumberCodeHelper.service.getValue("PSO", template.getProductGubun()));
		
		this.enable = template.isEnabled();
		
		this.duration = DateUtil.getDuration(new Date(template.getPlanStartDate().getTime()),new Date(template.getPlanEndDate().getTime()))+1;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getManDay() {
		return manDay;
	}

	public void setManDay(String manDay) {
		this.manDay = manDay;
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

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public boolean isManager() {
		return manager;
	}

	public void setManager(boolean manager) {
		this.manager = manager;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getProductGubunCode() {
		return productGubunCode;
	}

	public void setProductGubunCode(String productGubunCode) {
		this.productGubunCode = productGubunCode;
	}

	public String getProductGubunName() {
		return productGubunName;
	}

	public void setProductGubunName(String productGubunName) {
		this.productGubunName = productGubunName;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getOutputDisplay() {
		return outputDisplay;
	}

	public void setOutputDisplay(String outputDisplay) {
		this.outputDisplay = outputDisplay;
	}
}
