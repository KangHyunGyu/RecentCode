/**
 * 
 */
package com.e3ps.portal.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.e3ps.approval.ApprovalLine;
import com.e3ps.approval.ApprovalMaster;
import com.e3ps.approval.ApprovalObjectLink;
import com.e3ps.approval.bean.ApprovalListData;
import com.e3ps.approval.service.ApprovalHelper;
import com.e3ps.approval.util.ApprovalUtil;
import com.e3ps.change.EChangeActivity;
import com.e3ps.change.beans.ECAData;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.AccessControlUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.org.People;
import com.e3ps.project.EProject;
import com.e3ps.project.ETask;
import com.e3ps.project.ProjectRole;
import com.e3ps.project.RoleUserLink;
import com.e3ps.project.TaskRoleLink;
import com.e3ps.project.beans.IssueData;
import com.e3ps.project.beans.ProjectQuery;
import com.e3ps.project.beans.ProjectTaskData;
import com.e3ps.project.issue.IssueRequest;
import com.e3ps.project.key.ProjectKey.STATEKEY;
import com.e3ps.workspace.bean.NoticeData;
import com.e3ps.workspace.notice.Notice;

import wt.fc.PagingSessionHelper;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.org.WTPrincipal;
import wt.org.WTUser;
import wt.pds.StatementSpec;
import wt.query.ArrayExpression;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.session.SessionHelper;

