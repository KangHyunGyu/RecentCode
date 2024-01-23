package com.e3ps.project.service;

import com.e3ps.project.ETaskNode;

import wt.method.RemoteInterface;

@RemoteInterface
public interface ProjectMailBrokerSerivce {

	void taskStart(ETaskNode task);

}
