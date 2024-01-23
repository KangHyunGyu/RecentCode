package com.e3ps.groupware.service;

import com.e3ps.common.jdf.log.Logger;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.groupware.workprocess.WorkProcessForm;

import wt.services.StandardManager;
import wt.util.WTException;

public class StandardWPFormService extends StandardManager implements WPFormService {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.GROUPWARE.getName());
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default factory for the class.
	 * @return
	 * @throws WTException
	 */
	public static StandardWPFormService newStandardWPFormService() throws WTException {
		StandardWPFormService instance = new StandardWPFormService();
		instance.initialize();
		return instance;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WPFormService#sendMail(com.e3ps.groupware.workprocess.WorkProcessForm)
	 */
	@Override
	public void sendMail(WorkProcessForm form) {
		Logger.info.println("메일 발송 구현 바람");
	}
}
