package com.e3ps.workspace.service;

import java.util.Map;

import com.e3ps.workspace.notice.Notice;

import wt.fc.Persistable;
import wt.method.RemoteInterface;

@RemoteInterface
public interface WorkspaceService {

	public abstract void createNoticeAction(Map<String, Object> reqMap) throws Exception;
	
	public abstract void changePasswordAction(Map<String, Object> reqMap) throws Exception;
	
	public abstract Notice modifyNoticeAction(Map<String, Object> reqMap) throws Exception;

	public abstract Notice modifyViewCntAction(Map<String, Object> reqMap) throws Exception;
	
	public abstract void deleteNoticeAction(Map<String, Object> reqMap) throws Exception;
	
	public void deleteWFItem(Persistable persistable);
}
