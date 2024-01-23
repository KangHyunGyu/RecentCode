package com.e3ps.epm.bean;

import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.CommonUtil;
import wt.epm.EPMDocument;
import wt.org.WTUser;
import wt.part.WTPart;

public class EpmPartStateData {

	private String epmOid;
	private String epmNumber;
	private String epmName;
	private String epmRev;
	private String epmState;
	private String epmStateName;
	private String epmCreator;
	private String epmCreatorFullName;
	
	private String partOid;
	private String partNumber;
	private String partName;
	private String partRev;
	private String partState;
	private String partStateName;
	private String partCreator;
	private String partCreatorFullName;
	
	public EpmPartStateData(EPMDocument epm, WTPart part) throws Exception {
		
		if(epm != null) {
			this.epmOid = CommonUtil.getOIDString(epm);
			this.epmNumber = epm.getNumber();
			this.epmName = epm.getName();
			this.epmRev = epm.getVersionIdentifier().getSeries().getValue();
			this.epmState = epm.getLifeCycleState().toString();
			this.epmStateName = epm.getLifeCycleState().getDisplay(MessageUtil.getLocale());
			WTUser crUser = (WTUser) part.getCreator().getObject();
			this.epmCreator = crUser.getName();
			this.epmCreatorFullName = crUser.getFullName();
		}
		
		if(part != null) {
			this.partOid = CommonUtil.getOIDString(part);
			this.partNumber = part.getNumber();
			this.partName = part.getName();
			this.partRev = part.getVersionIdentifier().getSeries().getValue();
			this.partState = part.getLifeCycleState().toString();
			this.partStateName = part.getLifeCycleState().getDisplay(MessageUtil.getLocale());
			WTUser crUser = (WTUser) part.getCreator().getObject();
			this.partCreator = crUser.getName();
			this.partCreatorFullName = crUser.getFullName();
		}
	}

	public String getEpmOid() {
		return epmOid;
	}

	public void setEpmOid(String epmOid) {
		this.epmOid = epmOid;
	}

	public String getEpmNumber() {
		return epmNumber;
	}

	public void setEpmNumber(String epmNumber) {
		this.epmNumber = epmNumber;
	}

	public String getEpmName() {
		return epmName;
	}

	public void setEpmName(String epmName) {
		this.epmName = epmName;
	}

	public String getEpmRev() {
		return epmRev;
	}

	public void setEpmRev(String epmRev) {
		this.epmRev = epmRev;
	}

	public String getEpmState() {
		return epmState;
	}

	public void setEpmState(String epmState) {
		this.epmState = epmState;
	}

	public String getEpmStateName() {
		return epmStateName;
	}

	public void setEpmStateName(String epmStateName) {
		this.epmStateName = epmStateName;
	}

	public String getEpmCreator() {
		return epmCreator;
	}

	public void setEpmCreator(String epmCreator) {
		this.epmCreator = epmCreator;
	}

	public String getEpmCreatorFullName() {
		return epmCreatorFullName;
	}

	public void setEpmCreatorFullName(String epmCreatorFullName) {
		this.epmCreatorFullName = epmCreatorFullName;
	}

	public String getPartOid() {
		return partOid;
	}

	public void setPartOid(String partOid) {
		this.partOid = partOid;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getPartRev() {
		return partRev;
	}

	public void setPartRev(String partRev) {
		this.partRev = partRev;
	}

	public String getPartState() {
		return partState;
	}

	public void setPartState(String partState) {
		this.partState = partState;
	}

	public String getPartStateName() {
		return partStateName;
	}

	public void setPartStateName(String partStateName) {
		this.partStateName = partStateName;
	}

	public String getPartCreator() {
		return partCreator;
	}

	public void setPartCreator(String partCreator) {
		this.partCreator = partCreator;
	}

	public String getPartCreatorFullName() {
		return partCreatorFullName;
	}

	public void setPartCreatorFullName(String partCreatorFullName) {
		this.partCreatorFullName = partCreatorFullName;
	}
	
}
