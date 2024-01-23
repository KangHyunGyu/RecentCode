package com.e3ps.common.mail;

import java.util.Hashtable;
import java.util.Iterator;

import com.e3ps.common.log4j.Log4jPackages;



public class EmailProcessQueue {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LOGGER.info("========== EmailProcessQueue main");
	}
	
	/**
	 * 
	 * @desc	: 결재 메일 발송
	 * @author	: tsuam
	 * @date	: 2019. 7. 31. 
	 * @method	: sendMailApproval
	 * @return	: void
	 * @param oid
	 */
	public static void sendMailApproval(Hashtable<String, Object> mailHash) {
		LOGGER.info("==========EmailProcessQueue sendMailApproval START");
		
		sendMail(mailHash, "approve_notice.html");
		
		LOGGER.info("==========EmailProcessQueue sendMailApproval END");
		
	}
	
	/**
	 * 
	 * @desc	: 배포 메일
	 * @author	: tsuam
	 * @date	: 2019. 10. 15.
	 * @method	: sendMailDistribute
	 * @return	: void
	 * @param mailHash
	 */
	public static void sendMailDistribute(Hashtable<String, Object> mailHash) {
		
		LOGGER.info("==========EmailProcessQueue sendMailDistribute START");
		
		sendMail(mailHash, "distribute_notice.html");
		
		LOGGER.info("==========EmailProcessQueue sendMailDistribute END");
		
	}
	
	/**
	 * 
	 * @desc	: EO 업무 부서 할당
	 * @author	: tsuam
	 * @date	: 2019. 10. 28.
	 * @method	: sendMailEODepartment
	 * @return	: void
	 * @param mailHash
	 */
	public static void sendMailEODepartment(Hashtable<String, Object> mailHash){
		LOGGER.info("==========EmailProcessQueue sendMailEODepartment START");
		
		sendMail(mailHash, "eo_department_notice.html");
		
		LOGGER.info("==========EmailProcessQueue sendMailEODepartment END");
	}
	
	
	/**
	 * 
	 * @desc	: Eo 업무 담당자 할당
	 * @author	: tsuam
	 * @date	: 2019. 10. 28.
	 * @method	: sendMailEOAssaign
	 * @return	: void
	 * @param mailHash
	 */
	public static void sendMailEOAssaign(Hashtable<String, Object> mailHash){
		LOGGER.info("==========EmailProcessQueue sendMailEOAssaign START");
		
		sendMail(mailHash, "eo_assign_notice.html");
		
		LOGGER.info("==========EmailProcessQueue sendMailEOAssaign END");
	}
	
	/**
	 * 
	 * @desc	: EO 업무 완료
	 * @author	: tsuam
	 * @date	: 2019. 10. 28.
	 * @method	: sendMailEOComplete
	 * @return	: void
	 * @param mailHash
	 */
	public static void sendMailEOComplete(Hashtable<String, Object> mailHash){
		LOGGER.info("==========EmailProcessQueue sendMailEOComplete START");
		
		sendMail(mailHash, "eo_notice.html");
		
		LOGGER.info("==========EmailProcessQueue sendMailEOComplete END");
	}
	
	/**
	 * 
	 * @desc	: 결재 최종 승인 완료시 메일 발송
	 * @author	: tsuam
	 * @date	: 2019. 10. 29.
	 * @method	: sendMailApprovalComplete
	 * @return	: void
	 * @param mailHash
	 */
	public static void sendMailApprovalComplete(Hashtable<String, Object> mailHash){
		
		LOGGER.info("==========EmailProcessQueue sendMailApprovalComplete START");
		
		sendMail(mailHash, "approved_notice.html");
		
		LOGGER.info("==========EmailProcessQueue sendMailApprovalComplete END");
		
	}
	
	/**
	 * 
	 * @desc	: 반려 이메일
	 * @author	: plmadmin
	 * @date	: 2020. 2. 25.
	 * @method	: sendMailReject
	 * @return	: void
	 * @param mailHash
	 */
	public static void sendMailReject(Hashtable<String, Object> mailHash){
		
		LOGGER.info("==========EmailProcessQueue sendMailApprovalComplete START");
		
		sendMail(mailHash, "reject_notice.html");
		
		LOGGER.info("==========EmailProcessQueue sendMailApprovalComplete END");
		
	}
	
	/**
	 * 
	 * @desc	: 위임 이메일
	 * @author	: plmadmin
	 * @date	: 2020. 2. 25.
	 * @method	: sendMailReject
	 * @return	: void
	 * @param mailHash
	 */
	public static void sendMailDelegate(Hashtable<String, Object> mailHash){
		
		LOGGER.info("==========EmailProcessQueue sendMailDelegate START");
		
		sendMail(mailHash, "approve_notice.html");
		
		LOGGER.info("==========EmailProcessQueue sendMailDelegate END");
		
	}
	
	/**
	 * 
	 * @desc	: Drop 메일
	 * @author	: plmadmin
	 * @date	: 2020. 3. 16.
	 * @method	: sendMailDrop
	 * @return	: void
	 * @param mailHash
	 */
	public static void sendMailDrop(Hashtable<String, Object> mailHash){
		
		LOGGER.info("==========EmailProcessQueue sendMailDrop START");
		
		sendMail(mailHash, "drop_notice.html");
		
		LOGGER.info("==========EmailProcessQueue sendMailDrop END");
		
	}
	
	/**
	 * 
	 * @desc	: 이슈 등록 메일
	 * @author	: tsjeong
	 * @date	: 2020. 10. 21.
	 * @method	: sendMailIssueAssaign
	 * @return	: void
	 * @param mailHash
	 */
	public static void sendMailIssueAssaign(Hashtable<String, Object> mailHash){
		
		LOGGER.info("==========EmailProcessQueue sendMailIssueAssaign START");
		
		sendMail(mailHash, "assaign_issue.html");
		
		LOGGER.info("==========EmailProcessQueue sendMailIssueAssaign END");
		
	}
	
	/**
	 * 
	 * @desc	: 이슈 해결방안 등록 메일
	 * @author	: tsjeong
	 * @date	: 2020. 10. 21.
	 * @method	: sendMailIssueSolution
	 * @return	: void
	 * @param mailHash
	 */
	public static void sendMailIssueSolution(Hashtable<String, Object> mailHash){
		
		LOGGER.info("==========EmailProcessQueue sendMailIssueSolution START");
		
		sendMail(mailHash, "solution_issue.html");
		
		LOGGER.info("==========EmailProcessQueue sendMailIssueSolution END");
		
	}
	
	/**
	 * 
	 * @desc	: 이슈 검토 완료 메일
	 * @author	: tsjeong
	 * @date	: 2020. 10. 21.
	 * @method	: sendMailIssueComplete
	 * @return	: void
	 * @param mailHash
	 */
	public static void sendMailIssueComplete(Hashtable<String, Object> mailHash){
		
		LOGGER.info("==========EmailProcessQueue sendMailIssueComplete START");
		
		sendMail(mailHash, "complete_issue.html");
		
		LOGGER.info("==========EmailProcessQueue sendMailIssueComplete END");
		
	}
	
	/**
	 * 
	 * @desc	: 이슈 검토 반려 메일
	 * @author	: tsjeong
	 * @date	: 2020. 10. 21.
	 * @method	: sendMailIssueReject
	 * @return	: void
	 * @param mailHash
	 */
	public static void sendMailIssueReject(Hashtable<String, Object> mailHash){
		
		LOGGER.info("==========EmailProcessQueue sendMailIssueReject START");
		
		sendMail(mailHash, "reject_issue.html");
		
		LOGGER.info("==========EmailProcessQueue sendMailIssueReject END");
		
	}
	
	public static void sendMailTaskDelay(Hashtable<String, Object> mailHash){
		
		LOGGER.info("==========EmailProcessQueue sendMailIssueReject START");
		
		sendMail(mailHash, "delay_task.html");
		
		LOGGER.info("==========EmailProcessQueue sendMailIssueReject END");
		
	}
	
	public static void sendMailEODelay(Hashtable<String, Object> mailHash){
		
		LOGGER.info("==========EmailProcessQueue sendMailIssueReject START");
		
		sendMail(mailHash, "delay_eo.html");
		
		LOGGER.info("==========EmailProcessQueue sendMailIssueReject END");
		
	}
	
	
	
	/**
	 * 
	 * @desc	: N개의 메일 발송
	 * @author	: tsuam
	 * @date	: 2019. 10. 15.
	 * @method	: sendMail
	 * @return	: void
	 * @param mailHash
	 * @param mailFormat
	 */
	public static void sendMail(Hashtable<String, Object> mailHash,String mailFormat){
		
		try {
			
			Iterator it = mailHash.keySet().iterator();
			
			while(it.hasNext()) {
				
				String key = (String)it.next();
				Object obj = mailHash.get(key);
				
				if(obj instanceof String){
					continue;
				}
				
				Hashtable<String, Object> hashMailInfo = (Hashtable)mailHash.get(key);
				hashMailInfo = getHtmlTemplate(hashMailInfo,mailFormat);
				
				hashMailInfo.put("toMail", "hgkang@e3ps.com");
				MailUtil.manager.sendWorldexMail(hashMailInfo);
				
				hashMailInfo.put("toMail", "dblim@e3ps.com");
				MailUtil.manager.sendWorldexMail(hashMailInfo);
				
				hashMailInfo.put("toMail", "sdhwang@e3ps.com");
				MailUtil.manager.sendWorldexMail(hashMailInfo);
			}

			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @desc	:
	 * @author	: plmadmin
	 * @date	: 2019. 10. 14.
	 * @method	: getHtmlTemplate
	 * @return	: Hashtable<String,Object>
	 * @param ht
	 * @param mailFormat approve_notice.html,
	 * @return
	 */
	public static Hashtable<String, Object> getHtmlTemplate(Hashtable<String, Object> ht,String mailFormat) {

		MailHtmlContentTemplate template = MailHtmlContentTemplate.getInstance();

		try {
		    LOGGER.info(
			    ">>>>>> com.e3ps.common.mail.EmailProcessQueue.getHtmlTemplate(Hashtable<String, Object>) ");
		    ht.put("content", template.htmlContent(ht, mailFormat));
		    
		} catch (Exception e) {
		    e.printStackTrace();
		}

		return ht;
	}

}
