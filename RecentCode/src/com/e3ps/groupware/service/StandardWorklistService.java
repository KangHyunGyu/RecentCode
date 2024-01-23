package com.e3ps.groupware.service;

import com.e3ps.common.log4j.Log4jPackages;

import wt.services.StandardManager;
import wt.util.WTException;

public class StandardWorklistService extends StandardManager implements WorklistService {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.GROUPWARE.getName());
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default factory for the class.
	 * @return
	 * @throws WTException
	 */
	public static StandardWorklistService newStandardWorklistService() throws WTException {
		StandardWorklistService instance = new StandardWorklistService();
		instance.initialize();
		return instance;
	}

	
}
