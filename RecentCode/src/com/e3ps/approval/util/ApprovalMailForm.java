package com.e3ps.approval.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.e3ps.approval.ApprovalLine;
import com.e3ps.approval.ApprovalMaster;
import com.e3ps.approval.bean.ApprovalData;
import com.e3ps.approval.bean.ApprovalLineData;
import com.e3ps.approval.service.ApprovalHelper;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.org.People;
import com.e3ps.org.service.PeopleHelper;

import wt.fc.Persistable;
import wt.org.WTUser;

public class ApprovalMailForm {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.APPROVAL.getName());
	
	public static Hashtable<String, Object> setApprovealMailInfo(ApprovalLine line)  {
		Hashtable<String, Object> mailHash = new Hashtable<String, Object>();
		
		try{
			String hostName = WCUtil.getPlmHost();
			String  role= line.getRole().toString();
			
			
			
			List<ApprovalLine> list = ApprovalHelper.manager.getApprovalOnGoingLine(line.getMaster());
			
			Map<String, String> map= ApprovalUtil.getObjectTypeNameMap();
			//메일 수집
			int i = 0;
			for(ApprovalLine rline : list) {
				ApprovalLineData lineData = new ApprovalLineData(rline);
				lineData.checkLastSeqToSameRole();
				
				//합의 , 수신 인 경우 동일한 Role인 경우 제외
				if(role.equals(ApprovalUtil.ROLE_DISCUSS) || role.equals(ApprovalUtil.ROLE_RECEIVE)) {
					if(role.equals(rline.getRole().toString())) {
						continue;
					}
				}
				Hashtable<String, Object> mapMailInfo = new Hashtable<String, Object>();
				
				ApprovalData data = ApprovalHelper.manager.getApprovalData(rline);
				String objectTypName = map.get(data.getObjectType());
				
				String subject = "("+objectTypName +") ["+line.getMaster().getOwner().getFullName()+"]"+data.getTitle() +"의 " + data.getRoleName() +" 업무가 도착 하였습니다.";
				
				String workUrl = "http://" + hostName + CommonUtil.getURLString("/workspace/detailApproval") + "?type=approval&oid="+CommonUtil.getOIDString(rline);
				
				People pp = PeopleHelper.manager.getIDPeople(rline.getOwner().getName());
				
				mapMailInfo = new Hashtable<String, Object>();
				String toName = rline.getOwner().getFullName();
				String toMail = rline.getOwner().getEMail();
				
				mapMailInfo.put("subject", subject); //메일 제목
				mapMailInfo.put("toName",toName);	///메일 받는 사람
				mapMailInfo.put("toMail",toMail);	///메일 받는 사람
				//메일 Form 
				mapMailInfo.put("gubun", objectTypName);	
				mapMailInfo.put("workName", data.getRoleName());
				mapMailInfo.put("viewString", data.getTitle());
				mapMailInfo.put("description", subject);
				mapMailInfo.put("domainUrl", hostName);
				mapMailInfo.put("creatorName",data.getCreatorName());
				mapMailInfo.put("startDate", data.getStartDateFormat());
				mapMailInfo.put("workUrl", workUrl);
				mapMailInfo.put("ownerName", toName);
				mapMailInfo.put("approvalType", StringUtil.checkReplaceStr(lineData.getApprovalGubun(), "담당자"));
				mailHash.put("mailInfo"+i, mapMailInfo);
				i++;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return mailHash;
	}
	/**
	 * 
	 * @desc	: 최종 결재 완료후 승인 완료시 결재 라인 메일 발송(기안,협의,승인, -- 수신 제외)
	 * @author	: tsuam
	 * @date	: 2019. 10. 29.
	 * @method	: setApprovedMailInfo
	 * @return	: Hashtable<String,Object>
	 * @param pp
	 * @return
	 * @throws Exception
	 */
	public static Hashtable<String, Object> setApprovedMailInfo(Persistable pp)  {
		Hashtable<String, Object> mailHash = new Hashtable<String, Object>();
		try {
			Map<String, String> map = ApprovalUtil.getApprovalObjectInfo(pp);
			ApprovalMaster master = ApprovalHelper.manager.getApprovalMaster(pp);
			List<ApprovalLine> approvalList = ApprovalHelper.manager.getApprovalLastLineAll(master);
			
			
			String gubun = map.get("objectType");
			Map<String, String> gubunMap = ApprovalUtil.getObjectTypeNameMap();
			gubun = gubunMap.get(gubun);
			String number = map.get("number");
			String name = map.get("name");
			String title = map.get("title");
			String creator =map.get("creator");
			//String state = eo.getLifeCycleState().getDisplay();		
			String subject = gubun+"의 승인 완료  공지 메일 입니다." +title;
			String workName = number;
			String viewString = title;
			String createName = creator;
			String startDate = DateUtil.getToDay();
			String hostName = WCUtil.getPlmHost();
			
			String description =  subject;
			int i = 1;
			LOGGER.info("setApprovedMailInfo approvalList =" + approvalList.size());
			for(ApprovalLine line : approvalList){
				LOGGER.info("setApprovedMailInfo approvalList 1 =" + line.getOwner().getName() +","+line.getOwner().getFullName());
				if(line.getRole().toString().equals(ApprovalUtil.ROLE_RECEIVE)){
					continue;
				}
				//LOGGER.info("setApprovedMailInfo approvalList 2 =" + line.getOwner().getName() +","+line.getOwner().getFullName());
				
				
				//LOGGER.info("setApprovedMailInfo = " + line.getOwner().getName() +":" + line.getOwner().getFullName());
				
				String toName = "";
				String toMail = "";
				toName = line.getOwner().getFullName();
				toMail = line.getOwner().getEMail();
				
				if(toName == null || toMail == null){
					LOGGER.info("sendApprovalMail Not Email : " + line.getOwner().getName());
					continue;
				}
				
				LOGGER.info("setApprovedMailInfo Email Send user :  " + toName +":" + toMail);
				Hashtable<String, Object> mapMailInfo = new Hashtable<String, Object>();
				mapMailInfo.put("subject", subject); //메일 제목
				mapMailInfo.put("toName",toName);	///메일 받는 사람
				mapMailInfo.put("toMail",toMail);	///메일 받는 사람
				//메일 Form 
				mapMailInfo.put("gubun", gubun);	
				mapMailInfo.put("workName", workName);
				mapMailInfo.put("viewString", viewString); //업무 대상
				mapMailInfo.put("description", description);
				mapMailInfo.put("domainUrl", hostName);
				mapMailInfo.put("ownerName",toName);
				mapMailInfo.put("creatorName",createName);
				mapMailInfo.put("startDate", startDate);
				//mapMailInfo.put("workUrl", workUrl);
				mapMailInfo.put("ownerName", toName);
				mailHash.put("mailInfo"+i, mapMailInfo);
				i++;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return mailHash;
	}
	
	/**
	 * 
	 * @desc	: 반려 메일
	 * @author	: tsuam
	 * @date	: 2020. 2. 25.
	 * @method	: setReturnMailInfo
	 * @return	: Hashtable<String,Object>
	 * @param pp
	 * @return
	 */
	public static Hashtable<String, Object> setReturnMailInfo(Persistable pp)  {
		Hashtable<String, Object> mailHash = new Hashtable<String, Object>();
		try {
			Map<String, String> map = ApprovalUtil.getApprovalObjectInfo(pp);
			ApprovalMaster master = ApprovalHelper.manager.getApprovalMaster(pp);
			List<ApprovalLine> approvalList = ApprovalHelper.manager.getApprovalLastLineAll(master);
			
			
			String gubun = map.get("objectType");
			Map<String, String> gubunMap = ApprovalUtil.getObjectTypeNameMap();
			gubun = gubunMap.get(gubun);
			String number = map.get("number");
			String name = map.get("name");
			String title = map.get("title");
			String creator =map.get("creator");
			//String state = eo.getLifeCycleState().getDisplay();		
			String subject = gubun+" 의 반려  공지 메일 입니다." +title;
			String workName = number;
			String viewString = title;
			String createName = creator;
			String startDate = DateUtil.getToDay();
			String hostName = WCUtil.getPlmHost();
			
			String description =  subject;
			int i = 1;
			
			for(ApprovalLine line : approvalList){
				
				//기안에게만 메일 발송
				if(!line.getRole().toString().equals(ApprovalUtil.ROLE_DRAFT)){
					continue;
				}
				
				Hashtable<String, Object> mapMailInfo = new Hashtable<String, Object>();
				
				//LOGGER.info("setApprovedMailInfo = " + line.getOwner().getName() +":" + line.getOwner().getFullName());
				
				String toName = "";
				String toMail = "";
				toName = line.getOwner().getFullName();
				toMail = line.getOwner().getEMail();
				
				if(toName == null || toMail == null){
					LOGGER.info("sendApprovalMail Not Email : " + line.getOwner().getName() +toName +"," + toMail);
					continue;
				}
				
				//LOGGER.info("setApprovedMailInfo Email Send user :  " + line.getOwner().getName() +":" + line.getOwner().getFullName());
				
				mapMailInfo.put("subject", subject); //메일 제목
				mapMailInfo.put("toName",toName);	///메일 받는 사람
				mapMailInfo.put("toMail",toMail);	///메일 받는 사람
				//메일 Form 
				mapMailInfo.put("gubun", gubun);	
				mapMailInfo.put("workName", workName);
				mapMailInfo.put("viewString", viewString); //업무 대상
				mapMailInfo.put("description", description);
				mapMailInfo.put("domainUrl", hostName);
				mapMailInfo.put("ownerName",toName);
				mapMailInfo.put("creatorName",createName);
				mapMailInfo.put("startDate", startDate);
				//mapMailInfo.put("workUrl", workUrl);
				mapMailInfo.put("ownerName", toName);
				mailHash.put("mailInfo"+i, mapMailInfo);
				i++;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return mailHash;
	}
	
	/**
	 * 
	 * @desc	: 위임 메일
	 * @author	: tsuam
	 * @date	: 2020. 2. 25.
	 * @method	: setsendDelegateMailInfo
	 * @return	: Hashtable<String,Object>
	 * @param pp
	 * @return
	 */
	public static Hashtable<String, Object> setDelegateMailInfo(ApprovalLine beforeLine,WTUser delegateWTUser)  {
		Hashtable<String, Object> mailHash = new Hashtable<String, Object>();
		try {
			
			Map<String, String> map= ApprovalUtil.getObjectTypeNameMap();
			String startDate = DateUtil.getToDay();
			String hostName = WCUtil.getPlmHost();
			
			ApprovalData data = ApprovalHelper.manager.getApprovalData(beforeLine);
			ApprovalLineData lineData = new ApprovalLineData(beforeLine);
			lineData.checkLastSeqToSameRole();
			
			String objectTypName = map.get(data.getObjectType());
			
			String subject = "("+objectTypName +") ["+beforeLine.getMaster().getOwner().getFullName()+"]"+data.getTitle() +"의 " + data.getRoleName()+" 업무가 위임 되었습니다.";
			
			//String workUrl = "http://" + hostName + CommonUtil.getURLString("/workspace/detailApproval") + "?type=approval&oid="+CommonUtil.getOIDString(afterLine);
			
			String description =  beforeLine.getDescription();
			int i = 1;
				
			Hashtable<String, Object> mapMailInfo = new Hashtable<String, Object>();
			
			People pp = PeopleHelper.manager.getIDPeople(delegateWTUser.getName());
			String toName = delegateWTUser.getFullName();
			String toMail = delegateWTUser.getEMail();
			mapMailInfo.put("subject", subject); //메일 제목
			mapMailInfo.put("toName",toName);	///메일 받는 사람
			mapMailInfo.put("toMail",toMail);	///메일 받는 사람
			//메일 Form 
			mapMailInfo.put("gubun", objectTypName);	
			mapMailInfo.put("workName", data.getRoleName());
			mapMailInfo.put("viewString", data.getTitle()); //업무 대상
			mapMailInfo.put("description", description);
			mapMailInfo.put("domainUrl", hostName);
			mapMailInfo.put("ownerName",toName);
			mapMailInfo.put("creatorName",data.getCreatorName());
			mapMailInfo.put("startDate", startDate);
			//mapMailInfo.put("workUrl", workUrl);
			mapMailInfo.put("ownerName", toName);
			mapMailInfo.put("approvalType", StringUtil.checkReplaceStr(lineData.getApprovalGubun(), "담당자"));
			mailHash.put("mailInfo"+i, mapMailInfo);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return mailHash;
	}
	
	/**
	 * 
	 * @desc	: ECR Drop인 경우 발송
	 * @author	: plmadmin
	 * @date	: 2020. 3. 16.
	 * @method	: setDropMailInfo
	 * @return	: Hashtable<String,Object>
	 * @param pp
	 * @return
	 */
	public static Hashtable<String, Object> setDropMailInfo(Persistable pp)  {
		Hashtable<String, Object> mailHash = new Hashtable<String, Object>();
		try {
			Map<String, String> map = ApprovalUtil.getApprovalObjectInfo(pp);
			ApprovalMaster master = ApprovalHelper.manager.getApprovalMaster(pp);
			List<ApprovalLine> approvalList = ApprovalHelper.manager.getApprovalLastLineAll(master);
			
			
			String gubun = map.get("objectType");
			Map<String, String> gubunMap = ApprovalUtil.getObjectTypeNameMap();
			gubun = gubunMap.get(gubun);
			String number = map.get("number");
			String name = map.get("name");
			String title = map.get("title");
			String creator =map.get("creator");
			//String state = eo.getLifeCycleState().getDisplay();		
			String subject = gubun+" 의 Drop 메일 입니다." +title;
			String workName = number;
			String viewString = title;
			String createName = creator;
			String startDate = DateUtil.getToDay();
			String hostName = WCUtil.getPlmHost();
			
			String description =  subject;
			int i = 1;
			
			for(ApprovalLine line : approvalList){
				
				//기안에게만 메일 발송
				if(!line.getRole().toString().equals(ApprovalUtil.ROLE_DRAFT)){
					continue;
				}
				
				Hashtable<String, Object> mapMailInfo = new Hashtable<String, Object>();
				
				//LOGGER.info("setApprovedMailInfo = " + line.getOwner().getName() +":" + line.getOwner().getFullName());
				
				String toName = "";
				String toMail = "";
				toName = line.getOwner().getFullName();
				toMail = line.getOwner().getEMail();
				
				if(toName == null || toMail == null){
					LOGGER.info("sendApprovalMail Not Email : " + line.getOwner().getName() +toName +"," + toMail);
					continue;
				}
				
				//LOGGER.info("setApprovedMailInfo Email Send user :  " + line.getOwner().getName() +":" + line.getOwner().getFullName());
				
				mapMailInfo.put("subject", subject); //메일 제목
				mapMailInfo.put("toName",toName);	///메일 받는 사람
				mapMailInfo.put("toMail",toMail);	///메일 받는 사람
				//메일 Form 
				mapMailInfo.put("gubun", gubun);	
				mapMailInfo.put("workName", workName);
				mapMailInfo.put("viewString", viewString); //업무 대상
				mapMailInfo.put("description", description);
				mapMailInfo.put("domainUrl", hostName);
				mapMailInfo.put("ownerName",toName);
				mapMailInfo.put("creatorName",createName);
				mapMailInfo.put("startDate", startDate);
				//mapMailInfo.put("workUrl", workUrl);
				mapMailInfo.put("ownerName", toName);
				mailHash.put("mailInfo"+i, mapMailInfo);
				i++;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return mailHash;
	}
	

}
