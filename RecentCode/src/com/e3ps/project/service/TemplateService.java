package com.e3ps.project.service;

import java.util.Map;

import com.e3ps.project.EProjectNode;
import com.e3ps.project.EProjectTemplate;
import com.e3ps.project.ETask;

import wt.method.RemoteInterface;

@RemoteInterface
public interface TemplateService {

	/**
	 * 템플릿 저장
	 * 
	 * @param hash
	 * @return
	 * @throws Exception
	 */
	public abstract EProjectTemplate save(Map<String, Object> hash) throws Exception;
	
	/**
	 * 템플릿 저장(업로드)
	 * 
	 * @param hash
	 * @return
	 * @throws Exception
	 */
	public abstract EProjectTemplate upload(Map<String, Object> hash) throws Exception;

	/**
	 * 템플릿 수정시
	 * 
	 * @param hash
	 * @return
	 * @throws Exception
	 */
	public abstract String update(Map<String, Object> hash) throws Exception;

	/**
	 * 템플릿등록시
	 * 
	 * @param hash
	 * @return
	 * @throws Exception
	 */
	public abstract ETask createTask(Map<String, Object> reqMap) throws Exception;

	/**
	 * 템플릿 수정시
	 * 
	 * @param hash
	 * @return
	 * @throws Exception
	 */
	public abstract ETask updateTask(Map<String, Object> hash) throws Exception;

	/**
	 * Role 수정 (TASK 담당자 수정)
	 * 
	 * @param hash
	 * @throws Exception
	 */
	public abstract String editRole(Map<String, Object> reqMap) throws Exception;

	/**
	 * 선후행 맺기
	 * 
	 * @param hash
	 * @throws Exception
	 */
	public abstract void setPreTask(Map<String, Object> reqMap) throws Exception;

	public abstract Map<String, Object> addChildTask(Map<String, Object> reqMap) throws Exception;

	public abstract Map<String, Object> addNextTask(Map<String, Object> reqMap) throws Exception;

	public abstract Map<String, Object> deleteTask(Map<String, Object> reqMap) throws Exception;

	/**
	 * 템플릿 삭제시
	 * 
	 * @param hash
	 * @return
	 * @throws Exception
	 */
	public abstract String delete(Map<String, Object> reqMap) throws Exception;

	public abstract Map<String, Object> moveUpTask(Map<String, Object> reqMap) throws Exception;

	public abstract Map<String, Object> moveDownTask(Map<String, Object> reqMap) throws Exception;

	public abstract EProjectTemplate copyTemplate(Map<String, Object> hash) throws Exception;

	public abstract void copyTemplate(EProjectNode orgTemp, EProjectNode newTemp) throws Exception;

}
