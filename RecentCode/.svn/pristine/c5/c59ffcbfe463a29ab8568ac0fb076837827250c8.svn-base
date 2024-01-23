package com.e3ps.project.beans;

import java.util.List;

import com.e3ps.common.bean.AccessControlData;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.util.AccessControlUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.SearchUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.doc.E3PSDocument;
import com.e3ps.project.EOutput;
import com.e3ps.project.EProjectNode;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.OutputType;
import com.e3ps.project.OutputTypeStep;
import com.e3ps.project.TaskRoleLink;
import com.e3ps.project.service.ProjectHelper;

import wt.access.AccessControlHelper;
import wt.access.AccessPermission;
import wt.admin.AdminDomainRef;
import wt.admin.AdministrativeDomain;
import wt.admin.AdministrativeDomainHelper;
import wt.clients.folder.FolderTaskLogic;
import wt.doc.WTDocument;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.folder.Folder;
import wt.inf.container.WTContainerHelper;
import wt.inf.container.WTContainerRef;
import wt.org.WTPrincipal;
import wt.org.WTUser;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.vc.VersionControlHelper;

public class ProjectOutputData {
	private String oid;
	private String name;
	private String docType;
	private String docTypeDisplay;
	private String description;
	private String location;
	private String foid;
	
	private String taskOid;
	private String taskName;
	private String roleName;
	
	private String pjtOid;
	private String pjtNumber;
	private String pjtName;
	
	private String stepOid;
	private String stepCode;
	private String stepName;
	
	private String docOid;
	private String docNumber;
	private String docName;
	private String docState;
	private String docStateName;
	private String docVersion;
	private String docCreateDate;
	private String docCreator;
	private String docCreatorFullName;
	private String docLocation;
	private String docFoid;
	private String lastDocVersion;
	
