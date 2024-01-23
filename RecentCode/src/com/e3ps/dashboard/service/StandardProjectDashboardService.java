package com.e3ps.dashboard.service;

import java.io.Serializable;

import wt.method.RemoteAccess;
import wt.services.StandardManager;
import wt.util.WTException;

public class StandardProjectDashboardService extends StandardManager implements RemoteAccess,  Serializable, ProjectDashboardService {

	private static final long serialVersionUID = 1L;

	public static StandardProjectDashboardService newStandardProjectDashboardService() throws WTException {
		StandardProjectDashboardService instance = new StandardProjectDashboardService();
		instance.initialize();
		return instance;
	}

	
}
