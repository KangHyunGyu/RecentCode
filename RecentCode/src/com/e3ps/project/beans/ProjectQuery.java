package com.e3ps.project.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.web.ParamUtil;
import com.e3ps.doc.E3PSDocument;
import com.e3ps.org.People;
import com.e3ps.project.EOutput;
import com.e3ps.project.EProject;
import com.e3ps.project.EProjectMasteredLink;
import com.e3ps.project.EProjectNode;
import com.e3ps.project.ETask;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.ProjectRole;
import com.e3ps.project.RoleUserLink;
import com.e3ps.project.ScheduleNode;
import com.e3ps.project.TaskRoleLink;
import com.e3ps.project.issue.IssueRequest;
import com.e3ps.project.key.ProjectKey.IssueKey;
import com.e3ps.project.key.ProjectKey.PROJECTKEY;
import com.e3ps.project.key.ProjectKey.STATEKEY;

import wt.fc.ReferenceFactory;
import wt.org.WTUser;
import wt.pdmlink.PDMLinkProduct;
import wt.query.ArrayExpression;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.query.SubSelectExpression;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;
import wt.vc.Mastered;

public class ProjectQuery {
	
	static final boolean SERVER = wt.method.RemoteMethodServer.ServerFlag;
    public static ProjectQuery manager = new ProjectQuery();
    
