package com.e3ps.project.service;

import com.e3ps.common.log4j.Log4jPackages;

import wt.services.ServiceFactory;

public class ProjectMailBrokerHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PROJECT.getName());
	public static final ProjectMailBrokerSerivce service = ServiceFactory.getService(ProjectMailBrokerSerivce.class);
}
