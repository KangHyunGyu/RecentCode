package com.e3ps.groupware.service;

import java.util.Hashtable;

import wt.fc.QueryResult;
import wt.method.RemoteInterface;

@RemoteInterface
public interface BoardService {
	
	QueryResult commentSeach(String oid);
	
	String create(Hashtable hash, String[] loc) throws Exception;
	
	String delete(String oid);
	
	String modify(Hashtable hash, String[] loc, String[] deloc) throws Exception;
	
	
}
