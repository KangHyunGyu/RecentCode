package com.e3ps.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.project.EOutput;
import com.e3ps.project.ETask;
import com.e3ps.project.OutputType;
import com.e3ps.project.OutputTypeStep;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.ServiceFactory;
import wt.util.WTException;

public class OutputHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PROJECT.getName());
	public static final OutputService service = ServiceFactory.getService(OutputService.class);
	public static final OutputHelper manager = new OutputHelper();
	
	/**
     * @param task
     * @return List<EOutput>
     * @throws WTException
     * @메소드명 : getOutput
     * @작성자 : tsjeong
     * @작성일 : 2020. 12. 04
     * @설명 : 산출물 리스트
     */
    public List<EOutput> getOutput(ETask task) throws WTException{
	List<EOutput> list = new ArrayList<>();
	QuerySpec spec = new QuerySpec();
	int idx = spec.appendClassList(EOutput.class, true);
	spec.appendWhere(
	        new SearchCondition(EOutput.class, "taskReference.key.id", "=", CommonUtil.getOIDLongValue(task)),
	        new int[] {idx});
	QueryResult qr = PersistenceHelper.manager.find(spec);
	EOutput output = null;
	if (qr != null && qr.hasMoreElements()) {
	    while (qr.hasMoreElements()) {
		Object[] objArr = (Object[]) qr.nextElement();
		output = (EOutput) objArr[0];
		list.add(output);
	    }
	}
	return list;
    }
	
	public List<Map<String, Object>> getOutputType() throws Exception {
		OutputType[] output = OutputType.getOutputTypeSet();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < output.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("key", output[i].toString());
			map.put("value", output[i].getDisplay(Locale.KOREA));
			list.add(map);
		}
		return list;
	}
	
	public List<Map<String, Object>> getOutputStep(String key, String parentOid) throws Exception {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		QuerySpec query = getOutputStepQuery(key, parentOid);
		
		QueryResult qr =  PersistenceHelper.manager.find(query);
		
		while(qr.hasMoreElements()) {
			OutputTypeStep step = (OutputTypeStep) qr.nextElement();
			
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("key", CommonUtil.getOIDString(step));
			map.put("value", step.getName());
			
			list.add(map);
		}
		
		return list;
	}
	
	public QuerySpec getOutputStepQuery(String key, String parentOid) throws WTException {
		
		QuerySpec query = new QuerySpec(OutputTypeStep.class);
		query.appendWhere(new SearchCondition(OutputTypeStep.class,
				"outputType", "=", OutputType.toOutputType(key)),
				new int[] { 0 });

		query.appendAnd();
		if (parentOid == null || "".equals(parentOid)) {
			query.appendWhere(new SearchCondition(OutputTypeStep.class, "parentReference.key.classname", true),
					new int[] { 0 });
		} else {
			query.appendWhere(new SearchCondition(OutputTypeStep.class, "parentReference.key.id", "=", CommonUtil.getOIDLongValue(parentOid)),
					new int[] { 0 });
		}

		query.appendOrderBy(new OrderBy(new ClassAttribute(OutputTypeStep.class, OutputTypeStep.CODE), false),
				new int[] { 0 });

		
		return query;
	}
}
