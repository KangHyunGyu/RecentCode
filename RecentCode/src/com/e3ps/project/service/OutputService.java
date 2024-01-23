package com.e3ps.project.service;

import java.util.ArrayList;
import java.util.Map;

import wt.fc.QueryResult;
import wt.method.RemoteInterface;

import com.e3ps.project.OutputType;
import com.e3ps.project.OutputTypeStep;

@RemoteInterface
public interface OutputService {
	public String saveOutput(Map<String, Object> hash) throws Exception;

	public String updateOutput(Map<String, Object> hash) throws Exception;

	public String deleteOutput(Map<String, Object> hash) throws Exception;

	public String linkOutputDocument(Map<String, Object> hash) throws Exception;

	public String deleteOutputDocumentLink(Map<String, Object> hash) throws Exception;

	public ArrayList<OutputType> getOutputType() throws Exception;

	public QueryResult getCodeList(String key);

	public QueryResult getCodeList(String key, OutputTypeStep parent);
}