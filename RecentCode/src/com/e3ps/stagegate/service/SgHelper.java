package com.e3ps.stagegate.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.json.JSONException;
import org.json.JSONObject;

import com.e3ps.change.EChangeRequest2;
import com.e3ps.change.EcrProjectLink;
import com.e3ps.change.beans.ECRData;
import com.e3ps.common.code.bean.NumberCodeData;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.content.service.CommonContentHelper;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.project.EProject;
import com.e3ps.stagegate.ProjectStageGateLink;
import com.e3ps.stagegate.SGObject;
import com.e3ps.stagegate.SGObjectMaster;
import com.e3ps.stagegate.SGObjectMasterLink;
import com.e3ps.stagegate.SGObjectValue;
import com.e3ps.stagegate.SGObjectValueLink;
import com.e3ps.stagegate.StageGate;
import com.e3ps.stagegate.StageGateMasterLink;
import com.e3ps.stagegate.bean.SGChartData;
import com.e3ps.stagegate.bean.SGObjectMasterData;
import com.e3ps.stagegate.bean.SGObjectValueData;
import com.e3ps.stagegate.util.StageGateUtil;

import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentRoleType;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.pds.StatementSpec;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.ServiceFactory;
import wt.session.SessionHelper;
import wt.util.WTException;

public class SgHelper {
	public static final SgService service = ServiceFactory.getService(SgService.class);
	public static final SgHelper manager = new SgHelper();
	
	public StageGate getStageGate(String code) throws WTException{
		StageGate sg = null;
    	QuerySpec qs = new QuerySpec();
    	try{
	    	int ii = qs.addClassList(StageGate.class,true);
	    	qs.appendWhere(new SearchCondition(StageGate.class,StageGate.CODE,"=",code),new int[]{ii});
    	}catch (Exception e) {
    		e.printStackTrace();
		}
    	QueryResult result = PersistenceHelper.manager.find((StatementSpec)qs);
    	if(result.hasMoreElements()) {
    		Object[] obj = (Object[]) result.nextElement();
    		sg = (StageGate) obj[0];
    	}
    	return sg;
    }
	
	public SGObjectMaster getSGMaster(String code) throws WTException{
		SGObjectMaster objMaster = null;
    	QuerySpec qs = new QuerySpec();
    	try{
	    	int ii = qs.addClassList(StageGate.class,false);
	    	int jj = qs.addClassList(SGObjectMaster.class, true);
	    	qs.appendWhere(new SearchCondition(StageGate.class,StageGate.CODE,"=",code),new int[]{ii});
	    	qs.appendAnd();
	    	qs.appendWhere(new SearchCondition(StageGate.class,"thePersistInfo.theObjectIdentifier.id",
	    			SGObjectMaster.class,"stageGateReference.key.id"),new int[]{ii,jj});
	    	qs.appendAnd();
	    	qs.appendWhere(new SearchCondition(SGObjectMaster.class,"lastVersion",SearchCondition.IS_TRUE),new int[]{jj});
    	}catch (Exception e) {
    		e.printStackTrace();
		}
    	QueryResult result = PersistenceHelper.manager.find((StatementSpec)qs);
    	if(result.hasMoreElements()) {
    		Object[] obj = (Object[]) result.nextElement();
    		objMaster = (SGObjectMaster) obj[0];
    	}
    	return objMaster;
    }
	
	public boolean checkStageGateList(String code) throws WTException{
		boolean resultV = false;
    	QuerySpec qs = new QuerySpec();
    	try{
	    	int ii = qs.addClassList(StageGate.class,true);
	    	qs.appendWhere(new SearchCondition(StageGate.class,StageGate.CODE,"=",code),new int[]{ii});
    	}catch (Exception e) {
    		e.printStackTrace();
		}
    	QueryResult result = PersistenceHelper.manager.find((StatementSpec)qs);
    	if(result.size() > 0) {
    		resultV = true;
    	}
    	return resultV;
    }
	
