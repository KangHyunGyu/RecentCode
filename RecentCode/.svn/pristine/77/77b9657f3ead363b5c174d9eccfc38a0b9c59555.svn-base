package com.e3ps.calendar.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.e3ps.calendar.CalendarProjectLink;
import com.e3ps.calendar.DevelopmentStage;
import com.e3ps.calendar.DevelopmentStageCalendar;
import com.e3ps.calendar.DevelopmentStageCalendarLink;
import com.e3ps.calendar.bean.DSCData;
import com.e3ps.calendar.bean.DSData;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.web.PageQueryBroker;
import com.e3ps.project.EOutput;
import com.e3ps.project.EProject;
import com.e3ps.project.ETask;

import wt.fc.PagingQueryResult;
import wt.fc.PagingSessionHelper;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.pds.StatementSpec;
import wt.query.ArrayExpression;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.ServiceFactory;
import wt.util.WTException;

public class DsHelper {
	public static final DsService service = ServiceFactory.getService(DsService.class);
	public static final DsHelper manager = new DsHelper();

	public boolean checkOverlapByName(String name) throws WTException{
		boolean resultV = false;
    	QuerySpec qs = new QuerySpec();
    	try{
	    	int ii = qs.addClassList(DevelopmentStageCalendar.class,true);
	    	qs.appendWhere(new SearchCondition(DevelopmentStageCalendar.class,DevelopmentStageCalendar.NAME,"=",name),new int[]{ii});
    	}catch (Exception e) {
    		e.printStackTrace();
		}
    	QueryResult result = PersistenceHelper.manager.find((StatementSpec)qs);
    	if(result.size() > 0) {
    		resultV = true;
    	}
    	return resultV;
    }
	
	public Map<String, Object> getDsScrollList(Map<String, Object> reqMap) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
		
		List<DSCData> list = new ArrayList<>();
		
