package com.e3ps.change.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.e3ps.approval.CommonActivity;
import com.e3ps.change.ApplyChangeState;
import com.e3ps.change.ApplyHistory;
import com.e3ps.change.EChangeActivity;
import com.e3ps.change.EChangeActivityDefinitionRoot;
import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.EChangeRequest2;
import com.e3ps.change.EOEul;

import wt.fc.QueryResult;
import wt.method.RemoteInterface;
import wt.part.WTPart;
import wt.util.WTException;
import wt.vc.Versioned;
import wt.vc.baseline.ManagedBaseline;

@RemoteInterface
public interface ChangeService {
	
	public static final Logger logger = LoggerFactory.getLogger(ChangeService.class);
	
	public static final String ORDER_REGIST = "등록";
	public static final String ORDER_WORK = "진행";
	public static final String ORDER_COMPLETE = "완료";

	public static final String ECO_BEFORE_APPROVING = "사전결재중";
	public static final String ECO_ECA_WORKING = "ECA 활동중";
	public static final String ECO_ECA_COMPLETE = "ECA 활동완료";
	public static final String ECO_AFTER_APPROVING = "최종결재중";
	public static final String ECO_COMPLETE = "완료됨";
	public static final String ECO_REJECTED = "반려됨";
	public static final String ECO_WORKING = "작업 중";
	public static final String ECO_APPROVING = "승인중";
	public static final String ECO_CANCELLED = "취소됨";
	public static final String ECO_STOPPED = "중단됨";
	
	public static final String ACTIVITY_STANDBY = "대기중";
	public static final String ACTIVITY_WORKING = "작업중";
	public static final String ACTIVITY_APPROVING = "승인중";
	public static final String ACTIVITY_CANCELED = "반려됨";
	public static final String ACTIVITY_APPROVED = "작업완료";
	public static final String ACTIVITY_CANCELLED = "취소됨";
	public static final String ACTIVITY_STOPPED = "중단됨";
	
	public static final String ACTIVE_WORK_LAST_APPROVAL = "ECO완료배포";
	public static final String ACTIVE_WORK_COMPLETE = "ECO완료";
	public static final String ACTIVE_WORK_REGIST = "ECO등록";
	
	public static final String ACTIVE_WORK_REGIST_TITLE = "신규 ECO 를 등록해 주십시오";
	public static final String ACTIVE_WORK_COMPLETE_TITLE = "ECO활동이 모두 완료 되었습니다.";

	public static final String ACTIVE_ROLE_CHIEF = "CHIEF";
	public static final String ACTIVE_ROLE_WORKING = "WORKING";

	public static final String ECR_NO = "ECR_NO";
	public static final String ECR_EXIST = "ECR_EXIST";

	public static final String PART_TYPE_PART = "PART";
	public static final String PART_TYPE_TOP = "TOP";
	
	void deleteEcr(String oid) throws WTException;
	
	void commitRequest(EChangeRequest2 ecr, String state) throws Exception;
	
	ArrayList<Versioned> getTargetList(EChangeRequest2 ecr) throws WTException;
	
	EChangeOrder2 registCompleteApproval(EChangeOrder2 eco,	Hashtable hash) throws Exception;
	
	String approveEul(Hashtable hash) throws Exception;
	
	void commitEOEul(EOEul eul, String state) throws Exception;
	
	Hashtable getSapData(EOEul eul) throws Exception;
	
	void saveEulData(EOEul eul) throws Exception;
	
	void saveChangeData(Hashtable hash, boolean revision, EOEul eul);
	
	boolean isAllSuccess(ApplyHistory ah) throws Exception;
	
	void cancelChangeData(EOEul eul, ApplyChangeState acs) throws Exception;
	
	void reApply(EOEul eul, ApplyChangeState acs) throws Exception;

	ManagedBaseline createBaseline(WTPart wtpart, EOEul eulb) throws Exception;
	
	EChangeOrder2 getEO(ManagedBaseline bl) throws Exception;
	
	String rework(String oid) throws Exception;
	
	String getLocaleString(String ss);
	
	boolean addWorkingActive(HashMap map);
	
	boolean delWorkingActive(String eaOid);
	
	EChangeActivity getECAWorking(EChangeActivity activity);
	
	EChangeOrder2 getEO(CommonActivity com) throws Exception;
	
	Vector getEoEul(CommonActivity com);
	
	Vector getEoEul(EChangeOrder2 eco);
	
	Vector getEcoPartList(EChangeOrder2 eco);
	
	QueryResult getEcoPartLink(EChangeOrder2 eco);
	
	void commitPartStateChange(Vector vecPart) throws Exception;
	
	public ArrayList getSelectStateList();

	EChangeActivityDefinitionRoot getActiveDefinitionECR() throws WTException;

	EChangeActivity getChangeActivity(long changeOid, long defOid)
			throws WTException;
	
	EChangeRequest2 createEcr(Map<String, Object> reqMap)throws WTException;
	
	EChangeRequest2 updateEcr(Map<String, Object> reqMap)throws WTException;
	
	public void sendActivityAssignMail(EChangeActivity eca) throws Exception;
	
	public void deleteEChangeContents(String oid) throws Exception;
	
}