	public ProjectOutputData(EOutput output) throws Exception {
		this.oid = CommonUtil.getOIDString(output);
		this.name = output.getName();
		this.docType = output.getDocType();
		OutputType outputType = OutputType.toOutputType(this.docType);
		if(outputType != null) {
			this.docTypeDisplay = outputType.getDisplay();
		}
		this.description = StringUtil.checkNull(output.getDescription());
		this.location = StringUtil.checkNull(output.getLocation()).replace("/Default", "");
		
		if(output.getLocation() != null && !(output.getLocation().isEmpty())) {
			Folder folder = FolderTaskLogic.getFolder(output.getLocation(), WTContainerRef.newWTContainerRef(WCUtil.getPDMLinkProduct()));
			if(folder != null) {
				this.foid = CommonUtil.getOIDString(folder);
			}
		}
		
		ETaskNode task = output.getTask();
		if(task != null) {
			this.taskOid = CommonUtil.getOIDString(task);
			this.taskName = task.getName();
			this.roleName = "";
			List<TaskRoleLink> roleList = ProjectHelper.manager.getTaskRoleLinkList(task);
			for(TaskRoleLink link : roleList) {
				if(!"".equals(this.roleName)) {
					this.roleName += ", ";
				}
				this.roleName += link.getRole().getName();
			}
			
			EProjectNode project = task.getProject();
			if(project != null) {
				this.pjtOid = CommonUtil.getOIDString(project);
				this.pjtNumber = project.getCode();
				this.pjtName = project.getName();
			}
		}
		
		OutputTypeStep step = output.getStep();
		if(step != null) {
			this.stepOid = CommonUtil.getOIDString(step);
			this.stepCode = step.getCode();
			this.stepName = step.getName();
		}
		
		
		try {
			

//			WTContainerRef container_ref = WTContainerHelper.service.getByPath("/wt.inf.container.OrgContainer=worldex/wt.pdmlink.PDMLinkProduct=worldex");
//			AdministrativeDomain domain = AdministrativeDomainHelper.manager.getDomain("/Default", container_ref);
//	    	AdminDomainRef doaminref = AdminDomainRef.newAdminDomainRef(domain);
//			
//	    	WTPrincipal user = SessionHelper.getPrincipal();
//	    	
//			boolean chk = AccessControlHelper.manager.hasAccess(user, "WCTYPE|"+WTDocument.class.getName(), doaminref, null, AccessPermission.READ);
//			boolean chk2 = AccessControlHelper.manager.hasAccess(user, "WCTYPE|"+E3PSDocument.class.getName(), doaminref, null, AccessPermission.READ);
			
			boolean chk = AccessControlUtil.checkPermissionForObject(E3PSDocument.class);
			
			if(chk) {
				WTDocument doc = output.getDocument();
				if(doc != null) {
					this.docOid = CommonUtil.getOIDString(doc);
					this.docNumber = doc.getNumber();
					this.docName = doc.getName();
					this.docState = StringUtil.checkNull(doc.getLifeCycleState().toString());
					this.docStateName = StringUtil.checkNull(doc.getLifeCycleState().getDisplay());
					this.docVersion = StringUtil.checkNull(doc.getVersionIdentifier().getSeries().getValue());
					this.lastDocVersion = getLastDocumentVersion(output);
					this.docCreateDate = DateUtil.getDateString(doc.getCreateTimestamp(), "d");
					WTUser crUser = (WTUser) doc.getCreator().getObject();
					this.docCreator = StringUtil.checkNull(crUser.getName());
					this.docCreatorFullName = StringUtil.checkNull(crUser.getFullName());
					this.docLocation = StringUtil.checkNull(doc.getLocation()).replace("/Default", "");
					Folder folder = FolderTaskLogic.getFolder(doc.getLocation(), WTContainerRef.newWTContainerRef(WCUtil.getPDMLinkProduct()));
					if(folder != null) {
						this.foid = CommonUtil.getOIDString(folder);
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private String getLastDocumentVersion(EOutput output) throws Exception{
		WTDocument doc = output.getDocument();
		WTDocument last = null;
		String lastDocVersion = "";
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(WTDocument.class, true);
		qs.appendWhere(VersionControlHelper.getSearchCondition(WTDocument.class, true),new int[]{ii});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(WTDocument.class,"master>number","=",doc.getNumber()),new int[]{ii});
		SearchUtil.addLastVersionCondition(qs, WTDocument.class, ii);
		QueryResult qr = PersistenceHelper.manager.find(qs);
		if(qr.hasMoreElements()){
			Object[] o = (Object[])qr.nextElement();
			last = (WTDocument)o[0];
			lastDocVersion = StringUtil.checkNull(last.getVersionIdentifier().getSeries().getValue());
		}
		return lastDocVersion;
	}

	public String getLastDocVersion() {
		return lastDocVersion;
	}

	public void setLastDocVersion(String lastDocVersion) {
		this.lastDocVersion = lastDocVersion;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getDocTypeDisplay() {
		return docTypeDisplay;
	}

	public void setDocTypeDisplay(String docTypeDisplay) {
		this.docTypeDisplay = docTypeDisplay;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getPjtOid() {
		return pjtOid;
	}

	public void setPjtOid(String pjtOid) {
		this.pjtOid = pjtOid;
	}

	public String getPjtNumber() {
		return pjtNumber;
	}

	public void setPjtNumber(String pjtNumber) {
		this.pjtNumber = pjtNumber;
	}

	public String getPjtName() {
		return pjtName;
	}

	public void setPjtName(String pjtName) {
		this.pjtName = pjtName;
	}

	public String getStepOid() {
		return stepOid;
	}

	public void setStepOid(String stepOid) {
		this.stepOid = stepOid;
	}

	public String getStepCode() {
		return stepCode;
	}

	public void setStepCode(String stepCode) {
		this.stepCode = stepCode;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public String getDocOid() {
		return docOid;
	}

	public void setDocOid(String docOid) {
		this.docOid = docOid;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDocState() {
		return docState;
	}

	public void setDocState(String docState) {
		this.docState = docState;
	}

	public String getDocStateName() {
		return docStateName;
	}

	public void setDocStateName(String docStateName) {
		this.docStateName = docStateName;
	}

	public String getDocCreateDate() {
		return docCreateDate;
	}

	public void setDocCreateDate(String docCreateDate) {
		this.docCreateDate = docCreateDate;
	}

	public String getDocLocation() {
		return docLocation;
	}

	public void setDocLocation(String docLocation) {
		this.docLocation = docLocation;
	}

	public String getDocCreator() {
		return docCreator;
	}

	public void setDocCreator(String docCreator) {
		this.docCreator = docCreator;
	}

	public String getDocCreatorFullName() {
		return docCreatorFullName;
	}

	public void setDocCreatorFullName(String docCreatorFullName) {
		this.docCreatorFullName = docCreatorFullName;
	}

	public String getDocVersion() {
		return docVersion;
	}

	public void setDocVersion(String docVersion) {
		this.docVersion = docVersion;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getFoid() {
		return foid;
	}

	public void setFoid(String foid) {
		this.foid = foid;
	}
	
}
