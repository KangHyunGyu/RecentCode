package com.e3ps.common.bean;


import com.e3ps.common.content.util.ContentUtil;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.ObjectUtil;
import com.e3ps.common.util.StringUtil;

import wt.content.ContentHolder;
import wt.doc.WTDocument;
import wt.enterprise.BasicTemplateProcessor;
import wt.enterprise.RevisionControlled;
import wt.epm.EPMDocument;
import wt.inf.container.WTContainer;
import wt.org.WTPrincipalReference;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.session.SessionHelper;

public class RevisionData extends AccessControlData {
	 
	private String oid; // oid
	private String container;
	private String relateType;
	private String relateTypeName;
	private String number;
	private String name; // revision name
	
	private String location; // folder path
	private String foid; // folder oid
	
	private String state; // state(session locale)
	private String stateName;

	private String revision; 	// revision		( A )
	private String version; 	// version 		( A.1 (Design) ) 
	private String iteration; 	// iteration	( 1 )
	private String rev;			// version  	( A.1 ) 

	private String creator; // creator
	private String creatorFullName; // creatorFullName
	private String creatorDeptName; // creatorDeptName
	
	private String modifier; // modifier
	private String modifierFullName; // modifierFullName
	
	private String createDate; // createDate
	private String createDateFormat; // createDate(YYYY-MM-DD)
	private String createDateToMinute; // createDate(YYYY-MM-DD HH:MI)
	
	private String modifyDate; // modifyDate
	private String modifyDateFormat; // modifyDate(YYYY-MM-DD)
	private String modifyDateToMinute; // modifyDate(YYYY-MM-DD HH:MI)
	
	private String icon;
	private String checkoutState;
	private String checkoutStateName;
	
	private String primaryURL;
	private String fileName;
	private String fileSize;
	private String fileIcon;
	
	private String iterationNote;
	
	private boolean isSelect = true;
	
	
	private boolean isModify;
	private boolean isRevise;
	private boolean isDelete;
	private boolean isWithdrawn;
	private boolean isModifyAttribute;
	private boolean isRecall;
	
	
	private WTContainer continer;
	private String containerName;

	//hblee: DocumentData에서 RevisionData 상속 목적
	public RevisionData() throws Exception{
		
	}
	
	public RevisionData(String oid) throws Exception {
		super(oid);
		RevisionControlled revision = (RevisionControlled) CommonUtil.getObject(oid);
		
		_RevisionData(revision);
	}
	
	public RevisionData(RevisionControlled revision) throws Exception {
		super(revision);
		_RevisionData(revision);
	}
	
	public void _RevisionData(RevisionControlled revision) throws Exception {
		this.oid = CommonUtil.getOIDString(revision);
				
		this.name = StringUtil.checkNull(revision.getName());
		this.location = StringUtil.checkNull(revision.getLocation()).replace("/Default", "");
		
		this.state = StringUtil.checkNull(revision.getLifeCycleState().toString());
		this.stateName = StringUtil.checkNull(revision.getLifeCycleState().getDisplay(MessageUtil.getLocale()));
		this.revision = StringUtil.checkNull(revision.getVersionIdentifier().getSeries().getValue());
		this.version = StringUtil.checkNull(revision.getIterationDisplayIdentifier().toString());
		this.iteration = StringUtil.checkNull(revision.getIterationIdentifier().getSeries().getValue());
		this.rev = StringUtil.checkNull(this.revision + "." + this.iteration);
		
		WTUser crUser = (WTUser) revision.getCreator().getObject();
		this.creator = StringUtil.checkNull(crUser.getName());
		//PeopleData pData = new PeopleData(this.creator);
		this.creatorFullName = StringUtil.checkNull(crUser.getFullName());
		//this.creatorDeptName = pData.getDepartmentName();

		WTUser moUser = (WTUser) revision.getModifier().getObject();
		this.modifier = StringUtil.checkNull(moUser.getName());
		this.modifierFullName = StringUtil.checkNull(moUser.getFullName());
		
		this.createDate = DateUtil.getDateString(revision.getPersistInfo().getCreateStamp(), "all");
		this.createDateFormat = DateUtil.getDateString(revision.getPersistInfo().getCreateStamp(), "d");
		this.createDateToMinute = DateUtil.getTimeFormat(revision.getPersistInfo().getCreateStamp(), "yyyy-MM-dd HH:mm");
		
		this.modifyDate = DateUtil.getDateString(revision.getPersistInfo().getModifyStamp(), "all");
		this.modifyDateFormat = DateUtil.getDateString(revision.getPersistInfo().getModifyStamp(), "d");
		this.modifyDateToMinute = DateUtil.getTimeFormat(revision.getPersistInfo().getModifyStamp(), "yyyy-MM-dd HH:mm");
		
		this.icon = BasicTemplateProcessor.getObjectIconImgTag(revision);
		this.checkoutState = revision.getCheckoutInfo().getState().toString();
		this.checkoutStateName = revision.getCheckoutInfo().getState().getDisplay(MessageUtil.getLocale());
				
		this.iterationNote = StringUtil.checkNull(revision.getIterationNote());
		this.iterationNote = iterationNote.replace("undefined", "");
		
		if(revision instanceof WTPart) {
			
			WTPart object = (WTPart)revision;
			this.containerName = object.getContainer().getName();
		}else if(revision instanceof WTDocument) {
			
			WTDocument object = (WTDocument)revision;
			this.containerName = object.getContainer().getName();
			
		}else if(revision instanceof EPMDocument) {
			
			EPMDocument object = (EPMDocument)revision;
			this.containerName = object.getContainer().getName();
			
		}
		//ACL
	}
	
