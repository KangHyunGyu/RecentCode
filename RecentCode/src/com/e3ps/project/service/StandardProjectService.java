package com.e3ps.project.service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.joda.time.DateTime;

import com.e3ps.admin.service.AdminHelper;
import com.e3ps.common.code.NumberCode;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.db.DBConnectionManager;
import com.e3ps.common.iba.IBAUtil;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.SequenceDao;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.common.web.ParamUtil;
import com.e3ps.common.workflow.E3PSWorkflowHelper;
import com.e3ps.groupware.service.WFItemHelper;
import com.e3ps.org.People;
import com.e3ps.project.EOutput;
import com.e3ps.project.EProject;
import com.e3ps.project.EProjectMasteredLink;
import com.e3ps.project.EProjectNode;
import com.e3ps.project.EProjectTemplate;
import com.e3ps.project.ETask;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.PrePostLink;
import com.e3ps.project.ProjectRegistApproval;
import com.e3ps.project.ProjectRegistLink;
import com.e3ps.project.ProjectRole;
import com.e3ps.project.ProjectStopHistory;
import com.e3ps.project.RoleUserLink;
import com.e3ps.project.ScheduleNode;
import com.e3ps.project.TaskOutputLink;
import com.e3ps.project.TaskRoleLink;
import com.e3ps.project.beans.ProjectDao;
import com.e3ps.project.beans.ProjectData;
import com.e3ps.project.beans.ProjectGanttLinkData;
import com.e3ps.project.beans.ProjectGanttTaskData;
import com.e3ps.project.beans.ProjectMailBroker;
import com.e3ps.project.beans.ProjectTreeModel;
import com.e3ps.project.beans.ProjectTreeNode;
import com.e3ps.project.beans.ProjectUtil;
import com.e3ps.project.issue.IssueRequest;
import com.e3ps.project.issue.IssueSolution;
import com.e3ps.project.issue.SolutionLink;
import com.e3ps.project.issue.TaskRequestLink;
import com.e3ps.project.key.ProjectKey.FOLDERKEY;
import com.e3ps.project.key.ProjectKey.MESSAGEKEY;
import com.e3ps.project.key.ProjectKey.PROJECTKEY;
import com.e3ps.project.key.ProjectKey.STATEKEY;

import wt.clients.folder.FolderTaskLogic;
import wt.content.ContentHelper;
import wt.enterprise.RevisionControlled;
import wt.epm.EPMDocument;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.fc.collections.WTArrayList;
import wt.fc.collections.WTCollection;
import wt.fc.collections.WTHashSet;
import wt.fc.collections.WTSet;
import wt.folder.Folder;
import wt.folder.FolderEntry;
import wt.folder.FolderHelper;
import wt.inf.container.WTContainerRef;
import wt.introspection.ClassInfo;
import wt.introspection.WTIntrospector;
import wt.lifecycle.LifeCycleHelper;
import wt.lifecycle.LifeCycleTemplate;
import wt.method.RemoteAccess;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.pdmlink.PDMLinkProduct;
import wt.pds.DatabaseInfoUtilities;
import wt.pds.StatementSpec;
import wt.pom.Transaction;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.StandardManager;
import wt.session.SessionContext;
import wt.session.SessionHelper;
import wt.util.WTException;

