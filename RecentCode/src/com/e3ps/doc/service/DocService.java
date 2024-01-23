package com.e3ps.doc.service;

import java.util.Map;

import com.e3ps.doc.E3PSDocument;

import wt.doc.WTDocument;
import wt.method.RemoteInterface;

@RemoteInterface
public interface DocService {

	public abstract E3PSDocument createDocAction(Map<String, Object> reqMap) throws Exception;

	public abstract E3PSDocument modifyDocAction(Map<String, Object> reqMap) throws Exception;

	public abstract void deleteDocAction(Map<String, Object> reqMap) throws Exception;

	public abstract void withdrawnDocAction(Map<String, Object> reqMap) throws Exception;

	public abstract E3PSDocument reviseDocAction(Map<String, Object> reqMap) throws Exception;
	
	WTDocument getLastDocument(String number);
	
	public abstract E3PSDocument createDocAction2(Map<String, Object> reqMap) throws Exception;

	boolean createDocFolderAction(Map<String, Object> reqMap) throws Exception;
	
	boolean modifyDocFolderAction(Map<String, Object> reqMap) throws Exception;

}
