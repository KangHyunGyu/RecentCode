/**
 * 
 */
package com.e3ps.statistics.service;


import com.e3ps.common.log4j.Log4jPackages;

import wt.services.StandardManager;
import wt.util.WTException;

@SuppressWarnings("serial")
public class StandardStatisticsService extends StandardManager implements StatisticsService{
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.STATISTICS.getName());
	
	public static StandardStatisticsService newStandardStatisticsService() throws WTException {
		final StandardStatisticsService instance = new StandardStatisticsService();
		instance.initialize();
		return instance;
	}
}
