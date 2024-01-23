package com.e3ps.groupware.service;

import com.e3ps.common.log4j.Log4jPackages;

import wt.services.ServiceFactory;

public class FavoriteHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.GROUPWARE.getName());
	/** singleton service instance */
	public static final FavoriteService service = ServiceFactory.getService(FavoriteService.class);
	
	
}
