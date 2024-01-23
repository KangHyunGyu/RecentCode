package com.e3ps.load;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteMethodServer;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTException;

import com.e3ps.common.code.NumberCode;
import com.e3ps.common.code.NumberCodeType;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.JExcelUtil;
import com.e3ps.common.util.StringUtil;

public class CodeLoader {

    /**
    * @param args
    */
    public static void main(final String[] args)throws Exception{
        // TODO Auto-generated method stub
        System.out.println("Initializing...");
       // if ((args == null) || (args.length < 3)){
         //   System.out.println("CodeLoader version 1.0\n Usage [Excel File Path] [User Name] [User Password]]");
          //  System.exit(0);
        //}
        setUser("wcadmin", "wcadmin");
        //System.out.println("args[0] " + args[0]);
        //System.out.println("args[1] " + args[1]);
        new CodeLoader().loadCode();
    }

    public static void setUser(final String id, final String pw)
    {
        RemoteMethodServer.getDefault().setUserName(id);
        RemoteMethodServer.getDefault().setPassword(pw);
    }

    public void loadCode()throws Exception{

        //String sWtHome = wt.util.WTProperties.getLocalProperties().getProperty("wt.home", "");
        //String sFilePath = sWtHome + "/loadFiles/e3ps/ProjectTemplate.xls" ;
        //String sFilePath = sWtHome + "/loadFiles/" + filePath ;
    	String filePath ="D:\\ptc\\Windchill_12.1\\Windchill\\loadFiles\\e3ps\\NumberCode\\NumberCode.xls";
        File newfile =  new File(filePath);
        Workbook wb = JExcelUtil.getWorkbook(newfile);
        Sheet[] sheets = wb.getSheets();
        		
       // for (int i = 0; i < sheets.length; i++)
       // {
            int rows = sheets[0].getRows();
            
            System.out.println("rows = " +rows);
            Cell[] cell = sheets[0].getRow(1);

            NumberCodeType ctype = null;
            
            Hashtable hash = new Hashtable();
            
            for (int j = 1; j < rows; j++)
            {
                String codeType = "";
                String codeID = "";
                String codeName = "";
                String codeDesc = "";
                String codeEngName = "";
                String parentCode = "";
                String sort = "";

                cell = sheets[0].getRow(j);
                codeType = StringUtil.checkNull(JExcelUtil.getContent(cell, 0).trim());
                
                codeID = JExcelUtil.getContent(cell, 2).trim();
                codeName = JExcelUtil.getContent(cell, 3).trim();
                codeEngName = JExcelUtil.getContent(cell, 4).trim();
                codeDesc = JExcelUtil.getContent(cell, 5).trim();
                parentCode = JExcelUtil.getContent(cell, 6).trim();
                sort = JExcelUtil.getContent(cell, 7).trim();
                
                if(codeType.length()>0 ){
                	ctype =(NumberCodeType) NumberCodeType.toNumberCodeType(codeType);
                }
                
                if (ctype!=null) {
                	
                	
                	//부모코드
                	NumberCode pCode = null;
                	if(parentCode.length()>0){
                     	/*
                		if(hash.containsKey(parentCode)) {
                     		pCode = (NumberCode)hash.get("parentCode");
                     		
                     		System.out.println(" containsKey parentCode =" + parentCode);
                     		
                     	}else {
                     		pCode  = partParentCode(ctype.toString(), parentCode);
                     		
                     		System.out.println(" NON containsKey parentCode =" + parentCode);
                     		hash.put(codeID, pCode);
                     	}
                     	*/
                		
                		pCode  = partParentCode(ctype.toString(), parentCode);
                    }
                	
                	System.out.println(codeID +","+ codeName +"," + parentCode +" "+ pCode);
                	
                	//자신의 코드
                	NumberCode nCode  = getPartCode(codeID, ctype.toString(), pCode);
                	
                	

                	if(nCode==null){
                		nCode = NumberCode.newNumberCode();
                		nCode.setCodeType(ctype);
                	}
                    
                	nCode.setCode(codeID);
                    nCode.setName(codeName);
                    nCode.setEngName(StringUtil.checkNull(codeEngName));
                    nCode.setDescription(codeDesc);
                    nCode.setSort(Integer.parseInt(sort));
                    nCode.setDisabled(false);
                  
                	
                    //if(parentCode.equals("B") && codeID.equals("01")) System.out.println("pCode------->>>"+pCode.getName());
                    if(pCode != null) nCode.setParent(pCode);
                    PersistenceHelper.manager.save(nCode);
                    
                   // System.out.println(">>"+codeID + " OK.");
                    //if(j>10) return;
                }
                else {
                    //System.out.println("NumberCodeRB . CodeType:"+ctype.toString()+", Code:"+codeID);
                }

             }//for  j
            
            

      //  }//for i

        System.out.println("###########################");
        System.out.println("Code Loader.");
        System.out.println("###########################");

    }//class

    public NumberCode partParentCode(String codeType, String pCodeID){
    	try{
    		
    		if(pCodeID == null && pCodeID.length() == 0) return null;
        	
    		QuerySpec select = new QuerySpec(NumberCode.class);
	        select.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", codeType), new int[] { 0 });
	        select.appendAnd();
	        select.appendWhere(new SearchCondition(NumberCode.class, "code", "=", pCodeID), new int[] { 0 });
	        QueryResult result = PersistenceHelper.manager.find(select);
	        
	        
	        System.out.println(select.toString());
	        
	        while (result.hasMoreElements())
	        {
	        	NumberCode pCode = (NumberCode)result.nextElement();
	        	return pCode;
	        }
	    }
	    catch (QueryException e)
	    {
	        e.printStackTrace();
	    }
	    catch (WTException e)
	    {
	        e.printStackTrace();
	    }
	    
	    return null;
    }
    
    /**
     * 
      * @desc :
      * @author : tsuam
      * @date : 2022. 11. 1.
      * @method : getPartCode
      * @param codeID
      * @param codeType
      * @param parentLocation
      * @return NumberCode
     */
    public NumberCode getPartCode(String codeID, String codeType, NumberCode parentCode){
		
			try{
				QuerySpec query = new QuerySpec(NumberCode.class);
				query.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", codeType), new int[] { 0 });
				query.appendAnd();
				//query.appendWhere(new SearchCondition(NumberCode.class, "disabled", SearchCondition.IS_FALSE), new int[] { 0 });
				//query.appendAnd();
				query.appendWhere(new SearchCondition(NumberCode.class, "code", "=", codeID), new int[] { 0 });
				query.appendAnd();
				
				if(parentCode == null) {
					query.appendWhere(new SearchCondition(NumberCode.class,"parentReference.key.id",SearchCondition.EQUAL,(long)0),new int[] { 0 });
				}else {
					query.appendWhere(new SearchCondition(NumberCode.class,"parentReference.key.id",SearchCondition.EQUAL,CommonUtil.getOIDLongValue(parentCode)),new int[] { 0 });
				}
				
				
		        QueryResult result = PersistenceHelper.manager.find(query);
		        
		        while (result.hasMoreElements())
		        {
		        	NumberCode partCode = (NumberCode)result.nextElement();
		        	return partCode;
		        }
			}catch(Exception ex){
				ex.printStackTrace();
				return null;
			}
			return null;
		
    }

}
