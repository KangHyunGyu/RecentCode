package com.e3ps.epm.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.e3ps.change.EChangeActivity;
import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.EChangeRequest2;
import com.e3ps.change.EcoTargetResultLink;
import com.e3ps.change.beans.ChangeECOSearch;
import com.e3ps.change.beans.ChangeECRSearch;
import com.e3ps.change.beans.ECOData;
import com.e3ps.change.service.ChangeHelper;
import com.e3ps.common.bean.RevisionData;
import com.e3ps.common.iba.IBAUtil;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.FolderUtil;
import com.e3ps.common.util.SearchUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.common.web.PageQueryBroker;
import com.e3ps.doc.E3PSDocument;
import com.e3ps.epm.EpmLocation;
import com.e3ps.epm.bean.EpmData;
import com.e3ps.epm.bean.EpmPartStateData;
import com.e3ps.epm.bean.StructureData;
import com.e3ps.epm.util.EpmPropList;
import com.e3ps.epm.util.EpmUtil;
import com.e3ps.org.People;
import com.e3ps.part.comparator.bean.CADBomData;
import com.e3ps.part.service.PartHelper;
import com.e3ps.part.util.PartUtil;
import com.e3ps.project.EProject;
import com.e3ps.project.EProjectMasteredLink;
import com.e3ps.project.beans.ProjectData;
import com.ptc.wvs.server.util.PublishUtils;

import wt.clients.folder.FolderTaskLogic;
import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentItem;
import wt.enterprise.BasicTemplateProcessor;
import wt.enterprise.RevisionControlled;
import wt.epm.EPMDocument;
import wt.epm.EPMDocumentMaster;
import wt.epm.build.EPMBuildHistory;
import wt.epm.build.EPMBuildRule;
import wt.epm.structure.EPMDescribeLink;
import wt.epm.structure.EPMMemberLink;
import wt.epm.structure.EPMReferenceLink;
import wt.epm.structure.EPMStructureHelper;
import wt.fc.PagingQueryResult;
import wt.fc.PagingSessionHelper;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.folder.Folder;
import wt.folder.IteratedFolderMemberLink;
import wt.inf.container.WTContainer;
import wt.inf.container.WTContainerRef;
import wt.method.MethodContext;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.pds.StatementSpec;
import wt.pom.DBProperties;
import wt.pom.WTConnection;
import wt.query.ArrayExpression;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.representation.Representation;
import wt.services.ServiceFactory;
import wt.session.SessionHelper;
import wt.util.WTAttributeNameIfc;
import wt.util.WTException;
import wt.vc.VersionControlHelper;
import wt.vc.wip.WorkInProgressHelper;
import wt.vc.wip.Workable;