    /**
     * 프로젝트 이력 목록
     * @param projectCode
     * @return
     * @throws QueryException
     */
    public QuerySpec getProjectList(String projectCode) throws WTException{

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
    
	/** 프로젝트 목록 검색 쿼리
     * @param hash
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public QuerySpec getProjectListQuery(Map<String, Object> reqMap) throws Exception{
    	
    	//소팅
		Map<String, Object> sort = reqMap.get("sort") != null ? (Map<String, Object>) reqMap.get("sort") : new HashMap<>();
    			
		String name = StringUtil.checkNull((String) reqMap.get("name"));
		name = name.replace("[", "[[]");
		String number = StringUtil.checkNull((String) reqMap.get("number"));
		String planStartDate = StringUtil.checkNull((String) reqMap.get("planStartDate"));
		String planStartDateE = StringUtil.checkNull((String) reqMap.get("planStartDateE"));
		String planEndDate = StringUtil.checkNull((String) reqMap.get("planEndDate"));
		String planEndDateE = StringUtil.checkNull((String) reqMap.get("planEndDateE"));
		
		
		String groupCode = StringUtil.checkNull((String) reqMap.get("groupCode"));
		String materialCode = StringUtil.checkNull((String) reqMap.get("materialCode"));
		String levelCode = StringUtil.checkNull((String) reqMap.get("levelCode"));
		
		
		List<String> state = StringUtil.checkReplaceArray(reqMap.get("state"));
		
		List<String> pmList = StringUtil.checkReplaceArray(reqMap.get("pm"));

		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(EProject.class, true);
		int jj = qs.addClassList(PDMLinkProduct.class, false);

    	qs.appendWhere(new SearchCondition(EProject.class,"productReference.key.id",PDMLinkProduct.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{ii,jj});
    	qs.appendAnd();
    	qs.appendWhere(new SearchCondition(EProject.class,"lastVersion",SearchCondition.IS_TRUE),new int[]{0});
    	
		if (name.length() > 0) {
    		qs.appendAnd();
    		qs.appendWhere(new SearchCondition(EProject.class, "name", SearchCondition.LIKE, "%" + name.trim() + "%", false), new int[]{ii});
    	}
    	
		if (number.length() > 0) {
    		qs.appendAnd();
    		qs.appendWhere(new SearchCondition(EProject.class, EProject.CODE, SearchCondition.LIKE, "%" + number.trim() + "%", false), new int[]{ii});
    	}
    	
		if (state.size() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			qs.appendWhere(new SearchCondition(new ClassAttribute(EProject.class, EProject.LIFE_CYCLE_STATE), SearchCondition.IN, new ArrayExpression(state.toArray())), new int[] { ii });
		}

    	if(planStartDate!=null && planStartDate.length()>0){
    		qs.appendAnd();
    		qs.appendWhere(new SearchCondition(EProject.class,EProject.PLAN_START_DATE,SearchCondition.GREATER_THAN_OR_EQUAL, DateUtil.convertStartDate(planStartDate)),	new int[] { ii });
    	}
    	
    	if(planStartDateE!=null && planStartDateE.length()>0){
    		qs.appendAnd();
    		qs.appendWhere(new SearchCondition(EProject.class,EProject.PLAN_START_DATE,SearchCondition.LESS_THAN, DateUtil.convertEndDate(planStartDateE)),	new int[] { ii });
    	}
    	
    	if(planEndDate!=null && planEndDate.length()>0){
    		qs.appendAnd();
    		qs.appendWhere(new SearchCondition(EProject.class,EProject.PLAN_END_DATE,SearchCondition.GREATER_THAN_OR_EQUAL, DateUtil.convertStartDate(planEndDate)),	new int[] { ii });
    	}
    	
    	if(planEndDateE!=null && planEndDateE.length()>0){
    		qs.appendAnd();
    		qs.appendWhere(new SearchCondition(EProject.class,EProject.PLAN_END_DATE,SearchCondition.LESS_THAN, DateUtil.convertEndDate(planEndDateE)),	new int[] { ii });
    	}
    	
    	if(groupCode!=null && groupCode.length()>0){
    		qs.appendAnd();
    		qs.appendWhere(new SearchCondition(EProject.class,EProject.GROUP_CODE,SearchCondition.EQUAL, groupCode),	new int[] { ii });
    	}
    	
    	if(materialCode!=null && materialCode.length()>0){
    		qs.appendAnd();
    		qs.appendWhere(new SearchCondition(EProject.class,EProject.MATERIAL_CODE,SearchCondition.EQUAL, materialCode),	new int[] { ii });
    	}
    	
    	if(levelCode!=null && levelCode.length()>0){
    		qs.appendAnd();
    		qs.appendWhere(new SearchCondition(EProject.class,EProject.LEVEL_CODE,SearchCondition.EQUAL, levelCode),	new int[] { ii });
    	}
    	
    	
    	if(!CommonUtil.isAdmin()) {
    		int roleIdx = qs.addClassList(ProjectRole.class, false);
		    int linkIdx = qs.addClassList(RoleUserLink.class,false);
		    
		    List<Long> userOidList = new ArrayList<>();
		    
		    WTUser user = (WTUser) SessionHelper.manager.getPrincipal();
	    	userOidList.add(CommonUtil.getOIDLongValue(user));
    		
    		qs.appendAnd();
    		SearchCondition sc = new SearchCondition(new ClassAttribute(EProject.class, EProject.PERSIST_INFO + ".theObjectIdentifier.id"), SearchCondition.EQUAL, new ClassAttribute(ProjectRole.class, "projectReference.key.id"));
    	    qs.appendWhere(sc, new int[] { ii, roleIdx });
    	    
    	    /*
    	    qs.appendAnd();
    	    qs.appendWhere(new SearchCondition(ProjectRole.class, ProjectRole.CODE, SearchCondition.EQUAL, PROJECTKEY.PM), new int[] { roleIdx });
    	    */
    	    
    	    qs.appendAnd();
    	    sc = new SearchCondition(new ClassAttribute(ProjectRole.class, ProjectRole.PERSIST_INFO + ".theObjectIdentifier.id"), SearchCondition.EQUAL, new ClassAttribute(RoleUserLink.class, "roleBObjectRef.key.id"));
    	    qs.appendWhere(sc, new int[] { roleIdx, linkIdx });
    	    
    	    qs.appendAnd();
    	    qs.appendWhere(new SearchCondition(new ClassAttribute(RoleUserLink.class, "roleAObjectRef.key.id"), SearchCondition.IN, new ArrayExpression(userOidList.toArray())), new int[] { linkIdx });
    	}
    	
    	
    	
