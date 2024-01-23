package com.e3ps.change.beans;

import java.io.File;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.httpgw.GatewayAuthenticator;
import wt.method.RemoteMethodServer;
import wt.org.OrganizationServicesHelper;
import wt.org.WTPrincipalReference;
import wt.org.WTUser;
import wt.query.QuerySpec;
import wt.query.SearchCondition;

import com.e3ps.change.EChangeActivityDefinition;
import com.e3ps.change.EChangeActivityDefinitionRoot;
import com.e3ps.common.util.JExcelUtil;
import com.e3ps.org.Department;

public class ChangeLoader {

	public static void main(String[] args)throws Exception{
		
		String sWtHome = wt.util.WTProperties.getLocalProperties().getProperty("wt.home", "");
        String sFilePath = sWtHome + "\\loadFiles\\e3ps\\ChangeActivityDefinition.xls" ;
        setUser(args[0], args[1]);
		new ChangeLoader().loadDefinition(sFilePath);
	}
	
	public static void setUser(final String id, final String pw)
    {
        RemoteMethodServer.getDefault().setUserName(id);
        RemoteMethodServer.getDefault().setPassword(pw);
    }
	
	public void loadDefinition(String sFilePath)throws Exception{
		RemoteMethodServer rms = RemoteMethodServer.getDefault (); 
    	GatewayAuthenticator auth = new GatewayAuthenticator (); 
    	auth.setRemoteUser ( "wcadmin"); 
    	rms.setAuthenticator (auth);

		
		File newfile =  new File(sFilePath);

		System.out.println(newfile.exists());

		Workbook wb = JExcelUtil.getWorkbook(newfile);

		Sheet[] sheets = wb.getSheets();
		int rootCount = 0;
		EChangeActivityDefinitionRoot root = null;
        for (int i = 0; i < sheets.length; i++)
        {
            int rows = sheets[i].getRows();
            for (int j = 1; j < rows; j++)
            {
                if (JExcelUtil.checkLine(sheets[i].getRow(j),2))
                {
                	
                    Cell[] cell = sheets[i].getRow(j);
                    String rootName = JExcelUtil.getContent(cell, 0).trim();
                    String step = JExcelUtil.getContent(cell, 1).trim();
                    String name = JExcelUtil.getContent(cell, 2).trim();
                    String name_eng = JExcelUtil.getContent(cell, 3).trim();
                    String description = JExcelUtil.getContent(cell, 4).trim();
                    String activeType = JExcelUtil.getContent(cell, 5).trim();
                    String owner = JExcelUtil.getContent(cell, 6).trim();
                    
                    if(rootName!=null && rootName.length()>0){
                    	root = getRoot(rootName);
                    	if(root==null){
                    		root = EChangeActivityDefinitionRoot.newEChangeActivityDefinitionRoot();
                    	}
              
                    	root.setName(rootName);
                    	root.setName_eng(rootName);
                    	root.setSortNumber(rootCount++);
                    	root = (EChangeActivityDefinitionRoot)PersistenceHelper.manager.save(root);
                    }
                    
                    EChangeActivityDefinition def = getActivity(name,root);
                    if(def==null){
                    	def = EChangeActivityDefinition.newEChangeActivityDefinition();
                    }
                    
                    def.setName(name);
                    def.setName_eng(name_eng);
                    def.setDescription(description);
                    def.setActiveType(activeType);
                    def.setStep(step);
                    def.setSortNumber(j);
                    def.setRoot(root);
                    if(owner!=null && owner.length()>0){
                    	WTUser user = (WTUser)OrganizationServicesHelper.manager.getUser(owner);
                    	def.setOwner(WTPrincipalReference.newWTPrincipalReference(user));
                    	def.setWorker(WTPrincipalReference.newWTPrincipalReference(user));
                    }
                    def = (EChangeActivityDefinition)PersistenceHelper.manager.save(def);
                    
                    System.out.println(">> 수정 완료");
                }
            }
        }
	}
	
	public EChangeActivityDefinition getActivity(String name, EChangeActivityDefinitionRoot root)throws Exception{
		QuerySpec qs = new QuerySpec(EChangeActivityDefinition.class);
		qs.appendWhere(new SearchCondition(EChangeActivityDefinition.class,"name","=",name),new int[]{0});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(EChangeActivityDefinition.class,"rootReference.key.id","=",root.getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		if(qr.hasMoreElements()){
			return (EChangeActivityDefinition)qr.nextElement();
		}
		return null;
	}
	
	public EChangeActivityDefinitionRoot getRoot(String name)throws Exception{
		QuerySpec qs = new QuerySpec(EChangeActivityDefinitionRoot.class);
		qs.appendWhere(new SearchCondition(EChangeActivityDefinitionRoot.class,"name","=",name),new int[]{0});
		QueryResult qr = PersistenceHelper.manager.find(qs);
		if(qr.hasMoreElements()){
			return (EChangeActivityDefinitionRoot)qr.nextElement();
		}
		return null;
	}
	
	public Department getDepartment(String name)throws Exception{
		QuerySpec qs = new QuerySpec(Department.class);
		qs.appendWhere(new SearchCondition(Department.class,"name","=",name),new int[]{0});
		QueryResult qr = PersistenceHelper.manager.find(qs);
		if(qr.hasMoreElements()){
			return (Department)qr.nextElement();
		}
		return null;
	}
}

