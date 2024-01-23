package com.e3ps.project.service;

import java.util.List;
import java.util.Map;

import com.e3ps.project.OutputTypeStep;

import wt.fc.QueryResult;
import wt.method.RemoteInterface;
import wt.util.WTException;

@RemoteInterface
public interface OutputTypeService {

	List<Map<String, Object>> getOutputType() throws Exception;

	QueryResult getCodeList(String key);

	QueryResult getCodeList(String key, OutputTypeStep parent);

	OutputTypeStep getOutputTypeStep(String outputType, String code);

	List<Map<String, Object>> getPSOTree(String codeType) throws Exception;

	boolean existsCode(String code) throws WTException;
	
	public abstract void savePSOCodeAction(Map<String, Object> reqMap) throws Exception;
}
