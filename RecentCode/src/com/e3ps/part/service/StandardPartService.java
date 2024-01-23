package com.e3ps.part.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.e3ps.admin.service.AdminHelper;
import com.e3ps.approval.service.ApprovalHelper;
import com.e3ps.change.service.ChangeECOHelper;
import com.e3ps.common.iba.AttributeKey;
import com.e3ps.common.iba.IBAUtil;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.query.SearchUtil;
import com.e3ps.common.service.CommonHelper;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.ObjectUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.doc.E3PSDocument;
import com.e3ps.doc.util.DocTypePropList;
import com.e3ps.epm.dnc.CadAttributeDNC;
import com.e3ps.epm.service.EpmHelper;
import com.e3ps.epm.util.EpmPropList;
import com.e3ps.epm.util.EpmPublishUtil;
import com.e3ps.erp.util.ERPInterface;
import com.e3ps.part.bean.PartData;
import com.e3ps.part.util.PartPropList;

import wt.doc.WTDocument;
import wt.enterprise.RevisionControlled;
import wt.epm.EPMDocument;
import wt.epm.EPMDocumentMaster;
import wt.epm.build.EPMBuildRule;
import wt.epm.structure.EPMReferenceLink;
import wt.fc.IdentityHelper;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceServerHelper;
import wt.fc.QueryResult;
import wt.fc.collections.WTArrayList;
import wt.fc.collections.WTCollection;
import wt.fc.collections.WTHashSet;
import wt.fc.collections.WTSet;
import wt.lifecycle.LifeCycleHelper;
import wt.lifecycle.LifeCycleManaged;
import wt.lifecycle.State;
import wt.part.PartType;
import wt.part.QuantityUnit;
import wt.part.Source;
import wt.part.WTPart;
import wt.part.WTPartDescribeLink;
import wt.part.WTPartMaster;
import wt.part.WTPartMasterIdentity;
import wt.pom.Transaction;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.StandardManager;
import wt.util.WTException;
import wt.vc.VersionControlHelper;
import wt.vc.views.ViewHelper;
import wt.vc.wip.WorkInProgressHelper;
import wt.vc.wip.Workable;

