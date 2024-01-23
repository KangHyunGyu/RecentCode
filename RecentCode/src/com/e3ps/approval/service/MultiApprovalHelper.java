package com.e3ps.approval.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.e3ps.approval.MultiApproval;
import com.e3ps.approval.MultiApprovalObjectLink;
import com.e3ps.approval.bean.MultiApprovalData;
import com.e3ps.common.bean.RevisionData;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.web.PageQueryBroker;
import com.e3ps.doc.E3PSDocument;
import com.e3ps.org.People;

import wt.doc.WTDocument;
import wt.enterprise.RevisionControlled;
import wt.epm.EPMDocument;
import wt.fc.PagingQueryResult;
import wt.fc.PagingSessionHelper;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.pds.StatementSpec;
import wt.query.ArrayExpression;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.ServiceFactory;
import wt.session.SessionHelper;
import wt.util.WTAttributeNameIfc;

public class MultiApprovalHelper {
	
public static final  MultiApprovalService service = ServiceFactory.getService(MultiApprovalService.class);

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.APPROVAL.getName());
	public static final MultiApprovalHelper manager = new MultiApprovalHelper();
	
	/**
	 *  
	 * @desc	: 일괄 결재 리스트
	 * @author	: tsuam
	 * @date	: 2019. 8. 2.
	 * @method	: getMultiApprovalList
	 * @return	: List<DocData>
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	
	public List<MultiApprovalData> getMultiListALL(Map<String, Object> reqMap) throws Exception {
		
		List<MultiApprovalData> list = new ArrayList<>();
		
		QuerySpec query = getMultiListQuery(reqMap);
		
		QueryResult result = PersistenceHelper.manager.find((StatementSpec)query);
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			MultiApproval multi = (MultiApproval) obj[0];
			
			MultiApprovalData data = new MultiApprovalData(multi);
			
			list.add(data);
		}
		
		return list;
	}
	
	public Map<String, Object> getMultiScrollListALL(Map<String, Object> reqMap) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
				
		List<MultiApprovalData> list = new ArrayList<>();
		
		int page = (Integer) reqMap.get("page");
		int rows = (Integer) reqMap.get("rows");
		
		QuerySpec query = getMultiListQuery(reqMap);
		
		PagingQueryResult result = PageQueryBroker.openPagingSession((page - 1) * rows, rows, query, true);
		
		int totalSize = result.getTotalSize();
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			MultiApproval multi = (MultiApproval) obj[0];
			
			MultiApprovalData data = new MultiApprovalData(multi);
			
			list.add(data);
		}
		
		map.put("list", list);
		map.put("totalSize", totalSize);
		
		return map;
	}
	
	public Map<String, Object> getMultiScrollListALL2(Map<String, Object> reqMap) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
		
		List<MultiApprovalData> list = new ArrayList<>();
		
		int page = (Integer) reqMap.get("page");
		int rows = (Integer) reqMap.get("rows");
		
		String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));
		PagingQueryResult result = null;
		if (sessionId.length() > 0) {
			result = PagingSessionHelper.fetchPagingSession((page-1)*rows, rows, Long.valueOf(sessionId));
		} else {
			QuerySpec query = getMultiListQuery3(reqMap);

			result = PageQueryBroker.openPagingSession((page-1)*rows, rows, query, true);
		}
//		QuerySpec query = getMultiListQuery2(reqMap);
		
		int totalSize = result.getTotalSize();
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			MultiApproval multi = (MultiApproval) obj[0];
			
			MultiApprovalData data = new MultiApprovalData(multi);
			
			list.add(data);
		}
		
		map.put("list", list);
		map.put("totalSize", totalSize);
		map.put("sessionId", result.getSessionId() == 0 ? "" : result.getSessionId());
		
		return map;
	}

	/**
	 * 
	 * @desc	: 스크롤 다운 방식
	 * @author	: tsuam
	 * @date	: 2019. 8. 5.
	 * @method	: getMultiListScroll
	 * @return	: List<MultiApprovalData>
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public List<MultiApprovalData> getMultiListScroll(Map<String, Object> reqMap) throws Exception {
		
		List<MultiApprovalData> list = new ArrayList<>();
		
		int page = (Integer) reqMap.get("page");
		int rows = (Integer) reqMap.get("rows");
		
		QuerySpec query = getMultiListQuery(reqMap);
    	
		PagingQueryResult result = PageQueryBroker.openPagingSession((page - 1) * rows, rows, query, true);
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			MultiApproval multi = (MultiApproval) obj[0];
			
			MultiApprovalData data = new MultiApprovalData(multi);
			
			list.add(data);
		}
		
		return list;
	}
	
	/**
	 * 
	 * @desc	: page지 처리 방식
	 * @author	: tsuam
	 * @date	: 2019. 8. 5.
	 * @method	: getMultiListPage
	 * @return	: Map<String,Object>
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getMultiListPage(Map<String, Object> reqMap) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
		
		List<MultiApprovalData> list = new ArrayList<>();
		
		int page = (Integer) reqMap.get("page");
		int rows = (Integer) reqMap.get("rows");
		
		QuerySpec query = getMultiListQuery(reqMap);
    	
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
			MultiApproval doc = (MultiApproval) obj[0];
			
			MultiApprovalData data = new MultiApprovalData(doc);
			
			list.add(data);
		}
		
		map.put("list", list);
		map.put("totalSize", totalSize);
		map.put("totalPage", totalPage);
		
		return map;
	}
	
	public QuerySpec getMultiListQuery(Map<String, Object> reqMap) throws Exception {
		
		String number = StringUtil.checkNull((String) reqMap.get("number"));
		String name = StringUtil.checkNull((String) reqMap.get("name"));
		String predate = StringUtil.checkNull((String) reqMap.get("predate"));
		String postdate = StringUtil.checkNull((String) reqMap.get("postdate"));
		
		
		List<String> creator = StringUtil.checkReplaceArray(reqMap.get("creator"));
		List<String> state = StringUtil.checkReplaceArray(reqMap.get("state"));
		
		//Sort
		boolean sortCheck = true;
		if(reqMap.get("sortCheck") != null){
			sortCheck = (boolean) reqMap.get("sortCheck");
		}
		String sortValue = StringUtil.checkNull((String) reqMap.get("sortValue"));
		
		QuerySpec qs = new QuerySpec();
		int idx = qs.addClassList(MultiApproval.class, true);
		//int idx2 = qs.addClassList(ApprovalMaster, arg1)
		SearchCondition sc = null;
		
		
    	
		
		//문서번호
		if(number.length() > 0) {
			if(qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(MultiApproval.class, MultiApproval.NUMBER, SearchCondition.LIKE, "%" + number + "%", false);
			qs.appendWhere(sc, new int[] { idx });
		}
		
		//문서명
		if(name.length() > 0) {
			if(qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(MultiApproval.class, MultiApproval.NAME, SearchCondition.LIKE, "%" + name + "%", false);
			qs.appendWhere(sc, new int[] { idx });
		}
		
		//작성자
		if (creator.size() > 0) {
			List<Long> userOidLongValueList = new ArrayList<>();
			
			for(String pp : creator) {
				People people = (People) CommonUtil.getObject(pp);
				WTUser user = people.getUser();
				
				userOidLongValueList.add(CommonUtil.getOIDLongValue(user));
			}
			
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(MultiApproval.class, MultiApproval.OWNER + "." + "key.id"), SearchCondition.IN, new ArrayExpression(userOidLongValueList.toArray()));
			qs.appendWhere(sc, new int[] { idx });
		}
				
		//상태
		if (state.size() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(MultiApproval.class, MultiApproval.STATE), SearchCondition.IN, new ArrayExpression(state.toArray()));
			qs.appendWhere(sc, new int[] { idx });
		}
		
		//등록일
    	if(predate.length() > 0){
    		if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
    		sc = new SearchCondition(MultiApproval.class, WTAttributeNameIfc.CREATE_STAMP_NAME ,SearchCondition.GREATER_THAN,DateUtil.convertStartDate(predate));
    		qs.appendWhere(sc, new int[] { idx });
    	}
    	if(postdate.length() > 0){
    		if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
    		sc = new SearchCondition(MultiApproval.class, WTAttributeNameIfc.CREATE_STAMP_NAME,SearchCondition.LESS_THAN,DateUtil.convertEndDate(postdate));
    		qs.appendWhere(sc, new int[] { idx });
    	}
    	
    	//소팅
    	if (sortValue.length() > 0) {
    		if (sortCheck) {
    			qs.appendOrderBy(new OrderBy(new ClassAttribute(MultiApproval.class, sortValue), false), new int[] { idx });
    		} else {
    			qs.appendOrderBy(new OrderBy(new ClassAttribute(MultiApproval.class, sortValue), true), new int[] { idx });
    		}
    	} else {
    		qs.appendOrderBy(new OrderBy(new ClassAttribute(MultiApproval.class, WTAttributeNameIfc.CREATE_STAMP_NAME), true), new int[] { idx });
    	}
    			
    	
    	return qs;
	}
	
	public QuerySpec getMultiListQuery2(Map<String, Object> reqMap) throws Exception {
		
		//소팅
		Map<String, Object> sort = reqMap.get("sort") != null ? (Map<String, Object>) reqMap.get("sort") : new HashMap<>();
				
		String number = StringUtil.checkNull((String) reqMap.get("number"));
		String name = StringUtil.checkNull((String) reqMap.get("name"));
		String predate = StringUtil.checkNull((String) reqMap.get("predate"));
		String postdate = StringUtil.checkNull((String) reqMap.get("postdate"));
		
		
		List<String> creator = StringUtil.checkReplaceArray(reqMap.get("creator"));
		List<String> state = StringUtil.checkReplaceArray(reqMap.get("state"));
		
		QuerySpec qs = new QuerySpec();
		int idx = qs.addClassList(MultiApproval.class, true);
		//int idx2 = qs.addClassList(ApprovalMaster, arg1)
		SearchCondition sc = null;
		
		//문서번호
		if(number.length() > 0) {
			if(qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(MultiApproval.class, MultiApproval.NUMBER, SearchCondition.LIKE, "%" + number + "%", false);
			qs.appendWhere(sc, new int[] { idx });
		}
		
		//문서명
		if(name.length() > 0) {
			if(qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(MultiApproval.class, MultiApproval.NAME, SearchCondition.LIKE, "%" + name + "%", false);
			qs.appendWhere(sc, new int[] { idx });
		}
		
		//작성자
		if (creator.size() > 0) {
			List<Long> userOidLongValueList = new ArrayList<>();
			
			for(String pp : creator) {
				People people = (People) CommonUtil.getObject(pp);
				WTUser user = people.getUser();
				
				userOidLongValueList.add(CommonUtil.getOIDLongValue(user));
			}
			
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(MultiApproval.class, MultiApproval.OWNER + "." + "key.id"), SearchCondition.IN, new ArrayExpression(userOidLongValueList.toArray()));
			qs.appendWhere(sc, new int[] { idx });
		}
		
		//상태
		if (state.size() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(MultiApproval.class, MultiApproval.STATE), SearchCondition.IN, new ArrayExpression(state.toArray()));
			qs.appendWhere(sc, new int[] { idx });
		}
		
		//등록일
		if(predate.length() > 0){
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(MultiApproval.class, WTAttributeNameIfc.CREATE_STAMP_NAME ,SearchCondition.GREATER_THAN,DateUtil.convertStartDate(predate));
			qs.appendWhere(sc, new int[] { idx });
		}
		if(postdate.length() > 0){
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(MultiApproval.class, WTAttributeNameIfc.CREATE_STAMP_NAME,SearchCondition.LESS_THAN,DateUtil.convertEndDate(postdate));
			qs.appendWhere(sc, new int[] { idx });
		}
		
		//소팅
		if (sort.size() > 0) {
			String id = (String) sort.get("id");
			String dir = (String) sort.get("dir");
			
			String sortValue = "";
			if("objectTypeName".equals(id)) {
				sortValue = MultiApproval.OBJECT_TYPE;
			} else if("number".equals(id)) {
				sortValue = MultiApproval.NUMBER;
			} else if("name".equals(id)) {
				sortValue = MultiApproval.NAME;
			} else if("stateName".equals(id)) {
				sortValue = MultiApproval.STATE;
			} else if("ownerFullName".equals(id)) {
				sortValue = MultiApproval.OWNER;
			} else if("createDateFormat".equals(id)) {
				sortValue = MultiApproval.CREATE_TIMESTAMP;
			}
			
			if ("asc".equals(dir)) {
				qs.appendOrderBy(new OrderBy(new ClassAttribute(MultiApproval.class, sortValue), false), new int[] { idx });
			} else if("desc".equals(dir)) {
				qs.appendOrderBy(new OrderBy(new ClassAttribute(MultiApproval.class, sortValue), true), new int[] { idx });
			}
		} else {
			qs.appendOrderBy(new OrderBy(new ClassAttribute(MultiApproval.class, WTAttributeNameIfc.CREATE_STAMP_NAME), true), new int[] { idx });
		}

		
		
		return qs;
	}
	public QuerySpec getMultiListQuery3(Map<String, Object> reqMap) throws Exception {
		//소팅
		Map<String, Object> sort = reqMap.get("sort") != null ? (Map<String, Object>) reqMap.get("sort") : new HashMap<>();
				
		String number = StringUtil.checkNull((String) reqMap.get("number"));
		String name = StringUtil.checkNull((String) reqMap.get("name"));
		name = name.replace("[", "[[]");
		String predate = StringUtil.checkNull((String) reqMap.get("predate"));
		String postdate = StringUtil.checkNull((String) reqMap.get("postdate"));
		
		List<String> creator = StringUtil.checkReplaceArray(reqMap.get("creator"));
		List<String> state = StringUtil.checkReplaceArray(reqMap.get("state"));
		List<String> objectTypeList = StringUtil.checkReplaceArray(reqMap.get("objectType"));
		
		QuerySpec qs = new QuerySpec();
		int idx = qs.addClassList(MultiApproval.class, true);
		//int idx2 = qs.addClassList(ApprovalMaster, arg1)
		SearchCondition sc = null;
		
		//문서번호
		if(number.length() > 0) {
			if(qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(MultiApproval.class, MultiApproval.NUMBER, SearchCondition.LIKE, "%" + number + "%", false);
			qs.appendWhere(sc, new int[] { idx });
		}
		
		//문서명
		if(name.length() > 0) {
			if(qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(MultiApproval.class, MultiApproval.NAME, SearchCondition.LIKE, "%" + name + "%", false);
			qs.appendWhere(sc, new int[] { idx });
		}
		
		WTUser user = (WTUser)SessionHelper.manager.getPrincipal();
		
		if (qs.getConditionCount() > 0) {
			qs.appendAnd();
		}
		sc = new SearchCondition(MultiApproval.class, MultiApproval.OWNER + "." + "key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(user));
		qs.appendWhere(sc, new int[] { idx });
		
		//작성자
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
//			sc = new SearchCondition(new ClassAttribute(MultiApproval.class, MultiApproval.OWNER + "." + "key.id"), SearchCondition.IN, new ArrayExpression(userOidLongValueList.toArray()));
//			qs.appendWhere(sc, new int[] { idx });
//		}
		
		//상태
		if (state.size() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(MultiApproval.class, MultiApproval.STATE), SearchCondition.IN, new ArrayExpression(state.toArray()));
			qs.appendWhere(sc, new int[] { idx });
		}
		
		//구분
		if (objectTypeList.size() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(MultiApproval.class, MultiApproval.OBJECT_TYPE), SearchCondition.IN, new ArrayExpression(objectTypeList.toArray()));
			qs.appendWhere(sc, new int[] { idx });
		}
		
		//등록일
		if(predate.length() > 0){
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(MultiApproval.class, WTAttributeNameIfc.CREATE_STAMP_NAME ,SearchCondition.GREATER_THAN,DateUtil.convertStartDate(predate));
			qs.appendWhere(sc, new int[] { idx });
		}
		if(postdate.length() > 0){
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(MultiApproval.class, WTAttributeNameIfc.CREATE_STAMP_NAME,SearchCondition.LESS_THAN,DateUtil.convertEndDate(postdate));
			qs.appendWhere(sc, new int[] { idx });
		}
		
		
		
		
		//소팅
		if (sort.size() > 0) {
			String id = (String) sort.get("id");
			String dir = (String) sort.get("dir");
			
			String sortValue = "";
			if("objectTypeName".equals(id)) {
				sortValue = MultiApproval.OBJECT_TYPE;
			} else if("number".equals(id)) {
				sortValue = MultiApproval.NUMBER;
			} else if("name".equals(id)) {
				sortValue = MultiApproval.NAME;
			} else if("stateName".equals(id)) {
				sortValue = MultiApproval.STATE;
			} else if("ownerFullName".equals(id)) {
				sortValue = MultiApproval.OWNER;
			} else if("createDateFormat".equals(id)) {
				sortValue = MultiApproval.CREATE_TIMESTAMP;
			}
			
			if ("asc".equals(dir)) {
				qs.appendOrderBy(new OrderBy(new ClassAttribute(MultiApproval.class, sortValue), false), new int[] { idx });
			} else if("desc".equals(dir)) {
				qs.appendOrderBy(new OrderBy(new ClassAttribute(MultiApproval.class, sortValue), true), new int[] { idx });
			}
		} else {
			qs.appendOrderBy(new OrderBy(new ClassAttribute(MultiApproval.class, WTAttributeNameIfc.CREATE_STAMP_NAME), true), new int[] { idx });
		}

		
		
		return qs;
	}

	/**
	 * 
	 * @desc	: 일괄 결재 대상 링크 객체
	 * @author	: tsuam
	 * @date	: 2019. 8. 6.
	 * @method	: getMultiApprovalLinkList
	 * @return	: List<MultiApprovalObjectLink>
	 * @param multi
	 * @return
	 * @throws Exception
	 */
	public List<MultiApprovalObjectLink> getMultiApprovalLinkList(MultiApproval multi) throws Exception{
		
		List<MultiApprovalObjectLink> list = new ArrayList<MultiApprovalObjectLink>();
		
		QueryResult qr = PersistenceHelper.manager.navigate(multi, "obj", MultiApprovalObjectLink.class,false );
		LOGGER.info("getMultiApprovalLinkList qr =" + qr.size());
				
		while(qr.hasMoreElements()) {
			MultiApprovalObjectLink link = (MultiApprovalObjectLink)qr.nextElement();
			LOGGER.info("getMultiApprovalLinkList link =" + link);
			list.add(link);
		}
		
		return list;
	}
	
	/**
	 * 
	 * @desc	: 일괄 결재 대상 Object
	 * @author	: tsuam
	 * @date	: 2019. 8. 6.
	 * @method	: getMultiApprovalObjectList
	 * @return	: List<Persistable>
	 * @param multi
	 * @return
	 * @throws Exception
	 */
	public List<Persistable> getMultiApprovalObjectList(MultiApproval multi) throws Exception{
		
		List<Persistable> list = new ArrayList<Persistable>();
		
		QueryResult qr = PersistenceHelper.manager.navigate(multi, "obj", MultiApprovalObjectLink.class );
		
		while(qr.hasMoreElements()) {
			Persistable per = (Persistable)qr.nextElement();
			
			String vrOid = CommonUtil.getVROID(per);
			per = CommonUtil.getObject(vrOid);
			
			list.add(per);
		}
		
		return list;
	} 
	
	/**
	 * @desc : object로 MultiApproval 객체 찾기
	 * @author : sangylee
	 * @date : 2019. 12. 2.
	 * @method : getMultiApproval
	 * @return : MultiApproval
	 * @param per
	 * @throws Exception
	 */
	public MultiApproval getMultiApproval(Persistable per) throws Exception{
		
		MultiApproval multi = null;
		
		QueryResult qr = PersistenceHelper.manager.navigate(per, "multi", MultiApprovalObjectLink.class);
		
		if(qr.hasMoreElements()) {
			multi = (MultiApproval)qr.nextElement();
		}
		
		return multi;
	} 
	
	/**
	 * 
	 * @desc	: 일괄 결재 대상 Object 리스트
	 * @author	: tsuam
	 * @date	: 2019. 8. 7.
	 * @method	: getRelatedObject
	 * @return	: List<RevisionData>
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public List<RevisionData> getRelatedObject(Map<String, Object> reqMap) throws Exception {
		
		List<RevisionData> datalist = new ArrayList<RevisionData>();
		
		String oid = (String)reqMap.get("oid");
		MultiApproval multi = (MultiApproval)CommonUtil.getObject(oid);		
		LOGGER.info("getRelatedObject oid =" + oid);
		List<Persistable> list = getMultiApprovalObjectList(multi);
		LOGGER.info("getRelatedObject list =" + list.size());
		for(Persistable per : list) {
			RevisionControlled rv = (RevisionControlled)per;
			String vrOid = CommonUtil.getVROID(rv);
			rv = (RevisionControlled)CommonUtil.getObject(vrOid);
			RevisionData data = new RevisionData(rv);
			if(rv instanceof EPMDocument) {
				data.setNumber(((EPMDocument)rv).getNumber());
			}else if(rv instanceof WTDocument) {
				data.setNumber(((WTDocument)rv).getNumber());
			}else if(rv instanceof WTPart) {
				data.setNumber(((WTPart)rv).getNumber());
			}
			
			datalist.add(data);
		}
		
		return datalist;
	}
	
	public static void main(String[] args) {
		String oid = "com.e3ps.approval.MultiApproval:460502";
		MultiApproval multi = (MultiApproval)CommonUtil.getObject(oid);
		
		try {
			MultiApprovalHelper.manager.getMultiApprovalLinkList(multi);
			MultiApprovalHelper.manager.getMultiApprovalObjectList(multi);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
