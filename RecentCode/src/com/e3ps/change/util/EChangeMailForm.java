package com.e3ps.change.util;

import java.util.Hashtable;

import com.e3ps.change.EChangeActivity;
import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.EChangeRequest2;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.WCUtil;

import wt.fc.Persistable;
import wt.org.WTUser;

public class EChangeMailForm {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.CHANGE.getName());
	
	/**
	 * 
	 * @desc	: 업무 할당된 사람에게 메일 발송
	 * @author	: tsuam
	 * @date	: 2019. 10. 25.
	 * @method	: setEOAssignMailInfo
	 * @return	: void
	 * @param runTask
	 * @throws Exception
	 */
	public static Hashtable<String, Object> setActivityAssignMailInfo(EChangeActivity eca) throws Exception{
		
		String hostName = WCUtil.getPlmHost();
		
		String workName = eca.getName(); // 활동명
		Persistable per = eca.getOrder();
		
		String ecNumber = "";
		String ecName = "";
		String gubun = "";
		String creatorName = "";
		if(per instanceof EChangeRequest2) {
			EChangeRequest2 ecr = (EChangeRequest2) per;
			ecNumber = ecr.getRequestNumber();
			ecName = ecr.getName();
			gubun = "ECR";
			creatorName = ecr.getOwner().getFullName();
			
		}else if(per instanceof EChangeOrder2) {
			EChangeOrder2 eco = (EChangeOrder2) per;
			ecNumber = eco.getOrderNumber();
			ecName = eco.getName();
			gubun = "ECO";
			creatorName = eco.getOwner().getFullName();
		}
		String viewString = "[" + ecNumber +"]" + ecName;
		String subject = ecNumber + " [" + workName + "] 업무가 도착 하였습니다.";
		String startDate = DateUtil.getToDay();
	
		WTUser owner = (WTUser) eca.getOwner().getPrincipal();
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
	
	public static Hashtable<String, Object> setActivityDelayMailInfo(EChangeActivity eca) throws Exception{
		
		String hostName = WCUtil.getPlmHost();
		
		String workName = eca.getName(); // 활동명
		Persistable per = eca.getOrder();
		
		String ecNumber = eca.getName();
		String ecName = eca.getName();
		String finishDate = DateUtil.getDateString(eca.getFinishDate(), "d");
		String gubun = "ECA";
		String creatorName = eca.getOwner().getFullName();
		if(per instanceof EChangeRequest2) {
			EChangeRequest2 ecr = (EChangeRequest2) per;
			if(ecr != null) {
				ecNumber = ecr.getRequestNumber();
				ecName = ecr.getName();
				gubun = "ECR";
				creatorName = ecr.getOwner().getFullName();
			}
		}else if(per instanceof EChangeOrder2) {
			EChangeOrder2 eco = (EChangeOrder2) per;
			if(eco != null) {
				ecNumber = eco.getOrderNumber();
				ecName = eco.getName();
				gubun = "ECO";
				creatorName = eco.getOwner().getFullName();
			}
		}
		String viewString = "[" + ecNumber +"]" + ecName;
		String subject = "(설변업무)"+ecNumber + " [" + workName + "] 지연업무 확인 요청드립니다.";
		String startDate = DateUtil.getToDay();
	
		WTUser owner = (WTUser) eca.getOwner().getPrincipal();
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
		mapMailInfo.put("finishDate", finishDate);
		mailHash.put("mailInfo", mapMailInfo);
		
		return mailHash;
	}
	
	public static Hashtable<String, Object> setActivityDeadlineMailInfo(EChangeActivity eca) throws Exception{
		
		String hostName = WCUtil.getPlmHost();
		
		String workName = eca.getName(); // 활동명
		Persistable per = eca.getOrder();
		
		String ecNumber = eca.getName();
		String ecName = eca.getName();
		String gubun = "ECA";
		String creatorName = eca.getOwner().getFullName();
		if(per instanceof EChangeRequest2) {
			EChangeRequest2 ecr = (EChangeRequest2) per;
			if(ecr != null) {
				ecNumber = ecr.getRequestNumber();
				ecName = ecr.getName();
				gubun = "ECR";
				creatorName = ecr.getOwner().getFullName();
			}
		}else if(per instanceof EChangeOrder2) {
			EChangeOrder2 eco = (EChangeOrder2) per;
			if(eco != null) {
				ecNumber = eco.getOrderNumber();
				ecName = eco.getName();
				gubun = "ECO";
				creatorName = eco.getOwner().getFullName();
			}
		}
		String viewString = "[" + ecNumber +"]" + ecName;
		String subject = ecNumber + " [" + workName + "] 업무마감일에 도래했습니다. 확인 요청드립니다.";
		String startDate = DateUtil.getToDay();
	
		WTUser owner = (WTUser) eca.getOwner().getPrincipal();
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
