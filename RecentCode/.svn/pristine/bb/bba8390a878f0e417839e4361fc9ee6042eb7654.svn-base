package com.e3ps.load;

import java.io.File;
import java.io.Serializable;

import com.e3ps.common.code.NumberCode;
import com.e3ps.common.code.NumberCodeType;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.pds.StatementSpec;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTException;
import wt.util.WTInvalidParameterException;

public class CodeLoader_Before implements RemoteAccess, Serializable{

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.LOAD.getName());
	private static final long serialVersionUID = 1L;
	static final boolean SERVER = wt.method.RemoteMethodServer.ServerFlag;
	
	public static void main(String[] args) throws Exception {

		String sWtHome = wt.util.WTProperties.getLocalProperties().getProperty("wt.home", "");
		String sFilePath = sWtHome + "\\loadFiles\\e3ps\\NumberCode.xls";

		if (args != null && args.length > 0 && args[0].trim().length() > 0) {
			sFilePath = args[0];
		}

		setUser(args[1], args[2]);

		new CodeLoader_Before().load(sFilePath);
		
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
				
				int sort = 1; 
				String tmpSortCheck = "";
				
				String tmpCodeType = "";
				int seq = 1;
				for (int j = 1; j < rows; j++) {
					
					if (checkLine(sheets[i].getRow(j), 0)) {
						Cell[] cell = sheets[i].getRow(j);

						String codeType = StringUtil.checkNull(getContent(cell, 0).trim());
						String code = StringUtil.checkNull(getContent(cell, 1).trim());
						String name = StringUtil.checkNull(getContent(cell, 2).trim());
						String description = StringUtil.checkNull(getContent(cell, 3).trim());
						String disabled = StringUtil.checkNull(getContent(cell, 4).trim());
						String pCode = StringUtil.checkNull(getContent(cell, 5).trim());
						
						
						if (codeType.length() > 0) {

							if(!tmpCodeType.equals(codeType)) {
								tmpCodeType = codeType;
								seq = 1;
							}
							
							NumberCodeType nCodeType = null;
							
							try {
								nCodeType = NumberCodeType.toNumberCodeType(codeType);
							}catch(WTInvalidParameterException e){
								System.out.println("#####################################");
								System.out.println("["+codeType +"] 해당 CODETYPE 존재하지 않음");
								System.out.println("#####################################");
								continue;
							}
							
							if("true".equals(nCodeType.getShortDescription())) {
								code = nCodeType.getLongDescription() + seq++;
							}
							
							NumberCode numberCode = null;
							if(pCode.length() > 0) {
								NumberCode pnc = getNumberCode(pCode, codeType);
								numberCode = getNumberCode(code, codeType, pnc);
							}else {
								numberCode = getNumberCode(code, codeType);
							}
							
							if(numberCode == null) {
								numberCode = NumberCode.newNumberCode();
								
								numberCode.setCodeType(nCodeType);
							}
							
							numberCode.setCode(code);
							numberCode.setName(name);
							numberCode.setDescription(description);
							if("0".equals(disabled)){
								numberCode.setDisabled(false);
							} else if("1".equals(disabled)) {
								numberCode.setDisabled(true);
							} else {
								numberCode.setDisabled(false);
							}
							
							if(!tmpSortCheck.equals(codeType + pCode)) {
								tmpSortCheck = codeType + pCode;
								sort = 1;
							}
							numberCode.setSort(sort);
							
							String pCodeType = "";
							
							if (pCode.length() > 0) {
								
								if("JELSTDPARTCODE".equals(codeType) || "UNITDIVISIONCODE".equals(codeType)) {
									pCodeType = "JELPROCESSTYPE";
								} else {
									pCodeType = codeType;
								}
								
								NumberCode parentCode = getNumberCode(pCode, pCodeType);

								if (parentCode == null) {
									throw new Exception("Parent Code에 대한 NumberCode가 없습니다....... [" + i + "] Sheet ["
											+ j + "] row");
								}

								numberCode.setParent(parentCode);
							}

							PersistenceHelper.manager.save(numberCode);

							sort++;

						} else {
							throw new Exception("NumberCode Code가 정의되어 있지 않습니다..... [" + i + "] Sheet [" + j + "] row");
						}
					}
				}
			}

			System.out.println("\n\n-----------------------------------------------------");
			System.out.println(sheets[0].getRows() + " Line Insert Complete..............");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public NumberCode getNumberCode(String code, String codeType) {
		QuerySpec qs = null;
		NumberCode numberCode = null;

		try {
			qs = new QuerySpec();

			int idx = qs.addClassList(NumberCode.class, true);

			qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.CODE, SearchCondition.EQUAL, code),
					new int[] { idx });

			qs.appendAnd();
			
			qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.CODE_TYPE, SearchCondition.EQUAL, codeType),
					new int[] { idx });
			
			QueryResult qr = PersistenceHelper.manager.find((StatementSpec) qs);

			if (qr.hasMoreElements()) {
				Object[] o = (Object[]) qr.nextElement();
				numberCode = (NumberCode) o[0];
			}

		} catch (QueryException e) {
			e.printStackTrace();
		} catch (WTException e) {
			e.printStackTrace();
		}

		return numberCode;

	}
	
	public NumberCode getNumberCode(String code, String codeType, NumberCode pnc) {
		QuerySpec qs = null;
		NumberCode numberCode = null;

		try {
			qs = new QuerySpec();

			int idx = qs.addClassList(NumberCode.class, true);

			qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.CODE, SearchCondition.EQUAL, code),
					new int[] { idx });

			qs.appendAnd();
			
			qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.CODE_TYPE, SearchCondition.EQUAL, codeType),
					new int[] { idx });
			
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(NumberCode.class, "parentReference.key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(pnc)), new int[] { idx });
			
			QueryResult qr = PersistenceHelper.manager.find((StatementSpec) qs);

			if (qr.hasMoreElements()) {
				Object[] o = (Object[]) qr.nextElement();
				numberCode = (NumberCode) o[0];
			}

		} catch (QueryException e) {
			e.printStackTrace();
		} catch (WTException e) {
			e.printStackTrace();
		}

		return numberCode;

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
