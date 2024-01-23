package com.e3ps.approval.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.e3ps.approval.ApprovalLine;
import com.e3ps.approval.ApprovalLineTemplate;
import com.e3ps.approval.ApprovalMaster;
import com.e3ps.approval.ApprovalObjectLink;
import com.e3ps.approval.ApprovalToObjectLink;
import com.e3ps.approval.ApproveRoleType;
import com.e3ps.approval.ApproveStateType;
import com.e3ps.approval.CommonActivity;
import com.e3ps.approval.MultiApproval;
import com.e3ps.approval.WFHistory;
import com.e3ps.approval.bean.ApprovalData;
import com.e3ps.approval.bean.ApprovalLineData;
import com.e3ps.approval.util.ApprovalMailForm;
import com.e3ps.approval.util.ApprovalUtil;
import com.e3ps.change.EChangeActivity;
import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.EChangeRequest2;
import com.e3ps.change.beans.ECOData;
import com.e3ps.change.service.ChangeHelper;
import com.e3ps.change.service.ChangeService;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.message.util.MultiLangUtil;
import com.e3ps.common.util.AccessControlUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.OwnPersistable;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.TypeUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.common.web.ParamUtil;
import com.e3ps.common.workflow.E3PSWorkflowHelper;
import com.e3ps.distribute.DistributeDocument;
import com.e3ps.distribute.DistributeRegToPartLink;
import com.e3ps.distribute.DistributeRegistration;
import com.e3ps.distribute.service.DistributeHelper;
import com.e3ps.epm.service.EpmHelper;
import com.e3ps.erp.util.ERPInterface;
import com.e3ps.interfaces.cpc.service.CPCService;
import com.e3ps.org.People;
import com.e3ps.org.bean.PeopleData;
import com.e3ps.org.service.PeopleHelper;
import com.e3ps.queue.E3PSQueueHelper;

import wt.enterprise.Managed;
import wt.enterprise.RevisionControlled;
import wt.epm.EPMDocument;
import wt.epm.EPMDocumentMaster;
import wt.epm.structure.EPMReferenceLink;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.fc.WTObject;
import wt.fc.collections.WTArrayList;
import wt.fc.collections.WTCollection;
import wt.fc.collections.WTHashSet;
import wt.fc.collections.WTList;
import wt.fc.collections.WTSet;
import wt.lifecycle.LifeCycleHelper;
import wt.lifecycle.LifeCycleManaged;
import wt.lifecycle.LifeCycleTemplate;
import wt.lifecycle.State;
import wt.org.WTPrincipal;
import wt.org.WTPrincipalReference;
import wt.org.WTUser;
import wt.ownership.Ownership;
import wt.part.WTPart;
import wt.pom.Transaction;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.StandardManager;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.vc.Iterated;
import wt.vc.VersionControlHelper;
import wt.vc.Versioned;

