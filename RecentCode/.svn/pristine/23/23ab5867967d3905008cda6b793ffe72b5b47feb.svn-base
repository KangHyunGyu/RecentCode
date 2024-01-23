package com.e3ps.part.bomLoader.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.e3ps.common.content.util.ContentUtil;
import com.e3ps.common.drm.E3PSDRMHelper;
import com.e3ps.common.iba.IBAUtil;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.service.CommonHelper;
import com.e3ps.common.util.ObjectUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.epm.service.EpmHelper;
import com.e3ps.epm.util.EpmPublishUtil;
import com.e3ps.part.bomLoader.bean.LoadBomData;
import com.e3ps.part.service.PartHelper;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import wt.epm.EPMDocument;
import wt.epm.EPMDocumentMaster;
import wt.epm.build.EPMBuildRule;
import wt.epm.structure.EPMReferenceLink;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.WTObject;
import wt.fv.uploadtocache.CachedContentDescriptor;
import wt.part.PartType;
import wt.part.Quantity;
import wt.part.QuantityUnit;
import wt.part.Source;
import wt.part.WTPart;
import wt.part.WTPartHelper;
import wt.part.WTPartMaster;
import wt.part.WTPartUsageLink;
import wt.pom.Transaction;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.StandardManager;
import wt.util.WTAttributeNameIfc;
import wt.util.WTException;
import wt.vc.views.ViewHelper;
import wt.vc.wip.WorkInProgressHelper;

