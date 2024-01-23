package com.e3ps.project.service;

import java.util.ArrayList;
import java.util.Hashtable;

import com.e3ps.common.folder.service.CommonFolderHelper;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.query.SearchUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.org.People;
import com.e3ps.project.EOutput;
import com.e3ps.project.EProject;
import com.e3ps.project.ETask;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.ProjectRole;
import com.e3ps.project.RoleUserLink;
import com.e3ps.project.TaskRoleLink;
import com.e3ps.project.issue.IssueRequest;
import com.e3ps.project.key.ProjectKey.IssueKey;
import com.e3ps.project.key.ProjectKey.PROJECTKEY;

import wt.doc.WTDocument;
import wt.fc.ReferenceFactory;
import wt.folder.Folder;
import wt.folder.IteratedFolderMemberLink;
import wt.org.WTUser;
import wt.pdmlink.PDMLinkProduct;
import wt.query.ClassAttribute;
import wt.query.ExistsExpression;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.query.TableExpression;
import wt.services.StandardManager;
import wt.util.WTAttributeNameIfc;

public class StandardProjectQueryService extends StandardManager implements ProjectQueryService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PROJECT.getName());
	private static final long serialVersionUID = 1L;

	public static StandardProjectQueryService newStandardProjectQueryService() throws Exception {
		final StandardProjectQueryService instance = new StandardProjectQueryService();
		instance.initialize();
		return instance;
	}
	
	/** 프로젝트 목록 검색 쿼리
     * @param hash
     * @return
     * @throws Exception
     */
	@SuppressWarnings("rawtypes")
	@Override
    public QuerySpec getListQuery(Hashtable hash) throws Exception{
    	
    	String name = (String) hash.get("name");
    	String number = (String) hash.get("number");
    	String state = (String) hash.get("state");
    	String planStartDate = (String) hash.get("planStartDate");
    	String planEndDate = (String) hash.get("planEndDate");
    	String planStartDateE = (String) hash.get("planStartDateE");
    	String planEndDateE = (String) hash.get("planEndDateE");
    	String pm = (String) hash.get("pm");
    	
    	String sortValue = (String)hash.get("sortValue");
    	String sortCheck = (String)hash.get("sortCheck");

    	QuerySpec qs = new QuerySpec();
    	int ii = qs.addClassList(EProject.class,true);
    	int jj = qs.addClassList(PDMLinkProduct.class,false);
    	int kk = 0;
    	int ll = 0;
    	if(pm != null && pm.length() > 0){
	    	kk = qs.addClassList(ProjectRole.class, false);
		    ll = qs.addClassList(RoleUserLink.class,false);
    	}

    	try{
	    	qs.appendWhere(new SearchCondition(EProject.class,"productReference.key.id",PDMLinkProduct.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{ii,jj});
	    	qs.appendAnd();
	    	qs.appendWhere(new SearchCondition(EProject.class,"lastVersion",SearchCondition.IS_TRUE),new int[]{0});
	
	    	/////검색 조건////
	    	// 이름
	    	if(name != null && name.length() > 0){
	    		qs.appendAnd();
	    		qs.appendWhere(new SearchCondition(EProject.class, "name", SearchCondition.EQUAL, name), new int[]{ii});
	    	}
	    	
	    	// 번호
	    	if(number != null && number.length() > 0){
	    		qs.appendAnd();
	    		qs.appendWhere(new SearchCondition(EProject.class, EProject.CODE, SearchCondition.LIKE, "%" + number.trim() + "%", false), new int[]{ii});
	    	}
	    	
	    	// 상태
	    	if(state != null && state.length() > 0){
	    		qs.appendAnd();
	    		qs.appendWhere(new SearchCondition(EProject.class, "state.state", SearchCondition.LIKE, state, false), new int[]{ii});
	    	}
	    	
	    	// 계획 시작 / 종료일
	    	if(planStartDate != null && planStartDate.length() > 0){
	    		qs.appendAnd();
	    		qs.appendWhere(new SearchCondition(EProject.class,EProject.PLAN_START_DATE,SearchCondition.GREATER_THAN_OR_EQUAL, DateUtil.convertStartDate(planStartDate)),	new int[] { ii });
	    	}
	    	if(planStartDateE != null && planStartDateE.length() > 0){
	    		qs.appendAnd();
	    		qs.appendWhere(new SearchCondition(EProject.class,EProject.PLAN_START_DATE,SearchCondition.LESS_THAN, DateUtil.convertEndDate(planStartDateE)),	new int[] { ii });
	    	}
	    	if(planEndDate != null && planEndDate.length() > 0){
	    		qs.appendAnd();
	    		qs.appendWhere(new SearchCondition(EProject.class,EProject.PLAN_END_DATE,SearchCondition.GREATER_THAN_OR_EQUAL, DateUtil.convertStartDate(planEndDate)),	new int[] { ii });
	    	}
	    	if(planEndDateE != null && planEndDateE.length() > 0){
	    		qs.appendAnd();
	    		qs.appendWhere(new SearchCondition(EProject.class,EProject.PLAN_END_DATE,SearchCondition.LESS_THAN, DateUtil.convertEndDate(planEndDateE)),	new int[] { ii });
	    	}
	
	    	// PM
	    	if(pm != null && pm.length() > 0){
	    		qs.appendAnd();
	    		qs.appendOpenParen();
	    		SearchCondition sc = new SearchCondition(new ClassAttribute(EProject.class, "thePersistInfo.theObjectIdentifier.id"), "=", new ClassAttribute(ProjectRole.class, "projectReference.key.id"));
	    	    sc.setFromIndicies(new int[] { ii, kk }, 0);
	    	    sc.setOuterJoin(2);
	    	    qs.appendWhere(sc, new int[] { ii, kk });
	    	    qs.appendAnd();
	    	    qs.appendWhere(new SearchCondition(ProjectRole.class,"code","=",PROJECTKEY.PM),new int[]{kk});
	    	    qs.appendCloseParen();
	    	    
	    	    qs.appendAnd();
	    	    SearchCondition sc2 = new SearchCondition(new ClassAttribute(ProjectRole.class, "thePersistInfo.theObjectIdentifier.id"), "=", new ClassAttribute(RoleUserLink.class, "roleBObjectRef.key.id"));
	    	    sc2.setFromIndicies(new int[] { kk, ll }, 0);
	    	    sc2.setOuterJoin(2);
	    	    qs.appendWhere(sc2, new int[] { kk, ll });
	    	    qs.appendAnd();
	    	    qs.appendWhere(new SearchCondition(RoleUserLink.class,"roleAObjectRef.key.id","=", CommonUtil.getOIDLongValue(pm)) , new int[] { ll });
	    	}
	    	
	    	if(sortValue != null && sortValue.length() > 0) {
	    		if("true".equals(sortCheck)){
	    			if( !"creator".equals(sortValue)){
	    				qs.appendOrderBy(new OrderBy(new ClassAttribute(EProject.class,sortValue), true), new int[] { ii });
	    			}else{
	    				if(qs.getConditionCount() > 0) qs.appendAnd();
	    				int idx_user = qs.appendClassList(WTUser.class, false);
	    				int idx_people = qs.appendClassList(People.class, false);
	    				
	    				
	    				ClassAttribute ca = null;
	    	            ClassAttribute ca2 = null;
	
	    	            ca = new ClassAttribute(EProject.class, "creator.key.id");
	    				ca2 = new ClassAttribute(WTUser.class, "thePersistInfo.theObjectIdentifier.id");
	    				
	    				
	    				SearchCondition sc2 = new SearchCondition(ca, "=", ca2);
	    				
	    				qs.appendWhere(sc2, new int[]{ii, idx_user});
	    				
	    				ClassAttribute ca3 = new ClassAttribute(People.class, "userReference.key.id");
	    				
	    				qs.appendAnd();
	    				qs.appendWhere(new SearchCondition(ca2, "=", ca3), new int[]{idx_user, idx_people});
	    				SearchUtil.setOrderBy(qs, People.class, idx_people, People.NAME, "sort" , true);
	    			}
	    		}else{
	    			if( !"creator.key.id".equals(sortValue)){
	    				qs.appendOrderBy(new OrderBy(new ClassAttribute(EProject.class,sortValue), false), new int[] { ii });
	    			}else{
	    				if(qs.getConditionCount() > 0) qs.appendAnd();
	    				int idx_user = qs.appendClassList(WTUser.class, false);
	    				int idx_people = qs.appendClassList(People.class, false);
	    				
	    				
	    				ClassAttribute ca = null;
	    	            ClassAttribute ca2 = null;
	
	    	            ca = new ClassAttribute(EProject.class, "creator.key.id");
	    				ca2 = new ClassAttribute(WTUser.class, "thePersistInfo.theObjectIdentifier.id");
	    				
	    				
	    				SearchCondition sc2 = new SearchCondition(ca, "=", ca2);
	    				
	    				qs.appendWhere(sc2, new int[]{ii, idx_user});
	    				
	    				ClassAttribute ca3 = new ClassAttribute(People.class, "userReference.key.id");
	    				
	    				qs.appendAnd();
	    				qs.appendWhere(new SearchCondition(ca2, "=", ca3), new int[]{idx_user, idx_people});
	    				SearchUtil.setOrderBy(qs, People.class, idx_people, People.NAME, "sort" , false);
	    			}
	    		}
	    		
	    	}else{
	    		qs.appendOrderBy(new OrderBy(new ClassAttribute(EProject.class,"thePersistInfo.createStamp"), true), new int[] { ii }); 
	    	}
    	}catch (Exception e) {
    		e.printStackTrace();
		}
		return qs;
    }
	
	/**
     * 프로젝트 이력 목록
     * @param projectCode
     * @return
     * @throws Exception
     */
	@Override
    public QuerySpec getProjectList(String projectCode) throws Exception{

    	QuerySpec qs = new QuerySpec();
    	try{
	    	int ii = qs.addClassList(EProject.class,true);
	    	qs.appendWhere(new SearchCondition(EProject.class,EProject.CODE,"=",projectCode),new int[]{ii});
	
	    	qs.appendOrderBy(new OrderBy(new ClassAttribute(EProject.class,"productReference.key.id"),false),new int[]{0});
	    	qs.appendOrderBy(new OrderBy(new ClassAttribute(EProject.class,"version"),true),new int[]{0});
	    	qs.appendOrderBy(new OrderBy(new ClassAttribute(EProject.class,"thePersistInfo.createStamp"),true),new int[]{0});
    	}catch (Exception e) {
    		e.printStackTrace();
		}
    	return qs;
    }
	
	/**
     * 나의 프로젝트 목록
     * @param hash
     * @return
     * @throws Exception 
     */
	@SuppressWarnings({ "rawtypes", "deprecation" })
	@Override
    public QuerySpec getMyProject(Hashtable hash) throws Exception{
    	String name = (String)hash.get("name");
    	String state = (String)hash.get("state");
    	String planStartDate = (String)hash.get("planStartDate");
    	String planStartDateE = (String)hash.get("planStartDateE");
    	String sortValue = (String)hash.get("sortValue");
    	String sortCheck = (String)hash.get("sortCheck");
    	Long user = (Long)hash.get("user");
    	
    	QuerySpec mainQs = new QuerySpec();
    	int iii = mainQs.addClassList(EProject.class,true);
    	try{
	    	QuerySpec qs = new QuerySpec();
	    	qs.getFromClause().setAliasPrefix("B");
	    	int ii = qs.addClassList(EProject.class,false);
	    	int jj = qs.addClassList(PDMLinkProduct.class,false);
	    	qs.appendSelect(new ClassAttribute(EProject.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{ii},true);
	    	qs.appendWhere(new SearchCondition(EProject.class,"productReference.key.id",PDMLinkProduct.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{ii,jj});
	    	qs.appendAnd();
	    	qs.appendWhere(new SearchCondition(EProject.class,"lastVersion",SearchCondition.IS_TRUE),new int[]{0});
	
	
	    	int kk = qs.addClassList(ProjectRole.class,false);
	    	int ll = qs.addClassList(RoleUserLink.class,false);
	    	qs.appendAnd();
	    	qs.appendWhere(new SearchCondition(EProject.class,"thePersistInfo.theObjectIdentifier.id",ProjectRole.class,"projectReference.key.id"),new int[]{ii,kk});
	    	qs.appendAnd();
	    	qs.appendWhere(new SearchCondition(ProjectRole.class,"thePersistInfo.theObjectIdentifier.id",RoleUserLink.class,"roleBObjectRef.key.id"),new int[]{kk,ll});
	    	qs.appendAnd();
	    	qs.appendWhere(new SearchCondition(RoleUserLink.class,"roleAObjectRef.key.id","=",user),new int[]{ll});
	
	    	/////검색 조건////
	
	    	if(planStartDate!=null && planStartDate.length()>0){
	    		qs.appendAnd();
	    		qs.appendWhere(new SearchCondition(EProject.class,EProject.PLAN_START_DATE,SearchCondition.GREATER_THAN_OR_EQUAL, DateUtil.convertStartDate(planStartDate)),	new int[] { ii });
	    	}
	    	if(planStartDateE!=null && planStartDateE.length()>0){
	    		qs.appendAnd();
	    		qs.appendWhere(new SearchCondition(EProject.class,EProject.PLAN_START_DATE,SearchCondition.LESS_THAN, DateUtil.convertEndDate(planStartDateE)),	new int[] { ii });
	    	}
	    	if(name!=null && name.length()>0){
	    		qs.appendAnd();
	    		qs.appendWhere(new SearchCondition(EProject.class, "name", SearchCondition.LIKE, "%" + name.trim() + "%", false), new int[]{ii});
	    	}
	
	    	if(state!=null && state.length()>0){
	    		qs.appendAnd();
	    		qs.appendWhere(new SearchCondition(EProject.class, "state.state", "=", state), new int[]{ii});
	    	}
	    	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	    	TableExpression[] tables = new TableExpression[2];
	    	String[] aliases = new String[2];
	    	tables[0] = mainQs.getFromClause().getTableExpressionAt(iii);
	    	aliases[0] = mainQs.getFromClause().getAliasAt(iii);
	    	tables[1] = qs.getFromClause().getTableExpressionAt(ii);
	    	aliases[1] = qs.getFromClause().getAliasAt(ii);
	
	    	SearchCondition correlatedJoin = new SearchCondition(EProject.class, WTAttributeNameIfc.ID_NAME, EProject.class, WTAttributeNameIfc.ID_NAME);
	    	qs.appendAnd();
	    	qs.appendWhere(correlatedJoin, tables, aliases);
	
	    	mainQs.appendWhere(new ExistsExpression(qs));
	
	    	if(sortValue != null && sortValue.length() > 0) {
	    		if("true".equals(sortCheck))
	    			mainQs.appendOrderBy(new OrderBy(new ClassAttribute(EProject.class,sortValue), true), new int[] { ii });
	    		else
	    			mainQs.appendOrderBy(new OrderBy(new ClassAttribute(EProject.class,sortValue), false), new int[] { ii });
	    	}else
	    		mainQs.appendOrderBy(new OrderBy(new ClassAttribute(EProject.class,"thePersistInfo.createStamp"), true), new int[] { ii }); 
    	}catch (Exception e) {
			e.printStackTrace();
		}
    	return mainQs;
    }
	
	/**
     * 나의 태스크 목록
     * @param hash
     * @return
     * @throws Exception 
     */
	@SuppressWarnings({ "rawtypes", "deprecation" })
	@Override
    public QuerySpec getMyTask(Hashtable hash) throws Exception{
    	String name = (String)hash.get("name");
    	String state = (String)hash.get("state");
    	String planStartDate = (String)hash.get("planStartDate");
    	String planStartDateE = (String)hash.get("planStartDateE");
    	String taskName = (String)hash.get("taskName");
    	String taskState = (String)hash.get("taskState");
    	Long user = (Long)hash.get("user");
    	
		QuerySpec mainQs = new QuerySpec();
		int iii = mainQs.addClassList(ETask.class,true);
		try{
			QuerySpec qs = new QuerySpec();
			qs.getFromClause().setAliasPrefix("B");
			
			int kk = qs.addClassList(ETask.class,false);
			int ii = qs.addClassList(EProject.class,false);
			int jj = qs.addClassList(PDMLinkProduct.class,false);
			
			int ll = qs.addClassList(TaskRoleLink.class,false);
			int mm = qs.addClassList(ProjectRole.class,false);
			int nn = qs.addClassList(RoleUserLink.class,false);
			
			qs.appendSelect(new ClassAttribute(ETask.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{kk},true);
			
			qs.appendWhere(new SearchCondition(ETask.class,"projectReference.key.id",EProject.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{kk,ii});
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(EProject.class,"productReference.key.id",PDMLinkProduct.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{ii,jj});
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(EProject.class,"lastVersion",SearchCondition.IS_TRUE),new int[]{ii});
			
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(ETask.class,"thePersistInfo.theObjectIdentifier.id",TaskRoleLink.class,"roleBObjectRef.key.id"),new int[]{kk,ll});
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(ProjectRole.class,"thePersistInfo.theObjectIdentifier.id",TaskRoleLink.class,"roleAObjectRef.key.id"),new int[]{mm,ll});
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(RoleUserLink.class,"roleBObjectRef.key.id",ProjectRole.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{nn,mm});
			
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(RoleUserLink.class,"roleAObjectRef.key.id","=",user),new int[]{nn});
			
			//////////////// 검색 조건
			
			if(planStartDate!=null && planStartDate.length()>0){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(ETask.class,ETask.PLAN_START_DATE,SearchCondition.GREATER_THAN, DateUtil.convertStartDate(planStartDate)),	new int[] { kk });
			}
			if(planStartDateE!=null && planStartDateE.length()>0){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(ETask.class,ETask.PLAN_START_DATE,SearchCondition.LESS_THAN, DateUtil.convertEndDate(planStartDateE)),	new int[] { kk });
			}
			if(name!=null && name.length()>0){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(EProject.class, "name", SearchCondition.LIKE, "%" + name.trim() + "%", false), new int[]{ii});
			}
			if(taskName!=null && taskName.length()>0){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(ETask.class, ETask.NAME, SearchCondition.LIKE, "%" + taskName.trim() + "%", false), new int[]{kk});
			}
			if(state!=null && state.length()>0){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(EProject.class, "state.state", SearchCondition.EQUAL, state, false), new int[]{ii});
			}
			if(taskState!=null && taskState.length()>0){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(ETask.class, ETask.STATUS, SearchCondition.EQUAL, taskState, false), new int[]{kk});
			}
			
			////////////////////////////
			
			TableExpression[] tables = new TableExpression[2];
			String[] aliases = new String[2];
			tables[0] = mainQs.getFromClause().getTableExpressionAt(iii);
			aliases[0] = mainQs.getFromClause().getAliasAt(iii);
			tables[1] = qs.getFromClause().getTableExpressionAt(kk);
			aliases[1] = qs.getFromClause().getAliasAt(kk);
			
			SearchCondition correlatedJoin = new SearchCondition(ETask.class, WTAttributeNameIfc.ID_NAME, ETask.class, WTAttributeNameIfc.ID_NAME);
			qs.appendAnd();
			qs.appendWhere(correlatedJoin, tables, aliases);
			
			mainQs.appendWhere(new ExistsExpression(qs));
			
			mainQs.appendOrderBy(new OrderBy(new ClassAttribute(ETask.class,"planStartDate"),false),new int[]{iii});
 
    	}catch (Exception e) {
			e.printStackTrace();
		}
    	return mainQs;
    }
	
	/**
     * 나의 이슈 목록
     * @param hash
     * @return
     * @throws Exception 
     */
	@SuppressWarnings("rawtypes")
	@Override
    public QuerySpec getMyIssue(Hashtable hash) throws Exception{
    	String name = (String)hash.get("name");
    	String state = (String)hash.get("state");
    	String issueType = (String)hash.get("issueType");
    	String role = (String)hash.get("role");
    	Long user = (Long)hash.get("user");
    	
    	QuerySpec qs = new QuerySpec();
		
		int ll = qs.addClassList(IssueRequest.class,true);
		int kk = qs.addClassList(ETask.class,false);
		int ii = qs.addClassList(EProject.class,false);
		int jj = qs.addClassList(PDMLinkProduct.class,false);
		
		try{

			qs.appendWhere(new SearchCondition(ETask.class,"thePersistInfo.theObjectIdentifier.id",IssueRequest.class,"taskReference.key.id"),new int[]{kk,ll});
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(ETask.class,"projectReference.key.id",EProject.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{kk,ii});
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(EProject.class,"productReference.key.id",PDMLinkProduct.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{ii,jj});
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(EProject.class,"lastVersion",SearchCondition.IS_TRUE),new int[]{ii});
			
			////////////////검색 조건
			
			if("manager".equals(role)){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(IssueRequest.class,"managerReference.key.id","=",user),new int[]{ll});
			}else if("creator".equals(role)){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(IssueRequest.class,"creatorReference.key.id","=",user),new int[]{ll});
			}else{
				qs.appendAnd();
				qs.appendOpenParen();
				qs.appendWhere(new SearchCondition(IssueRequest.class,"creatorReference.key.id","=",user),new int[]{ll});
				qs.appendOr();
				qs.appendWhere(new SearchCondition(IssueRequest.class,"managerReference.key.id","=",user),new int[]{ll});
				qs.appendCloseParen();
			}
			
			if(issueType!=null && issueType.length()>0){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(IssueRequest.class,IssueRequest.ISSUE_TYPE,"=",issueType),new int[]{ll});
			}
			
			if(name!=null && name.length()>0){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(IssueRequest.class, IssueRequest.NAME, SearchCondition.LIKE, "%" + name.trim() + "%", false), new int[]{ll});
			}
			
			if("PROGRESS".equals(state)){
				qs.appendAnd();
				qs.appendOpenParen();
				qs.appendWhere(new SearchCondition(IssueRequest.class,IssueRequest.STATE,"=",IssueKey.ISSUE_REQUEST),new int[]{ll});
				qs.appendOr();
				qs.appendWhere(new SearchCondition(IssueRequest.class,IssueRequest.STATE,"=",IssueKey.ISSUE_CHECK),new int[]{ll});
				qs.appendCloseParen();
			}else if("COMPLETED".equals(state)){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(IssueRequest.class,IssueRequest.STATE,"=",IssueKey.ISSUE_COMPLETE),new int[]{ll});
			}
			
			////////////////////////////
			
			qs.appendOrderBy(new OrderBy(new ClassAttribute(IssueRequest.class,"thePersistInfo.createStamp"),true),new int[]{ll});

    	}catch (Exception e) {
			e.printStackTrace();
		}
    	return qs;
    }
	
	/**
     * 산출물 현황
     * @param hash
     * @return
     * @throws Exception 
     */
	@SuppressWarnings("rawtypes")
	@Override
    public QuerySpec getOutputList(Hashtable hash) throws Exception{
    	String name = (String)hash.get("name");
    	String taskName = (String)hash.get("taskName");
    	String docName = (String)hash.get("docName");
    	String division = (String)hash.get("division");
    	String folder = (String)hash.get("folder");
    	String pm = (String)hash.get("pm");
    	String tempowner = (String)hash.get("tempowner");
    	Long product = (Long)hash.get("product");
    	
		QuerySpec qs = new QuerySpec();
		ReferenceFactory rf = new ReferenceFactory();
		int ii = qs.addClassList(EProject.class,true);
		int jj = qs.addClassList(ETaskNode.class,true);
		int kk = qs.addClassList(EOutput.class,true);
		int ll = qs.addClassList(WTDocument.class,true);
		int mm = qs.addClassList(PDMLinkProduct.class,false);
		try{
			qs.appendWhere(new SearchCondition(EProject.class,"productReference.key.id",PDMLinkProduct.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{ii,mm});
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(EProject.class,"thePersistInfo.theObjectIdentifier.id",ETaskNode.class,"projectReference.key.id"),new int[]{ii,jj});
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(ETaskNode.class,"thePersistInfo.theObjectIdentifier.id",EOutput.class,"taskReference.key.id"),new int[]{jj,kk});
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(WTDocument.class,"thePersistInfo.theObjectIdentifier.id",EOutput.class,"documentReference.key.id"),new int[]{ll,kk});
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(EProject.class,"lastVersion",SearchCondition.IS_TRUE),new int[]{ii});
			
			/////검색 조건////
			if(division!=null && division.length()>0){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(PDMLinkProduct.class,"thePersistInfo.theObjectIdentifier.id","=",product),	new int[] { mm });
			}
			if(name!=null && name.length()>0){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(EProject.class, "name", SearchCondition.LIKE, "%" + name.trim() + "%", false), new int[]{ii});
			}
			if(taskName!=null && taskName.length()>0){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(ETaskNode.class, ETaskNode.NAME, SearchCondition.LIKE, "%" + taskName.trim() + "%", false), new int[]{jj});
			}
			if(docName!=null && docName.length()>0){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(WTDocument.class, "master>name", SearchCondition.LIKE, "%" + docName.trim() + "%", false), new int[]{ll});
			}
			if(tempowner!=null && tempowner.length()>0){
				qs.appendAnd();
				int idxUser = qs.addClassList(WTUser.class, false);
				qs.appendWhere(new SearchCondition(new ClassAttribute(WTUser.class,"thePersistInfo.theObjectIdentifier.id"), "=",
						new ClassAttribute(WTDocument.class,"iterationInfo.creator.key.id")),
						new int[] { idxUser, ll });
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(WTUser.class, "thePersistInfo.theObjectIdentifier.id","=", CommonUtil.getOIDLongValue(tempowner)), new int[] { idxUser });
			}
			if(folder!=null && folder.length()>0){
				qs.appendAnd();
				
				int folder_idx = qs.addClassList(IteratedFolderMemberLink.class, false);
				SearchCondition sc1 = new SearchCondition(IteratedFolderMemberLink.class,"roleBObjectRef.key.branchId",  WTDocument.class,"iterationInfo.branchId");
				qs.appendWhere(sc1, new int[] { folder_idx, ll });
			
				qs.appendAnd();
				
				Folder ff = (Folder)rf.getReference(folder).getObject();
				ArrayList folders = CommonFolderHelper.service.getFolderTree(ff);
				
				qs.appendOpenParen();
			
				qs.appendWhere(new SearchCondition(IteratedFolderMemberLink.class,"roleAObjectRef.key.id", SearchCondition.EQUAL,
						ff.getPersistInfo().getObjectIdentifier().getId()), new int[] { folder_idx });
			
				for (int fi = 0; fi < folders.size(); fi++) {
					String[] s = (String[]) folders.get(fi);
					Folder sf = (Folder) rf.getReference(s[2]).getObject();
					qs.appendOr();
					qs.appendWhere(new SearchCondition(
							IteratedFolderMemberLink.class,
							"roleAObjectRef.key.id", SearchCondition.EQUAL,
							sf.getPersistInfo().getObjectIdentifier()
							.getId()), new int[] { folder_idx });
				}
				qs.appendCloseParen();
			}
			
			ClassAttribute classattribute = new ClassAttribute(
			WTDocument.class, "thePersistInfo.createStamp");
			classattribute.setColumnAlias("sort0");
			int[] fieldNoArr = { ll };
			qs.appendSelect(classattribute, fieldNoArr, false);
			OrderBy orderby = new OrderBy(classattribute, true);
			qs.appendOrderBy(orderby, fieldNoArr);

    	}catch (Exception e) {
			e.printStackTrace();
		}
    	return qs;
    }
	
	/**
     * 이슈 관리 목록
     * @param hash
     * @return
     * @throws Exception 
     */
	@SuppressWarnings("rawtypes")
	@Override
    public QuerySpec getIssueList(Hashtable hash) throws Exception{

    	String name = (String)hash.get("name");
    	String state = (String)hash.get("state");
    	String issueType = (String)hash.get("issueType");
    	String role = (String)hash.get("role");
    	String number = (String)hash.get("number");
    	String manager = (String)hash.get("manager");
    	
    	String userOid = (String)hash.get("user");
    	
    	WTUser user = null;
    	
    	if( userOid != null && userOid.length() > 0 ){
    		user = (WTUser)CommonUtil.getObject(userOid);
		}
    	
		QuerySpec qs = new QuerySpec();
		
		int ll = qs.addClassList(IssueRequest.class,true);
		int kk = qs.addClassList(ETask.class,false);
		int ii = qs.addClassList(EProject.class,false);
		int jj = qs.addClassList(PDMLinkProduct.class,false);
			try{
			qs.appendWhere(new SearchCondition(ETask.class,"thePersistInfo.theObjectIdentifier.id",IssueRequest.class,"taskReference.key.id"),new int[]{kk,ll});
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(ETask.class,"projectReference.key.id",EProject.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{kk,ii});
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(EProject.class,"productReference.key.id",PDMLinkProduct.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{ii,jj});
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(EProject.class,"lastVersion",SearchCondition.IS_TRUE),new int[]{ii});
			
			
			if( number != null){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(ETask.class,"thePersistInfo.theObjectIdentifier.id",IssueRequest.class,"taskReference.key.id"),new int[]{kk,ll});
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(ETask.class,"projectReference.key.id",EProject.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{kk,ii});
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(EProject.class,"productReference.key.id",PDMLinkProduct.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{ii,jj});
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(EProject.class, EProject.CODE, SearchCondition.LIKE  , "%"+number.toUpperCase()+"%"),new int[]{ii});
				
			}
			
			////////////////검색 조건
			if( manager != null && manager.length() > 0 ){
				//user = CommonUtil.findUserID(manager);
				user = (WTUser)CommonUtil.getObject(manager);
			}
			if(user != null){
				if("manager".equals(role)){
					qs.appendAnd();
					qs.appendWhere(new SearchCondition(IssueRequest.class,"managerReference.key.id","=",user.getPersistInfo().getObjectIdentifier().getId()),new int[]{ll});
				}else if("creator".equals(role)){
					qs.appendAnd();
					qs.appendWhere(new SearchCondition(IssueRequest.class,"creatorReference.key.id","=",user.getPersistInfo().getObjectIdentifier().getId()),new int[]{ll});
				}else{
					qs.appendAnd();
					qs.appendOpenParen();
					qs.appendWhere(new SearchCondition(IssueRequest.class,"creatorReference.key.id","=",user.getPersistInfo().getObjectIdentifier().getId()),new int[]{ll});
					qs.appendOr();
					qs.appendWhere(new SearchCondition(IssueRequest.class,"managerReference.key.id","=",user.getPersistInfo().getObjectIdentifier().getId()),new int[]{ll});
					qs.appendCloseParen();
				}
			}
			if(issueType!=null && issueType.length()>0){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(IssueRequest.class,IssueRequest.ISSUE_TYPE,"=",issueType),new int[]{ll});
			}
			
			if(name!=null && name.length()>0){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(IssueRequest.class, IssueRequest.NAME, SearchCondition.LIKE, "%" + name.trim() + "%", false), new int[]{ll});
			}
			
			if("PROGRESS".equals(state)){
				qs.appendAnd();
				qs.appendOpenParen();
				qs.appendWhere(new SearchCondition(IssueRequest.class,IssueRequest.STATE,"=", IssueKey.ISSUE_REQUEST),new int[]{ll});
				qs.appendOr();
				qs.appendWhere(new SearchCondition(IssueRequest.class,IssueRequest.STATE,"=", IssueKey.ISSUE_CHECK),new int[]{ll});
				qs.appendCloseParen();
			}else if("COMPLETED".equals(state)){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(IssueRequest.class,IssueRequest.STATE,"=", IssueKey.ISSUE_COMPLETE),new int[]{ll});
			}
			
			////////////////////////////
			
			qs.appendOrderBy(new OrderBy(new ClassAttribute(IssueRequest.class,"thePersistInfo.createStamp"),true),new int[]{ll});

    	}catch (Exception e) {
			e.printStackTrace();
		}
    	return qs;
    }
}
