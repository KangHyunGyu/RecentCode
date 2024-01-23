package com.e3ps.epm.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.e3ps.admin.service.AdminHelper;
import com.e3ps.approval.service.ApprovalHelper;
import com.e3ps.common.content.service.CommonContentHelper;
import com.e3ps.common.content.util.ContentUtil;
import com.e3ps.common.drm.E3PSDRMHelper;
import com.e3ps.common.folder.FolderUtil;
import com.e3ps.common.iba.IBAUtil;
import com.e3ps.common.jdf.config.Config;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.message.PLMMsgConfigJSON;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.query.SearchUtil;
import com.e3ps.common.service.CommonHelper;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.ObjectUtil;
import com.e3ps.common.util.POIUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.epm.util.EpmPublishUtil;
import com.e3ps.epm.util.EpmUtil;
import com.e3ps.epm.util.MigExcelData;
import com.e3ps.part.service.BomHelper;
import com.e3ps.part.service.PartHelper;
import com.ptc.wvs.server.util.PublishUtils;

import jxl.Cell;
import wt.clients.folder.FolderTaskLogic;
import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentRoleType;
import wt.enterprise.RevisionControlled;
import wt.epm.E3PSRENameObject;
import wt.epm.EPMApplicationType;
import wt.epm.EPMAuthoringAppType;
import wt.epm.EPMContextHelper;
import wt.epm.EPMDocument;
import wt.epm.EPMDocumentMaster;
import wt.epm.EPMDocumentType;
import wt.epm.build.EPMBuildHistory;
import wt.epm.build.EPMBuildRule;
import wt.epm.structure.EPMDescribeLink;
import wt.epm.structure.EPMReferenceLink;
import wt.epm.workspaces.EPMWorkspaceManagerEvent;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceServerHelper;
import wt.fc.QueryResult;
import wt.folder.Folder;
import wt.folder.FolderEntry;
import wt.folder.FolderHelper;
import wt.fv.uploadtocache.CachedContentDescriptor;
import wt.inf.container.WTContainer;
import wt.inf.container.WTContainerRef;
import wt.lifecycle.LifeCycleHelper;
import wt.lifecycle.LifeCycleManaged;
import wt.lifecycle.State;
import wt.part.Quantity;
import wt.part.SyncedWithCADStatus;
import wt.part.WTPart;
import wt.part.WTPartMaster;
import wt.part.WTPartUsageLink;
import wt.pds.StatementSpec;
import wt.pom.Transaction;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.representation.Representation;
import wt.services.StandardManager;
import wt.util.WTException;
import wt.vc.VersionControlHelper;
import wt.vc.wip.WorkInProgressServiceEvent;

