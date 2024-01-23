package com.e3ps.common.history.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.e3ps.common.history.DownloadHistory;
import com.e3ps.common.history.ERPHistory;
import com.e3ps.common.history.LoginHistory;
import com.e3ps.common.history.bean.DownloadHistoryData;
import com.e3ps.common.history.bean.ERPHistoryData;
import com.e3ps.common.history.bean.LoginHistoryData;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.web.PageQueryBroker;
import com.e3ps.doc.E3PSDocument;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;

import wt.fc.PagingQueryResult;
import wt.fc.PagingSessionHelper;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.pds.StatementSpec;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.ServiceFactory;

public class HistoryHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	public static final HistoryService service = ServiceFactory.getService(HistoryService.class);
	
	public static final HistoryHelper manager = new HistoryHelper();
	
	public List<LoginHistoryData> getLoginHistory(Map<String, Object> reqMap) throws Exception {
		
		List<LoginHistoryData> list = new ArrayList<>();
		
		QuerySpec qs = getLoginHistoryQuery(reqMap);
		
		QueryResult qr = PersistenceHelper.manager.find((StatementSpec)qs);
		
		while(qr.hasMoreElements()){
			Object[] obj = (Object[]) qr.nextElement();
			LoginHistory history = (LoginHistory) obj[0];
			
			LoginHistoryData data = new LoginHistoryData(history);
			
			list.add(data);
		}
		
		return list;
	}
	
	public QuerySpec getLoginHistoryQuery(Map<String, Object> reqMap) throws Exception {
		
		String id = StringUtil.checkNull((String) reqMap.get("id"));
		String name = StringUtil.checkNull((String) reqMap.get("name"));
		String predate = StringUtil.checkNull((String) reqMap.get("predate"));
		String postdate = StringUtil.checkNull((String) reqMap.get("postdate"));
		
		QuerySpec query = new QuerySpec();
		int idx = query.addClassList(LoginHistory.class, true);
		
		SearchCondition sc = null;
		
		//ID
		if(id.length() > 0) {
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(LoginHistory.class, LoginHistory.ID, SearchCondition.LIKE, "%" + id + "%", false);
			query.appendWhere(sc, new int[] { idx });
		}
		
		//이름
		if(name.length() > 0) {
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(LoginHistory.class, LoginHistory.NAME, SearchCondition.LIKE, "%" + name + "%", false);
			query.appendWhere(sc, new int[] { idx });
		}
		
		//접속일
    	if(predate.length() > 0){
    		if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
    		sc = new SearchCondition(LoginHistory.class, LoginHistory.CON_TIME ,SearchCondition.GREATER_THAN, DateUtil.convertStartDate(predate).toString());
    		query.appendWhere(sc, new int[] { idx });
    	}
    	if(postdate.length() > 0){
    		if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
    		
    		sc = new SearchCondition(LoginHistory.class, LoginHistory.CON_TIME,SearchCondition.LESS_THAN, DateUtil.convertEndDate(postdate).toString());
    		query.appendWhere(sc, new int[] { idx });
    	}
    	
    	query.appendOrderBy(new OrderBy(new ClassAttribute(LoginHistory.class, LoginHistory.CON_TIME), true), new int[] { idx });
    	
    	return query;
	}
	
	/**
	 * @desc	: DownloadHistory 검색 결과 가져오기
	 * @author	: tsjeong
	 * @date	: 2019. 11. 15.
	 * @method	: getDownloadHistory
	 * @return	: List<DownloadHistoryData>
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public List<DownloadHistoryData> getDownloadHistory(Map<String, Object> reqMap) throws Exception {
		
		List<DownloadHistoryData> list = new ArrayList<>();
		
		QuerySpec qs = getDownloadHistoryQuery(reqMap);
		
		QueryResult qr = PersistenceHelper.manager.find((StatementSpec)qs);
		
		while(qr.hasMoreElements()){
			Object[] obj = (Object[]) qr.nextElement();
			DownloadHistory history = (DownloadHistory) obj[0];
			
			DownloadHistoryData data = new DownloadHistoryData(history);
			
			list.add(data);
		}
		
		return list;
	}
	
	/**
	 * @desc	: DownloadHistory 검색 쿼리
	 * @author	: tsjeong
	 * @date	: 2019. 11. 15.
	 * @method	: getDownloadHistoryQuery
	 * @return	: QuerySpec
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public QuerySpec getDownloadHistoryQuery(Map<String, Object> reqMap) throws Exception {
		
		List<String> module = StringUtil.checkReplaceArray(reqMap.get("module"));
		
		String id = StringUtil.checkNull((String) reqMap.get("id"));
		String name = StringUtil.checkNull((String) reqMap.get("name"));
		String predate = StringUtil.checkNull((String) reqMap.get("predate"));
		String postdate = StringUtil.checkNull((String) reqMap.get("postdate"));
		
		QuerySpec query = new QuerySpec();
		int idx = query.addClassList(DownloadHistory.class, true);
		
		SearchCondition sc = null;
		
		//ID
		if(module.size() > 0) {
			int index = 0;
			for(String moduleSearch : module){
				if(index==0){
					if(query.getConditionCount() > 0) {
						query.appendAnd();
						query.appendOpenParen();
					}else {
						query.appendOpenParen();
					}
				}else{
					if(query.getConditionCount() > 0) {
						query.appendOr();
					}
				}
				if(moduleSearch.equals("ECR")){
					sc = new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, SearchCondition.LIKE, "%" + "E3PSEChangeRequest" + "%", false);
					query.appendWhere(sc, new int[] { idx });
				}
				if(moduleSearch.equals("ECO")){
					sc = new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, SearchCondition.LIKE, "%" + "E3PSEChangeOrder" + "%", false);
					query.appendWhere(sc, new int[] { idx });
				}
				if(moduleSearch.equals("ECN")){
					sc = new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, SearchCondition.LIKE, "%" + "E3PSEChangeNotice" + "%", false);
					query.appendWhere(sc, new int[] { idx });
				}
				if(moduleSearch.equals("도면")){
					sc = new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, SearchCondition.LIKE, "%" + "EPMDocument" + "%", false);
					query.appendWhere(sc, new int[] { idx });
				}
				if(moduleSearch.equals("부품")){
					sc = new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, SearchCondition.LIKE, "%" + "WTPart" + "%", false);
					query.appendWhere(sc, new int[] { idx });
				}
				if(moduleSearch.equals("배포")){
					sc = new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, SearchCondition.LIKE, "%" + "DistributeDocument" + "%", false);
					query.appendWhere(sc, new int[] { idx });
				}
				if(moduleSearch.equals("문서")){
					sc = new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, SearchCondition.LIKE, "%" + "E3PSDocument" + "%", false);
					query.appendWhere(sc, new int[] { idx });
				}
				
				if(index==(module.size()-1)){
					query.appendCloseParen();
				}
				index ++;
			}
			query.appendOrderBy(new OrderBy(new ClassAttribute(DownloadHistory.class, DownloadHistory.DOWNLOAD_DATE), true), new int[] { idx });
		}
		
		if(id.length() > 0 || name.length() > 0) {
			
			int idx_user = query.addClassList(WTUser.class, true);
			
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			
			sc = new SearchCondition(DownloadHistory.class, DownloadHistory.USER_REFERENCE + ".key.id", WTUser.class, "thePersistInfo.theObjectIdentifier.id");
			query.appendWhere(sc, new int[] { idx, idx_user });
			
			//ID
			if(id.length() > 0) {
				if(query.getConditionCount() > 0) {
					query.appendAnd();
				}
				sc = new SearchCondition(WTUser.class, WTUser.NAME, SearchCondition.LIKE, "%" + id + "%", false);
				query.appendWhere(sc, new int[] { idx_user });
			}
			
			//이름
			if(name.length() > 0) {
				if(query.getConditionCount() > 0) {
					query.appendAnd();
				}
				sc = new SearchCondition(WTUser.class, WTUser.FULL_NAME, SearchCondition.LIKE, "%" + name + "%", false);
				query.appendWhere(sc, new int[] { idx_user });
			}
		}
		
		
		//등록일
    	if(predate.length() > 0){
    		if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
    		sc = new SearchCondition(DownloadHistory.class, DownloadHistory.DOWNLOAD_DATE, SearchCondition.GREATER_THAN, DateUtil.convertStartDate(predate));
    		query.appendWhere(sc, new int[] { idx });
    	}
    	if(postdate.length() > 0){
    		if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
    		sc = new SearchCondition(DownloadHistory.class, DownloadHistory.DOWNLOAD_DATE, SearchCondition.LESS_THAN, DateUtil.convertEndDate(postdate));
    		query.appendWhere(sc, new int[] { idx });
    	}
    	
    	query.appendOrderBy(new OrderBy(new ClassAttribute(DownloadHistory.class, DownloadHistory.DOWNLOAD_DATE), true), new int[] { idx });
    	
    	return query;
	}
	
	/**
	 * @desc	: 로그인 이력 검색 리스트, 스크롤 액션
	 * @author	: tsjeong
	 * @date	: 2019. 12. 3.
	 * @method	: getLoginHistoryScrollList
	 * @return	: Map<String,Object>
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getLoginHistoryScrollList(Map<String, Object> reqMap) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
		
		List<LoginHistoryData> list = new ArrayList<>();
		
		int page = (Integer) reqMap.get("page");
		int rows = (Integer) reqMap.get("rows");
		String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));
		
		PagingQueryResult result = null;
		
		if (sessionId.length() > 0) {
			result = PagingSessionHelper.fetchPagingSession((page - 1) * rows, rows, Long.valueOf(sessionId));
		} else {
			QuerySpec query = getLoginHistoryListQuery(reqMap);
			
			result = PageQueryBroker.openPagingSession((page - 1) * rows, rows, query, true);
		}
		
		int totalSize = result.getTotalSize();
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			LoginHistory history = (LoginHistory) obj[0];
			
			LoginHistoryData data = new LoginHistoryData(history);
			
			list.add(data);
		}
		
		map.put("list", list);
		map.put("totalSize", totalSize);
		map.put("sessionId", result.getSessionId() == 0 ? "" : result.getSessionId());
		
		return map;
	}
	
	public QuerySpec getLoginHistoryListQuery(Map<String, Object> reqMap) throws Exception {
		
		String id = StringUtil.checkNull((String) reqMap.get("id"));
		String name = StringUtil.checkNull((String) reqMap.get("name"));
		String predate = StringUtil.checkNull((String) reqMap.get("predate"));
		String postdate = StringUtil.checkNull((String) reqMap.get("postdate"));
		
		//Sort
		boolean sortCheck = true;
		if(reqMap.get("sortCheck") != null){
			sortCheck = (boolean) reqMap.get("sortCheck");
		}
		String sortValue = StringUtil.checkNull((String) reqMap.get("sortValue"));
		
		QuerySpec query = new QuerySpec();
		int idx = query.addClassList(LoginHistory.class, true);
			
		SearchCondition sc = null;
		
		//ID
		if(id.length() > 0) {
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(LoginHistory.class, LoginHistory.ID, SearchCondition.LIKE, "%" + id + "%", false);
			query.appendWhere(sc, new int[] { idx });
		}
		
		//이름
		if(name.length() > 0) {
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(LoginHistory.class, LoginHistory.NAME, SearchCondition.LIKE, "%" + name + "%", false);
			query.appendWhere(sc, new int[] { idx });
		}
		
		//접속일
    	if(predate.length() > 0){
    		if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
    		sc = new SearchCondition(LoginHistory.class, "thePersistInfo.createStamp" ,SearchCondition.GREATER_THAN, DateUtil.convertStartDate(predate));
    		query.appendWhere(sc, new int[] { idx });
    	}
    	if(postdate.length() > 0){
    		if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
    		
    		sc = new SearchCondition(LoginHistory.class, "thePersistInfo.createStamp",SearchCondition.LESS_THAN, DateUtil.convertEndDate(postdate));
    		query.appendWhere(sc, new int[] { idx });
    	}
    	
    	//소팅
    	if (sortValue.length() > 0) {
    		if (sortCheck) {
    			query.appendOrderBy(new OrderBy(new ClassAttribute(LoginHistory.class, sortValue), false), new int[] { idx });
    		} else {
    			query.appendOrderBy(new OrderBy(new ClassAttribute(LoginHistory.class, sortValue), true), new int[] { idx });
    		}
    	} else {
    		query.appendOrderBy(new OrderBy(new ClassAttribute(LoginHistory.class, "thePersistInfo.createStamp"), true), new int[] { idx });
    	}
    	
    	return query;
	}
	
	/**
	 * @desc	: 로그인 이력 검색 리스트, 스크롤 액션
	 * @author	: tsjeong
	 * @date	: 2020. 09. 07.
	 * @method	: getLoginHistoryScrollList2
	 * @return	: Map<String,Object>
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getLoginHistoryScrollList2(Map<String, Object> reqMap) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
		
		List<LoginHistoryData> list = new ArrayList<>();
		
		int start = (Integer) reqMap.get("start");
		int count = (Integer) reqMap.get("count");
		String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));
		
		PagingQueryResult result = null;
		
		if (sessionId.length() > 0) {
			result = PagingSessionHelper.fetchPagingSession(start, count, Long.valueOf(sessionId));
		} else {
			QuerySpec query = getLoginHistoryListQuery2(reqMap);
			
			result = PageQueryBroker.openPagingSession(start, count, query, true);
		}
		
		int totalSize = result.getTotalSize();
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			LoginHistory history = (LoginHistory) obj[0];
			
			LoginHistoryData data = new LoginHistoryData(history);
			
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
	
	public QuerySpec getLoginHistoryListQuery2(Map<String, Object> reqMap) throws Exception {
		//소팅
		Map<String, Object> sort = reqMap.get("sort") != null ? (Map<String, Object>) reqMap.get("sort") : new HashMap<>();
		
		String userId = StringUtil.checkNull((String) reqMap.get("userId"));
		String name = StringUtil.checkNull((String) reqMap.get("name"));
		String predate = StringUtil.checkNull((String) reqMap.get("predate"));
		String postdate = StringUtil.checkNull((String) reqMap.get("postdate"));
		
		QuerySpec query = new QuerySpec();
		int idx = query.addClassList(LoginHistory.class, true);
		
		SearchCondition sc = null;
		
		//ID
		if(userId.length() > 0) {
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(LoginHistory.class, LoginHistory.ID, SearchCondition.LIKE, "%" + userId + "%", false);
			query.appendWhere(sc, new int[] { idx });
		}
		
		//이름
		if(name.length() > 0) {
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(LoginHistory.class, LoginHistory.NAME, SearchCondition.LIKE, "%" + name + "%", false);
			query.appendWhere(sc, new int[] { idx });
		}
		
		//접속일
		if(predate.length() > 0){
			if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(LoginHistory.class, "thePersistInfo.createStamp" ,SearchCondition.GREATER_THAN, DateUtil.convertStartDate(predate));
			query.appendWhere(sc, new int[] { idx });
		}
		if(postdate.length() > 0){
			if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
			
			sc = new SearchCondition(LoginHistory.class, "thePersistInfo.createStamp",SearchCondition.LESS_THAN, DateUtil.convertEndDate(postdate));
			query.appendWhere(sc, new int[] { idx });
		}
		
		// 소팅
		if (sort.size() > 0) {
			String id = (String) sort.get("id");
			String dir = (String) sort.get("dir");
					
			String sortValue = "";
			if("id".equals(id)) {
				sortValue = LoginHistory.ID;
			} else if("name".equals(id)) {
				sortValue = LoginHistory.NAME;
			} else if("conTime".equals(id)) {
				sortValue = LoginHistory.CON_TIME;
			} else if("ip".equals(id)) {
				sortValue = LoginHistory.IP;
			} else if("browser".equals(id)) {
				sortValue = LoginHistory.BROWSER;
			}
					
			if ("asc".equals(dir)) {
				query.appendOrderBy(new OrderBy(new ClassAttribute(LoginHistory.class, sortValue), false), new int[] { idx });
			} else if("desc".equals(dir)) {
				query.appendOrderBy(new OrderBy(new ClassAttribute(LoginHistory.class, sortValue), true), new int[] { idx });
			}
		} else {
			query.appendOrderBy(new OrderBy(new ClassAttribute(LoginHistory.class, LoginHistory.CON_TIME), true), new int[] { idx });
		}
		
		return query;
	}
	
	/**
	 * @desc	: 다운로드 이력 검색 리스트, 스크롤 액션
	 * @author	: tsjeong
	 * @date	: 2019. 12. 2.
	 * @method	: getDownloadHistoryScrollList
	 * @return	: List<DownloadHistoryData>
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getDownloadHistoryScrollList(Map<String, Object> reqMap) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
		
		List<DownloadHistoryData> list = new ArrayList<>();
		
		int page = (Integer) reqMap.get("page");
		int rows = (Integer) reqMap.get("rows");
		String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));
		
		PagingQueryResult result = null;
		
		if (sessionId.length() > 0) {
			result = PagingSessionHelper.fetchPagingSession((page - 1) * rows, rows, Long.valueOf(sessionId));
		} else {
			QuerySpec query = getDownloadHistoryListQuery(reqMap);
			
			result = PageQueryBroker.openPagingSession((page - 1) * rows, rows, query, true);
		}
		
		int totalSize = result.getTotalSize();
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			DownloadHistory history = (DownloadHistory) obj[0];
			
			DownloadHistoryData data = new DownloadHistoryData(history);
			
			list.add(data);
		}
		
		map.put("list", list);
		map.put("totalSize", totalSize);
		map.put("sessionId", result.getSessionId() == 0 ? "" : result.getSessionId());
		
		return map;
	}
	
	/**
	 * @desc	: DownloadHistory 검색 결과 가져오는 쿼리 실행
	 * @author	: tsjeong
	 * @date	: 2019. 12. 2.
	 * @method	: getDownloadHistoryListQuery
	 * @return	: QuerySpec
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public QuerySpec getDownloadHistoryListQuery(Map<String, Object> reqMap) throws Exception {
		
		List<String> module = StringUtil.checkReplaceArray(reqMap.get("module"));
		
		String id = StringUtil.checkNull((String) reqMap.get("id"));
		String name = StringUtil.checkNull((String) reqMap.get("name"));
		String predate = StringUtil.checkNull((String) reqMap.get("predate"));
		String postdate = StringUtil.checkNull((String) reqMap.get("postdate"));
		
		//Sort
		boolean sortCheck = true;
		if(reqMap.get("sortCheck") != null){
			sortCheck = (boolean) reqMap.get("sortCheck");
		}
		String sortValue = StringUtil.checkNull((String) reqMap.get("sortValue"));
		
		QuerySpec query = new QuerySpec();
		int idx = query.addClassList(DownloadHistory.class, true);
		
		SearchCondition sc = null;
		
		//ID
		if(module.size() > 0) {
			int index = 0;
			for(String moduleSearch : module){
				if(index==0){
					if(query.getConditionCount() > 0) {
						query.appendAnd();
						query.appendOpenParen();
					}else {
						query.appendOpenParen();
					}
				}else{
					if(query.getConditionCount() > 0) {
						query.appendOr();
					}
				}
				if(moduleSearch.equals("ECR")){
					sc = new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, SearchCondition.LIKE, "%" + "E3PSEChangeRequest" + "%", false);
					query.appendWhere(sc, new int[] { idx });
				}
				if(moduleSearch.equals("ECO")){
					sc = new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, SearchCondition.LIKE, "%" + "E3PSEChangeOrder" + "%", false);
					query.appendWhere(sc, new int[] { idx });
				}
				if(moduleSearch.equals("ECN")){
					sc = new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, SearchCondition.LIKE, "%" + "E3PSEChangeNotice" + "%", false);
					query.appendWhere(sc, new int[] { idx });
				}
				if(moduleSearch.equals("도면")){
					sc = new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, SearchCondition.LIKE, "%" + "EPMDocument" + "%", false);
					query.appendWhere(sc, new int[] { idx });
				}
				if(moduleSearch.equals("부품")){
					sc = new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, SearchCondition.LIKE, "%" + "WTPart" + "%", false);
					query.appendWhere(sc, new int[] { idx });
				}
				if(moduleSearch.equals("배포")){
					sc = new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, SearchCondition.LIKE, "%" + "DistributeDocument" + "%", false);
					query.appendWhere(sc, new int[] { idx });
				}
				if(moduleSearch.equals("문서")){
					sc = new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, SearchCondition.LIKE, "%" + "E3PSDocument" + "%", false);
					query.appendWhere(sc, new int[] { idx });
				}
				
				if(index==(module.size()-1)){
					query.appendCloseParen();
				}
				index ++;
			}
			query.appendOrderBy(new OrderBy(new ClassAttribute(DownloadHistory.class, DownloadHistory.DOWNLOAD_DATE), true), new int[] { idx });
		}
		
		if(id.length() > 0 || name.length() > 0) {
			
			int idx_user = query.addClassList(WTUser.class, true);
			
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			
			sc = new SearchCondition(DownloadHistory.class, DownloadHistory.USER_REFERENCE + ".key.id", WTUser.class, "thePersistInfo.theObjectIdentifier.id");
			query.appendWhere(sc, new int[] { idx, idx_user });
			
			//ID
			if(id.length() > 0) {
				if(query.getConditionCount() > 0) {
					query.appendAnd();
				}
				sc = new SearchCondition(WTUser.class, WTUser.NAME, SearchCondition.LIKE, "%" + id + "%", false);
				query.appendWhere(sc, new int[] { idx_user });
			}
			
			//이름
			if(name.length() > 0) {
				if(query.getConditionCount() > 0) {
					query.appendAnd();
				}
				sc = new SearchCondition(WTUser.class, WTUser.FULL_NAME, SearchCondition.LIKE, "%" + name + "%", false);
				query.appendWhere(sc, new int[] { idx_user });
			}
		}

		//등록일
    	if(predate.length() > 0){
    		if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
    		sc = new SearchCondition(DownloadHistory.class, DownloadHistory.DOWNLOAD_DATE, SearchCondition.GREATER_THAN, DateUtil.convertStartDate(predate));
    		query.appendWhere(sc, new int[] { idx });
    	}
    	if(postdate.length() > 0){
    		if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
    		sc = new SearchCondition(DownloadHistory.class, DownloadHistory.DOWNLOAD_DATE, SearchCondition.LESS_THAN, DateUtil.convertEndDate(postdate));
    		query.appendWhere(sc, new int[] { idx });
    	}
    	
    	//소팅
    	if (sortValue.length() > 0) {
    		if (sortCheck) {
    			query.appendOrderBy(new OrderBy(new ClassAttribute(DownloadHistory.class, sortValue), false), new int[] { idx });
    		} else {
    			query.appendOrderBy(new OrderBy(new ClassAttribute(DownloadHistory.class, sortValue), true), new int[] { idx });
    		}
    	} else {
    		query.appendOrderBy(new OrderBy(new ClassAttribute(DownloadHistory.class, DownloadHistory.DOWNLOAD_DATE), true), new int[] { idx });
    	}
    	
    	return query;
	}
	
	/**
	 * @desc	: 다운로드 이력 검색 리스트, 스크롤 액션
	 * @author	: tsjeong
	 * @date	: 2020. 09. 07.
	 * @method	: getDownloadHistoryScrollList2
	 * @return	: List<DownloadHistoryData>
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getDownloadHistoryScrollList2(Map<String, Object> reqMap) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
		
		List<DownloadHistoryData> list = new ArrayList<>();
		
		int start = (Integer) reqMap.get("start");
		int count = (Integer) reqMap.get("count");
		String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));
		
		PagingQueryResult result = null;
		
		if (sessionId.length() > 0) {
			result = PagingSessionHelper.fetchPagingSession(start, count, Long.valueOf(sessionId));
		} else {
			QuerySpec query = getDownloadHistoryListQuery2(reqMap);
			
			result = PageQueryBroker.openPagingSession(start, count, query, true);
		}
		
		int totalSize = result.getTotalSize();
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			DownloadHistory history = (DownloadHistory) obj[0];
			
			DownloadHistoryData data = new DownloadHistoryData(history);
			
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
	
	/**
	 * @desc	: DownloadHistory 검색 결과 가져오는 쿼리 실행
	 * @author	: tsjeong
	 * @date	: 2019. 12. 2.
	 * @method	: getDownloadHistoryListQuery
	 * @return	: QuerySpec
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public QuerySpec getDownloadHistoryListQuery2(Map<String, Object> reqMap) throws Exception {
		//소팅
		Map<String, Object> sort = reqMap.get("sort") != null ? (Map<String, Object>) reqMap.get("sort") : new HashMap<>();
		
		List<String> module = StringUtil.checkReplaceArray(reqMap.get("module"));
		String userId = StringUtil.checkNull((String) reqMap.get("userId"));
		String name = StringUtil.checkNull((String) reqMap.get("name"));
		String predate = StringUtil.checkNull((String) reqMap.get("predate"));
		String postdate = StringUtil.checkNull((String) reqMap.get("postdate"));
		
		QuerySpec query = new QuerySpec();
		int idx = query.addClassList(DownloadHistory.class, true);
		
		SearchCondition sc = null;
		
		//ID
		if(module.size() > 0) {
			int index = 0;
			for(String moduleSearch : module){
				if(index==0){
					if(query.getConditionCount() > 0) {
						query.appendAnd();
						query.appendOpenParen();
					}else {
						query.appendOpenParen();
					}
				}else{
					if(query.getConditionCount() > 0) {
						query.appendOr();
					}
				}
				if(moduleSearch.equals("ECR")){
					sc = new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, SearchCondition.LIKE, "%" + "E3PSEChangeRequest" + "%", false);
					query.appendWhere(sc, new int[] { idx });
				}
				if(moduleSearch.equals("ECO")){
					sc = new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, SearchCondition.LIKE, "%" + "E3PSEChangeOrder" + "%", false);
					query.appendWhere(sc, new int[] { idx });
				}
				if(moduleSearch.equals("도면")){
					sc = new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, SearchCondition.LIKE, "%" + "EPMDocument" + "%", false);
					query.appendWhere(sc, new int[] { idx });
				}
				if(moduleSearch.equals("부품")){
					sc = new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, SearchCondition.LIKE, "%" + "WTPart" + "%", false);
					query.appendWhere(sc, new int[] { idx });
				}
				if(moduleSearch.equals("배포")){
					sc = new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, SearchCondition.LIKE, "%" + "DistributeDocument" + "%", false);
					query.appendWhere(sc, new int[] { idx });
				}
				if(moduleSearch.equals("문서")){
					sc = new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, SearchCondition.LIKE, "%" + "E3PSDocument" + "%", false);
					query.appendWhere(sc, new int[] { idx });
				}
				if(moduleSearch.equals("이슈")){
					sc = new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, SearchCondition.LIKE, "%" + "IssueReqeust" + "%", false);
					query.appendWhere(sc, new int[] { idx });
				}
				if(moduleSearch.equals("이슈방안")){
					sc = new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, SearchCondition.LIKE, "%" + "IssueSolution" + "%", false);
					query.appendWhere(sc, new int[] { idx });
				}
				if(moduleSearch.equals("공지")){
					sc = new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, SearchCondition.LIKE, "%" + "Notice" + "%", false);
					query.appendWhere(sc, new int[] { idx });
				}
				
				if(index==(module.size()-1)){
					query.appendCloseParen();
				}
				index ++;
			}
//			query.appendOrderBy(new OrderBy(new ClassAttribute(DownloadHistory.class, DownloadHistory.DOWNLOAD_DATE), true), new int[] { idx });
		}
		
		if(userId.length() > 0 || name.length() > 0) {
			
			int idx_user = query.addClassList(WTUser.class, true);
			
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			
			sc = new SearchCondition(DownloadHistory.class, DownloadHistory.USER_REFERENCE + ".key.id", WTUser.class, "thePersistInfo.theObjectIdentifier.id");
			query.appendWhere(sc, new int[] { idx, idx_user });
			
			//ID
			if(userId.length() > 0) {
				if(query.getConditionCount() > 0) {
					query.appendAnd();
				}
				sc = new SearchCondition(WTUser.class, WTUser.NAME, SearchCondition.LIKE, "%" + userId + "%", false);
				query.appendWhere(sc, new int[] { idx_user });
			}
			
			//이름
			if(name.length() > 0) {
				if(query.getConditionCount() > 0) {
					query.appendAnd();
				}
				sc = new SearchCondition(WTUser.class, WTUser.FULL_NAME, SearchCondition.LIKE, "%" + name + "%", false);
				query.appendWhere(sc, new int[] { idx_user });
			}
		}
		
		//등록일
		if(predate.length() > 0){
			if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(DownloadHistory.class, DownloadHistory.DOWNLOAD_DATE, SearchCondition.GREATER_THAN, DateUtil.convertStartDate(predate));
			query.appendWhere(sc, new int[] { idx });
		}
		if(postdate.length() > 0){
			if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(DownloadHistory.class, DownloadHistory.DOWNLOAD_DATE, SearchCondition.LESS_THAN, DateUtil.convertEndDate(postdate));
			query.appendWhere(sc, new int[] { idx });
		}
		
		// 소팅
		if (sort.size() > 0) {
			String id = (String) sort.get("id");
			String dir = (String) sort.get("dir");
					
			String sortValue = "";
			if("fName".equals(id)) {
				sortValue = DownloadHistory.F_NAME;
			}
					
			if ("asc".equals(dir)) {
				query.appendOrderBy(new OrderBy(new ClassAttribute(DownloadHistory.class, sortValue), false), new int[] { idx });
			} else if("desc".equals(dir)) {
				query.appendOrderBy(new OrderBy(new ClassAttribute(DownloadHistory.class, sortValue), true), new int[] { idx });
			}
		} else {
			query.appendOrderBy(new OrderBy(new ClassAttribute(DownloadHistory.class, DownloadHistory.DOWNLOAD_DATE), true), new int[] { idx });
		}
		return query;
	}
	
	/**
	 * @desc	: erp 전송 이력
	 * @author	: gs
	 * @date	: 2020. 09. 07.
	 * @method	: getDownloadHistoryScrollList2
	 * @return	: List<DownloadHistoryData>
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getErpHistory(Map<String, Object> reqMap) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
		
		List<ERPHistoryData> list = new ArrayList<>();
		
		int start = (Integer) reqMap.get("start");
		int count = (Integer) reqMap.get("count");
		String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));
		
		PagingQueryResult result = null;
		
		if (sessionId.length() > 0) {
			result = PagingSessionHelper.fetchPagingSession(start, count, Long.valueOf(sessionId));
		} else {
			QuerySpec query = getErpHistoryListQuery(reqMap);
			
			result = PageQueryBroker.openPagingSession(start, count, query, true);
		}
		
		int totalSize = result.getTotalSize();
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			ERPHistory history = (ERPHistory) obj[0];
			
			ERPHistoryData data = new ERPHistoryData(history);
			
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
	
	/**
	 * @desc	: getErpHistoryListQuery 검색 결과 가져오는 쿼리 실행
	 * @author	: hckim
	 * @date	: 2020. 1. 7.
	 * @method	: getErpHistoryListQuery
	 * @return	: QuerySpec
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public QuerySpec getErpHistoryListQuery(Map<String, Object> reqMap) throws Exception {
		//소팅
		Map<String, Object> sort = reqMap.get("sort") != null ? (Map<String, Object>) reqMap.get("sort") : new HashMap<>();
		
		String state = StringUtil.checkNull((String) reqMap.get("state"));
		String sendType = StringUtil.checkNull((String) reqMap.get("sendType"));
		String sender = StringUtil.checkNull((String) reqMap.get("sender"));
		String msg = StringUtil.checkNull((String) reqMap.get("msg"));
		String predate = StringUtil.checkNull((String) reqMap.get("predate"));
		String postdate = StringUtil.checkNull((String) reqMap.get("postdate"));
		String sendObjNumber = StringUtil.checkNull((String) reqMap.get("sendObjNumber"));
		QuerySpec query = new QuerySpec();
		
		int idx = query.addClassList(ERPHistory.class, true);
		
		SearchCondition sc = null;
		if(sendObjNumber.length() > 0) {
    		if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
    		sc = new SearchCondition(ERPHistory.class, ERPHistory.MSG, SearchCondition.LIKE, sendObjNumber+"%");
    		query.appendWhere(sc, new int[]{idx});
		}
		if(state.length() > 0) {
    		if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
    		sc = new SearchCondition(ERPHistory.class, ERPHistory.STATE, SearchCondition.LIKE, "%"+state+"%");
    		query.appendWhere(sc, new int[]{idx});
		}
		if(sendType.length() > 0) {
    		if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
    		sc = new SearchCondition(ERPHistory.class, ERPHistory.SEND_TYPE, SearchCondition.EQUAL, sendType);
    		query.appendWhere(sc, new int[]{idx});
		}
		if(sender.length() > 0) {
    		if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
    		sc = new SearchCondition(ERPHistory.class, ERPHistory.SENDER, SearchCondition.LIKE, sender+"%");
    		query.appendWhere(sc, new int[]{idx});
		}
		if(msg.length() > 0) {
    		if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
    		sc = new SearchCondition(ERPHistory.class, ERPHistory.MSG, SearchCondition.LIKE, "%"+msg+"%");
    		query.appendWhere(sc, new int[]{idx});
		}
		if(predate.length() > 0) {
    		if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
    		sc = new SearchCondition(ERPHistory.class, "thePersistInfo.createStamp", SearchCondition.GREATER_THAN_OR_EQUAL, DateUtil.convertStartDate(predate));
    		query.appendWhere(sc, new int[]{idx});
		}
		if(postdate.length() > 0) {
    		if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
    		sc = new SearchCondition(ERPHistory.class, "thePersistInfo.createStamp", SearchCondition.LESS_THAN_OR_EQUAL, DateUtil.convertStartDate(postdate));
    		query.appendWhere(sc, new int[]{idx});
		}
		
		if (sort.size() > 0) {
			String id = (String) sort.get("id");
			String dir = (String) sort.get("dir");
			
			String sortValue = "";
			if("sendTime".equals(id)) {
				sortValue = "thePersistInfo.createStamp";
			}
			
			if ("asc".equals(dir)) {
				query.appendOrderBy(new OrderBy(new ClassAttribute(ERPHistory.class, sortValue), false), new int[] { idx });
			} else if ("desc".equals(dir)) {
				query.appendOrderBy(new OrderBy(new ClassAttribute(ERPHistory.class, sortValue), true), new int[] { idx });
			}
			
		} else {
			query.appendOrderBy(new OrderBy(new ClassAttribute(ERPHistory.class, "thePersistInfo.createStamp"), true), new int[] { idx });
		}
		
		return query;
	}
}

