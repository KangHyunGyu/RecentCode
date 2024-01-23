package com.e3ps.project.service;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.EChangeRequest2;
import com.e3ps.change.beans.ChangeECOSearch;
import com.e3ps.change.beans.ChangeECRSearch;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.query.SearchUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.web.PageQueryBroker;
import com.e3ps.doc.E3PSDocument;
import com.e3ps.doc.service.DocHelper;
import com.e3ps.epm.service.EpmHelper;
import com.e3ps.part.service.PartHelper;
import com.e3ps.project.EOutput;
import com.e3ps.project.EProject;
import com.e3ps.project.EProjectMasteredLink;
import com.e3ps.project.EProjectNode;
import com.e3ps.project.ETask;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.PrePostLink;
import com.e3ps.project.ProjectRole;
import com.e3ps.project.ProjectStopHistory;
import com.e3ps.project.RoleUserLink;
import com.e3ps.project.TaskOutputLink;
import com.e3ps.project.TaskRoleLink;
import com.e3ps.project.beans.ProjectData;
import com.e3ps.project.beans.ProjectGanttEditTaskData;
import com.e3ps.project.beans.ProjectGanttLinkData;
import com.e3ps.project.beans.ProjectGanttTaskData;
import com.e3ps.project.beans.ProjectGanttViewTaskData;
import com.e3ps.project.beans.ProjectMasteredLinkData;
import com.e3ps.project.beans.ProjectOutputData;
import com.e3ps.project.beans.ProjectQuery;
import com.e3ps.project.beans.ProjectRoleData;
import com.e3ps.project.beans.ProjectStopHistoryData;
import com.e3ps.project.beans.ProjectTaskData;
import com.e3ps.project.beans.ProjectTreeData;
import com.e3ps.project.key.ProjectKey.STATEKEY;
import com.e3ps.project.util.ETaskMailForm;
import com.e3ps.queue.E3PSQueueHelper;

import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentItem;
import wt.content.ContentRoleType;
import wt.content.HolderToContent;
import wt.epm.EPMDocument;
import wt.fc.PagingQueryResult;
import wt.fc.PagingSessionHelper;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteMethodServer;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.pds.StatementSpec;
import wt.query.ArrayExpression;
import wt.query.ClassAttribute;
import wt.query.KeywordExpression;
import wt.query.OrderBy;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.query.SubSelectExpression;
import wt.services.ServiceFactory;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.vc.Mastered;

