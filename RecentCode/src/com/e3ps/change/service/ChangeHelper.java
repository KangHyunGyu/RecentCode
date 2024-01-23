package com.e3ps.change.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.e3ps.approval.ApprovalLine;
import com.e3ps.approval.ApprovalMaster;
import com.e3ps.approval.ApproveStateType;
import com.e3ps.approval.bean.ApprovalData;
import com.e3ps.approval.service.ApprovalHelper;
import com.e3ps.change.DocumentActivityOutput;
import com.e3ps.change.EChangeActivity;
import com.e3ps.change.EChangeActivityDefinitionRoot;
import com.e3ps.change.EChangeContents;
import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.EChangeRequest2;
import com.e3ps.change.EcoPartLink;
import com.e3ps.change.EcrPartLink;
import com.e3ps.change.RequestOrderLink;
import com.e3ps.change.beans.ActivityDocDataNEW;
import com.e3ps.change.beans.ActivityDocDataOLD;
import com.e3ps.change.beans.ChangeECOSearch;
import com.e3ps.change.beans.ChangeECRSearch;
import com.e3ps.change.beans.ECAData;
import com.e3ps.change.beans.ECOData;
import com.e3ps.change.beans.ECRData;
import com.e3ps.change.beans.EChangeContentsData;
import com.e3ps.change.util.EChangeMailForm;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.web.PageQueryBroker;
import com.e3ps.epm.bean.EpmData;
import com.e3ps.epm.service.EpmHelper;
import com.e3ps.part.bean.PartData;
import com.e3ps.part.service.PartHelper;
import com.e3ps.queue.E3PSQueueHelper;

import wt.fc.PagingQueryResult;
import wt.fc.PagingSessionHelper;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.part.WTPart;
import wt.part.WTPartMaster;
import wt.pds.StatementSpec;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.ServiceFactory;
import wt.util.WTAttributeNameIfc;
import wt.util.WTException;

/**
 * <pre>
 * Change 서비스 헬퍼
 *
 * [변경이력]
 * - 2015.02.04 (dlee) : 클래스 생성
 * </pre>
 */
