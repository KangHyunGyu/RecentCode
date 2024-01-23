package com.e3ps.approval.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.e3ps.approval.ApprovalLine;
import com.e3ps.approval.ApprovalObjectLink;
import com.e3ps.approval.ApproveRoleType;
import com.e3ps.approval.ApproveStateType;
import com.e3ps.approval.CommonActivity;
import com.e3ps.approval.MultiApproval;
import com.e3ps.approval.bean.ApprovalData;
import com.e3ps.approval.bean.ApprovalLineData;
import com.e3ps.approval.bean.ApprovalListData;
import com.e3ps.change.EChangeActivity;
import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.EChangeRequest2;
import com.e3ps.change.beans.ECOData;
import com.e3ps.change.service.ChangeHelper;
import com.e3ps.change.service.ChangeService;
import com.e3ps.common.code.NumberCode;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.AccessControlUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.distribute.DistributeDocument;
import com.e3ps.distribute.DistributeRegistration;
import com.e3ps.doc.E3PSDocument;
import com.e3ps.org.bean.PeopleData;
import com.e3ps.project.EProject;
import com.e3ps.project.EProjectNode;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.issue.IssueRequest;

import wt.doc.WTDocument;
import wt.enterprise.RevisionControlled;
import wt.epm.EPMDocument;
import wt.fc.Persistable;
import wt.org.WTUser;
import wt.part.WTPart;

