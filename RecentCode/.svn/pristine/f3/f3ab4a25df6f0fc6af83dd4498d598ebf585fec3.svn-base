package com.e3ps.load;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import wt.admin.AdminDomainRef;
import wt.admin.AdministrativeDomain;
import wt.admin.AdministrativeDomainHelper;
import wt.admin.AdministrativeDomainManager;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.inf.container.WTContainerHelper;
import wt.inf.container.WTContainerRef;
import wt.method.RemoteMethodServer;
import wt.org.WTGroup;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTException;

import com.e3ps.admin.service.AdminHelper;
import com.e3ps.common.code.NumberCode;
import com.e3ps.common.code.NumberCodeType;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.JExcelUtil;
import com.e3ps.common.util.StringUtil;

public class DomainLoader {

    public static void main(final String[] args)throws Exception{
        System.out.println("Initializing...");
        setUser("wcadmin", "wcadmin");
        new DomainLoader().loadGroup();
    }

    public static void setUser(final String id, final String pw)
    {
        RemoteMethodServer.getDefault().setUserName(id);
        RemoteMethodServer.getDefault().setPassword(pw);
    }

	public void loadGroup(){

		String filePath = "D:\\ptc\\Windchill_12.1\\Windchill\\loadFiles\\e3ps\\Domain.xls";
		File newfile = new File(filePath);
		Workbook wb = JExcelUtil.getWorkbook(newfile);
		Sheet[] sheets = wb.getSheets();

		int rows = sheets[0].getRows();

		System.out.println("rows = " + rows);
		Cell[] cell = sheets[0].getRow(1);
		
		String orgName = ConfigImpl.getInstance().getString("org.context.name");
		String productName = ConfigImpl.getInstance().getString("product.context.name");
		
		try {
			
			WTContainerRef container_ref = WTContainerHelper.service.getByPath("/wt.inf.container.OrgContainer="+orgName+"/wt.pdmlink.PDMLinkProduct="+productName);
			
			for (int j = 1; j < rows; j++) {
	
				cell = sheets[0].getRow(j);
				String parentDomainPath = StringUtil.checkNull(JExcelUtil.getContent(cell, 0).trim());
				String domainName = StringUtil.checkNull(JExcelUtil.getContent(cell, 1).trim());
				String domainDescription = StringUtil.checkNull(JExcelUtil.getContent(cell, 2).trim());
				AdministrativeDomain domain = AdministrativeDomainHelper.manager.getDomain(parentDomainPath, container_ref);
				AdministrativeDomain checkDomain = AdministrativeDomainHelper.manager.getDomain(parentDomainPath + "/" + domainName, container_ref);
				if(checkDomain != null) continue;
				AdminDomainRef doaminref = AdminDomainRef.newAdminDomainRef(domain);
				AdministrativeDomainHelper.manager.createDomain(doaminref, domainName, domainDescription, container_ref);
				System.out.println("도메인 경로:" + parentDomainPath + "/" + domainName + " ::: " + domainDescription + " ::: 생성완료");
			}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
