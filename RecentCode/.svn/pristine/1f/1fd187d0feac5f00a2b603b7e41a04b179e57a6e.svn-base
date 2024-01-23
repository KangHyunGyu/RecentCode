<%@page import="com.e3ps.org.bean.PeopleData"%>
<%@page import ="wt.epm.EPMDocument,
				wt.part.WTPart,
				wt.org.*,
				wt.vc.*,
				wt.fc.*,
				wt.query.*,
				wt.util.*,
				wt.session.*,
				wt.epm.*,
				wt.iba.value.*,
				java.util.*,
				java.io.*,
				wt.clients.folder.FolderTaskLogic,
				wt.folder.Folder,
				com.e3ps.project.*,
				com.e3ps.project.beans.*,
				java.net.*,
				org.apache.poi.hssf.usermodel.HSSFCell,
				org.apache.poi.hssf.usermodel.HSSFRow,
				org.apache.poi.hssf.usermodel.HSSFSheet,
				org.apache.poi.hssf.usermodel.HSSFWorkbook,
				org.apache.poi.poifs.filesystem.POIFSFileSystem,
				java.text.*,java.util.*,
				wt.fc.*,wt.query.*,
				com.e3ps.common.util.*,
				java.math.BigDecimal,
				wt.query.ClassAttribute,wt.query.OrderBy,
				java.util.ArrayList"%>
<%@page contentType="text/html; charset=euc-kr"%>				
<jsp:useBean id="wtcontext" class="wt.httpgw.WTContextBean" scope="session" />
<jsp:setProperty name="wtcontext" property="request" value="<%=request%>" />
<%

String rootId = request.getParameter("oid");
ReferenceFactory rf = new ReferenceFactory();
ScheduleNode obj = (ScheduleNode)rf.getReference(rootId).getObject();