@SuppressWarnings("serial")
public class StandardEpmService extends StandardManager implements EpmService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.EPM.getName());
	
	public static final String ROOTLOCATION = "/Default/Drawing";
	
	public static StandardEpmService newStandardEpmService() throws WTException {
		final StandardEpmService instance = new StandardEpmService();
		instance.initialize();
		return instance;
	}
	
	@Override
	public EPMDocument createEpmAction(Map<String, Object> reqMap) throws Exception{
		EPMDocument epm = null;
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String description = StringUtil.checkNull((String) reqMap.get("description")); // 설명
			
			//첨부파일 정보
			String primary = StringUtil.checkNull((String) reqMap.get("PRIMARY")); // 주 첨부파일
			List<String> secondary = StringUtil.checkReplaceArray(reqMap.get("SECONDARY")); // 첨부파일
			
			//관련 부품 정보
			List<Map<String, Object>> ownershipPartList = (List<Map<String, Object>>) reqMap.get("ownershipPartList"); // 주 부품
			List<Map<String, Object>> relatedPartList = (List<Map<String, Object>>) reqMap.get("relatedPartList"); // 관련 부품
			
			//결재 정보
			String appState = StringUtil.checkNull((String) reqMap.get("appState"));
			List<Map<String, Object>> approvalList = (List<Map<String, Object>>) reqMap.get("approvalList");
			
			String ownershipPartOid = "";
			if(ownershipPartList != null && ownershipPartList.size() > 0) {
				ownershipPartOid = (String) ownershipPartList.get(0).get("oid");
			} else {
				ownershipPartOid = StringUtil.checkNull((String) reqMap.get("partOid"));
			}
			
			WTPart ownershipPart = (WTPart) CommonUtil.getObject(ownershipPartOid);
			
			epm = EPMDocument.newEPMDocument();
			
			//primary check
			String newFileName = "";
			String orgFileName = "";
			String fileName = "";
			String fileDir = "";
			String fileEnd = "";
			File file = null;
			File rfile = null;
			String location = StringUtil.checkNull((String)reqMap.get("location"));
			String[] splitLoc = location.split("/");	// /Default/제어/J-O0601300/~
			if (primary.length() > 0) {
				file = EpmUtil.getPrimaryFileName(primary);
				fileName = primary.split("/")[1];
				orgFileName = file.getAbsolutePath();
				fileDir = file.getParent();
				
				if(isFileNameCheck(fileName)){
	            	throw new Exception(MessageUtil.getMessage("중복된 파일입니다."));
	            }
				
				int lastIndex = fileName.lastIndexOf(".");
				fileEnd = fileName.substring(lastIndex).toLowerCase();
				newFileName = fileDir + File.separator +fileName;
				rfile = new File(newFileName);
				file.renameTo(rfile);
				file = rfile;

				epm.setCADName(fileName);
				EPMDocumentType docType = getEPMDocumentType(fileEnd);
				epm.setDocType(docType);
			} else {
				throw new Exception(MessageUtil.getMessage("첨부파일은 필수입니다."));
			}
			String name = "";
			String number = "";
			
			if(ownershipPart != null) {
				name = ownershipPart.getName();
				number = ownershipPart.getNumber() + fileEnd;
			} else {
				name = StringUtil.checkNull((String) reqMap.get("name"));
				number = fileName;
			}
			
			EPMDocument existEPM = EpmHelper.manager.getEPMDocument(number.toUpperCase());
			if(existEPM != null) {
				throw new Exception(MessageUtil.getMessage("등록된 도면 번호 입니다."));
			}
			
			//set container, location, lifecycle
			epm = (EPMDocument)CommonHelper.service.setVersiondDefault(epm, reqMap);
			
			//set properties
			epm.setNumber(number);
			epm.setName(name);
			epm.setDescription(description);
			
			String applicationType = "MANUAL";
			
			String extName = "";
			
			String authoringType ="";
			if(primary.length() > 0) {
				extName = primary.split("/")[1];
				authoringType = EpmUtil.getAuthoringType(extName);
			}
			
			EPMDocumentMaster epmMaster = (EPMDocumentMaster)epm.getMaster();
			EPMContextHelper.setApplication(EPMApplicationType.toEPMApplicationType(applicationType));
			epmMaster.setOwnerApplication(EPMContextHelper.getApplication());
			EPMAuthoringAppType appType = EPMAuthoringAppType.toEPMAuthoringAppType(authoringType);
			epmMaster.setAuthoringApplication(appType);
			
			//save part
			epm = (EPMDocument) PersistenceHelper.manager.save(epm);
			
			//IBA Set
			setAttributes(epm, reqMap);
			
			//attach files
			CommonContentHelper.service.attach((ContentHolder)epm, primary, secondary);
			
		
			//주 부품 따라 바뀜
			if(ownershipPart != null) {
				//ownershipPart link
				createBuildRule(ownershipPart, epm);
				
				//buildHistory
				EPMBuildRule builder = EpmHelper.manager.getBuildRule(epm);
				createBuildHistory(builder);
				
				//copy IBA
				copyPartToEPMAttribute(ownershipPart, epm);
			} else {
				//결재선 지정
				ApprovalHelper.service.registApproval(epm, approvalList, appState, null);
			}
			
			//relatedPart link
			if(relatedPartList != null) {
				for(Map<String, Object> relatedPartMap : relatedPartList) {
					String oid = (String) relatedPartMap.get("oid");
					
					WTPart part = (WTPart) CommonUtil.getObject(oid);
					
					EPMDescribeLink link = EPMDescribeLink.newEPMDescribeLink(part, epm);
					
					PersistenceServerHelper.manager.insert(link);
				}
			}
			
			//권한
			RevisionControlled per = (RevisionControlled) epm;
			AdminHelper.service.setAuthToObject(per, null);
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		
		return epm;
	}
	
	/**
	 * 부품에서 설계 도면 생성
	 */
	@Override
	public EPMDocument createDesignEpmAction(Map<String, Object> reqMap) throws Exception{
		EPMDocument epm = null;
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String location = StringUtil.checkNull((String) reqMap.get("location"));
			String number = StringUtil.checkNull((String) reqMap.get("number"));
			String name = StringUtil.checkNull((String) reqMap.get("name"));
			String lifecycle = StringUtil.checkNull((String) reqMap.get("lifecycle"));							// LifeCycle
			//File primaryFile = (File)reqMap.get("primaryFile");
			String cadType = StringUtil.checkNull((String) reqMap.get("cadType"));
			String fileName = StringUtil.checkNull((String) reqMap.get("fileName"));
			EPMDocument templateEPM = (EPMDocument)reqMap.get("templateEPM");
			EPMDocumentType docType = EPMDocumentType.toEPMDocumentType(cadType);  //CAD Type
			
			epm = EPMDocument.newEPMDocument();
			
			
			epm.setNumber(number);
			epm.setName(name);
			epm.setCADName(fileName);
			epm.setDocType(docType);
			
			String applicationType = "EPM";  //응용 프로그램
			String authoringType = "PROE";	 //CAD 종류

			EPMDocumentMaster epmMaster = (EPMDocumentMaster)epm.getMaster();
			epmMaster.setOwnerApplication(EPMApplicationType.toEPMApplicationType(applicationType));
			EPMAuthoringAppType appType = EPMAuthoringAppType.toEPMAuthoringAppType(authoringType);
			epmMaster.setAuthoringApplication(appType);
			epm.setAuthoringAppVersion(templateEPM.getAuthoringAppVersion());
			epm.setAuthoringAppVersionReference(templateEPM.getAuthoringAppVersionReference());
			
			epm = (EPMDocument)CommonHelper.service.setVersiondDefault(epm, reqMap);
			
			epm = (EPMDocument) PersistenceHelper.manager.save(epm);
			
			
			EpmPublishUtil.publish(epm);
			
			//권한
			RevisionControlled per = (RevisionControlled) epm;
			AdminHelper.service.setAuthToObject(per, null);
			
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
		
		return epm;
	}
	
	@Override
	public EPMDocument modifyEpmAction(Map<String, Object> reqMap) throws Exception{
		EPMDocument epm = null;
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			if(oid.length() > 0) {
				epm = (EPMDocument) CommonUtil.getObject(oid);
				
				String container = StringUtil.checkNull((String)reqMap.get("container"));
				String location = StringUtil.checkNull((String) reqMap.get("location"));
				String description = StringUtil.checkNull((String) reqMap.get("description"));
				String primary = StringUtil.checkNull((String) reqMap.get("PRIMARY"));
				List<String> secondary = StringUtil.checkReplaceArray(reqMap.get("SECONDARY"));
				List<String> delocIds		= StringUtil.checkReplaceArray(reqMap.get("delocIds"));
				
				//check out - working copy
				epm = (EPMDocument) ObjectUtil.checkout(epm);
				
				//set properties
				epm.setDescription(description);
				
				epm = (EPMDocument) PersistenceHelper.manager.modify(epm);
				
				//checkin
				epm = (EPMDocument) ObjectUtil.checkin(epm);
				
				setAttributes(epm, reqMap);
				
				//folder location
				if(container.length() > 0) {
					
					WTContainer product = WCUtil.getPDMLinkProduct(container);
					
					if(product == null){
						product = WCUtil.getPDMLinkProduct();
					}
					
					WTContainerRef wtContainerRef = WTContainerRef.newWTContainerRef(product); 
					
					if(wtContainerRef != null) {
						Folder folder= FolderTaskLogic.getFolder(location, wtContainerRef);
						
						epm = (EPMDocument) FolderHelper.service.changeFolder((FolderEntry) epm, folder);
					}
				}
				
				//primary check
				String fileName = "";
				String cacheId = "";
				if (primary.length() > 0) {
					
					cacheId = primary.split("/")[0];
					fileName = primary.split("/")[1];
					
					/*if(!epm.getCADName().toUpperCase().equals(fileName.toUpperCase())){
						throw new Exception(MessageUtil.getMessage("동일한 파일명만 수정 가능합니다."));
					}*/
					
					//attach files
					if(cacheId.length() > 0) {
						CommonContentHelper.service.attach((ContentHolder)epm, primary, secondary, delocIds);
					}
					
					E3PSRENameObject.manager.EPMReName(epm, null, null, fileName);
				} 
//				else {
//					throw new Exception(MessageUtil.getMessage("첨부파일은 필수입니다."));
//				}
				
				
				//주첨부파일 드래그 변경으로 인해 첨부파일 관련 함수 추가
				if(secondary.size() > 0) {
					CommonContentHelper.service.delete(epm, ContentRoleType.SECONDARY);
					for (int i = 0; i < secondary.size(); i++) {
						String secondCacheId = secondary.get(i).split("/")[0];
						String epmFileName = secondary.get(i).split("/")[1];
						CachedContentDescriptor cacheDs = new CachedContentDescriptor(secondCacheId);
						CommonContentHelper.service.attach(epm, cacheDs, epmFileName, "", ContentRoleType.SECONDARY);
				    }
					if(delocIds.size() > 0) {
						for (int i = 0; i < delocIds.size(); i++) {
							ApplicationData appData = (ApplicationData) CommonUtil.getObject(delocIds.get(i));
							if(ContentRoleType.SECONDARY.equals(appData.getRole())) {
								CommonContentHelper.service.attach(epm, appData, false);
							}
						}
					}
				}else {
					if(delocIds.size() > 0) {
						CommonContentHelper.service.delete(epm, ContentRoleType.SECONDARY);
						for (int i = 0; i < delocIds.size(); i++) {
							ApplicationData appData = (ApplicationData) CommonUtil.getObject(delocIds.get(i));
							if(ContentRoleType.SECONDARY.equals(appData.getRole())) {
								CommonContentHelper.service.attach(epm, appData, false);
							}
						}
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
		
		return epm;
	}
	
	@Override
	public EPMDocument reviseEpmAction(Map<String, Object> reqMap) throws Exception{
		EPMDocument revisedEpm = null;
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			if(oid.length() > 0) {
				EPMDocument epm = (EPMDocument) CommonUtil.getObject(oid);
				
				WTPart part = PartHelper.manager.getWTPart(epm);
				
				if(part == null) {
					revisedEpm = (EPMDocument) ObjectUtil.revise(epm);
					
					EPMDocument epm2D = EpmHelper.manager.getDrawing((EPMDocumentMaster)epm.getMaster());
					
					if(epm2D != null) {
						epm2D = (EPMDocument) ObjectUtil.revise(epm2D);
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
		
		return revisedEpm;
	}
	
	@Override
	public EPMDocument withdrawEpmAction(Map<String, Object> reqMap) throws Exception{
		EPMDocument epm = null;
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			if(oid.length() > 0) {
				epm = (EPMDocument) CommonUtil.getObject(oid);
				
				WTPart part = PartHelper.manager.getWTPart(epm);
				
				if(part == null) {
					LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged) epm, State.toState("WITHDRAWN"), false);
					
					EPMDocument epm2D = EpmHelper.manager.getDrawing((EPMDocumentMaster)epm.getMaster());
					
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
		
		return epm;
	}
	
	@Override
	public void deleteEpmAction(Map<String, Object> reqMap) throws Exception{
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			if(oid.length() > 0) {
				EPMDocument epm = (EPMDocument) CommonUtil.getObject(oid);
				
				PersistenceHelper.manager.delete(epm);
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
	public boolean isFileNameCheck(String fileName){
    	boolean check = false;
    	try{
    		QuerySpec qs = new QuerySpec(EPMDocumentMaster.class);
    		
    		qs.appendWhere(new SearchCondition(EPMDocumentMaster.class, EPMDocumentMaster.CADNAME, SearchCondition.EQUAL, fileName, false ), new int[] {0});
    		
    		QueryResult rt=PersistenceHelper.manager.find(qs);
    		
    		if(rt.size()>0) return true; 
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	return check;
    }
	
	@Override
	public EPMDocumentType getEPMDocumentType(String fileName){
		
		String type = "OTHER";

		if (".drw".equals(fileName.toLowerCase())){
			type = "CADDRAWING";
		}
		else if (".prt".equals(fileName.toLowerCase())){
			type = "CADCOMPONENT";
		}
		else if (".asm".equals(fileName.toLowerCase())){
			type = "CADASSEMBLY";
		}
		else if (".frm".equals(fileName.toLowerCase())){
			type = "FORMAT";
		}
		else if (".dwg".equals(fileName.toLowerCase())){
			type = "CADDRAWING";
		}
		else if (".igs".equals(fileName.toLowerCase())){
			type = "IGES";
		}
		else if (".iges".equals(fileName.toLowerCase())){
			type = "IGES";
		}
		else if(".gif".equals(fileName.toLowerCase())){
			type = "PUB_GRAPHIC";
		}
		else if(".jpg".equals(fileName.toLowerCase())){
			type = "PUB_GRAPHIC";
		}
		else if(".zip".equals(fileName.toLowerCase())){
			type = "ZIP";
		}
		
        return EPMDocumentType.toEPMDocumentType(type);
    }
	
	/**
	 * 
	 * @desc	: WTPart 생성시 Creo 도면 생성
	 * @author	: tsuam
	 * @date	: 2019. 9. 9.
	 * @method	: createTempEPM
	 * @return	: void
	 */
	@Override
	public void createDesignEPM(String cadType, WTPart part, String container, String location) throws Exception {
		
		//template cad file
		EPMDocument epm = EpmHelper.manager.getCADTemplate(cadType);
		
		String extension = EpmUtil.getExtension(epm.getCADName());
		
		String fileName = part.getNumber().toLowerCase().concat(".").concat(extension);
	
		
		//EPM 속성
		Map<String, Object> reqMap = new HashMap<String, Object>();
		
		//String location = "/Default/Drawing";
		String number = part.getNumber().concat(".").concat(extension);
		String name = part.getName();
		String lifecycle = "LC_Default";							
		
		reqMap.put("container", container); 
		reqMap.put("location", location); 
		reqMap.put("number", number); 
		reqMap.put("name", name);
		reqMap.put("lifecycle", lifecycle);
		reqMap.put("cadType", cadType);
		reqMap.put("fileName", fileName);
		reqMap.put("templateEPM", epm);
		//reqMap.put("primaryFile", copyFile);
		
		EPMDocument epmNew = createDesignEpmAction(reqMap);
		
		/*CAD Copy*/
		ContentHelper.service.copyContent(epm, epmNew);
		
		/*BuildRule 연결*/
		createBuildRule(part, epmNew);
		
		/*BuildHistory 연결*/
		EPMBuildRule builder = EpmHelper.manager.getBuildRule(epmNew);
		createBuildHistory(builder);
		
		/*부품 속성 복사*/
		copyPartToEPMAttribute(part, epmNew);
		
	}
	
	/**
	 * 
	 * @desc	: 부품에서 제어 도면 생성
	 * @author	: tsuam
	 * @date	: 2019. 9. 10.
	 * @method	: createControlEPM
	 * @return	: void
	 * @param part
	 * @param location
	 * @param primary
	 */
	@Override
	public EPMDocument createControlEPM(WTPart part, String container, String location,String primary) throws Exception {
		
		//EPM 속성
		Map<String, Object> reqMap = new HashMap<String, Object>();
		
		File file = EpmUtil.getPrimaryFileName(primary);
		String fileName = file.getName();
		String extension = EpmUtil.getExtension(fileName);
		String number = part.getNumber().concat(".").concat(extension);
		String name = part.getName();
		String lifecycle = "LC_Default";							
		
		reqMap.put("container", container); 
		reqMap.put("location", location); 
		reqMap.put("number", number); 
		reqMap.put("name", name);
		reqMap.put("lifecycle", lifecycle);
		reqMap.put("PRIMARY", primary);
		reqMap.put("partOid", CommonUtil.getOIDString(part));
		EPMDocument epmNew = createEpmAction(reqMap);
		
		return epmNew;
		
		
	}
	
	@Override
	public Map<String, Object> createMultiEpmAction(Map<String, Object> reqMap) throws Exception{
		
		Map<String, Object> returnMap = new HashMap<>();
		
		boolean checkFail = false;
		
		Transaction trx = new Transaction();
		
		try {
			trx.start();
		
			String container = StringUtil.checkNull((String) reqMap.get("container"));
			String location = StringUtil.checkNull((String) reqMap.get("location"));
			String lifecycle = StringUtil.checkNull((String) reqMap.get("lifecycle"));
			
			String primary = StringUtil.checkNull((String) reqMap.get("PRIMARY"));
			List<String> secondary = StringUtil.checkReplaceArray(reqMap.get("SECONDARY"));
			
			List<Map<String, Object>> multiEpmList = new ArrayList<>();
			
			String cacheId = primary.split("/")[0];
		 	CachedContentDescriptor cacheDs = new CachedContentDescriptor(cacheId);
//			String uploadPath = cacheDs.getContentIdentity();
			String uploadPath = ContentUtil.getUploadPath(cacheDs.getEncodedCCD());
			//String tempPath = uploadPath.substring(0, uploadPath.lastIndexOf("\\"));
			
			String excelName = primary.split("/")[1];
			String filePath = uploadPath;
			
			File templateFile = new File(filePath);
		    
			//템플릿이 암호화 파일일 경우 복호화된 파일 가져오기 
			//templateFile = E3PSDRMHelper.manager.getDecryptedFile(templateFile, excelName);
			Config conf = ConfigImpl.getInstance();
			if(conf.getBoolean("drm.enable", false)) {
				templateFile = E3PSDRMHelper.manager.upload(templateFile, excelName);
			}
			
			XSSFWorkbook workbook = POIUtil.getWorkBook(templateFile);
			XSSFSheet sheet = POIUtil.getSheet(workbook, 0);
			int rows = POIUtil.getSheetRow(sheet);
			
			for (int j = 1; j < rows; j++) {
				Map<String, Object> multiMap = new HashMap<>();
				
				Row row = sheet.getRow(j);
				
				String partNumber = StringUtil.checkNull(POIUtil.getCellValue((XSSFCell)row.getCell(0)));
				String epmNumber = StringUtil.checkNull(POIUtil.getCellValue((XSSFCell)row.getCell(1)));
				String epmName = StringUtil.checkNull(POIUtil.getCellValue((XSSFCell)row.getCell(2)));
				String fileName = StringUtil.checkNull(POIUtil.getCellValue((XSSFCell)row.getCell(3)));
				String relatedPartNumber = StringUtil.checkNull(POIUtil.getCellValue((XSSFCell)row.getCell(4)));
				String description = StringUtil.checkNull(POIUtil.getCellValue((XSSFCell)row.getCell(5)));
				
				multiMap.put("partNumber", partNumber);
				multiMap.put("epmNumber", epmNumber);
				multiMap.put("epmName", epmName);
				multiMap.put("fileName", fileName);
				multiMap.put("relatedPartNumber", relatedPartNumber);
				multiMap.put("description", description);
				
				try {

					Map<String, Object> epmMap = new HashMap<>();
					
					epmMap.put("container", container); 
					epmMap.put("location", location); 
					epmMap.put("lifecycle", lifecycle);
					epmMap.put("description", description);
					
					WTPart part = PartHelper.manager.getPart(partNumber);
					if(partNumber.length() > 0 && part == null) {
						throw new Exception(MessageUtil.getMessage("입력된 주부품이 존재하지 않습니다."));
					}
					epmMap.put("partOid", CommonUtil.getOIDString(part));
					
					
					//주도면 없는 경우 사용됨
					epmMap.put("number", epmNumber);
					epmMap.put("name", epmName);
					epmMap.put("appState", "TEMP_STORAGE");
					
					for (String fileStr : secondary) {
						String secFileName = fileStr.split("/")[1];
				
						if(fileName.equals(secFileName)) {
							epmMap.put("PRIMARY", fileStr);
							break;
						}
				    }
					
					//관련 부품 추가
					if(relatedPartNumber.length() > 0) {
						
						List<Map<String, Object>> relatedPartList = new ArrayList<>();
						
						Map<String, Object> relatedPartMap = new HashMap<>();
						
						WTPart relatedPart = PartHelper.manager.getPart(relatedPartNumber);
						
						relatedPartMap.put("oid", CommonUtil.getOIDString(relatedPart));
						
						relatedPartList.add(relatedPartMap);
						
						epmMap.put("relatedPartList", relatedPartList);
					}
					
					createEpmActionNT(epmMap);
					
			    	multiMap.put("result", "O");
			    	multiMap.put("resultMessage", "");
				} catch (Exception e) {
					checkFail = true;
					multiMap.put("result", "X");
					multiMap.put("resultMessage", e.getLocalizedMessage());
					e.printStackTrace();
				}
				
				multiEpmList.add(multiMap);
			}
			
			returnMap.put("list", multiEpmList);
			
			if(!checkFail) {
				trx.commit();
				trx = null;
				returnMap.put("msg", "등록 되었습니다.");
			}else {
				returnMap.put("msg", "등록 실패 되었습니다.");
			}
		} finally {
			if(trx != null) {
				trx.rollback();
			}
		}
		
		return returnMap;
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
	
	/**
	 * 
	 * @desc	: Template 파일 EPMDocumetn 연결
	 * @author	: tsuam
	 * @date	: 2019. 9. 10.
	 * @method	: createHolderToContent
	 * @return	: void
	 * @param epm
	 * @param data
	 * @throws Exception
	 */
	private void createHolderToContent(EPMDocument epm,ApplicationData data)throws Exception{
		
	
		/*
		HolderToContent holder = HolderToContent.newHolderToContent(epm, data) ;
		
		PersistenceHelper.manager.save(holder);
		*/
	}
	
	/**
	 * 
	 * @desc	: OwnerShip 생성
	 * @author	: tsuam
	 * @date	: 2019. 9. 10.
	 * @method	: createBuildRule
	 * @return	: void
	 * @param part
	 * @param epm
	 * @throws Exception
	 */
	private void createBuildRule(WTPart part , EPMDocument epm) throws Exception{
		
		EPMBuildRule bulider = EpmHelper.manager.getBuildRule(part);
		
		if(bulider != null){
			String msg = part.getNumber() +"은 이미 다른 도면과 연결 되었습니다\n다른 부품을 선택해 주세요";
			throw new Exception(MessageUtil.getMessage(msg));
		}
		
		EPMBuildRule link = EPMBuildRule.newEPMBuildRule(epm, part);
		
		PersistenceServerHelper.manager.insert(link);
	
	}
	
	/**
	 * 
	 * @desc	: history 생성
	 * @author	: tsuam
	 * @date	: 2019. 9. 10.
	 * @method	: createBuildHistory
	 * @return	: void
	 * @param builder
	 * @throws Exception
	 */
	private void createBuildHistory(EPMBuildRule builder) throws Exception{
		
		EPMBuildHistory history = EPMBuildHistory.newEPMBuildHistory((EPMDocument)builder.getRoleAObject(), (WTPart)builder.getRoleBObject(), builder.getUniqueID());
		PersistenceServerHelper.manager.insert(history);
	}
	
	/**
	 * @desc : IBA copy 
	 * @author : sangylee
	 * @date : 2019. 9. 16.
	 * @method : copyPartToEPMAttribute
	 * @return : void
	 * @param part
	 * @param epm
	 * @throws Exception
	 */
	private void copyPartToEPMAttribute(WTPart part, EPMDocument epm) throws Exception {
//		IBAUtil.copyAttribute(part, epm);
	}
	
	@Override
	public void createCADBOMAction(Map<String, Object> reqMap) throws Exception{
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));

			EPMDocument rootEpm = (EPMDocument) CommonUtil.getObject(oid);
			
			//BuildRule 생성
			long startTime = System.currentTimeMillis();
			createPartToEPM(rootEpm);
			long endTime = System.currentTimeMillis();
			LOGGER.info("createPartToEPM Time ::: " + (endTime - startTime)/1000.0 + " second(s)");
			
			//BOM 생성
			startTime = System.currentTimeMillis();
			createWTPartUsageLink(rootEpm);
			endTime = System.currentTimeMillis();
			LOGGER.info("createWTPartUsageLink Time ::: " + (endTime - startTime)/1000.0 + " second(s)");
			
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
	
	public void createPartToEPM(EPMDocument rootEpm) throws Exception{
		
		List<EPMDocument> childrenListAll = new ArrayList<>();
		Map<String, Object> mapEPM = new HashMap<String,Object>();
		EpmHelper.manager.getCADChildrenList(rootEpm, mapEPM, childrenListAll);
		
		for(EPMDocument epm : childrenListAll) {
			String partNo = IBAUtil.getAttrValue(epm, "PART_NO");
			
			//part No 있는지 체크(있어야함 )
			if(partNo.length() > 0 ){
				boolean isPartNOCheck = EpmHelper.manager.isPartNOCheck(epm.getNumber(), partNo); //epmNumber 와 part_no 동일 하면 true,다른 면 false

				//part No와 epmNumber가 다른지 체크(같아야 함)
				if(isPartNOCheck){ 
					WTPart part = PartHelper.manager.getWTPart(epm);
					
					//epm과 연결된 부품 있는지 여부(없어야 함)
					if(part == null){
						part = PartHelper.manager.getPart(partNo);
						
						//partNo가 번호인 부품이 있는지 체크(있어야 함)
						if(part != null){
							EPMDocument buildRuleEpm = EpmHelper.manager.getEPMDocument(part);
							
							//부품과 연결된 epm이 있는지(없어야 함)
							if(buildRuleEpm == null){
								//부품 도면 연결
								
								String epmState = epm.getLifeCycleState().toString();
								
								LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged)part, State.toState(epmState), false);
								
								createBuildRule(part, epm);
								
								EPMBuildRule builder = EpmHelper.manager.getBuildRule(epm);
								createBuildHistory(builder);
							}
						}
					}
				}
			}
		}
	}
	
	public static void createWTPartUsageLink(EPMDocument rootEpm) throws Exception{
		createCADToBOM(rootEpm);
	}
	
	public static void createCADToBOM(EPMDocument epm) throws Exception{
		
		WTPart part = PartHelper.manager.getWTPart(epm);
		
		if(part == null){
			return;
		}
		
		//하위
		List<EPMDocument> list = EpmHelper.manager.getEPMChild(epm);
		Map<String, Object> mapCount = new HashMap<String, Object>();
		Map<String, Object> mapEPM = new HashMap<String, Object>();
		
		//수량 체크
		for(EPMDocument epmChild : list){
			
			String number = epmChild.getNumber();
			
			mapEPM.put(number, epmChild);
			
			if(mapCount.containsKey(number)){
				int quantity = (int)mapCount.get(number);
				
				quantity = quantity +1;
				
				mapCount.put(number, quantity);
			}else{
				mapCount.put(number, 1);
			}
			
			// 재귀 호출
			createCADToBOM(epmChild);
		}
		
		Iterator it = mapCount.keySet().iterator();
		
		//BOM 생성
		int i = 1;
		while(it.hasNext()){
			
			String key = (String)it.next();
			
			int quantity = (int)mapCount.get(key);
			String stringQuantity = String.valueOf(quantity);
			EPMDocument empChild = (EPMDocument)mapEPM.get(key);
			
			WTPart partChild = PartHelper.manager.getWTPart(empChild);
			
			if(partChild != null){
				WTPartUsageLink usageLink = BomHelper.manager.getLink(partChild, part);
				if(usageLink == null){
					createWTPartUsageLink(part, partChild, stringQuantity);
				}
			}
			i++;
		}
	}
	
	public static void createWTPartUsageLink(WTPart pPart,WTPart cPart,String quantity) throws Exception {
		
		WTPartUsageLink link = WTPartUsageLink.newWTPartUsageLink(pPart, (WTPartMaster) cPart.getMaster());
		
        link.setQuantity(Quantity.newQuantity(Double.parseDouble(quantity), cPart.getDefaultUnit()));
        
        //CAD 도면 연결시 필수 - WGM과 동기화
        SyncedWithCADStatus syWithCad = SyncedWithCADStatus.toSyncedWithCADStatus("yes");
		link.setCadSynchronized(syWithCad);
		PersistenceServerHelper.manager.insert(link);
		
	}
	
	
	@Override
	public void changePartNO(String oid){
		
		EPMDocument epm = (EPMDocument)CommonUtil.getObject(oid);
		
		String part_no = EpmUtil.getCADToPartNO(epm.getNumber());
		
		IBAUtil.changeIBAValue(epm, "PART_NO", part_no, "string");
		
	}
	
	@Override
	public EPMDocument createEpmActionNT(Map<String, Object> reqMap) throws Exception{
		EPMDocument epm = null;
		
		try {
			String description = StringUtil.checkNull((String) reqMap.get("description"));
			
			String primary = StringUtil.checkNull((String) reqMap.get("PRIMARY"));
			List<String> secondary = StringUtil.checkReplaceArray(reqMap.get("SECONDARY"));
			
			List<Map<String, Object>> ownershipPartList = (List<Map<String, Object>>) reqMap.get("ownershipPartList");
			List<Map<String, Object>> relatedPartList = (List<Map<String, Object>>) reqMap.get("relatedPartList");
			
			//결재 정보
			String appState = StringUtil.checkNull((String) reqMap.get("appState"));
			List<Map<String, Object>> approvalList = (List<Map<String, Object>>) reqMap.get("approvalList");
			
			String ownershipPartOid = "";
			if(ownershipPartList != null && ownershipPartList.size() > 0) {
				ownershipPartOid = (String) ownershipPartList.get(0).get("oid");
			} else {
				ownershipPartOid = StringUtil.checkNull((String) reqMap.get("partOid"));
			}
			
			WTPart ownershipPart = (WTPart) CommonUtil.getObject(ownershipPartOid);
			
			epm = EPMDocument.newEPMDocument();
			
			//primary check
			String newFileName = "";
			String orgFileName = "";
			String fileName = "";
			String fileDir = "";
			String fileEnd = "";
			File file = null;
			File rfile = null;
			String container = StringUtil.checkNull((String)reqMap.get("container"));
			String location = StringUtil.checkNull((String)reqMap.get("location"));
			String[] splitLoc = location.split("/");	// /Default/제어/J-O0601300/~
			if (primary.length() > 0) {
				
				file = EpmUtil.getPrimaryFileName(primary);
				fileName = primary.split("/")[1];
				orgFileName = file.getAbsolutePath();
				fileDir = file.getParent();
				
				if("ELECTRIC".equals(container)) {
					if(splitLoc.length > 3) {
						fileName = splitLoc[3] + "_" + fileName;
					}
				}
				
				if(isFileNameCheck(fileName)){
	            	throw new Exception(MessageUtil.getMessage("중복된 파일입니다."));
	            }
				
				int lastIndex = fileName.lastIndexOf(".");
				fileEnd = fileName.substring(lastIndex).toLowerCase();
				newFileName = fileDir + File.separator +fileName;
				rfile = new File(newFileName);
				file.renameTo(rfile);
				file = rfile;

				epm.setCADName(fileName);
				EPMDocumentType docType = getEPMDocumentType(fileEnd);
				epm.setDocType(docType);
			} else {
				throw new Exception(MessageUtil.getMessage("첨부파일은 필수입니다."));
			}
			String name = "";
			String number = "";
			
			if(ownershipPart != null) {
				name = ownershipPart.getName();
				number = ownershipPart.getNumber() + fileEnd;
			} else {
				name = StringUtil.checkNull((String) reqMap.get("name"));
				number = StringUtil.checkNull((String) reqMap.get("number"));
				if("ELECTRIC".equals(container)) {
					if(splitLoc.length > 3) {
						number = splitLoc[3] + "_" + number + fileEnd;
					}else {
						number = number + fileEnd;
					}
				}else {
					number = number + fileEnd;
				}
			}
			
			EPMDocument existEPM = EpmHelper.manager.getEPMDocument(number.toUpperCase());
			if(existEPM != null) {
				throw new Exception(MessageUtil.getMessage("등록된 도면 번호 입니다."));
			}
			
			//set container, location, lifecycle
			epm = (EPMDocument)CommonHelper.service.setVersiondDefault(epm, reqMap);
			
			//set properties
			epm.setNumber(number);
			epm.setName(name);
			epm.setDescription(description);
			
			String applicationType = "MANUAL";
			
			String extName = "";
			
			String authoringType ="";
			if(primary.length() > 0) {
				extName = primary.split("/")[1];
				authoringType = EpmUtil.getAuthoringType(extName);
			}
			
			EPMDocumentMaster epmMaster = (EPMDocumentMaster)epm.getMaster();
			EPMContextHelper.setApplication(EPMApplicationType.toEPMApplicationType(applicationType));
			epmMaster.setOwnerApplication(EPMContextHelper.getApplication());
			EPMAuthoringAppType appType = EPMAuthoringAppType.toEPMAuthoringAppType(authoringType);
			epmMaster.setAuthoringApplication(appType);
			
			//save part
			epm = (EPMDocument) PersistenceHelper.manager.save(epm);
			
			//attach files
			CommonContentHelper.service.attach((ContentHolder)epm, primary, secondary);
			
		
			//주 부품 따라 바뀜
			if(ownershipPart != null) {
				//ownershipPart link
				createBuildRule(ownershipPart, epm);
				
				//buildHistory
				EPMBuildRule builder = EpmHelper.manager.getBuildRule(epm);
				createBuildHistory(builder);
				
				//copy IBA
				copyPartToEPMAttribute(ownershipPart, epm);
			} else {
				//결재선 지정
				ApprovalHelper.service.registApproval(epm, approvalList, appState, null);
			}
			
			//relatedPart link
			if(relatedPartList != null) {
				for(Map<String, Object> relatedPartMap : relatedPartList) {
					String oid = (String) relatedPartMap.get("oid");
					
					WTPart part = (WTPart) CommonUtil.getObject(oid);
					
					EPMDescribeLink link = EPMDescribeLink.newEPMDescribeLink(part, epm);
					
					PersistenceServerHelper.manager.insert(link);
				}
			}
			
		} catch (Exception e) {
			throw new Exception(e);
		}		
		return epm;
	}
	
	@Override
	public void epmNonPublishBatch(String cadType,String product){
		try{
			//CADCOMPONENT CADASSEMBLY ,CADDRAWING
			//wt.pdmlink.PDMLinkProduct wt.inf.library.WTLibrary
			LOGGER.info(":::::::::::::: epmNonPublishBatch START :::::::::::::");
			List<EPMDocument> list = EpmHelper.manager.getNonPublishList(cadType, product);
			LOGGER.info(":::::::::::::: epmNonPublishBatch totalcount = "+list.size());
			int i = 1;
			for(EPMDocument epm : list){
				String log = i++ +"."+epm.getNumber();
				LOGGER.info(log);
				createLog(log, "nonPublish");
				
				publish(epm);
			}
			LOGGER.info(":::::::::::::: epmNonPublishBatch totalcount = "+list.size());
			String totlCount = "total Count = " +list.size();
			createLog(totlCount, "nonPublish");
			LOGGER.info(":::::::::::::: epmNonPublishBatch END :::::::::::::");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void publish(EPMDocument epm){
		try{
			
			//기존 변환 파일 삭제
			Representation representation = PublishUtils.getRepresentation(epm);
			if(representation != null){
				PersistenceHelper.manager.delete(representation);
			}
			EpmPublishUtil.publish(epm);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private void createLog(String log,String fileName) {
		//LOGGER.info("========== "+fileName+" ===========");
		//String toDay =DateUtil.getToDay().replace(", newChar)
		String toDate = DateUtil.getToDay();
		toDate = toDate.replace("/", "");
		String filePath = "D:\\e3ps\\NonPublishBatch";
		
		File folder = new File(filePath);
		
		if(!folder.isDirectory()){
			
			folder.mkdirs();
		}
		fileName = fileName.replace(",", "||");
		String toDay = com.e3ps.common.util.DateUtil.getCurrentDateString("date");
		toDay = com.e3ps.common.util.StringUtil.changeString(toDay, "/", "-");
		String logFileName = fileName+"_" + toDay.concat(".log");
		String logFilePath = filePath.concat(File.separator).concat(logFileName);
		File file = new File(logFilePath);
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(file, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		PrintWriter out = new PrintWriter(new BufferedWriter(fw), true);
		out.write(log);
		//LOGGER.info(log);
		out.write("\n");
		out.close();
	}

	@Override
	public EPMDocument getLastEPMDocument(String number) throws WTException {
		EPMDocument epm = null;
		try {
			Class class1 = EPMDocument.class;

			QuerySpec qs = new QuerySpec();
			int i = qs.appendClassList(class1, true);

			// 최신 이터레이션
			qs.appendWhere(
					VersionControlHelper.getSearchCondition(class1, true),
					new int[] { i });

			// 최신 버젼
			SearchUtil.addLastVersionCondition(qs, class1, i);

			qs.appendAnd();
			qs.appendWhere(new SearchCondition(class1, EPMDocument.NUMBER,
					SearchCondition.EQUAL, number), new int[] { i });

			QueryResult qr = PersistenceHelper.manager.find(qs);

			while (qr.hasMoreElements()) {

				Object obj[] = (Object[]) qr.nextElement();
				epm = (EPMDocument) obj[0];

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return epm;
	}

	@Override
	public EPMDocument getEPMDocument(String number, String version) throws WTException {
		if (number == null || version == null) {
			return null;
		}

		QuerySpec qs = new QuerySpec(EPMDocument.class);

		qs.appendWhere(VersionControlHelper.getSearchCondition(
				EPMDocument.class, true), new int[] { 0 });

		qs.appendAnd();
		qs.appendWhere(new SearchCondition(EPMDocument.class,
				EPMDocument.NUMBER, SearchCondition.EQUAL, number),
				new int[] { 0 });

		qs.appendAnd();
		qs.appendWhere(new SearchCondition(EPMDocument.class,
				"versionInfo.identifier.versionId", "=", version),
				new int[] { 0 });

		QueryResult qr = PersistenceHelper.manager.find((StatementSpec) qs);
		if (qr.hasMoreElements()) {
			return (EPMDocument) qr.nextElement();
		} else {
			return null;
		}
	}

	@Override
	public EPMDocument getEPMDocument(String number) throws Exception {
		EPMDocument epm = null;

		try {
			QuerySpec qs = new QuerySpec();
			int i = qs.appendClassList(EPMDocument.class, true);

			// 최신 이터레이션
			qs.appendWhere(VersionControlHelper.getSearchCondition(
					EPMDocument.class, true), new int[] { i });
			// 최신 버젼
			SearchUtil.addLastVersionCondition(qs, EPMDocument.class, i);

			qs.appendAnd();
			qs.appendWhere(new SearchCondition(EPMDocument.class,
					EPMDocument.NUMBER, SearchCondition.EQUAL, number),
					new int[] { i });
			QueryResult qr = PersistenceHelper.manager.find(qs);
			while (qr.hasMoreElements()) {

				Object obj[] = (Object[]) qr.nextElement();
				epm = (EPMDocument) obj[0];

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return epm;
	}

	@Override
	public EPMDocument getEPMDocument(WTPart _part) throws Exception {
		if (_part == null) {
			return null;
		}
		QueryResult qr = null;
		if (VersionControlHelper.isLatestIteration(_part))
			qr = PersistenceHelper.manager.navigate(_part, "buildSource",
					EPMBuildRule.class);
		else
			qr = PersistenceHelper.manager.navigate(_part, "builtBy",
					EPMBuildHistory.class);
		while (qr != null && qr.hasMoreElements())
			return (EPMDocument) qr.nextElement();
		return null;
	}

	@Override
	public EPMDocument getEPM2D(EPMDocumentMaster master) {
		EPMDocument epm2D = null;
		try {
			QuerySpec qs = new QuerySpec();
			int idxA = qs.addClassList(EPMReferenceLink.class, false);
			int idxB = qs.addClassList(EPMDocument.class, true);

			// Join
			qs.appendWhere(new SearchCondition(EPMReferenceLink.class, "roleAObjectRef.key.id",
					EPMDocument.class, "thePersistInfo.theObjectIdentifier.id"), new int[] { idxA, idxB });
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(EPMReferenceLink.class, "roleBObjectRef.key.id",
					SearchCondition.EQUAL, CommonUtil.getOIDLongValue(master)), new int[] { idxA });

			// qs.appendAnd();
			// qs.appendWhere(new SearchCondition(EPMReferenceLink.class,"referenceType",
			// SearchCondition.EQUAL,"DRAWING"),new int[]{idxA}); //DRAWING
			// 최신 이터레이션
			qs.appendAnd();
			qs.appendWhere(VersionControlHelper.getSearchCondition(EPMDocument.class, true),
					new int[] { idxB });

			// 최신 버전
			SearchUtil.addLastVersionCondition(qs, EPMDocument.class, idxB);

			QueryResult rt = PersistenceHelper.manager.find(qs);
			while (rt.hasMoreElements()) {
				Object[] oo = (Object[]) rt.nextElement();
				epm2D = (EPMDocument) oo[0];
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return epm2D;
	}
	
	/**
	 * @desc	: 도면 속성 저장
	 * @author	: shkim
	 * @date	: 2020. 09. 17.
	 * @method	: setAttributes
	 * @param   : epm, reqMap
	 * @return 
	 */
	public void setAttributes(EPMDocument epm, Map<String, Object> reqMap) {
		//IBA
		String customer = StringUtil.checkNull((String) reqMap.get("customer")); // 고객사
		String upg = StringUtil.checkNull((String) reqMap.get("upg")); // UPG
		String extensions = StringUtil.checkNull((String) reqMap.get("extensions")); // 확장자

		IBAUtil.changeIBAValue(epm, "CUSTOMER", customer, "string");
		IBAUtil.changeIBAValue(epm, "UPG", upg, "string");
		IBAUtil.changeIBAValue(epm, "EXTENSIONS", extensions, "string");
	}
	
	/**
	 * @desc	: 도면 폴더 생성
	 * @author	: hgkang
	 * @date	: 2023. 01. 10.
	 * @method	: createEpmFolderAction
	 * @param   : reqMap
	 * @return 
	 */
	@Override
	public void createEpmFolderAction(Map<String, Object> reqMap) throws Exception {
		
		//String projectCode = "TEST0002";
		//String rootLocation = "D:\\drawing\\";
		String rootPath = "/Default/Drawing";
		
		String nasLocation = StringUtil.checkNull((String)reqMap.get("nasLocation"));
		String plmLocation = StringUtil.checkNull((String)reqMap.get("location"));
		
		File projectRoot = new File("D:/CadFolderLocate/"+nasLocation);
		Folder rootFolder = FolderUtil.getFolder(rootPath);
		Folder projectFolder = FolderUtil.getFolder("/Default"+plmLocation);
		
		QueryResult qr = FolderHelper.service.findFolderContents(projectFolder);
		Map<String,FolderEntry> rootData = new HashMap<>();
		while(qr.hasMoreElements()){
			FolderEntry entry = (FolderEntry)qr.nextElement();
			if(!(entry instanceof Folder)){
				rootData.put(entry.getName(), entry);
			}
		}
		
		excuteFiles(projectRoot, rootFolder, rootData);
		
		System.out.println("ok");
	}
	
	private static void excuteFiles(File file, Folder folder, Map<String,FolderEntry> rootData) throws Exception {
		if(file.isFile()) {
			FolderEntry entry = rootData.get(file.getName());
			if(entry!=null){
				FolderHelper.service.changeFolder(entry, folder);
				System.out.println("<br>move File : " + file.getAbsolutePath());
			}
			
		}else {
			String subfolderPath = folder.getFolderPath() + "/" +file.getName();
			Folder subFolder = FolderUtil.getFolder(subfolderPath);
			
			System.out.println("<br>create subfolder : " + subfolderPath);
			
			File[] subFiles = file.listFiles();
	   		for(File subFile: subFiles) {
	   			excuteFiles(subFile, subFolder,rootData);
	    	}
		}
	}
	
	@Override
	public void eventListener(Object obj, String event) {
		System.out.println("EPM EventListener Name ::: " + event);
		if(EPMWorkspaceManagerEvent.POST_WORKSPACE_CHECKIN.equalsIgnoreCase(event)) {
			if(obj instanceof EPMDocument) {
				EPMDocument epm = (EPMDocument)obj;
				String cadName = epm.getCADName();
				String number = cadName.substring(0,cadName.lastIndexOf("."));
				WTPart part = null;
				boolean isMig = false;
				try {
					part = PartHelper.manager.getPart(number);
					//check Migration Data
					if(part!=null){
						String migDataValue = IBAUtil.getAttrValue2(part, "MIGDATA");
						if(migDataValue.length()>0) isMig=true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(!isMig) return;
				
//				EPMDocument epm2D = null;
//				EPMDocument epm3D = null;
//				WTPart part = null;
//				try {
//					if("CADDRAWING".equals(epm.getDocType().toString())) {
//						epm2D = epm;
//						epm3D = EpmHelper.manager.get2DTo3DEPM(epm);
//						part = PartHelper.manager.getWTPart(epm3D);
//					}else {
//						epm3D = epm;
//						epm2D = EpmHelper.manager.getDrawing((EPMDocumentMaster)epm3D.getMaster());
//						part = PartHelper.manager.getWTPart(epm3D);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				
				JSONArray migData = MigExcelData.getInstance().getMigData();
				for(int i=0; i<migData.size(); i++) {
					Map<String, String> rowData = (Map<String, String>)migData.get(i);
					String partNo = rowData.get("final_new_code");
					if(partNo.length() > 0 && partNo.equalsIgnoreCase(number)){
						System.out.println("EPM EventListener Check-in EPM to Part Number ::: " + partNo);
						
						if(part != null) {
							
							//PART IBA SAVE
							IBAUtil.changeIBAValue(part, "OLD_PN", rowData.get("drawing_no"), "string");
							IBAUtil.changeIBAValue(part, "OEM_PART_NO", rowData.get("part_no"), "string");
							IBAUtil.changeIBAValue(part, "TITLE", rowData.get("description"), "string");
							IBAUtil.changeIBAValue(part, "CUSTOMER", rowData.get("customer"), "string");
							IBAUtil.changeIBAValue(part, "MATERIAL", rowData.get("material"), "string");
//							IBAUtil.changeIBAValue(part, "EXTERNAL_DIAMETER", rowData.get("external_diameter"), "string");
//							IBAUtil.changeIBAValue(part, "INTERNAL_DIAMETER", rowData.get("inner_diameter"), "string");
//							IBAUtil.changeIBAValue(part, "THICKNESS", rowData.get("thickness"), "string");
//							IBAUtil.changeIBAValue(part, "HALL_DIA", rowData.get("hall_dia"), "string");
//							IBAUtil.changeIBAValue(part, "HALL_COUNT", rowData.get("hall_cnt"), "string");
							IBAUtil.changeIBAValue(part, "EQUIPMENT", rowData.get("equipment"), "string");
							IBAUtil.changeIBAValue(part, "RESISTIVITY", rowData.get("resistivity"), "string");
//							IBAUtil.changeIBAValue(part, "REMARK", rowData.get("remark"), "string");
//							IBAUtil.changeIBAValue(part, "DESCRIPTION", rowData.get("special_note"), "string");
							IBAUtil.changeIBAValue(part, "DRAW_NO", rowData.get("final_new_code"), "string");
							System.out.println("EPM EventListener Part ("+partNo+") IBA Save ::: ");
							
						}
						
						if(epm!=null) {
							//EPM IBA SAVE
							IBAUtil.changeIBAValue(epm, "OLD_PN", rowData.get("drawing_no"), "string");
							IBAUtil.changeIBAValue(epm, "OEM_PART_NO", rowData.get("part_no"), "string");
							IBAUtil.changeIBAValue(epm, "TITLE", rowData.get("description"), "string");
							IBAUtil.changeIBAValue(epm, "CUSTOMER", rowData.get("customer"), "string");
							IBAUtil.changeIBAValue(epm, "MATERIAL", rowData.get("material"), "string");
							IBAUtil.changeIBAValue(epm, "EQUIPMENT", rowData.get("equipment"), "string");
							IBAUtil.changeIBAValue(epm, "RESISTIVITY", rowData.get("resistivity"), "string");
							IBAUtil.changeIBAValue(epm, "DRAW_NO", rowData.get("final_new_code"), "string");
							System.out.println("EPM EventListener EPM ("+partNo+") IBA Save ::: ");
						}
						
						break;
					}
					
				}
				
			}
		}
		
	}
}
