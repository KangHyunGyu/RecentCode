package com.e3ps.part.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.CookieGenerator;

import com.e3ps.common.iba.IBAUtil;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.ObjectSort;
import com.e3ps.common.util.SearchUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.epm.dnc.CadAttributeDNC;
import com.e3ps.part.bean.BomTreeData;
import com.e3ps.part.bean.BomTreeExcelData;
import com.e3ps.part.bean.PartData;
import com.e3ps.part.util.PartUtil;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import wt.fc.ObjectToObjectLink;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.WTObject;
import wt.lifecycle.State;
import wt.part.WTPart;
import wt.part.WTPartBaselineConfigSpec;
import wt.part.WTPartConfigSpec;
import wt.part.WTPartHelper;
import wt.part.WTPartMaster;
import wt.part.WTPartStandardConfigSpec;
import wt.part.WTPartUsageLink;
import wt.pds.StatementSpec;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.ServiceFactory;
import wt.util.WTAttributeNameIfc;
import wt.util.WTException;
import wt.vc.baseline.Baseline;
import wt.vc.baseline.BaselineMember;
import wt.vc.views.View;
import wt.vc.views.ViewHelper;

public class BomHelper {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PART.getName());
	public static final BomService service = ServiceFactory.getService(BomService.class);

	public static final BomHelper manager = new BomHelper();
	
	private View getView() throws WTException {
		return ViewHelper.service.getView("Design");
	}
	
	public List<BomTreeData> getBomRoot(Map<String, Object> reqMap) throws Exception {

		List<BomTreeData> list = new ArrayList<>();

		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String view = StringUtil.checkNull((String) reqMap.get("view"));
		String desc = StringUtil.checkReplaceStr((String) reqMap.get("desc"), "true");
		String baseline = StringUtil.checkNull((String) reqMap.get("baseline"));

		String initLevel = StringUtil.checkReplaceStr((String) reqMap.get("initLevel"), "1");
		
		WTPart part = (WTPart) CommonUtil.getObject(oid);
		
		Baseline bsobj = null;
		if (baseline.length() > 0) {
			bsobj = (Baseline) CommonUtil.getObject(baseline);
		}
		if (bsobj != null) {
			part = getPartByBaseline(part, bsobj);
		}

		View[] views = ViewHelper.service.getAllViews();

		if(view.length() == 0){
			view = views[0].getName();
		}
		View viewObj = ViewHelper.service.getView(view);
		
		BomTreeData root = new BomTreeData(part, null, 0, 0, "");
		
		List<BomTreeData> childrenList = new ArrayList<>();
		if("ALL".equals(initLevel)) {
			childrenList = getBomChildrenAll(part, desc, bsobj, viewObj, root.getTreeId(), root.getLevel());
		} else {
			childrenList = getBomChildrenByInitLevel(part, desc, bsobj, viewObj, root.getTreeId(), root.getLevel(), Integer.parseInt(initLevel));
		}
		
		root.setChildren(childrenList);
		
		list.add(root);
		
		return list;
	}
	
	public List<BomTreeData> getBomChildren(Map<String, Object> reqMap) throws Exception {

		List<BomTreeData> list = new ArrayList<>();

		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		int level = (int) reqMap.get("level");
		String treeId = StringUtil.checkNull((String) reqMap.get("treeId"));
		
		String view = StringUtil.checkNull((String) reqMap.get("view"));
		String desc = StringUtil.checkReplaceStr((String) reqMap.get("desc"), "true");
		String baseline = StringUtil.checkNull((String) reqMap.get("baseline"));

		WTPart part = (WTPart) CommonUtil.getObject(oid);

		Baseline bsobj = null;
		if (baseline.length() > 0) {
			bsobj = (Baseline) CommonUtil.getObject(baseline);
		}
		if (bsobj != null) {
			part = getPartByBaseline(part, bsobj);
		}

		View[] views = ViewHelper.service.getAllViews();

		if(view.length() == 0){
			view = views[0].getName();
		}
		
		View viewObj = ViewHelper.service.getView(view);
		
		list = getBomChildren(part, desc, bsobj, viewObj, treeId, level);
		
		return list;
	}
	
	public List<BomTreeData> getBomChildren(WTPart parent, String desc, Baseline bsobj, View view, String parentTreeId, int level) throws Exception {

		List<BomTreeData> list = new ArrayList<>();

		List<Object[]> childrenList = new ArrayList<>();
		
		if(view == null){
			view = getView();
		}
		
		if ("true".equals(desc)) {
			if (bsobj == null) {
				childrenList = descentLastPart(parent, view, null);
			} else {
				childrenList = descentLastPart(parent, bsobj, null);
			}
		} else {
			if (bsobj == null) {
				childrenList = ancestorPart(parent, view, null);
			} else {
				childrenList = ancestorPart(parent, bsobj, null);
			}
		}
		
		sortChildren(childrenList);
		
		for (int i = 0; i < childrenList.size(); i++) {
			Object[] o = (Object[]) childrenList.get(i);
			
			BomTreeData data = new BomTreeData((WTPart) o[1], (WTPartUsageLink) o[0], level + 1, i, parentTreeId);
			if(isChildren((WTPart) o[1], desc, bsobj)) {
				data.setChildren(new ArrayList<BomTreeData>());
			}
			
			list.add(data);
		}
		
		return list;
	}
	
	public List<BomTreeData> getBomChildrenByInitLevel(WTPart parent, String desc, Baseline bsobj, View view, String parentTreeId, int level, int initLevel) throws Exception {

		List<BomTreeData> list = new ArrayList<>();

		List<Object[]> childrenList = new ArrayList<>();
		
		if(view == null){
			view = getView();
		}
		
		if ("true".equals(desc)) {
			if (bsobj == null) {
				childrenList = descentLastPart(parent, view, null);
			} else {
				childrenList = descentLastPart(parent, bsobj, null);
			}
		} else {
			if (bsobj == null) {
				childrenList = ancestorPart(parent, view, null);
			} else {
				childrenList = ancestorPart(parent, bsobj, null);
			}
		}
		
		sortChildren(childrenList);
		
		for (int i = 0; i < childrenList.size(); i++) {
			Object[] o = (Object[]) childrenList.get(i);
			
			BomTreeData data = new BomTreeData((WTPart) o[1], (WTPartUsageLink) o[0], level + 1, i, parentTreeId);
			
			if(data.getLevel() < initLevel) {
				data.setChildren(getBomChildrenByInitLevel((WTPart) o[1], desc, bsobj, view, data.getTreeId(), data.getLevel(), initLevel));
			} else if(data.getLevel() == initLevel) {
				if(isChildren((WTPart) o[1], desc, bsobj)) {
					data.setChildren(new ArrayList<BomTreeData>());
				}
			}
			
			list.add(data);
		}
		
		return list;
	}
	
	public List<BomTreeData> getBomChildrenAll(WTPart parent, String desc, Baseline bsobj, View view, String parentTreeId, int level) throws Exception {

		List<BomTreeData> list = null;

		List<Object[]> childrenList = new ArrayList<>();
		
		if(view == null){
			view = getView();
		}
		
		if ("true".equals(desc)) {
			if (bsobj == null) {
				childrenList = descentLastPart(parent, view, null);
			} else {
				childrenList = descentLastPart(parent, bsobj, null);
			}
		} else {
			if (bsobj == null) {
				childrenList = ancestorPart(parent, view, null);
			} else {
				childrenList = ancestorPart(parent, bsobj, null);
			}
		}
		
		if(childrenList.size() > 0) {
			list = new ArrayList<>();
			
			sortChildren(childrenList);
		}
		
		for (int i = 0; i < childrenList.size(); i++) {
			Object[] o = (Object[]) childrenList.get(i);
			
			BomTreeData data = new BomTreeData((WTPart) o[1], (WTPartUsageLink) o[0], level + 1, i, parentTreeId);
			
			data.setChildren(getBomChildrenAll((WTPart) o[1], desc, bsobj, view, data.getTreeId(), data.getLevel()));
			
			list.add(data);
		}
		
		return list;
	}
	
	public List<BomTreeData> getBomItemList(Map<String, Object> reqMap) throws Exception {

		List<BomTreeData> list = new ArrayList<>();

		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String view = StringUtil.checkNull((String) reqMap.get("view"));
		String baseline = StringUtil.checkNull((String) reqMap.get("baseline"));
		
		WTPart part = (WTPart) CommonUtil.getObject(oid);

		Baseline bsobj = null;
		
		if (baseline.length() > 0) {
			bsobj = (Baseline) CommonUtil.getObject(baseline);
		}
		if (bsobj != null) {
			part = getPartByBaseline(part, bsobj);
		}

		View[] views = ViewHelper.service.getAllViews();

		if(view.length() == 0){
			view = views[0].getName();
		}
		View viewObj = ViewHelper.service.getView(view);
		
		BomTreeData root = new BomTreeData(part, null, 0, 0, "");
		
		List<BomTreeData> childrenList = new ArrayList<>();
			
		Map<String, BomTreeData> map = getBomItemAll(part, bsobj, viewObj, root.getTreeId(), root.getLevel(), null);
		
		Iterator<String> it = map.keySet().iterator();
		
		while(it.hasNext()) {
			String key = it.next();
			
			BomTreeData data = map.get(key);
			
			childrenList.add(data);
		}
		
		sort(childrenList);
		
		list.add(root);
		list.addAll(childrenList);
		
		return list;
	}
	
	public Map<String, BomTreeData> getBomItemAll(WTPart parent, Baseline bsobj, View view, String parentTreeId, int level, Map<String, BomTreeData> map) throws Exception {

		if(map == null) {
			map = new HashMap<String, BomTreeData>();	
		}

		List<Object[]> childrenList = new ArrayList<>();
		
		if(view == null){
			view = getView();
		}
		
		if (bsobj == null) {
			childrenList = descentLastPart(parent, view, null);
		} else {
			childrenList = descentLastPart(parent, bsobj, null);
		}
		
		for (int i = 0; i < childrenList.size(); i++) {
			Object[] o = (Object[]) childrenList.get(i);
			
			BomTreeData data = new BomTreeData((WTPart) o[1], (WTPartUsageLink) o[0], level + 1, i, parentTreeId);
			
			if(map.containsKey(data.getNumber())) {
				BomTreeData tempData = (BomTreeData)map.get(data.getNumber());
				tempData.setQuantity(tempData.getQuantity()+data.getQuantity());
				map.put(data.getNumber(), tempData);
			} else {
				map.put(data.getNumber(), data);
			}
			
			getBomItemAll((WTPart) o[1], bsobj, view, data.getTreeId(), data.getLevel(), map);
		}
		
		return map;
	}
	
	public List<BomTreeData> getBomPartList(Map<String, Object> reqMap) throws Exception {

		List<BomTreeData> list = new ArrayList<>();

		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String type = StringUtil.checkNull((String) reqMap.get("type"));
		
		WTPart part = (WTPart) CommonUtil.getObject(oid);

		Baseline bsobj = null;
		
		List<Object[]> bomPartList = new ArrayList<>();
		
		if ("end".equals(type)) {
			
			list = getEndItemList(part, null, bsobj, getView());
			
		} else {
			if ("up".equals(type)) {
				bomPartList = ancestorPart(part, getView(), null);
			} else if ("down".equals(type)) {
				bomPartList = descentLastPart(part, getView(), null);
			}
			
			for (int i = 0; i < bomPartList.size(); i++) {
				Object[] o = (Object[]) bomPartList.get(i);
				
				BomTreeData data = new BomTreeData((WTPart) o[1], (WTPartUsageLink) o[0], 0, i, "");
				
				list.add(data);
			}
		}
		
		sort(list);
		
		return list;
	}

	public List<Object[]> descentLastPart(WTPart part, Baseline baseline, State state) throws WTException {
		List<Object[]> list = new ArrayList<>();
		if (!PersistenceHelper.isPersistent(part)) {
			return list;
		}
			
		try {
			WTPartBaselineConfigSpec configSpec = WTPartBaselineConfigSpec.newWTPartBaselineConfigSpec(baseline);
			QueryResult qr = WTPartHelper.service.getUsesWTParts(part, configSpec);

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
	

	public List<Object[]> descentLastPartExcel(WTPart part, View view, State state)
			throws WTException {
		List<Object[]> list = new ArrayList<>();
		
		if (!PersistenceHelper.isPersistent(part)) {
			return list;
		}
		
		try {
			
			String assembleType = IBAUtil.getAttrValue(part, CadAttributeDNC.ASSEMBLE_TYPE.getKey());
			if(assembleType.indexOf("구매품") > -1 || assembleType.indexOf("분리불가") > -1) {
				return list;
			}
			
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

	public List<Object[]> ancestorPart(WTPart part, View view, State state) throws WTException {
		List<Object[]> list = new ArrayList<>();
		try {
			WTPartMaster master = (WTPartMaster) part.getMaster();
			QuerySpec qs = new QuerySpec();
			int index1 = qs.addClassList(WTPartUsageLink.class, true);
			int index2 = qs.addClassList(WTPart.class, true);
			qs.appendWhere(new SearchCondition(WTPartUsageLink.class, "roleBObjectRef.key.id", "=", master.getPersistInfo().getObjectIdentifier().getId()), new int[] { index1 });
			SearchCondition sc = new SearchCondition(new ClassAttribute(WTPartUsageLink.class, "roleAObjectRef.key.id"), "=", new ClassAttribute(WTPart.class, "thePersistInfo.theObjectIdentifier.id"));
			sc.setFromIndicies(new int[] { index1, index2 }, 0);
			sc.setOuterJoin(0);
			qs.appendAnd();
			qs.appendWhere(sc, new int[] { index1, index2 });
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(WTPart.class, "iterationInfo.latest", SearchCondition.IS_TRUE, true), new int[] { index2 });
			
			if (view != null) {
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(WTPart.class, "view.key.id", "=", view.getPersistInfo().getObjectIdentifier().getId()), new int[] { index2 });
			}
			
			if (state != null) {
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(WTPart.class, "state.state", "=", state.toString()), new int[] { index2 });
			}

			SearchUtil.addLastVersionCondition(qs, WTPart.class, index2);

			qs.appendOrderBy(new OrderBy(new ClassAttribute(WTPart.class,"master>number"), true), new int[] { index2 });
			
			QueryResult qr = PersistenceHelper.manager.find((StatementSpec)qs);
			while (qr.hasMoreElements()) {
				Object oo[] = (Object[]) qr.nextElement();
				list.add(oo);
			}
		} catch (Exception ex) {
			throw new WTException();
		}
		
		return list;
	}

	public List<Object[]> ancestorPart(WTPart part, Baseline baseline, State state)
			throws WTException {
		List<Object[]> list = new ArrayList<>();
		try {
			WTPartMaster master = (WTPartMaster) part.getMaster();
			QuerySpec qs = new QuerySpec();
			int index1 = qs.addClassList(WTPartUsageLink.class, true);
			int index2 = qs.addClassList(WTPart.class, true);
			qs.appendWhere(new SearchCondition(WTPartUsageLink.class, "roleBObjectRef.key.id", "=", master.getPersistInfo().getObjectIdentifier().getId()), new int[] { index1 });
			SearchCondition sc = new SearchCondition(new ClassAttribute(WTPartUsageLink.class, "roleAObjectRef.key.id"), "=", new ClassAttribute(WTPart.class, "thePersistInfo.theObjectIdentifier.id"));
			sc.setFromIndicies(new int[] { index1, index2 }, 0);
			sc.setOuterJoin(0);
			qs.appendAnd();
			qs.appendWhere(sc, new int[] { index1, index2 });

			if (state != null) {
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(WTPart.class, "state.state", "=", state.toString()), new int[] { index2 });
			}

			if (baseline != null) {
				int index3 = qs.addClassList(BaselineMember.class, false);
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(WTPart.class, "thePersistInfo.theObjectIdentifier.id", BaselineMember.class, "roleBObjectRef.key.id"), new int[] { index2, index3 });
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(BaselineMember.class, "roleAObjectRef.key.id", "=", baseline.getPersistInfo().getObjectIdentifier().getId()), new int[] { index3 });
			}
			
			qs.appendOrderBy(new OrderBy(new ClassAttribute(WTPart.class,"master>number"), true), new int[] { index2 });
			
			QueryResult qr = PersistenceHelper.manager.find((StatementSpec)qs);
			
			while (qr.hasMoreElements()) {
				Object oo[] = (Object[]) qr.nextElement();
				list.add(oo);
			}
		} catch (Exception ex) {
			throw new WTException();
		}
		
		return list;
	}
	
	public WTPart getPartByBaseline(WTPart part, Baseline baseline) throws Exception {
		
		QuerySpec qs = new QuerySpec();
		
		int ii = qs.addClassList(WTPart.class, true);
		int jj = qs.addClassList(BaselineMember.class, false);
		
		qs.appendWhere(new SearchCondition(BaselineMember.class, "roleBObjectRef.key.id", WTPart.class, "thePersistInfo.theObjectIdentifier.id"), new int[] { jj, ii });
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(BaselineMember.class, "roleAObjectRef.key.id", "=", CommonUtil.getOIDLongValue(baseline)), new int[] { jj });
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(WTPart.class, "masterReference.key.id", "=", CommonUtil.getOIDLongValue(part)), new int[] { ii });
		
		QueryResult qr = PersistenceHelper.manager.find((StatementSpec)qs);
		if (qr.hasMoreElements()) {
			Object[] o = (Object[]) qr.nextElement();
			part = (WTPart) o[0];
		}
		
		return part;
	}
	
	
	public boolean isChildren(WTPart part, String desc, Baseline baseline) throws Exception{
		
		List<Object[]> list= new ArrayList<Object[]>();
		if ("true".equals(desc)) {
			if (baseline == null) {
				list = descentLastPart(part, getView(), null);
			} else {
				list = descentLastPart(part, baseline, null);
			}
		} else {
			if (baseline == null) {
				list = ancestorPart(part, getView(), null);
			} else {
				list = ancestorPart(part, baseline, null);
			}
		}
		boolean isChildren = true;
		if(list.size()==0){
			isChildren = false;
		}
		
		return isChildren;
	}
	
	public List<BomTreeData> getEndItemListRecursive(WTPart parent, WTPartUsageLink link,  Baseline bsobj, View view) throws Exception {

		List<BomTreeData> list = new ArrayList<>();

		List<Object[]> childrenList = new ArrayList<>();
		
		if(view == null) {
			view = getView();
		}
		
		if (bsobj == null) {
			childrenList = ancestorPart(parent, view, null);
		} else {
			childrenList = ancestorPart(parent, bsobj, null);
		}
		
		if(childrenList.size() > 0) {
			for (int i = 0; i < childrenList.size(); i++) {
				Object[] o = (Object[]) childrenList.get(i);
				
				list.addAll(getEndItemListRecursive((WTPart) o[1], (WTPartUsageLink) o[0], bsobj, view));
			}
		} else {
			BomTreeData data = new BomTreeData(parent, link, 0, 0, "");
			
			list.add(data);
		}
		
		return list;
	}
	
	public List<BomTreeData> getEndItemList(WTPart parent, WTPartUsageLink link,  Baseline bsobj, View view) throws Exception {

		List<BomTreeData> list = getEndItemListRecursive(parent, link, bsobj, view);
		
		List<BomTreeData> newList = new ArrayList<>();
		
		for(BomTreeData data : list) {
			boolean contain = false;
			for(BomTreeData newData : newList) {
				if(newData.getNumber().equals(data.getNumber())) {
					contain = true;
					break;
				}
			}
			if(!contain) {
				newList.add(data);
			}
		}
		
		list = newList;
		
		return list;
	}
	
	public void sort(List<BomTreeData> list) {
		
		/*
		
			@Override
			public int compare(BomTreeData a, BomTreeData b) {
				return a.getNumber().compareTo(b.getNumber());
			}
		});
		*/
		Collections.sort(list, new ObjectSort());
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
    
    
    public List<BomTreeExcelData> getBomExcelList(WTPart part) throws Exception {
		
    	List<BomTreeExcelData> list = new ArrayList<>();
    	
		View view = getView();
    	
		getBomExcelChildrenAll(part, null, view, 0, list);
		
		return list;
    }
    
    public void getBomExcelChildrenAll(WTPart parent, WTPartUsageLink link, View view, int level, List<BomTreeExcelData> list) throws Exception {

    	BomTreeExcelData data = new BomTreeExcelData(parent, link, level);
    	
    	list.add(data);
    	
		List<Object[]> childrenList = new ArrayList<>();
		
		if(view == null){
			view = getView();
		}
		
		childrenList = descentLastPart(parent, view, null);
		
		sortChildren(childrenList);
		
		for (int i = 0; i < childrenList.size(); i++) {
			Object[] o = (Object[]) childrenList.get(i);
			
			getBomExcelChildrenAll((WTPart) o[1], (WTPartUsageLink) o[0], view, data.getLevel() + 1, list);
		}
		
		//Collections.sort(list, new ObjectSort());
	}
    
    public List<BomTreeExcelData> getBomExcelListExcel(WTPart part) throws Exception {
		
    	List<BomTreeExcelData> list = new ArrayList<>();
    	
		View view = getView();
    	
		getBomExcelChildrenAllExcel(part, null, view, 0, list);
		
		return list;
    }
    
    public void getBomExcelChildrenAllExcel(WTPart parent, WTPartUsageLink link, View view, int level, List<BomTreeExcelData> list) throws Exception {

    	BomTreeExcelData data = new BomTreeExcelData(parent, link, level);
    	
    	list.add(data);
    	
		List<Object[]> childrenList = new ArrayList<>();
		
		if(view == null){
			view = getView();
		}
		
		childrenList = descentLastPartExcel(parent, view, null);
		
		sortChildren(childrenList);
		
		for (int i = 0; i < childrenList.size(); i++) {
			Object[] o = (Object[]) childrenList.get(i);
			
			getBomExcelChildrenAllExcel((WTPart) o[1], (WTPartUsageLink) o[0], view, data.getLevel() + 1, list);
		}
		
		//Collections.sort(list, new ObjectSort());
	}
    
    public void sortChildren(List<Object[]> list) {
		
    	Collections.sort(list, new Comparator<Object[]>() {
			@Override
			public int compare(Object[] a, Object[] b) {
				return ((WTPart)a[1]).getNumber().compareTo(((WTPart)b[1]).getNumber());
			}
		});
	}

    public void downloadBomExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	List<BomTreeExcelData> list = new ArrayList<>();
    	
    	String oid = request.getParameter("oid");
    	
    	WTPart part = (WTPart) CommonUtil.getObject(oid);
    	
    	list = getBomExcelListExcel(part);
    	
		String fileName = new String((part.getNumber() + " " + part.getName() + ".xls").getBytes("euc-kr"), "8859_1");
		
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\""); 
		response.setContentType("application/vnd.ms-excel");
		
		request.setCharacterEncoding("euc-kr");
		
		File newfile = new File(WCUtil.getWTHome()+"\\codebase\\excelTemplate\\bomExcelTemplate.xls");
		Workbook wb = Workbook.getWorkbook(newfile);
		WritableWorkbook workbook = Workbook.createWorkbook(response.getOutputStream(), wb);
		
		WritableSheet sheet = workbook.getSheet(0);
		PartData pD = new PartData(part);
		pD.loadAttributes();
		
		sheet.addCell(new Label(3, 5, (String)pD.getAttributes().getOrDefault(CadAttributeDNC.AN_PRODUCT_SERIALNO.getKey(), "")));
		sheet.addCell(new Label(3, 6, part.getNumber()));
		sheet.addCell(new Label(3, 7, part.getName()));
		
		sheet.addCell(new Label(6, 5, part.getCreatorFullName()));
		sheet.addCell(new Label(6, 6, pD.getRev()));
		sheet.addCell(new Label(6, 7, pD.getDescription()));
		
		
		int row = 16;
		int count = 1;
		for(BomTreeExcelData data : list) {
			
//			if(data.getAssembleType() != null && (data.getAssembleType().indexOf("구매품") > -1 || data.getAssembleType().indexOf("분리불가") > -1)) {
//				continue;
//			}
			
			int columnIndex = 4;
			sheet.addCell(new Label(0, row, Integer.toString(count++)));
			sheet.addCell(new Label(data.getLevel()+1, row, "O")); // 레벨
			sheet.addCell(new Label(columnIndex++, row, data.getNumber())); // 품번
			sheet.addCell(new Label(columnIndex++, row, data.getName())); // 품명
			columnIndex++;
			sheet.addCell(new Label(columnIndex++, row, data.getQuantity())); // 갯수
			sheet.addCell(new Label(columnIndex++, row, data.getMaterial())); // 재질
			sheet.addCell(new Label(columnIndex++, row, data.getTreatment())); // 후처리
			sheet.addCell(new Label(columnIndex++, row, data.getCategory())); // 분류
			sheet.addCell(new Label(columnIndex++, row, data.getDescription()));// 비고
//			sheet.addCell(new Label(columnIndex++, row, data.getMethod())); // 공법
//			sheet.addCell(new Label(columnIndex++, row, data.getSpecification())); // 규격 및 특징
//			sheet.addCell(new Label(columnIndex++, row, data.getWeight())); // 중량
//			columnIndex++;
//			sheet.addCell(new Label(columnIndex++, row, "")); // 도면 3D
//			sheet.addCell(new Label(columnIndex++, row, "")); // 도면 2D
//			sheet.addCell(new Label(columnIndex++, row, data.getProd_company_name())); // 양산업체 명
//			sheet.addCell(new Label(columnIndex++, row, data.getProd_company_manager())); // 담당자
//			sheet.addCell(new Label(columnIndex++, row, data.getProd_company_tell())); // 연락처
//			sheet.addCell(new Label(columnIndex++, row, data.getStart_unit_cost())); // 시작단가
//			sheet.addCell(new Label(columnIndex++, row, data.getStart_cost())); // 시작 금액
//			sheet.addCell(new Label(columnIndex++, row, data.getProd_unit_cost())); // 양산 단가
//			sheet.addCell(new Label(columnIndex++, row, data.getProd_cost()));// 양산 금액
//			sheet.addCell(new Label(columnIndex++, row, data.getDescription()));// 비고
			row ++;
		}
		
		CookieGenerator cg = new CookieGenerator();
		cg.setCookieName("fileDownload");
		cg.addCookie(response, "true");
		
		workbook.write();
		workbook.close();
    }
    
    /**
	 * 
	 * @desc	: UsageLink 찾기
	 * @author	: tsuam
	 * @date	: 2019. 10. 10.
	 * @method	: getUsageLink
	 * @return	: WTPartUsageLink
	 * @param child
	 * @param parent
	 * @return
	 * @throws Exception
	 */
	public static List<WTPartUsageLink> getUsageLink(WTPart child, WTPart parent) throws Exception{
		 return getLinkObject((WTPartMaster) child.getMaster(), parent);
	}
	
	/**
	 * 
	 * @desc	: UsageLink 찾기
	 * @author	: tsuam
	 * @date	: 2019. 10. 10.
	 * @method	: getLinkObject
	 * @return	: WTPartUsageLink
	 * @param roleA
	 * @param roleB
	 * @return
	 * @throws Exception
	 */
	public static List<WTPartUsageLink> getLinkObject(WTObject roleA, WTObject roleB) throws Exception {
		
		List<WTPartUsageLink> list = new ArrayList<WTPartUsageLink>();
		
        QuerySpec query = new QuerySpec();
        int linkIndex = query.appendClassList(WTPartUsageLink.class, true);
        SearchCondition cond1 = new SearchCondition(WTPartUsageLink.class, WTAttributeNameIfc.ROLEB_OBJECT_ID, "=", new Long(PersistenceHelper.getObjectIdentifier(roleA).getId()));
        SearchCondition cond2 = new SearchCondition(WTPartUsageLink.class, WTAttributeNameIfc.ROLEA_OBJECT_ID, "=", new Long(PersistenceHelper.getObjectIdentifier(roleB).getId()));
        query.appendWhere(cond1, new int[] { linkIndex });
        query.appendAnd();
        query.appendWhere(cond2, new int[] { linkIndex });
        QueryResult result = PersistenceHelper.manager.find(query);
        
        while(result.hasMoreElements()){
        	  Object[] obj = (Object[]) result.nextElement();
        	  WTPartUsageLink link = (WTPartUsageLink) obj[0];
        	  list.add(link);
        }
        return list;
        /*
        if (result.size() == 0) return null;
        Object[] obj = (Object[]) result.nextElement();
        return (WTPartUsageLink) obj[0];
        */
    }
	
	/**
	 * 
	 * @desc	: 정규 배포시 하위 구조  BOM 부품 조회 
	 * @author	: plmadmin
	 * @date	: 2020. 2. 21.
	 * @method	: getBomPartALL
	 * @return	: Map<String,BomTreeData>
	 * @param parent
	 * @param bsobj
	 * @param view
	 * @param parentTreeId
	 * @param level
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, WTPart> getBomPartALL(WTPart parentPart, View view, Map<String, WTPart> map) throws Exception {

		if(map == null) {
			map = new HashMap<String, WTPart>();	
		}
		
		map.put(parentPart.getNumber(), parentPart);
		
		List<Object[]> childrenList = new ArrayList<>();
		
		if(view == null){
			view = getView();
		}
		
		childrenList = descentLastPart(parentPart, view, null);
		
		for (int i = 0; i < childrenList.size(); i++) {
			Object[] o = (Object[]) childrenList.get(i);
			
			WTPart childPart = (WTPart) o[1];
			String childNumber = childPart.getNumber();
			String version = childPart.getVersionIdentifier().getValue();
			
			//중복 제거
			if(map.containsKey(childNumber)){
				continue;
			}
			
			//배포 대상 부품 
			boolean isDisPart = PartUtil.isDistributePart(childNumber);
			if(isDisPart){
				map.put(childNumber, childPart);
			}
			
			getBomPartALL(childPart,  view, map);
		}
		
		return map;
	}
}
