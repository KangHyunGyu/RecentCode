package com.e3ps.doc.util;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//문서 유형에 따른 IBA 속성 키 리스트 뽑아오는 Enum 클래스
//사용 예제
//DocTypePropList obj = Stream.of(DocTypePropList.values()).filter(h -> h.getDocTypeCode().equals("D001")).findFirst().orElseThrow(() -> new Exception("문서 유형 코드가 존재하지 않음"));
//Map<String,String> propMap = obj.getProps();
//propMap.forEach( (jspName, ibaKey) -> { IBAUtil.changeIBAValue(part, ibaKey, StringUtil.checkNull((String)reqMap.get(jspName)), "string") });
public enum DocTypePropList {
	
	//계약,개발검토서
	CONTRACT_DEVELOPMENT_REVIEW("D001", "/Default/Document/1.계약 개발 검토서", Map.ofEntries(
//			  new AbstractMap.SimpleEntry<String, String>("customerName", "CUSTOMERNAME"),
//			  new AbstractMap.SimpleEntry<String, String>("counsecustomer", "COUNSECUSTOMER"),
//			  new AbstractMap.SimpleEntry<String, String>("contact", "CONTACT"),
//			  new AbstractMap.SimpleEntry<String, String>("dateReceipt", "DATERECEIPT"),
//			  new AbstractMap.SimpleEntry<String, String>("receptionist", "RECEPTIONIST"),
//			  new AbstractMap.SimpleEntry<String, String>("consultMethod", "CONSULTMETHOD"),
//			  new AbstractMap.SimpleEntry<String, String>("applicationMaterial", "APPLICATIONMATERIAL"),
//			  new AbstractMap.SimpleEntry<String, String>("receptionClass", "RECEPTIONCLASS"),
//			  new AbstractMap.SimpleEntry<String, String>("productName", "PRODUCTNAME"),
//			  new AbstractMap.SimpleEntry<String, String>("productStandard", "PRODUCTSTANDARD"),
//			  new AbstractMap.SimpleEntry<String, String>("quan", "QUAN"),
//			  new AbstractMap.SimpleEntry<String, String>("price", "PRICE"),
//			  new AbstractMap.SimpleEntry<String, String>("submissionDeadline", "SUBMISSIONDEADLINE"),
//			  new AbstractMap.SimpleEntry<String, String>("dueDate", "DUEDATE"),
//			  new AbstractMap.SimpleEntry<String, String>("requirements", "REQUIREMENTS"),
//			  new AbstractMap.SimpleEntry<String, String>("contents", "CONTENTS")
			  
			  new AbstractMap.SimpleEntry<String, String>("customerNameCDR","CUSTOMERNAMECDR"),
			  new AbstractMap.SimpleEntry<String, String>("counseCustomerCDR","COUNSECUSTOMERCDR"),
			  new AbstractMap.SimpleEntry<String, String>("contactCDR","CONTACTCDR"),
			  new AbstractMap.SimpleEntry<String, String>("dateReceiptCDR", "DATERECEIPTCDR"),
			  new AbstractMap.SimpleEntry<String, String>("receptionistCDR", "RECEPTIONISTCDR"),
			  new AbstractMap.SimpleEntry<String, String>("consultMethodCDR","CONSULTMETHODCDR"),
			  new AbstractMap.SimpleEntry<String, String>("applicationMaterialCDR","APPLICATIONMATERIALCDR"),
			  new AbstractMap.SimpleEntry<String, String>("receptionClassCDR","RECEPTIONCLASSCDR"),
			  new AbstractMap.SimpleEntry<String, String>("productNameCDR","PRODUCTNAMECDR"),
			  new AbstractMap.SimpleEntry<String, String>("productStandardCDR","PRODUCTSTANDARDCDR"),
			  new AbstractMap.SimpleEntry<String, String>("quanCDR","QUANCDR"),
			  new AbstractMap.SimpleEntry<String, String>("priceCDR","PRICECDR"),
			  new AbstractMap.SimpleEntry<String, String>("submissionDeadlineCDR","SUBMISSIONDEADLINECDR"),
			  new AbstractMap.SimpleEntry<String, String>("dueDateCDR","DUEDATECDR"),
//			  new AbstractMap.SimpleEntry<String, String>("requirementsCDR","REQUIREMENTSCDR"),
			  
			  new AbstractMap.SimpleEntry<String, String>("companyDrawCDR","COMPANYDRAWCDR"),
			  new AbstractMap.SimpleEntry<String, String>("processDevRevCDR","PROCESSDEVREVCDR"),
			  new AbstractMap.SimpleEntry<String, String>("sampleSubCDR","SAMPLESUBCDR"),
			  new AbstractMap.SimpleEntry<String, String>("reReportCDR","REREPORTCDR"),
			  new AbstractMap.SimpleEntry<String, String>("reqAnalysisCDR","REQANALYSISCDR"),
			  new AbstractMap.SimpleEntry<String, String>("requirementEtcCDR","REQUIREMENTETCCDR"),
			  
			  new AbstractMap.SimpleEntry<String, String>("descriptionCDR","DESCRIPTIONCDR")
			  
			)),
//	//도면출도 의뢰서
//	DRAWING_REQUEST("D002", Map.ofEntries(
//			new AbstractMap.SimpleEntry<String, String>("dateReceipt", "DATERECEIPT"),
//			new AbstractMap.SimpleEntry<String, String>("productName", "PRODUCTNAME"),
//			new AbstractMap.SimpleEntry<String, String>("drawNumber", "DRAWNUMBER"),
//			new AbstractMap.SimpleEntry<String, String>("partNumber", "PARTNUMBER"),
//			new AbstractMap.SimpleEntry<String, String>("wtUse", "WTUSE"),
//			new AbstractMap.SimpleEntry<String, String>("notationConfirm", "NOTATIONCONFIRM"),
//			new AbstractMap.SimpleEntry<String, String>("fileForm", "FILEFORM"),
//			new AbstractMap.SimpleEntry<String, String>("releaseDate", "RELEASEDATE"),
//			new AbstractMap.SimpleEntry<String, String>("withDraw", "WITHDRAW"),
//			new AbstractMap.SimpleEntry<String, String>("CONTENTS", "CONTENTS")
//			)),
//	//설계 변경 요청서
//	ENGINEERING_CHANGE_REQUEST("D003", Map.ofEntries(
//			new AbstractMap.SimpleEntry<String, String>("customerName", "CUSTOMERNAME"),
//			new AbstractMap.SimpleEntry<String, String>("productName", "PRODUCTNAME"),
//			new AbstractMap.SimpleEntry<String, String>("docMaterial", "DOCMATERIAL"),
//			new AbstractMap.SimpleEntry<String, String>("drawNumber", "DRAWNUMBER"),
//			new AbstractMap.SimpleEntry<String, String>("partNumber", "PARTNUMBER"),
//			new AbstractMap.SimpleEntry<String, String>("designChangeReason", "DESIGNCHANGEREASON"),
//			new AbstractMap.SimpleEntry<String, String>("designChangeCont", "DESIGNCHANGECONT"),
//			new AbstractMap.SimpleEntry<String, String>("appliedPeriod", "APPLIEDPERIOD"),
//			new AbstractMap.SimpleEntry<String, String>("contents", "CONTENTS")
//			)),
	//서비스 요청서
	SERVICE_REQUEST("D002", "/Default/Document/2.서비스 요청서", Map.ofEntries(
			new AbstractMap.SimpleEntry<String, String>("customerNameSR", "CUSTOMERNAMESR"),
			new AbstractMap.SimpleEntry<String, String>("managerSR", "MANAGERSR"),
			new AbstractMap.SimpleEntry<String, String>("partNumberSR", "PARTNUMBERSR"),
			new AbstractMap.SimpleEntry<String, String>("productNameSR", "PRODUCTNAMESR"),
			new AbstractMap.SimpleEntry<String, String>("drawNumberSR", "DRAWNUMBERSR"),
			new AbstractMap.SimpleEntry<String, String>("barcodeNumberSR", "BARCODENUMBERSR"),
			new AbstractMap.SimpleEntry<String, String>("markNumberSR", "MARKNUMBERSR"),
			new AbstractMap.SimpleEntry<String, String>("quanSR", "QUANSR"),
			new AbstractMap.SimpleEntry<String, String>("divisionSR", "DIVISIONSR"),
			new AbstractMap.SimpleEntry<String, String>("usageSR", "USAGESR"),
			new AbstractMap.SimpleEntry<String, String>("descriptionSR", "DESCRIPTIONSR")
			)),
	//측정 의뢰서
	MEASUREMENT_REQUEST("D003", "/Default/Document/3.측정 의뢰서", Map.ofEntries(
			new AbstractMap.SimpleEntry<String, String>("dateReceiptMR", "DATERECEIPTMR"),
			new AbstractMap.SimpleEntry<String, String>("compDateMR", "COMPDATEMR"),
			new AbstractMap.SimpleEntry<String, String>("quanMR", "QUANMR"),
			new AbstractMap.SimpleEntry<String, String>("productNameMR", "PRODUCTNAMEMR"),
			new AbstractMap.SimpleEntry<String, String>("partNumberMR", "PARTNUMBERMR"),
			new AbstractMap.SimpleEntry<String, String>("customerNameMR", "CUSTOMERNAMEMR"),
			new AbstractMap.SimpleEntry<String, String>("markNumberMR", "MARKNUMBERMR"),
			new AbstractMap.SimpleEntry<String, String>("docMaterialMR", "DOCMATERIALMR"),
			new AbstractMap.SimpleEntry<String, String>("productStatusMR", "PRODUCTSTATUSMR"),
			new AbstractMap.SimpleEntry<String, String>("illuminanceMeasMR", "ILLUMINANCEMEASMR"),
			new AbstractMap.SimpleEntry<String, String>("productSurfaceConMRT", "PRODUCTSURFACECONMRT"),
			new AbstractMap.SimpleEntry<String, String>("productSurfaceConMRB", "PRODUCTSURFACECONMRB"),
			new AbstractMap.SimpleEntry<String, String>("productSurfaceConMRE", "PRODUCTSURFACECONMRE"),
			new AbstractMap.SimpleEntry<String, String>("importanceMR", "IMPORTANCEMR"),
			new AbstractMap.SimpleEntry<String, String>("meaWeightMR", "MEAWEIGHTMR"),
			new AbstractMap.SimpleEntry<String, String>("meaResistanceMR", "MEARESISTANCEMR"),
			new AbstractMap.SimpleEntry<String, String>("meaCornerMR", "MEACORNERMR"),
			new AbstractMap.SimpleEntry<String, String>("meaThicknessMR", "MEATHICKNESSMR"),
			new AbstractMap.SimpleEntry<String, String>("descriptionMR", "DESCRIPTIONMR")
			)),
	//분석 의뢰서
		ANALYSIS_REQUEST("D004", "/Default/Document/4.분석 의뢰서", Map.ofEntries(
				new AbstractMap.SimpleEntry<String, String>("dateReceiptAR", "DATERECEIPTAR"),
				new AbstractMap.SimpleEntry<String, String>("compDateAR", "COMPDATEAR"),
				new AbstractMap.SimpleEntry<String, String>("wtUseAR", "WTUSEAR"),
				new AbstractMap.SimpleEntry<String, String>("customerNameAR", "CUSTOMERNAMEAR"),
				new AbstractMap.SimpleEntry<String, String>("refDepartmentAR", "REFDEPARTMENTAR"),
				new AbstractMap.SimpleEntry<String, String>("quanAR", "QUANAR"),
				new AbstractMap.SimpleEntry<String, String>("productNameAR", "PRODUCTNAMEAR"),
				new AbstractMap.SimpleEntry<String, String>("specManufactureAR", "SPECMANUFACTUREAR"),
				new AbstractMap.SimpleEntry<String, String>("purposeAR", "PURPOSEAR"),
				new AbstractMap.SimpleEntry<String, String>("analysisAgencyInAR", "ANALYSISAGENCYINAR"),
				new AbstractMap.SimpleEntry<String, String>("analysisAgencyOutAR", "ANALYSISAGENCYOUTAR"),
				new AbstractMap.SimpleEntry<String, String>("analysisItemAR", "ANALYSISITEMAR"),
				new AbstractMap.SimpleEntry<String, String>("docMaterialAR", "DOCMATERIALAR"),
				new AbstractMap.SimpleEntry<String, String>("sampleDataNameAR", "SAMPLEDATANAMEAR"),
				new AbstractMap.SimpleEntry<String, String>("divisionAR", "DIVISIONAR"),
				new AbstractMap.SimpleEntry<String, String>("descriptionAR", "DESCRIPTIONAR")
				)),
	//공정 변경 요청서
	PROCESS_CHANGE_REQUEST("D005", "/Default/Document/5.공정 변경 요청서", Map.ofEntries(
			new AbstractMap.SimpleEntry<String, String>("beforeChangePCR", "BEFORECHANGEPCR"),
			new AbstractMap.SimpleEntry<String, String>("afterChangePCR", "AFTERCHANGEPCR"),
			new AbstractMap.SimpleEntry<String, String>("changeReasonPCR", "CHANGEREASONPCR"),
			new AbstractMap.SimpleEntry<String, String>("productPartReviewPCR", "PRODUCTPARTREVIEWPCR"),
			new AbstractMap.SimpleEntry<String, String>("qualityPartReviewPCR", "QUALITYPARTREVIEWPCR"),
			new AbstractMap.SimpleEntry<String, String>("relatedDepReviewPCR", "RELATEDDEPREVIEWPCR")
			)),
	//공정 개발 검토서
	PROCESS_DEVELOPMENT_REVIEW("D006", "/Default/Document/6.공정 개발 검토서", Map.ofEntries(
			new AbstractMap.SimpleEntry<String, String>("customerNamePDR", "CUSTOMERNAMEPDR"),
			new AbstractMap.SimpleEntry<String, String>("productNamePDR", "PRODUCTNAMEPDR"),
//			new AbstractMap.SimpleEntry<String, String>("productSizePDR", "PRODUCTSIZEPDR"),
			new AbstractMap.SimpleEntry<String, String>("productOutDiameterPDR", "PRODUCTOUTDIAMETERPDR"),
			new AbstractMap.SimpleEntry<String, String>("productInDiameterPDR", "PRODUCTINDIAMETERPDR"),
			new AbstractMap.SimpleEntry<String, String>("productThicknessPDR", "PRODUCTTHICKNESSPDR"),
			new AbstractMap.SimpleEntry<String, String>("drawNumberPDR", "DRAWNUMBERPDR"),
			new AbstractMap.SimpleEntry<String, String>("pricePDR", "PRICEPDR"),
			new AbstractMap.SimpleEntry<String, String>("inputRatePDR", "INPUTRATEPDR"),
			new AbstractMap.SimpleEntry<String, String>("outsourceRatePDR", "OUTSOURCERATEPDR"),
			new AbstractMap.SimpleEntry<String, String>("divisionPDR", "DIVISIONPDR"),
			new AbstractMap.SimpleEntry<String, String>("changeMaterialUnitPricePDR", "CHANGEMATERIALUNITPRICEPDR"),
			new AbstractMap.SimpleEntry<String, String>("marketVolatilityPDR", "MARKETVOLATILITYPDR"),
			new AbstractMap.SimpleEntry<String, String>("competPriceResponsPDR", "COMPETPRICERESPONSPDR"),
			new AbstractMap.SimpleEntry<String, String>("inPriceResponsPDR", "INPRICERESPONSPDR"),
			new AbstractMap.SimpleEntry<String, String>("possibilityOrderInFuturePDR", "POSSIBILITYORDERINFUTUREPDR"),
			new AbstractMap.SimpleEntry<String, String>("finalDecisionPDR", "FINALDECISIONPDR")
			));
//	//기타
//	ETC_CONTENTS("D009", "/Default/Document/기타", Map.ofEntries(
//				  new AbstractMap.SimpleEntry<String, String>("jsp name값", "IBA 키값")
//				));
//	//구매 의뢰서
//	PURCHASE_REQUEST("D010", "/Default/Document", Map.ofEntries(
//			  new AbstractMap.SimpleEntry<String, String>("jsp name값", "IBA 키값")
//			)),
//	//이슈 리스트
//	ISSUE_LIST("D011", "/Default/Document", Map.ofEntries(
//			  new AbstractMap.SimpleEntry<String, String>("jsp name값", "IBA 키값")
//			));
	
	private final String docTypeCode;
	private final String location;
	private final Map<String,String> props;
	
	private DocTypePropList(final String docTypeCode, final String location, final Map<String,String> props){
		this.docTypeCode = docTypeCode;
		this.props = props;
		this.location = location;
	}
	
	public String getDocTypeCode() {
		return this.docTypeCode;
	}
	
	public String getLocation() {
		return location;
	}

	public Map<String,String> getProps() {
		return this.props;
	}
	
}
