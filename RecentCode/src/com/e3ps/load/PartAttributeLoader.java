package com.e3ps.load;

import java.io.File;
import java.io.Serializable;

import com.e3ps.common.util.StringUtil;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;

public class PartAttributeLoader implements RemoteAccess, Serializable{

//	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.LOAD.getName());
	private static final long serialVersionUID = 1L;
	static final boolean SERVER = wt.method.RemoteMethodServer.ServerFlag;
	
	public static void main(String[] args) throws Exception {

		String sWtHome = wt.util.WTProperties.getLocalProperties().getProperty("wt.home", "");
		String sFilePath = sWtHome + "\\loadFiles\\e3ps\\partAttributeList.xls";

		if (args != null && args.length > 0 && args[0].trim().length() > 0) {
			sFilePath = args[0];
		}

		setUser(args[1], args[2]);

		new PartAttributeLoader().load(sFilePath);
		
	}

	public static void setUser(final String id, final String pw) {
		RemoteMethodServer.getDefault().setUserName(id);
		RemoteMethodServer.getDefault().setPassword(pw);
	}

	@SuppressWarnings("rawtypes")
	public void load(String sFilePath) {
		if (!SERVER) {
			try {
				Class argTypes[] = new Class[]{String.class};
				Object args[] = new Object[]{sFilePath};
				RemoteMethodServer.getDefault().invoke("load", null, this, argTypes, args);
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			System.out.println(" ========== # Part Attribute Load Start # ========== ");

			File newfile = new File(sFilePath);

			Workbook wb = Workbook.getWorkbook(newfile);

			Sheet[] sheets = wb.getSheets();

			for (int i = 0; i < 1; i++) {

				int rows = sheets[i].getRows();
				
				for (int j = 1; j < rows; j++) {
					
					if (checkLine(sheets[i].getRow(j), 0)) {
						Cell[] cell = sheets[i].getRow(j);

						
						String oldNumber = StringUtil.checkNull(getContent(cell, 2).trim()); // 구도번
						//System.out.println("NumberCode pCode ::: " + pCode);
						
						System.out.println("oldNumber : " + oldNumber);
					}
				}
			}

			System.out.println("\n\n-----------------------------------------------------");
			//System.out.println(sheets[0].getRows() + " Line Insert Complete..............");
			System.out.println(" ========== # Part Attribute Load End # ========== ");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	

	public boolean checkLine(Cell[] cell, int line) {
		String value = null;
		try {
			value = cell[line].getContents().trim();
		} catch (ArrayIndexOutOfBoundsException e) {
			e.getMessage();
			return false;
		}
		if (value == null || value.length() == 0)
			return false;
		return true;
	}

	public static String getContent(Cell[] cell, int idx) {
		try {
			String val = cell[idx].getContents();
			if (val == null)
				return "";
			return val.trim();
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		return "";
	}
}
