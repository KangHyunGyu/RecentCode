package com.e3ps.project.service;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.e3ps.common.code.NumberCode;
import com.e3ps.common.code.bean.NumberCodeData;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.web.ParamUtil;
import com.e3ps.project.EOutput;
import com.e3ps.project.EProjectNode;
import com.e3ps.project.EProjectTemplate;
import com.e3ps.project.ETask;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.OutputTypeStep;
import com.e3ps.project.PrePostLink;
import com.e3ps.project.ProjectRole;
import com.e3ps.project.ScheduleNode;
import com.e3ps.project.TaskOutputLink;
import com.e3ps.project.TaskRoleLink;
import com.e3ps.project.beans.TemplateData;
import com.e3ps.project.beans.TemplateOutputData;
import com.e3ps.project.beans.TemplateTaskData;
import com.e3ps.project.beans.TemplateTreeData;
import com.e3ps.project.beans.TemplateTreeModel;
import com.e3ps.project.key.ProjectKey.FOLDERKEY;
import com.e3ps.project.key.ProjectKey.STATEKEY;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.introspection.ClassInfo;
import wt.introspection.WTIntrospector;
import wt.method.RemoteMethodServer;
import wt.pdmlink.PDMLinkProduct;
import wt.pds.DatabaseInfoUtilities;
import wt.pds.StatementSpec;
import wt.pom.Transaction;
import wt.query.ClassAttribute;
import wt.query.KeywordExpression;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.ServiceFactory;
import wt.session.SessionHelper;
import wt.util.WTException;

