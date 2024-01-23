/**
 * 
 */
package com.e3ps.common.excelDown.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.util.CookieGenerator;

import com.e3ps.change.beans.ECOData;
import com.e3ps.change.beans.ECRData;
import com.e3ps.common.code.bean.NumberCodeData;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.service.CommonHelper;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.doc.bean.E3PSDocumentData;
import com.e3ps.epm.bean.EpmData;
import com.e3ps.epm.bean.EpmPartStateData;
import com.e3ps.epm.dnc.CadAttributeDNC;
import com.e3ps.org.bean.PeopleData;
import com.e3ps.part.bean.PartData;
import com.e3ps.project.EOutput;
import com.e3ps.project.EProject;
import com.e3ps.project.ETask;
import com.e3ps.project.beans.ProjectData;
import com.e3ps.project.beans.ProjectGanttLinkData;
import com.e3ps.project.beans.ProjectGanttViewTaskData;
import com.e3ps.project.beans.ProjectOutputData;
import com.e3ps.project.beans.ProjectRoleData;
import com.e3ps.project.service.OutputHelper;
import com.e3ps.project.service.ProjectHelper;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import wt.services.StandardManager;
import wt.util.WTException;

@SuppressWarnings("serial")
public class StandardExcelDownService extends StandardManager implements ExcelDownService {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	public static final String location = WCUtil.getWTHome()+"\\codebase\\excelTemplate\\";
	public static final String docExcelForm = "doc.xlsx";
	public static final String projectExcelForm = "project.xlsx";
	public static final String issueExcelForm = "issue.xlsx";
	public static final String ecrExcelForm = "ecr.xlsx";
	public static final String ecrDetailExcelForm = "DetailECR.xlsx";
	public static final String ecoExcelForm = "eco.xlsx";
	public static final String ecnExcelForm = "ECN.xlsx";
	public static final String epmExcelForm = "EPM.xlsx";
	public static final String epmPartStateExcelForm = "EPMPARTSTATE.xlsx";
	public static final String partExcelForm = "part.xlsx";
	public static final String disExcelForm = "distribute.xlsx";
	public static final String disItemExcelForm = "distributeItem.xls";
	public static final String disPartExcelForm = "distributePart.xlsx";
	public static final String disPurchaseExcelForm = "distributePurchase.xlsx";
	public static final String disEstimatetExcelForm = "distributeEstimate.xlsx";
	public static final String receiptExcelForm = "receipt.xls";
	public static final String supplierExcelForm = "supplier.xls";
	public static final String oldcarExcelForm = "oldcar.xlsx";
	public static final String benchmarkingExcelForm = "benchmarking.xlsx";
	public static final String userExcelForm = "user.xlsx";
	public static final String projectOutputForm = "projectoutput.xlsx";
	public static final String projectGanttForm = "ProjectGantt.xlsx";
	
	public static final String ERPPartExcelForm = "ERPPart.xls";
	public static final String ERPECRIssueExcelForm = "ERPECRIssue.xls";
	public static final String ERPECRExcelForm = "ERPECR.xls";
	public static final String ERPBOMExcelForm = "ERPBOM.xls";
	public static final String ERPBOMChangeExcelForm = "ERPBOMChange.xls";
	
	public static final String ERPProjectExcelForm = "ERPProject.xls";
	public static final String ERPProjectUserExcelForm = "ERPProjectUser.xls";
	public static final String ERPIssueExcelForm = "ERPIssue.xls";
	public static final String ERPPurchaseExcelForm = "ERPPurchase.xls";
	public static final String ERPPurchaseListExcelForm = "ERPPurchaseList.xls";
	public static final String ERPSupplierExcelForm = "ERPSupplier.xls";
	public static final String ERPDepartmentExcelForm = "ERPDepartment.xls";
	public static final String ERPUserExcelForm = "ERPUser.xls";
	public static final String ERPSiteExcelForm = "ERPSite.xls";
	public static final String ERPEstimateExcelForm = "ERPEstimate.xls";
	public static final String ERPEstimateListExcelForm = "ERPEstimateList.xls";
	
	public static final String todoExcelForm = "todoList.xls";
	
	public static final String numberCodeExcelForm = "NumberCodeList.xlsx";
	
	public static final String epmUploadForm = "epmUploadTemplate.xlsx";
	
	public static final String ganttExcelForm = "gantt.xlsx";
	
	public static StandardExcelDownService newStandardExcelDownService() throws WTException{
		final StandardExcelDownService instance = new StandardExcelDownService();
		instance.initialize();
		return instance;
	}
	
	/**
	 * @desc	: 엑셀 템플릿 가져오기
	 * @author	: mnyu
	 * @date	: 2019. 11. 21.
	 * @method	: getExcelSheet
	 * @return	: WritableWorkbook
	 * @param template
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	private WritableWorkbook getWorkbook(String template, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String fileName = new String(template.getBytes("euc-kr"), "8859_1");
		
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename="+fileName); 
		response.setContentType("application/vnd.ms-excel");
		
		request.setCharacterEncoding("euc-kr");
		
		File newfile = new File(location + template);
		jxl.Workbook wb = jxl.Workbook.getWorkbook(newfile);
		WritableWorkbook workbook = jxl.Workbook.createWorkbook(response.getOutputStream(), wb);

		return workbook;
	}

	/**
	 * @desc	: 파라미터 저장
	 * @author	: mnyu
	 * @date	: 2019. 11. 21.
	 * @method	: parameterMap
	 * @return	: Map<String,Object>
	 * @param request
	 * @return
	 */
	private Map<String, Object> parameterMap(HttpServletRequest request) {
		Map<String, Object> reqMap = new HashMap<String,Object>();
		
		Map<String,String[]> params = request.getParameterMap();
		Iterator<String> it = params.keySet().iterator();

		while(it.hasNext()){
			String key = it.next();
			Object values = params.get(key);
			if(params.get(key).length < 2){	// 배열 아닌경우
				values = StringUtil.checkNull(params.get(key)[0]);
			}
			reqMap.put(key, values);
		}
		return reqMap;
	}

	/**
	 * @desc	: workbookOutput
	 * @author	: mnyu
	 * @date	: 2020. 2. 6.
	 * @method	: workbookOutput
	 * @return	: void
	 * @param workbook
	 * @param response
	 * @throws IOException 
	 * @throws WriteException 
	 */
	private void workbookOutput(WritableWorkbook workbook, HttpServletResponse response) throws IOException, WriteException {
		CookieGenerator cg = new CookieGenerator();
		cg.setCookieName("fileDownload");
		cg.addCookie(response, "true");
		workbook.write();
		workbook.close();
	}
	