public class StandardProjectService extends StandardManager implements RemoteAccess, Serializable, ProjectService {
    
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PROJECT.getName());
	private static final long serialVersionUID = 1L;

	public static StandardProjectService newStandardProjectService() throws WTException {
    	StandardProjectService instance = new StandardProjectService();
		instance.initialize();
		return instance;
	}
    
    /**코드로 프로젝트 가져오기. 최신버전.
     * @param number
     * @return
     * @throws Exception
     */
    @Override
    public EProject getProject(String number)throws Exception{
		if (number == null || number.length() == 0)
			return null;
		try {
			QuerySpec qs = new QuerySpec(EProject.class);
			qs.appendWhere(new SearchCondition(EProject.class, EProject.CODE,
					"=", number), new int[] { 0 });
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(EProject.class,
					EProject.LAST_VERSION, SearchCondition.IS_TRUE),
					new int[] { 0 });

			QueryResult qr = PersistenceHelper.manager.find(qs);
			if (qr.hasMoreElements()) {
				return (EProject) qr.nextElement();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new WTException(e);
		}
		return null;
    }
    
    /**
     * 프로젝트 생성시
     * @param hash
     * @return
     * @throws Exception
     */
    @Override
	public EProject createProject(Map<String, Object> hash) throws WTException {

		Transaction trx = new Transaction();

		EProject project = null;
		try {
			trx.start();

			project = EProject.newEProject();

			String number = ParamUtil.get(hash, "number");
			String name = ParamUtil.get(hash, "name");
			String description = ParamUtil.get(hash, "description");
			String template = ParamUtil.get(hash, "template");
			String planStartDate = ParamUtil.get(hash, "planStartDate");
			String pm = ParamUtil.get(hash, "pm");
			
			
			String group = ParamUtil.get(hash, "groupCode");
			String material = ParamUtil.get(hash, "materialCode");
			String level = ParamUtil.get(hash, "levelCode");
			
			String tempNumber = group+material+level + DateUtil.getToDay("YY");
			String noFormat = "000";
			String seqNo = SequenceDao.manager.getSeqNo(tempNumber + "", noFormat, "EProject", "code");
			
			number = tempNumber+seqNo;
			
			EProjectTemplate temp = (EProjectTemplate) CommonUtil.getObject(template);
			
			PDMLinkProduct ot = WCUtil.getPDMLinkProduct();
			project.setProduct(ot);

			project.setCode(number);
			project.setName(name);
			project.setDescription(description);
			project.setPlanStartDate(DateUtil.convertDate(planStartDate));
			project.setPlanEndDate(DateUtil.convertDate(planStartDate));
			project.setVersion(0);
			
			project.setOutputType(temp.getOutputType());
			project.setProjectType(temp.getOutputType().toString());
			project.setTemplate(temp);
			
			project.setGroupCode(group);
			project.setMaterialCode(material);
			project.setLevelCode(level);
			
			// folder setting
			Folder folder = FolderTaskLogic.getFolder("/Default/Project", WCUtil.getWTContainerRef());
			FolderHelper.assignLocation((FolderEntry) project, folder);
			PDMLinkProduct e3psProduct = WCUtil.getPDMLinkProduct();
			WTContainerRef wtContainerRef = WTContainerRef.newWTContainerRef(e3psProduct);
			// project.setContainer(e3psProduct);
			LifeCycleHelper.setLifeCycle(project, LifeCycleHelper.service.getLifeCycleTemplate("LC_Project", wtContainerRef)); // Lifecycle

			project = (EProject) PersistenceHelper.manager.save(project);

			if (pm != null && pm.length() > 0) {
				People people = (People) CommonUtil.getObject(pm);
				WTUser user = people.getUser();
				ProjectRole pmRole = ProjectRole.newProjectRole();
				pmRole.setCode(PROJECTKEY.PM);
				NumberCode pmCode = CodeHelper.manager.getNumberCode("PROJECTROLE", PROJECTKEY.PM);
				if(pmCode != null) {
					pmRole.setName(pmCode.getName());
				} else {
					pmRole.setName("PM");
				}
				pmRole.setProject(project);
				pmRole.setSort(-1);
				pmRole = (ProjectRole) PersistenceHelper.manager.save(pmRole);

				RoleUserLink ulink = RoleUserLink.newRoleUserLink(user, pmRole);
				PersistenceHelper.manager.save(ulink);
			}

			copyProject(temp, project);

			project = (EProject) PersistenceHelper.manager.refresh(project);
			ProjectTreeModel model2 = new ProjectTreeModel(project);
			model2.setPlanSchedule(DateUtil.convertStartDate(planStartDate));

			project = (EProject) PersistenceHelper.manager.refresh(project);

			trx.commit();
			trx = null;
			
			//ERP 전송
			//ERPInterface.send(project);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return project;
    }
    
    /**
     * 프로젝트 수정시
     * @param hash
     * @return
     * @throws Exception
     */
    
    @Override
    public String update(Map<String, Object> hash)throws Exception{

		Transaction trx = new Transaction();
		String msg = MESSAGEKEY.UPDATE;

		try {
			trx.start();

			ReferenceFactory rf = new ReferenceFactory();
			String oid = ParamUtil.get(hash, "oid");

			EProject project = (EProject) rf.getReference(oid).getObject();

			String name = ParamUtil.get(hash, "name");
			String description = ParamUtil.get(hash, "description");

			project.setName(name);
			project.setDescription(description);

			project = (EProject) PersistenceHelper.manager.modify(project);

			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getLocalizedMessage() + MESSAGEKEY.UPDATE_ERROR;
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return msg;
	}
    
    /**
     * 프로젝트 삭제시
     * @param hash
     * @return
     * @throws Exception
     */
    
    @Override
    public String delete(Map<String, Object> hash)throws Exception{

    	Transaction trx = new Transaction();
    	String msg = MESSAGEKEY.DELETE;

    	try {
    	    trx.start();
    	    
    	    String oid = ParamUtil.get(hash,"oid");
    	    ReferenceFactory rf = new ReferenceFactory();
    	    
    	    EProject project = (EProject)rf.getReference(oid).getObject();
    	   
    	    QuerySpec qs = new QuerySpec(EProject.class);
    	    qs.appendWhere(new SearchCondition(EProject.class,EProject.CODE,"=",project.getCode()),new int[]{0});
    	    
    	    QueryResult projects = PersistenceHelper.manager.find(qs); 
    	    
    	    QueryResult qr2 = PersistenceHelper.manager.navigate(project,"regist",ProjectRegistLink.class);
 	    	while(qr2.hasMoreElements()){
 	    		ProjectRegistApproval pra = (ProjectRegistApproval)qr2.nextElement();
 	    		WFItemHelper.service.deleteWFItem(pra);
 	    		PersistenceHelper.manager.delete(pra);
 	    	}
 	    	
    	    WFItemHelper.service.deleteWFItem(project);
    	    
    	    while(projects.hasMoreElements()){
    	    	EProject pp = (EProject)projects.nextElement();
    	    	ProjectHelper.service.delete(pp);
    	    }
    	    
            trx.commit();
            trx = null;
            
            //ERP 전송
            //ERPInterface.send(project, true);

       } catch(Exception e) {
    	   e.printStackTrace();
           msg = e.getLocalizedMessage()+MESSAGEKEY.DELETE_ERROR;
           throw new WTException(e);
       } finally {
           if(trx!=null){
                trx.rollback();
        }
       }
        return msg;
    }
    

    /**프로젝트 복사 (이력 남길시)
     * @param orgProject
     * @param newProject
     * @throws Exception
     */
    
    @Override
    public void copyProject(EProjectNode orgProject, EProjectNode newProject)throws Exception{
    	
    	/// pm copy///////////
    	Object[] pm = (Object[])ProjectMemberHelper.service.getPM(orgProject);
    	if(pm!=null){
	    	ProjectRole opmRole = (ProjectRole)pm[0];
	    	RoleUserLink ulink = (RoleUserLink)pm[1];
	    	
	    	if(ulink != null){
		    	ProjectRole pmRole = ProjectRole.newProjectRole();
		    	pmRole.setCode(opmRole.getCode());
		    	pmRole.setDescription(opmRole.getDescription());
		    	pmRole.setName(opmRole.getName());
		    	pmRole.setProject(newProject);
		    	pmRole.setSort(opmRole.getSort());
		    	pmRole = (ProjectRole)PersistenceHelper.manager.save(pmRole);
		    	
		    	RoleUserLink nLink = RoleUserLink.newRoleUserLink(ulink.getUser(), pmRole);
		    	PersistenceHelper.manager.save(nLink);
	    	}else{
	    		LOGGER.info("[copyProject]PM ulink is null");
	    	}
    	}
    	
    	//////// task copy ///////////////////////////////////////
    	
    	String poid = CommonUtil.getOIDString(orgProject);
    	String newOid = CommonUtil.getOIDString(newProject);
	    ProjectTreeModel model = new ProjectTreeModel(orgProject);
	    
	    HashMap<String, String> parentHash = new HashMap<>();
	    parentHash.put(poid, newOid);
	    
	    for(int i=0; i<model.list.size();i++){
	    	String[] o = (String[])model.list.get(i);
	    	ProjectTreeNode node = (ProjectTreeNode)model.nodeMap.get(o[2]);
	    	ETaskNode sgnode = (ETaskNode)node.getUserObject();
	    	
	    	String parentOid = newOid;
	    	ProjectTreeNode parentNode = (ProjectTreeNode)node.getParent();
	    	if(parentNode!=null){
	    		ScheduleNode parentSgnode = (ScheduleNode)parentNode.getUserObject();
	    		parentOid = (String)parentHash.get(parentSgnode.getPersistInfo().getObjectIdentifier().toString());
	    	}
	    	
	    	ETaskNode newNode = ETask.newETask();
	    	
	    	copyTaskAttribute(sgnode,newNode);
	    	newNode.setProject(newProject);
	    	//newNode.setCreator(SessionHelper.manager.getPrincipalReference());
	    	newNode.setParent((ScheduleNode)CommonUtil.getObject(parentOid));
	    	
	    	newNode = (ETaskNode)PersistenceHelper.manager.save(newNode);
	    	
    	    parentHash.put(sgnode.getPersistInfo().getObjectIdentifier().toString(), newNode.getPersistInfo().getObjectIdentifier().toString());
	    }
	    
	    /////// 관련 링크 copy /////////////////////////
	    for(int i=0; i<model.list.size();i++){
	    	String[] o = (String[])model.list.get(i);
	    	ProjectTreeNode node = (ProjectTreeNode)model.nodeMap.get(o[2]);
	    	ETaskNode sgnode = (ETaskNode)node.getUserObject();
	    	
	    	String newNodeOid = (String)parentHash.get(sgnode.getPersistInfo().getObjectIdentifier().toString());
	    	ScheduleNode newNode = (ScheduleNode)CommonUtil.getObject(newNodeOid);
	    	
	    	///////////// role copy //////////
	    	QueryResult roleQr = PersistenceHelper.manager.navigate(sgnode,"role" , TaskRoleLink.class);
	    	
	    	while(roleQr.hasMoreElements()){
	    		ProjectRole sgrole = (ProjectRole)roleQr.nextElement();
	    		
	    		ProjectRole rr = TemplateHelper.manager.getProjectRole(newProject, sgrole.getCode());
	    		if(rr==null){
	    			rr = ProjectRole.newProjectRole();
	    			rr.setName(sgrole.getName());
    	    		rr.setCode(sgrole.getCode());
    	    		rr.setProject(newProject);
    	    		rr = (ProjectRole)PersistenceHelper.manager.save(rr);
    	    		
    	    		QueryResult uqr = PersistenceHelper.manager.navigate(sgrole, "user", RoleUserLink.class);
    	    		while(uqr.hasMoreElements()){
    	    			WTUser user = (WTUser)uqr.nextElement();
    	    			
    	    			RoleUserLink newLink = RoleUserLink.newRoleUserLink(user, rr);
    	    			PersistenceHelper.manager.save(newLink);
    	    		}
	    		}
	    		TaskRoleLink link = TaskRoleLink.newTaskRoleLink(rr,(ETaskNode)newNode);
	    		PersistenceHelper.manager.save(link);
    	    }
	    	
	    	/////// 선후행 copy //////////////
	    	QueryResult qr = PersistenceHelper.manager.navigate(sgnode,"pre",PrePostLink.class);
 	    	while(qr.hasMoreElements()){
 	    		ETaskNode preNode = (ETaskNode)qr.nextElement();	
 	    		String newPreNodeOid = (String)parentHash.get(preNode.getPersistInfo().getObjectIdentifier().toString());
 	    		LOGGER.info("newPreNodeOid :: "+newPreNodeOid);
 	    		ETaskNode newPreNode = (ETaskNode)CommonUtil.getObject(newPreNodeOid);
 	    		
 	    		PrePostLink link = PrePostLink.newPrePostLink(newPreNode, (ETaskNode)newNode);
 	    		PersistenceHelper.manager.save(link);
 	    	}
 	    	
 	    	////// 산출물 copy /////////////////
 	    	QueryResult qr2 = PersistenceHelper.manager.navigate(sgnode,"output",TaskOutputLink.class);
 	    	while(qr2.hasMoreElements()){
 	    		 EOutput output = (EOutput)qr2.nextElement();
 	    		 EOutput newOutput = EOutput.newEOutput();
 	    		 newOutput.setName(output.getName());
 	    		 newOutput.setDescription(output.getDescription());
 	    		 newOutput.setLocation(output.getLocation());

 	    		 newOutput.setDocType(output.getDocType());
 	    		 newOutput.setStep(output.getStep());
 	    		 newOutput.setAttachFile(output.isAttachFile());
 	    		 newOutput.setDocument(output.getDocument());
 	    		 newOutput.setTask((ETaskNode)newNode);
 	    		 newOutput = (EOutput)PersistenceHelper.manager.save(newOutput);
 	    	}
 	    	
 	    	/// issue copy////
	    	QueryResult issueQr = PersistenceHelper.manager.navigate(sgnode,"request", TaskRequestLink.class);
	    	while(issueQr.hasMoreElements()){
	    		IssueRequest ir = (IssueRequest)issueQr.nextElement();
	    		
	    		IssueRequest newIr = IssueRequest.newIssueRequest();
	    		newIr.setCreator(ir.getCreator());
	    		newIr.setIssueType(ir.getIssueType());
	    		newIr.setManager(ir.getManager());
	    		newIr.setName(ir.getName());
	    		newIr.setProblem(ir.getProblem());
	    		newIr.setRequestDate(ir.getRequestDate());
	    		newIr.setState(ir.getState());
	    		newIr.setTask((ETaskNode)newNode);
	    		
	    		newIr = (IssueRequest)PersistenceHelper.manager.save(newIr);
	    		
	    		ContentHelper.service.copyContent(ir, newIr);
	    		
	    		QueryResult solQr = PersistenceHelper.manager.navigate(ir,"solution", SolutionLink.class);
		    	while(solQr.hasMoreElements()){
		    		IssueSolution so = (IssueSolution)solQr.nextElement();
		    		
		    		IssueSolution newSo = IssueSolution.newIssueSolution();
		    		newSo.setSolution(so.getSolution());
		    		newSo.setCreator(so.getCreator());
		    		newSo.setRequest(newIr);
		    		newSo.setSolutionDate(so.getSolutionDate());
		    		newSo = (IssueSolution)PersistenceHelper.manager.save(newSo);
		    		
		    		ContentHelper.service.copyContent(so, newSo);
		    	}
	    	}
	    	//IBA Copy
	    	String orgProjectproductGroupCode = StringUtil.checkNull(IBAUtil.getAttrValue(orgProject, "PRODUCTGROUP"));
	    	
	    	if(orgProjectproductGroupCode.length()>0) {
	    		IBAUtil.changeIBAValue(newProject, "PRODUCTGROUP", orgProjectproductGroupCode);
	    	}
	    }
    }
    
    public void copyProjectCreate(EProjectNode orgProject, EProjectNode newProject)throws Exception{
    	
    	/// pm copy///////////
    	Object[] pm = (Object[])ProjectMemberHelper.service.getPM(orgProject);
    	if(pm!=null){
	    	ProjectRole opmRole = (ProjectRole)pm[0];
	    	RoleUserLink ulink = (RoleUserLink)pm[1];
	    	
	    	if(ulink != null){
		    	ProjectRole pmRole = ProjectRole.newProjectRole();
		    	pmRole.setCode(opmRole.getCode());
		    	pmRole.setDescription(opmRole.getDescription());
		    	pmRole.setName(opmRole.getName());
		    	pmRole.setProject(newProject);
		    	pmRole.setSort(opmRole.getSort());
		    	pmRole = (ProjectRole)PersistenceHelper.manager.save(pmRole);
		    	
		    	RoleUserLink nLink = RoleUserLink.newRoleUserLink(ulink.getUser(), pmRole);
		    	PersistenceHelper.manager.save(nLink);
	    	}else{
	    		LOGGER.info("[copyProject]PM ulink is null");
	    	}
    	}
    	
    	//////// task copy ///////////////////////////////////////
    	
    	String poid = CommonUtil.getOIDString(orgProject);
    	String newOid = CommonUtil.getOIDString(newProject);
	    ProjectTreeModel model = new ProjectTreeModel(orgProject);
	    
	    HashMap<String, String> parentHash = new HashMap<>();
	    parentHash.put(poid, newOid);
	    
	    for(int i=0; i<model.list.size();i++){
	    	String[] o = (String[])model.list.get(i);
	    	ProjectTreeNode node = (ProjectTreeNode)model.nodeMap.get(o[2]);
	    	ETaskNode sgnode = (ETaskNode)node.getUserObject();
	    	
	    	String parentOid = newOid;
	    	ProjectTreeNode parentNode = (ProjectTreeNode)node.getParent();
	    	if(parentNode!=null){
	    		ScheduleNode parentSgnode = (ScheduleNode)parentNode.getUserObject();
	    		parentOid = (String)parentHash.get(parentSgnode.getPersistInfo().getObjectIdentifier().toString());
	    	}
	    	
	    	ETaskNode newNode = ETask.newETask();
	    	
	    	copyTaskCreateAttribute(sgnode,newNode);
	    	newNode.setProject(newProject);
	    	//newNode.setCreator(SessionHelper.manager.getPrincipalReference());
	    	newNode.setParent((ScheduleNode)CommonUtil.getObject(parentOid));
	    	
	    	newNode = (ETaskNode)PersistenceHelper.manager.save(newNode);
	    	
    	    parentHash.put(sgnode.getPersistInfo().getObjectIdentifier().toString(), newNode.getPersistInfo().getObjectIdentifier().toString());
	    }
	    
	    /////// 관련 링크 copy /////////////////////////
	    for(int i=0; i<model.list.size();i++){
	    	String[] o = (String[])model.list.get(i);
	    	ProjectTreeNode node = (ProjectTreeNode)model.nodeMap.get(o[2]);
	    	ETaskNode sgnode = (ETaskNode)node.getUserObject();
	    	
	    	String newNodeOid = (String)parentHash.get(sgnode.getPersistInfo().getObjectIdentifier().toString());
	    	ScheduleNode newNode = (ScheduleNode)CommonUtil.getObject(newNodeOid);
	    	
	    	///////////// role copy //////////
	    	QueryResult roleQr = PersistenceHelper.manager.navigate(sgnode,"role" , TaskRoleLink.class);
	    	
	    	while(roleQr.hasMoreElements()){
	    		ProjectRole sgrole = (ProjectRole)roleQr.nextElement();
	    		
	    		ProjectRole rr = TemplateHelper.manager.getProjectRole(newProject, sgrole.getCode());
	    		if(rr==null){
	    			rr = ProjectRole.newProjectRole();
	    			rr.setName(sgrole.getName());
    	    		rr.setCode(sgrole.getCode());
    	    		rr.setProject(newProject);
    	    		rr = (ProjectRole)PersistenceHelper.manager.save(rr);
    	    		
    	    		QueryResult uqr = PersistenceHelper.manager.navigate(sgrole, "user", RoleUserLink.class);
    	    		while(uqr.hasMoreElements()){
    	    			WTUser user = (WTUser)uqr.nextElement();
    	    			
    	    			RoleUserLink newLink = RoleUserLink.newRoleUserLink(user, rr);
    	    			PersistenceHelper.manager.save(newLink);
    	    		}
	    		}
	    		TaskRoleLink link = TaskRoleLink.newTaskRoleLink(rr,(ETaskNode)newNode);
	    		PersistenceHelper.manager.save(link);
    	    }
	    	
	    	////// 산출물 copy /////////////////
	  	    QueryResult qr2 = PersistenceHelper.manager.navigate(sgnode,"output",TaskOutputLink.class);
	  	    while(qr2.hasMoreElements()){
	  	    	EOutput output = (EOutput)qr2.nextElement();
	  	    	EOutput newOutput = EOutput.newEOutput();
	  	    	newOutput.setName(output.getName());
	  	    	newOutput.setDescription(output.getDescription());
	  	    	newOutput.setLocation(output.getLocation());

	  	    	newOutput.setDocType(output.getDocType());
	  	    	
	  	    	newOutput.setTask((ETaskNode)newNode);
	  	    	newOutput = (EOutput)PersistenceHelper.manager.save(newOutput);
	  	    }
	    	
	    	/////// 선후행 copy //////////////
	    	QueryResult qr = PersistenceHelper.manager.navigate(sgnode,"pre",PrePostLink.class);
 	    	while(qr.hasMoreElements()){
 	    		ETaskNode preNode = (ETaskNode)qr.nextElement();	
 	    		String newPreNodeOid = (String)parentHash.get(preNode.getPersistInfo().getObjectIdentifier().toString());
 	    		LOGGER.info("newPreNodeOid :: "+newPreNodeOid);
 	    		ETaskNode newPreNode = (ETaskNode)CommonUtil.getObject(newPreNodeOid);
 	    		
 	    		PrePostLink link = PrePostLink.newPrePostLink(newPreNode, (ETaskNode)newNode);
 	    		PersistenceHelper.manager.save(link);
 	    	}
 	    	
	    	//IBA Copy
	    	String orgProjectproductGroupCode = StringUtil.checkNull(IBAUtil.getAttrValue(orgProject, "PRODUCTGROUP"));
	    	
	    	if(orgProjectproductGroupCode.length()>0) {
	    		IBAUtil.changeIBAValue(newProject, "PRODUCTGROUP", orgProjectproductGroupCode);
	    	}
	    }
    }
    
    
    /**
     * Task 속성 복사
     * @param org
     * @param node
     * @throws Exception
     */
    
    @Override
    public void copyTaskAttribute(ETaskNode org, ETaskNode node)throws Exception{
    	
    	node.setCompletion(org.getCompletion());
    	node.setCreator(org.getCreator());
    	node.setDescription(org.getDescription());
    	node.setEndDate(org.getEndDate());
    	node.setManDay(org.getManDay());
    	node.setName(org.getName());
    	node.setPlanEndDate(org.getPlanEndDate());
    	node.setPlanStartDate(org.getPlanStartDate());
    	node.setSort(org.getSort());
    	node.setStartDate(org.getStartDate());
    	node.setStatus(org.getStatus());
    }
    
    public void copyTaskCreateAttribute(ETaskNode org, ETaskNode node)throws Exception{
    	
    	//node.setCompletion(org.getCompletion());
    	node.setCreator(org.getCreator());
    	node.setDescription(org.getDescription());
    	//node.setEndDate(org.getEndDate());
    	node.setManDay(org.getManDay());
    	node.setName(org.getName());
    	node.setPlanEndDate(org.getPlanEndDate());
    	node.setPlanStartDate(org.getPlanStartDate());
    	node.setSort(org.getSort());
    	//node.setStartDate(org.getStartDate());
    	node.setStatus("READY");
    }
    
    /**프로젝트 및 태스크 상태 설정 & 결재완료시 태스크 시작
     * @param pjt
     * @param state
     * @throws Exception
     */
    
    @Override
    public void setState(EProject pjt, String state)throws Exception{
    	
    	System.out.println("EPROJECT RESTART : state : progress?  " + state);
    	
    	SessionContext prev = SessionContext.newContext();
		Transaction trs = new Transaction();
		try {
			trs.start();
			SessionHelper.manager.setAdministrator();
			//lifecycle 결재시 상태값 변경
	    	if("APPROVED".equals(state)){
	    		state = STATEKEY.PROGRESS;
				pjt.setVersion(pjt.getVersion() + 1);
	    		PersistenceHelper.manager.modify(pjt);
	    		
	    	}else if("APPROVING".equals(state)){
	    		
	    		state = STATEKEY.SIGN;
	    	}
			E3PSWorkflowHelper.manager.changeLCState(pjt, state);
			
			if(state.equals(STATEKEY.PROGRESS)){
				
				//프로젝트 하위의 후행 태스크의 시작 이벤트.
				ProjectTreeModel model = new ProjectTreeModel(pjt);
			    model.start();
			}
			
		    //하위 태스크의 상태를 전부 변경하는 로직.
			if(STATEKEY.MODIFY.equals(state) || STATEKEY.STOP.equals(state)){
				
				System.out.println("################ : " + STATEKEY.STOP);
				
				state = STATEKEY.READY;
				
		    	QuerySpec qs = new QuerySpec();
		    	QueryResult qr = null;
		    	int idx = qs.addClassList(ETask.class, true);
		    	
		    	qs.appendWhere(new SearchCondition(ETask.class, "projectReference.key.id", "=", CommonUtil.getOIDLongValue(pjt)), new int[]{idx});
		    	qs.appendAnd();
		    	qs.appendWhere(new SearchCondition(ETask.class, "status", "<>", STATEKEY.COMPLETED), new int[]{idx});
		    	//qs.appendAnd();
		    	//qs.appendWhere(new SearchCondition(ETask.class, "status", "<>", STATEKEY.PROGRESS), new int[]{idx});
		    	
		    	
		    	System.out.println("qs ===> " + qs);
		    	
		    	qr = PersistenceHelper.manager.find(qs);
		    	
		    	while(qr.hasMoreElements()){
		    		Object o[] = (Object[])qr.nextElement();
		    		ETask task = (ETask)o[0];
		    		task.setStatus(state);
		    		PersistenceHelper.manager.save(task);
		    	}
			}
			
			trs.commit();
			
			//ERPInterface.send(pjt);
			trs = null;
		} catch (Exception e) {
			e.printStackTrace();
			trs.rollback();
		} finally {
			if (trs != null)
				trs.rollback();
			SessionContext.setContext(prev);
		}
    	
    }

    /**프로젝트 삭제시
     * @param node
     * @throws Exception
     */
    
    @SuppressWarnings("rawtypes")
	@Override
    public void delete(ScheduleNode node)throws Exception{
    	
    	ArrayList al = ProjectDao.manager.getStructure(node.getPersistInfo().getObjectIdentifier().getId(), 1);
   	    ReferenceFactory rf = new ReferenceFactory();
    	for(int i=0; i< al.size(); i++){
    		String[] s = (String[])al.get(i);
    		ScheduleNode ccnode = (ScheduleNode)rf.getReference( s[2]).getObject();
     	    delete(ccnode);
    	}

    	if(node instanceof ETaskNode){
    		QueryResult qr = PersistenceHelper.manager.navigate(node, "role", TaskRoleLink.class, false);
	    	while(qr.hasMoreElements()){
	    		TaskRoleLink rlink = (TaskRoleLink)qr.nextElement();
	    		ProjectRole role = rlink.getRole();
	    		
	    		QueryResult qr2 = PersistenceHelper.manager.navigate(role, "task", TaskRoleLink.class, false);
	    		if(qr2.size()==0){
	    			PersistenceHelper.manager.delete(role);
	    		}else{
	    			PersistenceHelper.manager.delete(rlink);
	    		}
    		}
    	}
    	
    	QueryResult qr = PersistenceHelper.manager.navigate(node, "output", TaskOutputLink.class);
    	while(qr.hasMoreElements()){
    		EOutput sgoutput = (EOutput)qr.nextElement();
    		Map<String, Object> hash = new HashMap<String, Object>();
    		hash.put("oid", sgoutput.getPersistInfo().getObjectIdentifier().toString());
    		LOGGER.info("sgoutput -> "+sgoutput.getPersistInfo().getObjectIdentifier().toString());
    		
    		OutputHelper.service.deleteOutput(hash);
    		//PersistenceHelper.manager.delete(sgoutput);
		}
    	
    	//20201016 sangylee 추가(체크아웃 취소시 워킹카피 프로젝트 삭제시 관련 이슈 삭제)
    	QueryResult issueQr = PersistenceHelper.manager.navigate(node,"request", TaskRequestLink.class);
    	while(issueQr.hasMoreElements()){
    		IssueRequest ir = (IssueRequest)issueQr.nextElement();
    		
    		QueryResult solQr = PersistenceHelper.manager.navigate(ir,"solution", SolutionLink.class);
	    	while(solQr.hasMoreElements()){
	    		IssueSolution so = (IssueSolution)solQr.nextElement();
	    		
	    		PersistenceHelper.manager.delete(so);
	    	}
	    	
	    	PersistenceHelper.manager.delete(ir);
		}
    	
//    	if(node instanceof EProjectNode){
//	    	QueryResult qr2 = PersistenceHelper.manager.navigate(node,"regist",ProjectRegistLink.class);
//	    	while(qr2.hasMoreElements()){
//	    		ProjectRegistApproval pra = (ProjectRegistApproval)qr2.nextElement();
//	    		WFItemHelper.service.deleteWFItem(pra);
//	    	}
//    	}
    	PersistenceHelper.manager.delete(node);
    	
    }

    /**태스크 생성
     * @param hash
     * @return
     * @throws Exception
     */
    
    @SuppressWarnings("rawtypes")
	@Override
    public  ETaskNode createTask(Hashtable hash)throws Exception{

          Transaction trx = new Transaction();
          
          ETaskNode newNode = null;
          try {
       	    	trx.start();
       	    
		 	    String selectId = (String)ProjectUtil.get(hash,"selectId");
		 	    String cname = (String)ProjectUtil.get(hash,"cname");
		 	    String description = ProjectUtil.get(hash,"description");
		 	    int sort = ProjectUtil.getInt(hash,"sort");
		 	    double manDay = ProjectUtil.getDouble(hash,"manDay");
		 	    String planStartDate = ProjectUtil.get(hash,"planStartDate");
		  	    String planEndDate = ProjectUtil.get(hash,"planEndDate");
		 	    
		 	    ReferenceFactory rf = new ReferenceFactory();
		 	    ScheduleNode parent = (ScheduleNode)rf.getReference(selectId).getObject();
		 	    
		 	    //부모 객체의 선후행을 삭제.
		 	    if(parent instanceof ETaskNode){
		 	    	QuerySpec qs = new QuerySpec();
		 	    	int idx = qs.appendClassList(PrePostLink.class, true);
		 	    	//qs.appendWhere(new SearchCondition(PrePostLink.class, "roleAObjectRef.key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(selectId)), new int[]{idx});
		 	    	//qs.appendOr();
		 	    	qs.appendWhere(new SearchCondition(PrePostLink.class, "roleBObjectRef.key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(selectId)), new int[]{idx});
		 	    	QueryResult qr = PersistenceHelper.manager.find(qs);
		 	    	LOGGER.info("[createTask]관련 링크 확인 후 삭제...= " + qr.size());
		 	    	while(qr.hasMoreElements()){
		 	    		Object o[] = (Object[])qr.nextElement();
		 	    		PrePostLink link = (PrePostLink)o[0];
		 	    		PersistenceHelper.manager.delete(link);
		 	    	}
		 	    	
		 	    	//사용안함
		 	    	QuerySpec qs2 = new QuerySpec();
		 	    	int idx2 = qs2.appendClassList(EOutput.class, true);
		 	    	qs2.appendWhere(new SearchCondition(EOutput.class, "taskReference.key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(selectId)), new int[]{idx2});
		 	    	QueryResult qr2 = PersistenceHelper.manager.find(qs2);
		 	    	LOGGER.info("[createTask]산출물 확인 후 삭제...= " + qr2.size());
		 	    	while(qr2.hasMoreElements()){
		 	    		Object o[] = (Object[])qr2.nextElement();
		 	    		EOutput output = (EOutput)o[0];
		 	    		PersistenceHelper.manager.delete(output);
		 	    	}
		 	    }
		 	    
		 	    newNode = ETask.newETask();
		 	    
		 	    newNode.setDescription(description);
		 	    
		 	    Timestamp start = DateUtil.convertDate(planStartDate);
		  	    Timestamp end = DateUtil.convertDate(planEndDate);
		
		 	    newNode.setPlanStartDate(start);
		 	    newNode.setPlanEndDate(end);
		 	    newNode.setCreator(SessionHelper.manager.getPrincipalReference());
		 	    newNode.setName(cname);
		 	    newNode.setParent(parent);
		 	    newNode.setSort(sort);
		 	    newNode.setManDay(manDay);
		 	    newNode.setStatus(STATEKEY.READY);
		 	    
		 	    if(parent instanceof EProjectNode){
		 	    	newNode.setProject((EProjectNode)parent);
		 	    }else{
		 	    	newNode.setProject(((ETaskNode)parent).getProject());
		 	    }
		 	    
		 	    newNode = (ETaskNode)PersistenceHelper.manager.save(newNode);
		 	    
		    	ProjectTreeModel model = new ProjectTreeModel(newNode.getProject());
		    	model.setExecSchedule(newNode);
		    	trx.commit();
		        trx = null;

		   } catch(Exception e) {
			   e.printStackTrace();
			   throw new WTException(e);
		   } finally {
		       if(trx!=null){
		            trx.rollback();
		    }
		   }
    	return newNode;
    }
    
    /**태스크 수정
     * @param hash
     * @return
     * @throws Exception
     */
    
    @SuppressWarnings("rawtypes")
	@Override
    public  ETaskNode updateTask(Hashtable hash)throws Exception{

    	Transaction trx = new Transaction();
         
         ETaskNode node = null;
         try {
      	    	trx.start();
      	    	
		  	    String selectChild = (String)ProjectUtil.get(hash,"selectChild");
		  	    
		  	    String selectId = (String)ProjectUtil.get(hash,"selectId");
		  	    String cname = (String)ProjectUtil.get(hash,"cname");
		  	    String description = (String)ProjectUtil.get(hash,"description");
		  	    int sort = ProjectUtil.getInt(hash,"sort");
		  	    double manDay = ProjectUtil.getDouble(hash,"manDay");
		  	    String planStartDate = ProjectUtil.get(hash,"planStartDate");
		  	    String planEndDate = ProjectUtil.get(hash,"planEndDate");
		  	    
		  	    ReferenceFactory rf = new ReferenceFactory();
		  	    ScheduleNode parent = (ScheduleNode)rf.getReference(selectId).getObject();
		  	    
		  	    node = (ETaskNode)rf.getReference(selectChild).getObject();
		  	    
		  	    node.setDescription(description);
		  	    
		  	    Timestamp start = DateUtil.convertDate(planStartDate);
		  	    Timestamp end = DateUtil.convertDate(planEndDate);
		  	    
		  	    node.setPlanStartDate(start);
		  	    node.setPlanEndDate(end);
		  	    
		  	    node.setName(cname);
		  	    node.setParent(parent);
		  	    node.setSort(sort);
		  	    node.setManDay(manDay);
		  	    
		  	    node = (ETaskNode)PersistenceHelper.manager.save(node);
		    	ProjectTreeModel model = new ProjectTreeModel(node.getProject());
		    	model.setExecSchedule(node);
		    	
		    	trx.commit();
		        trx = null;
		
		   } catch(Exception e) {
			   e.printStackTrace();
			   throw new WTException(e);
		   } finally {
		       if(trx!=null){
		            trx.rollback();
		    }
		   }
		   return node;
    }
    
    /**
     * 태스크 저장
     * @param hash
     * @return
     * @throws Exception
     */
    
    @SuppressWarnings("rawtypes")
	@Override
    public ETaskNode nextSave(Hashtable hash)throws Exception{
        ETaskNode newNode = null;

        Transaction trx = new Transaction();

        try {
     	    trx.start();
     	    
     	    String selectId = (String)ProjectUtil.get(hash,"selectId");
	 	    String cname = (String)ProjectUtil.get(hash,"cname");
	 	    String description = ProjectUtil.get(hash,"description");
	 	    int sort = ProjectUtil.getInt(hash,"sort");
	 	    String planStartDate = ProjectUtil.get(hash,"planStartDate");
	  	    String planEndDate = ProjectUtil.get(hash,"planEndDate");
	  	    String cmd = ProjectUtil.get(hash,"cmd");
	  	  
	 	    ReferenceFactory rf = new ReferenceFactory();
	 	    ScheduleNode selectNode = (ScheduleNode)rf.getReference(selectId).getObject();
	 	    
	 	    newNode = ETask.newETask();
	 	    
	 	    newNode.setDescription(description);
	 	    
	 	    Timestamp start = DateUtil.convertDate(planStartDate);
	  	    Timestamp end = DateUtil.convertDate(planEndDate);
	
	 	    newNode.setPlanStartDate(start);
	 	    newNode.setPlanEndDate(end);
	 	    newNode.setCreator(SessionHelper.manager.getPrincipalReference());
	 	    newNode.setStatus(STATEKEY.READY);
	 	    
	 	    ETask down = null;
	 	    
	 	    if(selectNode instanceof EProjectNode){
    	    	newNode.setParent(selectNode);
    	    	newNode.setProject((EProjectNode)selectNode);
    	    }else{
    	    	newNode.setParent(selectNode.getParent());
    	    	newNode.setProject(((ETaskNode)selectNode).getProject());
    	    	
    	    	if("next".equals(cmd)){
	     	    	down = ProjectHelper.manager.getDownMinTask((ETask)selectNode);
	     	    	if(down != null){
	     	    		sort = down.getSort();
		     	    	newNode.setSort(sort);
		     	    	ProjectHelper.service.updateSort(down, sort);
	     	    	}else{
	     	    		sort = TemplateHelper.manager.getMaxSeq(selectNode.getParent());
	     	    	}
    	    	}else{
    	    		sort = selectNode.getSort();
    	    		ProjectHelper.service.updateSort((ETask)selectNode, sort);
    	    	}
    	    }
    	    newNode.setSort(sort);
    	    newNode.setName(cname);
	 	    newNode = (ETaskNode)PersistenceHelper.manager.save(newNode);
	 	    
	    	ProjectTreeModel model = new ProjectTreeModel(newNode.getProject());
	    	model.setExecSchedule(newNode);
	    	trx.commit();
	        trx = null;

        } catch(Exception e) {
        	e.printStackTrace();
        	 throw new WTException(e);
        } finally {
            if(trx!=null){
                 trx.rollback();
            }
        }
        return newNode;
    }
    
    /**태스크 삭제
     * @param hash
     * @return
     * @throws Exception
     */
    
    @SuppressWarnings("rawtypes")
	@Override
    public  ArrayList deleteTask(Hashtable hash)throws Exception{

        Transaction trx = new Transaction();
        ArrayList list = null;
        ETaskNode node = null;
        try {
     	    trx.start();

     	    String selectChild = (String)ProjectUtil.get(hash,"selectChild");
   	    
     	   	ReferenceFactory rf = new ReferenceFactory();	    
     	   	node = (ETaskNode)rf.getReference(selectChild).getObject();
     	   
     	   	ScheduleNode parent = node.getParent();
     	   	ProjectHelper.service.delete(node);
     	 
	    	ProjectTreeModel model = new ProjectTreeModel(node.getProject());	
	    	ProjectTreeNode pnode = (ProjectTreeNode)model.nodeMap.get(parent.getPersistInfo().getObjectIdentifier().toString());

	    	if(pnode.getChildCount()>0){
	    		pnode = (ProjectTreeNode)pnode.getChildAt(0);
	    	}
	    	
	    	list = model.setExecSchedule((ScheduleNode)pnode.getUserObject());
	    	trx.commit();
	        trx = null;
	
	   } catch(Exception e) {
		   e.printStackTrace();
		   throw new WTException(e);
	   } finally {
	       if(trx!=null){
	            trx.rollback();
	    }
	   }
    	return list;
    }
    
    /**선행 태스크 정보 저장
     * @param hash
     * @throws Exception
     */
    
    @SuppressWarnings("unchecked")
	@Override
	public void setPreTask(Map<String, Object> reqMap) throws Exception {

		Transaction trx = new Transaction();

		try {
			trx.start();

			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			List<Map<String, Object>> items = (List<Map<String, Object>>) reqMap.get("items");

			ETask task = (ETask) CommonUtil.getObject(oid);

			List<PrePostLink> preTaskLinkList = ProjectHelper.manager.getPreTaskLinkList(task);

			for (PrePostLink preTaskLink : preTaskLinkList) {
				PersistenceHelper.manager.delete(preTaskLink);
			}

			for (Map<String, Object> item : items) {
				String preTaskOid = (String) item.get("oid");

				ETask preTask = (ETask) CommonUtil.getObject(preTaskOid);

				PrePostLink link = PrePostLink.newPrePostLink(preTask, task);
				PersistenceHelper.manager.save(link);
			}

			ScheduleNode root = (ScheduleNode) task.getProject();
			ProjectTreeModel model = new ProjectTreeModel(root);
			model.setPlanSchedule(root.getPlanStartDate());

			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}

    
    
    /**태스크 완료시
     * @param hash
     * @throws Exception
     */
    
    @Override
	public ETask workComplete(Map<String, Object> hash) throws Exception {

		ETask task = null;
		Transaction trx = new Transaction();
		String msg = "작업이 " + MESSAGEKEY.COMPLETE;
		try {
			trx.start();

			String startDate = ParamUtil.get(hash, "startDate");
			String endDate = ParamUtil.get(hash, "endDate");

			ReferenceFactory rf = new ReferenceFactory();
			String oid = ParamUtil.get(hash, "oid");
			ETaskNode node = (ETaskNode) rf.getReference(oid).getObject();

			node.setStartDate(DateUtil.convertDate(startDate));
			node.setEndDate(DateUtil.convertDate(endDate));
			node.setCompletion(100);

			node = (ETaskNode) PersistenceHelper.manager.modify(node);

			// 태스크 완료시 이벤트 발생
			workComplete(node);

			task = (ETask) node;

			trx.commit();
			trx = null;

		} catch (Exception e) {
			msg = e.getLocalizedMessage() + MESSAGEKEY.COMPLETE_ERROR;
			e.printStackTrace();
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return task;
	}
    
    /**태스크 완료시 발생
     * @param node
     * @throws Exception
     */
    
    @Override
    public void workComplete(ETaskNode node)throws Exception{
    	
    	node.setStatus(STATEKEY.COMPLETED);
    	
		node = (ETaskNode)PersistenceHelper.manager.modify(node);
		
		ProjectTreeModel model = new ProjectTreeModel(node.getProject());
		model.setComplete();

		EProject pjt = (EProject)node.getProject();
		if(STATEKEY.COMPLETED.equals(pjt.getState().toString())){
			//프로젝트 완료 이벤트
			ProjectMailBroker broker = new ProjectMailBroker();
//			broker.completeProject(node.getProject());
		}else{
			//후행 태스크 시작 이벤트 
			model.start();
		}
    }
    
    /**완료취소시 Task 상태 변경 및 종료일 삭제
     * @param node
     * @throws Exception
     */
    
    @Override
    public String cancelComplete(Map<String, Object> hash)throws Exception{

       Transaction trx = new Transaction();
       String msg = MESSAGEKEY.COMCANCEL;
       try {
    	    trx.start();
    	    String oid = ParamUtil.get(hash, "oid");
    	    ETaskNode node = (ETaskNode)CommonUtil.getObject(oid);
    	    node.setEndDate(null);
    	    node.setStatus(STATEKEY.PROGRESS);
	    	node = (ETaskNode)PersistenceHelper.manager.modify(node);
	    	
	    	trx.commit();
	        trx = null;
	        
	   } catch(Exception e) {
		   msg = e.getLocalizedMessage() + MESSAGEKEY.COMCANCEL_ERROR;
		   e.printStackTrace();
		   throw new WTException(e);
	   } finally {
	       if(trx!=null){
	            trx.rollback();
	    }
	   }
	return msg;
    }
    
    /** 프로젝트 결재요청시 결재객체 생성
     * @param hash
     * @param lifecycle
     * @return
     * @throws Exception
     */
    
    @Override
    public ProjectRegistApproval startProjectRequest(String oid)throws Exception{
    	
       Transaction trx = new Transaction();
       ProjectRegistApproval approval = null;
       try {
    	    trx.start();
    	    
			String lifecycle = "LC_Default";
    	    String location = FOLDERKEY.PROJECT;
			
			ReferenceFactory rf = new ReferenceFactory();
			EProject project = (EProject)rf.getReference(oid).getObject();
			
    	    QueryResult qr = PersistenceHelper.manager.navigate(project,"regist",ProjectRegistLink.class);
    	    
			if(qr.hasMoreElements()){
				approval = (ProjectRegistApproval)qr.nextElement();
			}else{
    	    
				approval = ProjectRegistApproval.newProjectRegistApproval();
				
				PDMLinkProduct pdmProduct = project.getProduct();
		    	WTContainerRef wtContainerRef = WTContainerRef.newWTContainerRef(pdmProduct);
		    	    
		    	LifeCycleTemplate lc = LifeCycleHelper.service.getLifeCycleTemplate(lifecycle, wtContainerRef);
		    	LifeCycleHelper.setLifeCycle(approval , lc);
				//LifeCycleManaged mg= LifeCycleHelper.setLifeCycle(approval , lc);
				
				approval.setProject(project);
		    	approval.setContainer(pdmProduct);
		    	    
				Folder folder = FolderTaskLogic.getFolder(location, wtContainerRef);
					
				FolderHelper.assignLocation((FolderEntry) approval, folder);
				
		    	approval = (ProjectRegistApproval)PersistenceHelper.manager.save(approval);    
			}
    	    trx.commit();
	        trx = null;
	        
	   } catch(Exception e) {
		   e.printStackTrace();
		   throw new WTException(e);
	   } finally {
	       if(trx!=null){
	            trx.rollback();
	    }
	   }
	return approval;
    }
	/** 프로젝트 정지
	 * @param hash
	 * @param loc
	 * @throws Exception
	 */
    
    @Override
	public String stopProject(Map<String, Object> hash)throws Exception{

       Transaction trx = new Transaction();
       String msg = MESSAGEKEY.STOP;
       try {
    	    trx.start();
    	    ReferenceFactory rf = new ReferenceFactory();
    	    
    	    String oid = ParamUtil.get(hash,"oid");
    	    String stopComment = ParamUtil.get(hash, "stopComment");
    	    EProject sg = (EProject)rf.getReference(oid).getObject();
    	    
    	    //sg.setStatus(STATEKEY.STOP);
    	    //LifeCycleUtil.setLifeCycleState(sg, STATEKEY.STOP);
    	    setState(sg, STATEKEY.STOP);
    	    
    	    sg = (EProject)PersistenceHelper.manager.modify(sg);
    	    
    	    ProjectStopHistory history = ProjectStopHistory.newProjectStopHistory();
    	    history.setHistoryComment(stopComment);
    	    history.setGubun("STOP");
    	    history.setProject(sg);
    	    
    	    history = (ProjectStopHistory)PersistenceHelper.manager.save(history);
    	    
			trx.commit();
			trx = null;
			
       } catch(Exception e) {
    	   msg = e.getLocalizedMessage()+ MESSAGEKEY.STOP_ERROR;
    	   e.printStackTrace();
    	   throw new WTException(e);
       } finally {
           if(trx!=null){
				trx.rollback();
		   }
       }
	return msg;
	}
	
	
	/**
	 * 프로젝트 재시작
	 * @param hash
	 * @param loc
	 * @throws Exception
	 */
    
    @Override
	public String restartProject(Map<String, Object> hash)throws Exception{

		Transaction trx = new Transaction();
		String msg = "재시작되었습니다.";
		try {
			trx.start();
			ReferenceFactory rf = new ReferenceFactory();

			String oid = ParamUtil.get(hash, "oid");
			String startComment = ParamUtil.get(hash, "startComment");

			//String primary = StringUtil.checkNull((String) hash.get("PRIMARY"));
			//List<String> secondary = StringUtil.checkReplaceArray(hash.get("SECONDARY"));

			EProject sg = (EProject) rf.getReference(oid).getObject();
			setState(sg, STATEKEY.PROGRESS);
			sg = (EProject) PersistenceHelper.manager.modify(sg);

			ProjectStopHistory history = ProjectStopHistory.newProjectStopHistory();
			history.setHistoryComment(startComment);
			history.setGubun("START");
			history.setProject(sg);

			history = (ProjectStopHistory) PersistenceHelper.manager.save(history);
			
//			String cacheId = primary.split("/")[0];
//		    String fileName = primary.split("/")[1];
//		    CachedContentDescriptor cacheDs = new CachedContentDescriptor(cacheId);
//			CommonContentHelper.service.attach((ContentHolder) history, cacheDs, fileName, null, ContentRoleType.PRIMARY);

			trx.commit();
			trx = null;

		} catch (Exception e) {
			msg = e.getLocalizedMessage() + MESSAGEKEY.RESTART_ERROR;
			e.printStackTrace();
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return msg;
	}
    
    /**프로젝트 체크아웃
     * @param hash
     * @return
     * @throws Exception
     */
    
    @Override
    public EProject checkoutProject(Map<String, Object> hash)throws Exception{

       Transaction trx = new Transaction();
       EProject project = null;
       try {
    	    trx.start();
    	    ReferenceFactory rf = new ReferenceFactory();
    	    
    	    String oid = ParamUtil.get(hash,"oid");
    	  
    	    EProject org = (EProject)rf.getReference(oid).getObject();
    	    
    	    project = EProject.newEProject();
    	    copyAttribute(org, project);
    	    project.setLastVersion(true);
        	Folder folder = FolderTaskLogic.getFolder(FOLDERKEY.PROJECT, WCUtil.getWTContainerRef());
            FolderHelper.assignLocation((FolderEntry)project, folder);
    	    
    	    project = (EProject)PersistenceHelper.manager.save(project);
    	    copyProject(org, project);
    	    
    	    org.setLastVersion(false);
    	    PersistenceHelper.manager.modify(org);
    	    
    	    setState(project, STATEKEY.MODIFY);
    	    
    	    ContentHelper.service.copyContent(org, project);
    	    
    	 /// stop history link move ///
        	QuerySpec qs = new QuerySpec();
    	    int ii = qs.addClassList(ProjectStopHistory.class,true);
    	    qs.appendWhere(new SearchCondition(ProjectStopHistory.class,"projectReference.key.id","=",org.getPersistInfo().getObjectIdentifier().getId()),new int[]{ii});
    	    QueryResult historyQr = PersistenceHelper.manager.find(qs);
    	    while(historyQr.hasMoreElements()){
    	    	Object[] o2 = (Object[])historyQr.nextElement();
    			ProjectStopHistory history = (ProjectStopHistory)o2[0];
    			history.setProject(project);
    			PersistenceHelper.manager.modify(history);
    	    }
        	
			trx.commit();
			trx = null;
			
       } catch(Exception e) {
    	   e.printStackTrace();
    	   throw new WTException(e);
       } finally {
           if(trx!=null){
				trx.rollback();
		   }
       }
       return project;
	}
    
    /**프로젝트 속성 복사
     * @param org
     * @param project
     * @throws Exception
     */
    private void copyAttribute(EProject org, EProject project)throws Exception{
    	project.setCode(org.getCode());
    	project.setCompletion(org.getCompletion());
    	project.setDescription(org.getDescription());
    	project.setEndDate(org.getEndDate());
    	project.setLastVersion(org.isLastVersion());
    	project.setManDay(org.getManDay());
    	project.setName(org.getName());
    	project.setOutputType(org.getOutputType());
    	project.setParent(org.getParent());
    	project.setPlanEndDate(org.getPlanEndDate());
    	project.setPlanStartDate(org.getPlanStartDate());
    	project.setProduct(org.getProduct());
    	project.setStartDate(org.getStartDate());
    	project.setState(org.getState());
    	project.setTemplate(org.getTemplate());
    	project.setVersion(org.getVersion());
    	
    	project.setState(org.getState());
    	project.setProjectType(org.getProjectType());
    	
    	project.setGroupCode(org.getGroupCode());
    	project.setMaterialCode(org.getMaterialCode());
    	project.setLevelCode(org.getLevelCode());
    }
    
    /**체크인시
     * @param hash
     * @throws Exception
     */
    @Override
    public HashMap<String, Object> checkinProject(Map<String, Object> hash)throws Exception{

    	
        Transaction trx = new Transaction();
        HashMap<String, Object> returnMap = new HashMap<String, Object>();
        SessionContext prev = SessionContext.newContext();
        try {
    	    trx.start();
    	    ReferenceFactory rf = new ReferenceFactory();
    	    SessionHelper.manager.setAdministrator();
    	    String oid = ParamUtil.get(hash,"oid");
    	    String comment = ParamUtil.get(hash,"comment");
    	    
    	    EProject sg = (EProject)rf.getReference(oid).getObject();
    	    
    	    sg.setCheckInComment(comment);
	    	sg.setVersion(sg.getVersion()+1);
	    	
    	    PersistenceHelper.manager.modify(sg);
    	    
	    	QueryResult qr = PersistenceHelper.manager.navigate(sg,"regist",ProjectRegistLink.class);
	    	    
			if(qr.hasMoreElements()){
				ProjectRegistApproval approval = (ProjectRegistApproval)qr.nextElement();
				WFItemHelper.service.deleteWFItem(approval);
				PersistenceHelper.manager.delete(approval);
			}
			
			ProjectTreeModel model = new ProjectTreeModel(sg);
			model.setCompletion();
			model.start();
			
			double compleLange = sg.getCompletion();
			System.out.println("compleLange : " + compleLange);
			if(compleLange != 100.0) {
				E3PSWorkflowHelper.manager.changeLCState(sg,STATEKEY.PROGRESS);
			}else {
				E3PSWorkflowHelper.manager.changeLCState(sg,STATEKEY.COMPLETED);
			}
			 
	    	returnMap.put("oid", sg.getPersistInfo().getObjectIdentifier().toString());
	    	returnMap.put("url", "/Windchill/worldex/project/viewMain");
	    	returnMap.put("msg", MESSAGEKEY.CHECKIN);
	    	returnMap.put("isApp", "false");
    	    
			trx.commit();
			trx = null;
			
			//ERP 전송
			//ERPInterface.send(sg);
			
       } catch(Exception e) {
    	   e.printStackTrace();
    	   throw new WTException(e);
       } finally {
           if(trx!=null){
				trx.rollback();
		   }
           SessionContext.setContext(prev);
       }
    return returnMap;
	}
    
    /**체크아웃 취소
     * @param hash
     * @return
     * @throws Exception
     */
    
    @Override
    public EProject undoCheckoutProject(Map<String, Object> hash)throws Exception{

       Transaction trx = new Transaction();
       EProject project = null;
       SessionContext prev = SessionContext.newContext();
       try {
    	    trx.start();
    	    ReferenceFactory rf = new ReferenceFactory();
    	    SessionHelper.manager.setAdministrator();
    	    String oid = ParamUtil.get(hash,"oid");

    	    project = (EProject)rf.getReference(oid).getObject();
    	    int version = project.getVersion();
    	    
    	    QueryResult registqr = PersistenceHelper.manager.navigate(project,"regist",ProjectRegistLink.class);
    	    
			if(registqr.hasMoreElements()){
				ProjectRegistApproval approval = (ProjectRegistApproval)registqr.nextElement();
				WFItemHelper.service.deleteWFItem(approval);
				PersistenceHelper.manager.delete(approval);
			}
    	    
    	    QuerySpec qs = new QuerySpec(EProject.class);
    	    qs.appendWhere(new SearchCondition(EProject.class,EProject.CODE,"=",project.getCode()),new int[]{0});
    	    qs.appendAnd();
    	    qs.appendWhere(new SearchCondition(EProject.class,EProject.VERSION,"=",version),new int[]{0});
    	    qs.appendAnd();
    	    qs.appendOpenParen();
    	    qs.appendWhere(new SearchCondition(EProject.class,"state.state","=",STATEKEY.PROGRESS),new int[]{0});
    	    qs.appendOr();
    	    qs.appendWhere(new SearchCondition(EProject.class,"state.state","=",STATEKEY.COMPLETED),new int[]{0});
    	    qs.appendCloseParen();
    	    
    	    QueryResult qr = PersistenceHelper.manager.find(qs);
    	    
    	    if(qr.size()==0){
    	    	throw new WTException("오리지날 객체를 찾을 수 없습니다.");
    	    }
    	    
    	    EProject org = (EProject)qr.nextElement();
    	   
    	    /// stop history link move ///
        	QuerySpec historyQs = new QuerySpec();
    	    int ii = historyQs.addClassList(ProjectStopHistory.class,true);
    	    historyQs.appendWhere(new SearchCondition(ProjectStopHistory.class,"projectReference.key.id","=",project.getPersistInfo().getObjectIdentifier().getId()),new int[]{ii});
    	    QueryResult historyQr = PersistenceHelper.manager.find(historyQs);
    	    while(historyQr.hasMoreElements()){
    	    	Object[] o2 = (Object[])historyQr.nextElement();
    			ProjectStopHistory history = (ProjectStopHistory)o2[0];
    			history.setProject(org);
    			PersistenceHelper.manager.modify(history);
    	    }
    	    
    	    delete(project);
    	    double completeLange = org.getCompletion();
    	    if(completeLange != 100.0) {
    	    	setState(org, STATEKEY.PROGRESS);
    	    }
    	    org.setLastVersion(true);
    	    project = (EProject)PersistenceHelper.manager.modify(org);
    	    
			trx.commit();
			trx = null;
			
			
       } catch(Exception e) {
    	   e.printStackTrace();
    	   throw new WTException(e);
       } finally {
           if(trx!=null){
				trx.rollback();
		   }
           SessionContext.setContext(prev);
       }
       return project;
	}
    
    
    /**준비중 && 진행중인 Node가 있는지 확인
     * @param node
     * @return
     * @throws Exception
     */
    
    @Override
    public boolean isPreComplete(ScheduleNode node)throws Exception{
    	QuerySpec qs = new QuerySpec();
    	QueryResult qr = null;
    	
    	if(node instanceof EProject){
    		EProject pjt = (EProject)node;
    		int ii = qs.addClassList(EProject.class, false);
	    	int jj = qs.addClassList(PrePostLink.class, true);
	    	qs.appendOpenParen();
	    	qs.appendWhere(new SearchCondition(EProject.class,"state.state","=",STATEKEY.READY),new int[]{ii});
	    	qs.appendOr();
	    	qs.appendWhere(new SearchCondition(EProject.class,"state.state","=",STATEKEY.PROGRESS),new int[]{ii});
	    	qs.appendCloseParen();
	    	qs.appendAnd();
	    	qs.appendWhere(new SearchCondition(EProject.class,"thePersistInfo.theObjectIdentifier.id",PrePostLink.class,"roleAObjectRef.key.id"),new int[]{ii,jj});
	    	qs.appendAnd();
	    	qs.appendWhere(new SearchCondition(PrePostLink.class,"roleBObjectRef.key.id","=",pjt.getPersistInfo().getObjectIdentifier().getId()),new int[]{jj});
	    	qr = PersistenceHelper.manager.find(qs);
	    	
    	}else{
	    	ETaskNode task = (ETaskNode)node;
	    	int ii = qs.addClassList(ETaskNode.class, false);
	    	int jj = qs.addClassList(PrePostLink.class, true);
	    	qs.appendOpenParen();
	    	qs.appendWhere(new SearchCondition(ETaskNode.class,ETaskNode.STATUS,"=",STATEKEY.READY),new int[]{ii});
	    	qs.appendOr();
	    	qs.appendWhere(new SearchCondition(ETaskNode.class,ETaskNode.STATUS,"=",STATEKEY.PROGRESS),new int[]{ii});
	    	qs.appendCloseParen();
	    	qs.appendAnd();
	    	qs.appendWhere(new SearchCondition(ETaskNode.class,"thePersistInfo.theObjectIdentifier.id",PrePostLink.class,"roleAObjectRef.key.id"),new int[]{ii,jj});
	    	qs.appendAnd();
	    	qs.appendWhere(new SearchCondition(PrePostLink.class,"roleBObjectRef.key.id","=",task.getPersistInfo().getObjectIdentifier().getId()),new int[]{jj});
	    	qr = PersistenceHelper.manager.find(qs);
    	}
    	return qr.size()==0;
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    public boolean isLastNode(ScheduleNode node)throws Exception{
    	ArrayList list = ProjectDao.manager.getStructure(node.getPersistInfo().getObjectIdentifier().getId(), 1);
    	
    	return list.size()==0;
    }
    
    
    @Override
    public QueryResult getAllProgressProject()throws Exception{
    	QuerySpec qs = new QuerySpec();

    	int ii = qs.addClassList(EProject.class,true);
    	qs.appendWhere(new SearchCondition(EProject.class,"lastVersion",SearchCondition.IS_TRUE),new int[]{0});
        qs.appendAnd();
        qs.appendWhere(new SearchCondition(EProject.class,"state.state","=",STATEKEY.PROGRESS),new int[]{0});
    	
    	return PersistenceHelper.manager.find(qs);
    }

    
    @SuppressWarnings("rawtypes")
	@Override
    public ETaskNode upTask(Hashtable hash)throws Exception{

        Transaction trx = new Transaction();
        
        ETaskNode node = null;
        try {
     	    trx.start();
     	    
			String oid = (String) ProjectUtil.get(hash, "selectId");

			ReferenceFactory rf = new ReferenceFactory();

			ScheduleNode parent = (ScheduleNode) rf.getReference(oid).getObject();
			long sid = parent.getPersistInfo().getObjectIdentifier().getId();
			ArrayList list = ProjectDao.manager.getStructure(sid, 1);

			String selectChild = ProjectUtil.get(hash, "selectChild");
			node = (ETaskNode) rf.getReference(selectChild).getObject();
			for (int i = list.size() - 1; i >= 0; i--) {
				String[] s = (String[]) list.get(i);
				if (s[2].equals(selectChild)) {
					if (i - 1 >= 0) {
						String[] d = (String[]) list.get(i - 1);
						ScheduleNode up = (ScheduleNode) rf.getReference(d[2]).getObject();
						int upSeq = up.getSort();
						up.setSort(node.getSort());
						node.setSort(upSeq);
						node = (ETaskNode) PersistenceHelper.manager.modify(node);
						PersistenceHelper.manager.modify(up);
					}
					break;
				}
			}
	    	
	    	trx.commit();
	        trx = null;
	
	   } catch(Exception e) {
		   e.printStackTrace();
		   throw new WTException(e);
	   } finally {
	       if(trx!=null){
	            trx.rollback();
	    }
	   }
    	return node;
    }
   
    @SuppressWarnings("rawtypes")
	@Override
    public ETaskNode downTask(Hashtable hash)throws Exception{

    	Transaction trx = new Transaction();
        
        ETaskNode node = null;
        try {
     	    trx.start();
     	    
			String oid = (String) ProjectUtil.get(hash, "selectId");

			ReferenceFactory rf = new ReferenceFactory();

			ScheduleNode parent = (ScheduleNode) rf.getReference(oid).getObject();
			long sid = parent.getPersistInfo().getObjectIdentifier().getId();
			ArrayList list = ProjectDao.manager.getStructure(sid, 1);

			String selectChild = ProjectUtil.get(hash, "selectChild");
			node = (ETaskNode) rf.getReference(selectChild).getObject();

			for (int i = 0; i < list.size(); i++) {
				String[] s = (String[]) list.get(i);
				if (s[2].equals(selectChild)) {

					if ((i + 1) < list.size()) {
						String[] d = (String[]) list.get(i + 1);
						ScheduleNode down = (ScheduleNode) rf.getReference(d[2]).getObject();
						int downSeq = down.getSort();
						down.setSort(node.getSort());
						node.setSort(downSeq);
						node = (ETaskNode) PersistenceHelper.manager.modify(node);
						PersistenceHelper.manager.modify(down);
					}
					break;
				}
			}
	    	
	    	trx.commit();
	        trx = null;
	
	   } catch(Exception e) {
		   e.printStackTrace();
		   throw new WTException(e);
	   } finally {
	       if(trx!=null){
	            trx.rollback();
	    }
	   }
    	return node;
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    public String editMember(Map<String, Object>  hash)throws Exception{

       Transaction trx = new Transaction();
       String msg = MESSAGEKEY.UPDATE;
       
       try {
    	    trx.start();
    	    
    	    String oid = ParamUtil.get(hash,"oid");
    	    ReferenceFactory rf = new ReferenceFactory();
    	    String creator = ParamUtil.get(hash, "creator");
//    	    LOGGER.info("oid -> "+oid);
//    	    LOGGER.info("creator -> "+creator);
    		EProjectNode node = (EProjectNode)rf.getReference(oid).getObject();
    		ArrayList members = ProjectMemberHelper.service.getUserList(node);
    		
    		for(int i=0; i< members.size(); i++){
	   			 Object[] o = (Object[])members.get(i);
	   			 ProjectRole role = (ProjectRole)o[0];
	   			 RoleUserLink ulink = (RoleUserLink)o[1];
	   			 
//	   			 String userId = ParamUtil.get(hash, role.getCode());
	   			 String userId = ParamUtil.get(hash, role.getCode());
	   			 if(userId != null && userId.length() > 0){
		   			 WTUser newUser = null;
						try{
							People pp = (People)CommonUtil.getObject(userId);
							newUser = pp.getUser();
							 LOGGER.info("newUser -> "+newUser);
						}catch(Exception ex){}
		   			
		   			 if(ulink!=null){
			   				WTUser user = ulink.getUser();
			   				if(!user.getName().equals(userId)){
			   					if(newUser==null){
			   						PersistenceHelper.manager.delete(ulink);
			   					}else{
			   						ulink.setUser(newUser);
			   						PersistenceHelper.manager.modify(ulink);
			   					}
			   				}
		   			 }else{
		   				if(newUser!=null){
		   					ulink = RoleUserLink.newRoleUserLink(newUser, role);
		   					PersistenceHelper.manager.save(ulink);
		   				}
		   			 }
	   			 }else{
	   				 if(ulink != null){
	   					 PersistenceHelper.manager.delete(ulink);
	   				 }
	   			 }
    		}

            trx.commit();
            trx = null;

       } catch(Exception e) {
           e.printStackTrace();
           msg = e.getLocalizedMessage()+ MESSAGEKEY.UPDATE_ERROR;
           throw new WTException(e);
       } finally {
           if(trx!=null){
                trx.rollback();
           }
       }
	return msg;
    }
    
    
	@SuppressWarnings("rawtypes")
	@Override
    public String editMember2(Map<String, Object>  hash)throws Exception{
       Transaction trx = new Transaction();
       String msg = MESSAGEKEY.UPDATE;
       
       try {
    	    trx.start();
    	    
    	    String oid = ParamUtil.get(hash,"oid"); //project:116144
    	    ReferenceFactory rf = new ReferenceFactory();
    		EProjectNode node = (EProjectNode)rf.getReference(oid).getObject(); //project:116144
    		
    		ArrayList memberRole = ProjectMemberHelper.service.getProjectUserList(node);
    		WTSet delSet = new WTHashSet();
    		for(Object link : memberRole) {
	   			Object[] o = (Object[])link;
	   			 
	   			RoleUserLink ulink = (RoleUserLink)o[1];
	   			System.out.println("ulink ::: " + ulink);
	   			if(ulink != null) {
	   				ProjectRole role = ulink.getRoleBObject() != null ? (ProjectRole)ulink.getRoleBObject() : null;
//	   				if(role != null && role.getName().indexOf("PM") > -1) {
//	   					System.out.println("role ::: " + role);
//	   					continue;
//	   				}
	   				delSet.add(ulink);
	   			}
    		}
    		PersistenceHelper.manager.delete(delSet);
    		
    		WTCollection newList = new WTArrayList();
    		for(Entry<String, Object> entry : hash.entrySet()) {
    			
    			String hashKey = entry.getKey();
    			String hashValue = entry.getValue().toString();
    			
    			if(hashKey.startsWith("PR") && hashKey.length() == 4) {
    				List<String> userList = StringUtil.checkReplaceArray(hash.get(hashKey));
    				
    				ProjectRole role = ProjectHelper.manager.getProjectRoleByCode(node, hashKey);
    				
    				for(String peopleOid : userList) {
    					People people = (People)CommonUtil.getObject(peopleOid);
    					WTUser wtUser = people.getUser();
    					if(wtUser != null && role != null) {
    						RoleUserLink newLink = RoleUserLink.newRoleUserLink(wtUser, role);
    						newList.add(newLink);
    					}
    				}
    				
    			}
    		}
    		
    		if(newList.size() > 0) {
    			PersistenceHelper.manager.save(newList);
    		}
    		
            trx.commit();
            trx = null;

       } catch(Exception e) {
           e.printStackTrace();
           msg = e.getLocalizedMessage()+ MESSAGEKEY.UPDATE_ERROR;
           throw new WTException(e);
       } finally {
           if(trx!=null){
                trx.rollback();
           }
       }
	return msg;
    }
    
    @Override
    public ETask saveCompletion(Map<String, Object> hash)throws WTException{
    	
		ETask task = null;

		Transaction trx = new Transaction();
		String msg = MESSAGEKEY.UPDATE;
		try {
			trx.start();
			String oid = ParamUtil.get(hash, "oid");
			ETaskNode node = (ETaskNode) CommonUtil.getObject(oid);
			String cc = ParamUtil.get(hash, "completion");
			double completion = 0;
			try {
				completion = Double.parseDouble(cc);
			} catch (Exception ex) {
			}

			if (completion > 100)
				completion = 100;
			node.setCompletion(completion);
			node = (ETaskNode) PersistenceHelper.manager.modify(node);

			ProjectTreeModel model = new ProjectTreeModel(node.getProject());
			model.setCompletion();

			task = (ETask) node;
			
			trx.commit();
			trx = null;

		} catch (Exception e) {
			msg = e.getLocalizedMessage() + MESSAGEKEY.UPDATE_ERROR;
			e.printStackTrace();
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return task;
	}
    
    @Override
	public ETask changeStartDate(Map<String, Object> hash) throws Exception {

		ETask task = null;
		Transaction trx = new Transaction();
		String msg = MESSAGEKEY.UPDATE;
		try {
			trx.start();

			String startDate = ParamUtil.get(hash, "startDate");
			String oid = ParamUtil.get(hash, "oid");

			ReferenceFactory rf = new ReferenceFactory();
			ETaskNode node = (ETaskNode) rf.getReference(oid).getObject();

			node.setStartDate(DateUtil.convertDate(startDate));
			node = (ETaskNode) PersistenceHelper.manager.modify(node);

			ProjectTreeModel model = new ProjectTreeModel(node.getProject());
			model.setStartDate();

			task = (ETask) node;

			trx.commit();
			trx = null;

		} catch (Exception e) {
			msg = e.getLocalizedMessage() + MESSAGEKEY.UPDATE_ERROR;
			e.printStackTrace();
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return task;
	}
    
    /**
     * 프로젝트 스케줄
     * @throws Exception
     */
    
    @SuppressWarnings("rawtypes")
	@Override
    public void runProjectSchedule()throws Exception{
    	

       Transaction trx = new Transaction();
       try {
    	    trx.start();
    	    
    	    QueryResult qr = getAllProgressProject();
        	
    	    ArrayList list = new ArrayList();
        	
        	while(qr.hasMoreElements()){
        		Object[] o = (Object[])qr.nextElement();
        		EProject project = (EProject)o[0];
        		ProjectTreeModel model = new ProjectTreeModel(project);
        		list.add(model);
        		
        	}
        	sendAllProgress(list);	// 프로젝트 진행
        	sendWarningNoti(list);  // 지연태스크 알림
        	sendEndDateNoti(list);  // 종료3일전 알림
        	
			trx.commit();
			trx = null;
			
       } catch(Exception e) {
    	   e.printStackTrace();
    	   throw new WTException(e);
       } finally {
           if(trx!=null){
				trx.rollback();
		   }
       }
    }
    /**
     * 프로젝트 진행
     * @param list
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
	private void sendAllProgress(ArrayList list)throws Exception{
    	for(int i=0; i< list.size(); i++){
    		ProjectTreeModel model = (ProjectTreeModel)list.get(i);
    		model.start();
    	}
    }
    /**
     * 지연 태스크 메일 발송
     * @param list
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void sendWarningNoti(ArrayList list)throws Exception{
    	
    	Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DATE, -1);
		String yesterday = DateUtil.getDateString(ca.getTime(), "d");
		
		ArrayList delayedTask = new ArrayList();
		
    	for(int i=0; i< list.size(); i++){
    		ProjectTreeModel model = (ProjectTreeModel)list.get(i);
    		
    		for(int j=0; j< model.size(); j++){
    			ScheduleNode sn = model.get(j);
    			
    			if(sn instanceof ETaskNode){
    				ProjectTreeNode node = (ProjectTreeNode)model.nodeMap.get(sn.getPersistInfo().getObjectIdentifier().toString());
    				ETask task = (ETask)sn;
    				if(node.getChildCount()==0 && STATEKEY.PROGRESS.equals(task.getStatus())){
    				
    					String planEnd = DateUtil.getDateString(sn.getPlanEndDate(),"d");
    					if(yesterday.compareTo(planEnd)==0){
    						delayedTask.add(sn);
    					}
    				}
    			}
    		}
    	}
    	
    	ProjectMailBroker broker = new ProjectMailBroker();
    	broker.delayedTask(delayedTask);
    }
    
    /** 프로젝트 종료 3일전 알림 발송
     * @param list
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void sendEndDateNoti(ArrayList list)throws Exception{
    	Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DATE, 2);
		String third = DateUtil.getDateString(ca.getTime(), "d");
		
		ArrayList endDateNoti = new ArrayList();
		
    	for(int i=0; i< list.size(); i++){
    		ProjectTreeModel model = (ProjectTreeModel)list.get(i);
    		
    		for(int j=0; j< model.size(); j++){
    			ScheduleNode sn = model.get(j);
    			
    			if(sn instanceof ETaskNode){
    				ProjectTreeNode node = (ProjectTreeNode)model.nodeMap.get(sn.getPersistInfo().getObjectIdentifier().toString());
    				ETask task = (ETask)sn;
    				if(node.getChildCount()==0 && STATEKEY.PROGRESS.equals(task.getStatus())){
    				
    					String planEnd = DateUtil.getDateString(sn.getPlanEndDate(),"d");
    					if(third.compareTo(planEnd)==0){
    						endDateNoti.add(sn);
    					}
    				}
    			}
    		}
    	}
    	
    	ProjectMailBroker broker = new ProjectMailBroker();
    	broker.endDateNoti(endDateNoti);
    }
    
    
    
    /**
     *  사용안함.
     * @param oid
     * @return
     * @throws Exception
     */
    
    @SuppressWarnings("rawtypes")
	@Override
    public StringBuffer getPreTaskTree(String oid, String selectChild, ArrayList list, ProjectTreeModel model) throws Exception{

    	StringBuffer sb = new StringBuffer();
		QuerySpec query = new QuerySpec();
		QueryResult qr = null;
		
		int idx = query.addClassList(ETask.class, true);
		long loid = CommonUtil.getOIDLongValue(oid);
		
		query.appendWhere(new SearchCondition(ETask.class, "parentReference.key.id", SearchCondition.EQUAL, loid), new int[]{idx});
		query.appendOrderBy(new OrderBy(new ClassAttribute(ETask.class,"sort"), false), new int[] { idx });  
		
		qr = PersistenceHelper.manager.find(query);
		boolean enable = true;
		ScheduleNode parent = (ScheduleNode)CommonUtil.getObject(oid);
		while(qr.hasMoreElements()){
			Object o[] = (Object[])qr.nextElement();
			ETask task = (ETask)o[0];
			String taskOid = task.getPersistInfo().getObjectIdentifier().toString();
			
			if(taskOid.equals(selectChild)){
				enable=false;
			}
				
			ProjectTreeNode treeNode = (ProjectTreeNode)model.nodeMap.get(taskOid);
			String chk = "";
			if(!(enable && treeNode.getChildCount()==0)) {
				chk = "nocheckbox='1' ";
			}
			if(list.contains(taskOid)){
				chk = " checked='1' ";
			}
			sb.append("<item "+chk+"text='"+task.getName()+"' id='"+taskOid+"'>");
			
			sb.append(getPreTaskTree(taskOid, selectChild, list, model));
			
			sb.append("</item>");
			
		}
		return sb;
	}
    
    /**선행 태스크 정보 저장
     * @param hash
     * @throws Exception
     */
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public ArrayList setPreTask2(Hashtable hash)throws Exception{

       Transaction trx = new Transaction();
       
       ScheduleNode root = null;
       ArrayList newlist = new ArrayList();
       ArrayList list = null;
       
       try {
    	    trx.start();
    	    
    	    String oid = ProjectUtil.get(hash,"oid");
    	    String selectChild = ProjectUtil.get(hash,"selectChild");
    	    
    	    ReferenceFactory rf = new ReferenceFactory();
    	    ScheduleNode parent = (ScheduleNode)rf.getReference(oid).getObject();
    	    ETaskNode child = (ETaskNode)rf.getReference(selectChild).getObject();
    	    
    		QueryResult qr = PersistenceHelper.manager.navigate(child,"pre",PrePostLink.class,false);

    		Hashtable map = new Hashtable();
    		while(qr.hasMoreElements()){
    			PrePostLink link = (PrePostLink)qr.nextElement();
    			ETaskNode sgnode = link.getPre();
    			map.put(sgnode.getPersistInfo().getObjectIdentifier().toString(),link);
    		}
    		
    		String preTask = (String)hash.get("preTask");
    		String pre[] = preTask.split(",");
    		
    		for(int i=0; pre!=null && i< pre.length; i++){
    			
    			if(CommonUtil.getObject(pre[i]) != null){
    				
	    			if(pre[i] != null && pre[i].length() > 0){
		    			if(map.containsKey(pre[i])){
		    				map.remove(pre[i]);
		    				continue;
		    			}
		    			
		    			ETaskNode node = (ETaskNode)rf.getReference(pre[i]).getObject();
		    			
		    			PrePostLink link = PrePostLink.newPrePostLink(node,child);
		    			PersistenceHelper.manager.save(link);
		    			
	    			}
    			}
    		}
    		
    		Enumeration en = map.keys();
    		while(en.hasMoreElements()){
    			String ee = (String)en.nextElement();
    			PrePostLink nn = (PrePostLink)map.get(ee);
    			PersistenceHelper.manager.delete(nn);
    		}
    		
    		root = (ScheduleNode)rf.getReference(oid).getObject();
    		
    		for(int i=0; pre!=null && i< pre.length; i++){
				if(pre[i] != null && pre[i].length() > 0){

					if(CommonUtil.getObject(pre[i]) != null){
						
						ProjectTreeModel model = new ProjectTreeModel(root);
		        		ScheduleNode node = (ScheduleNode)rf.getReference( pre[i] ).getObject();
		        		
		    			list = model.setExecSchedule(node);
		    			
		    			/*for(int j=0; list != null && i<list.size(); i++){
		    				ScheduleNode scnode = (ScheduleNode)list.get(j);
		    				relist.add(scnode);		
		    			}*/
					}
        		}
    		}
    		
    		/*
    		// HashSet 데이터 형태로 생성되면서 중복 제거됨
    		HashSet hs = new HashSet(relist);
    		
    		// ArrayList 형태로 다시 생성
    		newlist = new ArrayList(hs);
    		LOGGER.info("리스트 사이즈 2 : " + newlist.size());
    		*/
            trx.commit();
            trx = null;

       } catch(Exception e) {
    	   e.printStackTrace();
    	   throw new WTException(e);
       } finally {
           if(trx!=null){
                trx.rollback();
           }
       }
       return list;
    }
    
    /**
     * Role 유저링크시
     * @param hash
     * @return
     * @throws Exception
     */
    
    @SuppressWarnings("rawtypes")
	@Override
    public ScheduleNode setRoleUserLink(Hashtable hash)throws Exception{

       Transaction trx = new Transaction();
       
       ScheduleNode node = null;
       try {
    	    trx.start();
    	    
    	    ReferenceFactory rf = new ReferenceFactory();
    	    String oid = ProjectUtil.get(hash,"oid");
    	    
    	    node = (ScheduleNode)rf.getReference(oid).getObject();

    	    String roleOid = ProjectUtil.get(hash,"roleOid");
    	    String roleUser = ProjectUtil.get(hash,"roleUser");
    	    
    	    WTUser user = (WTUser)CommonUtil.getObject(roleUser);
    	    ProjectRole role = (ProjectRole)CommonUtil.getObject(roleOid);;
    	    RoleUserLink link = null;
    	    
    	    QuerySpec qc = new QuerySpec();
    	    int idx = qc.appendClassList(RoleUserLink.class, true);
    	    qc.appendWhere(new SearchCondition(RoleUserLink.class, "roleBObjectRef.key.id", "=", CommonUtil.getOIDLongValue(roleOid)), new int[]{idx});
    	    
    	    QueryResult qr = PersistenceHelper.manager.find(qc);
    	    
    	    if(qr.size() == 0){
    	    	
    	    	link = RoleUserLink.newRoleUserLink(user, role);
				PersistenceHelper.manager.save(link);
				
    	    }else{
    	    	
	    	    while(qr.hasMoreElements()){
	    	    	Object[] o = (Object[])qr.nextElement();
	    	    	link = (RoleUserLink)o[0];
	    	    	link.setRole(role);
	    	    	link.setUser(user);
	    	    	PersistenceHelper.manager.modify(link);
	    	    }
    	    }
    	    
            trx.commit();
            trx = null;

       } catch(Exception e) {
    	   e.printStackTrace();
    	   throw new WTException(e);
       } finally {
           if(trx!=null){
                trx.rollback();
        }
       }
        return node;
    }
    
    /** 태스크 시작
     * @param hash
     * @return
     * @throws Exception
     */
    
    @Override
    public ETask startTask(String oid)throws Exception{

    	ETask task = null;
    	
		Transaction trx = new Transaction();

		try {
			trx.start();

			task = (ETask) CommonUtil.getObject(oid);
			task.setStatus(STATEKEY.PROGRESS);
			String date = DateUtil.getToDay();
			task.setStartDate(DateUtil.convertDate(date));
			task = (ETask) PersistenceHelper.manager.modify(task);

			//하위 태스크 시작 시 상위 태스크 시작으로 변경하는 로직 추가
			setStartParentTask(date, task);
			
			oid = CommonUtil.getOIDString(task);

			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return task;
    }
    
    private void setStartParentTask(String date, ScheduleNode node) throws Exception {
    	ScheduleNode parent = node.getParent();
    	if(parent != null) {
    		if(parent instanceof ETask) {
    			ETask parentTask = (ETask) parent;
    			if(STATEKEY.READY.equals(parentTask.getStatus())) {
    				parentTask.setStatus(STATEKEY.PROGRESS);
    				parentTask.setStartDate(DateUtil.convertDate(date));
    				parentTask = (ETask) PersistenceHelper.manager.modify(parentTask);
    			}
    			setStartParentTask(date, parent);
    		}
    	}
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ETask updateTaskAction(Map map) {
    	ETask task = null;
		Transaction trx = new Transaction();
		String msg = MESSAGEKEY.UPDATE;
		try {
			trx.start();
			task = (ETask) CommonUtil.getObject(ParamUtil.get(map, "oid"));
			task.setName(ParamUtil.get(map, "name"));
			task.setDescription(ParamUtil.get(map, "description"));
			PersistenceHelper.manager.modify(task);
			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getLocalizedMessage() + MESSAGEKEY.UPDATE_ERROR;
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return task;
	}
    
	@Override
	public EProject getProjectByName(String name) throws WTException {
		name = name.trim();
		
		if("".equals(name) || name.length() < 1){
			return null;
		}
		
		QuerySpec qs = new QuerySpec(EProject.class);
		qs.appendWhere(new SearchCondition(EProject.class, "name", "=", name), new int[] { 0 });
		QueryResult qr = PersistenceHelper.manager.find((StatementSpec) qs);
		
		EProject project = null;
		if (qr.hasMoreElements()) {
			project = (EProject) qr.nextElement();
		}
		
		return project;
	}
	
	@Override
	public EProject getProjectByCode(String code) throws WTException {
		code = code.trim();
		
		if("".equals(code) || code.length() < 1){
			return null;
		}
		
		QuerySpec qs = new QuerySpec(EProject.class);
		qs.appendWhere(new SearchCondition(EProject.class, "code", "=", code), new int[] { 0 });
		QueryResult qr = PersistenceHelper.manager.find((StatementSpec) qs);
		
		EProject project = null;
		if (qr.hasMoreElements()) {
			project = (EProject) qr.nextElement();
		}
		
		return project;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void createMasterLink(Map<String, Object> hash) throws Exception{
		String oid = ParamUtil.get(hash, "oid");
		List<Map<String, Object>> list = (List<Map<String, Object>>) hash.get("list");
		
		EProject project = (EProject)CommonUtil.getObject(oid);
		for(Map<String, Object> objMap : list) {
			String objOid = (String) objMap.get("oid");
			RevisionControlled per = (RevisionControlled) CommonUtil.getObject(objOid);
			
			String linkType = "";
			if(per instanceof WTPart) {
				linkType = "part";
			} else if(per instanceof EPMDocument) {
				linkType = "drawing";
			}
			
			EProjectMasteredLink link = ProjectHelper.manager.getMasteredLink(per.getMaster());
			if(link == null) {
				link = EProjectMasteredLink.newEProjectMasteredLink(per.getMaster(), project);
				link.setLinkType(linkType);
				PersistenceHelper.manager.save(link);
				
				//권한
				AdminHelper.service.setAuthToObject(per, project);
			}
		}
	}
	
	@Override
	public void deleteMasterLink(String oid) throws Exception{
		
		EProjectMasteredLink link = (EProjectMasteredLink)CommonUtil.getObject(oid);
		
		PersistenceHelper.manager.delete(link);
	}
	
	/**
     *  Sort 수정  (StandardTemplateService에서 이동)
     * @param task
     * @param seq
     * @return
     * @throws Exception
     */
    @Override
    public int updateSort(ETask task, int seq)throws Exception{
    	
    	int count = 0;
    	ClassInfo classinfo = WTIntrospector.getClassInfo(task.getClass());
    	String tableName = "";
    	if (DatabaseInfoUtilities.isAutoNavigate(classinfo)) {
			tableName = DatabaseInfoUtilities.getBaseTableName(classinfo);
		} else {
			tableName = DatabaseInfoUtilities.getValidTableName(classinfo);
		}
    	
    	String parentKeyColumnName = DatabaseInfoUtilities.getValidColumnName(
				classinfo, "parentReference.key.id");
    	
    	String seqColumnName = DatabaseInfoUtilities.getValidColumnName(
				classinfo, ETask.SORT);


		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			con = DBConnectionManager.getInstance().getConnection("plm");
			//con = DBCPManager.getConnection("local");
			
			String sql = "UPDATE " + tableName + " set " + seqColumnName + "= " + seqColumnName + " + 1 where " + parentKeyColumnName
								+ " = ? and " + seqColumnName + " >= ?";
			st = con.prepareStatement(sql);
			long parentTaskId = task.getParent().getPersistInfo().getObjectIdentifier().getId();
		
			st.setLong(1, parentTaskId);
			st.setInt(2, seq);
			
			count = st.executeUpdate();
			
		}catch(Exception ex){
			ex.printStackTrace();
			throw new WTException(ex);
		} finally {
			try {
				if(rs != null){
					rs.close();
				}
				if(st != null){
					st.close();
				}
                if (con != null) {
                	con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
		}
		
		return count;
    }
    
    /**
	 * Role 수정 (TASK 담당자 수정)
	 * 
	 * @param hash
	 * @throws Exception
	 */

	@SuppressWarnings("unchecked")
	@Override
	public String editRole(Map<String, Object> reqMap) throws Exception {

		Transaction trx = new Transaction();
		String msg = MESSAGEKEY.UPDATE;
		try {
			trx.start();

			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			List<Map<String, Object>> items = (List<Map<String, Object>>) reqMap.get("items");

			ETask task = (ETask) CommonUtil.getObject(oid);

			List<TaskRoleLink> taskRoleLinkList = TemplateHelper.manager.getTaskRoleLinkList(task);

			List<ProjectRole> tempRoleList = new ArrayList<ProjectRole>(); // 현재 링크에 있는 Role List
			for (TaskRoleLink taskRoleLink : taskRoleLinkList) {
				ProjectRole role = taskRoleLink.getRole();
				
				System.out.println("editRole => " + role.getName() + " / " + role.getCode());
				System.out.println("editRole info : " + CommonUtil.getOIDString(role) );
				
				if("PM".equals(role.getName())) {
					System.out.println("====PM ROLE CONTINUE====");
					continue;
				}
				tempRoleList.add(role);
				PersistenceHelper.manager.delete(taskRoleLink);
			}
			
			for (Map<String, Object> item : items) {
				
				String key = (String) item.get("key");
				System.out.println(" key === > " + key + " // task.getProject() : " + task.getProject());
				
				ProjectRole role = TemplateHelper.manager.getProjectRole(task.getProject(), key);

				if (role == null) {
					role = ProjectRole.newProjectRole();

					role.setName((String) item.get("value"));
					role.setCode((String) item.get("key"));
					role.setProject(task.getProject());
					role = (ProjectRole) PersistenceHelper.manager.save(role);
				} else {
					if (tempRoleList.contains(role)) { // 새로바뀐 Role에 현재링크 Role 있으면 리스트에서 제거
						tempRoleList.remove(role);
					}
				}

				TaskRoleLink link = TaskRoleLink.newTaskRoleLink(role, task);
				PersistenceHelper.manager.save(link);
			}

			/**
			 * 타스크에 맞춘 롤만 남게 리셋
			 */
			EProject pjt = (EProject)task.getProject();
			List<ETask> allList = new ArrayList<ETask>();
			
			allList = ProjectHelper.manager.getTaskList(CommonUtil.getOIDString(pjt), allList);

			Set<String> prList = new HashSet<String>();

			for(ETask ta : allList) {
				QueryResult qr = PersistenceHelper.manager.navigate(ta,"role",TaskRoleLink.class, false);
				while(qr.hasMoreElements()){
					TaskRoleLink link = (TaskRoleLink)qr.nextElement();
					ProjectRole role = link.getRole();
					prList.add(role.getCode());
				}
			}

			List<Object[]> objList = ProjectMemberHelper.manager.getRoleUserLinkList(pjt);
			for(Object[] obj : objList) {
				ProjectRole role = (ProjectRole) obj[1];
				if(!"PR01".equals(role.getCode())) {
					if(prList.contains(role.getCode())){
						System.out.println("no delete");
					}else{
						PersistenceHelper.manager.delete(role);
					}
				}
			}
			
			
			// 남은 Role(어느 링크와도 연관되지 않음) 삭제
			/*
			for (ProjectRole role : tempRoleList) {
				System.out.println("role.getName() : " + role.getName());
				System.out.println("role info : " + CommonUtil.getOIDString(role) );
				if("PM".equals(role.getName())) {
					continue;
				}
				PersistenceHelper.manager.delete(role);
			}
			 */
			
			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getLocalizedMessage() + MESSAGEKEY.UPDATE_ERROR;
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return msg;
	}
	
	// 템플릿 트리 Up 이등
	@Override
	public Map<String, Object> moveUpTask(Map<String, Object> reqMap) throws Exception {

		Map<String, Object> returnMap = new HashMap<>();

		Transaction trx = new Transaction();

		try {
			trx.start();

			String oid = StringUtil.checkNull((String) reqMap.get("oid"));

			ScheduleNode sNode = (ScheduleNode) CommonUtil.getObject(oid);

			EProject project = null;
			ETask task = null;

			if (sNode instanceof EProjectNode) {
				project = (EProject) sNode;
			} else if (sNode instanceof ETaskNode) {
				task = (ETask) sNode;
				project = (EProject) task.getProject();
			}

			if (project == sNode) {
				returnMap.put("msg", "최상위 Level은 이동이 불가합니다.");
				return returnMap;
			}

			ETask prevTask = TemplateHelper.manager.getPrevTask(task);

			if(prevTask != null) {
				int taskSort = task.getSort();
				int prevSort = prevTask.getSort();
				
				task.setSort(prevSort);
				prevTask.setSort(taskSort);
				
				task = (ETask) PersistenceHelper.manager.save(task);
				prevTask = (ETask) PersistenceHelper.manager.save(prevTask);
				
				returnMap.put("task", task);
				returnMap.put("prevTask", prevTask);
			} else {
				return returnMap;
			}

			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}

		return returnMap;
	}
	
	// 템플릿 트리 down 이등
	@Override
	public Map<String, Object> moveDownTask(Map<String, Object> reqMap) throws Exception {

		Map<String, Object> returnMap = new HashMap<>();

		Transaction trx = new Transaction();

		try {
			trx.start();

			String oid = StringUtil.checkNull((String) reqMap.get("oid"));

			ScheduleNode sNode = (ScheduleNode) CommonUtil.getObject(oid);

			EProject project = null;
			ETask task = null;

			if (sNode instanceof EProjectNode) {
				project = (EProject) sNode;
			} else if (sNode instanceof ETaskNode) {
				task = (ETask) sNode;
				project = (EProject) task.getProject();
			}

			if (project == sNode) {
				returnMap.put("msg", "최상위 Level은 이동이 불가합니다.");
				return returnMap;
			}

			ETask nextTask = TemplateHelper.manager.getNextTask(task);

			if (nextTask != null) {
				int taskSort = task.getSort();
				int nextSort = nextTask.getSort();

				task.setSort(nextSort);
				nextTask.setSort(taskSort);

				task = (ETask) PersistenceHelper.manager.save(task);
				nextTask = (ETask) PersistenceHelper.manager.save(nextTask);

				returnMap.put("task", task);
				returnMap.put("nextTask", nextTask);
			} else {
				return returnMap;
			}

			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}

		return returnMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ETask addGanttTaskAction(Map<String, Object> reqMap) {
		ETask task = null;
		Transaction trx = new Transaction();
		try {
			
			Map<String, Object> taskMap = (Map<String, Object>) reqMap.get("task");
			
			trx.start();
			
			task = ETask.newETask();
			
			task.setName(ParamUtil.get(taskMap, "text"));
			
			String startDate = (String) taskMap.get("start_date");
			String endDate = (String) taskMap.get("end_date");
			
			DateTime start_dt = new DateTime(startDate);
			DateTime end_dt = new DateTime(endDate);
			Timestamp start_date = new Timestamp(start_dt.getMillis());
			Timestamp end_date = new Timestamp(end_dt.getMillis());
			
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(end_date.getTime());
			cal.add(Calendar.DATE, -1);
			end_date = new Timestamp(cal.getTimeInMillis());
			
			task.setPlanStartDate(start_date);
			task.setPlanEndDate(end_date);
			
			task.setSort((int) reqMap.get("sort"));
			task.setStatus(STATEKEY.READY);
			task.setCreator(SessionHelper.manager.getPrincipalReference());
			
			String parentOid = StringUtil.checkNull((String) taskMap.get("parent"));
			if(parentOid.length() > 0) {
				ScheduleNode parentNode = (ScheduleNode) CommonUtil.getObject(parentOid);
				task.setParent(parentNode);
				
				EProject project = null;
				if(parentNode instanceof EProject) {
					project = (EProject) parentNode;
				} else {
					project = (EProject) ((ETask) parentNode).getProject();
				}
				task.setProject(project);
			}
			
			task = (ETask) PersistenceHelper.manager.save(task);
			
			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return task;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ETask updateGanttTaskAction(Map<String, Object> reqMap) {
    	ETask task = null;
		Transaction trx = new Transaction();
		String msg = MESSAGEKEY.UPDATE;
		try {
			
			Map<String, Object> taskMap = (Map<String, Object>) reqMap.get("task");
			
			trx.start();
			task = (ETask) CommonUtil.getObject(ParamUtil.get(taskMap, "id"));
			task.setName(ParamUtil.get(taskMap, "text"));
			
			//task.setPlanStartDate(palnStartDate);
			//task.setPlanEndDate(planEndDate);
			PersistenceHelper.manager.modify(task);
			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getLocalizedMessage() + MESSAGEKEY.UPDATE_ERROR;
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return task;
	}
	
	@Override
	public void deleteGanttTaskAction(Map<String, Object> reqMap) {
    	ETask task = null;
		Transaction trx = new Transaction();
		String msg = MESSAGEKEY.UPDATE;
		try {
			
			
			trx.start();
			task = (ETask) CommonUtil.getObject(ParamUtil.get(reqMap, "id"));
			
			delete(task);

			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getLocalizedMessage() + MESSAGEKEY.UPDATE_ERROR;
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}
	
	@Override
	public synchronized void updateGanttTaskAllAction(Map<String, Object> reqMap) {
		Transaction trx = new Transaction();
		String msg = MESSAGEKEY.UPDATE;
		try {
			
			List<Map<String, Object>> taskList = (List<Map<String, Object>>) reqMap.get("taskList");
			
			trx.start();
			for(Map<String, Object> taskMap : taskList) {
				String oid = ParamUtil.get(taskMap, "id");
				if(!oid.contains("com.e3ps.project.")) {
					continue;
				}
				ScheduleNode node = (ScheduleNode) CommonUtil.getObject(oid);
				
				node.setName(ParamUtil.get(taskMap, "text"));
				
				String startDate = (String) taskMap.get("start_date");
				String endDate = (String) taskMap.get("end_date");
				
//				Timestamp start_date = DateUtil.convertDate(startDate);
//				Timestamp end_date = DateUtil.convertDate(endDate);
				
				DateTime start_dt = new DateTime(startDate);
				DateTime end_dt = new DateTime(endDate);
				
				Timestamp start_date = new Timestamp(start_dt.getMillis());
				Timestamp end_date = new Timestamp(end_dt.getMillis());
				
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(end_date.getTime());
				cal.add(Calendar.DATE, -1);
				end_date = new Timestamp(cal.getTimeInMillis());
				
				node.setPlanStartDate(start_date);
				node.setPlanEndDate(end_date);
				
				PersistenceHelper.manager.modify(node);
			}
			
			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getLocalizedMessage() + MESSAGEKEY.UPDATE_ERROR;
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}
	
	@Override
	public PrePostLink addGanttLinkAction(Map<String, Object> reqMap) {
		PrePostLink link = null;
		Transaction trx = new Transaction();
		try {
			
			
			trx.start();
			ETask source = (ETask) CommonUtil.getObject(ParamUtil.get(reqMap, "source"));
			ETask target = (ETask) CommonUtil.getObject(ParamUtil.get(reqMap, "target"));
			
			link = PrePostLink.newPrePostLink(source, target);
			link = (PrePostLink) PersistenceHelper.manager.save(link);
			
			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return link;
	}
	
	@Override
	public void deleteGanttLinkAction(Map<String, Object> reqMap) {
		Transaction trx = new Transaction();
		try {
			
			trx.start();
			PrePostLink link = (PrePostLink) CommonUtil.getObject(ParamUtil.get(reqMap, "linkOid"));

			PersistenceHelper.manager.delete(link);
			
			trx.commit();
			trx = null;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void saveGanttAction(Map<String, Object> reqMap) {
		
		Transaction trx = new Transaction();
		String msg = MESSAGEKEY.UPDATE;
		try {
			
			List<Map<String, Object>> taskList = (List<Map<String, Object>>) reqMap.get("taskList");
			
			trx.start();
			for(Map<String, Object> taskMap : taskList) {
			}
			
			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getLocalizedMessage() + MESSAGEKEY.UPDATE_ERROR;
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}
	
	@Override
	public List<ProjectGanttTaskData> saveGanttTaskAllAction(Map<String, Object> reqMap) {
		
		List<ProjectGanttTaskData> list = new ArrayList<>();
		Transaction trx = new Transaction();
		String msg = MESSAGEKEY.UPDATE;
		try {
			
			List<Map<String, Object>> taskList = (List<Map<String, Object>>) reqMap.get("taskList");
			
			trx.start();
			for(Map<String, Object> taskMap : taskList) {
			}
			
			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getLocalizedMessage() + MESSAGEKEY.UPDATE_ERROR;
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProjectGanttLinkData> saveGanttLinkAllAction(Map<String, Object> reqMap) {
		
		List<ProjectGanttLinkData> list = new ArrayList<>();
		Transaction trx = new Transaction();
		String msg = MESSAGEKEY.UPDATE;
		try {
			
			List<Map<String, Object>> taskList = (List<Map<String, Object>>) reqMap.get("taskList");
			
			trx.start();
			for(Map<String, Object> taskMap : taskList) {
			}
			
			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getLocalizedMessage() + MESSAGEKEY.UPDATE_ERROR;
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void moveGanttTaskAction(Map<String, Object> reqMap) {
		
		Transaction trx = new Transaction();
		try {
			
			List<Map<String, Object>> children = (List<Map<String, Object>>) reqMap.get("children");
			
			trx.start();
			
			int index = 0;
			for(Map<String, Object> taskMap : children) {
				index++;
				String oid = ParamUtil.get(taskMap, "id");
				if(!oid.contains("com.e3ps.project.")) {
					continue;
				}
				ScheduleNode node = (ScheduleNode) CommonUtil.getObject(oid);
				
				node.setSort(index);
				
				PersistenceHelper.manager.save(node);
			}
			
			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}

	@Override
	public EProject copyProject(Map<String, Object> hash) throws Exception {
		Transaction trx = new Transaction();

		EProject project = null;
		
		String relatedProject = StringUtil.checkNull((String) ParamUtil.get(hash, "relatedProject"));
		try {
			trx.start();
			EProject orgProject = (EProject) CommonUtil.getObject(relatedProject);
			project = EProject.newEProject();

			String number = ParamUtil.get(hash, "number");
			String name = ParamUtil.get(hash, "name");
			String description = ParamUtil.get(hash, "description");
			String planStartDate = ParamUtil.get(hash, "planStartDate");
			String pm = ParamUtil.get(hash, "pm");
			EProjectTemplate temp = orgProject.getTemplate();
			PDMLinkProduct ot = WCUtil.getPDMLinkProduct();
			project.setProduct(ot);

			/*
			String number = "PJT" + "-20" + DateUtil.getCurrentDateString("month");
			String noFormat = "0000";
			String seqNo = SequenceDao.manager.getSeqNo(number + "-", noFormat, "EProject", "code");
			number = number + "-" + seqNo;
			*/
			

			project.setCode(number);
			project.setName(name);
			project.setDescription(description);
			project.setPlanStartDate(DateUtil.convertDate(planStartDate));
			project.setPlanEndDate(DateUtil.convertDate(planStartDate));
			project.setVersion(0);
			
			project.setOutputType(temp.getOutputType());
			project.setProjectType(temp.getOutputType().toString());
			project.setTemplate(temp);
			
			// folder setting
			Folder folder = FolderTaskLogic.getFolder("/Default/Project", WCUtil.getWTContainerRef());
			FolderHelper.assignLocation((FolderEntry) project, folder);
			PDMLinkProduct e3psProduct = WCUtil.getPDMLinkProduct();
			WTContainerRef wtContainerRef = WTContainerRef.newWTContainerRef(e3psProduct);
			// project.setContainer(e3psProduct);
			LifeCycleHelper.setLifeCycle(project,
					LifeCycleHelper.service.getLifeCycleTemplate("LC_Project", wtContainerRef)); // Lifecycle

			project = (EProject) PersistenceHelper.manager.save(project);

			if (pm != null && pm.length() > 0) {
				People people = (People) CommonUtil.getObject(pm);
				WTUser user = people.getUser();
				ProjectRole pmRole = ProjectRole.newProjectRole();
				pmRole.setCode(PROJECTKEY.PM);
				NumberCode pmCode = CodeHelper.manager.getNumberCode("PROJECTROLE", PROJECTKEY.PM);
				if(pmCode != null) {
					pmRole.setName(pmCode.getName());
				} else {
					pmRole.setName("PM");
				}
				pmRole.setProject(project);
				pmRole.setSort(-1);
				pmRole = (ProjectRole) PersistenceHelper.manager.save(pmRole);

				RoleUserLink ulink = RoleUserLink.newRoleUserLink(user, pmRole);
				PersistenceHelper.manager.save(ulink);
			}

			copyProject(orgProject, project);

			project = (EProject) PersistenceHelper.manager.refresh(project);
			ProjectTreeModel model2 = new ProjectTreeModel(project);
			model2.setPlanSchedule(DateUtil.convertStartDate(planStartDate));
			ContentHelper.service.copyContent(orgProject, project);
			project = (EProject) PersistenceHelper.manager.refresh(project);

			trx.commit();
			trx = null;
			
			//ERP 전송
			//ERPInterface.send(project);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return project;
	}
	
	@Override
	public EProject copyProjectCreate(Map<String, Object> hash) throws Exception {
		Transaction trx = new Transaction();

		EProject project = null;
		
		String relatedProject = StringUtil.checkNull((String) ParamUtil.get(hash, "relatedProject"));
		try {
			trx.start();
			EProject orgProject = (EProject) CommonUtil.getObject(relatedProject);
			project = EProject.newEProject();

			String number = ParamUtil.get(hash, "code");
			String name = ParamUtil.get(hash, "name");
			String description = ParamUtil.get(hash, "description");
			String planStartDate = ParamUtil.get(hash, "planStartDate");
			String pm = ParamUtil.get(hash, "pm");
			EProjectTemplate temp = orgProject.getTemplate();
			PDMLinkProduct ot = WCUtil.getPDMLinkProduct();
			project.setProduct(ot);
			
			String group = orgProject.getGroupCode();
			String material = orgProject.getMaterialCode();
			String level = orgProject.getLevelCode();
			
			String tempNumber = group+material+level + DateUtil.getToDay("YY");
			String noFormat = "000";
			String seqNo = SequenceDao.manager.getSeqNo(tempNumber + "", noFormat, "EProject", "code");
			
			number = tempNumber + seqNo;

			/*
			String number = "PJT" + "-20" + DateUtil.getCurrentDateString("month");
			String noFormat = "0000";
			String seqNo = SequenceDao.manager.getSeqNo(number + "-", noFormat, "EProject", "code");
			number = number + "-" + seqNo;
			*/
			

			project.setCode(number);
			project.setName(name);
			project.setDescription(description);
			project.setPlanStartDate(DateUtil.convertDate(planStartDate));
			project.setPlanEndDate(DateUtil.convertDate(planStartDate));
			project.setVersion(0);
			
			project.setOutputType(temp.getOutputType());
			project.setProjectType(temp.getOutputType().toString());
			project.setTemplate(temp);
			
			project.setGroupCode(group);
			project.setMaterialCode(material);
			project.setLevelCode(level);
			
			// folder setting
			Folder folder = FolderTaskLogic.getFolder("/Default/Project", WCUtil.getWTContainerRef());
			FolderHelper.assignLocation((FolderEntry) project, folder);
			PDMLinkProduct e3psProduct = WCUtil.getPDMLinkProduct();
			WTContainerRef wtContainerRef = WTContainerRef.newWTContainerRef(e3psProduct);
			// project.setContainer(e3psProduct);
			LifeCycleHelper.setLifeCycle(project,
					LifeCycleHelper.service.getLifeCycleTemplate("LC_Project", wtContainerRef)); // Lifecycle

			project = (EProject) PersistenceHelper.manager.save(project);

			if (pm != null && pm.length() > 0) {
				People people = (People) CommonUtil.getObject(pm);
				WTUser user = people.getUser();
				ProjectRole pmRole = ProjectRole.newProjectRole();
				pmRole.setCode(PROJECTKEY.PM);
				NumberCode pmCode = CodeHelper.manager.getNumberCode("PROJECTROLE", PROJECTKEY.PM);
				if(pmCode != null) {
					pmRole.setName(pmCode.getName());
				} else {
					pmRole.setName("PM");
				}
				pmRole.setProject(project);
				pmRole.setSort(-1);
				pmRole = (ProjectRole) PersistenceHelper.manager.save(pmRole);

				RoleUserLink ulink = RoleUserLink.newRoleUserLink(user, pmRole);
				PersistenceHelper.manager.save(ulink);
			}

			copyProjectCreate(orgProject, project);

			project = (EProject) PersistenceHelper.manager.refresh(project);
			ProjectTreeModel model2 = new ProjectTreeModel(project);
			model2.setPlanSchedule(DateUtil.convertStartDate(planStartDate));
			ContentHelper.service.copyContent(orgProject, project);
			project = (EProject) PersistenceHelper.manager.refresh(project);

			trx.commit();
			trx = null;
			
			//ERP 전송
			//ERPInterface.send(project);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return project;
	}
	
	@Override
	public EProject modifyProjectStartDate(Map<String, Object> hash) throws Exception {

		EProject project = null;
		
		Transaction trx = null; 
		
		String startDate = ParamUtil.get(hash, "startDate");
		Timestamp modifyStartDate = DateUtil.convertDate(startDate);
		project = (EProject)CommonUtil.getObject(ParamUtil.get(hash, "oid"));
		
		if(project != null && modifyStartDate != null) {
			List<ETask> taskList = ProjectHelper.manager.getProjectTaskList(project);
			
			for(ETask task: taskList) {
				//비교
				Timestamp taskStartDate = task.getStartDate();
				
				if(taskStartDate != null && (modifyStartDate.getTime() > taskStartDate.getTime())) {
					throw new Exception("Project 시작일은 Task 시작일보다 앞일 수 없습니다.");
				}
			}
		}
		
		try {
			trx = new Transaction();
			trx.start();
			
			project.setStartDate(DateUtil.convertDate(startDate));
			project = (EProject) PersistenceHelper.manager.modify(project);
			
			
			trx.commit();
			trx = null;

		} catch (Exception e) {
			throw e;
		} finally {
			if (trx != null) {
				trx.rollback();
				trx = null;
			}
		}
		
		return project;
	}
	
	

}