EProjectNode root = null;
if(obj instanceof EProjectNode){
	root = (EProjectNode)obj;
}else{
	ETaskNode taskNode = (ETaskNode)obj;
	root = taskNode.getProject();
}
long rootKey = root.getPersistInfo().getObjectIdentifier().getId();
ArrayList list = ProjectDao.manager.getStructure(rootKey);

     String sWtHome = "";
     String sFilePath = "", sFileName = "";
     int nSheetHeader = 1; // 
     
     	String refNo = "";
     	double amount = 1;

	   //file path
       sWtHome = wt.util.WTProperties.getLocalProperties().getProperty("wt.home", "");
       sFilePath = sWtHome + "\\codebase\\jsp\\project\\report" ;
       
       //file name
       SimpleDateFormat ff = new SimpleDateFormat("yyyyMMdd");
       sFileName = "Project_" +  ff.format(new Date()) + ".xls";

       //make WB
       POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(sFilePath + "\\ProjectWbs.xls"));
       HSSFWorkbook wb = new HSSFWorkbook(fs);
       HSSFSheet sheet = wb.getSheetAt(0);

       HSSFRow row = null;
       HSSFCell cell = null;
       short nCurrRow = (short)nSheetHeader;
       
       for(int i=0; i< list.size(); i++){
    	   String[] s = (String[])list.get(i);
           String level = s[0];
           String name = s[1];
           String sid = s[2];
           String parent = s[3];
           String desc = s[4];
           
           ScheduleNode node = (ScheduleNode)rf.getReference(sid).getObject();
           ProjectNodeData data = new ProjectNodeData(node);
 			
		   row = sheet.getRow(nCurrRow);
		   if(row==null)row = sheet.createRow(nCurrRow); 
		   nCurrRow++;

		   short cellRow = 0; 

		   //level
		   cell = row.getCell(cellRow);
		   if(cell==null)cell = row.createCell(cellRow);
		   cell.setCellValue(level);
		   cellRow++;
		   
		   //name
		   cell = row.getCell(cellRow);
		   if(cell==null)cell = row.createCell(cellRow);
		   cell.setCellValue(name);
		   cellRow++;
       		
		   //담당자
		   cell = row.getCell(cellRow);
		   if(cell==null)cell = row.createCell(cellRow);
		   cell.setCellValue(getOwner(node));
		   cellRow++;
		   
		   //작업현황
		   cell = row.getCell(cellRow);
		   if(cell==null)cell = row.createCell(cellRow);
		   cell.setCellValue(data.getState());
		   cellRow++;
		   
		 //계획시작일
		   cell = row.getCell(cellRow);
		   if(cell==null)cell = row.createCell(cellRow);
		   cell.setCellValue(DateUtil.getDateString(node.getPlanStartDate(),"d"));
		   cellRow++;
		   
		 //계획종료일
		   cell = row.getCell(cellRow);
		   if(cell==null)cell = row.createCell(cellRow);
		   cell.setCellValue(DateUtil.getDateString(node.getPlanEndDate(),"d"));
		   cellRow++;
		   
		   //실제 시작일
		   cell = row.getCell(cellRow);
		   if(cell==null)cell = row.createCell(cellRow);
		   cell.setCellValue(DateUtil.getDateString(node.getStartDate(),"d"));
		   cellRow++;
		   
		   //실제 종료일
		   cell = row.getCell(cellRow);
		   if(cell==null)cell = row.createCell(cellRow);
		   cell.setCellValue(DateUtil.getDateString(node.getEndDate(),"d"));
		   cellRow++;
		   
		   //선행업무
		   cell = row.getCell(cellRow);
		   if(cell==null)cell = row.createCell(cellRow);
		   cell.setCellValue(getPreTask(node));
		   cellRow++;
		   
		  //산출물등록
		   cell = row.getCell(cellRow);
		   if(cell==null)cell = row.createCell(cellRow);
		   cell.setCellValue(getOutput(node));
		   cellRow++;
    	}

    response.reset();
	response.setContentType("application/vnd.ms-excel;charset=euc-kr"); 
	response.setHeader("Content-Disposition","attachment; filename="+sFileName);
	ServletOutputStream objSos=response.getOutputStream();
    wb.write(objSos);

	%>
	
	<%!
	public String getOwner(ScheduleNode node) throws Exception{
		
		if(node instanceof EProjectNode){
			Object[] o = ProjectMemberHelper.service.getPM(node);
			ProjectRole role = (ProjectRole)o[0];
			RoleUserLink ulink = (RoleUserLink)o[1];
			PeopleData data = null;
			 
			if(ulink!=null){
				WTUser user = ulink.getUser();
			 	data = new PeopleData(user);
			}
			
			return data.getName();
		}
		
		String result = "";

		ArrayList members = ProjectMemberHelper.service.getOwner((ETaskNode)node);

		for(int i=0; i< members.size(); i++){
			 Object[] o = (Object[])members.get(i);
			 ProjectRole role = (ProjectRole)o[0];
			 RoleUserLink ulink = (RoleUserLink)o[1];
			 PeopleData data = null;
			 
			 if(ulink!=null){
				WTUser user = ulink.getUser();
			 	data = new PeopleData(user);
			 }
			
			 if(data!=null){
				result += data.getName();
				
				if(members.size()>i+1){
					result += ",";
				}
			 }
		}
		return result;
	}
	
	public String getPreTask(ScheduleNode node)throws Exception{
		String result = "";
		QueryResult qr = PersistenceHelper.manager.navigate(node,"pre",PrePostLink.class);
		boolean flag = false;
		while(qr.hasMoreElements()){
			ETaskNode sgnode = (ETaskNode)qr.nextElement();
			ProjectNodeData sd = new ProjectNodeData(sgnode);
			
			if(flag){
				result += ",";
			}
		
			result += sgnode.getName();
			flag = true;
		}
		return result;
	}

	public String getOutput(ScheduleNode node)throws Exception{
		String result = "";
		boolean flag = false;
		QueryResult qr2 = PersistenceHelper.manager.navigate(node,"output",TaskOutputLink.class);
		while(qr2.hasMoreElements()){
			
			if(flag){
				result += ",";
			}
			EOutput output = (EOutput)qr2.nextElement();
			result += output.getName();
			flag = true;
		}
		return result;
	}
	%>