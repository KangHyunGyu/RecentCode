package com.e3ps.common.workflow;

import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import wt.clients.vc.CheckInOutTaskLogic;
import wt.fc.EvolvableHelper;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceServerHelper;
import wt.fc.QueryResult;
import wt.fc.WTObject;
import wt.lifecycle.LifeCycleException;
import wt.lifecycle.LifeCycleHelper;
import wt.lifecycle.LifeCycleManaged;
import wt.lifecycle.State;
import wt.org.OrganizationServicesMgr;
import wt.org.WTPrincipal;
import wt.org.WTPrincipalReference;
import wt.org.WTUser;
import wt.ownership.OwnershipHelper;
import wt.ownership.OwnershipServerHelper;
import wt.pds.PDSObjectInput;
import wt.pom.Transaction;
import wt.project.Role;
import wt.query.ClassAttribute;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.team.Team;
import wt.team.TeamException;
import wt.team.TeamHelper;
import wt.team.TeamManaged;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;
import wt.vc.wip.Workable;
import wt.workflow.engine.WfActivity;
import wt.workflow.engine.WfProcess;
import wt.workflow.work.WfAssignedActivity;
import wt.workflow.work.WfAssignee;
import wt.workflow.work.WfAssignment;
import wt.workflow.work.WfAssignmentState;
import wt.workflow.work.WfPrincipalAssignee;
import wt.workflow.work.WorkItem;
import wt.workflow.work.WorkflowServerHelper;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.query.SearchUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.groupware.service.WFItemHelper;
import com.e3ps.groupware.workprocess.WFItem;
import com.e3ps.groupware.workprocess.WFItemUserLink;
import com.e3ps.org.People;
import com.e3ps.org.bean.PeopleData;

