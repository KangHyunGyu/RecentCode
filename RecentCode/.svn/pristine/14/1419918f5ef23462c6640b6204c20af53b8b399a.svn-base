<%@page import="org.apache.commons.codec.binary.Hex"%>
<%@page import="javax.crypto.Mac"%>
<%@page import="javax.crypto.spec.SecretKeySpec"%>
<%@page import="wt.session.SessionHelper"%>
<%@page import="org.springframework.web.servlet.ModelAndView"%>
<%@page import="org.springframework.web.servlet.view.RedirectView"%>
<%@page import="java.net.URL"%>
<%@page import="com.e3ps.common.jdf.config.ConfigImpl"%>
<%@page import="com.e3ps.common.drm.DRMService"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="wt.content.ContentServerHelper"%>
<%@page import="java.io.InputStream"%>
<%@page import="wt.content.FormatContentHolder"%>
<%@page import="java.util.Vector"%>
<%@page import="wt.content.ContentHolder"%>
<%@page import="wt.content.ContentHelper"%>
<%@page import="wt.content.ApplicationData"%>
<%@page import="wt.org.DirectoryContextProvider"%>
<%@page import="wt.org.WTGroup"%>
<%@page import="com.e3ps.common.util.CommonUtil"%>
<%@page import="java.util.Set"%>
<%@page import="wt.org.WTPrincipal"%>
<%@page import="wt.org.WTUser"%>
<%@page import="java.util.Enumeration"%>
<%@page import="wt.licenseusage.licensing.LicenseManagerHelper"%>
<%@page import="wt.org.LicenseGroups"%>
<%@page import="wt.org.LicenseGroupHelper"%>
<%@page import="wt.log4j.SystemOutFacade"%>
<%@page import="wt.fc.QueryResult"%>
<%@page import="wt.org.GroupUserLink"%>
<%@page import="wt.query.QuerySpec"%>
<%@page import="java.util.List"%>
<%@page import="wt.org.OrganizationServicesHelper"%>
<%@page import="com.duruan.shadowcube.SecureStreamReader"%>
<%@page import="com.duruan.shadowcube.SecureStreamException"%>
<%@page import="com.duruan.shadowcube.SecureStreamCreator"%>
<%@page import="com.e3ps.common.iba.IBAUtil"%>
<%@page import="com.e3ps.part.util.PartPropList"%>
<%@page import="jxl.Cell"%>
<%@page import="jxl.Sheet"%>
<%@page import="com.e3ps.common.util.JExcelUtil"%>
<%@page import="jxl.Workbook"%>
<%@page import="java.io.File"%>
<%@page import="wt.method.RemoteMethodServer"%>
<%@page import="wt.fc.PersistenceHelper"%>
<%@page import="wt.lifecycle.LifeCycleManaged"%>
<%@page import="wt.lifecycle.LifeCycleTemplate"%>
<%@page import="wt.lifecycle.LifeCycleHelper"%>
<%@page import="wt.clients.folder.FolderTaskLogic"%>
<%@page import="wt.folder.FolderHelper"%>
<%@page import="wt.folder.FolderEntry"%>
<%@page import="wt.folder.Folder"%>
<%@page import="wt.inf.container.WTContained"%>
<%@page import="wt.inf.container.WTContainerRef"%>
<%@page import="com.e3ps.common.util.WCUtil"%>
<%@page import="wt.inf.container.WTContainer"%>
<%@page import="com.e3ps.common.service.CommonHelper"%>
<%@page import="wt.vc.views.ViewHelper"%>
<%@page import="wt.part.Source"%>
<%@page import="wt.part.PartType"%>
<%@page import="wt.part.QuantityUnit"%>
<%@page import="wt.part.WTPart"%>
<%@page import="com.e3ps.common.util.StringUtil"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.e3ps.part.service.PartHelper"%>
<%@page import="com.e3ps.part.service.PartService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String secret = "A2EK1-UT58I-QB781-EM9D8-7T35B";
String companyCd = "1905010";
String epmId = "SSOTEST";
String apiTime = "20190701171111";


SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes("utf-8"), "HmacSHA512");

Mac hasher  = Mac.getInstance("HmacSHA512");
hasher.init(secretKey);
byte[] hash = hasher.doFinal((companyCd+epmId+apiTime).getBytes());
String en = Hex.encodeHexString(hash);

System.out.println("############## : " + en);
//gdddd
//"plm.wordexint.com/Windchill/userId=2015035&"

//2015035

//byte[] hhash  = hasher.getAlgorithm();

System.out.println(hasher.getAlgorithm());
System.out.println(hasher.getProvider());

//byte[] dn  = Hex.decodeHex( hhash.toString() );
//System.out.println("@@@@@@@@@@@@ : " + new String(dn,"UTF-8"));

%>


