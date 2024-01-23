package com.e3ps.part.bean;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.e3ps.approval.service.ApprovalHelper;
import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.service.ChangeECOHelper;
import com.e3ps.common.bean.RevisionData;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.iba.IBAUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.epm.dnc.CadAttributeDNC;
import com.e3ps.epm.service.EpmHelper;
import com.e3ps.epm.util.EpmUtil;
import com.e3ps.part.service.BomHelper;
import com.ptc.wvs.server.ui.UIHelper;

import wt.epm.EPMDocument;
import wt.epm.EPMDocumentMaster;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.vc.views.ViewHelper;

public class PartData extends RevisionData{

	private WTPart part;
	private EPMDocument epm;
	
	private String number; // part number
	
	private String epmOid;
	
	private String unit;
	private String unitDisplay;
	
	///
	private String material;
	private String category;
	private String treatment;
	private String assembleType;
	///
	
	//리스트 출력 IBA
	private String materialName; // 재질 명
	private String weight; // 중량
	private String upg; // upg
	private String customer_part_number; //고객품번
	private String unit_cost; // 단가
	private String finishName; // FINISH 명
	private String specialAttrName; // 특별특성 명
	private String certificationName; // 인증 및 법규 명
	private String carryOverName; // carry over 명
	private String pressure; // 허용 압력
	private String gasType; // 가스 타입
	
	private String method; // 공법
	private String specification; // 규격 및 특징
	private String description; // 비고
	private String start_company_name; // 시작업체 명
	private String start_company_manager; // 시작업체 담당자
	private String start_compan_tell; // 시작업체 연락처
	private String prod_company_name; // 양산업체 명
	private String prod_company_manager; // 양산업체 담당자
	private String prod_company_tell; // 양산업체 연락처
	private String start_unit_cost; // 시작 단가
	private String prod_unit_cost; // 양산 단가
	
	//배포에서 사용
	private Timestamp downloadDeadline;		// 다운로드 기한
	private String prUser;
	private String prDeptName;
	private String purchase_state;
	private String pjtNo;
	private String epmNo;
	private String epmver;
	private int	linkState;
	private String linkStateStr;
	private String distNo;
	private String supplierNames; //업체명
	private String requestNames;  //배포자
	private boolean lastCheck;  //배포자
	
	public String epm3DName;
	public String epm3DNumber;
	public String epm3DOid;
	public String epm2DName;
	public String epm2DNumber;
	public String epm2DOid;
	
	private Map<String, Object> attributes;
	
	private String publishURL;
	private String minPublishURL;
	private String lastApprover;
	
	private Map<String, String> bomEndItemHash;//BOM END ITEM(K:oid, V:number)
	
	
	public PartData(String oid) throws Exception {
		super(oid);
		
		WTPart part = (WTPart) CommonUtil.getObject(oid);
		
		_PartData(part);
	}
	
	public PartData(WTPart part) throws Exception {
		super(part);
		
		_PartData(part);
	}
	