public class EpmHelper {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.EPM.getName());
	
	public static final EpmService service = ServiceFactory.getService(EpmService.class);
	public static final EpmHelper manager = new EpmHelper();
	public static final String ROOTLOCATION = "/Default";
	
	public List<EpmData> getEpmList(Map<String, Object> reqMap) throws Exception {
		
		List<EpmData> list = new ArrayList<>();
		
		QuerySpec query = getEpmListQuery(reqMap);
		
		QueryResult result = PersistenceHelper.manager.find((StatementSpec)query);
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			EPMDocument epm = (EPMDocument) obj[0];
			
			EpmData data = new EpmData(epm);
			
			list.add(data);
		}
		
		return list;
	}
	
	public Map<String, Object> getEpmScrollList(Map<String, Object> reqMap) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
		
		List<EpmData> list = new ArrayList<>();
		
		int page = (Integer) reqMap.get("page");
		int rows = (Integer) reqMap.get("rows");
		String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));
    	
		String moduleType = StringUtil.checkNull((String) reqMap.get("moduleType"));
		
		PagingQueryResult result = null;
		
		if (sessionId.length() > 0) {
			result = PagingSessionHelper.fetchPagingSession((page - 1) * rows, rows, Long.valueOf(sessionId));
		} else {
			QuerySpec query = getEpmListQuery(reqMap);
			
			result = PageQueryBroker.openPagingSession((page - 1) * rows, rows, query, true);
		}
		
		int totalSize = result.getTotalSize();
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			EPMDocument epm = (EPMDocument) obj[0];
			
			EpmData data = new EpmData(epm);
			
			if("multiApproval".equals(moduleType)) {
				if(!"INWORK".equals(data.getState())) {	//작업중아닌 도면은 결재 추가 불가능
					data.setSelect(false);
				} else {
					/*if(EpmUtil.isWGM(epm)) {	//사용안함
						data.setSelect(false);
					} else {
						WTPart ownerPart = PartHelper.manager.getWTPart(epm);
						if(ownerPart != null) {
							data.setSelect(false);
						}
					}*/
					/*WTPart ownerPart = PartHelper.manager.getWTPart(epm);	//추후 복구
					if(ownerPart != null) {
						data.setSelect(false);
					}*/
					if("c/o".equals(data.getCheckoutState()) || "wrk".equals(data.getCheckoutState())) {
						data.setSelect(false);
					}
				}
			}else if("distribute".equals(moduleType)){
				if(!data.isDis()) {
					data.setSelect(false);
				}
			}
			list.add(data);
		}
		
		map.put("list", list);
		map.put("totalSize", totalSize);
		map.put("sessionId", result.getSessionId() == 0 ? "" : result.getSessionId());
		
		return map;
	}

	public QuerySpec getEpmListQuery(Map<String, Object> reqMap) throws Exception {
		
		//소팅
		Map<String, Object> sort = reqMap.get("sort") != null ? (Map<String, Object>) reqMap.get("sort") : new HashMap<>();
		
		//기본속성
		String location = StringUtil.checkReplaceStr((String) reqMap.get("location"), EpmPropList.EPM_CREATION_DATA.getLocations().get("Default")); // 도면 분류
		String version = StringUtil.checkNull((String) reqMap.get("version")); // 버전
		String number = StringUtil.checkNull((String) reqMap.get("number")); // 도면 번호
		String name = StringUtil.checkNull((String) reqMap.get("name")); // 도면 명
		name = name.replace("[", "[[]");
		String predate = StringUtil.checkNull((String) reqMap.get("predate")); // 작성일 pre
		String postdate = StringUtil.checkNull((String) reqMap.get("postdate")); // 작성일 post
		String predate_modify = StringUtil.checkNull((String) reqMap.get("predate_modify")); // 최종수정일 pre
		String postdate_modify = StringUtil.checkNull((String) reqMap.get("postdate_modify")); // 최종수정일 post
		String cadName = StringUtil.checkNull((String) reqMap.get("cadName")); // CAD Name
		//String searchChildFolder = StringUtil.checkReplaceStr((String)reqMap.get("searchChildFolder"), "true"); //하위 폴더 포함
		String likeSearchNumber = StringUtil.checkReplaceStr((String)reqMap.get("likeSearchNumber"), "false"); //도면 번호 like 검색
		String likeSearchName = StringUtil.checkReplaceStr((String)reqMap.get("likeSearchName"), "false"); //도면 명 like 검색
		System.out.println("$$$$ location:"+location);
		System.out.println("$$$$ likeSearchNumber:"+likeSearchNumber);
		List<String> cadDivision = StringUtil.checkReplaceArray(reqMap.get("cadDivision")); // CAD 구분
		List<String> cadType = StringUtil.checkReplaceArray(reqMap.get("cadType")); // CAD 타입
		List<String> creator = StringUtil.checkReplaceArray(reqMap.get("creator")); // 작성자
		List<String> modifier = StringUtil.checkReplaceArray(reqMap.get("modifier")); // 최종수정자
		List<String> state = StringUtil.checkReplaceArray(reqMap.get("state")); // 상태
		List<String> relatedProject = StringUtil.checkReplaceArray(reqMap.get("relatedProject")); // 관련 프로젝트
		
		//IBA
		String customer = StringUtil.checkNull((String) reqMap.get("customer")); // 고객사
		String upg = StringUtil.checkNull((String) reqMap.get("upg")); // UPG
		String extensions = StringUtil.checkNull((String) reqMap.get("extensions")); // 확장자
		String designed = StringUtil.checkNull((String) reqMap.get("designed")); // 설계자
		String customer_part_number = StringUtil.checkNull((String) reqMap.get("customer_part_number")); // 고객 품번
		String eo_number = StringUtil.checkNull((String) reqMap.get("eo_number")); // EO 번호
		String material = StringUtil.checkNull((String) reqMap.get("material")); // 재질
		String weight = StringUtil.checkNull((String) reqMap.get("weight")); // 중량
		String thickness = StringUtil.checkNull((String) reqMap.get("thickness")); // 두께
		String finish = StringUtil.checkNull((String) reqMap.get("finish")); // FINISH
		
		QuerySpec query = new QuerySpec();
		int idx = query.addClassList(EPMDocument.class, true);
		
		SearchCondition sc = null;
		
		//최신 이터레이션
		if(query.getConditionCount() > 0) {
			query.appendAnd(); 
		}
    	query.appendWhere(VersionControlHelper.getSearchCondition(EPMDocument.class, true), new int[]{idx});
    	

    	//컨테이너 
    	WTContainer product = WCUtil.getPDMLinkProduct();
    	
		//폴더분류
		Folder folder = FolderTaskLogic.getFolder(location, WTContainerRef.newWTContainerRef(product));
    	if (folder != null) {
    		if(query.getConditionCount() > 0) {
    			query.appendAnd(); 
    		}
			int f_idx = query.appendClassList(IteratedFolderMemberLink.class, false);
			ClassAttribute fca = new ClassAttribute(IteratedFolderMemberLink.class, "roleBObjectRef.key.branchId");
			SearchCondition fsc = new SearchCondition(fca, "=",
					new ClassAttribute(EPMDocument.class, "iterationInfo.branchId"));
			fsc.setFromIndicies(new int[] { f_idx, idx }, 0);
			fsc.setOuterJoin(0);
			query.appendWhere(fsc, new int[] { f_idx, idx });
			query.appendAnd();

			//query.appendOpenParen();
			List<Long> folderOidLongValueList = new ArrayList<>();
			
			long fid = folder.getPersistInfo().getObjectIdentifier().getId();

			folderOidLongValueList.add(fid);
			
			ArrayList<Folder> subFolderList = new ArrayList<>(); 
			FolderUtil.getSubFolderList(folder, subFolderList);
			
			if(subFolderList.size()>0) {
				for (Folder sub: subFolderList) {
					folderOidLongValueList.add(CommonUtil.getOIDLongValue(sub));
				}
			}
			
			sc = new SearchCondition(new ClassAttribute(IteratedFolderMemberLink.class, "roleAObjectRef.key.id"), SearchCondition.IN, new ArrayExpression(folderOidLongValueList.toArray()));
			query.appendWhere(sc, new int[] { f_idx });
		}
		
    	//컨테이너
    	if(query.getConditionCount() > 0) {
			query.appendAnd();
		}
		sc = new SearchCondition(EPMDocument.class, EPMDocument.CONTAINER_REFERENCE+".key.id", SearchCondition.EQUAL ,CommonUtil.getOIDLongValue(product));
		query.appendWhere(sc, new int[] { idx });
		
		//특정 객체에 대한 권한
		//query = AdminHelper.manager.getAuthQuerySpec(query, "EPMDocument", idx);
    	
		//버전
		if(version.length() > 0) {
			if("new".equals(version)) {
				//SearchUtil 내용 정리 필요
				SearchUtil.addLastVersionCondition(query, EPMDocument.class, idx);
			}
		} else {
			SearchUtil.addLastVersionCondition(query, EPMDocument.class, idx);
		}
		
//		if (location.length() > 0) {
//			int l = location.indexOf(ROOTLOCATION);
//			if (l >= 0) {
//				if (query.getConditionCount() > 0) {
//					query.appendAnd();
//				}
//				location = location.substring((l + ROOTLOCATION.length()));
//				// Folder Search
//				int folder_idx = query.addClassList(EpmLocation.class, false);
//				query.appendWhere(new SearchCondition(EpmLocation.class, EpmLocation.EPM, EPMDocument.class, "thePersistInfo.theObjectIdentifier.id"), new int[] { folder_idx, idx });
//				query.appendAnd();
//				query.appendOpenParen();
//				query.appendWhere(
//						new SearchCondition(EpmLocation.class, "loc", SearchCondition.EQUAL, location ),
//						new int[] { folder_idx });
//				query.appendOr();
//				query.appendWhere(
//						new SearchCondition(EpmLocation.class, "loc", SearchCondition.LIKE, location + "/%"),
//						new int[] { folder_idx });
//				query.appendOr();
//				query.appendWhere(new SearchCondition(EpmLocation.class, "loc", SearchCondition.EQUAL, "/Checked Out"), new int[] { folder_idx });
//				query.appendCloseParen();
//			}
//			
//			long aa =0;
//			//작업 공간 제외
//			if (query.getConditionCount() > 0) {
//				query.appendAnd();
//			}
//			query.appendWhere(new SearchCondition(EPMDocument.class, "ownership.owner.key.id", SearchCondition.EQUAL, aa), new int[] { idx });
//		}else {
//			long aa =0;
//			//작업 공간 제외
//			if (query.getConditionCount() > 0) {
//				query.appendAnd();
//			}
//			query.appendWhere(new SearchCondition(EPMDocument.class, "ownership.owner.key.id", SearchCondition.EQUAL, aa), new int[] { idx });
//		}
		
		//부품번호
		if(number.length() > 0) {
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			
			if("true".equals(likeSearchNumber)) {
				sc = new SearchCondition(EPMDocument.class, EPMDocument.NUMBER, SearchCondition.LIKE, "%" + number + "%", false);
			}else {
				sc = new SearchCondition(EPMDocument.class, EPMDocument.NUMBER, SearchCondition.EQUAL, number, false);
			}
			query.appendWhere(sc, new int[] { idx });
		}
		
		//부품명
		if(name.length() > 0) {
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			if("true".equals(likeSearchName)) {
				sc = new SearchCondition(EPMDocument.class, EPMDocument.NAME, SearchCondition.LIKE, "%" + name + "%", false);
			}else {
				sc = new SearchCondition(EPMDocument.class, EPMDocument.NUMBER, SearchCondition.EQUAL, name, false);
			}
			query.appendWhere(sc, new int[] { idx });
		}
		
		//CAD 파일명 
		if(cadName.length() > 0) {
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(EPMDocument.class, EPMDocument.CADNAME, SearchCondition.LIKE, "%" + name + "%", false);
			query.appendWhere(sc, new int[] { idx });
		}
		
		//CAD 구분
		if (cadDivision.size() > 0) {
			if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(EPMDocument.class, EPMDocument.AUTHORING_APPLICATION), SearchCondition.IN, new ArrayExpression(cadDivision.toArray()));
			query.appendWhere(sc, new int[] { idx });
		}
		
		//CAD 타입
		if (cadType.size() > 0) {
			if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(EPMDocument.class, EPMDocument.DOC_TYPE), SearchCondition.IN, new ArrayExpression(cadType.toArray()));
			query.appendWhere(sc, new int[] { idx });
		}
				
		//작성자
		if (creator.size() > 0) {
			List<Long> userOidLongValueList = new ArrayList<>();
			
			for(String pp : creator) {
				People people = (People) CommonUtil.getObject(pp);
				WTUser user = people.getUser();
				
				userOidLongValueList.add(CommonUtil.getOIDLongValue(user));
			}
			
			if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(EPMDocument.class, EPMDocument.CREATOR + ".key.id"), SearchCondition.IN, new ArrayExpression(userOidLongValueList.toArray()));
			query.appendWhere(sc, new int[] { idx });
		}
				
		//수정자
		if (modifier.size() > 0) {
			List<Long> userOidLongValueList = new ArrayList<>();
			
			for(String pp : modifier) {
				People people = (People) CommonUtil.getObject(pp);
				WTUser user = people.getUser();
				
				userOidLongValueList.add(CommonUtil.getOIDLongValue(user));
			}
			
			if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(EPMDocument.class, "iterationInfo.modifier.key.id"), SearchCondition.IN, new ArrayExpression(userOidLongValueList.toArray()));
			query.appendWhere(sc, new int[] { idx });
		}
		
		//상태
		if (state.size() > 0) {
			if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(EPMDocument.class, EPMDocument.LIFE_CYCLE_STATE), SearchCondition.IN, new ArrayExpression(state.toArray()));
			query.appendWhere(sc, new int[] { idx });
		}
		
		//Working Copy 제외
		/*
		 * if (query.getConditionCount() > 0) { query.appendAnd(); }
		 * query.appendWhere(new SearchCondition(EPMDocument.class, EPMDocument.CHECKOUT_INFO +
		 * ".state", SearchCondition.NOT_EQUAL, "wrk", false), new int[] { idx });
		 */
		//등록일
    	if(predate.length() > 0){
    		if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
    		sc = new SearchCondition(EPMDocument.class, EPMDocument.CREATE_TIMESTAMP ,SearchCondition.GREATER_THAN,DateUtil.convertStartDate(predate));
    		query.appendWhere(sc, new int[] { idx });
    	}
    	if(postdate.length() > 0){
    		if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
    		sc = new SearchCondition(EPMDocument.class, EPMDocument.CREATE_TIMESTAMP,SearchCondition.LESS_THAN,DateUtil.convertEndDate(postdate));
    		query.appendWhere(sc, new int[] { idx });
    	}
    	
    	//수정일
    	if(predate_modify.length() > 0){
    		if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
    		sc = new SearchCondition(EPMDocument.class, EPMDocument.MODIFY_TIMESTAMP ,SearchCondition.GREATER_THAN,DateUtil.convertStartDate(predate_modify));
    		query.appendWhere(sc, new int[] { idx });
    	}
    	if(postdate_modify.length() > 0){
    		if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
    		sc = new SearchCondition(EPMDocument.class, EPMDocument.MODIFY_TIMESTAMP,SearchCondition.LESS_THAN,DateUtil.convertEndDate(postdate_modify));
    		query.appendWhere(sc, new int[] { idx });
    	}
    	
    	if(relatedProject.size() > 0) {
    		int masterIdx = query.addClassList(EPMDocumentMaster.class, false);
    		int linkIdx = query.addClassList(EProjectMasteredLink.class, false);
    		int pjtIdx = query.addClassList(EProject.class, false);
    		
    		List<Long> projectOidLongValueList = new ArrayList<>();
    		for(String oid : relatedProject) {
    			projectOidLongValueList.add(CommonUtil.getOIDLongValue(oid));
    		}
    		
    		if(query.getConditionCount() > 0) {
    			query.appendAnd();
    		}
    		sc = new SearchCondition(new ClassAttribute(EPMDocument.class, EPMDocument.MASTER_REFERENCE + ".key.id"), SearchCondition.EQUAL, new ClassAttribute(EPMDocumentMaster.class, EPMDocumentMaster.PERSIST_INFO + ".theObjectIdentifier.id"));
    		query.appendWhere(sc, new int[] {idx, masterIdx});
    		
    		if(query.getConditionCount() > 0) {
    			query.appendAnd();
    		}
    		sc = new SearchCondition(new ClassAttribute(EPMDocumentMaster.class, EPMDocumentMaster.PERSIST_INFO + ".theObjectIdentifier.id"), SearchCondition.EQUAL, new ClassAttribute(EProjectMasteredLink.class, EProjectMasteredLink.ROLE_AOBJECT_REF + ".key.id"));
    		query.appendWhere(sc, new int[] {masterIdx, linkIdx});
    		
    		if(query.getConditionCount() > 0) {
    			query.appendAnd();
    		}
    		sc = new SearchCondition(new ClassAttribute(EProjectMasteredLink.class, EProjectMasteredLink.ROLE_BOBJECT_REF + ".key.id"), SearchCondition.EQUAL, new ClassAttribute(EProject.class, EProject.PERSIST_INFO + ".theObjectIdentifier.id"));
    		query.appendWhere(sc, new int[] {linkIdx, pjtIdx});
    		
    		if(query.getConditionCount() > 0) {
    			query.appendAnd();
    		}
    		sc = new SearchCondition(new ClassAttribute(EProject.class, EProject.PERSIST_INFO + ".theObjectIdentifier.id")
    				, SearchCondition.IN, new ArrayExpression(projectOidLongValueList.toArray()));
    		query.appendWhere(sc, new int[] {pjtIdx});
    	}

    	Hashtable<String, Object> params = new Hashtable<>();
    	
    	params.put("CUSTOMER", customer);
    	params.put("UPG", upg);
    	params.put("EXTENSIONS", extensions);
    	params.put("DESIGNED", designed);
    	params.put("CUSTOMER_PART_NUMBER", customer_part_number);
    	params.put("EO_NUMBER", eo_number);
    	params.put("MATERIAL", material);
    	params.put("WEIGHT", weight);
    	params.put("THICKNESS", thickness);
    	params.put("FINISH", finish);
    	