	private void genelateSGObject(SGObjectMaster objMaster) throws Exception {
		SGObject obj = null;
		Map<String, Object> map = new HashMap<>();
		map.put("codeType", "SG");
		map.put("parentCode", "GATE");
		List<NumberCodeData> list = CodeHelper.manager.getSgCodeChildList(map);
		for(NumberCodeData data : list){
			obj = SGObject.newSGObject();
			obj.setObjType(data.getCode());
			obj.setObjMaster(objMaster);
			PersistenceHelper.manager.save(obj);
			
			map = new HashMap<>();
			map.put("codeType", "SG");
			map.put("parentCode", data.getCode());
			List<NumberCodeData> childList = CodeHelper.manager.getSgCodeChildList(map);
			if(childList.size() > 0) {
				genelateSGObjectValue(obj, childList, "");
			}else {
				String jsonString = genelateJSON(obj, data);
				SGObjectValue objValue = SGObjectValue.newSGObjectValue();
				objValue.setObj(obj);
				objValue.setDivision(jsonString);
				PersistenceHelper.manager.save(objValue);
			}
			
		}
		genelateLight(objMaster);
	}
	
	private void genelateLight(SGObjectMaster objMaster) throws Exception  {
		SGObject obj = SGObject.newSGObject();
		obj.setObjType("LIGHT");
		obj.setObjMaster(objMaster);
		PersistenceHelper.manager.save(obj);
		
		SGObjectValue objValue = null;
		for(int i=1; i <= 6; i++) {
			JSONObject jObj = new JSONObject();
			jObj.put("code", String.valueOf(i));
			jObj.put("name", String.valueOf(i));
			jObj.put("seq", i);
			jObj.put("value0", "");
			jObj.put("value1", "");
			jObj.put("value2", "");
			jObj.put("value3", "");
			jObj.put("value4", "");
			jObj.put("value5", "");
			jObj.put("value6", "");
			jObj.put("value7", "");
			jObj.put("value8", "");
			jObj.put("value9", "");
			String jsonString = jObj.toJSONString();
			
			objValue = SGObjectValue.newSGObjectValue();
			objValue.setObj(obj);
			objValue.setDivision(jsonString);
			objValue.setSeq(i);
			objValue.setParentCode("");
			objValue.setIsSubTitle(false);
			PersistenceHelper.manager.save(objValue);
		}
		
	}
	
	private void genelateSGObjectValue(SGObject obj, List<NumberCodeData> list, String parentCode) throws Exception {
		SGObjectValue objValue = null;
		boolean check = StageGateUtil.isSubTitle(obj.getObjType());
		boolean isSubTitle = false;
		String jsonString = "";
		if(check && "".equals(parentCode)) {
			isSubTitle = true;
		}
		for(NumberCodeData data : list){
			if("DS".equals(obj.getObjType())) {
				jsonString = genelateDS(obj, data);
			}else {
				jsonString = genelateJSON(obj, data);
			}
			//parsingJSON(jsonString);
			objValue = SGObjectValue.newSGObjectValue();
			objValue.setObj(obj);
			objValue.setDivision(jsonString);
			objValue.setSeq(data.getSort());
			objValue.setParentCode(parentCode);
			objValue.setIsSubTitle(isSubTitle);
			PersistenceHelper.manager.save(objValue);
			Map<String, Object> map = new HashMap<>();
			map.put("codeType", "SG");
			map.put("parentCode", data.getCode());
			List<NumberCodeData> childList = CodeHelper.manager.getSgCodeChildList(map);
			genelateSGObjectValue(obj, childList, data.getCode());
		}
		
	}
	
	private String genelateDS(SGObject obj, NumberCodeData data) throws JSONException {
		JSONObject jObj = new JSONObject();
		jObj.put("code", data.getCode());
		jObj.put("name", data.getName());
		jObj.put("seq", data.getSort());
		jObj.put("value0", "");
		jObj.put("value1", "");
		jObj.put("value2", "");
		jObj.put("value3", "");
		jObj.put("value4", "");
		jObj.put("value5", "");
		jObj.put("value6", "");
		jObj.put("value7", "");
		jObj.put("value8", "");
		jObj.put("value9", "");
		String desc = data.getDescription();
		if(desc!=null && desc.trim().length()>0){
        	StringTokenizer tokens = new StringTokenizer(desc,",");
        	while(tokens.hasMoreElements()){
            	String i = tokens.nextToken();
            	int index = Integer.parseInt(i)-1;
            	String key = "value"+String.valueOf(index);
            	jObj.put(key, "signal_NA");
        	}
        }
		
		String jsonString = jObj.toJSONString();
		return jsonString;
		
	}
	
