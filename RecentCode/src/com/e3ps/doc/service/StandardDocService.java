package com.e3ps.doc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.e3ps.admin.service.AdminHelper;
import com.e3ps.approval.service.ApprovalHelper;
import com.e3ps.change.EChangeContents;
import com.e3ps.change.service.ChangeECOHelper;
import com.e3ps.common.bean.FolderData;
import com.e3ps.common.content.service.CommonContentHelper;
import com.e3ps.common.folder.FolderUtil;
import com.e3ps.common.iba.IBAUtil;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.query.SearchUtil;
import com.e3ps.common.service.CommonHelper;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.ObjectUtil;
import com.e3ps.common.util.SequenceDao;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.doc.E3PSDocument;
import com.e3ps.doc.ETCDocumentContents;
import com.e3ps.doc.PDRDocumentContents;
import com.e3ps.doc.util.DocTypePropList;
import com.e3ps.org.People;

import wt.clients.folder.FolderTaskLogic;
import wt.content.ApplicationData;
import wt.content.ContentHolder;
import wt.content.ContentRoleType;
import wt.doc.WTDocument;
import wt.enterprise.RevisionControlled;
import wt.fc.IdentityHelper;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceServerHelper;
import wt.fc.QueryResult;
import wt.fc.collections.WTArrayList;
import wt.fc.collections.WTCollection;
import wt.fc.collections.WTHashSet;
import wt.fc.collections.WTSet;
import wt.folder.Folder;
import wt.folder.FolderEntry;
import wt.folder.FolderHelper;
import wt.folder.SubFolder;
import wt.folder.SubFolderIdentity;
import wt.fv.uploadtocache.CachedContentDescriptor;
import wt.inf.container.WTContainerRef;
import wt.lifecycle.LifeCycleHelper;
import wt.lifecycle.LifeCycleManaged;
import wt.lifecycle.State;
import wt.part.WTPart;
import wt.part.WTPartDescribeLink;
import wt.pom.Transaction;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.StandardManager;
import wt.util.WTException;
import wt.vc.VersionControlHelper;
import java.util.stream.Stream;

