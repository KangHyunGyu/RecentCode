package com.e3ps.project.util;

import java.util.Hashtable;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.project.EProject;
import com.e3ps.project.ETask;

import wt.org.WTUser;

public class ETaskMailForm {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PROJECT.getName());
	
	public static Hashtable<String, Object> setTaskDelayMailInfo(ETask task, WTUser owner) throws Exception{
		
		String hostName = WCUtil.getPlmHost();
		
		String workName = task.getName(); // 활동명
		String planStartDate = DateUtil.getDateString(task.getPlanStartDate(), "d");
		String planEndDate = DateUtil.getDateString(task.getPlanEndDate(), "d");
		EProject project = (EProject) task.getProject();
		String ecNumber = project.getCode();
		String ecName = project.getName();
		String gubun = "TASK";
		String creatorName = project.getCreator().getFullName();
		String viewString = "[" + ecNumber +"]" + ecName;
		String subject = "(프로젝트)"+ecNumber + " [" + workName + "] 지연업무 확인 요청드립니다.";
		String startDate = DateUtil.getToDay();
	
		Hashtable<String, Object> mailHash = new Hashtable<String, Object>();
		if(owner == null){
			return mailHash;
		}
		Hashtable<String, Object> mapMailInfo = new Hashtable<String, Object>();
		
		String toName = owner.getFullName();
		String toMail = owner.getEMail();
		String taskUrl = hostName+ "/Windchill/worldex/project/viewMain?oid="+CommonUtil.getOIDString(task);
		mapMailInfo.put("subject", subject); //메일 제목
		mapMailInfo.put("toName",toName);	///메일 받는 사람
		mapMailInfo.put("toMail",toMail);	///메일 받는 사람
		//메일 Form 
		mapMailInfo.put("gubun", gubun);	
		mapMailInfo.put("workName", workName);
		mapMailInfo.put("viewString", viewString); //업무 대상
		mapMailInfo.put("description", subject);
		mapMailInfo.put("domainUrl", hostName);
		mapMailInfo.put("taskUrl", taskUrl);
		mapMailInfo.put("creatorName",creatorName);
		mapMailInfo.put("startDate", startDate);
		mapMailInfo.put("ownerName", toName);
		mapMailInfo.put("planStartDate", planStartDate);
		mapMailInfo.put("planEndDate", planEndDate);
		mailHash.put("mailInfo", mapMailInfo);
		
		return mailHash;
	}
	
	public static Hashtable<String, Object> setTaskDeadlineMailInfo(ETask task, WTUser owner) throws Exception{
		
		String hostName = WCUtil.getPlmHost();
		
		String workName = task.getName(); // 활동명
		EProject project = (EProject) task.getProject();
		
		String ecNumber = project.getCode();
		String ecName = project.getName();
		String gubun = "PROJECT";
		String creatorName = project.getCreator().getFullName();
		String viewString = "[" + ecNumber +"]" + ecName;
		String subject = ecNumber + " [" + workName + "] 업무마감일에 도래했습니다. 확인 요청드립니다.";
		String startDate = DateUtil.getToDay();
	
		Hashtable<String, Object> mailHash = new Hashtable<String, Object>();
		if(owner == null){
			return mailHash;
		}
		Hashtable<String, Object> mapMailInfo = new Hashtable<String, Object>();
		
		String toName = owner.getFullName();
		String toMail = owner.getEMail();
		
		mapMailInfo.put("subject", subject); //메일 제목
		mapMailInfo.put("toName",toName);	///메일 받는 사람
		mapMailInfo.put("toMail",toMail);	///메일 받는 사람
		//메일 Form 
		mapMailInfo.put("gubun", gubun);	
		mapMailInfo.put("workName", workName);
		mapMailInfo.put("viewString", viewString); //업무 대상
		mapMailInfo.put("description", subject);
		mapMailInfo.put("domainUrl", hostName);
		mapMailInfo.put("creatorName",creatorName);
		mapMailInfo.put("startDate", startDate);
		mapMailInfo.put("ownerName", toName);
		mailHash.put("mailInfo", mapMailInfo);
		
		return mailHash;
	}
}
