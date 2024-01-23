package com.e3ps.calendar.service;

import java.util.List;
import java.util.Map;

import com.e3ps.calendar.CalendarProjectLink;
import com.e3ps.calendar.DevelopmentStage;
import com.e3ps.calendar.DevelopmentStageCalendar;
import com.e3ps.calendar.DevelopmentStageCalendarLink;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.project.EProject;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.pom.Transaction;
import wt.services.StandardManager;
import wt.util.WTException;

public class StandardDsService extends StandardManager implements DsService{
	private static final long serialVersionUID = 1L;

	public static StandardDsService newStandardDsService() throws WTException {
		StandardDsService instance = new StandardDsService();
		instance.initialize();
		return instance;
	}
	@SuppressWarnings("unchecked")
	@Override
	public DevelopmentStageCalendar createDsAction(Map<String, Object> reqMap) throws Exception {
		DevelopmentStageCalendar dsc = null;
		DevelopmentStage ds = null;
		Transaction trx = new Transaction();
		try {
			trx.start();
			String primary = StringUtil.checkNull((String) reqMap.get("PRIMARY"));
			List<String> secondary = StringUtil.checkReplaceArray(reqMap.get("SECONDARY"));
			
			String name = StringUtil.checkNull((String) reqMap.get("name"));
			String remark = StringUtil.checkNull((String) reqMap.get("remark"));
			String startDate = StringUtil.checkNull((String) reqMap.get("startDate"));
			String endDate = StringUtil.checkNull((String) reqMap.get("endDate"));
			
			if(startDate.contains("-")) {
				startDate = startDate.split(" ")[0];
				startDate = startDate.replace("-", "/");
			}
			if(endDate.contains("-")) {
				endDate = endDate.split(" ")[0];
				endDate = endDate.replace("-", "/");
			}
			dsc = DevelopmentStageCalendar.newDevelopmentStageCalendar();
			dsc.setName(name);
			dsc.setStartDate(DateUtil.convertDate(startDate));
			dsc.setEndDate(DateUtil.convertDate(endDate));
			dsc.setRemark(remark);
			dsc = (DevelopmentStageCalendar) PersistenceHelper.manager.save(dsc);
			
			List<Map<String, Object>> dsList = (List<Map<String, Object>>) reqMap.get("dsList");
			for(Map<String, Object> map : dsList) {
				int dsseq = (int) map.get("index");
				String dsname = (String) map.get("name");
				String dsstartDate = (String) map.get("startDate");
				String dsendDate = (String) map.get("endDate");
				String dsremark = (String) map.get("remark");
				if(dsstartDate.contains("-")) {
					dsstartDate = dsstartDate.split(" ")[0];
					dsstartDate = dsstartDate.replace("-", "/");
				}
				if(dsendDate.contains("-")) {
					dsendDate = dsendDate.split(" ")[0];
					dsendDate = dsendDate.replace("-", "/");
				}
				
				ds = DevelopmentStage.newDevelopmentStage();
				ds.setName(dsname);
				ds.setStartDate(DateUtil.convertDate(dsstartDate));
				ds.setEndDate(DateUtil.convertDate(dsendDate));
				ds.setRemark(dsremark);
				ds.setSeq(dsseq);
				ds.setDsc(dsc);
				ds = (DevelopmentStage) PersistenceHelper.manager.save(ds);
	    	}
			
//			CommonContentHelper.service.attach((ContentHolder)dsc, primary, secondary);

			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return dsc;
	}
	@SuppressWarnings("unchecked")
	@Override
	public DevelopmentStageCalendar modifyDsAction(Map<String, Object> reqMap) throws Exception {
		DevelopmentStageCalendar dsc = null;
		Transaction trx = new Transaction();
		try {
			trx.start();
			String primary = StringUtil.checkNull((String) reqMap.get("PRIMARY"));
			List<String> secondary = StringUtil.checkReplaceArray(reqMap.get("SECONDARY"));
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String name = StringUtil.checkNull((String) reqMap.get("name"));
			String remark = StringUtil.checkNull((String) reqMap.get("remark"));
			String startDate = StringUtil.checkNull((String) reqMap.get("startDate"));
			String endDate = StringUtil.checkNull((String) reqMap.get("endDate"));
			
			if(startDate.contains("-")) {
				startDate = startDate.split(" ")[0];
				startDate = startDate.replace("-", "/");
			}
			if(endDate.contains("-")) {
				endDate = endDate.split(" ")[0];
				endDate = endDate.replace("-", "/");
			}
			dsc = (DevelopmentStageCalendar) CommonUtil.getObject(oid);
			dsc.setName(name);
			dsc.setStartDate(DateUtil.convertDate(startDate));
			dsc.setEndDate(DateUtil.convertDate(endDate));
			dsc.setRemark(remark);
			dsc = (DevelopmentStageCalendar) PersistenceHelper.manager.modify(dsc);
			
			QueryResult qr = null;
			qr = PersistenceHelper.manager.navigate(dsc, "ds", DevelopmentStageCalendarLink.class, false);
			while (qr.hasMoreElements()) {
				DevelopmentStageCalendarLink link = (DevelopmentStageCalendarLink) qr.nextElement();
				DevelopmentStage ds = link.getDs();
				PersistenceHelper.manager.delete(ds);
			}
			
			List<Map<String, Object>> dsList = (List<Map<String, Object>>) reqMap.get("dsList");
			for(Map<String, Object> map : dsList) {
				int dsseq = (int) map.get("index");
				String dsname = (String) map.get("name");
				String dsstartDate = (String) map.get("startDate");
				String dsendDate = (String) map.get("endDate");
				String dsremark = (String) map.get("remark");
				if(dsstartDate.contains("-")) {
					dsstartDate = dsstartDate.split(" ")[0];
					dsstartDate = dsstartDate.replace("-", "/");
				}
				if(dsendDate.contains("-")) {
					dsendDate = dsendDate.split(" ")[0];
					dsendDate = dsendDate.replace("-", "/");
				}
				
				DevelopmentStage ds = DevelopmentStage.newDevelopmentStage();
				ds.setName(dsname);
				ds.setStartDate(DateUtil.convertDate(dsstartDate));
				ds.setEndDate(DateUtil.convertDate(dsendDate));
				ds.setRemark(dsremark);
				ds.setSeq(dsseq);
				ds.setDsc(dsc);
				ds = (DevelopmentStage) PersistenceHelper.manager.save(ds);
	    	}
			
//			CommonContentHelper.service.attach((ContentHolder)dsc, primary, secondary);

			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return dsc;
	}
	@Override
	public void deleteDsAction(Map<String, Object> reqMap) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			DevelopmentStageCalendar dsc  = (DevelopmentStageCalendar) CommonUtil.getObject(oid);
			
			QueryResult qr = null;
			qr = PersistenceHelper.manager.navigate(dsc, "ds", DevelopmentStageCalendarLink.class, false);
			while (qr.hasMoreElements()) {
				DevelopmentStageCalendarLink link = (DevelopmentStageCalendarLink) qr.nextElement();
				DevelopmentStage ds = link.getDs();
				PersistenceHelper.manager.delete(link);
				ds = (DevelopmentStage) PersistenceHelper.manager.refresh(ds);
				ds = (DevelopmentStage) PersistenceHelper.manager.delete(ds);
			}

			qr = PersistenceHelper.manager.navigate(dsc,"project",CalendarProjectLink.class,false);
			while(qr.hasMoreElements()){
				CalendarProjectLink link = (CalendarProjectLink)qr.nextElement();
				PersistenceHelper.manager.delete(link);
			}
			
			PersistenceHelper.manager.delete(dsc);
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}
	@Override
	public void createLinkAction(Map<String, Object> reqMap) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();
			String poid = StringUtil.checkNull((String) reqMap.get("poid"));
			String soid = StringUtil.checkNull((String) reqMap.get("soid"));
			
			EProject project = (EProject)CommonUtil.getObject(poid);
			
			DevelopmentStageCalendar dsc = (DevelopmentStageCalendar) CommonUtil.getObject(soid);
			
			CalendarProjectLink link = CalendarProjectLink.newCalendarProjectLink(dsc, project);
			
			PersistenceHelper.manager.save(link);
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		
	}
	@Override
	public void modifyLinkAction(Map<String, Object> reqMap) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();
			String poid = StringUtil.checkNull((String) reqMap.get("poid"));
			String soid = StringUtil.checkNull((String) reqMap.get("soid"));
			EProject project = (EProject)CommonUtil.getObject(poid);
			
			QueryResult qr = PersistenceHelper.manager.navigate(project,"dsc",CalendarProjectLink.class,false);
			while(qr.hasMoreElements()){
				CalendarProjectLink link = (CalendarProjectLink)qr.nextElement();
				PersistenceHelper.manager.delete(link);
			}
			
			DevelopmentStageCalendar dsc = (DevelopmentStageCalendar) CommonUtil.getObject(soid);
			CalendarProjectLink link = CalendarProjectLink.newCalendarProjectLink(dsc, project);
			PersistenceHelper.manager.save(link);
			
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		
	}
	@Override
	public void deleteLinkAction(Map<String, Object> reqMap) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();
			String poid = StringUtil.checkNull((String) reqMap.get("poid"));
			EProject project = (EProject)CommonUtil.getObject(poid);
			
			QueryResult qr = PersistenceHelper.manager.navigate(project,"dsc",CalendarProjectLink.class,false);
			while(qr.hasMoreElements()){
				CalendarProjectLink link = (CalendarProjectLink)qr.nextElement();
				PersistenceHelper.manager.delete(link);
			}
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		
	}

}
