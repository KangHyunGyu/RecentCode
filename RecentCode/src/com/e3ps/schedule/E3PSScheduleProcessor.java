package com.e3ps.schedule;

import java.util.Properties;

import org.slf4j.Logger;

import com.e3ps.common.log4j.Log4jPackages;
import com.ptc.wvs.server.schedule.Schedulable;
import com.ptc.wvs.server.schedule.ScheduledJobProcessor;

import wt.log4j.LogR;

public class E3PSScheduleProcessor extends ScheduledJobProcessor{
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.SCHEDULE.getName());

	private static final String JOB_EO_REMIND 	= "EO_REMIND_MAIL_BATCH";
	private static final String JOB_DISTRIBUTE_UPDATE 	= "DISTRIBUTE_UPDATE_BATCH";
	private static final String JOB_DOUBLE_BOM_CLEAR 	= "DOUBLE_BOM_CLEAR_BATCH";
	private static final String JOB_EO_TODAY 	= "EO_TODAY_MAIL_BATCH";
	private static final String JOB_EO_DELAY 	= "EO_DELAY_MAIL_BATCH";
	private static final String JOB_TASK_DELAY 	= "TASK_DELAY_MAIL_BATCH";
	private static final String JOB_ISSUE_DELAY 	= "ISSUE_DELAY_MAIL_BATCH";
	private static final String JOB_TEMP_DELETE 	= "TEMP_DELETE_BATCH";
	
	public E3PSScheduleProcessor(String arg0) {
		super(arg0);
		
	}

	@Override
	public void doScheduleJob(Schedulable arg0, boolean arg1, String arg2, Properties arg3) {
		if(JOB_EO_REMIND.equals(arg0.getIdentifier())){
			LOGGER.info("E3PS JOB Schedule START " + JOB_EO_REMIND + ":::::::::::::::");
			LOGGER.info("E3PS JOB Schedule END " + JOB_EO_REMIND+":::::::::::::::");
		} else if(JOB_DISTRIBUTE_UPDATE.equals(arg0.getIdentifier())){
			LOGGER.info("E3PS JOB Schedule START " + JOB_DISTRIBUTE_UPDATE + ":::::::::::::::");
			E3PSScheduleJobs.distributeUpdateBatch();
			LOGGER.info("E3PS JOB Schedule END " + JOB_DISTRIBUTE_UPDATE+":::::::::::::::");
		} else if(JOB_DOUBLE_BOM_CLEAR.equals(arg0.getIdentifier())){
			LOGGER.info("E3PS JOB Schedule START " + JOB_DOUBLE_BOM_CLEAR + ":::::::::::::::");
			E3PSScheduleJobs.doubleBomClearBatch();
			LOGGER.info("E3PS JOB Schedule END " + JOB_DOUBLE_BOM_CLEAR+":::::::::::::::");
		} else if(JOB_EO_TODAY.equals(arg0.getIdentifier())){
			LOGGER.info("E3PS JOB Schedule START " + JOB_EO_TODAY + ":::::::::::::::");
			LOGGER.info("E3PS JOB Schedule END " + JOB_EO_TODAY+":::::::::::::::");
		} else if(JOB_EO_DELAY.equals(arg0.getIdentifier())){
			LOGGER.info("E3PS JOB Schedule START " + JOB_EO_DELAY + ":::::::::::::::");
			E3PSScheduleJobs.eoDelayMailSendBatch();
			LOGGER.info("E3PS JOB Schedule END " + JOB_EO_DELAY+":::::::::::::::");
		} else if(JOB_TASK_DELAY.equals(arg0.getIdentifier())){
			LOGGER.info("E3PS JOB Schedule START " + JOB_TASK_DELAY + ":::::::::::::::");
			E3PSScheduleJobs.taskDelayMailSendBatch();
			LOGGER.info("E3PS JOB Schedule END " + JOB_TASK_DELAY+":::::::::::::::");
		} else if(JOB_ISSUE_DELAY.equals(arg0.getIdentifier())){
			LOGGER.info("E3PS JOB Schedule START " + JOB_ISSUE_DELAY + ":::::::::::::::");
			E3PSScheduleJobs.issueDelayMailSendBatch();
			LOGGER.info("E3PS JOB Schedule END " + JOB_ISSUE_DELAY+":::::::::::::::");
		} else if(JOB_TEMP_DELETE.equals(arg0.getIdentifier())){
			LOGGER.info("E3PS JOB Schedule START " + JOB_TEMP_DELETE + ":::::::::::::::");
			E3PSScheduleJobs.tempDeleteBatch();
			LOGGER.info("E3PS JOB Schedule END " + JOB_TEMP_DELETE+":::::::::::::::");
		}
		
	}

}