	//수정 : 최신 ,작업중 ,반려됨
	public boolean modifyBtn() throws Exception{
		
		RevisionControlled revision = (RevisionControlled)CommonUtil.getObject(this.oid);
		
		//최신 유무
		boolean isLastVersion = CommonUtil.isLastVersion(revision) ;
		
		boolean isModify = (super.isModify() || CommonUtil.isAdmin()) && isLastVersion;
		
		//System.out.println("super.isModify() =" + super.isModify());
		return isModify;
	}
	
	//삭제 : 최신 , 작업중 ,반려됨 
	public boolean deleteBtn() throws Exception{
		
		RevisionControlled revision = (RevisionControlled)CommonUtil.getObject(this.oid);
		boolean isLastVersion = CommonUtil.isLastVersion(revision) ;
		
		boolean isDelete = (super.isDelete() || CommonUtil.isAdmin()) && isLastVersion;
		
		
		return isDelete;
	}
	
	//개정 : 최신 , 승인됨
	public boolean reviseBtn() throws Exception{
		RevisionControlled revision = (RevisionControlled)CommonUtil.getObject(this.oid);
		boolean isLastVersion = CommonUtil.isLastVersion(revision) ;
		//this.isRevise = CommonUtil.isLastVersion(revision) && ("APPROVED".equals(this.state)) || CommonUtil.isAdmin();
		
		boolean isRevise = (super.isRevise() || CommonUtil.isAdmin() ) && isLastVersion;
		return isRevise;
	}
	
	//파일 다운로드 
	public boolean downloadBtn() throws Exception{
		
		boolean isDownload = super.isDownload() || CommonUtil.isAdmin();
		
		return isDownload;
	}
	
	//속성 수정 : 승인됨
	public boolean modifyAttributeBtn() throws Exception{
		RevisionControlled revision = (RevisionControlled)CommonUtil.getObject(this.oid);
		boolean isLastVersion = CommonUtil.isLastVersion(revision) ;
		this.isModifyAttribute = (("APPROVED".equals(this.state)) || CommonUtil.isAdmin()) && isLastVersion;
		
		return this.isModifyAttribute;
	}
	
	
	
	//폐기 : 최신 , 작업중 ,반려됨 , 작성자
	public boolean withdrawnBtn() throws Exception{
		
		RevisionControlled revision = (RevisionControlled)CommonUtil.getObject(this.oid);
		WTPrincipalReference owner = SessionHelper.manager.getPrincipalReference();
		this.isWithdrawn = CommonUtil.isLastVersion(revision) && "RETURN".equals(this.state) && owner.equals(revision.getCreator()) || CommonUtil.isAdmin();
		
		return this.isWithdrawn;
	}
	
	//회수 : 승인중, 작성자, 결재 마스터 있는지 여부
	public boolean recallBtn() throws Exception{
		
		return  true;
	}
		
	public void userDepartName() throws Exception{
		/*
		RevisionControlled revision = (RevisionControlled) CommonUtil.getObject(this.oid);
		WTPrincipalReference user = revision.getCreator();
		
		PeopleData pData = new PeopleData(user);
		this.creatorDeptName = pData.getDepartmentName();
		*/
	}
	
