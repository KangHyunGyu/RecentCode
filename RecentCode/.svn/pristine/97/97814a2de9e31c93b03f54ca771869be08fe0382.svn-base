package com.e3ps.distribute.bean;

import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.distribute.DistributeDocument;
import com.e3ps.distribute.DistributeRegPartToEpmLink;
import com.e3ps.distribute.DistributeRegToPartLink;
import com.e3ps.distribute.DistributeRegistration;
import com.e3ps.distribute.DistributeToPartLink;
import com.e3ps.epm.service.EpmHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;

import wt.enterprise.BasicTemplateProcessor;
import wt.epm.EPMDocument;
import wt.epm.EPMDocumentMaster;
import wt.part.WTPart;

public class DistributeRegToPartData {
	
	private String icon; 					// 배포 품목 아이콘
	private String distPartNumber; 	    	// 배포 품목 번호
	private String distPartName; 			// 배포 품목 명
	private String version; 			    // 배포 품목 버전
	private String stateName; 				// 배포 품목 상태
	private String epm_no; 					// 배포 도면 Number

	private String oid; 					// 배포 품목 링크 OID
	private String distPartOid; 			// 배포 품목 OID

	private DistributeRegistration distrbuteReg;   // 배포 객체
	private DistributeRegToPartLink link;
	
	public DistributeRegToPartData(DistributeRegToPartLink link) throws Exception {
		super();

		this.oid = CommonUtil.getOIDString(link);
		this.distrbuteReg = link.getDistributeReg();
		this.link = link;
		WTPart distPart = link.getPart();
		this.epm_no = distPart.getNumber();

		if(distPart != null) {
			this.icon = BasicTemplateProcessor.getObjectIconImgTag(distPart);
			this.distPartOid = CommonUtil.getOIDString(distPart);
			this.distPartNumber = StringUtil.checkNull(distPart.getNumber());
			this.distPartName = StringUtil.checkNull(distPart.getName());
			this.version = StringUtil.checkNull(distPart.getVersionIdentifier().getValue() + "." + distPart.getIterationIdentifier().getValue());
			this.stateName = StringUtil.checkNull(distPart.getLifeCycleState().getDisplay(MessageUtil.getLocale()));
		}
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDistPartNumber() {
		return distPartNumber;
	}

	public void setDistPartNumber(String distPartNumber) {
		this.distPartNumber = distPartNumber;
	}

	public String getDistPartName() {
		return distPartName;
	}

	public void setDistPartName(String distPartName) {
		this.distPartName = distPartName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getDistPartOid() {
		return distPartOid;
	}

	public void setDistPartOid(String distPartOid) {
		this.distPartOid = distPartOid;
	}

	@JsonIgnore
	public DistributeRegToPartLink getLink() {
		return link;
	}

	public void setLink(DistributeRegToPartLink link) {
		this.link = link;
	}

	public String getEpm_no() {
		return epm_no;
	}

	public void setEpm_no(String epm_no) {
		this.epm_no = epm_no;
	}
	
	@JsonIgnore
	public DistributeRegistration getDistrbuteReg() {
		return distrbuteReg;
	}

	public void setDistrbuteReg(DistributeRegistration distrbuteReg) {
		this.distrbuteReg = distrbuteReg;
	}
	
	
}