		int start = (Integer) reqMap.get("start");
		int count = (Integer) reqMap.get("count");
		String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));
    	
		PagingQueryResult result = null;
		
		if (sessionId.length() > 0) {
			result = PagingSessionHelper.fetchPagingSession(start, count, Long.valueOf(sessionId));
		} else {
			QuerySpec query = getDsListQuery(reqMap);
			
			result = PageQueryBroker.openPagingSession(start, count, query, true);
		}
		
		int totalSize = result.getTotalSize();
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			DevelopmentStageCalendar dsc = (DevelopmentStageCalendar) obj[0];
			
			DSCData data = new DSCData(dsc);
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
	
	@SuppressWarnings("unchecked")
	public QuerySpec getDsListQuery(Map<String, Object> reqMap) throws Exception {
		
		//소팅
		Map<String, Object> sort = reqMap.get("sort") != null ? (Map<String, Object>) reqMap.get("sort") : new HashMap<>();
		
		//기본속성
		String name = StringUtil.checkNull((String) reqMap.get("name"));
		String startDate = StringUtil.checkNull((String) reqMap.get("startDate"));
		String startDateE = StringUtil.checkNull((String) reqMap.get("startDateE"));
		String endDate = StringUtil.checkNull((String) reqMap.get("endDate"));
		String endDateE = StringUtil.checkNull((String) reqMap.get("endDateE"));
		
		List<String> relatedProject = StringUtil.checkReplaceArray(reqMap.get("relatedProject"));
		
		QuerySpec qs = new QuerySpec();
		int idx = qs.addClassList(DevelopmentStageCalendar.class, true);
		
		SearchCondition sc = null;
		
		//대일정 이름
		if(name.length() > 0) {
			if(qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(DevelopmentStageCalendar.class, DevelopmentStageCalendar.NAME, SearchCondition.LIKE, "%" + name + "%", false);
			qs.appendWhere(sc, new int[] { idx });
		}

		//시작일
    	if(startDate.length() > 0){
    		if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
    		sc = new SearchCondition(DevelopmentStageCalendar.class, DevelopmentStageCalendar.START_DATE ,SearchCondition.GREATER_THAN,DateUtil.convertStartDate(startDate));
    		qs.appendWhere(sc, new int[] { idx });
    	}
    	if(startDateE.length() > 0){
    		if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
    		sc = new SearchCondition(DevelopmentStageCalendar.class, DevelopmentStageCalendar.START_DATE,SearchCondition.LESS_THAN,DateUtil.convertEndDate(startDateE));
    		qs.appendWhere(sc, new int[] { idx });
    	}
    	
    	//종료일
    	if(endDate.length() > 0){
    		if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
    		sc = new SearchCondition(DevelopmentStageCalendar.class, DevelopmentStageCalendar.END_DATE ,SearchCondition.GREATER_THAN,DateUtil.convertStartDate(endDate));
    		qs.appendWhere(sc, new int[] { idx });
    	}
    	if(endDateE.length() > 0){
    		if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
    		sc = new SearchCondition(DevelopmentStageCalendar.class, DevelopmentStageCalendar.END_DATE,SearchCondition.LESS_THAN,DateUtil.convertEndDate(endDateE));
    		qs.appendWhere(sc, new int[] { idx });
    	}
    	
    	
    	// 관련 프로젝트
    	if (relatedProject.size() > 0) {
    		int pjtIdx = qs.addClassList(EProject.class, false);
    		int taskIdx = qs.addClassList(ETask.class, false);
    		int outputIdx = qs.addClassList(EOutput.class, false);
    		
    		List<Long> projectOidLongValueList = new ArrayList<>();
    		for(String oid : relatedProject) {
    			projectOidLongValueList.add(CommonUtil.getOIDLongValue(oid));
    		}
    		
    		if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
    		
    		sc = new SearchCondition(new ClassAttribute(EOutput.class, EOutput.DOCUMENT_REFERENCE + ".key.id"), SearchCondition.EQUAL, new ClassAttribute(DevelopmentStageCalendar.class, DevelopmentStageCalendar.PERSIST_INFO + ".theObjectIdentifier.id"));
    		qs.appendWhere(sc, new int[] { outputIdx, idx });
    		
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
    		
    		sc = new SearchCondition(new ClassAttribute(EProject.class, EProject.PERSIST_INFO + ".theObjectIdentifier.id"), SearchCondition.IN, new ArrayExpression(projectOidLongValueList.toArray()));
    		qs.appendWhere(sc, new int[] { pjtIdx });
		}
    	
    	// 소팅
		if (sort.size() > 0) {
			String id = (String) sort.get("id");
			String dir = (String) sort.get("dir");
			
			String sortValue = "";
			if("name".equals(id)) {
				sortValue = DevelopmentStageCalendar.NAME;
			} else if("createDateFormat".equals(id)) {
				sortValue = DevelopmentStageCalendar.CREATE_TIMESTAMP;
			} else if("modifyDateFormat".equals(id)) {
				sortValue = DevelopmentStageCalendar.MODIFY_TIMESTAMP;
			}
			
			if ("asc".equals(dir)) {
				qs.appendOrderBy(new OrderBy(new ClassAttribute(DevelopmentStageCalendar.class, sortValue), false), new int[] { idx });
			} else if("desc".equals(dir)) {
				qs.appendOrderBy(new OrderBy(new ClassAttribute(DevelopmentStageCalendar.class, sortValue), true), new int[] { idx });
			}
		} else {
			qs.appendOrderBy(new OrderBy(new ClassAttribute(DevelopmentStageCalendar.class, DevelopmentStageCalendar.MODIFY_TIMESTAMP), true), new int[] { idx });
		}
    	
    	return qs;
	}
	
	
	public List<DSData> getDSDataList(DevelopmentStageCalendar dsc, String type) throws Exception {
		List<DSData> list = new ArrayList<DSData>();
		DSData data = null;
		if(!"modify".equals(type)) {
			data = new DSData(dsc);
			list.add(data);
		}
		long parent = CommonUtil.getOIDLongValue(dsc);
		QueryResult qr = null;
		qr = PersistenceHelper.manager.navigate(dsc, "ds", DevelopmentStageCalendarLink.class, false);
		while (qr.hasMoreElements()) {
			DevelopmentStageCalendarLink link = (DevelopmentStageCalendarLink) qr.nextElement();
			DevelopmentStage ds = link.getDs();
			data = new DSData(ds, parent);
			list.add(data);
		}
		return list;
	}
	
	public String getDscOidFromLink(EProject project) throws WTException {
		String dsoid = "";
		QueryResult qr = PersistenceHelper.manager.navigate(project,"dsc",CalendarProjectLink.class,false);
		if(qr.hasMoreElements()){
			CalendarProjectLink link = (CalendarProjectLink)qr.nextElement();
			DevelopmentStageCalendar dsc = link.getDsc();
			dsoid = CommonUtil.getOIDString(dsc);
		}
		return dsoid;
	}
}
