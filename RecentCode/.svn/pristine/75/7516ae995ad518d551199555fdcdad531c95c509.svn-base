package com.e3ps.interfaces.util;

import java.sql.Connection;

import com.e3ps.common.db.DBConnectionManager;
import com.e3ps.common.jdf.config.ConfigEx;
import com.e3ps.common.jdf.config.ConfigExImpl;

public class InterfaceUtil {
	
	private static final ConfigExImpl conf = ConfigEx.getInstance("eSolution");

	public static Connection getCPCConnection() throws Exception {
		return DBConnectionManager.getInstance().getConnection("CPC");
	}

	
}