@SuppressWarnings("serial")
public class StandardDocService extends StandardManager implements DocService {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.DOC.getName());
	
	public static final String ROOTLOCATION = "/Default/Document";

	public static StandardDocService newStandardDocService() throws WTException {
		final StandardDocService instance = new StandardDocService();
		instance.initialize();
		return instance;
	}
	
	@Override
	public E3PSDocument createDocAction(Map<String, Object> reqMap) throws Exception{
		E3PSDocument newDoc = null;
		Transaction trx = null;
		
		try {
			trx = new Transaction();
			trx.start();
			String location = StringUtil.checkNull((String) reqMap.get("location"));
			String docName = StringUtil.checkNull((String) reqMap.get("docName"));
			String primary = StringUtil.checkNull((String) reqMap.get("PRIMARY"));
			String docAttribute = StringUtil.checkNull((String)reqMap.get("docAttribute"));
			String docCodeType = StringUtil.checkNull((String)reqMap.get("docCodeType"));
			String docNumber = DocHelper.manager.makeDocNumber(docCodeType, docAttribute);
			String extractedValue = docNumber.split("-")[0];
			
			List<String> secondary = StringUtil.checkReplaceArray(reqMap.get("SECONDARY"));
			String appState = StringUtil.checkNull((String) reqMap.get("appState"));
			List<Map<String, Object>> approvalList = (List<Map<String, Object>>) reqMap.get("approvalList");
			List<Map<String, Object>> relatedPartList = (List<Map<String, Object>>) reqMap.get("relatedPartList");
			List<Map<String,Object>> pdrContentsList = (List<Map<String,Object>>)reqMap.get("pdrContentsList");
			if(pdrContentsList==null)pdrContentsList = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> etcContentsList = (List<Map<String,Object>>)reqMap.get("etcContentsList");
			if(etcContentsList==null)etcContentsList = new ArrayList<Map<String,Object>>();
			
			
			// 문서 채번
//			String docNumber = DocHelper.manager.getDocCode(docCodeName);
//			docNumber += "-" +DateUtil.getToDay("YYMM") + "-";
//			String serial = SequenceDao.manager.getSeqNo(docNumber, "0000", "E3PSDocumentMaster", "WTDocumentNumber");
//			docNumber += serial;
			
			newDoc = E3PSDocument.newE3PSDocument();
			//set properties 
			newDoc.setNumber(docNumber);
			newDoc.setName(docName);
			newDoc.setDocAttribute(docAttribute);
			reqMap.put("location", location);
			reqMap.put("lifecycle", "LC_Default");
			
			//folder location
			WTContainerRef wtContainerRef = WCUtil.getWTContainerRef();
			Folder folder= FolderTaskLogic.getFolder(location, wtContainerRef);
			FolderHelper.assignLocation((FolderEntry) newDoc, folder);
			newDoc = (E3PSDocument)CommonHelper.service.setVersiondDefault(newDoc, reqMap);
			//save document
			newDoc = (E3PSDocument) PersistenceHelper.manager.save(newDoc);
			
			
			WTCollection wtc = new WTArrayList();
			//공정 개발 검토서 Contents List
			for(int i = 0 ; i < pdrContentsList.size(); i ++) {
				PDRDocumentContents pdr = PDRDocumentContents.newPDRDocumentContents();
				pdr.setName(StringUtil.checkNull((String)pdrContentsList.get(i).get("name")));
				pdr.setContents(StringUtil.checkNull((String)pdrContentsList.get(i).get("contents")));
				pdr.setSort(i);
				pdr.setPdr(newDoc);
				wtc.add(pdr);			
			}
			//기타 Contents List
			for(int i = 0 ; i < etcContentsList.size(); i ++) {
				ETCDocumentContents docEtc = ETCDocumentContents.newETCDocumentContents();
				docEtc.setName(StringUtil.checkNull((String)etcContentsList.get(i).get("name")));
				docEtc.setContents(StringUtil.checkNull((String)etcContentsList.get(i).get("contents")));
				docEtc.setSort(i);
				docEtc.setDocEtc(newDoc);
				wtc.add(docEtc);			
			}
			PersistenceHelper.manager.save(wtc);
			
			
			//attach files
			CommonContentHelper.service.attach((ContentHolder)newDoc, primary, secondary);
			
			//관련 부품 연결
			addDocToPartLink(newDoc, relatedPartList, false);
			
			ApprovalHelper.service.registApproval(newDoc, approvalList, appState, null);
			
			Optional op = Stream.of(DocTypePropList.values()).filter(h -> h.getDocTypeCode().equals(extractedValue)).findFirst();
			
			if(op.isPresent()) {
				DocTypePropList obj = (DocTypePropList) op.get();
				
				Map<String,String> propMap = obj.getProps();
				
				
				for ( String jspName : propMap.keySet() ) {
					String val = "";
					if(reqMap.get(jspName) instanceof String) {
						val = StringUtil.checkNull((String)reqMap.get(jspName));
					}else if(reqMap.get(jspName) instanceof ArrayList) {
						List<String> arr = (List<String>) reqMap.get(jspName);
//						val1 = String.join(", ", arr);
						for (String v:arr) {
							if(v != "") {
								if(val.length() == 0) {
									val = v;
								}else {
									val += ", " + v;
								}
							}
						}
					}
					
					IBAUtil.changeIBAValue(newDoc, propMap.get(jspName), val, "string");
			          System.out.println("방법1) key : " + jspName +" / value : " + propMap.get(jspName));
			          System.out.println("val ::: " + val);
			      }
			}
			
			
			//권한
//			RevisionControlled per = (RevisionControlled) newDoc;
//			AdminHelper.service.setAuthToObject(per, null);
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw e;
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		
		return newDoc;
	}
	
	@Override
	public E3PSDocument modifyDocAction(Map<String, Object> reqMap) throws Exception{
		E3PSDocument doc = null;
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			if(oid.length() > 0) {
				doc = (E3PSDocument) CommonUtil.getObject(oid);
				
				String location = StringUtil.checkNull((String) reqMap.get("location"));
				String docName = StringUtil.checkNull((String) reqMap.get("docName"));
//				String description = StringUtil.checkNull((String) reqMap.get("description"));
				String primary = StringUtil.checkNull((String) reqMap.get("PRIMARY"));
				List<String> secondary = StringUtil.checkReplaceArray(reqMap.get("SECONDARY"));
				List<String> delocIds = StringUtil.checkReplaceArray(reqMap.get("delocIds"));
				String docNumber = StringUtil.checkNull((String) reqMap.get("docNumber"));
				String extractedValue = docNumber.split("-")[0];
				String iterationNote = StringUtil.checkNull((String)reqMap.get("iterationNote"));
				
				List<Map<String, Object>> relatedPartList = (List<Map<String, Object>>) reqMap.get("relatedPartList");
				List<Map<String,Object>> pdrContentsList = (List<Map<String,Object>>)reqMap.get("pdrContentsList");
				List<Map<String,Object>> etcContentsList = (List<Map<String,Object>>)reqMap.get("etcContentsList");
				
				//check out - working copy
				doc = (E3PSDocument) ObjectUtil.checkout(doc);
				
				//set properties
				
				//modify Document
				doc = (E3PSDocument) PersistenceHelper.manager.modify(doc);
				
				//checkin
				doc = (E3PSDocument) ObjectUtil.checkin(doc, iterationNote);
				
				//공정 개발 검토서 Contents List
				WTCollection wtc = new WTArrayList();
				for(int i = 0 ; i < pdrContentsList.size(); i ++) {
					PDRDocumentContents pdr = PDRDocumentContents.newPDRDocumentContents();
					pdr.setName(StringUtil.checkNull((String)pdrContentsList.get(i).get("name")));
					pdr.setContents(StringUtil.checkNull((String)pdrContentsList.get(i).get("contents")));
					pdr.setSort(i);
					pdr.setPdr(doc);
					wtc.add(pdr);			
				}
				//기타 Contents List
				for(int i = 0 ; i < etcContentsList.size(); i ++) {
					ETCDocumentContents docEtc = ETCDocumentContents.newETCDocumentContents();
					docEtc.setName(StringUtil.checkNull((String)etcContentsList.get(i).get("name")));
					docEtc.setContents(StringUtil.checkNull((String)etcContentsList.get(i).get("contents")));
					docEtc.setSort(i);
					docEtc.setDocEtc(doc);
					wtc.add(docEtc);			
				}
				PersistenceHelper.manager.save(wtc);
				
				
				//doc name change
				if (!doc.getName().equals(docName)) {
					CommonHelper.service.changeRevisionName(doc, docName);
				}
				
				//관련 부품 연결
				addDocToPartLink(doc, relatedPartList, true);
				
				//attach files
				String cacheId = primary.split("/")[0];
				if(cacheId.length() > 0) {
					CommonContentHelper.service.attach((ContentHolder)doc, primary, secondary, delocIds);
				}
				
				//주첨부파일 드래그 변경으로 인해 첨부파일 관련 함수 추가
				if(secondary.size() > 0) {
					CommonContentHelper.service.delete(doc, ContentRoleType.SECONDARY);
					for (int i = 0; i < secondary.size(); i++) {
						String secondCacheId = secondary.get(i).split("/")[0];
						String fileName = secondary.get(i).split("/")[1];
						CachedContentDescriptor cacheDs = new CachedContentDescriptor(secondCacheId);
						CommonContentHelper.service.attach(doc, cacheDs, fileName, null, ContentRoleType.SECONDARY);
				    }
					
					if(delocIds.size() > 0) {
						for (int i = 0; i < delocIds.size(); i++) {
							ApplicationData appData = (ApplicationData) CommonUtil.getObject(delocIds.get(i));
							if(ContentRoleType.SECONDARY.equals(appData.getRole())) {
								
								CommonContentHelper.service.attach(doc, appData, false);
								
							}
						}
					}
				}else {
					if(delocIds.size() > 0) {
						CommonContentHelper.service.delete(doc, ContentRoleType.SECONDARY);
						for (int i = 0; i < delocIds.size(); i++) {
							ApplicationData appData = (ApplicationData) CommonUtil.getObject(delocIds.get(i));
							if(ContentRoleType.SECONDARY.equals(appData.getRole())) {
								CommonContentHelper.service.attach(doc, appData, false);
							}
						}
					}
				}
				
				//Data 저장
				Optional op = Stream.of(DocTypePropList.values()).filter(h -> h.getDocTypeCode().equals(extractedValue)).findFirst();
				
				if(op.isPresent()) {
					DocTypePropList obj = (DocTypePropList) op.get();
					
					Map<String,String> propMap = obj.getProps();
					
					for ( String jspName : propMap.keySet() ) {
						String val = "";
						if(reqMap.get(jspName) instanceof String) {
							val = StringUtil.checkNull((String)reqMap.get(jspName));
						}else if(reqMap.get(jspName) instanceof ArrayList) {
							List<String> arr = (List<String>) reqMap.get(jspName);
//							val1 = String.join(", ", arr);
							for (String v:arr) {
								if(v != "") {
									if(val.length() == 0) {
										val = v;
									}else {
										val += ", " + v;
									}
								}
							}
						}
						
						IBAUtil.changeIBAValue(doc, propMap.get(jspName), val, "string");
				          System.out.println("방법1) key : " + jspName +" / value : " + propMap.get(jspName));
				          System.out.println("val ::: " + val);
				      }
				}
				
//				DocTypePropList obj = Stream.of(DocTypePropList.values()).filter(h -> h.getDocTypeCode().equals(extractedValue)).findFirst().orElseThrow(() -> new Exception("문서 유형 코드가 존재하지 않음"));
//				
//				Map<String,String> propMap = obj.getProps();
//				for ( String jspName : propMap.keySet() ) {
//					String val = "";
//					if(reqMap.get(jspName) instanceof String) {
//						val = StringUtil.checkNull((String)reqMap.get(jspName));
//					}else if(reqMap.get(jspName) instanceof ArrayList) {
//						List<String> arr = (List<String>) reqMap.get(jspName);
////						val1 = String.join(", ", arr);
//						for (String v:arr) {
//							if(v != "") {
//								if(val.length() == 0) {
//									val = v;
//								}else {
//									val += ", " + v;
//								}
//							}
//						}
//					}
//					
//					IBAUtil.changeIBAValue(doc, propMap.get(jspName), val, "string");
//			          //System.out.println("방법1) key : " + jspName +" / value : " + propMap.get(jspName));
//			          //System.out.println("val ::: " + val);
//			      }
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
		
		return doc;
	}
	
	@Override
	public void deleteDocAction(Map<String, Object> reqMap) throws Exception{
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			if(oid.length() > 0) {
				E3PSDocument doc = (E3PSDocument) CommonUtil.getObject(oid);
				deleteDocToPartLink(doc);
				PersistenceHelper.manager.delete(doc);
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
	
	/**  E3PSDocument -- WTPart 링크 수정(등록, 삭제)
	* @param document
	* @param partOids
	* @param isDelete
	* @throws Exception
	*/
	public void addDocToPartLink(E3PSDocument doc, List<Map<String, Object>> list, boolean isDelete) throws Exception {
    	if(isDelete) {
    		deleteDocToPartLink(doc);
    	}
    	
    	WTCollection wc = new WTArrayList();
    	
    	for(Map<String, Object> map : list) {
    		String oid = (String) map.get("oid");
    		
    		WTPart part = (WTPart) CommonUtil.getObject(oid);
    		
    		WTPartDescribeLink link = WTPartDescribeLink.newWTPartDescribeLink(part, doc);
    		wc.add(link);
    	}
    	
    	if(wc.size() > 0) {
    		PersistenceServerHelper.manager.insert(wc);
    	}
    }
	
	/**  E3PSDocument -- WTPart 링크 삭제
     * @param document
     * @throws Exception
     */
    public void deleteDocToPartLink(E3PSDocument doc) throws Exception {
    	QueryResult results = PersistenceHelper.manager.navigate(doc, "describes", WTPartDescribeLink.class, false);
    	
    	WTSet ws = new WTHashSet(results);
    	
        PersistenceServerHelper.manager.remove(ws);
    }
    
	/**
	 * @desc	: 문서 폐기
	 * @author	: mnyu
	 * @date	: 2019. 10. 21.
	 * @method	: withdrawnDocAction
	 * @param	: reqMap
	 * @return	: Map<String,Object>
	 */
	@Override
	public void withdrawnDocAction(Map<String, Object> reqMap) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();

			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String appState = StringUtil.checkNull((String) reqMap.get("appState"));
			
			if(oid.length() > 0){
				E3PSDocument doc = (E3PSDocument) CommonUtil.getObject(oid);
				LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged) doc, State.toState(appState), false);
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
	 * @desc	: 문서 개정
	 * @author	: mnyu
	 * @date	: 2019. 10. 29.
	 * @method	: reviseDocAction
	 * @param	: reqMap
	 * @return	: Map<String,Object>
	 */
	@Override
	public E3PSDocument reviseDocAction(Map<String, Object> reqMap) throws Exception {
		E3PSDocument newDoc = null;
		
		Transaction trx = new Transaction();
		try {
			trx.start();

			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String appState = StringUtil.checkNull((String) reqMap.get("appState"));

			E3PSDocument oldDoc = (E3PSDocument)CommonUtil.getObject(oid);

			// 개정
			newDoc = (E3PSDocument) ObjectUtil.revise(oldDoc);
			newDoc = (E3PSDocument)CommonHelper.service.setVersiondDefault(newDoc, reqMap);
	        
			// 관련부품 연결
			QueryResult resultPart = PersistenceHelper.manager.navigate(oldDoc, "describes", WTPartDescribeLink.class);
			while(resultPart.hasMoreElements()){
				WTPart part = (WTPart) resultPart.nextElement();
				WTPartDescribeLink link = WTPartDescribeLink.newWTPartDescribeLink(part, newDoc);
				PersistenceServerHelper.manager.insert(link);
			}
			
			//결재선 임시저장
			ApprovalHelper.service.registApproval(newDoc, null, appState, null);
			
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return newDoc;
	}

	@Override
	public WTDocument getLastDocument(String number) {
		WTDocument doc = null;

		try {
			QuerySpec query = new QuerySpec();
			int idx = query.addClassList(WTDocument.class, true);

			// 최신 이터레이션
			query.appendWhere(VersionControlHelper.getSearchCondition(WTDocument.class, true),
					new int[] { idx });
			// 최신 버전
			SearchUtil.addLastVersionCondition(query, WTDocument.class, idx);
			query.appendAnd();
			query.appendWhere(new SearchCondition(WTDocument.class, "master>number", SearchCondition.EQUAL,
					number, false), new int[] { idx });

			QueryResult rt = PersistenceHelper.manager.find(query);
			while (rt.hasMoreElements()) {
				Object[] obj = (Object[]) rt.nextElement();
				doc = (WTDocument) obj[0];
				return doc;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	@Override
	public E3PSDocument createDocAction2(Map<String, Object> reqMap) throws Exception {
		E3PSDocument doc = null;
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String location = StringUtil.checkNull((String) reqMap.get("location"));
			//String docNumber = StringUtil.checkNull((String) reqMap.get("docNumber"));E3PSDocument
			String docName = StringUtil.checkNull((String) reqMap.get("docName"));
			String description = StringUtil.checkNull((String) reqMap.get("description"));
			String primary = StringUtil.checkNull((String) reqMap.get("PRIMARY"));
			String appState = StringUtil.checkNull((String) reqMap.get("appState"));
			
			String maker = StringUtil.checkNull((String) reqMap.get("maker"));
			String model = StringUtil.checkNull((String) reqMap.get("model"));
			String spec = StringUtil.checkNull((String) reqMap.get("spec"));
			String activeOid = StringUtil.checkNull((String) reqMap.get("activeOid"));
			String activeLinkOid = StringUtil.checkNull((String) reqMap.get("activeLinkOid"));
			String activeLinkType = StringUtil.checkNull((String) reqMap.get("activeLinkType"));
			
			String docCodeName = StringUtil.checkNull((String) reqMap.get("docCode"));
			
			List<String> secondary = StringUtil.checkReplaceArray(reqMap.get("SECONDARY"));
			
			List<Map<String, Object>> approvalList = (List<Map<String, Object>>) reqMap.get("approvalList");
			List<Map<String, Object>> relatedPartList = (List<Map<String, Object>>) reqMap.get("relatedPartList");
			
			//create new document
			doc = E3PSDocument.newE3PSDocument();
			
			//set container
			//PDMLinkProduct pdmProduct = WCUtil.getPDMLinkProduct();
			
			//doc.setContainer(pdmProduct);
			
			// 문서 채번
			String docNumber = DocHelper.manager.getDocCode(docCodeName);
			docNumber += "-" +DateUtil.getToDay("YYMM") + "-";
			String serial = SequenceDao.manager.getSeqNo(docNumber, "0000", "E3PSDocumentMaster", "WTDocumentNumber");
			docNumber += serial;
			
			//set properties 
			doc.setNumber(docNumber);
			doc.setName(docName);
			doc.setDescription(description);
			
			doc.setSpec(spec);
			doc.setMaker(maker);
			doc.setModel(model);
			
			//folder location
			//WTContainerRef wtContainerRef = WCUtil.getWTContainerRef();
			
			//Folder folder= FolderTaskLogic.getFolder(location, wtContainerRef);
			
			//FolderHelper.assignLocation((FolderEntry) doc, folder);
			doc = (E3PSDocument)CommonHelper.service.setVersiondDefault(doc, reqMap);
			//save document
			
			doc = (E3PSDocument) PersistenceHelper.manager.save(doc);
			
			//attach files
			CommonContentHelper.service.attach((ContentHolder)doc, primary, secondary);
			
			//관련 부품 연결
			addDocToPartLink(doc, relatedPartList, false);
			
			ApprovalHelper.service.registApproval(doc, approvalList, appState, null);
			
			//권한
			RevisionControlled per = (RevisionControlled) doc;
			AdminHelper.service.setAuthToObject(per, null);
			
			ChangeECOHelper.service.connectEca(doc,activeOid, activeLinkOid, activeLinkType);
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		
		return doc;
	}
	
	/**
	 * @desc	: 문서 Folder 등록 Action
	 * @author	: hgkang
	 * @date	: 2023. 2. 14.
	 * @method	: createDocFolderAction
	 * @param   : reqMap
	 * @return  : boolean
	 * @throws  : Exception
	 */
	@Override
	public boolean createDocFolderAction(Map<String, Object> reqMap) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String path = "/Default/Document";
			
			String name = StringUtil.checkNull((String) reqMap.get("name")); //폴더 명
			String parentOid = StringUtil.checkNull((String) reqMap.get("parentOid")); //상위폴더 OID
			
			if(parentOid.length() > 0) { 
				SubFolder parent = (SubFolder)CommonUtil.getObject(parentOid);
				path = parent.getFolderPath() + "/" + name;
			}else {
				path = path += "/" + name;
			}
			  
			FolderUtil.createFolder(path);
			
			trx.commit();
			trx = null;
			
			return true;
			
		}catch(Exception e) {
			throw e;
			
		}finally {
			
			if(trx != null){
				trx.rollback();
			}
		}
	}

	/**
	 * @desc	: 문서 Folder 수정 Action
	 * @author	: hgkang
	 * @date	: 2023. 2. 14.
	 * @method	: modifyDocFolderAction
	 * @param   : reqMap
	 * @return  : boolean
	 * @throws  : Exception
	 */
	@Override
	public boolean modifyDocFolderAction(Map<String, Object> reqMap) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String name = StringUtil.checkNull((String) reqMap.get("name"));
			
			SubFolder folder = (SubFolder) CommonUtil.getObject(oid);
			
			SubFolderIdentity asd = (SubFolderIdentity)folder.getIdentificationObject();
			asd.setName(name);
			
			IdentityHelper.service.changeIdentity(folder, asd);
			
			trx.commit();
			trx = null;
			
			return true;
			
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			if(trx != null) {
				trx.rollback();
			}
		}
	}
}
