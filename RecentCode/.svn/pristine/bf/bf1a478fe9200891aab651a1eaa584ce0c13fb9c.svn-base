/**
 * 
 */
package com.e3ps.dashboard.bean;

import java.util.ArrayList;
import java.util.List;

import com.e3ps.common.util.DateUtil;
import com.e3ps.project.EProject;
import com.e3ps.project.ETask;
import com.e3ps.project.beans.ProjectUtil;
import com.e3ps.project.key.ProjectKey;
import com.e3ps.project.key.ProjectKey.STATEKEY;
import com.e3ps.project.service.ProjectHelper;
import com.e3ps.project.service.ProjectMemberHelper;

import wt.org.WTUser;

/**
 * @author Administrator
 *
 */
public class ProjectDashboardData extends DashboardData {

	private static final int STATE_BAR_DELAY = 5;
	
	public String productName;
	
	public String code;
	public String planStartDate;
	public String planEndDate;
	
	public String startDate;
	public String endDate;
	
	public boolean isDelay;
	
	public String productNumber;
	
	public String pmName;
	
	public int planDuration;
	
	public double completion;
	public String stateTag;
	
	public List<DashboardTaskData> taskData;
	
	public ProjectDashboardData(String oid) {
		super(oid);
	}
	
	public ProjectDashboardData(EProject project) throws Exception{
		super(project);
		
		this.productName = project.getContainerName();
		
		this.code = project.getCode();
		this.planStartDate = DateUtil.getDateString(project.getPlanStartDate(),"d");
		this.planEndDate = DateUtil.getDateString(project.getPlanEndDate(),"d");

		this.startDate = DateUtil.getDateString(project.getStartDate(),"d");
		this.endDate = DateUtil.getDateString(project.getEndDate(),"d");
		
		this.isDelay = ProjectUtil.isDelay(project) == STATE_BAR_DELAY;
		this.completion = project.getCompletion();
		
		WTUser pm = ProjectMemberHelper.manager.getPM(project);
		if(pm != null) {
			this.pmName = pm.getFullName();
		}
		
		this.planDuration = ProjectUtil.getDuration(project.getPlanStartDate(), project.getPlanEndDate());
		if((STATEKEY.PROGRESS.equals(this.state) && this.isDelay)) {
			this.stateStr =  ProjectKey.STATEKEY.DELAY_KO;
		}
		
		this.stateTag = this.progressTag(this.state, this.stateStr, this.isDelay);
	}
	
	public String progressTag(String state, String stateStr, boolean isDelay) {
		String barColor = "blank";
		if(  STATEKEY.COMPLETED.equals(state )) {
			barColor = "green";
		}else if( STATEKEY.PROGRESS.equals(state )){
			barColor = isDelay ? "red" : "blue";
		}
		
		StringBuffer result = new StringBuffer();
		
		result.append("<img src='/Windchill/jsp/project/images/project/state_blank_bar.gif'>")
		.append("<img src='/Windchill/jsp/project/images/project/state_"+barColor+"_bar.gif'>")
		.append("<img src='/Windchill/jsp/project/images/project/state_blank_bar.gif'>")
		.append(" " + stateStr + "");
		
		return result.toString();
	}
	
	public void setChildTasks() throws Exception {
		taskData = new ArrayList<DashboardTaskData>();
		
		List<ETask> eTasks = ProjectHelper.manager.getProjectTaskChildren(this.oid);
		for(ETask task: eTasks) {
			taskData.add(new DashboardTaskData(task));
		}
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public boolean isDelay() {
		return isDelay;
	}

	public void setDelay(boolean isDelay) {
		this.isDelay = isDelay;
	}

	public String getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
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

	public String getStateTag() {
		return stateTag;
	}

	public void setStateTag(String stateTag) {
		this.stateTag = stateTag;
	}

	public List<DashboardTaskData> getTaskData() {
		return taskData;
	}

	public void setTaskData(List<DashboardTaskData> taskData) {
		this.taskData = taskData;
	}

	public static int getStateBarDelay() {
		return STATE_BAR_DELAY;
	}
	
	
}


class DashboardTaskData {
	public String name;

	public String planStartDate;
	public String planEndDate;
	

	public String startDate;
	public String endDate;
	
	public DashboardTaskData(ETask task) {
		this.name = task.getName();
		this.planStartDate = DateUtil.getDateString(task.getPlanStartDate(),"d");
		this.planEndDate = DateUtil.getDateString(task.getPlanEndDate(),"d");
		this.startDate = DateUtil.getDateString(task.getStartDate(),"d");
		this.endDate = DateUtil.getDateString(task.getEndDate(),"d");
	}
	
}
