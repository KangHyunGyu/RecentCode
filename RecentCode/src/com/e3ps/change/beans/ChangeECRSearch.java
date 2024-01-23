package com.e3ps.change.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.e3ps.approval.service.ApprovalHelper;
import com.e3ps.approval.service.ApprovalService;
import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.EChangeRequest2;
import com.e3ps.change.EcrPartLink;
import com.e3ps.change.EcrProjectLink;
import com.e3ps.change.EcrTargetLink;
import com.e3ps.change.RequestOrderLink;
//import com.e3ps.common.code.service.NumberCodeHelper;
import com.e3ps.common.query.SearchUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.epm.bean.EpmData;
import com.e3ps.org.Department;
import com.e3ps.org.People;
import com.e3ps.org.bean.OrgDao;
import com.e3ps.org.bean.PeopleData;
import com.e3ps.part.bean.PartData;
import com.e3ps.project.EProject;
import com.e3ps.project.beans.ProjectData;

import wt.epm.EPMDocument;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.WTObject;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.part.WTPartMaster;
import wt.query.ArrayExpression;
import wt.query.ClassAttribute;
import wt.query.KeywordExpression;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.query.StringSearch;
import wt.query.SubSelectExpression;
import wt.query.TableExpression;
import wt.session.SessionHelper;
import wt.util.WTAttributeNameIfc;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;
import wt.vc.VersionControlHelper;


public class ChangeECRSearch {
	
	public static ChangeECRSearch manager  = new ChangeECRSearch();
	
