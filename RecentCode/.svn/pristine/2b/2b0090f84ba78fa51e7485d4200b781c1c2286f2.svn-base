package com.e3ps.project.beans;

import java.util.List;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.project.EProject;
import com.e3ps.project.ETask;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.service.ProjectHelper;
import com.e3ps.project.service.ProjectMemberHelper;

import wt.org.WTUser;

public class ProjectTaskData {
	
	private String oid;
	private String name;
	private String description;
	
	private String pjtOid;
	private String pjtNumber;
	private String pjtName;
	private String pjtState;
	private String output;
	private String outputDisplay;
	private String pjtStartDate;
	private String pjtPlanStartDate;
	
	private String state;
	private String stateTag;
	private String manDay;
	private String planStartDate;
	private String planEndDate;
	private String createDate;
	private String pmName;
	private String startDate;
	private String endDate;
	private int delayDate;
	
	private int planDuration; 
	
	private double completion;
	private String completionFormat;
	
	private double preferComp;
	private String preferCompFormat;
	
	private boolean child;
	
	public ProjectTaskData(ETaskNode task) throws Exception {
		
		this.oid = CommonUtil.getOIDString(task);
		this.name = task.getName();
		this.description = task.getDescription();
		
		EProject project = (EProject) task.getProject();
		this.pjtOid = CommonUtil.getOIDString(project);
		this.pjtNumber = project.getCode();
		this.pjtName = project.getName();
		this.pjtState = project.getState().toString();
		this.output = project.getOutputType().toString();
		this.outputDisplay = project.getOutputType().getDisplay();
		this.pjtPlanStartDate = DateUtil.getDateString(project.getPlanStartDate(),"d");
		this.pjtStartDate = DateUtil.getDateString(project.getStartDate(),"d");
		
		this.manDay = ProjectUtil.numberFormat(task.getManDay());
		this.planStartDate = DateUtil.getDateString(task.getPlanStartDate(),"d");
		this.planEndDate = DateUtil.getDateString(task.getPlanEndDate(),"d");
		this.createDate = DateUtil.getDateString(task.getPersistInfo().getCreateStamp(),"d");
		this.startDate = DateUtil.getDateString(task.getStartDate(),"d");
		this.endDate = DateUtil.getDateString(task.getEndDate(),"d");
		
		List<ETask> children = ProjectHelper.manager.getProjectTaskChildren(this.oid);
		this.child = children.size() > 0;
		
		WTUser pm = ProjectMemberHelper.manager.getPM(task);
		this.state = task.getStatus();
		this.stateTag = ProjectUtil.getStateTag(task);
		this.planDuration = ProjectUtil.getDuration(task.getPlanStartDate(), task.getPlanEndDate());
		
		this.completion = task.getCompletion();
		this.completionFormat = ProjectUtil.numberFormat(task.getCompletion());
		
		this.preferComp = ProjectUtil.getPreferComp(task);
		this.preferCompFormat = ProjectUtil.numberFormat(this.preferComp);
		
		if(pm != null) {
			this.pmName = pm.getFullName();
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

	public String getPjtOid() {
		return pjtOid;
	}

	public void setPjtOid(String pjtOid) {
		this.pjtOid = pjtOid;
	}

	public String getPjtNumber() {
		return pjtNumber;
	}

	public void setPjtNumber(String pjtNumber) {
		this.pjtNumber = pjtNumber;
	}

	public String getPjtName() {
		return pjtName;
	}

	public void setPjtName(String pjtName) {
		this.pjtName = pjtName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateTag() {
		return stateTag;
	}

	public void setStateTag(String stateTag) {
		this.stateTag = stateTag;
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

	public String getManDay() {
		return manDay;
	}

	public void setManDay(String manDay) {
		this.manDay = manDay;
	}

	public String getPlanStartDate() {
		return planStartDate;
	}

	public void setPlanStartDate(String planStartDate) {
		this.planStartDate = planStartDate;
	}

	public String getPlanEndDate() {
		return planEndDate;
	}

	public void setPlanEndDate(String planEndDate) {
		this.planEndDate = planEndDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getPmName() {
		return pmName;
	}

	public void setPmName(String pmName) {
		this.pmName = pmName;
	}

	public int getPlanDuration() {
		return planDuration;
	}

	public void setPlanDuration(int planDuration) {
		this.planDuration = planDuration;
	}

	public double getCompletion() {
		return completion;
	}

	public void setCompletion(double completion) {
		this.completion = completion;
	}

	public String getCompletionFormat() {
		return completionFormat;
	}

	public void setCompletionFormat(String completionFormat) {
		this.completionFormat = completionFormat;
	}

	public double getPreferComp() {
		return preferComp;
	}

	public void setPreferComp(double preferComp) {
		this.preferComp = preferComp;
	}

	public String getPreferCompFormat() {
		return preferCompFormat;
	}

	public void setPreferCompFormat(String preferCompFormat) {
		this.preferCompFormat = preferCompFormat;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPjtState() {
		return pjtState;
	}

	public void setPjtState(String pjtState) {
		this.pjtState = pjtState;
	}

	public boolean isChild() {
		return child;
	}

	public void setChild(boolean child) {
		this.child = child;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getPjtStartDate() {
		return pjtStartDate;
	}

	public void setPjtStartDate(String pjtStartDate) {
		this.pjtStartDate = pjtStartDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public int getDelayDate() {
		return delayDate;
	}
	
	public void setDelayDate(int delayDate) {
		this.delayDate = delayDate;
	}

	public String getPjtPlanStartDate() {
		return pjtPlanStartDate;
	}

	public void setPjtPlanStartDate(String pjtPlanStartDate) {
		this.pjtPlanStartDate = pjtPlanStartDate;
	}
	
}