	/**
	 * @desc	: workbook write/close
	 * @author	: mnyu
	 * @date	: 2020. 2. 5.
	 * @method	: workbookOutput
	 * @return	: void
	 * @param response
	 * @throws IOException 
	 */
	private void workbookOutput(SXSSFWorkbook workbook, HttpServletResponse response) throws IOException {
		//Cookie cookie = new Cookie("fileDownload", "true");
		//response.addCookie(cookie);
		CookieGenerator cg = new CookieGenerator();
		cg.setCookieName("fileDownload");
		cg.addCookie(response, "true");

		workbook.write(response.getOutputStream());
		workbook.close();
		workbook.dispose();
		
	}
	/**
	 * @desc	: 엑셀 템플릿 가져오기
	 * @author	: mnyu
	 * @date	: 2020. 1. 31.
	 * @method	: getWorkbook2
	 * @return	: Workbook
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	private SXSSFWorkbook getSXSSFWorkbook(String form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 엑셀 템플릿 가져오기
		FileInputStream fis = new FileInputStream(location + form);
		Workbook workbook = new XSSFWorkbook(fis);
		
		// SXSSF 생성
	    SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook((XSSFWorkbook) workbook, 100);
	    
		response.setContentType("application/msexcel");
	    response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", URLEncoder.encode(form,"UTF-8")));

		return sxssfWorkbook;
	}
	/**
	 * @desc	: cell 저장
	 * @author	: mnyu
	 * @date	: 2020. 2. 6.
	 * @method	: setCellValue
	 * @return	: void
	 * @param workbook
	 * @param columnIndex
	 * @param data
	 * @param alignCenter
	 */
	private void setCellValue(Workbook workbook, Row row, int columnIndex, String data, boolean alignCenter) {
		CellStyle cs = workbook.createCellStyle();
		Cell cell = row.createCell(columnIndex);
		if(alignCenter){
			cs.setAlignment(HorizontalAlignment.CENTER);
		}else{
			cs.setAlignment(HorizontalAlignment.LEFT);
		}
		Font newFont = cell.getSheet().getWorkbook().createFont();
	    newFont.setBold(true);
	    newFont.setFontHeightInPoints((short) 10);
	    newFont.setItalic(false);
	    cs.setFont(newFont);
		cs.setWrapText(true);	// 줄바꿈 처리
		cs.setVerticalAlignment(VerticalAlignment.CENTER);
		cell.setCellStyle(cs);
		cell.setCellValue(String.valueOf(StringUtil.checkNull(data)));
	}
	/**
	 * @desc	: 문서 엑셀 다운로드
	 * @author	: mnyu
	 * @date	: 2019. 11. 21.
	 * @method	: excelDownDoc
	 * @return void
	 */
	@Override
	public void excelDownDoc(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 파라미터 저장
		Map<String, Object> reqMap = parameterMap(request); 
		List<E3PSDocumentData> list = ExcelDownHelper.manager.getDocList(reqMap);
		
		// 엑셀 템플릿 가져오기
		SXSSFWorkbook workbook = getSXSSFWorkbook(docExcelForm, request, response);
		Sheet sheet = workbook.getSheetAt(0);
		
		Row row = null;
		int rowIdx = 0;
		for(E3PSDocumentData data : list){
			row = sheet.createRow(++rowIdx);
			int columnIndex = 0;
			setCellValue(workbook, row, columnIndex++, data.getNumber(), true);
			setCellValue(workbook, row, columnIndex++, data.getVersion(), true);
			setCellValue(workbook, row, columnIndex++, data.getName(), false);
			setCellValue(workbook, row, columnIndex++, data.getLocation(), false);
			setCellValue(workbook, row, columnIndex++, data.getStateName(), true);
			setCellValue(workbook, row, columnIndex++, data.getCreatorDeptName(), true);
			setCellValue(workbook, row, columnIndex++, data.getCreator(), true);
			setCellValue(workbook, row, columnIndex++, data.getCreateDateFormat(), true);
			setCellValue(workbook, row, columnIndex++, data.getModifyDateFormat(), true);
		}
		
		// workbook write/close
		workbookOutput(workbook, response);
	}

	/**
	 * @desc	: 도면 엑셀 다운로드
	 * @author	: mnyu
	 * @date	: 2019. 11. 21.
	 * @method	: excelDownEpm
	 * @return void
	 */
	@Override
	public void excelDownEpm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 파라미터 저장
		Map<String, Object> reqMap = parameterMap(request); 
		List<EpmData> list = ExcelDownHelper.manager.getEpmList(reqMap);
		
		// 엑셀 템플릿 가져오기
		SXSSFWorkbook workbook = getSXSSFWorkbook(epmExcelForm, request, response);
		Sheet sheet = workbook.getSheetAt(0);
		
		Row row = null;
		int rowIdx = 0;
		for(EpmData data : list){
			row = sheet.createRow(++rowIdx);
			int columnIndex = 0;
			
			Map<String, Object> attributes = CommonHelper.manager.getAttributes(data.getOid());
			
			setCellValue(workbook, row, columnIndex++, data.getCadDivision(), true); // CAD구분
			setCellValue(workbook, row, columnIndex++, data.getNumber(), false); // 도면번호
			setCellValue(workbook, row, columnIndex++, data.getRev(), true); // 버전
			setCellValue(workbook, row, columnIndex++, data.getName(), false); // 도면명
			
			setCellValue(workbook, row, columnIndex++, (String)attributes.get(CadAttributeDNC.ATT_MATERIAL.getKey()), false); // 재질
			setCellValue(workbook, row, columnIndex++, (String)attributes.get(CadAttributeDNC.ATT_TREATMENT.getKey()), false); // 후처리
			setCellValue(workbook, row, columnIndex++, (String)attributes.get(CadAttributeDNC.ATT_CATEGORY.getKey()), false); // 분류
			
			setCellValue(workbook, row, columnIndex++, data.getLocation(), false); // 도면분류
			setCellValue(workbook, row, columnIndex++, data.getStateName(), true); // 상태
			setCellValue(workbook, row, columnIndex++, data.getCreatorFullName(), true); // 작성자
			setCellValue(workbook, row, columnIndex++, data.getCreateDateFormat(), true); // 작성일
			setCellValue(workbook, row, columnIndex++, data.getModifyDateFormat(), true); // 최종수정일
		}
		// workbook write/close
		workbookOutput(workbook, response);
	}
	@Override
	public void excelDownEpmPartState(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 파라미터 저장
		Map<String, Object> reqMap = parameterMap(request); 
		List<EpmPartStateData> list = ExcelDownHelper.manager.getEpmPartStateList(reqMap);
		
		// 엑셀 템플릿 가져오기
		SXSSFWorkbook workbook = getSXSSFWorkbook(epmPartStateExcelForm, request, response);
		Sheet sheet = workbook.getSheetAt(0);
		
		Row row = null;
		int rowIdx = 0;
		for(EpmPartStateData data : list){
			row = sheet.createRow(++rowIdx);
			int columnIndex = 0;
			setCellValue(workbook, row, columnIndex++, data.getEpmNumber(), true);
			setCellValue(workbook, row, columnIndex++, data.getEpmRev(), false);
			setCellValue(workbook, row, columnIndex++, data.getEpmName(), true);
			setCellValue(workbook, row, columnIndex++, data.getEpmStateName(), false);
			setCellValue(workbook, row, columnIndex++, data.getEpmCreatorFullName(), false);
			setCellValue(workbook, row, columnIndex++, data.getPartNumber(), true);
			setCellValue(workbook, row, columnIndex++, data.getPartRev(), false);
			setCellValue(workbook, row, columnIndex++, data.getPartName(), true);
			setCellValue(workbook, row, columnIndex++, data.getPartStateName(), false);
			setCellValue(workbook, row, columnIndex++, data.getPartCreatorFullName(), false);
		}
		// workbook write/close
		workbookOutput(workbook, response);
	}
	/**
	 * @desc	: 부품 엑셀 다운로드
	 * @author	: mnyu
	 * @date	: 2019. 11. 22.
	 * @method	: excelDownPart
	 * @return void
	 */
	@Override
	public void excelDownPart(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		// 파라미터 저장
//		Map<String, Object> reqMap = parameterMap(request); 
//		List<PartData> list = ExcelDownHelper.manager.getPartList(reqMap);
//		
//		// 엑셀 템플릿 가져오기
//		SXSSFWorkbook workbook = getSXSSFWorkbook(partExcelForm, request, response);
//		Sheet sheet = workbook.getSheetAt(0);
//		
//		Row row = null;
//		int rowIdx = 0;
//		for(PartData data : list){
//			
//			Map<String, Object> attributes = CommonHelper.manager.getAttributes(data.getOid());
//			
//			row = sheet.createRow(++rowIdx);
//			int columnIndex = 0;
//			setCellValue(workbook, row, columnIndex++, data.getNumber(), false); // 번호
//			setCellValue(workbook, row, columnIndex++, data.getRev(), true); // 버전
//			setCellValue(workbook, row, columnIndex++, data.getName(), false); // 이름
//			setCellValue(workbook, row, columnIndex++, data.getUnit(), true); // 단위
//			setCellValue(workbook, row, columnIndex++, StringUtil.checkNull(attributes.get(CadAttributeDNC.ATT_TREATMENT.getKey())), true); // 후처리
//			setCellValue(workbook, row, columnIndex++, StringUtil.checkNull(attributes.get(CadAttributeDNC.ATT_MATERIAL.getKey())), true); // 재질
//			setCellValue(workbook, row, columnIndex++, StringUtil.checkNull(attributes.get(CadAttributeDNC.ATT_CATEGORY.getKey())), true); // 분류
//			setCellValue(workbook, row, columnIndex++, data.getStateName(), true); // 상태
//			setCellValue(workbook, row, columnIndex++, data.getCreatorFullName(), true); // 작성자
//			setCellValue(workbook, row, columnIndex++, data.getCreateDateFormat(), true); // 작성일
//			setCellValue(workbook, row, columnIndex++, data.getModifyDateFormat(), true); // 수정일
//			
//		}
//		// workbook write/close
//		workbookOutput(workbook, response);
	}