	public static QuerySpec getECRQuery(Map<String, Object> reqMap) throws Exception{

		QuerySpec qs  = new QuerySpec();
		int idx = qs.addClassList(EChangeRequest2.class, true);
		SearchCondition sc = null;
		try {
			String requestNumber = StringUtil.checkNull((String) reqMap.get("requestNumber"));
			String name = StringUtil.checkNull((String) reqMap.get("name"));
			name = name.replace("[", "[[]");
			String predate = StringUtil.checkNull((String) reqMap.get("predate"));
			String postdate = StringUtil.checkNull((String) reqMap.get("postdate"));
			String modifyPredate = StringUtil.checkNull((String) reqMap.get("modifyPredate"));
			String modifyPostdate = StringUtil.checkNull((String) reqMap.get("modifyPostdate"));
			String preSpecificDate = StringUtil.checkNull((String) reqMap.get("preSpecificDate"));
			String postSpecificDate = StringUtil.checkNull((String) reqMap.get("postSpecificDate"));
			String applyDate = StringUtil.checkNull((String) reqMap.get("applyDate"));
			String sortCheck = StringUtil.checkNull(String.valueOf((boolean) reqMap.get("sortCheck")));
			String sortValue = StringUtil.checkNull((String) reqMap.get("sortValue"));
			List<String> relatedProject = StringUtil.checkReplaceArray(reqMap.get("relatedProject"));
			List<String> creator = StringUtil.checkReplaceArray(reqMap.get("creator"));
			List<String> state = StringUtil.checkReplaceArray(reqMap.get("state"));
			
			//번호
			if(requestNumber.length() > 0) {
				if(qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				sc = new SearchCondition(EChangeRequest2.class, EChangeRequest2.REQUEST_NUMBER, SearchCondition.LIKE, "%" + requestNumber + "%", false);
				qs.appendWhere(sc, new int[] { idx });
			}
			
			//이름
			if(name.length() > 0) {
				if(qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				sc = new SearchCondition(EChangeRequest2.class, EChangeRequest2.NAME, SearchCondition.LIKE, "%" + name + "%", false);
				qs.appendWhere(sc, new int[] { idx });
			}
			
			// 등록자
			if (creator.size() > 0) {
				List<Long> userOidLongValueList = new ArrayList<>();

				for (String pp : creator) {
					People people = (People) CommonUtil.getObject(pp);
					WTUser user = people.getUser();

					userOidLongValueList.add(CommonUtil.getOIDLongValue(user));
				}

				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				sc = new SearchCondition(
						new ClassAttribute(EChangeRequest2.class, "creator.key.id"), SearchCondition.IN,
						new ArrayExpression(userOidLongValueList.toArray()));
				qs.appendWhere(sc, new int[] { idx });
			}

			// 적용 요구 시점
			if(applyDate.length() > 0) {
				if(applyDate.equals("specificDate")) {
					// 특정일자 Start
					if(preSpecificDate.trim().length() > 0){
					     if (qs.getConditionCount() > 0)
					        qs.appendAnd();
					     qs.appendWhere(new SearchCondition(EChangeRequest2.class,EChangeRequest2.SPECIFIC_DATE,SearchCondition.GREATER_THAN_OR_EQUAL, DateUtil.convertStartDate(preSpecificDate)),	new int[] { idx });
					}
					// 특정일자 End
					if(postSpecificDate.trim().length() > 0){
					    if (qs.getConditionCount() > 0)
					        qs.appendAnd();
					    qs.appendWhere(new SearchCondition(EChangeRequest2.class,EChangeRequest2.SPECIFIC_DATE,SearchCondition.LESS_THAN, DateUtil.convertEndDate(postSpecificDate)),	new int[] { idx }); 
					}
					qs.appendAnd();
					sc = new SearchCondition(EChangeRequest2.class, EChangeRequest2.APPLY_DATE, SearchCondition.EQUAL, "specificDate", false);
					qs.appendWhere(sc, new int[] { idx });
				} else {
					if(qs.getConditionCount() > 0) {
						qs.appendAnd();
					}
					sc = new SearchCondition(EChangeRequest2.class, EChangeRequest2.APPLY_DATE, SearchCondition.EQUAL, applyDate, false);
					qs.appendWhere(sc, new int[] { idx });
				}
			}
			
			//작성일_이전
			if(predate.trim().length() > 0){
			     if (qs.getConditionCount() > 0)
			        qs.appendAnd();
			     qs.appendWhere(new SearchCondition(EChangeRequest2.class,"thePersistInfo.createStamp",SearchCondition.GREATER_THAN_OR_EQUAL, DateUtil.convertStartDate(predate)),	new int[] { idx });
			}
			//작성일_이후
			if(postdate.trim().length() > 0){
			    if (qs.getConditionCount() > 0)
			        qs.appendAnd();
			    qs.appendWhere(new SearchCondition(EChangeRequest2.class,"thePersistInfo.createStamp",SearchCondition.LESS_THAN, DateUtil.convertEndDate(postdate)),	new int[] { idx }); 
			}
			
			//수정일_이전
			if(modifyPredate.trim().length() > 0){
				if (qs.getConditionCount() > 0)
					qs.appendAnd();
				qs.appendWhere(new SearchCondition(EChangeRequest2.class,"thePersistInfo.modifyStamp",SearchCondition.GREATER_THAN_OR_EQUAL, DateUtil.convertStartDate(modifyPredate)),	new int[] { idx });
			}
			//수정일_이후
			if(modifyPostdate.trim().length() > 0){
				if (qs.getConditionCount() > 0)
					qs.appendAnd();
				qs.appendWhere(new SearchCondition(EChangeRequest2.class,"thePersistInfo.modifyStamp",SearchCondition.LESS_THAN, DateUtil.convertEndDate(modifyPostdate)),	new int[] { idx }); 
			}
			
			//상태
			if (state.size() > 0) {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				sc = new SearchCondition(new ClassAttribute(EChangeRequest2.class, EChangeRequest2.LIFE_CYCLE_STATE), SearchCondition.IN,
						new ArrayExpression(state.toArray()));
				qs.appendWhere(sc, new int[] { idx });
			}
			
			//관련 프로젝트
			if(relatedProject.size() > 0){
				int pjtIdx = qs.addClassList(EcrProjectLink.class, false);
				
				List<Long> projectOidLongValueList = new ArrayList<>();
	    		for(String oid : relatedProject) {
	    			projectOidLongValueList.add(CommonUtil.getOIDLongValue(oid));
	    		}
	    		
	    		if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
	    		
	    		sc = new SearchCondition(new ClassAttribute(EcrProjectLink.class, "roleBObjectRef.key.id"), SearchCondition.IN, new ArrayExpression(projectOidLongValueList.toArray()));
	    		qs.appendWhere(sc, new int[] { pjtIdx });
	    		
	    		qs.appendAnd();
	    		sc = new SearchCondition(new ClassAttribute(EcrProjectLink.class, "roleAObjectRef.key.id"), SearchCondition.EQUAL, new ClassAttribute(EChangeRequest2.class, EChangeRequest2.PERSIST_INFO + ".theObjectIdentifier.id"));
	    		qs.appendWhere(sc, new int[] {pjtIdx, idx});

			}
			
			boolean desc = false;
			if (!"asc".equals(sortCheck)) {
				desc = true;
			}
			if(sortValue==null || sortValue.length()==0){
				sortValue = "updateDate";
			}
			
			// #. 필드별 컬럼정보 (jqgrid 의 Column 설정 확인할것)
			Map<String, String> sortColumnMap = new HashMap<String, String>();
			sortColumnMap.put("requestNumber", EChangeRequest2.REQUEST_NUMBER);
			sortColumnMap.put("name", EChangeRequest2.NAME);
			sortColumnMap.put("createDate", WTObject.CREATE_TIMESTAMP);
			sortColumnMap.put("updateDate", WTObject.MODIFY_TIMESTAMP);
			sortColumnMap.put("tempcreator", SearchUtil.USER_PREFIX+"workerReference.key.id");
			sortColumnMap.put("lifeCycleState", "state.state");
			sortColumnMap.put("project", SearchUtil.IBA_PREFIX + "MODEL");
			
			SearchUtil.appendOrderBy(qs,EChangeRequest2.class, sortColumnMap.get(sortValue) ,idx, desc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return qs;
	}
	
	private static QuerySpec getSubQuery(QuerySpec mainQuery,Class linkClass,Class roleBClass, String column,String value, String alias) throws WTPropertyVetoException, WTException{
		StringSearch stringsearch = new StringSearch(column);
		stringsearch.setValue(value.trim());
        QuerySpec qs = new QuerySpec();
		qs.getFromClause().setAliasPrefix(alias);
		int ii = qs.addClassList(EChangeRequest2.class, false);
		int jj = qs.addClassList(linkClass, false);
		int mm = qs.addClassList(roleBClass, false);
		qs.appendSelect(new ClassAttribute(EChangeRequest2.class,WTAttributeNameIfc.ID_NAME),new int[]{ii},true);
		qs.appendWhere(new SearchCondition(EChangeRequest2.class,WTAttributeNameIfc.ID_NAME,linkClass,WTAttributeNameIfc.ROLEA_OBJECT_ID),new int[]{ii,jj});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(roleBClass,WTAttributeNameIfc.ID_NAME,linkClass,WTAttributeNameIfc.ROLEB_OBJECT_ID),new int[]{mm,jj});
		qs.appendAnd();
		qs.appendWhere(stringsearch.getSearchCondition(roleBClass),new int[]{mm});
		TableExpression[] tables = new TableExpression[2];
		String[] aliases = new String[2];
		tables[0] = mainQuery.getFromClause().getTableExpressionAt(0);
		aliases[0] = mainQuery.getFromClause().getAliasAt(0);
		tables[1] = qs.getFromClause().getTableExpressionAt(ii);
		aliases[1] = qs.getFromClause().getAliasAt(ii);
		SearchCondition correlatedJoin = new SearchCondition(EChangeRequest2.class, WTAttributeNameIfc.ID_NAME, EChangeRequest2.class, WTAttributeNameIfc.ID_NAME);
		qs.appendAnd();
		qs.appendWhere(correlatedJoin, tables, aliases);
		mainQuery.setAdvancedQueryEnabled(true);
		return qs;
	}

	/**
	 * ecr에서 Target part link
	 * @param eco
	 * @return
	 */
	public static List<WTPart> getECOrelatedParts(EChangeRequest2 ecr) {
		List<WTPart> relatedParts = new ArrayList<WTPart>();
		QueryResult qr = null;
		try {
			QuerySpec qs = new QuerySpec();
			int ii = qs.addClassList(EcrTargetLink.class,true);
			int jj = qs.addClassList(WTPart.class,true);
			qs.appendWhere(new SearchCondition(EcrTargetLink.class,"roleAObjectRef.key.id","=",ecr.getPersistInfo().getObjectIdentifier().getId()),new int[]{ii});
			qs.appendAnd();

			ClassAttribute target = new ClassAttribute(EcrTargetLink.class, "roleBObjectRef.key.id");
			ClassAttribute wtpart = new ClassAttribute(WTPart.class, "thePersistInfo.theObjectIdentifier.id");

			SearchCondition value = new SearchCondition(wtpart, "=", target);
			qs.appendWhere(value, new int[]{jj, ii});

			qr = PersistenceHelper.manager.find(qs);
			while(qr.hasMoreElements()){
				Object[] o = (Object[])qr.nextElement();
				EcrTargetLink link = (EcrTargetLink)o[0];
				WTPart part = (WTPart)o[1];
				relatedParts.add(part);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return relatedParts;
	}
	
	public static List<PartData> getECOrelatedPartDatas(EChangeRequest2 ecr) {
		List<PartData> relatedParts = new ArrayList<PartData>();
		QueryResult qr = null;
		try {
			QuerySpec qs = new QuerySpec();
			int ii = qs.addClassList(EcrTargetLink.class,true);
			int jj = qs.addClassList(WTPart.class,true);
			qs.appendWhere(new SearchCondition(EcrTargetLink.class,"roleAObjectRef.key.id","=",ecr.getPersistInfo().getObjectIdentifier().getId()),new int[]{ii});
			qs.appendAnd();

			ClassAttribute target = new ClassAttribute(EcrTargetLink.class, "roleBObjectRef.key.id");
			ClassAttribute wtpart = new ClassAttribute(WTPart.class, "thePersistInfo.theObjectIdentifier.id");

			SearchCondition value = new SearchCondition(wtpart, "=", target);
			qs.appendWhere(value, new int[]{jj, ii});

			qr = PersistenceHelper.manager.find(qs);
			
			while(qr.hasMoreElements()){
				Object[] o = (Object[])qr.nextElement();
				EcrTargetLink link = (EcrTargetLink)o[0];
				WTPart part = (WTPart)o[1];
				PartData data = new PartData(part);
				relatedParts.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return relatedParts;
	}
	
	/**
	 * 관련 ecr Target part link
	 * @param eco
	 * @return
	 */
	public static JSONArray getECOrelatedPartsJSO(EChangeRequest2 ecr) {
		JSONArray jsonRows = new JSONArray();
		QueryResult qr = null;
		try {
			QuerySpec qs = new QuerySpec();
			int ii = qs.addClassList(EcrTargetLink.class,true);
			int jj = qs.addClassList(WTPart.class,true);
			qs.appendWhere(new SearchCondition(EcrTargetLink.class,"roleAObjectRef.key.id","=",ecr.getPersistInfo().getObjectIdentifier().getId()),new int[]{ii});
			qs.appendAnd();

			ClassAttribute target = new ClassAttribute(EcrTargetLink.class, "roleBObjectRef.key.id");
			ClassAttribute wtpart = new ClassAttribute(WTPart.class, "thePersistInfo.theObjectIdentifier.id");

			SearchCondition value = new SearchCondition(wtpart, "=", target);
			qs.appendWhere(value, new int[]{jj, ii});

			qr = PersistenceHelper.manager.find(qs);
			
			while(qr.hasMoreElements()){
				Object[] o = (Object[])qr.nextElement();
				EcrTargetLink link = (EcrTargetLink)o[0];
				WTPart part = (WTPart)o[1];
				PartData partData = new PartData(part);
				String icon = partData.getIcon();
				String partOid = WCUtil.getOid(part);
				String version = partData.getVersion() 
						+ "." + partData.getIteration() + "(" + part.getViewName() + ")";
				String revision = partData.getVersion();
				
//				HashMap ibaMap = partData.getIBAAttributes();
				
				// #. iba 중 spec 값을 name 으로 사용함
//				String partNameSpec = TypeUtil.stringValue(ibaMap.get("SPEC"));
				
				JSONObject jsonRow = new JSONObject();
				jsonRow.put("id", partOid);
				jsonRow.put("oid", partOid);
				jsonRow.put("number", partData.getNumber());
				jsonRow.put("name", partData.getName());
				jsonRow.put("icon", icon);
				jsonRow.put("version", version);
				jsonRow.put("revision", revision);
				
				JSONObject jsonState = new JSONObject();
				jsonState.put("value", part.getLifeCycleState().toString());
				jsonState.put("name", part.getLifeCycleState().getDisplay());
				jsonRow.put("state", jsonState);
				
				jsonRow.put("creatorName", part.getCreatorName());
				jsonRow.put("creatorFullName", part.getCreatorName());
				jsonRow.put("createDate", partData.getCreateDate().substring(0, 10));
				jsonRow.put("updateDate", DateUtil.getDateString(part.getPersistInfo().getModifyStamp(), "d"));
				
				// IBA 에서 조회 후 코드값으로 처리
				JSONObject jsonIba = new JSONObject();
				// area
//				JSONObject jsonArea = new JSONObject();
//				String areaValue = TypeUtil.stringValue(ibaMap.get("AREA"));
//				jsonArea.put("value", areaValue);
//				jsonArea.put("name", NumberCodeHelper.service.getName("AREA", areaValue));
//				jsonIba.put("area", jsonArea);
//				
//				// model
//				jsonIba.put("model", TypeUtil.stringValue(ibaMap.get("MODEL")));
//				
//				// bumun
//				JSONObject jsonBumun = new JSONObject();
//				String bumunValue = TypeUtil.stringValue(ibaMap.get("BUMUN"));
//				jsonBumun.put("value", bumunValue);
//				jsonBumun.put("name", NumberCodeHelper.service.getName("BUMUN", bumunValue));
//				jsonIba.put("bumun", jsonBumun);
//				
//				// partProductGroup1
//				JSONObject jsonPartProductGroup1 = new JSONObject();
//				String partProductGroup1Value = TypeUtil.stringValue(ibaMap.get("PARTPRODUCTGROUP1"));
//				jsonPartProductGroup1.put("value", partProductGroup1Value);
//				jsonPartProductGroup1.put("name", NumberCodeHelper.service.getName("PARTPRODUCTGROUP1", partProductGroup1Value));
//				jsonIba.put("partProductGroup1", jsonPartProductGroup1);
//				
//				// partProductGroup2
//				JSONObject jsonPartProductGroup2 = new JSONObject();
//				String partProductGroup2Value = TypeUtil.stringValue(ibaMap.get("PARTPRODUCTGROUP2"));
//				jsonPartProductGroup2.put("value", partProductGroup2Value);
//				jsonPartProductGroup2.put("name", NumberCodeHelper.service.getName("PARTPRODUCTGROUP2", partProductGroup2Value));
//				jsonIba.put("partProductGroup2", jsonPartProductGroup2);
//				
//				// phantom
//				JSONObject jsonPhantom = new JSONObject();
//				String phantomValue = TypeUtil.stringValue(ibaMap.get("PHANTOM"));
//				jsonPhantom.put("value", phantomValue);
//				jsonPhantom.put("name", NumberCodeHelper.service.getName("PHANTOM", phantomValue));
//				jsonIba.put("phantom", jsonPhantom);
//				
//				// pm
//				JSONObject jsonPm = new JSONObject();
//				String pmValue = TypeUtil.stringValue(ibaMap.get("PM"));
//				jsonPm.put("value", pmValue);
//				jsonPm.put("name", NumberCodeHelper.service.getName("PM", pmValue));
//				jsonIba.put("pm", jsonPm);
//				
//				// delivery
//				JSONObject jsonDelivery = new JSONObject();
//				String deliveryValue = TypeUtil.stringValue(ibaMap.get("DELIVERY"));
//				jsonDelivery.put("value", deliveryValue);
//				jsonDelivery.put("name", NumberCodeHelper.service.getName("DELIVERY", deliveryValue));
//				jsonIba.put("delivery", jsonDelivery);
//				
//				// domestic
//				JSONObject jsonDomestic = new JSONObject();
//				String domesticValue = TypeUtil.stringValue(ibaMap.get("DOMESTIC"));
//				jsonDomestic.put("value", domesticValue);
//				jsonDomestic.put("name", NumberCodeHelper.service.getName("DOMESTIC", domesticValue));
//				jsonIba.put("domestic", jsonDomestic);
//				
//				// unit
//				JSONObject jsonUnit = new JSONObject();
//				String unitValue = TypeUtil.stringValue(ibaMap.get("UNIT"));
//				jsonUnit.put("value", unitValue);
//				jsonUnit.put("name", NumberCodeHelper.service.getName("UNIT", unitValue));
//				jsonIba.put("unit", jsonUnit);
				
				// partNameSpec
//				jsonIba.put("partNameSpec", partNameSpec);
//				
//				// partNameSpecInfosJson
//				String partNameSpecInfosJson = TypeUtil.stringValue(ibaMap.get("SPECINFO"));
//				jsonIba.put("partNameSpecInfosJson", partNameSpecInfosJson);
				
				jsonRow.put("iba", jsonIba);
				
				jsonRows.put(jsonRow);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonRows;
	}

	/**
	 * eco에서 Target epm link
	 * @param eco
	 * @return
	 */
	public static List<Object> getECOrelatedDrawings(EChangeRequest2 ecr) {
		List<Object> relatedDrawings = new ArrayList<Object>();
		QueryResult qr = null;
		try {
			QuerySpec qs = new QuerySpec();
			int ii = qs.addClassList(EcrTargetLink.class,true);
			int ee = qs.addClassList(EPMDocument.class,true);
			qs.appendWhere(new SearchCondition(EcrTargetLink.class,"roleAObjectRef.key.id","=",ecr.getPersistInfo().getObjectIdentifier().getId()),new int[]{ii});
			qs.appendAnd();
			ClassAttribute ca1 = new ClassAttribute(EcrTargetLink.class, "roleBObjectRef.key.id");
			ClassAttribute ca2 = new ClassAttribute(EPMDocument.class, "thePersistInfo.theObjectIdentifier.id");

			SearchCondition sc1 = new SearchCondition(ca2, "=", ca1);
			qs.appendWhere(sc1, new int[]{ee, ii});

			qr = PersistenceHelper.manager.find(qs);
			
			while(qr.hasMoreElements()){
				Object[] o = (Object[])qr.nextElement();
				EcrTargetLink link = (EcrTargetLink)o[0];
				EPMDocument epm = (EPMDocument)o[1];
				relatedDrawings.add(epm);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return relatedDrawings;
	}
	
	public static List<EPMDocument> getECOrelatedDrawings2(EChangeRequest2 ecr) {
		List<EPMDocument> relatedDrawings = new ArrayList<EPMDocument>();
		QueryResult qr = null;
		try {
			QuerySpec qs = new QuerySpec();
			int ii = qs.addClassList(EcrTargetLink.class,true);
			int ee = qs.addClassList(EPMDocument.class,true);
			qs.appendWhere(new SearchCondition(EcrTargetLink.class,"roleAObjectRef.key.id","=",ecr.getPersistInfo().getObjectIdentifier().getId()),new int[]{ii});
			qs.appendAnd();
			ClassAttribute ca1 = new ClassAttribute(EcrTargetLink.class, "roleBObjectRef.key.id");
			ClassAttribute ca2 = new ClassAttribute(EPMDocument.class, "thePersistInfo.theObjectIdentifier.id");

			SearchCondition sc1 = new SearchCondition(ca2, "=", ca1);
			qs.appendWhere(sc1, new int[]{ee, ii});

			qr = PersistenceHelper.manager.find(qs);
			
			while(qr.hasMoreElements()){
				Object[] o = (Object[])qr.nextElement();
				EcrTargetLink link = (EcrTargetLink)o[0];
				EPMDocument epm = (EPMDocument)o[1];
				relatedDrawings.add(epm);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return relatedDrawings;
	}
	
	public static List<EpmData> getECOrelatedDrawingDatas(EChangeRequest2 ecr) {
		List<EpmData> relatedDrawings = new ArrayList<EpmData>();
		QueryResult qr = null;
		try {
			QuerySpec qs = new QuerySpec();
			int ii = qs.addClassList(EcrTargetLink.class,true);
			int ee = qs.addClassList(EPMDocument.class,true);
			qs.appendWhere(new SearchCondition(EcrTargetLink.class,"roleAObjectRef.key.id","=",ecr.getPersistInfo().getObjectIdentifier().getId()),new int[]{ii});
			qs.appendAnd();
			ClassAttribute ca1 = new ClassAttribute(EcrTargetLink.class, "roleBObjectRef.key.id");
			ClassAttribute ca2 = new ClassAttribute(EPMDocument.class, "thePersistInfo.theObjectIdentifier.id");

			SearchCondition sc1 = new SearchCondition(ca2, "=", ca1);
			qs.appendWhere(sc1, new int[]{ee, ii});

			qr = PersistenceHelper.manager.find(qs);
			
			while(qr.hasMoreElements()){
				Object[] o = (Object[])qr.nextElement();
				EcrTargetLink link = (EcrTargetLink)o[0];
				EPMDocument epm = (EPMDocument)o[1];
				EpmData data = new EpmData(epm);
				relatedDrawings.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return relatedDrawings;
	}
	
	/**
	 * 관련 ecr Target epm link
	 * @param eco
	 * @return
	 */
	public static JSONArray getECOrelatedDrawingsJSO(EChangeRequest2 ecr) {
		JSONArray jsonDrawing = new JSONArray();
		QueryResult qr = null;
		try {
			QuerySpec qs = new QuerySpec();
			int ii = qs.addClassList(EcrTargetLink.class,true);
			int ee = qs.addClassList(EPMDocument.class,true);
			qs.appendWhere(new SearchCondition(EcrTargetLink.class,"roleAObjectRef.key.id","=",ecr.getPersistInfo().getObjectIdentifier().getId()),new int[]{ii});
			qs.appendAnd();
			ClassAttribute ca1 = new ClassAttribute(EcrTargetLink.class, "roleBObjectRef.key.id");
			ClassAttribute ca2 = new ClassAttribute(EPMDocument.class, "thePersistInfo.theObjectIdentifier.id");

			SearchCondition sc1 = new SearchCondition(ca2, "=", ca1);
			qs.appendWhere(sc1, new int[]{ee, ii});

			qr = PersistenceHelper.manager.find(qs);
			
			while(qr.hasMoreElements()){
				Object[] o = (Object[])qr.nextElement();
				EcrTargetLink link = (EcrTargetLink)o[0];
				EPMDocument epm = (EPMDocument)o[1];
				String epmOid = WCUtil.getOid(epm);
				JSONObject jsonRow = new JSONObject();
				jsonRow.put("id", epm.getPersistInfo().getObjectIdentifier().toString());
				jsonRow.put("oid", epmOid);
				jsonRow.put("number", epm.getNumber());
				jsonRow.put("name", epm.getName().toString());
				jsonRow.put("version", VersionControlHelper.getVersionIdentifier(epm).getSeries().getValue()+"."+epm.getIterationIdentifier().getSeries().getValue());
				jsonRow.put("cadName", epm.getCADName());
				// #. EPM에 IBA 값을 가져옴
//				EpmData epmData = new EpmData(epm);
//				String partNameSpec = TypeUtil.stringValue(epmData.getIbaAttr().get("SPEC"));
//				String partName = TypeUtil.stringValue(epmData.getIbaAttr().get("PART_NAME"));
//				String partNumber = TypeUtil.stringValue(epmData.getIbaAttr().get("PART_NO"));
//				String projectName = TypeUtil.stringValue(epmData.getIbaAttr().get("PROJECT_NAME"));
//				String modelName = TypeUtil.stringValue(epmData.getIbaAttr().get("MODEL"));
//				
//				String drawingSizeCode = TypeUtil.stringValue(epmData.getIbaAttr().get("DRAWINGSIZE"));
//				String drawingSize = NumberCodeHelper.service.getName("DRAWINGSIZE", drawingSizeCode);
//				String drawingProductCode = TypeUtil.stringValue(epmData.getIbaAttr().get("DRWPRODUCTTYPE"));
//				String drawingProduct = NumberCodeHelper.service.getName("DRWPRODUCTTYPE", drawingProductCode);
//				String drawingTypeCode = TypeUtil.stringValue(epmData.getIbaAttr().get("DRAWINGTYPE"));
//				String drawingType = NumberCodeHelper.service.getName("DRAWINGTYPE", drawingTypeCode);
//				String drawingNumber = TypeUtil.stringValue(epmData.getIbaAttr().get("DRAWING_NUMBER"));
//				
//				JSONObject ibaObject = new JSONObject();
//				ibaObject.put("partNameSpec", partNameSpec);
//				ibaObject.put("partName", partName);
//				ibaObject.put("partNumber", partNumber);
//				ibaObject.put("projectName", projectName);
//				ibaObject.put("modelName", modelName);
//				ibaObject.put("drawingSize", drawingSize);
//				ibaObject.put("drawingProduct", drawingProduct);
//				ibaObject.put("drawingType", drawingType);
//				ibaObject.put("drawingNumber", drawingNumber);
//
//				jsonRow.put("iba", ibaObject);
				
				jsonDrawing.put(jsonRow);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonDrawing;
	}

	public static QuerySpec getMyworkECRQuery() throws WTException {
		
		QuerySpec qs  = new QuerySpec();
		int ii = qs.addClassList(EChangeRequest2.class, true);
		int uu = qs.addClassList(WTUser.class, false);
		int pp = qs.addClassList(People.class, false);
		int dd = qs.addClassList(Department.class, false);
		try {
			
			WTUser user = (WTUser)SessionHelper.manager.getPrincipal();
			PeopleData pd = new PeopleData(user);
			//String deptOid = CommonUtil.getOIDString(pd.department);
			String deptOid = pd.getDepartmentOID();
			Department dept = (Department)CommonUtil.getObject(deptOid);
			
			qs.appendOpenParen();
			ApprovalHelper.service.appendStateSearchCondition(qs,EChangeRequest2.class,ii,ApprovalService.MASTER_APPROVING);
			qs.appendOr();
			ApprovalHelper.service.appendStateSearchCondition(qs,EChangeRequest2.class,ii,ApprovalService.MASTER_INWORK);
			qs.appendOr();
			ApprovalHelper.service.appendStateSearchCondition(qs,EChangeRequest2.class,ii,ApprovalService.MASTER_REJECTED);
			qs.appendCloseParen();
			
			qs.appendAnd();
			qs.appendSelect(new ClassAttribute(EChangeRequest2.class,"owner.key.id"),new int[]{ii},true);

			qs.appendWhere(new SearchCondition(EChangeRequest2.class, "owner.key.id",
					WTUser.class, "thePersistInfo.theObjectIdentifier.id"), new int[] { ii, uu });
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(People.class, "userReference.key.id",
					WTUser.class, "thePersistInfo.theObjectIdentifier.id"), new int[] { pp, uu });
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(People.class, "departmentReference.key.id",
					Department.class, "thePersistInfo.theObjectIdentifier.id"), new int[] { pp, dd });
			
			if(dept!=null){
				ArrayList list = OrgDao.service.getDepartmentTree(dept.getPersistInfo().getObjectIdentifier().getId());

				qs.appendAnd();
				qs.appendOpenParen();
				qs.appendWhere(new SearchCondition(Department.class, "thePersistInfo.theObjectIdentifier.id", "=", dept.getPersistInfo().getObjectIdentifier().getId()), new int[] { dd });
				for(int i=0; i< list.size(); i++){
					String[] node = (String[])list.get(i);
					
					qs.appendOr();
					
					long did = Long.parseLong(node[2].substring(node[2].lastIndexOf(":")+1));
					qs.appendWhere(new SearchCondition(Department.class, "thePersistInfo.theObjectIdentifier.id", "=", did), new int[] { dd });
				}
				qs.appendCloseParen();
				TableExpression[] tables = new TableExpression[2];
				String[] aliases = new String[2];
				tables[0] = qs.getFromClause().getTableExpressionAt(0);
				aliases[0] = qs.getFromClause().getAliasAt(0);
				tables[1] = qs.getFromClause().getTableExpressionAt(ii);
				aliases[1] = qs.getFromClause().getAliasAt(ii);
				SearchCondition correlatedJoin = new SearchCondition(EChangeRequest2.class, "owner.key.id", EChangeRequest2.class, "owner.key.id");
				qs.appendAnd();
				qs.appendWhere(correlatedJoin, tables, aliases);
				qs.setAdvancedQueryEnabled(true);
			}
				
		} catch (Exception e) {
			throw new WTException(e.getLocalizedMessage());
		}
		return qs;
	}
	
	public static List<ProjectData> getRelatedProject(EChangeRequest2 ecr) throws Exception{
		List<ProjectData> relatedProjects = new ArrayList<ProjectData>();
		QueryResult qr = PersistenceHelper.manager.navigate(ecr,"project",EcrProjectLink.class);
		if (qr.size() > 0) {
			while(qr.hasMoreElements()){
				EProject project = (EProject)qr.nextElement();
				ProjectData data = new ProjectData(project);
				relatedProjects.add(data);
			}
		}
		return relatedProjects;
	}
	
	public static List<ECOData> getRelatedEco(EChangeRequest2 ecr) throws Exception{
		List<ECOData> relatedECOs = new ArrayList<ECOData>();
		QueryResult qr = PersistenceHelper.manager.navigate(ecr,"order",RequestOrderLink.class);
		if (qr.size() > 0) {
			while(qr.hasMoreElements()){
				EChangeOrder2 eco = (EChangeOrder2)qr.nextElement();
				ECOData data = new ECOData(eco);
				relatedECOs.add(data);
			}
		}
		return relatedECOs;
	}
	
	/**
	 * ECR Part  관련 링크
	 * @methodName : getECRPartLink
	 * @author : tsuam
	 * @date : 2021.11.18
	 * @return : void
	 * @description :
	 */
	public List<EcrPartLink> getECRPartLink(String oid ) throws Exception{
		EChangeRequest2 ecr = (EChangeRequest2)CommonUtil.getObject(oid);
		
		return getECRPartLink(ecr);
	}
	
	public List<EcrPartLink> getECRPartLink(EChangeRequest2 ecr ) throws Exception{
		List<EcrPartLink> list = new ArrayList<EcrPartLink>();
		QueryResult qr = PersistenceHelper.manager.navigate(ecr,"part",EcrPartLink.class,false);
		while(qr.hasMoreElements()){
			EcrPartLink link = (EcrPartLink)qr.nextElement();
			
			list.add(link);
		}
		
		return list;
	}
	
}