public class PortalHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PORTAL.getName());
	public static final PortalHelper manager = new PortalHelper();

	/**
	 * @desc	: 공지사항 목록 (4개)
	 * @author	: mnyu
	 * @date	: 2019. 12. 9.
	 * @method	: getNoticeList
	 * @return	: void
	 * @throws Exception 
	 */
	public List<NoticeData> getNoticeList() throws Exception{
		List<NoticeData> list = new ArrayList<NoticeData>();
		QuerySpec qs = new QuerySpec();
		
		int idx = qs.addClassList(Notice.class, true);
		qs.appendOrderBy(new OrderBy(new ClassAttribute(Notice.class, "thePersistInfo.createStamp"), true), new int[] { idx });
		
		QueryResult qr = PagingSessionHelper.openPagingSession(0, 4, qs);
		while (qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			NoticeData data = new NoticeData((Notice)obj[0]);
			list.add(data);
		}
		return list;
	}

	/**
	 * @desc	: 결재 목록 (5개)
	 * @author	: mnyu
	 * @date	: 2019. 12. 10.
	 * @method	: getApprovalList
	 * @return	: List<ApprovalListData>
	 * @return
	 */
	public List<ApprovalListData> getApprovalList() throws Exception{
		List<ApprovalListData> list = new ArrayList<ApprovalListData>();
		WTUser user = (WTUser)SessionHelper.manager.getPrincipal();
		long userOid = CommonUtil.getOIDLongValue(user);
		QuerySpec qs = new QuerySpec();
		
		int idx0 = qs.addClassList(ApprovalMaster.class, false);
		int idx1 = qs.addClassList(ApprovalObjectLink.class, true);
		int idx2 = qs.addClassList(ApprovalLine.class, true);
		
		ApprovalHelper.manager.getApprovalJoinQuery(qs);
		
		//로그인 유저 및 최신 결재 라인
		ApprovalHelper.manager.getOwnerApprovalLine(qs, idx2,true);
		 
		//마스터 상태 (합의중,승인중,완료됨)
		qs.appendAnd(); 
		qs.appendOpenParen();
	 	SearchCondition sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.STATE,SearchCondition.EQUAL,ApprovalUtil.STATE_MASTER_DISCUSSING);
	 	qs.appendWhere(sc, new int[] { idx0 });
	 
	 	qs.appendOr();
	 	sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.STATE,SearchCondition.EQUAL,ApprovalUtil.STATE_MASTER_APPROVING);
	 	qs.appendWhere(sc, new int[] { idx0 });
	 
	 	qs.appendOr();
	 	sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.STATE,SearchCondition.EQUAL,ApprovalUtil.STATE_MASTER_COMPLETED);
	 	qs.appendWhere(sc, new int[] { idx0 });
	 	qs.appendCloseParen();
	 	
	 	
  		
	 	//설계변경활동 제외
	 	qs.appendAnd(); 
	 	sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.OBJECT_TYPE,"<>",ApprovalUtil.OBJECT_TYPE_ECA);
	 	qs.appendWhere(sc, new int[] { idx0 });
	 	
	 	//결재자
		qs.appendAnd();
		sc = new SearchCondition(ApprovalLine.class, ApprovalLine.OWNER + "." + "key.id", SearchCondition.EQUAL, userOid);
		qs.appendWhere(sc, new int[] { idx2 });
	 			
	 	//결재라인 상태
	 	qs.appendAnd(); 
	 	sc = new SearchCondition(ApprovalLine.class, ApprovalLine.STATE,SearchCondition.EQUAL,"ONGOING");
	 	qs.appendWhere(sc, new int[] { idx2 });
	 	
	 	qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class, "thePersistInfo.createStamp"), true), new int[] { idx2 });
	 	
	 	QueryResult qr = PagingSessionHelper.openPagingSession(0, 4, qs);
	 	while (qr.hasMoreElements()) {
	 		Object[] obj = (Object[]) qr.nextElement();
	 		 ApprovalObjectLink link = (ApprovalObjectLink)obj[0];
			 ApprovalLine line = (ApprovalLine)obj[1];
			 String state = ((ApprovalMaster)link.getRoleBObject()).getState().toString();
			 ApprovalListData data = ApprovalUtil.setApprovalObject(line, link, state);
			 if(data!=null) list.add(data);
	 	}
	 	
	 	return list;
	}
	
	/**
	 * @Desc   : 나의 프로젝트 지연 태스크 목록 5개
	 * @author : shkim
	 * @method : getDelayTaskList
	 * @return : List<ProjectTaskData>
	 * @throws : Exception
	 */
	public List<ProjectTaskData> getDelayTaskList() throws Exception {
		List<ProjectTaskData> list = new ArrayList<>();
		
		WTUser user = (WTUser) SessionHelper.manager.getPrincipal();
		
		QuerySpec qs = new QuerySpec();
		
		int taskIdx = qs.addClassList(ETask.class, true);
		int pjtIdx = qs.addClassList(EProject.class, false);
		int taskRoleLinkIdx = qs.addClassList(TaskRoleLink.class, false);
		int roleIdx = qs.addClassList(ProjectRole.class, false);
		int roleUserLinkIdx = qs.addClassList(RoleUserLink.class, false);
		
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
		sc = new SearchCondition(ETask.class, ETask.STATUS, SearchCondition.EQUAL, STATEKEY.PROGRESS);
		qs.appendWhere(sc, new int[] {taskIdx});
		
		qs.appendAnd();
		sc = new SearchCondition(ETask.class, ETask.PLAN_END_DATE, SearchCondition.LESS_THAN, DateUtil.convertStartDate(DateUtil.getToDay()));
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
		sc = new SearchCondition(RoleUserLink.class, RoleUserLink.ROLE_AOBJECT_REF + ".key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(user));
		qs.appendWhere(sc, new int[] {roleUserLinkIdx});
		
		qs.appendOrderBy(new OrderBy(new ClassAttribute(ETask.class, ETask.PLAN_END_DATE), false), new int[] {taskIdx});
		
		QueryResult qr = PagingSessionHelper.openPagingSession(0, 5, qs);
		
		while(qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			ETask task = (ETask) obj[0];
			ProjectTaskData data = new ProjectTaskData(task);
			
			data.setDelayDate(DateUtil.getDaysDiff(DateUtil.getToDay(), DateUtil.getDateString(task.getPlanEndDate(), "d")));
			
			list.add(data);
		}
		
		return list;
	}
	
	/**
	 * @Desc   : 이슈 목록 5개
	 * @author : shkim
	 * @method : getDelayTaskList
	 * @return : List<ProjectTaskData>
	 * @throws : Exception
	 */
	public List<IssueData> getIssueList() throws Exception {
		List<IssueData> list = new ArrayList<>();
		
		QuerySpec qs = ProjectQuery.manager.getMyIssue(new HashMap<>());
		
		QueryResult qr = PagingSessionHelper.openPagingSession(0, 5, qs);
		while(qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			IssueRequest ir = (IssueRequest) obj[0];
			IssueData data = new IssueData(ir);
			
			list.add(data);
		}
		
		return list;
	}
	public List<ECAData> getECAList() throws Exception{
		WTUser user = (WTUser)SessionHelper.manager.getPrincipal();
		long userOid = CommonUtil.getOIDLongValue(user);
		List<ECAData> list = new ArrayList<ECAData>();
		Timestamp toDate = DateUtil.convertDate(DateUtil.getToDay());
		QuerySpec qs = new QuerySpec();
		
		int idx = qs.addClassList(EChangeActivity.class, true);
		SearchCondition sc = new SearchCondition(EChangeActivity.class, EChangeActivity.ACTIVE_STATE,SearchCondition.EQUAL,"작업중");
	 	qs.appendWhere(sc, new int[] { idx });
	 	
	 	qs.appendAnd();
	 	sc = new SearchCondition(EChangeActivity.class, EChangeActivity.FINISH_DATE,SearchCondition.LESS_THAN,toDate);
	 	qs.appendWhere(sc, new int[] { idx });
	 	
	 	//결재자
		qs.appendAnd();
		sc = new SearchCondition(EChangeActivity.class, EChangeActivity.OWNER + "." + "key.id", SearchCondition.EQUAL, userOid);
		qs.appendWhere(sc, new int[] { idx });
	 			
		qs.appendOrderBy(new OrderBy(new ClassAttribute(EChangeActivity.class, "thePersistInfo.createStamp"), true), new int[] { idx });
//		System.out.println("MainECA // qs ::: " + qs);
		QueryResult qr = PagingSessionHelper.openPagingSession(0, 4, qs);
		while (qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			EChangeActivity eca = (EChangeActivity) obj[0];
			ECAData data = new ECAData(eca);
			data.setDelay(DateUtil.getDaysDiff(DateUtil.getToDay(), DateUtil.getDateString(eca.getFinishDate(), "d")));
			list.add(data);
		}
		return list;
	}
}