	private String genelateJSON(SGObject obj, NumberCodeData data) throws JSONException {
		JSONObject jObj = new JSONObject();
		jObj.put("code", data.getCode());
		jObj.put("name", data.getName());
		jObj.put("seq", data.getSort());
		jObj.put("value0", "");
		jObj.put("value1", "");
		jObj.put("value2", "");
		jObj.put("value3", "");
		jObj.put("value4", "");
		jObj.put("value5", "");
		jObj.put("value6", "");
		jObj.put("value7", "");
		jObj.put("value8", "");
		jObj.put("value9", "");
		String jsonString = jObj.toJSONString();
		return jsonString;
		
	}
	
	public SGObjectMaster createFoundation(EProject project, StageGate sg) throws Exception {
				
		SGObjectMaster objMaster = SGObjectMaster.newSGObjectMaster();
		objMaster.setCode(project.getCode());
		objMaster.setVersion(0);
		objMaster.setLastVersion(true);
		objMaster.setStageGate(sg);
		objMaster.setRemark("First history");
		objMaster.setOwner(SessionHelper.manager.getPrincipalReference());
		objMaster = (SGObjectMaster) PersistenceHelper.manager.save(objMaster);
		
		genelateSGObject(objMaster);
		
		return objMaster;
	}
	
	public void deleteObject(StageGate sg) throws WTException {
		QueryResult qr = null;
		QueryResult qr2 = null;
		QueryResult qr3 = null;
		
		qr = PersistenceHelper.manager.navigate(sg, "objMaster", StageGateMasterLink.class, false);
		
		while (qr.hasMoreElements()) {
			
			StageGateMasterLink plink = (StageGateMasterLink) qr.nextElement();
			SGObjectMaster objMaster = plink.getObjMaster();
			qr2 = PersistenceHelper.manager.navigate(objMaster, "obj", SGObjectMasterLink.class, false);
			while (qr2.hasMoreElements()) {
				SGObjectMasterLink link = (SGObjectMasterLink) qr2.nextElement();
				SGObject obj = link.getObj();
				qr3 = PersistenceHelper.manager.navigate(obj, "objValue", SGObjectValueLink.class, false);
				while (qr3.hasMoreElements()) {
					SGObjectValueLink valueLink = (SGObjectValueLink) qr3.nextElement();
					SGObjectValue objValue = valueLink.getObjValue();
					
					PersistenceHelper.manager.delete(valueLink);
					objValue = (SGObjectValue) PersistenceHelper.manager.refresh(objValue);
					objValue = (SGObjectValue) PersistenceHelper.manager.delete(objValue);
				}
				PersistenceHelper.manager.delete(link);
				obj = (SGObject) PersistenceHelper.manager.refresh(obj);
				obj = (SGObject) PersistenceHelper.manager.delete(obj);
			}
			PersistenceHelper.manager.delete(plink);
			objMaster = (SGObjectMaster) PersistenceHelper.manager.refresh(objMaster);
			objMaster = (SGObjectMaster) PersistenceHelper.manager.delete(objMaster);
		}
		
		qr = PersistenceHelper.manager.navigate(sg, "project", ProjectStageGateLink.class, false);
		while (qr.hasMoreElements()) {
			ProjectStageGateLink link = (ProjectStageGateLink) qr.nextElement();
			PersistenceHelper.manager.delete(link);
		}
	}
	