@SuppressWarnings("serial")
public class StandardApprovalService  extends StandardManager implements ApprovalService {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.APPROVAL.getName());
	
	public static StandardApprovalService newStandardApprovalService() throws WTException {
		final StandardApprovalService instance = new StandardApprovalService();
		instance.initialize();
		return instance;
	}
	
	/**
	 * 결재선 등록,수정 
	 */
	@Override
	public void registApproval(Persistable per, List<Map<String, Object>> approvalList,String appState,ApprovalLine line) throws Exception {
		
		LOGGER.info("============ registApproval============");
		LOGGER.info(String.valueOf(approvalList));
		
		//APPROVING,TEMP_STORAGE
		
		//Role별  Array 취합
		Map<String , List<String>> approvalMap = ApprovalUtil.getRoleTypeList(approvalList);
		
		List<String> discussList = approvalMap.get(ApprovalUtil.ROLE_DISCUSS);
		List<String> approveList = approvalMap.get(ApprovalUtil.ROLE_APPROVE);
		List<String> receiveList = approvalMap.get(ApprovalUtil.ROLE_RECEIVE);
		List<String> draftList = new ArrayList<String>();
		
		//기안 리스트 생성
		boolean isSessionChange = false;
		WTUser dratUser = (WTUser)SessionHelper.manager.getPrincipal();
		String oldUserId =  dratUser.getName();
		PeopleData pData = new PeopleData(dratUser);
		draftList.add(pData.getOid());
		
		
		String draftState = ApprovalUtil.STATE_LINE_ONGOING;		//기안
		String discussState = ApprovalUtil.STATE_LINE_STANDING;		//협의	
		String approveState = ApprovalUtil.STATE_LINE_STANDING;		//승인
		String receiveState = ApprovalUtil.STATE_LINE_STANDING;		//수신
		
		//결재 라인 상태 체크
		if(appState.equals(ApprovalUtil.STATE_MASTER_APPROVING)) {
			appState = ApprovalUtil.STATE_MASTER_DISCUSSING;
			draftState = ApprovalUtil.STATE_LINE_COMPLETE;
			if(discussList.size()>0) {
				discussState = ApprovalUtil.STATE_LINE_ONGOING;
			}else {
				approveState = ApprovalUtil.STATE_LINE_ONGOING;
				appState = ApprovalUtil.STATE_MASTER_APPROVING;
			}
		}
		
		int ver = 0 ;
		int seq = 0;
		
		ApprovalMaster master = ApprovalHelper.manager.getApprovalMaster(per);
		
		if(line != null) {
			ver = line.getVer();
		}
		
		if(master == null) {
			//마스터 생성
			master  = createApprovalMaster(per,appState);
			//마스터와 결재 대상 링크 생성
			createApprovalLink(per, master);
			LOGGER.info("==========  master null + ApprovalUtil.ROLE_DRAFT 생성");
			//기안 Role 생성
			seq = createApprovalLine(master, draftList,ApprovalUtil.ROLE_DRAFT,seq,draftState,ver);
		}else {
			//마스터와 상태 변경
			changeStateApprovalMaster(master, appState);
			
			if(per instanceof EChangeOrder2
					&& master.getState().equals(ApproveStateType.toApproveStateType("COMPLETED"))) {
				master  = createApprovalMaster(per,appState);
				createApprovalLink(per, master);
				seq = createApprovalLine(master, draftList,ApprovalUtil.ROLE_DRAFT,seq,draftState,ver);
			} else {
				deleteApprovalLine(master, true);
			}
			
			//타이틀 변경
			//Map<String, String> map = ApprovalUtil.getApprovalObjectInfo(per);
	        //String title = map.get("title");
	        //master.setTitle(title);
	        //master = (ApprovalMaster) PersistenceHelper.manager.save(master);
	        
			//기존 결재 라인 삭제
			
			seq = 1;
		}
		
		//결재라인 합의
		seq = createApprovalLine(master, discussList,ApprovalUtil.ROLE_DISCUSS,seq,discussState,ver);
		//결재라인 승인
		seq = createApprovalLine(master, approveList,ApprovalUtil.ROLE_APPROVE,seq,approveState,ver);
		//결재랑인 수신
		seq = createApprovalLine(master, receiveList,ApprovalUtil.ROLE_RECEIVE,seq,receiveState,ver);
		
		List<ApprovalLine> list = ApprovalHelper.manager.getApprovalRoleLine(master, ApprovalUtil.ROLE_DRAFT, "", true);
		
		//기안자 라인
		line = list.get(0);
		//실 사용자 저장
		
		//기안자 결재 history 
		if(appState.equals("APPROVING")){
			String state = line.getRole().getDisplay();
			WTPrincipal wtprincipal = (WTPrincipal)line.getOwner().getObject();
			saveWFHistory((WTObject)per, state, "", wtprincipal);
			
			line.setApproveDate(new Timestamp(System.currentTimeMillis()));
			if(line.getStartDate() == null){
				line.setStartDate(new Timestamp(System.currentTimeMillis()));
			}
		}
		
		//기안 업데이트
		if(line.getStartDate() == null){
			line.setStartDate(new Timestamp(System.currentTimeMillis()));
		}
		line = (ApprovalLine) PersistenceHelper.manager.modify(line);
		
		//객체 상태 변경
		changeStatePersistable(per,master);
		
		//메일 발송
		if(!"TEMP_STORAGE".equals(master.getState().toString())) {
			sendApprovalMail(line);
		}
		
		if(isSessionChange){
			SessionHelper.manager.setPrincipal(oldUserId);
		}
	
	}
	
	/**
	 * 
	 * @desc	: 임시 저장에서 제출,저장 시
	 * @author	: tsuam
	 * @date	: 2019. 11. 11.
	 * @method	: updateApproval
	 * @return	: void
	 * @param per
	 * @param approvalList
	 * @param appState
	 * @param line
	 * @throws Exception
	 */
	public void updateApproval(Persistable per, List<Map<String, Object>> approvalList,String appState,ApprovalLine line) throws Exception {
		
		
		LOGGER.info("============ updateApproval============");
		LOGGER.info(String.valueOf(approvalList));
		
		//APPROVING,TEMP_STORAGE
		
		//Role별  Array 취합
		Map<String , List<String>> approvalMap = ApprovalUtil.getRoleTypeList(approvalList);
		
		List<String> discussList = approvalMap.get(ApprovalUtil.ROLE_DISCUSS);
		List<String> approveList = approvalMap.get(ApprovalUtil.ROLE_APPROVE);
		List<String> receiveList = approvalMap.get(ApprovalUtil.ROLE_RECEIVE);
		
		String draftState = ApprovalUtil.STATE_LINE_ONGOING;		//기안
		String discussState = ApprovalUtil.STATE_LINE_STANDING;		//협의	
		String approveState = ApprovalUtil.STATE_LINE_STANDING;		//승인
		String receiveState = ApprovalUtil.STATE_LINE_STANDING;		//수신
		
		//결재 라인 상태 체크
		if(appState.equals(ApprovalUtil.STATE_MASTER_APPROVING)) {
			appState = ApprovalUtil.STATE_MASTER_DISCUSSING;
			draftState = ApprovalUtil.STATE_LINE_COMPLETE;
			if(discussList.size()>0) {
				discussState = ApprovalUtil.STATE_LINE_ONGOING;
			}else {
				approveState = ApprovalUtil.STATE_LINE_ONGOING;
				appState = ApprovalUtil.STATE_MASTER_APPROVING;
			}
		}
		int ver = 0 ;
		int seq = 1;
		
		ApprovalMaster master = null;
		
		if(per instanceof EChangeOrder2) {
			EChangeOrder2 eco = (EChangeOrder2) per;
			if(eco.getLifeCycleState().toString().equals("ECACOMPLETE")) {
				master = ApprovalHelper.manager.getApprovalStateMaster(per, "TEMP_STORAGE");
			} else if(eco.getLifeCycleState().toString().equals("RETURN")) {
				master = ApprovalHelper.manager.getApprovalStateMaster(per, "REJECTED");
			} else {
				master = ApprovalHelper.manager.getApprovalMaster(per);
			}
		} else {
			master = ApprovalHelper.manager.getApprovalMaster(per);
		}
		
		ver = line.getVer();
		//마스터와 상태 변경
		changeStateApprovalMaster(master, appState);
		//기존 결재 라인 삭제
		deleteApprovalLine(master, true);
		
		
		//결재라인 합의
		seq = createApprovalLine(master, discussList,ApprovalUtil.ROLE_DISCUSS,seq,discussState,ver);
		//결재라인 승인
		seq = createApprovalLine(master, approveList,ApprovalUtil.ROLE_APPROVE,seq,approveState,ver);
		//결재랑인 수신
		seq = createApprovalLine(master, receiveList,ApprovalUtil.ROLE_RECEIVE,seq,receiveState,ver);
		
		//List<ApprovalLine> list = ApprovalHelper.manager.getApprovalRoleLine(master, ApprovalUtil.ROLE_DRAFT, "", true);
		
		//기안자 결재 history 
		if(appState.equals("APPROVING")){
			String state = line.getRole().getDisplay();
			WTPrincipal wtprincipal = (WTPrincipal)line.getOwner().getObject();
			saveWFHistory((WTObject)per, state, "", wtprincipal);
		}
		
		
	
	
	}
	
	/**
	 * 
	 * @desc	: 결재 관련 삭제 (Master,Link,Line)
	 * @author	: tsuam
	 * @date	: 2019. 8. 8.
	 * @method	: deleteApproval
	 * @return	: void
	 * @param per
	 * @throws Exception
	 */
	@Override
	public void deleteApproval(Persistable per) throws Exception{
		
		ApprovalMaster master = ApprovalHelper.manager.getApprovalMaster(per);
		
		//결재 마스터 있는경우 삭제
		if(master != null) {
			//결재 라인 삭제
			deleteApprovalLine(master, false);
			//결재 객체 삭제
			deleteApprovalObjectLink(master);
			
			//결재 마스터 삭제
			PersistenceHelper.manager.delete(master);
		}
	}
	/**
	 * 
	 * @desc	: 결재 마스터 생성
	 * @author	: tsuam
	 * @date	: 2019. 7. 16.
	 * @method	: createApprovalMaster
	 * @return	: ApprovalMaster
	 * @param per
	 * @return
	 * @throws Exception
	 */
	private ApprovalMaster createApprovalMaster(Persistable per,String state) throws Exception{
		
		ApprovalMaster am = ApprovalMaster.newApprovalMaster();
		Map<String, String> map = ApprovalUtil.getApprovalObjectInfo(per);
		String objectType = map.get("objectType");
    	am.setState(ApproveStateType.toApproveStateType(state));
        am.setOwner(SessionHelper.manager.getPrincipalReference());
        am.setActive(0);
        am.setObjectType(objectType);
        
        String title = map.get("title");
        am.setTitle(title);
        
        am = (ApprovalMaster)PersistenceHelper.manager.save(am);
		
		return am;
		
	}
	
	/**
	 * 
	 * @desc	: 결재 라인 생성
	 * @author	: tsuam
	 * @date	: 2019. 7. 17.
	 * @method	: createApprovalLine
	 * @return	: int
	 * @param master
	 * @param appLine
	 * @param roleType
	 * @param seq
	 * @param state
	 * @return
	 * @throws Exception
	 */
	private int createApprovalLine(ApprovalMaster master,List<String> appLine,String roleType,int seq,String state,int ver) throws Exception{
		
		Persistable appObj = ApprovalHelper.manager.getApprovalObject(master);
		LOGGER.info(roleType + "appLine = " +appLine.size());
		WTCollection collection = new WTArrayList();
		int i = 0 ; 
		for(String poid : appLine) {
			LOGGER.info("poid = " +poid);
			Persistable userObject = CommonUtil.getObject(poid);
			WTUser user = null;
			if(userObject instanceof People) {
				People pp = (People)CommonUtil.getObject(poid);
				user = pp.getUser();
			}else {
				user = (WTUser)userObject;
			}
			boolean isPermission = AccessControlUtil.checkPermissionForObject(CommonUtil.getOIDString(appObj),(WTPrincipal) user);
			if(!isPermission) throw new Exception(user.getName()+"("+user.getFullName()+") 님이 결재 문서에 대한 권한이 없습니다. 결재 라인을 확인해주세요.");
			
			ApprovalLine line = ApprovalLine.newApprovalLine();
			Ownership ownership = Ownership.newOwnership(user);
			line.setOwner(ownership.getOwner());
			line.setRole(ApproveRoleType.toApproveRoleType(roleType));
			line.setSeq(seq);
			line.setState(ApproveStateType.toApproveStateType(state));
			line.setMaster(master);
			
			//승인 Role 경우 직렬로 진행 하기 때문에 첫번째 승인 라인만 OnGoing 로 변경
			if(ApprovalUtil.STATE_LINE_ONGOING.equals(state)) {
				if(roleType.equals(ApprovalUtil.ROLE_APPROVE) )
				{  //첫번째
					if(i > 0 ) {
						line.setState(ApproveStateType.toApproveStateType(ApprovalUtil.STATE_LINE_STANDING));
					}
					line.setStartDate(new Timestamp(System.currentTimeMillis()));
				}else {
					line.setStartDate(new Timestamp(System.currentTimeMillis()));
				}
			}
			
			if(ApprovalUtil.STATE_LINE_COMPLETE.equals(state)) {
				line.setStartDate(new Timestamp(System.currentTimeMillis()));
				line.setApproveDate(new Timestamp(System.currentTimeMillis()));
			}
			
			line.setVer(ver);
			
			//PersistenceHelper.manager.save(var1)
			collection.add(line);
			if( !(roleType.equals(ApprovalUtil.ROLE_DISCUSS) || roleType.equals(ApprovalUtil.ROLE_RECEIVE)) ) {
				seq++;
			}
			
			i++;
			
		}
		
		if( appLine.size() > 0 && (roleType.equals(ApprovalUtil.ROLE_DISCUSS) || roleType.equals(ApprovalUtil.ROLE_RECEIVE)) ){
			seq++;
		}
		PersistenceHelper.manager.save(collection);
		
		return seq;
	}
	
	
	/**
	 * 
	 * @desc	: 결재 객체로 결재 삭제
	 * @author	: tsuam
	 * @date	: 2019. 7. 23.
	 * @method	: deleteApproval
	 * @return	: void
	 * @param per
	 * @throws Exception
	 */
	private void deletePerApproval(Persistable per) throws Exception{
		
		ApprovalMaster master = ApprovalHelper.manager.getApprovalMaster(per);
		if(master != null) {
			deleteMasterToApproval(master);
		}
		
	}
	
	/**
	 * 
	 * @desc	: 결재 마스터로 결재 삭제
	 * @author	: tsuam
	 * @date	: 2019. 7. 24.
	 * @method	: deleteMasterApproval
	 * @return	: void
	 * @param master
	 * @throws Exception
	 */
	private void deleteMasterToApproval(ApprovalMaster master) throws Exception{
		
		
		if(master != null) {
			deleteApprovalLine(master, true);
			//deleteApprovalObjectLink(master);
			
			//PersistenceHelper.manager.delete(master);
		}
		
	}
	
	
	
	/**
	 * 
	 * @desc	: 결재 대상 링크 삭제
	 * @author	: tsuam
	 * @date	: 2019. 7. 23.
	 * @method	: deleteApprovalObjectLink
	 * @return	: void
	 * @param master
	 * @throws Exception
	 */
	private void deleteApprovalObjectLink(ApprovalMaster master)throws Exception {
		
		ApprovalObjectLink link = ApprovalHelper.manager.getApprovalObjectLink(master);
		
		PersistenceHelper.manager.delete(link);
		
	}
	
	
	/**
	 * 
	 * @desc	: 결재 라인 삭제
	 * @author	: tsuam
	 * @date	: 2019. 7. 23.
	 * @method	: deleteApprovalLine
	 * @return	: void
	 * @param master
	 * @param isLast
	 * @throws Exception
	 */
	private void deleteApprovalLine(ApprovalMaster master,boolean isLast)throws Exception {
		
		List<ApprovalLine> list = ApprovalHelper.manager.getApprovalRoleLine(master, "", "", isLast);
		WTSet ws = new WTHashSet();
		for(ApprovalLine line : list) {
			if(line.getRole().toString().equals(ApprovalUtil.ROLE_DRAFT)){
				continue;
			}
			ws.add(line);
		}
		PersistenceHelper.manager.delete(ws);
		
	}
	
	
	/**
	 * 
	 * @desc	: 결재 라인 스타트
	 * @author	: tsuam
	 * @date	: 2019. 7. 19.
	 * @method	: startApprovalLine
	 * @return	: void
	 * @param list
	 * @throws Exception
	 */
	@Deprecated
	private void startApprovalLine(List<ApprovalLine> list) throws Exception{
		WTCollection collection = new WTArrayList();
		for(ApprovalLine line : list) {
			line.setState(ApproveStateType.toApproveStateType(ApprovalUtil.STATE_LINE_ONGOING));
			line.setStartDate(new Timestamp(System.currentTimeMillis()));
			collection.add(line);
		}
		
		PersistenceHelper.manager.save(collection);
		
	}
	
	/**
	 * 
	 * @desc	: 결재 대상 과 결재 마스터 링크 생성
	 * @author	: tsuam
	 * @date	: 2019. 7. 18.
	 * @method	: createApprovalLink
	 * @return	: void
	 * @param obj
	 * @param master
	 * @throws Exception
	 */
	private void createApprovalLink(Persistable obj,ApprovalMaster master) throws Exception{
		ApprovalObjectLink link = ApprovalObjectLink.newApprovalObjectLink(obj, master);
		PersistenceHelper.manager.save(link);
	}
	
	
	@Override
	public void createTemplate(Map<String,Object> reqMap) throws Exception {
		
		String title = StringUtil.checkNull((String) reqMap.get("title"));
		
		List<Map<String, Object>> discussList = (List<Map<String, Object>>) reqMap.get("DISCUSS");
		List<Map<String, Object>> approveList = (List<Map<String, Object>>) reqMap.get("APPROVE");
		List<Map<String, Object>> receiveList = (List<Map<String, Object>>) reqMap.get("RECEIVE");
		
		ArrayList<String> discussUser = ApprovalUtil.getListMapToList(discussList);
		ArrayList<String> approveUser = ApprovalUtil.getListMapToList(approveList);
		ArrayList<String> receiveUser = ApprovalUtil.getListMapToList(receiveList);
		
		ApprovalLineTemplate temp = ApprovalLineTemplate.newApprovalLineTemplate();
		
		temp.setName(title);
		temp.setDiscussList(discussUser);
		temp.setApproveList(approveUser);
		temp.setReceiveList(receiveUser);
		temp.setOwner(SessionHelper.manager.getPrincipalReference());
		
		PersistenceHelper.manager.save(temp);
	}
	
	@Override
	public void deleteTemplate(Map<String,Object> reqMap) throws Exception {
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			List<Map<String, Object>> deleteItemList = (List<Map<String, Object>>) reqMap.get("deleteItemList");
			
			for(Map<String, Object> item : deleteItemList) {
				String oid = StringUtil.checkNull((String) item.get("oid"));
				
				if(oid.length() > 0) {
					ApprovalLineTemplate temp = (ApprovalLineTemplate) CommonUtil.getObject(oid);
					
					PersistenceHelper.manager.delete(temp);
				}
			}
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}

	@Override
	public void approveAction(Map<String,Object> reqMap) throws Exception {
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String type = StringUtil.checkNull((String) reqMap.get("type"));
			
			if(type.equals("tempStorage")) {  //임시저장
				approveTempAction(reqMap);
			}else {  //결재
				approveWorkingAction(reqMap);
			}
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}	
		//ApprovalUtil.getRoleTypeList(approvalList);
		
	}
	
	/**
	 * drop 
	 */
	@Override
	public void dropAction(Map<String,Object> reqMap) throws Exception {
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String lineState = ApprovalUtil.STATE_LINE_COMPLETE;
			String description = StringUtil.checkNull((String) reqMap.get("description"));
			ApprovalLine line = (ApprovalLine)CommonUtil.getObject(oid);
			
			
			String role = line.getRole().getDisplay();
			String state = ApproveStateType.toApproveStateType(lineState).getDisplay();
			state = role +"("+state+")";
			WTPrincipal wtprincipal = SessionHelper.manager.getPrincipal();
			
			description = " [DROP] " + description;
			
			/*자신의 결재 라인  상태 변경*/
			line = changeStateApprovalLine(line, description, lineState,false,true);
			
			/*마스터 상태 변경*/
			String masterState = ApprovalUtil.STATE_MASTER_COMPLETED;
			
			ApprovalMaster master = changeStateApprovalMaster(line.getMaster(), masterState);
			Persistable per = ApprovalHelper.manager.getApprovalObject(master);
			
			/*결재 History 생성*/
			saveWFHistory((WTObject)per, state, description, wtprincipal);
			
			/*Drop 메일 발송*/
			sendDropMail(per);
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}	
	}
	
	/**
	 * 위임
	 */
	@Override
	public void delegateAction(Map<String,Object> reqMap) throws Exception {
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String delegateUser = StringUtil.checkNull((String) reqMap.get("delegateUser"));//위임 유저
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); //결재 라인 
			
			LOGGER.info("delegateUser =" + delegateUser);
			LOGGER.info("oid =" + oid);
			People pp = (People)CommonUtil.getObject(delegateUser);
			WTPrincipal wtprincipal = (WTPrincipal)pp.getUser();
			ApprovalLine line = (ApprovalLine)CommonUtil.getObject(oid);
			String roleType = line.getRole().toString();
			int seq = line.getSeq();
			String state = line.getState().toString();
			int ver = line.getVer();
			
			List<String> appLine = new ArrayList<String>();
			appLine.add(delegateUser);
			
			//위임 결재 라인 생성
			createApprovalLine(line.getMaster(), appLine, roleType, seq, state, ver);
			
			//위임 결재 라인 업데이트
			String description = line.getOwner().getFullName()+"에서 "+pp.getName()+"으로 위임 되었습니다.";
			String lineState = ApprovalUtil.STATE_LINE_DELEGATE;
			
			changeStateApprovalLine(line, description, lineState, false, true);
			
			Persistable per = ApprovalHelper.manager.getApprovalObject(line.getMaster());
			
			//결재 history
			saveWFHistory((WTObject)per, lineState, description, wtprincipal);
			
			//위임 메일
			LOGGER.info("============= 위임 메일 발송=============");
			
			Persistable userObject = CommonUtil.getObject(delegateUser);
			WTUser user = null;
			if(userObject instanceof People) {
				People ppuser = (People)CommonUtil.getObject(delegateUser);
				user = ppuser.getUser();
			}else {
				user = (WTUser)userObject;
			}
			LOGGER.info("============= user :"+ user);
			if(user != null){
				sendDelegateMail(line, user);
			}
			LOGGER.info("============= 위임 메일 발송=============");
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}	
		//ApprovalUtil.getRoleTypeList(approvalList);
		
	}
	
	/**
	 * 회수
	 */
	@Override
	public void recallAction(Map<String,Object> reqMap) throws Exception {
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); //결재 객체 
			
			Persistable per = (Persistable) CommonUtil.getObject(oid);
			
			ApprovalMaster master = ApprovalHelper.manager.getApprovalMaster(per);
			
			/*마스터 상태 변경 : 임시저장*/
			String masterState = ApprovalUtil.STATE_MASTER_TEMP_STORAGE;
			
			master = changeStateApprovalMaster(master, masterState);
			
			//기안 라인
			ApprovalLine draftLine = ApprovalHelper.manager.getApprovalRoleFirst(master, ApprovalUtil.ROLE_DRAFT, "", true);
			WTPrincipalReference ownerRef = draftLine.getOwner();
			WTUser owner = (WTUser) ownerRef.getObject();
			int ver = draftLine.getVer();
			
			/*현재 결재라인 최신여부 false*/
			List<ApprovalLine> lineList = ApprovalHelper.manager.getApprovalLastLineAll(master);
			for(ApprovalLine line : lineList) {
				line.setLast(false);
				String description = line.getDescription();
				line.setDescription(StringUtil.checkNull(description) + " 회수되었습니다.");
				PersistenceHelper.manager.save(line);
			}
			
			//기안 Role 생성(상태 : 진행중)
			List<String> draftList = new ArrayList<>();
			draftList.add(CommonUtil.getOIDString(owner));
			createApprovalLine(master, draftList,ApprovalUtil.ROLE_DRAFT,0,ApprovalUtil.STATE_LINE_ONGOING, ver + 1);
			
			List<ApprovalLine> list = ApprovalHelper.manager.getApprovalRoleLine(master, ApprovalUtil.ROLE_DRAFT, "", true);
			
			//기안자 라인
			draftLine = list.get(0);
			
			draftLine.setDescription("이전 결재 회수 후 새로운 결재 기안입니다.");
			
			draftLine = (ApprovalLine) PersistenceHelper.manager.modify(draftLine);
			
			//per 객체 상태 변경
			if(per instanceof RevisionControlled) {	//부품, 도면, 문서 
				String vrOid = CommonUtil.getVROID(per);
				Persistable vrPer = CommonUtil.getObject(vrOid);
				
				if(per instanceof WTPart) {
					changeStatePart((WTPart)vrPer, "INWORK");
				} else {
					LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged)vrPer, State.toState("INWORK"), false);
				}
			} else { //배포, 일괄결재, ECR, ECA, ECNECCB
				if(per instanceof MultiApproval) {
					
					String ownerName = SessionHelper.manager.getPrincipal().getName();
					SessionHelper.manager.setAdministrator();
					
					MultiApproval multi = (MultiApproval) per;
					multi.setState(master.getState());
					PersistenceHelper.manager.modify(multi);
					
					//일괄 결재 대상 상태 변경
					List<Persistable> objList = MultiApprovalHelper.manager.getMultiApprovalObjectList(multi);
					
					WTList wlist = new WTArrayList();
					
					if(multi.getObjectType().equals("doc") || multi.getObjectType().equals("epm") ){
						for(Persistable revPer : objList) {
							wlist.add(revPer);
						}
						LifeCycleHelper.service.setLifeCycleState(wlist, State.toState("INWORK"), false);
					}else{ //부품인 경우
						for(Persistable revPer : objList) {
							WTPart part= (WTPart)revPer;
							changeStatePart(part, "INWORK");
						}
					}
					
					SessionHelper.manager.setPrincipal(ownerName);
				} else {
					LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged)per, State.toState("INWORK"), false);
				}
			}
			
			//설변 결재 회수 history 
			WTPrincipal wtprincipal = SessionHelper.manager.getPrincipal();
			saveWFHistory((WTObject)per, "회수", "", wtprincipal);
			
			//회수 알림 메일 : 없음
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}	
		//ApprovalUtil.getRoleTypeList(approvalList);
		
	}
	
	/**
	 * 
	 * @desc	: 결재 관련 메리 발송 데이터 수집
	 * @author	: tsuam
	 * @date	: 2019. 7. 31.
	 * @method	: approvalMail
	 * @return	: void
	 * @param line
	 */
	@Override
	public void sendApprovalMail(ApprovalLine line) throws Exception {
		
		
		Hashtable<String, Object> mailHash = ApprovalMailForm.setApprovealMailInfo(line);
		if(mailHash.size() > 0 ) {
			mailHash.put(E3PSQueueHelper.queueName, E3PSQueueHelper.QueueName_Mail);
			mailHash.put(E3PSQueueHelper.className, E3PSQueueHelper.ClassName_Mail);
			mailHash.put(E3PSQueueHelper.methodName, E3PSQueueHelper.MethodName_Approval_Mail);
			E3PSQueueHelper.manager.createQueue(mailHash);
		}
	}
	
	@Override
	public boolean saveWFHistory(WTObject wtobject, String state, String comment, WTPrincipal wtprincipal )
	    	throws Exception {

			boolean saveOk = false;
			try {
				WTPrincipalReference userRef = WTPrincipalReference.newWTPrincipalReference(wtprincipal);
				People pp = PeopleHelper.manager.getPeople((WTUser)wtprincipal);
				String deptName = pp.getDepartment() == null ?  "지정안됨" : pp.getDepartment().getName() ;
	    
				WFHistory history = WFHistory.newWFHistory();
				history.setActivityName(state);
				history.setWfcomment(comment);
				history.setOwner(userRef);
	    		history.setDepartmentName(deptName);
	    
				if(wtobject instanceof RevisionControlled) {
					RevisionControlled rc=(RevisionControlled)wtobject;
					Versioned vc = (Versioned)rc;
					String version = VersionControlHelper.getVersionIdentifier(vc).getValue();
					history.setObjectVersion(version);
					history.setWfObject((WTObject)rc.getMaster());
				} else {
					history.setObjectVersion("");
					history.setWfObject(wtobject);
				}
				PersistenceHelper.manager.save(history);
			} catch (Exception e) {
				e.printStackTrace();
				saveOk = false;
			}
			return saveOk;
		}
	
	public void createApprovalMailQueue(ApprovalMaster master) {
		
	}
	
	
	
	/**
	 * 
	 * @desc	: 임시 저장  Action
	 * @author	: tsuam
	 * @date	: 2019. 7. 24.
	 * @method	: approveTempAction
	 * @return	: void
	 * @param reqMap
	 * @throws Exception
	 */
	private void approveTempAction(Map<String,Object> reqMap)throws Exception {
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String type = StringUtil.checkNull((String) reqMap.get("type"));
		String lineState = StringUtil.checkNull((String) reqMap.get("lineState"));
		String description = StringUtil.checkNull((String) reqMap.get("description"));
		
		ApprovalLine line = (ApprovalLine)CommonUtil.getObject(oid);
		Persistable per = ApprovalHelper.manager.getApprovalObject(line.getMaster());
		
		List<Map<String, Object>> approvalList = (List<Map<String, Object>>) reqMap.get("approvalList");
		String state_line = ApprovalUtil.STATE_LINE_COMPLETE;
		ApprovalMaster master = null;
		//결재선 재지정
		if(lineState.equals(ApprovalUtil.STATE_LINE_STANDING)) {		//임시 저장
			updateApproval(per, approvalList, ApprovalUtil.STATE_MASTER_TEMP_STORAGE, line );
			state_line =ApprovalUtil.STATE_LINE_ONGOING;
			
			master= ApprovalHelper.manager.getApprovalMaster(per);
			
		}else if(lineState.equals(ApprovalUtil.STATE_LINE_COMPLETE)) {	//결재 시작
			updateApproval(per, approvalList, ApprovalUtil.STATE_MASTER_APPROVING, line);
			
			if(per instanceof EChangeOrder2) {
				EChangeOrder2 eco = (EChangeOrder2) per;
				if(eco.getLifeCycleState().toString().equals("ECACOMPLETE") 
						|| eco.getLifeCycleState().toString().equals("RETURN")) {
					String[] states = { "APPROVING", "DISCUSSING", "REJECTED" };
					for (String state : states) {
					    master = ApprovalHelper.manager.getApprovalStateMaster(per, state);
					    if (master != null) {
					        break;
					    }
					}
				} else {
					master = ApprovalHelper.manager.getApprovalMaster(per);
				}
			} else {
				master = ApprovalHelper.manager.getApprovalMaster(per);
			}
			changeStatePersistable(per, master);
			
		}else if(lineState.equals(ApprovalUtil.STATE_LINE_CANCEL)) {  //결재 삭제
			deletePerApproval(per);
		}
		
		//의견 저장
		line.setDescription(description);
		
		//완료 및 완료 시간
		if(state_line.equals(ApprovalUtil.STATE_LINE_COMPLETE)){
			line.setState(ApproveStateType.toApproveStateType(state_line));
			line.setApproveDate(new Timestamp(System.currentTimeMillis()));
		}
		
		line = (ApprovalLine)PersistenceHelper.manager.save(line);
		
		//메일 발송
		if(state_line.equals(ApprovalUtil.STATE_LINE_COMPLETE)){
			sendApprovalMail(line);
		}
		
		
	}
	
	/**
	 * 
	 * @desc	: 결재 Action
	 * @author	: tsuam
	 * @date	: 2019. 7. 25.
	 * @method	: approveWorkiingAction
	 * @return	: void
	 * @param reqMap
	 * @throws Exception
	 */
	private void approveWorkingAction(Map<String,Object> reqMap)throws Exception {
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String lineState = StringUtil.checkNull((String) reqMap.get("lineState"));
		String description = StringUtil.checkNull((String) reqMap.get("description"));
		ApprovalLine line = (ApprovalLine)CommonUtil.getObject(oid);
		ApprovalMaster master = line.getMaster();
		Persistable per = ApprovalHelper.manager.getApprovalObject(master);
		String role = line.getRole().toString();
		String roleDisplay = line.getRole().getDisplay();
		
		String state = ApproveStateType.toApproveStateType(lineState).getDisplay();
		state = roleDisplay +"("+state+")";
		WTPrincipal wtprincipal = SessionHelper.manager.getPrincipal();
		
		/*자신의 결재 라인  상태 변경*/
		line = changeStateApprovalLine(line, description,lineState,false,true);
		
		/*결재 진행*/
		String masterState = "";
		if(lineState.equals(ApprovalUtil.STATE_LINE_REJECT)) { //반려
			masterState = ApprovalUtil.STATE_MASTER_REJECTED;
			updateApprovalLine(master);
		}else { //합의 ,이의, 수신
			masterState = nextApprovalStart(line);
		}
		/*마스터 상태 변경*/
		master = changeStateApprovalMaster(master, masterState);
		
		/*객체 상태 변경 수신 제외*/
		if(!role.equals(ApprovalUtil.ROLE_RECEIVE)){
			changeStatePersistable(per, master);
		}
		
		/*결재 History 생성*/
		saveWFHistory((WTObject)per, state, description, wtprincipal);
		
		//승인자 이고  승인 완료시
		if(role.equals(ApprovalUtil.ROLE_APPROVE) && masterState.equals("COMPLETED")){
			if( per instanceof EChangeOrder2){
				ApprovalData data = new ApprovalData(master);
			    data.completeAction(MASTER_APPROVED, null);
			}else if( per instanceof EChangeRequest2){
				ApprovalData data = new ApprovalData(master);
			    data.completeAction(MASTER_APPROVED, null);
			}else if( per instanceof EChangeActivity){
				ApprovalData data = new ApprovalData(master);
			    data.completeAction(MASTER_APPROVED, description);
			}else if( per instanceof CommonActivity){
				ApprovalData data = new ApprovalData(master);
			    data.completeAction(MASTER_APPROVED, null);
			}else if( per instanceof DistributeRegistration) {
				DistributeRegistration distributeReg = (DistributeRegistration) per;
				DistributeDocument distribute = distributeReg.getDistribute();
				DistributeRegToPartLink distributeRegPart = DistributeHelper.manager.getDistributeRegToPartLinkOid((DistributeRegistration)distributeReg);
				
				// 배포서버에 DistributeData 전송
				CPCService.service.insertDistribute(distribute , distributeReg);
				// 배포서버에 DistributePartData 전송
				CPCService.service.insertDistributePart(distributeReg);
				// 배포 첨부파일 리스트 전송 
				List<Map<String,Object>> ftpFileList = CPCService.service.insertDistributeAttachFile(distributeRegPart);
				// 배포 파일 FTP 업로드
				DistributeHelper.service.uploadDistFile(ftpFileList);
				
			}else {
				activeApproved(per);
			}
			//승인 완료시 전체  메일 발송
			if(!(per instanceof EChangeActivity)) {
				sendApprovedMail(per);
			}
			//반려시
		}else if(role.equals(ApprovalUtil.ROLE_APPROVE) && masterState.equals(ApprovalUtil.STATE_MASTER_REJECTED)){
			
			//반려 결재에 대한 E3PSChangeActivity ,ECCB
			rejectApproval(per);
			//반려 메일
			sendRejectMail(per);
		}

		/*메일 발송*/
		//if(per instanceof E3PSEChangeActivity || per instanceof ECNECCB){
			
		//} else {
			LOGGER.info("::::::: approveWorkingAction sendApprovalMail ::::::");
			if(!masterState.equals(ApprovalUtil.STATE_MASTER_REJECTED)){
				sendApprovalMail(line);
			}
			
		//}
	}
	
	/**
	 * 
	 * @desc	: 반려 Action
	 * @author	: tsuam
	 * @date	: 2019. 7. 25.
	 * @method	: rejectApproval
	 * @return	: void
	 * @param line
	 * @throws Exception
	 */
	private void rejectApproval(Persistable per) throws Exception {
		
		
	}
	
	/**
	 * 
	 * @desc	: Next 라인 Start
	 * @author	: tsuam
	 * @date	: 2019. 11. 14.
	 * @method	: nextApprovalStart
	 * @return	: String
	 * @param line
	 * @return
	 * @throws Exception
	 */
	private String  nextApprovalStart(ApprovalLine line) throws Exception {
		
		String role = line.getRole().toString();
		ApprovalMaster master = line.getMaster();
		List<ApprovalLine> nextLineList =  new ArrayList<ApprovalLine>();
		String masterState = ApprovalUtil.STATE_MASTER_DISCUSSING;

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
		/*
		LOGGER.info(">>>>> stateChaneApprovalLine After S<<<<");
		LOGGER.info(CommonUtil.getOIDString(line));
		LOGGER.info(line.getRole().toString());
		LOGGER.info(line.getDescription());
		LOGGER.info(line.getState().toString());
		LOGGER.info(line.getApproveDate());
		LOGGER.info(">>>>> stateChaneApprovalLine After E<<<<");
		*/
		return line;
	}
	
	/**
	 * 
	 * @desc	: 결재 마스터 상태 변경
	 * @author	: tsuam
	 * @date	: 2019. 7. 31.
	 * @method	: changeStateApprovalMaster
	 * @return	: ApprovalMaster
	 * @param master
	 * @param masterState
	 * @return
	 * @throws Exception
	 */
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
	
	
	/**
	 * 
	 * @desc	: 결재 대상 객체 상태 변경
	 * @author	: tsuam
	 * @date	: 2019. 7. 31.
	 * @method	: changeStatePersistable
	 * @return	: void
	 * @param per
	 * @param master
	 * @throws Exception
	 */
	private void changeStatePersistable(Persistable per,ApprovalMaster master) throws Exception{
		
		LOGGER.info("============ stateChangeObject S===========");
		if(per == null) {
			per = ApprovalHelper.manager.getApprovalObject(master);
		}
		String ownerName = SessionHelper.manager.getPrincipal().getName();
		SessionHelper.manager.setAdministrator();
		
		String setState = ApprovalUtil.getMasterToObjectState(per, master.getState().toString());
		LOGGER.info("============ master.getState().toString() : " + master.getState().toString() +",setState =" + setState );
		if(per instanceof WTPart) {
			
			LOGGER.info("============ WTPart setState =" + setState);
			
			
			if(setState.length() >0 ) {
				
				changeStatePart((WTPart)per, setState);
				//LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged)per, State.toState(setState), false);
			}
			
			
		}else if(per instanceof MultiApproval) {
			changeStateMulti((MultiApproval)per, master);
		}
		else if(per instanceof EChangeOrder2) {
			String oid =  CommonUtil.getOIDString(per);
			EChangeOrder2 eco = (EChangeOrder2) CommonUtil.getObject(oid);
			ECOData data = new ECOData(eco);
			
			if("TEMP_STORAGE".equals(master.getState().toString()) &&
					eco.getLifeCycleState().toString().equals("ECAWORKING")) {
				E3PSWorkflowHelper.manager.changeLCState(eco, "ECACOMPLETE");
			}
			else if("TEMP_STORAGE".equals(master.getState().toString())) {
				E3PSWorkflowHelper.manager.changeLCState(eco, "INWORK");
			}else if("APPROVING".equals(setState)) {
				if(eco.getLifeCycleState().toString().equals("RETURN")) {
					if(data.getEcaState().equals(ChangeService.ACTIVITY_APPROVED)) {
						E3PSWorkflowHelper.manager.changeLCState(eco, "AFTERAPPROVING");
					} else {
						E3PSWorkflowHelper.manager.changeLCState(eco, "BEFOREAPPROVING");
					}
				} else {
					if(eco.getLifeCycleState().toString().equals("ECACOMPLETE") ||
							data.getEcaState().equals(ChangeService.ACTIVITY_APPROVED)) {
						E3PSWorkflowHelper.manager.changeLCState(eco, "AFTERAPPROVING");
					} else {
						E3PSWorkflowHelper.manager.changeLCState(eco, "BEFOREAPPROVING");
					}
				}
			}else if("REJECTED".equals(setState)) {
				if(data.getEcaState().equals(ChangeService.ACTIVITY_APPROVED)) {
					E3PSWorkflowHelper.manager.changeLCState(eco, "AFTERAPPROVING");
				} else {
					E3PSWorkflowHelper.manager.changeLCState(eco, "RETURN");
				}
			}else {
				E3PSWorkflowHelper.manager.changeLCState(eco, setState);
			}
		}
		else if(per instanceof EChangeActivity) {
			String oid =  CommonUtil.getOIDString(per);
			EChangeActivity eca = (EChangeActivity) CommonUtil.getObject(oid);
			
			if("RETURN".equals(setState)) {
				eca.setDescription(eca.getDescription()+"_"+ChangeService.ACTIVITY_CANCELED);
				PersistenceHelper.manager.modify(eca);
				if(eca.getOrder() instanceof EChangeOrder2){
					EChangeOrder2 eco = (EChangeOrder2)eca.getOrder();
					LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged)eco, State.toState(ChangeService.ECO_CANCELLED), false);
					PersistenceHelper.manager.modify(eco);
					ChangeHelper.manager.changeActivityApprovalState((Persistable)eco, setState);
				}else if(eca.getOrder() instanceof EChangeRequest2){
					EChangeRequest2 ecr = (EChangeRequest2)eca.getOrder();
					LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged)ecr, State.toState(ChangeService.ECO_CANCELLED), false);
					PersistenceHelper.manager.modify(ecr);
					ChangeHelper.manager.changeActivityApprovalState((Persistable)ecr, setState);
				}
			}else {
				eca.setActiveState(setState);
				PersistenceHelper.manager.modify(eca);
			}
		}else {
			
			LOGGER.info("============ 1.WTDocument,EPMDocument,Distriubute,E3PSEChange,setState =" + setState +", per =" + per);
			SessionHelper.manager.setAdministrator();
			
			//버전충 최신 Iteraion 
			if(per instanceof RevisionControlled){
				String vrOid = CommonUtil.getVROID(per);
				per = CommonUtil.getObject(vrOid);
			}
			
			if(setState.length() >0 ) {
				LOGGER.info("============ 2.WTDocument,Distriubute,E3PSEChange,E3PSChangeActivity setState =" + setState +", per =" + per);
				LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged)per, State.toState(setState), false);
			}
			//String ownerName = SessionHelper.manager.getPrincipal().getName();
			
		}
		
		SessionHelper.manager.setPrincipal(ownerName);
		
		LOGGER.info("============ stateChangeObject E===========");
	}
	
	/**
	 * 
	 * @desc	: 반려시 기존 결재 라인 업데이트 , 신규 생성
	 * @author	: tsuam
	 * @date	: 2019. 7. 29.
	 * @method	: copyApprovalLine
	 * @return	: void
	 * @param master
	 * @throws Exception
	 */
	private void updateApprovalLine(ApprovalMaster master) throws Exception{
		
		 List<ApprovalLine> list = ApprovalHelper.manager.getApprovalLastLineAll(master);
		 WTCollection wc = new WTArrayList();
		 int ver =0;
		 int seq = 0;
		 for(ApprovalLine line : list) {
			
			 line.setLast(false);
			 ver = line.getVer();
			 
			 wc.add(line);
		 }
		PersistenceHelper.manager.save(wc);
		 
		Map<String , List<String>> approvalMap =  ApprovalUtil.getLineRoleTypeList(list);
		
		List<String> draftList 	 = approvalMap.get(ApprovalUtil.ROLE_DRAFT);
		List<String> discussList = approvalMap.get(ApprovalUtil.ROLE_DISCUSS);
		List<String> approveList = approvalMap.get(ApprovalUtil.ROLE_APPROVE);
		List<String> receiveList = approvalMap.get(ApprovalUtil.ROLE_RECEIVE);
		
		
		ver = ver+1;
		seq = 0;
		//결재라인 기안
		seq = createApprovalLine(master, draftList,ApprovalUtil.ROLE_DRAFT,seq,ApprovalUtil.STATE_LINE_ONGOING,ver);
		//결재라인 합의
		seq = createApprovalLine(master, discussList,ApprovalUtil.ROLE_DISCUSS,seq,ApprovalUtil.STATE_LINE_STANDING,ver);
		//결재라인 승인
		seq = createApprovalLine(master, approveList,ApprovalUtil.ROLE_APPROVE,seq,ApprovalUtil.STATE_LINE_STANDING,ver);
		//결재랑인 수신
		seq = createApprovalLine(master, receiveList,ApprovalUtil.ROLE_RECEIVE,seq,ApprovalUtil.STATE_LINE_STANDING,ver);
		
	}
	
	/**
	 * 
	 * @desc	: 일괄 결재 대상 상태 변경 및 일괄 결재 객체 상태 변경
	 * @author	: tsuam
	 * @date	: 2019. 8. 6.
	 * @method	: changeStateMulti
	 * @return	: void
	 * @param multi
	 * @param master
	 * @throws Exception
	 */
	private void changeStateMulti(MultiApproval multi,ApprovalMaster master) throws Exception{
		
		String ownerName = SessionHelper.manager.getPrincipal().getName();
		SessionHelper.manager.setAdministrator();
		//일괄 결재 상태 변경
		multi.setState(master.getState());
		PersistenceHelper.manager.modify(multi);
		
		//일괄 결재 대상 상태 변경
		List<Persistable> list = MultiApprovalHelper.manager.getMultiApprovalObjectList(multi);
		WTList wlist = new WTArrayList();
		String setState = "";
		
		for(Persistable per : list) {
			setState = ApprovalUtil.getMasterToObjectState(per, master.getState().toString());
			wlist.add(per);
		}
		
		if(multi.getObjectType().equals("doc") || multi.getObjectType().equals("epm") ){
			
			if(setState.length() > 0 && wlist.size() > 0 ) {
				
				LifeCycleHelper.service.setLifeCycleState(wlist, State.toState(setState), false);
				
			}
			
		}else{ //부품인 경우
			for(Persistable per : list) {
				
				WTPart part= (WTPart)per;
				
				changeStatePart(part, setState);
			}
			
		}
		
		SessionHelper.manager.setPrincipal(ownerName);
		
		
	}
	
	/**
	 * 
	 * @desc	:
	 * @author	: 엄태식
	 * @date	: 2019. 8. 28.
	 * @method	: changeStatePart
	 * @return	: void
	 * @param part
	 * @param setState
	 * @throws Exception
	 */
	@Override
	public void changeStatePart(WTPart part,String setState) throws Exception {
		
		//버전충 최신 Iteraion 
		String vrOid = CommonUtil.getVROID(part);
		part = (WTPart)CommonUtil.getObject(vrOid);
		
		//부품 상태 변경
		LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged)part, State.toState(setState), false);
		
		LOGGER.info("changeStatePart =" + part.getNumber() +", setState =" + setState);
		
		//3D 도면 상태 변경
		EPMDocument epm3D = EpmHelper.manager.getEPMDocument(part);
		//2D 도면 상태 변경
		if(epm3D != null){
			
			LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged)epm3D, State.toState(setState), false);
			
			LOGGER.info("changeStatePart =" + epm3D.getNumber() +", setState =" + setState);
			
			List<EPMReferenceLink> list = EpmHelper.manager.getEPMReferenceList((EPMDocumentMaster)epm3D.getMaster());
			
			for(EPMReferenceLink link : list){
				EPMDocument epm2D = link.getReferencedBy();
				LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged)epm2D, State.toState(setState), false);
				LOGGER.info("changeStatePart =" + epm2D.getNumber() +", setState =" + setState);
				
			}
		}
		if(setState.equals("APPROVED")) {
			//ERPInterface.send(part);
		}
		
		
	}
	
	/**
	 * 
	 * @desc	: 승인 완료후 Active 
	 *          : 배포,ECR ,ECNECCB,ECA,ECN
	 * @author	: tsuam
	 * @date	: 2019. 9. 18.
	 * @method	: activeApproved
	 * @return	: void
	 */
	private void activeApproved(Persistable pp) throws Exception{
		LOGGER.info("============ activeApproved ===========");
		
//		if(pp instanceof DistributeDocument){
//			LOGGER.info("============ Approved  DistributeDocument Action Start===========");
//			
//			Hashtable ht = new Hashtable();
//			ht.put(E3PSQueueHelper.queueName, E3PSQueueHelper.QueueName_Distribute);
//			ht.put(E3PSQueueHelper.className, E3PSQueueHelper.ClassName_Distribute);
//			ht.put(E3PSQueueHelper.methodName, E3PSQueueHelper.MethodName_Distribute);
//			ht.put("oid", CommonUtil.getOIDString(pp));
//			E3PSQueueHelper.manager.createQueue(ht);
//			LOGGER.info("============ Approved  DistributeDocument Action END===========");
//		}else 
		if(pp instanceof WTPart) {
			
			LOGGER.info("============ activeApproved WTPart ERP 전송 Start===========");
			
			Hashtable ht = new Hashtable();
			ht.put(E3PSQueueHelper.queueName, E3PSQueueHelper.QueueName_ERP_PART);
			ht.put(E3PSQueueHelper.className, E3PSQueueHelper.ClassName_ERP_PART);
			ht.put(E3PSQueueHelper.methodName, E3PSQueueHelper.MethodName_ERP_PART);
			ht.put("oid", CommonUtil.getOIDString(pp)); 
			E3PSQueueHelper.manager.createQueue(ht);
			
			LOGGER.info("============ activeApproved WTPart ERP 전송 END===========");
		}if( pp instanceof MultiApproval){
			MultiApproval multi = (MultiApproval) pp;
			if(multi.getObjectType().equals("part")){
				LOGGER.info("============ activeApproved MultiApproval WTPart ERP 전송 Start===========");
				
				Hashtable ht = new Hashtable();
				ht.put(E3PSQueueHelper.queueName, E3PSQueueHelper.QueueName_ERP_PART);
				ht.put(E3PSQueueHelper.className, E3PSQueueHelper.ClassName_ERP_PART);
				ht.put(E3PSQueueHelper.methodName, E3PSQueueHelper.MethodName_ERP_PART);
				ht.put("oid", CommonUtil.getOIDString(pp)); 
				E3PSQueueHelper.manager.createQueue(ht);
				
				LOGGER.info("============ activeApproved MultiApproval WTPart ERP 전송 END===========");
			}
		}
		
		
	}
	
	
	/**
	 * 
	 * @desc	: 결재 승인 완료시 메일 전송
	 * 대상 : 부품,도면,ECN,배포
	 * @author	: tsuam
	 * @date	: 2019. 10. 29.
	 * @method	: sendApprovedMail
	 * @return	: void
	 * @param pp
	 */
	private void sendApprovedMail(Persistable pp) throws Exception{
		
		Hashtable<String, Object> mailHash = ApprovalMailForm.setApprovedMailInfo(pp);
		if(mailHash.size() > 0 ) {
			mailHash.put(E3PSQueueHelper.queueName, E3PSQueueHelper.QueueName_Mail);
			mailHash.put(E3PSQueueHelper.className, E3PSQueueHelper.ClassName_Mail);
			mailHash.put(E3PSQueueHelper.methodName, E3PSQueueHelper.MethodName_Approval_Complete_Mail);
			E3PSQueueHelper.manager.createQueue(mailHash);
		}
	}
	
	/**
	 * 
	 * @desc	: 반려 메일 전송
	 * @author	: tsuam
	 * @date	: 2020. 2. 25.
	 * @method	: sendReturnMail
	 * @return	: void
	 * @param pp
	 * @throws Exception
	 */
	private void sendRejectMail(Persistable pp) throws Exception{
		
		Hashtable<String, Object> mailHash = ApprovalMailForm.setReturnMailInfo(pp);
		if(mailHash.size() > 0 ) {
			mailHash.put(E3PSQueueHelper.queueName, E3PSQueueHelper.QueueName_Mail);
			mailHash.put(E3PSQueueHelper.className, E3PSQueueHelper.ClassName_Mail);
			mailHash.put(E3PSQueueHelper.methodName, E3PSQueueHelper.MethodName_Approval_Reject_Mail);
			E3PSQueueHelper.manager.createQueue(mailHash);
		}
	}
	
	/**
	 * 
	 * @desc	: 위임 메일
	 * @author	: plmadmin
	 * @date	: 2020. 2. 26.
	 * @method	: sendDelegateMail
	 * @return	: void
	 * @param beforeLine
	 * @param delegateWTUser
	 * @throws Exception
	 */
	private void sendDelegateMail(ApprovalLine beforeLine,WTUser delegateWTUser) throws Exception{
		Hashtable<String, Object> mailHash = ApprovalMailForm.setDelegateMailInfo(beforeLine,delegateWTUser);
		
		if(mailHash.size() > 0 ) {
			mailHash.put(E3PSQueueHelper.queueName, E3PSQueueHelper.QueueName_Mail);
			mailHash.put(E3PSQueueHelper.className, E3PSQueueHelper.ClassName_Mail);
			mailHash.put(E3PSQueueHelper.methodName, E3PSQueueHelper.MethodName_Approval_Delegate_Mail);
			E3PSQueueHelper.manager.createQueue(mailHash);
		}
	}
	
	/**
	 * 
	 * @desc	: Drop
	 * @author	: plmadmin
	 * @date	: 2020. 2. 26.
	 * @method	: sendDelegateMail
	 * @return	: void
	 * @param beforeLine
	 * @param delegateWTUser
	 * @throws Exception
	 */
	private void sendDropMail(Persistable pp) throws Exception{
		Hashtable<String, Object> mailHash = ApprovalMailForm.setDropMailInfo(pp);
		
		if(mailHash.size() > 0 ) {
			mailHash.put(E3PSQueueHelper.queueName, E3PSQueueHelper.QueueName_Mail);
			mailHash.put(E3PSQueueHelper.className, E3PSQueueHelper.ClassName_Mail);
			mailHash.put(E3PSQueueHelper.methodName, E3PSQueueHelper.MethodName_Approval_Drop_Mail);
			E3PSQueueHelper.manager.createQueue(mailHash);
		}
	}

	@Override
	public void appendStateSearchCondition(QuerySpec qs, Class targetClass, int targetIdx, String state)
			throws QueryException {
		int idx = qs.addClassList(ApprovalMaster.class,false);
		int linkIdx = qs.addClassList(ApprovalObjectLink.class,false);
		qs.appendWhere(new SearchCondition(targetClass,"thePersistInfo.theObjectIdentifier.id",ApprovalObjectLink.class,"roleAObjectRef.key.id"),new int[]{targetIdx,linkIdx});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(ApprovalObjectLink.class,"roleBObjectRef.key.id",ApprovalMaster.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{linkIdx,idx});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(ApprovalMaster.class,ApprovalMaster.STATE,"=",state),new int[]{idx});
		
	}

	@Override
	public void registApproval(Persistable per, Map<String, Object> reqMap) throws WTException {
		Transaction trx = new Transaction();
        try{
            trx.start();

            ReferenceFactory rf = new ReferenceFactory();
            
            WTUser user = (WTUser)SessionHelper.manager.getPrincipal();
            QueryResult masterqr = getApprovalMaster(per,true);
            ApprovalMaster am = null;
            int active = 0;
            
            boolean isApprovalEnabled = true; //결재 상신 가능 상태
            String objectType = "";
            String title = "";
            if(per instanceof EChangeActivity){
            	objectType = "ECA";
            	title = "ECA";
            }else if(per instanceof EChangeOrder2){
            	objectType = "ECO";
            	title = "ECO";
            }else if(per instanceof EChangeRequest2){
            	objectType = "ECR";
            	title = "ECR";
            }
            if(masterqr.size()==0){
            	//결재 마스터 신규 연결
            	am = ApprovalMaster.newApprovalMaster();
            	am.setState(ApproveStateType.toApproveStateType(ApprovalUtil.STATE_MASTER_APPROVING));
                am.setOwner(SessionHelper.manager.getPrincipalReference());
                am.setActive(0);
                am.setObjectType(objectType);
                am.setTitle(title);
                setPersistableState(per, "INWORK");
                am.setActive(0);
                am = (ApprovalMaster)PersistenceHelper.manager.save(am);
                ApprovalObjectLink link = ApprovalObjectLink.newApprovalObjectLink(per,am);
                PersistenceHelper.manager.save(link);
            }else{
            	
            	
            	//결재 마스터 갱신 - 반려후 재상신 일경우를 대비함
            	while(masterqr.hasMoreElements()){
            		Object[] o = (Object[])masterqr.nextElement();
            		am = (ApprovalMaster)o[0];
            		setPersistableState(per, "INWORK");
            		am.setState(ApproveStateType.toApproveStateType(ApprovalUtil.STATE_MASTER_APPROVING));
                    am.setOwner(SessionHelper.manager.getPrincipalReference());
	            	am.setTitle(title);
	            	am = (ApprovalMaster)PersistenceHelper.manager.modify(am);
	            	ApprovalObjectLink link = (ApprovalObjectLink)o[1];
	            	link.setObj(per);
	            	PersistenceHelper.manager.modify(link);
            	}

            	if(isApprovalEnabled){
            		//결재 라인 삭제 - 반려후 재상신 일경우를 대비함
	            	active = am.getActive();

	            	if(active==0){
	            		active = -1;
	            	}
	            	
		            QueryResult qr = getApprovalLine(am);
		            if( qr != null ) {
		                while(qr.hasMoreElements()){
		                    ApprovalLine line = (ApprovalLine)qr.nextElement();
		                    int lseq = line.getSeq();
		                    if(lseq>active){
		                        PersistenceHelper.manager.delete(line);
		                    }else{
		                    	line.setHistoryCheck(true);
		                    	PersistenceHelper.manager.modify(line);
		                    }
		                }
		            }
		            
		            am.setActive(active+1);
	            	am = (ApprovalMaster)PersistenceHelper.manager.modify(am);
		            active++;
            	}
            }
           // LOGGER.info("user.getName() > "+user.getName()+"//"+"am.getOwner2() > "+am.getOwner2());
            if(isApprovalEnabled){
	            //요청자 
	            ApprovalLine line = ApprovalLine.newApprovalLine();
	            Ownership ownership = Ownership.newOwnership(user);
    			line.setOwner(ownership.getOwner());
    			line.setRole(ApproveRoleType.toApproveRoleType(ApprovalUtil.ROLE_DRAFT));
    			line.setState(ApproveStateType.toApproveStateType(ApprovalUtil.STATE_LINE_STANDING));
    			
	            line.setName(WORKING_REPORTER);
	            line.setSeq(active++);
	            line.setReadCheck(true);
	            line.setMaster(am);
	            line.setStepName(APPROVE_REQUEST);
	            line.setSeq(0);
	            line = (ApprovalLine)PersistenceHelper.manager.save(line);
	            
	            //결재라인 지정
//	            String[] agreeUser = ParamUtil.getValues(param,"agreeUser");
//	            String[] approveUser = ParamUtil.getValues(param,"approveUser");
//	            String[] notifyUser = ParamUtil.getValues(param,"notifyUser");
	            List<Map<String, Object>> approvalList = (List<Map<String, Object>>) reqMap.get("appList");
	            LOGGER.info("approvalList size -> "+approvalList.size());
				Map<String , List<String>> approvalMap = ApprovalUtil.getRoleTypeList(approvalList);
				
				List<String> agreeUser = approvalMap.get(ApprovalUtil.ROLE_DISCUSS);
				List<String> approveUser = approvalMap.get(ApprovalUtil.ROLE_APPROVE);
				List<String> notifyUser = approvalMap.get(ApprovalUtil.ROLE_RECEIVE);
				boolean agreeFirst = false;
				if(agreeUser.size() > 0) {
					agreeFirst = true;
				}else {
					agreeFirst = false;
				}
	            Ownership ownership2 = null;
	            for(String agree:agreeUser){
	            	 LOGGER.info("agreeUser >"+agree);
                	People puser = (People)CommonUtil.getObject(agree);
                	user = puser.getUser();
	            	line = ApprovalLine.newApprovalLine();
	           		line.setName(WORKING_DISCUSSER);
	                line.setSeq(active++);
	                line.setReadCheck(false);
	                line.setMaster(am);
	                line.setStepName(APPROVE_DISCUSS);
	                
	                ownership2 = Ownership.newOwnership(user);
	                line.setSeq(1);
	    			line.setOwner(ownership2.getOwner());
	    			line.setRole(ApproveRoleType.toApproveRoleType(ApprovalUtil.ROLE_DISCUSS));
	    			if(agreeFirst) {
	    				line.setState(ApproveStateType.toApproveStateType(ApprovalUtil.STATE_LINE_ONGOING));
	    			}else {
	    				line.setState(ApproveStateType.toApproveStateType(ApprovalUtil.STATE_LINE_STANDING));
	    			}
	    			
	                line = (ApprovalLine)PersistenceHelper.manager.save(line);
	           }
	            
	            for(String approve:approveUser){
	            	 LOGGER.info("approveUser >"+approve);
	            	People puser = (People)CommonUtil.getObject(approve);
                	user = puser.getUser();
	            	line = ApprovalLine.newApprovalLine();
	           		line.setName(WORKING_REVIEWER);
	                line.setSeq(2);
	                line.setReadCheck(false);
	                line.setMaster(am);
	                line.setStepName(APPROVE_POSTAPPROVE);
	                
	                ownership2 = Ownership.newOwnership(user);
	    			line.setOwner(ownership2.getOwner());
	    			line.setRole(ApproveRoleType.toApproveRoleType(ApprovalUtil.ROLE_APPROVE));
	    			if(agreeFirst) {
	    				line.setState(ApproveStateType.toApproveStateType(ApprovalUtil.STATE_LINE_STANDING));
	    			}else {
	    				line.setState(ApproveStateType.toApproveStateType(ApprovalUtil.STATE_LINE_ONGOING));
	    				agreeFirst = true;
	    			}
	    			
	                line = (ApprovalLine)PersistenceHelper.manager.save(line);
	           }
	           
	            for(String notify:notifyUser){
	        	   LOGGER.info("notifyUser >"+notify);
	        	   People puser = (People)CommonUtil.getObject(notify);
	        	   user = puser.getUser();
	            	line = ApprovalLine.newApprovalLine();
	           		line.setName(WORKING_TEMP);
	                line.setSeq(3);
	                
	                line.setReadCheck(false);
	                line.setMaster(am);
	                line.setStepName(APPROVE_NOTIFICATE);
	                
	                ownership2 = Ownership.newOwnership(user);
	    			line.setOwner(ownership2.getOwner());
	    			line.setRole(ApproveRoleType.toApproveRoleType(ApprovalUtil.ROLE_RECEIVE));
	    			line.setState(ApproveStateType.toApproveStateType(ApprovalUtil.STATE_LINE_STANDING));
	                line = (ApprovalLine)PersistenceHelper.manager.save(line);
	           }
	           
	           boolean request = true;//TypeUtil.booleanValue(ParamUtil.get(param,"submitApproval"), true); // 임시 저장
	           
	           if(request){
	        	   request(am);
	           }
           }
           trx.commit();
           trx = null;

        }catch(Exception ex){
            ex.printStackTrace();
            throw new WTException(ex);
        }finally{
            if (trx != null)
                trx.rollback();
        }
		
	}
	public void request(ApprovalMaster master) throws WTException {
        commit(master,LINE_REQUEST);
	}
	public void commit(ApprovalLine line, String line_state) throws WTException {
	}
	public void commit(ApprovalMaster master, String line_state)
			throws WTException {

        QueryResult qr = activeLine(master);
        ApprovalLine line = null;

        if( qr != null ) {
            if( qr.hasMoreElements() ) {
                line = (ApprovalLine)qr.nextElement();
                setPersistableState( getApprovalObject(master), "APPROVING");
                commit(line,line_state);
            }
        }
	}
	private QueryResult activeLine(ApprovalMaster master) throws WTException {
		QuerySpec qs = new QuerySpec(ApprovalLine.class);
        qs.appendWhere(new SearchCondition(ApprovalLine.class,"masterReference.key.id","=",master.getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
        qs.appendAnd();
        qs.appendWhere(new SearchCondition(ApprovalLine.class,"seq","=",master.getActive()),new int[]{0});
        qs.appendAnd();
        qs.appendWhere(new SearchCondition(ApprovalLine.class,ApprovalLine.APPROVE_DATE,true),new int[]{0});
        return PersistenceHelper.manager.find(qs);
	}
	public QueryResult nextLine(ApprovalLine line) throws WTException {
		QuerySpec qs = new QuerySpec(ApprovalLine.class);
        qs.appendWhere(new SearchCondition(ApprovalLine.class,"masterReference.key.id","=",line.getMaster().getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
        qs.appendAnd();
//        qs.appendWhere(new SearchCondition(ApprovalLine.class,"seq","=",line.getSeq()+1),new int[]{0});
//        qs.appendAnd();
        qs.appendOpenParen();
        qs.appendWhere(new SearchCondition(ApprovalLine.class,"name","=",WORKING_REVIEWER),new int[]{0});
        qs.appendOr();
        qs.appendWhere(new SearchCondition(ApprovalLine.class,"name","=",WORKING_DISCUSSER),new int[]{0});
        qs.appendOr();
        qs.appendWhere(new SearchCondition(ApprovalLine.class,"name","=",WORKING_WORKING),new int[]{0});
        qs.appendCloseParen();
        return PersistenceHelper.manager.find(qs);
	}
	@Override
	public void removeProcess(Persistable per) throws WTException {
		Transaction trx = new Transaction();
        try{
            trx.start();
			QueryResult qr = getApprovalMaster(per,false);
			while(qr.hasMoreElements()){
	    		Object[] o = (Object[])qr.nextElement();
	    		ApprovalMaster am = (ApprovalMaster)o[0];
	    		remove(am);
			}
			trx.commit();
	        trx = null;

	    }catch(Exception ex){
	        ex.printStackTrace();
	        throw new WTException(ex);
	    }finally{
	        if (trx != null){
	        	trx.rollback();
	        }
	    }
		
	}

	private void remove(ApprovalMaster master) throws WTException {
		

        QueryResult qr = getApprovalLine(master);

        if( qr != null ) {
            while(qr.hasMoreElements()){
                ApprovalLine line = (ApprovalLine)qr.nextElement();
                PersistenceHelper.manager.delete(line);
            }
            PersistenceHelper.manager.delete(master);
        }
	}

	private void complete(ApprovalLine line) throws Exception {
	}
	
	 public Persistable getApprovalObject(ApprovalMaster master)throws WTException{
	        return getApprovalObject(master.getPersistInfo().getObjectIdentifier().getId());
	 }
	
	

	 private Persistable getApprovalObject(long id)throws WTException{

	        QuerySpec qs = new QuerySpec();
	        int ii = qs.addClassList(ApprovalObjectLink.class,true);
	        int jj = qs.addClassList(ApprovalMaster.class,false);

	        qs.appendWhere(new SearchCondition(ApprovalObjectLink.class,"roleBObjectRef.key.id","=",id),new int[]{ii});
	        //        qs.appendAnd();
	        //        qs.appendWhere(new SearchCondition(ApprovalObjectLink.class,"roleAObjectRef.key.id",Persistable.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{ii,jj});

	        //LOGGER.info("@ qs = " + qs.toString());
	        QueryResult qr = PersistenceHelper.manager.find(qs);
	        Object[] o = null;
	        if(qr.hasMoreElements()){
	            o = (Object[])qr.nextElement();
	            //LOGGER.info("@ object = " + o.toString());

	            return (Persistable)((ApprovalObjectLink)o[0]).getObj();
	        }
	        return null;
	}
	 
		
		public boolean checkWithdrawal(ApprovalMaster master) throws WTException {
			// latest 결재선을 쿼리하여 결재/합의 리스트가 결재된것이 있는지 체크
	        //ApprovalMaster ap = getApprovalMaster(obj);
	        // LOGGER.info("@@ call checkWithdrawal!!");
	        ArrayList appLine = queryLastApprovalLine( master );
	        ApprovalLine al = null;
	        String name = null;

	        if( appLine != null) {
	            // 기안자와 현재 사용자가 같은지 체크
	            // 같지 않으면 false
	            al = (ApprovalLine)appLine.get(0);
	            WTUser user = (WTUser)SessionHelper.manager.getPrincipal();
	            if( !al.getOwner().equals( user.getName()) ) {
	                return false;
	            }
	            //LOGGER.info("@@ appLine = " +  appLine.size());
	            //LOGGER.info("@@ ApprovalLine = " +  al.toString() + " , name = " + name);

	            for(int i=1; appLine.size()>i; i++){
	                al = (ApprovalLine)appLine.get(i);
	                name =al.getName();
	                //LOGGER.info("@@ ApprovalLine = " +  al.toString() + " , name = " + name);
	                
	                //데모준비 주석
	                /*if( name.equals(WORKING_REVIEWER) ||name.equals(WORKING_WORKING) ) {
	                    if( al.getApproveDate() != null )
	                        return false;
	                }*/
	            }
	        }

	        return true;
		}
		private ArrayList queryLastApprovalLine(ApprovalMaster master)
				throws WTException {

	        QueryResult qr = getApprovalLine(master);
	        ArrayList appLine = new ArrayList();
	        ApprovalLine line = null;

	        if( qr != null ) {
	            while(qr.hasMoreElements()){
	                line = (ApprovalLine)qr.nextElement();
	                if( line.getStepName().equals( APPROVE_REQUEST ) ) {
	                    appLine = new ArrayList();
	                }
	                appLine.add(line);
	            }
	        }

	        return appLine;
		}
	@Override
	public void registWork(Persistable per, WTUser worker) throws WTException {
		Transaction trx = new Transaction();
        try{
            trx.start();
            WTUser user = (WTUser)SessionHelper.manager.getPrincipal();
            //결재 마스터 생성
            ApprovalMaster am = ApprovalMaster.newApprovalMaster();
    		Map<String, String> map = ApprovalUtil.getApprovalObjectInfo(per);
    		String objectType = map.get("objectType");
        	am.setState(ApproveStateType.toApproveStateType(ApprovalUtil.STATE_MASTER_APPROVING));
            am.setOwner(SessionHelper.manager.getPrincipalReference());
            am.setActive(0);
            am.setObjectType(objectType);
            String title = map.get("title");
            am.setTitle(title);
            setPersistableState(per, "INWORK");
            am = (ApprovalMaster)PersistenceHelper.manager.save(am);

            ApprovalObjectLink link = ApprovalObjectLink.newApprovalObjectLink(per,am);
            PersistenceHelper.manager.save(link);
            
            //제출자
            ApprovalLine line = ApprovalLine.newApprovalLine();
            Ownership ownership = Ownership.newOwnership(user);
			line.setOwner(ownership.getOwner());
			line.setRole(ApproveRoleType.toApproveRoleType(ApprovalUtil.ROLE_DRAFT));
			line.setState(ApproveStateType.toApproveStateType(ApprovalUtil.STATE_LINE_COMPLETE));
            line.setName(WORKING_REPORTER);
            line.setSeq(0);
            line.setReadCheck(true);
            line.setMaster(am);
            line = (ApprovalLine)PersistenceHelper.manager.save(line);

            //작업자
            line = ApprovalLine.newApprovalLine();
            Ownership ownership2 = Ownership.newOwnership(worker);
			line.setOwner(ownership2.getOwner());
			line.setRole(ApproveRoleType.toApproveRoleType(ApprovalUtil.ROLE_APPROVE));
			line.setState(ApproveStateType.toApproveStateType(ApprovalUtil.STATE_LINE_ONGOING));
            line.setName(WORKING_WORKING);
            line.setSeq(1);
            line.setReadCheck(false);
            line.setMaster(am);
            line = (ApprovalLine)PersistenceHelper.manager.save(line);
            
            //요청
            request(am);
            
            
			String state = line.getRole().getDisplay();
			WTPrincipal wtprincipal = (WTPrincipal)line.getOwner().getObject();
			saveWFHistory((WTObject)per, state, "", wtprincipal);
			
    		
    		//메일 발송
//    		sendApprovalMail(line);
//    		if(isSessionChange){
//    			SessionHelper.manager.setPrincipal(oldUserId);
//    		}

            trx.commit();
            trx = null;

        }catch(Exception ex){
            ex.printStackTrace();
            throw new WTException(ex);
        }finally{
            if (trx != null)
                trx.rollback();
        }
	}

	@Override
	public ApprovalMaster getApprovalMaster(Persistable per) throws WTException {
		QueryResult qr = getApprovalMaster(per,false);
        if(qr.hasMoreElements()){
            Object[] o = (Object[])qr.nextElement();
            return (ApprovalMaster)o[0];
        }
        return null;
	}

	@Override
	public QueryResult getApprovalMaster(Persistable per, boolean link) throws WTException {
		ArrayList<Persistable> list = new ArrayList<Persistable>();
		if(per instanceof Iterated){
			QueryResult qr = VersionControlHelper.service.iterationsOf((Iterated)per);
			while(qr.hasMoreElements()){
				Iterated it = (Iterated)qr.nextElement();
				list.add(it);
			}
		}else{
			list.add(per);
		}
		
		QuerySpec qs = new QuerySpec();
        int jj = qs.addClassList(ApprovalMaster.class,true);
        int ii = qs.addClassList(ApprovalObjectLink.class,true);
        boolean flag = false;
        boolean isNullCheck = true;
        for(Persistable item : list){
    		if(null==item){
    			isNullCheck = false;
    			break;
    		}
    	}
        if(isNullCheck){
	        qs.appendOpenParen();
	        	
		        for(Persistable item : list){
		        	if(flag){
		        		qs.appendOr();
		        	}else{
		        		flag = true;
		        	}
	        	    qs.appendWhere(new SearchCondition(ApprovalObjectLink.class,"roleAObjectRef.key.id","=",item.getPersistInfo().getObjectIdentifier().getId()),new int[]{ii});
		        }
	        qs.appendCloseParen();
	        qs.appendAnd();
        }
        qs.appendWhere(new SearchCondition(ApprovalObjectLink.class,"roleBObjectRef.key.id",ApprovalMaster.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{ii,jj});

        return PersistenceHelper.manager.find(qs);
	}

	@Override
	public QueryResult getApprovalLine(ApprovalMaster master) throws WTException {
		QuerySpec qs = new QuerySpec(ApprovalLine.class);
        qs.appendWhere(new SearchCondition(ApprovalLine.class,"masterReference.key.id","=",master.getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
        qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class,"seq"),false),0);
        return PersistenceHelper.manager.find(qs);
	}

	@Override
	public String getCreatorFullName(Object pbo) throws WTException {
		String creatorName = "";
        
        if(pbo instanceof EChangeActivity){
        	creatorName = ((WTUser)SessionHelper.manager.getPrincipal()).getFullName();
        }if(pbo instanceof OwnPersistable){
            creatorName = ((OwnPersistable)pbo).getOwner().getFullName();
        }else if(pbo instanceof Versioned ){
            creatorName = ((Versioned)pbo).getCreator().getFullName();
        }else if(pbo instanceof Managed){
            creatorName = ((Managed)pbo).getCreator().getFullName();
        }
        return creatorName;
	}

	@Override
	public String getCreateTime(Persistable per) {
		Timestamp create = per.getPersistInfo().getCreateStamp();
    	Timestamp tt = new Timestamp(create.getTime() + (60 * 60 * 1000 * 9  )  );
    	
    	return DateUtil.getDateString(  tt  ,"a");
	}

	@Override
	public void registApprovalChange(Persistable per, Map<String, Object> param) throws WTException {
		
	}

	@Override
	public boolean isAccessModify(Persistable per) throws Exception {
		String creatorName = getCreatorName(per);
        return isAccessModify(per, creatorName);
	}

	@Override
	public boolean isAccessModify(Persistable per, String creatorName) throws Exception {
		if(!CommonUtil.isAdmin() ){
			if(creatorName==null){
	            throw new WTException("작성자를 찾을 수 없습니다.");
	        }
			
			String suser = SessionHelper.manager.getPrincipal().getName();
	        if(!suser.equals(creatorName)){
	             return false;
	        }
		}

        return isModify(per);
	}

	@Override
	public boolean isModify(Persistable per) throws WTException {
		ApprovalMaster appMaster = ApprovalHelper.service.getApprovalMaster(per);
        if (appMaster == null) return true;

        String state = appMaster.getState().toString();

        return	"INWORK".equals(state)
        ||	ApprovalService.MASTER_INWORK.equals(state)
        ||  ApprovalService.MASTER_REJECTED.equals(state)
        ||  ApprovalService.MASTER_REWORKING.equals(state)
        ||  ApprovalService.MASTER_WITHDRAWAL.equals(state);
	}
	
	 private String getCreatorName(Object pbo) throws Exception{
	        String creatorName = "";
	        
	        if(pbo instanceof OwnPersistable){
	            creatorName = ((OwnPersistable)pbo).getOwner().getName();
	        }else if(pbo instanceof Versioned ){
	            creatorName = ((Versioned)pbo).getCreator().getName();
	        }else if(pbo instanceof Managed){
	            creatorName = ((Managed)pbo).getCreator().getName();
	        }
	        return creatorName;
	  }

	@Override
	public ApprovalData getApprovalHistory(String oid) throws Exception {
		Persistable per = WCUtil.getObject(oid);
		ApprovalData data = null;
		ApprovalLine line = null;
		ApprovalMaster master = ApprovalHelper.service.getApprovalMaster(per);
		MultiApproval multiApproval = null;
		WTPart part = null;
		EPMDocument epmDocument = null;
		ApprovalToObjectLink oLink = null;
		ApprovalObjectLink oLink2 = null;
		Object pbo = null;
		Object pbo2 = null;
		if(oid.contains("EChangeOrder2")){ 
			EChangeOrder2 order =(EChangeOrder2) CommonUtil.getObject(oid);
			QuerySpec qs = new QuerySpec(EChangeOrder2.class);
			//qs.appendWhere(new SearchCondition(EChangeOrder2.class,"completeWorkReference.key.id","=",ca.getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
			qs.appendWhere(new SearchCondition(EChangeOrder2.class,"thePersistInfo.theObjectIdentifier.id","=",CommonUtil.getOIDLongValue(order)),new int[]{0});
			
			QueryResult qrq = PersistenceHelper.manager.find(qs);
			EChangeOrder2 eco =null;
			LOGGER.info("getApprovalHistory qr size ="+qrq.size());
			if(qrq.hasMoreElements()){
			    eco = (EChangeOrder2)qrq.nextElement();
			    CommonActivity activity =  eco.getCompleteWork();	
			    master = ApprovalHelper.service.getApprovalMaster(activity);
			    per = master;
		    }
		}
		
		
		if(per instanceof ApprovalLine){
			line = (ApprovalLine)per;
			master = line.getMaster();
			data = new ApprovalData(master);
		}else if(per instanceof ApprovalMaster){
			master = (ApprovalMaster)per;
			data = new ApprovalData(master);
		}else if(per instanceof WTPart){
			part = (WTPart)per;
			QuerySpec qs = new QuerySpec(ApprovalToObjectLink.class);
		    qs.appendWhere(new SearchCondition(ApprovalToObjectLink.class, "roleBObjectRef.key.id" ,"=", part.getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
		    QueryResult qr = PersistenceHelper.manager.find(qs);
		    
		    if( qr != null && qr.size() > 0) {
		        while(qr.hasMoreElements()) {
		            oLink = (ApprovalToObjectLink)qr.nextElement();
		            pbo = oLink.getRoleAObject();
		            multiApproval = (MultiApproval)pbo;
		        }
		    
			    multiApproval = (MultiApproval)multiApproval;
			    
			    QuerySpec qss = new QuerySpec(ApprovalObjectLink.class);
			    qss.appendWhere(new SearchCondition(ApprovalObjectLink.class, "roleAObjectRef.key.id" ,"=", multiApproval.getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
			    QueryResult qrr = PersistenceHelper.manager.find(qss);
			    
			    if( qrr != null && qrr.size() > 0 ) {
			    	while(qrr.hasMoreElements()) {
			            oLink2 = (ApprovalObjectLink)qrr.nextElement();
			            pbo2 = oLink2.getRoleBObject();
			            master = (ApprovalMaster)pbo2;
			  		}
			    }
		    
				data = new ApprovalData(master);
			
		    }else {
		    
		    	data = new ApprovalData(per);
		    }
		
		}else if(per instanceof EPMDocument){
			epmDocument = (EPMDocument)per;
			QuerySpec qs = new QuerySpec(ApprovalToObjectLink.class);
		    qs.appendWhere(new SearchCondition(ApprovalToObjectLink.class, "roleBObjectRef.key.id" ,"=", epmDocument.getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
		    QueryResult qr = PersistenceHelper.manager.find(qs);
		    
		    if( qr != null && qr.size() > 0) {
		        while(qr.hasMoreElements()) {
		            oLink = (ApprovalToObjectLink)qr.nextElement();
		            pbo = oLink.getRoleAObject();
		            multiApproval = (MultiApproval)pbo;
		        }
		    
			    multiApproval = (MultiApproval)multiApproval;
			    
			    QuerySpec qss = new QuerySpec(ApprovalObjectLink.class);
			    qss.appendWhere(new SearchCondition(ApprovalObjectLink.class, "roleAObjectRef.key.id" ,"=", multiApproval.getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
			    QueryResult qrr = PersistenceHelper.manager.find(qss);
			    
			    if( qrr != null && qrr.size() > 0 ) {
			    	while(qrr.hasMoreElements()) {
			            oLink2 = (ApprovalObjectLink)qrr.nextElement();
			            pbo2 = oLink2.getRoleBObject();
			            master = (ApprovalMaster)pbo2;
			  		}
			    }
		    
				data = new ApprovalData(master);
			
		    }else {
		    
		    	data = new ApprovalData(per);
		    }
		
		}else{
			data = new ApprovalData(per);
		}
		
		return data;
	}

	@Override
	public ArrayList<ApprovalLine> getLastApprovalLine(String oid) throws WTException {
		Persistable per = WCUtil.getObject(oid);
		ApprovalMaster master = ApprovalHelper.service.getApprovalMaster(per);
		ArrayList<ApprovalLine> list = new ArrayList<ApprovalLine>();
		QuerySpec qs = new QuerySpec(ApprovalLine.class);
        qs.appendWhere(new SearchCondition(ApprovalLine.class,"masterReference.key.id","=",master.getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
        qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class,"seq"),false),new int[]{0});
        QueryResult qr  = PersistenceHelper.manager.find(qs);
        boolean flag = false;
        while(qr.hasMoreElements()){
        	ApprovalLine line = (ApprovalLine)qr.nextElement();
        	if(WORKING_REPORTER.equals(line.getName()) && LINE_STANDING.equals(line.getState())){ //제출자
        		flag = true;
        	}
        	if(flag)list.add(line);
        }
        return list;
	}
	
	@Override
	public void setPersistableState(Persistable per, String state) throws WTException {
		
		if(per instanceof EChangeOrder2) {
			EChangeOrder2 eco =  (EChangeOrder2) per;
			
			WTPrincipal currentUser = SessionHelper.manager.getPrincipal();
            String curUserName = currentUser.getName();
            SessionHelper.manager.setAdministrator();
            
            if(state.equals("INWORK")) {
                LifeCycleHelper.service.setLifeCycleState(eco, State.toState(state) );
            } else {
                LifeCycleHelper.service.setLifeCycleState(eco, State.toState("AFTERAPPROVING") );
            }
            
            SessionHelper.manager.setPrincipal(curUserName);
            
		} else if( per instanceof LifeCycleManaged ) {

            WTPrincipal currentUser = SessionHelper.manager.getPrincipal();
            String curUserName = currentUser.getName();
            SessionHelper.manager.setAdministrator();
            
			LifeCycleTemplate temp = LifeCycleHelper.service.getLifeCycleTemplate((LifeCycleManaged)per);
            LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged)per, State.toState(state) );
			
            SessionHelper.manager.setPrincipal(curUserName);

        } else if( per instanceof MultiApproval ) {
            // query linked object
            QuerySpec qs = new QuerySpec(ApprovalToObjectLink.class);
            qs.appendWhere(new SearchCondition(ApprovalToObjectLink.class, "roleAObjectRef.key.id" ,"=", per.getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
            QueryResult qr = PersistenceHelper.manager.find(qs);

            ApprovalToObjectLink oLink = null;
            WTPart wtPart = null;
            EPMDocument epmDocument= null;
            Object pbo = null;
            if( qr != null ) {
                while(qr.hasMoreElements()) {
                    oLink = (ApprovalToObjectLink)qr.nextElement();
                    pbo = oLink.getObj();

                    if( pbo instanceof WTPart) {
                        wtPart = (WTPart)pbo;

                        if( state.equals("REJECTED") || state.equals("WITHDRAWN")) {
                            state = "INWORK";
                        }
                        setPersistableState(wtPart, state);

                    } 
                    if( pbo instanceof EPMDocument) {
                        epmDocument = (EPMDocument)pbo;

                        if( state.equals("REJECTED") || state.equals("WITHDRAWN")) {
                            state = "INWORK";
                        }
                        setPersistableState(epmDocument, state);
                    }
                }
            }
        }
        
        try {
			ApprovalData data = new ApprovalData(per);
			//data.changeState(state);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getState(Persistable per) throws WTException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public void appendReceiveList(ApprovalMaster appMaster, List<WTPrincipal> userList) throws Exception {
		
		if(appMaster != null && userList.size() > 0) {
			
			List<ApprovalLineData> listLineData = ApprovalHelper.manager.getApprovalLastLine(appMaster, true);
			
			Integer maxSeq = null;
			for(ApprovalLineData line : listLineData) {
				
				if(
					ApprovalUtil.STATE_LINE_REJECT.equals(line.getState()) ||
					ApprovalUtil.STATE_LINE_DELEGATE.equals(line.getState()) ||
					ApprovalUtil.STATE_LINE_COMPLETE.equals(line.getState())
						) {
					continue;
				}
				
				if(
					!(ApprovalUtil.ROLE_APPROVE.equals(line.getRoleType()) ||
					ApprovalUtil.ROLE_DISCUSS.equals(line.getRoleType()))
						) {
					continue;
				}
				
				if(maxSeq == null) {
					maxSeq = line.getSeq();
				}else {
					maxSeq = maxSeq < line.getSeq() ? line.getSeq() : maxSeq;
				}
			}
			
			if(maxSeq != null) {
//				createApprovalLine(master, receiveList,ApprovalUtil.ROLE_RECEIVE,seq,receiveState,ver);
				
				Transaction trx = null;
				
				try {
					trx = new Transaction();
					trx.start();
					
					for(WTPrincipal user : userList) {
						ApprovalLine line = ApprovalLine.newApprovalLine();
						Ownership ownership = Ownership.newOwnership(user);
		    			line.setOwner(ownership.getOwner());
		    			line.setRole(ApproveRoleType.toApproveRoleType(ApprovalUtil.ROLE_RECEIVE));
		    			line.setState(ApproveStateType.toApproveStateType(ApprovalUtil.STATE_LINE_STANDING));
		    			
			            line.setName(WORKING_TEMP);
			            line.setSeq(maxSeq+1);
			            line.setReadCheck(true);
			            line.setMaster(appMaster);
			            line.setStepName(APPROVE_NOTIFICATE);
			            PersistenceHelper.manager.save(line);
					}
					
					trx.commit();
					trx = null;
					
				}catch(Exception e) {
					throw e;
				}finally {
					if(trx != null) {
						
						trx.rollback();
						trx = null;
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @desc	: 결재 마스터 완료상태 변경
	 * @author	: tsuam
	 * @date	: 2019. 7. 31.
	 * @method	: addtionalStateApprovalMaster
	 * @return	: ApprovalMaster
	 * @param master
	 * @param masterState
	 * @return
	 * @throws Exception
	 */
	public void addtionalStateApprovalMaster(Persistable per, List<Map<String, Object>> approvalList,String appState,ApprovalLine line) throws Exception {	

		//		if(master.getState().toString().equals(ApprovalUtil.STATE_MASTER_COMPLETED)){
		//			return master;
		//		}
		//		
		//		if(masterState.equals(ApprovalUtil.STATE_MASTER_COMPLETED)) {
		//			master.setCompleteDate(new Timestamp(System.currentTimeMillis()));
		//		}
		//		master.setState(ApproveStateType.toApproveStateType(masterState));
		//		master = (ApprovalMaster)PersistenceHelper.manager.save(master);
		//		return master;
		
		//Role별  Array 취합
		Map<String , List<String>> approvalMap = ApprovalUtil.getRoleTypeList(approvalList);
		
		List<String> discussList = approvalMap.get(ApprovalUtil.ROLE_DISCUSS);
		List<String> approveList = approvalMap.get(ApprovalUtil.ROLE_APPROVE);
		List<String> receiveList = approvalMap.get(ApprovalUtil.ROLE_RECEIVE);
		List<String> draftList = new ArrayList<String>();
		
		//기안 리스트 생성
		boolean isSessionChange = false;
		WTUser dratUser = (WTUser)SessionHelper.manager.getPrincipal();
		String oldUserId =  dratUser.getName();
		PeopleData pData = new PeopleData(dratUser);
		draftList.add(pData.getOid());
		
		
		String draftState = ApprovalUtil.STATE_LINE_ONGOING;		//기안
		String discussState = ApprovalUtil.STATE_LINE_STANDING;		//협의	
		String approveState = ApprovalUtil.STATE_LINE_STANDING;		//승인
		String receiveState = ApprovalUtil.STATE_LINE_STANDING;		//수신
		
		//결재 라인 상태 체크
		if(appState.equals(ApprovalUtil.STATE_MASTER_APPROVING)) {
			appState = ApprovalUtil.STATE_MASTER_DISCUSSING;
			draftState = ApprovalUtil.STATE_LINE_COMPLETE;
			if(discussList.size()>0) {
				discussState = ApprovalUtil.STATE_LINE_ONGOING;
			}else {
				approveState = ApprovalUtil.STATE_LINE_ONGOING;
				appState = ApprovalUtil.STATE_MASTER_APPROVING;
			}
		}
		int ver = 0 ;
		int seq = 0;
		
		ApprovalMaster master = ApprovalHelper.manager.getApprovalMaster(per);
		
		if(line != null) {
			ver = line.getVer();
		}
		
		if(master == null) {
			//마스터 생성
			master  = createApprovalMaster(per,appState);
			//마스터와 결재 대상 링크 생성
			createApprovalLink(per, master);
			//기안 Role 생성
			seq = createApprovalLine(master, draftList,ApprovalUtil.ROLE_DRAFT,seq,draftState,ver);
		}else {
			//마스터와 상태 변경
			changeStateApprovalMaster(master, appState);
			
			//타이틀 변경
//			Map<String, String> map = ApprovalUtil.getApprovalObjectInfo(per);
//	        String title = map.get("title");
//	        master.setTitle(title);
//	        master = (ApprovalMaster) PersistenceHelper.manager.save(master);
	        
			//기존 결재 라인 삭제
			deleteApprovalLine(master, true);
			
			seq = 1;
		}
		
		//결재라인 합의
		seq = createApprovalLine(master, discussList,ApprovalUtil.ROLE_DISCUSS,seq,discussState,ver);
		//결재라인 승인
		seq = createApprovalLine(master, approveList,ApprovalUtil.ROLE_APPROVE,seq,approveState,ver);
		//결재랑인 수신
		seq = createApprovalLine(master, receiveList,ApprovalUtil.ROLE_RECEIVE,seq,receiveState,ver);
		
		List<ApprovalLine> list = ApprovalHelper.manager.getApprovalRoleLine(master, ApprovalUtil.ROLE_DRAFT, "", true);
		
		//기안자 라인
		line = list.get(0);
		//실 사용자 저장
		
		//기안자 결재 history 
		if(appState.equals("APPROVING")){
			String state = line.getRole().getDisplay();
			WTPrincipal wtprincipal = (WTPrincipal)line.getOwner().getObject();
			saveWFHistory((WTObject)per, state, "", wtprincipal);
			
			line.setApproveDate(new Timestamp(System.currentTimeMillis()));
			if(line.getStartDate() == null){
				line.setStartDate(new Timestamp(System.currentTimeMillis()));
			}
		}
		
		//기안 업데이트
		if(line.getStartDate() == null){
			line.setStartDate(new Timestamp(System.currentTimeMillis()));
		}
		line = (ApprovalLine) PersistenceHelper.manager.modify(line);
		
		//객체 상태 변경
		changeStatePersistable(per,master);
		
		//메일 발송
		if(!"TEMP_STORAGE".equals(master.getState().toString())) {
			sendApprovalMail(line);
		}
		
		if(isSessionChange){
			SessionHelper.manager.setPrincipal(oldUserId);
		}
	}
	
//	private void (Persistable per,ApprovalMaster master) throws Exception{
//		
//	}
}
