package com.e3ps.stagegate.service;

import java.io.FileInputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.web.util.CookieGenerator;

import com.e3ps.common.content.service.CommonContentHelper;
import com.e3ps.common.content.util.ContentUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.project.EProject;
import com.e3ps.stagegate.SGObject;
import com.e3ps.stagegate.SGObjectMaster;
import com.e3ps.stagegate.SGObjectValue;
import com.e3ps.stagegate.StageGate;
import com.e3ps.stagegate.bean.SGObjectValueData;

import wt.clients.folder.FolderTaskLogic;
import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentItem;
import wt.content.ContentRoleType;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.folder.Folder;
import wt.folder.FolderEntry;
import wt.folder.FolderHelper;
import wt.fv.uploadtocache.CachedContentDescriptor;
import wt.inf.container.WTContainerRef;
import wt.lifecycle.LifeCycleHelper;
import wt.pdmlink.PDMLinkProduct;
import wt.pom.Transaction;
import wt.services.StandardManager;
import wt.util.EncodingConverter;
import wt.util.WTException;

@SuppressWarnings("serial")
public class StandardSgService extends StandardManager implements SgService{
	
	public static final String location = WCUtil.getWTHome()+"\\codebase\\excelTemplate\\";
	public static final String riskExcelForm = "risk.xlsx";
	public static final String cstopExcelForm = "cstop.xlsx";
	
	public static StandardSgService newStandardSgService() throws WTException {
		final StandardSgService instance = new StandardSgService();
		instance.initialize();
		return instance;
	}
	
	@Override
	public StageGate createGateAction(Map<String, Object> reqMap) throws Exception {
		StageGate sg = null;
		Transaction trx = new Transaction();
		
		try {
			trx.start();
			String projectOid = StringUtil.checkNull((String) reqMap.get("oid"));
			EProject project = (EProject) CommonUtil.getObject(projectOid);
			
			//
			sg = StageGate.newStageGate();
			sg.setProject(project);
			sg.setCode(project.getCode());
			// folder setting
			Folder folder = FolderTaskLogic.getFolder("/Default/StageGate", WCUtil.getWTContainerRef());
			FolderHelper.assignLocation((FolderEntry) sg, folder);
			PDMLinkProduct e3psProduct = WCUtil.getPDMLinkProduct();
			WTContainerRef wtContainerRef = WTContainerRef.newWTContainerRef(e3psProduct);
			LifeCycleHelper.setLifeCycle(sg,
					LifeCycleHelper.service.getLifeCycleTemplate("LC_Project", wtContainerRef));
			sg = (StageGate) PersistenceHelper.manager.save(sg);
			
			SgHelper.manager.createFoundation(project, sg);
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		
		return sg;
	}

	@Override
	public void deleteGateAction(StageGate sg) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();
			SgHelper.manager.deleteObject(sg);
			sg = (StageGate) PersistenceHelper.manager.refresh(sg);
			sg = (StageGate) PersistenceHelper.manager.delete(sg);
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}

	@Override
	public void modifyObjectValue(Map<String, Object> reqMap) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			String type = StringUtil.checkNull((String) reqMap.get("type"));
			String code = StringUtil.checkNull((String) reqMap.get("code"));
			String name = StringUtil.checkNull((String) reqMap.get("name"));
			String value0 = StringUtil.checkNull((String) reqMap.get("value0"));
			String value1 = StringUtil.checkNull((String) reqMap.get("value1"));
			String value2 = StringUtil.checkNull((String) reqMap.get("value2"));
			String value3 = StringUtil.checkNull((String) reqMap.get("value3"));
			String value4 = StringUtil.checkNull((String) reqMap.get("value4"));
			String value5 = StringUtil.checkNull((String) reqMap.get("value5"));
			String value6 = StringUtil.checkNull((String) reqMap.get("value6"));
			String value7 = StringUtil.checkNull((String) reqMap.get("value7"));
			String value8 = StringUtil.checkNull((String) reqMap.get("value8"));
			String value9 = StringUtil.checkNull((String) reqMap.get("value9"));
			
