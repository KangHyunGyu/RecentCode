package com.e3ps.doc.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.e3ps.admin.service.AdminHelper;
import com.e3ps.change.EChangeContents;
import com.e3ps.change.beans.EChangeContentsData;
import com.e3ps.common.bean.RevisionData;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.log4j.ObjectLogger;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.FolderUtil;
import com.e3ps.common.util.SearchUtil;
import com.e3ps.common.util.SequenceDao;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.common.util.WebUtil;
import com.e3ps.common.web.PageQueryBroker;
import com.e3ps.doc.DocCodeToValueDefinitionLink;
import com.e3ps.doc.DocCodeType;
import com.e3ps.doc.DocKey;
import com.e3ps.doc.DocLocation;
import com.e3ps.doc.DocProjectLink;
import com.e3ps.doc.DocValueDefinition;
import com.e3ps.admin.MasterACLWTUserLink;
import com.e3ps.doc.E3PSDocument;
import com.e3ps.doc.ETCDocumentContents;
import com.e3ps.doc.ETCDocumentContentsData;
import com.e3ps.doc.PDRDocumentContents;
import com.e3ps.doc.PDRDocumentContentsData;
import com.e3ps.doc.bean.DocData;
import com.e3ps.doc.bean.DocValueDefinitionData;
import com.e3ps.doc.bean.E3PSDocumentData;
import com.e3ps.org.People;
import com.e3ps.part.bean.PartData;
import com.e3ps.project.EOutput;
import com.e3ps.project.EProject;
import com.e3ps.project.ETask;
import com.e3ps.project.beans.ProjectData;

import wt.clients.folder.FolderTaskLogic;
import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentItem;
import wt.content.ContentRoleType;
import wt.doc.DocumentType;
import wt.doc.WTDocument;
import wt.doc.WTDocumentMaster;
import wt.epm.EPMDocument;
import wt.fc.PagingQueryResult;
import wt.fc.PagingSessionHelper;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.folder.Folder;
import wt.folder.IteratedFolderMemberLink;
import wt.inf.container.WTContainer;
import wt.inf.container.WTContainerRef;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.part.WTPartDescribeLink;
import wt.pds.StatementSpec;
import wt.query.ArrayExpression;
import wt.query.ClassAttribute;
import wt.query.CompoundQuerySpec;
import wt.query.ConstantExpression;
import wt.query.KeywordExpression;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.query.SetOperator;
import wt.query.SubSelectExpression;
import wt.services.ServiceFactory;
import wt.session.SessionHelper;
import wt.util.WTAttributeNameIfc;
import wt.vc.VersionControlHelper;

import org.joda.time.DateTime;

public class DocHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.DOC.getName());
	
	public static final DocService service = ServiceFactory.getService(DocService.class);
	public static final DocHelper manager = new DocHelper();
	
	public static final String ROOTLOCATION = "/Default";
	
	public List<E3PSDocumentData> getDocList(Map<String, Object> reqMap) throws Exception {
		
		List<E3PSDocumentData> list = new ArrayList<>();
		
		QuerySpec query = getDocListQuery(reqMap);
		
		QueryResult result = PersistenceHelper.manager.find((StatementSpec)query);
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			E3PSDocument doc = (E3PSDocument) obj[0];
			
			E3PSDocumentData data = new E3PSDocumentData(doc);
			
			list.add(data);
		}
		
		return list;
	}
	
