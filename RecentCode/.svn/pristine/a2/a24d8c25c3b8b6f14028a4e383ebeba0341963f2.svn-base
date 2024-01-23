package com.e3ps.common.history.service;

import java.util.Map;

import com.e3ps.common.history.DownloadHistory;
import com.e3ps.common.history.LoginHistory;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.org.OrganizationServicesMgr;
import wt.org.WTUser;
import wt.pom.Transaction;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.StandardManager;
import wt.session.SessionContext;
import wt.session.SessionHelper;

@SuppressWarnings("serial")
public class StandardHistoryService extends StandardManager implements HistoryService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	public static StandardHistoryService newStandardHistoryService() throws Exception {
		final StandardHistoryService instance = new StandardHistoryService();
		instance.initialize();
		return instance;
	}
	
	@Override
	public String createDownloadHistory(Map<String, String> map) throws Exception{
		 
		 Transaction trx = new Transaction();
		 
		try{
			trx.start();
			
			DownloadHistory history = null;
			
			String dOid = "";
			String userId = "";
			String fName = "";
			String ip = "";
			
			dOid = map.get("dOid");
			userId = map.get("userId");
			fName = map.get("fname");
			ip = map.get("ip");
			
			WTUser user = OrganizationServicesMgr.getUser(userId);
			
			if( user == null){
				user = (WTUser)SessionHelper.getPrincipal();
			}
			
  			QuerySpec qs = new QuerySpec();
			int idx = qs.appendClassList(DownloadHistory.class, true);
			qs.appendWhere(new SearchCondition(DownloadHistory.class, DownloadHistory.D_OID, "=", dOid), new int[] {idx });
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(DownloadHistory.class, DownloadHistory.F_NAME, "=", fName), new int[] {idx});
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(DownloadHistory.class, "userReference.key.id", "=", CommonUtil.getOIDLongValue(user)), new int[] {idx });
			QueryResult qr = PersistenceHelper.manager.find(qs);
			
			if(qr.hasMoreElements()){
				Object o[] = (Object[])qr.nextElement();
				 history = (DownloadHistory)o[0];
				 history.setDCount(history.getDCount()+1);
				 history.setDownloadDate(DateUtil.getToDayTimestamp());
				 history.setIp(ip);
				 PersistenceHelper.manager.modify(history);
			}else{
				history = DownloadHistory.newDownloadHistory();
				history.setDCount(1);
				history.setName(user.getFullName());
				history.setId(userId);
				history.setFName(fName);
				history.setDCount(1);
				history.setDOid(dOid);
				history.setUser(user);
				history.setDownloadDate(DateUtil.getToDayTimestamp());
				history.setIp(ip);
				PersistenceHelper.manager.save(history);
			}
			trx.commit();
            trx = null;
			
		}catch(Exception e){
			e.printStackTrace();
		} finally {
           if(trx!=null){
                trx.rollback();
                trx = null;
           	}
		}
		return "";
	}
	
	@Override
	public void createLoginHistory(String id, String browser,String ip){
		SessionContext prev = SessionContext.newContext();
		Transaction trx = new Transaction();
		
		LoginHistory history = null;
		try{
			trx.start();
			
			SessionHelper.manager.setAdministrator();
			
			WTUser user = OrganizationServicesMgr.getUser(id);
			
			if(user != null) {
				history = LoginHistory.newLoginHistory();
				history.setConTime(DateUtil.getCurrentDateString("a"));
				history.setId(user.getName());
				history.setName(user.getFullName());
				history.setBrowser(browser);
				history.setIp(ip);
				
				PersistenceHelper.manager.save(history);
			}
			
			trx.commit();
			trx = null;
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if(trx != null) trx.rollback();
			SessionContext.setContext(prev);
		}
		
		
	}

}