//	/**
//	 * @desc	: 배포 엑셀 다운로드
//	 * @author	: mnyu
//	 * @date	: 2019. 11. 22.
//	 * @method	: excelDownDistribute
//	 * @return void
//	 */
//	@Override
//	public void excelDownDistribute(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		// 파라미터 저장
//		Map<String, Object> reqMap = parameterMap(request); 
//		List<DistributeDocumentData> list = ExcelDownHelper.manager.getDistributeList(reqMap);
//		
//		// 엑셀 템플릿 가져오기
//		SXSSFWorkbook workbook = getSXSSFWorkbook(disExcelForm, request, response);
//		Sheet sheet = workbook.getSheetAt(0);
//		
//		Row row = null;
//		int rowIdx = 0;
//		for(DistributeDocumentData data : list){
//			row = sheet.createRow(++rowIdx);
//			int columnIndex = 0;
//			setCellValue(workbook, row, columnIndex++, data.getDistributeNumber(), true);
//			setCellValue(workbook, row, columnIndex++, data.getDistributeName(), false);
//			setCellValue(workbook, row, columnIndex++, data.getDistributeTypeName(), true);
//			setCellValue(workbook, row, columnIndex++, data.getClassification(), true);
//			setCellValue(workbook, row, columnIndex++, data.getCadTypeStr(), true);
//			setCellValue(workbook, row, columnIndex++, data.getPjtNo(), true);
//			setCellValue(workbook, row, columnIndex++, data.getOrderTypeStr(), true);
//			setCellValue(workbook, row, columnIndex++, data.getRequesterName(), true);
//			setCellValue(workbook, row, columnIndex++, data.getSupplierName(), false);
//			setCellValue(workbook, row, columnIndex++, data.getCreateDateFormat(), true);
//			setCellValue(workbook, row, columnIndex++, data.getCreator(), true);
//			setCellValue(workbook, row, columnIndex++, data.getStateName(), true);
//		}
//		// workbook write/close
//		workbookOutput(workbook, response);
//	}
//	/**
//	 * @desc	: 접수 엑셀 다운로드
//	 * @author	: mnyu
//	 * @date	: 2019. 11. 25.
//	 * @method	: excelDownReceipt
//	 * @return void
//	 */
//	@Override
//	public void excelDownReceipt(ReceiptData rec, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		List<ReceiptData> list = ExcelDownHelper.manager.getReceiptList(rec);
//		
//		// 엑셀 템플릿 가져오기
//		WritableWorkbook workbook = getWorkbook(receiptExcelForm, request, response);
//		
//		WritableSheet sheet = workbook.getSheet(0);
//		int row = 0;
//		for(ReceiptData data : list){
//			row ++;
//			int columnIndex = 0;
//			sheet.addCell(new Label(columnIndex++, row, data.getSubmit_number()));
//			sheet.addCell(new Label(columnIndex++, row, data.getSubmit_name()));
//			sheet.addCell(new Label(columnIndex++, row, data.getSubmit_typeStr()));
//			sheet.addCell(new Label(columnIndex++, row, data.getSubmit_classification_name()));
//			sheet.addCell(new Label(columnIndex++, row, data.getPjt_no()));
//			sheet.addCell(new Label(columnIndex++, row, data.getState_name()));
//			sheet.addCell(new Label(columnIndex++, row, data.getCompany_name()));
//			sheet.addCell(new Label(columnIndex++, row, data.getC_dateStr()));
//			sheet.addCell(new Label(columnIndex++, row, data.getReceptionist_name()));
//		}
//		// workbook write/close
//		workbookOutput(workbook, response);
//	}
//	/**
//	 * @desc	: 업체 목록 엑셀 다운로드
//	 * @author	: mnyu
//	 * @date	: 2019. 11. 25.
//	 * @method	: excelDownSupplier
//	 * @return void
//	 */
//	@Override
//	public void excelDownSupplier(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		// 파라미터 저장
//		Map<String, Object> reqMap = parameterMap(request); 
//		List<SupplierData> list = ExcelDownHelper.manager.getSupplierList(reqMap);
//		
//		// 엑셀 템플릿 가져오기
//		WritableWorkbook workbook = getWorkbook(supplierExcelForm, request, response);
//		
//		WritableSheet sheet = workbook.getSheet(0);
//		int row = 0;
//		for(SupplierData data : list){
//			row ++;
//			int columnIndex = 0;
//			sheet.addCell(new Label(columnIndex++, row, data.getSupplierCode()));
//			sheet.addCell(new Label(columnIndex++, row, data.getSupplierName()));
//			sheet.addCell(new Label(columnIndex++, row, data.getSupplierID()));
//			sheet.addCell(new Label(columnIndex++, row, data.getNationalCode()));
//			sheet.addCell(new Label(columnIndex++, row, data.getBusinessCondition()));
//			sheet.addCell(new Label(columnIndex++, row, data.getBusinessType()));
//			sheet.addCell(new Label(columnIndex++, row, data.getEmail()));
//			sheet.addCell(new Label(columnIndex++, row, data.getTelNo()));
//			sheet.addCell(new Label(columnIndex++, row, data.getFaxNo()));
//			sheet.addCell(new Label(columnIndex++, row, data.getEnabled()));
//		}
//		// workbook write/close
//		workbookOutput(workbook, response);
//	}
//	
//
//	/**
//	 * @desc	: 정규 배포 엑셀 다운로드
//	 * @author	: mnyu
//	 * @date	: 2020. 1. 15.
//	 * @method	: excelDownRegularDistribute
//	 * @return void
//	 */
//	@Override
//	public void excelDownRegularDistribute(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		// 파라미터 저장
//		Map<String, Object> reqMap = parameterMap(request); 
//		List<DistributeDocumentData> list = ExcelDownHelper.manager.getRegularDistribute(reqMap);
//		
//		String orderType = StringUtil.checkNull((String) reqMap.get("orderType"));	// 구매 발주(P),견적 발주(E)
//		String form = disPurchaseExcelForm;
//		if(orderType.equals("E")){ // 견적
//			form = disEstimatetExcelForm;
//		}
//		
//		// 엑셀 템플릿 가져오기
//		SXSSFWorkbook workbook = getSXSSFWorkbook(form, request, response);
//		Sheet sheet = workbook.getSheetAt(0);
//
//		Row row = null;
//		int rowIdx = 0;
//		for(DistributeDocumentData data : list){
//			row = sheet.createRow(++rowIdx);
//			int columnIndex = 0;
//			setCellValue(workbook, row, columnIndex++, data.getDistributeNumber(), true);
//			setCellValue(workbook, row, columnIndex++, data.getDistributeName(), false);
//			if(orderType.equals("P")){ // 구매
//				setCellValue(workbook, row, columnIndex++, data.getPjtNo(), true);
//			}
//			setCellValue(workbook, row, columnIndex++, data.getRequestNumber(), true);
//			setCellValue(workbook, row, columnIndex++, data.getSupplierName(), false);
//			setCellValue(workbook, row, columnIndex++, data.getCreateDateFormat(), true);
//			setCellValue(workbook, row, columnIndex++, data.getRequesterName(), true);
//		}
//		// workbook write/close
//		workbookOutput(workbook, response);
//	}

	/**
	 * @desc	: 배포 대상 Item 엑셀 다운로드
	 * @author	: mnyu
	 * @date	: 2020. 1. 3.
	 * @method	: excelDownDistributeItem
	 * @return void
	 */
	/*
	@Override
	public void excelDownDistributeItem(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 파라미터 저장
		Map<String, Object> reqMap = parameterMap(request); 
		List<DistributeItemData> list = ExcelDownHelper.manager.getDistributeItemList(reqMap);
		
		// 엑셀 템플릿 가져오기
		WritableWorkbook workbook = getWorkbook(disItemExcelForm, request, response);
		
		WritableSheet sheet = workbook.getSheet(0);
		int row = 0;
		for(DistributeItemData data : list){
			row ++;
			int columnIndex = 0;
			sheet.addCell(new Label(columnIndex++, row, data.getDistributeNumber()));
			sheet.addCell(new Label(columnIndex++, row, data.getDistributeName()));
			sheet.addCell(new Label(columnIndex++, row, data.getSupplierName()));
			sheet.addCell(new Label(columnIndex++, row, data.getStateName()));
			sheet.addCell(new Label(columnIndex++, row, data.getCreateDateFormat()));
			sheet.addCell(new Label(columnIndex++, row, data.getPjtNo()));
			sheet.addCell(new Label(columnIndex++, row, data.getRequestNumber()));
			sheet.addCell(new Label(columnIndex++, row, data.getType()));
			sheet.addCell(new Label(columnIndex++, row, data.getNumber()));
			sheet.addCell(new Label(columnIndex++, row, data.getName()));
			sheet.addCell(new Label(columnIndex++, row, data.getVersion()));
		}
		// workbook write/close
		workbookOutput(workbook, response);
	}*/
