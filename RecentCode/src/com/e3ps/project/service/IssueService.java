package com.e3ps.project.service;

import java.util.HashMap;
import java.util.Map;

import jxl.write.WritableWorkbook;

public interface IssueService {

	public HashMap<String, Object> saveIssue(Map<String, Object> hash) throws Exception;

	public HashMap<String, Object> updateIssue(Map<String, Object> hash) throws Exception;

	public String deleteIssue(Map<String, Object> hash) throws Exception;

	public HashMap<String, Object> createSolution(Map<String, Object> hash) throws Exception;

	public HashMap<String, Object> updateSolution(Map<String, Object> hash) throws Exception;

	public String deleteSolution(Map<String, Object> hash) throws Exception;

	public String issueComplete(Map<String, Object> hash) throws Exception;

	public String cancelComplete(Map<String, Object> hash) throws Exception;
	
	public WritableWorkbook downloadMyIssueListExcel(Map<String, Object> hash, WritableWorkbook workbook);
	
	public WritableWorkbook downloadSearchIssueExcelDown(Map<String, Object> hash, WritableWorkbook workbook);
}
