package com.e3ps.project.beans;

import java.sql.Timestamp;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.project.EProject;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.ScheduleNode;
import com.e3ps.project.key.ProjectKey.STATEKEY;
import com.e3ps.project.service.ProjectHelper;

public class ProjectScheduleData {

	private Timestamp planStartDate;
	private Timestamp planEndDate;
	private String planStartDateStr;
	private String planEndDateStr;
	
	private int planDuration; 
	private int planDurationHoliday; 
	
	private double completion;
	private String completionFormat;
	
	private double preferComp;
	private String preferCompFormat;
	
	private Timestamp startDate;
	private Timestamp endDate;
	private String startDateStr;
	private String endDateStr;
	
	private boolean start;
	
	private boolean multiStart;
	
	public String stateTag;
	
	public ProjectScheduleData(ScheduleNode node) throws Exception {
		
		this.planStartDate = node.getPlanStartDate();
		this.planEndDate = node.getPlanEndDate();
		this.planStartDateStr = DateUtil.getDateString(this.planStartDate,"d");
		this.planEndDateStr = DateUtil.getDateString(this.planEndDate,"d");
		
		
		this.planDuration = ProjectUtil.getDuration(this.planStartDate, this.planEndDate);
		this.planDurationHoliday = ProjectUtil.getDurationHoliday(this.planStartDate, this.planEndDate); 
				
		this.completion = node.getCompletion();
		this.completionFormat = ProjectUtil.numberFormat(node.getCompletion());
		
		this.preferComp = ProjectUtil.getPreferComp(node);
		this.preferCompFormat = ProjectUtil.numberFormat(this.preferComp);
		
		this.startDate = node.getStartDate();
		this.endDate = node.getEndDate();
		this.startDateStr = DateUtil.getDateString(this.startDate,"d");
		this.endDateStr = DateUtil.getDateString(this.endDate,"d");
		
		if(node instanceof ETaskNode){
			ETaskNode task = (ETaskNode)node;
			EProject pjt = (EProject)task.getProject();
			boolean isChildren = ProjectHelper.manager.getProjectTaskDataChildren(CommonUtil.getOIDString(node)).size() > 0;
			
			if(!isChildren && STATEKEY.READY.equals(task.getStatus()) && STATEKEY.PROGRESS.equals(pjt.getState().toString())) {
				this.start = true;
				this.multiStart = false;
			}
			
			if(isChildren && !STATEKEY.COMPLETED.equals(task.getStatus())
					&& STATEKEY.PROGRESS.equals(pjt.getState().toString())) {
				this.start = false;
				this.multiStart = true;
			}
		}
		
		this.stateTag = ProjectUtil.getStateTag(node);
	}

	public boolean isMultiStart() {
		return multiStart;
	}

	public void setMultiStart(boolean multiStart) {
		this.multiStart = multiStart;
	}

	public Timestamp getPlanStartDate() {
		return planStartDate;
	}

	public void setPlanStartDate(Timestamp planStartDate) {
		this.planStartDate = planStartDate;
	}

	public Timestamp getPlanEndDate() {
		return planEndDate;
	}

	public void setPlanEndDate(Timestamp planEndDate) {
		this.planEndDate = planEndDate;
	}

	public String getPlanStartDateStr() {
		return planStartDateStr;
	}

	public void setPlanStartDateStr(String planStartDateStr) {
		this.planStartDateStr = planStartDateStr;
	}

	public String getPlanEndDateStr() {
		return planEndDateStr;
	}

	public void setPlanEndDateStr(String planEndDateStr) {
		this.planEndDateStr = planEndDateStr;
	}

	public int getPlanDuration() {
		return planDuration;
	}

	public void setPlanDuration(int planDuration) {
		this.planDuration = planDuration;
	}

	public int getPlanDurationHoliday() {
		return planDurationHoliday;
	}

	public void setPlanDurationHoliday(int planDurationHoliday) {
		this.planDurationHoliday = planDurationHoliday;
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

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getStartDateStr() {
		return startDateStr;
	}

	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}

	public String getEndDateStr() {
		return endDateStr;
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public String getStateTag() {
		return stateTag;
	}

	public void setStateTag(String stateTag) {
		this.stateTag = stateTag;
	}
	
}
