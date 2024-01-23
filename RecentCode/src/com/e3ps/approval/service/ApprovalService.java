package com.e3ps.approval.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.e3ps.approval.ApprovalLine;
import com.e3ps.approval.ApprovalMaster;
import com.e3ps.approval.bean.ApprovalData;

import wt.fc.Persistable;
import wt.fc.QueryResult;
import wt.fc.WTObject;
import wt.method.RemoteInterface;
import wt.org.WTPrincipal;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.util.WTException;

@RemoteInterface
public interface ApprovalService {
	public static final String MASTER_WORKING = "작업중";
    public static final String MASTER_APPROVING = "결재중";
    public static final String MASTER_APPROVED = "결재완료";
    public static final String MASTER_REJECTED = "반려";
    public static final String MASTER_WITHDRAWAL = "회수";
    public static final String MASTER_REWORKING = "재작성중";
    public static final String MASTER_INWORK = "작성중";
    public static final String MASTER_CANCELLED = "취소";

    public static final String LINE_REQUEST = "요청";
    public static final String LINE_STANDING = "대기";
    public static final String LINE_APPROVING = "결재중";
    public static final String LINE_COMMIT = "완료";
    public static final String LINE_REJECTED = "반려";
    public static final String LINE_STANDING_CANCEL = "대기-회수";
    public static final String LINE_WITHDRAWAL = "회수";
    public static final String LINE_TEMP = "수신";
    public static final String LINE_WORKING = "작업중";
    public static final String LINE_REWORKING = "재작성중";
    public static final String LINE_INWORKING = "작성중";
    public static final String LINE_DISCUSSING = "수신";
    public static final String LINE_DISCUSSING_AGREE = "합의";
    public static final String LINE_DISCUSSING_REJECT = "반대";
    public static final String LINE_COMPLETE  = "완료됨";
    public static final String LINE_CANCELED  = "취소됨";

    public static final String APPROVE_REQUEST= "작성";
    public static final String APPROVE_PREAPPROVE= "합의전결재";
    public static final String APPROVE_DISCUSS= "합의";
    public static final String APPROVE_POSTAPPROVE= "결재";
    public static final String APPROVE_NOTIFICATE= "수신";

    public static final String WORKING_REPORTER="제출자";
    public static final String WORKING_REVIEWER="승인자";
    public static final String WORKING_DISCUSSER="합의자";
    public static final String WORKING_WORKING="작업자";
    public static final String WORKING_TEMP="수신자";
    
    public static final String WORKING_READY="등록전";
    public static final String CENCEL_WORK ="반려통지";
    
    public static final String DISTRIBUTE ="배포";

	public abstract void createTemplate(Map<String, Object> reqMap) throws Exception;

	public abstract void approveAction(Map<String, Object> reqMap) throws Exception;

	public abstract void registApproval(Persistable per, List<Map<String, Object>> approvalList, String appState, ApprovalLine line)
			throws Exception;

	public abstract void delegateAction(Map<String, Object> reqMap) throws Exception;

	public abstract void sendApprovalMail(ApprovalLine master) throws Exception;

	public abstract void deleteApproval(Persistable per) throws Exception;

	public abstract void deleteTemplate(Map<String, Object> reqMap) throws Exception;

	public abstract boolean saveWFHistory(WTObject wtobject, String state, String comment, WTPrincipal wtprincipal) throws Exception;

	public abstract void dropAction(Map<String, Object> reqMap) throws Exception;

	public abstract void recallAction(Map<String, Object> reqMap) throws Exception;

	void changeStatePart(WTPart part, String setState) throws Exception;

	void appendStateSearchCondition(QuerySpec qs, Class targetClass,
			int targetIdx, String state) throws QueryException;
	
	void registApproval(Persistable per, Map<String, Object> param)
			throws WTException;
	
	void removeProcess(Persistable per) throws WTException;
	
	void registWork(Persistable per, WTUser worker) throws WTException;
	
	public ApprovalMaster getApprovalMaster(Persistable per) throws WTException;
	
	public QueryResult getApprovalMaster(Persistable per, boolean link) throws WTException;
	
	public QueryResult getApprovalLine(ApprovalMaster master) throws WTException;
	
	public String getCreatorFullName(Object pbo) throws WTException;
	
	public String getCreateTime(Persistable per);
	
	public void registApprovalChange(Persistable per, Map<String,Object> param) throws WTException;
	
	public boolean isAccessModify(Persistable per) throws Exception;
	
	public boolean isAccessModify(Persistable per, String creatorName) throws Exception;
	
	public boolean isModify(Persistable per) throws WTException;
	
	public String getState(Persistable per) throws WTException;
	
	public ApprovalData getApprovalHistory(String oid) throws Exception;
	
	public ArrayList<ApprovalLine> getLastApprovalLine(String oid) throws WTException;
	
	public void appendReceiveList(ApprovalMaster appMaster, List<WTPrincipal> userList) throws Exception;
	
	public void setPersistableState(Persistable per, String state) throws WTException;

}
