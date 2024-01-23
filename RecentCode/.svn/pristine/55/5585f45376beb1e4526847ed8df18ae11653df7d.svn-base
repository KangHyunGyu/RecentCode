package com.e3ps.schedule;

import com.e3ps.admin.service.AdminHelper;
import com.e3ps.change.service.ChangeHelper;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.epm.service.EpmHelper;
import com.e3ps.part.service.BomHelper;
import com.e3ps.project.service.IssueHelper;
import com.e3ps.project.service.ProjectHelper;

public class E3PSScheduleJobs {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.SCHEDULE.getName());
	
	/**
	 * 
	 * @desc	: distribute update
	 * @author	: mnyu
	 * @date	: 2020. 1. 23.
	 * @method	: distributeUpdateBatch
	 * @return	: void
	 */
	public static void distributeUpdateBatch() {
//		try {
//			LOGGER.info("***** [START] distributeUpdateBatch *****");
//			DistributeHelper.service.scheduleDistriubteUpdate();
//			LOGGER.info("***** [END] distributeUpdateBatch *****");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
	}
	/**
	 * 
	 * @desc	: double bom clear
	 * @author	: mnyu
	 * @date	: 2020. 1. 23.
	 * @method	: doubleBomClearBatch
	 * @return	: void
	 */
	public static void doubleBomClearBatch() {
//		try {
//			LOGGER.info("***** [START] doubleBomClearBatch *****");
//			BomHelper.service.doubleBomClear();
//			LOGGER.info("***** [END] doubleBomClearBatch *****");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
	}
	
	/**
	 * 
	 * @desc	: 변환 안되 도면 변환
	 * @author	: plmadmin
	 * @date	: 2020. 3. 13.
	 * @method	: epmPublishBatch
	 * @return	: void
	 */
	public static void epmPublishBatch() {
//		try {
//			LOGGER.info("***** [START] epmPublishBatch *****");
//			EpmHelper.service.epmNonPublishBatch("","");
//			LOGGER.info("***** [END] epmPublishBatch *****");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
	}
	
	/**
	 * 
	 * @desc	: 설계변경활동 지연업무 메일 발송
	 * @author	: plmadmin
	 * @date	: 2020. 10. 22.
	 * @method	: eoDelayMailSendBatch
	 * @return	: void
	 */
	public static void eoDelayMailSendBatch() {
		try {
			LOGGER.info("***** [START] eoDelayMailSendBatch *****");
			ChangeHelper.manager.delayMailSend();
			LOGGER.info("***** [END] eoDelayMailSendBatch *****");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @desc	: 프로젝트 테스크 지연업무 메일 발송
	 * @author	: plmadmin
	 * @date	: 2020. 10. 22.
	 * @method	: eoDelayMailSendBatch
	 * @return	: void
	 */
	public static void taskDelayMailSendBatch() {
		try {
			LOGGER.info("***** [START] taskDelayMailSendBatch *****");
			ProjectHelper.manager.delayTaskMailSchedule();
			LOGGER.info("***** [END] taskDelayMailSendBatch *****");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @desc	: 프로젝트 이슈 지연 메일 발송
	 * @author	: plmadmin
	 * @date	: 2020. 10. 22.
	 * @method	: eoDelayMailSendBatch
	 * @return	: void
	 */
	public static void issueDelayMailSendBatch() {
		try {
			LOGGER.info("***** [START] issueDelayMailSendBatch *****");
			IssueHelper.manager.delayIssueMailSchedule();
			LOGGER.info("***** [END] issueDelayMailSendBatch *****");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @desc	: WT_HOME\temp\e3ps 파일 삭제
	 * @author	: plmadmin
	 * @date	: 2020. 10. 22.
	 * @method	: tempDeleteBatch
	 * @return	: void
	 */
	public static void tempDeleteBatch() {
		try {
			LOGGER.info("***** [START] tempDeleteBatch *****");
			AdminHelper.manager.tempDeleteBatchSchedule();
			LOGGER.info("***** [END] tempDeleteBatch *****");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
