package com.e3ps.part.editor.service;

import java.util.List;
import java.util.Map;

import com.e3ps.part.editor.bean.BomEditorTreeData;

import wt.method.RemoteInterface;
import wt.part.WTPart;

@RemoteInterface
public interface BomEditorService {

	public abstract WTPart saveChildrenBomAction(Map<String, Object> reqMap) throws Exception;

	public abstract WTPart saveAddedPartAction(Map<String, Object> reqMap) throws Exception;

	public abstract WTPart deleteBomPartAction(Map<String, Object> reqMap) throws Exception;

	public abstract WTPart modifyBomPartAction(Map<String, Object> reqMap) throws Exception;

	public abstract List<WTPart> saveAddedPartListAction(Map<String, Object> reqMap) throws Exception;

}
