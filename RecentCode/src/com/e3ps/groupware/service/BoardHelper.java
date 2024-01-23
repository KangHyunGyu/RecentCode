package com.e3ps.groupware.service;

import com.e3ps.common.log4j.Log4jPackages;

import wt.services.ServiceFactory;

public class BoardHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.GROUPWARE.getName());
	/** singleton service instance */
	public static final BoardService service = ServiceFactory.getService(BoardService.class);
	
	
}