//	/**
//	 * @desc	: 배포 부품 엑셀 다운로드
//	 * @author	: mnyu
//	 * @date	: 2020. 1. 7.
//	 * @method	: excelDownDistributePart
//	 * @return void
//	 */
//	@Override
//	public void excelDownDistributePart(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		// 파라미터 저장
//		Map<String, Object> reqMap = parameterMap(request); 
//		List<DistributeToPartLinkData> list = ExcelDownHelper.manager.getDistributePartList(reqMap);
//		
//		// 엑셀 템플릿 가져오기
//		SXSSFWorkbook workbook = getSXSSFWorkbook(disPartExcelForm, request, response);
//		Sheet sheet = workbook.getSheetAt(0);
//		
//		Row row = null;
//		int rowIdx = 0;
//		for(DistributeToPartLinkData data : list){
//			row = sheet.createRow(++rowIdx);
//			int columnIndex = 0;
//			setCellValue(workbook, row, columnIndex++, data.getOrderTypeStr(), true);
//			setCellValue(workbook, row, columnIndex++, data.getRequestNumber(), true);
//			setCellValue(workbook, row, columnIndex++, data.getDistributeNumber(), true);
//			setCellValue(workbook, row, columnIndex++, data.getSupplierName(), false);
//			setCellValue(workbook, row, columnIndex++, data.getPartNumber(), false);
//			setCellValue(workbook, row, columnIndex++, data.getEpmNumber(), true);
//			setCellValue(workbook, row, columnIndex++, data.getLinkStateStr(), true);
//			setCellValue(workbook, row, columnIndex++, data.getDownloadDeadline(), true);
//		}
//		// workbook write/close
//		workbookOutput(workbook, response);
//	}
	
	/**
	 * @desc	: NumberCode 엑셀 다운로드
	 * @author	: mnyu
	 * @date	: 2020. 2. 21.
	 * @method	: excelDownNumberCode
	 * @return void
	 */
	@Override
	public void excelDownNumberCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 파라미터 저장
		Map<String, Object> reqMap = parameterMap(request); 
		
		String codeType = StringUtil.checkNull((String) reqMap.get("codeTypeValue"));
		List<NumberCodeData> list = ExcelDownHelper.manager.getNumberCodeList(codeType);
		
		// 엑셀 템플릿 가져오기
		SXSSFWorkbook workbook = getSXSSFWorkbook(numberCodeExcelForm, request, response);
		Sheet sheet = workbook.getSheetAt(0);

		Row row = null;
		int rowIdx = 0;
		for(NumberCodeData data : list){
			row = sheet.createRow(++rowIdx);
			int columnIndex = 0;
			setCellValue(workbook, row, columnIndex++, data.getCodeTypeName(), true);
			setCellValue(workbook, row, columnIndex++, data.getParentCode(), true);
			setCellValue(workbook, row, columnIndex++, data.getParentName(), true);
			setCellValue(workbook, row, columnIndex++, data.getCode(), true);
			setCellValue(workbook, row, columnIndex++, data.getName(), true);
			setCellValue(workbook, row, columnIndex++, data.getEngName(), true);
			setCellValue(workbook, row, columnIndex++, data.getDescription(), false);
			setCellValue(workbook, row, columnIndex++, data.getSort()+"", true);
			setCellValue(workbook, row, columnIndex++, data.isActive()+"", true);
		}
		// workbook write/close
		workbookOutput(workbook, response);
	}
	
	
	
	/**
	 * @desc	: ECR 엑셀 다운로드
	 * @author	: tsjeong
	 * @date	: 2020. 10. 07.
	 * @method	: excelDownECR
	 * @return void
	 */
	@Override
	public void excelDownECR(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 파라미터 저장
		Map<String, Object> reqMap = parameterMap(request); 
		
		List<ECRData> list = ExcelDownHelper.manager.getECRList(reqMap);
		
		// 엑셀 템플릿 가져오기
		SXSSFWorkbook workbook = getSXSSFWorkbook(ecrExcelForm, request, response);
		Sheet sheet = workbook.getSheetAt(0);
		
		Row row = null;
		int rowIdx = 0;
		for(ECRData data : list){
			row = sheet.createRow(++rowIdx);
			int columnIndex = 0;
			setCellValue(workbook, row, columnIndex++, data.getEcrNumber(), true); //번호
			setCellValue(workbook, row, columnIndex++, data.getName(), false); //제목
			setCellValue(workbook, row, columnIndex++, data.getState(), true); //작업현황
			setCellValue(workbook, row, columnIndex++, data.getCreator(), true); //등록자
			setCellValue(workbook, row, columnIndex++, data.getCreateDate(), true); //최초등록일
		}
		// workbook write/close
		workbookOutput(workbook, response);
	}
	
	/**
	 * @desc	: ECO 엑셀 다운로드
	 * @author	: tsjeong
	 * @date	: 2020. 10. 07.
	 * @method	: excelDownECO
	 * @return void
	 */
	@Override
	public void excelDownECO(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 파라미터 저장
		Map<String, Object> reqMap = parameterMap(request); 
		
		List<ECOData> list = ExcelDownHelper.manager.getECOList(reqMap);
		
		// 엑셀 템플릿 가져오기
		SXSSFWorkbook workbook = getSXSSFWorkbook(ecoExcelForm, request, response);
		Sheet sheet = workbook.getSheetAt(0);
		
		Row row = null;
		int rowIdx = 0;
		for(ECOData data : list){
			row = sheet.createRow(++rowIdx);
			int columnIndex = 0;
			setCellValue(workbook, row, columnIndex++, data.getOrderNumber(), false); //ECO 번호
			setCellValue(workbook, row, columnIndex++, data.getName(), true); //ECO 제목
			setCellValue(workbook, row, columnIndex++, data.getState(), true); //작업현황
			setCellValue(workbook, row, columnIndex++, data.getCreator(), true); //등록자
			setCellValue(workbook, row, columnIndex++, data.getCreateDate(), true); //최초등록일
			setCellValue(workbook, row, columnIndex++, data.getCarType(), true); //차종
			setCellValue(workbook, row, columnIndex++, data.getChangeDesc(), false); //내역
			setCellValue(workbook, row, columnIndex++, data.getChangeOwner(), false); //주관
			setCellValue(workbook, row, columnIndex++, data.getUpg(), true); //UPG
			setCellValue(workbook, row, columnIndex++, data.getApplyDate(), true); //적용요구시점
		}
		// workbook write/close
		workbookOutput(workbook, response);
	}

	/**
	 * @desc	: project 엑셀 다운로드
	 * @author	: tsjeong
	 * @date	: 2020. 10. 07.
	 * @method	: excelDownProject
	 * @return void
	 */
	@Override
	public void excelDownProject(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 파라미터 저장
		Map<String, Object> reqMap = parameterMap(request); 
		
		List<ProjectData> list = ExcelDownHelper.manager.getProjectList(reqMap);
		
		// 엑셀 템플릿 가져오기
		SXSSFWorkbook workbook = getSXSSFWorkbook(projectExcelForm, request, response);
		Sheet sheet = workbook.getSheetAt(0);
		
		Row row = null;
		int rowIdx = 0;
		for(ProjectData data : list){
			row = sheet.createRow(++rowIdx);
			int columnIndex = 0;
			setCellValue(workbook, row, columnIndex++, data.getCode(), false); //프로젝트 번호
			setCellValue(workbook, row, columnIndex++, data.getName(), true); //프로젝트 명
			setCellValue(workbook, row, columnIndex++, data.getProjectTypeDisplay(), true); //인증 타입
			setCellValue(workbook, row, columnIndex++, data.getPlanDuration()+"("+data.getPlanDurationHoliday()+")", true); //기간
			setCellValue(workbook, row, columnIndex++, data.getPlanStartDate(), true); //계획 시작일
			setCellValue(workbook, row, columnIndex++, data.getPlanEndDate(), true); // 계획 종료일
			setCellValue(workbook, row, columnIndex++, data.getPmName(), false); //PM
			setCellValue(workbook, row, columnIndex++, data.getCreatorFullName(), false); //등록자
			setCellValue(workbook, row, columnIndex++, data.getCreateDate(), true); //최초 등록일
		}
		// workbook write/close
		workbookOutput(workbook, response);
	}
	
	/**
	 * @desc	: User 엑셀 다운로드
	 * @author	: tsjeong
	 * @date	: 2020. 10. 07.
	 * @method	: excelDownUser
	 * @return void
	 */
	@Override
	public void excelDownUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 파라미터 저장
		Map<String, Object> reqMap = parameterMap(request); 
		
		List<PeopleData> list = ExcelDownHelper.manager.getUserList(reqMap);
		
		// 엑셀 템플릿 가져오기
		SXSSFWorkbook workbook = getSXSSFWorkbook(userExcelForm, request, response);
		Sheet sheet = workbook.getSheetAt(0);
		
		Row row = null;
		int rowIdx = 0;
		for(PeopleData data : list){
			row = sheet.createRow(++rowIdx);
			int columnIndex = 0;
			setCellValue(workbook, row, columnIndex++, data.getId(), false); //아이디
			setCellValue(workbook, row, columnIndex++, data.getName(), true); //이름
			setCellValue(workbook, row, columnIndex++, data.getDepartmentName(), true); //부서
			setCellValue(workbook, row, columnIndex++, data.getDuty(), true); //직위
			setCellValue(workbook, row, columnIndex++, data.getEmail(), true); //이메일
			setCellValue(workbook, row, columnIndex++, data.getOfficeTel(), true); // 회사번호
			setCellValue(workbook, row, columnIndex++, data.getCellTel(), false); //개인번호
			setCellValue(workbook, row, columnIndex++, data.getDisableKor(), false); //재직 여부
		}
		// workbook write/close
		workbookOutput(workbook, response);
	}
	
	/**
	 * @desc	: 엑셀 업로드 템플릿 다운로드
	 * @author	: shkim
	 * @date	: 2020. 10. 28.
	 * @method	: excelDownEpmUploadTemplate
	 * @param   : req, res
	 * @return  : void
	 */
	@Override
	public void excelDownEpmUploadTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] secondaryList = (String[]) request.getParameterValues("SECONDARY");
		
		SXSSFWorkbook workbook = getSXSSFWorkbook(epmUploadForm, request, response);
		Sheet sheet = workbook.getSheetAt(0);
		
		Row row = null;
		int rowIdx = 0;
		if(secondaryList != null && secondaryList.length > 0) {
			for(String secondary : secondaryList) {
				String fileName = secondary.split("/")[1];
				
				row = sheet.createRow(++rowIdx);
				setCellValue(workbook, row, 1, fileName.toUpperCase(), true);
				setCellValue(workbook, row, 3, fileName, true);
			}
		}
		workbookOutput(workbook, response);
	}
	
	/**
	 * @desc	: 엑셀 템플릿 다운로드
	 * @author	: tsjeong
	 * @date	: 2020. 12. 04.
	 * @method	: excelDownTemplate
	 * @return void
	 */
	@Override
	public void excelDownTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 파라미터 저장
		Map<String, Object> reqMap = parameterMap(request);
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		List<ProjectGanttViewTaskData> list = ProjectHelper.manager.getProjectGanttViewTask(reqMap);
		EProject project = (EProject) CommonUtil.getObject(oid);
		// 엑셀 템플릿 가져오기
		SXSSFWorkbook workbook = getSXSSFWorkbook2(ganttExcelForm, project, request, response);
		Sheet sheet = workbook.getSheetAt(0);
		Row row = null;
		int rowIdx = 2;
		int num = 0;
		List<String> overlapChkList = new ArrayList<String>();
		int seq = 0;
		for (ProjectGanttViewTaskData data : list) {
			ETask task = null;
			int outputRow = 0;
			if(CommonUtil.getObject(data.getId()) instanceof ETask) {
			    String preTaskName = "";
			    task = (ETask) CommonUtil.getObject(data.getId());
			    if (task != null) {
//					ETask preTask = ExcelDownHelper.manager.getPreTask(task);
//					if (preTask != null) {
//						preTaskName = preTask.getName();
//					}
					preTaskName = ExcelDownHelper.manager.getPreTaskList(task);
				    List<EOutput> outputList = new ArrayList<EOutput>();
				    outputList = OutputHelper.manager.getOutput(task);
				    List<ProjectRoleData> roleData = data.getTaskRole();
				    String roleName = "";
				    String levelOne = "";
				    String levelTwo = "";
				    String levelThree = "";
				    boolean taskSubFlag = false;
				    if(task.getParent() instanceof EProject) {
				    	taskSubFlag = ExcelDownHelper.manager.getCheckETaskSub(task, project);//task가 하위가 존재하면 false
				    	levelOne = task.getName();
				    }else if(task.getParent().getParent() instanceof EProject) {
				    	taskSubFlag = ExcelDownHelper.manager.getCheckETaskSub(task, project);//task가 하위가 존재하면 false
					    if(task.getSort()==1) {
					    	levelOne = task.getParent().getName();
					    	levelTwo = task.getName();
					    }else {
					    	levelOne = "";
					    	levelTwo = task.getName();
					    }
				    }else {
				    	taskSubFlag = ExcelDownHelper.manager.getCheckETaskSub(task, project);//task가 하위가 존재하면 false
					    if(task.getSort()==1) {
					    	levelOne = task.getParent().getParent().getName();
					    	levelTwo = task.getParent().getName();
					    	levelThree = task.getName();
					    }else {
					    	levelOne = "";
					    	levelTwo = "";
					    	levelThree = task.getName();
					    }
				    }
				    
				    if(taskSubFlag) {
				    	if(levelOne != "") {
					    	boolean isOverlap = false;
					    	
					    	if(overlapChkList.size() > 0) {
					    		for(String chk : overlapChkList) {
						    		if(chk.equals(levelOne)) {
						    			isOverlap = true;
						    		}
						    	}
					    	}
					    	if(isOverlap) {
					    		levelOne = "";
					    	}else {
					    		overlapChkList.add(levelOne);
					    	}
					    	
					    }
				    	row = sheet.createRow(++rowIdx);
				    	outputRow = rowIdx;
				    	int tempRow = rowIdx;
				    	int columnIndex = 0;
				    	for(ProjectRoleData role : roleData) {
				    		if("".equals(roleName)) {
				    			roleName = role.getRoleName();
				    		}else {
				    			roleName = roleName +", "+ role.getRoleName();
				    		}
				    	}
				    	setCellValue(workbook, row, columnIndex++, String.valueOf(num++), true);		// No.
				    	setCellValue(workbook, row, columnIndex++, levelOne, true);			// 1Level
				    	setCellValue(workbook, row, columnIndex++, levelTwo, true);			// 2Level
				    	setCellValue(workbook, row, columnIndex++, levelThree, true);			// 3Level
				    	setCellValue(workbook, row, columnIndex++, data.getDescription(), true);		// 상세설명
				    	setCellValue(workbook, row, columnIndex++, String.valueOf(data.getDuration()), false);	// 기간(일)
				    	long pOid = CommonUtil.getOIDLongValue(task);
						String taskName = String.valueOf(pOid);
				    	setCellValue(workbook, row, columnIndex++, taskName, true);			// TASK 명
				    	setCellValue(workbook, row, columnIndex++, preTaskName, false);			// 선행 태스크 명
				    	setCellValue(workbook, row, 12, roleName, false);			// Role
				    	//setCellValue(workbook, row, columnIndex++, String.valueOf(data.getProgress()), false);// 진행률
				    	if(outputList.size() > 0) {
				    		int outputNameColumnIndex = columnIndex;
				    		int outputPathColumnIndex = columnIndex+1;
				    		int blankColumnIndex = columnIndex+2;
				    		int outputTypeColumnIndex = columnIndex+3;
				    		for(int i=0; i < outputList.size(); i++) {
				    			EOutput output = outputList.get(i);
				    			if(i != 0 ) {
				    				row = sheet.createRow(outputRow);
				    			}
				    			setCellValue(workbook, row, outputNameColumnIndex, output.getName(), false);				// 산출물 정의 명
				    			setCellValue(workbook, row, outputPathColumnIndex, output.getLocation().replaceAll("/Default/Document", ""), false);				// 산출물 경로
				    			setCellValue(workbook, row, blankColumnIndex, "", false);			// ""
				    			setCellValue(workbook, row, outputTypeColumnIndex, output.getDocType(), false);			// 산출물 인증타입
				    			rowIdx = outputRow;
				    			++outputRow;
				    		}
				    		if(tempRow != rowIdx) {
				    			sheet. addMergedRegion (new CellRangeAddress(( int) tempRow , ( int )rowIdx , ( int) 0, (int )0 ));
				    			sheet. addMergedRegion (new CellRangeAddress(( int) tempRow , ( int )rowIdx , ( int) 1, (int )1 ));
				    			sheet. addMergedRegion (new CellRangeAddress(( int) tempRow , ( int )rowIdx , ( int) 2, (int )2 ));
				    			sheet. addMergedRegion (new CellRangeAddress(( int) tempRow , ( int )rowIdx , ( int) 3, (int )3 ));
				    			sheet. addMergedRegion (new CellRangeAddress(( int) tempRow , ( int )rowIdx , ( int) 4, (int )4 ));
				    			sheet. addMergedRegion (new CellRangeAddress(( int) tempRow , ( int )rowIdx , ( int) 5, (int )5 ));
				    			sheet. addMergedRegion (new CellRangeAddress(( int) tempRow , ( int )rowIdx , ( int) 6, (int )6 ));
				    			sheet. addMergedRegion (new CellRangeAddress(( int) tempRow , ( int )rowIdx , ( int) 7, (int )7 ));
				    			sheet. addMergedRegion (new CellRangeAddress(( int) tempRow , ( int )rowIdx , ( int) 10, (int )10 ));
				    			sheet. addMergedRegion (new CellRangeAddress(( int) tempRow , ( int )rowIdx , ( int) 11, (int )11 ));
				    		}
				    	}
				    }
			    }
			}
		    
		}
		// workbook write/close
		workbookOutput(workbook, response);
	    }
	
	
	/**
	 * 프로젝트 산출물 리스트 엑셀 Export
	 * 
	 * @author hckim
	 * @date 2021. 01. 18
	 * @param req
	 * @param res
	 */
	@Override
	public void excelDownProjectOutput(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 파라미터 저장
		Map<String, Object> reqMap = parameterMap(request); 
		List<ProjectOutputData> list = ExcelDownHelper.manager.getProjectOutputList(reqMap);
		
		// 엑셀 템플릿 가져오기
		SXSSFWorkbook workbook = getSXSSFWorkbook(projectOutputForm, request, response);
		Sheet sheet = workbook.getSheetAt(0);
		
		Row row = null;
		int rowIdx = 0;
		for(ProjectOutputData data : list){
			row = sheet.createRow(++rowIdx);
			int columnIndex = 0;
			setCellValue(workbook, row, columnIndex++, data.getTaskName(), false);
			setCellValue(workbook, row, columnIndex++, data.getName(), false);
			setCellValue(workbook, row, columnIndex++, data.getRoleName(), false);
			setCellValue(workbook, row, columnIndex++, data.getDocTypeDisplay(), true);
			setCellValue(workbook, row, columnIndex++, data.getDocStateName(), true);
			setCellValue(workbook, row, columnIndex++, data.getDocNumber(), true);
			setCellValue(workbook, row, columnIndex++, data.getDocCreateDate(), true);
		}
		
		// workbook write/close
		workbookOutput(workbook, response);
	}
	
	/**
	 * 엑셀 간트 Excel Download
	 * @author hckim
	 * @date 2021. 01. 27
	 * @param req
	 * @param res
	 */
	@Override
	public void excelDownGanttExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] projectOids = request.getParameterValues("excelProjectList");
		
		List<ProjectGanttViewTaskData> tasks = new ArrayList<ProjectGanttViewTaskData>();
		List<ProjectGanttLinkData> links = new ArrayList<ProjectGanttLinkData>();
		for(String oid : projectOids) {
			Map<String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("oid", oid);
			
			List<ProjectGanttViewTaskData> tempList = ProjectHelper.manager.getProjectGanttViewTask(reqMap);
			tempList.remove(tempList.size()-1);
			
			for(ProjectGanttViewTaskData taskData : tempList) {
				tasks.add(taskData);
			}
			
			//링크
			List<ProjectGanttLinkData> tempLink = ProjectHelper.manager.getProjectGanttLink(reqMap);
			for(ProjectGanttLinkData linkData : tempLink) {
				links.add(linkData);
			}
		}
		
		
		String[] arrWbs = request.getParameterValues("excelGanttWBS");
		
		// 엑셀 템플릿 가져오기
		SXSSFWorkbook workbook = getSXSSFWorkbook(projectGanttForm, request, response);
		Sheet sheet = workbook.getSheetAt(0);
		
		Row row = null;
		int rowIdx = 0;
		for(ProjectGanttViewTaskData data : tasks){
			row = sheet.createRow(++rowIdx);
			int columnIndex = 0;
			
			setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(arrWbs[rowIdx-1], ""), false);
			setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(data.getText(),""), false);
			setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(String.valueOf(Math.round(data.getProgress()*100))+"%", ""), true);
			setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(data.getStart_date(),""), true);
			setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(data.getEndDate(),""), true);
			setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(data.getReal_start(),""), true);
			setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(data.getReal_end(),""), true);
			setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(String.valueOf(data.getDuration()),""), false);
			
			//ROLE
			StringBuilder excelRoleText = new StringBuilder();
			List<ProjectRoleData> roleList = data.getTaskRole(); 
			if(roleList!=null && roleList.size() > 0) {
				
				for(ProjectRoleData role : roleList) {
					String userName = role.getUserName();
					if(userName != null&& !"".equals(userName)) {
						excelRoleText.append(role.getRoleName()+"("+role.getUserName()+")\n");
					}else{
						excelRoleText.append(role.getRoleName()+"(미지정)\n");
					}
					
				}
				
			}
			setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(excelRoleText.toString().trim(),""), false);
			
			//선행
			String taskId = data.getId();
			for(ProjectGanttLinkData link : links) {
				if(taskId.equals(link.getTarget())) {
					ETask preTask = (ETask)CommonUtil.getObject(link.getSource());
					setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(preTask.getName(),""), false);
				}
			}
			
		}
		
		// workbook write/close
		workbookOutput(workbook, response);
	}
	
	
	/**
	 * @desc	: POI 엑셀 수정
	 * @author	: gs
	 * @date	: 2020. 12. 08.
	 * @method	: getSXSSFWorkbook2
	 * @return	: Workbook
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	private SXSSFWorkbook getSXSSFWorkbook2(String form,EProject project, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 엑셀 템플릿 가져오기
		FileInputStream fis = new FileInputStream(location + form);
		Workbook workbook = new XSSFWorkbook(fis);
		Sheet sheet = workbook.getSheetAt(0);
		Row headerRow = sheet.getRow(1);
	    Cell headerCell1 = headerRow.createCell(0);
	    headerCell1.setCellValue(project.getName()+"_Template");
	    Cell headerCell2 = headerRow.createCell(3);
	    headerCell2.setCellValue(project.getProjectType());
	    Cell headerCell3 = headerRow.createCell(4);
	    headerCell3.setCellValue("O");
	    Cell headerCell4 = headerRow.createCell(5);
	    headerCell4.setCellValue(project.getDescription());
		// SXSSF 생성
	    SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook((XSSFWorkbook) workbook, 100);
	    
		response.setContentType("application/msexcel");
	    response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", URLEncoder.encode(form,"UTF-8")));

		return sxssfWorkbook;
	}

	
	/**
	 * @desc	: WBS 엑셀 Import /Export 기능 
	 * @author	: gs
	 * @date	: 2021. 05. 24.
	 * @return	: Workbook
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@Override
	public void excelDownGanttExcel2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] projectOids = request.getParameterValues("excelProjectList");

		List<ProjectGanttViewTaskData> tasks = new ArrayList<ProjectGanttViewTaskData>();
		List<ProjectGanttLinkData> links = new ArrayList<ProjectGanttLinkData>();
		for(String oid : projectOids) {
			Map<String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("oid", oid);
			
			List<ProjectGanttViewTaskData> tempList = ProjectHelper.manager.getProjectGanttViewTask(reqMap);
			tempList.remove(tempList.size()-1);
			
			for(ProjectGanttViewTaskData taskData : tempList) {
				tasks.add(taskData);
			}
			
			//링크
			List<ProjectGanttLinkData> tempLink = ProjectHelper.manager.getProjectGanttLink(reqMap);
			for(ProjectGanttLinkData linkData : tempLink) {
				links.add(linkData);
			}
		}
		
		String[] arrWbs = request.getParameterValues("excelGanttWBS");
		
		// 엑셀 템플릿 가져오기
		SXSSFWorkbook workbook = getSXSSFWorkbook(projectGanttForm, request, response);
		Sheet sheet = workbook.getSheetAt(0);
		
		Row row = null;
		int rowIdx = 0;
		
		EProject project = null;
		ETask task = null;
		for(ProjectGanttViewTaskData data : tasks){
			row = sheet.createRow(++rowIdx);
			int columnIndex = 0;
			
			setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(arrWbs[rowIdx-1], ""), false);
			setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(data.getText(),""), false);
			setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(String.valueOf(Math.round(data.getProgress()*100))+"%", ""), true);
			setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(data.getStart_date(),""), true);
			setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(data.getEndDate(),""), true);
			setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(data.getReal_start(),""), true);
			setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(data.getReal_end(),""), true);
			setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(String.valueOf(data.getDuration()),""), false);
			
			//ROLE
			StringBuilder excelRoleText = new StringBuilder();
			List<ProjectRoleData> roleList = data.getTaskRole(); 
			if(roleList!=null && roleList.size() > 0) {
				int i=0;
				for(ProjectRoleData role : roleList) {
					if(i > 0) {
						excelRoleText.append(",\n");
	    			}
					String userName = role.getUserName();
					if(userName != null&& !"".equals(userName)) {
						excelRoleText.append(role.getRoleName()+"("+role.getUserName()+")");
					}else{
						excelRoleText.append(role.getRoleName()+"(미지정)");
					}
					i++;
				}
				
			}
			setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(excelRoleText.toString().trim(),""), false);
			
			//선행
			boolean setValueCheck = true;
			String taskId = data.getId();
			StringBuilder preTaskText = new StringBuilder();
			if(links.size() > 0) {
				int i=0;
				for(ProjectGanttLinkData link : links) {
					if(taskId.equals(link.getTarget())) {
						if(i > 0) {
							preTaskText.append("&\n");
		    			}
						ETask preTask = (ETask)CommonUtil.getObject(link.getSource());
						preTaskText.append(preTask.getName());
						setValueCheck = false;
						i++;
					}
				}
			}
			
			if(setValueCheck) {
				setCellValue(workbook, row, columnIndex++, "", false);
			}else {
				setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(preTaskText.toString().trim(),""), false);
			}
			
			
			//태스크 코드 - 선행 코드
//			if(CommonUtil.getObject(data.getId()) instanceof ETask) {
//				task = (ETask) CommonUtil.getObject(data.getId());
//			    if (task != null) {
//			    	long pOid = CommonUtil.getOIDLongValue(task);
//					String taskCode = String.valueOf(pOid);
//			    	setCellValue(workbook, row, columnIndex++, taskCode, false);
//			    }
//			}else if(CommonUtil.getObject(data.getId()) instanceof EProject) {
//			    setCellValue(workbook, row, columnIndex++, "", false);
//			}
//		    
//		    setValueCheck = true;
//			String taskId2 = data.getId();
//			StringBuilder preTaskText2 = new StringBuilder();
//			if(links.size() > 0) {
//				int i=0;
//				for(ProjectGanttLinkData link : links) {
//					if(taskId2.equals(link.getTarget())) {
//						if(i > 0) {
//							preTaskText2.append(",\n");
//		    			}
//						ETask preTask = (ETask)CommonUtil.getObject(link.getSource());
//						long pOid = CommonUtil.getOIDLongValue(preTask);
//						String taskOid = String.valueOf(pOid);
//						preTaskText2.append(taskOid);
//						setValueCheck = false;
//						i++;
//					}
//				}
//			}
//			
//			if(setValueCheck) {
//				setCellValue(workbook, row, columnIndex++, "", false);
//			}else {
//				setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(preTaskText2.toString().trim(),""), false);
//			}
			
		    
			//산출물
			if(CommonUtil.getObject(data.getId()) instanceof ETask) {
			    task = (ETask) CommonUtil.getObject(data.getId());
			    if (task != null) {
				    List<EOutput> outputList = OutputHelper.manager.getOutput(task);
				    StringBuilder outputName = new StringBuilder();
			    	StringBuilder outputLocation = new StringBuilder();
			    	StringBuilder outputType = new StringBuilder();
			    	StringBuilder outputStep = new StringBuilder();
				    if(outputList.size() > 0) {
				    	int i=0;
			    		for(EOutput output : outputList) {
			    			if(i > 0) {
			    				outputName.append("&\n");
			    				outputLocation.append(",\n");
			    				outputType.append(",\n");
			    				outputStep.append(",\n");
			    			}
			    			
			    			if(output.getName() != null&& !"".equals(output.getName())) {
			    				outputName.append(output.getName());
							}
			    			if(output.getLocation() != null&& !"".equals(output.getLocation())) {
			    				outputLocation.append(output.getLocation().replaceAll("/Default/Document", ""));
							}
			    			if(output.getDocType() != null&& !"".equals(output.getDocType())) {
			    				outputType.append(output.getDocType());
							}
			    			if(output.getStep() != null) {
			    				outputStep.append(output.getStep().getCode());
							}
			    			i++;
			    		}
			    	}
				    setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(outputName.toString().trim(),""), false);				// 산출물 정의 명
	    			setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(outputLocation.toString().trim(),""), false);				// 산출물 경로
	    			setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(outputType.toString().trim(),""), false);			// 산출물 타입
	    			setCellValue(workbook, row, columnIndex++, StringUtil.checkReplaceStr(outputStep.toString().trim(),""), false);			// 산출물 인증타입
				    
			    }
			}else if(CommonUtil.getObject(data.getId()) instanceof EProject) {
			    project = (EProject) CommonUtil.getObject(data.getId());
			    setCellValue(workbook, row, columnIndex++, "", false);
    			setCellValue(workbook, row, columnIndex++, "", false);
    			setCellValue(workbook, row, columnIndex++, StringUtil.checkNull(project.getProjectType()), false);
    			setCellValue(workbook, row, columnIndex++, "", false);
			}
			
			
		}
		
		// workbook write/close
		workbookOutput(workbook, response);
	}

	@Override
	public void excelDownDistribute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void excelDownSupplier(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void excelDownRegularDistribute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void excelDownDistributePart(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
}
