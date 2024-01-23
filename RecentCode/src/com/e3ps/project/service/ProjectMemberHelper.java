package com.e3ps.project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.query.SearchUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.project.EProjectNode;
import com.e3ps.project.ETask;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.ProjectRole;
import com.e3ps.project.RoleUserLink;
import com.e3ps.project.ScheduleNode;
import com.e3ps.project.TaskRoleLink;
import com.e3ps.project.beans.ProjectRoleData;
import com.e3ps.project.key.ProjectKey.PROJECTKEY;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.org.WTUser;
import wt.query.ClassAttribute;
import wt.query.ConstantExpression;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.ServiceFactory;
import wt.session.SessionHelper;
import wt.util.WTException;

public class ProjectMemberHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PROJECT.getName());
	public static final ProjectMemberService service = ServiceFactory.getService(ProjectMemberService.class);
	
	public static final ProjectMemberHelper manager = new ProjectMemberHelper();
	
	public List<ProjectRoleData> getRoleUserList(EProjectNode project)throws Exception{
	    
    	ArrayList<ProjectRoleData> list = new ArrayList<>();
		
    	List<Object[]> objList = ProjectMemberHelper.manager.getRoleUserLinkList(project);
    	
    	for(Object[] obj : objList) {
    		ProjectRoleData data = new ProjectRoleData(obj);
    		
    		list.add(data);
    	}
    	
		return list;
    }

	public List<Object[]> getRoleUserLinkList(EProjectNode project)throws Exception{
	    
    	ArrayList<Object[]> list = new ArrayList<>();
		try {
			QuerySpec qs = new QuerySpec();
			int ii = qs.addClassList(RoleUserLink.class, true);
			int jj = qs.addClassList(ProjectRole.class, true);

			qs.appendWhere(new SearchCondition(ProjectRole.class, "projectReference.key.id", "=",
					project.getPersistInfo().getObjectIdentifier().getId()), new int[] { jj });
			qs.appendAnd();
			SearchCondition sc = new SearchCondition(
					new ClassAttribute(ProjectRole.class, "thePersistInfo.theObjectIdentifier.id"), "=",
					new ClassAttribute(RoleUserLink.class, "roleBObjectRef.key.id"));
			sc.setFromIndicies(new int[] { jj, ii }, 0);
			sc.setOuterJoin(2);
			qs.appendWhere(sc, new int[] { jj, ii });
			
			qs.appendOrderBy(new OrderBy(new ClassAttribute(ProjectRole.class, ProjectRole.NAME), false), new int[]{jj});

			QueryResult qr = PersistenceHelper.manager.find(qs);
			while (qr.hasMoreElements()) {
				Object[] o = (Object[]) qr.nextElement();
				list.add(o);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new WTException(e);
		}
		return list;
    }
	
	public boolean isPM(ScheduleNode node, WTUser user)throws Exception{
    	return isRoleMember(node,PROJECTKEY.PM,user);
    }
    
    public boolean isRoleMember(ScheduleNode node,String role, WTUser user)throws Exception{
    	if(node instanceof ETaskNode){
    		node = ((ETaskNode)node).getProject();
		}
		QueryResult qr = null;
		try {
			QuerySpec qs = new QuerySpec();
			int ii = qs.addClassList(ProjectRole.class, true);
			int jj = qs.addClassList(RoleUserLink.class, false);
			qs.appendWhere(new SearchCondition(ProjectRole.class,
					"projectReference.key.id", "=", node.getPersistInfo()
							.getObjectIdentifier().getId()), new int[] { ii });
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(ProjectRole.class, "code", "=",role), new int[] { ii });
			qs.appendAnd();
			SearchCondition sc = new SearchCondition(ProjectRole.class,	"thePersistInfo.theObjectIdentifier.id", RoleUserLink.class, "roleBObjectRef.key.id");
			qs.appendWhere(sc, new int[] { ii, jj });
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(RoleUserLink.class,"roleAObjectRef.key.id", "=", user.getPersistInfo().getObjectIdentifier().getId()), new int[] { jj });

			qr = PersistenceHelper.manager.find(qs);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WTException(e);
		}
    	return qr.size()>0;
    }
    
    public QuerySpec taskRoleMemberQS(ScheduleNode node, WTUser user)throws Exception{
    	QuerySpec qs = new QuerySpec();
    	
		int ii = qs.addClassList(RoleUserLink.class, true);
		int jj = qs.addClassList(ProjectRole.class, true);
		int kk = qs.addClassList(TaskRoleLink.class, true);
		
		qs.appendWhere(new SearchCondition(TaskRoleLink.class, "roleBObjectRef.key.id", "=", node.getPersistInfo().getObjectIdentifier().getId()), new int[] { kk });
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(ProjectRole.class, "thePersistInfo.theObjectIdentifier.id", TaskRoleLink.class, "roleAObjectRef.key.id"), new int[] { jj, kk });
		qs.appendAnd();
		SearchCondition sc = new SearchCondition( new ClassAttribute(ProjectRole.class, "thePersistInfo.theObjectIdentifier.id"), "=", new ClassAttribute(RoleUserLink.class, "roleBObjectRef.key.id"));
		qs.appendWhere(sc, new int[] { jj, ii });
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(RoleUserLink.class, "roleAObjectRef.key.id", "=", user.getPersistInfo().getObjectIdentifier().getId()), new int[] { ii });
    	
    	return qs;
    }
 
    public boolean isTaskRoleMember(ScheduleNode node, WTUser user)throws Exception{

    	QueryResult qr = null;
		try {
			QuerySpec qs = taskRoleMemberQS(node, user);

			qr = PersistenceHelper.manager.find(qs);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WTException(e);
		}
    	return qr.size()>0;
    }
    
    public boolean taskRoleMemberCodeCheck(ScheduleNode node) throws Exception {
    	WTUser user = (WTUser) SessionHelper.manager.getPrincipal();
    	return taskRoleMemberCodeCheck(node,user);
    }
   
    public boolean taskRoleMemberCodeCheck(ScheduleNode node, WTUser user)throws Exception{
    	boolean chk = false;
    	QueryResult qr = null;
		try {
			QuerySpec qs = taskRoleMemberQS(node, user);

			qr = PersistenceHelper.manager.find(qs);
			if(qr.size() == 1) {
				while(qr.hasMoreElements()) {
					Object[] o = (Object[])qr.nextElement();
					ProjectRole pr = (ProjectRole)o[1];
					
					String code = pr.getCode();
					if("PR00".equals(code)) {
						chk = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new WTException(e);
		}
    	return chk;
    }
    
    public List<ProjectRoleData> getOwner(ETask task)throws Exception{
	    
    	ArrayList<ProjectRoleData> list = new ArrayList<>();
		
    	List<Object[]> objList = ProjectMemberHelper.manager.getOwnerLinkList(task);
    	
    	for(Object[] obj : objList) {
    		ProjectRoleData data = new ProjectRoleData(obj);
    		
    		list.add(data);
    	}
    	
		return list;
    }

    public List<Object[]> getOwnerLinkList(ETask task)throws Exception{
    	
    	List<Object[]> list = new ArrayList<>();
    	
		QuerySpec qs = new QuerySpec();
		
		int ii = qs.addClassList(RoleUserLink.class, true);
		int jj = qs.addClassList(ProjectRole.class, true);
		int kk = qs.addClassList(TaskRoleLink.class, true);
		
		qs.appendWhere(new SearchCondition(TaskRoleLink.class, "roleBObjectRef.key.id", "=",
				task.getPersistInfo().getObjectIdentifier().getId()), new int[] { kk });
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(ProjectRole.class, "thePersistInfo.theObjectIdentifier.id",
				TaskRoleLink.class, "roleAObjectRef.key.id"), new int[] { jj, kk });
		qs.appendAnd();
		SearchCondition sc = new SearchCondition(
				new ClassAttribute(ProjectRole.class, "thePersistInfo.theObjectIdentifier.id"), "=",
				new ClassAttribute(RoleUserLink.class, "roleBObjectRef.key.id"));
		sc.setFromIndicies(new int[] { jj, ii }, 0);
		sc.setOuterJoin(2);
		qs.appendWhere(sc, new int[] { jj, ii });

		QueryResult qr = PersistenceHelper.manager.find(qs);
		while (qr.hasMoreElements()) {
			Object[] o = (Object[]) qr.nextElement();
			list.add(o);
		}
    	
    	return list;
    }
    
	public WTUser getPM(ScheduleNode node) throws Exception {

		if (node instanceof ETaskNode) {
			node = ((ETaskNode) node).getProject();
		}

		try {

			QuerySpec qs = new QuerySpec();
			int ii = qs.addClassList(ProjectRole.class, true);
			int jj = qs.addClassList(RoleUserLink.class, true);

			qs.appendWhere(new SearchCondition(ProjectRole.class, "projectReference.key.id", "=",
					node.getPersistInfo().getObjectIdentifier().getId()), new int[] { ii });
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(ProjectRole.class, "code", "=", PROJECTKEY.PM), new int[] { ii });
			qs.appendAnd();
			SearchCondition sc = new SearchCondition(
					new ClassAttribute(ProjectRole.class, "thePersistInfo.theObjectIdentifier.id"), "=",
					new ClassAttribute(RoleUserLink.class, "roleBObjectRef.key.id"));
			sc.setFromIndicies(new int[] { ii, jj }, 0);
			sc.setOuterJoin(2);
			qs.appendWhere(sc, new int[] { ii, jj });

			QueryResult qr = PersistenceHelper.manager.find(qs);
			if (qr.hasMoreElements()) {
				Object[] obj = (Object[]) qr.nextElement();
				RoleUserLink link = (RoleUserLink) obj[1];

				if (link != null) {
					return link.getUser();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new WTException(e);
		}

		return null;
	}
	
	public Map<String, Object> searchProjectRoleUserList(Map<String, Object> reqMap) throws Exception{
		
		//본인 제외 프로젝트 롤에 등록된 인원 목록을 가져온다.
		
		List<ProjectRoleData> list = new ArrayList<ProjectRoleData>();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String keyword = StringUtil.checkNull((String) reqMap.get("keyword"));
		
		QuerySpec qs = new QuerySpec();
		
		SearchCondition sc = null;
		
		int userLinkIdx = qs.addClassList(RoleUserLink.class, true);
		int roleIdx = qs.addClassList(ProjectRole.class, true);
		int userIdx = qs.addClassList(WTUser.class, false);
		
		
		sc = new SearchCondition(SearchUtil.getQK(ProjectRole.class, "IDA2A2"), SearchCondition.EQUAL, SearchUtil.getQK(RoleUserLink.class, "IDA3B5"));
		qs.appendWhere(sc, new int[] {roleIdx, userLinkIdx});
		qs.appendAnd();
		sc = new SearchCondition(SearchUtil.getQK(RoleUserLink.class, "IDA3A5"), SearchCondition.EQUAL, SearchUtil.getQK(WTUser.class, "IDA2A2"));
		qs.appendWhere(sc, new int[] {userLinkIdx, userIdx});
		qs.appendAnd();
		sc = new SearchCondition(SearchUtil.getQK(ProjectRole.class, "IDA3A3"), SearchCondition.EQUAL, new ConstantExpression(CommonUtil.getOIDLongValue(oid)));
		qs.appendWhere(sc, new int[] {roleIdx});
		
		if(keyword.length() > 0) {
			if(qs.getConditionCount() > 0) qs.appendAnd();
			sc = new SearchCondition(SearchUtil.getQK(WTUser.class, WTUser.FULL_NAME), SearchCondition.LIKE, new ConstantExpression("%"+keyword+"%"));
			qs.appendWhere(sc, new int[] {userIdx});
		}
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		while (qr.hasMoreElements()) {
			Object[] o = (Object[]) qr.nextElement();
			ProjectRoleData data = new ProjectRoleData(o);
    		list.add(data);
		}
		
		reqMap.put("list", list);
		
		return reqMap;
	}
}
