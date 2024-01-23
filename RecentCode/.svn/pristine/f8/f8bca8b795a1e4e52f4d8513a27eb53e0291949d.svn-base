package com.e3ps.project.beans;

import org.springframework.beans.BeanUtils;

import com.e3ps.common.bean.RevisionData;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.epm.service.EpmHelper;
import com.e3ps.part.service.PartHelper;
import com.e3ps.project.EProjectMasteredLink;

import wt.enterprise.RevisionControlled;
import wt.epm.EPMDocument;
import wt.epm.EPMDocumentMaster;
import wt.part.WTPart;
import wt.part.WTPartMaster;
import wt.vc.Mastered;

public class ProjectMasteredLinkData {
	
	private String linkOid;
	
	private String oid;
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
	
	private String modifyDate; // modifyDate
	private String modifyDateFormat; // modifyDate(YYYY-MM-DD)
	
	public ProjectMasteredLinkData(EProjectMasteredLink link) throws Exception {

		this.linkOid = CommonUtil.getOIDString(link);
		
		Mastered mastered = link.getTarget();
		
		String number = "";
		RevisionControlled revision = null;
		if(mastered instanceof WTPartMaster) {
			WTPartMaster partMaster = (WTPartMaster) mastered;
			WTPart part = PartHelper.service.getLastWTPart(partMaster);
			revision = (RevisionControlled) part;
			number = partMaster.getNumber();
		} else if(mastered instanceof EPMDocumentMaster) {
			EPMDocumentMaster epmMaster = (EPMDocumentMaster)mastered;
			number = epmMaster.getNumber();
			EPMDocument epm = EpmHelper.service.getLastEPMDocument(number);
			revision = (RevisionControlled) epm;
		}
		RevisionData revisionData = new RevisionData(revision);
		
		BeanUtils.copyProperties(revisionData, this);
		this.number = number;
	}

	public String getLinkOid() {
		return linkOid;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public void setLinkOid(String linkOid) {
		this.linkOid = linkOid;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
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

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
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

	public String getRev() {
		return rev;
	}

	public void setRev(String rev) {
		this.rev = rev;
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

	public String getCreatorDeptName() {
		return creatorDeptName;
	}

	public void setCreatorDeptName(String creatorDeptName) {
		this.creatorDeptName = creatorDeptName;
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
}
