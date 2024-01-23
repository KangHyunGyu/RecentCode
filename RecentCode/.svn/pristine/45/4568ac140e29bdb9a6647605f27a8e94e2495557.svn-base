package com.e3ps.project.beans;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.e3ps.common.util.DateUtil;
import com.e3ps.project.EProject;
import com.e3ps.project.ETask;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.ScheduleNode;
import com.e3ps.project.key.ProjectKey.STATEKEY;
import com.e3ps.project.service.ProjectHelper;

public class ProjectTreeNode extends DefaultProjectTreeNode {
	
	public boolean editFlag = false;
	
	public ArrayList preTask = new ArrayList();
	
	
	public Timestamp orgPlanStartDate;
	public Timestamp orgPlanEndDate;
	public Timestamp orgStartDate;
	public Timestamp orgEndDate;
	public double orgCompletion;
	public String orgNodeState;
	public double orgNodeManDay;
	
	public ProjectTreeNode(ScheduleNode userObject) {
		super(userObject);
		
		
		orgPlanStartDate = userObject.getPlanStartDate();
		orgPlanEndDate = userObject.getPlanEndDate();
		orgCompletion = userObject.getCompletion();
		orgStartDate = userObject.getStartDate();
		orgEndDate = userObject.getEndDate();
		orgNodeManDay = userObject.getManDay();
		if(userObject instanceof ETaskNode){
			ETaskNode tasknode = (ETask)userObject;
			orgNodeState = tasknode.getStatus();
		}
		if(userObject instanceof EProject){
			EProject pjtnode = (EProject)userObject;
			orgNodeState = pjtnode.getState().toString();
		}
	}
	
	public boolean isEditable(){
		
		String state = "";
		if(userObject instanceof ETaskNode){
			ETaskNode tasknode = (ETask)userObject;
			state = tasknode.getStatus();
		}
		if(userObject instanceof EProject){
			EProject pjtnode = (EProject)userObject;
			state = pjtnode.getState().toString();
		}
		return !STATEKEY.COMPLETED.equals(state);
	}
	
	public String getLocationId(){
		Object[] path = this.getUserObjectPath();
		StringBuffer sb = new StringBuffer();
		for(int i=0; i< path.length; i++){
			ScheduleNode sn = (ScheduleNode)path[i];
			long sid = sn.getPersistInfo().getObjectIdentifier().getId();
			sb.append(sid);
		}
		return sb.toString();
	}
	
	public String getLocationName(){
		Object[] path = this.getUserObjectPath();
		StringBuffer sb = new StringBuffer();
		for(int i=0; i< path.length; i++){
			ScheduleNode sn = (ScheduleNode)path[i];
			sb.append("/");
			sb.append(sn.getName());
		}
		return sb.toString();
	}
	
	public String toString(){
		ScheduleNode node = (ScheduleNode)this.getUserObject();
		return node.getClass().getName() + ":" + node.getName();
	}
	
	public void setPreTaskId()throws Exception{
		if(getUserObject() instanceof ETaskNode) {
			List<ProjectTaskData> taskDataList = ProjectHelper.manager.getPreTaskList((ETaskNode) getUserObject());
			ArrayList preTaskList = new ArrayList();
			for(ProjectTaskData data : taskDataList) {
				preTaskList.add(data.getOid());
			}
			preTask = preTaskList;
		}
	}
	
	public boolean checkPlanEdit(){
		String orgStart = DateUtil.getDateString(orgPlanStartDate, "d");
		String newStart = DateUtil.getDateString(((ScheduleNode)getUserObject()).getPlanStartDate(), "d");
		
		
		
		if(!orgStart.equals(newStart)){
			return true;
		};
		
		String orgEnd = DateUtil.getDateString(orgPlanEndDate, "d");
		String newEnd = DateUtil.getDateString(((ScheduleNode)getUserObject()).getPlanEndDate(), "d");
		
		if(!orgEnd.equals(newEnd)){
			return true;
		};
		
		
		
		
		if(orgNodeManDay != (((ScheduleNode)getUserObject()).getManDay())){
			return true;
		}
		
		return false;
	}
	
	/**
	 * @return
	 */
	public boolean checkEdit(){
		String orgStart = DateUtil.getDateString(orgStartDate, "d");
		String newStart = DateUtil.getDateString(((ScheduleNode)getUserObject()).getStartDate(), "d");
		
		if(!orgStart.equals(newStart)){
			return true;
		};
		
		String orgEnd = DateUtil.getDateString(orgEndDate, "d");
		String newEnd = DateUtil.getDateString(((ScheduleNode)getUserObject()).getEndDate(), "d");
		
		if(!orgEnd.equals(newEnd)){
			return true;
		};
		
		String state = "";
		if(userObject instanceof ETaskNode){
			ETaskNode tasknode = (ETask)userObject;
			state = tasknode.getStatus();
		}
		if(userObject instanceof EProject){
			EProject pjtnode = (EProject)userObject;
			state = pjtnode.getState().toString();
		}
		
		if(orgNodeState!=null && !orgNodeState.equals(state)){
			return true;
		}
		
		return false;
	}
}
