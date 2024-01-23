package com.e3ps.change.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.e3ps.change.EChangeActivity;
import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.EcoPartLink;
import com.e3ps.change.EcoTargetLink;
import com.e3ps.change.beans.ChangeECOSearch;
import com.e3ps.change.beans.ECAData;
import com.e3ps.change.beans.ECOData;
import com.e3ps.change.beans.EChangeActivityDefinitionData;
import com.e3ps.change.util.ChangeUtil;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.service.CommonHelper;
import com.e3ps.common.util.AccessControlUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.ObjectUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.epm.service.EpmHelper;
import com.e3ps.part.bean.PartData;
import com.e3ps.part.service.PartHelper;

import wt.epm.EPMDocument;
import wt.epm.EPMDocumentMaster;
import wt.epm.build.EPMBuildHistory;
import wt.epm.build.EPMBuildRule;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.part.WTPart;
import wt.part.WTPartMaster;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.ServiceFactory;
import wt.vc.VersionControlHelper;

/**
 * <pre>
 * ChangeECO 서비스 헬퍼
 *
 * [변경이력]
 * - 2015.02.04 (dlee) : 클래스 생성
 * </pre>
 */
public class ChangeECOHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.CHANGE.getName());
	public static final ChangeECOService service = ServiceFactory.getService(ChangeECOService.class);
	public static final ChangeECOHelper manager = new ChangeECOHelper();
	
	/**
	 * @desc	: 관련 ECO 가져오기
	 * @author	: shkim
	 * @date	: 2020. 9. 16.
	 * @method	: getRelatedECO
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	public List<ECOData> getRelatedECO(Map<String, Object> reqMap) throws Exception {
		
		List<ECOData> list = new ArrayList<>();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		Persistable per = CommonUtil.getObject(oid);
		
		QueryResult qr = null;
		if(per instanceof WTPart) {
			WTPart part = (WTPart) per;
			
			qr = PersistenceHelper.manager.navigate(part, EcoTargetLink.ECR_ROLE, EcoTargetLink.class);
			
			while(qr.hasMoreElements()) {
				EChangeOrder2 eco = (EChangeOrder2) qr.nextElement();
				ECOData data = new ECOData(eco);
				list.add(data);
			}
			
		}else if(per instanceof EPMDocument) {
			EPMDocument epm = (EPMDocument) per;
			
			qr = PersistenceHelper.manager.navigate(epm, EcoTargetLink.ECR_ROLE, EcoTargetLink.class);
			
			while(qr.hasMoreElements()) {
				EChangeOrder2 eco = (EChangeOrder2) qr.nextElement();
				ECOData data = new ECOData(eco);
				list.add(data);
			}
		}
		
		return list;
	}
	
	/**
	 * @desc	: part 에서 관련 ECO 가져오기
	 * @author	: whseo
	 * @date	: 2023. 11. 23.
	 * @method	: getRelatedECODataList
	 * @param WTPart
	 * @return List<ECOData>
	 */
	public List<ECOData> getRelatedECODataList(WTPart part) throws Exception {
		List<ECOData> returnList = new ArrayList<ECOData>();
		List<EcoPartLink> list = getRelatedECO(part);
		for(EcoPartLink link : list) {
			EChangeOrder2 eco = (EChangeOrder2)link.getEco();
			ECOData data = new ECOData(eco);
			returnList.add(data);
		}
		return returnList;
	}
	
	/**
	 * @desc	: part 에서 관련 ECO 가져오기
	 * @author	: whseo
	 * @date	: 2023. 11. 23.
	 * @method	: getRelatedECO
	 * @param WTPart
	 * @return List<EcoPartLink>
	 */
	public List<EcoPartLink> getRelatedECO(WTPart part) throws Exception {
		List<EcoPartLink> list = new ArrayList<EcoPartLink>();
		WTPartMaster master = (WTPartMaster) part.getMaster();
		QueryResult qr = PersistenceHelper.manager.navigate(master,"eco",EcoPartLink.class,false);
		while(qr.hasMoreElements()){
			EcoPartLink link = (EcoPartLink)qr.nextElement();
			list.add(link);
		}
		return list;
	}
	
	
	public List<ECAData> getActivity(Map<String, Object> reqMap) throws Exception {
		List<ECAData> list = new ArrayList<>();
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		long id = Long.parseLong(oid.substring(oid.lastIndexOf(":")+1));
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(EChangeActivity.class,true);
		qs.appendWhere(new SearchCondition(EChangeActivity.class,"orderReference.key.id","=",id),new int[]{ii});
		qs.appendOrderBy(new OrderBy(new ClassAttribute(EChangeActivity.class,EChangeActivity.STEP),false),new int[]{ii});
		qs.appendOrderBy(new OrderBy(new ClassAttribute(EChangeActivity.class,"sortNumber"),false),new int[]{ii});
		QueryResult result = PersistenceHelper.manager.find(qs);
		while(result.hasMoreElements()){
		    Object[] o = (Object[])result.nextElement();
		    EChangeActivity activity = (EChangeActivity)o[0];
		    ECAData data = new ECAData(activity);
		    list.add(data);
		}
		
		return list;
	}
	
	public List<EChangeActivityDefinitionData> getActivityDef(Map<String, Object> reqMap) throws Exception {
		List<EChangeActivityDefinitionData> list = new ArrayList<>();
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		long id = Long.parseLong(oid.substring(oid.lastIndexOf(":")+1));
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(EChangeActivity.class,true);
		qs.appendWhere(new SearchCondition(EChangeActivity.class,"orderReference.key.id","=",id),new int[]{ii});
		qs.appendOrderBy(new OrderBy(new ClassAttribute(EChangeActivity.class,EChangeActivity.STEP),false),new int[]{ii});
		qs.appendOrderBy(new OrderBy(new ClassAttribute(EChangeActivity.class,"sortNumber"),false),new int[]{ii});
		QueryResult result = PersistenceHelper.manager.find(qs);
		while(result.hasMoreElements()){
		    Object[] o = (Object[])result.nextElement();
		    EChangeActivity activity = (EChangeActivity)o[0];
		    EChangeActivityDefinitionData data = new EChangeActivityDefinitionData(activity);
		    list.add(data);
		}
		
		return list;
	}
	
	//TODO
	/******************************************************************************************************************************************************/
	
	public Map<String,Object> isRevisePart(WTPart part,boolean isEPMCheck ) throws Exception{
		
		Map<String,Object> map = new HashMap<String, Object>();
		String message ="";
		boolean isRevisePart = true;
		
		boolean isRevise3D = true;  
		boolean isRevise2D = true;
		boolean isReviseRelate = true;
		boolean isLastestPart = true; 
		
		isLastestPart = CommonHelper.manager.isLastestRevision(part); //최신 유무
		isRevisePart = part.getLifeCycleState().toString().equals("APPROVED") && isLastestPart && !ObjectUtil.isCheckOut(part);
		
		if(!isRevisePart){
			message = part.getNumber()+MessageUtil.getMessage("가 최신 또는 승인,체크인 품목이 아닙니다.");
		}
		
		//3D
		if(isEPMCheck) {
			EPMDocument epm3D = EpmHelper.manager.getEPMDocument(part);
			if(epm3D != null) {
				if(!epm3D.getLifeCycleState().toString().equals("APPROVED")) {
					isRevise3D = false;
				}
				isRevise3D = CommonHelper.manager.isLastestRevision(epm3D) && isRevise3D && !ObjectUtil.isCheckOut(epm3D);
				
				if(!isRevise3D) {
					message = epm3D.getNumber()+MessageUtil.getMessage("가 최신 또는 승인,체크인 품목이 아닙니다.");
				}
				
				//2D
				EPMDocument epm2D = EpmHelper.manager.getDrawing((EPMDocumentMaster)epm3D.getMaster());
				if(epm2D != null) {
					if(!epm2D.getLifeCycleState().toString().equals("APPROVED")) {
						isRevise2D = false;
					}
					isRevise2D = CommonHelper.manager.isLastestRevision(epm2D) && isRevise2D && !ObjectUtil.isCheckOut(epm2D);
					
					if(!isRevise2D) {
						message = message +" "+ epm2D.getNumber()+MessageUtil.getMessage("가 최신 또는 승인,체크인 품목이 아닙니다.");
					}
				}
			}
			//관련 도면 
			List<EPMDocument> relateList = EpmHelper.manager.getEPMDesribeEPM(part);;
			for(EPMDocument epmRelate : relateList) {
				if(!epmRelate.getLifeCycleState().toString().equals("APPROVED")) {
					isReviseRelate = false;
				}
				isReviseRelate = CommonHelper.manager.isLastestRevision(epmRelate) && isReviseRelate && !ObjectUtil.isCheckOut(epmRelate);
				

				if(!isReviseRelate) {
					message = message +" "+ epmRelate.getNumber()+MessageUtil.getMessage("가 최신 또는 승인,체크인 품목이 아닙니다.");
				}
				
				if(!isReviseRelate) {
					break;
				}
			}
			isRevisePart = isRevisePart && isRevise3D && isRevise2D && isReviseRelate;
		}
		
		
		
		map.put("isRevisePart", isRevisePart);
		map.put("message", message);
		
		return map;
	}
	
	public List<Map<String,Object>> getECOActivePartList(EChangeOrder2 eco) throws Exception{
		
		return getECOActivePartList(eco, "");
	}
	
	public List<Map<String,Object>> getECOActivePartList(EChangeOrder2 eco,String checkType) throws Exception{
		
		System.out.println("======= getECOActivePartList =======");
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		Map<String,Object> oidMap = new HashMap<>();
		try {
			long eID = eco.getPersistInfo().getObjectIdentifier().getId();
			String ecoID = String.valueOf(eID);
			oidMap.put("oid", ecoID);
			List<ECAData> ecaList = getActivity(oidMap);
			ECAData eca = ecaList.get(0);
			
			boolean isECAComplete = false;
			if(eca != null) {
				isECAComplete = eca.getState().equals("COMPLETED"); // 완료이면 무조건 개정 대상 -
			}
			List<EcoPartLink> list = getECOPartLink(eco);
			
			for(EcoPartLink link : list) {
				WTPartMaster master = link.getPart();
				String linkOid = CommonUtil.getOIDString(link);
				String version = link.getVersion();
				
				WTPart part = PartHelper.manager.getPart(master.getNumber(), version);
				if(part == null ){
					continue;
				}
				String oid = CommonUtil.getOIDString(part);
				String iteration = part.getIterationIdentifier().getSeries().getValue();
				String ver = version+"."+iteration;
				String state = part.getLifeCycleState().toString();
				String stateName = part.getLifeCycleState().getDisplay(MessageUtil.getLocale());
				
				//개정후 품목
				WTPart nextPart = null;
				String nextVer = "";
				String nextState = "";
				String nextOid = "";
				String nextStateName = "";
				boolean isDelete = true;
				
				nextPart = (WTPart)ObjectUtil.getNextPartVersion(part);
				
				if(nextPart != null) {
					nextOid = CommonUtil.getOIDString(nextPart);
					nextVer = nextPart.getVersionInfo().getIdentifier().getValue()+"."+nextPart.getIterationIdentifier().getSeries().getValue();
					nextState =  nextPart.getLifeCycleState().toString();
					nextStateName = nextPart.getLifeCycleState().getDisplay();
				}
				isDelete = false;
			
//				if(link.isRevise()) {
//					nextPart = (WTPart)ObjectUtil.getNextPartVersion(part);
//					
//					if(nextPart != null) {
//						nextOid = CommonUtil.getOIDString(nextPart);
//						nextVer = nextPart.getVersionInfo().getIdentifier().getValue()+"."+nextPart.getIterationIdentifier().getSeries().getValue();
//						nextState =  nextPart.getLifeCycleState().toString();
//						nextStateName = nextPart.getLifeCycleState().getDisplay();
//					}
//					isDelete = false;
//				}
				
				Map<String,Object> map = new HashMap<String, Object>();
				
				//개정 정합성 체크
				checkType = StringUtil.checkNull(checkType);
				if(checkType.length()> 0) {
					
					if(isECAComplete) {
						map.put("isRevisePart",false);
						map.put("isRevisePartName","-");
					}else {
						if(link.isRevise()) {
							map.put("isRevisePart",false);
							map.put("isRevisePartName","-");
						}else {
							Map<String,Object> partCheckMap = isRevisePart(part,false);
							boolean isRevisePart = (boolean)partCheckMap.get("isRevisePart");
							String isRevisePartName = isRevisePart ? "O" :"-";
							
							map.put("isRevisePart",true);
							map.put("isRevisePartName",isRevisePartName);
						}
					}
					
				}
				map.put("isCheckInPart", link.isCheckin());
				map.put("isDelete",isDelete);
				map.put("isChange",false);
				
				map.put("oid",eID);
				map.put("number",part.getNumber());
				map.put("name",part.getName());
				map.put("ver",ver);
				map.put("rev",ver);
				map.put("state",stateName);
				map.put("stateName",stateName);
				map.put("nextVer",nextVer);
				map.put("nextRev",nextVer);
				map.put("nextState",nextState);
				map.put("nextStateName",nextStateName);
				map.put("nextOid",nextOid);
				map.put("partOid",oid);
				map.put("linkOid",linkOid);
				
				returnList.add(map);
				
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new Exception(e.getLocalizedMessage());
		}
		
		
		return returnList;
	}

	public List<EcoPartLink> getECOPartLink(EChangeOrder2 eco ) throws Exception{
		List<EcoPartLink> list = new ArrayList<EcoPartLink>();
		QueryResult qr = PersistenceHelper.manager.navigate(eco,"part",EcoPartLink.class,false);
		while(qr.hasMoreElements()){
			EcoPartLink link = (EcoPartLink)qr.nextElement();
			list.add(link);
		}
		
		return list;
	}
	
	public List<String> getECOStateList() throws Exception{
		List<String> list = new ArrayList<String>();
		
		list.add(ChangeService.ECO_WORKING);
		list.add(ChangeService.ECO_BEFORE_APPROVING);
		list.add(ChangeService.ECO_ECA_WORKING);
		list.add(ChangeService.ECO_ECA_COMPLETE);
		list.add(ChangeService.ECO_COMPLETE);
		list.add(ChangeService.ECO_REJECTED);
		
		return list;
	}
	
	/**
	 * 설계 변경 대상 품목 3D,2D,관련 도면 개정전,개정후
	 * @methodName : getECOActiveEPMList
	 * @author : tsuam
	 * @date : 2021.12.10
	 * @return : List<Map<String,Object>>
	 * @description :
	 */
	public List<Map<String,Object>> getECOActiveEPMList(EChangeOrder2 eco) throws Exception{
		return getECOActiveEPMList(eco, "");
	}
	
	public List<Map<String,Object>> getECOActiveEPMList(EChangeOrder2 eco,String moduleType) throws Exception{
		
		System.out.println("======= getECOActiveEPMList =======");
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		try {
			List<EcoPartLink> list = getECOPartLink(eco);
			
			boolean aclCheck = AccessControlUtil.checkPermissionForObject(WTPart.class);
			if(aclCheck) {
				for(EcoPartLink link : list) {
					
					//개전전 품목
					WTPartMaster master =link.getPart();
					
					String version = link.getVersion();
					
					WTPart part = PartHelper.manager.getPart(master.getNumber(), version);
					
					if(part == null){
						continue;
					}
					
					//3D
					EPMDocument epm3D = EpmHelper.manager.getEPMDocument(part);
					if(epm3D != null) {
						String epm3DVer = epm3D.getVersionIdentifier().getSeries().getValue();
						Map<String,Object> map3D = ChangeUtil.setEPMDocument(epm3D,link.isRevise());
						returnList.add(map3D);
						//2D
						EPMDocument epm2D = EpmHelper.manager.getDrawing((EPMDocumentMaster)epm3D.getMaster());
						if(epm2D != null) {
							Map<String,Object> map2D = ChangeUtil.setEPMDocument(epm2D,link.isRevise());
							returnList.add(map2D);
						}
					}
					
					//관련 도면 
					List<EPMDocument> relateList= EpmHelper.manager.getEPMDesribeEPM(part);
					
					for(EPMDocument epmRelate : relateList) {
						Map<String,Object> mapRelate = ChangeUtil.setEPMDocument(epmRelate,link.isRevise());
						returnList.add(mapRelate);
					}
					
					moduleType = StringUtil.checkNull(moduleType);
					
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			throw new Exception(e.getLocalizedMessage());
		}
		
		
		return returnList;
	}
}