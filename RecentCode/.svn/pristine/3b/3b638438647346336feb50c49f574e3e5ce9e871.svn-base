package com.e3ps.common.mail;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;

import com.e3ps.common.log4j.Log4jPackages;

import wt.org.OrganizationServicesHelper;
import wt.org.WTPrincipal;
import wt.queue.MethodArgument;
import wt.queue.ProcessingQueue;
import wt.queue.QueueHelper;
import wt.scheduler.PrimitiveArg;
import wt.scheduler.ScheduleItem;
import wt.scheduler.ScheduleMethodArg;
import wt.scheduler.SchedulingHelper;
import wt.session.SessionHelper;
import wt.util.WTException;

public class EMailScheduler {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	private static final String  shceduleQueueName = "E3psEmailScheduleQueue";
	private static final String  processQueueName = "E3psEmailProcessQueue";
	private static final String  targetClassName = "com.e3ps.common.mail.MailFTP";
	private static final String  targetMethodName = "sendMail";
	
	//esolution 2.0 
	private static final String  approvalClassName = "com.e3ps.common.mail.EmailProcessQueue";
	private static final String  approvalMethodName = "sendMailApproval";
	public static void createScheduleItem (Hashtable ht) throws WTException {
		
	    ScheduleItem item = ScheduleItem.newScheduleItem();     
        item.setQueueName ( shceduleQueueName );
	    item.setTargetClass ( targetClassName );
	            
	    // The first method argument is a String
	    PrimitiveArg arg1 = PrimitiveArg.newPrimitiveArg ();
	    arg1.setSequenceNumber (0);
	    arg1.setArgVal ( new MethodArgument (Hashtable.class, ht ) );
	    ScheduleMethodArg[] m_args = null;
        m_args = new ScheduleMethodArg[1];
        m_args[0] = arg1;
        
	    item.setTargetMethod ( targetMethodName );
	    item.setStartDate (new Timestamp (System.currentTimeMillis ()));
	    item.setToBeRun (1);
	    item.setPeriodicity (1);
	    item.setItemName ( "EMailSender" );
	    item.setItemDescription ( "EMailSender" );                      
	    
	    item = SchedulingHelper.service.addItem (item, m_args);        
	}
	
	public static void createProcessItem (HashMap hm) throws WTException {
		WTPrincipal principal = OrganizationServicesHelper.manager.getUser("wcadmin");                         
	    ProcessingQueue queue = (ProcessingQueue)QueueHelper.manager.getQueue(processQueueName, ProcessingQueue.class);        
	    
	     if( queue == null) {
	        throw new WTException(  "PLEASE MAKE THE QUEUE [" + processQueueName + "] WITH QUEUE MANAGER OF WINDCHILL!!!"  );
	    } 
	    
	    Class [] argClasses = { HashMap.class};
	    Object [] argObjects = { hm };        
	    
	    queue.addEntry(principal, targetMethodName, targetClassName,  argClasses, argObjects);
	}
	
	public static void createApprovalMailQueue(Hashtable ht) throws WTException {
		
		String ownerName = SessionHelper.manager.getPrincipal().getName();
		LOGGER.info("==========createApprovalMailQueue ==========");
		WTPrincipal principal = OrganizationServicesHelper.manager.getUser("wcadmin");    
		
		SessionHelper.manager.setAdministrator();
	    ProcessingQueue queue = (ProcessingQueue)QueueHelper.manager.getQueue(processQueueName, ProcessingQueue.class);        
	    
	    
	     if( queue == null) {
	    	 queue = QueueHelper.manager.createQueue(processQueueName);
	    	
	    	 LOGGER.info("==========createApprovalMailQueue queue = "  +queue.getName() + "::"+queue);
	    	 //queue = ProcessingQueue.newProcessingQueue (processQueueName);
	    	 //PersistenceHelper.manager.store(queue);
	        //throw new WTException(  "PLEASE MAKE THE QUEUE [" + processQueueName + "] WITH QUEUE MANAGER OF WINDCHILL!!!"  );
	    } 
	    
	     Class [] argClasses = { Hashtable.class};
		 Object [] argObjects = { ht };      
	    
	    queue.addEntry(principal, approvalMethodName,approvalClassName,  argClasses, argObjects);
	    
		SessionHelper.manager.setPrincipal(ownerName);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EmailProcessQueue.sendMailApproval(null);
	}
	
}
