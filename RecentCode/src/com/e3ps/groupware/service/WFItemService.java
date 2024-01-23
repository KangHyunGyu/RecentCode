package com.e3ps.groupware.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Vector;

import com.e3ps.groupware.workprocess.WFItem;
import com.e3ps.groupware.workprocess.WFItemUserLink;

import wt.fc.Persistable;
import wt.fc.QueryResult;
import wt.fc.WTObject;
import wt.method.RemoteInterface;
import wt.org.WTPrincipal;
import wt.org.WTUser;
import wt.query.QuerySpec;
import wt.util.WTException;
import wt.workflow.work.WorkItem;

@RemoteInterface
public interface WFItemService {
	
	/**
	 * 워크플로 상태변경 로봇 이벤트 실행시
	 * @param _obj
	 * @param _event
	 */
	void eventListener(Object _obj, String _event);
	
	/**
	 * 워크플로 상태변경 로봇 실행시 WFItem 상태 변경
	 * @param _obj
	 */
	void setState(WTObject _obj);
	
	/**
	 * 결재 객체에서 WFItem을 찾는다.
	 * 
	 * @param wtobject:결재객체
	 * @return WFItem
	 */
	WFItem getWFItem(WTObject wtobject);
	
	/**
	 * WFItem 에 결재 라인이 남아있는지를 체크한다.
	 * 
	 * @param wtobject:결재객체
	 * @return true:결재라인이 남음, false:결재라인이 없음
	 */
	boolean isRemainProcessLine(WTObject wtobject);
	
	/**
	 * WFItem 의 다음 결재라인의 ActivityName을 반환한다.
	 * 
	 * @param wtobject:결재객체
	 * @return WFItemUserLink의 ActivityName
	 */
	String getNextActivityName(WTObject wtobject);
	
	/**
	 * 결재중인 유저를 반환하는 메소드
	 * 
	 * @param wfItem
	 * @return WTUser의 ArrayList
	 */
	ArrayList getProcessingUser(WFItem wfItem);
	
	/**
	 * WFItem의 state를 set한다.
	 * 
	 * @param wtobject:결재객체
	 * @param state
	 */
	void setWFItemState(WTObject wtobject, String state);
	
	/**
	 * 반려시 결재 정보를 초기화한다. (사용하지 않음)
	 * @param wtobject
	 * @throws Exception
	 */
	void reSet(WTObject wtobject) throws Exception;
	
	/**
	 * 
	 * @param workitem
	 * @param activityName
	 * @param comment
	 * @param state
	 */
	void setWFItemUserLinkState(WorkItem workitem, String activityName, String comment, String state);
	
	/**
	 * 결재라인의 상태와 comment를 설정한다.(사용하지 않음)
	 * 
	 * @param workitem
	 * @param state
	 * @param comment
	 */
	void setWFItemUserLinkState(WorkItem workitem, String activityName, String comment);
	
	/**
	 * 
	 * @param wtobject
	 */
	void setReworkAppLineSet(WTObject wtobject);
	
	/**
	 * WFItem 객체를 생성하고 WFObjectWFItemLink를 생성한다.
	 * 
	 * @param pbo 결재객체
	 * @param status 결재객체의 속성
	 * @return item WFItem
	 */
	WFItem newWFItem(WTObject obj, WTPrincipal owner);
	
	/**
	 * WFItemUserLink 객체 생성
	 * @param user
	 * @param item
	 * @param activity
	 * @param order
	 */
	void newWFItemUserLink(WTUser user, WFItem item, String activity, int order);
	
	/**
	 * WFItemUserLink객체를 생성한다.
	 * 
	 * @param user 결재라인에 포함된 WTUser
	 * @param item WFItem
	 * @param activity WFItemUserLink.activityName
	 */
	void newWFItemUserLink(WTUser user, WFItem item, String activity, int order, int seq);
	
	QueryResult getLinkQueryResult(WFItem wfItem, String activity);
	
	QuerySpec getLinkQuerySpec(WFItem wfItem, String order);
	
	/**
	 * 결재 정보를 모두 지운다.
	 * 
	 * @param persistable
	 */
	void deleteWFItem(Persistable persistable);
	
	/**
	 * 결재 정보를 모두 지운다.
	 * 
	 * @param persistable
	 */
	void deleteWFItem(WFItem wfitem);
	
