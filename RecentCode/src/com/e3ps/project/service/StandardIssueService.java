package com.e3ps.project.service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.e3ps.approval.ApprovalLine;
import com.e3ps.approval.ApprovalMaster;
import com.e3ps.approval.ApprovalObjectLink;
import com.e3ps.approval.ApproveRoleType;
import com.e3ps.approval.ApproveStateType;
import com.e3ps.approval.service.ApprovalHelper;
import com.e3ps.approval.util.ApprovalMailForm;
import com.e3ps.approval.util.ApprovalUtil;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.content.service.CommonContentHelper;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.JExcelUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.common.web.ParamUtil;
import com.e3ps.org.People;
import com.e3ps.org.bean.PeopleData;
import com.e3ps.org.service.UserHelper;
import com.e3ps.project.ControllerException;
import com.e3ps.project.EProject;
import com.e3ps.project.EProjectMasteredLink;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.beans.IssueData;
import com.e3ps.project.beans.ProjectData;
import com.e3ps.project.beans.ProjectQuery;
import com.e3ps.project.issue.IssueDocLink;
import com.e3ps.project.issue.IssueRequest;
import com.e3ps.project.issue.IssueSolution;
import com.e3ps.project.key.ProjectKey.IssueKey;
import com.e3ps.project.key.ProjectKey.MESSAGEKEY;
import com.e3ps.project.key.ProjectKey.NUMBERCODEKEY;
import com.e3ps.project.util.IssueMailForm;
import com.e3ps.queue.E3PSQueueHelper;

import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import wt.content.ContentHolder;
import wt.doc.WTDocument;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceServerHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.fc.WTObject;
import wt.fc.collections.WTArrayList;
import wt.fc.collections.WTCollection;
import wt.method.RemoteAccess;
import wt.org.WTPrincipal;
import wt.org.WTUser;
import wt.ownership.Ownership;
import wt.pom.Transaction;
import wt.query.QuerySpec;
import wt.services.StandardManager;
import wt.session.SessionHelper;
import wt.util.WTException;