public class ApprovalUtil {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.APPROVAL.getName());

	/** 결재선 ROLE **/
	public static final String ROLE_DRAFT = "DRAFT";
	public static final String ROLE_DISCUSS = "DISCUSS";
	public static final String ROLE_APPROVE = "APPROVE";
	public static final String ROLE_RECEIVE = "RECEIVE";

	public static final String ROLE_ALL = "ALL";
	public static final String ROLE_EXCEPT_RECEIVE = "EXCEPT_RECEIVE";

	/** 결재선 마스트 상태 **/
	public static final String STATE_MASTER_TEMP_STORAGE = "TEMP_STORAGE"; // 임시저장
	public static final String STATE_MASTER_DISCUSSING = "DISCUSSING"; // 합의중
	public static final String STATE_MASTER_APPROVING = "APPROVING"; // 승인
	public static final String STATE_MASTER_REJECTED = "REJECTED"; // 반려됨
	public static final String STATE_MASTER_COMPLETED = "COMPLETED"; // 완료됨
	public static final String STATE_MASTER_CANCLED = "CANCLED"; // 취소됨

	/** 결재라인 상태 **/
	public static final String STATE_LINE_STANDING = "STANDING"; // 대기중
	public static final String STATE_LINE_ONGOING = "ONGOING"; // 진행중
	public static final String STATE_LINE_COMPLETE = "COMPLETE"; // 완료
	public static final String STATE_LINE_REJECT = "REJECT"; // 반려
	public static final String STATE_LINE_APPEAL = "APPEAL"; // 이의
	public static final String STATE_LINE_AGREEMENT = "AGREEMENT"; // 합의
	public static final String STATE_LINE_DELEGATE = "DELEGATE"; // 위임
	public static final String STATE_LINE_CANCEL = "CANCEL"; // 취소

	/* 결재 대상 객체 */
	public static final String OBJECT_TYPE_PART = "PART";
	public static final String OBJECT_TYPE_EPM = "EPM";
	public static final String OBJECT_TYPE_DOC = "DOC";
	public static final String OBJECT_TYPE_ECR = "ECR";
	public static final String OBJECT_TYPE_ECO = "ECO";
	public static final String OBJECT_TYPE_ECN = "ECN";
	public static final String OBJECT_TYPE_ECA = "ECA";
	public static final String OBJECT_TYPE_CA = "CA";
	public static final String OBJECT_TYPE_MUTIL = "MUTIL";
	public static final String OBJECT_TYPE_MUTIL_DOC = "MUTIL_DOC";
	public static final String OBJECT_TYPE_MUTIL_PART = "MUTIL_PART";
	public static final String OBJECT_TYPE_MUTIL_EPM = "MUTIL_EPM";
	public static final String OBJECT_TYPE_DIST = "DISTRIBUTE";
	public static final String OBJECT_TYPE_DISTREG = "DISTRIBUTEREG";
	public static final String OBJECT_TYPE_ECNECCB = "ECNECCB";
	public static final String OBJECT_TYPE_PROJECT = "PROJECT";
	public static final String OBJECT_TYPE_OLDCAR = "OLDCAR";
	public static final String OBJECT_TYPE_BENCHMARKING = "BENCH_MARKING";
	public static final String OBJECT_TYPE_PROJECT_ISSUE = "PROJECT_ISSUE";

	/* 결재 대상 객체 ECA */
	public static final String OBJECT_TYPE_ECA_CHANGEDRWBOM = "CHANGEDRWBOM";
	public static final String OBJECT_TYPE_ECA_CONTROLCHANGEDRWBOM = "CONTROLCHANGEDRWBOM";
	public static final String OBJECT_TYPE_ECA_MODIFYSOURCE = "MODIFYSOURCE";
	public static final String OBJECT_TYPE_ECA_CHANGEPURCHASEDOC = "CHANGEPURCHASEDOC";
	public static final String OBJECT_TYPE_ECA_NOTICEECHANGE = "NOTICEECHANGE";
	public static final String OBJECT_TYPE_ECA_CHANGEMANUDOC = "CHANGEMANUDOC";
	public static final String OBJECT_TYPE_ECA_CHANGETESTDOC = "CHANGETESTDOC";
	public static final String OBJECT_TYPE_ECA_CHANGEAPPLY = "CHANGEAPPLY";
	public static final String OBJECT_TYPE_ECA_CreateTRCR = "CreateTRCR";

	/**
	 * 
	 * @desc : 결재 Role 타입 기안,합의,승인,수신
	 * @author : tsuam
	 * @date : 2019. 7. 16.
	 * @method : getApprovalRoleType
	 * @return : void
	 */
	public static List<Map<String, String>> getApprovalRoleType() {

		ApproveRoleType[] roleType = ApproveRoleType.getApproveRoleTypeSet();

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < roleType.length; i++) {
			Map<String, String> map = new HashMap<String, String>();

			map.put("key", roleType[i].toString());
			map.put("value", roleType[i].getDisplay(MessageUtil.getLocale()));
			list.add(map);
		}

		return list;

	}

	/**
	 * 전자 결재 전체 상태
	 * 
	 * @desc :
	 * @author : tsuam
	 * @date : 2019. 7. 16.
	 * @method : getApproveState
	 * @return : List<Map<String,String>>
	 * @return
	 */
	public static List<Map<String, String>> getApproveState() {

		ApproveStateType[] roleType = ApproveStateType.getApproveStateTypeSet();

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < roleType.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("key", roleType[i].toString());
			map.put("value", roleType[i].getDisplay(MessageUtil.getLocale()));
			list.add(map);
		}

		return list;

	}

	/**
	 * 
	 * @desc : 결재 마스터 상태
	 * @author : tsuam
	 * @date : 2019. 7. 16.
	 * @method : getMasterState
	 * @return : List<Map<String,String>>
	 * @return
	 */
	public static List<Map<String, String>> getMasterState() {

		ApproveStateType[] roleType = ApproveStateType.getApproveStateTypeSet();

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < roleType.length; i++) {
			Map<String, String> map = new HashMap<String, String>();

			if (roleType[i].getShortDescription().equals("MASTER")) {
				map.put("key", roleType[i].toString());
				map.put("value", roleType[i].getDisplay(MessageUtil.getLocale()));
				list.add(map);
			}

		}

		return list;

	}

	/**
	 * 
	 * @desc : 결재 라인 상태
	 * @author : tsuam
	 * @date : 2019. 7. 16.
	 * @method : getLineState
	 * @return : List<Map<String,String>>
	 * @return
	 */
	public static List<Map<String, String>> getLineState() {

		ApproveStateType[] roleType = ApproveStateType.getApproveStateTypeSet();

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < roleType.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			if (roleType[i].getShortDescription().equals("LINE")) {
				map.put("value", roleType[i].toString());
				map.put("name", roleType[i].getDisplay());
				list.add(map);
			}

		}
		return list;

	}

	/**
	 * 
	 * @desc : 결재선 지정 그리드 정보를 기안,합의,결재,수신 으로 ArrayList 로 분활
	 * @author : tsuam
	 * @date : 2019. 7. 16.
	 * @method : getRoleTypeList
	 * @return : void
	 * @param approvalList
	 */
	public static Map<String, List<String>> getRoleTypeList(List<Map<String, Object>> approvalList) {

		List<String> approveList = new ArrayList<String>();
		List<String> discussList = new ArrayList<String>();
		List<String> receiveList = new ArrayList<String>();
		if (approvalList != null) {
			for (Map<String, Object> map : approvalList) {

				String roleType = StringUtil.checkNull((String) map.get("roleType"));
				String oid = StringUtil.checkNull((String) map.get("pOid"));

				if (roleType.equals(ApprovalUtil.ROLE_APPROVE)) {
					approveList.add(oid);
				} else if (roleType.equals(ApprovalUtil.ROLE_DISCUSS)) {
					discussList.add(oid);
				} else if (roleType.equals(ApprovalUtil.ROLE_RECEIVE)) {
					receiveList.add(oid);
				}
			}
		}

		Map<String, List<String>> approvalMap = new HashMap<String, List<String>>();

		approvalMap.put(ROLE_APPROVE, approveList);
		approvalMap.put(ROLE_DISCUSS, discussList);
		approvalMap.put(ROLE_RECEIVE, receiveList);

		return approvalMap;

	}

	/**
	 * 
	 * @desc : 기존 결재 라인에서 Role별 리스트
	 * @author : tsuam
	 * @date : 2019. 7. 29.
	 * @method : getLineRoleTypeList
	 * @return : Map<String,List<String>>
	 * @param approvalList
	 * @return
	 */
	public static Map<String, List<String>> getLineRoleTypeList(List<ApprovalLine> approvalList) throws Exception {

		List<String> approveList = new ArrayList<String>();
		List<String> discussList = new ArrayList<String>();
		List<String> receiveList = new ArrayList<String>();
		List<String> draftList = new ArrayList<String>();
		if (approvalList != null) {
			for (ApprovalLine line : approvalList) {

				LOGGER.info(" : line line.getState() = " + line.getState().toString());
				LOGGER.info(" : line.getState().toString().equals(STATE_LINE_DELEGATE) = "
						+ line.getState().toString().equals(STATE_LINE_DELEGATE));
				if (line.getState().toString().equals(STATE_LINE_DELEGATE)) { // 위임 운 제외
					continue;
				}

				String roleType = line.getRole().toString();

				WTUser user = (WTUser) line.getOwner().getPrincipal();
				String oid = CommonUtil.getOIDString(user);

				if (roleType.equals(ROLE_APPROVE)) {
					approveList.add(oid);
				} else if (roleType.equals(ROLE_DISCUSS)) {
					discussList.add(oid);
				} else if (roleType.equals(ROLE_RECEIVE)) {
					receiveList.add(oid);
				} else if (roleType.equals(ROLE_DRAFT)) {
					draftList.add(oid);
				}
			}
		}

		Map<String, List<String>> approvalMap = new HashMap<String, List<String>>();

		approvalMap.put(ROLE_APPROVE, approveList);
		approvalMap.put(ROLE_DISCUSS, discussList);
		approvalMap.put(ROLE_RECEIVE, receiveList);
		approvalMap.put(ROLE_DRAFT, draftList);
		return approvalMap;

	}

	public static ApprovalListData setApprovalObject(ApprovalLine line, ApprovalObjectLink link, String state) throws Exception {
		
		boolean isPermission = AccessControlUtil.checkPermissionForOid(link.getRoleAObjectId().toString());
		if(!isPermission) return null;
		
		Persistable obj = link.getObj();
		
		ApprovalListData data = new ApprovalListData();
		ApprovalLineData lineData = new ApprovalLineData(line);
		Map<String, String> objectMap = getApprovalObjectInfo(obj);

		String tmp = line.getMaster().getObjectType();
		if(OBJECT_TYPE_PROJECT_ISSUE.equals(tmp)) {
			data.setRoleName("이슈 방안");
		}else {
			data.setRoleName(lineData.getRoleName());
		}
		data.setRoleType(lineData.getRoleType());
		
		data.setTitle(objectMap.get("title"));
		data.setObjectOid(objectMap.get("objectOid"));
		data.setOid(lineData.getOid());
		data.setState(state);
		data.setStateName(ApproveStateType.toApproveStateType(state).getDisplay(MessageUtil.getLocale()));
		data.setOwner(lineData.getOwner());
		data.setOwnerFullName(lineData.getOwnerFullName());

		data.setStartDateFormat(lineData.getStartDateFormat());

		Timestamp createDate = line.getMaster().getPersistInfo().getCreateStamp();
		Timestamp completeDate = line.getMaster().getCompleteDate();
		Map<String, String> objectTypeMap = getObjectTypeNameMap();
		String createName = line.getMaster().getOwner().getFullName();
		data.setCreateDate(createDate.toString());
		data.setCreateDateFormat(DateUtil.getDateString(createDate, "d"));
		data.setCreateFullName(createName);
		data.setObjectType(line.getMaster().getObjectType());
		if(line.getMaster().getObjectType().equals("ECO")) {
			EChangeOrder2 eco = (EChangeOrder2) CommonUtil.getObject(objectMap.get("objectOid"));
			ECOData eoData = new ECOData(eco);
			if(eoData.getState().equals("ECACOMPLETE")) {
				data.setObjectTypeName("ECO 최종승인");
			} else {
				data.setObjectTypeName(objectTypeMap.get(line.getMaster().getObjectType()));
			}
		} else if("DOC".equals(line.getMaster().getObjectType())) {
			E3PSDocument doc = (E3PSDocument) obj;
			String doc_Att = doc.getDocAttribute();
			NumberCode code = CodeHelper.manager.getNumberCode("DOCCODETYPE", doc_Att);
			
			data.setObjectTypeName(objectTypeMap.get(line.getMaster().getObjectType())+ " ["+code.getName()+"]");
		} else {
			data.setObjectTypeName(objectTypeMap.get(line.getMaster().getObjectType()));
		}

		if (completeDate != null) {
			data.setCompleteDate(completeDate.toString());
			data.setCompleteDateFormat(DateUtil.getDateString(completeDate, "d"));
		}

		if (line.getMaster().getObjectType().equals("ECA")) {
			EChangeActivity eca = (EChangeActivity) CommonUtil.getObject(objectMap.get("objectOid"));
			Persistable per = eca.getOrder();
			data.setChangeOid(CommonUtil.getOIDString(per));
		}

		return data;
	}

	/**
	 * 
	 * @desc : 결재 대상 객체 정보
	 * @author : tsuam
	 * @date : 2019. 7. 22.
	 * @method : getApprovalObject
	 * @return : Map<String,String>
	 * @param obj
	 * @return
	 */
	public static Map<String, String> getApprovalObjectInfo(Persistable obj) {

		Map<String, String> map = new HashMap<String, String>();
		String objectType = "";
		String number = "";
		String name = "";
		String title = "";
		String creator = "";
		String supplierId = "";
		String supplierName = "";
		String orderType = "";
		/*
		 * 프로젝트 이슈 결재 데이터로 나오게 하기 위한 조치.
		 */
		String issueName = "";
		String taskName = "";
		if (obj instanceof WTPart) {

			WTPart part = (WTPart) obj;
			objectType = OBJECT_TYPE_PART;
			number = part.getNumber();
			name = part.getName();
			creator = part.getCreatorFullName();
		} else if (obj instanceof EPMDocument) {

			EPMDocument epm = (EPMDocument) obj;
			objectType = OBJECT_TYPE_EPM;
			number = epm.getNumber();
			name = epm.getName();
			creator = epm.getCreatorFullName();

		} else if (obj instanceof E3PSDocument) {

			E3PSDocument doc = (E3PSDocument) obj;
			objectType = OBJECT_TYPE_DOC;
			number = doc.getNumber();
			name = doc.getName();
			creator = doc.getCreatorFullName();
		} else if (obj instanceof MultiApproval) {
			MultiApproval multi = (MultiApproval) obj;

			if (multi.getObjectType().equals("epm")) {
				objectType = OBJECT_TYPE_MUTIL_EPM;
			} else if (multi.getObjectType().equals("part")) {
				objectType = OBJECT_TYPE_MUTIL_PART;
			} else {
				objectType = OBJECT_TYPE_MUTIL_DOC;
			}

			number = multi.getNumber();
			name = multi.getName();
			creator = multi.getOwner().getFullName();
		}else if (obj instanceof DistributeDocument) {
			DistributeDocument dis = (DistributeDocument) obj;
			objectType = OBJECT_TYPE_DIST;
			number = dis.getDistributeNumber();
			name = dis.getDistributeName();
			creator = dis.getCreatorFullName();
		}else if (obj instanceof DistributeRegistration) {
			DistributeRegistration dr = (DistributeRegistration) obj;
			DistributeDocument dis = dr.getDistribute();
			objectType = OBJECT_TYPE_DISTREG;
			number = dis.getDistributeNumber();
			name = dis.getDistributeName();
			creator = dis.getCreatorFullName();
		}else if (obj instanceof EChangeOrder2) {
			EChangeOrder2 eco = (EChangeOrder2) obj;
			objectType = OBJECT_TYPE_ECO;
			number = eco.getOrderNumber();
			name = eco.getName();
			creator = eco.getOwner().getFullName();
		} else if (obj instanceof EChangeRequest2) {
			EChangeRequest2 ecr = (EChangeRequest2) obj;
			objectType = OBJECT_TYPE_ECR;
			number = ecr.getRequestNumber();
			name = ecr.getName();
			creator = ecr.getOwner().getFullName();
		} else if (obj instanceof EChangeActivity) {
			EChangeActivity eca = (EChangeActivity) obj;
			String tempNumber = "";
			if (eca.getOrder() instanceof EChangeOrder2) {
				EChangeOrder2 eco = (EChangeOrder2) eca.getOrder();
				tempNumber = eco.getOrderNumber() + " / " + eco.getName();
			} else if (eca.getOrder() instanceof EChangeRequest2) {
				EChangeRequest2 ecr = (EChangeRequest2) eca.getOrder();
				tempNumber = ecr.getRequestNumber() + " / " + ecr.getName();
			}
			objectType = OBJECT_TYPE_ECA;
			number = tempNumber;
			name = eca.getName();
			creator = eca.getOwner().getFullName();
		} else if (obj instanceof CommonActivity) {
			CommonActivity ca = (CommonActivity) obj;
			objectType = OBJECT_TYPE_CA;
			number = "CA";
			name = ca.getTitle();
			creator = ca.getOwner().getFullName();
		} else if (obj instanceof EProject) {
			EProject p = (EProject) obj;
			objectType = OBJECT_TYPE_PROJECT;
			number = p.getCode();
			name = p.getName();
			creator = p.getCreator().getFullName();
		}else if (obj instanceof IssueRequest) {
			IssueRequest is = (IssueRequest) obj;
			objectType = OBJECT_TYPE_PROJECT_ISSUE;
			ETaskNode task = is.getTask();
			EProjectNode pr = task.getProject();
			number = pr.getCode();
			name = pr.getName();
			WTUser user = is.getCreator();
			creator = user.getFullName();
			taskName = task.getName();
			issueName = is.getName();
		}
		title = "[" + number + "] " + name;
		if(!issueName.equals("") && !taskName.equals("")) {
			title += " > 테스크" + "[" + taskName +  "]" + " > 이슈 : " + issueName;
		}
		map.put("objectType", objectType);
		map.put("number", number);
		map.put("name", name);
		map.put("title", title);
		map.put("creator", creator);
		map.put("objectOid", CommonUtil.getVROID(obj));
		map.put("supplierName", supplierName);
		map.put("supplierId", supplierId);
		map.put("orderType", orderType);

		return map;
	}

	public static List<ApprovalLineData> getNextApproveLine(List<ApprovalLineData> list, ApprovalLine line) {

		List<ApprovalLineData> nextLine = new ArrayList<ApprovalLineData>();
		for (ApprovalLineData data : list) {

			if (data.getState().equals(ApprovalUtil.STATE_LINE_COMPLETE)) {
				continue;
			}

			if (line.getSeq() == data.getSeq()) {
				break;
			}

			nextLine.add(data);
		}

		return nextLine;
	}

	/**
	 * 
	 * @desc : 결재 마스터에 따른 Object 별 상태
	 * @author : tsuam
	 * @date : 2019. 7. 26.
	 * @method : getMasterToObjectState
	 * @return : void
	 */
	public static String getMasterToObjectState(Persistable per, String masterState) {

		String objectState = "";
		Map<String, String> map = getObjectStateMap();

		map = getObjectStateMap();

		objectState = StringUtil.checkNull(map.get(masterState));
		return objectState;

	}

	public static Map<String, String> getObjectStateMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put(STATE_MASTER_APPROVING, "APPROVING");
		map.put(STATE_MASTER_COMPLETED, "APPROVED");
		map.put(STATE_MASTER_DISCUSSING, "APPROVING");
		map.put(STATE_MASTER_REJECTED, "RETURN");

		return map;
	}

	/**
	 * 
	 * @desc : ECA 결재 진행 상태
	 * @author : tsuam
	 * @date : 2019. 10. 24.
	 * @method : getObjectECAStateMap
	 * @return : Map<String,String>
	 * @return
	 */
	public static Map<String, String> getObjectECAStateMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put(STATE_MASTER_APPROVING, "APPROVING");
		map.put(STATE_MASTER_COMPLETED, "COMPLETED");
		map.put(STATE_MASTER_DISCUSSING, "APPROVING");
		map.put(STATE_MASTER_REJECTED, "PROGRESS");

		return map;
	}

	/**
	 * 
	 * @desc : ECN 결재 진행 상태
	 * @author : tsuam
	 * @date : 2019. 10. 24.
	 * @method : getObjectECNStateMap
	 * @return : Map<String,String>
	 * @return
	 */
	public static Map<String, String> getObjectECNStateMap() {
		Map<String, String> map = new HashMap<String, String>();

		// map.put(STATE_MASTER_TEMP_STORAGE, "ECNNOTICE");
		map.put(STATE_MASTER_APPROVING, "APPROVING");
		map.put(STATE_MASTER_COMPLETED, "COMPLETED");
		map.put(STATE_MASTER_DISCUSSING, "APPROVING");
		map.put(STATE_MASTER_REJECTED, "ECNECCB");

		return map;
	}

	/**
	 * 
	 * @desc : ECN 결재 진행 상태
	 * @author : tsuam
	 * @date : 2019. 10. 24.
	 * @method : getObjectECNStateMap
	 * @return : Map<String,String>
	 * @return
	 */
	public static Map<String, String> getObjectECRStateMap() {
		Map<String, String> map = new HashMap<String, String>();

		// map.put(STATE_MASTER_TEMP_STORAGE, "ECNNOTICE"); //공지중
		map.put(STATE_MASTER_APPROVING, "APPROVING"); // 공지중
		map.put(STATE_MASTER_COMPLETED, "ECRCOMPLETED");
		map.put(STATE_MASTER_DISCUSSING, "APPROVING"); // 공지중
		map.put(STATE_MASTER_REJECTED, "RETURN");

		return map;
	}

	/**
	 * 
	 * @desc : 결재 타입 리
	 * @author : tsuam
	 * @date : 2019. 7. 30.
	 * @method : getMasterState
	 * @return : List<Map<String,String>>
	 * @return
	 */
	public static List<Map<String, String>> getObjectTypeList() {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		Map<String, String> mapType = getObjectTypeNameMap();

		Iterator it = mapType.keySet().iterator();

		while (it.hasNext()) {
			Map<String, String> map = new HashMap<String, String>();
			String key = (String) it.next();

			map.put("key", key);
			map.put("value", mapType.get(key));
			list.add(map);

		}

		return list;

	}

	public static List<Map<String, String>> getMultiObjectTypeList() {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		Map<String, String> mapType = getMultiObjectTypeNameMap();

		Iterator it = mapType.keySet().iterator();

		while (it.hasNext()) {
			Map<String, String> map = new HashMap<String, String>();
			String key = (String) it.next();

			map.put("key", key);
			map.put("value", mapType.get(key));
			list.add(map);

		}

		return list;

	}

	public static Map<String, String> getMultiObjectTypeNameMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put(OBJECT_TYPE_DOC.toLowerCase(), "문서");
		map.put(OBJECT_TYPE_EPM.toLowerCase(), "도면");
		map.put(OBJECT_TYPE_PART.toLowerCase(), "부품");
		return map;
	}

	/**
	 * 
	 * @desc : 결재 객체 타입별 Name
	 * @author : tsuam
	 * @date : 2019. 8. 5.
	 * @method : getObjectTypeNameMap
	 * @return : Map<String,String>
	 * @return
	 */
	public static Map<String, String> getObjectTypeNameMap() {

		Map<String, String> map = new HashMap<String, String>();

		map.put(OBJECT_TYPE_DOC, "문서");
		map.put(OBJECT_TYPE_EPM, "도면");
		map.put(OBJECT_TYPE_PART, "부품");

		map.put(OBJECT_TYPE_ECR, OBJECT_TYPE_ECR);
		map.put(OBJECT_TYPE_ECO, OBJECT_TYPE_ECO);
		map.put(OBJECT_TYPE_ECA, "설계변경활동");
		map.put(OBJECT_TYPE_CA, OBJECT_TYPE_ECO + "최종승인");
		map.put(OBJECT_TYPE_ECN, OBJECT_TYPE_ECN);
		map.put(OBJECT_TYPE_MUTIL, "일괄결재");
		map.put(OBJECT_TYPE_PROJECT_ISSUE, "프로젝트 이슈");
		map.put(OBJECT_TYPE_DIST, "도면출도의뢰서");
		map.put(OBJECT_TYPE_DISTREG, "배포");

		map.put(OBJECT_TYPE_MUTIL_EPM, "일괄결재(도면)");
		map.put(OBJECT_TYPE_MUTIL_PART, "일괄결재(부품)");
		map.put(OBJECT_TYPE_MUTIL_DOC, "일괄결재(문서)");

		// map.put(OBJECT_TYPE_DIS, "배포");
//		map.put(OBJECT_TYPE_OLDCAR, "과거차문제점");
//		map.put(OBJECT_TYPE_BENCHMARKING, "벤치마킹");

		return map;

	}

	/**
	 * 
	 * @desc : 결재선 임시 저장시 ArryList 객체로 변환
	 * @author : tsuam
	 * @date : 2019. 8. 5.
	 * @method : getListMapToList
	 * @return : List<String>
	 * @param listMap
	 * @return
	 */
	public static ArrayList<String> getListMapToList(List<Map<String, Object>> listMap) {
		ArrayList<String> list = new ArrayList<String>();

		for (Map<String, Object> map : listMap) {

			String oid = (String) map.get("pOid");
			LOGGER.info("getListMapToList oid =" + oid);
			list.add(oid);
		}

		return list;

	}

	/**
	 * 
	 * @desc : Template 에서 Blob의 ArryList에서 PeopleData 로 변경
	 * @author : tsuam
	 * @date : 2019. 8. 5.
	 * @method : getArryListOidToList
	 * @return : List<PeopleData>
	 * @param listOid
	 * @return
	 * @throws Exception
	 */
	public static List<PeopleData> getListOidToList(List<String> listOid, List<PeopleData> list, String role)
			throws Exception {

		ApproveRoleType roleType = ApproveRoleType.toApproveRoleType(role);
		for (String oid : listOid) {
			LOGGER.info("oid =" + oid);
			PeopleData data = new PeopleData(oid);
			data.setRoleType(roleType.toString());
			data.setRoleName(roleType.getDisplay(MessageUtil.getLocale()));
			list.add(data);
		}

		return list;

	}

	public static Map<String, String> getMultiTypeInfo(String objectType) {

		Map<String, String> map = new HashMap<String, String>();

		if (objectType.equals("part")) {
			map.put("pageName", "multiAppPart");
			map.put("title", "부품");
		} else if (objectType.equals("doc")) {
			map.put("pageName", "multiAppDoc");
			map.put("title", "문서");
		} else if (objectType.equals("epm")) {
			map.put("pageName", "multiAppEpm");
			map.put("title", "도면");
		}

		return map;
	}

	/**
	 * 
	 * @desc : 업무 이력 대상 객체
	 * @author : tsuam
	 * @date : 2019. 11. 1.
	 * @method : getWFHistoryTarget
	 * @return : void
	 * @param oid
	 */
	public static Persistable getWFHistoryTarget(String oid) {

		Persistable returnPP = null;
		Persistable pp = CommonUtil.getObject(oid);

		returnPP = pp;

		return returnPP;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getApprovalRoleType();
	}

}
