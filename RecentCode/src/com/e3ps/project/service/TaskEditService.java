package com.e3ps.project.service;

import java.util.ArrayList;
import java.util.Hashtable;

import org.jdom2.Element;

import com.e3ps.project.ETaskNode;
import com.e3ps.project.ScheduleNode;

import wt.util.WTException;

public interface TaskEditService {

	public String rightClickMenu() throws Exception;

//	public String loadEditTaskGridXml(String oid) throws Exception;

	public String createEditTaskGridXml(String oid) throws Exception;

	// private String imgUrl(ProjectNodeData data);
	public StringBuffer getTree(String oid) throws Exception;

	public StringBuffer getTreeXml(String oid, Element rootrow)
			throws Exception;

	public ArrayList<ETaskNode> editCell(Hashtable hash) throws Exception;

	public ArrayList<ETaskNode> getPlan(ArrayList<String> list) throws Exception;

	ArrayList<ETaskNode> getPlan(String oid) throws Exception;

	StringBuffer getTreeJsp(String projectOid) throws Exception;

	boolean isPrePostLink(ETaskNode preTask, ETaskNode postTask);

	ScheduleNode isPreTaskNode(String postOid, ScheduleNode preNode) throws WTException;

}