	public SGObject getObjBySg(SGObjectMaster objMaster, String objType) throws WTException {
		SGObject obj = null;
		long oid = CommonUtil.getOIDLongValue(objMaster);
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(SGObject.class,true);
		qs.appendWhere(new SearchCondition(SGObject.class,"objMasterReference.key.id",SearchCondition.EQUAL,oid),new int[]{ii});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(SGObject.class,SGObject.OBJ_TYPE,SearchCondition.EQUAL,objType),new int[]{ii});
		QueryResult result = PersistenceHelper.manager.find(qs);
		if(result.hasMoreElements()){
		    Object[] o = (Object[])result.nextElement();
		    obj = (SGObject)o[0];
		}
		return obj;
	}
	
	public SGObject getObjByType(SGObjectMaster objMaster, String objType) throws WTException {
		SGObject obj = null;
		long oid = CommonUtil.getOIDLongValue(objMaster);
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(SGObject.class,true);
		qs.appendWhere(new SearchCondition(SGObject.class,"objMasterReference.key.id",SearchCondition.EQUAL,oid),new int[]{ii});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(SGObject.class,SGObject.OBJ_TYPE,SearchCondition.EQUAL,objType),new int[]{ii});
		QueryResult result = PersistenceHelper.manager.find(qs);
		if(result.hasMoreElements()){
		    Object[] o = (Object[])result.nextElement();
		    obj = (SGObject)o[0];
		}
		return obj;
	}
	
	public List<SGObjectValueData> getValueList(SGObjectMaster objMaster, String objType) throws WTException, JSONException{
		List<SGObjectValueData> list = new ArrayList<>();
		SGObject obj = getObjByType(objMaster, objType);
		SGObjectValue objValue = null;
		
		long oid = CommonUtil.getOIDLongValue(obj);
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(SGObjectValue.class,true);
		qs.appendWhere(new SearchCondition(SGObjectValue.class,"objReference.key.id",SearchCondition.EQUAL,oid),new int[]{ii});
		qs.appendOrderBy(new OrderBy(new ClassAttribute(SGObjectValue.class,SGObjectValue.SEQ),false),new int[]{ii});
		QueryResult result = PersistenceHelper.manager.find(qs);
		while(result.hasMoreElements()){
		    Object[] o = (Object[])result.nextElement();
		    objValue = (SGObjectValue)o[0];
		    SGObjectValueData data = new SGObjectValueData(objValue);
		    list.add(data);
		}
    	return list;
    }
	
	public URL getImgURL(SGObject obj) throws WTException {
		URL imgURL = null;
		QueryResult qr = ContentHelper.service.getContentsByRole(obj, ContentRoleType.PRIMARY);
    	ApplicationData appData = null;
    	if (qr.hasMoreElements()) {
    		appData = (ApplicationData) qr.nextElement();
    		imgURL = ContentHelper.service.getDownloadURL(obj, appData);
    		
    	}
    	return imgURL;
	}
	
	public List<SGObjectValueData> getParentValueList(SGObjectMaster objMaster, String objType) throws WTException, JSONException{
		List<SGObjectValueData> list = new ArrayList<>();
		SGObject obj = getObjByType(objMaster, objType);
		SGObjectValue objValue = null;
		
		long oid = CommonUtil.getOIDLongValue(obj);
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(SGObjectValue.class,true);
		qs.appendWhere(new SearchCondition(SGObjectValue.class,"objReference.key.id",SearchCondition.EQUAL,oid),new int[]{ii});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(SGObjectValue.class,"isSubTitle",SearchCondition.IS_TRUE),new int[]{ii});
		qs.appendOrderBy(new OrderBy(new ClassAttribute(SGObjectValue.class,SGObjectValue.SEQ),false),new int[]{ii});
		QueryResult result = PersistenceHelper.manager.find(qs);
		while(result.hasMoreElements()){
		    Object[] o = (Object[])result.nextElement();
		    objValue = (SGObjectValue)o[0];
		    SGObjectValueData data = new SGObjectValueData(objValue);
		    list.add(data);
		}
    	return list;
    }
	
	public List<SGObjectValueData> getChildValueList(SGObjectMaster objMaster, String objType) throws WTException, JSONException{
		List<SGObjectValueData> parentList = getParentValueList(objMaster, objType);
		List<SGObjectValueData> list = new ArrayList<>();
		SGObject obj = getObjByType(objMaster, objType);
		SGObjectValue objValue = null;
		
		long oid = CommonUtil.getOIDLongValue(obj);
		
		for(SGObjectValueData data : parentList) {
			//list.add(data);
			String parentCode = data.getCode();
			QuerySpec qs = new QuerySpec();
			int ii = qs.addClassList(SGObjectValue.class,true);
			qs.appendWhere(new SearchCondition(SGObjectValue.class,"objReference.key.id",SearchCondition.EQUAL,oid),new int[]{ii});
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(SGObjectValue.class,"parentCode",SearchCondition.EQUAL,parentCode),new int[]{ii});
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(SGObjectValue.class,"isSubTitle",SearchCondition.IS_FALSE),new int[]{ii});
			qs.appendOrderBy(new OrderBy(new ClassAttribute(SGObjectValue.class,SGObjectValue.SEQ),false),new int[]{ii});
			QueryResult result = PersistenceHelper.manager.find(qs);
			while(result.hasMoreElements()){
			    Object[] o = (Object[])result.nextElement();
			    objValue = (SGObjectValue)o[0];
			    SGObjectValueData childData = new SGObjectValueData(objValue);
			    list.add(childData);
			}
		}
		
		
    	return list;
    }
	
	public List<ECRData> getECRList(EProject project) throws Exception{
		List<ECRData> ecrList = new ArrayList<ECRData>();
		QueryResult qr = PersistenceHelper.manager.navigate(project,"ecr",EcrProjectLink.class);
		if (qr.size() > 0) {
			while(qr.hasMoreElements()){
				EChangeRequest2 ecr = (EChangeRequest2)qr.nextElement();
				ECRData data = new ECRData(ecr);
				ecrList.add(data);
			}
		}
		return ecrList;
	}
	
	public List<SGChartData> getChartData(SGObjectMaster objMaster) throws WTException, JSONException{
		List<SGChartData> list = new ArrayList<>();
		String objType = "CSTOP";
		String chartCode = "CHART";
		SGObject obj = getObjByType(objMaster, objType);
		SGObjectValue objValue = null;
		
		long oid = CommonUtil.getOIDLongValue(obj);
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(SGObjectValue.class,true);
		qs.appendWhere(new SearchCondition(SGObjectValue.class,"objReference.key.id",SearchCondition.EQUAL,oid),new int[]{ii});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(SGObjectValue.class,SGObjectValue.DIVISION,SearchCondition.LIKE, "%" + chartCode + "%"),new int[]{ii});
		
		qs.appendOrderBy(new OrderBy(new ClassAttribute(SGObjectValue.class,SGObjectValue.SEQ),false),new int[]{ii});
		QueryResult result = PersistenceHelper.manager.find(qs);
		while(result.hasMoreElements()){
		    Object[] o = (Object[])result.nextElement();
		    objValue = (SGObjectValue)o[0];
		    SGChartData data = new SGChartData(objValue);
		    list.add(data);
		}
    	return list;
    }
	
	public SGObjectMaster createCopy(SGObjectMaster oldMaster, String remark) throws Exception {
		
		SGObjectMaster newMaster = SGObjectMaster.newSGObjectMaster();
		newMaster.setCode(oldMaster.getCode());
		newMaster.setVersion(oldMaster.getVersion()+1);
		newMaster.setLastVersion(true);
		newMaster.setStageGate(oldMaster.getStageGate());
		newMaster.setRemark(remark);
		newMaster.setOwner(SessionHelper.manager.getPrincipalReference());
		newMaster = (SGObjectMaster) PersistenceHelper.manager.save(newMaster);
		
		genelateCopy(oldMaster, newMaster);
		
		return newMaster;
	}
	
	private void genelateCopy(SGObjectMaster oldMaster, SGObjectMaster newMaster) throws Exception {
		
		QueryResult qr = null;
		QueryResult qr2 = null;
		
		qr = PersistenceHelper.manager.navigate(oldMaster, "obj", SGObjectMasterLink.class, false);
		while (qr.hasMoreElements()) {
			SGObjectMasterLink link = (SGObjectMasterLink) qr.nextElement();
			SGObject obj = link.getObj();
			
			SGObject newObj = SGObject.newSGObject();
			newObj.setObjType(obj.getObjType());
			newObj.setObjMaster(newMaster);
			newObj.setObjCode(obj.getObjCode());
			PersistenceHelper.manager.save(newObj);
			
			setAppData(obj, newObj);
			
			qr2 = PersistenceHelper.manager.navigate(obj, "objValue", SGObjectValueLink.class, false);
			while (qr2.hasMoreElements()) {
				SGObjectValueLink valueLink = (SGObjectValueLink) qr2.nextElement();
				SGObjectValue objValue = valueLink.getObjValue();
				
				SGObjectValue newObjValue = SGObjectValue.newSGObjectValue();
				newObjValue.setObj(newObj);
				newObjValue.setDivision(objValue.getDivision());
				newObjValue.setSeq(objValue.getSeq());
				newObjValue.setParentCode(objValue.getParentCode());
				newObjValue.setIsSubTitle(objValue.isIsSubTitle());
				PersistenceHelper.manager.save(newObjValue);
				
				setAppData(objValue, newObjValue);
			}
		}
	}
	
	private void setAppData(ContentHolder obj, ContentHolder newObj) throws Exception {
		ApplicationData primaryAppData = null;
		ApplicationData secondaryAppData = null;
		QueryResult qr = null;
		QueryResult qr2 = null;
		qr = ContentHelper.service.getContentsByRole(obj, ContentRoleType.PRIMARY);
    	if (qr.hasMoreElements()) {
    		primaryAppData = (ApplicationData) qr.nextElement();
    		CommonContentHelper.service.attach(newObj, primaryAppData, true);
    	}
    	
    	qr2 = ContentHelper.service.getContentsByRole(obj, ContentRoleType.SECONDARY);
    	while (qr2.hasMoreElements()) {
    		secondaryAppData = (ApplicationData) qr2.nextElement();
    		CommonContentHelper.service.attach(newObj, secondaryAppData, false);
    	}
		
	}
	
	public boolean isOverLap(SGObjectMaster objMaster, String remark) throws WTException{
		boolean result = false;
		StageGate sg = objMaster.getStageGate();
		long oid = CommonUtil.getOIDLongValue(sg);
    	QuerySpec qs = new QuerySpec();
    	try{
	    	int ii = qs.addClassList(StageGate.class,false);
	    	int jj = qs.addClassList(SGObjectMaster.class, true);
	    	qs.appendWhere(new SearchCondition(StageGate.class,"thePersistInfo.theObjectIdentifier.id","=",oid),new int[]{ii});
	    	qs.appendAnd();
	    	qs.appendWhere(new SearchCondition(StageGate.class,"thePersistInfo.theObjectIdentifier.id",
	    			SGObjectMaster.class,"stageGateReference.key.id"),new int[]{ii,jj});
	    	qs.appendAnd();
	    	qs.appendWhere(new SearchCondition(SGObjectMaster.class,SGObjectMaster.REMARK,SearchCondition.EQUAL,remark),new int[]{jj});
    	}catch (Exception e) {
    		e.printStackTrace();
		}
    	QueryResult qr = PersistenceHelper.manager.find((StatementSpec)qs);
    	if(qr.hasMoreElements()) {
    		result = true;
    	}
    	
    	return result;
    }
	
	public List<SGObjectMasterData> getMasterList(StageGate sg) throws WTException{
		List<SGObjectMasterData> list = new ArrayList<SGObjectMasterData>();
		SGObjectMaster objMaster = null;
		long oid = CommonUtil.getOIDLongValue(sg);
    	QuerySpec qs = new QuerySpec();
    	try{
	    	int ii = qs.addClassList(StageGate.class,false);
	    	int jj = qs.addClassList(SGObjectMaster.class, true);
	    	qs.appendWhere(new SearchCondition(StageGate.class,"thePersistInfo.theObjectIdentifier.id","=",oid),new int[]{ii});
	    	qs.appendAnd();
	    	qs.appendWhere(new SearchCondition(StageGate.class,"thePersistInfo.theObjectIdentifier.id",
	    			SGObjectMaster.class,"stageGateReference.key.id"),new int[]{ii,jj});
	    	qs.appendOrderBy(new OrderBy(new ClassAttribute(SGObjectMaster.class,SGObjectMaster.VERSION),true),new int[]{jj});
    	}catch (Exception e) {
    		e.printStackTrace();
		}
    	QueryResult qr = PersistenceHelper.manager.find((StatementSpec)qs);
    	while(qr.hasMoreElements()) {
    		Object[] obj = (Object[]) qr.nextElement();
    		objMaster = (SGObjectMaster) obj[0];
    		SGObjectMasterData data = new SGObjectMasterData(objMaster);
    		list.add(data);
    	}
    	
    	return list;
    }

	public List<SGObjectValue> getSGObjectValueList(SGObjectMaster objMaster, String objType) throws WTException, JSONException{
		List<SGObjectValue> list = new ArrayList<>();
		SGObject obj = getObjByType(objMaster, objType);
		
		long oid = CommonUtil.getOIDLongValue(obj);
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(SGObjectValue.class,true);
		qs.appendWhere(new SearchCondition(SGObjectValue.class,"objReference.key.id",SearchCondition.EQUAL,oid),new int[]{ii});
		qs.appendOrderBy(new OrderBy(new ClassAttribute(SGObjectValue.class,SGObjectValue.SEQ),false),new int[]{ii});
		QueryResult result = PersistenceHelper.manager.find(qs);
		while(result.hasMoreElements()){
		    Object[] o = (Object[])result.nextElement();
		    SGObjectValue objValue = (SGObjectValue)o[0];
		    list.add(objValue);
		}
    	return list;
    }
}
