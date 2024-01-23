package com.e3ps.project.service;

import java.util.ArrayList;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.org.WTUser;
import wt.query.ClassAttribute;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.StandardManager;
import wt.util.WTException;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.project.EProjectNode;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.ProjectRole;
import com.e3ps.project.RoleUserLink;
import com.e3ps.project.ScheduleNode;
import com.e3ps.project.TaskRoleLink;
import com.e3ps.project.key.ProjectKey.PROJECTKEY;


public class StandardProjectMemberService extends StandardManager implements wt.method.RemoteAccess, java.io.Serializable, ProjectMemberService {
    
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PROJECT.getName());
	
    public static StandardProjectMemberService newStandardProjectMemberService() throws WTException {
    	StandardProjectMemberService instance = new StandardProjectMemberService();
		instance.initialize();
		return instance;
	}
    
    @Override
    public ArrayList<Object> getUserList(EProjectNode project)throws Exception{
    
    	ArrayList<Object> result = new ArrayList<Object>();
    	try{
    	QuerySpec qs = new QuerySpec();
    	int ii = qs.addClassList(ProjectRole.class, true);
    	int jj = qs.addClassList(RoleUserLink.class,true);
    	
    	qs.appendWhere(new SearchCondition(ProjectRole.class,"projectReference.key.id","=",project.getPersistInfo().getObjectIdentifier().getId()),new int[]{ii});
    	qs.appendAnd();
    	SearchCondition sc = new SearchCondition(new ClassAttribute(ProjectRole.class,
        "thePersistInfo.theObjectIdentifier.id"), "=", new ClassAttribute(RoleUserLink.class,
        "roleBObjectRef.key.id"));
    	sc.setFromIndicies(new int[] { ii, jj }, 0);
    	sc.setOuterJoin(2);
    	qs.appendWhere(sc, new int[] { ii, jj });
    	QueryResult qr = PersistenceHelper.manager.find(qs);
    	while(qr.hasMoreElements()){
    		Object[] o = (Object[])qr.nextElement();
    		result.add(o);
    	}
    	}catch(Exception e){
    		e.printStackTrace();
    		throw new WTException(e);
    	}
    	return result;
    }
    
    @Override
    public ArrayList<Object> getProjectUserList(EProjectNode project)throws Exception{
    
    	ArrayList<Object> result = new ArrayList<Object>();
    	try{
    	QuerySpec qs = new QuerySpec();
    	int ii = qs.addClassList(ProjectRole.class, true);
    	int jj = qs.addClassList(RoleUserLink.class,true);
    	
    	qs.appendWhere(new SearchCondition(ProjectRole.class,"projectReference.key.id","=",project.getPersistInfo().getObjectIdentifier().getId()),new int[]{ii});
    	qs.appendAnd();
    	SearchCondition sc = new SearchCondition(new ClassAttribute(ProjectRole.class,
        "thePersistInfo.theObjectIdentifier.id"), "=", new ClassAttribute(RoleUserLink.class,
        "roleBObjectRef.key.id"));
    	sc.setFromIndicies(new int[] { ii, jj }, 0);
    	sc.setOuterJoin(2);
    	qs.appendWhere(sc, new int[] { ii, jj });
    	QueryResult qr = PersistenceHelper.manager.find(qs);
    	while(qr.hasMoreElements()){
    		Object[] o = (Object[])qr.nextElement();
    		result.add(o);
    	}
    	}catch(Exception e){
    		e.printStackTrace();
    		throw new WTException(e);
    	}
    	return result;
    }
    
    @Override
    public boolean isOwner(ScheduleNode task, WTUser user)throws WTException{
    	
    	
			QuerySpec qs = new QuerySpec();
			int ii = qs.addClassList(ProjectRole.class, false);
			int jj = qs.addClassList(RoleUserLink.class, true);
			int kk = qs.addClassList(TaskRoleLink.class, false);
			qs.appendWhere(new SearchCondition(TaskRoleLink.class,"roleBObjectRef.key.id", "=", task.getPersistInfo().getObjectIdentifier().getId()), new int[] { kk });
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(ProjectRole.class,"thePersistInfo.theObjectIdentifier.id",TaskRoleLink.class, "roleAObjectRef.key.id"), new int[] {ii, kk });
			qs.appendAnd();
			SearchCondition sc = new SearchCondition(ProjectRole.class,	"thePersistInfo.theObjectIdentifier.id",RoleUserLink.class, "roleBObjectRef.key.id");
			qs.appendWhere(sc, new int[] { ii, jj });
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(RoleUserLink.class,"roleAObjectRef.key.id", "=", user.getPersistInfo().getObjectIdentifier().getId()), new int[] { jj });

			QueryResult qr = PersistenceHelper.manager.find(qs);

			boolean result = false;
			
			if (qr.size() > 0) {
				result = true;
			}

    	return result;
    }
    
    
    /**Owner Role 가져오기
     * @param task
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<Object> getOwner(ETaskNode task)throws Exception{
    	QuerySpec qs = new QuerySpec();
    	int ii = qs.addClassList(ProjectRole.class, true);
    	int jj = qs.addClassList(RoleUserLink.class,true);
    	int kk = qs.addClassList(TaskRoleLink.class,false);
    	qs.appendWhere(new SearchCondition(TaskRoleLink.class,"roleBObjectRef.key.id","=",task.getPersistInfo().getObjectIdentifier().getId()),new int[]{kk});
    	qs.appendAnd();
    	qs.appendWhere(new SearchCondition(ProjectRole.class,"thePersistInfo.theObjectIdentifier.id",TaskRoleLink.class,"roleAObjectRef.key.id"),new int[]{ii,kk});
    	qs.appendAnd();
    	SearchCondition sc = new SearchCondition(new ClassAttribute(ProjectRole.class,
        "thePersistInfo.theObjectIdentifier.id"), "=", new ClassAttribute(RoleUserLink.class,
        "roleBObjectRef.key.id"));
    	sc.setFromIndicies(new int[] { ii, jj }, 0);
    	sc.setOuterJoin(2);
    	qs.appendWhere(sc, new int[] { ii, jj });
    	
    	ArrayList<Object> result = new ArrayList<Object>();
    	result.add(getPM(task));
    	
    	QueryResult qr = PersistenceHelper.manager.find(qs);
    	while(qr.hasMoreElements()){
    		Object[] o = (Object[])qr.nextElement();
    		result.add(o);
    	}
    	
    	return result;
    }
    
    @Override
    public Object[] getPM(ScheduleNode node)throws Exception{
    	
    	if(node instanceof ETaskNode){
    		node = ((ETaskNode)node).getProject();
    	}
    	
    	try{
    	
    	QuerySpec qs = new QuerySpec();
    	int ii = qs.addClassList(ProjectRole.class, true);
    	int jj = qs.addClassList(RoleUserLink.class,true);
 
    	qs.appendWhere(new SearchCondition(ProjectRole.class,"projectReference.key.id","=",node.getPersistInfo().getObjectIdentifier().getId()),new int[]{ii});
    	qs.appendAnd();
    	qs.appendWhere(new SearchCondition(ProjectRole.class,"code","=",PROJECTKEY.PM),new int[]{ii});
    	qs.appendAnd();
    	SearchCondition sc = new SearchCondition(new ClassAttribute(ProjectRole.class,
        "thePersistInfo.theObjectIdentifier.id"), "=", new ClassAttribute(RoleUserLink.class,
        "roleBObjectRef.key.id"));
    	sc.setFromIndicies(new int[] { ii, jj }, 0);
    	sc.setOuterJoin(2);
    	qs.appendWhere(sc, new int[] { ii, jj });
    	
    	QueryResult qr = PersistenceHelper.manager.find(qs);
    	if(qr.hasMoreElements()){
    		return (Object[])qr.nextElement();
    	}
    	}catch(Exception e){
    		e.printStackTrace();
    		throw new WTException(e);
    	}
    	return null;
    }
}