public class StandardIssueService extends StandardManager implements RemoteAccess, Serializable, IssueService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PROJECT.getName());
	private static final long serialVersionUID = 1L;

	public static StandardIssueService newStandardIssueService() throws WTException {
		StandardIssueService instance = new StandardIssueService();
		instance.initialize();
		return instance;
	}

	@Override
	public HashMap<String, Object> saveIssue(Map<String, Object> hash) throws Exception {

		Transaction trx = new Transaction();
		HashMap<String, Object> returnMap = new HashMap<String, Object>();

		try {
			trx.start();

			IssueRequest issue = IssueRequest.newIssueRequest();

			String name = ParamUtil.get(hash, "name");
			String issueType = ParamUtil.get(hash, "issueType");
			String manager = ParamUtil.get(hash, "manager");
			String problem = ParamUtil.get(hash, "problem");
			String taskOid = ParamUtil.get(hash, "taskOid");
			String deadLine = ParamUtil.get(hash, "deadLine");
			String tempmanager = ParamUtil.get(hash, "tempmanager");
			String primary = StringUtil.checkNull((String) hash.get("PRIMARY"));
			List<String> secondary = StringUtil.checkReplaceArray(hash.get("SECONDARY"));
			LOGGER.info(String.valueOf(secondary.size()));
			People people = (People) CommonUtil.getObject(manager);
			WTUser user = UserHelper.service.getWTUser(tempmanager);
			if (people != null) {
				user = people.getUser();
			}

			ReferenceFactory rf = new ReferenceFactory();
			ETaskNode tn = (ETaskNode) rf.getReference(taskOid).getObject();

			issue.setDeadLine(DateUtil.convertDate(deadLine));
			issue.setTask(tn);
			issue.setName(name);
			issue.setIssueType(issueType);
			issue.setManager(user);
			issue.setState(IssueKey.ISSUE_CHECK);
			issue.setCreator((WTUser) SessionHelper.manager.getPrincipal());
			issue.setProblem(problem);

			issue = (IssueRequest) PersistenceHelper.manager.save(issue);

			CommonContentHelper.service.attach((ContentHolder) issue, primary, secondary);

			/*
			 * 나의 업무에서 볼 수 있도록, 이슈 결재라인 추가 하기.
			 */
			createApprovalIssue(tn, issue);

			/*
			 * if (files != null) { for (WBFile file : files) { CommonContentHelper.service
			 * .attach(issue, file, "", false); } }
			 */

			// ProjectMailBroker broker = new ProjectMailBroker();
			// broker.createIssueMail(issue);

			Hashtable mailhash = new Hashtable();

			mailhash.put("cmd", "createIssue");
			mailhash.put("oid", issue.getPersistInfo().getObjectIdentifier().toString());
			mailhash.put("manager", CommonUtil.getOIDString(user));
			sendAssaignMail(issue);
			// MailUtil.projectMail(mailhash);
			System.out.println("============ createIssueComplate ============");
			trx.commit();
			trx = null;

			returnMap.put("obj", issue);
			returnMap.put("oid", CommonUtil.getOIDString(issue));
			returnMap.put("msg", MESSAGEKEY.CREATE);

		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("msg", e.getLocalizedMessage() + MESSAGEKEY.CREATE_ERROR);
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return returnMap;
	}

	public void createApprovalIssue(ETaskNode task, IssueRequest issue) throws Exception {

		Transaction trx = new Transaction();

		try {

			trx.start();

			LOGGER.info("============ createApprovalIssue ============");
			System.out.println("============ createApprovalIssue ============");
			String state = ApprovalUtil.STATE_LINE_ONGOING; // 결재 라인 진행중 -> 이슈에선 요청중일때
			String appState = ApprovalUtil.STATE_MASTER_DISCUSSING; // 마스터 상태 협의 중
			// Role 별 Array
			List<String> discussList = new ArrayList<String>();
			List<String> approveList = new ArrayList<String>();
			List<String> draftList = new ArrayList<String>();

			// 기안 리스트 생성
			WTUser dratUser = (WTUser) SessionHelper.manager.getPrincipal();
			PeopleData pData = new PeopleData(dratUser);
			draftList.add(pData.getOid());

			String draftState = ApprovalUtil.STATE_LINE_COMPLETE; // 기안
			String discussState = ApprovalUtil.STATE_LINE_ONGOING; // 협의
			String approveState = ApprovalUtil.STATE_LINE_STANDING; // 승인

			// 결재 라인 생성
			int seq = 0;
			int ver = 0;

			// 마스터 생성
			// master = createApprovalMaster(issue,appState);
			ApprovalMaster am = ApprovalMaster.newApprovalMaster();
			Map<String, String> map = ApprovalUtil.getApprovalObjectInfo(issue);
			String objectType = map.get("objectType");
			am.setState(ApproveStateType.toApproveStateType(appState));
			am.setOwner(SessionHelper.manager.getPrincipalReference());
			am.setActive(0);
			am.setObjectType(objectType);

			String title = map.get("title");
			am.setTitle(title);

			am = (ApprovalMaster) PersistenceHelper.manager.save(am);

			LOGGER.info("==========  ISSUE ApprovalMaster 생성");
			System.out.println("==========  ISSUE ApprovalMaster 생성");
			// 마스터와 결재 대상 링크 생성
			// createApprovalLink(issue, master);
			ApprovalObjectLink link = ApprovalObjectLink.newApprovalObjectLink(issue, am);
			PersistenceHelper.manager.save(link);
			LOGGER.info("==========  ISSUE ApprovalObjectLink 생성");
			System.out.println("==========  ISSUE ApprovalObjectLink 생성");

			// 기안 Role 생성
			seq = createApprovalLine(am, draftList, ApprovalUtil.ROLE_DRAFT, seq, draftState, ver);
			LOGGER.info("==========  master null + ApprovalUtil.ROLE_DRAFT 생성");
			System.out.println("==========  master null + ApprovalUtil.ROLE_DRAFT 생성");

			// 협의라인 합의
			WTUser discussUser = issue.getManager();
			PeopleData dUser = new PeopleData(discussUser);
			discussList.add(dUser.getOid());
			seq = createApprovalLine(am, discussList, ApprovalUtil.ROLE_DISCUSS, seq, discussState, ver);
			LOGGER.info("==========  master null + ApprovalUtil.ROLE_DISCUSS 생성");
			System.out.println("==========  master null + ApprovalUtil.ROLE_DISCUSS 생성");
			// 결재라인 승인
			WTUser approveUser = issue.getCreator();
			PeopleData aUser = new PeopleData(approveUser);
			approveList.add(aUser.getOid()); // 최초 이슈 등록자가 최종 결재를 맡는다.
			seq = createApprovalLine(am, approveList, ApprovalUtil.ROLE_APPROVE, seq, approveState, ver);
			LOGGER.info("==========  master null + ApprovalUtil.ROLE_APPROVE 생성");
			System.out.println("==========  master null + ApprovalUtil.ROLE_APPROVE 생성");

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

	private int createApprovalLine(ApprovalMaster master, List<String> appLine, String roleType, int seq, String state,
			int ver) throws Exception {

		LOGGER.info(roleType + "appLine = " + appLine.size());
		WTCollection collection = new WTArrayList();
		int i = 0;
		for (String poid : appLine) {
			LOGGER.info("poid = " + poid);
			Persistable userObject = CommonUtil.getObject(poid);
			;
			WTUser user = null;
			if (userObject instanceof People) {
				People pp = (People) CommonUtil.getObject(poid);
				user = pp.getUser();
			} else {
				user = (WTUser) userObject;
			}

			ApprovalLine line = ApprovalLine.newApprovalLine();
			Ownership ownership = Ownership.newOwnership(user);
			line.setOwner(ownership.getOwner());
			line.setRole(ApproveRoleType.toApproveRoleType(roleType));
			line.setSeq(seq);
			line.setState(ApproveStateType.toApproveStateType(state));
			line.setMaster(master);

			// 승인 Role 경우 직렬로 진행 하기 때문에 첫번째 승인 라인만 OnGoing 로 변경
			if (ApprovalUtil.STATE_LINE_ONGOING.equals(state)) {
				if (roleType.equals(ApprovalUtil.ROLE_APPROVE)) { // 첫번째
					if (i > 0) {
						line.setState(ApproveStateType.toApproveStateType(ApprovalUtil.STATE_LINE_STANDING));
					}
					line.setStartDate(new Timestamp(System.currentTimeMillis()));
				} else {
					line.setStartDate(new Timestamp(System.currentTimeMillis()));
				}
			}

			if (ApprovalUtil.STATE_LINE_COMPLETE.equals(state)) {
				line.setStartDate(new Timestamp(System.currentTimeMillis()));
				line.setApproveDate(new Timestamp(System.currentTimeMillis()));
			}

			line.setVer(ver);

			// PersistenceHelper.manager.save(var1)
			collection.add(line);
			if (!(roleType.equals(ApprovalUtil.ROLE_DISCUSS) || roleType.equals(ApprovalUtil.ROLE_RECEIVE))) {
				seq++;
			}

			i++;

		}

		if (appLine.size() > 0
				&& (roleType.equals(ApprovalUtil.ROLE_DISCUSS) || roleType.equals(ApprovalUtil.ROLE_RECEIVE))) {
			seq++;
		}
		PersistenceHelper.manager.save(collection);

		return seq;
	}

	@Override
	public HashMap<String, Object> updateIssue(Map<String, Object> hash) throws Exception {

		Transaction trx = new Transaction();
		HashMap<String, Object> returnMap = new HashMap<String, Object>();

		try {
			trx.start();

			IssueRequest issue = IssueRequest.newIssueRequest();

			String oid = ParamUtil.get(hash, "oid");

			String name = ParamUtil.get(hash, "name");
			String issueType = ParamUtil.get(hash, "issueType");
			String manager = ParamUtil.get(hash, "manager");
			String problem = ParamUtil.get(hash, "problem");

			String tempmanager = ParamUtil.get(hash, "tempmanager");
			List<String> secondary = StringUtil.checkReplaceArray(hash.get("SECONDARY"));
			List<String> delocIds = StringUtil.checkReplaceArray(hash.get("delocIds"));

			People people = (People) CommonUtil.getObject(manager);
			WTUser user = UserHelper.service.getWTUser(tempmanager);
			if (people != null) {
				user = people.getUser();
			}

			ReferenceFactory rf = new ReferenceFactory();
			issue = (IssueRequest) rf.getReference(oid).getObject();

			issue.setName(name);
			issue.setIssueType(issueType);
			issue.setManager(user);
			issue.setProblem(problem);

			issue = (IssueRequest) PersistenceHelper.manager.modify(issue);

			// Vector secondaryDelFile = (Vector)hash.get("secondaryDelFile");
			// QueryResult secondaryFiles = ContentHelper.service.getContentsByRole(issue,
			// ContentRoleType.SECONDARY);
			//
			// while(secondaryFiles.hasMoreElements()){
			// ContentItem item2 = (ContentItem) secondaryFiles.nextElement();
			// String itemId = item2.getPersistInfo().getObjectIdentifier().toString();
			//
			// if(secondaryDelFile==null || !secondaryDelFile.contains(itemId)){
			// PersistenceHelper.manager.delete(item2);
			// }
			// }

			/*
			 * Object secondaryDelFile = (Object) hash.get("secondaryDelFile");
			 * Vector<String> delFile = new Vector(); if (secondaryDelFile instanceof
			 * String) { delFile.add((String) secondaryDelFile); } else if (secondaryDelFile
			 * instanceof Vector) { delFile = (Vector) secondaryDelFile; }
			 */

			CommonContentHelper.service.attach((ContentHolder) issue, null, secondary, delocIds, false);

			trx.commit();
			trx = null;

			returnMap.put("obj", issue);
			returnMap.put("oid", CommonUtil.getOIDString(issue));
			returnMap.put("msg", MESSAGEKEY.UPDATE);

		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("msg", e.getLocalizedMessage() + MESSAGEKEY.UPDATE_ERROR);
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return returnMap;
	}

	@Override
	public String deleteIssue(Map<String, Object> hash) throws Exception {

		Transaction trx = new Transaction();
		String msg = MESSAGEKEY.DELETE;
		try {
			trx.start();

			String oid = ParamUtil.get(hash, "oid");

			ReferenceFactory rf = new ReferenceFactory();
			IssueRequest issue = (IssueRequest) rf.getReference(oid).getObject();
			IssueData data = new IssueData(issue);
			IssueSolution solution = data.Solution();
			if (solution != null)
				solution = (IssueSolution) PersistenceHelper.manager.delete(solution);

			PersistenceHelper.manager.delete(issue);

			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getLocalizedMessage() + MESSAGEKEY.DELETE_ERROR;
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return msg;
	}

	@Override
	public HashMap<String, Object> createSolution(Map<String, Object> hash) throws Exception {

		Transaction trx = new Transaction();
		HashMap<String, Object> returnMap = new HashMap<String, Object>();

		try {
			trx.start();

			IssueSolution solution = IssueSolution.newIssueSolution();

			String oid = ParamUtil.get(hash, "oid");
			String desc = ParamUtil.get(hash, "solution");

			String primary = StringUtil.checkNull((String) hash.get("PRIMARY"));
			List<String> secondary = StringUtil.checkReplaceArray(hash.get("SECONDARY"));

			WTUser user = (WTUser) SessionHelper.manager.getPrincipal();

			ReferenceFactory rf = new ReferenceFactory();
			IssueRequest issue = (IssueRequest) rf.getReference(oid).getObject();

			String requestDate = ParamUtil.get(hash, "requestDate");
			issue.setRequestDate(DateUtil.convertDate(requestDate));
			issue.setState(IssueKey.ISSUE_CHECK);
			issue = (IssueRequest) PersistenceHelper.manager.modify(issue);

			solution.setSolution(desc);
			solution.setRequest(issue);
			solution.setCreator(user);

			solution = (IssueSolution) PersistenceHelper.manager.save(solution);

			CommonContentHelper.service.attach((ContentHolder) solution, primary, secondary);
			// ProjectMailBroker broker = new ProjectMailBroker();
			// broker.createSolutionMail(solution);

			String[] docOids = (String[]) hash.get("docOids");
			WTDocument doc = null;
			IssueDocLink dlink = null;
			if (docOids != null) {
				for (int i = 0; i < docOids.length; i++) {
					doc = (WTDocument) rf.getReference(docOids[i]).getObject();
					dlink = IssueDocLink.newIssueDocLink(solution, doc);
					PersistenceServerHelper.manager.insert(dlink);
				}
			}

			// issueSolution 등록 후, 협의 라인 완료 결재 진행 ->
			nextIssueApprovalLine(issue);

			Hashtable mailhash = new Hashtable();
			mailhash.put("cmd", "saveSolution");
			mailhash.put("oid", issue.getPersistInfo().getObjectIdentifier().toString());
			mailhash.put("userName", issue.getCreator().getPersistInfo().getObjectIdentifier().toString());

			// MailUtil.projectMail(mailhash);
			sendSolutionMail(issue);
			returnMap.put("obj", solution);
			returnMap.put("oid", CommonUtil.getOIDString(issue));
			returnMap.put("msg", MESSAGEKEY.CREATE);

			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("msg", e.getLocalizedMessage() + MESSAGEKEY.CREATE_ERROR);
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return returnMap;
	}

	public void nextIssueApprovalLine(IssueRequest issue) throws Exception {
		
		QueryResult qr = PersistenceHelper.manager.navigate(issue, "appMaster", ApprovalObjectLink.class);

		while(qr.hasMoreElements()){
			ApprovalMaster master =(ApprovalMaster) qr.nextElement();
			String state = ApprovalUtil.STATE_LINE_ONGOING;
			//Master를 통해 Line 가져오기
			List<ApprovalLine> list = ApprovalHelper.manager.getApprovalRoleLine(master, "", "", false);
			
			for(ApprovalLine line : list){
				//진행 중인 라인 찾기.
				if(state.equals(line.getState().toString())){
					//다음 master 상태 구하면서 Line 상태 변경
					String masterState = nextApprovalStart(line);
					//master 상태 변경
					master = changeStateApprovalMaster(master, masterState);
					//System.out.println(" LINE + MASTER 상태 변경 완료 =========");
					break;
				}
			}
		}
		/*
		 * 마스터 상태 변경 master = changeStateApprovalMaster(master, masterState);
		 */
	}

	private String  nextApprovalStart(ApprovalLine line) throws Exception {
		
		String role = line.getRole().toString();
		ApprovalMaster master = line.getMaster();
		List<ApprovalLine> nextLineList =  new ArrayList<ApprovalLine>();
		String masterState = ApprovalUtil.STATE_MASTER_DISCUSSING;
		//기존 라인 일단 완료처리 하기
		changeStateApprovalLine(line, "", ApprovalUtil.STATE_LINE_COMPLETE, false , true);
		/*합의 체크  : 병렬 체크*/
		if(role.equals(ApprovalUtil.ROLE_DISCUSS)) {
			boolean isOnGoing = ApprovalHelper.manager.isDiscussOnGoing(line);
			if(isOnGoing) {
				return masterState;
			}else{
				nextLineList = ApprovalHelper.manager.getNextApproval(master, line.getSeq()+1);
			}
		}
		
		/*승인 체크 : 직렬 체크*/
		if(role.equals(ApprovalUtil.ROLE_APPROVE)) {
			nextLineList = ApprovalHelper.manager.getNextApproval(master, line.getSeq()+1);
		}
		
		//다음 결재 진행 (승임 및,수신)
		for(ApprovalLine  nextLine : nextLineList) {
			
			if(nextLine.getRole().toString().equals(ApprovalUtil.ROLE_APPROVE)) {
				masterState = ApprovalUtil.STATE_MASTER_APPROVING;
			}else {
				masterState = ApprovalUtil.STATE_MASTER_COMPLETED;
			}
			changeStateApprovalLine(nextLine, "", ApprovalUtil.STATE_LINE_ONGOING, true, false);
		}
		
		/*결재자 없을시 결재 완료*/
		if(nextLineList.size() ==0 ) {
			masterState = ApprovalUtil.STATE_MASTER_COMPLETED;
		}
		
		return masterState;
		
	}
	
	private ApprovalLine changeStateApprovalLine(ApprovalLine line,String description,String lineState,boolean isStartDate,boolean isApprovalDate) throws Exception{
		
		LOGGER.info("stateChaneApprovalLine lineState = " + lineState);
		line.setDescription(description);
		line.setState(ApproveStateType.toApproveStateType(lineState));
		
		if(isApprovalDate) {
			line.setApproveDate(new Timestamp(System.currentTimeMillis()));
		}
		
		if(isStartDate) {
			line.setStartDate(new Timestamp(System.currentTimeMillis()));
		}
		
		line = (ApprovalLine)PersistenceHelper.manager.save(line);
		
		return line;
	}
	
	private ApprovalMaster changeStateApprovalMaster(ApprovalMaster master,String masterState) throws Exception{
		
		if(master.getState().toString().equals(ApprovalUtil.STATE_MASTER_COMPLETED)){
			return master;
		}
		
		if(masterState.equals(ApprovalUtil.STATE_MASTER_COMPLETED) || masterState.equals(ApprovalUtil.STATE_MASTER_REJECTED)) {
			master.setCompleteDate(new Timestamp(System.currentTimeMillis()));
		}
		master.setState(ApproveStateType.toApproveStateType(masterState));
		master = (ApprovalMaster)PersistenceHelper.manager.save(master);
		return master;
	}
	
	@Override
	public HashMap<String, Object> updateSolution(Map<String, Object> hash) throws Exception {

		Transaction trx = new Transaction();
		HashMap<String, Object> returnMap = new HashMap<String, Object>();

		try {
			trx.start();

			String oid = ParamUtil.get(hash, "oid");

			String desc = ParamUtil.get(hash, "solution");
			List<String> secondary = StringUtil.checkReplaceArray(hash.get("SECONDARY"));
			List<String> delocIds = StringUtil.checkReplaceArray(hash.get("delocIds"));

			ReferenceFactory rf = new ReferenceFactory();
			IssueRequest issue = (IssueRequest) rf.getReference(oid).getObject();

			String requestDate = ParamUtil.get(hash, "requestDate");
			issue.setRequestDate(DateUtil.convertDate(requestDate));
			issue = (IssueRequest) PersistenceHelper.manager.modify(issue);

			IssueData data = new IssueData(issue);
			IssueSolution solution = data.Solution();

			solution.setSolution(desc);

			solution = (IssueSolution) PersistenceHelper.manager.modify(solution);

			// Vector secondaryDelFile = (Vector)hash.get("secondaryDelFile");
			// QueryResult secondaryFiles =
			// ContentHelper.service.getContentsByRole(solution,
			// ContentRoleType.SECONDARY);
			//
			// while(secondaryFiles.hasMoreElements()){
			// ContentItem item2 = (ContentItem) secondaryFiles.nextElement();
			// String itemId = item2.getPersistInfo().getObjectIdentifier().toString();
			//
			// if(secondaryDelFile==null || !secondaryDelFile.contains(itemId)){
			// PersistenceHelper.manager.delete(item2);
			// }
			// }

			/*
			 * Object secondaryDelFile = (Object) hash.get("secondaryDelFile");
			 * Vector<String> delFile = new Vector(); if (secondaryDelFile instanceof
			 * String) { delFile.add((String) secondaryDelFile); } else if (secondaryDelFile
			 * instanceof Vector) { delFile = (Vector) secondaryDelFile; }
			 */

			// CommonContentHelper.service.update(solution, files, delFile);
			CommonContentHelper.service.attach((ContentHolder) solution, null, secondary, delocIds, false);

			QueryResult results = PersistenceHelper.manager.navigate(solution, "doc", IssueDocLink.class, false);
			while (results.hasMoreElements()) {
				IssueDocLink link = (IssueDocLink) results.nextElement();
				PersistenceServerHelper.manager.remove(link);
			}

			String[] docOids = (String[]) hash.get("docOids");
			WTDocument doc = null;
			IssueDocLink dlink = null;
			if (docOids != null) {
				for (int i = 0; i < docOids.length; i++) {
					doc = (WTDocument) rf.getReference(docOids[i]).getObject();
					dlink = IssueDocLink.newIssueDocLink(solution, doc);
					PersistenceServerHelper.manager.insert(dlink);
				}
			}
			trx.commit();
			trx = null;

			returnMap.put("obj", issue);
			returnMap.put("oid", CommonUtil.getOIDString(issue));
			returnMap.put("msg", MESSAGEKEY.UPDATE);

		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("msg", e.getLocalizedMessage() + MESSAGEKEY.UPDATE_ERROR);
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return returnMap;
	}

	@Override
	public String deleteSolution(Map<String, Object> hash) throws Exception {

		Transaction trx = new Transaction();
		String msg = MESSAGEKEY.DELETE;
		try {
			trx.start();

			String oid = ParamUtil.get(hash, "oid");

			ReferenceFactory rf = new ReferenceFactory();
			IssueRequest issue = (IssueRequest) rf.getReference(oid).getObject();
			IssueData data = new IssueData(issue);
			IssueSolution solution = data.Solution();

			PersistenceHelper.manager.delete(solution);

			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getLocalizedMessage() + MESSAGEKEY.DELETE_ERROR;
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return msg;
	}

	@Override
	public String issueComplete(Map<String, Object> hash) throws Exception {

		Transaction trx = new Transaction();
		String msg = MESSAGEKEY.COMPLETE;
		try {
			trx.start();

			String oid = ParamUtil.get(hash, "oid");

			ReferenceFactory rf = new ReferenceFactory();
			IssueRequest issue = (IssueRequest) rf.getReference(oid).getObject();
			issue.setState(IssueKey.ISSUE_COMPLETE);
			PersistenceHelper.manager.modify(issue);
			//이슈 결재 상태 완료하기 
			nextIssueApprovalLine(issue);
			
			sendCompleteMail(issue);

			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getLocalizedMessage() + MESSAGEKEY.CHECKING_ERROR;
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return msg;
	}

	@Override
	public String cancelComplete(Map<String, Object> hash) throws Exception {

		Transaction trx = new Transaction();
		String msg = MESSAGEKEY.COMCANCEL;
		try {
			trx.start();

			String oid = ParamUtil.get(hash, "oid");

			ReferenceFactory rf = new ReferenceFactory();
			IssueRequest issue = (IssueRequest) rf.getReference(oid).getObject();
			issue.setState(IssueKey.ISSUE_CHECK);
			PersistenceHelper.manager.modify(issue);
			sendRejectMail(issue);

			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getLocalizedMessage() + MESSAGEKEY.COMCANCEL_ERROR;
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return msg;
	}

	@Override
	public WritableWorkbook downloadMyIssueListExcel(Map<String, Object> hash, WritableWorkbook workbook) {

		try {

			QuerySpec qs = ProjectQuery.manager.getMyIssue(hash);
			qs.setAdvancedQueryEnabled(true);
			QueryResult qr = PersistenceHelper.manager.find(qs);

			WritableSheet sheet = workbook.createSheet("목록", 1);

			WritableCellFormat titleformat = JExcelUtil.getCellFormat(Alignment.CENTRE, Colour.LIGHT_GREEN);
			WritableCellFormat cellformat = new WritableCellFormat();
			cellformat.setAlignment(Alignment.CENTRE);

			int row = 0;

			String[] titles = { "프로젝트번호", "프로젝트명", "태스트 명", "이슈제목", "이슈타입", "제기자", "제기일자", "담당자", "상태" };
			int[] sizes = { 20, 20, 15, 15, 12, 12, 17, 17, 14, 20, 15, 12, 20, 12, 14 };

			for (int i = 0; i < titles.length; i++) {
				sheet.setColumnView(i, sizes[i]);
				sheet.addCell(new Label(i, row, (String) titles[i], titleformat));
			}

			while (qr.hasMoreElements()) {
				Object[] o = (Object[]) qr.nextElement();
				IssueRequest issue = (IssueRequest) o[0];
				IssueData data = new IssueData(issue);

				String issueOid = WCUtil.getOid(issue);
				row++;
				int columnIndex = 0;
				sheet.addCell(new Label(columnIndex++, row, issue.getTask().getProject().getCode()));
				sheet.addCell(new Label(columnIndex++, row, issue.getTask().getProject().getName()));
				sheet.addCell(new Label(columnIndex++, row, issue.getTask().getName()));
				sheet.addCell(new Label(columnIndex++, row, issue.getName()));

				sheet.addCell(new Label(columnIndex++, row,
						CodeHelper.service.getName(NUMBERCODEKEY.ISSUETYPE, issue.getIssueType())));
				sheet.addCell(new Label(columnIndex++, row, data.getCreatorFullName()));
				sheet.addCell(new Label(columnIndex++, row,
						DateUtil.getDateString(issue.getPersistInfo().getCreateStamp(), "d")));
				sheet.addCell(new Label(columnIndex++, row, data.getManagerFullName()));
				sheet.addCell(new Label(columnIndex++, row, data.getState()));
			}
			return workbook;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
	}

	@Override
	public WritableWorkbook downloadSearchIssueExcelDown(Map<String, Object> hash, WritableWorkbook workbook) {

		try {
			QuerySpec qs = ProjectQuery.manager.getIssueList(hash);

			QueryResult qr = PersistenceHelper.manager.find(qs);
			WritableSheet sheet = workbook.createSheet("목록", 1);

			WritableCellFormat titleformat = JExcelUtil.getCellFormat(Alignment.CENTRE, Colour.LIGHT_GREEN);
			WritableCellFormat cellformat = new WritableCellFormat();
			cellformat.setAlignment(Alignment.CENTRE);

			int row = 0;

			String[] titles = { "프로젝트 번호", "프로젝트 명", "태스크 명", "이슈 제목", "제기자", "제기일자", "담당자", "상태" };
			int[] sizes = { 20, 30, 20, 20, 15, 20, 15, 12 };

			for (int i = 0; i < titles.length; i++) {
				sheet.setColumnView(i, sizes[i]);
				sheet.addCell(new Label(i, row, (String) titles[i], titleformat));
			}
			while (qr.hasMoreElements()) {
				Object[] o = (Object[]) qr.nextElement();
				IssueRequest issue = (IssueRequest) o[0];
				IssueData data = new IssueData(issue);

				row++;
				int columnIndex = 0;
				sheet.addCell(new Label(columnIndex++, row, issue.getTask().getProject().getCode()));
				sheet.addCell(new Label(columnIndex++, row, issue.getTask().getProject().getName()));
				sheet.addCell(new Label(columnIndex++, row, issue.getTask().getName()));
				sheet.addCell(new Label(columnIndex++, row, data.getTitle()));
				sheet.addCell(new Label(columnIndex++, row, data.getCreatorFullName(), cellformat));
				sheet.addCell(new Label(columnIndex++, row, data.getCreateDate(), cellformat));
				sheet.addCell(new Label(columnIndex++, row, data.getManagerFullName(), cellformat));
				sheet.addCell(new Label(columnIndex++, row, data.getState(), cellformat));

			}
			return workbook;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
	}

	/**
	 * 
	 * @desc : 이슈 담당자 업무 할당 메일 전송
	 * @author : tsjeong
	 * @date : 2020. 10. 21.
	 * @method : sendApprovedMail
	 * @return : void
	 * @param pp
	 */
	private void sendAssaignMail(IssueRequest pp) throws Exception {

		Hashtable<String, Object> mailHash = IssueMailForm.setAssaignMailInfo(pp);
		if (mailHash.size() > 0) {
			mailHash.put(E3PSQueueHelper.queueName, E3PSQueueHelper.QueueName_Mail);
			mailHash.put(E3PSQueueHelper.className, E3PSQueueHelper.ClassName_Mail);
			mailHash.put(E3PSQueueHelper.methodName, E3PSQueueHelper.MethodName_Issue_Assign_Mail);
			E3PSQueueHelper.manager.createQueue(mailHash);
		}
	}

	/**
	 * 
	 * @desc : 이슈 해결방안 등록 메일 전송
	 * @author : tsjeong
	 * @date : 2020. 10. 21.
	 * @method : sendSolutionMail
	 * @return : void
	 * @param pp
	 */
	private void sendSolutionMail(IssueRequest pp) throws Exception {

		Hashtable<String, Object> mailHash = IssueMailForm.setSolutionMailInfo(pp);
		if (mailHash.size() > 0) {
			mailHash.put(E3PSQueueHelper.queueName, E3PSQueueHelper.QueueName_Mail);
			mailHash.put(E3PSQueueHelper.className, E3PSQueueHelper.ClassName_Mail);
			mailHash.put(E3PSQueueHelper.methodName, E3PSQueueHelper.MethodName_Issue_Solution_Mail);
			E3PSQueueHelper.manager.createQueue(mailHash);
		}
	}

	/**
	 * 
	 * @desc : 이슈 검토 완료 메일 전송
	 * @author : tsjeong
	 * @date : 2020. 10. 21.
	 * @method : sendCompleteMail
	 * @return : void
	 * @param pp
	 */
	private void sendCompleteMail(IssueRequest pp) throws Exception {

		Hashtable<String, Object> mailHash = IssueMailForm.setCompleteMailInfo(pp);
		if (mailHash.size() > 0) {
			mailHash.put(E3PSQueueHelper.queueName, E3PSQueueHelper.QueueName_Mail);
			mailHash.put(E3PSQueueHelper.className, E3PSQueueHelper.ClassName_Mail);
			mailHash.put(E3PSQueueHelper.methodName, E3PSQueueHelper.MethodName_Issue_Complete_Mail);
			E3PSQueueHelper.manager.createQueue(mailHash);
		}
	}

	/**
	 * 
	 * @desc : 이슈 검토 완료 메일 전송
	 * @author : tsjeong
	 * @date : 2020. 10. 21.
	 * @method : sendRejectMail
	 * @return : void
	 * @param pp
	 */
	private void sendRejectMail(IssueRequest pp) throws Exception {

		Hashtable<String, Object> mailHash = IssueMailForm.setRejectMailInfo(pp);
		if (mailHash.size() > 0) {
			mailHash.put(E3PSQueueHelper.queueName, E3PSQueueHelper.QueueName_Mail);
			mailHash.put(E3PSQueueHelper.className, E3PSQueueHelper.ClassName_Mail);
			mailHash.put(E3PSQueueHelper.methodName, E3PSQueueHelper.MethodName_Issue_Reject_Mail);
			E3PSQueueHelper.manager.createQueue(mailHash);
		}
	}
}
