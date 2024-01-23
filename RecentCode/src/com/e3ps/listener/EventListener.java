package com.e3ps.listener;

import com.e3ps.common.folder.service.CommonFolderHelper;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.distribute.service.DistributeHelper;
//import com.e3ps.org.beans.PeopleHelper;
import com.e3ps.org.service.PeopleHelper;

import wt.events.KeyedEvent;
import wt.services.ServiceEventListenerAdapter;

public class EventListener extends ServiceEventListenerAdapter {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.LISTENER.getName());
	
	private static String LAF_HOME = "";

	public EventListener(String s) {
		super(s);
	}

	public void notifyVetoableEvent(Object obj) throws Exception {
		KeyedEvent keyedevent = (KeyedEvent) obj;
		Object eventObj = keyedevent.getEventTarget();
		String eventType = keyedevent.getEventType();
		//LOGGER.info("keyedevent : " +obj +","+ eventObj +","+eventType);
		/*
		 * if (obj instanceof EChangeActivity){ //LOGGER.info(
		 * "[EventListener][keyedevent]"+keyedevent.getEventType()); }
		 */
	    
		//Last 버전 iba 변경
		//EventVersionManager.manager.eventListener(eventObj, keyedevent.getEventType());
		
		DistributeHelper.service.eventListener(eventObj, keyedevent.getEventType());
		
		
		CommonFolderHelper.service.eventListener(eventObj, eventType);
		//워크플로 상태변경 로봇 이벤트 실행시 STATE_CHANGE
		//WFItemHelper.service.eventListener(eventObj, keyedevent.getEventType());
		
		//설계변경 
		//EventEOHelper.service.eventListener(eventObj, keyedevent.getEventType());
		
		//일괄결재 상태 변경
		//EventAsmHelper.service.eventListener(eventObj, keyedevent.getEventType());
		
		
		// 유저 생성시 People 싱크
		PeopleHelper.service.eventListener(eventObj, keyedevent.getEventType());
		
		
		
	}
}