    	// 소팅
		if (sort.size() > 0) {
			String id = (String) sort.get("id");
			String dir = (String) sort.get("dir");
			
			String sortValue = "";
			if("code".equals(id)) {
				sortValue = EProject.CODE;
			} else if("name".equals(id)) {
				sortValue = "name";
			} else if("planStartDate".equals(id)) {
				sortValue = EProject.PLAN_START_DATE;
			} else if("planEndDate".equals(id)) {
				sortValue = EProject.PLAN_END_DATE;
			} else if("stateTag".equals(id)) {
				sortValue = EProject.LIFE_CYCLE_STATE;
			} else if("createDate".equals(id)) {
				sortValue = EProject.CREATE_TIMESTAMP;
			}
			
			if ("asc".equals(dir)) {
				qs.appendOrderBy(new OrderBy(new ClassAttribute(EProject.class, sortValue), false), new int[] { ii });
			} else if("desc".equals(dir)) {
				qs.appendOrderBy(new OrderBy(new ClassAttribute(EProject.class, sortValue), true), new int[] { ii });
			}
		} else {
			qs.appendOrderBy(new OrderBy(new ClassAttribute(EProject.class,EProject.CODE), true), new int[] { ii });
		}
		
    	
		return qs;
    }
    
    /**
     * 나의 프로젝트 목록
     * 210531 gskang 중복 수정
     */
    public QuerySpec getMyProject(Map<String, Object> reqMap) throws WTException, WTPropertyVetoException{
    	
    	QuerySpec qs = new QuerySpec();
    	
    	int ii = qs.addClassList(EProject.class,true);
    	qs.setAdvancedQueryEnabled(true);
    	
    	QuerySpec subQs = myProjectSubQuerySelect(reqMap);
		
    	qs.appendWhere(new SearchCondition(new ClassAttribute(EProject.class, "thePersistInfo.theObjectIdentifier.id"), 
    			SearchCondition.IN, new SubSelectExpression(subQs)), new int[] {ii});
    	
    	qs.appendOrderBy(new OrderBy(new ClassAttribute(EProject.class,"thePersistInfo.createStamp"), true), new int[] { ii });
    	
    	return qs;
    }
    
    /**
     * 나의 프로젝트 목록
     * 210531 gskang 중복 수정
     */
    private QuerySpec myProjectSubQuerySelect(Map<String, Object> reqMap) throws WTException, WTPropertyVetoException {
    	WTUser user = (WTUser)SessionHelper.manager.getPrincipal();
    	
    	String name = StringUtil.checkNull((String) reqMap.get("name"));
    	String number = StringUtil.checkNull((String) reqMap.get("number"));
    	String planStartDate = StringUtil.checkNull((String) reqMap.get("planStartDate"));
    	String planStartDateE = StringUtil.checkNull((String) reqMap.get("planStartDateE"));
    	List<String> state = StringUtil.checkReplaceArray(reqMap.get("state"));
    	
    	QuerySpec qs = new QuerySpec();
    	qs.getFromClause().setAliasPrefix("C");
    	
    	int ii = qs.addClassList(EProject.class,false);
    	int jj = qs.addClassList(PDMLinkProduct.class,false);
    	int kk = qs.addClassList(ProjectRole.class,false);
    	int ll = qs.addClassList(RoleUserLink.class,false);
    	qs.setDistinct(true);
    	qs.appendSelect(new ClassAttribute(EProject.class, "thePersistInfo.theObjectIdentifier.id"),false);
    	
    	qs.appendWhere(new SearchCondition(EProject.class,"productReference.key.id",PDMLinkProduct.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{ii,jj});
    	
    	qs.appendAnd();
    	qs.appendWhere(new SearchCondition(EProject.class,"lastVersion",SearchCondition.IS_TRUE),new int[]{ii});
    	
    	qs.appendAnd();
    	qs.appendWhere(new SearchCondition(EProject.class,"thePersistInfo.theObjectIdentifier.id",ProjectRole.class,"projectReference.key.id"),new int[]{ii,kk});
    	
    	qs.appendAnd();
    	qs.appendWhere(new SearchCondition(ProjectRole.class,"thePersistInfo.theObjectIdentifier.id",RoleUserLink.class,"roleBObjectRef.key.id"),new int[]{kk,ll});
    	
    	qs.appendAnd();
    	qs.appendWhere(new SearchCondition(RoleUserLink.class,"roleAObjectRef.key.id","=",CommonUtil.getOIDLongValue(user)),new int[]{ll});

    	if(planStartDate.length()>0){
    		qs.appendAnd();
    		qs.appendWhere(new SearchCondition(EProject.class,EProject.PLAN_START_DATE,SearchCondition.GREATER_THAN_OR_EQUAL, DateUtil.convertStartDate(planStartDate)),	new int[] { ii });
    	}
    	
    	if(planStartDateE.length()>0){
    		qs.appendAnd();
    		qs.appendWhere(new SearchCondition(EProject.class,EProject.PLAN_START_DATE,SearchCondition.LESS_THAN, DateUtil.convertEndDate(planStartDateE)),	new int[] { ii });
    	}
    	
    	if(name.length()>0){
    		qs.appendAnd();
    		qs.appendWhere(new SearchCondition(EProject.class, "name", SearchCondition.LIKE, "%" + name.trim() + "%", false), new int[]{ii});
    	}
    	
    	if(number.length()>0){
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(EProject.class, EProject.CODE, SearchCondition.LIKE, "%" + number.trim() + "%", false), new int[]{ii});
		}

    	if (state.size() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			qs.appendWhere(new SearchCondition(new ClassAttribute(EProject.class, EProject.LIFE_CYCLE_STATE), SearchCondition.IN, new ArrayExpression(state.toArray())), new int[] { ii });
		}
    	
    	return qs;
    }
    
    /**
     * 나의 태스크 목록
     * @param hash
     * @return
     * @throws Exception 
     */
	public QuerySpec getMyTask(Map<String, Object> reqMap) throws WTException {

		WTUser user = (WTUser) SessionHelper.manager.getPrincipal();

		String name = StringUtil.checkNull((String) reqMap.get("name"));
		String planStartDate = StringUtil.checkNull((String) reqMap.get("planStartDate"));
		String planStartDateE = StringUtil.checkNull((String) reqMap.get("planStartDateE"));
		String planEndDate = StringUtil.checkNull((String) reqMap.get("planEndDate"));
		String planEndDateE = StringUtil.checkNull((String) reqMap.get("planEndDateE"));
		List<String> state = StringUtil.checkReplaceArray(reqMap.get("state"));

		QuerySpec qs = new QuerySpec();
		try {

			int ii = qs.addClassList(ETask.class, true);
			int jj = qs.addClassList(PDMLinkProduct.class, false);
			int kk = qs.addClassList(EProject.class, false);
			int ll = qs.addClassList(TaskRoleLink.class, false);
			int mm = qs.addClassList(ProjectRole.class, false);
			int nn = qs.addClassList(RoleUserLink.class, false);
			
			qs.appendSelect(new ClassAttribute(ETask.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{ii},true);
			
			qs.appendWhere(new SearchCondition(ETask.class,"projectReference.key.id",EProject.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{ii,kk});
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(EProject.class,"productReference.key.id",PDMLinkProduct.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{kk,jj});
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(EProject.class,"lastVersion",SearchCondition.IS_TRUE),new int[]{kk});
			
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(ETask.class,"thePersistInfo.theObjectIdentifier.id",TaskRoleLink.class,"roleBObjectRef.key.id"),new int[]{ii,ll});
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(ProjectRole.class,"thePersistInfo.theObjectIdentifier.id",TaskRoleLink.class,"roleAObjectRef.key.id"),new int[]{mm,ll});
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(RoleUserLink.class,"roleBObjectRef.key.id",ProjectRole.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{nn,mm});
			
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(RoleUserLink.class,"roleAObjectRef.key.id","=",CommonUtil.getOIDLongValue(user)),new int[]{nn});
			
			//////////////// 검색 조건
			if(planStartDate.length()>0){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(ETask.class,ETask.PLAN_START_DATE,SearchCondition.GREATER_THAN_OR_EQUAL, DateUtil.convertStartDate(planStartDate)),	new int[] { ii });
			}
			
			if(planStartDateE.length()>0){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(ETask.class,ETask.PLAN_START_DATE,SearchCondition.LESS_THAN, DateUtil.convertEndDate(planStartDateE)),	new int[] { ii });
			}
			
			if(planEndDate.length()>0){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(ETask.class,ETask.PLAN_END_DATE,SearchCondition.GREATER_THAN_OR_EQUAL, DateUtil.convertStartDate(planEndDate)),	new int[] { ii });
			}
			
			if(planEndDateE.length()>0){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(ETask.class,ETask.PLAN_END_DATE,SearchCondition.LESS_THAN, DateUtil.convertEndDate(planEndDateE)),	new int[] { ii });
			}
			
			if(name.length()>0){
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(ETask.class, ETask.NAME, SearchCondition.LIKE, "%" + name.trim() + "%", false), new int[]{ii});
			}
			
			if (state.size() > 0) {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(new ClassAttribute(ETask.class, ETask.STATUS), SearchCondition.IN, new ArrayExpression(state.toArray())), new int[] { ii });
			}
			
			qs.appendOrderBy(new OrderBy(new ClassAttribute(ETask.class, "planStartDate"), false), new int[] { ii });

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(qs);
		return qs;
    }
    
    /**
     * 나의 이슈 목록
     * @param hash
     * @return
     * @throws Exception 
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public QuerySpec getMyIssue(Map hash) throws WTException{
    	
    	WTUser user = (WTUser)SessionHelper.manager.getPrincipal();
    	long userOid = CommonUtil.getOIDLongValue(user);
    	
    	String name = ParamUtil.get(hash,"name");
    	String state = ParamUtil.get(hash,"state");
    	String issueType = ParamUtil.get(hash,"issueType");
    	String role = ParamUtil.get(hash,"role");
    	
    	boolean searchAll = ParamUtil.getBoolean(hash, "searchAll");
    	
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
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(EProject.class, "state.state", SearchCondition.EQUAL, STATEKEY.PROGRESS), new int[] { ii });
			
			if(!searchAll){
				if("manager".equals(role)){
					qs.appendAnd();
					qs.appendWhere(new SearchCondition(IssueRequest.class,"managerReference.key.id","=",userOid),new int[]{ll});
				}else if("creator".equals(role)){
					qs.appendAnd();
					qs.appendWhere(new SearchCondition(IssueRequest.class,"creatorReference.key.id","=",userOid),new int[]{ll});
				}else{
					qs.appendAnd();
					qs.appendOpenParen();
					qs.appendWhere(new SearchCondition(IssueRequest.class,"creatorReference.key.id","=",userOid),new int[]{ll});
					qs.appendOr();
					qs.appendWhere(new SearchCondition(IssueRequest.class,"managerReference.key.id","=",userOid),new int[]{ll});
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
			}else{
				qs.appendAnd();
				qs.appendOpenParen();
				qs.appendWhere(new SearchCondition(IssueRequest.class,"creatorReference.key.id","=",userOid),new int[]{ll});
				qs.appendOr();
				qs.appendWhere(new SearchCondition(IssueRequest.class,"managerReference.key.id","=",userOid),new int[]{ll});
				qs.appendCloseParen();
			}
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
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public QuerySpec getOutputList(Map<String, Object> hash) throws WTException{
    	
    	//소팅
		Map<String, Object> sort = hash.get("sort") != null ? (Map<String, Object>) hash.get("sort") : new HashMap<>();
    			
		String pjtNumber = ParamUtil.get(hash, "pjtNumber");
		String pjtName = ParamUtil.get(hash, "pjtName");
		String taskName = ParamUtil.get(hash, "taskName");
		String docName = ParamUtil.get(hash, "docName");

		QuerySpec qs = new QuerySpec();

		int ii = qs.addClassList(EOutput.class, true);
		int jj = qs.addClassList(ETaskNode.class, false);
		int kk = qs.addClassList(EProject.class, false);
		int ll = qs.addClassList(E3PSDocument.class, false);
		int mm = qs.addClassList(PDMLinkProduct.class, false);
			
		qs.appendWhere(new SearchCondition(EProject.class,"productReference.key.id",PDMLinkProduct.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{kk,mm});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(EProject.class,"thePersistInfo.theObjectIdentifier.id",ETaskNode.class,"projectReference.key.id"),new int[]{kk,jj});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(ETaskNode.class,"thePersistInfo.theObjectIdentifier.id",EOutput.class,"taskReference.key.id"),new int[]{jj,ii});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(E3PSDocument.class,"thePersistInfo.theObjectIdentifier.id",EOutput.class,"documentReference.key.id"),new int[]{ll,ii});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(EProject.class,"lastVersion",SearchCondition.IS_TRUE),new int[]{kk});
		
		/////검색 조건////
		if (pjtNumber.length() > 0) {
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(EProject.class, EProject.CODE, SearchCondition.LIKE, "%" + pjtNumber.trim() + "%", false), new int[]{kk});
		}
		
		if (pjtName.length() > 0) {
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(EProject.class, "name", SearchCondition.LIKE, "%" + pjtName.trim() + "%", false), new int[]{kk});
		}
		
		if (taskName.length() > 0) {
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(ETaskNode.class, ETaskNode.NAME, SearchCondition.LIKE, "%" + taskName.trim() + "%", false), new int[]{jj});
		}
		
		if (docName.length() > 0) {
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(E3PSDocument.class, E3PSDocument.NAME, SearchCondition.LIKE, "%" + docName.trim() + "%", false), new int[] { ll });
		}
		
		// 소팅
		if (sort.size() > 0) {
			String id = (String) sort.get("id");
			String dir = (String) sort.get("dir");
			
			String sortValue = "";
			Class cls = null;
			int idx = ii;
			if("pjtNumber".equals(id)) {
				sortValue = EProject.CODE;
				cls = EProject.class;
				idx = kk;
			} else if("pjtName".equals(id)) {
				sortValue = "name";
				cls = EProject.class;
				idx = kk;
			} else if("taskName".equals(id)) {
				sortValue = ETask.NAME;
				cls = ETask.class;
				idx = jj;
			} else if("docName".equals(id)) {
				sortValue = E3PSDocument.NAME;
				cls = E3PSDocument.class;
				idx = ll;
			} else if("docCreateDate".equals(id)) {
				sortValue = E3PSDocument.CREATE_TIMESTAMP;
				cls = E3PSDocument.class;
				idx = ll;
			} else if("docStateName".equals(id)) {
				sortValue = E3PSDocument.LIFE_CYCLE_STATE;
				cls = E3PSDocument.class;
				idx = ll;
			}
			
			if ("asc".equals(dir)) {
				qs.appendOrderBy(new OrderBy(new ClassAttribute(cls, sortValue), false), new int[] { idx });
			} else if("desc".equals(dir)) {
				qs.appendOrderBy(new OrderBy(new ClassAttribute(cls, sortValue), true), new int[] { idx });
			}
		} else {
			qs.appendOrderBy(new OrderBy(new ClassAttribute(E3PSDocument.class, E3PSDocument.CREATE_TIMESTAMP), true), new int[] { ll });
		}

		return qs;
	}
    
    /**
     * 이슈 관리 목록
     * @param hash
     * @return
     * @throws Exception 
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public QuerySpec getIssueList(Map hash) throws WTException{

    	WTUser user = null;
    	
    	String name = ParamUtil.get(hash,"name");
    	String state = ParamUtil.get(hash,"state");
    	String issueType = ParamUtil.get(hash,"issueType");
    	String role = ParamUtil.get(hash,"role");
    	String number = ParamUtil.get(hash,"number");
    	String manager = ParamUtil.get(hash,"manager");
    	boolean searchAll = ParamUtil.getBoolean(hash, "searchAll");
    	
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
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(EProject.class, "state.state", SearchCondition.EQUAL, STATEKEY.PROGRESS), new int[] { ii });
			
			if(!searchAll){
				////////////////검색 조건
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
				if( manager != null && manager.length() > 0 ){
					if(CommonUtil.getObject(manager) == null){
						user = CommonUtil.findUserID(manager);
					}else{
						People pp = (People)CommonUtil.getObject(manager);
						user = pp.getUser();
					}
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
					qs.appendWhere(new SearchCondition(IssueRequest.class,IssueRequest.STATE,"=",IssueKey.ISSUE_REQUEST),new int[]{ll});
					qs.appendOr();
					qs.appendWhere(new SearchCondition(IssueRequest.class,IssueRequest.STATE,"=",IssueKey.ISSUE_CHECK),new int[]{ll});
					qs.appendCloseParen();
				}else if("COMPLETED".equals(state)){
					qs.appendAnd();
					qs.appendWhere(new SearchCondition(IssueRequest.class,IssueRequest.STATE,"=",IssueKey.ISSUE_COMPLETE),new int[]{ll});
				}
			}
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
    public QuerySpec getProjectOutputList(Map<String, Object> hash) throws WTException{

		String oid = StringUtil.checkNull((String) hash.get("oid"));
		ScheduleNode node = (ScheduleNode) CommonUtil.getObject(oid);

		if (node instanceof ETaskNode) {
			node = ((ETaskNode) node).getProject();
		}

		EProjectNode project = (EProjectNode) node;

		QuerySpec qs = new QuerySpec();

		int ii = qs.addClassList(EOutput.class, true);
		int jj = qs.addClassList(ETaskNode.class, false);
		
		qs.appendWhere(new SearchCondition(ETaskNode.class, "thePersistInfo.theObjectIdentifier.id", EOutput.class, "taskReference.key.id"), new int[] { jj, ii });
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(ETaskNode.class, "projectReference.key.id", "=",	CommonUtil.getOIDLongValue(project)), new int[] { jj });
		qs.appendOrderBy(new OrderBy(new ClassAttribute(EOutput.class, "thePersistInfo.createStamp"), false), new int[] { ii });

    	return qs;
    }
    
    public QuerySpec getProjectMasteredList(Map<String, Object> hash) throws WTException{
    	ReferenceFactory rf = new ReferenceFactory();
    	
    	String oid = StringUtil.checkNull((String) hash.get("oid"));
    	String type = StringUtil.checkNull((String) hash.get("type"));
    	
    	ScheduleNode node = (ScheduleNode)rf.getReference(oid).getObject();

    	if(node instanceof ETaskNode){
    		node = ((ETaskNode)node).getProject();
    	}

    	EProject project = (EProject)node;
    	QuerySpec qs = new QuerySpec();
    	int ii = qs.addClassList(EProjectMasteredLink.class,true);

    	qs.appendWhere(new SearchCondition(EProjectMasteredLink.class, "linkType", SearchCondition.EQUAL, type), new int[]{ ii });
    	qs.appendAnd();
    	qs.appendWhere(new SearchCondition(EProjectMasteredLink.class, "roleBObjectRef.key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(project)), new int[]{ ii });
    	
    	return qs;
    }
    
    /**
	 * @desc	: 프로젝트  MasteredLink 찾기
	 * @author	: sangylee
	 * @date	: 2020. 9. 25.
	 * @method	: getProjectMasteredLink
	 * @param	: mastered
	 * @return	: QuerySpec
	 * @throws Exception 
	 */
    public QuerySpec getProjectMasteredLink(Mastered mastered) throws WTException{
    	
    	QuerySpec qs = new QuerySpec();
    	int ii = qs.addClassList(EProjectMasteredLink.class,true);

    	qs.appendWhere(new SearchCondition(EProjectMasteredLink.class, EProjectMasteredLink.ROLE_AOBJECT_REF + ".key.classname", SearchCondition.EQUAL, mastered.getClassInfo().getClassname()), new int[]{ ii });
    	
    	qs.appendAnd();
    	qs.appendWhere(new SearchCondition(EProjectMasteredLink.class, EProjectMasteredLink.ROLE_AOBJECT_REF + ".key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(mastered)), new int[]{ ii });
    	
    	return qs;
    }
}
