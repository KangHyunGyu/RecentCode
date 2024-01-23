package com.e3ps.part.editor.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.e3ps.common.iba.IBAUtil;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.ObjectUtil;
import com.e3ps.common.util.SearchUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.common.web.PageQueryBroker;
import com.e3ps.part.bean.PartData;
import com.e3ps.part.editor.bean.BomEditorTreeData;
import com.e3ps.part.service.PartHelper;
import com.e3ps.part.util.PartUtil;

import wt.fc.ObjectToObjectLink;
import wt.fc.PagingQueryResult;
import wt.fc.PagingSessionHelper;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.WTObject;
import wt.lifecycle.State;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.part.WTPartConfigSpec;
import wt.part.WTPartMaster;
import wt.part.WTPartStandardConfigSpec;
import wt.part.WTPartUsageLink;
import wt.pdmlink.PDMLinkProduct;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.ServiceFactory;
import wt.session.SessionHelper;
import wt.util.WTAttributeNameIfc;
import wt.util.WTException;
import wt.vc.VersionControlHelper;
import wt.vc.views.View;
import wt.vc.views.ViewHelper;

public class BomEditorHelper {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PART.getName());
	public static final BomEditorService service = ServiceFactory.getService(BomEditorService.class);

	public static final BomEditorHelper manager = new BomEditorHelper();
	
	private View getView() throws WTException {
		return ViewHelper.service.getView("Design");
	}
	
	public Map<String, Object> getBomPartScrollList(Map<String, Object> reqMap) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
		
		List<PartData> list = new ArrayList<>();
		
		int start = (Integer) reqMap.get("start");
		int count = (Integer) reqMap.get("count");
		String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));
		
		PagingQueryResult result = null;
		
		if (sessionId.length() > 0) {
			result = PagingSessionHelper.fetchPagingSession(start, count, Long.valueOf(sessionId));
		} else {
			QuerySpec query = getBomPartListQuery(reqMap);
			
			result = PageQueryBroker.openPagingSession(start, count, query, true);
		}
		
		int totalSize = result.getTotalSize();
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			WTPart part = (WTPart) obj[0];
			
			PartData data = new PartData(part);
			data.loadAttributes();
			
			list.add(data);
		}
		
		map.put("sessionId", result.getSessionId() == 0 ? "" : result.getSessionId());
		
		//webix
		map.put("data", list);
		map.put("total_count", totalSize);
		map.put("pos", start);
				
		return map;
	}

	public QuerySpec getBomPartListQuery(Map<String, Object> reqMap) throws Exception {
		
		//기본속성
		String number = StringUtil.checkNull((String) reqMap.get("number"));
		String name = StringUtil.checkNull((String) reqMap.get("name"));
		String isCreator = StringUtil.checkNull((String) reqMap.get("isCreator"));
		
		String parentOid = StringUtil.checkNull((String) reqMap.get("parentOid"));
		
		//IBA
		String specification = StringUtil.checkNull((String) reqMap.get("SPECIFICATION"));
		String maker = StringUtil.checkNull((String) reqMap.get("Maker"));
		String notice = StringUtil.checkNull((String) reqMap.get("NOTICE"));
		
		
		QuerySpec query = new QuerySpec();
		int idx = query.addClassList(WTPart.class, true);
		
		SearchCondition sc = null;
		
		//최신 이터레이션
		if(query.getConditionCount() > 0) {
			query.appendAnd(); 
		}
    	query.appendWhere(VersionControlHelper.getSearchCondition(WTPart.class, true), new int[]{idx});

    	//제품 
    	PDMLinkProduct product = WCUtil.getPDMLinkProduct();
    	if(query.getConditionCount() > 0) {
			query.appendAnd();
		}
		sc = new SearchCondition(WTPart.class, WTPart.CONTAINER_REFERENCE+".key.id", SearchCondition.EQUAL ,CommonUtil.getOIDLongValue(product));
		query.appendWhere(sc, new int[] { idx });
		
		//버전
		SearchUtil.addLastVersionCondition(query, WTPart.class, idx);
		
		//부품번호
		if(number.length() > 0) {
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(WTPart.class, WTPart.NUMBER, SearchCondition.LIKE, "%" + number + "%", false);
			query.appendWhere(sc, new int[] { idx });
		}
		
		//부품명
		if(name.length() > 0) {
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(WTPart.class, WTPart.NAME, SearchCondition.LIKE, "%" + name + "%", false);
			query.appendWhere(sc, new int[] { idx });
		}
		
		//작성자
		if (isCreator.length() > 0) {
			if("true".equals(isCreator)) {
				WTUser user = (WTUser) SessionHelper.manager.getPrincipal();
				
				if (query.getConditionCount() > 0) {
					query.appendAnd();
				}
				sc = new SearchCondition(WTPart.class, "iterationInfo.creator.key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(user));
				query.appendWhere(sc, new int[] { idx });
			}
		}
		
		//상태
		if(query.getConditionCount() > 0) {
			query.appendAnd();
		}
		sc = new SearchCondition(WTPart.class, WTPart.LIFE_CYCLE_STATE, SearchCondition.NOT_EQUAL, "WITHDRAWN");
		query.appendWhere(sc, new int[] { idx });
		
		//부품 추가 시 부모 부품 제외
		if(parentOid.length() > 0) {
			if(query.getConditionCount() > 0) {
				query.appendAnd();
			}
			sc = new SearchCondition(WTPart.class, WTPart.PERSIST_INFO + ".theObjectIdentifier.id", SearchCondition.NOT_EQUAL, CommonUtil.getOIDLongValue(parentOid));
			query.appendWhere(sc, new int[] { idx });
		}
		
		//Working Copy 제외
		if (query.getConditionCount() > 0) {
			query.appendAnd();
		}
		query.appendWhere(new SearchCondition(WTPart.class, WTPart.CHECKOUT_INFO + ".state", SearchCondition.NOT_EQUAL,
				"wrk", false), new int[] { idx }); 
		
		//IBA
		Hashtable<String, Object> params = new Hashtable<>();
    	
    	params.put("SPECIFICATION", specification);
    	params.put("Maker", maker);
    	params.put("NOTICE", notice);
    	
//    	IBAUtil.appendIBAWhere(query, WTPart.class, idx, params, true);
		
    	return query;
	}
	
	public List<BomEditorTreeData> getBomRoot(Map<String, Object> reqMap) throws Exception {

		List<BomEditorTreeData> list = new ArrayList<>();

		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		WTPart part = (WTPart) CommonUtil.getObject(oid);
		
		boolean isOwnerCheckOut = PartUtil.isOwnerCheckOut(part);
		BomEditorTreeData root = new BomEditorTreeData(part, null, 0, 0, "");
		if(isOwnerCheckOut){
			part = (WTPart) ObjectUtil.getWorkingObject(part);
			
			List<BomEditorTreeData> childrenList = new ArrayList<>();
			
			childrenList = getBomChildren(part, root.getTreeId(), root.getLevel());
			
			root.setChildren(childrenList);
		}
		list.add(root);
		
		return list;
	}
	
	public List<BomEditorTreeData> getBomChildren(Map<String, Object> reqMap) throws Exception {

		List<BomEditorTreeData> list = new ArrayList<>();

		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		int level = (int) reqMap.get("level");
		String treeId = StringUtil.checkNull((String) reqMap.get("treeId"));
		
		WTPart part = (WTPart) CommonUtil.getObject(oid);
		boolean isOwnerCheckOut = PartUtil.isOwnerCheckOut(part);
		if(isOwnerCheckOut){
			part = (WTPart) ObjectUtil.getWorkingObject(part);
		}
		
		list = getBomChildren(part, treeId, level);
		
		return list;
	}
	
	public List<BomEditorTreeData> getBomChildren(WTPart parent, String parentTreeId, int level) throws Exception {

		List<BomEditorTreeData> list = new ArrayList<>();

		List<Object[]> childrenList = new ArrayList<>();
		
		View view = getView();
		
		childrenList = descentLastPart(parent, view, null);
		
		sort(childrenList);
		
		for (int i = 0; i < childrenList.size(); i++) {
			Object[] o = (Object[]) childrenList.get(i);

			BomEditorTreeData data = new BomEditorTreeData((WTPart) o[1], (WTPartUsageLink) o[0], level + 1, i, parentTreeId);
			if(isChildren((WTPart) o[1])) {
				data.setChildren(new ArrayList<BomEditorTreeData>());
			}
			
			list.add(data);
		}
		
		return list;
	}
	
	public List<BomEditorTreeData> getUpdatedBomData(Map<String, Object> reqMap) throws Exception{
		
		List<BomEditorTreeData> list = new ArrayList<>();
			
		List<Map<String, Object>> parentItems = (List<Map<String, Object>>) reqMap.get("parentItems");
		
		for(Map<String, Object> parentItem : parentItems) {
			String oid = (String) parentItem.get("oid");
			String linkOid = (String) parentItem.get("linkOid");
			String parentTreeId = (String) parentItem.get("parent");
			int level = (int) parentItem.get("level");
			int seq = (int) parentItem.get("seq");
			
			WTPart pPart = (WTPart) CommonUtil.getObject(oid);
			WTPartUsageLink link = (WTPartUsageLink) CommonUtil.getObject(linkOid);
			
			pPart = (WTPart) ObjectUtil.getWorkingObject(pPart);
			
			BomEditorTreeData parentData = new BomEditorTreeData(pPart, link, level, seq, parentTreeId);
			
			List<BomEditorTreeData> childrenList = new ArrayList<>();
			
			childrenList = BomEditorHelper.manager.getBomChildren(pPart, parentData.getTreeId(), parentData.getLevel());
			
			parentData.setChildren(childrenList);
			
			list.add(parentData);
		}
		
		return list;
	}
	
	public List<BomEditorTreeData> getRefreshBomData(Map<String, Object> reqMap) throws Exception{
		
		List<BomEditorTreeData> list = new ArrayList<>();
			
		List<Map<String, Object>> parentItems = (List<Map<String, Object>>) reqMap.get("parentItems");
		
		for(Map<String, Object> parentItem : parentItems) {
			String number = (String) parentItem.get("number");
			//String oid = (String) parentItem.get("oid");
			String linkOid = (String) parentItem.get("linkOid");
			String parentTreeId = (String) parentItem.get("parent");
			int level = (int) parentItem.get("level");
			int seq = (int) parentItem.get("seq");
			
			WTPart pPart = (WTPart) PartHelper.manager.getPart(number);
			WTPartUsageLink link = (WTPartUsageLink) CommonUtil.getObject(linkOid);
			
			pPart = (WTPart) ObjectUtil.getWorkingObject(pPart);
			
			BomEditorTreeData parentData = new BomEditorTreeData(pPart, link, level, seq, parentTreeId);
			
			List<BomEditorTreeData> childrenList = new ArrayList<>();
			
			childrenList = BomEditorHelper.manager.getBomChildren(pPart, parentData.getTreeId(), parentData.getLevel());

			parentData.setChildren(childrenList);

			list.add(parentData);
		}
		
		return list;
	}

	public List<Object[]> descentLastPart(WTPart part, View view, State state)
			throws WTException {
		List<Object[]> list = new ArrayList<>();
		
		if (!PersistenceHelper.isPersistent(part)) {
			return list;
		}
			
		try {
			WTPartConfigSpec configSpec = WTPartConfigSpec.newWTPartConfigSpec(WTPartStandardConfigSpec.newWTPartStandardConfigSpec(view, state));
			QueryResult qr = wt.part.WTPartHelper.service.getUsesWTParts(part, configSpec);
			while (qr.hasMoreElements()) {
				Object oo[] = (Object[]) qr.nextElement();

				if (!(oo[1] instanceof WTPart)) {
					continue;
				}
				list.add(oo);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new WTException();
		}
		return list;
	}

	public boolean isChildren(WTPart part) throws Exception{
		
		List<Object[]> list= new ArrayList<Object[]>();
			
		list = descentLastPart(part, getView(), null);
		
		boolean isChildren = true;
		if(list.size()==0){
			isChildren = false;
		}
		
		return isChildren;
	}
	
	public void sort(List<Object[]> list) {
		
		Collections.sort(list, new Comparator<Object[]>() {
			@Override
			public int compare(Object[] a, Object[] b) {
				return ((WTPart) a[1]).getNumber().compareTo(((WTPart) b[1]).getNumber());
			}
		});
	}
	
	public  WTPartUsageLink getLink(WTPart child, WTPart parent) throws WTException
    {
        return (WTPartUsageLink) getLinkObject((WTPartMaster) child.getMaster(), parent, WTPartUsageLink.class);
    }

    public  ObjectToObjectLink getLinkObject(WTObject roleA, WTObject roleB, Class linkClz) throws WTException
    {
        QuerySpec query = new QuerySpec();
        int linkIndex = query.appendClassList(linkClz, true);
        SearchCondition cond1 = new SearchCondition(linkClz, WTAttributeNameIfc.ROLEB_OBJECT_ID, "=", new Long(PersistenceHelper
                .getObjectIdentifier(roleA).getId()));
        SearchCondition cond2 = new SearchCondition(linkClz, WTAttributeNameIfc.ROLEA_OBJECT_ID, "=", new Long(PersistenceHelper
                .getObjectIdentifier(roleB).getId()));
        query.appendWhere(cond1, new int[] { linkIndex });
        query.appendAnd();
        query.appendWhere(cond2, new int[] { linkIndex });
       
        QueryResult result = PersistenceHelper.manager.find(query);
        if (result.size() == 0) return null;
        Object[] obj = (Object[]) result.nextElement();
        return (ObjectToObjectLink) obj[0];
    }
}
