package com.e3ps.approval.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.e3ps.approval.ApprovalLine;
import com.e3ps.approval.ApprovalLineTemplate;
import com.e3ps.approval.ApprovalMaster;
import com.e3ps.approval.ApprovalObjectLink;
import com.e3ps.approval.MultiApproval;
import com.e3ps.approval.MultiApprovalObjectLink;
import com.e3ps.approval.WFHistory;
import com.e3ps.approval.bean.ApprovalData;
import com.e3ps.approval.bean.ApprovalLineData;
import com.e3ps.approval.bean.ApprovalLineTemplateData;
import com.e3ps.approval.bean.ApprovalListData;
import com.e3ps.approval.bean.WFHistoryData;
import com.e3ps.approval.util.ApprovalUtil;
import com.e3ps.change.EChangeActivity;
import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.service.ChangeService;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.AccessControlUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.SearchUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.web.PageQueryBroker;
import com.e3ps.epm.service.EpmHelper;
import com.e3ps.org.People;
import com.e3ps.org.bean.PeopleData;
import com.e3ps.org.service.PeopleHelper;
import com.e3ps.part.service.PartHelper;
import com.ptc.windchill.uwgm.common.container.OrganizationHelper;

import wt.enterprise.Master;
import wt.enterprise.RevisionControlled;
import wt.epm.EPMDocument;
import wt.fc.PagingQueryResult;
import wt.fc.PagingSessionHelper;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.lifecycle.LifeCycleManaged;
import wt.org.OrganizationServicesHelper;
import wt.org.WTUser;
import wt.query.ArrayExpression;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SQLFunction;
import wt.query.SearchCondition;
import wt.query.SubSelectExpression;
import wt.services.ServiceFactory;
import wt.session.SessionHelper;
import wt.util.WTAttributeNameIfc;
import wt.vc.Iterated;
import wt.vc.VersionControlHelper;
import wt.vc.Versioned;

