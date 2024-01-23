package com.e3ps.groupware.service;

import java.util.Date;

import com.e3ps.common.log4j.Log4jPackages;

import wt.services.ServiceFactory;

public class WFItemHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.GROUPWARE.getName());
	/** singleton service instance */
	public static final WFItemService service = ServiceFactory.getService(WFItemService.class);
	
	/**
	 * 
	 * @return
	 */
	public static String makeNo() {
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyMM");
		String seq = "PRC-" + format.format(new Date());
		return seq + "-" + com.e3ps.common.util.ManageSequence.getSeqNo(seq, "0000");
	}
}