public class ChangeHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.CHANGE.getName());
	public static final ChangeService service = ServiceFactory.getService(ChangeService.class);
	public static final ChangeHelper manager = new ChangeHelper();
	public Map<String, Object> getECOList(Map<String, Object> reqMap) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<ECOData> list = new ArrayList<>();
		int page = (Integer) reqMap.get("page");
		int rows = (Integer) reqMap.get("rows");
		String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));
		PagingQueryResult result = null;
		if (sessionId.length() > 0) {
			result = PagingSessionHelper.fetchPagingSession((page - 1) * rows, rows, Long.valueOf(sessionId));
		} else {
			QuerySpec query = ChangeECOSearch.getECOQuery(reqMap);
			result = PageQueryBroker.openPagingSession((page - 1) * rows, rows, query, true);
		}
		int totalSize = result.getTotalSize();
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			EChangeOrder2 eco = (EChangeOrder2) obj[0];
			ECOData data = new ECOData(eco);
			list.add(data);
		}
		map.put("list", list);
		map.put("totalSize", totalSize);
		map.put("sessionId", result.getSessionId() == 0 ? "" : result.getSessionId());
		return map;
		
	}
	
	public Map<String, Object> getECRList(Map<String, Object> reqMap) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<ECRData> list = new ArrayList<>();
		
		String id = (String) reqMap.get("id");
		
		int page = (Integer) reqMap.get("page");
		int rows = (Integer) reqMap.get("rows");
		String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));
		PagingQueryResult result = null;
		if (sessionId.length() > 0) {
			result = PagingSessionHelper.fetchPagingSession((page - 1) * rows, rows, Long.valueOf(sessionId));
		} else {
			QuerySpec query = ChangeECRSearch.getECRQuery(reqMap);
			result = PageQueryBroker.openPagingSession((page - 1) * rows, rows, query, true);
		}
		
		int totalSize = result.getTotalSize();
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			EChangeRequest2 ecr = (EChangeRequest2) obj[0];
			ECRData data = new ECRData(ecr);
			
			if("relatedECR".equals(id)) {
				//ecr;
				QueryResult qr = PersistenceHelper.manager.navigate(ecr,"order",RequestOrderLink.class);
				if(qr.size()>0) {
					data.setSelect(false);
				} 
			}
			
			data.ecrRelatedPartList();
			list.add(data);
		}
		
		map.put("list", list);
		map.put("totalSize", totalSize);
		map.put("sessionId", result.getSessionId() == 0 ? "" : result.getSessionId());
		
		return map;
	}
	
	public List<ECOData> getECOList2(Map<String, Object> reqMap) throws Exception {
		List<ECOData> list = new ArrayList<>();
		QuerySpec query = ChangeECOSearch.getECOQuery(reqMap);
		QueryResult result = PersistenceHelper.manager.find((StatementSpec) query);
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			EChangeOrder2 eco = (EChangeOrder2) obj[0];
			ECOData data = new ECOData(eco);
			list.add(data);
		}
		return list;
	}
	
	public List<ECRData> getECRList2(Map<String, Object> reqMap) throws Exception {
		List<ECRData> list = new ArrayList<>();
		QuerySpec query = ChangeECRSearch.getECRQuery(reqMap);
		QueryResult result = PersistenceHelper.manager.find((StatementSpec) query);
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			EChangeRequest2 eco = (EChangeRequest2) obj[0];
			ECRData data = new ECRData(eco);
			list.add(data);
		}
		return list;
	}
	
	public List<ECRData> getECRListQuick(Map<String, Object> reqMap) throws Exception {
		
		List<ECRData> list = new ArrayList<>();
		String number = StringUtil.checkNull((String) reqMap.get("value"));
		
		String id = StringUtil.checkNull((String) reqMap.get("id"));
		
		QuerySpec qs = new QuerySpec();
		Class cls = EChangeRequest2.class;
		int idx = qs.appendClassList(cls, true);
		qs.appendWhere(new SearchCondition(cls, EChangeRequest2.LIFE_CYCLE_STATE, SearchCondition.EQUAL, "APPROVED", false), new int[]{idx});
		qs.appendAnd();
		
		qs.appendOpenParen();
		qs.appendWhere(new SearchCondition(cls, EChangeRequest2.REQUEST_NUMBER, SearchCondition.LIKE, "%"+number.toUpperCase()+"%", false), new int[]{idx});
		qs.appendOr();
		qs.appendWhere(new SearchCondition(cls, EChangeRequest2.NAME, SearchCondition.LIKE, "%"+number.toUpperCase()+"%", false), new int[]{idx});
		qs.appendCloseParen();
		
		qs.appendOrderBy(new OrderBy(new ClassAttribute(cls, EChangeRequest2.REQUEST_NUMBER), false), new int[] { idx });
		
		QueryResult result = PersistenceHelper.manager.find((StatementSpec)qs);
		
		boolean listadd = true;
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			EChangeRequest2 ecr = (EChangeRequest2) obj[0];
			ECRData data = new ECRData(ecr);
			
			if("relatedECR".equals(id)) {
				QueryResult qr = PersistenceHelper.manager.navigate(ecr,"order",RequestOrderLink.class);
				if(qr.size() > 0) {
					listadd = false;
				}
			}
			
			if(listadd) {
				data.ecrRelatedPartList();
				list.add(data);
			}
		}
		
		return list;
	}
	
	public List<ECRData> getECOlink(Map<String, Object> reqMap) throws Exception {
		List<ECRData> list = new ArrayList<>();
		String oid = (String) reqMap.get("oid");
		EChangeOrder2 eco = (EChangeOrder2) CommonUtil.getObject(oid);
		QueryResult result = PersistenceHelper.manager.navigate(eco,"request",RequestOrderLink.class);
		while(result.hasMoreElements()){
			EChangeRequest2 ecr = (EChangeRequest2) result.nextElement();
			ECRData data = new ECRData(ecr);
			list.add(data);
		}
		return list;
	}
	
	public List<ActivityDocDataOLD> getEcaDocOLD(Map<String, Object> reqMap) throws Exception {
		List<ActivityDocDataOLD> list = new ArrayList<>();
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		Persistable per = CommonUtil.getObject(oid);
		if(per instanceof EChangeActivity) {
			EChangeActivity eca = (EChangeActivity) per;
			QuerySpec qs = new QuerySpec(DocumentActivityOutput.class);
			qs.appendWhere(new SearchCondition(DocumentActivityOutput.class,"activityReference.key.id","=",
					eca.getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
//			qs.appendAnd();
//			qs.appendWhere(new SearchCondition(DocumentActivityOutput.class,DocumentActivityOutput.NEW_LINK,SearchCondition.IS_NULL, true),new int[]{0});
			QueryResult  aqr = PersistenceHelper.manager.find(qs);
			while(aqr.hasMoreElements()){
				DocumentActivityOutput link = (DocumentActivityOutput)aqr.nextElement();
				if(link.getDocumentOldNumber() != null) {
					ActivityDocDataOLD data = new ActivityDocDataOLD(link);
					list.add(data);
				}
				
			}
		}
		return list;
	}
	
	public List<ActivityDocDataNEW> getEcaDocNEW(Map<String, Object> reqMap) throws Exception {
		List<ActivityDocDataNEW> list = new ArrayList<>();
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		Persistable per = CommonUtil.getObject(oid);
		if(per instanceof EChangeActivity) {
			EChangeActivity eca = (EChangeActivity) per;
			QuerySpec qs = new QuerySpec(DocumentActivityOutput.class);
			qs.appendWhere(new SearchCondition(DocumentActivityOutput.class,"activityReference.key.id","=",
					eca.getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
//			qs.appendAnd();
//			qs.appendWhere(new SearchCondition(DocumentActivityOutput.class,DocumentActivityOutput.OLD_LINK,SearchCondition.IS_NULL, true),new int[]{0});
			QueryResult  aqr = PersistenceHelper.manager.find(qs);
			while(aqr.hasMoreElements()){
				DocumentActivityOutput link = (DocumentActivityOutput)aqr.nextElement();
				if(link.getDocumentNewNumber() != null) {
					ActivityDocDataNEW data = new ActivityDocDataNEW(link);
					list.add(data);
				}
			}
		}
		return list;
	}
	
	public List<ECAData> getActivityStateList(String oid) throws Exception {
		List<ECAData> list = new ArrayList<>();
		long id = Long.parseLong(oid.substring(oid.lastIndexOf(":")+1));
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(EChangeActivity.class,true);
		qs.appendWhere(new SearchCondition(EChangeActivity.class,"orderReference.key.id","=",id),new int[]{ii});
		qs.appendOrderBy(new OrderBy(new ClassAttribute(EChangeActivity.class,"sortNumber"),false),new int[]{ii});
		QueryResult result = PersistenceHelper.manager.find(qs);
		while(result.hasMoreElements()){
		    Object[] o = (Object[])result.nextElement();
		    EChangeActivity eca = (EChangeActivity)o[0];
		    ECAData data = new ECAData(eca);
		    list.add(data);
		}
		return list;
	}
	
	public HashMap<Integer,ArrayList<ECAData>> getActivityStepList(String oid) throws Exception {
		HashMap<Integer,ArrayList<ECAData>> hash = new HashMap<Integer,ArrayList<ECAData>>();
		long id = Long.parseLong(oid.substring(oid.lastIndexOf(":")+1));
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(EChangeActivity.class,true);
		qs.appendWhere(new SearchCondition(EChangeActivity.class,"orderReference.key.id","=",id),new int[]{ii});
		qs.appendOrderBy(new OrderBy(new ClassAttribute(EChangeActivity.class,"sortNumber"),false),new int[]{ii});
		QueryResult result = PersistenceHelper.manager.find(qs);
		while(result.hasMoreElements()){
			Object[] o = (Object[])result.nextElement();
		    EChangeActivity eca = (EChangeActivity)o[0];
		    ECAData data = new ECAData(eca);
		    String step = eca.getStep();
		    char last = step.charAt(step.length() - 1);
		    int stepIndex = Integer.parseInt(String.valueOf(last));  
		    ArrayList<ECAData> list = (ArrayList<ECAData>)hash.get(stepIndex);
		    if(list==null){
		    	list = new ArrayList<ECAData>();
		    }
		    list.add(data);
		    hash.put(stepIndex,list);
		}
		return hash;
	}
	
	public ApprovalData getChangeObject(String oid) throws Exception {
		String approvalOid = oid;
		ReferenceFactory rf = new ReferenceFactory();
		Persistable per = rf.getReference(approvalOid).getObject();

		ApprovalData data = null;
		ApprovalLine line = null;
		ApprovalMaster master = null;
		if(per instanceof ApprovalLine){
		    line = (ApprovalLine)per;
		    master = line.getMaster();
		    data = new ApprovalData(master);
		}else if(per instanceof ApprovalMaster){
		    master = (ApprovalMaster)per;
		    data = new ApprovalData(master);
		}else{
		    data = new ApprovalData(per);
		    master = data.master;
		}
		return data;
		
	}
	
	public String getEcrRoot() throws WTException {
		String rootOid = "";
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(EChangeActivityDefinitionRoot.class,true);
		qs.appendWhere(new SearchCondition(EChangeActivityDefinitionRoot.class,EChangeActivityDefinitionRoot.NAME
				,"=","ECR"),new int[]{ii});
		QueryResult result = PersistenceHelper.manager.find(qs);
		if(result.hasMoreElements()) {
			Object[] o = (Object[])result.nextElement();
			EChangeActivityDefinitionRoot root = (EChangeActivityDefinitionRoot)o[0];
			rootOid = CommonUtil.getOIDString(root);
		}
		return rootOid;
		
	}
	
	public void changeActivityApprovalState(Persistable per, String state) throws Exception {
		long oid = per.getPersistInfo().getObjectIdentifier().getId();
	    QuerySpec qs = new QuerySpec();
	    int ii = qs.addClassList(EChangeActivity.class,true);
	    qs.appendWhere(new SearchCondition(EChangeActivity.class,"orderReference.key.id","=",oid),new int[]{ii});
	    qs.appendOrderBy(new OrderBy(new ClassAttribute(EChangeActivity.class,"sortNumber"),false),new int[]{ii});
	    QueryResult result = PersistenceHelper.manager.find(qs);
	    while(result.hasMoreElements()){
	    	Object[] o = (Object[]) result.nextElement();
	    	EChangeActivity eca = (EChangeActivity)o[0];
	    	if(state.equals("RETURN")) {
	    		eca.setActiveState(ChangeService.ACTIVITY_CANCELLED);
	    	}
			PersistenceHelper.manager.modify(eca);
    		ApprovalMaster master = ApprovalHelper.service.getApprovalMaster(eca);
    		if(master != null) {
    			if(state.equals("RETURN")) {
        			master.setState(ApproveStateType.toApproveStateType("REJECTED"));
        		}
        		PersistenceHelper.manager.modify(master);
    		}
	    }
	}
	
	public void delayMailSend() throws Exception {
		Timestamp toDate = DateUtil.convertDate(DateUtil.getToDay());
		Timestamp delayStamp = DateUtil.convertDate(DateUtil.getDateString(DateUtil.getDelayTime(DateUtil.getToDayTimestamp(), -1), "d"));
		QuerySpec qs = new QuerySpec();
		
		int idx = qs.addClassList(EChangeActivity.class, true);
		SearchCondition sc = new SearchCondition(EChangeActivity.class, EChangeActivity.ACTIVE_STATE,SearchCondition.EQUAL,"작업중");
	 	qs.appendWhere(sc, new int[] { idx });
	 	
	 	qs.appendAnd();
	 	sc = new SearchCondition(EChangeActivity.class, EChangeActivity.FINISH_DATE,SearchCondition.LESS_THAN,delayStamp);
	 	qs.appendWhere(sc, new int[] { idx });
	 			
		qs.appendOrderBy(new OrderBy(new ClassAttribute(EChangeActivity.class, "thePersistInfo.createStamp"), true), new int[] { idx });
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		while (qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			EChangeActivity eca = (EChangeActivity) obj[0];
			Hashtable<String, Object> mailHash = EChangeMailForm.setActivityDelayMailInfo(eca); // 지연 업무 메일폼
			//Hashtable<String, Object> mailHash = EChangeMailForm.setActivityDeadlineMailInfo(eca); // 마감일 도래 업무 메일폼
			if(mailHash.size() > 0 ) {
				mailHash.put(E3PSQueueHelper.queueName, E3PSQueueHelper.QueueName_Mail);
				mailHash.put(E3PSQueueHelper.className, E3PSQueueHelper.ClassName_Mail);
				mailHash.put(E3PSQueueHelper.methodName, E3PSQueueHelper.MethodName_EO_Delay_Mail);
				E3PSQueueHelper.manager.createQueue(mailHash);
			}
		}
		
	}
	
	public List<EChangeContentsData> getEChangeContents(String oid) throws Exception{
		
		List<EChangeContentsData> result = new ArrayList<EChangeContentsData>();
		
		QuerySpec qs = new QuerySpec();
		
		int idx = qs.addClassList(EChangeContents.class, true);
		
		SearchCondition sc = new SearchCondition(EChangeContents.class, EChangeContents.ECHANGE_REFERENCE + "." + WTAttributeNameIfc.REF_OBJECT_ID, SearchCondition.EQUAL, CommonUtil.getOIDLongValue(oid));
		
	 	qs.appendWhere(sc, new int[] { idx });
	 	
	 	qs.appendOrderBy(new OrderBy(new ClassAttribute(EChangeContents.class, EChangeContents.SORT), false), new int[] { idx });
	 	
	 	QueryResult qr = PersistenceHelper.manager.find(qs);
	 	
	 	while(qr.hasMoreElements()) {
	 		Object[] o = (Object[])qr.nextElement();
	 		EChangeContents ec = (EChangeContents)o[0];
	 		
	 		EChangeContentsData data = new EChangeContentsData(ec);
	 		
	 		result.add(data);
	 	}
	 	
	 	return result;
	}
	
	public QueryResult getEChangeContents_QR(String oid) throws Exception{
		
		QuerySpec qs = new QuerySpec();
		
		int idx = qs.addClassList(EChangeContents.class, true);
		
		SearchCondition sc = new SearchCondition(EChangeContents.class, EChangeContents.ECHANGE_REFERENCE + "." + WTAttributeNameIfc.REF_OBJECT_ID, SearchCondition.EQUAL, CommonUtil.getOIDLongValue(oid));
		
	 	qs.appendWhere(sc, new int[] { idx });
	 	
	 	qs.appendOrderBy(new OrderBy(new ClassAttribute(EChangeContents.class, EChangeContents.SORT), false), new int[] { idx });
	 	
	 	QueryResult qr = PersistenceHelper.manager.find(qs);
	 	
	 	return qr;
	}
	
//	public List<PartData> getECRPartDataList(String oid) throws Exception{
//		List<PartData> returnList = new ArrayList<PartData>();
//		
//		QuerySpec qs = new QuerySpec();
//		
//		int idx = qs.addClassList(EChangeRequest2.class, false);
//		int idx2 = qs.addClassList(WTPart.class, true);
//		
//		ClassAttribute attr1 = new ClassAttribute(EChangeRequest2.class, "partReference.key.id");
//		ClassAttribute attr2 = new ClassAttribute(WTPart.class, "thePersistInfo.theObjectIdentifier.id");
//		
//		SearchCondition sc = null;
//		
//		sc = new SearchCondition( attr1, "=", attr2 );
//		qs.appendWhere(sc, new int[]{idx, idx2});
//		qs.appendAnd();
//		sc = new SearchCondition(EChangeRequest2.class, "thePersistInfo.theObjectIdentifier.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(oid));
//		qs.appendWhere(sc, new int[] { idx });
//		
//		QueryResult result = PersistenceHelper.manager.find(qs);
//		
//		while(result.hasMoreElements()){
//			Object[] obj = (Object[]) result.nextElement();
//			WTPart part = (WTPart) obj[0];
//			
//			PartData data = new PartData(part);
//			data.loadAttributes();
//			
//			returnList.add(data);
//		}
//		
//		return returnList;
//	}
	
	/**
	 * ECR Part 연관 데이터 리스트
	 * @methodName : getECRPartDataList
	 * @author : tsuam
	 * @date : 2021.11.18
	 * @return : List<PartData>
	 * @description :
	 */
	public List<PartData> getECRPartDataList(String oid) throws Exception{
		EChangeRequest2 ecr = (EChangeRequest2)CommonUtil.getObject(oid);
		
		return getECRPartDataList(ecr);
	}
	
	
	public List<PartData> getECRPartDataList(EChangeRequest2 ecr) throws Exception{
		List<PartData> returnList = new ArrayList<PartData>();
		List<EcrPartLink> list = getECRPartLink(ecr);
		
		
		Map<String ,PartData> map = new HashMap<String, PartData>();
		for(EcrPartLink link : list) {
			String version = link.getVersion();
			WTPartMaster master = (WTPartMaster)link.getPart();
			WTPart part = PartHelper.manager.getPart(master.getNumber(),version);
			if(part == null) {
				continue;
			}
			PartData data = new PartData(part);		
			
			returnList.add(data);
		}
		
		return returnList;
	}
	
	/**
	 * ECR Part  관련 링크
	 * @methodName : getECRPartLink
	 * @author : tsuam
	 * @date : 2021.11.18
	 * @return : void
	 * @description :
	 */
	public List<EcrPartLink> getECRPartLink(String oid ) throws Exception{
		EChangeRequest2 ecr = (EChangeRequest2)CommonUtil.getObject(oid);
		
		return getECRPartLink(ecr);
	}
	
	public List<EcrPartLink> getECRPartLink(EChangeRequest2 ecr ) throws Exception{
		List<EcrPartLink> list = new ArrayList<EcrPartLink>();
		QueryResult qr = PersistenceHelper.manager.navigate(ecr,"part",EcrPartLink.class,false);
		while(qr.hasMoreElements()){
			EcrPartLink link = (EcrPartLink)qr.nextElement();
			
			list.add(link);
		}
		
		return list;
	}
	
	public static List<EcoPartLink> getECOPartLink(EChangeOrder2 eco ) throws Exception{
		List<EcoPartLink> list = new ArrayList<EcoPartLink>();
		QueryResult qr = PersistenceHelper.manager.navigate(eco,"part",EcoPartLink.class,false);
		while(qr.hasMoreElements()){
			EcoPartLink link = (EcoPartLink)qr.nextElement();
			list.add(link);
		}
		
		return list;
	}
	
	public List<PartData> getECOPartDataList(String oid) throws Exception{
		EChangeOrder2 eco = (EChangeOrder2)CommonUtil.getObject(oid);
		
		return getECOPartDataList(eco);
	}
	
	public List<PartData> getECOPartDataList(EChangeOrder2 eco) throws Exception{
		List<PartData> returnList = new ArrayList<PartData>();
		List<EcoPartLink> list = getECOPartLink(eco);
		for(EcoPartLink link : list) {
			String version = link.getVersion();
			WTPartMaster master = (WTPartMaster)link.getPart();
			WTPart part = PartHelper.manager.getPart(master.getNumber(),version);
			if(part == null){
				continue;
			}
			PartData data = new PartData(part);
			
			returnList.add(data);
		}
		
		return returnList;
	}
	
//	public List<EpmData> getECRRelatedEPMList(String oid) throws Exception{
//		List<EpmData> returnList = new ArrayList<EpmData>();
//		Map<String, Object> map = new HashMap<>();
//		
//		EChangeRequest2 ecr =(EChangeRequest2) CommonUtil.getObject(oid);
//		
//		String poid = ecr.getPart().getPersistInfo().getObjectIdentifier().getStringValue();
//		
//		map.put("oid", poid);
//		returnList = EpmHelper.manager.getMainEpm(map);
//		
//		return returnList;
//	}
	
	public List<EpmData> getECORelatedEPMList(String oid) throws Exception {
	    List<EpmData> returnList = new ArrayList<EpmData>();
	    List<PartData> partList = new ArrayList<PartData>();
	    Map<String, Object> map = new HashMap<>();

	    EChangeOrder2 eco = (EChangeOrder2) CommonUtil.getObject(oid);
	    
	    partList = getECOPartDataList(eco);

	    List<String> oidList = new ArrayList<>();

	    for (PartData part : partList) {
	        oidList.add(part.getOid());
	    }

	    map.put("oid", oidList);

	    returnList = EpmHelper.manager.getMainEpms(map);

	    return returnList;
	}
	
	public Map<String,Object> setECRDataAction(String oid) throws Exception{
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		EChangeRequest2 ecr = (EChangeRequest2) CommonUtil.getObject(oid);
		
		ECRData ecrData = new ECRData(ecr);
		
		ecrData.ecrRelatedPartList();
		
		List<PartData> partList = ecrData.getPartList();
		
		map.put("partList",partList);
		map.put("ecrData",ecrData);
		return map;
	}
	
	
	public Map<String,Object> getECRData(String oid) throws Exception{
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		EChangeRequest2 ecr = (EChangeRequest2) CommonUtil.getObject(oid);
		
		ECRData ecrData = new ECRData(ecr);
		
		ecrData.ecrRelatedPartList();
		
		List<PartData> partList = ecrData.getPartList();
		
		map.put("partList",partList);
		map.put("ecrData",ecrData);
		
		return map;
	}
	
}