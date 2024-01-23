package com.e3ps.project.beans;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.pom.Transaction;
import wt.query.QuerySpec;
import wt.query.SearchCondition;

import com.e3ps.common.util.DateUtil;
import com.e3ps.project.EProject;
import com.e3ps.project.ETask;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.PrePostLink;
import com.e3ps.project.key.ProjectKey.STATEKEY;


public class ProjectScheduler extends TimerTask implements wt.method.RemoteAccess, java.io.Serializable{
	
	static final boolean SERVER = wt.method.RemoteMethodServer.ServerFlag;
	
	public static ProjectScheduler manager = new ProjectScheduler();
	
	private static ProjectScheduler scheduler;
	
	
	public static ProjectScheduler getInstance(){
		
		if(scheduler==null){
			scheduler = new ProjectScheduler();
		}
		return scheduler;
	}
	
	public void start(){
		try{
			
			String today = DateUtil.getCurrentDateString("d");
			Date date = DateUtil.parseDateStr(today);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE,1); 
			cal.add(Calendar.HOUR, 1);
			//cal.add(Calendar.MINUTE, 30);
			Timer timer = new Timer(true);
			timer.scheduleAtFixedRate(this, cal.getTime(), 24*60*60*1000); 
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	
	public void run() {
		
		try{
			
			setState();
			//RemoteMethodServer.getDefault().invoke("setState", null, this, null, null);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
    public void setState()throws Exception{
    	
    	//boolean bool = SessionServerHelper.manager.setAccessEnforced(false);
    	//SessionContext sessionContext = SessionContext.newContext();
    	//SessionHelper.manager.setAdministrator();
    	
    	Transaction trx = new Transaction();
    	
    	try {
    		
	    	trx.start();
	    	
			String state = STATEKEY.PROGRESS;
	
			QuerySpec qs = new QuerySpec();
	    	
	    	int idx_1 = qs.addClassList(ETask.class, true);
	    	int idx_2 = qs.addClassList(EProject.class, false);
	    	
	    	qs.appendWhere(new SearchCondition(EProject.class,EProject.LAST_VERSION,SearchCondition.IS_TRUE),new int[]{idx_2});
	    	qs.appendAnd();
	    	qs.appendWhere(new SearchCondition(EProject.class,"state.state","=",STATEKEY.PROGRESS),new int[]{idx_2});
	    	qs.appendAnd();
	    	qs.appendWhere(new SearchCondition(ETask.class,"projectReference.key.id",EProject.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{idx_1,idx_2});
	    	qs.appendAnd();
	    	qs.appendWhere(new SearchCondition(ETask.class, "status", "=", STATEKEY.READY), new int[]{idx_1});
	    	qs.appendAnd();
	    	//qs.appendWhere(new SearchCondition(ETask.class, "planStartDate", "=", DateUtil.getTimestampFormat(DateUtil.getCurrentDateString("d")+" 12:59:59", "yyyy-MM-dd HH:mm:ss") ), new int[]{idx_1});
    		qs.appendWhere(new SearchCondition(ETask.class,ETask.PLAN_START_DATE,SearchCondition.LESS_THAN, DateUtil.convertEndDate(DateUtil.getCurrentDateString("d"))),	new int[] { idx_1 });
	    	QueryResult qr = PersistenceHelper.manager.find(qs);
	    	
	    	System.out.println("############# SCH...." + qs);
	    	System.out.println("############# SCH....Task Start size = " + qr.size());
	    	
	    	while(qr.hasMoreElements()){
	    		
	    		Object o[] = (Object[])qr.nextElement();
	    		ETask task = (ETask)o[0];
	    		EProject pjt =  (EProject)task.getProject();
	    		
	    		if(STATEKEY.PROGRESS.equals(pjt.getState().toString())){
	    		
					QueryResult preQr = PersistenceHelper.manager.navigate(task,"pre",PrePostLink.class);
					
					if(preQr.size() == 0){
						
						task.setStatus(state);
			    		PersistenceHelper.manager.save(task);
			    		
			    		System.out.println("Task Name is ["+pjt.getCode()+ "] || [" +task.getName() + "] Start !");
					
					}else{
						
						int count = 0;
						
						while (preQr.hasMoreElements()) {
							
							ETaskNode preTask = (ETaskNode) preQr.nextElement();
							if(!STATEKEY.COMPLETED.equals(preTask.getStatus())) {
								count++;
							}
						}
						
						if(count == 0){
							
							task.setStatus(state);
				    		PersistenceHelper.manager.save(task);
				    		
				    		System.out.println("Task Name is ["+pjt.getCode()+ "] || [" +task.getName() + "] Start !");
				    		
						}
					}
	    		}
			}
	        trx.commit();
	        trx = null;

	   } catch(Exception e) {
	       throw e;
	   } finally {
			//SessionContext.setContext(sessionContext);
			//SessionServerHelper.manager.setAccessEnforced(bool);
	       if(trx!=null){
	            trx.rollback();
	   		}
	   }
    }
}