			SGObjectValue objValue = (SGObjectValue) CommonUtil.getObject(oid);
			SGObjectValueData data = new SGObjectValueData(objValue);
			JSONObject jObj = new JSONObject();
			if(type.equals("quality")) {
				jObj.put("code", data.getCode());
				jObj.put("name", data.getName());
				jObj.put("seq", data.getSeq());
				jObj.put("value0", value0);
				jObj.put("value1", value1);
				jObj.put("value2", value2);
				jObj.put("value3", value3);
				jObj.put("value4", value4);
				jObj.put("value5", value5);
				jObj.put("value6", value6);
				jObj.put("value7", value7);
				jObj.put("value8", value8);
				jObj.put("value9", value9);
			}else {
				jObj.put("code", code);
				jObj.put("name", name);
				jObj.put("seq", data.getSeq());
				jObj.put("value0", value0);
				jObj.put("value1", value1);
				jObj.put("value2", value2);
				jObj.put("value3", value3);
				jObj.put("value4", value4);
				jObj.put("value5", value5);
				jObj.put("value6", value6);
				jObj.put("value7", value7);
				jObj.put("value8", value8);
				jObj.put("value9", value9);
			}
			
			String jsonString = jObj.toJSONString();
			
			objValue.setDivision(jsonString);
			PersistenceHelper.manager.modify(objValue);
			String primary = StringUtil.checkNull((String) reqMap.get("PRIMARY"));
			List<String> secondary = StringUtil.checkReplaceArray(reqMap.get("SECONDARY"));
//			List<String> delocIds		= StringUtil.checkReplaceArray(reqMap.get("delocIds"));
			