public class E3PSWorkflowHelper
  implements Externalizable
{
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
  private static final String RESOURCE = "e3ps.common.workflow.workflowResource";
  private static final String CLASSNAME = E3PSWorkflowHelper.class.getName();
  static final long serialVersionUID = 1L;
  public static final long EXTERNALIZATION_VERSION_UID = 957977401221134810L;
  protected static final long OLD_FORMAT_VERSION_UID = 6396745458964155413L;
  public static E3PSWorkflowHelper manager = new E3PSWorkflowHelper();

  public void writeExternal(ObjectOutput output)
    throws IOException
  {
    output.writeLong(957977401221134810L);
  }

  public void readExternal(ObjectInput input)
    throws IOException, ClassNotFoundException
  {
    long readSerialVersionUID = input.readLong();
    readVersion(this, input, readSerialVersionUID, false, false);
  }

  protected boolean readVersion(E3PSWorkflowHelper thisObject, ObjectInput input, long readSerialVersionUID, boolean passThrough, boolean superDone)
    throws IOException, ClassNotFoundException
  {
    boolean success = true;

    if (readSerialVersionUID != 957977401221134810L)
    {
      success = readOldVersion(input, readSerialVersionUID, passThrough, superDone);
      if ((input instanceof PDSObjectInput)) {
        EvolvableHelper.requestRewriteOfEvolvedBlobbedObject();
      }
    }
    return success;
  }

  private boolean readOldVersion(ObjectInput input, long readSerialVersionUID, boolean passThrough, boolean superDone)
    throws IOException, ClassNotFoundException
  {
    boolean success = true;

    if (readSerialVersionUID != 6396745458964155413L)
    {
      throw new InvalidClassException(CLASSNAME, "Local class not compatible: stream classdesc externalizationVersionUID=" + 
        readSerialVersionUID + 
        " local class externalizationVersionUID=" + 957977401221134810L);
    }
    return success;
  }
 
  public static Persistable getPBO(WorkItem workItem)
  {
    Persistable pbo = null;
    try
    {
      pbo = workItem.getPrimaryBusinessObject().getObject();
    }
    catch (Exception e)
    {
      LOGGER.info("사용가능하지 않은 WorkItem = " + workItem);
    }
    return pbo;
  }
  
  public void changeLCState(LifeCycleManaged lcm, String state)
  {
    if ((lcm == null) || (state == null)) return;
    State newState = State.toState(state);
    try
    {
      if (((lcm instanceof Workable)) && (CheckInOutTaskLogic.isCheckedOut((Workable)lcm))) return;
      if (newState.equals(lcm.getLifeCycleState())) return;

      LifeCycleHelper.service.setLifeCycleState(lcm, newState, false);
    }
    catch (LifeCycleException e)
    {
      e.printStackTrace();
    }
    catch (WTException e)
    {
      e.printStackTrace();
    }
  }

  public void addParticipant(LifeCycleManaged lcm, String roleName, String id)
  {
    if ((lcm instanceof TeamManaged))
    {
      try
      {
        Team team = TeamHelper.service.getTeam(lcm);
        Role role = Role.toRole(roleName);

        team.addPrincipal(role, id == null ? null : OrganizationServicesMgr.getPrincipal(id));
      }
      catch (TeamException e)
      {
        e.printStackTrace();
      }
      catch (WTException e)
      {
        e.printStackTrace();
      }
    }
  }

  public void deleteAllRoles(LifeCycleManaged lcm)
  {
    if ((lcm instanceof TeamManaged))
    {
      try
      {
        Team team = TeamHelper.service.getTeam(lcm);
        Vector vecRole = team.getRoles();
        for (int i = vecRole.size() - 1; i > -1; i--)
        {
          team.deleteRole((Role)vecRole.get(i));
        }
      }
      catch (TeamException e)
      {
        e.printStackTrace();
      }
      catch (WTException e)
      {
        e.printStackTrace();
      }
    }
  }

  /**
   * @param obj
   * @param roleName
   * @param id
   */
  public void deleteParticipant(LifeCycleManaged obj, String roleName, String id)
  {
      
      if (obj instanceof TeamManaged)
      {
          try
          {
              Team team = TeamHelper.service.getTeam((TeamManaged) obj);
              Role role = Role.toRole(roleName);

              Vector vecRole = team.getRoles();
              for (int i = vecRole.size() - 1; i > -1; i--)
              {
                  if (role.equals((Role) vecRole.get(i)))
                  {
                      team.deletePrincipalTarget(role, OrganizationServicesMgr.getPrincipal(id));
                      break;
                  }
              }
          }
          catch (TeamException e)
          {
              e.printStackTrace();
          }
          catch (WTException e)
          {
              e.printStackTrace();
          }
      }
  }
  
  public void deleteRole(LifeCycleManaged lcm, String roleName)
  {
    if ((lcm instanceof TeamManaged))
    {
      try
      {
        Team team = TeamHelper.service.getTeam(lcm);
        Role role = Role.toRole(roleName);

        Vector vecRole = team.getRoles();
        for (int i = vecRole.size() - 1; i > -1; i--)
        {
          if (!role.equals((Role)vecRole.get(i)))
            continue;
          team.deleteRole((Role)vecRole.get(i));
          break;
        }

      }
      catch (TeamException e)
      {
        e.printStackTrace();
      }
      catch (WTException e)
      {
        e.printStackTrace();
      }
    }
  }

  public boolean includeRole(LifeCycleManaged lcm, String roleName)
  {
    boolean flag = false;
    if ((lcm instanceof TeamManaged))
    {
      try
      {
        Team team = TeamHelper.service.getTeam(lcm);
        Role role = Role.toRole(roleName);

        Vector vecRole = team.getRoles();
        for (int i = vecRole.size() - 1; i > -1; i--)
        {
          if (!role.equals((Role)vecRole.get(i)))
            continue;
          Enumeration enumer = team.getPrincipalTarget(role);
          if (!enumer.hasMoreElements())
            continue;
          flag = true;
          break;
        }

      }
      catch (TeamException e)
      {
        e.printStackTrace();
      }
      catch (WTException e)
      {
        e.printStackTrace();
      }
    }
    return flag;
  }

  public Enumeration getRoleMember(LifeCycleManaged lcm, String roleName)
  {
    if ((lcm instanceof TeamManaged))
    {
      try
      {
        Team team = TeamHelper.service.getTeam(lcm);
        Role role = Role.toRole(roleName);

        Vector vecRole = team.getRoles();
        for (int i = vecRole.size() - 1; i > -1; i--)
        {
          if (role.equals((Role)vecRole.get(i)))
            return team.getPrincipalTarget(role);
        }
      }
      catch (TeamException e)
      {
        e.printStackTrace();
      }
      catch (WTException e)
      {
        e.printStackTrace();
      }
    }
    return null;
  }

  public ArrayList getRoleName(LifeCycleManaged _lcm, WTUser _user)
  {
    ArrayList list = new ArrayList();
    String id = _user.getName();
    if ((_lcm instanceof TeamManaged))
    {
      try
      {
        Team team = TeamHelper.service.getTeam(_lcm);

        Vector vec_role = team.getRoles();
        for (int i = vec_role.size() - 1; i > -1; i--)
        {
          Enumeration enm = team.getPrincipalTarget((Role)vec_role.get(i));
          while (enm.hasMoreElements())
          {
            WTPrincipalReference ref = (WTPrincipalReference)enm.nextElement();
            if (!id.equals(ref.getPrincipal().getName()))
              continue;
            list.add(vec_role.get(i));
          }
        }

      }
      catch (TeamException e)
      {
        e.printStackTrace();
      }
      catch (WTException e)
      {
        e.printStackTrace();
      }
    }
    return list;
  }

  public void deleteWfProcess(LifeCycleManaged _lcm)
  {
    try
    {
      DeleteWorkflow.delete(_lcm);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public QueryResult getWfProcess(LifeCycleManaged lcm)
  {
    try
    {
      QuerySpec query = new QuerySpec(WfProcess.class);
      SearchUtil.appendEQUAL(query, WfProcess.class, "businessObjReference", CommonUtil.getFullOIDString(lcm), 0);
      return PersistenceHelper.manager.find(query);
    }
    catch (WTException e)
    {
      e.printStackTrace();
    }
    return null;
  }

  public boolean equalLCState(LifeCycleManaged obj, String state)
  {
    if ((obj instanceof LifeCycleManaged))
    {
      LifeCycleManaged lcm = obj;
      State newState = State.toState(state);

      return newState.equals(lcm.getLifeCycleState());
    }
    return false;
  }

  public QueryResult getActivity(LifeCycleManaged lcm, String state)
  {
    try
    {
      Class Process = WfProcess.class;
      Class Activity = WfAssignedActivity.class;
      QuerySpec query = new QuerySpec();
      int idx1 = query.addClassList(Process, false);
      int idx2 = query.addClassList(Activity, true);

      SearchCondition s1 = new SearchCondition(
        new ClassAttribute(Process, 
        "thePersistInfo.theObjectIdentifier.id"), "=", 
        new ClassAttribute(Activity, 
        "parentProcessRef.key.id"));
      query.appendWhere(s1, new int[] { idx1, idx2 });

      SearchUtil.appendEQUAL(query, Process, "state", "OPEN_RUNNING", idx1);
      SearchUtil.appendEQUAL(query, Process, "businessObjReference", CommonUtil.getFullOIDString(lcm), idx1);
      SearchUtil.appendEQUAL(query, Activity, "state", state, idx2);

      return PersistenceHelper.manager.find(query);
    }
    catch (QueryException e)
    {
      e.printStackTrace();
    }
    catch (WTException e)
    {
      e.printStackTrace();
    }

    return null;
  }
  
  /**
   * 위임시 결재자 정보 수정 및 link 객체로 이력 남기기
   * @param req
   * @param res
 * @throws Exception 
   */
  public void reassign(Hashtable hash) throws Exception
  {
      String oid = (String) hash.get("oid");
      WorkItem workitem = (WorkItem) CommonUtil.getObject(oid);
      String userOid = (String) hash.get("newUser");
      People people = (People) CommonUtil.getObject(userOid);
      WTUser newUser = people.getUser();
      try
      {
    	  
          // WFItemUserLink 재설정
    	  WTUser oldUser = (WTUser)workitem.getOwnership().getOwner().getPrincipal();
    	  reassignWorkItem(workitem, newUser);
          WTUser currentUser = (WTUser)workitem.getOwnership().getOwner().getPrincipal();
          WTObject wtobject = (WTObject)getPBO(workitem);
          
          // Role에 할당된 사용자도 수정
          String assignRoleName = (String) hash.get("assignrolename");
          LOGGER.info("assignrolename = "+assignRoleName);
          LOGGER.info("wtobject = "+wtobject);
          if(assignRoleName != null)
          {
              E3PSWorkflowHelper.manager.deleteParticipant((LifeCycleManaged)wtobject, assignRoleName, currentUser.getName());
              E3PSWorkflowHelper.manager.addParticipant((LifeCycleManaged)wtobject, assignRoleName, newUser.getName());
              
              /*ECA 담당자 변경*/ 
              /*if(wtobject instanceof EChangeActivity){
            	  EChangeActivity eca =(EChangeActivity)wtobject;
            	  eca.setActiveUser(newUser);
            	  PersistenceHelper.manager.modify(eca);
              }*/
          }
          
          WFItem wfitem = WFItemHelper.service.getWFItem(wtobject);
          if(wfitem != null)
          {
              
        	  WfActivity activity = (WfActivity)workitem.getSource().getObject();
        	  String activityName = WFItemHelper.service.getWFItemActivityName(activity.getName());
        	  WFItemUserLink link =WFItemHelper.service.getOwnerApplineLink(oldUser, wfitem, activityName);
        	  
        	  if(link != null){
        		  //오늘날짜        		  
            	  //Timestamp today = DateUtil.getTimestampFormat(DateUtil.getToDay("yyyy-MM-dd hh:mm:ss"), "yyyy-MM-dd hh:mm:ss");            	 
            	  //Timestamp today = new java.sql.Timestamp(new java.util.Date().getTime());
            	  //newlink.setProcessDate(new java.sql.Timestamp(new java.util.Date().getTime())); 
            	  
            	  //기존객체 수정
            	  link.setState("위임");
            	  String comment = "'" + oldUser.getFullName() + "'로 부터 '" + newUser.getFullName() + "'으로 업무 위임";
            	  link.setComment(comment);
            	  link.setProcessDate(new java.sql.Timestamp(new java.util.Date().getTime()));
            	  int order = link.getProcessOrder();
            	  PersistenceHelper.manager.save(link);
            	  int seq = link.getSeqNo();
            	  
            	  //새로운 객체 생성.
                  WFItemUserLink newlink = WFItemUserLink.newWFItemUserLink(newUser, wfitem);
                  PeopleData pData = new PeopleData(newUser);
                  newlink.setDepartmentName(pData.getDepartmentName());
                  newlink.setActivityName(link.getActivityName());
                  newlink.setProcessOrder(order);
                  newlink.setDisabled(false);
                  newlink.setSeqNo(seq);
                  
                  PersistenceHelper.manager.save(newlink);
        	  }
             
          }
      }
      catch (WTException e)
      {
          e.printStackTrace();
      }
      catch (WTPropertyVetoException e)
      {
          e.printStackTrace();
      }
    }
  
	  /**위임시 workitem 정보 수정
	 * @param workitem
	 * @param newOwner
	 * @throws WTException
	 */
	public void reassignWorkItem( WorkItem workitem, WTPrincipal newOwner ) throws WTException {
	
		 Transaction trx = new Transaction();
		 trx.start();
		 WTObject wtobject = (WTObject) workitem.getPrimaryBusinessObject().getObject();
		
		 if (wtobject != null)
		 {
		     try
		     {
		         WfAssignment assignment = (WfAssignment) workitem.getParentWA().getObject();
		
		         WfAssignee assignee = assignment.getAssignee();
		         WTPrincipal oldOwner = OwnershipHelper.getOwner(workitem);
		         WTPrincipalReference oldOwnerReference = WTPrincipalReference.newWTPrincipalReference(oldOwner);
		         WTPrincipalReference newOwnerReference = WTPrincipalReference.newWTPrincipalReference(newOwner);
		
		         WorkflowServerHelper.service.revokeTaskBasedRights(workitem);
		         OwnershipServerHelper.service.changeOwner(workitem, newOwner, false);
		         workitem.setStatus(WfAssignmentState.POTENTIAL);
		         WorkflowServerHelper.service.setTaskBasedRights(workitem, newOwnerReference);
		         PersistenceServerHelper.manager.update(workitem);
		
		         Vector principals = assignment.getPrincipals();
		         principals.removeElement(oldOwnerReference);
		         principals.addElement(newOwnerReference);
		         assignment.setPrincipals(principals);
		
		         if (assignee instanceof WfPrincipalAssignee)
		         {
		             WTPrincipalReference principalReference = ((WfPrincipalAssignee) assignee).getPrincipal();
		             if (principalReference.getObject() instanceof WTUser)
		                 ((WfPrincipalAssignee) assignee).setPrincipal(newOwnerReference);
		         }
		
		         PersistenceServerHelper.manager.update(assignment);
		         trx.commit();
		         
		     }
		     catch (WTException e)
		     {
		         trx.rollback();
		         e.printStackTrace();
		     }
		     catch (wt.util.WTPropertyVetoException e)
		     {
		         trx.rollback();
		         e.printStackTrace();
		     }
	 	}
	//##end reassignWorkItem%412322FD0283.body
	}
}