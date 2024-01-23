package com.e3ps.part.editor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.ObjectUtil;
import com.e3ps.common.util.StringUtil;

import wt.fc.PersistenceHelper;
import wt.part.Quantity;
import wt.part.WTPart;
import wt.part.WTPartMaster;
import wt.part.WTPartUsageLink;
import wt.pom.Transaction;
import wt.services.StandardManager;
import wt.util.WTException;

@SuppressWarnings("serial")
public class StandardBomEditorService extends StandardManager implements BomEditorService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PART.getName());
	
	public static StandardBomEditorService newStandardBomEditorService() throws WTException {
		final StandardBomEditorService instance = new StandardBomEditorService();
		instance.initialize();
		return instance;
	}
	
	@Override
	public WTPart saveChildrenBomAction(Map<String, Object> reqMap) throws Exception{
		
		Transaction trx = new Transaction();
		WTPart pPart = null;
		try {
			trx.start();
			
			String parentOid = StringUtil.checkNull((String) reqMap.get("parentOid"));
			LOGGER.info("parentOid  ::: " + parentOid);
			List<Map<String, Object>> addedItemList = (List<Map<String, Object>>) reqMap.get("addedItemList");
			List<Map<String, Object>> editedItemList = (List<Map<String, Object>>) reqMap.get("editedItemList");
			List<Map<String, Object>> removedItemList = (List<Map<String, Object>>) reqMap.get("removedItemList");
			
			pPart = (WTPart)CommonUtil.getObject(parentOid);
			pPart = (WTPart)ObjectUtil.checkout(pPart);
			
			//추가된 리스트
			LOGGER.info("addedItem =================");
			for(Map<String, Object> addedItem : addedItemList) {
				
				String oid = StringUtil.checkNull((String) addedItem.get("oid"));
				WTPart cPart = (WTPart)CommonUtil.getObject(oid);
				int quantity = (int) addedItem.get("quantity");
				String quantityS = String.valueOf(quantity);
				
				
				bomAddAction(pPart, cPart, quantityS);
				LOGGER.info("oid ::: " + oid);
				LOGGER.info("quantity ::: " + quantity);
				
			}
			
			//수정된 리스트
			LOGGER.info("editedItem =================");
			for(Map<String, Object> editedItem : editedItemList) {
				
				String oid = StringUtil.checkNull((String) editedItem.get("oid"));
				WTPart cPart = (WTPart)CommonUtil.getObject(oid);
				int quantity = (int) editedItem.get("quantity");
				String quantityS = String.valueOf(quantity);
				
				bomModifyAction(pPart, cPart, quantityS);
				LOGGER.info("oid ::: " + oid);
				LOGGER.info("quantity ::: " + quantity);
				
			}
			
			//삭제된 리스트
			LOGGER.info("removedItem =================");
			for(Map<String, Object> removedItem : removedItemList) {
				
				String oid = StringUtil.checkNull((String) removedItem.get("oid"));
				WTPart cPart = (WTPart)CommonUtil.getObject(oid);
				bomDeleteAction(pPart, cPart);
				LOGGER.info("oid ::: " + oid);
			}
			
			pPart = (WTPart)ObjectUtil.checkin(pPart);
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		
		return pPart;
	}
	
	@Override
	public WTPart saveAddedPartAction(Map<String, Object> reqMap) throws Exception{
		
		Transaction trx = new Transaction();
		WTPart pPart = null;
		try {
			trx.start();
			
			String parentOid = StringUtil.checkNull((String) reqMap.get("parentOid"));
			Map<String, Object> addedPart = (Map<String, Object>) reqMap.get("addedPart");
			
			pPart = (WTPart)CommonUtil.getObject(parentOid);
			
			pPart = (WTPart)ObjectUtil.checkout(pPart);
			
			if(addedPart != null) {
				String oid = StringUtil.checkNull((String) addedPart.get("oid"));
				WTPart cPart = (WTPart)CommonUtil.getObject(oid);
				int quantity = 1;
				String quantityS = String.valueOf(quantity);
				
				bomAddAction(pPart, cPart, quantityS);
			}
			
			//pPart = (WTPart)ObjectUtil.checkin(pPart);
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		
		return pPart;
	}
	
	@Override
	public List<WTPart> saveAddedPartListAction(Map<String, Object> reqMap) throws Exception{
		
		Transaction trx = new Transaction();
		List<WTPart> list = new ArrayList<>();
		try {
			trx.start();
			
			String parentOid = StringUtil.checkNull((String) reqMap.get("parentOid"));
			List<Map<String, Object>> addItemList = (List<Map<String, Object>>) reqMap.get("addItemList");
			
			WTPart pPart = (WTPart)CommonUtil.getObject(parentOid);
			
			pPart = (WTPart)ObjectUtil.checkout(pPart);
			
			for(Map<String, Object> addedPart : addItemList) {
				if(addedPart != null) {
					String oid = StringUtil.checkNull((String) addedPart.get("oid"));
					WTPart cPart = (WTPart)CommonUtil.getObject(oid);
					int quantity = 1;
					String quantityS = String.valueOf(quantity);
					
					WTPartUsageLink link = BomEditorHelper.manager.getLink(cPart, pPart);
					if(link == null) {
						bomAddAction(pPart, cPart, quantityS);
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
		
		return list;
	}
	
	@Override
	public WTPart deleteBomPartAction(Map<String, Object> reqMap) throws Exception{
		
		Transaction trx = new Transaction();
		WTPart pPart = null;
		try {
			trx.start();
			
			String parentOid = StringUtil.checkNull((String) reqMap.get("parentOid"));
			List<Map<String, Object>> removedItemList = (List<Map<String, Object>>) reqMap.get("removedItemList");
			
			pPart = (WTPart)CommonUtil.getObject(parentOid);
			
			pPart = (WTPart)ObjectUtil.checkout(pPart);
			
			//삭제된 리스트
			for(Map<String, Object> removedItem : removedItemList) {
				String oid = StringUtil.checkNull((String) removedItem.get("oid"));
				WTPart cPart = (WTPart)CommonUtil.getObject(oid);
				bomDeleteAction(pPart, cPart);
			}
			
			//pPart = (WTPart)ObjectUtil.checkin(pPart);
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		
		return pPart;
	}
	
	@Override
	public WTPart modifyBomPartAction(Map<String, Object> reqMap) throws Exception{
		
		Transaction trx = new Transaction();
		WTPart pPart = null;
		try {
			trx.start();
			
			String parentOid = StringUtil.checkNull((String) reqMap.get("parentOid"));
			Map<String, Object> modifiedPart = (Map<String, Object>) reqMap.get("modifiedPart");
			
			pPart = (WTPart)CommonUtil.getObject(parentOid);
			
			pPart = (WTPart)ObjectUtil.checkout(pPart);
			
			if(modifiedPart != null) {
				String oid = StringUtil.checkNull((String) modifiedPart.get("oid"));
				WTPart cPart = (WTPart)CommonUtil.getObject(oid);
				int quantity = (int) modifiedPart.get("quantity");
				String quantityS = String.valueOf(quantity);
				
				bomModifyAction(pPart, cPart, quantityS);
			}
			
			//pPart = (WTPart)ObjectUtil.checkin(pPart);
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		
		return pPart;
	}
	
	public void bomAddAction(WTPart pPart ,WTPart cPart,String quantity) throws Exception {
		Quantity qt = Quantity.newQuantity(Double.parseDouble(quantity), cPart.getDefaultUnit());
		WTPartUsageLink link = WTPartUsageLink.newWTPartUsageLink(pPart, (WTPartMaster)cPart.getMaster());
		link.setQuantity(qt);
		PersistenceHelper.manager.save(link);
	}
	
	public void bomDeleteAction(WTPart pPart ,WTPart cPart) throws Exception{
		
		WTPartUsageLink link = BomEditorHelper.manager.getLink(cPart, pPart);
		PersistenceHelper.manager.delete(link);
	}
	
	public void bomModifyAction(WTPart pPart ,WTPart cPart,String quantity) throws Exception{
		
		Quantity qt = Quantity.newQuantity(Double.parseDouble(quantity), cPart.getDefaultUnit());
		WTPartUsageLink link = BomEditorHelper.manager.getLink(cPart, pPart);
		link.setQuantity(qt);
		PersistenceHelper.manager.save(link);
		
	}
	
	
	
	
}
