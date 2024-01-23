package com.e3ps.project.beans;

import java.util.List;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.project.EOutput;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.OutputType;
import com.e3ps.project.OutputTypeStep;
import com.e3ps.project.TaskRoleLink;
import com.e3ps.project.service.TemplateHelper;

import wt.util.WTException;

public class TemplateOutputData {
	private String oid;
	private String name;
	private String location;
	private String docType;
	private String docTypeDisplay;
	private String description;
	private String taskOid;
	private String taskName;
	private String roleName;
	private String stepOid;
	private String stepCode;
	private String stepName;
	
	public TemplateOutputData(EOutput output) throws WTException {
		this.oid = CommonUtil.getOIDString(output);
		this.name = StringUtil.checkNull(output.getName());
		this.docType = StringUtil.checkNull(output.getDocType());
		OutputType outputType = OutputType.toOutputType(output.getDocType());
		if(outputType != null) {
			this.docTypeDisplay = outputType.getDisplay();
		}
		this.location = StringUtil.checkNull(output.getLocation()).replace("/Default", "");
		this.description = StringUtil.checkNull(output.getDescription());
		OutputTypeStep step = output.getStep();
		if(step != null) {
			this.stepOid = CommonUtil.getOIDString(step);
			this.stepCode = step.getCode();
			this.stepName = step.getName();
		}
		
		ETaskNode task = output.getTask();
		
		if(task != null) {
			this.taskOid = CommonUtil.getOIDString(task);
			this.taskName = StringUtil.checkNull(task.getName());
			List<TaskRoleLink> roleList = TemplateHelper.manager.getTaskRoleLinkList(task);
			this.roleName = "";
			for(TaskRoleLink link : roleList) {
				if(!"".equals(this.roleName)) {
					this.roleName += ", ";
				}
				this.roleName += link.getRole().getName();
			}
		}
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTaskOid() {
		return taskOid;
	}

	public void setTaskOid(String taskOid) {
		this.taskOid = taskOid;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getStepCode() {
		return stepCode;
	}

	public void setStepCode(String stepCode) {
		this.stepCode = stepCode;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public String getStepOid() {
		return stepOid;
	}

	public void setStepOid(String stepOid) {
		this.stepOid = stepOid;
	}

	public String getDocTypeDisplay() {
		return docTypeDisplay;
	}

	public void setDocTypeDisplay(String docTypeDisplay) {
		this.docTypeDisplay = docTypeDisplay;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
}
