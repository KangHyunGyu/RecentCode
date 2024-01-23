package com.e3ps.project.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.project.EProject;
import com.e3ps.project.ETask;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.beans.IssueData;
import com.e3ps.project.beans.ProjectQuery;
import com.e3ps.project.issue.IssueRequest;
import com.e3ps.project.key.ProjectKey.STATEKEY;
import com.e3ps.project.util.IssueMailForm;
import com.e3ps.queue.E3PSQueueHelper;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.pdmlink.PDMLinkProduct;
import wt.pds.StatementSpec;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.ServiceFactory;

public class IssueHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PROJECT.getName());
	public static final IssueService service = ServiceFactory.getService(IssueService.class);
	public static final IssueHelper manager = new IssueHelper();
	
	public List<IssueData> getMyIssueList(Map<String, Object> reqMap) throws Exception {
		
		List<IssueData> list = new ArrayList<>();
		QuerySpec query = ProjectQuery.manager.getMyIssue(reqMap);
		
		QueryResult result = PersistenceHelper.manager.find((StatementSpec)query);
		while(result.hasMoreElements()){
			Object[] o = (Object[]) result.nextElement();
			IssueRequest issue = (IssueRequest)o[0];
			IssueData data = new IssueData(issue);
			
			list.add(data);
		}
		return list;
	}
	
	public List<IssueData> getIssueList(Map<String, Object> reqMap) throws Exception {
		
		List<IssueData> list = new ArrayList<>();
		QuerySpec query = ProjectQuery.manager.getIssueList(reqMap);
		
		QueryResult result = PersistenceHelper.manager.find((StatementSpec)query);
		while(result.hasMoreElements()){
			Object[] o = (Object[]) result.nextElement();
			IssueRequest issue = (IssueRequest)o[0];
			IssueData data = new IssueData(issue);
			
			list.add(data);
		}
		
		return list;
	}

	public List<IssueData> getIssueRequestList(Map<String, Object> reqMap) throws Exception {
		
		List<IssueData> list = new ArrayList<>();
		
		QuerySpec query = getIssueRequestListQuery(reqMap);
		
		QueryResult result = PersistenceHelper.manager.find((StatementSpec)query);
		System.out.println("query >>> " + query);
		System.out.println("size >>> " + result.size());
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			IssueRequest issue = (IssueRequest)obj[0];
			
			IssueData idata = new IssueData(issue);
			
			list.add(idata);
		}
		
		return list;
	}
	
	public QuerySpec getIssueRequestListQuery(Map<String, Object> reqMap) throws Exception {
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		ReferenceFactory rf = new ReferenceFactory();
		ETaskNode node = (ETaskNode)rf.getReference(oid).getObject();
		
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(IssueRequest.class,true);
		qs.appendWhere(new SearchCondition(IssueRequest.class,"taskReference.key.id","=",node.getPersistInfo().getObjectIdentifier().getId()),new int[]{ii});
		qs.appendOrderBy(new OrderBy(new ClassAttribute(IssueRequest.class,"thePersistInfo.createStamp"),true),new int[]{ii});

		return qs;
	}
	
	public void delayIssueMailSchedule() throws Exception {
		Timestamp delayStamp = DateUtil.convertDate(DateUtil.getDateString(DateUtil.getDelayTime(DateUtil.getToDayTimestamp(), -1), "d"));
		QuerySpec qs = new QuerySpec();
		
		int ll = qs.addClassList(IssueRequest.class,true);
		int kk = qs.addClassList(ETask.class,false);
		int ii = qs.addClassList(EProject.class,false);
		int jj = qs.addClassList(PDMLinkProduct.class,false);
		qs.appendWhere(new SearchCondition(ETask.class,"thePersistInfo.theObjectIdentifier.id",IssueRequest.class,"taskReference.key.id"),new int[]{kk,ll});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(ETask.class,"projectReference.key.id",EProject.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{kk,ii});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(EProject.class,"productReference.key.id",PDMLinkProduct.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{ii,jj});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(EProject.class,"lastVersion",SearchCondition.IS_TRUE),new int[]{ii});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(EProject.class, "state.state", SearchCondition.EQUAL, STATEKEY.PROGRESS), new int[] { ii });
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(IssueRequest.class, IssueRequest.DEAD_LINE, SearchCondition.EQUAL, delayStamp), new int[] {ll});
		
		QueryResult qr = PersistenceHelper.manager.find((StatementSpec) qs);
		while(qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			IssueRequest req = (IssueRequest) obj[0];
			Hashtable<String, Object> mailHash = IssueMailForm.setIssueDelayMailInfo(req); // 지연 업무 메일 폼
			//Hashtable<String, Object> mailHash = IssueMailForm.setIssueDeadlineMailInfo(req); // 마감일 도래 업무 메일폼
			if(mailHash.size() > 0 ) {
				mailHash.put(E3PSQueueHelper.queueName, E3PSQueueHelper.QueueName_Mail);
				mailHash.put(E3PSQueueHelper.className, E3PSQueueHelper.ClassName_Mail);
				mailHash.put(E3PSQueueHelper.methodName, E3PSQueueHelper.MethodName_Issue_Assign_Mail);
				E3PSQueueHelper.manager.createQueue(mailHash);
			}
		}
		
	}
}
