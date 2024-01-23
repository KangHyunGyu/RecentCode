package com.e3ps.approval.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.e3ps.approval.ApproveStateType;
import com.e3ps.approval.MultiApproval;
import com.e3ps.approval.MultiApprovalObjectLink;
import com.e3ps.common.bean.RevisionData;
import com.e3ps.common.content.service.CommonContentHelper;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.SequenceDao;
import com.e3ps.common.util.StringUtil;
import wt.content.ContentHolder;
import wt.enterprise.RevisionControlled;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.collections.WTArrayList;
import wt.fc.collections.WTCollection;
import wt.fc.collections.WTHashSet;
import wt.fc.collections.WTSet;
import wt.lifecycle.LifeCycleHelper;
import wt.lifecycle.LifeCycleManaged;
import wt.lifecycle.State;
import wt.part.WTPart;
import wt.pom.Transaction;
import wt.services.StandardManager;
import wt.session.SessionHelper;
import wt.util.WTException;

@SuppressWarnings("serial")
public class StandardMultiApprovalService extends StandardManager implements MultiApprovalService{
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.APPROVAL.getName());
	
	public static StandardMultiApprovalService newStandardMultiApprovalService() throws WTException {
		final StandardMultiApprovalService instance = new StandardMultiApprovalService();
		instance.initialize();
		return instance;
	}
	
	@Override
	public void createMultiApprovalAction(Map<String, Object> reqMap) throws Exception{
		
		MultiApproval multi = null;
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			//String number = StringUtil.checkNull((String) reqMap.get("number"));
			String name = StringUtil.checkNull((String) reqMap.get("name"));
			String description = StringUtil.checkNull((String) reqMap.get("description"));
			String objectType = StringUtil.checkReplaceStr((String) reqMap.get("objectType"),"doc");
			String appState = StringUtil.checkNull((String) reqMap.get("appState"));
			List<String> secondary = StringUtil.checkReplaceArray(reqMap.get("SECONDARY"));
			
			String number = objectType.toUpperCase() + DateUtil.getYear() + "-";
			String serial = SequenceDao.manager.getSeqNo(number, "0000", "MultiApproval", "MNumber");
			number += serial;
			
			
			//결재선
			List<Map<String, Object>> approvalList = (List<Map<String, Object>>) reqMap.get("approvalList");
			//일괄 결재 대상
			List<Map<String, Object>> multiObjectList = (List<Map<String, Object>>) reqMap.get("multiObjectList");
			multi = MultiApproval.newMultiApproval();
			multi.setObjectType(objectType);
			multi.setNumber(number);
			multi.setName(name);
			multi.setDescription(description);
			multi.setOwner(SessionHelper.manager.getPrincipalReference());
			
			multi.setState(ApproveStateType.toApproveStateType(appState));
			
			
			multi = (MultiApproval)PersistenceHelper.manager.save(multi);
			
			
			//attach files
			CommonContentHelper.service.attach((ContentHolder)multi, null, secondary);
			
			//일괄 결재 대상 등록
			createMultiObjectLink(multi, multiObjectList);
			
			//결재 등록
			ApprovalHelper.service.registApproval(multi, approvalList, appState, null);
			
			
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
	public MultiApproval modifyMultiAction(Map<String, Object> reqMap) throws Exception{
		MultiApproval multi = null;
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			if(oid.length() > 0) {
				multi = (MultiApproval) CommonUtil.getObject(oid);
				
				
				String name = StringUtil.checkNull((String) reqMap.get("name"));
				String description = StringUtil.checkNull((String) reqMap.get("description"));
				
				//첨부파일
				List<String> secondary = StringUtil.checkReplaceArray(reqMap.get("SECONDARY"));
				List<String> delocIds = StringUtil.checkReplaceArray(reqMap.get("delocIds"));
				
				//관련 객체
				List<Map<String, Object>> multiObjectList = (List<Map<String, Object>>) reqMap.get("multiObjectList");
				
				//check out - working copy
				
				
				//set properties
				multi.setName(name);
				multi.setDescription(description);
				
				multi = (MultiApproval) PersistenceHelper.manager.modify(multi);
				
				//attach files
				//CommonContentHelper.service.attach((ContentHolder)multi, null, secondary,delocIds);
				CommonContentHelper.service.attach((ContentHolder)multi, null, secondary,delocIds, false);
				
				//일괄 결재 대상 등록
				createMultiObjectLink(multi, multiObjectList);

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
		
		return multi;
	}
	
	@Override
	public void deleteMultiAction(Map<String, Object> reqMap) throws Exception{
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			if(oid.length() > 0) {
				MultiApproval multi = (MultiApproval) CommonUtil.getObject(oid);
				
				//결재 객체 생성
				createRelateApproval(multi);
				
				//일괄 결재 대상 삭제
				deleteMultiObjectLink(multi);
				
				//일괄 결재 삭제
				PersistenceHelper.manager.delete(multi);
				
				//결재 객체 삭제
				ApprovalHelper.service.deleteApproval(multi);
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
	
	/**
	 * 
	 * @desc	:
	 * @author	: tsuam
	 * @date	: 2019. 8. 2.
	 * @method	: createMultiObjectLink
	 * @return	: void
	 * @param multi
	 * @param multiObjectList
	 * @throws Exception
	 */
	private void createMultiObjectLink(MultiApproval multi,List<Map<String, Object>> multiObjectList) throws Exception{
		
		//관려 객체 삭제
		deleteMultiObjectLink(multi);
		
		//임시저장 결재마스터 삭제 및 Multi 결재 객체 연결 link 신규 생성
		WTCollection wc = new WTArrayList();
		for(Map<String, Object> map : multiObjectList) {
			String oid = (String)map.get("oid");
			Persistable per = CommonUtil.getObject(oid);
			
			ApprovalHelper.service.deleteApproval(per);
			
			MultiApprovalObjectLink link = MultiApprovalObjectLink.newApprovalToObjectLink(multi, per);
			wc.add(link);
		}
		
		PersistenceHelper.manager.save(wc);
		
	}
	
	/**
	 * 
	 * @desc	: 일괄 결재 대상 객체 삭제
	 * @author	: tsuam
	 * @date	: 2019. 8. 8.
	 * @method	: deleteMultiObjectLink
	 * @return	: void
	 * @param multi
	 * @param multiObjectList
	 * @throws Exception
	 */
	private void deleteMultiObjectLink(MultiApproval multi) throws Exception{
		
		QueryResult qr = PersistenceHelper.manager.navigate(multi, "obj", MultiApprovalObjectLink.class,false );
		
		WTSet ws = new WTHashSet(qr);
		
		PersistenceHelper.manager.delete(ws);
	}
	
	/**
	 * 
	 * @desc	: 일괄 결재 객체 삭제시 개별 결재 임시저장  및 상태 작업중
	 * @author	: tsuam
	 * @date	: 2020. 2. 28.
	 * @method	: createRelateApproval
	 * @return	: void
	 * @param multi
	 */
	private void createRelateApproval(MultiApproval multi) throws Exception{
			
		List<Persistable> list = MultiApprovalHelper.manager.getMultiApprovalObjectList(multi);
		String setState = "INWORK"; //작업중
		for(Persistable per : list) {
			RevisionControlled rv = (RevisionControlled)per;
			String vrOid = CommonUtil.getVROID(rv);
			rv = (RevisionControlled)CommonUtil.getObject(vrOid);
			RevisionData data = new RevisionData(rv);
			
			List<Map<String, Object>> approvalList = new ArrayList<Map<String, Object>>();
			
			//결재 라인 생성
			ApprovalHelper.service.registApproval(rv, approvalList, "TEMP_STORAGE", null); //임시저장
			
			//상태 변경
			if(rv instanceof WTPart){
				ApprovalHelper.service.changeStatePart((WTPart)rv, setState);
			}else{
				LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged)per, State.toState(setState), false);
			}
		}
		
		
	}
	
}
