package com.e3ps.common.service;

import java.util.List;
import java.util.Map;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.FolderUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;

import wt.clients.folder.FolderTaskLogic;
import wt.doc.WTDocument;
import wt.doc.WTDocumentMaster;
import wt.doc.WTDocumentMasterIdentity;
import wt.enterprise.RevisionControlled;
import wt.epm.EPMDocument;
import wt.epm.EPMDocumentMaster;
import wt.epm.EPMDocumentMasterIdentity;
import wt.fc.IdentityHelper;
import wt.folder.Folder;
import wt.folder.FolderEntry;
import wt.folder.FolderHelper;
import wt.inf.container.WTContained;
import wt.inf.container.WTContainer;
import wt.inf.container.WTContainerRef;
import wt.lifecycle.LifeCycleException;
import wt.lifecycle.LifeCycleHelper;
import wt.lifecycle.LifeCycleManaged;
import wt.lifecycle.LifeCycleTemplate;
import wt.lifecycle.State;
import wt.part.WTPart;
import wt.part.WTPartMaster;
import wt.part.WTPartMasterIdentity;
import wt.pom.Transaction;
import wt.services.StandardManager;
import wt.util.WTException;

@SuppressWarnings("serial")
public class StandardCommonService extends StandardManager implements CommonService {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());

	public static StandardCommonService newStandardCommonService() throws WTException {
		final StandardCommonService instance = new StandardCommonService();
		instance.initialize();
		return instance;
	}
	
	/**
	 * 
	 * @desc	: RevisionControlled Name 변경시 (WTPart,WTDocument,EPMDocument)
	 * @author	: tsuam
	 * @date	: 2019. 9. 11.
	 * @method	: changeRevsionName
	 * @return	: void
	 * @param rc
	 * @param name
	 * @throws Exception
	 */
	@Override
	public void changeRevisionName(RevisionControlled rc,String name) throws Exception {
		
		if(rc instanceof WTPart){
			WTPart part = (WTPart)rc;
			WTPartMaster partMaster = (WTPartMaster) (part.getMaster());
			WTPartMasterIdentity identity = (WTPartMasterIdentity) partMaster.getIdentificationObject();
			identity.setName(name);
			partMaster = (WTPartMaster) IdentityHelper.service.changeIdentity(partMaster, identity);
		}else if(rc instanceof WTDocument){
			
			WTDocument doc = (WTDocument)rc;
			WTDocumentMaster docMaster = (WTDocumentMaster) (doc.getMaster());
			WTDocumentMasterIdentity identity = (WTDocumentMasterIdentity) docMaster.getIdentificationObject();
			identity.setName(name);
			docMaster = (WTDocumentMaster) IdentityHelper.service.changeIdentity(docMaster, identity);
			
		}else if(rc instanceof EPMDocument){
			
			EPMDocument epm = (EPMDocument)rc;
			EPMDocumentMaster epmMaster = (EPMDocumentMaster) (epm.getMaster());
			EPMDocumentMasterIdentity identity = (EPMDocumentMasterIdentity) epmMaster.getIdentificationObject();
			identity.setName(name);
			epmMaster = (EPMDocumentMaster) IdentityHelper.service.changeIdentity(epmMaster, identity);
		}
	}
	
	
	
	/**
	 * 
	 * @desc	: Versiond Default Setting
	 * @author	: tsuam
	 * @date	: 2019. 9. 11.
	 * @method	: changeRevsionName
	 * @return	: void
	 * @param rc
	 * @param name
	 * @throws Exception
	 */
