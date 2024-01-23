package com.e3ps.load;

import java.io.File;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.doc.DocCodeToValueDefinitionLink;
import com.e3ps.doc.DocCodeType;
import com.e3ps.doc.DocValueDefinition;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import wt.clients.folder.FolderTaskLogic;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.folder.Folder;
import wt.inf.container.WTContainer;
import wt.inf.container.WTContainerRef;
import wt.method.RemoteMethodServer;
import wt.pds.StatementSpec;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTException;

public class DocCodeTypeLoader {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.LOAD.getName());
	
	public static void main(String[] args) throws Exception {

		String sWtHome = wt.util.WTProperties.getLocalProperties().getProperty("wt.home", "");
		String sFilePath = sWtHome + "\\loadFiles\\e3ps\\DocCodeType.xls";

		if (args != null && args.length > 0 && args[0].trim().length() > 0) {
			sFilePath = args[0];
		}

		setUser(args[1], args[2]);

		new DocCodeTypeLoader().load(sFilePath);
		
	}

	public static void setUser(final String id, final String pw) {
		RemoteMethodServer.getDefault().setUserName(id);
		RemoteMethodServer.getDefault().setPassword(pw);
	}

	public void load(String sFilePath) {

		try {

			File newfile = new File(sFilePath);

			Workbook wb = Workbook.getWorkbook(newfile);

			Sheet[] sheets = wb.getSheets();

			for (int i = 0; i < sheets.length; i++) {

				int rows = sheets[i].getRows();
				
				for (int j = 1; j < rows; j++) {
					
					if (checkLine(sheets[i].getRow(j), 0)) {
						Cell[] cell = sheets[i].getRow(j);

						String code = StringUtil.checkNull(getContent(cell, 0).trim());
						String name = StringUtil.checkNull(getContent(cell, 1));
						String eName = StringUtil.checkNull(getContent(cell, 2).trim());
						String sort = StringUtil.checkNull(getContent(cell, 3).trim());
						String folderPath = StringUtil.checkNull(getContent(cell, 4).trim());
						String attributes = StringUtil.checkNull(getContent(cell, 5).trim());
						
						System.out.println("##########################################################");
						System.out.println("DocCodeType seq ::::  " + j);
						System.out.println("DocCodeType code ::::  " + code);
						System.out.println("DocCodeType name ::::  " + name);
						System.out.println("DocCodeType eName ::::  " + eName);
						System.out.println("DocCodeType sort ::: " + sort);
						System.out.println("DocCodeType folderPath ::: " + folderPath);
						System.out.println("DocCodeType attributes ::: " + attributes);
						
						DocCodeType dct = getDocCodeType(code);
						
						if(dct == null) {
							dct = DocCodeType.newDocCodeType();
							
							dct.setCode(code);
						}
						
						dct.setName(name);
						dct.setEngName(eName);
						dct.setSort(Integer.parseInt(sort));
						
						WTContainer product = WCUtil.getPDMLinkProduct();
						WTContainerRef wtContainerRef = WTContainerRef.newWTContainerRef(product);
						Folder folder = FolderTaskLogic.getFolder(folderPath, wtContainerRef);
						dct.setFolder(folder);
						
						dct = (DocCodeType) PersistenceHelper.manager.save(dct);
						
						deleteAttributeLink(dct);
						  
						if(attributes != null && attributes.length() > 0) {
							for(String attribute : attributes.split(",")) {
								DocValueDefinition dvd = getDocValueDefinition(attribute.trim().toLowerCase());
								DocCodeToValueDefinitionLink link = DocCodeToValueDefinitionLink.newDocCodeToNumberCodeLink();
								link.setDocCode(dct); link.setValueDefiniton(dvd);
								PersistenceHelper.manager.save(link);
							}
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

	public DocCodeType getDocCodeType(String code) {
		QuerySpec qs = null;
		DocCodeType dct = null;

		try {
			qs = new QuerySpec(DocCodeType.class);

			qs.appendWhere(new SearchCondition(DocCodeType.class, DocCodeType.CODE, SearchCondition.EQUAL, code), new int[0]);

			QueryResult qr = PersistenceHelper.manager.find((StatementSpec) qs);

			if (qr.hasMoreElements()) {
				dct = (DocCodeType) qr.nextElement();
			}

		} catch (QueryException e) {
			e.printStackTrace();
		} catch (WTException e) {
			e.printStackTrace();
		}

		return dct;

	}
	
	public DocValueDefinition getDocValueDefinition(String code) {
		QuerySpec qs = null;
		DocValueDefinition dvd = null;
		
		try {
			qs = new QuerySpec(DocValueDefinition.class);
			
			qs.appendWhere(new SearchCondition(DocValueDefinition.class, DocValueDefinition.CODE, SearchCondition.EQUAL, code), new int[0]);
			
			QueryResult qr = PersistenceHelper.manager.find((StatementSpec) qs);
			
			if(qr.hasMoreElements()) {
				dvd = (DocValueDefinition) qr.nextElement();
			}
			
		}catch (QueryException e) {
			e.printStackTrace();
		}catch (WTException e) {
			e.printStackTrace();
		}
		
		return dvd;
	}
	
	public void deleteAttributeLink(DocCodeType dct) {
		try {
			QueryResult qr = PersistenceHelper.manager.navigate(dct, "valueDefiniton", DocCodeToValueDefinitionLink.class, false);
			
			while(qr.hasMoreElements()) {
				DocCodeToValueDefinitionLink link = (DocCodeToValueDefinitionLink) qr.nextElement();
				PersistenceHelper.manager.delete(link);
			}
			
		} catch (WTException e) {
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