@SuppressWarnings("serial")
public class StandardBomLoaderService extends StandardManager implements BomLoaderService{
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PART.getName());
	
	public static StandardBomLoaderService newStandardBomLoaderService() throws WTException {
		final StandardBomLoaderService instance = new StandardBomLoaderService();
		instance.initialize();
		return instance;
	}
	
	@Override
	public List<LoadBomData> checkPartAction(Map<String, Object> reqMap) throws Exception{
		
		List<LoadBomData> list = new ArrayList<>();
		
		QuantityUnit[] unitType = QuantityUnit.getQuantityUnitSet();
		
		Map<String, Object> unitMap = new HashMap<>();
		
		for(QuantityUnit type : unitType) {
			unitMap.put(type.toString().toUpperCase(), type.toString().toUpperCase());
		}
		
		String primary = StringUtil.checkNull((String) reqMap.get("PRIMARY"));
		String orgFileName =StringUtil.checkNull((String) reqMap.get("orgFileName"));
		
		
		String cacheId = primary.split("/")[0];
	 	CachedContentDescriptor cacheDs = new CachedContentDescriptor(cacheId);
//		String uploadPath = cacheDs.getContentIdentity();
	 	String uploadPath = ContentUtil.getUploadPath(cacheDs.getEncodedCCD());
		String tmepPath = uploadPath.substring(0, uploadPath.lastIndexOf("\\"));
		
		
		//String excelName = primary.split("/")[1];
		String filePath = tmepPath+File.separator+orgFileName;
		
		LOGGER.info("orgFileName: " + orgFileName);
		LOGGER.info("filePath: " + filePath);
		//String excelName = primary.split("/")[3];
		//String filePath = primary.split("/")[4];
		
		File templateFile = new File(filePath);
	    
		Workbook wb = Workbook.getWorkbook(templateFile);
		
		Sheet[] sheets = wb.getSheets();
		Map<Integer, String> mapLevel = new HashMap<Integer, String>();
		Map<String, Object> mapPart = new HashMap<>();
		
		for (int i = 0; i < sheets.length; i++) {
			
			int rows = sheets[i].getRows();
			
			int seq = 0;
			
			int firstLevel = 0;
			
			for (int j = 8; j < rows; j++) {
				if (checkLine(sheets[i].getRow(j), 0)) {
					seq++;
					Cell[] cell = sheets[i].getRow(j);
					
					LoadBomData data = new LoadBomData(cell);
					data.setLine(seq);
					
					if(data.getLine() == 1) {
						firstLevel = Integer.parseInt(data.getLevel());
					}
					
					//검증
					checkPart(data, mapPart, mapLevel, unitMap, firstLevel);
					
					list.add(data);
				}	
			}
		}
		
		return list;
	}
	
	@Override
	public List<LoadBomData> checkBomAction(Map<String, Object> reqMap) throws Exception{
		
		List<Map<String, Object>> bomList = (List<Map<String, Object>>) reqMap.get("bomList");
		
		QuantityUnit[] unitType = QuantityUnit.getQuantityUnitSet();
		
		Map<String, Object> unitMap = new HashMap<>();
		Map<String, Object> partMap = new HashMap<>();
		Map<String, Object> linkMap = new HashMap<>();
		Map<String, Object> childrenMap = new HashMap<>();
		
		for(QuantityUnit type : unitType) {
			unitMap.put(type.toString().toUpperCase(), type.toString().toUpperCase());
		}
		
		//전체 리스트
		List<LoadBomData> list = changeTreeDataList(bomList);
		
		//bom check 시작
		for(LoadBomData data : list) {
			checkBom(data, partMap, linkMap, childrenMap);
		}
		
		return list;
	}
	
	@Override
	public List<LoadBomData> loadPartAction(Map<String, Object> reqMap) throws Exception{
		
		List<LoadBomData> list = new ArrayList<>();
		
		List<Map<String, Object>> bomList = (List<Map<String, Object>>) reqMap.get("bomList");
		
		Transaction trx = new Transaction();
		
		try {
			trx.start();
			
			//부품 등록
			for(Map<String, Object> partMap : bomList) {
				LoadBomData data = new LoadBomData(partMap);
				
				makePart(data);
				
				list.add(data);
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
		
		return list;
	}
	
	@Override
	public List<LoadBomData> loadBomAction(Map<String, Object> reqMap) throws Exception{
		
		List<Map<String, Object>> bomList = (List<Map<String, Object>>) reqMap.get("bomList");
		
		Map<Integer, LoadBomData> mapTree = new HashMap<Integer, LoadBomData>();
		List<LoadBomData> list = new ArrayList<>(); 
			
		Map<Integer, LoadBomData> listMap = new LinkedHashMap<>(); 
		
		Transaction trx = new Transaction();
		
		try {
			trx.start();
			
			//hierarchy 구조로 변환
			LoadBomData rootData = changeTreeData(bomList);
			
			//bom load 시작
			makePartTree(null, rootData, new HashMap<String, LoadBomData>(), listMap);
			
			trx.commit();
			trx = null;
			
			for(int i = 0; i < listMap.size(); i++) {
				LoadBomData data = listMap.get(i + 1);
				
				LOGGER.info(data.getLine() + ", " + data.getNumber() + ", " + data.getVerification() + ", " + data.getResult());
				
				list.add(data);
			}
			LOGGER.info("end");
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		
		return list;
	}

	public void makePartTree(LoadBomData pData, LoadBomData data, Map<String, LoadBomData> makedMap, Map<Integer, LoadBomData> listMap) throws Exception {
		
		WTPart part = getWTPart(data.getNumber());
		
		if(pData == null) {
			if("REVISE".equals(data.getChange()) && "Y".equals(data.getCheckChange())) {
				revisePartAction(part, data);
				data.setVerification("성공");
				data.setResult("개정되었습니다.");
			} else if("REVISE".equals(data.getChange()) && "N".equals(data.getCheckChange())) {
				data.setVerification("성공");
				data.setResult("개정하지 않았습니다.");
			} else {
				data.setVerification("성공");
				data.setResult("변경 없음");
			}
		} else {
			if("REVISE".equals(data.getChange()) && "Y".equals(data.getCheckChange())) {
				revisePartAction(part, data);
				data.setVerification("성공");
				data.setResult("개정되었습니다.");
			} else if("REVISE".equals(data.getChange()) && "N".equals(data.getCheckChange())) {
				data.setVerification("성공");
				data.setResult("개정하지 않았습니다.");
			} else if("REVISECHANGEQTY".equals(data.getChange()) && "Y".equals(data.getCheckChange())) {
				revisePartAction(part, data);
				if("REVISE".equals(pData.getChange()) && "Y".equals(pData.getCheckChange())) {
					data.setVerification("성공");
					data.setResult("개정 및 수량 변경되었습니다.");
				} else if("REVISE".equals(pData.getChange()) && "N".equals(pData.getCheckChange())) {
					data.setVerification("성공");
					data.setResult("상위 부품이 개정되지 않아 수량 변경은 진행하지 않았습니다.");
				} else {
					data.setVerification("성공");
					data.setResult("개정 및 수량 변경되었습니다.");
				}
			} else if("REVISECHANGEQTY".equals(data.getChange()) && "N".equals(data.getCheckChange())) {
				if("REVISE".equals(pData.getChange()) && "Y".equals(pData.getCheckChange())) {
					data.setVerification("성공");
					data.setResult("개정하지 않고 수량 변경만 진행하였습니다.");
				} else if("REVISE".equals(pData.getChange()) && "N".equals(pData.getCheckChange())) {
					data.setVerification("성공");
					data.setResult("개정하지 않았고 상위 부품이 개정되지 않아 수량 변경을 진행하지 않았습니다.");
				} else {
					data.setVerification("성공");
					data.setResult("개정하지 않고 수량 변경만 진행하였습니다.");
				}
			} else if("CHANGEQTY".equals(data.getChange())) {
				if("REVISE".equals(pData.getChange()) && "Y".equals(pData.getCheckChange())) {
					data.setVerification("성공");
					data.setResult("수량을 변경하였습니다.");
				} else if("REVISE".equals(pData.getChange()) && "N".equals(pData.getCheckChange())) {
					data.setVerification("성공");
					data.setResult("상위 부품이 개정되지 않아 수량을 변경하지 않았습니다.");
				} else {
					data.setVerification("성공");
					data.setResult("수량을 변경하였습니다.");
				}
			} else if("ADDPART".equals(data.getChange())) {
				if("REVISE".equals(pData.getChange()) && "Y".equals(pData.getCheckChange())) {
					data.setVerification("성공");
					data.setResult("부품을 추가하였습니다.");
				} else if("REVISE".equals(pData.getChange()) && "N".equals(pData.getCheckChange())) {
					data.setVerification("성공");
					data.setResult("상위 부품이 개정되지 않아 부품을 추가하지 않았습니다.");
				} else {
					data.setVerification("성공");
					data.setResult("부품을 추가하였습니다.");
				}
			} else {
				data.setVerification("성공");
				data.setResult("변경 없음");
			}
		}
		
		for(LoadBomData child : data.getPartChildren()){
			makePartTree(data, child, makedMap, listMap);
		}
		
		makeBom(data, makedMap);
		
		makedMap.put(data.getNumber(), data);
		
		listMap.put(data.getLine(), data);
	}
	
	public void makePart(LoadBomData data) throws Exception {
		WTPart productPart = getWTPart(data.getNumber());
		if(productPart == null){
			productPart = createPartAction(data);
			data.setResult("부품 등록 성공");
		} else {
			if("MODIFY".equals(data.getChange())) {
				//productPart = modifyPartAction(productPart, data);
				//data.setResult("부품 수정 성공");
				data.setResult("변경 없음");
			} else {
				data.setResult("변경 없음");
			}
			/*if("MODIFY".equals(data.getChange()) && "Y".equals(data.getCheckChange())) {
				productPart = modifyPartAction(productPart, data);
			}*/
		}
		data.setVerification("성공");
	}
	
	public void makeBom(LoadBomData pData, Map<String, LoadBomData> makedMap) throws Exception {
		
		if(!makedMap.containsKey(pData.getNumber())) {
			WTPart pPart = getWTPart(pData.getNumber());
			
			if(pData.getPartChildren().size() == 0 && !WorkInProgressHelper.isCheckedOut(pPart)) {
				return;
			}
			
			if("INWORK".equals(pPart.getLifeCycleState().toString())) {
				
				for(LoadBomData child : pData.getPartChildren()){
					
					WTPart cPart = getWTPart(child.getNumber());
					
					WTPartUsageLink link = getUsageLink(cPart, pPart);
					
					if(link != null) {
						if(!compareUsageLinkData(link, child)) {
							pPart = (WTPart) ObjectUtil.checkout(pPart);
							
							link = getUsageLink(cPart, pPart);
							
							link = modifyWTPartUsageLink(link, child.getQuantity(), cPart);
						}
					} else if(link == null) {
						pPart = (WTPart) ObjectUtil.checkout(pPart);
						
						createWTPartUsageLink(pPart, cPart, child.getQuantity());
					}
				}
			}
			
			if(WorkInProgressHelper.isCheckedOut(pPart)) {
				pPart = (WTPart) ObjectUtil.checkin(pPart);
			}
		}
		
	}
	
	@Override
	public WTPart createPartAction(LoadBomData data) throws Exception{
		
		WTPart part = null;
		
		Transaction trx = new Transaction();
		
		try {
			trx.start();
			
			//기본 정보
			//String location = StringUtil.checkNull((String) reqMap.get("location"));
			String materialType = data.getClassification();
			String number = data.getNumber();
			String name = data.getName();
			String unit = data.getUnit().toLowerCase();
			String folderName = "";
			
			
			String lifecycle = "LC_Default";							// LifeCycle
			String view = "Design";									// view
			String wtPartType = "separable";
			String source = "make";
			
			if("J".equals(materialType) || "R".equals(materialType)) {
				wtPartType = "project";
				folderName = "01.Product";
			}else if("M".equals(materialType)) {
				wtPartType = "module";
				folderName = "02.Module";
			} else if("U".equals(materialType)) {
				wtPartType = "unit";
				folderName = "03.Unit";
			} else if("A".equals(materialType)) {
				folderName = "04.Assy";
			} else if("P".equals(materialType)) {
				folderName = "05.Part";
			}else{
				folderName ="";
			}
			
			//부품 생성
			part = WTPart.newWTPart();
			
			//set properties
			part.setNumber(number);
			part.setName(name);
			part.setDefaultUnit(QuantityUnit.toQuantityUnit(unit));
			part.setPartType(PartType.toPartType(wtPartType));
			part.setSource(Source.toSource(source));
			
			ViewHelper.assignToView(part, ViewHelper.service.getView(view));
			
			String location = "Default/Material/" + folderName;

			Map<String,Object> reqMap = new HashMap<String, Object>();//reqMap.put("location", location);
			
			reqMap.put("location", location);
			reqMap.put("lifecycle", lifecycle);
			reqMap.put("container", "");
			part = (WTPart)CommonHelper.service.setVersiondDefault(part, reqMap);
			//save part
			part = (WTPart) PersistenceHelper.manager.save(part);
			
			//set IBA
			setAttributes(part, data);
			
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
	public WTPart modifyPartAction(WTPart part, LoadBomData data) throws Exception{
		
		Transaction trx = new Transaction();
		
		try {
			trx.start();
			
			//check out - working copy
			part = (WTPart) ObjectUtil.checkout(part);
			
			//기본 정보
			//String location = StringUtil.checkNull((String) reqMap.get("location"));
			String unit = data.getUnit().toLowerCase();
			
			//set properties
			part.setDefaultUnit(QuantityUnit.toQuantityUnit(unit));

			//save part
			part = (WTPart) PersistenceHelper.manager.modify(part);
			
			//checkin : bom 생성시에 확인후 checkin 함 -> bom 생성 버튼 따로 두어서 체크인 해야함
			part = (WTPart) ObjectUtil.checkin(part);
			
			//set IBA //수정시 속성 변경 없음
			//setModifyAttributes(part, data);
			
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
	public WTPart revisePartAction(WTPart part, LoadBomData data) throws Exception{
		
		Transaction trx = new Transaction();
		
		try {
			trx.start();
			
			//revise
			WTPart revisedPart = (WTPart) ObjectUtil.revise(part);
			
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
	public WTPartUsageLink createWTPartUsageLink(WTPart pPart,WTPart cPart,String quantity) throws Exception {
		
		WTPartUsageLink link = WTPartUsageLink.newWTPartUsageLink(pPart, (WTPartMaster) cPart.getMaster());
		
        link.setQuantity(Quantity.newQuantity(Double.parseDouble(quantity), cPart.getDefaultUnit()));
        
        link = (WTPartUsageLink) PersistenceHelper.manager.save(link);
		
		return link;
	}
	
	@Override
	public WTPartUsageLink modifyWTPartUsageLink(WTPartUsageLink link,String quantity, WTPart cPart) throws Exception {
		
		link.setQuantity(Quantity.newQuantity(Double.parseDouble(quantity), cPart.getDefaultUnit()));
		link = (WTPartUsageLink) PersistenceHelper.manager.save(link);
		
		return link;
	}

	@Override
	public void removeAllLink(WTPart parent) throws WTException {
		QueryResult qr = WTPartHelper.service.getUsesWTPartMasters(parent);
		
		while(qr.hasMoreElements()) {
			WTPartUsageLink element = (WTPartUsageLink) qr.nextElement();
			PersistenceHelper.manager.delete(element);
		}
	}
	
	public List<WTPartUsageLink> getAllLink(WTPart parent) throws WTException {
		List<WTPartUsageLink> list = new ArrayList<>();
		
		QueryResult qr = WTPartHelper.service.getUsesWTPartMasters(parent);
		
		while(qr.hasMoreElements()) {
			WTPartUsageLink element = (WTPartUsageLink) qr.nextElement();
			
			list.add(element);
		}
		
		return list;
	}
	
	public void setAttributes(WTPart part, LoadBomData data) {
		
		//IBA
		String classification = data.getClassification();
		String material = data.getMaterial();
		String snock = data.getSnock();
		String cPartNum = data.getcPartNum();
		String purchaseFlag = data.getPurchaseFlag();
		String summary = data.getSummary();
		String notice = data.getNotice();
		String endDate = data.getEndDate();
		String specification = data.getSpecification();
		
		String partType = data.getPartType();
		String surfaceTreatment = data.getSurfaceTreatment();
		String maker = data.getMaker();
		
		IBAUtil.changeIBAValue(part, "CLASSIFICATION", classification, "string");
		if(purchaseFlag.length() > 0 && ("Y".equals(purchaseFlag) || "N".equals(purchaseFlag))) {
			IBAUtil.changeIBAValue(part, "PURCHASEFLAG", purchaseFlag, "string");
		} else {
			IBAUtil.changeIBAValue(part, "PURCHASEFLAG", "Y", "string");
		}
		IBAUtil.changeIBAValue(part, "MATERIAL", material, "string");
		IBAUtil.changeIBAValue(part, "CPARTNUM", cPartNum, "string");
		if(snock.length() > 0 && ("Y".equals(snock) || "N".equals(snock))) {
			IBAUtil.changeIBAValue(part, "SNOCK", snock, "boolean");
		} else {
			IBAUtil.changeIBAValue(part, "SNOCK", "N", "boolean");
		}
		IBAUtil.changeIBAValue(part, "Summary", summary, "string");
		String partTypeCode = "";
		if("원자재".equals(partType)) {
			partTypeCode = "RAW";
		} else if("부자재".equals(partType)) {
			partTypeCode = "SUB";
		}
		IBAUtil.changeIBAValue(part, "PartType", partTypeCode, "string");
		IBAUtil.changeIBAValue(part, "SurfaceTreatment", surfaceTreatment, "string");
		IBAUtil.changeIBAValue(part, "Maker", maker, "string");
		IBAUtil.changeIBAValue(part, "NOTICE", notice, "string");
		IBAUtil.changeIBAValue(part, "ENDDATE", endDate, "string");
		IBAUtil.changeIBAValue(part, "SPECIFICATION", specification, "string");
	}
	
	public void setModifyAttributes(WTPart part, LoadBomData data) {
		
		//IBA
		String classification = data.getClassification();
		String material = data.getMaterial();
		String snock = data.getSnock();
		String cPartNum = data.getcPartNum();
		String purchaseFlag = data.getPurchaseFlag();
		String summary = data.getSummary();
		String notice = data.getNotice();
		String endDate = data.getEndDate();
		String specification = data.getSpecification();
		
		String partType = data.getPartType();
		String surfaceTreatment = data.getSurfaceTreatment();
		String maker = data.getMaker();
		
		//임시처리
		/*IBAUtil.changeIBAValue(part, "CLASSIFICATION", classification, "string");
		if(purchaseFlag.length() > 0 && ("Y".equals(purchaseFlag) || "N".equals(purchaseFlag))) {
			IBAUtil.changeIBAValue(part, "PURCHASEFLAG", purchaseFlag, "string");
		} else {
			IBAUtil.changeIBAValue(part, "PURCHASEFLAG", "Y", "string");
		}
		IBAUtil.changeIBAValue(part, "NOTICE", notice, "string");
		IBAUtil.changeIBAValue(part, "ENDDATE", endDate, "string");
		IBAUtil.changeIBAValue(part, "SPECIFICATION", specification, "string");*/
	}
	
	public boolean checkLine(Cell[] cell, int line) {
		String value = null;
		try {
			value = cell[line].getContents().trim();
		} catch (ArrayIndexOutOfBoundsException e) {
			e.getMessage();
			return false;
		}
		if (value == null || value.length() == 0)
			return false;
		return true;
	}

	public static String getContent(Cell[] cell, int idx) {
		try {
			String val = cell[idx].getContents();
			if (val == null)
				return "";
			return val.trim();
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		return "";
	}
	
	public static WTPart getWTPart(String partNumber) throws Exception{
		
		return PartHelper.manager.getPart(partNumber);
		
	}
	
	public static WTPartUsageLink getUsageLink(WTPart child, WTPart parent) throws Exception{
		 return (WTPartUsageLink) getLinkObject((WTPartMaster) child.getMaster(), parent);
	}
	
	public static WTPartUsageLink getLinkObject(WTObject roleA, WTObject roleB) throws Exception {
        QuerySpec query = new QuerySpec();
        int linkIndex = query.appendClassList(WTPartUsageLink.class, true);
        SearchCondition cond1 = new SearchCondition(WTPartUsageLink.class, WTAttributeNameIfc.ROLEB_OBJECT_ID, "=", new Long(PersistenceHelper.getObjectIdentifier(roleA).getId()));
        SearchCondition cond2 = new SearchCondition(WTPartUsageLink.class, WTAttributeNameIfc.ROLEA_OBJECT_ID, "=", new Long(PersistenceHelper.getObjectIdentifier(roleB).getId()));
        query.appendWhere(cond1, new int[] { linkIndex });
        query.appendAnd();
        query.appendWhere(cond2, new int[] { linkIndex });
        QueryResult result = PersistenceHelper.manager.find(query);
        if (result.size() == 0) return null;
        Object[] obj = (Object[]) result.nextElement();
        return (WTPartUsageLink) obj[0];
    }
	
	public void checkPart(LoadBomData data, Map<String, Object> mapPart, Map<Integer, String> mapLevel, Map<String, Object> unitMap, int firstLevel) throws Exception {
		
		String result = "검증 완료";
		boolean isError = false;
		//=================EXCEL DATA 검증 START===================
		if(!isError) {
			if(data.getLevel().length() == 0){
				result = " sheet의 " + data.getLine() + "번째 line의  Level 필드의 입력값이 없습니다.";
				isError = true;
			}
		}
		
		int intLevel = 0;
		try{	
			intLevel = Integer.parseInt(data.getLevel());
        }catch(Exception ex){
        	result = " sheet의 " + data.getLine() + "번째 line의  Level 필드의 입력값이 숫자가 아닙니다. 숫자만 입력하십시오.";
        	isError = true;
        }
		
		if(!isError) {
			if(!BomLoaderHelper.manager.checkStoredLength(data.getName(), 60, true)){
				result = " sheet의 " + data.getLine() + "번째 line의  Name 필드의 입력값이  길이(60)를 초과 하였습니다.";
				isError = true;
	        }
		}
		
		if(!isError) {
			if(data.getUnit().length() > 0 && !unitMap.containsKey(data.getUnit().toUpperCase())){
				result = " sheet의 " + data.getLine() + "번째 line의  단위 필드의 입력값이 유효하지 않습니다.";
				isError = true;
	        }
		}
		
		if(!isError) {
			if(data.getQuantity().length() == 0){
				result = " sheet의 " + data.getLine() + "번째 line의  수량 필드의 입력값이 없습니다.";
				isError = true;
	        }
		}
		
		if(!isError) {
			try{
	        	Double.parseDouble(data.getQuantity().trim());
	        }catch(Exception ex){
	        	result = " sheet의 " + data.getLine() + "번째 line의  수량 필드의 입력값이 숫자가 아닙니다. 숫자만 입력하십시오.";
	        	isError = true;
	        }
		}
		
		if(!isError) {
			if(mapPart.containsKey(data.getNumber())) {
				LoadBomData preData = (LoadBomData) mapPart.get(data.getNumber());
				
				String attrCheck = preData.attrCheck(data);
				if(!"".equals(attrCheck)){
	        		int preLine = preData.getLine();
	        		result = " sheet의 " + preLine + "번째 line의  부품과 " + data.getLine() + " 번째 line의 부품의 " + attrCheck + " 속성이 일치 하지 않습니다.";
	        		isError = true;
	        	}
			} else {
				//채번 검증
				boolean isNumberCheck = BomLoaderHelper.manager.isPartValidate(data.getNumber());
				
				if(!isNumberCheck) {
					result = " sheet의 " + data.getLine() + "번째 line의  부품 번호 체계가 적합하지 않습니다.";
					isError = true;
				}
			}
			
			if(!isError) {
				mapPart.put(data.getNumber(), data);
			}
		}
		//=================EXCEL DATA 검증 END===================
		
		//=================부품 DATA 검증 START===================
		if(!isError) {
			WTPart productPart = getWTPart(data.getNumber());
			if(productPart == null){
				result = "검증 완료(신규 등록)";
				//data.setChange("CREATE");
			} else {
				String[] compareResult = comparePartData(productPart, data);
				//boolean compareResult = comparePartData(productPart, data);
				
				if(!"success".equals(compareResult[0])) {
					if("modify".equals(compareResult[0])) {
						//result = compareResult[1] + " 속성이 등록된 부품과 달라 부품 수정을 진행합니다.";
						result = compareResult[1] + " 속성이 등록된 부품과 다릅니다.";
						data.setChange("MODIFY");
					} else if("fail".equals(compareResult[0])) {
						if("APPROVED".equals(productPart.getLifeCycleState().toString())) {
							result = compareResult[1] + " 속성이 등록된 부품과 다릅니다. 승인된 부품은 변경할 수 없습니다.";
						} else if("INWORK".equals(productPart.getLifeCycleState().toString()) || "RETURN".equals(productPart.getLifeCycleState().toString())) {
							result = compareResult[1] + " 속성이 등록된 부품과 다릅니다.";
						}
						isError = true;
					}
				}
			}
		}
		//=================부품 DATA 검증 END===================
		String verification = "실패";
		if(!isError) {
			verification = "성공";
			if("MODIFY".equals(data.getChange())) {
				verification = "수정";
			}
		}
		
		data.setResult(result);
		data.setVerification(verification);
	}
	
	public void checkBom(LoadBomData data, Map<String, Object> partMap, Map<String, Object> linkMap, Map<String, Object> childrenMap) throws Exception {
		
		String result = "검증 완료";
		boolean isError = false;
		
		//자식 중복 체크(리스트 구성시 진행)
		if(data.getVerification() != null && "중복".equals(data.getVerification())) {
			result = data.getResult();
			isError = true;
		}
		
		//=================EXCEL DATA 검증 START===================
		int intLevel = 0;
		try{	
			intLevel = Integer.parseInt(data.getLevel());
        }catch(Exception ex){
        	result = " sheet의 " + data.getLine() + "번째 line의  Level 필드의 입력값이 숫자가 아닙니다. 숫자만 입력하십시오.";
        	isError = true;
        }
		
		/*if(!isError) {
			if(data.getQuantity().length() == 0){
				result = " sheet의 " + data.getLine() + "번째 line의  수량 필드의 입력값이 없습니다.";
				isError = true;
	        }
		}
		
		if(!isError) {
			try{
	        	Double.parseDouble(data.getQuantity().trim());
	        }catch(Exception ex){
	        	result = " sheet의 " + data.getLine() + "번째 line의  수량 필드의 입력값이 숫자가 아닙니다. 숫자만 입력하십시오.";
	        	isError = true;
	        }
		}
		
		if(!isError) {
			if(data.getQuantity().length() == 0){
				result = " sheet의 " + data.getLine() + "번째 line의  수량 필드의 입력값이 없습니다.";
				isError = true;
	        }
		}*/
		if(!isError) {
			if(linkMap.containsKey(data.getParentNumber() + "_" + data.getNumber())) {
				LoadBomData preData = (LoadBomData) linkMap.get(data.getParentNumber() + "_" + data.getNumber());
				
				if(!data.getQuantity().equals(preData.getQuantity())) {
					result = " sheet의 " + preData.getLine() + "번째 line의  수량과 " + data.getLine() + "번째 line의 수량이 다릅니다.";
					isError = true;
				}
			}
		}
		
		if(!isError) {
			if(data.getParentNumber() != null){
				
				boolean isCheck = BomLoaderHelper.manager.isBomValidate(data.getParentNumber(), data.getNumber());
	        	if(!isCheck){
	        		result = " sheet의 " + data.getLine() + "번째 line의 상하위  부품 번호체계가 적합하지 않습니다";
	        		isError = true;
	        	}
	        } else {
	        	if("실패".equals(data.getVerification())) {
	        		result = " sheet의 " + data.getLine() + "번째 line의 상위 부품 오류로 부품 번호체계 검증을 하지 못했습니다.";
	        		isError = true;
	        	}
	        }
		}
		//=================EXCEL DATA 검증 END===================
		
		//=================BOM DATA 검증 START===================
		if(!isError) {
			WTPart productPart = getWTPart(data.getNumber());
			if(productPart == null){
				result = "부품이 존재하지 않습니다.";
				isError = true;
			} else {
				//children 추가 및 수량 확인
				if(!partMap.containsKey(data.getNumber())) {
					boolean checkChild = false;
					for(LoadBomData child : data.getPartChildren()) {
						
						WTPart cPart = getWTPart(child.getNumber());
						
						WTPartUsageLink link = getUsageLink(cPart, productPart);
						
						if(link == null) {
							checkChild = true; //하위에 부품 추가됨
						} else {
							//하위 부품의 수량 변경
							if(!compareUsageLinkData(link, data)) {
								checkChild = true;
							}
						}
						
						if(checkChild) {
							if("APPROVED".equals(productPart.getLifeCycleState().toString())) {
								result = "개정하시겠습니까?";
								data.setChange("REVISE");
							}
							break;
						}
					}
					
					partMap.put(data.getNumber(), data);
				} else {
					LoadBomData preData = (LoadBomData) partMap.get(data.getNumber());
					
					data.setChange(preData.getChange());
				}
				
				//자신의 수량 및 추가 확인
				if(data.getParentNumber() != null) {
					if(!linkMap.containsKey(data.getParentNumber() + "_" + data.getNumber())) {
						WTPart pPart = getWTPart(data.getParentNumber());
						
						WTPartUsageLink link = getUsageLink(productPart, pPart);
						
						if(link != null) {
							boolean checkQty = compareUsageLinkData(link, data);
							
							if(!checkQty) {
								result = "수량이 변경되었습니다.";
								if("REVISE".equals(data.getChange())) {
									data.setChange("REVISECHANGEQTY");
									result = "수량이 변경되었습니다. 개정하시겠습니까?";
								} else {
									data.setChange("CHANGEQTY");
								}
							}
						} else {
							result = "부품이 추가되었습니다.";
							data.setChange("ADDPART");
						}
						
						linkMap.put(data.getParentNumber() + "_" + data.getNumber(), data);
					} else {
						LoadBomData preData = (LoadBomData) linkMap.get(data.getParentNumber() + "_" + data.getNumber());
						
						data.setChange(preData.getChange());
					}
				}
			}
		}
		//=================BOM DATA 검증 END===================
		String verification = "실패";
		if(!isError) {
			verification = "성공";
			if("REVISE".equals(data.getChange())) {
				verification = "개정";
			} else if("CHANGEQTY".equals(data.getChange())) {
				verification = "변경";
			} else if("REVISECHANGEQTY".equals(data.getChange())) {
				verification = "개정, 변경";
			} else if("ADDPART".equals(data.getChange())) {
				verification = "추가";
			}
		}
		
		data.setResult(result);
		data.setVerification(verification);
	}
	
	public String[] comparePartData(WTPart part, LoadBomData data) throws Exception {
		
		String[] ret = new String[2];
		
		ret[0] = "fail";
		ret[1] = "";
		
		if(!data.getUnit().toUpperCase().equals(part.getDefaultUnit().toString().toUpperCase())) {
			if(!"".equals(ret[1])) {
				ret[1] += ", ";
			}
			ret[1] = "단위";
		}
		
		if(!data.getName().toUpperCase().equals(part.getName().toUpperCase())) {
			LOGGER.info(data.getNumber());
			LOGGER.info(data.getName());
			LOGGER.info(String.valueOf(data.getName().length()));
			LOGGER.info(part.getName());
			LOGGER.info(String.valueOf(part.getName().length()));
			if(!"".equals(ret[1])) {
				ret[1] += ", ";
			}
			ret[1] = "품명";
		}
		
		Map<String, Object> partAttributes = IBAUtil.getAttributes(part);
		
		String purchaseFlag = StringUtil.checkNull((String) partAttributes.get("PURCHASEFLAG")).trim();			//구매여부
		String endDate = StringUtil.checkNull((String) partAttributes.get("ENDDATE")).trim();						//종료일자
		String notice = StringUtil.checkNull((String) partAttributes.get("NOTICE")).trim();						//description -> 비고
		String specification = StringUtil.checkNull((String) partAttributes.get("SPECIFICATION")).trim();			//규격
		String summary = StringUtil.checkNull((String) partAttributes.get("Summary")).trim();						//개요
		String material = StringUtil.checkNull((String) partAttributes.get("MATERIAL")).trim();					//material
		String snock = StringUtil.checkNull((String) partAttributes.get("SNOCK")).trim();							//sn자재여부
		String cPartNum= StringUtil.checkNull((String) partAttributes.get("CPARTNUM")).trim();						//참조부품
		String partType = StringUtil.checkNull((String) partAttributes.get("PartType")).trim();					//자재타입
		String surfaceTreatment = StringUtil.checkNull((String) partAttributes.get("SurfaceTreatment")).trim();	//표면처리
		String maker = StringUtil.checkNull((String) partAttributes.get("Maker")).trim();							//Maker
		
		if(!"APPROVED".equals(part.getLifeCycleState().toString()) && "".equals(ret[1])) {
			ret[0] = "modify";
		}
		
		if("".equals(ret[1])) {
			ret[0] = "success";
		}
		
		return ret;
	}
	
	public boolean compareUsageLinkData(WTPartUsageLink link, LoadBomData data) throws Exception {
		
		String quantity = data.getQuantity();  //수량
		
		Double qty = new Double(quantity.trim());
		if(!qty.equals(link.getQuantity().getAmount())) {
			return false;
		}
		
		return true;
	}
	
	public LoadBomData changeTreeData(List<Map<String, Object>> bomList) throws Exception {
		//hierarchy 구조로 변환
		Map<Integer, LoadBomData> mapTree = new HashMap<Integer, LoadBomData>();
		
		String rootLevel = "";
		for(Map<String, Object> partMap : bomList) {
			LoadBomData data = new LoadBomData(partMap);
			
			if("".equals(rootLevel)) {
				rootLevel = data.getLevel();
			}
			int intLevel = Integer.parseInt(data.getLevel());
			
			int pLevel = intLevel - 1;
			
			if(mapTree.get(pLevel) != null) {
				LoadBomData pData = mapTree.get(pLevel);
				
				List<LoadBomData> children = pData.getPartChildren();
				
				for(LoadBomData child : children) {
					if(data.getNumber().equals(child.getNumber())) {
						data.setVerification("중복");
						data.setResult(child.getLine() + " 번째 부품과 중복됩니다.");
					}
				}
				children.add(data);
				
				pData.setPartChildren(children);
				
				mapTree.put(pLevel, pData);
			}
			
			mapTree.put(intLevel, data);
		}

		int rootIntLevel = Integer.parseInt(rootLevel);
		
		LoadBomData rootData = mapTree.get(rootIntLevel);
		
		return rootData;
	}
	
	public List<LoadBomData> changeTreeDataList(List<Map<String, Object>> bomList) throws Exception {
		
		List<LoadBomData> list = new ArrayList<>();
		
		LoadBomData rootData = changeTreeData(bomList);
		
		changeTreeDataList(rootData, list);
		
		return list;
	}
	
	public void changeTreeDataList(LoadBomData data, List<LoadBomData> list) throws Exception {
		
		list.add(data);
		
		List<LoadBomData> children = data.getPartChildren();
		
		for(LoadBomData child : children) {
			child.setParentNumber(data.getNumber());
			
			changeTreeDataList(child, list);
		}
	}
}