//    	IBAUtil.appendIBAWhere(query, EPMDocument.class, idx, params, true);
    	
    	// 소팅
		if (sort.size() > 0) {
			String id = (String) sort.get("id");
			String dir = (String) sort.get("dir");
			
			String sortValue = "";
			if("number".equals(id)) {
				sortValue = EPMDocument.NUMBER;
			} else if("name".equals(id)) {
				sortValue = EPMDocument.NAME;
			} else if("stateName".equals(id)) {
				sortValue = EPMDocument.LIFE_CYCLE_STATE;
			} else if("createDateFormat".equals(id)) {
				sortValue = EPMDocument.CREATE_TIMESTAMP;
			} else if("modifyDateFormat".equals(id)) {
				sortValue = EPMDocument.MODIFY_TIMESTAMP;
			}
			
			if ("asc".equals(dir)) {
				query.appendOrderBy(new OrderBy(new ClassAttribute(EPMDocument.class, sortValue), false), new int[] { idx });
			} else if("desc".equals(dir)) {
				query.appendOrderBy(new OrderBy(new ClassAttribute(EPMDocument.class, sortValue), true), new int[] { idx });
			}
		} else {
			query.appendOrderBy(new OrderBy(new ClassAttribute(EPMDocument.class, E3PSDocument.MODIFY_TIMESTAMP), true), new int[] { idx });
		}
		System.out.println("getEpmListQuery ::: " + query);
		System.out.println(query.toString());
		LOGGER.info(query.toString());
    	return query;
	}
	
	public List<EpmData> getMainEpm(Map<String, Object> reqMap) throws Exception {
		
		List<EpmData> list = new ArrayList<>();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		WTPart part = (WTPart) CommonUtil.getObject(oid);
		
		EPMDocument epm = getEPMDocument(part);
		
		if(epm != null) {
			EpmData data = new EpmData(epm);
			list.add(data);
		}
		
		return list;
	}
	
	public List<EpmData> getMainEpms(Map<String, Object> reqMap) throws Exception {
	    List<EpmData> list = new ArrayList<>();

	    List<String> oidList = (List<String>) reqMap.get("oid");

	    for (String oid : oidList) {
	        WTPart part = (WTPart) CommonUtil.getObject(oid);
	        EPMDocument epm = getEPMDocument(part);

	        if (epm != null) {
	            EpmData data = new EpmData(epm);
	            list.add(data);
	        }
	    }

	    return list;
	}
	
	/**
	 * 
	 * @desc	: 부품에서  ownerShip EPMdocument
	 * @author	: tsuam
	 * @date	: 2019. 9. 9.
	 * @method	: getEPMDocument
	 * @return	: EPMDocument
	 * @param part
	 * @return
	 * @throws Exception
	 */
	public EPMDocument getEPMDocument(WTPart part) throws Exception {
		
        if (part == null) {
        	return null; 
        }
        
        QueryResult qr = null;
        
        if (VersionControlHelper.isLatestIteration(part)) {
        	qr = PersistenceHelper.manager.navigate(part, "buildSource", EPMBuildRule.class);
        } else {
        	qr = PersistenceHelper.manager.navigate(part, "builtBy", EPMBuildHistory.class);
        }
            
        while (qr != null && qr.hasMoreElements()) {
        	return (EPMDocument) qr.nextElement();
        }
            
        return null;
    }
	
	/**
	 * 
	 * @desc	: WTPart 또는 품목에서 
	 * @author	: tsuam
	 * @date	: 2019. 9. 9.
	 * @method	: getBuildRule
	 * @return	: EPMBuildRule
	 * @param obj
	 * @return
	 * @throws WTException
	 */
	public  EPMBuildRule getBuildRule(Object obj) throws Exception {
		QueryResult qr = null;
		
		if(obj instanceof WTPart){
			WTPart part =(WTPart)obj;
			qr = PersistenceHelper.manager.navigate(part, "buildSource", EPMBuildRule.class, false);
		}else{
			EPMDocument epm =(EPMDocument)obj;
			qr = PersistenceHelper.manager.navigate(epm, "buildTarget", EPMBuildRule.class, false);
		}
      
        while (qr.hasMoreElements())
        {
            EPMBuildRule ebr = (EPMBuildRule) qr.nextElement();
            if (!WorkInProgressHelper.isWorkingCopy((Workable) ebr.getBuildSource()));
                return ebr;
        }

        return null;
    }
	
	public List<StructureData> getStructure(Map<String, Object> reqMap) throws Exception {
		
		List<StructureData> list = new ArrayList<>();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		EPMDocument epm = (EPMDocument) CommonUtil.getObject(oid);
		
		if(epm.getDocType().toString().equals("CADASSEMBLY") && epm.getAuthoringApplication().toString().equals("PROE")) {
			List<Object[]> objectList = EpmHelper.manager.getEPMChildObject(epm);
			
			for(Object[] obj : objectList) {
				EPMMemberLink link = (EPMMemberLink)obj[0];
				EPMDocumentMaster master = (EPMDocumentMaster)link.getRoleBObject();
				EPMDocument epmChild = getEPMDocument(master.getNumber());
				
				//EPMDocument epmChild = (EPMDocument)obj[1];
				
				StructureData data = new StructureData(epmChild, link);
				
				list.add(data);
			}
			
		}
		
		return list;
	}
	
	public List<EpmData> getReference(Map<String, Object> reqMap) throws Exception {
		
		List<EpmData> list = new ArrayList<>();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		EPMDocument epm = (EPMDocument) CommonUtil.getObject(oid);
		
		if(epm != null) {
			if(epm.getDocType().toString().equals("CADDRAWING") && epm.getAuthoringApplication().toString().equals("SOLIDWORKS")) {
				if(epm != null) {
					List<EPMReferenceLink> refList = getReferenceDependency(epm, "references");
					
					for(EPMReferenceLink link : refList) {
						EPMDocumentMaster master = (EPMDocumentMaster)link.getReferences();
						EPMDocument epmdoc = getLastEPMDocument(master);
						
						EpmData data = new EpmData(epmdoc);
		
						list.add(data);
					}
				}
			}
		}
		return list;
	}

	public List<EPMReferenceLink> getReferenceDependency(EPMDocument doc, String role) throws Exception {
    	List<EPMReferenceLink> references = new ArrayList<>();
    	
    	QueryResult queryReferences = null;
    	
        if(role.equals("referencedBy")) { //참조항목
        	queryReferences = EPMStructureHelper.service.navigateReferencedBy((EPMDocumentMaster)doc.getMaster(), null, false);
        } else { //참조
        	queryReferences = EPMStructureHelper.service.navigateReferences(doc, null, false);
        }
        
        EPMReferenceLink referenceLink = null;
        while(queryReferences.hasMoreElements()) {
        	referenceLink = (EPMReferenceLink)queryReferences.nextElement();
        	references.add(referenceLink);
        }
        
        return references;
    }
	
	public EPMDocument getLastEPMDocument(EPMDocumentMaster master) throws Exception{
    	
		EPMDocument epm = null;
		
		long longoid = CommonUtil.getOIDLongValue(master);
		Class class1 = EPMDocument.class;
	
		QuerySpec qs = new QuerySpec();
		int i = qs.appendClassList(class1, true);
	 
		qs.appendWhere(VersionControlHelper.getSearchCondition(EPMDocument.class, true), new int[] { i });
	 
		SearchUtil.addLastVersionCondition(qs, EPMDocument.class, i);
	 
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(class1, "masterReference.key.id", SearchCondition.EQUAL,longoid), new int[] { i });
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		while(qr.hasMoreElements()){
			Object obj[] = (Object[])qr.nextElement();
			epm = (EPMDocument)obj[0];
		}
   	 
		return epm;
	}
	
	public List<EpmData> getReferenceBy(Map<String, Object> reqMap) throws Exception {
		
		List<EpmData> list = new ArrayList<>();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		EPMDocument epm = (EPMDocument) CommonUtil.getObject(oid);
		
		if(epm != null) {
			List<EPMReferenceLink> refList = getEPMReferenceList((EPMDocumentMaster)epm.getMaster());
			LOGGER.info("getReferenceBy =" + refList.size());
			for(EPMReferenceLink link : refList) {
				EPMDocument epmdoc = link.getReferencedBy();
				EpmData data = new EpmData(epmdoc);
				
				list.add(data);
			}
		}
		
		return list;
	}
	
	/**
	 * 
	 * @desc	: 2D Drawing link
	 * @author	: tsuam
	 * @date	: 2019. 9. 19.
	 * @method	: getEPMReferenceList
	 * @return	: List<EPMReferenceLink>
	 * @param master
	 * @return
	 * @throws Exception
	 */
	public List<EPMReferenceLink>  getEPMReferenceList(EPMDocumentMaster master) throws Exception {
    	List<EPMReferenceLink> list = new ArrayList<EPMReferenceLink>();
    	
    	QuerySpec qs = new QuerySpec();
    	
		int idxA = qs.addClassList(EPMReferenceLink.class, true);
		int idxB = qs.addClassList(EPMDocument.class, false);
		
		//Join
		qs.appendWhere(new SearchCondition(EPMReferenceLink.class, EPMReferenceLink.ROLE_AOBJECT_REF + ".key.id",
											EPMDocument.class, EPMReferenceLink.PERSIST_INFO + ".theObjectIdentifier.id"),new int[]{idxA,idxB});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(EPMReferenceLink.class, EPMReferenceLink.ROLE_BOBJECT_REF + ".key.id",
											SearchCondition.EQUAL,CommonUtil.getOIDLongValue(master)),new int[]{idxA});
		
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(EPMReferenceLink.class, EPMReferenceLink.REFERENCE_TYPE,
											SearchCondition.EQUAL,"DRAWING"),new int[]{idxA}); //DRAWING
		//최신 이터레이션
		qs.appendAnd();
		qs.appendWhere(VersionControlHelper.getSearchCondition(EPMDocument.class, true), new int[] { idxB });
		
		//최신 버전
		SearchUtil.addLastVersionCondition(qs, EPMDocument.class, idxB);

		QueryResult rt = PersistenceHelper.manager.find(qs);
		
		//LOGGER.info("getEPMReferenceList =" + rt.size());
		while(rt.hasMoreElements()){
			Object[] oo = (Object[]) rt.nextElement();
			EPMReferenceLink link = (EPMReferenceLink)oo[0];
			list.add(link);
		}
	    
		
	    return list;
	}
	
	/**
	 * 2D에서 3D 
	 * @methodName : get2DTo3DPM
	 * @author : tsuam
	 * @date : 2021.11.29
	 * @return : EPMDocument
	 * @description :
	 */
	public EPMDocument get2DTo3DEPM(EPMDocument epm) throws Exception{
		
		EPMDocument epm3D = null;
		
		if(epm.getDocType().toString().equals("CADDRAWING")) {
			
			List<EPMReferenceLink> references = new ArrayList<>();
			
			QueryResult queryReferences = EPMStructureHelper.service.navigateReferences(epm, null, false);
			EPMReferenceLink referenceLink = null;
			while(queryReferences.hasMoreElements()) {
				referenceLink = (EPMReferenceLink)queryReferences.nextElement();
				references.add(referenceLink);
			}
			
			for(EPMReferenceLink link : references) {
				EPMDocumentMaster master = (EPMDocumentMaster)link.getReferences();
				epm3D = getLastEPMDocument(master);
			}
			
		}
		
		return epm3D;
		
	}
	
	/**
	 * 
	 * @desc	: 2D Drawing
	 * @author	: tsuam
	 * @date	: 2019. 9. 19.
	 * @method	: getEPMToDrawing
	 * @return	: EPMDocument
	 * @param master
	 * @return
	 * @throws Exception
	 */
	public EPMDocument getDrawing(EPMDocumentMaster master) throws Exception{
		EPMDocument epm2d = null;
		
		List<EPMReferenceLink> list = getEPMReferenceList(master);
		
		for(EPMReferenceLink link : list){
			
			epm2d = link.getReferencedBy();
			
			if(epm2d.getDocType().toString().equals("CADDRAWING")){
				return epm2d;
			}
		}
		
		return epm2d;
		
	}
	public List<EPMDocument> getDrawingList(String number,boolean isCADASSEMBLY) throws Exception {
		return getDrawingList(number, isCADASSEMBLY, true);
	}
	/**
	 * 
	 * @desc	: 부품의 관련  DRW 리스트  : 도면 배포용
	 * 			P342E300610 : ( like 'P342E300610X%' or  = 'P342E300610.DRW'
	 * @author	: tsuam
	 * @date	: 2020. 1. 21.
	 * @method	: getDrawingList
	 * @return	: List<EPMDocument>
	 * @param number
	 * @param isCADASSEMBLY ASM,PRT
	 * @param isDrawing  DWG,STEP
	 * @return
	 * @throws Exception
	 */
	public List<EPMDocument> getDrawingList(String number,boolean isCADASSEMBLY,boolean isDrawing) throws Exception {
		
		List<EPMDocument> list = new ArrayList<EPMDocument>();
		QuerySpec query = new QuerySpec();
		SearchCondition sc = null;
		int idx = query.addClassList(EPMDocumentMaster.class, true);
		if(isDrawing){
			sc = new SearchCondition(EPMDocumentMaster.class, EPMDocumentMaster.DOC_TYPE, SearchCondition.EQUAL, "CADDRAWING", false);
			query.appendWhere(sc, new int[] { idx });
		}
		
		if(query.getConditionCount() > 0) {
			query.appendAnd();
		}
		
		
		if(isDrawing){ //DRW
			if(isCADASSEMBLY){
				query.appendOpenParen();
					sc = new SearchCondition(EPMDocumentMaster.class, EPMDocumentMaster.NUMBER, SearchCondition.LIKE, number + "X%", false);
					query.appendWhere(sc, new int[] { idx });
					query.appendOr();
					sc = new SearchCondition(EPMDocumentMaster.class, EPMDocumentMaster.NUMBER, SearchCondition.EQUAL, number + ".DRW", false);
					query.appendWhere(sc, new int[] { idx });
				query.appendCloseParen();
			}else{
				sc = new SearchCondition(EPMDocumentMaster.class, EPMDocumentMaster.NUMBER, SearchCondition.EQUAL, number + ".DRW", false);
				query.appendWhere(sc, new int[] { idx });
			}
		}else{ //PRT,ASM,DWG
			query.appendOpenParen();
			if(isCADASSEMBLY){
				sc = new SearchCondition(EPMDocumentMaster.class, EPMDocumentMaster.NUMBER, SearchCondition.LIKE, number + "X%", false);
				query.appendWhere(sc, new int[] { idx });
				query.appendOr();
				sc = new SearchCondition(EPMDocumentMaster.class, EPMDocumentMaster.NUMBER, SearchCondition.EQUAL, number + ".DRW", false);
				query.appendWhere(sc, new int[] { idx });
				query.appendOr();
				sc = new SearchCondition(EPMDocumentMaster.class, EPMDocumentMaster.NUMBER, SearchCondition.EQUAL, number + ".ASM", false);
				query.appendWhere(sc, new int[] { idx });
			}else{
				sc = new SearchCondition(EPMDocumentMaster.class, EPMDocumentMaster.NUMBER, SearchCondition.EQUAL, number + ".DRW", false);
				query.appendWhere(sc, new int[] { idx });
				query.appendOr();
				sc = new SearchCondition(EPMDocumentMaster.class, EPMDocumentMaster.NUMBER, SearchCondition.EQUAL, number + ".PRT", false);
				query.appendWhere(sc, new int[] { idx });
			}
				
				
				
			query.appendCloseParen();
			
			/*
			if(isCADASSEMBLY){
				query.appendOpenParen();
					sc = new SearchCondition(EPMDocumentMaster.class, EPMDocumentMaster.NUMBER, SearchCondition.LIKE, number + "X%", false);
					query.appendWhere(sc, new int[] { idx });
					query.appendOr();
					sc = new SearchCondition(EPMDocumentMaster.class, EPMDocumentMaster.NUMBER, SearchCondition.EQUAL, number + ".ASM", false);
					query.appendOr();
					sc = new SearchCondition(EPMDocumentMaster.class, EPMDocumentMaster.NUMBER, SearchCondition.EQUAL, number + ".DRW", false);
					query.appendWhere(sc, new int[] { idx });
				query.appendCloseParen();
			}else{
				query.appendOpenParen();
					sc = new SearchCondition(EPMDocumentMaster.class, EPMDocumentMaster.NUMBER, SearchCondition.EQUAL, number + ".DRW", false);
					query.appendWhere(sc, new int[] { idx });
					query.appendOr();
					sc = new SearchCondition(EPMDocumentMaster.class, EPMDocumentMaster.NUMBER, SearchCondition.EQUAL, number + ".PRT", false);
					query.appendWhere(sc, new int[] { idx });
				query.appendCloseParen();
				
				
			}
			*/
		}
		
		
		LOGGER.info("isDrawing =" + isDrawing);
		LOGGER.info("getDrawingList =" + query.toString());
		QueryResult rt =PersistenceHelper.manager.find(query);
		
		while(rt.hasMoreElements()){
			Object[] obj = (Object[])rt.nextElement();
			EPMDocumentMaster master = (EPMDocumentMaster)obj[0];
			EPMDocument epm = getEPMDocument(master.getNumber());
			//LOGGER.info("master =" + master.getNumber() +",epm = " + epm);
			list.add(epm);
		}
		
		return list;
	}
	
	public List<EpmData> getRelatedEpm(Map<String, Object> reqMap) throws Exception {
		
		List<EpmData> list = new ArrayList<>();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		Persistable per = CommonUtil.getObject(oid);
		
		
		if(per != null) {
			if(per instanceof WTPart) {
				WTPart part = (WTPart) per;
				boolean lastVer = CommonUtil.isLastVersion(part);
				List<EPMDescribeLink> descList = getEPMDescribeLink(part, lastVer);
				
				for(EPMDescribeLink link : descList){
					EPMDocument epmLink = (EPMDocument)link.getRoleBObject();
					EpmData data = new EpmData(epmLink);
					list.add(data);
				}
			}
//			else if(per instanceof DistributeDocument){
//				DistributeDocument dis = (DistributeDocument) per;
//				list = DistributeHelper.manager.getEpmList(dis);
//			}
			else if(per instanceof EChangeOrder2){
				list = ChangeHelper.manager.getECORelatedEPMList(oid);
			}else if(per instanceof EChangeRequest2){
				//list = ChangeHelper.manager.getECRRelatedEPMList(oid);
				
				
//				EChangeRequest2 ecr = (EChangeRequest2) per;
//				List<EPMDocument> relatedDrawings = new ArrayList<EPMDocument>();
//				relatedDrawings = ChangeECRSearch.getECOrelatedDrawings2(ecr);
//				for(EPMDocument epm : relatedDrawings) {
//					EpmData data = new EpmData(epm);
//					list.add(data);
//				}
			}
		}
		
		return list;
	}
	
	public List<EPMDocument> getEPMDesribeEPM(WTPart part) throws Exception{
		boolean lastVer = CommonUtil.isLastVersion(part);
		List<EPMDescribeLink> linklist= getEPMDescribeLink(part, lastVer);
		List<EPMDocument> list = new ArrayList<EPMDocument>();
		for(EPMDescribeLink link : linklist ){
			EPMDocument epm = link.getDescribedBy();
			list.add(epm);
		}
		
		return list;
	}
	
	public List<EpmData> getResultEpm(Map<String, Object> reqMap) throws Exception {
		
		List<EpmData> list = new ArrayList<>();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		Persistable per = CommonUtil.getObject(oid);
		
		
		if(per != null) {
			if(per instanceof WTPart) {
				WTPart part = (WTPart) per;
				boolean lastVer = CommonUtil.isLastVersion(part);
				List<EPMDescribeLink> descList = getEPMDescribeLink(part, lastVer);
				
				for(EPMDescribeLink link : descList){
					EPMDocument epmLink = (EPMDocument)link.getRoleBObject();
					EpmData data = new EpmData(epmLink);
					list.add(data);
				}
			}
//			else if(per instanceof DistributeDocument){
//				DistributeDocument dis = (DistributeDocument) per;
//				list = DistributeHelper.manager.getEpmList(dis);
//			}
			else if(per instanceof EChangeOrder2){
				EChangeOrder2 eco = (EChangeOrder2) per;
				List<EPMDocument> relatedDrawings = new ArrayList<EPMDocument>();
				relatedDrawings = ChangeECOSearch.getECOrelatedResultDrawings2(eco);
				for(EPMDocument epm : relatedDrawings) {
					EpmData data = new EpmData(epm);
					list.add(data);
				}
			}else if(per instanceof EChangeRequest2){
				EChangeRequest2 ecr = (EChangeRequest2) per;
				List<EPMDocument> relatedDrawings = new ArrayList<EPMDocument>();
				relatedDrawings = ChangeECRSearch.getECOrelatedDrawings2(ecr);
				for(EPMDocument epm : relatedDrawings) {
					EpmData data = new EpmData(epm);
					list.add(data);
				}
			}else if(per instanceof EChangeActivity){
				EChangeActivity eca = (EChangeActivity) per;
				if(eca.getOrder() instanceof EChangeOrder2){
					EChangeOrder2 eco = (EChangeOrder2)eca.getOrder();
					List<Object> relatedResultDrawings = new ArrayList<Object>();
					relatedResultDrawings = ChangeECOSearch.getECOrelatedResultDrawings(eco);
					int total = relatedResultDrawings.size();
					if(total>0){
						for (int i = 0; i < relatedResultDrawings.size(); i++) {
							Object[] o = (Object[])relatedResultDrawings.get(i);
							EcoTargetResultLink link = (EcoTargetResultLink)o[0];
							EPMDocument epm = (EPMDocument)o[1];
							EpmData data = new EpmData(epm);
							list.add(data);
						}
					}
				}else if(eca.getOrder() instanceof EChangeRequest2) {
					EChangeRequest2 ecr = (EChangeRequest2) eca.getOrder();
					List<EPMDocument> relatedDrawings = new ArrayList<EPMDocument>();
					relatedDrawings = ChangeECRSearch.getECOrelatedDrawings2(ecr);
					for(EPMDocument epm : relatedDrawings) {
						EpmData data = new EpmData(epm);
						list.add(data);
					}
				}
				
			}
		}
		
		return list;
	}
	
	public List<EPMDescribeLink> getEPMDescribeLink(RevisionControlled rc, boolean isLast){
    	
    	List<EPMDescribeLink> list = new ArrayList<EPMDescribeLink>();
    	try{
    		
    		long longOid = CommonUtil.getOIDLongValue(rc);
    		Class cls = null;
    		
    		String columnName = "";
    		
    		QuerySpec qs = new QuerySpec();
    		int idxA = qs.addClassList(EPMDescribeLink.class, true);
    		int idxB = qs.addClassList(EPMDocument.class, false);
    		int idxC = qs.addClassList(WTPart.class, false);
    		int idx = 0;
    		if(rc instanceof EPMDocument){
    			cls = WTPart.class;
    			columnName = EPMDescribeLink.ROLE_BOBJECT_REF + ".key.id";
    			idx = idxC;
    		}else if(rc instanceof WTPart){
    			cls = EPMDocument.class;
    			columnName = EPMDescribeLink.ROLE_AOBJECT_REF + ".key.id";
    			idx = idxB;
    		}
    		//Join
    		qs.appendWhere(new SearchCondition(EPMDescribeLink.class, EPMDescribeLink.ROLE_BOBJECT_REF + ".key.id",
    				EPMDocument.class, EPMDescribeLink.PERSIST_INFO + ".theObjectIdentifier.id"),new int[]{idxA,idxB});
    		
    		qs.appendAnd();
    		qs.appendWhere(new SearchCondition(EPMDescribeLink.class,EPMDescribeLink.ROLE_AOBJECT_REF + ".key.id",
    				WTPart.class,EPMDescribeLink.PERSIST_INFO + ".theObjectIdentifier.id"),new int[]{idxA,idxC});
    		
    		qs.appendAnd();
    		qs.appendWhere(new SearchCondition(EPMDescribeLink.class,columnName,
    											SearchCondition.EQUAL,longOid),new int[]{idxA});
    		
    		if(isLast){
    			//최신 이터레이션
        		qs.appendAnd();
        		qs.appendWhere(VersionControlHelper.getSearchCondition(cls, true), new int[] { idx });
        		
        		//최신 버전
        		SearchUtil.addLastVersionCondition(qs, cls, idx);
    		}
    		
			QueryResult rt =PersistenceHelper.manager.find(qs);
			while(rt.hasMoreElements()){
				Object[] oo = (Object[]) rt.nextElement();
				EPMDescribeLink link = (EPMDescribeLink)oo[0];
				list.add(link);
				
			}
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	return list;
    }
	
	/**
	 * 
	 * @desc	: CAD Type 별 Template CAD 파일
	 * @author	: plmadmin
	 * @date	: 2019. 9. 2.
	 * @method	: getCADTemplate
	 * @return	: EPMDocument
	 * @param cadType
	 * @return
	 * @throws Exception
	 */
	public EPMDocument  getCADTemplate(String cadType) throws Exception{
		
		String owner = SessionHelper.getPrincipal().getName();
		SessionHelper.manager.setAdministrator();
		
		EPMDocument epm = null;
		QuerySpec qs = new QuerySpec();
		//int idxA = qs.addClassList(ContainerFilterLink.class, false);
		int idxB = qs.addClassList(EPMDocument.class, true);
		
		String number = EpmUtil.getTemplateNumber(cadType);
		
		if(number.length() ==0 ){
			throw new Exception(MessageUtil.getMessage("Template 파일이 존재 하지 않습니다."));
		}
		
		qs.appendWhere(VersionControlHelper.getSearchCondition(EPMDocument.class, true), new int[]{idxB});
		//Join
		/*
		qs.appendWhere(new SearchCondition(ContainerFilterLink.class, ContainerFilterLink.ROLE_BOBJECT_REF + ".key.id",
				EPMDocument.class, EPMDocument.PERSIST_INFO + ".theObjectIdentifier.id"),new int[]{idxA,idxB});
		*/
		
		
		/*
		qs.appendAnd();
		SearchCondition sc = new SearchCondition(ContainerFilterLink.class, ContainerFilterLink.FILTERED,SearchCondition.IS_FALSE);
		qs.appendWhere(sc, new int[] { idxA });
		*/
		qs.appendAnd();
		SearchCondition sc = new SearchCondition(EPMDocument.class, EPMDocument.DOC_TYPE, SearchCondition.EQUAL, cadType);
		qs.appendWhere(sc, new int[] { idxB });
		
		qs.appendAnd();
		sc = new SearchCondition(EPMDocument.class, EPMDocument.NUMBER, SearchCondition.EQUAL, number);
		qs.appendWhere(sc, new int[] { idxB });
		
		LOGGER.info(qs.toString());
	
		QueryResult rt = PersistenceHelper.manager.find(qs);
		if(rt.size() > 0){
			Object[] obj = (Object[])rt.nextElement();
			epm = (EPMDocument)obj[0];
		}
		
		if(epm == null){
			throw new Exception(MessageUtil.getMessage("Template 파일이 존재 하지 않습니다."));
		}
		
		
		SessionHelper.manager.setPrincipal(owner);
		
		return epm;
		
	}
	
	/**
	 * 
	 * @desc	: 3D,STEP,2D Drawing 에서 변환 파일 Dwg,PDF 파일  ,Role Type ADDITIONAL_FILES
	 * @author	: tsuam
	 * @date	: 2019. 9. 19.
	 * @method	: getDrawingPublishFile
	 * @return	: List<ApplicationData>
	 * @param epm
	 * @return
	 */
	public List<ApplicationData> getDrawingPublishFile(EPMDocument epm){
    	
		List<ApplicationData> list = new ArrayList<ApplicationData>();
    	try{
    		
    		if(!"PROE".equals(epm.getAuthoringApplication().toString())) return list;
			Representation representation = PublishUtils.getRepresentation(epm); 
			if(representation == null) return  list;
			
			boolean isDrawing = epm.getDocType().toString().equals("CADDRAWING") && epm.getAuthoringApplication().toString().equals("PROE");
			
			representation = (Representation) ContentHelper.service.getContents(representation);
	        Vector contentList = ContentHelper.getContentList(representation);
	       // LOGGER.info("getDrawingPublishFile contentList =" + contentList.size());
	        for (int l = 0; l < contentList.size(); l++) {
	            ContentItem contentitem = (ContentItem) contentList.elementAt(l);
	            if( contentitem instanceof ApplicationData){
	            	ApplicationData app = (ApplicationData) contentitem;
	            	//String fileName= EpmUtil.getPublishFile(epm, drawAppData.getFileName());
	            	// LOGGER.info("getDrawingPublishFile contentList =" + app.getFileName());
	            	String role = app.getRole().toString();
	            	String fileName = app.getFileName();
	            	
	            	boolean isDrawingPublish = isDrawing && (role.equals("ADDITIONAL_FILES") && ( fileName.toLowerCase().indexOf("dwg") >= 0  || fileName.toLowerCase().indexOf("pdf") >= 0 ));
	            	
	            	boolean is3DPublish = !isDrawing && role.equals("ADDITIONAL_FILES") && fileName.toLowerCase().indexOf("stp") >= 0;
	            	//LOGGER.info("getDrawingPublishFile contentList =" + app.getFileName() + ",Role = " + role +",isDrawingPublish =" + isDrawingPublish);
	            	if(isDrawingPublish){
	            		list.add(app);
	            	} else if(is3DPublish) {
	            		list.add(app);
	            	}
	            }
	        }
	       
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	LOGGER.info("getDrawingPublishFile list =" + list.size());
    	return list;
    }
	
	public String  getDrawingPublishListDown(EPMDocument epm){
		List<ApplicationData>  list = getDrawingPublishFile(epm);
		String fileUrlList = "";
		
		for(ApplicationData data : list){
			
			String url = CommonUtil.getURLString("/content/publishDownload") + "?appOid=" + CommonUtil.getOIDString(data);
			LOGGER.info("publish File data = " + data.getFileName());
			String fileDown = "<a href="+ url+">"+data.getFileName()+"</a>";
			LOGGER.info("publish File fileDown =" +fileDown) ;
			
			if(fileUrlList != "") {
				fileUrlList += ", ";
			}
			fileUrlList = fileUrlList + fileDown;
		}
		
		return fileUrlList;
	}
	
	
	/**
	 * 
	 * @desc	: 도면 변환 파일 
	 * @author	: tsuam
	 * @date	: 2020. 2. 13.
	 * @method	: getDrawingPublishFile
	 * @return	: List<DistributeFileData>
	 * @param dis
	 * @param location
	 * @param fileList
	 * @param part
	 * @param epm
	 * @return
	 * @throws Exception
	 */
//	public List<DistributeFileData> getDrawingPublishFile(DistributeDocument dis,String location,List<DistributeFileData> fileList,EPMDocument epm) throws Exception{
//    	
//		if(epm == null){
//			return fileList;
//		}
//		
//				
//		boolean isCreo = "PROE".equals(epm.getAuthoringApplication().toString());
//		
//		if(isCreo){
//			// Creo 
//			fileList = getCreoFileList(fileList, location, dis,epm);
//		}else{
//			// 제어도면
//			fileList = getOtherFileList(fileList, location, dis, epm);
//		}
//		
//		
//		
//    	return fileList;
//    }
//	
//	public List<DistributeFileData> getDrawingPublishFileList(DistributeDocument dis,String location,List<DistributeFileData> fileList,PartData partData) throws Exception{
//    	
//		
//		
//		
//			// Creo 
//			fileList = getERPToCPCFileList(fileList, location, dis, partData);
//		
//		
//		
//		
//    	return fileList;
//    }
	
	/**
	 * @desc	: 제어도면 파일 리스트
	 * @author	: mnyu
	 * @date	: 2019. 10. 30.
	 * @method	: getOtherFileList
	 * @return	: List<DistributeFileData>
	 * @param fileList
	 * @param location
	 * @param dis
	 * @param part
	 * @param epm
	 * @return
	 * @throws Exception 
	 */
//	private List<DistributeFileData> getOtherFileList(List<DistributeFileData> fileList, String location,
//			DistributeDocument dis, EPMDocument epm) throws Exception {
//		
//		ApplicationData data = ContentUtil.getPrimaryFile(epm);
//		DistributeFileData fileData = new DistributeFileData();
//		
//		// 배포 - 도면
//		String number = epm.getNumber();
//		String fileType = "epm";
//	
//		String fileLocation = location + "/" + number;
//		
//		fileData.setApp(data);
//		fileData.setEpm(epm);
//		fileData.setFileType(fileType);
//		fileData.setFileLocation(fileLocation);
//		fileData.setMainNo(dis.getDistributeNumber());
//		fileData.setSubNo(number);
//		
//		fileList.add(fileData);
//		
//		return fileList;
//	}
//
//	/**
//	 * @desc	: Creo 파일 리스트
//	 * @author	: mnyu
//	 * @date	: 2019. 10. 30.
//	 * @method	: getCreoFileList
//	 * @return	: List<DistributeFileData>
//	 * @param fileList
//	 * @param location
//	 * @param dis
//	 * @param part
//	 * @param epm
//	 * @return
//	 * @throws Exception 
//	 */
//	
//	private List<DistributeFileData> getCreoFileList(List<DistributeFileData> fileList, String location,
//			DistributeDocument dis, EPMDocument epm) throws Exception {
//		
//		List<ApplicationData> list = getDrawingPublishFile(epm);
//		//LOGGER.info("getDrawingPublishFile list 2. =" + list.size());
//		for(ApplicationData data : list){
//			DistributeFileData fileData = new DistributeFileData();
//			String fileLocation = location + "/" + epm.getNumber();
//			
//			fileData.setApp(data);
//			fileData.setEpm(epm);
//			fileData.setFileType("epm");
//			fileData.setFileLocation(fileLocation);
//			fileData.setMainNo(dis.getDistributeNumber());
//			fileData.setSubNo(epm.getNumber());
//			
//			fileList.add(fileData);
//		}
//		
//		
//		return fileList;
//	}
//	
//	private List<DistributeFileData> getERPToCPCFileList(List<DistributeFileData> fileList, String location,
//			DistributeDocument dis, PartData partData) throws Exception {
//		
//		WTPart part = (WTPart)CommonUtil.getObject(partData.getOid());
//		String partNumber = partData.getNumber();
//		String epmNumber = partData.getEpmNo();
//		String epmVer = partData.getEpmver();
//	
//		if(partData.getLinkState() != 5 ){
//			return fileList;
//		}
//		
//		String fileType = "part";
//		
//		EPMDocument epm = EpmHelper.manager.getEPMDocument(epmNumber,epmVer);
//		if(epm != null){
//			List<ApplicationData> list = getDrawingPublishFile(epm);
//			//LOGGER.info("getDrawingPublishFile list 2. =" + list.size());
//			for(ApplicationData data : list){
//				DistributeFileData fileData = new DistributeFileData();
//				String fileLocation = location + "/" + partNumber;
//				
//				fileData.setApp(data);
//				fileData.setPart(part);
//				fileData.setEpm(epm);
//				fileData.setFileType(fileType);
//				fileData.setFileLocation(fileLocation);
//				fileData.setMainNo(dis.getDistributeNumber());
//				fileData.setSubNo(partNumber);
//				fileData.setEpmNo(epmNumber);
//				
//				fileList.add(fileData);
//			}
//		}
//		
//		
//		return fileList;
//	}

	//CADCOMPONENT
	/**
	 * 
	 * @desc	: 3D CADCOMPONENT 인경우 step 파일 추출
	 * @author	: tsuam
	 * @date	: 2019. 10. 21.
	 * @method	: getParPublishFile
	 * @return	: List<ApplicationData>
	 * @param epm
	 * @return
	 */
	public List<ApplicationData> getPartPublishFile(EPMDocument epm){
    	
		List<ApplicationData> list = new ArrayList<ApplicationData>();
    	try{
    		boolean isPart = epm.getDocType().toString().equals("CADCOMPONENT") ?  true : false;
    		boolean isProe = epm.getAuthoringApplication().toString().equals("PROE") ?  true : false;
    		if(!(isPart && isProe)) return list;
			Representation representation = PublishUtils.getRepresentation(epm); 
			if(representation == null) return  list;
			
			representation = (Representation) ContentHelper.service.getContents(representation);
	        Vector contentList = ContentHelper.getContentList(representation);
	       // LOGGER.info("getDrawingPublishFile contentList =" + contentList.size());
	        for (int l = 0; l < contentList.size(); l++) {
	            ContentItem contentitem = (ContentItem) contentList.elementAt(l);
	            if( contentitem instanceof ApplicationData){
	            	ApplicationData app = (ApplicationData) contentitem;
	            	//String fileName= EpmUtil.getPublishFile(epm, drawAppData.getFileName());
	            	// LOGGER.info("getDrawingPublishFile contentList =" + app.getFileName());
	            	String role = app.getRole().toString();
	            	String fileName = app.getFileName();
	            	boolean isDrawingPublish = (role.equals("ADDITIONAL_FILES") && ( fileName.toLowerCase().indexOf(".stp") >= 0 ));
	            	//LOGGER.info("getDrawingPublishFile contentList =" + app.getFileName() + ",Role = " + role +",isDrawingPublish =" + isDrawingPublish);
	            	if(isDrawingPublish){
	            		list.add(app);
	            	}
	            }
	        }
	       
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	LOGGER.info("getDrawingPublishFile list =" + list.size());
    	return list;
    }
	
	public List<RevisionData> getRelatedEpmRevisionData(Map<String, Object> reqMap) throws Exception {

		List<RevisionData> list = new ArrayList<>();
		
		List<EpmData> epmList = getRelatedEpm(reqMap);
		
		for(EpmData data : epmList) {
			RevisionData rData = (RevisionData) data;
			list.add(rData);
		}
		
		return list;
	}
	
	/**
	 * @desc : CAD BOM 리스트 체크
	 * @author : sangylee
	 * @date : 2020. 1. 2.
	 * @method : checkCADBOMList
	 * @return : List<Map<String,Object>>
	 * @param reqMap
	 * @throws Exception
	 */
	public List<CADBomData> checkCADBOMList(Map<String, Object> reqMap) throws Exception{
		
		List<CADBomData> list = new ArrayList<>();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));

		EPMDocument epm = (EPMDocument) CommonUtil.getObject(oid);
		
		EpmHelper.manager.checkCADChildrenList(epm, null, list);
		
		return list;
	}
	
	/**
	 * @desc : children 리스트 가져오기
	 * @author : sangylee
	 * @date : 2020. 1. 2.
	 * @method : getCADToBOMList
	 * @return : void
	 * @param epm
	 * @param list
	 * @throws Exception
	 */
	public void checkCADChildrenList(EPMDocument epm, Map<String, Object> mapEPM, List<CADBomData> list) throws Exception{
		
		if(mapEPM == null) {
			mapEPM = new HashMap<>();
		}
		
		List<EPMDocument> childrenList = getEPMChild(epm);
		
		if(!mapEPM.containsKey(epm.getNumber())) {
			
			CADBomData data = new CADBomData();
			
			data.setOid(CommonUtil.getOIDString(epm));
			data.setName(epm.getName());
			data.setNumber(epm.getNumber());
			data.setIcon(BasicTemplateProcessor.getObjectIconImgTag(epm));
			String version = epm.getVersionIdentifier().getSeries().getValue().trim();
			String iteration = epm.getIterationIdentifier().getSeries().getValue();
			String rev = version + "." + iteration;
			data.setRev(rev);
			String state = StringUtil.checkNull(epm.getLifeCycleState().toString());
			String stateName = StringUtil.checkNull(epm.getLifeCycleState().getDisplay(MessageUtil.getLocale()));
			data.setState(state);
			data.setStateName(stateName);
			
			String partNo = IBAUtil.getAttrValue(epm, "PART_NO");

			
			//Part_No 없는 경우
			if(partNo.length() > 0) {
				data.setPartNo(partNo);
				
				//Part_NO와 epm NO 다른 경우
				boolean isPartNOCheck = isPartNOCheck(epm.getNumber(), partNo);
				if(isPartNOCheck) {
					data.setPartNoCheck("동일");
				} else {
					data.setPartNoCheck("다름");
				}
				
				//WTPart가 없는 경우
				WTPart part = PartHelper.manager.getPart(partNo);
				if(part != null) {
					data.setIsPart("부품 있음");
				} else {
					data.setIsPart("부품 없음");
				}
			} else {
				data.setPartNo("부품 번호 없음");
				data.setPartNoCheck("부품 번호 없음");
				data.setIsPart("부품 번호 없음");
			}
			
			//부품 연결 여부
			WTPart part = PartHelper.manager.getWTPart(epm);
			if(part == null) {
				data.setLinkedPart(MessageUtil.getMessage("없음"));
			} else {
				data.setLinkedPart(part.getNumber());
				data.setPartOid(CommonUtil.getOIDString(part));
			}
			
			list.add(data);
			
			for(EPMDocument epmChild : childrenList){
				// 재귀 호출
				checkCADChildrenList(epmChild, mapEPM, list);
			}
		}
		
		mapEPM.put(epm.getNumber(), epm);
	}

	public void getCADChildrenList(EPMDocument epm, Map<String, Object> mapEPM, List<EPMDocument> list) throws Exception{
		
		List<EPMDocument> childrenList = getEPMChild(epm);
		
		if(!mapEPM.containsKey(epm.getNumber())) {
			
			list.add(epm);
			
			for(EPMDocument epmChild : childrenList){
				// 재귀 호출
				getCADChildrenList(epmChild, mapEPM, list);
			}
		}
		
		mapEPM.put(epm.getNumber(), epm);
	}

	/**
	 * 
	 * @desc	: 자식 리스트
	 * @author	: tsuam
	 * @date	: 2019. 11. 5.
	 * @method	: getEPMChild
	 * @return	: List<EPMDocument>
	 * @param epm
	 * @return
	 * @throws Exception
	 */
	public List<EPMDocument> getEPMChild(EPMDocument epm)throws Exception {
		
		
		List<EPMDocument> list = new ArrayList<EPMDocument>();
		QuerySpec query = new QuerySpec();
		SearchCondition sc = null;
		int idx = query.addClassList(EPMMemberLink.class, true);
		/*
		int idx_master = query.addClassList(EPMDocumentMaster.class, false);
		int idx_epm = query.addClassList(EPMDocument.class, true);
		
		sc = new SearchCondition(new ClassAttribute(EPMMemberLink.class, "roleBObjectRef.key.id"), "=",
                new ClassAttribute(EPMDocumentMaster.class, "thePersistInfo.theObjectIdentifier.id"));
        query.appendWhere(sc, new int[] { idx, idx_master });
        
        query.appendAnd();
        sc = new SearchCondition(new ClassAttribute(EPMDocument.class, EPMDocument.MASTER_REFERENCE+".key.id"), "=",
                new ClassAttribute(EPMDocumentMaster.class, "thePersistInfo.theObjectIdentifier.id"));
        query.appendWhere(sc, new int[] { idx_epm, idx_master });
        
        //최신 Iteration
        query.appendAnd();
		query.appendWhere(VersionControlHelper.getSearchCondition(EPMDocument.class, true), new int[]{idx_epm});
		
		//최신 Version
		SearchUtil.addLastVersionCondition(query, EPMDocument.class, idx_epm);
		
		query.appendAnd();
		
		
		query.appendOrderBy(new OrderBy(new ClassAttribute(EPMDocument.class, EPMDocument.NUMBER), false), new int[] { idx_epm });
		*/
		sc = new SearchCondition(EPMMemberLink.class, "roleAObjectRef.key.id", SearchCondition.EQUAL ,CommonUtil.getOIDLongValue(epm));
		query.appendWhere(sc, new int[] { idx });
		QueryResult rt = PersistenceHelper.manager.find(query);
		
		while(rt.hasMoreElements()){
			Object[] obj = (Object[])rt.nextElement();
			EPMMemberLink link = (EPMMemberLink)obj[0];
			EPMDocumentMaster master = (EPMDocumentMaster)link.getRoleBObject();
			EPMDocument epmChild = getEPMDocument(master.getNumber());
			if(link.isSuppressed()){
				//SUPPRESSED_EXCEPTION
				continue;
			}
			
			String number = epmChild.getNumber();
			list.add(epmChild);
			
			
		}
		
		return list;
				
	}
	
	public List<Object[]> getEPMChildObject(EPMDocument epm)throws Exception {
		
		
		List<Object[]> list = new ArrayList<Object[]>();
		QuerySpec query = new QuerySpec();
		SearchCondition sc = null;
		int idx = query.addClassList(EPMMemberLink.class, true);
		/*
		int idx_master = query.addClassList(EPMDocumentMaster.class, false);
		int idx_epm = query.addClassList(EPMDocument.class, true);
		
		sc = new SearchCondition(new ClassAttribute(EPMMemberLink.class, "roleBObjectRef.key.id"), "=",
                new ClassAttribute(EPMDocumentMaster.class, "thePersistInfo.theObjectIdentifier.id"));
        query.appendWhere(sc, new int[] { idx, idx_master });
        
        query.appendAnd();
        sc = new SearchCondition(new ClassAttribute(EPMDocument.class, EPMDocument.MASTER_REFERENCE+".key.id"), "=",
                new ClassAttribute(EPMDocumentMaster.class, "thePersistInfo.theObjectIdentifier.id"));
        query.appendWhere(sc, new int[] { idx_epm, idx_master });
        
        //최신 Iteration
        query.appendAnd();
		query.appendWhere(VersionControlHelper.getSearchCondition(EPMDocument.class, true), new int[]{idx_epm});
		
		//최신 Version
		SearchUtil.addLastVersionCondition(query, EPMDocument.class, idx_epm);
		
		query.appendAnd();
		
		
		query.appendOrderBy(new OrderBy(new ClassAttribute(EPMDocument.class, EPMDocument.NUMBER), false), new int[] { idx_epm });
		*/
		
		sc = new SearchCondition(EPMMemberLink.class, "roleAObjectRef.key.id", SearchCondition.EQUAL ,CommonUtil.getOIDLongValue(epm));
		query.appendWhere(sc, new int[] { idx });
		QueryResult rt = PersistenceHelper.manager.find(query);
		
		while(rt.hasMoreElements()){
			Object[] obj = (Object[])rt.nextElement();
		
			list.add(obj);
		}
		
		return list;
				
	}
	
	public boolean isPartNOCheck(String epmNumber ,String part_no){
		
		boolean isPartNoCheck = false;
		String epmNumberTemp = epmNumber.substring(0, epmNumber.lastIndexOf("."));
		
		if(epmNumberTemp.equals(part_no) ){
			isPartNoCheck = true;
		}
		
		return isPartNoCheck;
	}
	
	/**
	 * 
	 * @desc	:  최신 EPMDocument wrk-p 제외
	 * @author	: tsuam
	 * @date	: 2020. 1. 21.
	 * @method	: getEPMDocument
	 * @return	: EPMDocument
	 * @param number
	 * @return
	 */
	public EPMDocument getEPMDocument(String number){
		EPMDocument epm = null;
		try{
			QuerySpec qs = new QuerySpec();
			int idx = qs.addClassList(EPMDocument.class, true);
			qs.appendWhere(VersionControlHelper.getSearchCondition(EPMDocument.class, true), new int[]{idx});
			
			qs.appendAnd();
			SearchCondition sc = new SearchCondition(EPMDocument.class, EPMDocument.NUMBER, SearchCondition.EQUAL ,number);
			qs.appendWhere(sc, new int[] { idx });
			
			qs.appendAnd();
			
			qs.appendWhere(new SearchCondition(EPMDocument.class, EPMDocument.CHECKOUT_INFO +
			 ".state", SearchCondition.NOT_EQUAL, "wrk-p", false), new int[] { idx });
			
			qs.appendOrderBy(new OrderBy(new ClassAttribute(EPMDocument.class, WTAttributeNameIfc.ID_NAME), true), new int[] { idx });
			
			//LOGGER.info(qs.toString());
			QueryResult rt = PersistenceHelper.manager.find(qs);
			
			if(rt.hasMoreElements()){
				Object[] obj = (Object[])rt.nextElement();
				epm = (EPMDocument)obj[0];
				
				LOGGER.info(epm +","+ epm.getNumber() +","+ epm.getCheckoutInfo().toString() +","+ epm.getVersionIdentifier().getValue()+","+epm.getIterationIdentifier().getSeries().getValue());
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return epm;
	}
	
	/**
	 * 
	 * @desc	: 버전별 최신 도면
	 * @author	: tsuam
	 * @date	: 2020. 1. 12.
	 * @method	: getEPMDocument
	 * @return	: EPMDocument
	 * @param number
	 * @param version
	 * @return
	 * @throws Exception
	 */
	public EPMDocument getEPMDocument(String number, String version) throws Exception {
		QuerySpec qs = new QuerySpec(EPMDocument.class);

        qs.appendWhere(VersionControlHelper.getSearchCondition(EPMDocument.class, true), new int[] { 0 });
        qs.appendAnd();
        qs.appendWhere(new SearchCondition(EPMDocument.class, "master>number", "=", number), new int[] { 0 });
        qs.appendAnd();
        qs.appendWhere(new SearchCondition(EPMDocument.class, "versionInfo.identifier.versionId", "=", version), new int[] { 0 });
        qs.appendAnd();
        qs.appendWhere(new SearchCondition(EPMDocument.class, EPMDocument.CHECKOUT_INFO +
   			 ".state", SearchCondition.NOT_EQUAL, "wrk-p", false), new int[] { 0 });
        
        QueryResult qr = PersistenceHelper.manager.find(qs);
        if ( qr.hasMoreElements() ) {
            return (EPMDocument) qr.nextElement();
        }

        return null;
	}

	public List<EpmPartStateData> getEpmPartStateList(Map<String, Object> reqMap) throws Exception {
		
		List<EpmPartStateData> list = new ArrayList<>();
		
		QuerySpec query = getEpmPartStateListQuery(reqMap);
		
		QueryResult result = PersistenceHelper.manager.find((StatementSpec)query);
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			EPMDocument epm = (EPMDocument) obj[0];
			WTPart part = (WTPart) obj[1];
			
			EpmPartStateData data = new EpmPartStateData(epm, part);
			
			list.add(data);
		}
		
		return list;
	}
	
	public Map<String, Object> getEpmPartStateScrollList(Map<String, Object> reqMap) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
		
		List<EpmPartStateData> list = new ArrayList<>();
		
		int start = (Integer) reqMap.get("start");
		int count = (Integer) reqMap.get("count");
		String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));
    	
		PagingQueryResult result = null;
		
		if (sessionId.length() > 0) {
			result = PagingSessionHelper.fetchPagingSession(start, count, Long.valueOf(sessionId));
		} else {
			QuerySpec query = getEpmPartStateListQuery(reqMap);
			
			result = PageQueryBroker.openPagingSession(start, count, query, true);
		}
		
		int totalSize = result.getTotalSize();
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			EPMDocument epm = (EPMDocument) obj[0];
			WTPart part = (WTPart) obj[1];
			
			EpmPartStateData data = new EpmPartStateData(epm, part);
			
			list.add(data);
		}
		
		map.put("list", list);
		map.put("totalSize", totalSize);
		map.put("sessionId", result.getSessionId() == 0 ? "" : result.getSessionId());
		
		//webix
		map.put("data", list);
		map.put("total_count", totalSize);
		map.put("pos", start);
		
		return map;
	}
	
	public QuerySpec getEpmPartStateListQuery(Map<String, Object> reqMap) throws Exception {
		
		String epmNumber = StringUtil.checkNull((String) reqMap.get("epmNumber"));
		String epmName = StringUtil.checkNull((String) reqMap.get("epmName"));
		List<String> epmState = StringUtil.checkReplaceArray(reqMap.get("epmState"));
		List<String> epmCreator = StringUtil.checkReplaceArray(reqMap.get("epmCreator"));
		
		String partNumber = StringUtil.checkNull((String) reqMap.get("partNumber"));
		String partName = StringUtil.checkNull((String) reqMap.get("partName"));
		List<String> partState = StringUtil.checkReplaceArray(reqMap.get("partState"));
		List<String> partCreator = StringUtil.checkReplaceArray(reqMap.get("partCreator"));
		
		String difference = StringUtil.checkNull((String) reqMap.get("difference"));
		
		QuerySpec query = new QuerySpec();
		int idx_epm = query.addClassList(EPMDocument.class, true);
		int idx_buildRule = query.addClassList(EPMBuildRule.class, false);
		int idx_part = query.addClassList(WTPart.class, true);
		
		SearchCondition sc = null;
		
		sc = new SearchCondition(
				EPMDocument.class, "iterationInfo.branchId", EPMBuildRule.class, "roleAObjectRef.key.branchId");
		sc.setOuterJoin(0);
		
		query.appendWhere(sc, new int[] {idx_epm, idx_buildRule});
		
		if(query.getConditionCount() > 0) {
			query.appendAnd(); 
		}
		
		sc = new SearchCondition(
				EPMBuildRule.class, "roleBObjectRef.key.branchId", WTPart.class, "iterationInfo.branchId");
		sc.setOuterJoin(0);
		
		query.appendWhere(sc, new int[] {idx_buildRule, idx_part});
		
		//다른 항목
		if(difference.length() > 0) {
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			
			if("version".equals(difference)) {
				sc = new SearchCondition(new ClassAttribute(EPMDocument.class, "versionInfo.identifier.versionId"), SearchCondition.NOT_EQUAL,
						new ClassAttribute(WTPart.class, "versionInfo.identifier.versionId"));
				query.appendWhere(sc, new int[] { idx_epm, idx_part });
			} else if("state".equals(difference)){
				sc = new SearchCondition(new ClassAttribute(EPMDocument.class, EPMDocument.LIFE_CYCLE_STATE), SearchCondition.NOT_EQUAL,
						new ClassAttribute(WTPart.class, WTPart.LIFE_CYCLE_STATE));
				query.appendWhere(sc, new int[] { idx_epm, idx_part });
			}
		}
		
		//최신 이터레이션
		if(query.getConditionCount() > 0) {
			query.appendAnd(); 
		}
    	query.appendWhere(VersionControlHelper.getSearchCondition(EPMDocument.class, true), new int[]{idx_epm});
    	
    	if(query.getConditionCount() > 0) {
			query.appendAnd(); 
		}
    	query.appendWhere(VersionControlHelper.getSearchCondition(WTPart.class, true), new int[]{idx_part});
    	
    	//최신 버전
    	SearchUtil.addLastVersionCondition(query, WTPart.class, idx_part);
    	
		//도면 번호
		if(epmNumber.length() > 0) {
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(EPMDocument.class, EPMDocument.NUMBER, SearchCondition.LIKE, "%" + epmNumber + "%", false);
			query.appendWhere(sc, new int[] { idx_epm });
		}
		
		//도면 명
		if(epmName.length() > 0) {
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(EPMDocument.class, EPMDocument.NAME, SearchCondition.LIKE, "%" + epmName + "%", false);
			query.appendWhere(sc, new int[] { idx_epm });
		}
		
		//도면 작성자
		if (epmCreator.size() > 0) {
			List<Long> userOidLongValueList = new ArrayList<>();
			
			for(String pp : epmCreator) {
				People people = (People) CommonUtil.getObject(pp);
				WTUser user = people.getUser();
				
				userOidLongValueList.add(CommonUtil.getOIDLongValue(user));
			}
			
			if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(EPMDocument.class, EPMDocument.CREATOR + ".key.id"), SearchCondition.IN, new ArrayExpression(userOidLongValueList.toArray()));
			query.appendWhere(sc, new int[] { idx_epm });
		}
				
		//도면 상태
		if (epmState.size() > 0) {
			if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(EPMDocument.class, EPMDocument.LIFE_CYCLE_STATE), SearchCondition.IN, new ArrayExpression(epmState.toArray()));
			query.appendWhere(sc, new int[] { idx_epm });
		}
		
		//부품 번호
		if(partNumber.length() > 0) {
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(WTPart.class, WTPart.NUMBER, SearchCondition.LIKE, "%" + partNumber + "%", false);
			query.appendWhere(sc, new int[] { idx_part });
		}
		
		//부품 명
		if(partName.length() > 0) {
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(WTPart.class, WTPart.NAME, SearchCondition.LIKE, "%" + partName + "%", false);
			query.appendWhere(sc, new int[] { idx_part });
		}
		
		//부품 작성자
		if (partCreator.size() > 0) {
			List<Long> userOidLongValueList = new ArrayList<>();
			
			for(String pp : partCreator) {
				People people = (People) CommonUtil.getObject(pp);
				WTUser user = people.getUser();
				
				userOidLongValueList.add(CommonUtil.getOIDLongValue(user));
			}
			
			if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(WTPart.class, WTPart.CREATOR + ".key.id"), SearchCondition.IN, new ArrayExpression(userOidLongValueList.toArray()));
			query.appendWhere(sc, new int[] { idx_part });
		}
				
		//부품 상태
		if (partState.size() > 0) {
			if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(WTPart.class, WTPart.LIFE_CYCLE_STATE), SearchCondition.IN, new ArrayExpression(partState.toArray()));
			query.appendWhere(sc, new int[] { idx_part });
		}
		
    	//소팅
		query.appendOrderBy(new OrderBy(new ClassAttribute(EPMDocument.class, EPMDocument.MODIFY_TIMESTAMP), true), new int[] { idx_epm });
    	
    	return query;
	}
	
	/**
	 * 
	 * @desc	: 배포 대상 부품 기준으로 도면 리스트
	 * @author	: tsuam
	 * @date	: 2020. 2. 10.
	 * @method	: getPartToDrawingList
	 * @return	: List<EPMDocument>
	 * @param part
	 * @return
	 * @throws Exception
	 */
	public List<EPMDocument> getPartToDrawingList(WTPart part,boolean isDrawing) throws Exception{
		// CAD 정합성 체크 
		String partNumber = part.getNumber();
		EPMDocument epm = EpmHelper.manager.getEPMDocument(part);
		String cadType = "";
		boolean isWGM = false;
		boolean isControl = PartUtil.isContorlPart(part.getNumber());
		
		
		//CADASSEMBLY ,
		if(epm != null){
			isWGM = EpmUtil.isWGM(epm);
			cadType = epm.getDocType().toString();
		}
		List<EPMDocument> epmList;
		if(isWGM && cadType.equals("CADASSEMBLY")){
			
			epmList = EpmHelper.manager.getDrawingList(partNumber,true,isDrawing);
		}else{
			epmList = EpmHelper.manager.getDrawingList(partNumber,false,isDrawing);
		}
		
		return epmList;
	}

	/**
	 * @desc : 드로잉 .drw, .prt 리스트
	 * @author : sangylee
	 * @date : 2020. 2. 12.
	 * @method : getPartXEPMList
	 * @return : List<EPMDocument>
	 * @param number
	 * @throws Exception
	 */
	public List<EPMDocument> getPartXEPMList(String number) throws Exception {
		
		List<EPMDocument> list = new ArrayList<EPMDocument>();
		QuerySpec query = new QuerySpec();
		SearchCondition sc = null;
		int idx = query.addClassList(EPMDocumentMaster.class, true);
		
		/*sc = new SearchCondition(EPMDocumentMaster.class, EPMDocumentMaster.DOC_TYPE, SearchCondition.EQUAL, "CADDRAWING", false);
		query.appendWhere(sc, new int[] { idx });
		
		query.appendAnd();*/
		
		sc = new SearchCondition(EPMDocumentMaster.class, EPMDocumentMaster.NUMBER, SearchCondition.LIKE, number + "X%", false);
		query.appendWhere(sc, new int[] { idx });
		
		LOGGER.info(query.toString());
		QueryResult rt =PersistenceHelper.manager.find(query);
		
		while(rt.hasMoreElements()){
			Object[] obj = (Object[])rt.nextElement();
			EPMDocumentMaster master = (EPMDocumentMaster)obj[0];
			EPMDocument epm = getEPMDocument(master.getNumber());
			LOGGER.info("master =" + master.getNumber() +",epm = " + epm);
			list.add(epm);
		}
		
		return list;
	}
	
	public List<EPMDocument> getNonStepPublishList() throws Exception{
		
		List<EPMDocument> list = null;
		return list;
	}
	
	public List<EPMDocument> getNonPublishList(String cadType,String product) throws Exception{
		MethodContext methodcontext = null;
		WTConnection wtconnection = null;

        PreparedStatement st = null;
        ResultSet rs = null;
        List<EPMDocument> list = new ArrayList<EPMDocument>();
        try {
			methodcontext = MethodContext.getContext();
			wtconnection = (WTConnection) methodcontext.getConnection();
			Connection con = wtconnection.getConnection();
			
			StringBuffer sql = new StringBuffer();
			
			sql.append("select T0.*,T1.ida2a2 from ");
			sql.append("(");
			sql.append("select A0.* ");
			sql.append("from view_last_epmdocument A0,derivedimage B0 ");
			sql.append("where a0.partoid= B0.ida3a6(+) and B0.ida2a2 is null ");
			sql.append("and A0.STATECHECKOUTINFO='c/i' ");
			
			if(cadType.length() > 0){
				sql.append("and a0.doctype='"+cadType+"'"); //CADCOMPONENT CADASSEMBLY ,CADDRAWING
			}
			
			if(product.length()>0){
				sql.append("and a0.CLASSNAMEKEYCONTAINERREFEREN='"+product+"' ");
			}else{
				sql.append("and a0.CLASSNAMEKEYCONTAINERREFEREN in ('wt.pdmlink.PDMLinkProduct','wt.inf.library.WTLibrary') ");
			}
			
			sql.append("and authoringapplication='PROE'");
			
			 //wt.pdmlink.PDMLinkProduct wt.inf.library.WTLibrary
			sql.append(") T0,derivedimage T1 ");
			sql.append("where T0.partoid =T1.ida3therepresentablereferenc(+) ");
			sql.append("and T1.ida2a2 is null ");
			
			LOGGER.info("getNonPublishList :" + sql.toString());
			st = con.prepareStatement(sql.toString());
			
			rs = st.executeQuery();
			
			while(rs.next()){
				long longOid = rs.getLong("partoid");
				String oid = "wt.epm.EPMDocument:"+longOid;
				EPMDocument epm = (EPMDocument)CommonUtil.getObject(oid);
				
				list.add(epm);
			}
			
        } catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
            if ( rs != null ) {
                rs.close();
            }
            if ( st != null ) {
                st.close();
            }
			if (DBProperties.FREE_CONNECTION_IMMEDIATE
					&& !wtconnection.isTransactionActive()) {
				MethodContext.getContext().freeConnection();
			}
		}
		
		return list;
	}
	
	/**
	 * @desc : 과련 프로젝트 가져오기
	 * @author : shkim
	 * @date : 2020. 2. 12.
	 * @method : getRelatedProjectList
	 * @return : List<ProjectData>
	 * @param epm
	 * @throws Exception
	 */
	public List<ProjectData> getRelatedProjectList(EPMDocument epm) throws Exception {
		List<ProjectData> list = new ArrayList<>();
		
		EPMDocumentMaster master = (EPMDocumentMaster) epm.getMaster();
		
		QueryResult qr = PersistenceHelper.manager.navigate(master, EProjectMasteredLink.PROJECT_ROLE, EProjectMasteredLink.class);
		
		while(qr.hasMoreElements()) {
			EProject project = (EProject) qr.nextElement();
			ProjectData data = new ProjectData(project);
			list.add(data);
		}
		
		return list;
	}
	
	public static void main(String[] args) {
		
		try{
			String cadType = "CADCOMPONENT";
			EPMDocument epm = EpmHelper.manager.getCADTemplate(cadType);
			LOGGER.info(epm.getNumber());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
}