	public void _PartData(WTPart part) throws Exception{
		this.part = part;
		String lastOid = lastVersionOid();
		String preOid = CommonUtil.getOIDString(part);
		
		if(!lastOid.equals(preOid)) {
			this.lastCheck = false;
		} else {
			this.lastCheck = true;
		}
		ownerEpm();
		this.number = StringUtil.checkNull(part.getNumber());
		
		this.unit = part.getDefaultUnit().toString();
		this.unitDisplay = part.getDefaultUnit().toString().toUpperCase();
		String[] sss = UIHelper.getDefaultVisualizationData(CommonUtil.getOIDString(part), false, Locale.KOREA);
		String url = sss[17];
		if(part != null) {
			if (sss[17].length() == 0) {
				this.publishURL = "javascript:void(openCreoViewWVSPopup('" + CommonUtil.getOIDString(part) + "'))";
				this.minPublishURL = CommonUtil.getOIDString(part);
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
		
		bomEndItemHash = new HashMap<>();
		
		WTUser lastAppUser = ApprovalHelper.manager.getLastApprover(part);
		this.lastApprover = lastAppUser!=null?lastAppUser.getFullName():"";
	}
	
	@Override
	public boolean deleteBtn() throws Exception {
		
		return super.deleteBtn() && (ownerEpm() == null);
	}
	
	public String epmOid() throws Exception{
		if(this.epm ==null) {
			this.epm = EpmHelper.manager.getEPMDocument(part);
			this.epmOid = CommonUtil.getOIDString(this.epm);
		}else {
			this.epmOid = CommonUtil.getOIDString(this.epm);
		}
		
		return epmOid;
	}
	
	/**
	 * 
	 * @desc	: Creo 의  Drawing
	 * @author	: tsuam
	 * @date	: 2019. 11. 8.
	 * @method	: drawingEPM
	 * @return	: EPMDocument
	 * @return
	 * @throws Exception
	 */
	public EPMDocument drawingEPM() throws Exception{
		this.epm = ownerEpm();
		EPMDocument drawingEPM = null;
		if(epm != null){
			drawingEPM = EpmHelper.manager.getDrawing((EPMDocumentMaster)epm.getMaster());
		}
		
		return drawingEPM;
	}
	
	/**
	 * 
	 * @desc	: Creo 의  Drawing CAD 파일명
	 * @author	: tsuam
	 * @date	: 2019. 11. 8.
	 * @method	: drawingEPM
	 * @return	: EPMDocument
	 * @return
	 * @throws Exception
	 */
	public String drawingName() throws Exception{
		
		EPMDocument drawingEPM = drawingEPM();
		String drawingName = "";
		if(drawingEPM != null){
			drawingName = drawingEPM.getCADName();
		}
		
		return drawingName;
	}
	
	public void loadAttributes() throws Exception{
		this.attributes = IBAUtil.getAttributes(this.part);
		
		this.materialName = CodeHelper.service.getValue("MATERIAL", (String) attributes.get("MATERIAL"));
		this.weight = (String) attributes.get("WEIGHT");
		this.upg = (String) attributes.get("UPG");
		this.customer_part_number = (String) attributes.get("CUSTOMER_PART_NUMBER");
		this.unit_cost = (String) attributes.get("UNIT_COST");
		this.finishName = CodeHelper.service.getValue("FINISH", (String) attributes.get("FINISH"));
		String specialAttrValue = (String) attributes.get("SPECIAL_ATTRIBUTE");
		if(specialAttrValue != null && specialAttrValue.length() > 0) {
			String specialAttr = CodeHelper.manager.getValueSplit("SPECIALATTR", specialAttrValue, ",");
			this.specialAttrName = specialAttr;
		}
		String certiValue = (String) attributes.get("CERTIFICATION_REGULATIONS");
		if(certiValue != null && certiValue.length() > 0) { 
			String certification = CodeHelper.manager.getValueSplit("CERTIFICATION", certiValue, ",");
			this.certificationName = certification;
		}
		this.carryOverName = CodeHelper.service.getValue("CARRYOVER", (String) attributes.get("CARRY_OVER"));
		this.pressure = (String) attributes.get("ALLOWABLE_PRESSURE");
		this.gasType = (String) attributes.get("GAS_TYPE");
		this.description = (String) attributes.get("DESCRIPTION");
		
		this.method = (String) attributes.get("METHOD");
		this.specification = (String) attributes.get("SPECIFICATION");
		this.start_company_name = (String) attributes.get("START_COMPANY_NAME");
		this.start_company_manager = (String) attributes.get("START_COMPANY_MANAGER");
		this.start_compan_tell = (String) attributes.get("START_COMPANY_TELL");
		this.start_unit_cost = (String) attributes.get("START_UNIT_COST");
		this.prod_company_name = (String) attributes.get("PROD_COMPANY_NAME");
		this.prod_company_manager = (String) attributes.get("PROD_COMPANY_MANAGER");
		this.prod_company_tell = (String) attributes.get("PROD_COMPANY_TELL");
		this.prod_unit_cost = (String) attributes.get("PROD_UNIT_COST");
		
		
		this.treatment = (String)attributes.get(CadAttributeDNC.ATT_TREATMENT.getKey());
		this.material = (String)attributes.get(CadAttributeDNC.ATT_MATERIAL.getKey());
		this.category = (String)attributes.get(CadAttributeDNC.ATT_CATEGORY.getKey());
		this.assembleType = (String)attributes.get(CadAttributeDNC.ASSEMBLE_TYPE.getKey());
		
	}
	
	public void bomEndItemHash() throws Exception{
		
		if(this.part != null) {
			List<BomTreeData> bomTreeData = BomHelper.manager.getEndItemList(this.part, null, null, ViewHelper.service.getView("Design"));
			
			if(bomTreeData != null) {
				for(BomTreeData data : bomTreeData) {
					this.bomEndItemHash.put(data.getOid(), data.getNumber());
				}
			}
		}
	}
	
	public EPMDocument ownerEpm() throws Exception{
		
		if(this.epm ==null) {
			this.epm = EpmHelper.manager.getEPMDocument(part);
			
			
			if(this.epm !=null) {
				this.epm3DName = this.epm.getName();
				this.epm3DNumber = this.epm.getNumber();
				this.epm3DOid = CommonUtil.getOIDString(this.epm);
				EPMDocument epm2D = EpmHelper.manager.getDrawing((EPMDocumentMaster)this.epm.getMaster());
				
				if(epm2D != null) {
					this.epm2DName = epm2D.getName();
					this.epm2DNumber = epm2D.getNumber();
					this.epm2DOid = CommonUtil.getOIDString(epm2D);
				}
			}
			
		}else {
			
			this.epm3DName = this.epm.getName();
			this.epm3DNumber = this.epm.getNumber();
			this.epm3DOid = CommonUtil.getOIDString(this.epm);
			//System.out.println("3.Part Data ownerEpm epm3DOid = " +epm3DOid);
			EPMDocument epm2D = EpmHelper.manager.getDrawing((EPMDocumentMaster)this.epm.getMaster());
			
			if(epm2D != null) {
				this.epm2DName = epm2D.getName();
				this.epm2DNumber = epm2D.getNumber();
				this.epm2DOid = CommonUtil.getOIDString(epm2D);
			}
			
		}
		
		return this.epm;
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

	public String getThumbnail() throws Exception {
		if(this.epm == null) {
			this.epm = EpmHelper.manager.getEPMDocument(part);
		}
		
		String thumbnail = EpmUtil.getThumbnail(epm);
		
		return thumbnail;
	}

	public String getMinPublishURL() {
		return minPublishURL;
	}

	public void setMinPublishURL(String minPublishURL) {
		this.minPublishURL = minPublishURL;
	}

	public String getEpmOid() {
		return epmOid;
	}

	public void setEpmOid(String epmOid) {
		this.epmOid = epmOid;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnitDisplay() {
		return unitDisplay;
	}

	public void setUnitDisplay(String unitDisplay) {
		this.unitDisplay = unitDisplay;
	}

	public Timestamp getDownloadDeadline() {
		return downloadDeadline;
	}

	public void setDownloadDeadline(Timestamp downloadDeadline) {
		this.downloadDeadline = downloadDeadline;
	}

	public String getPrUser() {
		return prUser;
	}

	public void setPrUser(String prUser) {
		this.prUser = prUser;
	}

	public String getPrDeptName() {
		return prDeptName;
	}

	public void setPrDeptName(String prDeptName) {
		this.prDeptName = prDeptName;
	}

	public String getPurchase_state() {
		return purchase_state;
	}

	public void setPurchase_state(String purchase_state) {
		this.purchase_state = purchase_state;
	}

	public String getPjtNo() {
		return pjtNo;
	}

	public void setPjtNo(String pjtNo) {
		this.pjtNo = pjtNo;
	}

	public String getEpmNo() {
		return epmNo;
	}

	public void setEpmNo(String epmNo) {
		this.epmNo = epmNo;
	}

	public String getEpmver() {
		return epmver;
	}

	public void setEpmver(String epmver) {
		this.epmver = epmver;
	}

	public int getLinkState() {
		return linkState;
	}

	public void setLinkState(int linkState) {
		this.linkState = linkState;
	}
	public String getDistNo() {
		return distNo;
	}

	public void setDistNo(String distNo) {
		this.distNo = distNo;
	}

	public String getLinkStateStr() {
		return linkStateStr;
	}

	public void setLinkStateStr(String linkStateStr) {
		this.linkStateStr = linkStateStr;
	}

	public String getSupplierNames() {
		return supplierNames;
	}

	public void setSupplierNames(String supplierNames) {
		this.supplierNames = supplierNames;
	}

	public String getRequestNames() {
		return requestNames;
	}

	public void setRequestNames(String requestNames) {
		this.requestNames = requestNames;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getFinishName() {
		return finishName;
	}

	public void setFinishName(String finishName) {
		this.finishName = finishName;
	}

	public String getSpecialAttrName() {
		return specialAttrName;
	}

	public void setSpecialAttrName(String specialAttrName) {
		this.specialAttrName = specialAttrName;
	}

	public String getCertificationName() {
		return certificationName;
	}

	public void setCertificationName(String certificationName) {
		this.certificationName = certificationName;
	}

	public String getCarryOverName() {
		return carryOverName;
	}

	public void setCarryOverName(String carryOverName) {
		this.carryOverName = carryOverName;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getUpg() {
		return upg;
	}

	public void setUpg(String upg) {
		this.upg = upg;
	}

	public String getCustomer_part_number() {
		return customer_part_number;
	}

	public void setCustomer_part_number(String customer_part_number) {
		this.customer_part_number = customer_part_number;
	}

	public String getUnit_cost() {
		return unit_cost;
	}

	public void setUnit_cost(String unit_cost) {
		this.unit_cost = unit_cost;
	}

	public String getPressure() {
		return pressure;
	}

	public void setPressure(String pressure) {
		this.pressure = pressure;
	}

	public String getGasType() {
		return gasType;
	}

	public void setGasType(String gasType) {
		this.gasType = gasType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getStart_company_name() {
		return start_company_name;
	}

	public void setStart_company_name(String start_company_name) {
		this.start_company_name = start_company_name;
	}

	public String getStart_company_manager() {
		return start_company_manager;
	}

	public void setStart_company_manager(String start_company_manager) {
		this.start_company_manager = start_company_manager;
	}

	public String getStart_compan_tell() {
		return start_compan_tell;
	}

	public void setStart_compan_tell(String start_compan_tell) {
		this.start_compan_tell = start_compan_tell;
	}

	public String getProd_company_name() {
		return prod_company_name;
	}

	public void setProd_company_name(String prod_company_name) {
		this.prod_company_name = prod_company_name;
	}

	public String getProd_company_manager() {
		return prod_company_manager;
	}

	public void setProd_company_manager(String prod_company_manager) {
		this.prod_company_manager = prod_company_manager;
	}

	public String getProd_company_tell() {
		return prod_company_tell;
	}

	public void setProd_company_tell(String prod_company_tell) {
		this.prod_company_tell = prod_company_tell;
	}

	public String getStart_unit_cost() {
		return start_unit_cost;
	}

	public void setStart_unit_cost(String start_unit_cost) {
		this.start_unit_cost = start_unit_cost;
	}

	public String getProd_unit_cost() {
		return prod_unit_cost;
	}

	public void setProd_unit_cost(String prod_unit_cost) {
		this.prod_unit_cost = prod_unit_cost;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTreatment() {
		return treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}

	public String getAssembleType() {
		return assembleType;
	}

	public void setAssembleType(String assembleType) {
		this.assembleType = assembleType;
	}

	public Map<String, String> getBomEndItemHash() {
		return bomEndItemHash;
	}

	public void setBomEndItemHash(Map<String, String> bomEndItemHash) {
		this.bomEndItemHash = bomEndItemHash;
	}

	public boolean isLastCheck() {
		return lastCheck;
	}

	public void setLastCheck(boolean lastCheck) {
		this.lastCheck = lastCheck;
	}

	public String getEpm3DName() {
		return epm3DName;
	}

	public void setEpm3DName(String epm3dName) {
		epm3DName = epm3dName;
	}

	public String getEpm3DNumber() {
		return epm3DNumber;
	}

	public void setEpm3DNumber(String epm3dNumber) {
		epm3DNumber = epm3dNumber;
	}

	public String getEpm3DOid() {
		return epm3DOid;
	}

	public void setEpm3DOid(String epm3dOid) {
		epm3DOid = epm3dOid;
	}

	public String getEpm2DName() {
		return epm2DName;
	}

	public void setEpm2DName(String epm2dName) {
		epm2DName = epm2dName;
	}

	public String getEpm2DNumber() {
		return epm2DNumber;
	}

	public void setEpm2DNumber(String epm2dNumber) {
		epm2DNumber = epm2dNumber;
	}

	public String getEpm2DOid() {
		return epm2DOid;
	}

	public void setEpm2DOid(String epm2dOid) {
		epm2DOid = epm2dOid;
	}

	public String getLastApprover() {
		return lastApprover;
	}

	public void setLastApprover(String lastApprover) {
		this.lastApprover = lastApprover;
	}
	
	
	
	
}
