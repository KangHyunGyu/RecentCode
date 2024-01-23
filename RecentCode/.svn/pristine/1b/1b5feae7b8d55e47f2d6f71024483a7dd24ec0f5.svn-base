package com.e3ps.project.util;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.e3ps.approval.ApprovalLine;
import com.e3ps.approval.bean.ApprovalData;
import com.e3ps.approval.service.ApprovalHelper;
import com.e3ps.approval.util.ApprovalUtil;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.org.People;
import com.e3ps.org.service.PeopleHelper;
import com.e3ps.project.beans.IssueData;
import com.e3ps.project.issue.IssueRequest;
import com.e3ps.project.issue.IssueSolution;
import com.e3ps.project.service.IssueHelper;

public class IssueMailForm {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PROJECT.getName());
	
	public static Hashtable<String, Object> setAssaignMailInfo(IssueRequest line)  {
		Hashtable<String, Object> mailHash = new Hashtable<String, Object>();
		
		try{
			String hostName = WCUtil.getPlmHost();
			
			//메일 수집
			Hashtable<String, Object> mapMailInfo = new Hashtable<String, Object>();
				
			IssueData data = new IssueData(line);
			
			String objectTypName = "이슈";
				
			String subject = "("+objectTypName +") ["+data.getCreatorFullName()+"]"+data.getTitle() +"의 " + "이슈" +" 업무가 도착 하였습니다.";
				
			String workUrl = "http://" + hostName + CommonUtil.getURLString("/project/issue/viewIssue") + "?oid="+CommonUtil.getOIDString(line);

			mapMailInfo = new Hashtable<String, Object>();
			String toName = data.getManagerFullName();
			String toMail = line.getManager().getEMail();
			
			mapMailInfo.put("workName", "해결");
			mapMailInfo.put("subject", subject); //메일 제목
			mapMailInfo.put("toName",toName);	///메일 받는 사람
			mapMailInfo.put("toMail",toMail);	///메일 받는 사람
			//메일 Form 
			mapMailInfo.put("gubun", objectTypName);	
			mapMailInfo.put("requestDate", data.getRequestDate());	
			mapMailInfo.put("deadline", data.getDeadLine());	
			mapMailInfo.put("viewString", data.getTitle());
			mapMailInfo.put("description", subject);
			mapMailInfo.put("domainUrl", hostName);
			mapMailInfo.put("creatorName",data.getCreatorFullName());
			mapMailInfo.put("startDate", data.getCreateDate());
			mapMailInfo.put("workUrl", workUrl);
			mapMailInfo.put("ownerName", toName);
			mailHash.put("mailInfo", mapMailInfo);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return mailHash;
	}
	
	public static Hashtable<String, Object> setSolutionMailInfo(IssueRequest line)  {
		Hashtable<String, Object> mailHash = new Hashtable<String, Object>();
		
		try{
			String hostName = WCUtil.getPlmHost();
			
			//메일 수집
			Hashtable<String, Object> mapMailInfo = new Hashtable<String, Object>();
			
			IssueData data = new IssueData(line);
			
			String objectTypName = "이슈 해결방안";
			
			String subject = "("+objectTypName +") ["+data.getCreatorFullName()+"]"+data.getTitle() +"의 " + "해결방안" +" 이 등록되었습니다.";
			
			String workUrl = "http://" + hostName + CommonUtil.getURLString("/project/issue/viewIssue") + "?oid="+CommonUtil.getOIDString(line);
			
			mapMailInfo = new Hashtable<String, Object>();
			String toName = data.getCreatorFullName();
			String toMail = line.getCreator().getEMail();
			
			mapMailInfo.put("workName", "\\");
			mapMailInfo.put("subject", subject); //메일 제목
			mapMailInfo.put("toName",toName);	///메일 받는 사람
			mapMailInfo.put("toMail",toMail);	///메일 받는 사람
			//메일 Form 
			mapMailInfo.put("gubun", objectTypName);	
			mapMailInfo.put("requestDate", data.getRequestDate());	
			mapMailInfo.put("deadline", data.getDeadLine());	
			mapMailInfo.put("viewString", data.getTitle());
			mapMailInfo.put("description", subject);
			mapMailInfo.put("domainUrl", hostName);
			mapMailInfo.put("creatorName",data.Solution().getCreator().getFullName());
			mapMailInfo.put("startDate", data.Solution().getCreateTimestamp());
			mapMailInfo.put("workUrl", workUrl);
			mapMailInfo.put("ownerName", toName);
			mailHash.put("mailInfo", mapMailInfo);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return mailHash;
	}
	
	public static Hashtable<String, Object> setCompleteMailInfo(IssueRequest line)  {
		Hashtable<String, Object> mailHash = new Hashtable<String, Object>();
		
		try{
			String hostName = WCUtil.getPlmHost();
			
			//메일 수집
			Hashtable<String, Object> mapMailInfo = new Hashtable<String, Object>();
			
			IssueData data = new IssueData(line);
			
			String objectTypName = "이슈";
			
			String subject = "("+objectTypName +") ["+data.getCreatorFullName()+"]"+data.getTitle() +"의 " + "이슈의 해결이 " +"완료되었습니다.";
			
			String workUrl = "http://" + hostName + CommonUtil.getURLString("/project/issue/viewIssue") + "?oid="+CommonUtil.getOIDString(line);
			
			mapMailInfo = new Hashtable<String, Object>();
			String toName = data.getCreatorFullName();
			String toMail = line.getManager().getEMail();
			
			mapMailInfo.put("workName", "해결");
			mapMailInfo.put("subject", subject); //메일 제목
			mapMailInfo.put("toName",toName);	///메일 받는 사람
			mapMailInfo.put("toMail",toMail);	///메일 받는 사람
			//메일 Form 
			mapMailInfo.put("gubun", objectTypName);	
			mapMailInfo.put("requestDate", data.getRequestDate());	
			mapMailInfo.put("deadline", data.getDeadLine());	
			mapMailInfo.put("viewString", data.getTitle());
			mapMailInfo.put("description", subject);
			mapMailInfo.put("domainUrl", hostName);
			mapMailInfo.put("creatorName",data.Solution().getCreator().getFullName());
			mapMailInfo.put("startDate", data.getCreateDate());
			mapMailInfo.put("workUrl", workUrl);
			mapMailInfo.put("ownerName", toName);
			mailHash.put("mailInfo", mapMailInfo);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return mailHash;
	}

	public static Hashtable<String, Object> setRejectMailInfo(IssueRequest line)  {
		Hashtable<String, Object> mailHash = new Hashtable<String, Object>();
		
		try{
			String hostName = WCUtil.getPlmHost();
			
			//메일 수집
			Hashtable<String, Object> mapMailInfo = new Hashtable<String, Object>();
			
			IssueData data = new IssueData(line);
			
			String objectTypName = "이슈";
			
			String subject = "("+objectTypName +") ["+data.getCreatorFullName()+"]"+data.getTitle() +"의 " + "등록하신 이슈 해결방안이" +" 완료취소 되었습니다.";
			
			String workUrl = "http://" + hostName + CommonUtil.getURLString("/project/issue/viewIssue") + "?oid="+CommonUtil.getOIDString(line);
			
			mapMailInfo = new Hashtable<String, Object>();
			String toName = data.getCreatorFullName();
			String toMail = line.getManager().getEMail();
			
			mapMailInfo.put("workName", "해결");
			mapMailInfo.put("subject", subject); //메일 제목
			mapMailInfo.put("toName",toName);	///메일 받는 사람
			mapMailInfo.put("toMail",toMail);	///메일 받는 사람
			//메일 Form 
			mapMailInfo.put("gubun", objectTypName);	
			mapMailInfo.put("requestDate", data.getRequestDate());	
			mapMailInfo.put("deadline", data.getDeadLine());	
			mapMailInfo.put("viewString", data.getTitle());
			mapMailInfo.put("description", subject);
			mapMailInfo.put("domainUrl", hostName);
			mapMailInfo.put("creatorName",data.Solution().getCreator().getFullName());
			mapMailInfo.put("startDate", data.getCreateDate());
			mapMailInfo.put("workUrl", workUrl);
			mapMailInfo.put("ownerName", toName);
			mailHash.put("mailInfo", mapMailInfo);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return mailHash;
	}
	
	public static Hashtable<String, Object> setIssueDelayMailInfo(IssueRequest line)  {
		Hashtable<String, Object> mailHash = new Hashtable<String, Object>();
		
		try{
			String hostName = WCUtil.getPlmHost();
			
			//메일 수집
			Hashtable<String, Object> mapMailInfo = new Hashtable<String, Object>();
			
			IssueData data = new IssueData(line);
			
			String objectTypName = "이슈";
			
			String subject = "("+objectTypName +") ["+data.getCreatorFullName()+"]"+data.getTitle() +" 지연업무 확인 요청드립니다.";
			
			String workUrl = "http://" + hostName + CommonUtil.getURLString("/project/issue/viewIssue") + "?oid="+CommonUtil.getOIDString(line);
			
			mapMailInfo = new Hashtable<String, Object>();
			String toName = data.getManagerFullName();
			String toMail = line.getManager().getEMail();
			
			mapMailInfo.put("workName", "해결");
			mapMailInfo.put("subject", subject); //메일 제목
			mapMailInfo.put("toName",toName);	///메일 받는 사람
			mapMailInfo.put("toMail",toMail);	///메일 받는 사람
			//메일 Form 
			mapMailInfo.put("gubun", objectTypName);	
			mapMailInfo.put("requestDate", data.getRequestDate());	
			mapMailInfo.put("deadline", data.getDeadLine());	
			mapMailInfo.put("viewString", data.getTitle());
			mapMailInfo.put("description", subject);
			mapMailInfo.put("domainUrl", hostName);
//			mapMailInfo.put("creatorName",data.Solution().getCreator().getFullName());
			mapMailInfo.put("startDate", data.getCreateDate());
			mapMailInfo.put("workUrl", workUrl);
			mapMailInfo.put("ownerName", toName);
			mailHash.put("mailInfo", mapMailInfo);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return mailHash;
	}
	
	public static Hashtable<String, Object> setIssueDeadlineMailInfo(IssueRequest line)  {
		Hashtable<String, Object> mailHash = new Hashtable<String, Object>();
		
		try{
			String hostName = WCUtil.getPlmHost();
			
			//메일 수집
			Hashtable<String, Object> mapMailInfo = new Hashtable<String, Object>();
			
			IssueData data = new IssueData(line);
			
			String objectTypName = "이슈";
			
			String subject = "("+objectTypName +") ["+data.getCreatorFullName()+"]"+data.getTitle() +" 업무마감일에 도래했습니다. 확인 요청드립니다.";
			
			String workUrl = "http://" + hostName + CommonUtil.getURLString("/project/issue/viewIssue") + "?oid="+CommonUtil.getOIDString(line);
			
			mapMailInfo = new Hashtable<String, Object>();
			String toName = data.getManagerFullName();
			String toMail = line.getManager().getEMail();
			
			mapMailInfo.put("workName", "해결");
			mapMailInfo.put("subject", subject); //메일 제목
			mapMailInfo.put("toName",toName);	///메일 받는 사람
			mapMailInfo.put("toMail",toMail);	///메일 받는 사람
			//메일 Form 
			mapMailInfo.put("gubun", objectTypName);	
			mapMailInfo.put("requestDate", data.getRequestDate());	
			mapMailInfo.put("deadline", data.getDeadLine());	
			mapMailInfo.put("viewString", data.getTitle());
			mapMailInfo.put("description", subject);
			mapMailInfo.put("domainUrl", hostName);
//			mapMailInfo.put("creatorName",data.Solution().getCreator().getFullName());
			mapMailInfo.put("startDate", data.getCreateDate());
			mapMailInfo.put("workUrl", workUrl);
			mapMailInfo.put("ownerName", toName);
			mailHash.put("mailInfo", mapMailInfo);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return mailHash;
	}
}
