package com.e3ps.workspace.service;

import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Base64.Encoder;

import com.e3ps.common.content.service.CommonContentHelper;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.org.People;
import com.e3ps.org.WTUserPeopleLink;
import com.e3ps.workspace.notice.Notice;
import com.e3ps.workspace.util.PasswordChange;

import wt.content.ContentHolder;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.WTObject;
import wt.org.WTUser;
import wt.pom.Transaction;
import wt.services.StandardManager;
import wt.session.SessionHelper;
import wt.util.WTException;

@SuppressWarnings("serial")
public class StandardWorkspaceService extends StandardManager implements WorkspaceService{

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.WORKSPACE.getName());
	
	public static StandardWorkspaceService newStandardWorkspaceService() throws WTException {
		final StandardWorkspaceService instance = new StandardWorkspaceService();
		instance.initialize();
		return instance;
	}
	
	@Override
	public void createNoticeAction(Map<String, Object> reqMap) throws Exception {
		
		Notice notice = null;
		
		Transaction trx = new Transaction();
		
		try {
			trx.start();
			
			String title = StringUtil.checkNull((String) reqMap.get("title"));
			String contents = StringUtil.checkNull((String) reqMap.get("contents"));
			String notifyType = StringUtil.checkNull((String) reqMap.get("notifyType"));
			String deadline = StringUtil.checkNull((String) reqMap.get("deadline"));
			List<String> secondary = StringUtil.checkReplaceArray(reqMap.get("SECONDARY"));
			List<String> delocIds = StringUtil.checkReplaceArray(reqMap.get("delocIds"));
			
			//create new notice
			notice = Notice.newNotice();
			
			//set properties 
			notice.setTitle(title);
			notice.setContents(contents);
			notice.setOwner(SessionHelper.manager.getPrincipalReference());
			notice.setNotifyType(notifyType);
			notice.setDeadline(deadline);
			
			//save document
			notice = (Notice) PersistenceHelper.manager.save(notice);
			
			//attach files
			if(secondary != null) {
				CommonContentHelper.service.attach(notice, null, secondary, delocIds,false);
			}
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}
	
	@Override
	public void changePasswordAction(Map<String, Object> reqMap) throws Exception {
		String id = StringUtil.checkNull((String) reqMap.get("id"));
		String newPw = StringUtil.checkNull((String) reqMap.get("newPw"));

		if(newPw.length() > 0) {
			PasswordChange.changePassword(id, newPw);
			
			QueryResult qr = PersistenceHelper.manager.navigate((WTUser) wt.org.OrganizationServicesMgr.getPrincipal(id), "people", WTUserPeopleLink.class);

			if (qr.hasMoreElements()) {
				People people = (People) qr.nextElement();

				// 오늘날짜 가져오기
				Calendar cal = Calendar.getInstance();
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH) + 1;
				int date = cal.get(Calendar.DATE);

				String stringChangeDate = year + "/" + month + "/" + date;
				people.setPwChangeDate(stringChangeDate);
				
				Encoder encoder  = Base64.getEncoder();
            	byte[] targetBytes = newPw.getBytes();
            	byte[] encoded = encoder.encode(targetBytes);
				people.setPassword(new String(encoded));

				PersistenceHelper.manager.modify(people);
			}
		}
	}
	
	// 공지사항 수정 Action
	public Notice modifyNoticeAction(Map<String, Object> reqMap) throws Exception{
		Notice notice = null;
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			if(oid.length() > 0) {
				notice = (Notice) CommonUtil.getObject(oid);
				
				String title = StringUtil.checkNull((String) reqMap.get("title"));
				String notifyType = StringUtil.checkNull((String) reqMap.get("notifyType"));
				String deadline = StringUtil.checkNull((String) reqMap.get("deadline"));
				String contents = StringUtil.checkNull((String) reqMap.get("contents"));
				List<String> secondary = StringUtil.checkReplaceArray(reqMap.get("SECONDARY"));
				List<String> delocIds = StringUtil.checkReplaceArray(reqMap.get("delocIds"));
				
				notice.setTitle(title);
				notice.setContents(contents);
				notice.setNotifyType(notifyType);
				notice.setDeadline(deadline);
				
				notice = (Notice) PersistenceHelper.manager.modify(notice);
				
				//attach files
				CommonContentHelper.service.attach((ContentHolder)notice, null, secondary, delocIds,false);
			}
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		
		return notice;
	}
	
	// 공지사항 수정 Action
	public Notice modifyViewCntAction(Map<String, Object> reqMap) throws Exception{
		Notice notice = null;
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			if(oid.length() > 0) {
				notice = (Notice) CommonUtil.getObject(oid);
				
				notice.setCnt(notice.getCnt()+1);
				
				notice = (Notice) PersistenceHelper.manager.save(notice);
			}
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		
		return notice;
	}
	
	// 공지사항 삭제 Action
	@Override
	public void deleteNoticeAction(Map<String, Object> reqMap) throws Exception{
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			if(oid.length() > 0) {
				Notice notice = (Notice) CommonUtil.getObject(oid);
				
				PersistenceHelper.manager.delete(notice);
			}
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}

	@Override
	public void deleteWFItem(Persistable persistable) {
		// TODO Auto-generated method stub
		
	}
	
}
