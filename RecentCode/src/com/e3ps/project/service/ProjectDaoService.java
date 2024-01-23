package com.e3ps.project.service;

import java.util.ArrayList;

import wt.method.RemoteInterface;

@RemoteInterface
public interface ProjectDaoService {

	ArrayList getStructure(long oid) throws Exception;

	ArrayList getStructure(long oid, int depth) throws Exception;

	ArrayList getStructure(long oid, int depth, String[] fields) throws Exception;

	ArrayList getStepTree(long oid, String typeId, long parentId) throws Exception;

}
