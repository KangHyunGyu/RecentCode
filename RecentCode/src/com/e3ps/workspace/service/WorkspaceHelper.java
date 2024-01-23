package com.e3ps.workspace.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.web.PageQueryBroker;
import com.e3ps.doc.E3PSDocument;
import com.e3ps.org.People;
import com.e3ps.workspace.bean.NoticeData;
import com.e3ps.workspace.notice.Notice;

import wt.fc.PagingQueryResult;
import wt.fc.PagingSessionHelper;
import wt.org.WTUser;
import wt.query.ArrayExpression;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.ServiceFactory;

public class WorkspaceHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.WORKSPACE.getName());	
	public static final WorkspaceHelper manager = new WorkspaceHelper();

	public static final WorkspaceService service = ServiceFactory.getService(WorkspaceService.class);
	
	public Map<String, Object> getNoticeList(Map<String, Object> reqMap) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
		
		List<NoticeData> list = new ArrayList<>();
		
		int page = (Integer) reqMap.get("page");
		int rows = (Integer) reqMap.get("rows");
		String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));
    	
		PagingQueryResult result = null;
		
		if (sessionId.length() > 0) {
			result = PagingSessionHelper.fetchPagingSession((page - 1) * rows, rows, Long.valueOf(sessionId));
		} else {
			QuerySpec query = getNoticeListQuery(reqMap);
			
			result = PageQueryBroker.openPagingSession((page - 1) * rows, rows, query, true);
		}
		
		int totalSize = result.getTotalSize();
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			Notice notice = (Notice) obj[0];
			
			NoticeData data = new NoticeData(notice);
			
			list.add(data);
		}
		
		map.put("list", list);
		map.put("totalSize", totalSize);
		map.put("sessionId", result.getSessionId() == 0 ? "" : result.getSessionId());
		
		return map;
	}
	
	public Map<String, Object> getNoticeList2(Map<String, Object> reqMap) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
		
		List<NoticeData> list = new ArrayList<>();
		
		int start = (Integer) reqMap.get("start");
		int count = (Integer) reqMap.get("count");
		String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));
		
		PagingQueryResult result = null;
		
		if (sessionId.length() > 0) {
			result = PagingSessionHelper.fetchPagingSession(start, count, Long.valueOf(sessionId));
		} else {
			QuerySpec query = getNoticeListQuery2(reqMap);
			
			result = PageQueryBroker.openPagingSession(start, count, query, true);
		}
		
		int totalSize = result.getTotalSize();
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			Notice notice = (Notice) obj[0];
			
			NoticeData data = new NoticeData(notice);
			
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
	
	public QuerySpec getNoticeListQuery(Map<String, Object> reqMap) throws Exception {
		
		String title = StringUtil.checkNull((String) reqMap.get("title"));
		title = title.replace("[", "[[]");
		String contents = StringUtil.checkNull((String) reqMap.get("contents"));
		String predate = StringUtil.checkNull((String) reqMap.get("predate"));
		String postdate = StringUtil.checkNull((String) reqMap.get("postdate"));
		
		List<String> creator = StringUtil.checkReplaceArray(reqMap.get("creator"));
		
		//Sort
		boolean sortCheck = true;
		if(reqMap.get("sortCheck") != null){
			sortCheck = (boolean) reqMap.get("sortCheck");
		}
		String sortValue = StringUtil.checkNull((String) reqMap.get("sortValue"));
		
		QuerySpec query = new QuerySpec();
		int idx = query.addClassList(Notice.class, true);
		
		SearchCondition sc = null;
		
		//문서명
		if(title.length() > 0) {
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(Notice.class, Notice.TITLE, SearchCondition.LIKE, "%" + title + "%", false);
			query.appendWhere(sc, new int[] { idx });
		}
		
		//비고
		if(contents.length() > 0) {
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(Notice.class, Notice.CONTENTS, SearchCondition.LIKE, "%" + contents + "%", false);
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
			sc = new SearchCondition(new ClassAttribute(Notice.class, Notice.OWNER + "." + "key.id"), SearchCondition.IN, new ArrayExpression(userOidLongValueList.toArray()));
			query.appendWhere(sc, new int[] { idx });
		}
				
		//등록일
    	if(predate.length() > 0){
    		if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
    		sc = new SearchCondition(Notice.class, Notice.CREATE_TIMESTAMP ,SearchCondition.GREATER_THAN,DateUtil.convertStartDate(predate));
    		query.appendWhere(sc, new int[] { idx });
    	}
    	if(postdate.length() > 0){
    		if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
    		sc = new SearchCondition(Notice.class, Notice.CREATE_TIMESTAMP,SearchCondition.LESS_THAN,DateUtil.convertEndDate(postdate));
    		query.appendWhere(sc, new int[] { idx });
    	}
    	
    	//소팅
    	if (sortValue.length() > 0) {
    		if (sortCheck) {
    			query.appendOrderBy(new OrderBy(new ClassAttribute(Notice.class, sortValue), false), new int[] { idx });
    		} else {
    			query.appendOrderBy(new OrderBy(new ClassAttribute(Notice.class, sortValue), true), new int[] { idx });
    		}
    	} else {
    		query.appendOrderBy(new OrderBy(new ClassAttribute(Notice.class, Notice.CREATE_TIMESTAMP), true), new int[] { idx });
    	}
    	
    	
    	return query;
	}

	public QuerySpec getNoticeListQuery2(Map<String, Object> reqMap) throws Exception {
		
		//소팅
		Map<String, Object> sort = reqMap.get("sort") != null ? (Map<String, Object>) reqMap.get("sort") : new HashMap<>();
				
		String title = StringUtil.checkNull((String) reqMap.get("title"));
		String contents = StringUtil.checkNull((String) reqMap.get("contents"));
		String predate = StringUtil.checkNull((String) reqMap.get("predate"));
		String postdate = StringUtil.checkNull((String) reqMap.get("postdate"));
		
		List<String> creator = StringUtil.checkReplaceArray(reqMap.get("creator"));
		
		QuerySpec query = new QuerySpec();
		int idx = query.addClassList(Notice.class, true);
		
		SearchCondition sc = null;
		
		//문서명
		if(title.length() > 0) {
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(Notice.class, Notice.TITLE, SearchCondition.LIKE, "%" + title + "%", false);
			query.appendWhere(sc, new int[] { idx });
		}
		
		//비고
		if(contents.length() > 0) {
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(Notice.class, Notice.CONTENTS, SearchCondition.LIKE, "%" + contents + "%", false);
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
			sc = new SearchCondition(new ClassAttribute(Notice.class, Notice.OWNER + "." + "key.id"), SearchCondition.IN, new ArrayExpression(userOidLongValueList.toArray()));
			query.appendWhere(sc, new int[] { idx });
		}
		
		//등록일
		if(predate.length() > 0){
			if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(Notice.class, Notice.CREATE_TIMESTAMP ,SearchCondition.GREATER_THAN,DateUtil.convertStartDate(predate));
			query.appendWhere(sc, new int[] { idx });
		}
		if(postdate.length() > 0){
			if (query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(Notice.class, Notice.CREATE_TIMESTAMP,SearchCondition.LESS_THAN,DateUtil.convertEndDate(postdate));
			query.appendWhere(sc, new int[] { idx });
		}
		
		//소팅
		if (sort.size() > 0) {
			String id = (String) sort.get("id");
			String dir = (String) sort.get("dir");
			
			String sortValue = "";
			if("notifyType".equals(id)) {
				sortValue = Notice.NOTIFY_TYPE;
			} else if("title".equals(id)) {
				sortValue = Notice.TITLE;
			} else if("createDateFormat".equals(id)) {
				sortValue = Notice.CREATE_TIMESTAMP;
			} else if("deadline".equals(id)) {
				sortValue = Notice.DEADLINE;
			} else if("cnt".equals(id)) {
				sortValue = Notice.CNT;
			}
			
			if ("asc".equals(dir)) {
				query.appendOrderBy(new OrderBy(new ClassAttribute(Notice.class, sortValue), false), new int[] { idx });
			} else if("desc".equals(dir)) {
				query.appendOrderBy(new OrderBy(new ClassAttribute(Notice.class, sortValue), true), new int[] { idx });
			}
		} else {
			query.appendOrderBy(new OrderBy(new ClassAttribute(Notice.class, Notice.CREATE_TIMESTAMP), true), new int[] { idx });
		}
		
		
		return query;
	}
}
