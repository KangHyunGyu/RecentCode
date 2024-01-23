package com.e3ps.distribute.bean;

import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.distribute.DistributeDocument;
import com.e3ps.distribute.DistributeToPartLink;

import wt.enterprise.BasicTemplateProcessor;
import wt.part.WTPart;

public class DistributeToPartData {
	
	private String icon; 					// 배포 품목 아이콘
	private String distPartNumber; 	    	// 배포 품목 번호
	private String distPartName; 			// 배포 품목 명
	private String version; 			    // 배포 품목 버전
	private String stateName; 				// 배포 품목 상태

	private String oid; 					// 배포 품목 링크 OID
	private String distPartOid; 			// 배포 품목 OID

	private DistributeDocument distrbute;   // 배포 객체
	private DistributeToPartLink link;
	
	public DistributeToPartData(DistributeToPartLink link) throws Exception {
		super();

		this.oid = CommonUtil.getOIDString(link);
		this.distrbute = link.getDistribute();
		this.link = link;
		WTPart distPart = link.getPart();

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

	public DistributeDocument getDistrbute() {
		return distrbute;
	}

	public void setDistrbute(DistributeDocument distrbute) {
		this.distrbute = distrbute;
	}

	public DistributeToPartLink getLink() {
		return link;
	}

	public void setLink(DistributeToPartLink link) {
		this.link = link;
	}
	
	
}