//	@Override
//	public LifeCycleManaged setVersiondDefault(LifeCycleManaged ver,Map<String, Object> reqMap) throws Exception{
//		
//		String location = StringUtil.checkNull((String)reqMap.get("location"));
//		String lifecycle = StringUtil.checkNull((String)reqMap.get("lifecycle"));
//		String container = StringUtil.checkNull((String)reqMap.get("container"));
//		
//		System.out.println("folder 저장소 ::: " + location);
//		
//		WTContainer product = null;
//		WTContainerRef wtContainerRef = null;	
//		
//		/*Container 없을시 Default*/
//		if(container.length() > 0 ){
//			product = WCUtil.getPDMLinkProduct(container);
//		}else{
//			product = WCUtil.getPDMLinkProduct();
//		}
//		
//		if(product == null){
//			product = WCUtil.getLibrary(container);
//		}
//		wtContainerRef = WTContainerRef.newWTContainerRef(product); 
//		
//		/*제품 set*/
//		((WTContained)ver).setContainer(product);
//		
//		/*Folder set 없을시  객체 초기화*/
//		if(location.length() > 0){
//			Folder folder= FolderTaskLogic.getFolder(location, wtContainerRef);
//			FolderHelper.assignLocation((FolderEntry) ver, folder);
//		}
//		
//		/*Lifecycle set 없을시  객체 초기화*/
//		if(lifecycle.length() > 0){
//			LifeCycleTemplate tmpLifeCycle = LifeCycleHelper.service.getLifeCycleTemplate(lifecycle, wtContainerRef);
//			ver = (LifeCycleManaged) LifeCycleHelper.setLifeCycle(ver, tmpLifeCycle);
//		}
//		
//		return ver;
//	}
		
	@Override
	public LifeCycleManaged setVersiondDefault(LifeCycleManaged ver, Map<String, Object> reqMap) throws Exception {
		String location = StringUtil.checkNull((String)reqMap.get("location"));
		String lifecycle = StringUtil.checkNull((String)reqMap.get("lifecycle"));
		
		WTContainer product = WCUtil.getPDMLinkProduct();
		WTContainerRef wtContainerRef = WTContainerRef.newWTContainerRef(product); 
		
		/*제품 set*/
		((WTContained)ver).setContainer(product);
		
		/*Folder set 없을시  객체 초기화*/
		if(location.length() > 0){
			Folder folder= FolderTaskLogic.getFolder(location, wtContainerRef);
			
			FolderHelper.assignLocation((FolderEntry) ver, folder);
		}
		
		/*Lifecycle set 없을시  객체 초기화*/
		if(lifecycle.length() > 0){
			LifeCycleTemplate tmpLifeCycle = LifeCycleHelper.service.getLifeCycleTemplate(lifecycle, wtContainerRef);
			ver = (LifeCycleManaged) LifeCycleHelper.setLifeCycle(ver, tmpLifeCycle);
		}
		
		return ver;
	}
	
	@Override
	public void saveFolderAction(Map<String, Object> reqMap) throws Exception {

		Transaction trx = new Transaction();
		
		try {
			trx.start();
			
			String folderOid = StringUtil.checkNull((String) reqMap.get("folderOid"));
			
			List<Map<String, Object>> addedItemList = (List<Map<String, Object>>) reqMap.get("addedItemList");
			List<Map<String, Object>> editedItemList = (List<Map<String, Object>>) reqMap.get("editedItemList");
			List<Map<String, Object>> removedItemList = (List<Map<String, Object>>) reqMap.get("removedItemList");
			
			//추가된 리스트
			for(Map<String, Object> addedItem : addedItemList) {
				String name = StringUtil.checkNull((String) addedItem.get("name"));
				Folder parentFolder = FolderUtil.getFolderOid(folderOid);
				Folder createdFolder = FolderUtil.createFolder(parentFolder, name);
				
				if("제어".equals(parentFolder.getName())) {
					FolderUtil.createFolder(createdFolder, "설계도");
					FolderUtil.createFolder(createdFolder, "승인도");
				}
			}
			
			//수정된 리스트
			for(Map<String, Object> editedItem : editedItemList) {
				String name = StringUtil.checkNull((String) editedItem.get("name"));
				String oid = StringUtil.checkNull((String) editedItem.get("oid"));
				FolderUtil.modifyFolder(oid, name);
			}
			
			//삭제된 리스트
			for(Map<String, Object> removedItem : removedItemList) {
				String oid = StringUtil.checkNull((String) removedItem.get("oid"));
				FolderUtil.deleteFolder(oid);
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
	public void changeLCState(LifeCycleManaged lcm, String state)  throws Exception{
		if ((lcm == null) || (state == null))
			return;
		State newState = State.toState(state);
		try {
			/*
			if (((lcm instanceof Workable)) && (CheckInOutTaskLogic.isCheckedOut((Workable) lcm)))
				return;
			if (newState.equals(lcm.getLifeCycleState()))
				return;
			*/
			if (newState.equals(lcm.getLifeCycleState()))
				return;
			LifeCycleHelper.service.setLifeCycleState(lcm, newState, false);
		} catch (LifeCycleException e) {
			e.printStackTrace();
			throw new Exception(e);
		} catch (WTException e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}
}