			//attach files
			if(secondary.size() > 0) {
				SGObject obj = objValue.getObj();
				CommonContentHelper.service.attach((ContentHolder)obj, primary, secondary);
			}
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		
	}

	@Override
	public void uploadFile(Map<String, Object> fileMap) throws Exception {
		List<String> secondary = new ArrayList<String>();
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			Persistable per = null;
			String oid = StringUtil.checkNull((String) fileMap.get("oid"));
			String cacheId = StringUtil.checkNull((String) fileMap.get("cacheId"));
			String roleType = StringUtil.checkNull((String) fileMap.get("roleType"));
			String saveName = StringUtil.checkNull((String) fileMap.get("saveName"));
			String secondaryData = "";
			if(cacheId.length() > 0 && saveName.length() > 0 && roleType.length() > 0) {
				secondaryData = cacheId+"/"+saveName+"/"+roleType;
				secondary.add(secondaryData);
			}
			
			per = CommonUtil.getObject(oid);
			if(per instanceof SGObjectValue){
				SGObjectValue objValue = (SGObjectValue) per;
				if(secondary.size() > 0) {
					SGObject obj = objValue.getObj();
					CommonContentHelper.service.attach((ContentHolder)obj, "", secondary);
				}
			}else if(per instanceof SGObject){
				if(secondary.size() > 0) {
					SGObject obj = (SGObject) per;
					CommonContentHelper.service.attach((ContentHolder)obj, "", secondary);
				}
			}
			
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}
	
	@Override
	public void upsertObjectValue(SGObject obj, Map<String, Object> reqMap) throws Exception {
		Transaction trx = new Transaction();
		SGObjectValue objValue = null;
		try {
			trx.start();
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String code = StringUtil.checkNull((String) reqMap.get("code"));
			String name = StringUtil.checkNull((String) reqMap.get("name"));
			String seq = StringUtil.checkNull((String) reqMap.get("seq"));
			String value0 = StringUtil.checkNull((String) reqMap.get("value0"));
			String value1 = StringUtil.checkNull((String) reqMap.get("value1"));
			String value2 = StringUtil.checkNull((String) reqMap.get("value2"));
			String value3 = StringUtil.checkNull((String) reqMap.get("value3"));
			String value4 = StringUtil.checkNull((String) reqMap.get("value4"));
			String value5 = StringUtil.checkNull((String) reqMap.get("value5"));
			String value6 = StringUtil.checkNull((String) reqMap.get("value6"));
			String value7 = StringUtil.checkNull((String) reqMap.get("value7"));
			String value8 = StringUtil.checkNull((String) reqMap.get("value8"));
			String value9 = StringUtil.checkNull((String) reqMap.get("value9"));
			
			JSONObject jObj = new JSONObject();
			if(StringUtil.checkString(oid)) {
				objValue = (SGObjectValue) CommonUtil.getObject(oid);
			}else {
				objValue = SGObjectValue.newSGObjectValue();
				objValue.setObj(obj);
			}
			
			jObj.put("code", code);
			jObj.put("name", name);
			jObj.put("seq", seq);
			jObj.put("value0", value0);
			jObj.put("value1", value1);
			jObj.put("value2", value2);
			jObj.put("value3", value3);
			jObj.put("value4", value4);
			jObj.put("value5", value5);
			jObj.put("value6", value6);
			jObj.put("value7", value7);
			jObj.put("value8", value8);
			jObj.put("value9", value9);
			
			String jsonString = jObj.toJSONString();
			objValue.setSeq(Integer.parseInt(seq));
			objValue.setDivision(jsonString);
			PersistenceHelper.manager.save(objValue);
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		
	}

	@Override
	public void modifyLight(Map<String, Object> map) throws Exception {
		Transaction trx = new Transaction();
		SGObjectValue objValue = null;
		
		try {
			trx.start();
			String oid = StringUtil.checkNull((String) map.get("oid"));
			String value1 = StringUtil.checkNull((String) map.get("value1"));
			String value2 = StringUtil.checkNull((String) map.get("value2"));
			objValue = (SGObjectValue) CommonUtil.getObject(oid);
			
			JSONObject getDiv = new JSONObject(objValue.getDivision());
			String code = getDiv.getString("code");
		    String name = getDiv.getString("name");
		    int seq = getDiv.getInt("seq");
			
		    JSONObject jObj = new JSONObject();
			jObj.put("code", code);
			jObj.put("name", name);
			jObj.put("seq", seq);
			jObj.put("value0", "");
			jObj.put("value1", value1);
			jObj.put("value2", value2);
			jObj.put("value3", "");
			jObj.put("value4", "");
			jObj.put("value5", "");
			jObj.put("value6", "");
			jObj.put("value7", "");
			jObj.put("value8", "");
			jObj.put("value9", "");
			
			String jsonString = jObj.toJSONString();
			objValue.setSeq(seq);
			objValue.setDivision(jsonString);
			PersistenceHelper.manager.modify(objValue);
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}

	@Override
	public SGObjectMaster revisionGate(Map<String, Object> reqMap) throws Exception {
		Transaction trx = new Transaction();
		SGObjectMaster newMaster = null;
		try {
			trx.start();
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String remark = StringUtil.checkNull((String) reqMap.get("remark"));
			SGObjectMaster oldMaster = (SGObjectMaster) CommonUtil.getObject(oid);
			oldMaster.setLastVersion(false);
			oldMaster = (SGObjectMaster) PersistenceHelper.manager.modify(oldMaster);
			oldMaster = (SGObjectMaster) PersistenceHelper.manager.refresh(oldMaster);
			
			newMaster = SgHelper.manager.createCopy(oldMaster, remark);
			
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return newMaster;
		
	}
	
	@Override
	public void exportExcelRisk(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String oid =  StringUtil.checkNull((String) request.getParameter("oid"));
		String objType =  StringUtil.checkNull((String) request.getParameter("objType"));
		
		//엑셀파일 시작 줄 지정
		int INITIAL_ROW_IDX = 7;
		
		Workbook workbook = getWorkbook(riskExcelForm, request, response);
		
		if(workbook.getSheetAt(0) == null) {
			if(workbook != null) {
				workbook.close();
			}
			throw new Exception("읽을 시트가 없습니다.");
		}
		Sheet sheet = workbook.getSheetAt(0);
		
		//row
		List<CellStyle> defaultStyleList = new ArrayList<CellStyle>();
		int defaultRowHeight = 500;
		int excel_row_idx = INITIAL_ROW_IDX;
		
		SGObjectMaster objMaster = (SGObjectMaster) CommonUtil.getObject(oid);
		
		List<SGObjectValueData> list = SgHelper.manager.getValueList(objMaster, objType);
		
		for(int i=0; i<list.size(); i++){
			SGObjectValueData data = list.get(i);
			Row row = null;
		  
			//set Row Height
			if(i == 0) {
				row = sheet.getRow(excel_row_idx);
				defaultRowHeight = row.getHeight();
			}else {
				row = sheet.createRow(excel_row_idx);
				row.setHeight((short) defaultRowHeight); 
			}
			
			//Cell
			int columnIndex = 0;
			JSONObject jsObj = new JSONObject(data.getDivision());
			int signalLength = "signal_".length();
			setCellValueCS(defaultStyleList, row, i, columnIndex++, String.valueOf(i+1));
			setCellValueCS(defaultStyleList, row, i, columnIndex++, jsObj.getString("value0"));
			setCellValueCS(defaultStyleList, row, i, columnIndex++, jsObj.getString("value1"));
			setCellValueCS(defaultStyleList, row, i, columnIndex++, jsObj.getString("value2").substring(signalLength));
			setCellValueCS(defaultStyleList, row, i, columnIndex++, jsObj.getString("value3").substring(signalLength));
			setCellValueCS(defaultStyleList, row, i, columnIndex++, jsObj.getString("value4").substring(signalLength));
			setCellValueCS(defaultStyleList, row, i, columnIndex++, jsObj.getString("value5").substring(signalLength));
			setCellValueCS(defaultStyleList, row, i, columnIndex++, jsObj.getString("value6"));
			setCellValueCS(defaultStyleList, row, i, columnIndex++, jsObj.getString("value7"));
			setCellValueCS(defaultStyleList, row, i, columnIndex++, jsObj.getString("value8"));
			setCellValueCS(defaultStyleList, row, i, columnIndex++, jsObj.getString("value9"));
			
			excel_row_idx++; 
		}
		 
		// workbook write/close
		workbookOutput(workbook, response);
	}
	
	/**
	 * 
	  * @Method Name : setCellValue
	  * @작성일 : 2021. 5. 13.
	  * @작성자 : mjroh
	  * @Method 설명 : 셀 데이터 입력
	  * @param row
	  * @param columnIndex
	  * @param data
	 */
	private void setCellValue(Row row, int columnIndex, Object data) {
		Cell cell = null;
		cell = row.getCell(columnIndex);
		
		String dataStr = String.valueOf(data);
		if(data instanceof Double) {
			cell.setCellValue(Double.valueOf(dataStr));
		}else {
			cell.setCellValue(dataStr);
		}
	}
	
	/**
	 * 
	  * @Method Name : setCellValueCS
	  * @작성일 : 2021. 5. 13.
	  * @작성자 : mjroh
	  * @Method 설명 : 셀 데이터 입력 (첫번째 행의 스타일을 사용하고자 할 때)
	  * @param defaultStyleList
	  * @param row
	  * @param rowIndex
	  * @param columnIndex
	  * @param data
	 */
	private void setCellValueCS(List<CellStyle> defaultStyleList, Row row, int rowIndex, int columnIndex, String data) {
		Cell cell = null;
		//set Style
		if(rowIndex == 0) {
			cell = row.getCell(columnIndex);
			CellStyle cs = cell.getCellStyle();
			defaultStyleList.add(cs);
		}else {
			cell = row.createCell(columnIndex);
			CellStyle cs = defaultStyleList.get(columnIndex);
			cell.setCellStyle(cs);
		}
		cell.setCellValue(String.valueOf(StringUtil.checkNull(data)));
	}
	
	private void workbookOutput(Workbook workbook, HttpServletResponse response) throws Exception {
		CookieGenerator cg = new CookieGenerator();
		cg.setCookieName("fileDownload");
		cg.addCookie(response, "true");

		// SXSSF 생성
	    SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook((XSSFWorkbook) workbook, 100);
	    
	    sxssfWorkbook.write(response.getOutputStream());
	    sxssfWorkbook.close();
	    sxssfWorkbook.dispose();
		
	}
	
	private Workbook getWorkbook(String form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 엑셀 템플릿 가져오기
		FileInputStream fis = new FileInputStream(location + form);
		Workbook workbook = new XSSFWorkbook(fis);
		
		response.setContentType("application/msexcel");
	    response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", URLEncoder.encode(form,"UTF-8")));

		return workbook;
	}
	
	@Override
	public void exportExcelCStop(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String oid =  StringUtil.checkNull((String) request.getParameter("oid"));
		String objType =  StringUtil.checkNull((String) request.getParameter("objType"));
		
		//엑셀파일 시작 줄 지정
		int INITIAL_ROW_IDX = 7;
		int INITIAL_COL_IDX = 3;
		
		Workbook workbook = getWorkbook(cstopExcelForm, request, response);
		
		if(workbook.getSheetAt(0) == null) {
			if(workbook != null) {
				workbook.close();
			}
			throw new Exception("읽을 시트가 없습니다.");
		}
		Sheet sheet = workbook.getSheetAt(0);
		
		//row
		int excel_row_idx = INITIAL_ROW_IDX;
		
		SGObjectMaster objMaster = (SGObjectMaster) CommonUtil.getObject(oid);
		
		List<SGObjectValueData> list = SgHelper.manager.getValueList(objMaster, objType);
		
		for(int i=0; i<list.size(); i++){
			SGObjectValueData data = list.get(i);
			Row row = null;
		  
			//set Row Height
			row = sheet.getRow(excel_row_idx);
			//System.out.println("excel row " + excel_row_idx + "cell length==" + row.getPhysicalNumberOfCells());
			
			//Cell
			int columnIndex = INITIAL_COL_IDX;
			JSONObject jsObj = new JSONObject(data.getDivision());
			boolean isDash = false;
			if("-".equals(jsObj.getString("name"))) {
				isDash = true;
			}
			
			setCellValue(row, columnIndex, 					 isDash? jsObj.getString("value0") : Double.valueOf(jsObj.getString("value0")));//GR1
			setCellValue(row, columnIndex = columnIndex + 2, isDash? jsObj.getString("value1") : Double.valueOf(jsObj.getString("value1")));//GR2
			setCellValue(row, columnIndex = columnIndex + 2, isDash? jsObj.getString("value2") : Double.valueOf(jsObj.getString("value2")));//GR3
			setCellValue(row, columnIndex = columnIndex + 2, isDash? jsObj.getString("value3") : Double.valueOf(jsObj.getString("value3")));//GR4
			setCellValue(row, columnIndex = columnIndex + 2, isDash? jsObj.getString("value4") : Double.valueOf(jsObj.getString("value4")));//GR5
			setCellValue(row, columnIndex = columnIndex + 2, isDash? jsObj.getString("value5") : Double.valueOf(jsObj.getString("value5")));//GR6
			setCellValue(row, columnIndex = columnIndex + 2, isDash? jsObj.getString("value6") : Double.valueOf(jsObj.getString("value6")));//Target
			
			excel_row_idx++; 
		}
		 
		// workbook write/close
		workbookOutput(workbook, response);
	}
	
	@Override
	public void importExcelCStop(Map<String, Object> reqMap) throws Exception {
		String oid =  StringUtil.checkNull((String) reqMap.get("cOid"));
		String objType =  StringUtil.checkNull((String) reqMap.get("cObjType"));
		String primary = StringUtil.checkNull((String) reqMap.get("PRIMARY"));
		
		if (!StringUtil.checkString(primary)) {
			throw new Exception("첨부파일을 등록해야 합니다.");
		}
		
		// 엑셀 가져오기
		Workbook workbook = null;
		boolean isParse = true;
		try {
		    if (StringUtil.checkString(primary)) {
				String tmp = primary.split("/")[0];
				EncodingConverter localEncodingConverter = new EncodingConverter();
				String str = localEncodingConverter.decode(tmp);
				String[] arrayOfString = str.split(":");
				Long.parseLong(arrayOfString[0]);
		    }
		} catch (NumberFormatException e) {
		    isParse = false;
		}
		
		if (StringUtil.checkString(primary) && isParse) {
		    String cacheId = primary.split("/")[0];
		    CachedContentDescriptor cacheDs = new CachedContentDescriptor(cacheId);
//		    String uploadPath = cacheDs.getContentIdentity();
		    String uploadPath = ContentUtil.getUploadPath(cacheDs.getEncodedCCD());
		    
		    FileInputStream fis = new FileInputStream(uploadPath);
		    workbook = new XSSFWorkbook(fis);
		}
		
		//엑셀파일 시작 줄 지정
		int INITIAL_ROW_IDX = 7;
		int LAST_ROW_IDX = 31;
		
		if(workbook.getSheetAt(0) == null) {
			if(workbook != null) {
				workbook.close();
			}
			throw new Exception("읽을 시트가 없습니다.");
		}
		Sheet sheet = workbook.getSheetAt(0);
		
		//기존
		SGObjectMaster objMaster = (SGObjectMaster) CommonUtil.getObject(oid);
		List<SGObjectValue> list = SgHelper.manager.getSGObjectValueList(objMaster, objType);
		
		//수정
		for(int ri = INITIAL_ROW_IDX; ri<=LAST_ROW_IDX; ri++){
			//set Row Height
			Row row = sheet.getRow(ri);
			//System.out.println("excel row " + ri + "cell length==" + row.getPhysicalNumberOfCells());
			
			JSONObject jObj = new JSONObject();
			
			//Cell
			int columnIndex = 0;
			String name = getCellValue(row, columnIndex);
			if(!StringUtil.checkString(name)) {
				//System.out.println("get name null");
				continue;
			}
			
			//get Data
			SGObjectValue objValue = getSGObjectValueFromName(name, list);
			//System.out.println("get name[" + (objValue != null) + "]==" + name);
			if(objValue == null) {
				continue;
			}
			
			SGObjectValueData data = new SGObjectValueData(objValue);
			
			String value0 = getCellValue(row, columnIndex = columnIndex + 3);
			String value1 = getCellValue(row, columnIndex = columnIndex + 2);
			String value2 = getCellValue(row, columnIndex = columnIndex + 2);
			String value3 = getCellValue(row, columnIndex = columnIndex + 2);
			String value4 = getCellValue(row, columnIndex = columnIndex + 2);
			String value5 = getCellValue(row, columnIndex = columnIndex + 2);
			String value6 = getCellValue(row, columnIndex = columnIndex + 2);
			
			jObj.put("code", data.getCode());
			jObj.put("name", data.getName());
			jObj.put("seq", data.getSeq());
			jObj.put("value0", value0);
			jObj.put("value1", value1);
			jObj.put("value2", value2);
			jObj.put("value3", value3);
			jObj.put("value4", value4);
			jObj.put("value5", value5);
			jObj.put("value6", value6);
			jObj.put("value7", "");
			jObj.put("value8", "");
			jObj.put("value9", "");
			
			String jsonString = jObj.toJSONString();
			objValue.setDivision(jsonString);
			
			PersistenceHelper.manager.modify(objValue);
		}
		
		workbook.close();
	}
	
	/**
	 * 
	  * @Method Name : getCellValue
	  * @작성일 : 2021. 5. 14.
	  * @작성자 : mjroh
	  * @Method 설명 : 엑셀 데이터 가져오기
	  * @param row
	  * @param columnIndex
	  * @return
	 */
	private String getCellValue(Row row, int columnIndex) {
		Cell cell = row.getCell(columnIndex);
		String val = "";
		if(cell != null) {
			if(CellType.NUMERIC.equals(cell.getCellType())) {
				if( DateUtil.isCellDateFormatted(cell)) {
					Date date = cell.getDateCellValue();
					val = new SimpleDateFormat("yyyy-MM-dd").format(date);
				}else {
					val = cell.getNumericCellValue()+"";
				}
				
			}else if(CellType.STRING.equals(cell.getCellType())) {
				val = cell.getStringCellValue().trim();
				
			}else if(CellType.BOOLEAN.equals(cell.getCellType())) {
				val = Boolean.toString(cell.getBooleanCellValue());
				
			}else if(CellType.FORMULA.equals(cell.getCellType())) {
				val = cell.getStringCellValue();
				
			}else {
				val = "";
			}
		}
		return val;
	}
	
	/**
	 * 
	  * @Method Name : getSGObjectValueFromName
	  * @작성일 : 2021. 5. 14.
	  * @작성자 : mjroh
	  * @Method 설명 : Name으로 SGObjectValue 가져오기
	  * @param name
	  * @param list
	  * @return
	  * @throws Exception
	 */
	private SGObjectValue getSGObjectValueFromName (String name, List<SGObjectValue> list) throws Exception {
		SGObjectValue objValue = null;
		if (!StringUtil.checkString(name)) {
			throw new NullPointerException();
		}
		
		for(SGObjectValue val : list) {
			SGObjectValueData data = new SGObjectValueData(val);
			if(name.equals(data.getName())) {
				objValue = val;
			}
		}
		return objValue;
	}

	@Override
	public void fileUpload(Map<String, Object> reqMap) throws Exception {
		ContentHolder holder = null;
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String primary = StringUtil.checkNull((String) reqMap.get("PRIMARY"));
			List<String> secondary = StringUtil.checkReplaceArray(reqMap.get("SECONDARY"));
			List<String> delocIds		= StringUtil.checkReplaceArray(reqMap.get("delocIds"));
			
			holder = (ContentHolder) CommonUtil.getObject(oid);
				
			String cacheId = primary.split("/")[0];
			if(cacheId.length() > 0) {
				CommonContentHelper.service.attach((ContentHolder)holder, primary, secondary,delocIds);
			}
			
			//주첨부파일 드래그 변경으로 인해 첨부파일 관련 함수 추가
			if(secondary.size() > 0) {
				
				if(delocIds.size() > 0) {
					holder = (SGObjectValue) ContentHelper.service.getContents(holder);
					Vector files = ContentHelper.getApplicationData(holder);
					if (files != null) {
						for (int i = 0; i < files.size(); i++) {
							ApplicationData data =(ApplicationData) files.get(i);
							String dataOid = CommonUtil.getOIDString(data);
							if(!delocIds.contains(dataOid)) {
								CommonContentHelper.service.delete(holder, (ApplicationData) files.get(i));
							}
						}
					}
				}else {
					CommonContentHelper.service.delete(holder, ContentRoleType.SECONDARY);
				}
				
				for (int i = 0; i < secondary.size(); i++) {
					String secondCacheId = secondary.get(i).split("/")[0];
					String fileName = secondary.get(i).split("/")[1];
					CachedContentDescriptor cacheDs = new CachedContentDescriptor(secondCacheId);
					CommonContentHelper.service.attach(holder, cacheDs, fileName, "", ContentRoleType.SECONDARY);
			    }
			}else {
				if(delocIds.size() > 0) {
					holder = (SGObjectValue) ContentHelper.service.getContents(holder);
					Vector files = ContentHelper.getApplicationData(holder);
					if (files != null) {
						for (int i = 0; i < files.size(); i++) {
							ApplicationData data =(ApplicationData) files.get(i);
							String dataOid = CommonUtil.getOIDString(data);
							if(!delocIds.contains(dataOid)) {
								CommonContentHelper.service.delete(holder, (ApplicationData) files.get(i));
							}
						}
					}
				}else {
					CommonContentHelper.service.delete(holder, ContentRoleType.SECONDARY);
				}
			}
			
			holder = (ContentHolder) PersistenceHelper.manager.refresh(holder);
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}

	@Override
	public void uploadPrimary(Map<String, Object> fileMap) throws Exception {
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			String oid = StringUtil.checkNull((String) fileMap.get("oid"));
			String cacheId = StringUtil.checkNull((String) fileMap.get("cacheId"));
			String roleType = StringUtil.checkNull((String) fileMap.get("roleType"));
			String saveName = StringUtil.checkNull((String) fileMap.get("saveName"));
			
			String primaryData = "";
			ContentHolder holder = null;
			
			if(cacheId.length() > 0 && saveName.length() > 0 && roleType.length() > 0) {
				primaryData = cacheId+"/"+saveName+"/"+roleType;
			}
			
			holder = (ContentHolder) CommonUtil.getObject(oid);
				
			if (StringUtil.checkString(primaryData)) {
			    ContentItem item = null;
			    QueryResult result = ContentHelper.service.getContentsByRole(holder, ContentRoleType.PRIMARY);
			    if (result.hasMoreElements()) {
					item = (ContentItem) result.nextElement();
					CommonContentHelper.service.delete(holder, item);
			    }
	
			    String cacheId2 = primaryData.split("/")[0];
			    String fileName = primaryData.split("/")[1];
			    CachedContentDescriptor cacheDs = new CachedContentDescriptor(cacheId2);
			    CommonContentHelper.service.attach(holder, cacheDs, fileName, "", ContentRoleType.PRIMARY);
			}
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		
	}
}
