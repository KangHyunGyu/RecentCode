package com.e3ps.epm.bean;

import java.sql.Timestamp;
import java.util.Locale;

import com.e3ps.approval.service.ApprovalHelper;
import com.e3ps.common.bean.RevisionData;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.content.util.ContentUtil;
import com.e3ps.common.iba.IBAUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.epm.dnc.CadAttributeDNC;
import com.e3ps.epm.service.EpmHelper;
import com.e3ps.epm.util.EpmUtil;
import com.e3ps.part.service.PartHelper;
import com.ptc.wvs.server.ui.UIHelper;

import wt.enterprise.BasicTemplateProcessor;
import wt.epm.EPMDocument;
import wt.inf.container.WTContainer;
import wt.org.WTUser;
import wt.part.WTPart;

public class EpmData extends RevisionData{

	private EPMDocument epm;
	private WTPart part;
	
	private String number; // epm number
	private String icon;
	private String description;
	private String cadDivision;
	private String cadType;
	
	private boolean isDrawing = false;
	private boolean isWGM = false;
	private boolean isDis = false;		// 배포 도면 대상 여부
	private boolean isChagePartNoBtn = false;
	
	private Timestamp downloadDeadline;		// 다운로드 기한
	
	private String container;	//container
	private String publishURL;
	private String minPublishURL;
	private String refOwnerPartNumber;
	private String refOwnerPartVer;
	private String refOwnerPartOid;
	private String lastApprover;
	
	public EpmData(String oid) throws Exception {
		super(oid);
		
		EPMDocument epm = (EPMDocument) CommonUtil.getObject(oid);
		
		_EpmData(epm);
	}
	
	public EpmData(EPMDocument epm) throws Exception {
		super(epm);
		
		_EpmData(epm);
	}
	
	public void _EpmData(EPMDocument epm) throws Exception{
		
		this.epm = epm;
		this.number = StringUtil.checkNull(epm.getNumber());
	    this.icon = BasicTemplateProcessor.getObjectIconImgTag(epm);
	    this.description = epm.getDescription();
	    this.cadDivision = epm.getAuthoringApplication().getDisplay();
	    this.cadType = epm.getDocType().getDisplay();
	    this.isDrawing = epm.getDocType().toString().equals("CADDRAWING") && epm.getAuthoringApplication().toString().equals("SOLIDWORKS");
	    this.isWGM = EpmUtil.isWGM(this.epm);
	    if("CADDRAWING".equals(epm.getDocType().toString())) {
			EPMDocument epm3D = EpmHelper.manager.get2DTo3DEPM(epm);
			this.part = PartHelper.manager.getWTPart(epm3D);
		}
	    this.refOwnerPartNumber = partName();
	    this.refOwnerPartVer = partVer();
	    this.refOwnerPartOid = partOid();
	    String[] sss = UIHelper.getDefaultVisualizationData(CommonUtil.getOIDString(epm), false, Locale.KOREA);
		String url = sss[17];
		if(epm != null) {
			if (sss[17].length() == 0) {
				this.publishURL = "javascript:void(openCreoViewWVSPopup('" + CommonUtil.getOIDString(epm) + "'))";
				this.minPublishURL = CommonUtil.getOIDString(epm);
			} else {
				String[] count = url.split(" ");
				if (count.length > 6 ) {
	
					String third = count[2];
					String fourth = count[3];
					String fifth = count[4];
					String sixth = count[5];
					String sum = third + fourth + fifth + sixth;
					this.publishURL = sum.substring(6, sum.length() - 6);
					this.minPublishURL = third.substring(38, third.length() - 2);
				}
			}
		}
		WTUser lastAppUser = this.part!= null?ApprovalHelper.manager.getLastApprover(this.part):null;
		this.lastApprover = lastAppUser!=null?lastAppUser.getFullName():"";
	}
	
	public boolean isDis() throws Exception{
		//this.isDis = "APPROVED".equals(epm.getState().toString()) && !this.isWGM && ownerPart() == null; // 승인됨 , MANUAL - 부품 존재 X
		//this.isDis = "APPROVED".equals(epm.getState().toString()) && ownerPart() == null; // 승인됨  - 부품 존재 X
		this.isDis = true;//"APPROVED".equals(epm.getState().toString()); // 승인됨 (tsuam 수정)
		return this.isDis;
	}
	
	/**
	 * 
	 * @desc	: 주파트
	 * @author	: tsuam
	 * @date	: 2019. 7. 22.
	 * @method	: ownerPart
	 * @return	: WTPart
	 * @return
	 * @throws Exception
	 */
	public WTPart ownerPart() throws Exception{
		if(this.part == null) {
			this.part = PartHelper.manager.getWTPart(this.epm);
		}
		
		return this.part;
	}

