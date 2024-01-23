<%@page import="com.e3ps.distribute.DistributeRegistration"%>
<%@page import="com.e3ps.distribute._DistributeRegDistributeRegistration"%>
<%@page import="com.e3ps.interfaces.cpc.service.CPCHelper"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="com.e3ps.common.util.FTPUtil"%>
<%@page import="org.apache.commons.net.ftp.FTPClient"%>
<%@page import="wt.content.ContentRoleType"%>
<%@page import="wt.content.ContentItem"%>
<%@page import="wt.content.ContentHolder"%>
<%@page import="wt.content.ContentHelper"%>
<%@page import="wt.admin.AdminDomainRef"%>
<%@page import="wt.admin.AdministrativeDomainHelper"%>
<%@page import="wt.folder.SubFolder"%>
<%@page import="wt.session.SessionHelper"%>
<%@page import="wt.org.WTPrincipal"%>
<%@page import="wt.method.MethodContext"%>
<%@page import="wt.method.RemoteMethodServer"%>
<%@page import="com.e3ps.admin.AuthorityGroup"%>
<%@page import="com.e3ps.admin.util.ObjectKey"%>
<%@page import="wt.folder.FolderMemberLink"%>
<%@page import="wt.content.ApplicationData"%>
<%@page import="com.e3ps.part.bean.PartData"%>
<%@page import="java.util.Map"%>
<%@page import="com.e3ps.part.service.BomHelper"%>
<%@page import="com.e3ps.part.bean.BomTreeData"%>
<%@page import="java.util.List"%>
<%@page import="com.e3ps.common.util.StringUtil"%>
<%@page import="com.e3ps.common.util.CommonUtil"%>
<%@page import="com.e3ps.part.service.PartHelper"%>
<%@page import="java.util.HashMap"%>
<%@page import="wt.vc.views.ViewHelper"%>
<%@page import="wt.vc.views.View"%>
<%@page import="wt.vc.baseline.Baseline"%>
<%@page import="com.ptc.wvs.common.cadagent.CadProxy"%>
<%@page import="wt.lifecycle.State"%>
<%@page import="wt.vc.VersionControlHelper"%>
<%@page import="wt.fc.QueryResult"%>
<%@page import="wt.fc.ReferenceFactory"%>
<%@page import="wt.part.WTPart"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	/* String oid = "wt.content.ApplicationData:1082126";
	ApplicationData appData = (ApplicationData) CommonUtil.getObject(oid);
	System.out.println("ecr appData -> "+appData);
	System.out.println("appData.getRole() -> "+appData.getRole()); */
	/* out.println("start\n");
	CadProxy cadProxy = new CadProxy();
	out.println(cadProxy.getUseworker()+"\n");
	out.println("end\n");
	
	
	FolderMemberLink */
	/* out.print("map:"+ request.getServerName() +"<br>" + request.getRequestURI() + "<br>");
	String[] uriArr = request.getRequestURI().split("/");
	String contextName = uriArr[2];
	out.print("contextName:"+ contextName+ "<br>");
	WTPrincipal sessionUser = SessionHelper.manager.getPrincipal();
	out.println("sessionUser ==> " +sessionUser); */
	//out.print(keyList.stream().filter(item -> ag.getAuthObjectType().equals((String)item.get("code"))).findFirst().orElseThrow(()-> new Exception("ENUM 클래스에 코드가 존재하지 않음")).get("name"));
	
// 	AdminDomainRef ref = AdministrativeDomainHelper.getAdminDomainRef("/Default");
// 	SubFolder folder = (SubFolder)CommonUtil.getObject("wt.folder.SubFolder:1136908");
// 	AdministrativeDomainHelper.manager.changeAdministrativeDomain(folder, ref, true);
DistributeRegistration distributeReg =(DistributeRegistration) CommonUtil.getObject("com.e3ps.distribute.DistributeRegistration:5932036"); 
	QueryResult qr = ContentHelper.service.getContentsByRole(distributeReg, ContentRoleType.SECONDARY);
			
			while(qr.hasMoreElements()) {
				ContentItem conItem = (ContentItem)qr.nextElement();
				ApplicationData appData  = (ApplicationData)conItem;
				out.print("appdata:" + appData.getFileName()+"/"+appData.getPersistInfo().getObjectIdentifier().getStringValue()+"<br>");
				
				
			}
%>

<html>
<head>
<title>Test</title>
</head>
<body>
<%
/* EPMDocument epm = (EPMDocument) CommonUtil .getObject("wt.epm.EPMDocument:92062770");
Representation representation = PublishUtils.getRepresentation(epm);
boolean isOutOfDate = false;
if(representation != null){
	  DerivedImage dimage = (DerivedImage)representation;
    
    if(dimage != null){
  	  isOutOfDate = dimage.isOutOfDate();
    }
}

out.print("<br> isOutOfDate -> " + isOutOfDate); */
/* String oid = "wt.epm.EPMDocument:133880756";
EPMDocument epm = (EPMDocument) CommonUtil .getObject(oid);
Object obj = CommonUtil.getPersistable(oid);
AccessControlData aclData = new AccessControlData(oid);
ContentHolder holder = ContentHelper.service.getContents((ContentHolder) obj);
String type = "SECONDARY";
ContentRoleType roleType = ContentRoleType.toContentRoleType(type);

QueryResult qr = ContentHelper.service.getContentsByRole(holder, roleType);
out.print("size : " + qr.size() + "<br>");
while (qr.hasMoreElements()) {
	ContentItem item = (ContentItem) qr.nextElement();

	if (item != null) {

		ApplicationData data = (ApplicationData) item;
		//URL viewUrl = ContentHelper.getDownloadURL(holder, data);// data.getViewContentURL(holder);
		
		//String url = CommonUtil.getURLString("/content/fileDownload") + "?holderOid=" + CommonUtil.getOIDString(holder) + "&appOid=" + CommonUtil.getOIDString(data);
		//data.setDescription("N");
		//Map<String, Object> map = new HashMap<String, Object>();
		out.print("file name  : " + data.getFileName() + " / " + data.getRole().getDisplay() + " / " + data.getPersistInfo().getObjectIdentifier().getStringValue() + "<br>");
		//map.put("name", data.getFileName());
		//map.put("oid", data.getPersistInfo().getObjectIdentifier().toString());
		//map.put("size", String.valueOf(data.getFileSize()));
		//map.put("url", url);
		//map.put("uploadedFromPath", viewUrl.toString());
		//map.put("isDownload", aclData.isDownload());
	}
} */
/* String FILE_PATH = "D:\\FILE_PATH";
String homeLocation = FILE_PATH;
String remotePath = homeLocation +"/DIST-2023-0001"  ;

// FTPClient client = FTPUtil.manager.connection();
QueryResult qr = ContentHelper.service.getContentsByRole((ContentHolder)CommonUtil.getObject("com.e3ps.doc.E3PSDocument:4874923"), ContentRoleType.PRIMARY);
out.println("?:"+qr.size());
 */
/* while (qr.hasMoreElements()) {
	 out.println("hi!");
		ContentItem item = (ContentItem) qr.nextElement();
		if (item != null) {
			
			ApplicationData data = (ApplicationData) item;
			
			out.println("app:"+data.getFileName());
			//FTPUtil.manager.uploadFTP(client, data, remotePath);
		}
	} */


String jsonString = JSONArray.toJSONString(CPCHelper.manager.getCompanys());
out.print("json String:"+jsonString+"<br>");
//FTPUtil.manager.disconnect(client);

%>
</body>
</html>

