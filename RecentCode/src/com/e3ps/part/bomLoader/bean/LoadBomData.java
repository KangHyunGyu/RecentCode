package com.e3ps.part.bomLoader.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.e3ps.common.util.StringUtil;

import jxl.Cell;

public class LoadBomData {

	private int line;
	
	private String id;
	private String level;
	private String number;
	private String name;
	private String epmNumber;
	private String epmName; 
	private String unit;
	private String quantity;
	private String fileName;
	private String purchaseFlag;
	private String endDate;
	private String notice;
	private String specification;
	private String createDate;
	private String etc;
	private String relatedEpm;
	private String summary;
	private String material;
	private String snock;
	private String cPartNum;
	private String partType;
	private String surfaceTreatment;
	private String maker;
	
	private String classification;
	private String verification;
	private String result;
	private String change = "NO";	//등록(CREATE), 수정(MODIFY), 개정(REVISE), 변함없음(NO)
	private String checkChange = "Y";	//change에 대한 선택
	private String parentNumber;
	
	private List<LoadBomData> partChildren = new ArrayList<>();
	
	public LoadBomData(Cell[] cell) throws Exception {
		int column = 0;
		this.level = StringUtil.checkNull(getContent(cell, column++).trim()); //level
		this.number = StringUtil.checkNull(getContent(cell, column++).trim());   //number
		this.name = StringUtil.checkNull(getContent(cell, column++).trim());   //number
		this.unit= StringUtil.checkNull(getContent(cell, column++).trim());			//단위
		this.quantity =StringUtil.checkNull(getContent(cell, column++).trim());  //수량
		this.purchaseFlag = StringUtil.checkNull(getContent(cell, column++).trim());			//구매여부
		this.endDate = StringUtil.checkNull(getContent(cell, column++).trim());			//종료일자
		this.summary = StringUtil.checkNull(getContent(cell, column++).trim());			//개요
		this.specification = StringUtil.checkNull(getContent(cell, column++).trim());			//규격
		this.notice = StringUtil.checkNull(getContent(cell, column++).trim());				//비고
		this.material = StringUtil.checkNull(getContent(cell, column++).trim());			//material
		this.snock = StringUtil.checkReplaceStr(getContent(cell, column++).trim(), "N");			//sn자재여부
		this.cPartNum = StringUtil.checkNull(getContent(cell, column++).trim());			//CPARTNUM
		this.partType = StringUtil.checkNull(getContent(cell, column++).trim());			//PartType
		this.surfaceTreatment = StringUtil.checkNull(getContent(cell, column++).trim());			//SurfaceTreatment
		this.maker = StringUtil.checkNull(getContent(cell, column++).trim());			// Maker
		this.classification = this.number.substring(0, 1);
	}
	
	public LoadBomData(Map<String, Object> map) throws Exception {
		this.line = (int) map.get("line");	//line
		this.level = StringUtil.checkNull((String) map.get("level")).trim(); //level
		this.number = StringUtil.checkNull((String) map.get("number")).trim();   //number
		this.name = StringUtil.checkNull((String) map.get("name")).trim();   //number
		this.unit= StringUtil.checkNull((String) map.get("unit")).trim();			//단위
		this.quantity =StringUtil.checkNull((String) map.get("quantity")).trim();  //수량
		this.purchaseFlag = StringUtil.checkNull((String) map.get("purchaseFlag")).trim();			//구매여부
		this.endDate = StringUtil.checkNull((String) map.get("endDate")).trim();			//종료일자
		this.summary = StringUtil.checkNull((String) map.get("summary")).trim();			//개요
		this.specification = StringUtil.checkNull((String) map.get("specification")).trim();			//규격
		this.notice = StringUtil.checkNull((String) map.get("notice")).trim();			//비고
		this.material = StringUtil.checkNull((String) map.get("material")).trim();			//material
		this.snock = StringUtil.checkReplaceStr((String) map.get("snock"), "N").trim();			//sn자재여부
		this.cPartNum = StringUtil.checkNull((String) map.get("cPartNum")).trim();			//CPARTNUM
		this.partType = StringUtil.checkNull((String) map.get("partType")).trim();			//PartType
		this.surfaceTreatment = StringUtil.checkNull((String) map.get("surfaceTreatment")).trim();			//SurfaceTreatment
		this.maker = StringUtil.checkNull((String) map.get("maker")).trim();			// Maker
		this.change = StringUtil.checkNull((String) map.get("change")).trim();			//등록, 수정, 삭제, 변함없음
		this.checkChange = StringUtil.checkNull((String) map.get("checkChange")).trim();			//등록, 수정, 삭제, 변함없음
		this.classification = this.number.substring(0, 1);
	}
	
	public static String getContent(Cell[] cell, int idx) {
		try {
			String val = cell[idx].getContents();
			if (val == null)
				return "";
			return val;
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		return "";
	}

	public String attrCheck(Object obj){
		LoadBomData data = (LoadBomData)obj;
		
		String ret = "";
		
		if(!data.getName().equals(this.name)){
			if(!"".equals(ret)) {
				ret += ", ";
			}
			ret += "Name";
		}
		
		if(!data.getUnit().equals(this.unit)){
			if(!"".equals(ret)) {
				ret += ", ";
			}
			ret += "단위";
		}
		
		return ret;
	}
	
	public boolean equals(Object obj){
		LoadBomData data = (LoadBomData)obj;
		
		if(!data.getName().equals(this.name)){
			return false;
		}
		
		if(!data.getUnit().equals(this.unit)){
			return false;
		}
		
		return true;
	}
	
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPurchaseFlag() {
		return purchaseFlag;
	}

	public void setPurchaseFlag(String purchaseFlag) {
		this.purchaseFlag = purchaseFlag;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getEtc() {
		return etc;
	}

	public void setEtc(String etc) {
		this.etc = etc;
	}

	public String getRelatedEpm() {
		return relatedEpm;
	}

	public void setRelatedEpm(String relatedEpm) {
		this.relatedEpm = relatedEpm;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getSnock() {
		return snock;
	}

	public void setSnock(String snock) {
		this.snock = snock;
	}

	public String getcPartNum() {
		return cPartNum;
	}

	public void setcPartNum(String cPartNum) {
		this.cPartNum = cPartNum;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public String getVerification() {
		return verification;
	}

	public void setVerification(String verification) {
		this.verification = verification;
	}

	public String getChange() {
		return change;
	}

	public void setChange(String change) {
		this.change = change;
	}

	public String getCheckChange() {
		return checkChange;
	}

	public void setCheckChange(String checkChange) {
		this.checkChange = checkChange;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentNumber() {
		return parentNumber;
	}

	public void setParentNumber(String parentNumber) {
		this.parentNumber = parentNumber;
	}

	public List<LoadBomData> getPartChildren() {
		return partChildren;
	}

	public void setPartChildren(List<LoadBomData> partChildren) {
		this.partChildren = partChildren;
	}

	public String getPartType() {
		return partType;
	}

	public void setPartType(String partType) {
		this.partType = partType;
	}

	public String getSurfaceTreatment() {
		return surfaceTreatment;
	}

	public void setSurfaceTreatment(String surfaceTreatment) {
		this.surfaceTreatment = surfaceTreatment;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}
	
}
