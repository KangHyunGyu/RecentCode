package com.e3ps.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.e3ps.project.EProject;
import com.e3ps.project.EProjectNode;
import com.e3ps.project.ETask;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.PrePostLink;
import com.e3ps.project.ProjectRegistApproval;
import com.e3ps.project.ScheduleNode;
import com.e3ps.project.beans.ProjectGanttLinkData;
import com.e3ps.project.beans.ProjectGanttTaskData;
import com.e3ps.project.beans.ProjectTreeModel;

import wt.fc.QueryResult;
import wt.util.WTException;

public interface ProjectService {

	public EProject getProject(String number) throws Exception;

	public EProject createProject(Map<String, Object> hash) throws Exception;

	public String update(Map<String, Object> hash) throws Exception;

	public String delete(Map<String, Object> hash) throws Exception;

	public void copyProject(EProjectNode orgProject, EProjectNode newProject)
			throws Exception;

	public void copyTaskAttribute(ETaskNode org, ETaskNode node)
			throws Exception;

	public void setState(EProject pjt, String state) throws Exception;

	public void delete(ScheduleNode node) throws Exception;

	public ETaskNode createTask(Hashtable hash) throws Exception;

	public ETaskNode updateTask(Hashtable hash) throws Exception;

	public ETaskNode nextSave(Hashtable hash) throws Exception;

	public ArrayList deleteTask(Hashtable hash) throws Exception;

	public ETask workComplete(Map<String, Object> hash) throws Exception;

	public void workComplete(ETaskNode node) throws Exception;

	public String cancelComplete(Map<String, Object> hash) throws Exception;

	public ProjectRegistApproval startProjectRequest(String oid)
			throws Exception;

	public String stopProject(Map<String, Object> hash) throws Exception;

	public String restartProject(Map<String, Object> hash) throws Exception;

	public EProject checkoutProject(Map<String, Object> hash) throws Exception;

	// private void copyAttribute(EProject org, EProject project)throws
	// Exception;
	public HashMap<String, Object> checkinProject(Map<String, Object> hash)
			throws Exception;

	public EProject undoCheckoutProject(Map<String, Object> hash)
			throws Exception;

	public boolean isPreComplete(ScheduleNode node) throws Exception;

	public boolean isLastNode(ScheduleNode node) throws Exception;

	public QueryResult getAllProgressProject() throws Exception;

	public ETaskNode upTask(Hashtable hash) throws Exception;

	public ETaskNode downTask(Hashtable hash) throws Exception;

	public String editMember(Map<String, Object> hash) throws Exception;

	public ETask saveCompletion(Map<String, Object> hash) throws Exception;

	public ETask changeStartDate(Map<String, Object> hash) throws Exception;

	public void runProjectSchedule() throws Exception;

	public StringBuffer getPreTaskTree(String oid, String selectChild,
			ArrayList list, ProjectTreeModel model) throws Exception;

	public ArrayList setPreTask2(Hashtable hash) throws Exception;

	public ScheduleNode setRoleUserLink(Hashtable hash) throws Exception;

	public ETask startTask(String oid) throws Exception;

	public ETask updateTaskAction(Map map);

	EProject getProjectByCode(String code) throws WTException;

	EProject getProjectByName(String name) throws WTException;

	public abstract void createMasterLink(Map<String, Object> hash) throws Exception;

	public abstract void deleteMasterLink(String oid) throws Exception;

	/**
	 *  Sort 수정  (StandardTemplateService에서 이동)
	 * @param task
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	int updateSort(ETask task, int seq) throws Exception;

	/**선행 태스크 정보 저장
	 * @param hash
	 * @throws Exception
	 */
	void setPreTask(Map<String, Object> reqMap) throws Exception;

	/**
	 * Role 수정 (TASK 담당자 수정)
	 * 
	 * @param hash
	 * @throws Exception
	 */
	String editRole(Map<String, Object> reqMap) throws Exception;

	Map<String, Object> moveUpTask(Map<String, Object> reqMap) throws Exception;

	Map<String, Object> moveDownTask(Map<String, Object> reqMap) throws Exception;

	public abstract ETask updateGanttTaskAction(Map<String, Object> reqMap);

	public abstract void updateGanttTaskAllAction(Map<String, Object> reqMap);

	public abstract void deleteGanttTaskAction(Map<String, Object> reqMap);

	public abstract PrePostLink addGanttLinkAction(Map<String, Object> reqMap);

	public abstract void deleteGanttLinkAction(Map<String, Object> reqMap);

	public abstract ETask addGanttTaskAction(Map<String, Object> reqMap);

	public abstract List<ProjectGanttTaskData> saveGanttTaskAllAction(Map<String, Object> reqMap);

	public abstract List<ProjectGanttLinkData> saveGanttLinkAllAction(Map<String, Object> reqMap);

	public abstract void saveGanttAction(Map<String, Object> reqMap);

	public abstract void moveGanttTaskAction(Map<String, Object> reqMap);

	public EProject copyProject(Map<String, Object> hash) throws Exception;

	public String editMember2(Map<String, Object> hash) throws Exception;

	public EProject modifyProjectStartDate(Map<String, Object> hash) throws Exception;

	public EProject copyProjectCreate(Map<String, Object> reqMap) throws Exception;;
}