public class TemplateHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PROJECT.getName());
	public static final TemplateService service = ServiceFactory.getService(TemplateService.class);
	
	public static final TemplateHelper manager = new TemplateHelper();
	
	/**
	 * @desc	: 탬플릿 리스트 가져오기 (ProjectHelper에서 이동)
	 * @author	: sangylee
	 * @date	: 2020. 9. 18.
	 * @method	: getTemplateList
	 * @param	: reqMap
	 * @return	: List<TemplateData>
	 */
	public List<TemplateData> getTemplateList(Map<String, Object> reqMap) throws Exception {
		
		List<TemplateData> list = new ArrayList<>();
		QuerySpec query = getTemplateListQuery(reqMap);
		
		QueryResult result = PersistenceHelper.manager.find((StatementSpec)query);
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			EProjectTemplate pjt = (EProjectTemplate) obj[0];
			TemplateData data = new TemplateData(pjt);
			list.add(data);
		}
		
		return list;
	}

	/**
	 * @desc	: 탬플릿 리스트 가져오기 Query (ProjectQuery에서 이동)
	 * @author	: sangylee
	 * @date	: 2020. 9. 18.
	 * @method	: getTemplateList
	 * @param	: reqMap
	 * @return	: List<TemplateData>
	 */
	public QuerySpec getTemplateListQuery(Map<String, Object> map) throws WTException {
		
		String number = StringUtil.checkNull((String) map.get("number"));
		String name = StringUtil.checkNull((String) map.get("name"));
		
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(EProjectTemplate.class,true);
		int jj = qs.addClassList(PDMLinkProduct.class,false);
		
		qs.appendWhere(new SearchCondition(EProjectTemplate.class,"productReference.key.id",PDMLinkProduct.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{ii,jj});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(EProjectTemplate.class,"lastVersion",SearchCondition.IS_TRUE),new int[]{0});
		
		SearchCondition sc = null;
		if(number.length() > 0) {
			if(qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			
			sc = new SearchCondition(EProjectTemplate.class, EProjectTemplate.CODE, SearchCondition.LIKE, "%" + number + "%", false);
			qs.appendWhere(sc, new int[] { ii });
		}
		
		if(name.length() > 0) {
			if(qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			
			sc = new SearchCondition(EProjectTemplate.class, EProjectTemplate.NAME, SearchCondition.LIKE, "%" + name + "%", false);
			qs.appendWhere(sc, new int[] { ii });
		}

		qs.appendOrderBy(new OrderBy(new ClassAttribute(EProjectTemplate.class,"thePersistInfo.createStamp"),true),new int[]{0});
		return qs;
	}
	
	/**
	 * @desc	: 탬플릿 리스트 SelectBox
	 * @author	: sangylee
	 * @date	: 2020. 9. 22.
	 * @method	: getRelatedTemplate
	 * @param	: reqMap
	 * @return	: List<TemplateData>
	 */
	public List<TemplateData> getRelatedTemplate(Map<String, Object> reqMap) throws Exception {
		
		List<TemplateData> list = new ArrayList<>();

		String value = StringUtil.checkNull((String) reqMap.get("value"));
		
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(EProjectTemplate.class,true);
		int jj = qs.addClassList(PDMLinkProduct.class,false);
		
		qs.appendWhere(new SearchCondition(EProjectTemplate.class,"productReference.key.id",PDMLinkProduct.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{ii,jj});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(EProjectTemplate.class,"lastVersion",SearchCondition.IS_TRUE),new int[]{0});
		
		qs.appendAnd();
		qs.appendOpenParen();
		qs.appendWhere(new SearchCondition(EProjectTemplate.class, EProjectTemplate.CODE, SearchCondition.LIKE, "%"+value.toUpperCase()+"%", false), new int[]{ii});
		qs.appendOr();
		qs.appendWhere(new SearchCondition(EProjectTemplate.class, EProjectTemplate.NAME, SearchCondition.LIKE, "%"+value.toUpperCase()+"%", false), new int[]{ii});
		qs.appendCloseParen();
		
		qs.appendOrderBy(new OrderBy(new ClassAttribute(EProjectTemplate.class,EProjectTemplate.CODE),true),new int[]{0});
		
		QueryResult result = PersistenceHelper.manager.find((StatementSpec)qs);
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			EProjectTemplate pjt = (EProjectTemplate) obj[0];
			TemplateData data = new TemplateData(pjt);
			list.add(data);
		}
		
		return list;
	}
	
	/**
	 * @desc	: 탬플릿 트리 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 14.
	 * @method	: getTemplateTree
	 * @param	: reqMap
	 * @return	: List<TemplateTreeData>
	 */
	public List<TemplateTreeData> getTemplateTree(Map<String, Object> reqMap) throws Exception {
		
		List<TemplateTreeData> list = new ArrayList<>();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		EProjectTemplate template = (EProjectTemplate) CommonUtil.getObject(oid);
		
		TemplateTreeData root = new TemplateTreeData(template);
		
		root.setData(getTemplateChildren(root));
		
		list.add(root);
		
		return list;
	}
	
	/**
	 * @desc	: 탬플릿 트리 하위 가져오기 재귀
	 * @author	: sangylee
	 * @date	: 2020. 9. 14.
	 * @method	: getTemplateChildren
	 * @param	: parent
	 * @return	: List<TemplateTreeData>
	 */
	public List<TemplateTreeData> getTemplateChildren(TemplateTreeData parent) throws Exception {

		List<TemplateTreeData> list = new ArrayList<>();
		
		List<ETask> childrenList = getTemplateTaskChildren(parent.getOid());
		
		for(ETask task : childrenList) {
			TemplateTreeData data = new TemplateTreeData(task);
			
			data.setData(getTemplateChildren(data));
			
			list.add(data);
		}
		
		return list;
	}
	
	/**
	 * @desc	: 탬플릿 트리 Flat List 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 14.
	 * @method	: getTemplateChildrenFlatList
	 * @param	: reqMap
	 * @return	: List<TemplateTreeData>
	 */
	public List<TemplateTreeData> getTemplateChildrenFlatList(ScheduleNode node) throws Exception {
		
		List<TemplateTreeData> list = new ArrayList<>();
		
		String oid = CommonUtil.getOIDString(node);
		
		List<ETask> children = TemplateHelper.manager.getTemplateTaskChildren(oid);
		
		for(ETask child : children) {
			TemplateTreeData treeData = new TemplateTreeData(child);
			
			list.add(treeData);
			
			list.addAll(getTemplateChildrenFlatList(child));
		}
		
		return list;
	}
	
	/**
	 * @desc	: 탬플릿 트리 하위 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 14.
	 * @method	: getChildrenQuery
	 * @param	: parentOid
	 * @return	: List<ETask>
	 */
	public List<ETask> getTemplateTaskChildren(String parentOid) throws Exception {
		
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
	
	/**
	 * @desc	: 탬플릿 선행 태스크 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 14.
	 * @method	: getPreTaskList
	 * @param	: task
	 * @return	: List<TemplateTaskData>
	 * @throws Exception 
	 */
	public List<TemplateTaskData> getPreTaskList(ETaskNode task) throws Exception {
		List<TemplateTaskData> list = new ArrayList<>();
		
		List<PrePostLink> preLinkList = TemplateHelper.manager.getPreTaskLinkList(task);
		
		for(PrePostLink link : preLinkList) {
			ETaskNode preTask = link.getPre();
			TemplateTaskData data = new TemplateTaskData(preTask);
			
			list.add(data);
		}
		
		return list;
	}
	
	/**
	 * @desc	: 탬플릿 선행 태스크 링크 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 15.
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
	 * @desc	: 탬플릿 후행 태스크 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 14.
	 * @method	: getPostTaskList
	 * @param	: task
	 * @return	: List<TemplateTaskData>
	 * @throws Exception 
	 */
	public List<TemplateTaskData> getPostTaskList(ETaskNode task) throws Exception {
		List<TemplateTaskData> list = new ArrayList<>();
		
		List<PrePostLink> postLinkList = TemplateHelper.manager.getPostTaskLinkList(task);
		
		for(PrePostLink link : postLinkList) {
			ETaskNode postTask = link.getPost();
			TemplateTaskData data = new TemplateTaskData(postTask);
			
			list.add(data);
		}
		
		return list;
	}
	
	/**
	 * @desc	: 탬플릿 후행 태스크 링크 가져오기
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
	 * @desc	: 탬플릿 Task 산출물 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 14.
	 * @method	: getOutputList
	 * @param	: task
	 * @return	: List<OutputData>
	 */
	public List<TemplateOutputData> getTaskOutputList(ETaskNode task) throws WTException {
		List<TemplateOutputData> list = new ArrayList<>();
		
		QueryResult qr = PersistenceHelper.manager.navigate(task,"output",TaskOutputLink.class);
		while(qr.hasMoreElements()){
			EOutput output = (EOutput)qr.nextElement();
			TemplateOutputData data = new TemplateOutputData(output);
			list.add(data);
		}
		
		return list;
	}
	
	/**
	 * @desc	: 탬플릿 Task Role 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 14.
	 * @method	: getTaskRoleList
	 * @param	: task
	 * @return	: List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getTaskRoleList(ETaskNode task) throws WTException {
		List<Map<String, Object>> list = new ArrayList<>();
		
		List<TaskRoleLink> linkList = TemplateHelper.manager.getTaskRoleLinkList(task);
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
	 * @desc	: 탬플릿 Task Role Link 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 14.
	 * @method	: getTaskRoleList
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
	 * @desc	: 탬플릿 Task Role Link 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 17.
	 * @method	: getTaskRoleList
	 * @param	: role
	 * @return	: List<TaskRoleLink>
	 */
	public List<TaskRoleLink> getTaskRoleLinkList(ProjectRole role) throws WTException {
		List<TaskRoleLink> list = new ArrayList<>();
		
		QueryResult qr = PersistenceHelper.manager.navigate(role,TaskRoleLink.TASK_ROLE,TaskRoleLink.class, false);
		while(qr.hasMoreElements()){
			TaskRoleLink link = (TaskRoleLink)qr.nextElement();
			
			list.add(link);
		}
		
		return list;
	}
	
	/**
	 * @desc	: max seq (StandardTemplateService에서 Helper로 이동)
	 * @author	: sangylee
	 * @date	: 2020. 9. 17.
	 * @method	: getMaxSeq
	 * @param	: parent
	 * @return	: int
	 */
	public int getMaxSeq(ScheduleNode parent)throws Exception{

    	if (!wt.method.RemoteMethodServer.ServerFlag) {
			Class argTypes[] = new Class[]{ScheduleNode.class};
			Object args[] = new Object[]{parent};
			try {
				Object obj =RemoteMethodServer.getDefault().invoke("getMaxSeq", StandardTemplateService.class.getName() , null, argTypes, args);
				return ((Integer)obj).intValue();

			} catch (RemoteException e) {
				e.printStackTrace();
				throw new WTException(e);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new WTException(e);
			}
		}
        
    	int result = 1;
    	
    	Class taskClass = ETask.class;
 
    	ClassInfo classinfo = WTIntrospector.getClassInfo(taskClass);
    	
    	String task_seqColumnName = DatabaseInfoUtilities.getValidColumnName(classinfo, ScheduleNode.SORT);
    	
    	System.out.println("## task_seqColumnName NVL :   " + task_seqColumnName);
		
		QuerySpec spec = new QuerySpec();
		spec.setAdvancedQueryEnabled(true);
		spec.setDescendantQuery(false);
		spec.addClassList(taskClass, false);
    	KeywordExpression ke = new KeywordExpression("ISNULL(Max(" + task_seqColumnName +"), 0) ");
    	ke.setColumnAlias("task_seq");
    	spec.appendSelect(ke, new int[]{0}, false);
    	
    	
    	long id = 0;
    	
		if(parent != null){
			id = parent.getPersistInfo().getObjectIdentifier().getId();
		}
		
    	spec.appendWhere(new SearchCondition(taskClass, ScheduleNode.PARENT_REFERENCE + ".key.id", "=", id), new int[]{0});
    	
    	QueryResult rrrr = PersistenceHelper.manager.find(spec);
    	
    	if(rrrr.hasMoreElements()){
    		Object o[] =(Object[])rrrr.nextElement();
    		BigDecimal number = (BigDecimal)o[0];
    		result = number.intValue() + 1;
    	}
    	
    	return result;
    }
	
	/**
	 * @desc	: 탬플릿 트리 Next task list 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 17.
	 * @method	: getNextTaskList
	 * @param	: task
	 * @return	: List<ETask>
	 */
	public List<ETask> getNextTaskList(ETask task) throws Exception {

		List<ETask> list = new ArrayList<>();
		
		QuerySpec query = new QuerySpec();
		
		int idx = query.addClassList(ETask.class, true);
		
		query.appendWhere(new SearchCondition(ETask.class, ETask.SORT, SearchCondition.GREATER_THAN, task.getSort()), new int[] { idx });
		
		query.appendAnd();
		
		query.appendWhere(new SearchCondition(ETask.class, ETask.PARENT_REFERENCE + ".key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(task.getParent())), new int[] { idx });
		
		QueryResult qr = PersistenceHelper.manager.find(query);
		
		while (qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			ETask next = (ETask) obj[0];
			
			list.add(next);
		}
		
		return list;
	}
	
	/**
	 * @desc	: 탬플릿 트리 Next task 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 18.
	 * @method	: getNextTask
	 * @param	: task
	 * @return	: ETask
	 */
	public ETask getNextTask(ETask task) throws Exception {

		ETask nextTask = null;
		
		QuerySpec query = new QuerySpec();
		
		int idx = query.addClassList(ETask.class, true);
		
		query.appendWhere(new SearchCondition(ETask.class, ETask.SORT, SearchCondition.EQUAL, task.getSort() + 1), new int[] { idx });
		
		query.appendAnd();
		
		query.appendWhere(new SearchCondition(ETask.class, ETask.PARENT_REFERENCE + ".key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(task.getParent())), new int[] { idx });
		
		QueryResult qr = PersistenceHelper.manager.find(query);
		
		if (qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			nextTask = (ETask) obj[0];
		}
		
		return nextTask;
	}
	
	/**
	 * @desc	: 탬플릿 트리 Prev task 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 18.
	 * @method	: getPrevTask
	 * @param	: task
	 * @return	: ETask
	 */
	public ETask getPrevTask(ETask task) throws Exception {

		ETask prevTask = null;
		
		QuerySpec query = new QuerySpec();
		
		int idx = query.addClassList(ETask.class, true);
		
		query.appendWhere(new SearchCondition(ETask.class, ETask.SORT, SearchCondition.EQUAL, task.getSort() - 1), new int[] { idx });
		
		query.appendAnd();
		
		query.appendWhere(new SearchCondition(ETask.class, ETask.PARENT_REFERENCE + ".key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(task.getParent())), new int[] { idx });
		
		QueryResult qr = PersistenceHelper.manager.find(query);
		
		if (qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			prevTask = (ETask) obj[0];
		}
		
		return prevTask;
	}
	
	/**
	 * @desc	: ProjectRole 가져오기 (TemplateService에서 이동)
	 * @author	: sangylee
	 * @date	: 2020. 9. 18.
	 * @method	: getProjectRole
	 * @param	: project, roleCode
	 * @return	: ProjectRole<ETask>
	 */
	public ProjectRole getProjectRole(EProjectNode project, String roleCode) throws Exception {
		QuerySpec qs = new QuerySpec(ProjectRole.class);
		qs.appendWhere(new SearchCondition(ProjectRole.class, "projectReference.key.id", "=", project.getPersistInfo().getObjectIdentifier().getId()), new int[] { 0 });
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(ProjectRole.class, ProjectRole.CODE, "=", roleCode), new int[] { 0 });
		QueryResult qr = PersistenceHelper.manager.find(qs);

		ProjectRole rr = null;

		if (qr.hasMoreElements()) {
			rr = (ProjectRole) qr.nextElement();
		}
		return rr;
	}
	
	/**
	 * @desc	: Template ProjectRole 리스트 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 21.
	 * @method	: getProjectRoleList
	 * @param	: project
	 * @return	: List<ProjectRole>
	 */
	public List<ProjectRole> getProjectRoleList(EProjectNode project) throws Exception {
		
		List<ProjectRole> list = new ArrayList<>();
		
		QuerySpec qs = new QuerySpec(ProjectRole.class);
		qs.appendWhere(new SearchCondition(ProjectRole.class, "projectReference.key.id", "=",
				project.getPersistInfo().getObjectIdentifier().getId()), new int[] { 0 });
		
		QueryResult qr = PersistenceHelper.manager.find(qs);

		ProjectRole rr = null;

		while(qr.hasMoreElements()) {
			rr = (ProjectRole) qr.nextElement();
			
			list.add(rr);
		}
		
		return list;
	}
	
	/**
	 * @desc	: 템플릿 산출물 리스트 가져오기 (TemplateController 에서 이동 및 수정)
	 * @author	: sangylee
	 * @date	: 2020. 9. 21.
	 * @method	: getTemplateOutputList
	 * @param	: reqMap
	 * @return	: List<OutputData>
	 */
	public List<TemplateOutputData> getTemplateOutputList(Map<String, Object> reqMap) throws WTException {

		List<TemplateOutputData> list = new ArrayList<TemplateOutputData>();
		
		String oid = (String) reqMap.get("oid");
		
		ScheduleNode node = (ScheduleNode)CommonUtil.getObject(oid);
		if(node instanceof ETaskNode){
			node = ((ETaskNode)node).getProject();
		}
		EProjectNode project = (EProjectNode)node;
		
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(EOutput.class,true);
		int jj = qs.addClassList(ETaskNode.class,true);
		
		qs.appendWhere(new SearchCondition(ETaskNode.class,"thePersistInfo.theObjectIdentifier.id",EOutput.class,"taskReference.key.id"),new int[]{jj,ii});
		
		qs.appendAnd();
		
		qs.appendWhere(new SearchCondition(ETaskNode.class,"projectReference.key.id","=",CommonUtil.getOIDLongValue(project)),new int[]{jj});
		
		qs.appendOrderBy(new OrderBy(new ClassAttribute(EOutput.class,"thePersistInfo.createStamp"),false),new int[]{0});
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		while(qr.hasMoreElements()){
			Object[] o = (Object[])qr.nextElement();
			EOutput output = (EOutput)o[0];
			TemplateOutputData data = new TemplateOutputData(output);
			
			list.add(data);
		}
		
		return list;
	}
	
	/**
	 * @desc	: 탬플릿 트리 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 21.
	 * @method	: getTemplateLevelList
	 * @param	: node
	 * @return	: void
	 */
//	public void getTemplateLevelList(ScheduleNode node, List<ArrayList<ScheduleNode>> list) throws Exception {
//		
//		List<ETask> children = TemplateHelper.manager.getTemplateTaskChildren(CommonUtil.getOIDString(node));
//		
//		for(ETask task : children) {
//
//			ArrayList<ScheduleNode>
//			
//			
//			list.add(e)
//			TemplateTreeData data = new TemplateTreeData(task, parent.getOid());
//			
//			data.setData(getTemplateChildren(data));
//			
//			list.add(data);
//		}
//		
//		
//		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
//		
//		EProjectTemplate template = (EProjectTemplate) CommonUtil.getObject(oid);
//		
//		TemplateTreeData root = new TemplateTreeData(template);
//		
//		root.setData(getTemplateChildren(root));
//		
//		list.add(root);
//	}
	
	/**
	 * @desc	: POI 엑셀 임포트/업로드
	 * @author	: gs
	 * @date	: 2020. 12. 08.
	 * @method	: load
	 * @param	: node
	 * @return	: EProjectTemplate
	 */
	public EProjectTemplate load(String sFilePath) throws Exception {
		EProjectTemplate result = null;
		Transaction trx = null;
		XSSFWorkbook wb = null;
		FileInputStream fis = null;
		try {
			trx = new Transaction();
			trx.start();
			
			fis= new FileInputStream(sFilePath);
            wb = new XSSFWorkbook(fis);

			XSSFSheet sheet = wb.getSheetAt(0);
			XSSFRow curRow;
			XSSFCell curCell;
			int rowIndex = sheet.getPhysicalNumberOfRows();
			
			curRow = sheet.getRow(1);
			String templateName = checkCell(curRow.getCell(0));
			String outputType = checkCell(curRow.getCell(3));
			String enabled = checkCell(curRow.getCell(4));
			String description = checkCell(curRow.getCell(5));
			
			Map<String, Object> templateMap = new HashMap<>();
			templateMap.put("name", templateName);
			templateMap.put("outputType", outputType);
			templateMap.put("enabled", Boolean.toString("O".equals(enabled)));
			templateMap.put("description", description);
			
			EProjectTemplate template = TemplateHelper.service.save(templateMap);
			
			Map<String, Object> parents = new HashMap<>();
			parents.put("0", template);
			
			Map<String, Object> seqMap = new HashMap<>();
			Map<String, Object> temp = new HashMap<>();
			curRow = sheet.getRow(2);
			if(checkLine(curRow.getCell(4))) {
				System.out.println("checkCell(curRow.getCell(4)) -> "+checkCell(curRow.getCell(4)));
				if("4Level".equals(checkCell(curRow.getCell(4)))) {
					for(int i=3; i<rowIndex; i++) {
						
						curRow = sheet.getRow(i);
		                if(curRow != null) {
		                	if(checkLine(curRow.getCell(1)) || checkLine(curRow.getCell(2)) || checkLine(curRow.getCell(3))) {
		                		ETask task = null;
		    					String taskName = null;
		    					int depth = 5;
		    					int parentDepth = 0;
		    					for(int j=1; j<depth; j++) {
		    						curCell = curRow.getCell(j);
		    						if(!checkLine(curCell)) {
		    							continue;
		    						}
		    						if(curCell != null || curCell.getCellType() != CellType.BLANK) {
		    							taskName = checkCell(curRow.getCell(j));
		    							
		    							ScheduleNode parent = (ScheduleNode) parents.get(Integer.toString(j-1));
		    							String poid = parent.getPersistInfo().getObjectIdentifier().toString();
		    							
		    							Integer childCount = (Integer) seqMap.get(poid);
		    							if(childCount == null) {
		    								childCount = new Integer(1);
		    							}else {
		    								childCount = new Integer(childCount.intValue() + 1);
		    							}
		    							seqMap.put(poid, childCount);
		    							
		    							Map<String, Object> activityMap = new HashMap<>();
		    							activityMap.put("oid", parent.getPersistInfo().getObjectIdentifier().toString());
		    							activityMap.put("name", taskName);
		    							activityMap.put("sort", childCount.toString());
		    							
		    							task = createTask(activityMap);

		    							parentDepth = j;
		    							parents.put(Integer.toString(parentDepth), task);
		    						}
		    					}
		    					String taskDescription = checkCell(curRow.getCell(5));
		    					String duration = checkCell(curRow.getCell(6));
		    					String taskCode = checkCell(curRow.getCell(7));
		    					String preTask = checkCell(curRow.getCell(8));
		    					String outputNames = checkCell(curRow.getCell(9));
		    					String outputPaths = checkCell(curRow.getCell(10));
		    					String tOutputType = checkCell(curRow.getCell(11));
		    					String outputStep = checkCell(curRow.getCell(12));
		    					String roles = checkCell(curRow.getCell(13));
		    					String taskOid = task.getPersistInfo().getObjectIdentifier().toString();
		    					
		    					Map<String, Object> taskMap = new HashMap<>();
		    					taskMap.put("oid", taskOid);
		    					taskMap.put("taskName", taskName);
		    					taskMap.put("description", taskDescription);
		    					taskMap.put("manDay", duration);
		    					
		    					task = updateTask(taskMap);
		    					
		    					// 선행 태스크 Set
		    					temp.put(taskCode, task);
		    					if(preTask != null && preTask.length() > 0) {
		    						Map<String, Object> preMap = new HashMap<>();
		    						List<Map<String, Object>> items = new ArrayList<>();
		    						for(String pt : preTask.split(",")) {
		    							ETask pre = (ETask) temp.get(pt.trim());
		    							Map<String, Object> item = new HashMap<>();
		    							if(pre != null) {
		    								item.put("oid", CommonUtil.getOIDString(pre));
		    								items.add(item);
		    							}
		    						}
		    						preMap.put("oid", taskOid);
		    						preMap.put("items", items);
		    						setPreTask(preMap);
		    					}
		    					
		    					// 산출물 Set
		    					if(outputNames != null && outputNames.length() > 0) {
		    						String[] outputName = outputNames.split("\\|\\|");
		    						String[] outputPath = outputPaths.split("\\|\\|");
		    						OutputTypeStep ots = OutputTypeHelper.manager.getOutputTypeStep("PSO", outputStep);
		    						String outputTypeOid = "";
		    						if(ots != null) {
		    							outputTypeOid = CommonUtil.getOIDString(ots);
		    						}
		    						for(int o=0; o<outputName.length; o++) {
		    							Map<String, Object> outputMap = new HashMap<>();
		    							outputMap.put("oid", taskOid);
		    							outputMap.put("name", outputName[o].trim());
		    							outputMap.put("outputType", tOutputType);
		    							outputMap.put("outputStep", outputTypeOid);
		    							outputMap.put("location", FOLDERKEY.DOCUMENT + outputPath[o].trim());
		    							OutputHelper.service.saveOutput(outputMap);
		    						}
		    					}
		    					
		    					// Role Set
		    					if(roles != null && roles.length() > 0) {
		    						Map<String, Object> roleMap = new HashMap<>();
		    						List<Map<String, Object>> items = new ArrayList<>();
		    						for(String role : roles.split(",")) {
		    							Map<String, Object> item = null;
		    							NumberCode nc = null;
		    							if("CFT".equals(role)) {
		    								for(NumberCodeData data : CodeHelper.manager.getNumberCodeList("PROJECTROLE")) {
		    									item = new HashMap<>();
		    									item.put("key", data.getCode());
		    									item.put("value", data.getName());
		    									items.add(item);
		    								}
		    							}else {
		    								item = new HashMap<>();
		    								nc = CodeHelper.manager.getNumberCodeByName("PROJECTROLE", role.trim());
		    								if(nc != null) {
		    									item.put("key", nc.getCode());
		    									item.put("value", nc.getName());
		    								}
		    								items.add(item);
		    							}
		    							
		    						}
		    						roleMap.put("oid", taskOid);
		    						roleMap.put("items", items);
		    						TemplateHelper.service.editRole(roleMap);
		    					}
		                	}
						}
					}
				}else {
					for(int i=3; i<rowIndex; i++) {
						
						curRow = sheet.getRow(i);
		                if(curRow != null) {
		                	if(checkLine(curRow.getCell(1)) || checkLine(curRow.getCell(2)) || checkLine(curRow.getCell(3))) {
		                		ETask task = null;
		    					String taskName = null;
		    					int depth = 4;
		    					int parentDepth = 0;
		    					for(int j=1; j<depth; j++) {
		    						curCell = curRow.getCell(j);
		    						if(!checkLine(curCell)) {
		    							continue;
		    						}
		    						if(curCell != null || curCell.getCellType() != CellType.BLANK) {
		    							taskName = checkCell(curRow.getCell(j));
		    							
		    							ScheduleNode parent = (ScheduleNode) parents.get(Integer.toString(j-1));
		    							String poid = parent.getPersistInfo().getObjectIdentifier().toString();
		    							
		    							Integer childCount = (Integer) seqMap.get(poid);
		    							if(childCount == null) {
		    								childCount = new Integer(1);
		    							}else {
		    								childCount = new Integer(childCount.intValue() + 1);
		    							}
		    							seqMap.put(poid, childCount);
		    							
		    							Map<String, Object> activityMap = new HashMap<>();
		    							activityMap.put("oid", parent.getPersistInfo().getObjectIdentifier().toString());
		    							activityMap.put("name", taskName);
		    							activityMap.put("sort", childCount.toString());
		    							
		    							task = createTask(activityMap);

		    							parentDepth = j;
		    							parents.put(Integer.toString(parentDepth), task);
		    						}
		    					}
		    					String taskDescription = checkCell(curRow.getCell(4));
		    					String duration = checkCell(curRow.getCell(5));
		    					String taskCode = checkCell(curRow.getCell(6));
		    					String preTask = checkCell(curRow.getCell(7));
		    					String outputNames = checkCell(curRow.getCell(8));
		    					String outputPaths = checkCell(curRow.getCell(9));
		    					String tOutputType = checkCell(curRow.getCell(10));
		    					String outputStep = checkCell(curRow.getCell(11));
		    					String roles = checkCell(curRow.getCell(12));
		    					String taskOid = task.getPersistInfo().getObjectIdentifier().toString();
		    					
		    					Map<String, Object> taskMap = new HashMap<>();
		    					taskMap.put("oid", taskOid);
		    					taskMap.put("taskName", taskName);
		    					taskMap.put("description", taskDescription);
		    					taskMap.put("manDay", duration);
		    					
		    					task = updateTask(taskMap);
		    					
		    					// 선행 태스크 Set
		    					temp.put(taskCode, task);
		    					if(preTask != null && preTask.length() > 0) {
		    						Map<String, Object> preMap = new HashMap<>();
		    						List<Map<String, Object>> items = new ArrayList<>();
		    						for(String pt : preTask.split(",")) {
		    							ETask pre = (ETask) temp.get(pt.trim());
		    							Map<String, Object> item = new HashMap<>();
		    							if(pre != null) {
		    								item.put("oid", CommonUtil.getOIDString(pre));
		    								items.add(item);
		    							}
		    						}
		    						preMap.put("oid", taskOid);
		    						preMap.put("items", items);
		    						setPreTask(preMap);
		    					}
		    					
		    					// 산출물 Set
		    					if(outputNames != null && outputNames.length() > 0) {
		    						String[] outputName = outputNames.split("\\|\\|");
		    						String[] outputPath = outputPaths.split("\\|\\|");
		    						OutputTypeStep ots = OutputTypeHelper.manager.getOutputTypeStep("PSO", outputStep);
		    						String outputTypeOid = "";
		    						if(ots != null) {
		    							outputTypeOid = CommonUtil.getOIDString(ots);
		    						}
		    						for(int o=0; o<outputName.length; o++) {
		    							Map<String, Object> outputMap = new HashMap<>();
		    							outputMap.put("oid", taskOid);
		    							outputMap.put("name", outputName[o].trim());
		    							outputMap.put("outputType", tOutputType);
		    							outputMap.put("outputStep", outputTypeOid);
		    							outputMap.put("location", FOLDERKEY.DOCUMENT + outputPath[o].trim());
		    							OutputHelper.service.saveOutput(outputMap);
		    						}
		    					}
		    					
		    					// Role Set
		    					if(roles != null && roles.length() > 0) {
		    						Map<String, Object> roleMap = new HashMap<>();
		    						List<Map<String, Object>> items = new ArrayList<>();
		    						for(String role : roles.split(",")) {
		    							Map<String, Object> item = null;
		    							NumberCode nc = null;
		    							if("CFT".equals(role)) {
		    								for(NumberCodeData data : CodeHelper.manager.getNumberCodeList("PROJECTROLE")) {
		    									item = new HashMap<>();
		    									item.put("key", data.getCode());
		    									item.put("value", data.getName());
		    									items.add(item);
		    								}
		    							}else {
		    								item = new HashMap<>();
		    								nc = CodeHelper.manager.getNumberCodeByName("PROJECTROLE", role.trim());
		    								if(nc != null) {
		    									item.put("key", nc.getCode());
		    									item.put("value", nc.getName());
		    								}
		    								items.add(item);
		    							}
		    							
		    						}
		    						roleMap.put("oid", taskOid);
		    						roleMap.put("items", items);
		    						TemplateHelper.service.editRole(roleMap);
		    					}
		                	}
						}
					}
				}
			}
			
			
			template = (EProjectTemplate) PersistenceHelper.manager.refresh(template);
			result = template;
			TemplateTreeModel model = new TemplateTreeModel(template);
			model.setSchedule();
			
			
			trx.commit();
		}catch(Exception e) {
			trx.rollback();
			e.printStackTrace();
		}finally {
			if(trx!=null) {
				trx.rollback();
				trx = null;
			}
		}
		return result;
	}
	
	/**
	 * @desc	: WBS 엑셀 Import /Export 기능 
	 * @author	: gs
	 * @date	: 2021. 05. 24.
	 * @method	: load2
	 * @param	: node
	 * @return	: EProjectTemplate
	 */
	public EProjectTemplate load2(String sFilePath) throws Exception {
		EProjectTemplate result = null;
		Transaction trx = null;
		XSSFWorkbook wb = null;
		FileInputStream fis = null;
		try {
			trx = new Transaction();
			trx.start();
			
			fis= new FileInputStream(sFilePath);
            wb = new XSSFWorkbook(fis);

			XSSFSheet sheet = wb.getSheetAt(0);
			XSSFRow curRow;
			XSSFCell curCell;
			int rowIndex = sheet.getPhysicalNumberOfRows();
			
			curRow = sheet.getRow(1);
			String templateName = checkCell(curRow.getCell(1));
			String outputType = checkCell(curRow.getCell(12));
			String description = "";
			
			if(checkLine(curRow.getCell(12))) {
				Map<String, Object> templateMap = new HashMap<>();
				templateMap.put("name", templateName);
				templateMap.put("outputType", outputType);
				templateMap.put("enabled", true);
				templateMap.put("description", description);
				
				EProjectTemplate template = TemplateHelper.service.save(templateMap);
				
				Map<String, Object> parents = new HashMap<>();
				parents.put("1", template);
				
				Map<String, Object> seqMap = new HashMap<>();
				Map<String, Object> temp = new HashMap<>();
				
	    						
				for(int i=2; i<rowIndex; i++) {
					curRow = sheet.getRow(i);
	                if(curRow != null) {
	                	if(checkLine(curRow.getCell(0))) {
							
							String wbs = checkCell(curRow.getCell(0));
							String presentTaskLevel = "";
							int level = 0;
							if(wbs != null && wbs.trim().length()>0){
					        	StringTokenizer tokens = new StringTokenizer(wbs, ".");
					        	level = tokens.countTokens();
					        	presentTaskLevel = Integer.toString(tokens.countTokens());
					        }
							
							ETask task = null;
							String taskName = null;
							
							curCell = curRow.getCell(1);
							if(!checkLine(curCell)) {
								continue;
							}
							
							taskName = checkCell(curRow.getCell(1));
							
							ScheduleNode parent = (ScheduleNode) parents.get(Integer.toString(level-1));
							
							Map<String, Object> activityMap = new HashMap<>();
							activityMap.put("oid", parent.getPersistInfo().getObjectIdentifier().toString());
							activityMap.put("name", taskName);
							activityMap.put("sort", Integer.toString(i));
							
							task = createTask(activityMap);
							
				        	parents.put(presentTaskLevel, task);
		
							
				        	
							String duration = checkCell(curRow.getCell(7));
							String roles = checkCell(curRow.getCell(8));
							//String taskCode = checkCell(curRow.getCell(10));
							String preTask = checkCell(curRow.getCell(9));
							String outputNames = checkCell(curRow.getCell(10));
							String outputPaths = checkCell(curRow.getCell(11));
							String outputTypes = checkCell(curRow.getCell(12));
							String outputSteps = checkCell(curRow.getCell(13));
							
							String taskOid = task.getPersistInfo().getObjectIdentifier().toString();
							
							Map<String, Object> taskMap = new HashMap<>();
							taskMap.put("oid", taskOid);
							taskMap.put("taskName", taskName);
							taskMap.put("description", "");
							taskMap.put("manDay", duration);
							
							task = updateTask(taskMap);
							
							// 선행 태스크 Set
							temp.put(taskName, task);
							if(preTask != null && preTask.length() > 0) {
								preTask = preTask.trim();
								Map<String, Object> preMap = new HashMap<>();
								List<Map<String, Object>> items = new ArrayList<>();
								for(String pt : preTask.split("&")) {
									ETask pre = (ETask) temp.get(pt.trim());
									Map<String, Object> item = new HashMap<>();
									if(pre != null) {
										item.put("oid", CommonUtil.getOIDString(pre));
										items.add(item);
									}
								}
								preMap.put("oid", taskOid);
								preMap.put("items", items);
								setPreTask(preMap);
							}
							
							// 산출물 Set
							if(outputNames != null && outputNames.length() > 0) {
								outputNames = outputNames.trim();
								outputPaths = outputPaths.trim();
								outputTypes = outputTypes.trim();
								
								String[] outputName = outputNames.split("&");
	    						String[] outputPath = outputPaths.split(",");
	    						String[] outputTypess = outputTypes.split(",");
	    						String[] outputStep = outputSteps.split(",");
								
								for(int o=0; o<outputName.length; o++) {
									OutputTypeStep ots = OutputTypeHelper.manager.getOutputTypeStep("PSO", outputStep[o].trim());
									String outputTypeOid = "";
									if(ots != null) {
										outputTypeOid = CommonUtil.getOIDString(ots);
									}
									
									Map<String, Object> outputMap = new HashMap<>();
									outputMap.put("oid", taskOid);
									outputMap.put("name", outputName[o].trim());
									outputMap.put("outputType", outputTypess[o].trim());
									outputMap.put("outputStep", outputTypeOid);
									outputMap.put("location", FOLDERKEY.DOCUMENT + outputPath[o].trim());
									OutputHelper.service.saveOutput(outputMap);
								}
							}
							
							// Role Set
							if(roles != null && roles.length() > 0) {
								Map<String, Object> roleMap = new HashMap<>();
								List<Map<String, Object>> items = new ArrayList<>();
								for(String role : roles.split(",")) {
									role = role.trim();
									//System.out.println("role -> "+role.split("\\(")[0]);
									Map<String, Object> item = null;
									NumberCode nc = null;
									if("CFT".equals(role.split("\\(")[0])) {
										for(NumberCodeData data : CodeHelper.manager.getNumberCodeList("PROJECTROLE")) {
											item = new HashMap<>();
											item.put("key", data.getCode());
											item.put("value", data.getName());
											items.add(item);
										}
									}else {
										item = new HashMap<>();
										nc = CodeHelper.manager.getNumberCodeByName("PROJECTROLE", role.split("\\(")[0]);
										if(nc != null) {
											item.put("key", nc.getCode());
											item.put("value", nc.getName());
										}
										items.add(item);
									}
									
									
								}
								roleMap.put("oid", taskOid);
								roleMap.put("items", items);
								TemplateHelper.service.editRole(roleMap);
							}
	                	}
	                }
				}
				
				template = (EProjectTemplate) PersistenceHelper.manager.refresh(template);
				result = template;
				TemplateTreeModel model = new TemplateTreeModel(template);
				model.setSchedule();
			}
			
			
			
			
			trx.commit();
		}catch(Exception e) {
			trx.rollback();
			e.printStackTrace();
		}finally {
			if(trx!=null) {
				trx.rollback();
				trx = null;
			}
		}
		return result;
	}
	
	/**
	 * @desc	: WBS 엑셀 Import /Export 기능 
	 * @author	: phko
	 * @date	: 2022. 12. 06.
	 * @method	: DncTemplateLoad
	 * @param	: node
	 * @return	: EProjectTemplate
	 */
	public EProjectTemplate TemplateLoad(String sFilePath) throws Exception {
		EProjectTemplate result = null;
		try {
			File newfile = new File(sFilePath);
			Workbook wb = Workbook.getWorkbook(newfile);

			for (int sheet_i = 0; sheet_i < wb.getNumberOfSheets(); sheet_i++) {

				Sheet sheet = wb.getSheet(sheet_i);

				int rows = sheet.getRows();

				Cell[] cell = sheet.getRow(1);
				String templateName = getContent(cell, 0).trim();
				String enabled = getContent(cell, 3).trim();
				String description = getContent(cell, 4).trim();

				Map<String, Object> templateMap = new HashMap<>();
				templateMap.put("name", templateName);
				templateMap.put("outputType", "GENERAL");
				templateMap.put("enabled", Boolean.toString("O".equals(enabled)));
				templateMap.put("description", description);
				EProjectTemplate template = TemplateHelper.service.save(templateMap);

				Map<String, Object> parents = new HashMap<>();
				parents.put("0", template);

				Map<String, Object> seqMap = new HashMap<String, Object>();
				Map<String, Object> temp = new HashMap<String, Object>();
				List<String> tempTeskOid = new ArrayList<String>();
				Map<String, String> tempPreTesk = new HashMap<String, String>();

				for (int i = 3; i < rows; i++) {
					if (checkLine(sheet.getRow(i), 0) || checkLine(sheet.getRow(i), 1) || checkLine(sheet.getRow(i), 2)) {
						cell = sheet.getRow(i);

						ETask task = null;
						String taskName = null;
						int depth = 3;
						int parentDepth = 0;

						/* 1. Tesk Level */
						for (int j = 0; j < depth; j++) {
							if (!checkLine(sheet.getRow(i), j)) {
								continue;
							}
							taskName = getContent(cell, j).trim();

							ScheduleNode parent = (ScheduleNode) parents.get(Integer.toString(j));
							String poid = parent.getPersistInfo().getObjectIdentifier().toString();

							Integer childCount = (Integer) seqMap.get(poid);
							if (childCount == null)
								childCount = new Integer(1);
							else
								childCount = new Integer(childCount.intValue() + 1);

							seqMap.put(poid, childCount);

							Map<String, Object> activityMap = new HashMap<>();
							activityMap.put("oid", parent.getPersistInfo().getObjectIdentifier().toString());
							activityMap.put("name", taskName);
							activityMap.put("sort", childCount.toString());

							task = createTask(activityMap);

							parentDepth = j + 1;
							parents.put(Integer.toString(parentDepth), task);
						}

						/* 2. Task 기본정보 저장 */
						String taskDescription = getContent(cell, 3).trim();// 상세설명
						String duration = getContent(cell, 4).trim();// 기간
						String preTask = getContent(cell, 5).trim();// 선행 태스크
						String outputNames = getContent(cell, 6).trim();// 산출물
						String outputPaths = getContent(cell, 7).trim();// 산출물 경로
						String roles = getContent(cell, 9).trim();// Role

						String taskOid = task.getPersistInfo().getObjectIdentifier().toString();

						Map<String, Object> taskMap = new HashMap<>();
						taskMap.put("oid", taskOid);
						taskMap.put("taskName", taskName);
						taskMap.put("description", taskDescription);
						taskMap.put("manDay", duration);

						task = updateTask(taskMap);

						/* 3. 선행 태스크 Set */
						temp.put(taskName, task);
						if (preTask != null && preTask.length() > 0) {
							String tt = savePreTask(taskOid, preTask, temp);
							if (tt.length() > 0) {
								tempTeskOid.add(taskOid);
								tempPreTesk.put(taskOid, preTask);
							}
						}

						/* 4. 산출물 Set */
						if (outputNames != null && outputNames.length() > 0) {
							String[] outputName = outputNames.split("\\|\\|");
							String[] outputPath = outputPaths.split("\\|\\|");
							for (int o = 0; o < outputName.length; o++) {
								Map<String, Object> outputMap = new HashMap<>();
								outputMap.put("oid", taskOid);
								outputMap.put("name", outputName[o].trim());
								outputMap.put("location", FOLDERKEY.DOCUMENT + "/" + outputPath[o].trim());
								OutputHelper.service.saveOutput(outputMap);
							}
						}

						/* 5. Role Set */
						if (roles != null && roles.length() > 0) {
							Map<String, Object> roleMap = new HashMap<>();
							List<Map<String, Object>> items = new ArrayList<>();
							for (String role : roles.split(",")) {
								Map<String, Object> item = null;
								NumberCode nc = null;

								item = new HashMap<>();
								nc = CodeHelper.manager.getNumberCode("PROJECTROLE", role.trim());
								if (nc != null) {
									item.put("key", nc.getCode());
									item.put("value", nc.getName());
								}
								items.add(item);
							}
							roleMap.put("oid", taskOid);
							roleMap.put("items", items);
							TemplateHelper.service.editRole(roleMap);
						}
					}
				}

				for (String oid : tempTeskOid) {
					String preTask = tempPreTesk.get(oid);
					savePreTask(oid, preTask, temp);
				}

				template = (EProjectTemplate) PersistenceHelper.manager.refresh(template);
				result = template;
				TemplateTreeModel model = new TemplateTreeModel(template);
				model.setSchedule();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	public String savePreTask(String taskOid,String preTask, Map<String,Object> temp) throws Exception {
		
		String result="";
		Map<String, Object> preMap = new HashMap<>();
		List<Map<String, Object>> items = new ArrayList<>();
		for(String pt : preTask.split(",")) {
			ETask pre = (ETask) temp.get(pt.trim());
			Map<String, Object> item = new HashMap<>();
			
			if(pre != null) {
				item.put("oid", CommonUtil.getOIDString(pre));
				items.add(item);
			}else {
				items = null;
				break;
			}
		}
		
		if(items != null) {
			preMap.put("oid", taskOid);
			preMap.put("items", items);
			setPreTask(preMap);
		}else {
			result=taskOid;
		}
		return result;
	}
	
	public boolean checkLine(Cell[] cell, int line) throws Exception {
		String value = null;
		try {
			value = cell[line].getContents().trim();
		} catch (Exception e) {
//			e.printStackTrace();
			return false;
		}
		if (value == null || value.length() == 0)
			return false;
		return true;
	}
	
	public static String getContent(Cell[] cell, int idx) throws Exception {
		try {
			String val = cell[idx].getContents();
			if (val == null)
				return "";
			return val.trim();
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return "";
	}
	
	public ETask createTask(Map<String, Object> reqMap) throws Exception {
		
		ETask newTask = null;

		try {

			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String name = StringUtil.checkNull((String) reqMap.get("name"));
			String description = StringUtil.checkNull((String) reqMap.get("description"));
			String sort = StringUtil.checkNull((String) reqMap.get("sort"));
			double manDay = ParamUtil.getDouble(reqMap, "manDay");

			ScheduleNode parent = (ScheduleNode) CommonUtil.getObject(oid);

			newTask = ETask.newETask();

			newTask.setDescription(description);

			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(DateUtil.getTimestampFormat("20000101", "yyyyMMdd").getTime());
			Timestamp start = new Timestamp(ca.getTime().getTime());

			BigDecimal bd = new BigDecimal(manDay);
			bd = bd.setScale(0, BigDecimal.ROUND_UP);

			ca.add(Calendar.DATE, bd.intValue() - 1);
			Timestamp end = new Timestamp(ca.getTime().getTime());

			newTask.setPlanStartDate(start);
			newTask.setPlanEndDate(end);
			newTask.setCreator(SessionHelper.manager.getPrincipalReference());
			newTask.setName(name);
			newTask.setParent(parent);
			newTask.setSort(Integer.parseInt(sort));
			newTask.setManDay(manDay);
			newTask.setStatus(STATEKEY.READY);

			if (parent instanceof EProjectNode) {
				newTask.setProject((EProjectNode) parent);
			} else {
				newTask.setProject(((ETaskNode) parent).getProject());
			}

			newTask = (ETask) PersistenceHelper.manager.save(newTask);

		} catch (Exception e) {
			e.printStackTrace();
			throw new WTException(e);
		}
		
		return newTask;
	}
	
	public void setPreTask(Map<String, Object> reqMap) throws Exception {
		
		try {

			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			List<Map<String, Object>> items = (List<Map<String, Object>>) reqMap.get("items");

			ETask task = (ETask) CommonUtil.getObject(oid);
			List<PrePostLink> preTaskLinkList = TemplateHelper.manager.getPreTaskLinkList(task);

			if(preTaskLinkList.size() > 0) {
				for (PrePostLink preTaskLink : preTaskLinkList) {
					PersistenceHelper.manager.delete(preTaskLink);
				}
			} else {
				for (Map<String, Object> item : items) {
					String preTaskOid = (String) item.get("oid");
	
					ETask preTask = (ETask) CommonUtil.getObject(preTaskOid);
	
					PrePostLink link = PrePostLink.newPrePostLink(preTask, task);
					PersistenceHelper.manager.save(link);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new WTException(e);
		}
	}
	
	public ETask updateTask(Map<String, Object> hash) throws Exception {
		
		ETask task = null;

		try {
			String oid = ParamUtil.get(hash, "oid");
			String taskName = ParamUtil.get(hash, "taskName");
			String description = StringUtil.checkNull(ParamUtil.get(hash, "description"));
			double manDay = ParamUtil.getDouble(hash, "manDay");

			task = (ETask) CommonUtil.getObject(oid);

			task.setName(taskName);
			task.setManDay(manDay);
			task.setDescription(description);

			task = (ETask) PersistenceHelper.manager.save(task);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return task;
	}
	
	/**
	 * @desc	: POI 엑셀 cell 유형 체크
	 * @author	: gs
	 * @date	: 2020. 12. 08.
	 * @method	: checkCell
	 * @return	: String
	 */
	@SuppressWarnings("deprecation")
	public String checkCell(XSSFCell cell) {
		String value = "";
		if(cell != null) {
			switch (cell.getCellType()) {
            case FORMULA:
            	value = cell.getCellFormula();
                break;
            case NUMERIC:
            	value = "" + cell.getNumericCellValue();
                break;
            case STRING:
            	value = "" + cell.getStringCellValue();
                break;
            case BLANK:
            	value = "";
                break;
            case ERROR:
            	value = "" + cell.getErrorCellValue();
                break;
            default:
            }
		}
		return value;
	}
	/**
	 * @desc	: POI 엑셀 레벨 체크
	 * @author	: gs
	 * @date	: 2020. 12. 08.
	 * @method	: checkLine
	 * @return	: boolean
	 */
	public boolean checkLine(XSSFCell cell) throws Exception {
		String value = null;
		try {
			value = checkCell(cell);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (value == null || value.length() == 0)
			return false;
		return true;
	}
}