public class ProjectHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PROJECT.getName());
	public static ProjectService service = ServiceFactory.getService(ProjectService.class);
	public static final ProjectHelper manager = new ProjectHelper();
	
	public List<ProjectData> getProjectList(Map<String, Object> reqMap) throws Exception {
		
		List<ProjectData> list = new ArrayList<>();
		QuerySpec query = ProjectQuery.manager.getProjectListQuery(reqMap);

		QueryResult result = PersistenceHelper.manager.find((StatementSpec)query);
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			EProject pjt = (EProject) obj[0];
			
			ProjectData data = new ProjectData(pjt);
			
			list.add(data);
		}
		
		return list;
	}
	
	public Map<String, Object> getProjectScrollList(Map<String, Object> reqMap) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
		
		List<ProjectData> list = new ArrayList<>();
		
		int page = (Integer) reqMap.get("page");
		int rows = (Integer) reqMap.get("rows");
		
		String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));
		
		PagingQueryResult result = null;
		
		if (sessionId.length() > 0) {
			result = PagingSessionHelper.fetchPagingSession((page - 1) * rows, rows, Long.valueOf(sessionId));
		} else {
			QuerySpec query = ProjectQuery.manager.getProjectListQuery(reqMap);
			result = PageQueryBroker.openPagingSession((page - 1) * rows, rows, query, true);
		}
		
		int totalSize = result.getTotalSize();
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			EProject pjt = (EProject) obj[0];
			ProjectData data = new ProjectData(pjt);
			
			list.add(data);
		}

		map.put("list", list);
		map.put("totalSize", totalSize);
		map.put("sessionId", result.getSessionId() == 0 ? "" : result.getSessionId());
		
		return map;
	}
	
	public List<ProjectData> getMyProjectList(Map<String, Object> reqMap) throws Exception {
		
		List<ProjectData> list = new ArrayList<>();
		
		QuerySpec query = ProjectQuery.manager.getMyProject(reqMap);
		
		QueryResult result = PersistenceHelper.manager.find((StatementSpec)query);
		LOGGER.info("result " + result.size());
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			EProject pjt = (EProject) obj[0];
			
			ProjectData data = new ProjectData(pjt);
			
			list.add(data);
		}
		
		return list;
	}
	
	public List<ProjectTaskData> getMyTaskList(Map<String, Object> reqMap) throws Exception {
		
		List<ProjectTaskData> list = new ArrayList<>();
		
		QuerySpec query = ProjectQuery.manager.getMyTask(reqMap);
		QueryResult result = PersistenceHelper.manager.find((StatementSpec)query);
		LOGGER.info("result " + result.size());
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			ETask task = (ETask)obj[0];
			
			ProjectTaskData data = new ProjectTaskData(task);
			
			list.add(data);
		}
		
		return list;
	}

	public List<ProjectOutputData> getOutputList(Map<String, Object> reqMap) throws Exception {
		
		List<ProjectOutputData> list = new ArrayList<>();
		QuerySpec query = ProjectQuery.manager.getOutputList(reqMap);
		
		QueryResult result = PersistenceHelper.manager.find((StatementSpec)query);
		while(result.hasMoreElements()){
			Object[] o = (Object[]) result.nextElement();
			EOutput output = (EOutput)o[0];

			ProjectOutputData data = new ProjectOutputData(output);
			
			list.add(data);
		}
		
		return list;
	}
	
	public Map<String, Object> getOutputScrollList(Map<String, Object> reqMap) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
		
		List<ProjectOutputData> list = new ArrayList<>();
		
		int start = (Integer) reqMap.get("start");
		int count = (Integer) reqMap.get("count");
		
		String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));
		
		PagingQueryResult result = null;
		
		if (sessionId.length() > 0) {
			result = PagingSessionHelper.fetchPagingSession(start, count, Long.valueOf(sessionId));
		} else {
			QuerySpec query = ProjectQuery.manager.getOutputList(reqMap);
			
			result = PageQueryBroker.openPagingSession(start, count, query, true);
		}
		
		int totalSize = result.getTotalSize();
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			EOutput output = (EOutput)obj[0];

			ProjectOutputData data = new ProjectOutputData(output);
			
			list.add(data);
		}
		
		map.put("sessionId", result.getSessionId() == 0 ? "" : result.getSessionId());

		//webix
		map.put("data", list);
		map.put("total_count", totalSize);
		map.put("pos", start);
		
		return map;
	}
	
	public List<ProjectOutputData> getProjectOutputList(Map<String, Object> reqMap) throws Exception {
		
		List<ProjectOutputData> list = new ArrayList<>();
		QuerySpec query = ProjectQuery.manager.getProjectOutputList(reqMap);
		QueryResult result = PersistenceHelper.manager.find((StatementSpec)query);
		while(result.hasMoreElements()){
			Object[] o = (Object[]) result.nextElement();
			EOutput output = (EOutput)o[0];
			if(output != null) {
				ProjectOutputData data = new ProjectOutputData(output);
				
				list.add(data);
			}
		}
		return list;
	}
	
	/**
	 * @desc	: 프로젝트  MasteredLink 찾기
	 * @author	: sangylee
	 * @date	: 2020. 9. 25.
	 * @method	: getMasteredLink
	 * @param	: mastered
	 * @return	: EProjectMasteredLink
	 * @throws Exception 
	 */
	public EProjectMasteredLink getMasteredLink(Mastered mastered) throws Exception {
		EProjectMasteredLink link = null;
		
		QuerySpec query = ProjectQuery.manager.getProjectMasteredLink(mastered);
		QueryResult result = PersistenceHelper.manager.find((StatementSpec)query);
		if(result.hasMoreElements()){
			Object[] o = (Object[])result.nextElement();
			link = (EProjectMasteredLink)o[0];
		}
		return link;
	}
	
	public List<ProjectMasteredLinkData> getMasteredLinkList(Map<String, Object> reqMap) throws Exception {
		List<ProjectMasteredLinkData> list = new ArrayList<ProjectMasteredLinkData>();
		
		QuerySpec query = ProjectQuery.manager.getProjectMasteredList(reqMap);
		QueryResult result = PersistenceHelper.manager.find((StatementSpec)query);
		while(result.hasMoreElements()){
			Object[] o = (Object[])result.nextElement();
			EProjectMasteredLink link = (EProjectMasteredLink)o[0];
			
			ProjectMasteredLinkData data = new ProjectMasteredLinkData(link);
			
			list.add(data);
		}
		return list;
	}
	
	/**
	 * @desc	: 관련 프로젝트 autocomplete
	 * @author	: sangylee
	 * @date	: 2020. 9. 4.
	 * @method	: getSearchRelatedProject
	 * @return	: List<ProjectData>
	 * @param reqMap
	 * @return
	 * @throws QueryException 
	 */
	@SuppressWarnings("rawtypes")
	public List<ProjectData> getSearchRelatedProject(Map<String, Object> reqMap) throws Exception {
		List<ProjectData> list = new ArrayList<ProjectData>();
		String number = StringUtil.checkNull((String) reqMap.get("number"));
		
		QuerySpec qs = new QuerySpec();
		Class cls = EProject.class;
		
		int idx = qs.appendClassList(cls, true);
		qs.appendWhere(new SearchCondition(cls,"lastVersion",SearchCondition.IS_TRUE),new int[]{0});
		qs.appendAnd(); 
		qs.appendOpenParen();
		qs.appendWhere(new SearchCondition(cls, EProject.CODE, SearchCondition.LIKE, "%"+number.toUpperCase()+"%", false), new int[]{idx});
		qs.appendOr();
		qs.appendWhere(new SearchCondition(cls, "name", SearchCondition.LIKE, "%"+number.toUpperCase()+"%", false), new int[]{idx});
		qs.appendCloseParen();
		
		
		if(!CommonUtil.isAdmin()) {
    		int roleIdx = qs.addClassList(ProjectRole.class, false);
		    int linkIdx = qs.addClassList(RoleUserLink.class,false);
		    
		    List<Long> userOidList = new ArrayList<>();
		    
		    WTUser user = (WTUser) SessionHelper.manager.getPrincipal();
	    	userOidList.add(CommonUtil.getOIDLongValue(user));
    		
    		qs.appendAnd();
    		SearchCondition sc = new SearchCondition(new ClassAttribute(EProject.class, EProject.PERSIST_INFO + ".theObjectIdentifier.id"), SearchCondition.EQUAL, new ClassAttribute(ProjectRole.class, "projectReference.key.id"));
    	    qs.appendWhere(sc, new int[] { idx, roleIdx });
    	    
    	    qs.appendAnd();
    	    sc = new SearchCondition(new ClassAttribute(ProjectRole.class, ProjectRole.PERSIST_INFO + ".theObjectIdentifier.id"), SearchCondition.EQUAL, new ClassAttribute(RoleUserLink.class, "roleBObjectRef.key.id"));
    	    qs.appendWhere(sc, new int[] { roleIdx, linkIdx });
    	    
    	    qs.appendAnd();
    	    qs.appendWhere(new SearchCondition(new ClassAttribute(RoleUserLink.class, "roleAObjectRef.key.id"), SearchCondition.IN, new ArrayExpression(userOidList.toArray())), new int[] { linkIdx });
    	}
		
		qs.appendOrderBy(new OrderBy(new ClassAttribute(cls, EProject.CODE), false), new int[] { idx });
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		while(qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			EProject pj = (EProject)obj[0];
			ProjectData data = new ProjectData(pj);
			
			list.add(data);
		}
		
		return list;
	}
	
	public List<ProjectData> getRelatedProject(Map<String, Object> reqMap) throws Exception {
		List<ProjectData> list = new ArrayList<ProjectData>();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		Persistable per = CommonUtil.getObject(oid);
		
		if(per != null) {
			if(per instanceof E3PSDocument) {
				E3PSDocument doc = (E3PSDocument) per;
				list = DocHelper.manager.getRelatedProjectList(doc);
			}else if(per instanceof EChangeOrder2) {
				EChangeOrder2 eco = (EChangeOrder2) per;
				list = ChangeECOSearch.getRelatedProject(eco);
			}else if(per instanceof EChangeRequest2) {
				EChangeRequest2 ecr = (EChangeRequest2) per;
				list = ChangeECRSearch.getRelatedProject(ecr);
			}else if(per instanceof EPMDocument) {
				EPMDocument epm = (EPMDocument) per;
				list = EpmHelper.manager.getRelatedProjectList(epm);
			}else if(per instanceof WTPart) {
				WTPart part = (WTPart) per;
				list = PartHelper.manager.getRelatedProjectList(part);
			}
		}
		
		return list;
	}
	
	/**
	 * @desc	: 프로젝트 리스트 가져오기 (StandardTemplateService에서 이동)
	 * @author	: sangylee
	 * @date	: 2020. 9. 22.
	 * @method	: getDownMinTask
	 * @param	: task
	 * @return	: ETask
	 */
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public ETask getDownMinTask(ETask task) throws Exception {

    	if (!wt.method.RemoteMethodServer.ServerFlag) {
			Class argTypes[] = new Class[]{ETask.class};
			Object args[] = new Object[]{task};
			try {
				Object obj =RemoteMethodServer.getDefault().invoke("getDownMinTask", StandardTemplateService.class.getName() , null, argTypes, args);
				return (ETask)obj;

			} catch (RemoteException e) {
				e.printStackTrace();
				throw new WTException(e);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new WTException(e);
			}
		}

		// SubQs
    	ETask reTask = null;
        
    	int selectSeq = task.getSort();

		QuerySpec Subqs = new QuerySpec();
		Subqs.setDescendantQuery(false);
		Class taskClass = task.getClass();
	    
		
	    int idx_up = Subqs.addClassList(taskClass, false);
	   
	    if (Subqs.getConditionCount() > 0){
			Subqs.appendAnd();
		}

		Subqs.appendSelect(new KeywordExpression("NVL(Min(" + ETask.SORT + "), -1)"), new int[] {idx_up}, false);
		Subqs.appendWhere(new SearchCondition(taskClass,
							     		ETask.SORT,
										">",
										selectSeq), new int[] {idx_up});


		
    	long parentId = task.getParent().getPersistInfo().getObjectIdentifier().getId();
    	SearchUtil.appendEQUAL(Subqs, taskClass, ETask.PARENT_REFERENCE + ".key.id", parentId, idx_up);
    

		// Main
		QuerySpec spec = new QuerySpec();
		spec.setDescendantQuery(false);
		spec.setAdvancedQueryEnabled(true);

        int taskClassPos = spec.addClassList(taskClass, true);

   
    	SearchUtil.appendEQUAL(spec, taskClass, ETask.PARENT_REFERENCE + ".key.id", parentId, taskClassPos);


        
	    if(spec.getConditionCount() > 0){
	    	spec.appendAnd();
	    }

	    ClassAttribute cAttr = new ClassAttribute(taskClass, ETask.SORT);
	    spec.appendWhere(new SearchCondition( cAttr,SearchCondition.EQUAL, new SubSelectExpression(Subqs) ) , new int[] {taskClassPos});


		QueryResult rs = PersistenceHelper.manager.find(spec);


		if(rs.hasMoreElements()){
			Object o[] = (Object[])rs.nextElement();
			reTask = (ETask)o[0];
		}

		return reTask;
    }
	
	/**
	 * @desc	: 프로젝트 Task Role 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 25.
	 * @method	: getTaskRoleList
	 * @param	: task
	 * @return	: List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getTaskRoleList(ETaskNode task) throws WTException {
		List<Map<String, Object>> list = new ArrayList<>();
		
		List<TaskRoleLink> linkList = ProjectHelper.manager.getTaskRoleLinkList(task);
		for(TaskRoleLink link : linkList) {
			ProjectRole role = link.getRole();
			
			Map<String, Object> map = new HashMap<>();
			
			map.put("oid", CommonUtil.getOIDString(role));
			map.put("code", role.getCode());
			map.put("name", role.getName());
			
			list.add(map);
		}
		
		return list;
	}
	
	/**
	 * @desc	: 프로젝트 Task Role Link 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 24.
	 * @method	: getTaskRoleLinkList
	 * @param	: task
	 * @return	: List<TaskRoleLink>
	 */
	public List<TaskRoleLink> getTaskRoleLinkList(ETaskNode task) throws WTException {
		List<TaskRoleLink> list = new ArrayList<>();
		
		QueryResult qr = PersistenceHelper.manager.navigate(task,"role",TaskRoleLink.class, false);
		while(qr.hasMoreElements()){
			TaskRoleLink link = (TaskRoleLink)qr.nextElement();
			
			list.add(link);
		}
		
		return list;
	}
	
	
	
	
	
	
	/**
	 * @desc	: 프로젝트 트리 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 25.
	 * @method	: getProjectTree
	 * @param	: reqMap
	 * @return	: List<TemplateTreeData>
	 */
	public List<ProjectTreeData> getProjectTreeAUI(Map<String, Object> reqMap) throws Exception {
		
		List<ProjectTreeData> list = new ArrayList<>();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		EProject project = (EProject) CommonUtil.getObject(oid);
		
		ProjectTreeData root = new ProjectTreeData(project);
		list.add(root);
		getProjectChildrenAUI(root, list);
		
		return list;
	}
	
	/**
	 * @desc	: 프로젝트 트리 하위 가져오기 재귀
	 * @author	: sangylee
	 * @date	: 2020. 9. 25.
	 * @method	: getProjectChildren
	 * @param	: parent
	 * @return	: List<TemplateTreeData>
	 */
	public List<ProjectTreeData> getProjectChildrenAUI(ProjectTreeData parent, List<ProjectTreeData> list) throws Exception {
		List<ETask> childrenList = getProjectTaskChildren(parent.getOid());
		
		for(ETask task : childrenList) {
			ProjectTreeData data = new ProjectTreeData(task);
			list.add(data);
			getProjectChildrenAUI(data, list);
		}
		return list;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * @desc	: 프로젝트 트리 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 25.
	 * @method	: getProjectTree
	 * @param	: reqMap
	 * @return	: List<TemplateTreeData>
	 */
	public List<ProjectTreeData> getProjectTree(Map<String, Object> reqMap) throws Exception {
		
		List<ProjectTreeData> list = new ArrayList<>();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		EProject project = (EProject) CommonUtil.getObject(oid);
		
		ProjectTreeData root = new ProjectTreeData(project);
		
		root.setData(getProjectChildren(root));
		
		list.add(root);
		
		return list;
	}
	
	/**
	 * @desc	: 프로젝트 트리 하위 가져오기 재귀
	 * @author	: sangylee
	 * @date	: 2020. 9. 25.
	 * @method	: getProjectChildren
	 * @param	: parent
	 * @return	: List<TemplateTreeData>
	 */
	public List<ProjectTreeData> getProjectChildren(ProjectTreeData parent) throws Exception {

		List<ProjectTreeData> list = new ArrayList<>();
		
		List<ETask> childrenList = getProjectTaskChildren(parent.getOid());
		
		for(ETask task : childrenList) {
			ProjectTreeData data = new ProjectTreeData(task);
			
			data.setData(getProjectChildren(data));
			
			list.add(data);
		}
		
		return list;
	}
	
	/**
	 * @desc	: 프로젝트 트리 하위 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 25.
	 * @method	: getProjectTaskChildren
	 * @param	: parentOid
	 * @return	: List<ETask>
	 */
	public List<ETask> getProjectTaskChildren(String parentOid) throws Exception {
		
		List<ETask> list = new ArrayList<>();
		
		QuerySpec query = new QuerySpec();
		
		int idx = query.addClassList(ETask.class, true);
		long loid = CommonUtil.getOIDLongValue(parentOid);
		
		query.appendWhere(new SearchCondition(ETask.class, "parentReference.key.id", SearchCondition.EQUAL, loid), new int[]{idx});
		
		query.appendOrderBy(new OrderBy(new ClassAttribute(ETask.class,"sort"), false), new int[] { idx });  
		query.appendOrderBy(new OrderBy(new ClassAttribute(ETask.class,"thePersistInfo.theObjectIdentifier.id"), false), new int[] { idx });  
		
		QueryResult qr = PersistenceHelper.manager.find(query);
		
		while(qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			ETask task = (ETask) obj[0];
			
			list.add(task);
		}
		
		return list;
	}
	
	
	
	
	public List<ETask> getTaskList(String oid, List<ETask> list) throws Exception {
		
		QuerySpec query = new QuerySpec();
		
		int idx = query.addClassList(ETask.class, true);
		long loid = CommonUtil.getOIDLongValue(oid);
		
		query.appendWhere(new SearchCondition(ETask.class, "parentReference.key.id", SearchCondition.EQUAL, loid), new int[]{idx});
		
		query.appendOrderBy(new OrderBy(new ClassAttribute(ETask.class,"sort"), false), new int[] { idx });  
		query.appendOrderBy(new OrderBy(new ClassAttribute(ETask.class,"thePersistInfo.theObjectIdentifier.id"), false), new int[] { idx });  
		
		QueryResult qr = PersistenceHelper.manager.find(query);
		
		while(qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			ETask task = (ETask) obj[0];
			list.add(task);
			
			list = getTaskList(CommonUtil.getOIDString(task), list);
		}
		
		return list;
	}
	
	
	
	
	
	/**
	 * @desc	: 프로젝트 자식 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 25.
	 * @method	: getProjectTaskChildren
	 * @param	: parentOid
	 * @return	: List<ETask>
	 */
	public List<ProjectTaskData> getProjectTaskDataChildren(String parentOid) throws Exception {
		
		List<ProjectTaskData> list = new ArrayList<>();
		
		List<ETask> taskList = ProjectHelper.manager.getProjectTaskChildren(parentOid);
		
		for(ETask task : taskList) {
			ProjectTaskData data = new ProjectTaskData(task);
			
			list.add(data);
		}
		
		return list;
	}
	
	/**
	 * @desc	: 프로젝트 선행 태스크 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 25.
	 * @method	: getPreTaskList
	 * @param	: task
	 * @return	: List<ProjectTaskData>
	 * @throws Exception 
	 */
	public List<ProjectTaskData> getPreTaskList(ETaskNode task) throws Exception {
		List<ProjectTaskData> list = new ArrayList<>();
		
		List<PrePostLink> preLinkList = ProjectHelper.manager.getPreTaskLinkList(task);
		
		for(PrePostLink link : preLinkList) {
			ETaskNode preTask = link.getPre();
			ProjectTaskData data = new ProjectTaskData(preTask);
			
			list.add(data);
		}
		
		return list;
	}
	
	/**
	 * @desc	: 프로젝트 선행 태스크 링크 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 25.
	 * @method	: getPreTaskLinkList
	 * @param	: task
	 * @return	: List<PrePostLink>
	 * @throws Exception 
	 */
	public List<PrePostLink> getPreTaskLinkList(ETaskNode task) throws Exception {
		List<PrePostLink> list = new ArrayList<>();
		
		QueryResult qr = PersistenceHelper.manager.navigate(task,"pre",PrePostLink.class, false);
		while(qr.hasMoreElements()){
			PrePostLink link = (PrePostLink)qr.nextElement();
			
			list.add(link);
		}
		
		return list;
	}
	
	/**
	 * @desc	: 프로젝트 후행 태스크 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 14.
	 * @method	: getPostTaskList
	 * @param	: task
	 * @return	: List<ProjectTaskData>
	 * @throws Exception 
	 */
	public List<ProjectTaskData> getPostTaskList(ETaskNode task) throws Exception {
		List<ProjectTaskData> list = new ArrayList<>();
		
		List<PrePostLink> postLinkList = ProjectHelper.manager.getPostTaskLinkList(task);
		
		for(PrePostLink link : postLinkList) {
			ETaskNode postTask = link.getPost();
			ProjectTaskData data = new ProjectTaskData(postTask);
			
			list.add(data);
		}
		
		return list;
	}
	
	/**
	 * @desc	: 프로젝트 후행 태스크 링크 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 15.
	 * @method	: getPostTaskLinkList
	 * @param	: task
	 * @return	: List<PrePostLink>
	 * @throws Exception 
	 */
	public List<PrePostLink> getPostTaskLinkList(ETaskNode task) throws Exception {
		List<PrePostLink> list = new ArrayList<>();
		
		QueryResult qr = PersistenceHelper.manager.navigate(task,"post",PrePostLink.class, false);
		while(qr.hasMoreElements()){
			PrePostLink link = (PrePostLink)qr.nextElement();
			
			list.add(link);
		}
		
		return list;
	}
	
	/**
	 * @desc	: 프로젝트 Task 산출물 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 25.
	 * @method	: getOutputList
	 * @param	: task
	 * @return	: List<OutputData>
	 * @throws Exception 
	 */
	public List<ProjectOutputData> getTaskOutputList(ETaskNode task) throws Exception {
		List<ProjectOutputData> list = new ArrayList<>();
		
		QueryResult qr = PersistenceHelper.manager.navigate(task,"output",TaskOutputLink.class);
		while(qr.hasMoreElements()){
			EOutput output = (EOutput)qr.nextElement();
			ProjectOutputData data = new ProjectOutputData(output);
			list.add(data);
		}
		
		return list;
	}
	
	/**
	 * @desc	: 프로젝트 편집 유저 권한
	 * @author	: sangylee
	 * @date	: 2020. 9. 29.
	 * @method	: isAuth
	 * @param	: project
	 * @return	: boolean
	 * @throws Exception 
	 */
	public boolean isAuth(EProject project) throws Exception {
		boolean isAuth = false;
		
		WTUser user = (WTUser) SessionHelper.manager.getPrincipal();
		
		if((CommonUtil.isAdmin() || ProjectMemberHelper.manager.isPM(project, user)) && project.isLastVersion()){
			isAuth = true;
		}
		
		return isAuth;
	}
	
	/**
	 * @desc	: 프로젝트 태스크 유저 권한
	 * @author	: sangylee
	 * @date	: 2020. 10. 20.
	 * @method	: isAuth
	 * @param	: project
	 * @return	: boolean
	 * @throws Exception 
	 */
	public boolean isTaskAuth(ETask task) throws Exception {
		boolean isAuth = false;
		
		WTUser user = (WTUser) SessionHelper.manager.getPrincipal();
		
		EProject project = (EProject) task.getProject();
		
		if((CommonUtil.isAdmin() || ProjectMemberHelper.manager.isPM(project, user) || ProjectMemberHelper.manager.isTaskRoleMember(task, user))  && project.isLastVersion()){
			isAuth = true;
		}
		
		return isAuth;
	}
	
	/**
	 * @desc	: 프로젝트 중단 이력
	 * @author	: sangylee
	 * @date	: 2020. 9. 29.
	 * @method	: getProjectStopHistoryList
	 * @param	: project
	 * @return	: List<OutputData>
	 * @throws Exception 
	 */
	public List<ProjectStopHistoryData> getProjectStopHistoryList(EProject project) throws Exception {
		
		List<ProjectStopHistoryData> list = new ArrayList<>();
		
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(ProjectStopHistory.class,true);
		qs.appendWhere(new SearchCondition(ProjectStopHistory.class,"projectReference.key.id","=", CommonUtil.getOIDLongValue(project)),new int[]{ii});
		qs.appendOrderBy(new OrderBy(new ClassAttribute(ProjectStopHistory.class,"thePersistInfo.createStamp"),false),new int[]{ii});
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		while(qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			ProjectStopHistory history = (ProjectStopHistory) obj[0];
			
			ProjectStopHistoryData data = new ProjectStopHistoryData(history);
			
			list.add(data);
		}
		
		return list;
	}
	
	/**
	 * @desc	: 프로젝트 편집 상태 권한
	 * @author	: sangylee
	 * @date	: 2020. 9. 29.
	 * @method	: isEditState
	 * @param	: project
	 * @return	: boolean
	 * @throws Exception 
	 */
	public boolean isEditState(EProject project) throws Exception {
		boolean isEditState = false;
		
		String state = project.getLifeCycleState().toString();
		
		if(STATEKEY.READY.equals(state) || STATEKEY.MODIFY.equals(state)){
			isEditState = true;
		}
		
		return isEditState;
	}
	
	/**
	 * @desc	: 프로젝트 리스트 코드로 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 10. 6.
	 * @method	: getProjectListByCode
	 * @param	: projectCode
	 * @return	: List<ProjectData>
	 * @throws Exception 
	 */
	public List<ProjectData> getProjectListByCode(String projectCode) throws Exception {
		
		List<ProjectData> list = new ArrayList<>();
		QuerySpec query = ProjectQuery.manager.getProjectList(projectCode);

		QueryResult result = PersistenceHelper.manager.find((StatementSpec)query);
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			EProject pjt = (EProject) obj[0];
			
			ProjectData data = new ProjectData(pjt);
			
			list.add(data);
		}
		
		return list;
	}
	
	
	public void delayTaskMailSchedule() throws Exception {
		Timestamp delayStamp = DateUtil.convertDate(DateUtil.getDateString(DateUtil.getDelayTime(DateUtil.getToDayTimestamp(), -1), "d"));
		QuerySpec qs = new QuerySpec();
		
		int taskIdx = qs.addClassList(ETask.class, true);
		int pjtIdx = qs.addClassList(EProject.class, false);
		int taskRoleLinkIdx = qs.addClassList(TaskRoleLink.class, false);
		int roleIdx = qs.addClassList(ProjectRole.class, false);
		int roleUserLinkIdx = qs.addClassList(RoleUserLink.class, false);
		int userIdx = qs.addClassList(WTUser.class, true);
		
		SearchCondition sc = new SearchCondition(EProject.class, "lastVersion", SearchCondition.IS_TRUE);
		qs.appendWhere(sc, new int[] {pjtIdx});
		
		qs.appendAnd();
		sc = new SearchCondition(EProject.class, "state.state", SearchCondition.EQUAL, STATEKEY.PROGRESS);
		qs.appendWhere(sc, new int[] {pjtIdx});
		
		qs.appendAnd();
		sc = new SearchCondition(new ClassAttribute(EProject.class, EProject.PERSIST_INFO + ".theObjectIdentifier.id"), SearchCondition.EQUAL, 
				(new ClassAttribute(ETask.class, ETask.PROJECT_REFERENCE + ".key.id")));
		qs.appendWhere(sc, new int[] {pjtIdx, taskIdx});
		
		qs.appendAnd();
		sc = new SearchCondition(ETask.class, ETask.PLAN_END_DATE, SearchCondition.EQUAL, delayStamp);
		qs.appendWhere(sc, new int[] {taskIdx});
		
		qs.appendAnd();
		sc = new SearchCondition(new ClassAttribute(ETask.class, ETask.PERSIST_INFO + ".theObjectIdentifier.id"), SearchCondition.EQUAL, 
				new ClassAttribute(TaskRoleLink.class, TaskRoleLink.ROLE_BOBJECT_REF + ".key.id"));
		qs.appendWhere(sc, new int[] {taskIdx, taskRoleLinkIdx});
		
		qs.appendAnd();
		sc = new SearchCondition(new ClassAttribute(TaskRoleLink.class, TaskRoleLink.ROLE_AOBJECT_REF + ".key.id"), SearchCondition.EQUAL, 
				new ClassAttribute(ProjectRole.class, ProjectRole.PERSIST_INFO + ".theObjectIdentifier.id"));
		qs.appendWhere(sc, new int[] {taskRoleLinkIdx, roleIdx});
		
		qs.appendAnd();
		sc = new SearchCondition(new ClassAttribute(ProjectRole.class, ProjectRole.PERSIST_INFO + ".theObjectIdentifier.id"), SearchCondition.EQUAL, 
				new ClassAttribute(RoleUserLink.class, RoleUserLink.ROLE_BOBJECT_REF + ".key.id"));
		qs.appendWhere(sc, new int[] {roleIdx, roleUserLinkIdx});
		
		qs.appendAnd();
		sc = new SearchCondition(RoleUserLink.class, RoleUserLink.ROLE_AOBJECT_REF + ".key.id", WTUser.class, WTUser.PERSIST_INFO + ".theObjectIdentifier.id");
		qs.appendWhere(sc, new int[] {roleUserLinkIdx, userIdx});
		
		
		QueryResult qr = PersistenceHelper.manager.find((StatementSpec) qs);
		while(qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			ETask task = (ETask) obj[0];
			WTUser user = (WTUser) obj[1];
			Hashtable<String, Object> mailHash = ETaskMailForm.setTaskDelayMailInfo(task, user); // 지연 업무 메일 폼
			//Hashtable<String, Object> mailHash = ETaskMailForm.setTaskDeadlineMailInfo(task, user); // 마감일 도래 업무 메일폼
			if(mailHash.size() > 0 ) {
				mailHash.put(E3PSQueueHelper.queueName, E3PSQueueHelper.QueueName_Mail);
				mailHash.put(E3PSQueueHelper.className, E3PSQueueHelper.ClassName_Mail);
				mailHash.put(E3PSQueueHelper.methodName, E3PSQueueHelper.MethodName_Task_Delay_Mail);
				E3PSQueueHelper.manager.createQueue(mailHash);
			}
		}
		
		
	}
	
	/**
	 * @desc	: 프로젝트 Gantt Task List 가져오기(edit)
	 * @author	: sangylee
	 * @date	: 2020. 10. 22.
	 * @method	: getProjectGanttEditTask
	 * @param	: reqMap
	 * @return	: List<ProjectGanttEditTaskData>
	 */
	public List<ProjectGanttEditTaskData> getProjectGanttEditTask(Map<String, Object> reqMap) throws Exception {
		
		List<ProjectGanttEditTaskData> list = new ArrayList<>();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		EProject project = (EProject) CommonUtil.getObject(oid);
		
		ProjectGanttEditTaskData root = new ProjectGanttEditTaskData(project);
		
		getProjectGanttEditTask(root, list);
		
		list.add(root);
		
		return list;
	}
	
	/**
	 * @desc	: 프로젝트 Gantt Task List 가져오기(view)
	 * @author	: sangylee
	 * @date	: 2020. 11. 10.
	 * @method	: getProjectGanttViewTask
	 * @param	: reqMap
	 * @return	: List<ProjectGanttTaskData>
	 */
	public List<ProjectGanttViewTaskData> getProjectGanttViewTask(Map<String, Object> reqMap) throws Exception {
		
		List<ProjectGanttViewTaskData> list = new ArrayList<>();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		EProject project = (EProject) CommonUtil.getObject(oid);
		
		ProjectGanttViewTaskData root = new ProjectGanttViewTaskData(project);
		
		getProjectGanttViewTask(root, list);
		
		list.add(root);
		
		return list;
	}
	
	/**
	 * @desc	: 프로젝트 Gantt Task List 가져오기 재귀(edit)
	 * @author	: sangylee
	 * @date	: 2020. 10. 22.
	 * @method	: getProjectGanttEditTask
	 * @param	: parent
	 * @return	: void
	 */
	public void getProjectGanttEditTask(ProjectGanttEditTaskData data, List<ProjectGanttEditTaskData> list) throws Exception {

		List<ETask> childrenList = getProjectTaskChildren(data.getId());
		
		if(childrenList.size() > 0) {
			data.setType(ProjectGanttTaskData.PROJECT);
		} else {
			data.setType(ProjectGanttTaskData.TASK);
		}
		
		list.add(data);
		
		for(ETask task : childrenList) {
			ProjectGanttEditTaskData taskData = new ProjectGanttEditTaskData(task);
			
			List<ProjectRoleData> owners = ProjectMemberHelper.manager.getOwner(task);
			taskData.setTaskRole(owners);
			
			getProjectGanttEditTask(taskData, list);
		}
	}
	
	/**
	 * @desc	: 프로젝트 Gantt Task List 가져오기 재귀(view)
	 * @author	: sangylee
	 * @date	: 2020. 11. 10.
	 * @method	: getProjectGanttViewTask
	 * @param	: parent
	 * @return	: void
	 */
	public void getProjectGanttViewTask(ProjectGanttViewTaskData data, List<ProjectGanttViewTaskData> list) throws Exception {

		List<ETask> childrenList = getProjectTaskChildren(data.getId());
		
		if(childrenList.size() > 0) {
			data.setType(ProjectGanttTaskData.PROJECT);
		} else {
			data.setType(ProjectGanttTaskData.TASK);
		}
		
		list.add(data);
		
		for(ETask task : childrenList) {
			ProjectGanttViewTaskData taskData = new ProjectGanttViewTaskData(task);
			
			List<ProjectRoleData> owners = ProjectMemberHelper.manager.getOwner(task);
			taskData.setTaskRole(owners);
			
			getProjectGanttViewTask(taskData, list);
		}
	}
	
	/**
	 * @desc	: 프로젝트 Gantt Link List 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 10. 22.
	 * @method	: getProjectGanttLink
	 * @param	: reqMap
	 * @return	: List<TemplateTreeData>
	 */
	public List<ProjectGanttLinkData> getProjectGanttLink(Map<String, Object> reqMap) throws Exception {
		
		List<ProjectGanttLinkData> list = new ArrayList<>();

		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		EProject project = (EProject) CommonUtil.getObject(oid);
		
		List<PrePostLink> linkList = getProjectPrePostLinkList(project);
		for(PrePostLink link : linkList) {
			
			ProjectGanttLinkData linkData = new ProjectGanttLinkData(link);
			
			list.add(linkData);
		}
		
		return list;
	}
	
	/**
	 * @desc	: 프로젝트 트리 하위 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 10. 22.
	 * @method	: getProjectPrePostLinkList
	 * @param	: project
	 * @return	: List<ETask>
	 */
	public List<PrePostLink> getProjectPrePostLinkList(EProject project) throws Exception {
		
		List<PrePostLink> list = new ArrayList<>();
		
		QuerySpec query = new QuerySpec();
		
		int idx = query.addClassList(PrePostLink.class, true);
		int idx2 = query.addClassList(ETask.class, false);
		
		query.appendWhere(new SearchCondition(PrePostLink.class, PrePostLink.ROLE_AOBJECT_REF + ".key.id", ETask.class, ETask.PERSIST_INFO + ".theObjectIdentifier.id"), new int[]{idx , idx2});
		
		query.appendAnd();
		query.appendWhere(new SearchCondition(ETask.class, ETask.PROJECT_REFERENCE + ".key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(project)), new int[]{ idx2});
		
		QueryResult qr = PersistenceHelper.manager.find(query);
		
		while(qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			PrePostLink task = (PrePostLink) obj[0];
			
			list.add(task);
		}
		
		return list;
	}
	
	/**
	 * @desc	: 다운로드
	 * @author	: wcadmin
	 * @date	: 2020. 11. 16.
	 * @method	: getProjectMasteredLinkDataList
	 * @param	: project
	 * @return	: List<ETask>
	 */
	public List<ProjectMasteredLinkData> getProjectMasteredLinkDataList(Map<String, Object> reqMap) throws Exception {
		List<ProjectMasteredLinkData> list = new ArrayList<ProjectMasteredLinkData>();
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String type = StringUtil.checkNull((String) reqMap.get("type"));
		EProject project = (EProject)CommonUtil.getObject(oid);
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(EProjectMasteredLink.class,true);

		qs.appendWhere(new SearchCondition(EProjectMasteredLink.class, "linkType", SearchCondition.EQUAL, type), new int[]{ ii });
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(EProjectMasteredLink.class, "roleBObjectRef.key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(project)), new int[]{ ii });
		QueryResult qr = PersistenceHelper.manager.find((StatementSpec)qs);
		while(qr.hasMoreElements()){
			Object[] o = (Object[])qr.nextElement();
			EProjectMasteredLink link = (EProjectMasteredLink)o[0];
			
			ProjectMasteredLinkData data = new ProjectMasteredLinkData(link);
			
			list.add(data);
		}
		return list;
	}
	
	/**
	 * @desc	: 다운로드
	 * @author	: wcadmin
	 * @date	: 2020. 11. 16.
	 * @method	: getDownloadList
	 * @param	: project
	 * @return	: List<ETask>
	 */
	public List<Object[]> getDownloadList(Map<String, Object> reqMap) throws Exception {
		List<ProjectMasteredLinkData> list = ProjectHelper.manager.getProjectMasteredLinkDataList(reqMap);
		List<Object[]> downloadList = new ArrayList<Object[]>();
		Persistable per = null;
		WTPart part = null;
		EPMDocument epm = null;
		ContentHolder holder = null;
		if(list.size() > 0) {
			for(ProjectMasteredLinkData data : list) {
				per = CommonUtil.getObject(data.getOid());
				if(per != null) {
					if(per instanceof WTPart) {
						part = (WTPart) CommonUtil.getObject(data.getOid());
						epm = EpmHelper.service.getEPMDocument(part);
						if(epm != null){
							holder = epm;
							QueryResult result = ContentHelper.service.getContentsByRole(holder, ContentRoleType.PRIMARY);
							if (result.hasMoreElements()) {
								ContentItem item = (ContentItem) result.nextElement();
								if (item instanceof ApplicationData) {
									ApplicationData adata = (ApplicationData) item;
									String aoid = adata.getPersistInfo().getObjectIdentifier().getStringValue();
									ApplicationData appData = (ApplicationData) CommonUtil.getObject(aoid); 
									downloadList.add(new Object[]{holder, appData});
								}
							}
						}
						
					}else if(per instanceof EPMDocument) {
						epm = (EPMDocument) CommonUtil.getObject(data.getOid());
						holder = epm;
						QueryResult result = ContentHelper.service.getContentsByRole(holder, ContentRoleType.PRIMARY);
						if (result.hasMoreElements()) {
							ContentItem item = (ContentItem) result.nextElement();
							if (item instanceof ApplicationData) {
								ApplicationData adata = (ApplicationData) item;
								String aoid = adata.getPersistInfo().getObjectIdentifier().getStringValue();
								ApplicationData appData = (ApplicationData) CommonUtil.getObject(aoid); 
								downloadList.add(new Object[]{holder, appData});
							}
						}
					}
				}
			}
		}
		return downloadList;
	}
	
	@SuppressWarnings("deprecation")
	public HolderToContent getHolderToContent(ContentHolder contentHolder, ApplicationData ad)throws Exception{
		QuerySpec spec = new QuerySpec(ApplicationData.class, wt.content.HolderToContent.class);
		spec.appendWhere(new SearchCondition(ApplicationData.class, "thePersistInfo.theObjectIdentifier.id", "=",  
				ad.getPersistInfo().getObjectIdentifier().getId()));
		QueryResult queryresult = PersistenceHelper.manager.navigate(contentHolder, "theContentItem" , spec, false);
		HolderToContent holdertocontent = (HolderToContent)queryresult.nextElement();
		return holdertocontent;
	}
	
	/**
	 * @desc	: projectRole.Code
	 * @author	: wcadmin
	 * @date	: 2022. 12. 19.
	 * @method	: getProjectRoleByCode
	 * @param	: ProjectRole
	 * @return	: return null
	 */
	public ProjectRole getProjectRoleByCode(EProjectNode project, String roleCode) throws Exception{
		
		QuerySpec query = new QuerySpec();
		int idxPR = query.addClassList(ProjectRole.class, true);
		
		SearchCondition sc = null;
		
		if(query.getConditionCount() > 0) {
			query.appendAnd();
		}
		sc = new SearchCondition(ProjectRole.class, ProjectRole.CODE, 
				SearchCondition.EQUAL, 
				roleCode);
		query.appendWhere(sc, new int[]{idxPR});
		
		if(query.getConditionCount() > 0) {
			query.appendAnd();
		}
		sc = new SearchCondition(ProjectRole.class, ProjectRole.PROJECT_REFERENCE+".key.id", 
				SearchCondition.EQUAL, 
				CommonUtil.getOIDLongValue(project));
		query.appendWhere(sc, new int[]{idxPR});
		
		QueryResult result = PersistenceHelper.manager.find(query);
		
		while(result.hasMoreElements()) {
			Object[] obj = (Object[])result.nextElement();
			return (ProjectRole)obj[0];
		}
		return null;
	}

	/**
	 * @desc	: Project,Task List
	 * @author	: hgkang
	 * @date	: 2023. 02. 09.
	 * @method	: getProjectTaskList
	 * @param	: ETask
	 * @return	: return list
	 * @throws WTException 
	 */
	public List<ETask> getProjectTaskList(EProject project) throws WTException {
		List<ETask> list = new ArrayList<>();
		
		QuerySpec query = new QuerySpec();
		
		int taskIdx = query.addClassList(ETask.class, true);
		int pjtIdx = query.addClassList(EProject.class, false);
		
		SearchCondition sc = null;
		
		if(query.getConditionCount() > 0) {
			query.appendAnd();
		}
		sc = new SearchCondition(
			EProject.class, EProject.PERSIST_INFO + ".theObjectIdentifier.id", 
			SearchCondition.EQUAL,
			CommonUtil.getOIDLongValue(project));
		query.appendWhere(sc, new int[] {pjtIdx});
		
		if(query.getConditionCount() > 0) {
			query.appendAnd();
		}
		sc = new SearchCondition(
			new ClassAttribute(EProject.class, EProject.PERSIST_INFO + ".theObjectIdentifier.id"), SearchCondition.EQUAL, 
			new ClassAttribute(ETask.class, ETask.PROJECT_REFERENCE + ".key.id")
		);
		query.appendWhere(sc, new int[] {pjtIdx, taskIdx});
		
		
		QueryResult qr = PersistenceHelper.manager.find(query);
		
		while(qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			ETask ETask = (ETask) obj[0];
			
			list.add(ETask);
		}
		
		return list;
	}
	
	
}