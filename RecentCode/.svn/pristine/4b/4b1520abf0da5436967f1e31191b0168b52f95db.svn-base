package com.e3ps.common.code.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.e3ps.common.code.NumberCode;
import com.e3ps.common.code.NumberCodeType;
import com.e3ps.common.code.bean.NumberCodeData;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.ServiceFactory;
import wt.util.WTException;

public class CodeHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	public static final CodeService service = ServiceFactory.getService(CodeService.class);
	
	public static final CodeHelper manager = new CodeHelper();
	
	
	public NumberCodeData getNumberCode(String codeType, String code, String pCode) throws Exception{
		NumberCodeData nCode = null; 
		
		QuerySpec qs = new QuerySpec();
		
		int idx = qs.addClassList(NumberCode.class, true);
		
		qs.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", codeType), new int[] { idx });
		
		if(code.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.CODE, SearchCondition.LIKE, "%" + code + "%", false), new int[] { idx });
		}
		
		if(pCode.length() > 0) {
			NumberCode parent = getNumberCode(codeType, pCode);
			
			if(parent != null) {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(NumberCode.class, "parentReference.key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(parent)), new int[] { idx });
			} 
		} else {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			qs.appendWhere(new SearchCondition(NumberCode.class, "parentReference.key.id", SearchCondition.EQUAL, 0L), new int[] { idx });
		}
		
		if(qs != null) {
			QueryResult qr = PersistenceHelper.manager.find(qs);
			
			while(qr.hasMoreElements()) {
				Object[] o = (Object[])qr.nextElement();
				NumberCode n = (NumberCode)o[0];
				nCode = new NumberCodeData(n);
			}
		}
		return nCode;
	}
	
	
	
	public List<NumberCodeData> getNumberCodeList(Map<String, Object> reqMap) throws Exception {
		List<NumberCodeData> list = new ArrayList<NumberCodeData>();
		
		QuerySpec qs = getNumberCodeListQuery(reqMap);
		
		if(qs != null) {
			QueryResult qr = PersistenceHelper.manager.find(qs);
			
			while(qr.hasMoreElements()) {
				Object[] o = (Object[])qr.nextElement();
				NumberCode code = (NumberCode)o[0];
				NumberCodeData data = new NumberCodeData(code);
				list.add(data);
			}
		}
		
		return list;
	}
	
	public QuerySpec getNumberCodeListQuery(Map<String, Object> reqMap) throws Exception {
		
		String code = StringUtil.checkNull((String) reqMap.get("code"));
		String name = StringUtil.checkNull((String) reqMap.get("name"));
		String engName = StringUtil.checkNull((String) reqMap.get("engName"));
		
		String codeType = StringUtil.checkNull((String) reqMap.get("codeType"));
		String parentCode = StringUtil.checkNull((String) reqMap.get("parentCode"));
		
		boolean disabledCheck = false;
		if(reqMap.get("disabledCheck") != null) {
			disabledCheck = (boolean) reqMap.get("disabledCheck");
		}
		
		QuerySpec qs = null;
		
		if(codeType.length() > 0) {
			
			qs = new QuerySpec();
			
			int idx = qs.addClassList(NumberCode.class, true);
			
			qs.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", codeType), new int[] { idx });
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(NumberCode.class, "codeType", "<>", "SG"), new int[] { idx });
			if(disabledCheck) {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(NumberCode.class, "disabled", SearchCondition.IS_FALSE), new int[] { idx });	
			}
			
			if(code.length() > 0) {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.CODE, SearchCondition.LIKE, "%" + code + "%", false), new int[] { idx });
			}
			
			if(name.length() > 0) {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.NAME, SearchCondition.LIKE, "%" + name + "%", false), new int[] { idx });
			}
			
			if(engName.length() > 0) {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.ENG_NAME, SearchCondition.LIKE, "%" + engName + "%", false), new int[] { idx });
			}
			
			if(parentCode.length() > 0) {
				
				NumberCode parent = getNumberCode(codeType, parentCode);
				
				if(parent != null) {
					if (qs.getConditionCount() > 0) {
						qs.appendAnd();
					}
					qs.appendWhere(new SearchCondition(NumberCode.class, "parentReference.key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(parent)), new int[] { idx });
				} else {
					if("JELSTDPARTCODE".equals(codeType) || "UNITDIVISIONCODE".equals(codeType)) {
						codeType = "JELPROCESSTYPE";
						
						parent = getNumberCode(codeType, parentCode);
						
						if(parent != null) {
							if (qs.getConditionCount() > 0) {
								qs.appendAnd();
							}
							
							qs.appendWhere(new SearchCondition(NumberCode.class, "parentReference.key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(parent)), new int[] { idx });
						}
					}
				}
			} else {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(NumberCode.class, "parentReference.key.id", SearchCondition.EQUAL, 0L), new int[] { idx });
			}
			
			if("PROJECTROLE".equals(codeType) ) {
				qs.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class, NumberCode.NAME), false), new int[] { idx });
				qs.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class, NumberCode.SORT), false), new int[] { idx });
			}else {
				qs.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class, NumberCode.SORT), false), new int[] { idx });
				qs.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class, NumberCode.NAME), false), new int[] { idx });
			}
		}
		
		return qs;
	}
	
	public List<NumberCodeData> getNumberCodeListAutoComplete(Map<String, Object> reqMap) throws Exception {
		List<NumberCodeData> list = new ArrayList<NumberCodeData>();
		
		int endLevel = 0;
		
		if(reqMap.get("endLevel") != null) {
			endLevel = (int) reqMap.get("endLevel");
		}
		
		QuerySpec qs = getNumberCodeListAutoCompleteQuery(reqMap);
		
		if(qs != null) {
			QueryResult qr = PersistenceHelper.manager.find(qs);
			
			while(qr.hasMoreElements()) {
				Object[] o = (Object[])qr.nextElement();
				NumberCode code = (NumberCode)o[0];
				NumberCodeData data = new NumberCodeData(code);
				
				int level = getLevel(code, 1);
				
				if(endLevel == level) {
					list.add(data);
				}
			}
		}
		
		return list;
	}
	
	public QuerySpec getNumberCodeListAutoCompleteQuery(Map<String, Object> reqMap) throws Exception {
		
		String value = StringUtil.checkNull((String) reqMap.get("value"));
		
		String codeType = StringUtil.checkNull((String) reqMap.get("codeType"));
		
		String roleCode = StringUtil.checkNull((String) reqMap.get("roleCode"));
		
		int endLevel = (int) reqMap.get("endLevel");
		
		QuerySpec qs = null;
		
		if(codeType.length() > 0) {
			
			qs = new QuerySpec();
			
			int idx = qs.addClassList(NumberCode.class, true);
			
			qs.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", codeType), new int[] { idx });
		
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			qs.appendWhere(new SearchCondition(NumberCode.class, "disabled", SearchCondition.IS_FALSE), new int[] { idx });	
			
			if(value.length() > 0) {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendOpenParen();
				qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.CODE, SearchCondition.LIKE, "%" + value + "%", false), new int[] { idx });
			
				qs.appendOr();
				
				qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.NAME, SearchCondition.LIKE, "%" + value + "%", false), new int[] { idx });
				
				qs.appendOr();
				
				qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.ENG_NAME, SearchCondition.LIKE, "%" + value + "%", false), new int[] { idx });
				qs.appendCloseParen();
			}
			
			if(endLevel != 1){
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(NumberCode.class, "parentReference.key.id", SearchCondition.NOT_EQUAL, 0L), new int[] { idx });
			}
			
			if(roleCode.length() > 0){
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				NumberCode code = CodeHelper.manager.getNumberCode(codeType, roleCode);
				qs.appendWhere(new SearchCondition(NumberCode.class, "parentReference.key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(code)), new int[] { idx });
			}
			
			qs.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class, NumberCode.SORT), false), new int[] { idx });
			
			qs.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class, NumberCode.NAME), false), new int[] { idx });
		}
		
		return qs;
	}
	
	public List<Map<String, Object>> getCodeTypeTree(Map<String, Object> reqMap) throws Exception {
		
		List<Map<String,Object>> list = new ArrayList<>();
		
		String codeType = StringUtil.checkNull((String) reqMap.get("codeType"));
		String roleCode = StringUtil.checkNull((String) reqMap.get("roleCode"));
		
		int endLevel = 0;
		if(reqMap.get("endLevel") != null) {
			endLevel = (int) reqMap.get("endLevel");
		}
		
		NumberCodeType ctype = NumberCodeType.toNumberCodeType(codeType);
		
		//1Level
		QuerySpec qs = new QuerySpec(NumberCode.class);
		
		qs.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", codeType), new int[] { 0 });
		
		if(roleCode.length() > 0){
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			qs.appendWhere(new SearchCondition(NumberCode.class, "code", "=", roleCode), new int[] { 0 });
		}
		
		qs.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class,NumberCode.CODE),false),new int[]{0});
		
		//System.out.println(""+qs.toString());
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		List<Map<String,Object>> newList = new ArrayList<>();
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("code", "");
		map.put("name", ctype.getDisplay());
		map.put("oid", codeType);
		map.put("level", 0);
		newList.add(map);
		
		int lastLevel = 0;
		
		while(qr.hasMoreElements()){
			NumberCode code = (NumberCode) qr.nextElement();
			
			map = new HashMap<String, Object>();
			
			map.put("oid", CommonUtil.getOIDString(code));
			map.put("codeType", codeType);
			map.put("name", code.getName());
			map.put("code", code.getCode());
			if(code.getParent() != null) {
				map.put("parentOid", CommonUtil.getOIDString(code.getParent()));
			} else {
				map.put("parentOid", codeType);
			}
			
			int level = getLevel(code, 1);
			map.put("level", level);
			if(level > lastLevel){
				lastLevel = level;
			}
			
			list.add(map);
			
		}
		
		for(int i = 1 ; i <= lastLevel; i ++ ) {
			if(i == 1) {
				for (int j = 0; j < list.size(); j++) {
					if((int)list.get(j).get("level") == i) {
						newList.add(list.get(j));
					}
				}
			}else {
				List<Map<String,Object>> childList = null;
				for (int k = 0; k < newList.size(); k++) {
					childList = new ArrayList<>();
					for (int l = 0; l < list.size(); l++) {
						int hLevel = (int)newList.get(k).get("level");
						int lLevel = (int)list.get(l).get("level");
						if(hLevel == (i-1) && lLevel == i ) {
							String hOid = (String)newList.get(k).get("oid");
							String lOid = (String)list.get(l).get("parentOid");
							if(hOid.equals(lOid)) {
								childList.add(list.get(l));
							}
						}
					}
					newList.addAll(k+1, childList);
				}
			}
		}
		
		return newList;
	}
	
	public static List<NumberCodeData> getCodeTypeTree2(Map<String, Object> reqMap) throws Exception {
		
		List<NumberCodeData> list = new ArrayList<>();
		
		String codeType = StringUtil.checkNull((String) reqMap.get("codeType"));
		String roleCode = StringUtil.checkNull((String) reqMap.get("roleCode"));
		long prentOid = 0;
		
		int endLevel = 0;
		if(reqMap.get("endLevel") != null) {
			endLevel = (int) reqMap.get("endLevel");
		}
		
		NumberCodeType ctype = NumberCodeType.toNumberCodeType(codeType);
		
		//1Level
		QuerySpec qs = new QuerySpec(NumberCode.class);
		
		qs.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", codeType), new int[] { 0 });
		
		if("JELSTDPARTCODE".equals(codeType) || "UNITDIVISIONCODE".equals(codeType)) {
			qs.appendOr();
			qs.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", "JELPROCESSTYPE"), new int[] { 0 });
		}
		
		if(roleCode.length() > 0){
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			qs.appendWhere(new SearchCondition(NumberCode.class, "code", "=", roleCode), new int[] { 0 });
		}
		
		if (qs.getConditionCount() > 0) {
			qs.appendAnd();
		}
		qs.appendWhere(new SearchCondition(NumberCode.class, "parentReference.key.id", "=", prentOid), new int[] { 0 });
		//query.appendAnd();
		//query.appendWhere(new SearchCondition(NumberCode.class, "disabled", SearchCondition.IS_FALSE), new int[] { 0 });
		//query.appendAnd();
		//query.appendWhere(new SearchCondition(NumberCode.class,"parentReference.key.id",SearchCondition.EQUAL,(long)0),new int[] { 0 });
		
		qs.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class,NumberCode.CODE),false),new int[]{0});
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		while(qr.hasMoreElements()){
			Object[] obj = (Object[]) qr.nextElement();
			NumberCodeData data = new NumberCodeData((NumberCode) obj[0]);
			//NumberCode children = getNumberCode(data.getCodeType(), data.getCode());
			list.add(data);
		}
		
		return list;
	}
	
	public static List<NumberCodeData> getSubNumbercodeList(NumberCode numbercode, String codeType) throws WTException {
		List<NumberCodeData> list = new ArrayList<NumberCodeData>();
			
		try {
			long longOid = 0;
			if(numbercode != null) {
				longOid = CommonUtil.getOIDLongValue(numbercode);
			}
				
			QuerySpec qs = new QuerySpec();
			int idx = qs.addClassList(NumberCode.class, true);
			
			qs.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", codeType), new int[] { 0 });
			
			if("JELSTDPARTCODE".equals(codeType) || "UNITDIVISIONCODE".equals(codeType)) {
				qs.appendOr();
				qs.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", "JELPROCESSTYPE"), new int[] { 0 });
			}
			
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			SearchCondition sc = new SearchCondition(NumberCode.class, "parentReference.key.id",
					SearchCondition.EQUAL, longOid);
			qs.appendWhere(sc, new int[] { idx });
				
			qs.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class, NumberCode.CODE), false),
					new int[] { idx });
				
			QueryResult qr = PersistenceHelper.manager.find(qs);
			while (qr.hasMoreElements()) {
				Object[] obj = (Object[]) qr.nextElement();
				NumberCodeData data = new NumberCodeData((NumberCode) obj[0]);
				NumberCode children = CodeHelper.manager.getNumberCode(data.getCodeType(), data.getCode());
					
				List<NumberCodeData> subList = getSubNumbercodeList(children, data.getCodeType());
					
				if(subList.size()>0) {
					data.setChildren(geSubNumbercodeList(children));
				}
					
				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return list;
	}

	public static List<NumberCodeData> geSubNumbercodeList(NumberCode numbercode) throws WTException {
		List<NumberCodeData> list = new ArrayList<NumberCodeData>();
			
		try {
			long longOid = CommonUtil.getOIDLongValue(numbercode);
				
			QuerySpec qs = new QuerySpec();
			int idx = qs.addClassList(NumberCode.class, true);
				
			SearchCondition sc = new SearchCondition(NumberCode.class, "parentReference.key.id",
					SearchCondition.EQUAL, longOid);
			qs.appendWhere(sc, new int[] { idx });
				
			qs.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class, NumberCode.CODE), false),
					new int[] { idx });
				
			QueryResult qr = PersistenceHelper.manager.find(qs);
			while (qr.hasMoreElements()) {
				Object[] obj = (Object[]) qr.nextElement();
				NumberCodeData data = new NumberCodeData((NumberCode) obj[0]);
				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return list;
	}
	
	public NumberCode getNumberCode(String codeType, String code){
		try{
			QuerySpec query = new QuerySpec(NumberCode.class);

			query.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", codeType), new int[] { 0 });
			query.appendAnd();
			query.appendWhere(new SearchCondition(NumberCode.class, "code", "=", code), new int[] { 0 });
			//query.appendAnd();
			//query.appendWhere(new SearchCondition(NumberCode.class, "disabled", SearchCondition.IS_FALSE), new int[] { 0 });

			query.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class,NumberCode.CODE),false),new int[]{0});
			QueryResult qr = PersistenceHelper.manager.find(query);

			if(qr.hasMoreElements()){
				NumberCode cc = (NumberCode)qr.nextElement();
				return cc;
			}
			return  null;
		}
		catch(Exception ex){
			return null;
		}
	}
	
	public NumberCode getNumberCodeByName(String codeType, String name){
		try{
			QuerySpec query = new QuerySpec(NumberCode.class);

			query.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", codeType), new int[] { 0 });
			query.appendAnd();
			query.appendWhere(new SearchCondition(NumberCode.class, "name", "=", name), new int[] { 0 });
			//query.appendAnd();
			//query.appendWhere(new SearchCondition(NumberCode.class, "disabled", SearchCondition.IS_FALSE), new int[] { 0 });

			query.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class,NumberCode.CODE),false),new int[]{0});
			QueryResult qr = PersistenceHelper.manager.find(query);

			if(qr.hasMoreElements()){
				NumberCode cc = (NumberCode)qr.nextElement();
				return cc;
			}
			return  null;
		}
		catch(Exception ex){
			return null;
		}
	}
	
	public NumberCode getEffectedNumberCode(String codeType, String code){
		try{
			QuerySpec query = new QuerySpec(NumberCode.class);

			query.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", codeType), new int[] { 0 });
			query.appendAnd();
			query.appendWhere(new SearchCondition(NumberCode.class, "description",  SearchCondition.LIKE, "%" + code + "%"), new int[] { 0 });
			//query.appendAnd();
			//query.appendWhere(new SearchCondition(NumberCode.class, "disabled", SearchCondition.IS_FALSE), new int[] { 0 });

			query.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class,NumberCode.CODE),false),new int[]{0});
			QueryResult qr = PersistenceHelper.manager.find(query);

			if(qr.hasMoreElements()){
				NumberCode cc = (NumberCode)qr.nextElement();
				return cc;
			}
			return  null;
		}
		catch(Exception ex){
			return null;
		}
	}
	
	
	/**
	 * @desc	: 이름으로 NumberCode list조회
	 * @author	: tsjeong
	 * @date	: 2019. 9. 26.
	 * @method	: getNumberCode
	 * @return	: NumberCode
	 * @param name
	 * @return
	 */
	public List<Long> getNumberCodeList(String name, String codeType){
		try{
			List<Long> list = new ArrayList<Long>();
			
			QuerySpec query = new QuerySpec();
			
			int idx = query.addClassList(NumberCode.class, true);
			
			query.appendWhere(new SearchCondition(NumberCode.class, NumberCode.NAME, SearchCondition.LIKE, "%" +name+ "%", false), new int[] { 0 });
			query.appendAnd();
			query.appendWhere(new SearchCondition(NumberCode.class, NumberCode.CODE_TYPE, SearchCondition.EQUAL, codeType), new int[] { 0 });
			
			QueryResult qr = PersistenceHelper.manager.find(query);
			
			while(qr.hasMoreElements()) {
				Object[] o = (Object[])qr.nextElement();
				NumberCode code = (NumberCode)o[0];
				long data = CommonUtil.getOIDLongValue(code);
				list.add(data);
			}
			return list;
		}
		catch(Exception ex){
			return null;
		}
	}
	
	public List<NumberCodeData> getCodeListByCodeType(Map<String, Object> reqMap) throws Exception {
		List<NumberCodeData> list = new ArrayList<NumberCodeData>();
		
		QuerySpec qs = getNumberCodeListQuery(reqMap);
		
		if(qs != null) {
			QueryResult qr = PersistenceHelper.manager.find(qs);
			
			while(qr.hasMoreElements()) {
				Object[] o = (Object[])qr.nextElement();
				NumberCode code = (NumberCode)o[0];
				NumberCodeData data = new NumberCodeData(code);
				list.add(data);
			}
		}
		
		return list;
	}
	
	private int getLevel(NumberCode code, int level){
		
		if(code == null) return 0;
		if(code.getParent() != null){
			level = getLevel(code.getParent(), level + 1);
		}
		
		return level;
	}
	public static void main(String[] args) {
		List<Long> list = new ArrayList<Long>();
		list = CodeHelper.manager.getNumberCodeList("이", "SITE");
		LOGGER.info("결과 - 결과 - 결과 -"+ list.get(0));
	}

	/**
	 * @desc	: number code List
	 * @author	: mnyu
	 * @date	: 2020. 2. 21.
	 * @method	: getNumberCodeList
	 * @return	: List<NumberCodeData>
	 * @param reqMap
	 * @return
	 */
	public List<NumberCodeData> getNumberCodeList(String codeType) throws Exception{
		List<NumberCodeData> list = new ArrayList<>();
		QuerySpec qs = new QuerySpec();
		int idx = qs.appendClassList(NumberCode.class, true);
		
		qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.CODE_TYPE, SearchCondition.EQUAL, codeType), new int[]{idx});
		
		qs.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class, "parentReference.key.id"),false),new int[]{0});
		
		qs.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class, "code"),false),new int[]{0});
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		while(qr.hasMoreElements()){
			Object[] o = (Object[])qr.nextElement();
			NumberCode code = (NumberCode)o[0];
			NumberCodeData data = new NumberCodeData(code);
			list.add(data);
		}
		return list;
	}
	
	/**
	 * @desc	: Multi Select NumberCode Name 가져오기
	 * @author	: shkim
	 * @date	: 2020. 9. 15.
	 * @method	: getValueSplit
	 * @param   : codeType, reqVal, split
	 * @return  : String
	 */
	public String getValueSplit(String codeType, String reqVal, String split) throws Exception {
		String returnValue = "";
		
		if(reqVal == null) {
			return returnValue;
		}
		
		String[] splitValue = reqVal.split(split);
		for(String code : splitValue) {
			NumberCode nc = CodeHelper.manager.getNumberCode(codeType, code);
			if(nc != null) {
				if(!"".equals(returnValue)) {
					returnValue += ", ";
				}
				returnValue += nc.getName();
			}
		}
		
		return returnValue;
	}
	
	/**
	 * @desc	: 2Level 넘버코드 리스트 가져오기
	 * @author	: shkim
	 * @date	: 2020. 9. 17.
	 * @method	: getNumberCodeChildListQuery
	 * @param   : reqMap
	 * @return  : QuerySpec
	 */
	public List<NumberCodeData> getNumberCodeChildList(Map<String, Object> reqMap) throws Exception {
		List<NumberCodeData> list = new ArrayList<NumberCodeData>();
		
		QuerySpec qs = getNumberCodeChildListQuery(reqMap);
		
		if(qs != null) {
			QueryResult qr = PersistenceHelper.manager.find(qs);
			
			while(qr.hasMoreElements()) {
				Object[] o = (Object[])qr.nextElement();
				NumberCode code = (NumberCode)o[0];
				NumberCodeData data = new NumberCodeData(code);
				list.add(data);
			}
		}
		
		return list;
	}
	
	/**
	 * @desc	: 2Level 넘버코드 리스트 가져오기
	 * @author	: shkim
	 * @date	: 2020. 9. 17.
	 * @method	: getNumberCodeChildListQuery
	 * @param   : reqMap
	 * @return  : QuerySpec
	 */
	public QuerySpec getNumberCodeChildListQuery(Map<String, Object> reqMap) throws Exception {
		
		String code = StringUtil.checkNull((String) reqMap.get("code"));
		String name = StringUtil.checkNull((String) reqMap.get("name"));
		String engName = StringUtil.checkNull((String) reqMap.get("engName"));
		
		String codeType = StringUtil.checkNull((String) reqMap.get("codeType"));
		String parentCode = StringUtil.checkNull((String) reqMap.get("parentCode"));
		
		boolean disabledCheck = false;
		if(reqMap.get("disabled") != null) {
			disabledCheck = (boolean) reqMap.get("disabled");
		}
		
		QuerySpec qs = null;
		
		if(codeType.length() > 0) {
			
			qs = new QuerySpec();
			
			int idx = qs.addClassList(NumberCode.class, true);
			
			qs.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", codeType), new int[] { idx });
			
			if(!disabledCheck) {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(NumberCode.class, "disabled", SearchCondition.IS_FALSE), new int[] { idx });	
			}
			
			if(code.length() > 0) {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.CODE, SearchCondition.LIKE, "%" + code + "%", false), new int[] { idx });
			}
			
			if(name.length() > 0) {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.NAME, SearchCondition.LIKE, "%" + name + "%", false), new int[] { idx });
			}
			
			if(engName.length() > 0) {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.ENG_NAME, SearchCondition.LIKE, "%" + engName + "%", false), new int[] { idx });
			}
			
			if(parentCode.length() > 0) {
				
				NumberCode parent = getNumberCode(codeType, parentCode);
				
				if(parent != null) {
					if (qs.getConditionCount() > 0) {
						qs.appendAnd();
					}
					qs.appendWhere(new SearchCondition(NumberCode.class, "parentReference.key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(parent)), new int[] { idx });
				}
			} else {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(NumberCode.class, "parentReference.key.id", SearchCondition.NOT_EQUAL, 0L), new int[] { idx });
			}
			
			qs.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class, NumberCode.CODE), false), new int[] { idx });
			
			qs.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class, NumberCode.SORT), false), new int[] { idx });
		}
		
		return qs;
	}
	
	public List<NumberCodeData> getSGCodeList(Map<String, Object> reqMap) throws Exception {
		List<NumberCodeData> list = new ArrayList<NumberCodeData>();
		
		QuerySpec qs = getSGCodeListQuery(reqMap);
		
		if(qs != null) {
			QueryResult qr = PersistenceHelper.manager.find(qs);
			
			while(qr.hasMoreElements()) {
				Object[] o = (Object[])qr.nextElement();
				NumberCode code = (NumberCode)o[0];
				NumberCodeData data = new NumberCodeData(code);
				list.add(data);
			}
		}
		
		return list;
	}
	
	public QuerySpec getSGCodeListQuery(Map<String, Object> reqMap) throws Exception {
		
		String code = StringUtil.checkNull((String) reqMap.get("code"));
		String name = StringUtil.checkNull((String) reqMap.get("name"));
		String engName = StringUtil.checkNull((String) reqMap.get("engName"));
		
		String codeType = "SG";
		String parentCode = StringUtil.checkNull((String) reqMap.get("parentCode"));
		
		boolean disabledCheck = false;
		if(reqMap.get("disabledCheck") != null) {
			disabledCheck = (boolean) reqMap.get("disabledCheck");
		}
		
		QuerySpec qs = null;
		
		if(codeType.length() > 0) {
			
			qs = new QuerySpec();
			
			int idx = qs.addClassList(NumberCode.class, true);
			
			qs.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", codeType), new int[] { idx });
			
			if(disabledCheck) {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(NumberCode.class, "disabled", SearchCondition.IS_FALSE), new int[] { idx });	
			}
			
			if(code.length() > 0) {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.CODE, SearchCondition.LIKE, "%" + code + "%", false), new int[] { idx });
			}
			
			if(name.length() > 0) {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.NAME, SearchCondition.LIKE, "%" + name + "%", false), new int[] { idx });
			}
			
			if(engName.length() > 0) {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.ENG_NAME, SearchCondition.LIKE, "%" + engName + "%", false), new int[] { idx });
			}
			
			if(parentCode.length() > 0) {
				
				NumberCode parent = getNumberCode(codeType, parentCode);
				
				if(parent != null) {
					if (qs.getConditionCount() > 0) {
						qs.appendAnd();
					}
					qs.appendWhere(new SearchCondition(NumberCode.class, "parentReference.key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(parent)), new int[] { idx });
				} else {
					if("JELSTDPARTCODE".equals(codeType) || "UNITDIVISIONCODE".equals(codeType)) {
						codeType = "JELPROCESSTYPE";
						
						parent = getNumberCode(codeType, parentCode);
						
						if(parent != null) {
							if (qs.getConditionCount() > 0) {
								qs.appendAnd();
							}
							
							qs.appendWhere(new SearchCondition(NumberCode.class, "parentReference.key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(parent)), new int[] { idx });
						}
					}
				}
			} else {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(NumberCode.class, "parentReference.key.id", SearchCondition.EQUAL, 0L), new int[] { idx });
			}
			
			if("PROJECTROLE".equals(codeType) ) {
				qs.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class, NumberCode.NAME), false), new int[] { idx });
				qs.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class, NumberCode.SORT), false), new int[] { idx });
			}else {
				qs.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class, NumberCode.SORT), false), new int[] { idx });
				qs.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class, NumberCode.NAME), false), new int[] { idx });
			}
		}
		
		return qs;
	}
	
	public List<Map<String, Object>> getSgCodeTypeTree(Map<String, Object> reqMap) throws Exception {
		
		List<Map<String,Object>> list = new ArrayList<>();
		
		String codeType = StringUtil.checkNull((String) reqMap.get("codeType"));
		String roleCode = StringUtil.checkNull((String) reqMap.get("roleCode"));
		codeType = "SG";
		int endLevel = 0;
		if(reqMap.get("endLevel") != null) {
			endLevel = (int) reqMap.get("endLevel");
		}
		
		NumberCodeType ctype = NumberCodeType.toNumberCodeType(codeType);
		
		//1Level
		QuerySpec qs = new QuerySpec(NumberCode.class);
		
		qs.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", codeType), new int[] { 0 });
		
		if("JELSTDPARTCODE".equals(codeType) || "UNITDIVISIONCODE".equals(codeType)) {
			qs.appendOr();
			qs.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", "JELPROCESSTYPE"), new int[] { 0 });
		}
		
		if(roleCode.length() > 0){
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			qs.appendWhere(new SearchCondition(NumberCode.class, "code", "=", roleCode), new int[] { 0 });
		}
		//query.appendAnd();
		//query.appendWhere(new SearchCondition(NumberCode.class, "disabled", SearchCondition.IS_FALSE), new int[] { 0 });
		//query.appendAnd();
		//query.appendWhere(new SearchCondition(NumberCode.class,"parentReference.key.id",SearchCondition.EQUAL,(long)0),new int[] { 0 });
		
		qs.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class,NumberCode.CODE),false),new int[]{0});
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("code", "");
		map.put("name", ctype.getDisplay());
		map.put("oid", codeType);
		
		list.add(map);
		
		while(qr.hasMoreElements()){
			NumberCode code = (NumberCode) qr.nextElement();
			
			map = new HashMap<String, Object>();
			
			map.put("oid", CommonUtil.getOIDString(code));
			map.put("codeType", codeType);
			map.put("name", code.getName());
			map.put("code", code.getCode());
			if(code.getParent() != null) {
				map.put("parentOid", CommonUtil.getOIDString(code.getParent()));
			} else {
				map.put("parentOid", codeType);
			}
			int level = getLevel(code, 1);
			if(level < endLevel){
				list.add(map);
			}
			
		}
		
		return list;
	}
	
	public List<NumberCodeData> getSgCodeChildList(Map<String, Object> reqMap) throws Exception {
		List<NumberCodeData> list = new ArrayList<NumberCodeData>();
		
		QuerySpec qs = getSgCodeChildListQuery(reqMap);
		
		if(qs != null) {
			QueryResult qr = PersistenceHelper.manager.find(qs);
			
			while(qr.hasMoreElements()) {
				Object[] o = (Object[])qr.nextElement();
				NumberCode code = (NumberCode)o[0];
				NumberCodeData data = new NumberCodeData(code);
				list.add(data);
			}
		}
		
		return list;
	}
	
	public QuerySpec getSgCodeChildListQuery(Map<String, Object> reqMap) throws Exception {
		
		String code = StringUtil.checkNull((String) reqMap.get("code"));
		String name = StringUtil.checkNull((String) reqMap.get("name"));
		String engName = StringUtil.checkNull((String) reqMap.get("engName"));
		
		String codeType = StringUtil.checkNull((String) reqMap.get("codeType"));
		String parentCode = StringUtil.checkNull((String) reqMap.get("parentCode"));
		
		boolean disabledCheck = false;
		if(reqMap.get("disabled") != null) {
			disabledCheck = (boolean) reqMap.get("disabled");
		}
		
		QuerySpec qs = null;
		
		if(codeType.length() > 0) {
			
			qs = new QuerySpec();
			
			int idx = qs.addClassList(NumberCode.class, true);
			
			qs.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", codeType), new int[] { idx });
			
			if(!disabledCheck) {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(NumberCode.class, "disabled", SearchCondition.IS_FALSE), new int[] { idx });	
			}
			
			if(code.length() > 0) {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.CODE, SearchCondition.LIKE, "%" + code + "%", false), new int[] { idx });
			}
			
			if(name.length() > 0) {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.NAME, SearchCondition.LIKE, "%" + name + "%", false), new int[] { idx });
			}
			
			if(engName.length() > 0) {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.ENG_NAME, SearchCondition.LIKE, "%" + engName + "%", false), new int[] { idx });
			}
			
			if(parentCode.length() > 0) {
				
				NumberCode parent = getNumberCode(codeType, parentCode);
				
				if(parent != null) {
					if (qs.getConditionCount() > 0) {
						qs.appendAnd();
					}
					qs.appendWhere(new SearchCondition(NumberCode.class, "parentReference.key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(parent)), new int[] { idx });
				}
			} else {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(NumberCode.class, "parentReference.key.id", SearchCondition.NOT_EQUAL, 0L), new int[] { idx });
			}
			qs.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class, NumberCode.SORT), false), new int[] { idx });
		}
		
		return qs;
	}
	
	/**
	 * @desc	: number code List
	 * @author	: mnyu
	 * @date	: 2020. 2. 21.
	 * @method	: getNumberCodeList
	 * @return	: List<NumberCodeData>
	 * @param reqMap
	 * @return
	 */
	public List<NumberCodeData> getNumberCodeList(String codeType, boolean searchDisabled) throws Exception{
		List<NumberCodeData> list = new ArrayList<>();
		QuerySpec qs = new QuerySpec();
		int idx = qs.appendClassList(NumberCode.class, true);
		
		qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.CODE_TYPE, SearchCondition.EQUAL, codeType), new int[]{idx});
		
		if(!searchDisabled) {
			if(qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.DISABLED, SearchCondition.IS_FALSE), new int[]{idx});
		}
		
		qs.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class, "parentReference.key.id"),false),new int[]{0});
		
		qs.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class, "code"),false),new int[]{0});
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		while(qr.hasMoreElements()){
			Object[] o = (Object[])qr.nextElement();
			NumberCode code = (NumberCode)o[0];
			NumberCodeData data = new NumberCodeData(code);
			list.add(data);
		}
		return list;
	}
}
