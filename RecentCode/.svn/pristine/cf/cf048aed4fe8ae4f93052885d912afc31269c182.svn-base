package com.e3ps.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.project.EOutput;
import com.e3ps.project.ETask;
import com.e3ps.project.OutputTypeStep;
import com.e3ps.project.beans.OutputTypeStepData;
import com.e3ps.project.beans.OutputTypeStepTreeData;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.ServiceFactory;
import wt.util.WTException;

public class OutputTypeHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PROJECT.getName());
	public static final OutputTypeService service = ServiceFactory.getService(OutputTypeService.class);
	public static final OutputTypeHelper manager = new OutputTypeHelper();
	
	public List<OutputTypeStepData> getPSOCodeList(Map<String, Object> reqMap) throws Exception {
		List<OutputTypeStepData> list = new ArrayList<OutputTypeStepData>();
		
		QuerySpec qs = getPSOCodeListQuery(reqMap);
		
		if(qs != null) {
			QueryResult qr = PersistenceHelper.manager.find(qs);
			
			while(qr.hasMoreElements()) {
				Object[] o = (Object[])qr.nextElement();
				OutputTypeStep code = (OutputTypeStep)o[0];
				OutputTypeStepData data = new OutputTypeStepData(code);
				list.add(data);
			}
		}
		
		return list;
	}
	
	public QuerySpec getPSOCodeListQuery(Map<String, Object> reqMap) throws Exception {
		
		String code = StringUtil.checkNull((String) reqMap.get("code"));
		String name = StringUtil.checkNull((String) reqMap.get("name"));
		
		String outputType = StringUtil.checkNull((String) reqMap.get("codeType"));
		String parentCode = StringUtil.checkNull((String) reqMap.get("parentCode"));
		
		QuerySpec qs = null;
		
		if(outputType.length() > 0) {
			
			qs = new QuerySpec();
			
			int idx = qs.addClassList(OutputTypeStep.class, true);
			
			qs.appendWhere(new SearchCondition(OutputTypeStep.class, "outputType", "=", outputType), new int[] { idx });
			
			if(code.length() > 0) {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(OutputTypeStep.class, OutputTypeStep.CODE, SearchCondition.LIKE, "%" + code + "%", false), new int[] { idx });
			}
			
			if(name.length() > 0) {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(OutputTypeStep.class, OutputTypeStep.NAME, SearchCondition.LIKE, "%" + name + "%", false), new int[] { idx });
			}
			
			if(parentCode.length() > 0) {
				
				OutputTypeStep parent = getOutputTypeStep(outputType, parentCode);
				
				if(parent != null) {
					if (qs.getConditionCount() > 0) {
						qs.appendAnd();
					}
					qs.appendWhere(new SearchCondition(OutputTypeStep.class, "parentReference.key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(parent)), new int[] { idx });
				}
			} else {
				if (qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(OutputTypeStep.class, "parentReference.key.id", SearchCondition.EQUAL, 0L), new int[] { idx });
			}
			
			qs.appendOrderBy(new OrderBy(new ClassAttribute(OutputTypeStep.class, OutputTypeStep.SORT), false), new int[] { idx });
			
			qs.appendOrderBy(new OrderBy(new ClassAttribute(OutputTypeStep.class, OutputTypeStep.NAME), false), new int[] { idx });
		}
		
		return qs;
	}
	
	public List<Map<String, Object>> getPSOTree(Map<String, Object> reqMap) throws Exception {
		
		List<Map<String,Object>> list = new ArrayList<>();
		
		String codeType = StringUtil.checkNull((String) reqMap.get("codetype"));
		
		//1Level
		QuerySpec qs = new QuerySpec(OutputTypeStep.class);
		
		qs.appendWhere(new SearchCondition(OutputTypeStep.class, "outputType", "=", codeType), new int[] { 0 });
		
		
		qs.appendOrderBy(new OrderBy(new ClassAttribute(OutputTypeStep.class,OutputTypeStep.CODE),false),new int[]{0});
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("codeType", codeType);
		map.put("code", "");
		map.put("name", codeType);
		map.put("oid", codeType);
		
		list.add(map);
		
		while(qr.hasMoreElements()){
			OutputTypeStep code = (OutputTypeStep) qr.nextElement();
			
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
			list.add(map);
			
		}
		
		return list;
	}
	
	public OutputTypeStep getOutputTypeStep(String codeType, String code){
		try{
			QuerySpec query = new QuerySpec(OutputTypeStep.class);

			query.appendWhere(new SearchCondition(OutputTypeStep.class, "outputType", "=", codeType), new int[] { 0 });
			query.appendAnd();
			query.appendWhere(new SearchCondition(OutputTypeStep.class, "code", "=", code), new int[] { 0 });
			//query.appendAnd();
			//query.appendWhere(new SearchCondition(NumberCode.class, "disabled", SearchCondition.IS_FALSE), new int[] { 0 });

			query.appendOrderBy(new OrderBy(new ClassAttribute(OutputTypeStep.class,OutputTypeStep.CODE),false),new int[]{0});
			QueryResult qr = PersistenceHelper.manager.find(query);

			if(qr.hasMoreElements()){
				OutputTypeStep cc = (OutputTypeStep)qr.nextElement();
				return cc;
			}
			return  null;
		}
		catch(Exception ex){
			return null;
		}
	}
	
	public static List<OutputTypeStepData> getOutputTypeStepList(OutputTypeStep outputStep, String codeType) throws WTException {
		List<OutputTypeStepData> list = new ArrayList<OutputTypeStepData>();
			
		try {
			long longOid = 0;
			if(outputStep != null) {
				longOid = CommonUtil.getOIDLongValue(outputStep);
			}
				
			QuerySpec qs = new QuerySpec();
			int idx = qs.addClassList(OutputTypeStep.class, true);
			
			qs.appendWhere(new SearchCondition(OutputTypeStep.class, "outputType", "=", codeType), new int[] { 0 });
			
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			SearchCondition sc = new SearchCondition(OutputTypeStep.class, "parentReference.key.id",
					SearchCondition.EQUAL, longOid);
			qs.appendWhere(sc, new int[] { idx });
				
			qs.appendOrderBy(new OrderBy(new ClassAttribute(OutputTypeStep.class, OutputTypeStep.CODE), false),
					new int[] { idx });
				
			QueryResult qr = PersistenceHelper.manager.find(qs);
			while (qr.hasMoreElements()) {
				Object[] obj = (Object[]) qr.nextElement();
				OutputTypeStepData data = new OutputTypeStepData((OutputTypeStep) obj[0]);
				OutputTypeStep children = OutputTypeHelper.manager.getOutputTypeStep(data.getCodeType(), data.getCode());
					
				List<OutputTypeStepData> subList = getOutputTypeStepList(children, data.getCodeType());
					
				if(subList.size()>0) {
					data.setChildren(geOutputTypeStepList(children));
				}
					
				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return list;
	}
	
	public static List<OutputTypeStepData> geOutputTypeStepList(OutputTypeStep outputStep) throws WTException {
		List<OutputTypeStepData> list = new ArrayList<OutputTypeStepData>();
			
		try {
			long longOid = CommonUtil.getOIDLongValue(outputStep);
				
			QuerySpec qs = new QuerySpec();
			int idx = qs.addClassList(OutputTypeStep.class, true);
				
			SearchCondition sc = new SearchCondition(OutputTypeStep.class, "parentReference.key.id",
					SearchCondition.EQUAL, longOid);
			qs.appendWhere(sc, new int[] { idx });
				
			qs.appendOrderBy(new OrderBy(new ClassAttribute(OutputTypeStep.class, OutputTypeStep.CODE), false),
					new int[] { idx });
				
			QueryResult qr = PersistenceHelper.manager.find(qs);
			while (qr.hasMoreElements()) {
				Object[] obj = (Object[]) qr.nextElement();
				OutputTypeStepData data = new OutputTypeStepData((OutputTypeStep) obj[0]);
				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return list;
	}
	
	/**
	 * @desc	: Step 트리 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 10. 7.
	 * @method	: getStepTree
	 * @param	: reqMap
	 * @return	: List<OutputTypeStepTreeData>
	 */
	public List<OutputTypeStepTreeData> getStepTree(Map<String, Object> reqMap) throws Exception {
		
		List<OutputTypeStepTreeData> list = new ArrayList<>();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String outputType = StringUtil.checkNull((String) reqMap.get("outputType"));
		
		OutputTypeStepTreeData root = new OutputTypeStepTreeData(outputType, oid);
		
		root.setData(getStepChildren(root, oid));
		
		list.add(root);
		
		return list;
	}
	
	/**
	 * @desc	: Step 트리 하위 가져오기 재귀
	 * @author	: sangylee
	 * @date	: 2020. 10. 7.
	 * @method	: getStepChildren
	 * @param	: parent, projectOid
	 * @return	: List<OutputTypeStepTreeData>
	 */
	public List<OutputTypeStepTreeData> getStepChildren(OutputTypeStepTreeData parent, String projectOid) throws Exception {

		List<OutputTypeStepTreeData> list = new ArrayList<>();
		
		List<OutputTypeStep> childrenList = getStepChildrenQuery(parent.getOid());
		
		for(OutputTypeStep step : childrenList) {
			OutputTypeStepTreeData data = new OutputTypeStepTreeData(step);
			
			data.setData(getStepChildren(data, projectOid));
			data.getData().addAll(getStepTaskList(data, projectOid));
			
			list.add(data);
		}
		
		return list;
	}
	
	/**
	 * @desc	: Step 트리 하위 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 25.
	 * @method	: getStepChildrenQuery
	 * @param	: parentOid
	 * @return	: List<ETask>
	 */
	public List<OutputTypeStep> getStepChildrenQuery(String parentOid) throws Exception {
		
		List<OutputTypeStep> list = new ArrayList<>();
		
		QuerySpec query = new QuerySpec();
		
		int idx = query.addClassList(OutputTypeStep.class, true);
		
		long loid = 0L;
		if(parentOid != null && parentOid.length() > 0 && parentOid.indexOf(OutputTypeStep.class.getSimpleName()) > 0) {
			loid = CommonUtil.getOIDLongValue(parentOid);
		}
		
		query.appendWhere(new SearchCondition(OutputTypeStep.class, "parentReference.key.id", SearchCondition.EQUAL, loid), new int[]{idx});
		
		query.appendOrderBy(new OrderBy(new ClassAttribute(OutputTypeStep.class,"sort"), false), new int[] { idx });  
		query.appendOrderBy(new OrderBy(new ClassAttribute(OutputTypeStep.class,"thePersistInfo.theObjectIdentifier.id"), false), new int[] { idx });  
		
		QueryResult qr = PersistenceHelper.manager.find(query);
		
		while(qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			OutputTypeStep step = (OutputTypeStep) obj[0];
			
			list.add(step);
		}
		
		return list;
	}
	
	/**
	 * @desc	: Step Task 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 10. 8.
	 * @method	: getStepTaskList
	 * @param	: parent
	 * @return	: List<OutputTypeStepTreeData>
	 */
	public List<OutputTypeStepTreeData> getStepTaskList(OutputTypeStepTreeData stepData, String projectOid) throws Exception {

		List<OutputTypeStepTreeData> list = new ArrayList<>();
		
		List<Object[]> taskList = getStepTaskListQuery(stepData.getOid(), projectOid);
		
		for(Object[] obj : taskList) {
			ETask task = (ETask) obj[0];
			EOutput output = (EOutput) obj[1];
			OutputTypeStepTreeData data = new OutputTypeStepTreeData(task, output);
			
			list.add(data);
		}
		
		return list;
	}
	
	/**
	 * @desc	: Step Task 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 10. 8.
	 * @method	: getStepTaskListQuery
	 * @param	: stepOid, projectOid
	 * @return	: List<Object[]>
	 */
	public List<Object[]> getStepTaskListQuery(String stepOid, String projectOid) throws Exception {
		
		List<Object[]> list = new ArrayList<>();
		
		QuerySpec query = new QuerySpec();
		
		int idx = query.addClassList(ETask.class, true);
		int idx2 = query.addClassList(EOutput.class, true);
		int idx3 = query.addClassList(OutputTypeStep.class, false);
		
		long loid = CommonUtil.getOIDLongValue(stepOid);
		
		query.appendWhere(new SearchCondition(OutputTypeStep.class, OutputTypeStep.PERSIST_INFO + ".theObjectIdentifier.id", SearchCondition.EQUAL, loid), new int[]{idx3});
		
		query.appendAnd();
		query.appendWhere(new SearchCondition(OutputTypeStep.class, OutputTypeStep.PERSIST_INFO + ".theObjectIdentifier.id", EOutput.class, EOutput.STEP_REFERENCE + ".key.id"), new int[]{idx3, idx2});
		
		query.appendAnd();
		query.appendWhere(new SearchCondition(ETask.class, ETask.PERSIST_INFO + ".theObjectIdentifier.id", EOutput.class, EOutput.TASK_REFERENCE + ".key.id"), new int[]{idx, idx2});
		
		query.appendAnd();
		query.appendWhere(new SearchCondition(ETask.class, ETask.PROJECT_REFERENCE + ".key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(projectOid)), new int[]{idx});
		
		QueryResult qr = PersistenceHelper.manager.find(query);
		
		while(qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			
			list.add(obj);
		}
		
		return list;
	}
}
