package com.e3ps.load;

import java.io.File;
import java.io.Serializable;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.StringUtil;
import com.e3ps.project.OutputType;
import com.e3ps.project.OutputTypeStep;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.pds.StatementSpec;
import wt.query.QuerySpec;
import wt.query.SearchCondition;

public class OutputTypeStepLoader implements RemoteAccess, Serializable {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.LOAD.getName());
	private static final long serialVersionUID = 1L;
	static final boolean SERVER = wt.method.RemoteMethodServer.ServerFlag;
	
	public static void main(String[] args) throws Exception {
		String sWtHome = wt.util.WTProperties.getLocalProperties().getProperty("wt.home", "");
		String sFilePath = sWtHome + "\\loadFiles\\e3ps\\OutputTypeStep_Cowin.xls";

		if (args != null && args.length > 0 && args[0].trim().length() > 0) {
			sFilePath = args[0];
		}

		setUser(args[1], args[2]);

		new OutputTypeStepLoader().load(sFilePath);
	}
	
	public static void setUser(final String id, final String pw) {
		RemoteMethodServer.getDefault().setUserName(id);
		RemoteMethodServer.getDefault().setPassword(pw);
	}
	
	public void load(String sFilePath) {
		
		System.out.println(" === load Start");
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
			
			File newfile = new File(sFilePath);

			Workbook wb = Workbook.getWorkbook(newfile);

			Sheet[] sheets = wb.getSheets();

			for (int i = 0; i < sheets.length; i++) {

				int rows = sheets[i].getRows();
				for (int j = 1; j < rows; j++) {
					
					if (checkLine(sheets[i].getRow(j), 0)) {
						Cell[] cell = sheets[i].getRow(j);

						String outputType = StringUtil.checkNull(getContent(cell, 0).trim());
						String code = StringUtil.checkNull(getContent(cell, 1).trim());
						String name = StringUtil.checkNull(getContent(cell, 2).trim());
						String sortStr = StringUtil.checkNull(getContent(cell, 3).trim());
						
						System.out.println("outputType : "+outputType);
						System.out.println("code : "+code);
						System.out.println("name : "+name);
						System.out.println("sortStr : "+sortStr);
						
						int sort = 0;
						if(sortStr.length() > 0) {
							sort = Integer.parseInt(sortStr);
						}
						String pCode = StringUtil.checkNull(getContent(cell, 4).trim());
						
						OutputType ot = OutputType.toOutputType(outputType);
							
						OutputTypeStep ots = getOutputTypeStep(outputType, code);
							
						if(ots == null) {
							ots = OutputTypeStep.newOutputTypeStep();
							ots.setOutputType(ot);
						}
							
						ots.setCode(code);
						ots.setName(name);
						ots.setSort(sort);
							
						if (pCode.length() > 0) {
							OutputTypeStep pots = getOutputTypeStep(outputType, pCode);

							if (pots == null) {
								throw new Exception("Parent Code에 대한 OutputTypeStep이 없습니다....... [" + i + "] Sheet ["
										+ j + "] row");
							}

							ots.setParent(pots);
						}

						PersistenceHelper.manager.save(ots);


					} else {
						throw new Exception("OutputTypeStep Code가 정의되어 있지 않습니다..... [" + i + "] Sheet [" + j + "] row");
					}
				}
			}
			System.out.println("\n\n-----------------------------------------------------");
			System.out.println(sheets[0].getRows() + " Line Insert Complete..............");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public OutputTypeStep getOutputTypeStep(String outputType, String code) {
		OutputTypeStep ots = null;
		try {
			
			QuerySpec qs = new QuerySpec(OutputTypeStep.class);
			
			qs.appendWhere(new SearchCondition(OutputTypeStep.class, OutputTypeStep.OUTPUT_TYPE, SearchCondition.EQUAL, outputType), new int[0]);
			
			qs.appendAnd();

			qs.appendWhere(new SearchCondition(OutputTypeStep.class, OutputTypeStep.CODE, SearchCondition.EQUAL, code), new int[0]);
			
			QueryResult qr = PersistenceHelper.manager.find((StatementSpec) qs);

			if (qr.hasMoreElements()) {
				ots = (OutputTypeStep) qr.nextElement();
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ots;
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
