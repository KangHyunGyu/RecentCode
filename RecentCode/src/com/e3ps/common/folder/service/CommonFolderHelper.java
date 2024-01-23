package com.e3ps.common.folder.service;

import com.e3ps.common.log4j.Log4jPackages;

import wt.services.ServiceFactory;

public class CommonFolderHelper {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	public static final CommonFolderService service = ServiceFactory.getService(CommonFolderService.class);
}