@SuppressWarnings("serial")
public class StandardPartService extends StandardManager implements PartService {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PART.getName());
	
	public static final String ROOTLOCATION = "/Default";
	
	public static StandardPartService newStandardPartService() throws WTException {
		final StandardPartService instance = new StandardPartService();
		instance.initialize();
		return instance;
	}
	
	@Override
	public WTPart createPartAction(Map<String, Object> reqMap) throws Exception{
		WTPart part = null;
		
		Transaction trx = new Transaction();
		
		try {
			trx.start();
			
			String number = StringUtil.checkNull((String) reqMap.get("number"));
			String name = StringUtil.checkNull((String) reqMap.get("name"));
			String view = StringUtil.checkNull((String) reqMap.get("view"));
			String wtPartType = StringUtil.checkNull((String) reqMap.get("wtPartType"));
			String source = StringUtil.checkNull((String) reqMap.get("source"));

//			String appState = StringUtil.checkNull((String) reqMap.get("appState"));
//			List<Map<String, Object>> approvalList = (List<Map<String, Object>>) reqMap.get("approvalList");
//			if(approvalList == null) approvalList = new ArrayList<>();
			List<Map<String, Object>> relatedDocList = (List<Map<String, Object>>) reqMap.get("relatedDocList");
			if(relatedDocList == null) relatedDocList = new ArrayList<>();
			
			part = WTPart.newWTPart();
			
			part.setNumber(number);
			part.setName(name);
			part.setDefaultUnit(QuantityUnit.getQuantityUnitDefault());
			part.setPartType(PartType.toPartType(wtPartType));
			part.setSource(Source.toSource(source));
			
			ViewHelper.assignToView(part, ViewHelper.service.getView(view));
			
			
			Map<String,String> locationMap = PartPropList.PART_CREATION_DATA.getLocations();
			
			String unit = StringUtil.checkNull((String) reqMap.get("p_unit"));
			reqMap.put("location", locationMap.get(unit));
			
			part = (WTPart)CommonHelper.service.setVersiondDefault(part, reqMap);
			
			part = (WTPart) PersistenceHelper.manager.save(part);
			
			
			Map<String,String> partPropMap = PartPropList.PART_CREATION_DATA.getProps();
			
			for ( String jspElementName : partPropMap.keySet() ) {
				String val = StringUtil.checkNull((String) reqMap.get(jspElementName));
				if(val.length() > 0)IBAUtil.changeIBAValue(part, partPropMap.get(jspElementName), val, "string");
		   }
			
//			ApprovalHelper.service.registApproval(part, approvalList, appState, null);
			
			if(relatedDocList.size() > 0) {
				addPartToDocLink(part, relatedDocList, false);
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
		
		return part;
	}
	
	@Override
	public WTPart modifyPartAction(Map<String, Object> reqMap) throws Exception{
		WTPart part = null;
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			List<Map<String, Object>> relatedDocList = (List<Map<String, Object>>) reqMap.get("relatedDocList");
			
			if(oid.length() > 0) {
				part = (WTPart) CommonUtil.getObject(oid);
				
				//기본 정보
				String name = StringUtil.checkNull((String) reqMap.get("name"));
				//String endDate = StringUtil.checkNull((String) reqMap.get("ENDDATE")); // 폐기처리 시
				
				//check out - working copy
				part = (WTPart) ObjectUtil.checkout(part);
				
				part = (WTPart) PersistenceHelper.manager.modify(part);
				
				//checkin
				part = (WTPart) ObjectUtil.checkin(part);
				
				//set IBA
				Map<String,String> partPropMap = PartPropList.PART_CREATION_DATA.getProps();
				
				for ( String jspElementName : partPropMap.keySet() ) {
					String val = StringUtil.checkNull((String) reqMap.get(jspElementName));
					if(val.length() > 0) IBAUtil.changeIBAValue(part, partPropMap.get(jspElementName), val, "string");
			    }
				
				setPartToEPMAttribute(part, reqMap);
				
				//part name change
				if (!part.getName().equals(name)) {
					WTPartMaster partMaster = (WTPartMaster) (part.getMaster());
					WTPartMasterIdentity identity = (WTPartMasterIdentity) partMaster.getIdentificationObject();
					identity.setName(name);
					partMaster = (WTPartMaster) IdentityHelper.service.changeIdentity(partMaster, identity);
				}
				
				addPartToDocLink(part, relatedDocList, true);
				
				//폐기 처리
				/*
				 * if(endDate.length() > 0) { setWithdrawnPart(part, endDate); }
				 */
				
				EPMDocument epm3D = EpmHelper.manager.getEPMDocument(part);
				
//				if(epm3D != null) {
//					IBAUtil.copyAttribute(part, epm3D);
//				}
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
		
		return part;
	}
	
	/**
	 * 
	  * @desc : SPEC, MAker 속성 전파
	  * @author : tsuam
	  * @date : 2023. 1. 9.
	  * @method : setPartToEPMAttribute
	  * @param part
	  * @throws Exception void
	 */
	public void setPartToEPMAttribute(WTPart part, Map<String, Object> reqMap) throws Exception{
		
		EPMDocument epm = EpmHelper.manager.getEPMDocument(part);
		
		if(epm != null) {
			
			
			//epm = (EPMDocument) ObjectUtil.checkout(epm);
			//epm = (EPMDocument) ObjectUtil.checkin(epm);
			//epm = (EPMDocument) PersistenceHelper.manager.refresh(epm);
			
			//set IBA
			Map<String,String> partPropMap = EpmPropList.EPM_CREATION_DATA.getProps();
			
			for ( String jspElementName : partPropMap.keySet() ) {
				String val = StringUtil.checkNull((String) reqMap.get(jspElementName));
				if(val.length() > 0) IBAUtil.changeIBAValue(epm, partPropMap.get(jspElementName), val, "string");
			}
			
			//3D 변환
			//EpmPublishUtil.publish(epm);
			
			
			EPMDocument epm2D = EpmHelper.manager.getDrawing((EPMDocumentMaster)epm.getMaster());
			
			//2D 변환
			if(epm2D != null) {
				
				//EpmPublishUtil.publishL(epm2D);
				
			}
			
		}
		
	}
	
	@Override
	public WTPart revisePartAction(Map<String, Object> reqMap) throws Exception{
		WTPart revisedPart = null;
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			if(oid.length() > 0) {
				WTPart part = (WTPart) CommonUtil.getObject(oid);
				
				revisedPart = (WTPart) ObjectUtil.revise(part);
				
				EPMDocument epm = EpmHelper.manager.getEPMDocument(part);
				
				if(epm != null) {
					EPMDocument newEpm = (EPMDocument) ObjectUtil.revise(epm);
					
					EPMBuildRule newEbr = EpmHelper.manager.getBuildRule(revisedPart);
					
					newEbr.setBuildSource(newEpm);
					
					newEbr = (EPMBuildRule) PersistenceHelper.manager.save(newEbr);
					
					List<EPMReferenceLink> epm2DList = EpmHelper.manager.getEPMReferenceList((EPMDocumentMaster)newEpm.getMaster());
					
					for(EPMReferenceLink epmlink : epm2DList){
						EPMDocument epm2D = epmlink.getReferencedBy();
						if(epm2D.getDocType().toString().equals("CADDRAWING")){
							
							EPMDocument newEpm2D = (EPMDocument) ObjectUtil.revise(epm2D);
							
							EpmPublishUtil.publish(newEpm2D);
						}
					}
				}
				
				List<Map<String, Object>> approvalList = new ArrayList<>();
				
				ApprovalHelper.service.registApproval(revisedPart, approvalList, "TEMP_STORAGE", null);
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
		
		return revisedPart;
	}
	
	@Override
	public WTPart modifyPartAttributeAction(Map<String, Object> reqMap) throws Exception{
		WTPart part = null;
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			if(oid.length() > 0) {
				part = (WTPart) CommonUtil.getObject(oid);
				
				String endDate = StringUtil.checkNull((String) reqMap.get("ENDDATE"));
				String summary = StringUtil.checkNull((String) reqMap.get("Summary"));
				
				IBAUtil.changeIBAValue(part, "ENDDATE", endDate, "string");
				IBAUtil.changeIBAValue(part, "Summary", summary, "string");
				
				//폐기 처리
				if(endDate.length() > 0) {
					setWithdrawnPart(part, endDate);
				}
				
				EPMDocument epm3D = EpmHelper.manager.getEPMDocument(part);
				
//				if(epm3D != null) {
//					IBAUtil.copyAttribute(part, epm3D);
//					
//					if(endDate.length() > 0) {
//						LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged)epm3D, State.toState("WITHDRAWN"), false);
//						
//						List<EPMReferenceLink> list = EpmHelper.manager.getEPMReferenceList((EPMDocumentMaster)epm3D.getMaster());
//						
//						for(EPMReferenceLink link : list){
//							EPMDocument epm2D = link.getReferencedBy();
//							LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged)epm2D, State.toState("WITHDRAWN"), false);
//							
//						}
//					}
//				}
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
		
		return part;
	}
	
	@Override
	public WTPart withdrawPartAction(Map<String, Object> reqMap) throws Exception{
		WTPart part = null;
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			if(oid.length() > 0) {
				part = (WTPart) CommonUtil.getObject(oid);
				
				LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged) part, State.toState("WITHDRAWN"), false);
				
				EPMDocument epm = EpmHelper.manager.getEPMDocument(part);
				
				EPMDocument epm2D = null;
				if(epm != null) {
					LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged) epm, State.toState("WITHDRAWN"), false);
					
					epm2D = EpmHelper.manager.getDrawing((EPMDocumentMaster)epm.getMaster());
					
					if(epm2D != null) {
						LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged) epm2D, State.toState("WITHDRAWN"), false);
					}
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
		
		return part;
	}
	
	
	
	@Override
	public void deletePartAction(Map<String, Object> reqMap) throws Exception{
		
		Transaction trx = new Transaction();
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		try {
			trx.start();
			
			String partState = "";
			if(oid.length() > 0) {
				WTPart part = (WTPart) CommonUtil.getObject(oid);
				PartData partData = new PartData(part);
				
				partState = partData.getState();
				PersistenceHelper.manager.delete(part);
				
				if(partState.equals("APPROVED")) {
					//ERPInterface.send(part, true);
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
	public WTPart createProjectPartAction(String pjtNo,String pjtName) throws Exception{
		WTPart part = null;
		
		Transaction trx = new Transaction();
		
		try {
			trx.start();
			String log ="";
			LOGGER.info("createProjectPartAction " + pjtNo +"," + pjtName);
			//기본 정보
			//String location = StringUtil.checkNull((String) reqMap.get("location"));
			part = PartHelper.manager.getPart(pjtNo);
			
			if(part != null){
				LOGGER.info("createProjectPartAction EXIST " + pjtNo +"," + pjtName);
				return part;
			}
				LOGGER.info("createProjectPartAction CREATE " + pjtNo +"," + pjtName);
			
			String number = pjtNo;
			String name = pjtName;
			String unit = "set";
			String materialType = number.substring(0,1);
			
			
			//String lifecycle = "LC_Default"; 						// LifeCycle
			String view = "Design";									// view
			String wtPartType = "project";
			String source = "make";
			String locationFolder = "01.Product";
			String location = "Default/Material/" + locationFolder;
			Map<String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("materialType", number.substring(0,1));
			//부품 생성
			part = WTPart.newWTPart();
			
			//set properties
			part.setNumber(number);
			part.setName(name);
			part.setDefaultUnit(QuantityUnit.toQuantityUnit(unit));
			part.setPartType(PartType.toPartType(wtPartType));
			part.setSource(Source.toSource(source));
			
			ViewHelper.assignToView(part, ViewHelper.service.getView(view));
		
			reqMap.put("location", location);
			//reqMap.put("lifecycle", lifecycle);
			reqMap.put("materialType", materialType);
			
			part = (WTPart)CommonHelper.service.setVersiondDefault(part, reqMap);
			//save part
			part = (WTPart) PersistenceHelper.manager.save(part);
			
			//set IBA
			setAttributes(part, reqMap);
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		
		return part;
	}
	
	public void deletePartToDocLink(WTPart part) throws Exception {
    	QueryResult results = PersistenceHelper.manager.navigate(part, WTPartDescribeLink.DESCRIBED_BY_ROLE, WTPartDescribeLink.class, false);
    	
    	WTSet ws = new WTHashSet(results);
    	
        PersistenceServerHelper.manager.remove(ws);
    }
	
	public void setAttributes(WTPart part, Map<String, Object> reqMap) {
		
		//IBA
		String treatment = StringUtil.checkNull((String) reqMap.get("unit_cost"));
		String serialNo = StringUtil.checkNull((String) reqMap.get("finish"));
		String category = StringUtil.checkNull((String) reqMap.get("carry_over"));
		String material = StringUtil.checkNull((String) reqMap.get("allowable_pressure"));
		
		IBAUtil.changeIBAValue(part, CadAttributeDNC.ATT_TREATMENT.getKey(), treatment, "string");
		IBAUtil.changeIBAValue(part, CadAttributeDNC.AN_PRODUCT_SERIALNO.getKey(), serialNo, "string");
		IBAUtil.changeIBAValue(part, CadAttributeDNC.ATT_CATEGORY.getKey(), category, "string");
		IBAUtil.changeIBAValue(part, CadAttributeDNC.ATT_MATERIAL.getKey(), material, "string");
	}
	
	@Override
	public void setWithdrawnPart(WTPart part, String endDate) throws Exception {
		
		if(!"APPROVED".equals(part.getLifeCycleState().toString())) {
			ApprovalHelper.service.deleteApproval(part);
		}
		
		//부품 상태 변경
		LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged)part, State.toState("WITHDRAWN"), false);
		
		//드로잉 파트 폐기 및 ENDDATE 전송
		String number = part.getNumber();

		List<WTPart> drawingPartList = PartHelper.manager.getDrawingPartList(number);
		
		for(WTPart drwPart : drawingPartList) {
			drwPart = (WTPart) LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged)drwPart, State.toState("WITHDRAWN"), false);
			
			IBAUtil.changeIBAValue(drwPart, "ENDDATE", endDate, "string");
		}
		
		//드로잉 .drw, .prt 폐기 및 .prt ENDDATE 세팅
		List<EPMDocument> list = EpmHelper.manager.getPartXEPMList(number);
		
		for(EPMDocument drw : list) {
			LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged)drw, State.toState("WITHDRAWN"), false);
			
			if("CADCOMPONENT".equals(drw.getDocType().toString())) {
				IBAUtil.changeIBAValue(drw, "ENDDATE", endDate, "string");
			}
		}
	}

	@Override
	public WTPart getLastWTPart(WTPartMaster master) throws Exception {
		WTPart part = null;
		long longoid = CommonUtil.getOIDLongValue(master);
		Class class1 = WTPart.class;

		QuerySpec qs = new QuerySpec();
		int i = qs.appendClassList(class1, true);

		qs.appendWhere(new SearchCondition(class1, "iterationInfo.latest", SearchCondition.IS_TRUE, true),
				new int[] { i });

		qs.appendAnd();
		qs.appendWhere(new SearchCondition(class1, "checkoutInfo.state", "<>", "wrk"), new int[] { i });

		qs.appendAnd();
		qs.appendWhere(new SearchCondition(class1, "masterReference.key.id", SearchCondition.EQUAL, longoid),
				new int[] { i });

		// 최신 이터레이션
		qs.appendAnd();
		qs.appendWhere(VersionControlHelper.getSearchCondition(WTPart.class, true), new int[] { i });

		// 최신 버젼
		SearchUtil.addLastVersionCondition(qs, class1, i);

		QueryResult qr = PersistenceHelper.manager.find(qs);
		while (qr.hasMoreElements()) {

			Object obj[] = (Object[]) qr.nextElement();
			part = (WTPart) obj[0];

		}
		return part;
	}

	@Override
	public WTPart getPart(String number, String version) throws WTException {
		if( number == null || version == null){
			return null;
		}
		
		QuerySpec qs = new QuerySpec(WTPart.class);

		qs.appendWhere(VersionControlHelper.getSearchCondition(WTPart.class, true), new int[] { 0 });
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(WTPart.class, "master>number", "=", number), new int[] { 0 });
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(WTPart.class, "versionInfo.identifier.versionId", "=", version),
				new int[] { 0 });
		QueryResult qr = PersistenceHelper.manager.find(qs);
		if (qr.hasMoreElements()) {
			return (WTPart) qr.nextElement();
		}

		return null;
	}

	@Override
	public EPMBuildRule getBuildRule(Object obj) throws WTException {
		QueryResult qr = null;
		if (obj instanceof WTPart) {
			WTPart part = (WTPart) obj;
			qr = PersistenceHelper.manager.navigate(part, "buildSource", EPMBuildRule.class, false);
		} else {
			EPMDocument epm = (EPMDocument) obj;
			qr = PersistenceHelper.manager.navigate(epm, "buildTarget", EPMBuildRule.class, false);
		}

		while (qr.hasMoreElements()) {
			EPMBuildRule ebr = (EPMBuildRule) qr.nextElement();
			if (!WorkInProgressHelper.isWorkingCopy((Workable) ebr.getBuildSource()))
				return ebr;
		}

		return null;
	}

	@Override
	public WTPart getPart(String number) throws Exception {
		WTPart part = null;
		try {
			if("".equals(number) || number.length() <1){
				return part;
			}
			
			Class class1 = WTPart.class;

			QuerySpec qs = new QuerySpec();
			int i = qs.appendClassList(class1, true);

			// 최신 이터레이션
			qs.appendWhere(VersionControlHelper.getSearchCondition(class1, true), new int[] { i });

			// 최신 버젼
			SearchUtil.addLastVersionCondition(qs, class1, i);

			qs.appendAnd();
			qs.appendWhere(new SearchCondition(class1, WTPart.NUMBER, SearchCondition.EQUAL, number),
					new int[] { i });
			
			QueryResult qr = PersistenceHelper.manager.find(qs);

			while (qr.hasMoreElements()) {

				Object obj[] = (Object[]) qr.nextElement();
				part = (WTPart) obj[0];

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return part;
	}

	@Override
	public WTPart createPartAction2(Map<String, Object> reqMap) throws Exception {
		WTPart part = null;
		
		Transaction trx = new Transaction();
		
		try {
			trx.start();
			
			//기본 정보
			//String location = StringUtil.checkNull((String) reqMap.get("location"));
			String materialType = StringUtil.checkNull((String) reqMap.get("materialType"));
			String number = StringUtil.checkNull((String) reqMap.get("number"));
			String name = StringUtil.checkNull((String) reqMap.get("name"));
			String unit = StringUtil.checkNull((String) reqMap.get("unit"));
			
			String lifecycle = StringUtil.checkNull((String) reqMap.get("lifecycle"));							// LifeCycle
			String view = StringUtil.checkNull((String) reqMap.get("view"));									// view
			String wtPartType = StringUtil.checkNull((String) reqMap.get("wtPartType"));
			String source = StringUtil.checkNull((String) reqMap.get("source"));

			String epmContainer = StringUtil.checkNull((String) reqMap.get("epmContainer"));
			
			//epm
			String epmType = StringUtil.checkNull((String) reqMap.get("epmType"));
			String cadType = StringUtil.checkNull((String) reqMap.get("cadType"));
			String epmLocation = StringUtil.checkNull((String) reqMap.get("epmLocation"));
			String primary = StringUtil.checkNull((String) reqMap.get("PRIMARY"));
			
			//결재 정보
			String appState = StringUtil.checkNull((String) reqMap.get("appState"));
			
			List<Map<String, Object>> approvalList = (List<Map<String, Object>>) reqMap.get("approvalList");
			
			//부품 생성
			part = WTPart.newWTPart();
			
			//set properties
			part.setNumber(number);
			part.setName(name);
			part.setDefaultUnit(QuantityUnit.toQuantityUnit(unit));
			part.setPartType(PartType.toPartType(wtPartType));
			part.setSource(Source.toSource(source));
			
			ViewHelper.assignToView(part, ViewHelper.service.getView(view));
			
			String locationFolder = "";
			if("M".equals(materialType)) {
				locationFolder = "02.Module";
			} else if("U".equals(materialType)) {
				locationFolder = "03.Unit";
			} else if("A".equals(materialType)) {
				locationFolder = "04.Assy";
			} else if("P".equals(materialType)) {
				locationFolder = "05.Part";
			}
			
			String location = "Default/Material/" + locationFolder;

			reqMap.put("location", location);
			part = (WTPart)CommonHelper.service.setVersiondDefault(part, reqMap);
			//save part
			part = (WTPart) PersistenceHelper.manager.save(part);
			
			//set IBA
			setAttributes(part, reqMap);
			
			if("control".equals(epmType)) {
				EpmHelper.service.createControlEPM(part, epmContainer, epmLocation, primary);
			} else if("design".equals(epmType)) {
				EpmHelper.service.createDesignEPM(cadType, part, epmContainer,epmLocation);
			}
			
			//결재선 지정
			if("A".equals(materialType) || "P".equals(materialType)) {
				ApprovalHelper.service.registApproval(part, approvalList, appState, null);
			}
			
			//권한
			RevisionControlled per = (RevisionControlled) part;
			AdminHelper.service.setAuthToObject(per, null);
			
			ChangeECOHelper.service.connectEcaPart(part,reqMap);
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		
		return part;
	}
	
	@Override
	public List<Map<String,Object>> createPartMultiAction(Map<String, Object> reqMap)  throws Exception{
		Transaction trx = new Transaction();
		List<Map<String,Object>> returnItem = new ArrayList<Map<String,Object>>();
		
		try {
			List<Map<String,Object>> itemList = (List<Map<String,Object>>)reqMap.get("itemList");

			String createType = (String)reqMap.get("createType");
			
			trx.start();
			
			for(Map<String,Object> map : itemList) {
				 map.put("createType", createType);
				 WTPart part = createPartAction(map);
				
				 if(part != null) {
					 map.put("oid", CommonUtil.getOIDString(part));
					 map.put("return", true);
					
				 }else {
					 map.put("oid", CommonUtil.getOIDString(part));
					 map.put("return",false );
				 }
				 returnItem.add(map);
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
		
		return returnItem;
	}
	
	/**
	 * @methodName : addPartToDocLink
	 * @author : shjeong
	 * @date : 2023.10.19
	 * @param part
	 * @param doc
	 * @param isDelete
	 * @throws Exception
	 * @description :
	 */
	@Override
	public void addPartToDocLink(WTPart part, List<Map<String,Object>> docList, boolean isDelete) throws Exception {
		
		if (isDelete) {
			deleteWTPartDescribeLink(part);
		}

		WTCollection wc = new WTArrayList();

		for(Map<String,Object> item : docList) {
			String oid = (String)item.get("oid");
			WTPartDescribeLink link = WTPartDescribeLink.newWTPartDescribeLink(part, (E3PSDocument)CommonUtil.getObject(oid));
			wc.add(link);
		}

		if (wc.size() > 0) {
			PersistenceServerHelper.manager.insert(wc);
		}
		
	}
	
	
	@Override
	public void deleteWTPartDescribeLink(WTPart part) throws Exception {
		QueryResult results = PersistenceHelper.manager.navigate(part, WTPartDescribeLink.DESCRIBED_BY_ROLE, WTPartDescribeLink.class, false);
		WTSet ws = new WTHashSet(results);
        PersistenceServerHelper.manager.remove(ws);
	}
	
	@Override
	public WTPart addRevision(Map<String,Object> reqMap) throws Exception{
		Transaction trx = new Transaction();
		String oid = StringUtil.checkNull((String)reqMap.get("oid"));
		String revision = StringUtil.checkNull((String)reqMap.get("revision"));
		WTPart returnPart = null;
		try {
			
			trx.start();
			
			if(oid.length() > 0) {
				
				WTPart part = (WTPart) CommonUtil.getObject(oid);
				Map<String, Object> attributes = IBAUtil.getAttributes(part);
				Set<String> ibaKeySet = attributes.keySet();
				
				for (String ibaKey : ibaKeySet) {
					reqMap.put(ibaKey.toLowerCase(), attributes.get(ibaKey));
				}
				
				String newPartNumber = part.getNumber().substring(0, part.getNumber().length()-1) + revision;
				
				//대칭 품목 생성 여부 확인
				if(PartHelper.manager.getPart(newPartNumber) != null) {
					throw new  Exception(MessageUtil.getMessage("이미 존재하는 부품 리비전 입니다."));
				}
				String partName = part.getName();
				//MAIN 속성
				reqMap.put("name", partName);
				reqMap.put("unit", part.getDefaultUnit().toString());
				reqMap.put("location", part.getLocation());
				reqMap.put("number", newPartNumber);
				reqMap.put("appState", "TEMP_STORAGE");
				
				returnPart = createPartAction(reqMap);
				
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
		
		return returnPart;
	}
	
	@Override
	public List<String> checkPartRevision(WTPart part) throws Exception{
		
		List<String> result = new ArrayList<String>();
		
		String subPartNumber = part.getNumber().substring(0,part.getNumber().length()-1);
		
		// 채우는 구문
        String[] AlphabetArr = new String[26];
        
        for( int i = 0; i < 26; i++ ) {
        	String alphabet = String.valueOf((char)(65+i));
        	if(PartHelper.manager.getPart(subPartNumber+alphabet) == null) 	result.add(alphabet);
        }
    
        return result;
	}
}
