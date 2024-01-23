package com.e3ps.project.beans;

import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.common.util.WebUtil;
import com.e3ps.project.EProjectNode;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.issue.IssueRequest;
import com.e3ps.project.issue.IssueSolution;
import com.e3ps.project.issue.SolutionLink;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.org.WTUser;


public class IssueData{
	
	private IssueRequest request;
	private IssueSolution solution;
	private EProjectNode project;
	//private ETaskNode task;
	
	private String pjtOid;
	private String taskOid;
	private String number;
	private String name;
	private String taskName;
	private String issueType;
	private String issueTypeKey;
	private String title;
	private String state;
	private String managerFullName;
	private String creatorFullName;
	private String oid;
	private String pjtName;
	private String issuePersist;
	private String createDate;
	private String requestDate;
	private String deadLine;
	
	public IssueData(IssueRequest request) throws Exception{
		this.request = request;
		this.pjtOid = WCUtil.getOid(request.getTask().getProject());
		this.taskOid = WCUtil.getOid(request.getTask());
		this.number = request.getTask().getProject().getCode();
		this.name = request.getName();
		this.taskName = request.getTask().getName();
		this.issueType = request.getIssueType();
		this.title = this.getTitle();
		this.state = this.getState();
		this.managerFullName = this.getManagerFullName();
		this.creatorFullName = this.getCreatorFullName();
		this.oid = WCUtil.getOid(request);
		this.pjtName = request.getTask().getProject().getName();
		this.issuePersist = request.getPersistInfo().getObjectIdentifier().toString();
		this.issueTypeKey = request.getIssueType();
		this.deadLine = DateUtil.getDateString(request.getDeadLine(),"d");
	}
	
	public String getTitle() throws Exception{
		
		StringBuffer result = new StringBuffer();
		
		if(request.getIssueType()!=null && request.getIssueType().length()>0){
			result.append("[");
			result.append(getIssueType());
			result.append("]");
		}
		
		result.append(request.getName());
		
		return result.toString();
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getProblem()throws Exception{
		return WebUtil.getHtml(request.getProblem());
	}
	
	public String getProjectName() throws Exception{
		return this.pjtName;
	}
	
	public String getIssueType()throws Exception{
		
		if(request.getIssueType()!=null && request.getIssueType().length()>0){
			return CodeHelper.service.getName("ISSUETYPE", request.getIssueType());
		}
		return "";
	}
	
	public String getState(){
		return request.getState();
	}
	
	public String getCreatorFullName(){
		return request.getCreator().getFullName();
	}
	
	
	public String getManagerFullName(){
		return request.getManager().getFullName();
	}
	
	public String getCreateDate(){
		return DateUtil.getDateString(request.getPersistInfo().getCreateStamp(),"d");
	}
	
	public String getRequestDate(){
		return DateUtil.getDateString(request.getRequestDate(),"d");
	}
	
	public IssueSolution Solution() throws Exception{
		if(solution!=null){
			return solution;
		}
		QueryResult qr = PersistenceHelper.manager.navigate(request, "solution", SolutionLink.class);
		if(qr.hasMoreElements()){
			return (IssueSolution)qr.nextElement();
		}
		return null;
	}
	
	public boolean isCreator(WTUser user){
		WTUser creator = request.getCreator();
		if(user.getName().equals(creator.getName())){
			return true;
		}
		return false;
	}
	
	public boolean isManager(WTUser user){
		WTUser manager = request.getManager();
		if(manager!=null && user.getName().equals(manager.getName())){
			return true;
		}
		return false;
	}
	
	public String getIssuePersist() {
		return issuePersist;
	}

	public void setIssuePersist(String issuePersist) {
		this.issuePersist = issuePersist;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPjtName() {
		return pjtName;
	}

	public void setPjtName(String pjtName) {
		this.pjtName = pjtName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public EProjectNode getProject() {
		return project;
	}

	public void setProject(EProjectNode project) {
		this.project = project;
	}

	public String getPjtOid() {
		return pjtOid;
	}

	public void setPjtOid(String pjtOid) {
		this.pjtOid = pjtOid;
	}

	public String getTaskOid() {
		return taskOid;
	}

	public void setTaskOid(String taskOid) {
		this.taskOid = taskOid;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public void setManagerFullName(String managerFullName) {
		this.managerFullName = managerFullName;
	}

	public void setCreatorFullName(String creatorFullName) {
		this.creatorFullName = creatorFullName;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

	public String getIssueTypeKey() {
		return issueTypeKey;
	}

	public void setIssueTypeKey(String issueTypeKey) {
		this.issueTypeKey = issueTypeKey;
	}

	public String getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(String deadLine) {
		this.deadLine = deadLine;
	}
	
	
}