/*
 * @(#) LoadOutputTypeStep.java  Create on 2004. 12. 20.
 * Copyright (c) e3ps. All rights reserverd
 */
package com.e3ps.project.beans;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.httpgw.GatewayAuthenticator;
import wt.method.RemoteMethodServer;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;

import com.e3ps.common.util.AuthHandler;
import com.e3ps.common.util.JExcelUtil;
import com.e3ps.project.OutputType;
import com.e3ps.project.OutputTypeStep;

public class LoadOutputType
{
    /**
     * 
     */
    private static String TYPE;
    public static void main(String[] args) throws IOException
    {
    	String userId = "wcadmin";
		try {
			wt.method.RemoteMethodServer methodServer = wt.method.RemoteMethodServer.getDefault();
			methodServer.setAuthenticator(AuthHandler.getMethodAuthenticator(userId));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
        if(args.length < 1)
        {
            System.out.println("> input output.xls file location");
            System.exit(0);
        }
        
        LoadOutputType loader = new LoadOutputType();
        String file = args[0];
        
        File newfile = null;
        try
        {
            newfile = new File(file);
            System.out.println("File : " + newfile.getName());
            if (!newfile.getName().endsWith(".xls"))
                return;
        }
        catch (Exception e)
        {
            return;
        }
        Workbook wb = JExcelUtil.getWorkbook(newfile);
        loader.load(wb);
        System.exit(0);
    }
    
    private void load(Workbook wb)
    {
    	RemoteMethodServer rms = RemoteMethodServer.getDefault (); 
    	GatewayAuthenticator auth = new GatewayAuthenticator (); 
    	auth.setRemoteUser ( "wcadmin"); 
    	rms.setAuthenticator (auth);
        Sheet[] sheets = wb.getSheets();
        int rows = sheets[0].getRows();
        for (int j = 1; j < rows; j++)
        {
        	createOutputType(sheets[0].getRow(j));
        }
    }

    private void createOutputType(Cell[] cell)
    {
        try
        {
        	
        	String outputType = JExcelUtil.getContent(cell, 0);
        	String code = JExcelUtil.getContent(cell, 1);
        	String name = JExcelUtil.getContent(cell, 2);
        	String sort = JExcelUtil.getContent(cell, 3);
        	String desc = JExcelUtil.getContent(cell, 4);
        	String parentCode = JExcelUtil.getContent(cell, 5);
        	
        	System.out.println("content 0 = " + JExcelUtil.getContent(cell, 0));
        	System.out.println("content 1 = " + JExcelUtil.getContent(cell, 1));
        	System.out.println("content 2 = " + JExcelUtil.getContent(cell, 2));
        	System.out.println("content 3 = " + JExcelUtil.getContent(cell, 3));
        	System.out.println("content 4 = " + JExcelUtil.getContent(cell, 4));
        	System.out.println("content 5 = " + JExcelUtil.getContent(cell, 5));
        	
        	
            OutputTypeStep output = getOutputCode(code, outputType);
            if(output == null){
            	System.out.println("### output is null : new Step");
            	output = OutputTypeStep.newOutputTypeStep();
            }
            output.setCode(code);
            output.setName(name);
            output.setSort(Integer.parseInt(sort));
            output.setDescription(desc);
            output.setOutputType(OutputType.toOutputType(outputType));
            
           	OutputTypeStep parentOutput = getOutputCode(parentCode, outputType);
            if(parentOutput != null){
            	output.setParent(parentOutput);
            }else{
            	//output.setParent(null);
            }
            output = (OutputTypeStep)PersistenceHelper.manager.save(output);
            
            System.out.println(output.getName()+" code Loader Success");
            
        }
        catch (WTException e)
        {
            e.printStackTrace();
        }
        catch (WTPropertyVetoException e)
        {
            e.printStackTrace();
        }
    }
    
    public OutputTypeStep getOutputCode(String codeID, String codeType){
    	if(codeID == null){
    		return null;
    	}
		try{
			QuerySpec query = new QuerySpec(OutputTypeStep.class);
			query.appendWhere(new SearchCondition(OutputTypeStep.class, "outputType", "=", codeType), new int[] { 0 });
			query.appendAnd();
			query.appendWhere(new SearchCondition(OutputTypeStep.class, "code", "=", codeID), new int[] { 0 });
	        QueryResult result = PersistenceHelper.manager.find(query);
	        
	        while (result.hasMoreElements())
	        {
	        	OutputTypeStep outputCode = (OutputTypeStep)result.nextElement();
	        	return outputCode;
	        }
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
		return null;
    }
}