package com.e3ps.epm.bean;

import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.epm.util.EpmUtil;

import wt.epm.EPMDocument;
import wt.epm.structure.EPMMemberLink;
import wt.org.WTUser;

public class StructureData {

	private EPMDocument epm;
	private String oid;
	
	private String number; // epm number
	private String name;
	
	private String state; // state(session locale)
	private String stateName;
	
	private String rev;			// version  	( A.1 ) 
	
	private String creator; // creator
	private String creatorFullName; // creatorFullName
	
	private String createDate; // createDate
	private String createDateFormat; // createDate(YYYY-MM-DD)
	
	private String modifier; // modifier
	private String modifierFullName; // modifierFullName
	
	private String modifyDate; // modifyDate
	private String modifyDateFormat; // modifyDate(YYYY-MM-DD)
	
	private String depType;		//종속성 유형
	private String suppressed;	//억제됨
	
	public StructureData(EPMDocument epm, EPMMemberLink link) throws Exception {
		
		_EpmData(epm, link);
	}
	
	public void _EpmData(EPMDocument epm, EPMMemberLink link) throws Exception{
		
		this.oid = CommonUtil.getOIDString(epm);
		this.number = StringUtil.checkNull(epm.getNumber());
		this.name = StringUtil.checkNull(epm.getName());
		
		this.state = StringUtil.checkNull(epm.getLifeCycleState().toString());
		this.stateName = StringUtil.checkNull(epm.getLifeCycleState().getDisplay(MessageUtil.getLocale()));
		
		String revision = StringUtil.checkNull(epm.getVersionIdentifier().getSeries().getValue());
		String iteration = StringUtil.checkNull(epm.getIterationIdentifier().getSeries().getValue());
		this.rev = StringUtil.checkNull(revision + "." + iteration);
		
		WTUser crUser = (WTUser) epm.getCreator().getObject();
		this.creator = StringUtil.checkNull(crUser.getName());
		this.creatorFullName = StringUtil.checkNull(crUser.getFullName());
		
		WTUser moUser = (WTUser) epm.getModifier().getObject();
		this.modifier = StringUtil.checkNull(moUser.getName());
		this.modifierFullName = StringUtil.checkNull(moUser.getFullName());
		
		this.createDate = DateUtil.getDateString(epm.getPersistInfo().getCreateStamp(), "all");
		this.createDateFormat = DateUtil.getDateString(epm.getPersistInfo().getCreateStamp(), "d");
		
		this.modifyDate = DateUtil.getDateString(epm.getPersistInfo().getModifyStamp(), "all");
		this.modifyDateFormat = DateUtil.getDateString(epm.getPersistInfo().getModifyStamp(), "d");
		
		//2 : 멤버
		//32768 : 표시되지 않은 멤버
		//131072 : ?
		if(link.getDepType() == 2) {
			this.depType = "멤버";
		} else if(link.getDepType() == 32768) {
			this.depType = "표시되지 않은 멤버";
		} else {
			this.depType = "";
		}
		
		if(link.isSuppressed()) {
			this.suppressed = "예";
		} else {
			this.suppressed = "아니오";
		}
		
	}
	

	public String getThumbnail() {
		String thumbnail = EpmUtil.getThumbnail(this.epm);
		
		return thumbnail;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
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

	public String getDepType() {
		return depType;
	}

	public void setDepType(String depType) {
		this.depType = depType;
	}

	public String getSuppressed() {
		return suppressed;
	}

	public void setSuppressed(String suppressed) {
		this.suppressed = suppressed;
	}
	
	
}
