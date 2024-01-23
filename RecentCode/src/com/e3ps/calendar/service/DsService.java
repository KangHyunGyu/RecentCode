package com.e3ps.calendar.service;

import java.util.Map;

import com.e3ps.calendar.DevelopmentStageCalendar;

import wt.method.RemoteInterface;

@RemoteInterface
public interface DsService {
	public abstract DevelopmentStageCalendar createDsAction(Map<String, Object> reqMap) throws Exception;
	
	public abstract DevelopmentStageCalendar modifyDsAction(Map<String, Object> reqMap) throws Exception;
	
	public abstract void deleteDsAction(Map<String, Object> reqMap) throws Exception;
	
	public abstract void createLinkAction(Map<String, Object> reqMap) throws Exception;
	
	public abstract void modifyLinkAction(Map<String, Object> reqMap) throws Exception;
	
	public abstract void deleteLinkAction(Map<String, Object> reqMap) throws Exception;

}