	public String getThumbnail() {
		String thumbnail = EpmUtil.getThumbnail(this.epm);
		
		return thumbnail;
	}

	//연관 파트 OID
	public String partOid() throws Exception{
		String pOid = "";
		if(this.part != null) {
			pOid = CommonUtil.getOIDString(this.part);
		}else {
			this.part = PartHelper.manager.getWTPart(epm);
			if(this.part != null) {
				pOid = CommonUtil.getOIDString(this.part);
			}
		}
		
		return pOid;
	}
	
	//연관 파트 OID
	public String partVer() throws Exception{
		String pVer = "";
		if(this.part != null) {
			pVer = this.part.getVersionIdentifier().getValue() +"." +this.part.getIterationIdentifier().getValue();
		}else {
			this.part = PartHelper.manager.getWTPart(epm);
			if(this.part != null) {
				pVer = this.part.getVersionIdentifier().getValue() +"." +this.part.getIterationIdentifier().getValue();
			}
		}
			
		return pVer;
	}
	
	//연관 파트 번호
	public String partNum() throws Exception{
		String pNum = "";
		if(this.part != null) {
			pNum = this.part.getNumber();
		}else {
			this.part = PartHelper.manager.getWTPart(epm);
			if(this.part != null) {
				pNum = this.part.getNumber();
			}
		}

		return pNum;
	}
	//연관 파트 이름
	public String partName() throws Exception{
		String pName = "";
		if(this.part != null) {
			pName = this.part.getName();
		}else {
			this.part = PartHelper.manager.getWTPart(epm);
			if(this.part != null) {
				pName = this.part.getName();
			}
		}

		return pName;
	}
	
	public boolean ChagePartNoBtn(){
		try{
			if(isWGM){
				String part_no = StringUtil.checkNull(IBAUtil.getAttrValue(this.epm, "PART_NUMBER"));
				String epmnumber = this.number.substring(0,this.number.indexOf("."));//this.number;
				
				if(!epmnumber.equals(part_no)){
					isChagePartNoBtn = true;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this.isChagePartNoBtn;
	}
	
	public String CADFile() throws Exception{

    	String url = ContentUtil.getPrimaryUrl(this.epm);
    	
    	return url;
    }
	
	public String CADName() throws Exception{

    	String cadName = this.epm.getCADName();

    	return cadName;
    }
	
	public String drawingPublishFile(){
		
		String fileDownUrl = EpmHelper.manager.getDrawingPublishListDown(this.epm);
		
		return fileDownUrl;
	}
	
	public String container() throws Exception{

    	String container = this.epm.getContainerName();
    	
    	return container;
    }
	

	public String getMinPublishURL() {
		return minPublishURL;
	}

	public void setMinPublishURL(String minPublishURL) {
		this.minPublishURL = minPublishURL;
	}

	public String getPublishURL() {
		return publishURL;
	}

	public void setPublishURL(String publishURL) {
		this.publishURL = publishURL;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getCadDivision() {
		return cadDivision;
	}

	public void setCadDivision(String cadDivision) {
		this.cadDivision = cadDivision;
	}

	public String getCadType() {
		return cadType;
	}

	public void setCadType(String cadType) {
		this.cadType = cadType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isDrawing() {
		return isDrawing;
	}

	public void setDrawing(boolean isDrawing) {
		this.isDrawing = isDrawing;
	}

	public boolean isWGM() {
		return isWGM;
	}

	public void setWGM(boolean isWGM) {
		this.isWGM = isWGM;
	}

	public Timestamp getDownloadDeadline() {
		return downloadDeadline;
	}

	public void setDownloadDeadline(Timestamp downloadDeadline) {
		this.downloadDeadline = downloadDeadline;
	}

	public String getContainer() {
		return container;
	}

	public void setContainer(String container) {
		this.container = container;
	}

	public String getRefOwnerPartNumber() {
		return refOwnerPartNumber;
	}

	public void setRefOwnerPartNumber(String refOwnerPartNumber) {
		this.refOwnerPartNumber = refOwnerPartNumber;
	}

	public String getRefOwnerPartVer() {
		return refOwnerPartVer;
	}

	public void setRefOwnerPartVer(String refOwnerPartVer) {
		this.refOwnerPartVer = refOwnerPartVer;
	}

	public String getRefOwnerPartOid() {
		return refOwnerPartOid;
	}

	public void setRefOwnerPartOid(String refOwnerPartOid) {
		this.refOwnerPartOid = refOwnerPartOid;
	}

	public String getLastApprover() {
		return lastApprover;
	}

	public void setLastApprover(String lastApprover) {
		this.lastApprover = lastApprover;
	}
	
	
}