	public void primaryFile(){
		
		try{
			RevisionControlled rc = (RevisionControlled)CommonUtil.getObject(this.oid);
			
			this.fileName = ContentUtil.getFileName((ContentHolder)rc);
			this.primaryURL = ContentUtil.getPrimaryUrl((ContentHolder)rc);
			this.fileSize = ContentUtil.getFileSize((ContentHolder)rc);
			this.fileIcon = ContentUtil.getFileIconStr((ContentHolder)rc);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public String lastVersionOid() throws Exception{
		RevisionControlled revision = (RevisionControlled) CommonUtil.getObject(this.oid);
		
		return CommonUtil.getOIDString(ObjectUtil.getLatestVersion(revision));
	}
	
	public boolean lastVersionBtn() throws Exception{
		RevisionControlled revision = (RevisionControlled) CommonUtil.getObject(this.oid);
		
		return CommonUtil.isLastVersion(revision);
	}
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getIteration() {
		return iteration;
	}

	public void setIteration(String iteration) {
		this.iteration = iteration;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatorFullName() {
		return creatorFullName;
	}

	public void setCreatorFullName(String creatorFullName) {
		this.creatorFullName = creatorFullName;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getModifierFullName() {
		return modifierFullName;
	}

	public void setModifierFullName(String modifierFullName) {
		this.modifierFullName = modifierFullName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateDateFormat() {
		return createDateFormat;
	}

	public void setCreateDateFormat(String createDateFormat) {
		this.createDateFormat = createDateFormat;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getModifyDateFormat() {
		return modifyDateFormat;
	}

	public void setModifyDateFormat(String modifyDateFormat) {
		this.modifyDateFormat = modifyDateFormat;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getCheckoutState() {
		return checkoutState;
	}

	public void setCheckoutState(String checkoutState) {
		this.checkoutState = checkoutState;
	}

	public String getPrimaryURL() {
		return primaryURL;
	}

	public void setPrimaryURL(String primaryURL) {
		this.primaryURL = primaryURL;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileIcon() {
		return fileIcon;
	}

	public void setFileIcon(String fileIcon) {
		this.fileIcon = fileIcon;
	}

	public String getRev() {
		return rev;
	}

	public void setRev(String rev) {
		this.rev = rev;
	}

	public String getIterationNote() {
		return iterationNote;
	}

	public void setIterationNote(String iterationNote) {
		this.iterationNote = iterationNote;
	}

	public String getCreatorDeptName() {
		return creatorDeptName;
	}

	public void setCreatorDeptName(String creatorDeptName) {
		this.creatorDeptName = creatorDeptName;
	}
	
	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public String getCheckoutStateName() {
		return checkoutStateName;
	}

	public void setCheckoutStateName(String checkoutStateName) {
		this.checkoutStateName = checkoutStateName;
	}

	public String getCreateDateToMinute() {
		return createDateToMinute;
	}

	public void setCreateDateToMinute(String createDateToMinute) {
		this.createDateToMinute = createDateToMinute;
	}

	public String getModifyDateToMinute() {
		return modifyDateToMinute;
	}

	public void setModifyDateToMinute(String modifyDateToMinute) {
		this.modifyDateToMinute = modifyDateToMinute;
	}

	public String getRelateType() {
		return relateType;
	}

	public void setRelateType(String relateType) {
		this.relateType = relateType;
	}

	public String getRelateTypeName() {
		return relateTypeName;
	}

	public void setRelateTypeName(String relateTypeName) {
		this.relateTypeName = relateTypeName;
	}

	public boolean isModify() {
		return isModify;
	}

	public void setModify(boolean isModify) {
		this.isModify = isModify;
	}

	public boolean isRevise() {
		return isRevise;
	}

	public void setRevise(boolean isRevise) {
		this.isRevise = isRevise;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public boolean isWithdrawn() {
		return isWithdrawn;
	}

	public void setWithdrawn(boolean isWithdrawn) {
		this.isWithdrawn = isWithdrawn;
	}

	public boolean isModifyAttribute() {
		return isModifyAttribute;
	}

	public void setModifyAttribute(boolean isModifyAttribute) {
		this.isModifyAttribute = isModifyAttribute;
	}

	public boolean isRecall() {
		return isRecall;
	}

	public void setRecall(boolean isRecall) {
		this.isRecall = isRecall;
	}

	public String getContainer() {
		return container;
	}

	public void setContainer(String container) {
		this.container = container;
	}

	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	
	
	
}
