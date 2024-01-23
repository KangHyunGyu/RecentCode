package com.e3ps.change.editor.service;

import java.util.ArrayList;

import com.e3ps.change.EOEul;
import com.e3ps.change.EulPartLink;
import com.e3ps.common.log4j.Log4jPackages;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.part.WTPart;
import wt.services.ServiceFactory;

public class EulPartHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.CHANGE.getName());
	public static final EulPartService service = ServiceFactory.getService(EulPartService.class);
	
	public static void removeAllEditPartsLink(EOEul eul) throws Exception {
		QueryResult qr = PersistenceHelper.manager.navigate(eul, "part",
				EulPartLink.class, false);
		while (qr.hasMoreElements()) {
			EulPartLink link = (EulPartLink) qr.nextElement();
			link.setDisabled(true);
			PersistenceHelper.manager.modify(link);
			// PersistenceHelper.manager.delete(link);
		}
	}

	public static void addEditPartsLink(EOEul eul, ArrayList co)
			throws Exception {
		for (int i = 0; i < co.size(); i++) {
			WTPart part = (WTPart) co.get(i);
			EulPartLink link = EulPartLink.newEulPartLink(part, eul);
			PersistenceHelper.manager.save(link);
		}
	}
}
