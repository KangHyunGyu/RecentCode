package com.e3ps.epm.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class MigExcelData {
	
	private final static String MIG_FILE_PATH = "D:\\ptc\\Windchill_12.1\\Windchill\\loadFiles\\e3ps\\PLM_MIG_EXCEL_DATA.xls";
	
	private JSONArray MIG_DATA;
	
	private static final MigExcelData instance = new MigExcelData();
	
	private MigExcelData() {
		this.initialize();
	}

	public static MigExcelData getInstance() {
		return MigExcelData.instance;
	}
	
	
	private void initialize() {
		File file =  new File(MIG_FILE_PATH);
		
		try {
			
			MIG_DATA = new JSONArray();
			
			JSONObject rowJsonObj = null;
			
			Workbook wb =  Workbook.getWorkbook(file);
			Sheet[] sheets = wb.getSheets();
			int rows = sheets[0].getRows();
			Cell[] cell = null;
			
			System.out.println("Migration Data Rows ::: " + rows + " row ");
			
			for (int j = 1; j < rows; j++){
				
				int idx = 0;
				rowJsonObj =  new JSONObject();
				cell = sheets[0].getRow(j);
				
				rowJsonObj.put("date", getContent(cell, idx++));
				rowJsonObj.put("drawing_no", getContent(cell, idx++));
				rowJsonObj.put("part_no", getContent(cell, idx++));
				rowJsonObj.put("description", getContent(cell, idx++));
				rowJsonObj.put("erpDescription", getContent(cell, idx++));
				rowJsonObj.put("stringCnt", getContent(cell, idx++));
				rowJsonObj.put("customer", getContent(cell, idx++));
				rowJsonObj.put("material", getContent(cell, idx++));
				rowJsonObj.put("external_diameter", getContent(cell, idx++));
				rowJsonObj.put("inner_diameter", getContent(cell, idx++));
				rowJsonObj.put("thickness", getContent(cell, idx++));
				rowJsonObj.put("hall_dia", getContent(cell, idx++));
				rowJsonObj.put("hall_cnt", getContent(cell, idx++));
				rowJsonObj.put("equipment", getContent(cell, idx++));
				rowJsonObj.put("resistivity", getContent(cell, idx++));
				rowJsonObj.put("remark", getContent(cell, idx++));
				rowJsonObj.put("revision", getContent(cell, idx++));
				rowJsonObj.put("special_note", getContent(cell, idx++));
				rowJsonObj.put("new_drawing_no", getContent(cell, idx++));
				rowJsonObj.put("inch", getContent(cell, idx++));
				rowJsonObj.put("middle_code", getContent(cell, idx++));
				rowJsonObj.put("seq", getContent(cell, idx++));
				rowJsonObj.put("seq_auto", getContent(cell, idx++));
				rowJsonObj.put("revision1", getContent(cell, idx++));
				rowJsonObj.put("final_new_code", getContent(cell, idx++));
				rowJsonObj.put("code_length", getContent(cell, idx++));
				
				MIG_DATA.add(rowJsonObj);
				
			}
			
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public JSONArray getMigData(){
		return MIG_DATA;
	}
	
	
	private String getContent(Cell[] cell, int idx){
		String val = "";
	    try{
	           val = cell[idx].getContents()==null?"":cell[idx].getContents().trim();
	    }catch (ArrayIndexOutOfBoundsException e){
	    	e.printStackTrace();
	    }
	    return val;
	}

}