public class ApprovalHelper {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.APPROVAL.getName());
	public static final ApprovalService service = ServiceFactory.getService(ApprovalService.class);
	public static final ApprovalHelper manager = new ApprovalHelper();

	/**
	 * 
	 * @desc : 결재 라인 정보
	 * @author : tsuam
	 * @date : 2019. 7. 23.
	 * @method : getApprovalLine
	 * @return : List<ApprovalLineData>
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public List<ApprovalLineData> getApprovalLine(Map<String, Object> reqMap) throws Exception {

		List<ApprovalLineData> list = new ArrayList<>();

		String oid = StringUtil.checkNull((String) reqMap.get("oid")); // 결재 라인 객체

		if (oid.length() > 0) {
			String type = StringUtil.checkNull((String) reqMap.get("type")); // view ,editor

			String searchRoleType = StringUtil.checkNull((String) reqMap.get("searchRoleType")); // view ,editor
			boolean isView = type.equals("view") ? true : false;

			Persistable per = CommonUtil.getObject(oid);
			if (per instanceof ApprovalLine) {
				ApprovalLine line = (ApprovalLine) per;
				// approval line 유무
				if (line.getMaster() != null) {
					list = getApprovalLastLine(line.getMaster(), isView, searchRoleType);
				}
			} else {
				ApprovalMaster master = getApprovalMaster(per);
				if(master != null) {
					list = getApprovalLastLine(master, isView, searchRoleType);
				}
			}
		}

		return list;
	}

	public List<Map<String, Object>> getSearchUserListAction(Map<String, Object> reqMap) throws Exception {

		List<Map<String, Object>> list = new ArrayList<>();

		List<PeopleData> peopleList = PeopleHelper.manager.getSearchUserListAction(reqMap);

		for (PeopleData pData : peopleList) {
			Map<String, Object> map = new HashMap<>();

			map.put("pOid", pData.getOid());
			map.put("name", pData.getName());
			map.put("departmentCode", pData.getDepartmentCode());
			map.put("departmentName", pData.getDepartmentName());
			map.put("dutyCode", pData.getDutyCode());
			map.put("duty", pData.getDuty());
			map.put("id", pData.getId());

			list.add(map);
		}

		return list;
	}

	/**
	 * 
	 * @desc : 임시저장함(임시저장,반려)
	 * @author : tsuam
	 * @date : 2019. 7. 18.
	 * @method : searchApproval
	 * @return : void
	 * @param reqMap
	 */
	public List<ApprovalListData> searchTempApproval(Map<String, Object> reqMap) throws Exception {

		List<ApprovalListData> list = new ArrayList<ApprovalListData>();

		// 검색 조건
		List<String> creatorList = StringUtil.checkReplaceArray(reqMap.get("creator"));
		List<String> stateList = StringUtil.checkReplaceArray(reqMap.get("state"));
		List<String> objectTypeList = StringUtil.checkReplaceArray(reqMap.get("objectType"));
		List<String> approverList = StringUtil.checkReplaceArray(reqMap.get("approver"));

		String predate = StringUtil.checkNull((String) reqMap.get("predate"));
		String postdate = StringUtil.checkNull((String) reqMap.get("postdate"));
		String predate_startDate = StringUtil.checkNull((String) reqMap.get("predate_startDate"));
		String postdate_startDate = StringUtil.checkNull((String) reqMap.get("postdate_startDate"));

		String title = StringUtil.checkNull((String) reqMap.get("title"));
		title = title.replace("[", "[[]");

		QuerySpec qs = new QuerySpec();
		WTUser user = (WTUser) SessionHelper.getPrincipal();

		int idx0 = qs.addClassList(ApprovalMaster.class, false);
		int idx1 = qs.addClassList(ApprovalObjectLink.class, true);
		int idx2 = qs.addClassList(ApprovalLine.class, true);
		SearchCondition sc = null;
		// Join
		getApprovalJoinQuery(qs);

		// 로그인 유저 및 최신 결재 라인
		getOwnerApprovalLine(qs, idx2, true);

		// master owner
		if (!CommonUtil.isAdmin()) {
			qs.appendAnd();
			sc = new SearchCondition(ApprovalMaster.class, "owner.key.id", SearchCondition.EQUAL,
					CommonUtil.getOIDLongValue(user));
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 마스터 상태 (임시 저장,반려)
		qs.appendAnd();
		qs.appendOpenParen();
		sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.STATE, SearchCondition.EQUAL,
				ApprovalUtil.STATE_MASTER_TEMP_STORAGE);
		qs.appendWhere(sc, new int[] { idx0 });
		qs.appendOr();
		sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.STATE, SearchCondition.EQUAL,
				ApprovalUtil.STATE_MASTER_REJECTED);
		qs.appendWhere(sc, new int[] { idx0 });
		qs.appendCloseParen();

		// 설계변경활동 제외
		qs.appendAnd();
		sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.OBJECT_TYPE, "<>", ApprovalUtil.OBJECT_TYPE_ECA);
		qs.appendWhere(sc, new int[] { idx0 });

		// 기안자
		qs.appendAnd();
		sc = new SearchCondition(ApprovalLine.class, ApprovalLine.ROLE, SearchCondition.EQUAL, ApprovalUtil.ROLE_DRAFT);
		qs.appendWhere(sc, new int[] { idx2 });

		// 타이틀
		if (title.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.TITLE, SearchCondition.LIKE,
					"%" + title + "%", false);
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 등록자
		if (creatorList.size() > 0) {
			List<Long> userOidLongValueList = new ArrayList<>();

			for (String pp : creatorList) {
				People people = (People) CommonUtil.getObject(pp);
				WTUser searchUser = people.getUser();

				userOidLongValueList.add(CommonUtil.getOIDLongValue(searchUser));
			}

			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(ApprovalMaster.class, ApprovalMaster.OWNER + "." + "key.id"),
					SearchCondition.IN, new ArrayExpression(userOidLongValueList.toArray()));
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 결재자
		if (approverList.size() > 0) {
			List<Long> userOidLongValueList = new ArrayList<>();

			for (String pp : approverList) {
				People people = (People) CommonUtil.getObject(pp);
				WTUser searchUser = people.getUser();

				userOidLongValueList.add(CommonUtil.getOIDLongValue(searchUser));
			}

			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(ApprovalLine.class, ApprovalLine.OWNER + "." + "key.id"),
					SearchCondition.IN, new ArrayExpression(userOidLongValueList.toArray()));
			qs.appendWhere(sc, new int[] { idx2 });
		}

		// 마스터 객체 타입 조회
		if (objectTypeList.size() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(ApprovalMaster.class, ApprovalMaster.OBJECT_TYPE),
					SearchCondition.IN, new ArrayExpression(objectTypeList.toArray()));
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 마스터 상태 조회
		if (stateList.size() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(ApprovalMaster.class, ApprovalMaster.STATE), SearchCondition.IN,
					new ArrayExpression(stateList.toArray()));
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 마스터 생성일(등록일)
		if (predate.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalMaster.class, WTAttributeNameIfc.CREATE_STAMP_NAME,
					SearchCondition.GREATER_THAN, DateUtil.convertStartDate(predate));
			qs.appendWhere(sc, new int[] { idx0 });
		}
		if (postdate.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalMaster.class, WTAttributeNameIfc.CREATE_STAMP_NAME,
					SearchCondition.LESS_THAN, DateUtil.convertEndDate(postdate));
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 수신일
		if (predate_startDate.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalLine.class, ApprovalLine.START_DATE, SearchCondition.GREATER_THAN,
					DateUtil.convertStartDate(predate_startDate));
			qs.appendWhere(sc, new int[] { idx2 });
		}
		if (postdate_startDate.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalLine.class, ApprovalLine.START_DATE, SearchCondition.LESS_THAN,
					DateUtil.convertEndDate(postdate_startDate));
			qs.appendWhere(sc, new int[] { idx2 });
		}
		System.out.println( " searchTempApproval ::: " + qs);

		qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class, ApprovalLine.START_DATE), true),
				new int[] { idx2 });
		LOGGER.info(qs.toString());
		QueryResult rt = PersistenceHelper.manager.find(qs);

		while (rt.hasMoreElements()) {
			Object[] objects = (Object[]) rt.nextElement();
			ApprovalObjectLink link = (ApprovalObjectLink) objects[0];
			ApprovalLine line = (ApprovalLine) objects[1];
			ApprovalMaster master = (ApprovalMaster) link.getRoleBObject();
			String state = master.getState().toString();
			ApprovalListData data = ApprovalUtil.setApprovalObject(line, link, state);
			if(data!=null) list.add(data);


		}

		return list;

	}

	/**
	 * 
	 * @desc : 발신함
	 * @author : tsuam
	 * @date : 2019. 7. 26.
	 * @method : searchSendItemAction
	 * @return : List<ApprovalListData>
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public List<ApprovalListData> searchSendItemAction(Map<String, Object> reqMap) throws Exception {

		List<ApprovalListData> list = new ArrayList<ApprovalListData>();

		QuerySpec qs = searchSendItemQuery(reqMap);

		QueryResult rt = PersistenceHelper.manager.find(qs);

		while (rt.hasMoreElements()) {
			Object[] objects = (Object[]) rt.nextElement();
			ApprovalObjectLink link = (ApprovalObjectLink) objects[0];
			ApprovalLine line = (ApprovalLine) objects[1];
			ApprovalMaster master = (ApprovalMaster) link.getRoleBObject();
			String state = master.getState().toString();
			ApprovalListData data = ApprovalUtil.setApprovalObject(line, link, state);
			if(data!=null) list.add(data);

		}

		return list;

	}

	public Map<String, Object> searchSendItemScrollAction(Map<String, Object> reqMap) throws Exception {

		Map<String, Object> map = new HashMap<>();

		List<ApprovalListData> list = new ArrayList<ApprovalListData>();

		int page = (Integer) reqMap.get("page");
		int rows = (Integer) reqMap.get("rows");

		String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));

		PagingQueryResult result = null;

		if (sessionId.length() > 0) {
			result = PagingSessionHelper.fetchPagingSession((page-1)*rows, rows, Long.valueOf(sessionId));
		} else {
			QuerySpec query = searchSendItemQuery(reqMap);

			result = PageQueryBroker.openPagingSession((page-1)*rows, rows, query, true);
		}

		int totalSize = result.getTotalSize();

		while (result.hasMoreElements()) {
			Object[] objects = (Object[]) result.nextElement();
			ApprovalObjectLink link = (ApprovalObjectLink) objects[0];
			ApprovalLine line = (ApprovalLine) objects[1];
			ApprovalMaster master = (ApprovalMaster) link.getRoleBObject();
			String state = master.getState().toString();
			ApprovalListData data = ApprovalUtil.setApprovalObject(line, link, state);
			if(data!=null) list.add(data);


		}

		map.put("list", list);
		map.put("totalSize", totalSize);
		map.put("sessionId", result.getSessionId() == 0 ? "" : result.getSessionId());

		return map;
	}

	public QuerySpec searchSendItemQuery(Map<String, Object> reqMap) throws Exception {

		List<String> creatorList = StringUtil.checkReplaceArray(reqMap.get("creator"));
		List<String> stateList = StringUtil.checkReplaceArray(reqMap.get("state"));
		List<String> objectTypeList = StringUtil.checkReplaceArray(reqMap.get("objectType"));

		String predate = StringUtil.checkNull((String) reqMap.get("predate"));
		String postdate = StringUtil.checkNull((String) reqMap.get("postdate"));
		String predate_completeDate = StringUtil.checkNull((String) reqMap.get("predate_completeDate"));
		String postdate_completeDate = StringUtil.checkNull((String) reqMap.get("postdate_completeDate"));

		String title = StringUtil.checkNull((String) reqMap.get("title"));
		title = title.replace("[", "[[]");

		QuerySpec qs = new QuerySpec();
		WTUser user = (WTUser) SessionHelper.getPrincipal();

		int idx0 = qs.addClassList(ApprovalMaster.class, false);
		int idx1 = qs.addClassList(ApprovalObjectLink.class, true);
		int idx2 = qs.addClassList(ApprovalLine.class, true);
		SearchCondition sc = null;
		// Join
		getApprovalJoinQuery(qs);

		// 로그인 유저 및 최신 결재 라인
		getOwnerApprovalLine(qs, idx2, true);

		// master owner
		if (!CommonUtil.isAdmin()) {
			qs.appendAnd();
			sc = new SearchCondition(ApprovalMaster.class, "owner.key.id", SearchCondition.EQUAL,
					CommonUtil.getOIDLongValue(user));
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 기안자
		qs.appendAnd();
		sc = new SearchCondition(ApprovalLine.class, ApprovalLine.ROLE, SearchCondition.EQUAL, ApprovalUtil.ROLE_DRAFT);
		qs.appendWhere(sc, new int[] { idx2 });

		// 타이틀
		if (title.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.TITLE, SearchCondition.LIKE,
					"%" + title + "%", false);
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 등록자
		if (creatorList.size() > 0) {
			List<Long> userOidLongValueList = new ArrayList<>();

			for (String pp : creatorList) {
				People people = (People) CommonUtil.getObject(pp);
				WTUser searchUser = people.getUser();

				userOidLongValueList.add(CommonUtil.getOIDLongValue(searchUser));
			}

			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(ApprovalMaster.class, ApprovalMaster.OWNER + "." + "key.id"),
					SearchCondition.IN, new ArrayExpression(userOidLongValueList.toArray()));
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 마스터 객체 타입 조회
		if (objectTypeList.size() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(ApprovalMaster.class, ApprovalMaster.OBJECT_TYPE),
					SearchCondition.IN, new ArrayExpression(objectTypeList.toArray()));
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 마스터 상태 조회
		if (stateList.size() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(ApprovalMaster.class, ApprovalMaster.STATE), SearchCondition.IN,
					new ArrayExpression(stateList.toArray()));
			qs.appendWhere(sc, new int[] { idx0 });
		} else {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.STATE, SearchCondition.NOT_EQUAL,
					ApprovalUtil.STATE_MASTER_TEMP_STORAGE);
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 마스터 생성일
		if (predate.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalMaster.class, WTAttributeNameIfc.CREATE_STAMP_NAME,
					SearchCondition.GREATER_THAN, DateUtil.convertStartDate(predate));
			qs.appendWhere(sc, new int[] { idx0 });
		}
		if (postdate.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalMaster.class, WTAttributeNameIfc.CREATE_STAMP_NAME,
					SearchCondition.LESS_THAN, DateUtil.convertEndDate(postdate));
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 마스터 완료일
		if (predate_completeDate.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.COMPLETE_DATE, SearchCondition.GREATER_THAN,
					DateUtil.convertStartDate(predate_completeDate));
			qs.appendWhere(sc, new int[] { idx0 });
		}
		if (postdate_completeDate.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.COMPLETE_DATE, SearchCondition.LESS_THAN,
					DateUtil.convertEndDate(postdate_completeDate));
			qs.appendWhere(sc, new int[] { idx0 });
		}

		qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class, ApprovalLine.START_DATE), true),
				new int[] { idx2 });
		System.out.println("searchSendItemQuery : : " + qs);
		return qs;

	}

	/**
	 * 
	 * @desc : 결재함
	 * @author : tsuam
	 * @date : 2019. 7. 19.
	 * @method : searchWorkingApproval
	 * @return : void
	 * @param reqMap
	 * @throws Exception
	 */
	public List<ApprovalListData> searchWorkingApproval(Map<String, Object> reqMap, boolean isChangeActivity)
			throws Exception {

		List<ApprovalListData> list = new ArrayList<ApprovalListData>();

		// 검색 조건
		List<String> creatorList = StringUtil.checkReplaceArray(reqMap.get("creator"));
		List<String> approverList = StringUtil.checkReplaceArray(reqMap.get("approver"));

		List<String> stateList = StringUtil.checkReplaceArray(reqMap.get("state"));
		List<String> objectTypeList = StringUtil.checkReplaceArray(reqMap.get("objectType"));

		String predate = StringUtil.checkNull((String) reqMap.get("predate"));
		String postdate = StringUtil.checkNull((String) reqMap.get("postdate"));
		String predate_startDate = StringUtil.checkNull((String) reqMap.get("predate_startDate"));
		String postdate_startDate = StringUtil.checkNull((String) reqMap.get("postdate_startDate"));

		String title = StringUtil.checkNull((String) reqMap.get("title"));
		title = title.replace("[", "[[]");

		QuerySpec qs = new QuerySpec();
		int idx0 = qs.addClassList(ApprovalMaster.class, false);
		int idx1 = qs.addClassList(ApprovalObjectLink.class, true);
		int idx2 = qs.addClassList(ApprovalLine.class, true);

		// qs.appendSelectAttribute(ApprovalMaster.STATE, idx0, true);

		getApprovalJoinQuery(qs);

		// 로그인 유저 및 최신 결재 라인
		getOwnerApprovalLine(qs, idx2, true);

		// 마스터 상태 (합의중,승인중,완료됨)
		qs.appendAnd();
		qs.appendOpenParen();
		SearchCondition sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.STATE, SearchCondition.EQUAL,
				ApprovalUtil.STATE_MASTER_DISCUSSING);
		qs.appendWhere(sc, new int[] { idx0 });

		qs.appendOr();
		sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.STATE, SearchCondition.EQUAL,
				ApprovalUtil.STATE_MASTER_APPROVING);
		qs.appendWhere(sc, new int[] { idx0 });

		qs.appendOr();
		sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.STATE, SearchCondition.EQUAL,
				ApprovalUtil.STATE_MASTER_COMPLETED);
		qs.appendWhere(sc, new int[] { idx0 });
		qs.appendCloseParen();

		// 설변업무
		if (isChangeActivity) {

			qs.appendAnd();
			sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.OBJECT_TYPE, SearchCondition.EQUAL,
					ApprovalUtil.OBJECT_TYPE_ECA);
			qs.appendWhere(sc, new int[] { idx0 });

			int ecaIdx = qs.appendClassList(EChangeActivity.class, false);

			qs.appendAnd();
			sc = new SearchCondition(ApprovalObjectLink.class, ApprovalObjectLink.ROLE_AOBJECT_REF + ".key.id",
					EChangeActivity.class, EChangeActivity.PERSIST_INFO + ".theObjectIdentifier.id");
			qs.appendWhere(sc, new int[] { idx1, ecaIdx });

			qs.appendAnd();
			sc = new SearchCondition(EChangeActivity.class, EChangeActivity.ACTIVE_STATE, SearchCondition.NOT_EQUAL,
					ChangeService.ECO_STOPPED);
			qs.appendWhere(sc, new int[] { ecaIdx });

		} else {

			qs.appendAnd();
			sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.OBJECT_TYPE, "<>",
					ApprovalUtil.OBJECT_TYPE_ECA);
			qs.appendWhere(sc, new int[] { idx0 });

		}

		// 결재라인 상태
		qs.appendAnd();
		sc = new SearchCondition(ApprovalLine.class, ApprovalLine.STATE, SearchCondition.EQUAL, "ONGOING");
		qs.appendWhere(sc, new int[] { idx2 });

		// 타이틀
		if (title.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.TITLE, SearchCondition.LIKE,
					"%" + title + "%", false);
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 등록자
		if (creatorList.size() > 0) {
			List<Long> userOidLongValueList = new ArrayList<>();

			for (String pp : creatorList) {
				People people = (People) CommonUtil.getObject(pp);
				WTUser searchUser = people.getUser();

				userOidLongValueList.add(CommonUtil.getOIDLongValue(searchUser));
			}

			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(ApprovalMaster.class, ApprovalMaster.OWNER + "." + "key.id"),
					SearchCondition.IN, new ArrayExpression(userOidLongValueList.toArray()));
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 결재자
		if (approverList.size() > 0) {
			List<Long> userOidLongValueList = new ArrayList<>();

			for (String pp : approverList) {
				People people = (People) CommonUtil.getObject(pp);
				WTUser searchUser = people.getUser();

				userOidLongValueList.add(CommonUtil.getOIDLongValue(searchUser));
			}

			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(ApprovalLine.class, ApprovalLine.OWNER + "." + "key.id"),
					SearchCondition.IN, new ArrayExpression(userOidLongValueList.toArray()));
			qs.appendWhere(sc, new int[] { idx2 });
		}

		// 마스터 객체 타입 조회
		if (objectTypeList.size() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(ApprovalMaster.class, ApprovalMaster.OBJECT_TYPE),
					SearchCondition.IN, new ArrayExpression(objectTypeList.toArray()));
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 마스터 상태 조회
		if (stateList.size() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(ApprovalMaster.class, ApprovalMaster.STATE), SearchCondition.IN,
					new ArrayExpression(stateList.toArray()));
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 마스터 생성일
		if (predate.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalMaster.class, WTAttributeNameIfc.CREATE_STAMP_NAME,
					SearchCondition.GREATER_THAN, DateUtil.convertStartDate(predate));
			qs.appendWhere(sc, new int[] { idx0 });
		}
		if (postdate.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalMaster.class, WTAttributeNameIfc.CREATE_STAMP_NAME,
					SearchCondition.LESS_THAN, DateUtil.convertEndDate(postdate));
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 수신일
		if (predate_startDate.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalLine.class, ApprovalLine.START_DATE, SearchCondition.GREATER_THAN,
					DateUtil.convertStartDate(predate_startDate));
			qs.appendWhere(sc, new int[] { idx2 });
		}
		if (postdate_startDate.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalLine.class, ApprovalLine.START_DATE, SearchCondition.LESS_THAN,
					DateUtil.convertEndDate(postdate_startDate));
			qs.appendWhere(sc, new int[] { idx2 });
		}

		// 수신일
		if (predate_startDate.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalLine.class, ApprovalLine.START_DATE, SearchCondition.GREATER_THAN,
					DateUtil.convertStartDate(predate_startDate));
			qs.appendWhere(sc, new int[] { idx2 });
		}
		if (postdate_startDate.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalLine.class, ApprovalLine.START_DATE, SearchCondition.LESS_THAN,
					DateUtil.convertEndDate(postdate_startDate));
			qs.appendWhere(sc, new int[] { idx2 });
		}
		
		qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class, ApprovalLine.START_DATE), true),
				new int[] { idx2 });
		LOGGER.info(qs.toString());
		QueryResult rt = PersistenceHelper.manager.find(qs);

		while (rt.hasMoreElements()) {
			Object[] objects = (Object[]) rt.nextElement();
			ApprovalObjectLink link = (ApprovalObjectLink) objects[0];
			ApprovalLine line = (ApprovalLine) objects[1];
			String state = ((ApprovalMaster) link.getRoleBObject()).getState().toString();
			ApprovalListData data = ApprovalUtil.setApprovalObject(line, link, state);
			if(data!=null) list.add(data);

		}

		return list;

	}

	/**
	 * 
	 * @desc : 진행함 ing ,완료함 complete,수신함 receive
	 * @author : tsuam
	 * @date : 2019. 7. 26.
	 * @method : searchWorkingLineApproval
	 * @return : List<ApprovalListData>
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public List<ApprovalListData> searchListItemAction(Map<String, Object> reqMap) throws Exception {

		List<ApprovalListData> list = new ArrayList<ApprovalListData>();

		QuerySpec query = searchListItemQuery(reqMap);

		QueryResult rt = PersistenceHelper.manager.find(query);

		while (rt.hasMoreElements()) {
			Object[] objects = (Object[]) rt.nextElement();
			ApprovalObjectLink link = (ApprovalObjectLink) objects[0];
			ApprovalLine line = (ApprovalLine) objects[1];
			String state = ((ApprovalMaster) link.getRoleBObject()).getState().toString();
			ApprovalListData data = ApprovalUtil.setApprovalObject(line, link, state);
			if(data!=null) {
				data.setProcessStepData(getProcessStepData(line.getMaster()));
				list.add(data);
			}

		}

		return list;

	}

	public Map<String, Object> searchListItemScrollAction(Map<String, Object> reqMap) throws Exception {

		Map<String, Object> map = new HashMap<>();

		List<ApprovalListData> list = new ArrayList<ApprovalListData>();

		int page = (Integer) reqMap.get("page");
		int rows = (Integer) reqMap.get("rows");

		String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));

		PagingQueryResult result = null;

		if (sessionId.length() > 0) {
			result = PagingSessionHelper.fetchPagingSession(( page - 1 ) * rows, rows, Long.valueOf(sessionId));
		} else {
			QuerySpec query = searchListItemQuery(reqMap);

			result = PageQueryBroker.openPagingSession(( page - 1 ) * rows, rows, query, true);
		}

		int totalSize = result.getTotalSize();

		while (result.hasMoreElements()) {
			Object[] objects = (Object[]) result.nextElement();
			ApprovalObjectLink link = (ApprovalObjectLink) objects[0];
			ApprovalLine line = (ApprovalLine) objects[1];
			String state = ((ApprovalMaster) link.getRoleBObject()).getState().toString();
			ApprovalListData data = ApprovalUtil.setApprovalObject(line, link, state);
			if(data!=null) {
				data.setProcessStepData(getProcessStepData(line.getMaster()));
				list.add(data);
			}

		}

		map.put("list", list);
		map.put("totalSize", totalSize);
		map.put("sessionId", result.getSessionId() == 0 ? "" : result.getSessionId());

		return map;

	}

	public QuerySpec searchListItemQuery(Map<String, Object> reqMap) throws Exception {

		// 소팅
		// Map<String, Object> sort = reqMap.get("sort") != null ? (Map<String, Object>)
		// reqMap.get("sort") : new HashMap<>();

		// 검색 조건
		String type = (String) reqMap.get("type");
		List<String> creatorList = StringUtil.checkReplaceArray(reqMap.get("creator"));
		List<String> stateList = StringUtil.checkReplaceArray(reqMap.get("state"));
		List<String> objectTypeList = StringUtil.checkReplaceArray(reqMap.get("objectType"));

		String predate = StringUtil.checkNull((String) reqMap.get("predate"));
		String postdate = StringUtil.checkNull((String) reqMap.get("postdate"));
		String predate_completeDate = StringUtil.checkNull((String) reqMap.get("predate_complete"));
		String postdate_completeDate = StringUtil.checkNull((String) reqMap.get("postdate_complete"));

		String title = StringUtil.checkNull((String) reqMap.get("title"));
		title = title.replace("[", "[[]");

		QuerySpec qs = new QuerySpec();

		int idx0 = qs.addClassList(ApprovalMaster.class, false);
		int idx1 = qs.addClassList(ApprovalObjectLink.class, true);
		int idx2 = qs.addClassList(ApprovalLine.class, true);
		SearchCondition sc = null;
		// qs.appendSelectAttribute(ApprovalMaster.STATE, idx0, true);

		getApprovalJoinQuery(qs);

		// 로그인 유저 및 최신 결재 라인
		getOwnerApprovalLine(qs, idx2, true);

		if (type.equals("ing")) { // 진행함
			// 마스터 상태 (합의중,승인중,완료됨)
			qs.appendAnd();
			qs.appendOpenParen();
			sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.STATE, SearchCondition.EQUAL,
					ApprovalUtil.STATE_MASTER_DISCUSSING);
			qs.appendWhere(sc, new int[] { idx0 });
			qs.appendOr();
			sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.STATE, SearchCondition.EQUAL,
					ApprovalUtil.STATE_MASTER_APPROVING);
			qs.appendWhere(sc, new int[] { idx0 });
			qs.appendCloseParen();

			// 결재 라인 상태
			qs.appendAnd();
			qs.appendOpenParen();
			sc = new SearchCondition(ApprovalLine.class, ApprovalLine.STATE, SearchCondition.EQUAL,
					ApprovalUtil.STATE_LINE_COMPLETE);
			qs.appendWhere(sc, new int[] { idx2 });
			qs.appendOr();
			sc = new SearchCondition(ApprovalLine.class, ApprovalLine.STATE, SearchCondition.EQUAL,
					ApprovalUtil.STATE_LINE_APPEAL);
			qs.appendWhere(sc, new int[] { idx2 });
			qs.appendCloseParen();

			// 결재 Role
			if (CommonUtil.isAdmin()) {
				qs.appendAnd(); // 기안,승인,합의
				sc = new SearchCondition(ApprovalLine.class, ApprovalLine.ROLE, SearchCondition.EQUAL,
						ApprovalUtil.ROLE_DRAFT);
				qs.appendWhere(sc, new int[] { idx2 });

			} else {
				qs.appendAnd(); // 기안,승인,합의
				qs.appendOpenParen();
				sc = new SearchCondition(ApprovalLine.class, ApprovalLine.ROLE, SearchCondition.EQUAL,
						ApprovalUtil.ROLE_DRAFT);
				qs.appendWhere(sc, new int[] { idx2 });
				qs.appendOr();
				sc = new SearchCondition(ApprovalLine.class, ApprovalLine.ROLE, SearchCondition.EQUAL,
						ApprovalUtil.ROLE_APPROVE);
				qs.appendWhere(sc, new int[] { idx2 });
				qs.appendOr();
				sc = new SearchCondition(ApprovalLine.class, ApprovalLine.ROLE, SearchCondition.EQUAL,
						ApprovalUtil.ROLE_DISCUSS);
				qs.appendWhere(sc, new int[] { idx2 });
				qs.appendCloseParen();
			}

		} else if (type.equals("complete") || type.equals("receive")) { // 완료함,수신함
			// 마스터 상태 (합의중,승인중,완료됨)
			qs.appendAnd();
			qs.appendOpenParen();
			sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.STATE, SearchCondition.EQUAL,
					ApprovalUtil.STATE_MASTER_COMPLETED);
			qs.appendWhere(sc, new int[] { idx0 });
			qs.appendOr();
			sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.STATE, SearchCondition.EQUAL,
					ApprovalUtil.STATE_MASTER_REJECTED);
			qs.appendWhere(sc, new int[] { idx0 });
			qs.appendCloseParen();

			if (type.equals("complete")) {// 완료함
				// 결재 라인 상태
				qs.appendAnd();
				qs.appendOpenParen();
				sc = new SearchCondition(ApprovalLine.class, ApprovalLine.STATE, SearchCondition.EQUAL,
						ApprovalUtil.STATE_LINE_COMPLETE);
				qs.appendWhere(sc, new int[] { idx2 });
				qs.appendOr();
				sc = new SearchCondition(ApprovalLine.class, ApprovalLine.STATE, SearchCondition.EQUAL,
						ApprovalUtil.STATE_LINE_APPEAL);
				qs.appendWhere(sc, new int[] { idx2 });
				qs.appendOr();
				sc = new SearchCondition(ApprovalLine.class, ApprovalLine.STATE, SearchCondition.EQUAL,
						ApprovalUtil.STATE_LINE_REJECT);
				qs.appendWhere(sc, new int[] { idx2 });
				qs.appendCloseParen();

				if (CommonUtil.isAdmin()) {
					qs.appendAnd();
					sc = new SearchCondition(ApprovalLine.class, ApprovalLine.ROLE, SearchCondition.EQUAL,
							ApprovalUtil.ROLE_DRAFT);
					qs.appendWhere(sc, new int[] { idx2 });
				} else {
					// 결재 Role
					qs.appendAnd(); // 기안,승인,합의
					qs.appendOpenParen();
					sc = new SearchCondition(ApprovalLine.class, ApprovalLine.ROLE, SearchCondition.EQUAL,
							ApprovalUtil.ROLE_DRAFT);
					qs.appendWhere(sc, new int[] { idx2 });
					qs.appendOr();
					sc = new SearchCondition(ApprovalLine.class, ApprovalLine.ROLE, SearchCondition.EQUAL,
							ApprovalUtil.ROLE_APPROVE);
					qs.appendWhere(sc, new int[] { idx2 });
					qs.appendOr();
					sc = new SearchCondition(ApprovalLine.class, ApprovalLine.ROLE, SearchCondition.EQUAL,
							ApprovalUtil.ROLE_DISCUSS);
					qs.appendWhere(sc, new int[] { idx2 });
					qs.appendCloseParen();
				}

			} else { // 수신함
				// 결재 라인 상태
				qs.appendAnd();
				sc = new SearchCondition(ApprovalLine.class, ApprovalLine.STATE, SearchCondition.EQUAL,
						ApprovalUtil.STATE_LINE_COMPLETE);
				qs.appendWhere(sc, new int[] { idx2 });
				// 결재 Role
				qs.appendAnd(); // 수신
				sc = new SearchCondition(ApprovalLine.class, ApprovalLine.ROLE, SearchCondition.EQUAL,
						ApprovalUtil.ROLE_RECEIVE);
				qs.appendWhere(sc, new int[] { idx2 });

			}

		}

		// 기안자
		// qs.appendAnd();
		// sc = new SearchCondition(ApprovalLine.class,
		// ApprovalLine.ROLE,SearchCondition.EQUAL,ApprovalUtil.ROLE_DRAFT);
		// qs.appendWhere(sc, new int[] { idx2 });

		// 타이틀
		if (title.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.TITLE, SearchCondition.LIKE,
					"%" + title + "%", false);
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 등록자
		if (creatorList.size() > 0) {
			List<Long> userOidLongValueList = new ArrayList<>();

			for (String pp : creatorList) {
				People people = (People) CommonUtil.getObject(pp);
				WTUser searchUser = people.getUser();

				userOidLongValueList.add(CommonUtil.getOIDLongValue(searchUser));
			}

			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(ApprovalMaster.class, ApprovalMaster.OWNER + "." + "key.id"),
					SearchCondition.IN, new ArrayExpression(userOidLongValueList.toArray()));
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 마스터 객체 타입 조회
		if (objectTypeList.size() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(ApprovalMaster.class, ApprovalMaster.OBJECT_TYPE),
					SearchCondition.IN, new ArrayExpression(objectTypeList.toArray()));
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 마스터 상태 조회
		if (stateList.size() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(ApprovalMaster.class, ApprovalMaster.STATE), SearchCondition.IN,
					new ArrayExpression(stateList.toArray()));
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 마스터 생성일
		if (predate.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalMaster.class, WTAttributeNameIfc.CREATE_STAMP_NAME,
					SearchCondition.GREATER_THAN, DateUtil.convertStartDate(predate));
			qs.appendWhere(sc, new int[] { idx0 });
		}
		if (postdate.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalMaster.class, WTAttributeNameIfc.CREATE_STAMP_NAME,
					SearchCondition.LESS_THAN, DateUtil.convertEndDate(postdate));
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 마스터 완료일
		if (predate_completeDate.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.COMPLETE_DATE, SearchCondition.GREATER_THAN,
					DateUtil.convertStartDate(predate_completeDate));
			qs.appendWhere(sc, new int[] { idx0 });
		}
		if (postdate_completeDate.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.COMPLETE_DATE, SearchCondition.LESS_THAN,
					DateUtil.convertEndDate(postdate_completeDate));
			qs.appendWhere(sc, new int[] { idx0 });
		}

		qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class, ApprovalLine.START_DATE), true),
				new int[] { idx2 });
		return qs;

	}

	/**
	 * 
	 * @desc : Role 별 리스트
	 * @author : tsuam
	 * @date : 2019. 7. 19.
	 * @method : getApprovalRoleLine
	 * @return : void
	 * @param master
	 * @param role
	 * @throws Exception
	 */
	public List<ApprovalLine> getApprovalRoleLine(ApprovalMaster master, String role, String state, boolean isLast)
			throws Exception {

		state = StringUtil.checkNull(state);

		List<ApprovalLine> list = new ArrayList<ApprovalLine>();
		QuerySpec qs = new QuerySpec();

		int idx0 = qs.addClassList(ApprovalMaster.class, false);
		int idx1 = qs.addClassList(ApprovalLine.class, true);

		SearchCondition outerJoinSc = new SearchCondition(ApprovalMaster.class, "thePersistInfo.theObjectIdentifier.id",
				ApprovalLine.class, "masterReference.key.id");
		outerJoinSc.setOuterJoin(SearchCondition.NO_OUTER_JOIN);
		qs.appendWhere(outerJoinSc, new int[] { idx0, idx1 });

		qs.appendAnd();
		SearchCondition sc = new SearchCondition(ApprovalMaster.class, WTAttributeNameIfc.ID_NAME,
				SearchCondition.EQUAL, CommonUtil.getOIDLongValue(master));
		qs.appendWhere(sc, new int[] { idx0 });

		if (role.length() > 0) {
			qs.appendAnd();
			sc = new SearchCondition(ApprovalLine.class, ApprovalLine.ROLE, SearchCondition.EQUAL, role);
			qs.appendWhere(sc, new int[] { idx1 });
		}

		if (state.length() > 0) {
			qs.appendAnd();
			sc = new SearchCondition(ApprovalLine.class, ApprovalLine.STATE, SearchCondition.EQUAL, state);
			qs.appendWhere(sc, new int[] { idx1 });
		}

		if (isLast) {
			qs.appendAnd();
			sc = new SearchCondition(ApprovalLine.class, ApprovalLine.LAST, SearchCondition.IS_TRUE);
			qs.appendWhere(sc, new int[] { idx1 });
		}

		qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class, ApprovalLine.SEQ), false),
				new int[] { idx1 });

		LOGGER.info("getApprovalRoleLine :" + qs);
		QueryResult rt = PersistenceHelper.manager.find(qs);

		while (rt.hasMoreElements()) {
			Object[] obj = (Object[]) rt.nextElement();

			ApprovalLine line = (ApprovalLine) obj[0];
			// ApprovalLineData data = new ApprovalLineData(line);
			list.add(line);
		}

		return list;

	}

	/**
	 * 
	 * @desc : Role 별 첫번째 Line
	 * @author : tsuam
	 * @date : 2019. 7. 19.
	 * @method : getApprovalRoleFirst
	 * @return : ApprovalLine
	 * @param master
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public ApprovalLine getApprovalRoleFirst(ApprovalMaster master, String role, String state, boolean isLast)
			throws Exception {

		List<ApprovalListData> list = new ArrayList<ApprovalListData>();
		QuerySpec qs = new QuerySpec();

		qs.setAdvancedQueryEnabled(true);
		int idx0 = qs.addClassList(ApprovalMaster.class, false);
		int idx1 = qs.addClassList(ApprovalLine.class, true);
		qs.appendRowNumCondition(2);

		qs.appendAnd();
		SearchCondition outerJoinSc = new SearchCondition(ApprovalMaster.class, "thePersistInfo.theObjectIdentifier.id",
				ApprovalLine.class, "masterReference.key.id");
		outerJoinSc.setOuterJoin(SearchCondition.NO_OUTER_JOIN);
		qs.appendWhere(outerJoinSc, new int[] { idx0, idx1 });

		qs.appendAnd();
		SearchCondition sc = new SearchCondition(ApprovalMaster.class, WTAttributeNameIfc.ID_NAME,
				SearchCondition.EQUAL, CommonUtil.getOIDLongValue(master));
		qs.appendWhere(sc, new int[] { idx0 });

		qs.appendAnd();
		sc = new SearchCondition(ApprovalLine.class, ApprovalLine.ROLE, SearchCondition.EQUAL, role);
		qs.appendWhere(sc, new int[] { idx1 });

		if (state.length() > 0) {
			qs.appendAnd();
			sc = new SearchCondition(ApprovalLine.class, ApprovalLine.STATE, SearchCondition.EQUAL, state);
			qs.appendWhere(sc, new int[] { idx1 });
		}

		if (isLast) {
			qs.appendAnd();
			sc = new SearchCondition(ApprovalLine.class, ApprovalLine.LAST, SearchCondition.IS_TRUE);
			qs.appendWhere(sc, new int[] { idx1 });
		}

		qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class, ApprovalLine.SEQ), false),
				new int[] { idx1 });

		LOGGER.info(qs.toString());

		QueryResult rt = PersistenceHelper.manager.find(qs);
		ApprovalLine line = null;
		if (rt.hasMoreElements()) {
			Object[] obj = (Object[]) rt.nextElement();
			line = (ApprovalLine) obj[0];
		}

		return line;
	}

	/**
	 * 
	 * @desc : Object 별 결재 이력
	 * @author : tsuam
	 * @date : 2019. 7. 23.
	 * @method : getApproveHistory
	 * @return : List<ApprovalLineData>
	 * @param master
	 * @return
	 * @throws Exception
	 */
	public List<ApprovalLineData> getApproveHistory(Persistable obj) throws Exception {

		return getApproveHistory(CommonUtil.getOIDString(obj));
	}

	/**
	 * 
	 * @desc : Object 별 결재 이력
	 * @author : tsuam
	 * @date : 2019. 7. 22.
	 * @method : getObjectApprovalLine
	 * @return : List<ApprovalData>
	 * @param oid : ApprovalMaster master
	 * @return
	 */
	public List<ApprovalLineData> getApproveHistory(String oid) throws Exception {

		List<ApprovalLineData> list = new ArrayList<ApprovalLineData>();
		Persistable per = CommonUtil.getObject(oid);
		ApprovalMaster master = getApprovalMaster(per);
		if (master != null) {
			if(per instanceof EChangeOrder2) {
				list = getApprovalLastLineECO(oid);
			} else {
				list = getApprovalLastLine(master, true);
			}
		} else {
			// per일 때 multi approval 있을 경우 보여주기
			if (!(per instanceof MultiApproval)) {
				if(per instanceof EPMDocument) {
					EPMDocument epm = (EPMDocument)per;
					Persistable newPer = null;
					if("CADDRAWING".equals(epm.getDocType().toString())) {
						EPMDocument epm3D = EpmHelper.manager.get2DTo3DEPM(epm);
						newPer = PartHelper.manager.getWTPart(epm3D);
					}else {
						newPer = PartHelper.manager.getWTPart(epm);
					}
					
					if(newPer!=null) per = newPer;
				}
				
				MultiApproval multi = MultiApprovalHelper.manager.getMultiApproval(per);
				if (multi != null) {
					master = getApprovalMaster(multi);

					if (master != null) {
						list = getApprovalLastLine(master, true);
					}
				}
			}
		}

		/*
		 * 
		 * QuerySpec qs = new QuerySpec();
		 * 
		 * int idx0 = qs.addClassList(ApprovalMaster.class, false); int idx1 =
		 * qs.addClassList(ApprovalObjectLink.class, false); int idx2 =
		 * qs.addClassList(ApprovalLine.class, true);
		 * 
		 * getApprovalJoinQuery(qs); qs.appendAnd(); SearchCondition sc = new
		 * SearchCondition(ApprovalObjectLink.class,
		 * WTAttributeNameIfc.ROLEA_OBJECT_ID,SearchCondition.EQUAL,CommonUtil.
		 * getOIDLongValue(oid)); qs.appendWhere(sc, new int[] { idx1 });
		 * qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class,
		 * ApprovalLine.SEQ), false), new int[] { idx2 }); LOGGER.info(qs); QueryResult
		 * rt = PersistenceHelper.manager.find(qs);
		 * 
		 * while(rt.hasMoreElements()) { Object[] objects = (Object[]) rt.nextElement();
		 * ApprovalLine line = (ApprovalLine)objects[0]; ApprovalLineData data = new
		 * ApprovalLineData(line); data.peopleCall(); list.add(data); }
		 */
		return list;
	}

	/**
	 * 
	 * @desc : 최신 결재 라인 전체
	 * @author : tsuam
	 * @date : 2019. 7. 29.
	 * @method : getApprovalLastLineAll
	 * @return : List<ApprovalLine>
	 * @param master
	 * @return
	 * @throws Exception
	 */
	public List<ApprovalLine> getApprovalLastLineAll(ApprovalMaster master) throws Exception {

		List<ApprovalLine> list = new ArrayList<ApprovalLine>();
		QuerySpec qs = new QuerySpec();

		int idx = qs.addClassList(ApprovalLine.class, true);

		SearchCondition sc = new SearchCondition(ApprovalLine.class,
				ApprovalLine.MASTER_REFERENCE + "." + WTAttributeNameIfc.REF_OBJECT_ID, SearchCondition.EQUAL,
				CommonUtil.getOIDLongValue(master));
		qs.appendWhere(sc, new int[] { idx });

		// 최신
		qs.appendAnd();
		sc = new SearchCondition(ApprovalLine.class, ApprovalLine.LAST, SearchCondition.IS_TRUE);
		qs.appendWhere(sc, new int[] { idx });

		qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class, ApprovalLine.SEQ), false),
				new int[] { idx });
		LOGGER.info(qs.toString());
		QueryResult rt = PersistenceHelper.manager.find(qs);

		while (rt.hasMoreElements()) {
			Object[] objects = (Object[]) rt.nextElement();
			ApprovalLine line = (ApprovalLine) objects[0];
			list.add(line);
		}

		return list;
	}

	/**
	 * 
	 * @desc : 최신 결재 라인 (기안자 제외)
	 * @author : tsuam
	 * @date : 2019. 7. 23.
	 * @method : getApprovalLastLine
	 * @return : List<ApprovalLineData>
	 * @param master :ApprovalMaster
	 * @return
	 * @throws Exception
	 */
	public List<ApprovalLineData> getApprovalLastLine(ApprovalMaster master, boolean isView) throws Exception {
		return getApprovalLastLine(CommonUtil.getOIDString(master), isView, ApprovalUtil.ROLE_ALL);
	}

	public List<ApprovalLineData> getApprovalLastLine(ApprovalMaster master, boolean isView, String searchRole)
			throws Exception {
		return getApprovalLastLine(CommonUtil.getOIDString(master), isView, searchRole);
	}

	/**
	 * 
	 * @desc : 최신 결재 라인 (기안자 제외) - 편집 및
	 * @author : tsuam
	 * @date : 2019. 7. 23.
	 * @method : getApprovalLine
	 * @return : List<ApprovalLineData>
	 * @param oid : ApprovalMaster Oid;
	 * @return
	 * @throws Exception
	 */
	public List<ApprovalLineData> getApprovalLastLine(String oid, boolean isView, String searchRoleType)
			throws Exception {

		List<ApprovalLineData> list = new ArrayList<ApprovalLineData>();

		QuerySpec qs = new QuerySpec();
		String searchRole = StringUtil.checkNull(searchRoleType);

		int idx = qs.addClassList(ApprovalLine.class, true);

		SearchCondition sc = new SearchCondition(ApprovalLine.class,
				ApprovalLine.MASTER_REFERENCE + "." + WTAttributeNameIfc.REF_OBJECT_ID, SearchCondition.EQUAL,
				CommonUtil.getOIDLongValue(oid));
		qs.appendWhere(sc, new int[] { idx });

		if (!isView) { // 편집용인경우 최신 , 기안 제외
			// 최신
			qs.appendAnd();
			sc = new SearchCondition(ApprovalLine.class, ApprovalLine.LAST, SearchCondition.IS_TRUE);
			qs.appendWhere(sc, new int[] { idx });

			qs.appendAnd();
			sc = new SearchCondition(ApprovalLine.class, ApprovalLine.ROLE, SearchCondition.NOT_EQUAL,
					ApprovalUtil.ROLE_DRAFT);
			qs.appendWhere(sc, new int[] { idx });

		}

		if (searchRole.length() > 0) {

			if (ApprovalUtil.ROLE_ALL.equals(searchRole)) {

			} else if (ApprovalUtil.ROLE_EXCEPT_RECEIVE.equals(searchRole)) {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				sc = new SearchCondition(ApprovalLine.class, ApprovalLine.ROLE, SearchCondition.NOT_EQUAL,
						ApprovalUtil.ROLE_RECEIVE);
				qs.appendWhere(sc, new int[] { idx });
			} else {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				sc = new SearchCondition(ApprovalLine.class, ApprovalLine.ROLE, SearchCondition.EQUAL, searchRole);
				qs.appendWhere(sc, new int[] { idx });
			}

		}

		qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class, ApprovalLine.VER), false),
				new int[] { idx });
		qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class, ApprovalLine.SEQ), false),
				new int[] { idx });
		qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class, "thePersistInfo.createStamp"), false),
				new int[] { idx });
		LOGGER.info(qs.toString());
		
		QueryResult rt = PersistenceHelper.manager.find(qs);

		while (rt.hasMoreElements()) {
			Object[] objects = (Object[]) rt.nextElement();
			ApprovalLine line = (ApprovalLine) objects[0];
			ApprovalLineData data = new ApprovalLineData(line);
			data.peopleCall();
			list.add(data);
		}
		
		//최종 승인자 체크
		Map<String, List<ApprovalLineData>> hashMap = new HashMap<>();
		for(ApprovalLineData data : list) {
			List<ApprovalLineData> tempList = hashMap.getOrDefault(data.getRoleType(), new ArrayList<>()); 
			tempList.add(data);
			
			hashMap.put(data.getRoleType(), tempList);
		}
		for(Entry<String, List<ApprovalLineData>> entry : hashMap.entrySet()) {
			
			int maxSeq = entry.getValue().stream().max(Comparator.comparing(ApprovalLineData::getSeq)).get().getSeq();
			List<ApprovalLineData> maxSeqLines = entry.getValue().stream().filter(obj -> obj.getSeq() == maxSeq).collect(Collectors.toList());
			
			if(maxSeqLines.size() > 0) {
				maxSeqLines.stream().forEach(obj -> obj.setLastSeqToSameRole(true));
			}
			
		}

		return list;
	}
	
	
	//TODO
	public List<ApprovalLineData> getApprovalLastLineECO(String oid)
			throws Exception {
		
		List<ApprovalLineData> list = new ArrayList<ApprovalLineData>();
		
		QuerySpec qs = new QuerySpec();
		
		int alIdx = qs.addClassList(ApprovalLine.class, true);
		int aoIdx = qs.addClassList(ApprovalObjectLink.class, false);
		//ApprovalObjectLink.class, "roleBObjectRef.key.id"
		
//		SearchCondition outerJoinSc = new SearchCondition(ApprovalLine.class, "masterReference.key.id",
//				ApprovalObjectLink.class, "roleBObjectRef.key.id");
//		outerJoinSc.setOuterJoin(SearchCondition.NO_OUTER_JOIN);
//		qs.appendWhere(outerJoinSc, new int[] { aoIdx, alIdx });
		
		ClassAttribute alAttr = new ClassAttribute(ApprovalLine.class, "masterReference.key.id");
		ClassAttribute aoAttr = new ClassAttribute(ApprovalObjectLink.class, "roleBObjectRef.key.id");
		
		SearchCondition sc = new SearchCondition(alAttr, "=", aoAttr);
		qs.appendWhere(sc, new int[]{alIdx, aoIdx});
		
		qs.appendAnd();
		
		sc = new SearchCondition(ApprovalObjectLink.class, "roleAObjectRef.key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(oid));
		qs.appendWhere(sc, new int[] { aoIdx });
		
//		qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class, ApprovalLine.VER), false),
//				new int[] { alIdx });
//		qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class, ApprovalLine.SEQ), false),
//				new int[] { alIdx });
		qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class, "thePersistInfo.createStamp"), false),
				new int[] { alIdx });
		
		QueryResult rt = PersistenceHelper.manager.find(qs);
		
		System.out.println("############################ 쿼리 : " + qs);

		while (rt.hasMoreElements()) {
			Object[] objects = (Object[]) rt.nextElement();
			ApprovalLine line = (ApprovalLine) objects[0];
			ApprovalLineData data = new ApprovalLineData(line);
			System.out.println("############################ data : " + data);
			data.peopleCall();
			list.add(data);
		}
		
		return list;
	}

	public List<ApprovalLine> getApprovalOnGoingLine(ApprovalMaster master) throws Exception {

		return getApprovalOnGoingLine(CommonUtil.getOIDString(master));
	}

	/**
	 * 
	 * @desc : 결재 진행중인 결재 라인
	 * @author : tsuam
	 * @date : 2019. 7. 31.
	 * @method : getApprovalOnGoingLine
	 * @return : List<ApprovalLineData>
	 * @param oid : master
	 * @return
	 * @throws Exception
	 */
	public List<ApprovalLine> getApprovalOnGoingLine(String oid) throws Exception {

		List<ApprovalLine> list = new ArrayList<ApprovalLine>();
		QuerySpec qs = new QuerySpec();

		int idx = qs.addClassList(ApprovalLine.class, true);

		SearchCondition sc = new SearchCondition(ApprovalLine.class,
				ApprovalLine.MASTER_REFERENCE + "." + WTAttributeNameIfc.REF_OBJECT_ID, SearchCondition.EQUAL,
				CommonUtil.getOIDLongValue(oid));
		qs.appendWhere(sc, new int[] { idx });

		qs.appendAnd();
		sc = new SearchCondition(ApprovalLine.class, ApprovalLine.LAST, SearchCondition.IS_TRUE);
		qs.appendWhere(sc, new int[] { idx });

		qs.appendAnd();

		sc = new SearchCondition(ApprovalLine.class, ApprovalLine.STATE, SearchCondition.EQUAL,
				ApprovalUtil.STATE_LINE_ONGOING);
		qs.appendWhere(sc, new int[] { idx });

		qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class, ApprovalLine.VER), false),
				new int[] { idx });
		qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class, ApprovalLine.SEQ), false),
				new int[] { idx });
		LOGGER.info(qs.toString());
		QueryResult rt = PersistenceHelper.manager.find(qs);

		while (rt.hasMoreElements()) {
			Object[] objects = (Object[]) rt.nextElement();
			ApprovalLine line = (ApprovalLine) objects[0];

			list.add(line);
		}

		return list;
	}

	/**
	 * 
	 * @desc : 결재 기본 데이터
	 * @author : tsuam
	 * @date : 2019. 7. 22.
	 * @method : getApprovalData
	 * @param oid : line
	 * @return : void
	 * 
	 */
	public ApprovalData getApprovalData(ApprovalLine line) throws Exception {

		return getApprovalData(CommonUtil.getOIDString(line));
	}

	/**
	 * 
	 * @desc : 결재 기본 데이터
	 * @author : tsuam
	 * @date : 2019. 7. 22.
	 * @method : getApprovalData
	 * @param oid : line
	 * @return : void
	 * 
	 */
	public ApprovalData getApprovalData(String oid) throws Exception {

		ApprovalLine line = (ApprovalLine) CommonUtil.getObject(oid);
		ApprovalLineData lineData = new ApprovalLineData(line);
		ApprovalMaster master = line.getMaster();
		Persistable obj = getApprovalObject(master);

		boolean isLastApproval = isLastApproval(master);

		Map<String, String> objMap = ApprovalUtil.getApprovalObjectInfo(obj);
		String objectType = objMap.get("objectType");
		String number = objMap.get("number");
		String name = objMap.get("name");
		String title = objMap.get("title");
		String objectOid = objMap.get("objectOid");
		String supplierName = objMap.get("supplierName");
		String supplierId = objMap.get("supplierId");
		String orderType = objMap.get("orderType");
		String creator = objMap.get("creator");
		ApprovalData data = new ApprovalData();

		data.setObjectType(objectType); // 구분
		data.setNumber(number);
		data.setCreatorName(creator);// (master.getOwner().getFullName());
		data.setName(name);
		data.setTitle(title);
		data.setObjectOid(objectOid);
		data.setStartDate(lineData.getStartDate());// 도착일
		data.setStartDateFormat(lineData.getStartDateFormat());
		data.setRoleName(lineData.getRoleName()); // 업무명
		data.setRoleType(lineData.getRoleType());
		data.setState(master.getState().toString()); // 마스터 상태
		data.setStateName(master.getState().getDisplay(MessageUtil.getLocale())); // 마스터 상태
		data.setOwnerFullName(lineData.getOwnerFullName()); // 작업자
		data.setDescription(lineData.getDescription());
		data.setLastApproval(isLastApproval);
		data.setSupplierId(supplierId);
		data.setSupplierName(supplierName);
		data.setOrderType(orderType);
		if (line.getMaster().getObjectType().equals("ECA")) {
			EChangeActivity eca = (EChangeActivity) CommonUtil.getObject(objMap.get("objectOid"));
			Persistable per = eca.getOrder();
			data.setChangeOid(CommonUtil.getOIDString(per));
		} else {
			data.setChangeOid("");
		}

		return data;

	}

	/**
	 * 
	 * @desc : 결재 대상 객체
	 * @author : tsuam
	 * @date : 2019. 7. 23.
	 * @method : getApprovalObject
	 * @return : Persistable
	 * @param master
	 * @return
	 * @throws Exception
	 */
	public Persistable getApprovalObject(ApprovalMaster master) throws Exception {

		QueryResult qr = PersistenceHelper.manager.navigate(master, "obj", ApprovalObjectLink.class);

		while (qr != null && qr.hasMoreElements()) {
			return (Persistable) qr.nextElement();
		}

		return null;

	}

	/**
	 * 
	 * @desc : 결재 대상 Link
	 * @author : tsuam
	 * @date : 2019. 7. 23.
	 * @method : getApprovalObjectLink
	 * @return : ApprovalObjectLink
	 * @param master
	 * @return
	 * @throws Exception
	 */
	public ApprovalObjectLink getApprovalObjectLink(ApprovalMaster master) throws Exception {

		QueryResult qr = PersistenceHelper.manager.navigate(master, "obj", ApprovalObjectLink.class, false);

		while (qr != null && qr.hasMoreElements()) {
			return (ApprovalObjectLink) qr.nextElement();
		}

		return null;

	}

	/**
	 * 
	 * @desc : 결재 대상의 ApprovalMaster
	 * @author : tsuam
	 * @date : 2019. 7. 23.
	 * @method : getApprovalMaster
	 * @return : ApprovalMaster
	 * @param per
	 * @return
	 * @throws Exception
	 */
	public ApprovalMaster getApprovalMaster(Persistable per) throws Exception {

		List<Long> list = new ArrayList<Long>();
		if (per instanceof Iterated) {
			QueryResult qr = VersionControlHelper.service.iterationsOf((Iterated) per);
			while (qr.hasMoreElements()) {
				Iterated it = (Iterated) qr.nextElement();
				list.add(CommonUtil.getOIDLongValue(it));
			}
		} else {
			list.add(CommonUtil.getOIDLongValue(per));
		}

		QuerySpec qs = new QuerySpec();
		int jj = qs.addClassList(ApprovalMaster.class, true);
		int ii = qs.addClassList(ApprovalObjectLink.class, true);

		qs.appendWhere(new SearchCondition(ApprovalObjectLink.class, "roleBObjectRef.key.id", ApprovalMaster.class,
				"thePersistInfo.theObjectIdentifier.id"), new int[] { ii, jj });

		qs.appendAnd();
		SearchCondition sc = new SearchCondition(new ClassAttribute(ApprovalObjectLink.class, "roleAObjectRef.key.id"),
				SearchCondition.IN, new ArrayExpression(list.toArray()));
		qs.appendWhere(sc, new int[] { ii });

		QueryResult qr = PersistenceHelper.manager.find(qs);

		if (qr.hasMoreElements()) {
			Object[] o = (Object[]) qr.nextElement();
			return (ApprovalMaster) o[0];
		}

		return null;

	}
	
	public ApprovalMaster getApprovalStateMaster(Persistable per, String appState) throws Exception {

		List<Long> list = new ArrayList<Long>();
		if (per instanceof Iterated) {
			QueryResult qr = VersionControlHelper.service.iterationsOf((Iterated) per);
			while (qr.hasMoreElements()) {
				Iterated it = (Iterated) qr.nextElement();
				list.add(CommonUtil.getOIDLongValue(it));
			}
		} else {
			list.add(CommonUtil.getOIDLongValue(per));
		}

		QuerySpec qs = new QuerySpec();
		int jj = qs.addClassList(ApprovalMaster.class, true);
		int ii = qs.addClassList(ApprovalObjectLink.class, true);

		qs.appendWhere(new SearchCondition(ApprovalObjectLink.class, "roleBObjectRef.key.id", ApprovalMaster.class,
				"thePersistInfo.theObjectIdentifier.id"), new int[] { ii, jj });

		qs.appendAnd();
		SearchCondition sc = new SearchCondition(new ClassAttribute(ApprovalObjectLink.class, "roleAObjectRef.key.id"),
				SearchCondition.IN, new ArrayExpression(list.toArray()));
		qs.appendWhere(sc, new int[] { ii });
		
		qs.appendAnd();
		sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.STATE, SearchCondition.EQUAL,
				appState);
		qs.appendWhere(sc, new int[] { jj });

		QueryResult qr = PersistenceHelper.manager.find(qs);

		if (qr.hasMoreElements()) {
			Object[] o = (Object[]) qr.nextElement();
			return (ApprovalMaster) o[0];
		}

		return null;

	}

	public boolean isDiscussOnGoing(ApprovalLine line) throws Exception {
		ApprovalMaster master = line.getMaster();
		String role = line.getRole().toString();
		String state = ApprovalUtil.STATE_LINE_ONGOING;
		boolean isLast = true;
		List<ApprovalLine> list = getApprovalRoleLine(master, role, state, isLast);
		return (list.size() > 0);

	}

	public boolean isApproveStanding(ApprovalLine line) throws Exception {

		ApprovalMaster master = line.getMaster();
		String role = line.getRole().toString();
		String state = ApprovalUtil.STATE_LINE_STANDING;
		boolean isLast = true;

		List<ApprovalLine> list = getApprovalRoleLine(master, role, state, isLast);

		return (list.size() > 0);

	}

	public List<ApprovalLine> getNextApproval(ApprovalMaster master, int seq) throws Exception {

		LOGGER.info(":::::::::::::::::: getNextApproval ::::::::::::::::::");
		List<ApprovalLine> list = new ArrayList<ApprovalLine>();
		QuerySpec qs = new QuerySpec();

		qs.setAdvancedQueryEnabled(true);
		int idx0 = qs.addClassList(ApprovalMaster.class, false);
		int idx1 = qs.addClassList(ApprovalLine.class, true);

		SearchCondition outerJoinSc = new SearchCondition(ApprovalMaster.class, "thePersistInfo.theObjectIdentifier.id",
				ApprovalLine.class, "masterReference.key.id");
		outerJoinSc.setOuterJoin(SearchCondition.NO_OUTER_JOIN);
		qs.appendWhere(outerJoinSc, new int[] { idx0, idx1 });

		qs.appendAnd();
		SearchCondition sc = new SearchCondition(ApprovalMaster.class, WTAttributeNameIfc.ID_NAME,
				SearchCondition.EQUAL, CommonUtil.getOIDLongValue(master));
		qs.appendWhere(sc, new int[] { idx0 });

		// SEQ
		qs.appendAnd();
		sc = new SearchCondition(ApprovalLine.class, ApprovalLine.SEQ, SearchCondition.EQUAL, seq);
		qs.appendWhere(sc, new int[] { idx1 });

		// 대기상태
		qs.appendAnd();
		sc = new SearchCondition(ApprovalLine.class, ApprovalLine.STATE, SearchCondition.EQUAL,
				ApprovalUtil.STATE_LINE_STANDING);
		qs.appendWhere(sc, new int[] { idx1 });

		// 최신
		qs.appendAnd();
		sc = new SearchCondition(ApprovalLine.class, ApprovalLine.LAST, SearchCondition.IS_TRUE);
		qs.appendWhere(sc, new int[] { idx1 });

		qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class, ApprovalLine.SEQ), false),
				new int[] { idx1 });

		LOGGER.info("getNextApproval tsuam : " + qs);

		QueryResult rt = PersistenceHelper.manager.find(qs);
		ApprovalLine line = null;
		while (rt.hasMoreElements()) {
			Object[] obj = (Object[]) rt.nextElement();
			line = (ApprovalLine) obj[0];
			list.add(line);
		}

		return list;
	}

	/**
	 * 
	 * @desc :유저별 결재 Template
	 * @author : tsuam
	 * @date : 2019. 8. 5.
	 * @method : getAppLineTemplateList
	 * @return : void
	 * @param oid :people Oid
	 * @throws Exception
	 */
	public List<PeopleData> getDetailTemplateList(Map<String, Object> reqMap) throws Exception {
		List<PeopleData> list = new ArrayList<PeopleData>();
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));

		QuerySpec qs = new QuerySpec();
		SearchCondition sc = null;

		int idx = qs.addClassList(ApprovalLineTemplate.class, true);

		sc = new SearchCondition(ApprovalLineTemplate.class, WTAttributeNameIfc.ID_NAME, SearchCondition.EQUAL,
				CommonUtil.getOIDLongValue(oid));
		qs.appendWhere(sc, new int[] { idx });

		qs.appendOrderBy(
				new OrderBy(new ClassAttribute(ApprovalLineTemplate.class, WTAttributeNameIfc.CREATE_STAMP_NAME),
						false),
				new int[] { idx });

		LOGGER.info(qs.toString());

		QueryResult qr = PersistenceHelper.manager.find(qs);

		while (qr.hasMoreElements()) {

			Object[] obj = (Object[]) qr.nextElement();
			ApprovalLineTemplate temp = (ApprovalLineTemplate) obj[0];
			ArrayList<String> discussList = temp.getDiscussList();
			ArrayList<String> approveList = temp.getApproveList();
			ArrayList<String> receiveList = temp.getReceiveList();

			list = ApprovalUtil.getListOidToList(discussList, list, ApprovalUtil.ROLE_DISCUSS);
			list = ApprovalUtil.getListOidToList(approveList, list, ApprovalUtil.ROLE_APPROVE);
			list = ApprovalUtil.getListOidToList(receiveList, list, ApprovalUtil.ROLE_RECEIVE);
		}

		return list;

	}

	public List<ApprovalLineTemplateData> getApprovalLineTemplateList(Map<String, Object> reqMap) throws Exception {

		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		List<ApprovalLineTemplateData> list = new ArrayList<ApprovalLineTemplateData>();
		WTUser user = null;
		if (oid.length() > 0) {
			People pp = (People) CommonUtil.getObject(oid);
			user = pp.getUser();
		} else {
			user = (WTUser) SessionHelper.getPrincipal();
		}

		QuerySpec qs = new QuerySpec();
		SearchCondition sc = null;

		int idx = qs.addClassList(ApprovalLineTemplate.class, true);

		sc = new SearchCondition(ApprovalLineTemplate.class, ApprovalLineTemplate.OWNER + "." + "key.id",
				SearchCondition.EQUAL, CommonUtil.getOIDLongValue(user));
		qs.appendWhere(sc, new int[] { idx });

		qs.appendOrderBy(
				new OrderBy(new ClassAttribute(ApprovalLineTemplate.class, WTAttributeNameIfc.CREATE_STAMP_NAME),
						false),
				new int[] { idx });

		LOGGER.info(qs.toString());

		QueryResult qr = PersistenceHelper.manager.find(qs);

		while (qr.hasMoreElements()) {

			Object[] obj = (Object[]) qr.nextElement();
			ApprovalLineTemplate template = (ApprovalLineTemplate) obj[0];
			ApprovalLineTemplateData data = new ApprovalLineTemplateData(template);
			list.add(data);
		}

		return list;

	}

	/**
	 * 
	 * @desc : 로그인 유저 및 최신 결재 라인
	 * @author : tsuam
	 * @date : 2019. 7. 19.
	 * @method : getOwerApprovalLine
	 * @return : void
	 * @param qs
	 * @param targetClass
	 * @param idx
	 * @throws Exception
	 */
	public void getOwnerApprovalLine(QuerySpec qs, int idx, boolean isLast) throws Exception {

		WTUser user = (WTUser) SessionHelper.getPrincipal();
		SearchCondition sc = null;
		/* 로그인 유저 */
		if (!CommonUtil.isAdmin()) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalLine.class, "owner.key.id", SearchCondition.EQUAL,
					CommonUtil.getOIDLongValue(user));
			qs.appendWhere(sc, new int[] { idx });
		}

		/* 최신 결재 라인 */
		if (isLast) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalLine.class, ApprovalLine.LAST, SearchCondition.IS_TRUE);
			qs.appendWhere(sc, new int[] { idx });
		}

	}

	/**
	 * 결재 라인 ApprovalMaster,ApprovalObjectLink,ApprovalLine Join
	 * 
	 * @desc :
	 * @author : tsuam
	 * @date : 2019. 7. 19.
	 * @method : getApprovalJonQuery
	 * @return : void
	 * @param qs
	 * @throws Exception
	 */
	public void getApprovalJoinQuery(QuerySpec qs) throws Exception {

		SearchCondition outerJoinSc = new SearchCondition(ApprovalMaster.class, "thePersistInfo.theObjectIdentifier.id",
				ApprovalObjectLink.class, "roleBObjectRef.key.id");
		outerJoinSc.setOuterJoin(SearchCondition.NO_OUTER_JOIN);
		qs.appendWhere(outerJoinSc, new int[] { 0, 1 });

		qs.appendAnd();
		outerJoinSc = new SearchCondition(ApprovalMaster.class, "thePersistInfo.theObjectIdentifier.id",
				ApprovalLine.class, "masterReference.key.id");
		outerJoinSc.setOuterJoin(SearchCondition.NO_OUTER_JOIN);
		qs.appendWhere(outerJoinSc, new int[] { 0, 2 });

	}

	/**
	 * 
	 * @desc : 진행중 단계 리스트
	 * @author : tsuam
	 * @date : 2019. 7. 29.
	 * @method : getProcessingList
	 * @return : void
	 * @param master
	 * @throws Exception
	 */
	private List<ApprovalLineData> getProcessStep(ApprovalMaster master) throws Exception {

		List<ApprovalLineData> list = new ArrayList<ApprovalLineData>();
		QuerySpec qs = new QuerySpec();

		int idx = qs.addClassList(ApprovalLine.class, true);
		SearchCondition sc = null;

		sc = new SearchCondition(ApprovalLine.class,
				ApprovalLine.MASTER_REFERENCE + "." + WTAttributeNameIfc.REF_OBJECT_ID, SearchCondition.EQUAL,
				CommonUtil.getOIDLongValue(master));
		qs.appendWhere(sc, new int[] { idx });

		qs.appendAnd();
		sc = new SearchCondition(ApprovalLine.class, ApprovalLine.LAST, SearchCondition.IS_TRUE);
		qs.appendWhere(sc, new int[] { idx });

		// 결재라인 상태
		qs.appendAnd();
		sc = new SearchCondition(ApprovalLine.class, ApprovalLine.STATE, SearchCondition.EQUAL, "ONGOING");
		qs.appendWhere(sc, new int[] { idx });

		qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class, ApprovalLine.START_DATE), true),
				new int[] { idx });

		QueryResult qr = PersistenceHelper.manager.find(qs);

		while (qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			ApprovalLine line = (ApprovalLine) obj[0];
			ApprovalLineData data = new ApprovalLineData(line);
			list.add(data);
		}

		return list;
	}

	private String getProcessStepData(ApprovalMaster master) throws Exception {

		List<ApprovalLineData> list = getProcessStep(master);
		String processStepData = "";

		for (ApprovalLineData data : list) {
			processStepData = processStepData + data.getOwnerFullName() + ",";
		}
		if (processStepData.length() > 0) {
			processStepData = processStepData.substring(0, processStepData.length() - 1);
		}

		return processStepData;
	}

	/**
	 * 
	 * @desc : 업무 이력
	 * @author : tsuam
	 * @date : 2019. 10. 31.
	 * @method : getWFHistoryList
	 * @return : QueryResult
	 * @param persistable
	 * @return
	 * @throws Exception
	 */
	public List<WFHistory> getWFHistoryList(Persistable persistable) throws Exception {

		QueryResult result = new QueryResult();
		List<WFHistory> list = new ArrayList<WFHistory>();
		try {
			long lpersistOid = 0;
			String versionStr = "";
			if (persistable instanceof RevisionControlled) {
				RevisionControlled rc = (RevisionControlled) persistable;

				Versioned vc = (Versioned) rc;
				versionStr = VersionControlHelper.getVersionIdentifier(vc).getValue();

				Master master = (Master) rc.getMaster();
				lpersistOid = CommonUtil.getOIDLongValue(master);
			} else {
				lpersistOid = CommonUtil.getOIDLongValue(persistable);
			}

			QuerySpec query = new QuerySpec();
			int historyIndex = query.appendClassList(WFHistory.class, true);

			query.appendOpenParen();
			SearchCondition sc = new SearchCondition(WFHistory.class, WFHistory.WF_OBJECT_REFERENCE + ".key.id",
					SearchCondition.EQUAL, lpersistOid);
			query.appendWhere(sc, new int[] { historyIndex });

			if (!versionStr.equals("")) {
				query.appendAnd();
				sc = new SearchCondition(WFHistory.class, WFHistory.OBJECT_VERSION, SearchCondition.EQUAL, versionStr);
				query.appendWhere(sc, new int[] { historyIndex });
			}
			query.appendCloseParen();

			SearchUtil.setOrderBy(query, WFHistory.class, historyIndex, WTAttributeNameIfc.CREATE_STAMP_NAME,
					"createDate", true);

			result = PersistenceHelper.manager.find(query);

			while (result.hasMoreElements()) {
				Object[] obj = (Object[]) result.nextElement();
				WFHistory history = (WFHistory) obj[0];

				list.add(history);
			}

			return list;
		} catch (Exception e) {
			throw new Exception(e.toString());
		}

	}

	/**
	 * 
	 * @desc : 업무 이력 리스트 Data
	 * @author : tsuam
	 * @date : 2019. 10. 31.
	 * @method : getWFHistoryDataList
	 * @return : List<WFHistoryData>
	 * @param persistable
	 * @return
	 * @throws Exception
	 */
	public List<WFHistoryData> getWFHistoryDataList(Persistable persistable) throws Exception {
		List<WFHistoryData> listData = new ArrayList<WFHistoryData>();
		List<WFHistory> list = getWFHistoryList(persistable);

		for (WFHistory history : list) {
			WFHistoryData data = new WFHistoryData(history);

			listData.add(data);
		}
		return listData;
	}

	/**
	 * 
	 * @desc : 대상 별 업무 이력 데이터
	 * @author : tsuam
	 * @date : 2019. 11. 1.
	 * @method : getWFHistoryDataList
	 * @return : List<WFHistoryData>
	 * @param oid
	 * @return
	 * @throws Exception
	 */
	public List<WFHistoryData> getWFHistoryDataList(String oid) throws Exception {

		Persistable pp = ApprovalUtil.getWFHistoryTarget(oid);

		List<WFHistoryData> listData = new ArrayList<WFHistoryData>();
		List<WFHistory> list = getWFHistoryList(pp);

		for (WFHistory history : list) {
			WFHistoryData data = new WFHistoryData(history);

			listData.add(data);
		}
		return listData;
	}

	/**
	 * 
	 * @desc : 최종 결재 존재 유무
	 * @author : plmadmin
	 * @date : 2020. 2. 18.
	 * @method : isLastApproval
	 * @return : boolean
	 * @param master
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	public boolean isLastApproval(ApprovalMaster master) throws Exception {
		boolean isLastApproval = true;
		LOGGER.info(":::::::::::::::::: getNextApproval ::::::::::::::::::");
		List<ApprovalLine> list = new ArrayList<ApprovalLine>();
		QuerySpec qs = new QuerySpec();

		qs.setAdvancedQueryEnabled(true);
		int idx0 = qs.addClassList(ApprovalMaster.class, false);
		int idx1 = qs.addClassList(ApprovalLine.class, true);

		SearchCondition outerJoinSc = new SearchCondition(ApprovalMaster.class, "thePersistInfo.theObjectIdentifier.id",
				ApprovalLine.class, "masterReference.key.id");
		outerJoinSc.setOuterJoin(SearchCondition.NO_OUTER_JOIN);
		qs.appendWhere(outerJoinSc, new int[] { idx0, idx1 });

		qs.appendAnd();
		SearchCondition sc = new SearchCondition(ApprovalMaster.class, WTAttributeNameIfc.ID_NAME,
				SearchCondition.EQUAL, CommonUtil.getOIDLongValue(master));
		qs.appendWhere(sc, new int[] { idx0 });

		// 대기상태
		qs.appendAnd();
		sc = new SearchCondition(ApprovalLine.class, ApprovalLine.STATE, SearchCondition.EQUAL,
				ApprovalUtil.STATE_LINE_STANDING);
		qs.appendWhere(sc, new int[] { idx1 });

		// 승인자
		qs.appendAnd();
		sc = new SearchCondition(ApprovalLine.class, ApprovalLine.ROLE, SearchCondition.EQUAL,
				ApprovalUtil.ROLE_APPROVE);
		qs.appendWhere(sc, new int[] { idx1 });

		// 최신
		qs.appendAnd();
		sc = new SearchCondition(ApprovalLine.class, ApprovalLine.LAST, SearchCondition.IS_TRUE);
		qs.appendWhere(sc, new int[] { idx1 });

		qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class, ApprovalLine.SEQ), false),
				new int[] { idx1 });

		LOGGER.info("getNextApproval tsuam : " + qs);

		QueryResult rt = PersistenceHelper.manager.find(qs);
		if (rt.hasMoreElements()) {
			isLastApproval = false;
		}

		return isLastApproval;
	}

	public ApprovalLine getDelegatApprovalLine(ApprovalLine line) throws Exception {

		LOGGER.info(":::::::::::::::::: getDelegatApprovalLine ::::::::::::::::::");
		List<ApprovalLine> list = new ArrayList<ApprovalLine>();
		QuerySpec qs = new QuerySpec();

		qs.setAdvancedQueryEnabled(true);
		int idx0 = qs.addClassList(ApprovalMaster.class, false);
		int idx1 = qs.addClassList(ApprovalLine.class, true);

		SearchCondition outerJoinSc = new SearchCondition(ApprovalMaster.class, "thePersistInfo.theObjectIdentifier.id",
				ApprovalLine.class, "masterReference.key.id");
		outerJoinSc.setOuterJoin(SearchCondition.NO_OUTER_JOIN);
		qs.appendWhere(outerJoinSc, new int[] { idx0, idx1 });

		qs.appendAnd();
		SearchCondition sc = new SearchCondition(ApprovalMaster.class, WTAttributeNameIfc.ID_NAME,
				SearchCondition.EQUAL, CommonUtil.getOIDLongValue(line.getMaster()));
		qs.appendWhere(sc, new int[] { idx0 });

		// 진행중
		qs.appendAnd();
		sc = new SearchCondition(ApprovalLine.class, ApprovalLine.STATE, SearchCondition.EQUAL,
				ApprovalUtil.STATE_LINE_ONGOING);
		qs.appendWhere(sc, new int[] { idx1 });

		// 승인자
		qs.appendAnd();
		sc = new SearchCondition(ApprovalLine.class, ApprovalLine.SEQ, SearchCondition.EQUAL, line.getSeq());
		qs.appendWhere(sc, new int[] { idx1 });

		// 최신
		qs.appendAnd();
		sc = new SearchCondition(ApprovalLine.class, ApprovalLine.LAST, SearchCondition.IS_TRUE);
		qs.appendWhere(sc, new int[] { idx1 });

		// qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class,
		// ApprovalLine.SEQ), false), new int[] { idx1 });

		LOGGER.info("getNextApproval tsuam : " + qs);

		return null;
		// QueryResult rt = PersistenceHelper.manager.find(qs);
		// if(rt.hasMoreElements()){
		// ApprovalLine line= false;
		// }

		// return isLastApproval;
	}

	public static void main(String[] args) {

		try {
			String oid = "com.e3ps.approval.ApprovalMaster:365245";
			ApprovalMaster master = (ApprovalMaster) CommonUtil.getObject(oid);
			// ApprovalMaster master = (ApprovalMaster)CommonUtil.getObject(oid);
			QueryResult qr = PersistenceHelper.manager.navigate(master, ApprovalObjectLink.OBJ_ROLE,
					ApprovalObjectLink.class, false);

			while (qr != null && qr.hasMoreElements()) {
				Object obj = qr.nextElement();

				LOGGER.info(obj.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * 객체로부터 진행중인 결재가 있으면 가져옵니다.
	 * @author hckim
	 */
	public List<ApprovalLine> getOnGoingApprovalFromPersistable(Persistable per) throws Exception{
		
		List<ApprovalLine> returnList = new ArrayList<>();
		
		if(per == null) {
			return returnList;
		}
		Map<String, String> appObjHash = ApprovalUtil.getApprovalObjectInfo(per);
		if("".equals(appObjHash.get("objectType"))) {
			return returnList;
		}
		
		QuerySpec query = new QuerySpec();
		int idxAppMaster = query.addClassList(ApprovalMaster.class, false);
		int idxAppObjLink = query.addClassList(ApprovalObjectLink.class, false);
		int idxAppLine = query.addClassList(ApprovalLine.class, true);
		
		SearchCondition sc = null;
		
		if(query.getConditionCount() > 0) {
			query.appendAnd();
		}
		sc = new SearchCondition(
				new ClassAttribute(ApprovalObjectLink.class, ApprovalObjectLink.ROLE_BOBJECT_REF+".key.id"),
				SearchCondition.EQUAL,
				new ClassAttribute(ApprovalMaster.class, ApprovalMaster.PERSIST_INFO+".theObjectIdentifier.id")
				);
		query.appendWhere(sc, new int[] {idxAppObjLink, idxAppMaster});
		
		
		if(query.getConditionCount() > 0) {
			query.appendAnd();
		}
		sc = new SearchCondition(
				new ClassAttribute(ApprovalLine.class, ApprovalLine.MASTER_REFERENCE+".key.id"),
				SearchCondition.EQUAL,
				new ClassAttribute(ApprovalMaster.class, ApprovalMaster.PERSIST_INFO+".theObjectIdentifier.id")
				);
		query.appendWhere(sc, new int[] {idxAppLine, idxAppMaster});
		
		
		if(query.getConditionCount() > 0) {
			query.appendAnd();
		}
		sc = new SearchCondition(
				ApprovalObjectLink.class, ApprovalObjectLink.ROLE_AOBJECT_REF+".key.id",
				SearchCondition.EQUAL,
				CommonUtil.getOIDLongValue(per)
				);
		query.appendWhere(sc, new int[] {idxAppObjLink});
		
		
		if(query.getConditionCount() > 0) {
			query.appendAnd();
		}
		sc = new SearchCondition(
				ApprovalLine.class, ApprovalLine.ROLE,
				SearchCondition.NOT_EQUAL,
				ApprovalUtil.ROLE_DRAFT
				);
		query.appendWhere(sc, new int[] {idxAppLine});
		
		
		if(query.getConditionCount() > 0) {
			query.appendAnd();
		}
		sc = new SearchCondition(
				ApprovalLine.class, ApprovalLine.STATE,
				SearchCondition.EQUAL,
				ApprovalUtil.STATE_LINE_ONGOING
				);
		query.appendWhere(sc, new int[] {idxAppLine});
		
		
		QueryResult result = PersistenceHelper.manager.find(query);
		
		if(result.hasMoreElements()) {
			Object[] obj = (Object[])result.nextElement();
			ApprovalLine line = (ApprovalLine)obj[0];
			
			returnList.add(line);
		}
		
		return returnList;
	}
	
	public WTUser getLastApprover(Persistable per) throws Exception{
		
		LifeCycleManaged lm = (LifeCycleManaged)per;
		if(!"APPROVED".equals(lm.getLifeCycleState().toString()))return null;
		
		List<Long> list = new ArrayList<Long>();
		if (per instanceof Iterated) {
			QueryResult qr = VersionControlHelper.service.iterationsOf((Iterated) per);
			while (qr.hasMoreElements()) {
				Iterated it = (Iterated) qr.nextElement();
				list.add(CommonUtil.getOIDLongValue(it));
			}
		} else {
			list.add(CommonUtil.getOIDLongValue(per));
		}
		
		QuerySpec query = new QuerySpec();
		query.setAdvancedQueryEnabled(true);
		int idxAppLine = query.addClassList(ApprovalLine.class, true);
		SearchCondition sc = null;
		
		query.appendOpenParen();
		
		QuerySpec subQuery1 = new QuerySpec();
		subQuery1.setAdvancedQueryEnabled(true);
		int idxAppObjLink1 = subQuery1.addClassList(ApprovalObjectLink.class, false);
		subQuery1.appendSelect(new ClassAttribute(ApprovalObjectLink.class, WTAttributeNameIfc.ROLEB_OBJECT_ID), false);
		subQuery1.setDistinct(true);
		sc = new SearchCondition(new ClassAttribute(ApprovalObjectLink.class, WTAttributeNameIfc.ROLEA_OBJECT_ID), SearchCondition.IN, new ArrayExpression(list.toArray()));
		subQuery1.appendWhere(sc, new int[] {idxAppObjLink1});
		
		if(per instanceof EChangeOrder2){
			subQuery1.appendAnd();
			
			QuerySpec sub_subQuery1 = new QuerySpec();
			int sub_idxAppObjLink1 = sub_subQuery1.addClassList(ApprovalObjectLink.class, false);
			SQLFunction max = SQLFunction.newSQLFunction(SQLFunction.MAXIMUM, new ClassAttribute(ApprovalObjectLink.class, WTAttributeNameIfc.ID_NAME));
			sub_subQuery1.appendSelect(max, false);
			sc = new SearchCondition(new ClassAttribute(ApprovalObjectLink.class, WTAttributeNameIfc.ROLEA_OBJECT_ID), SearchCondition.IN, new ArrayExpression(list.toArray()));
			sub_subQuery1.appendWhere(sc, new int[] {sub_idxAppObjLink1});
			
			sc = new SearchCondition(new ClassAttribute(ApprovalObjectLink.class, WTAttributeNameIfc.ID_NAME), SearchCondition.EQUAL, new SubSelectExpression(sub_subQuery1));
			subQuery1.appendWhere(sc, new int[] {idxAppObjLink1});
		}
		
		sc = new SearchCondition(new ClassAttribute(ApprovalLine.class, ApprovalLine.MASTER_REFERENCE+".key.id"), SearchCondition.EQUAL, new SubSelectExpression(subQuery1));
		query.appendWhere(sc, new int[] {idxAppLine});
		
		query.appendOr();
		
		QuerySpec subQuery2 = new QuerySpec();
		subQuery2.setAdvancedQueryEnabled(true);
		int idxAppObjLink2 = subQuery2.addClassList(ApprovalObjectLink.class, false);
		int idxMultiAppObjLink = subQuery2.addClassList(MultiApprovalObjectLink.class, false);
		subQuery2.appendSelect(new ClassAttribute(ApprovalObjectLink.class, WTAttributeNameIfc.ROLEB_OBJECT_ID), false);
		subQuery2.setDistinct(true);
		sc = new SearchCondition(MultiApprovalObjectLink.class, WTAttributeNameIfc.ROLEA_OBJECT_ID, ApprovalObjectLink.class, WTAttributeNameIfc.ROLEA_OBJECT_ID);
		subQuery2.appendWhere(sc, new int[] {idxMultiAppObjLink, idxAppObjLink2});
		subQuery2.appendAnd();
		sc = new SearchCondition(ApprovalObjectLink.class, WTAttributeNameIfc.ROLEA_CLASSNAME, SearchCondition.EQUAL, MultiApproval.class.getName());
		subQuery2.appendWhere(sc, new int[] {idxAppObjLink2});
		subQuery2.appendAnd();
		sc = new SearchCondition(new ClassAttribute(MultiApprovalObjectLink.class, WTAttributeNameIfc.ROLEB_OBJECT_ID), SearchCondition.IN, new ArrayExpression(list.toArray()));
		subQuery2.appendWhere(sc, new int[] {idxMultiAppObjLink});
		
		sc = new SearchCondition(new ClassAttribute(ApprovalLine.class, ApprovalLine.MASTER_REFERENCE+".key.id"), SearchCondition.EQUAL, new SubSelectExpression(subQuery2));
		query.appendWhere(sc, new int[] {idxAppLine});
		
		query.appendCloseParen();
		
		query.appendAnd();
		
		sc = new SearchCondition(ApprovalLine.class, ApprovalLine.ROLE, SearchCondition.EQUAL, "APPROVE");
		query.appendWhere(sc, new int[] {idxAppLine});
		
		query.appendAnd();
		
		sc = new SearchCondition(ApprovalLine.class, ApprovalLine.STATE, SearchCondition.EQUAL, "COMPLETE");
		query.appendWhere(sc, new int[] {idxAppLine});
		
		query.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class,ApprovalLine.APPROVE_DATE), true), new int[] {idxAppLine});
		QueryResult qr = PersistenceHelper.manager.find(query);
		
		if(qr.hasMoreElements()){
			Object[] o = (Object[])qr.nextElement();
			ApprovalLine line = (ApprovalLine)o[0];
			WTUser user = (WTUser)line.getOwner().getPrincipal();
			return user;
		}
		
		return null;
		
	}
	
	
}