	/**
	 * 기본 결재 '승인됨'후 나머지 작업을 수행한다.
	 * 
	 * @throws WTException
	 * 
	 */
	void processRemainWork(WTObject _wtobject) throws WTException;
	
	String createAsm(Hashtable hash);
	
	/**
	 * 대결재자 지정시 대결재자로 자동 변경
	 * @param wtobject
	 */
	void backup(WTObject wtobject);
	
	
	void reworkDataInit(WFItem wfItem);
	
	TreeMap getEnabledRouteMap();
	
	TreeMap getRouteMap();
	
	String getActivityType(String name);
	
	String getActivityName(String type);
	
	/**
	 * 수신 결재프로세스 유무 체크
	 * @param wtobject
	 * @return
	 */
	boolean isRecipientLine(WTObject wtobject);
	
	/**
	 * 수신 라인
	 * @param wtobject
	 * @throws Exception
	 */
	void setRecipientLine(WTObject wtobject) throws Exception;
	
	
	WFItemUserLink getWFitemUserLink(String oid);
	
	/**
	 * LifeCycle 상태
	 * @param lifeCycle
	 * @return
	 */
	Vector<wt.lifecycle.State> getLifeCycleState(String lifeCycle);
	
	/**
	 * Order Max 값
	 * @param item
	 * @return
	 */
	int getMaxOrderNumber(WFItem item, String StateYn);
	
	/**
	 * Seq Max 값 구하기
	 * @param parent
	 * @return
	 * @throws Exception
	 */
	int getMaxSeq(WFItem item) throws Exception;
	
	/**
	 * 링크 객체의 기안자 복사하기.
	 * @param item
	 */
	void newOrderWFItemUserLink(WFItem item);
	
	/**
	 * 링크 객체의 기안자 복사하기.
	 * @param item
	 */
	int getReOrder(WFItem wfItem);
	
	/**
	 * 
	 * @param user
	 * @param wfItem
	 * @param actName
	 * @param actYn
	 * @return
	 */
	WFItemUserLink newWFItemUserLink(WTUser user, WFItem wfItem, String actName, String actYn);
	
	/**
	 * 결재라인에서 최종 결재자 Return
	 * @param obj
	 * @return
	 */
	WTUser getLastApprover(WTObject obj);
	
	/**
	 * 결재선 저장
	 * @param title
	 * @param preDiscussUser
	 * @param discussUser
	 * @param postDiscussUser
	 * @param tempUser
	 * @throws Exception
	 */
	void createApprovalTemplate(String title, String[] preDiscussUser, String[] discussUser,
			String[] postDiscussUser, String[] tempUser) throws Exception;
	
	/**
	 * 결재선 삭제
	 * @param oid
	 * @throws Exception
	 */
	void deleteApprovalTemplate(String oid) throws Exception;
	
	/**
	 * 결재선 지정
	 * @param map
	 * @return
	 * @throws WTException
	 */
	boolean createAppLine(HashMap map) throws WTException;
	
	/**
	 * 결재선
	 * @param workItem
	 * @param disabled disabled : 0 (결재라인 사용) , disabed :1 history(결재라인 사용)
	 * @param state 수신,합의,결재,위임(결재 여부)
	 * @param activityName 수신,합의,결재
	 * @return
	 */
	Vector<WFItemUserLink> getAppline(WFItem wfItem, boolean disabled, String state,
			String activityName) throws Exception;
	
	/**
	 * 전체 결재라인
	 * @param wfItem
	 * @param disabled
	 * @param state
	 * @param activityName
	 * @return
	 */
	Vector<WFItemUserLink> getTotalAppline(WFItem wfItem);
	
	/**
	 * 결재자 WFItem 객체 찾기
	 * @param owner
	 * @param wfItem
	 * @param activityName
	 * @return
	 */
	WFItemUserLink getOwnerApplineLink(WTUser owner, WFItem wfItem, String activityName);
	
	/**
	 * 반려후 재작업 진행시 (워크플러워에서 실행);
	 * 
	 * @param wtobject
	 */
	void setReworkAppLine(WTObject wtobject);
	
	/**
	 * WfActivity Active명으로 WfItem Active명으로 치환
	 * @param activityName
	 * @return
	 */
	public String getWFItemActivityName(String activityName);
	
	
	QuerySpec getListWFitemQuery(HashMap map);
}
