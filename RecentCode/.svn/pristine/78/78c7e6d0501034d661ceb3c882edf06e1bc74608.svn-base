package com.e3ps.org.service;

import java.util.Enumeration;

import com.e3ps.common.log4j.Log4jPackages;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.org.WTUser;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.ServiceFactory;
import wt.session.SessionHelper;
import wt.util.WTException;

public class UserHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.ORG.getName());
	public static final UserService service = ServiceFactory.getService(UserService.class);
	
	/**
	* 접속한 계정이 Admin 그룹에 포함 되어 있는지를 알아낸다 <br>
	*/
	public static boolean isAdmin() throws Exception {
		return isMember ( "Administrators" );
	}

	/**
	* 접속한 계정이 Parameter로 넘어온 group 명의 그룹에 포함 되어 있는지를 알아낸다 <br>
	*/
	public static boolean isMember(String group) throws Exception {
		WTUser user = (wt.org.WTUser) SessionHelper.manager.getPrincipal ();
		Enumeration en = user.parentGroupNames ();
		while (en.hasMoreElements ()) {
			String st = (String) en.nextElement ();
			if (st.equals ( group )) return true;
		}
		return false;
	}
	
	public static WTUser getWTUser(String name) {
		try {
			QuerySpec spec = new QuerySpec();
			int userPos = spec.addClassList(WTUser.class, true);
			spec.appendWhere(new SearchCondition(WTUser.class, WTUser.NAME,
					"=", name), new int[] { userPos });
			QueryResult qr = PersistenceHelper.manager.find(spec);
			if (qr.hasMoreElements()) {
				Object[] obj = (Object[]) qr.nextElement();
				return (WTUser) obj[0];
			}
		} catch (QueryException e) {
			e.printStackTrace();
		} catch (WTException e) {
			e.printStackTrace();
		}
		return null;
	}
}
