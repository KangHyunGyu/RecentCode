package com.e3ps.project.service;

import java.util.ArrayList;

import wt.org.WTUser;

import com.e3ps.project.EProjectNode;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.ScheduleNode;

public interface ProjectMemberService {
	public ArrayList<Object> getUserList(EProjectNode project) throws Exception;

	public boolean isOwner(ScheduleNode task, WTUser user) throws Exception;

	public ArrayList<Object> getOwner(ETaskNode task) throws Exception;

	public Object[] getPM(ScheduleNode node) throws Exception;

	public ArrayList<Object> getProjectUserList(EProjectNode project) throws Exception;
}