//	public Map<String, Object> getDocScrollList(Map<String, Object> reqMap) throws Exception {
//		
//		Map<String, Object> map = new HashMap<>();
//		
//		List<E3PSDocumentData> list = new ArrayList<>();
//		
//		int page = (Integer) reqMap.get("page");
//		int rows = (Integer) reqMap.get("rows");
//		String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));
//		String moduleType = StringUtil.checkNull((String) reqMap.get("moduleType"));
//		PagingQueryResult result = null;
//		
//		if (sessionId.length() > 0) {
//			result = PagingSessionHelper.fetchPagingSession((page - 1) * rows, rows, Long.valueOf(sessionId));
//		} else {
//			QuerySpec query = getDocListQuery(reqMap);
//			result = PageQueryBroker.openPagingSession((page - 1) * rows, rows, query, true);
//		}
//		
//		int totalSize = result.getTotalSize();
//		
//		while(result.hasMoreElements()){
//			Object[] obj = (Object[]) result.nextElement();
//			E3PSDocument doc = (E3PSDocument) obj[0];
//			
//			E3PSDocumentData data = new E3PSDocumentData(doc);
//			data.primaryFile();
//			data.userDepartName();
//			if("multiApproval".equals(moduleType)) {
//				if(!"INWORK".equals(data.getState())) {
//					data.setSelect(false);
//				}
//			}else if("distribute".equals(moduleType)){
//				if(!"APPROVED".equals(data.getState())) {
//					data.setSelect(false);
//				}
//			}
//			 
//			list.add(data);
//		}
//		
//		map.put("list", list);
//		map.put("totalSize", totalSize);
//		map.put("sessionId", result.getSessionId() == 0 ? "" : result.getSessionId());
//		
//		return map;
//	}
	
	public Map<String, Object> getDocScrollList(Map<String, Object> reqMap) throws Exception {

		Map<String, Object> map = new HashMap<>();

		List<DocData> list = new ArrayList<>();

		int page = (Integer) reqMap.get("page");
		int rows = (Integer) reqMap.get("rows");
		
		System.out.println("rows :: " + rows);
		
		String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));
		System.out.println("sessionId :: " + sessionId);
		PagingQueryResult result = null;

		if (sessionId.length() > 0) {
			result = PagingSessionHelper.fetchPagingSession((page - 1) * rows, rows, Long.valueOf(sessionId));
		} else {
			QuerySpec query = getDocListQuery(reqMap);

			result = PageQueryBroker.openPagingSession((page - 1) * rows, rows, query, true);
		}

		int totalSize = result.getTotalSize();

		while (result.hasMoreElements()) {

			Object[] obj = (Object[]) result.nextElement();
			E3PSDocument doc = (E3PSDocument) obj[0];
			DocData data = new DocData(doc, false);

			list.add(data);	
			
		}

		map.put("list", list);
		map.put("totalSize", totalSize);
		map.put("sessionId", result.getSessionId() == 0 ? "" : result.getSessionId());

		return map;
	}

	public List<DocData> getDocList2(Map<String, Object> reqMap) throws Exception {
		
		List<DocData> list = new ArrayList<>();
		
		int page = (Integer) reqMap.get("page");
		int rows = (Integer) reqMap.get("rows");
		
		QuerySpec query = getDocListQuery(reqMap);
    	
		PagingQueryResult result = PageQueryBroker.openPagingSession((page - 1) * rows, rows, query, true);
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			WTDocument doc = (WTDocument) obj[0];
			
			DocData data = new DocData(doc);
			
			list.add(data);
		}
		
		return list;
	}
	
	public Map<String, Object> getDocList3(Map<String, Object> reqMap) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
		
		List<DocData> list = new ArrayList<>();
		
		int page = (Integer) reqMap.get("page");
		int rows = (Integer) reqMap.get("rows");
		
		QuerySpec query = getDocListQuery(reqMap);
    	
		PagingQueryResult result = PageQueryBroker.openPagingSession((page - 1) * rows, rows, query, true);
		
		int totalSize = result.getTotalSize();
		
		int totalPage = 0;
		if(totalSize % rows == 0) {
			totalPage = totalSize / rows;
		} else {
			totalPage = totalSize / rows + 1;
		}
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			WTDocument doc = (WTDocument) obj[0];
			
			DocData data = new DocData(doc);
			
			list.add(data);
		}
		
		map.put("list", list);
		map.put("totalSize", totalSize);
		map.put("totalPage", totalPage);
		
		return map;
	}
	
	public QuerySpec getDocListQuery(Map<String, Object> hash) throws Exception {

		QuerySpec query = new QuerySpec();
		int idx = query.addClassList(E3PSDocument.class, true);

		try {

//			String foid = StringUtil.checkNull((String) hash.get("foid"));
			String container = StringUtil.checkNull((String) hash.get("container"));
			String location = StringUtil.checkReplaceStr((String) hash.get("location"), "/Default/Document");
			String version = StringUtil.checkNull((String) hash.get("version"));
			String number = StringUtil.checkNull((String) hash.get("number"));
			String name = StringUtil.checkNull((String) hash.get("name"));
			name = name.replace("[", "[[]");
			//String docAttribute = StringUtil.checkNull((String) hash.get("docAttribute"));			
			String predate = StringUtil.checkNull((String) hash.get("predate"));
			String postdate = StringUtil.checkNull((String) hash.get("postdate"));
			String predate_modify = StringUtil.checkNull((String) hash.get("predate_modify"));
			String postdate_modify = StringUtil.checkNull((String) hash.get("postdate_modify"));
			String likeSearch = StringUtil.checkNull((String) hash.get("likeSearch"));
			
			List<String> eChangeProjectNumber = StringUtil.checkReplaceArray(hash.get("eChangeProjectNumber"));
			String eChangeProjectName = StringUtil.checkNull((String) hash.get("eChangeProjectName"));
			
//			String pjtCode = StringUtil.checkNull((String) hash.get("pjtCode"));
			
			String sortValue = StringUtil.checkNull((String) hash.get("sortValue"));
//			String sortCheck = StringUtil.checkNull((String) hash.get("sortCheck"));
			boolean sortCheck = true;
			if (hash.get("sortCheck") != null) {
				sortCheck = (boolean) hash.get("sortCheck");
			}

			List<String> docType = StringUtil.checkReplaceArray(hash.get("docType"));
			List<String> creator = StringUtil.checkReplaceArray(hash.get("creator"));
			List<String> state = StringUtil.checkReplaceArray(hash.get("state"));
			List<String> relatedPart = StringUtil.checkReplaceArray(hash.get("relatedPart"));
			List<String> project_code = StringUtil.checkReplaceArray(hash.get("project_code"));
			List<String> modifier = StringUtil.checkReplaceArray(hash.get("modifier"));
			List<String> docAttribute = StringUtil.checkReplaceArray(hash.get("docAttribute"));
			
			List<String> realtedDoc = StringUtil.checkReplaceArray(hash.get("relatedDoc"));
			
			
//			String name = "";
//			Folder folder = null;
//			if (foid.length() > 0) {
//				folder = (Folder) rf.getReference(foid).getObject();
//				location = FolderHelper.getFolderPath(folder);
//				name = folder.getName();
//			} else {
//				folder = FolderTaskLogic.getFolder(location, WCUtil.getWTContainerRef());
//				foid = "";
//			}
			// 제품 및 Library
			if(container.length()>0) {
				WTContainer wtcontainer = WCUtil.getWTContainer(container);
				
				if (query.getConditionCount() > 0) {
					query.appendAnd();
				}
				query.appendWhere(new SearchCondition(E3PSDocument.class, E3PSDocument.CONTAINER_REFERENCE + ".key.id", SearchCondition.EQUAL,
						CommonUtil.getOIDLongValue(wtcontainer)), new int[] { idx });
			}
			
			if(likeSearch.length()>0) {
				// 단품 문서번호(공통단 자동완성)
				if (number.length() > 0) {
					if (query.getConditionCount() > 0) {
						query.appendAnd();
					}
					query.appendWhere(new SearchCondition(E3PSDocument.class, E3PSDocument.NUMBER, SearchCondition.LIKE, "%" + number + "%", false), new int[] { idx });
				}
			} else {	
				// 문서번호(문서 검색화면)
				if (realtedDoc.size() > 0) {
					if (query.getConditionCount() > 0) {
						query.appendAnd();
					}
					query.appendWhere(new SearchCondition(new ClassAttribute(E3PSDocument.class, E3PSDocument.NUMBER),
							SearchCondition.IN, new ArrayExpression(realtedDoc.toArray())),
							new int[] { idx });
				}
			}

			// 문서명
			if (name.length() > 0) {
				if (query.getConditionCount() > 0) {
					query.appendAnd();
				}
				query.appendWhere(new SearchCondition(E3PSDocument.class, E3PSDocument.NAME, SearchCondition.LIKE,
						"%" + name + "%", false), new int[] { idx });
			}
			// 문서구분
			if (docType.size() > 0) {
				if(query.getConditionCount() > 0) {
					query.appendAnd();
				}
				query.appendWhere(new SearchCondition(
						new ClassAttribute(
								E3PSDocument.class, E3PSDocument.DOC_TYPE), 
						SearchCondition.IN, new ArrayExpression(docType.toArray())), new int[]{idx});
			}
			// 문서 유형
			if (docAttribute.size() > 0) {
				if(query.getConditionCount() > 0) {
					query.appendAnd();
				}
				query.appendWhere(new SearchCondition(
						new ClassAttribute(
								E3PSDocument.class, E3PSDocument.DOC_ATTRIBUTE), 
						SearchCondition.IN, new ArrayExpression(docAttribute.toArray())), new int[]{idx});
			}

			// 등록일
			if (predate.length() > 0) {
				if (query.getConditionCount() > 0) {
					query.appendAnd();
				}
				query.appendWhere(new SearchCondition(E3PSDocument.class, E3PSDocument.CREATE_TIMESTAMP,
						SearchCondition.GREATER_THAN, DateUtil.convertStartDate(predate)), new int[] { idx });
			}
			if (postdate.length() > 0) {
				if (query.getConditionCount() > 0) {
					query.appendAnd();
				}
				query.appendWhere(new SearchCondition(E3PSDocument.class, E3PSDocument.CREATE_TIMESTAMP,
						SearchCondition.LESS_THAN, DateUtil.convertEndDate(postdate)), new int[] { idx });
			}

			// 수정일
			if (predate_modify.length() > 0) {
				if (query.getConditionCount() > 0) {
					query.appendAnd();
				}
				query.appendWhere(new SearchCondition(E3PSDocument.class, E3PSDocument.MODIFY_TIMESTAMP,
						SearchCondition.GREATER_THAN, DateUtil.convertStartDate(predate_modify)), new int[] { idx });
			}
			if (postdate_modify.length() > 0) {
				if (query.getConditionCount() > 0) {
					query.appendAnd();
				}
				query.appendWhere(new SearchCondition(E3PSDocument.class, E3PSDocument.MODIFY_TIMESTAMP,
						SearchCondition.LESS_THAN, DateUtil.convertEndDate(postdate_modify)), new int[] { idx });
			}

			// 등록자
			if (creator.size() > 0) {
				List<Long> userOidLongValueList = new ArrayList<>();

				for (String pp : creator) {
					People people = (People) CommonUtil.getObject(pp);
					WTUser user = people.getUser();

					userOidLongValueList.add(CommonUtil.getOIDLongValue(user));
				}

				if (query.getConditionCount() > 0) {
					query.appendAnd();
				}
				SearchCondition sc = new SearchCondition(
						new ClassAttribute(E3PSDocument.class, E3PSDocument.CREATOR + "." + "key.id"), SearchCondition.IN,
						new ArrayExpression(userOidLongValueList.toArray()));
				query.appendWhere(sc, new int[] { idx });
			}

			// 수정자
			if (modifier.size() > 0) {
				List<Long> userOidLongValueList = new ArrayList<Long>();

				for (String pp : modifier) {
					People people = (People) CommonUtil.getObject(pp);
					WTUser user = people.getUser();

					userOidLongValueList.add(CommonUtil.getOIDLongValue(user));
				}

				if (query.getConditionCount() > 0) {
					query.appendAnd();
				}
				SearchCondition sc = new SearchCondition(
						new ClassAttribute(E3PSDocument.class, E3PSDocument.MODIFIER + "." + "key.id"), SearchCondition.IN,
						new ArrayExpression(userOidLongValueList.toArray()));
				query.appendWhere(sc, new int[] { idx });
			}

			// 상태
			if (state.size() > 0) {
				if (query.getConditionCount() > 0) {
					query.appendAnd();
				}
				SearchCondition sc = new SearchCondition(
						new ClassAttribute(E3PSDocument.class, E3PSDocument.LIFE_CYCLE_STATE), SearchCondition.IN,
						new ArrayExpression(state.toArray()));
				query.appendWhere(sc, new int[] { idx });
			}

			// 문서분류 , folder search
			
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
				query.appendWhere(new SearchCondition(fca, "=", new ClassAttribute(E3PSDocument.class, "iterationInfo.branchId")), new int[] { f_idx, idx });
				query.appendAnd();

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
				
				SearchCondition sc = new SearchCondition(new ClassAttribute(IteratedFolderMemberLink.class, "roleAObjectRef.key.id"), SearchCondition.IN, new ArrayExpression(folderOidLongValueList.toArray()));
				query.appendWhere(sc, new int[] { f_idx });
			}
			
//			if (location.length() > 0) {
//				int l = location.indexOf(ROOTLOCATION);
//				if (l >= 0) {
//					if (query.getConditionCount() > 0) {
//						query.appendAnd();
//					}
//					location = location.substring((l + ROOTLOCATION.length()));
//					// Folder Search
//					int folder_idx = query.addClassList(DocLocation.class, false);
//					query.appendWhere(new SearchCondition(DocLocation.class, DocLocation.DOC, E3PSDocument.class,
//							"thePersistInfo.theObjectIdentifier.id"), new int[] { folder_idx, idx });
//					query.appendAnd();
//					
//					query.appendOpenParen();
//					query.appendWhere(
//							new SearchCondition(DocLocation.class, "loc", SearchCondition.EQUAL, location ),
//							new int[] { folder_idx });
//					query.appendOr();
//					query.appendWhere(
//							new SearchCondition(DocLocation.class, "loc", SearchCondition.LIKE, location + "/%"),
//							new int[] { folder_idx });
//					query.appendCloseParen();
//				}
//			}

			// 관련 품목
			if (relatedPart.size() > 0) {
				setPartSubQuery(query, E3PSDocument.class, idx, relatedPart);
			}
			
			//관련 프로젝트
			if (project_code.size() > 0) {
				if (query.getConditionCount() > 0) {
					query.appendAnd();
				}
				//setProjectSubQuery(query, E3PSDocument.class, idx, project_code);
				//해당 프로젝트와 관련된 문서 번호 리스트를 가져온다.
				List<String> list = getReferencedDocByProject(project_code);
				if(list.size() <= 0)list = List.of("");
				ArrayExpression arryExpr = new ArrayExpression(list.toArray());
				query.appendWhere(new SearchCondition(new ClassAttribute(E3PSDocument.class, E3PSDocument.NUMBER ), SearchCondition.IN, arryExpr), new int[]{idx});
			}
			
			if(eChangeProjectNumber.size() > 0) {
				setRelatedDocumentProjectEchange(query, idx, eChangeProjectNumber, false);
			}
			
			if(eChangeProjectName.length() > 0) {
				List<String> temp = new ArrayList<String>();
				temp.add(eChangeProjectName);
				setRelatedDocumentProjectEchange(query, idx, temp, true);
			}

			// 최신 이터레이션
			if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
			query.appendWhere(VersionControlHelper.getSearchCondition(E3PSDocument.class, true), new int[] { idx });

			// 버전
			if (version.length() > 0) {
				if ("new".equals(version)) {
					// SearchUtil 내용 정리 필요
					SearchUtil.addLastVersionCondition(query, E3PSDocument.class, idx);
				}
			}

			// 소팅
			if (sortValue.length() > 0) {
				if (sortCheck) {
					query.appendOrderBy(new OrderBy(new ClassAttribute(E3PSDocument.class, sortValue), false),
							new int[] { idx });
				} else {
					query.appendOrderBy(new OrderBy(new ClassAttribute(E3PSDocument.class, sortValue), true),
							new int[] { idx });
				}
			} else {
				query.appendOrderBy(
						new OrderBy(new ClassAttribute(E3PSDocument.class, E3PSDocument.MODIFY_TIMESTAMP), true),
						new int[] { idx });
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		ObjectLogger.debug(query, "SearchQuery");
		return query;

	}
	
//	public QuerySpec getDocListQuery(Map<String, Object> reqMap) throws Exception {
//		
//		//소팅
//		Map<String, Object> sort = reqMap.get("sort") != null ? (Map<String, Object>) reqMap.get("sort") : new HashMap<>();
//		
//		//기본속성
//		String location = StringUtil.checkNull((String) reqMap.get("location"));
//		String version = StringUtil.checkNull((String) reqMap.get("version"));
//		String number = StringUtil.checkNull((String) reqMap.get("number"));
//		String name = StringUtil.checkNull((String) reqMap.get("name"));
//		String description = StringUtil.checkNull((String) reqMap.get("description"));
//		String predate = StringUtil.checkNull((String) reqMap.get("predate"));
//		String postdate = StringUtil.checkNull((String) reqMap.get("postdate"));
//		String predate_modify = StringUtil.checkNull((String) reqMap.get("predate_modify"));
//		String postdate_modify = StringUtil.checkNull((String) reqMap.get("postdate_modify"));
//		
//		List<String> creator = StringUtil.checkReplaceArray(reqMap.get("creator"));
//		List<String> state = StringUtil.checkReplaceArray(reqMap.get("state"));
//		List<String> relatedPart = StringUtil.checkReplaceArray(reqMap.get("relatedPart"));
//		List<String> relatedProject = StringUtil.checkReplaceArray(reqMap.get("relatedProject"));
//		List<String> docAttribute = StringUtil.checkReplaceArray(reqMap.get("docAttribute"));
//		
//		if(location.length() == 0) {
//			location = ROOTLOCATION;
//		}
//		
//		QuerySpec qs = new QuerySpec();
//		int idx = qs.addClassList(E3PSDocument.class, true);
//		qs.setAdvancedQueryEnabled(true);
//		
//		SearchCondition sc = null;
//		
//		//최신 이터레이션
//		if(qs.getConditionCount() > 0) {
//			qs.appendAnd(); 
//		}
//    	qs.appendWhere(VersionControlHelper.getSearchCondition(E3PSDocument.class, true), new int[]{idx});
//    	
//		//문서분류
//    	if (location.length() > 0) {
//			int l = location.indexOf(ROOTLOCATION);
//
//			if (l >= 0) {
//				if (qs.getConditionCount() > 0) {
//					qs.appendAnd();
//				}
//				location = location
//						.substring((l + ROOTLOCATION.length()));
//				// Folder Search
//				int folder_idx = qs.addClassList(DocLocation.class,
//						false);
//				qs.appendWhere(new SearchCondition(DocLocation.class,
//						DocLocation.DOC, E3PSDocument.class,
//						"thePersistInfo.theObjectIdentifier.id"),
//						new int[] { folder_idx, idx });
//				qs.appendAnd();
//
//				qs.appendOpenParen();
//				qs.appendWhere(
//						new SearchCondition(DocLocation.class, "loc", SearchCondition.EQUAL, location ),
//						new int[] { folder_idx });
//				qs.appendOr();
//				qs.appendWhere(
//						new SearchCondition(DocLocation.class, "loc", SearchCondition.LIKE, location + "/%"),
//						new int[] { folder_idx });
//				qs.appendCloseParen();
//			}
//
//		}
//    	
//    	// 문서 유형
//			if (docAttribute.size() > 0) {
//				if(qs.getConditionCount() > 0) {
//					qs.appendAnd();
//				}
//				qs.appendWhere(new SearchCondition(
//						new ClassAttribute(
//								E3PSDocument.class, E3PSDocument.DOC_ATTRIBUTE), 
//						SearchCondition.IN, new ArrayExpression(docAttribute.toArray())), new int[]{idx});
//			}
//    	
//    	//특정 객체에 대한 권한
//		//qs = AdminHelper.manager.getAuthQuerySpec(qs, "E3PSDocument", idx);
//    	
//    	/*
//    	Folder folder = FolderTaskLogic.getFolder(location, WCUtil.getWTContainerRef());
//    	if (folder != null) {
//    		if(qs.getConditionCount() > 0) {
//    			qs.appendAnd(); 
//    		}
//			int f_idx = qs.appendClassList(IteratedFolderMemberLink.class, false);
//			ClassAttribute fca = new ClassAttribute(IteratedFolderMemberLink.class, "roleBObjectRef.key.branchId");
//			SearchCondition fsc = new SearchCondition(fca, "=",
//					new ClassAttribute(WTDocument.class, "iterationInfo.branchId"));
//			fsc.setFromIndicies(new int[] { f_idx, idx }, 0);
//			fsc.setOuterJoin(0);
//			qs.appendWhere(fsc, new int[] { f_idx, idx });
//			qs.appendAnd();
//
//			qs.appendOpenParen();
//			long fid = folder.getPersistInfo().getObjectIdentifier().getId();
//			qs.appendWhere(
//					new SearchCondition(IteratedFolderMemberLink.class, "roleAObjectRef.key.id", "=", fid),
//					new int[] { f_idx });
//
//			ArrayList<Folder> subFolderList = new ArrayList<>(); 
//			FolderUtil.getSubFolderList(folder, subFolderList);
//			
//			for (Folder sub: subFolderList) {
//				qs.appendOr();
//				long sfid = CommonUtil.getOIDLongValue(sub);
//				qs.appendWhere(
//						new SearchCondition(IteratedFolderMemberLink.class, "roleAObjectRef.key.id", "=", sfid),
//						new int[] { f_idx });
//			}
//			qs.appendCloseParen();
//		}
//		*/
//    	
//		//버전
//		if(version.length() > 0) {
//			if("new".equals(version)) {
//				//SearchUtil 내용 정리 필요
//				SearchUtil.addLastVersionCondition(qs, E3PSDocument.class, idx);
//			}
//		}
//		
//		//문서번호
//		if(number.length() > 0) {
//			if(qs.getConditionCount() > 0) {
//				qs.appendAnd();
//			}
//			sc = new SearchCondition(E3PSDocument.class, E3PSDocument.NUMBER, SearchCondition.LIKE, "%" + number + "%", false);
//			qs.appendWhere(sc, new int[] { idx });
//		}
//		
//		//문서명
//		if(name.length() > 0) {
//			if(qs.getConditionCount() > 0) {
//				qs.appendAnd();
//			}
//			sc = new SearchCondition(E3PSDocument.class, E3PSDocument.NAME, SearchCondition.LIKE, "%" + name + "%", false);
//			qs.appendWhere(sc, new int[] { idx });
//		}
//
//		//설명
//		if(description.length() > 0) {
//			if(qs.getConditionCount() > 0) {
//				qs.appendAnd();
//			}
//			sc = new SearchCondition(E3PSDocument.class, E3PSDocument.DESCRIPTION, SearchCondition.LIKE, "%" + description + "%", false);
//			qs.appendWhere(sc, new int[] { idx });
//		}
//		
//		//작성자
//		if (creator.size() > 0) {
//			List<Long> userOidLongValueList = new ArrayList<>();
//			
//			for(String pp : creator) {
//				People people = (People) CommonUtil.getObject(pp);
//				WTUser user = people.getUser();
//				
//				userOidLongValueList.add(CommonUtil.getOIDLongValue(user));
//			}
//			
//			if (qs.getConditionCount() > 0) {
//				qs.appendAnd();
//			}
//			sc = new SearchCondition(new ClassAttribute(E3PSDocument.class, E3PSDocument.CREATOR + "." + "key.id"), SearchCondition.IN, new ArrayExpression(userOidLongValueList.toArray()));
//			qs.appendWhere(sc, new int[] { idx });
//		}
//				
//		//상태
//		if (state.size() > 0) {
//			if (qs.getConditionCount() > 0) {
//				qs.appendAnd();
//			}
//			sc = new SearchCondition(new ClassAttribute(E3PSDocument.class, E3PSDocument.LIFE_CYCLE_STATE), SearchCondition.IN, new ArrayExpression(state.toArray()));
//			qs.appendWhere(sc, new int[] { idx });
//		}
//
//		//등록일
//    	if(predate.length() > 0){
//    		if (qs.getConditionCount() > 0) {
//				qs.appendAnd();
//			}
//    		sc = new SearchCondition(E3PSDocument.class, E3PSDocument.CREATE_TIMESTAMP ,SearchCondition.GREATER_THAN,DateUtil.convertStartDate(predate));
//    		qs.appendWhere(sc, new int[] { idx });
//    	}
//    	if(postdate.length() > 0){
//    		if (qs.getConditionCount() > 0) {
//				qs.appendAnd();
//			}
//    		sc = new SearchCondition(E3PSDocument.class, E3PSDocument.CREATE_TIMESTAMP,SearchCondition.LESS_THAN,DateUtil.convertEndDate(postdate));
//    		qs.appendWhere(sc, new int[] { idx });
//    	}
//    	
//    	//수정일
//    	if(predate_modify.length() > 0){
//    		if (qs.getConditionCount() > 0) {
//				qs.appendAnd();
//			}
//    		sc = new SearchCondition(E3PSDocument.class, E3PSDocument.CREATE_TIMESTAMP ,SearchCondition.GREATER_THAN,DateUtil.convertStartDate(predate_modify));
//    		qs.appendWhere(sc, new int[] { idx });
//    	}
//    	if(postdate_modify.length() > 0){
//    		if (qs.getConditionCount() > 0) {
//				qs.appendAnd();
//			}
//    		sc = new SearchCondition(E3PSDocument.class, E3PSDocument.CREATE_TIMESTAMP,SearchCondition.LESS_THAN,DateUtil.convertEndDate(postdate_modify));
//    		qs.appendWhere(sc, new int[] { idx });
//    	}
//    	
//    	//E3PSDocument 추가 속성
//    	List<DocValueDefinitionData> docValueList = AdminHelper.manager.getDocValueList();
//    	for(DocValueDefinitionData data : docValueList) {
//    		String code = data.getCode();
//    		String value = StringUtil.checkNull((String) reqMap.get(code));
//    		String inputType = data.getInputType();
//    		
//    		if(value.length() > 0) {
//    			if("SELECT".equals(inputType)) {
//    				if(qs.getConditionCount() > 0) {
//        				qs.appendAnd();
//        			}
//        			sc = new SearchCondition(E3PSDocument.class, code, SearchCondition.EQUAL, value, false);
//        			qs.appendWhere(sc, new int[] { idx });
//    			} else if("TEXT".equals(inputType)) {
//					if(qs.getConditionCount() > 0) {
//						qs.appendAnd();
//					}
//					sc = new SearchCondition(E3PSDocument.class, code, SearchCondition.LIKE, "%"+value+"%");
//					qs.appendWhere(sc, new int[] { idx });
//    			} else if("DATE".equals(inputType)) {
//					if(qs.getConditionCount() > 0) {
//						qs.appendAnd();
//					}
//					sc = new SearchCondition(E3PSDocument.class, code, SearchCondition.EQUAL, DateUtil.convertDate(value));
//					qs.appendWhere(sc, new int[] { idx });
//    			}
//    		}
//    	}
//    	
//		// 관련 품목
//    	if (relatedPart.size() > 0) {
//    		setPartSubQuery(qs, E3PSDocument.class, idx, relatedPart);
//		}
//    	
//    	// 관련 프로젝트
//    	if (relatedProject.size() > 0) {
//    		int pjtIdx = qs.addClassList(EProject.class, false);
//    		int taskIdx = qs.addClassList(ETask.class, false);
//    		int outputIdx = qs.addClassList(EOutput.class, false);
//    		
//    		List<Long> projectOidLongValueList = new ArrayList<>();
//    		for(String oid : relatedProject) {
//    			projectOidLongValueList.add(CommonUtil.getOIDLongValue(oid));
//    		}
//    		
//    		if (qs.getConditionCount() > 0) {
//				qs.appendAnd();
//			}
//    		
//    		sc = new SearchCondition(new ClassAttribute(EOutput.class, EOutput.DOCUMENT_REFERENCE + ".key.id"), SearchCondition.EQUAL, new ClassAttribute(E3PSDocument.class, E3PSDocument.PERSIST_INFO + ".theObjectIdentifier.id"));
//    		qs.appendWhere(sc, new int[] { outputIdx, idx });
//    		
//    		if (qs.getConditionCount() > 0) {
//				qs.appendAnd();
//			}
//    		
//    		sc = new SearchCondition(new ClassAttribute(EOutput.class, EOutput.TASK_REFERENCE + ".key.id"), SearchCondition.EQUAL, new ClassAttribute(ETask.class, ETask.PERSIST_INFO + ".theObjectIdentifier.id"));
//    		qs.appendWhere(sc, new int[] { outputIdx, taskIdx });
//    		
//    		if (qs.getConditionCount() > 0) {
//				qs.appendAnd();
//			}
//    		
//    		sc = new SearchCondition(new ClassAttribute(ETask.class, ETask.PROJECT_REFERENCE + ".key.id"), SearchCondition.EQUAL, new ClassAttribute(EProject.class, EProject.PERSIST_INFO + ".theObjectIdentifier.id"));
//    		qs.appendWhere(sc, new int[] { taskIdx, pjtIdx });
//    		
//    		if (qs.getConditionCount() > 0) {
//				qs.appendAnd();
//			}
//    		
//    		sc = new SearchCondition(new ClassAttribute(EProject.class, EProject.PERSIST_INFO + ".theObjectIdentifier.id"), SearchCondition.IN, new ArrayExpression(projectOidLongValueList.toArray()));
//    		qs.appendWhere(sc, new int[] { pjtIdx });
//		}
//    	
//    	// 소팅
//		if (sort.size() > 0) {
//			String id = (String) sort.get("id");
//			String dir = (String) sort.get("dir");
//			
//			String sortValue = "";
//			if("number".equals(id)) {
//				sortValue = E3PSDocument.NUMBER;
//			} else if("name".equals(id)) {
//				sortValue = E3PSDocument.NAME;
//			} else if("stateName".equals(id)) {
//				sortValue = E3PSDocument.LIFE_CYCLE_STATE;
//			} else if("createDateFormat".equals(id)) {
//				sortValue = E3PSDocument.CREATE_TIMESTAMP;
//			} else if("modifyDateFormat".equals(id)) {
//				sortValue = E3PSDocument.MODIFY_TIMESTAMP;
//			}
//			
//			if ("asc".equals(dir)) {
//				qs.appendOrderBy(new OrderBy(new ClassAttribute(E3PSDocument.class, sortValue), false), new int[] { idx });
//			} else if("desc".equals(dir)) {
//				qs.appendOrderBy(new OrderBy(new ClassAttribute(E3PSDocument.class, sortValue), true), new int[] { idx });
//			}
//		} else {
//			qs.appendOrderBy(new OrderBy(new ClassAttribute(E3PSDocument.class, E3PSDocument.MODIFY_TIMESTAMP), true), new int[] { idx });
//		}
//    	return qs;
//	}

	/** 
	 * @desc	: 관련 품목 검색 SubQuery 추가
	 * @author	: mnyu
	 * @date	: 2019. 9. 5.
	 * @method	: setPartSubQuery
	 * @return	: void
	 * @param qs
	 * @param class1
	 * @param idx
	 * @param parts
	 * @throws Exception 
	 */
	private void setPartSubQuery(QuerySpec qs, Class cls, int idx, List<String> parts) throws Exception {
		QuerySpec subQs = searchPartSubQuery(parts);
		
		SubSelectExpression subfrom =  new SubSelectExpression(subQs);
        subfrom.setFromAlias(new String[]{"C0"}, 0);
        
        int sub_part_index = qs.appendFrom(subfrom);
        
        if(qs.getConditionCount() > 0)
        	qs.appendAnd();

        SearchCondition sc = new SearchCondition(new ClassAttribute(cls, "thePersistInfo.theObjectIdentifier.id"),"=",
        		new KeywordExpression(qs.getFromClause().getAliasAt(sub_part_index) + ".IDA3B5"));
        
        sc.setFromIndicies(new int[]{idx,sub_part_index},0);
        sc.setOuterJoin(0);
        qs.appendWhere(sc, new int[] { idx, sub_part_index });
	}
	
	/**
	 * @desc	: 품목 sub 쿼리 검색
	 * @author	: mnyu
	 * @date	: 2019. 9. 5.
	 * @method	: searchPartSubQuery
	 * @return	: QuerySpec
	 * @param parts
	 * @return
	 * @throws Exception 
	 */
	private QuerySpec searchPartSubQuery(List<String> parts) throws Exception {
		QuerySpec subQs = new QuerySpec();
		int part_idx = subQs.appendClassList(WTPart.class, false);
		int link_idx = subQs.appendClassList(WTPartDescribeLink.class, false);
		
		subQs.setDistinct(true);
		subQs.appendSelect(new ClassAttribute(WTPartDescribeLink.class, "roleBObjectRef.key.id"), false);
		
		SearchCondition sc = new SearchCondition(new ClassAttribute(WTPart.class, "thePersistInfo.theObjectIdentifier.id"), "=",
				new ClassAttribute(WTPartDescribeLink.class, "roleAObjectRef.key.id"));
		sc.setOuterJoin(0);
		subQs.appendWhere(sc, new int[] { part_idx, link_idx });
		subQs.appendAnd();
		ArrayExpression arryExpr = new ArrayExpression(parts.toArray());
		subQs.appendWhere(new SearchCondition(new ClassAttribute(WTPart.class, WTPart.NUMBER), SearchCondition.IN, arryExpr), new int[]{part_idx});
		
		return subQs;
	}
	
	public List<E3PSDocumentData> getRelatedDoc(Map<String, Object> reqMap) throws Exception {
		
		List<E3PSDocumentData> list = new ArrayList<>();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		Persistable per = CommonUtil.getObject(oid);
		
		QueryResult qr = null;
		if(per instanceof WTPart) {
			WTPart part = (WTPart) per;
			qr = PersistenceHelper.manager.navigate(part, WTPartDescribeLink.DESCRIBED_BY_ROLE, WTPartDescribeLink.class);
			
			while(qr.hasMoreElements()){
				E3PSDocument doc = (E3PSDocument) qr.nextElement();
				
				E3PSDocumentData data = new E3PSDocumentData(doc);
				
				list.add(data);
			}
		} else if(per instanceof E3PSDocumentData) {
			
		}
//		else if(per instanceof DistributeDocument){
//			DistributeDocument distribute = (DistributeDocument) per;
//			qr = PersistenceHelper.manager.navigate(distribute, "doc", DistributeToDocumentLink.class);
//			
//			while(qr.hasMoreElements()){
//				E3PSDocument doc = (E3PSDocument) qr.nextElement();
//				
//				E3PSDocumentData data = new E3PSDocumentData(doc);
//				
//				list.add(data);
//			}
//		}
		
		return list;
	}

	/**
	 * @desc	: DocValueDefinition, DocCodeType 가져오기
	 * @author	: mnyu
	 * @date	: 2019. 9. 6.
	 * @method	: getDoc
	 * @return	: Persistable
	 * @param code
	 * @return
	 * @throws  
	 */
	public Persistable getDoc(Class cls, String code) {
		try {
			QuerySpec qs = new QuerySpec();
			int idx = qs.addClassList(cls, true);
			qs.appendWhere(new SearchCondition(cls, "code", "=", code), new int[]{idx});
			
			QueryResult qr = PersistenceHelper.manager.find(qs);
			if(qr.hasMoreElements()){
				Object[] obj = (Object[]) qr.nextElement();
				if(obj[0] instanceof DocValueDefinition){
					return (DocValueDefinition) obj[0];
				}else if(obj[0] instanceof DocCodeType){
					return (DocCodeType) obj[0];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @desc	: DocCodeToValueDefinitionLink 가져오기
	 * @author	: mnyu
	 * @date	: 2019. 9. 6.
	 * @method	: getDocCodeToValueDefinitionLink
	 * @return	: DocCodeToValueDefinitionLink
	 * @param docCodeType
	 * @param definition
	 * @return
	 */
	public DocCodeToValueDefinitionLink getDocCodeToValueDefinitionLink(DocCodeType docCodeType,
			DocValueDefinition definition) {
		try {
			QuerySpec qs = new QuerySpec();
			Class cls = DocCodeToValueDefinitionLink.class;
			int idx = qs.addClassList(cls, true);
			qs.appendWhere(new SearchCondition(cls, "roleAObjectRef.key.id", "=", CommonUtil.getOIDLongValue(docCodeType)), new int[] {idx});
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(cls, "roleBObjectRef.key.id", "=", CommonUtil.getOIDLongValue(definition)), new int[] {idx});
			
			QueryResult qr = PersistenceHelper.manager.find(qs);
			if(qr.hasMoreElements()){
				Object[] obj = (Object[]) qr.nextElement();
				return (DocCodeToValueDefinitionLink) obj[0];
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * @desc	: 문서 속성 가져오기
	 * @author	: mnyu
	 * @date	: 2019. 9. 9.
	 * @method	: getAttribute
	 * @return	: List
	 * @param reqMap
	 * @return
	 */
	public List<DocValueDefinitionData> getAttribute(Map<String, Object> reqMap) throws Exception{
		List<DocValueDefinitionData> list = new ArrayList<DocValueDefinitionData>();
		
		String foid = StringUtil.checkNull((String) reqMap.get("foid"));
		QuerySpec qs = new QuerySpec();
		if (foid.length() == 0) return list;
		
		int codeType_idx = qs.addClassList(DocCodeType.class, false);
		int link_idx = qs.addClassList(DocCodeToValueDefinitionLink.class, false);
		int valueDifinition_idx = qs.addClassList(DocValueDefinition.class, true);
		
		SearchCondition sc = new SearchCondition(new ClassAttribute(DocCodeType.class, "thePersistInfo.theObjectIdentifier.id"), "=",
				new ClassAttribute(DocCodeToValueDefinitionLink.class, "roleAObjectRef.key.id"));
		sc.setOuterJoin(0);
		qs.appendWhere(sc, new int[] { codeType_idx, link_idx });
		
		qs.appendAnd();
		
		sc = new SearchCondition(new ClassAttribute(DocValueDefinition.class, "thePersistInfo.theObjectIdentifier.id"), "=",
				new ClassAttribute(DocCodeToValueDefinitionLink.class, "roleBObjectRef.key.id"));
		sc.setOuterJoin(0);
		qs.appendWhere(sc, new int[] { valueDifinition_idx, link_idx });
		
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(DocCodeType.class, "folderReference.key.id", "=", CommonUtil.getOIDLongValue(foid)), new int[] {codeType_idx});
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		while(qr.hasMoreElements()){
			Object[] obj = (Object[]) qr.nextElement();
			DocValueDefinition definition = (DocValueDefinition) obj[0];
			DocValueDefinitionData data = new DocValueDefinitionData(definition);
			
			list.add(data);
		}
		
		return list;
	}

	/**
	 * @desc	: DocCodeType code가져오기
	 * @author	: mnyu
	 * @date	: 2019. 9. 11.
	 * @method	: getDocCode
	 * @return	: String
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	public String getDocCode(String foid) throws Exception {
		String code = "DOC";
		QuerySpec qs = new QuerySpec();
		int idx= qs.addClassList(DocCodeType.class, true);
		qs.appendWhere(new SearchCondition(DocCodeType.class, "folderReference.key.id", "=", CommonUtil.getOIDLongValue(foid)), new int[] {idx});
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		while(qr.hasMoreElements()){
			Object[] obj = (Object[]) qr.nextElement();
			DocCodeType doc = (DocCodeType) obj[0];
			code = doc.getCode();
		}
		return code;
	}
	
	/**
	 * @desc	: 관련문서 가져오기
	 * @author	: mnyu
	 * @date	: 2019. 9. 17.
	 * @method	: getDocCode
	 * @return	: String
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	public List<RevisionData> getRelatedDocRevisionData(Map<String, Object> reqMap) throws Exception {

		List<RevisionData> list = new ArrayList<>();
		
		List<E3PSDocumentData> docList = getRelatedDoc(reqMap);
		
		for(E3PSDocumentData data : docList) {
			RevisionData rData = (RevisionData) data;
			list.add(rData);
		}
		
		return list;
	}
	
	/**
	 * @desc	: ProjectList List 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 09. 04.
	 * @method	: getRelatedProjectList
	 * @return	: List<ProjectData>
	 * @param doc
	 * @return
	 */
	public List<ProjectData> getRelatedProjectList(E3PSDocument doc) throws Exception{
		
		List<ProjectData> list = new ArrayList<>();
		
		QuerySpec qs = new QuerySpec();
		
		int pjtIdx = qs.addClassList(EProject.class, true);
		int docIdx = qs.addClassList(E3PSDocument.class, false);
		int taskIdx = qs.addClassList(ETask.class, false);
		int outputIdx = qs.addClassList(EOutput.class, false);
		
		SearchCondition sc = new SearchCondition(new ClassAttribute(EOutput.class, EOutput.DOCUMENT_REFERENCE + ".key.id"), SearchCondition.EQUAL, new ClassAttribute(E3PSDocument.class, E3PSDocument.PERSIST_INFO + ".theObjectIdentifier.id"));
		qs.appendWhere(sc, new int[] { outputIdx, docIdx });
		
		if (qs.getConditionCount() > 0) {
			qs.appendAnd();
		}
		
		sc = new SearchCondition(new ClassAttribute(EOutput.class, EOutput.TASK_REFERENCE + ".key.id"), SearchCondition.EQUAL, new ClassAttribute(ETask.class, ETask.PERSIST_INFO + ".theObjectIdentifier.id"));
		qs.appendWhere(sc, new int[] { outputIdx, taskIdx });
		
		if (qs.getConditionCount() > 0) {
			qs.appendAnd();
		}
		
		sc = new SearchCondition(new ClassAttribute(ETask.class, ETask.PROJECT_REFERENCE + ".key.id"), SearchCondition.EQUAL, new ClassAttribute(EProject.class, EProject.PERSIST_INFO + ".theObjectIdentifier.id"));
		qs.appendWhere(sc, new int[] { taskIdx, pjtIdx });
		
		if (qs.getConditionCount() > 0) {
			qs.appendAnd();
		}
		
		sc = new SearchCondition(E3PSDocument.class, E3PSDocument.PERSIST_INFO + ".theObjectIdentifier.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(doc));
		qs.appendWhere(sc, new int[] { docIdx });
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		while(qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			EProject project = (EProject) obj[0];
			ProjectData data = new ProjectData(project);
			
			list.add(data);
		}
		
		return list;
	}
	
	public WTDocument getDocument(String number , String version)throws Exception{
		
		if(number==null || version==null)return null;
		
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(WTDocument.class,true);
		qs.appendWhere(new SearchCondition(WTDocument.class,WTDocument.NUMBER,"=",number),new int[]{0});
	    qs.appendAnd();
	    qs.appendWhere(new SearchCondition(WTDocument.class,"versionInfo.identifier.versionId","=",version),new int[]{0});
	    qs.appendAnd();
	    qs.appendWhere(VersionControlHelper.getSearchCondition(WTDocument.class, true), new int[] {0});
	    QueryResult qr = PersistenceHelper.manager.find(qs);
	    if(qr.hasMoreElements()){
	    	Object[] o = (Object[])qr.nextElement();
	        return (WTDocument)o[0];
	    }
	    return null;
	}
	public WTDocument getLastDocument(WTDocumentMaster master)throws Exception{
	    QuerySpec qs = new QuerySpec();
	    int ii = qs.addClassList(WTDocument.class,true);
	    qs.appendWhere(new SearchCondition(WTDocument.class,"masterReference.key.id","=",master.getPersistInfo().getObjectIdentifier().getId()),new int[]{0});

	    SearchUtil.addLastVersionCondition(qs, WTDocument.class,0); 
	    qs.appendAnd();
	    qs.appendWhere(VersionControlHelper.getSearchCondition(WTDocument.class, true), new int[] {0});
	    QueryResult qr = PersistenceHelper.manager.find(qs);
	    if(qr.hasMoreElements()){
	        Object[] o = (Object[])qr.nextElement();
	        return (WTDocument)o[0];
	    }
	    return null;
	}
	public String getDownloadLink(WTDocument ndoc)throws Exception{
		ContentItem item = null;
	    QueryResult result = ContentHelper.service.getContentsByRole ((ContentHolder)ndoc, ContentRoleType.PRIMARY );
	    while (result.hasMoreElements ()) {
	        item = (ContentItem) result.nextElement ();
	    }
	    ApplicationData pAppData = null;
	    URL pUrl = null;
	    String nUrl = null;
	    if(item != null) {
	    	pAppData = (ApplicationData)item;
	    	pUrl = ContentHelper.getDownloadURL((ContentHolder)ndoc, pAppData, true,"");
	    	nUrl= WebUtil.getHost() + "worldex/project/downloadContent?holderOid="+CommonUtil.getOIDString(ndoc)+"&appOid="+CommonUtil.getOIDString(pAppData);
	    }
	    
	    return "<a href='"+nUrl+"'>"+CommonUtil.getContentIconStr(pAppData) + ndoc.getNumber() +"</a>";
	}
	
	public ArrayList<Map<String, String>> getDocListRest(Map<String, Object> reqMap) throws Exception {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		int start = (Integer) reqMap.get("start");
		int count = (Integer) reqMap.get("count");
		String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));
    	
		String moduleType = StringUtil.checkNull((String) reqMap.get("moduleType"));
		
		PagingQueryResult result = null;
		
		if (sessionId.length() > 0) {
			result = PagingSessionHelper.fetchPagingSession(start, count, Long.valueOf(sessionId));
		} else {
			QuerySpec query = getDocListQuery(reqMap);
			
			result = PageQueryBroker.openPagingSession(start, count, query, true);
		}
		
		while(result.hasMoreElements()){
			Map<String, String> map = new HashMap<>();
			Object[] obj = (Object[]) result.nextElement();
			E3PSDocument doc = (E3PSDocument) obj[0];
			
			E3PSDocumentData data = new E3PSDocumentData(doc);
			 
			map.put("name", data.getName());
			map.put("number", data.getNumber());
			map.put("oid", data.getOid());
			list.add(map);
		}
		
		
		return list;
	}
	
	/**
	 * @methodName : getRelatedPart
	 * @author : hckim
	 * @date : 2021.10.14
	 * @return : void
	 * @description : 문서와 관련된 Part 리스트
	 */
	public List<PartData> getRelatedPart(E3PSDocument doc) throws Exception {

		List<PartData> partList = new ArrayList<PartData>();
		QueryResult results = PersistenceHelper.manager.navigate(doc, WTPartDescribeLink.DESCRIBES_ROLE,
				WTPartDescribeLink.class, true);

		while (results.hasMoreElements()) {
			WTPart part = (WTPart) results.nextElement();
			partList.add(new PartData(part));
		}

		return partList;
	}
	
	/**
	 * @methodName : getReferencedProject
	 * @author : shjeong
	 * @date : 2022.12.06
	 * @return : List<ProjectData>
	 * @param 
	 * @description : 관련 프로젝트 Data List
	 */
	public List<EProject> getDocProjectLink(E3PSDocument doc) throws Exception {

		List<EProject> list = new ArrayList<EProject>();
		
		QueryResult qr = PersistenceHelper.manager.navigate(doc, DocProjectLink.PROJECT_ROLE, DocProjectLink.class);
		while (qr != null && qr.hasMoreElements()) {
			list.add((EProject) qr.nextElement());
		}

		return list;
	}
	
	public String makeDocNumber(String code, String docAttr) throws Exception {
		DateTime dt = DateTime.now();
		String docNumber = code.toUpperCase()+docAttr.toUpperCase() + "-";// 문서분류(2)문서유형(4)-
		docNumber += dt.getYear() + "-";// 년도(4)-
		docNumber += SequenceDao.manager.getSeqNo(docNumber, "0000", "E3PSDocumentMaster", "WTDocumentNumber");// 일련번호(4)

		return docNumber;
	}
	
	/**
	 * @methodName : getReferencedDocByProject
	 * @author : shjeong
	 * @date : 2023.01.26
	 * @return : List<ProjectData>
	 * @param 
	 * @description : 관련 프로젝트 Data List
	 */
	public List<String> getReferencedDocByProject(List<String> projects) throws Exception {

		List<String> list = new ArrayList<String>();
		
		List<String> projectCode = new ArrayList<String>();
		for(String oid : projects){
			EProject pjt = (EProject)CommonUtil.getObject(oid);
			projectCode.add(pjt.getCode());
		}
		
		list = searchProjectSubQuery(projectCode, list);
		list = searchProjectSubQuery2(projectCode, list);
		
		List<String> newList = list.stream().distinct().collect(Collectors.toList());

		return newList;
	}
	
	/**
	 * @desc	: 프로젝트 sub 쿼리 검색 ( 문서, 프로젝트 링크 테이블 검색 )
	 * @author	: shjeong
	 * @date	: 2023. 01. 30.
	 * @method	: searchProjectSubQuery
	 * @return	: List<Long>
	 * @param List<String>,List<Long>
	 * @return
	 * @throws Exception 
	 */
	private List<String> searchProjectSubQuery(List<String> projects, List<String> list) throws Exception {
		QuerySpec subQs = new QuerySpec();
		int project_idx = subQs.appendClassList(EProject.class, false);
		int link_idx = subQs.appendClassList(DocProjectLink.class, true);
		
		SearchCondition sc = new SearchCondition(new ClassAttribute(EProject.class, WTAttributeNameIfc.ID_NAME), "=",
				new ClassAttribute(DocProjectLink.class, WTAttributeNameIfc.ROLEB_OBJECT_ID));
		sc.setOuterJoin(0);
		subQs.appendWhere(sc, new int[] { project_idx, link_idx });
		subQs.appendAnd();
		ArrayExpression arryExpr = new ArrayExpression(projects.toArray());
		subQs.appendWhere(new SearchCondition(new ClassAttribute(EProject.class, EProject.CODE), SearchCondition.IN, arryExpr), new int[]{project_idx});
		
		QueryResult qr = PersistenceHelper.manager.find(subQs);
		while(qr.hasMoreElements()) {
			Object[] o = (Object[])qr.nextElement();
			DocProjectLink link = (DocProjectLink)o[0];
			E3PSDocument doc = (E3PSDocument)link.getRoleAObject();
			list.add(doc.getNumber());
		}
		return list;
	}
	
	/**
	 * @desc	: 프로젝트 sub 쿼리 검색2  ( 프로젝트 산출물 검색 )
	 * @author	: shjeong
	 * @date	: 2023. 01. 30.
	 * @method	: searchProjectSubQuery2
	 * @return	: List<Long>
	 * @param List<String>,List<Long>
	 * @return
	 * @throws Exception 
	 */
	private List<String> searchProjectSubQuery2(List<String> projects, List<String> list) throws Exception {
		QuerySpec subQs = new QuerySpec();
		int project_idx = subQs.appendClassList(EProject.class, false);
		int task_idx = subQs.appendClassList(ETask.class, false);
		int output_idx = subQs.appendClassList(EOutput.class, true);
		
		SearchCondition sc1 = new SearchCondition(new ClassAttribute(ETask.class, WTAttributeNameIfc.ID_NAME), "=",
				new ClassAttribute(EOutput.class, "taskReference.key.id"));
		sc1.setOuterJoin(0);
		subQs.appendWhere(sc1, new int[] { task_idx, output_idx });
		subQs.appendAnd();
		SearchCondition sc2 = new SearchCondition(new ClassAttribute(ETask.class, "projectReference.key.id"), "=",
				new ClassAttribute(EProject.class, WTAttributeNameIfc.ID_NAME));
		sc2.setOuterJoin(0);
		subQs.appendWhere(sc2, new int[] { task_idx, project_idx });
		subQs.appendAnd();
		subQs.appendWhere(new SearchCondition(new ClassAttribute(EProject.class, EProject.LAST_VERSION ), "TRUE"), new int[]{project_idx});
		subQs.appendAnd();
		
		ArrayExpression arryExpr = new ArrayExpression(projects.toArray());
		subQs.appendWhere(new SearchCondition(new ClassAttribute(EProject.class, EProject.CODE), SearchCondition.IN, arryExpr), new int[]{project_idx});
		subQs.appendAnd();
		subQs.appendWhere(new SearchCondition(new ClassAttribute(EOutput.class, "documentReference.key.id"), SearchCondition.NOT_NULL), new int[]{output_idx});
		subQs.appendAnd();
		subQs.appendWhere(new SearchCondition(new ClassAttribute(EOutput.class, "documentReference.key.id"), SearchCondition.NOT_EQUAL, new ConstantExpression(0L)), new int[]{output_idx});
		
		QueryResult qr = PersistenceHelper.manager.find(subQs);
		while(qr.hasMoreElements()) {
			Object[] o = (Object[])qr.nextElement();
			EOutput output = (EOutput)o[0];
			list.add(output.getDocument().getNumber());
		}
		
		return list;
	}
	
	/**
	 * @methodName : setRelatedDocumentProjectEchange
	 * @author : hckim
	 * @date : 2022.01.17
	 * @param mainQuery 주쿼리
	 * @param mainQueryIdx 주쿼리 인덱스
	 * @param searchTerm 검색 문자열 리스트
	 * @param searchByName 이름 검색 여부(false인 경우 번호검색으로 합니다.)
	 * @return : QuerySpec
	 * @description : 프로젝트 번호와 관련된 문서(참조문서, 산출물)을 검색합니다.
	 */
	@SuppressWarnings("deprecation")
	protected QuerySpec setRelatedDocumentProjectEchange(QuerySpec mainQuery, int mainQueryIdx, List<String> searchTerm, boolean searchByName) throws Exception{
		
		QuerySpec unionQueryProjectRefDoc = null;
		QuerySpec unionQueryProjectOutput = null;
		QuerySpec unionQueryEchangeRefDoc = null;
		QuerySpec unionQueryEchangeOutput = null;
		SearchCondition sc = null;
		
		if(!mainQuery.isAdvancedQueryEnabled()) {
			mainQuery.setAdvancedQueryEnabled(true);
		}
		
//		//프로젝트 참조문서
//		if(searchTerm.size() > 0) {
//			
//			unionQueryProjectRefDoc = new QuerySpec();
//			int idxProjectRefDocLink = unionQueryProjectRefDoc.appendClassList(ObjectToRefDocLink.class, false);
//			int idxProject = unionQueryProjectRefDoc.appendClassList(EProject.class, false);
//			
//			unionQueryProjectRefDoc.setDistinct(true);
//			unionQueryProjectRefDoc.setAdvancedQueryEnabled(true);
//			
//			ClassAttribute ca1 = new ClassAttribute(ObjectToRefDocLink.class, ObjectToRefDocLink.ROLE_BOBJECT_REF+".key.classname");
//			ca1.setColumnAlias("docclassname");
//			ClassAttribute ca2 = new ClassAttribute(ObjectToRefDocLink.class, ObjectToRefDocLink.ROLE_BOBJECT_REF+".key.branchId");
//			ca2.setColumnAlias("docobjectid");
//			
//			unionQueryProjectRefDoc.appendSelect(ca1, false);
//			unionQueryProjectRefDoc.appendSelect(ca2, false);
//			
//			if(unionQueryProjectRefDoc.getConditionCount() > 0) {
//				unionQueryProjectRefDoc.appendAnd();
//			}
//			unionQueryProjectRefDoc.appendOpenParen();
//			for(int i = 0; i < searchTerm.size(); i++) {
//				String term = searchByName ? "%"+searchTerm.get(i) +"%" : searchTerm.get(i);
//				
//				if(i > 0) {
//					unionQueryProjectRefDoc.appendOr();
//				}
//				
//				String searchType = searchByName ? EProjectNode.NAME : EProject.CODE;
//				sc = new SearchCondition(
//						EProject.class, searchType,
//						SearchCondition.LIKE,
//						term);
//				unionQueryProjectRefDoc.appendWhere(sc, new int[]{idxProject});
//				
//			}
//			unionQueryProjectRefDoc.appendCloseParen();
//			
//			if(unionQueryProjectRefDoc.getConditionCount() > 0) {
//				unionQueryProjectRefDoc.appendAnd();
//			}
//			
//			sc = new SearchCondition(
//					new ClassAttribute( ObjectToRefDocLink.class, ObjectToRefDocLink.ROLE_AOBJECT_REF+".key.id" ),
//					SearchCondition.EQUAL,
//					new ClassAttribute( EProject.class, EProject.PERSIST_INFO+".theObjectIdentifier.id" ));
//			unionQueryProjectRefDoc.appendWhere(sc, new int[]{idxProjectRefDocLink, idxProject});
//		
//		}
//		
//		//프로젝트 산출물
//		if(searchTerm.size() > 0) {
//			
//			unionQueryProjectOutput = new QuerySpec();
//			
//			int idxOutput = unionQueryProjectOutput.appendClassList(EOutput.class, false);
//			int idxTask = unionQueryProjectOutput.appendClassList(ETask.class, false);
//			int idxProject = unionQueryProjectOutput.appendClassList(EProject.class, false);
//			
//			unionQueryProjectOutput.setDistinct(true);
//			unionQueryProjectOutput.setAdvancedQueryEnabled(true);
//			
//			ClassAttribute ca1 = new ClassAttribute(EOutput.class, EOutput.DOCUMENT_REFERENCE +".key.classname");
//			ca1.setColumnAlias("docclassname");
//			ClassAttribute ca2 = new ClassAttribute(EOutput.class, EOutput.DOCUMENT_REFERENCE+".key.id");
//			ca2.setColumnAlias("docobjectid");
//			
//			unionQueryProjectOutput.appendSelect(ca1, false);
//			unionQueryProjectOutput.appendSelect(ca2, false);
//			
//			if(unionQueryProjectOutput.getConditionCount() > 0) {
//				unionQueryProjectOutput.appendAnd();
//			}
//			
//			unionQueryProjectOutput.appendOpenParen();
//			for(int i = 0; i<searchTerm.size(); i++) {
//				String term = searchByName ? "%"+searchTerm.get(i) +"%" : searchTerm.get(i);
//				
//				if(i > 0) {
//					unionQueryProjectOutput.appendOr();
//				}
//				
//				String searchType = searchByName ? EProjectNode.NAME : EProject.CODE;
//				sc = new SearchCondition(
//						EProject.class, searchType,
//						SearchCondition.LIKE,
//						term);
//				unionQueryProjectOutput.appendWhere(sc, new int[]{idxProject});
//				
//			}
//			unionQueryProjectOutput.appendCloseParen();
//			
//			if(unionQueryProjectOutput.getConditionCount() > 0) {
//				unionQueryProjectOutput.appendAnd();
//			}
//			
//			sc = new SearchCondition(
//					new ClassAttribute( ETask.class, ETask.PROJECT_REFERENCE+".key.id" ),
//					SearchCondition.EQUAL,
//					new ClassAttribute( EProject.class, EProject.PERSIST_INFO+".theObjectIdentifier.id" ));
//			unionQueryProjectOutput.appendWhere(sc, new int[]{idxTask, idxProject});
//			
//			if(unionQueryProjectOutput.getConditionCount() > 0) {
//				unionQueryProjectOutput.appendAnd();
//			}
//			
//			sc = new SearchCondition(
//					new ClassAttribute( EOutput.class, EOutput.TASK_REFERENCE+".key.id" ),
//					SearchCondition.EQUAL,
//					new ClassAttribute( ETask.class, ETask.PERSIST_INFO+".theObjectIdentifier.id" ));
//			unionQueryProjectOutput.appendWhere(sc, new int[]{idxOutput, idxTask});
//		}
//		
//		//설계변경 참조문서
//		if(searchTerm.size() > 0) {
//			
//			unionQueryEchangeRefDoc = new QuerySpec();
//			int idxEchangeRefDocLink = unionQueryEchangeRefDoc.appendClassList(ObjectToRefDocLink.class, false);
//			int idxEchange = unionQueryEchangeRefDoc.appendClassList(ECOChange.class, false);
//			
//			unionQueryEchangeRefDoc.setDistinct(true);
//			unionQueryEchangeRefDoc.setAdvancedQueryEnabled(true);
//			
//			ClassAttribute ca1 = new ClassAttribute(ObjectToRefDocLink.class, ObjectToRefDocLink.ROLE_BOBJECT_REF+".key.classname");
//			ca1.setColumnAlias("docclassname");
//			ClassAttribute ca2 = new ClassAttribute(ObjectToRefDocLink.class, ObjectToRefDocLink.ROLE_BOBJECT_REF+".key.branchId");
//			ca2.setColumnAlias("docobjectid");
//			
//			unionQueryEchangeRefDoc.appendSelect(ca1, false);
//			unionQueryEchangeRefDoc.appendSelect(ca2, false);
//			
//			if(unionQueryEchangeRefDoc.getConditionCount() > 0) {
//				unionQueryEchangeRefDoc.appendAnd();
//			}
//			
//			unionQueryEchangeRefDoc.appendOpenParen();
//			for(int i = 0; i<searchTerm.size(); i++) {
//				String term = searchByName ? "%"+searchTerm.get(i) +"%" : searchTerm.get(i);
//				
//				if(i > 0) {
//					unionQueryEchangeRefDoc.appendOr();
//				}
//				
//				String searchType = searchByName ? ECOChange.EO_NAME : ECOChange.EO_NUMBER;
//				sc = new SearchCondition(
//						ECOChange.class, searchType,
//						SearchCondition.LIKE,
//						term);
//				unionQueryEchangeRefDoc.appendWhere(sc, new int[]{idxEchange});
//			}
//			unionQueryEchangeRefDoc.appendCloseParen();
//			
//			if(unionQueryEchangeRefDoc.getConditionCount() > 0) {
//				unionQueryEchangeRefDoc.appendAnd();
//			}
//			
//			sc = new SearchCondition(
//					new ClassAttribute( ObjectToRefDocLink.class, ObjectToRefDocLink.ROLE_AOBJECT_REF+".key.id" ),
//					SearchCondition.EQUAL,
//					new ClassAttribute( ECOChange.class, ECOChange.PERSIST_INFO+".theObjectIdentifier.id" ));
//			unionQueryEchangeRefDoc.appendWhere(sc, new int[]{idxEchangeRefDocLink , idxEchange});
//		}
//		
//		
//		//설계변경 활동 산출물
//		if(searchTerm.size() > 0) {
//			
//			unionQueryEchangeOutput = new QuerySpec();
//			
//			//DocumentActivityLink
//			//QueryResult rt = PersistenceHelper.manager.navigate(eca, "doc",DocumentActivityLink.class);
//			int idxDoc = unionQueryEchangeOutput.appendClassList(WTDocument.class, false);
//			int idxDocMaster = unionQueryEchangeOutput.appendClassList(WTDocumentMaster.class, false);
//			int idxActivityDoc = unionQueryEchangeOutput.appendClassList(DocumentActivityLink.class, false);
//			int idxActivity = unionQueryEchangeOutput.appendClassList(EChangeActivity.class, false);
//			int idxEO = unionQueryEchangeOutput.appendClassList(ECOChange.class, false);
//			
//			unionQueryEchangeOutput.setDistinct(true);
//			unionQueryEchangeOutput.setAdvancedQueryEnabled(true);
//			
//			ClassAttribute ca1 = new ClassAttribute(WTDocument.class, WTDocument.PERSIST_INFO +".theObjectIdentifier.classname");
//			ca1.setColumnAlias("docclassname");
//			ClassAttribute ca2 = new ClassAttribute(WTDocument.class, WTDocument.PERSIST_INFO+".theObjectIdentifier.id");
//			ca2.setColumnAlias("docobjectid");
//			
//			unionQueryEchangeOutput.appendSelect(ca1, false);
//			unionQueryEchangeOutput.appendSelect(ca2, false);
//			
//			if(unionQueryEchangeOutput.getConditionCount() > 0) {
//				unionQueryEchangeOutput.appendAnd();
//			}
//			
//			unionQueryEchangeOutput.appendOpenParen();
//			for(int i = 0; i<searchTerm.size(); i++) {
//				String term = searchByName ? "%"+searchTerm.get(i) +"%" : searchTerm.get(i);
//				
//				if(i > 0) {
//					unionQueryEchangeOutput.appendOr();
//				}
//				
//				String searchType = searchByName ? ECOChange.EO_NAME : ECOChange.EO_NUMBER;
//				sc = new SearchCondition(
//						ECOChange.class, searchType,
//						SearchCondition.LIKE,
//						term);
//				unionQueryEchangeOutput.appendWhere(sc, new int[]{idxEO});
//			}
//			unionQueryEchangeOutput.appendCloseParen();
//			
//			if(unionQueryEchangeOutput.getConditionCount() > 0) {
//				unionQueryEchangeOutput.appendAnd();
//			}
//			
//			sc = new SearchCondition(
//					new ClassAttribute( EChangeActivity.class, EChangeActivity.EO_REFERENCE+".key.id" ),
//					SearchCondition.EQUAL,
//					new ClassAttribute( ECOChange.class, ECOChange.PERSIST_INFO+".theObjectIdentifier.id" ));
//			unionQueryEchangeOutput.appendWhere(sc, new int[]{idxActivity, idxEO});
//			
//			if(unionQueryEchangeOutput.getConditionCount() > 0) {
//				unionQueryEchangeOutput.appendAnd();
//			}
//			
//			sc = new SearchCondition(
//					new ClassAttribute( DocumentActivityLink.class, DocumentActivityLink.ROLE_BOBJECT_REF+".key.id" ),
//					SearchCondition.EQUAL,
//					new ClassAttribute( EChangeActivity.class, EChangeActivity.PERSIST_INFO+".theObjectIdentifier.id" ));
//			unionQueryEchangeOutput.appendWhere(sc, new int[]{idxActivityDoc, idxActivity});
//			
//			if(unionQueryEchangeOutput.getConditionCount() > 0) {
//				unionQueryEchangeOutput.appendAnd();
//			}
//			
//			sc = new SearchCondition(
//					new ClassAttribute( WTDocumentMaster.class, WTDocumentMaster.PERSIST_INFO+".theObjectIdentifier.id" ),
//					SearchCondition.EQUAL,
//					new ClassAttribute( DocumentActivityLink.class, DocumentActivityLink.ROLE_AOBJECT_REF+".key.id" ));
//			unionQueryEchangeOutput.appendWhere(sc, new int[]{idxDocMaster, idxActivityDoc});
//			
//			if(unionQueryEchangeOutput.getConditionCount() > 0) {
//				unionQueryEchangeOutput.appendAnd();
//			}
//			
//			sc = new SearchCondition(
//					new ClassAttribute( WTDocument.class, WTDocument.MASTER_REFERENCE+".key.id" ),
//					SearchCondition.EQUAL,
//					new ClassAttribute( WTDocumentMaster.class, WTDocumentMaster.PERSIST_INFO+".theObjectIdentifier.id" ));
//			unionQueryEchangeOutput.appendWhere(sc, new int[]{idxDoc, idxDocMaster});
//			
//			if(unionQueryEchangeOutput.getConditionCount() > 0) {
//				unionQueryEchangeOutput.appendAnd();
//			}
//			
//			//최신 이터레이션
//			unionQueryEchangeOutput.appendWhere(VersionControlHelper.getSearchCondition(WTDocument.class, true), new int[] { idxDoc });
//			
//			//최신 버전
//			SearchUtil.addLastVersionCondition(unionQueryEchangeOutput, WTDocument.class, idxDoc);
//			
//		}
		
		
		CompoundQuerySpec unionSpec = new CompoundQuerySpec();
		unionSpec.setSetOperator(SetOperator.UNION_ALL);
		unionSpec.addComponent(unionQueryProjectRefDoc);
		unionSpec.addComponent(unionQueryProjectOutput);
		unionSpec.addComponent(unionQueryEchangeRefDoc);
		unionSpec.addComponent(unionQueryEchangeOutput);
		
		
		if(unionSpec != null) {
			
			SubSelectExpression subQuery = new SubSelectExpression(unionSpec);
			subQuery.setFromAlias(new String[]{"project"}, 0);
			int idxSubQuery = mainQuery.appendFrom(subQuery);
			
			if(mainQuery.getConditionCount() > 0) {
				mainQuery.appendAnd();
			}
			
			mainQuery.appendOpenParen();
			
			sc = new SearchCondition(
					new ClassAttribute(WTDocument.class, WTDocument.PERSIST_INFO +".theObjectIdentifier.classname"),
					SearchCondition.EQUAL,
					new KeywordExpression(mainQuery.getFromClause().getAliasAt(idxSubQuery) + ".docclassname"));
			sc.setFromIndicies(new int[]{mainQueryIdx, idxSubQuery},0);
			sc.setOuterJoin(0);
			mainQuery.appendWhere(sc, new int[]{mainQueryIdx, idxSubQuery});
			if(mainQuery.getConditionCount() > 0) {
				mainQuery.appendAnd();
			}
			sc = new SearchCondition(
					new ClassAttribute(WTDocument.class, WTDocument.PERSIST_INFO +".theObjectIdentifier.id"),
					SearchCondition.EQUAL,
					new KeywordExpression(mainQuery.getFromClause().getAliasAt(idxSubQuery) + ".docobjectid")
					);
			sc.setFromIndicies(new int[]{mainQueryIdx, idxSubQuery},0);	
			sc.setOuterJoin(0);
			mainQuery.appendWhere(sc, new int[]{mainQueryIdx, idxSubQuery});
			
			mainQuery.appendOr();
			
			sc = new SearchCondition(
					new ClassAttribute(WTDocument.class, WTDocument.PERSIST_INFO +".theObjectIdentifier.classname"),
					SearchCondition.EQUAL,
					new KeywordExpression(mainQuery.getFromClause().getAliasAt(idxSubQuery) + ".docclassname"));
			sc.setFromIndicies(new int[]{mainQueryIdx, idxSubQuery},0);
			sc.setOuterJoin(0);
			mainQuery.appendWhere(sc, new int[]{mainQueryIdx, idxSubQuery});
			if(mainQuery.getConditionCount() > 0) {
				mainQuery.appendAnd();
			}
			sc = new SearchCondition(
					new ClassAttribute(WTDocument.class, WTDocument.BRANCH_IDENTIFIER),
					SearchCondition.EQUAL,
					new KeywordExpression(mainQuery.getFromClause().getAliasAt(idxSubQuery) + ".docobjectid")
					);
			sc.setFromIndicies(new int[]{mainQueryIdx, idxSubQuery},0);	
			sc.setOuterJoin(0);
			mainQuery.appendWhere(sc, new int[]{mainQueryIdx, idxSubQuery});
			
			mainQuery.appendCloseParen();
			
		}
		
		return mainQuery;
		
	}

	public List<DocCodeType> getDocCodeTypeList(String codeName) throws Exception{
		return getDocCodeTypeList(codeName, false);
	}
	
	public List<DocCodeType> getDocCodeTypeList(String codeName, boolean searchDisabled) throws Exception{
		List<DocCodeType> list = new ArrayList<>();
		if(StringUtil.checkNull(codeName).length() > 0) {
			
			QuerySpec query = new QuerySpec();
			int idx = query.addClassList(DocCodeType.class, true);
			
			SearchCondition sc = new SearchCondition(
					DocCodeType.class, DocCodeType.NAME,
					SearchCondition.EQUAL,
					codeName
					);
			query.appendWhere(sc, new int[]{idx});
			
			QueryResult qr = PersistenceHelper.manager.find(query);
			while(qr.hasMoreElements()){
				Object[] o = (Object[])qr.nextElement();
				DocCodeType code = (DocCodeType)o[0];
				list.add(code);
			}
			
		}
		return list;
	}
	
	public Map<String, String> getDocumetTypes() throws Exception {
		
		DocumentType[] types = DocumentType.getDocumentTypeSet();
		
		Map<String, String>  returnMap = new LinkedHashMap<String, String>();
		if(types.length > 0) {
			
			for(DocumentType type : types) {
				
				String name = type.toString();
				if(DocKey.ENUM_TYPE_GENERAL.getKey().equals(name) || DocKey.ENUM_TYPE_REFDOC.getKey().equals(name)) {
					returnMap.put(name, type.getDisplay(MessageUtil.getLocale()));
				}
			}
		}
		
		return returnMap;
	}
	
public List<PDRDocumentContentsData> getPDRDocumentContents(String oid) throws Exception{
		
		List<PDRDocumentContentsData> result = new ArrayList<PDRDocumentContentsData>();
		QuerySpec qs = new QuerySpec();
		int idx = qs.addClassList(PDRDocumentContents.class, true);
		SearchCondition sc = new SearchCondition(PDRDocumentContents.class, PDRDocumentContents.PDR_REFERENCE + "." + WTAttributeNameIfc.REF_OBJECT_ID, SearchCondition.EQUAL, CommonUtil.getOIDLongValue(oid));
	 	qs.appendWhere(sc, new int[] { idx });
	 	qs.appendOrderBy(new OrderBy(new ClassAttribute(PDRDocumentContents.class, PDRDocumentContents.SORT), false), new int[] { idx });
	 	
	 	QueryResult qr = PersistenceHelper.manager.find(qs);
	 	
	 	while(qr.hasMoreElements()) {
	 		Object[] o = (Object[])qr.nextElement();
	 		PDRDocumentContents pdrdc = (PDRDocumentContents)o[0];
	 		PDRDocumentContentsData data = new PDRDocumentContentsData(pdrdc);
	 		result.add(data);
	 	}
	 	
	 	return result;
	}

public List<ETCDocumentContentsData> getETCDocumentContents(String oid) throws Exception{
	
	List<ETCDocumentContentsData> result = new ArrayList<ETCDocumentContentsData>();
	QuerySpec qs = new QuerySpec();
	int idx = qs.addClassList(ETCDocumentContents.class, true);
	SearchCondition sc = new SearchCondition(ETCDocumentContents.class, ETCDocumentContents.DOC_ETC_REFERENCE + "." + WTAttributeNameIfc.REF_OBJECT_ID, SearchCondition.EQUAL, CommonUtil.getOIDLongValue(oid));
 	qs.appendWhere(sc, new int[] { idx });
 	qs.appendOrderBy(new OrderBy(new ClassAttribute(ETCDocumentContents.class, ETCDocumentContents.SORT), false), new int[] { idx });
 	
 	QueryResult qr = PersistenceHelper.manager.find(qs);
 	
 	while(qr.hasMoreElements()) {
 		Object[] o = (Object[])qr.nextElement();
 		ETCDocumentContents etcDoc = (ETCDocumentContents)o[0];
 		ETCDocumentContentsData data = new ETCDocumentContentsData(etcDoc);
 		result.add(data);
 	}
 	
 	return result;
}
}
