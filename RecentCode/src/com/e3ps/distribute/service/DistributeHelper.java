package com.e3ps.distribute.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.CookieGenerator;

import com.e3ps.approval.ApprovalLine;
import com.e3ps.approval.ApprovalMaster;
import com.e3ps.approval.ApprovalObjectLink;
import com.e3ps.approval.bean.ApprovalListData;
import com.e3ps.approval.util.ApprovalUtil;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.content.util.ContentUtil;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.AccessControlUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.FileUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.ZipUtil;
import com.e3ps.common.web.PageQueryBroker;
import com.e3ps.distribute.DistributeDocument;
import com.e3ps.distribute.DistributePartToEpmLink;
import com.e3ps.distribute.DistributeRegPartToEpmLink;
import com.e3ps.distribute.DistributeRegToPartLink;
import com.e3ps.distribute.DistributeRegistration;
import com.e3ps.distribute.DistributeToPartLink;
import com.e3ps.distribute.bean.DistributeData;
import com.e3ps.distribute.bean.DistributePartToEpmData;
import com.e3ps.distribute.bean.DistributeRegPartToEpmData;
import com.e3ps.distribute.bean.DistributeRegistrationData;
import com.e3ps.doc.E3PSDocument;
import com.e3ps.org.People;
import com.e3ps.part.bean.PartData;
import com.e3ps.part.service.PartHelper;
import com.e3ps.part.util.PartUtil;
import com.e3ps.project.EOutput;

import wt.content.ApplicationData;
import wt.content.ContentHolder;
import wt.fc.PagingQueryResult;
import wt.fc.PagingSessionHelper;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.pdmlink.PDMLinkProduct;
import wt.query.ArrayExpression;
import wt.query.ClassAttribute;
import wt.query.ConstantExpression;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.query.SubSelectExpression;
import wt.services.ServiceFactory;
import wt.session.SessionHelper;
import wt.util.WTAttributeNameIfc;
import wt.util.WTException;

public class DistributeHelper {
   
   public static final DistributeService service = ServiceFactory.getService(DistributeService.class);

   public static final DistributeHelper manager = new DistributeHelper();

   

   /**
    * @methodName : getDistributePartListByDistribute
    * @author : shjeong
    * @date : 2023.07.20
    * @return : Map<String,Object>
    * @description : 배포 객체로 배포 부품들 가져오기
    */
   public List<DistributeRegToPartLink> getDistributePartListByDistribute(DistributeRegistration distributeReg) throws Exception{
      
      List<DistributeRegToPartLink> resultList = new ArrayList<DistributeRegToPartLink>();
      
      QueryResult result = PersistenceHelper.manager.navigate(distributeReg, DistributeRegToPartLink.PART_ROLE, DistributeRegToPartLink.class, false);
      
      while(result.hasMoreElements()) {
    	  DistributeRegToPartLink link = (DistributeRegToPartLink) result.nextElement();
         resultList.add(link);
      }
      
      return resultList;
   }
   
   public List<DistributeRegToPartLink> getDistributePartListByDistributeReg(DistributeRegistration distributeReg) throws Exception{
      
      List<DistributeRegToPartLink> resultList = new ArrayList<DistributeRegToPartLink>();
      
      QueryResult result = PersistenceHelper.manager.navigate(distributeReg, DistributeRegToPartLink.PART_ROLE, DistributeRegToPartLink.class, false);
      
      while(result.hasMoreElements()) {
    	  DistributeRegToPartLink link = (DistributeRegToPartLink) result.nextElement();
         resultList.add(link);
      }
      
      return resultList;
   }
   
   /**
    * @methodName : getDistributeRegPartListByDistribute
    * @author : shjeong - hgkang
    * @date : 2023.07.20
    * @return : Map<String,Object>
    * @description : 배포 oid로 배포 부품들 가져오기
    */
   public List<DistributeRegToPartLink> getDistributeRegPartListByDistribute(DistributeRegistration oid) throws Exception{
      
      List<DistributeRegToPartLink> resultList = new ArrayList<DistributeRegToPartLink>();
      
      QueryResult result = PersistenceHelper.manager.navigate((DistributeRegistration)oid, DistributeRegToPartLink.PART_ROLE, DistributeRegToPartLink.class, false);
      
      while(result.hasMoreElements()) {
         DistributeRegToPartLink link = (DistributeRegToPartLink) result.nextElement();
         resultList.add(link);
      }
      
      return resultList;
   }
   
   /**
    * @methodName : searchDistributeScroll
    * @author : shjeong
    * @date : 2023.07.20
    * @return : Map<String,Object>
    * @description : 배포 조회 로직
    */
   public Map<String, Object> searchDistributeScroll(Map<String, Object> reqMap) throws Exception {
      Map<String, Object> map = new HashMap<>();

      List<DistributeData> list = new ArrayList<>();

      int page = (Integer) reqMap.get("page");
      int rows = (Integer) reqMap.get("rows");
      String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));

      PagingQueryResult result = null;

      if (sessionId.length() > 0) {
         result = PagingSessionHelper.fetchPagingSession((page - 1) * rows, rows, Long.valueOf(sessionId));
      } else {
         QuerySpec query = getDistributeListQuery(reqMap);

         result = PageQueryBroker.openPagingSession((page - 1) * rows, rows, query, true);
      }

      int totalSize = result.getTotalSize();
      
      while (result.hasMoreElements()) {
         Object[] obj = (Object[]) result.nextElement();
         DistributeDocument dist = (DistributeDocument) obj[0];
         if(dist != null) {
            DistributeData data = new DistributeData(dist);
            list.add(data);
         }
      }

      map.put("list", list);
      map.put("totalSize", totalSize);
      map.put("sessionId", result.getSessionId() == 0 ? "" : result.getSessionId());

      return map;
   }
   
   /** 미완성
    * @methodName : getDistributeListQuery
    * @author : shjeong
    * @date : 2023.07.20
    * @return : Map<String,Object>
    * @description : 배포 조회 쿼리
    */
   public QuerySpec getDistributeListQuery(Map<String, Object> reqMap) throws Exception {

      String distName = StringUtil.checkNull((String) reqMap.get("distName"));
      distName = distName.replace("[", "[[]");
      String distNumber = StringUtil.checkNull((String) reqMap.get("distNumber"));
      String distType = StringUtil.checkNull((String) reqMap.get("distType"));
      String company = StringUtil.checkNull((String) reqMap.get("company"));
      String regException = StringUtil.checkReplaceStr((String) reqMap.get("regException"),"false").toLowerCase();
      
      List<String> creator = StringUtil.checkReplaceArray(reqMap.get("creator"));
      String pre_createDate = StringUtil.checkNull((String) reqMap.get("pre_createDate"));
      String post_createDate = StringUtil.checkNull((String) reqMap.get("post_createDate"));
      
      List<String> state = StringUtil.checkReplaceArray(reqMap.get("state"));
      String pre_modifyDate = StringUtil.checkNull((String) reqMap.get("pre_modifyDate"));
      String post_modifyDate = StringUtil.checkNull((String) reqMap.get("post_modifyDate"));
      
      
      List<String> relatedPart = StringUtil.checkReplaceArray(reqMap.get("relatedPart"));
      
      
      QuerySpec qs = new QuerySpec();
      SearchCondition sc = null;
      
      int idx1 = qs.appendClassList(DistributeDocument.class, true);
      
      //배포 요청서 생성되지 않은 리스트만 출력
      if("true".equals(regException)) {
    	  int idx2 = qs.appendClassList(DistributeRegistration.class, false);
	      
	      sc = new SearchCondition(new ClassAttribute(DistributeDocument.class, WTAttributeNameIfc.ID_NAME), SearchCondition.EQUAL, new ClassAttribute(DistributeRegistration.class, DistributeRegistration.DISTRIBUTE_REFERENCE+".key.id"));
	      sc.setOuterJoin(SearchCondition.RIGHT_OUTER_JOIN);
	      qs.appendWhere(sc, new int[] {idx1, idx2});
    	  
    	  if (qs.getConditionCount() > 0) {
    			qs.appendAnd();
    	  }
    	  qs.appendWhere(new SearchCondition(DistributeRegistration.class, WTAttributeNameIfc.ID_NAME, SearchCondition.IS_NULL, false), new int[] {idx2});
      }
      
      
      // 구매요청 명
      if(distName.length() > 0) {
         if(qs.getConditionCount()>0) {
            qs.appendAnd();
         }
         qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.DISTRIBUTE_NAME, SearchCondition.LIKE,"%"+distName.trim()+"%" , false), new int[] {idx1});
      }
      
      if(distType.length() > 0) {
         if(qs.getConditionCount()>0) {
            qs.appendAnd();
         }
         qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.DISTRIBUTE_TYPE, SearchCondition.EQUAL, distType, false), new int[] {idx1});
      }
      
      
      if(company.length() > 0) {
         if(qs.getConditionCount()>0) {
            qs.appendAnd();
         }
         qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.COMPANY_ID, SearchCondition.EQUAL, company, false), new int[] {idx1});
      }
      
      
      //배포요청 번호
      if(distNumber.length()>0) {
         if(qs.getConditionCount()>0) {
            qs.appendAnd();
         }
         //SQLFunction upper = SQLFunction.newSQLFunction("UPPER", new ClassAttribute(PurchaseRequest.class, PurchaseRequest.PURCHASE_NUMBER));
         //String number = (String)reqMap.get("distNumber");
//         qs.appendWhere(new SearchCondition(upper, SearchCondition.LIKE, new ConstantExpression("%" + number.toUpperCase() + "%")), new int[] {idx1});
         qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.DISTRIBUTE_NUMBER, SearchCondition.LIKE, "%" + distNumber.toUpperCase() + "%", true), new int[] {idx1});
      }
      
      
      ///////////////// 상세검색 ////////////////////////
      
      // 상태
      if (state.size() > 0) {
         if (qs.getConditionCount() > 0) {
            qs.appendAnd();
         }
         sc = new SearchCondition(
               new ClassAttribute(DistributeDocument.class, DistributeDocument.LIFE_CYCLE_STATE), SearchCondition.IN,
               new ArrayExpression(state.toArray()));
         qs.appendWhere(sc, new int[] { idx1 });
      }
      // 상태
//   	  if (qs.getConditionCount() > 0) {
//   	     qs.appendAnd();
//   	  }
//   	  qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.LIFE_CYCLE_STATE, SearchCondition.EQUAL, "APPROVING", false), new int[] {idx1});
   	   
      
      // 작성자
      if (creator.size() > 0) {
         List<Long> userOidLongValueList = new ArrayList<>();
         
         for(String pp : creator) {
            People people = (People) CommonUtil.getObject(pp);
            WTUser user = people.getUser();
            
            userOidLongValueList.add(CommonUtil.getOIDLongValue(user));
         }
         
         if (qs.getConditionCount() > 0) {
            qs.appendAnd();
         }
         qs.appendWhere(new SearchCondition(new ClassAttribute(DistributeDocument.class, DistributeDocument.CREATOR + "." + "key.id"), SearchCondition.IN, new ArrayExpression(userOidLongValueList.toArray())), new int[] { idx1 });
      }
      
      // 작성일
      if(pre_createDate.length() > 0) {
         if(qs.getConditionCount()>0) {
            qs.appendAnd();
         }
         qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.CREATE_TIMESTAMP, SearchCondition.GREATER_THAN_OR_EQUAL, DateUtil.convertStartDate(pre_createDate)), new int[] {idx1});
      }
      
      if(post_createDate.length() > 0) {
         if(qs.getConditionCount()>0) {
            qs.appendAnd();
         }
         qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.CREATE_TIMESTAMP, SearchCondition.LESS_THAN, DateUtil.convertStartDate(post_createDate)), new int[] {idx1});
      }
      
      // 수정일
      if(pre_modifyDate.length() > 0) {
         if(qs.getConditionCount()>0) {
            qs.appendAnd();
         }
         qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.MODIFY_TIMESTAMP, SearchCondition.GREATER_THAN_OR_EQUAL, DateUtil.convertStartDate(pre_modifyDate)), new int[] {idx1});
      }
      
      if(post_modifyDate.length() > 0) {
         if(qs.getConditionCount()>0) {
            qs.appendAnd();
         }
         qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.MODIFY_TIMESTAMP, SearchCondition.LESS_THAN, DateUtil.convertStartDate(post_modifyDate)), new int[] {idx1});
      }
      
      // 품목
      if (relatedPart.size() > 0) {
         qs.setAdvancedQueryEnabled(true); //최상위 쿼리 고급쿼리 설정
         
         List<Long> partOidLongValueList = new ArrayList<>();
         
         for(String partNumber : relatedPart) {
            List<WTPart> partList = PartHelper.manager.getPartAllVersion(partNumber);
            for(WTPart part : partList) {
               partOidLongValueList.add(CommonUtil.getOIDLongValue(part));
            }
         }
         
         QuerySpec subQuery = new QuerySpec(); 
         int idx2 = subQuery.appendClassList(DistributeToPartLink.class, false);
         subQuery.getFromClause().setAliasPrefix("link"); // PurchaseRequestPartLink 테이블 Alias link로 변경
         subQuery.setAdvancedQueryEnabled(true); // 고급쿼리 설정

         ClassAttribute purchaseOidColumn = new ClassAttribute(DistributeToPartLink.class,DistributeToPartLink.ROLE_AOBJECT_REF + ".key.id"); // 구매요청 oid 컬럼 
         subQuery.setDistinct(true); // distinct 설정으로 중복되어 나오는 oid 값 제거
         subQuery.appendSelect(purchaseOidColumn, new int[] { idx2 }, false); // select distinct link.[구매요청oid] from PurchaseRequestPartLink link;
         sc = new SearchCondition(new ClassAttribute(DistributeToPartLink.class, DistributeToPartLink.ROLE_BOBJECT_REF +  ".key.id"), SearchCondition.IN, new ArrayExpression(partOidLongValueList.toArray()));
         subQuery.appendWhere(sc, new int[] { idx2 });
         
         if(qs.getConditionCount()>0) {
            qs.appendAnd();
         }
         // and A0.ida2a2 in( select distinct link.[구매요청oid] from PurchaseRequestPartLink link );
         sc = new SearchCondition(new ClassAttribute(DistributeDocument.class, WTAttributeNameIfc.ID_NAME), SearchCondition.IN, new SubSelectExpression(subQuery));
         qs.appendWhere(sc, new int[] { idx1 });
         
      }
      
      qs.appendOrderBy(new OrderBy(new ClassAttribute(DistributeDocument.class, DistributeDocument.MODIFY_TIMESTAMP), true), new int[] { idx1 });
      return qs;
   }
   
   
   public List<DistributeData> getRefDistributeListByPart(String oid) throws Exception{
      
      List<DistributeData> returnlist = new ArrayList<>();
      
      QuerySpec qs = new QuerySpec();
      SearchCondition sc = null;
      
      int idx1 = qs.appendClassList(DistributeDocument.class, true);
      qs.setAdvancedQueryEnabled(true);
      
      QuerySpec subQuery = new QuerySpec(); 
      int idx2 = subQuery.appendClassList(DistributeToPartLink.class, false);
      subQuery.getFromClause().setAliasPrefix("link"); 
      subQuery.setAdvancedQueryEnabled(true);

      ClassAttribute purchaseOidColumn = new ClassAttribute(DistributeToPartLink.class,DistributeToPartLink.ROLE_AOBJECT_REF + ".key.id");
      subQuery.setDistinct(true); 
      subQuery.appendSelect(purchaseOidColumn, new int[] { idx2 }, false);
      sc = new SearchCondition(new ClassAttribute(DistributeToPartLink.class, DistributeToPartLink.ROLE_BOBJECT_REF +  ".key.id"), SearchCondition.EQUAL, new ConstantExpression(CommonUtil.getOIDLongValue(oid)));
      subQuery.appendWhere(sc, new int[] { idx2 });
      
      sc = new SearchCondition(new ClassAttribute(DistributeDocument.class, WTAttributeNameIfc.ID_NAME), SearchCondition.IN, new SubSelectExpression(subQuery));
      qs.appendWhere(sc, new int[] { idx1 });
      
      qs.appendOrderBy(new OrderBy(new ClassAttribute(DistributeDocument.class, DistributeDocument.MODIFY_TIMESTAMP), true), new int[] { idx1 });
      
      QueryResult qr = PersistenceHelper.manager.find(qs);
      
      while(qr.hasMoreElements()) {
         Object[] o = (Object[])qr.nextElement();
         DistributeDocument distribute = (DistributeDocument)o[0];
         returnlist.add(new DistributeData(distribute));
      }
      
      return returnlist;
   }
   
   /**
    * @desc : 배포 Data 가져오기
    * @author : shjeong - hgkang
    * @date : 2023. 07. 20.
    * @method : getDistributeRegFileList
    * @param Map<String, Object>
    * @return List<Map<String,Object>>
    */
   public List<DistributeRegPartToEpmData> getPartDistributeRegDataList(DistributeRegistration oid) throws Exception {
      
      List<DistributeRegToPartLink> partLinkList = getDistributeRegPartListByDistribute(oid);
      
      List<DistributeRegPartToEpmData> returnList = new ArrayList<DistributeRegPartToEpmData>();
      
      for (DistributeRegToPartLink link : partLinkList) {
         QueryResult result = PersistenceHelper.manager.navigate(link, DistributeRegPartToEpmLink.DISTRIBUTE_REG_PART_TO_EPM_ROLE, DistributeRegPartToEpmLink.class, false);
         while(result.hasMoreElements()) {
            DistributeRegPartToEpmLink dpl = (DistributeRegPartToEpmLink) result.nextElement();
            returnList.add(new DistributeRegPartToEpmData(dpl));
         }
      }
      
      Collections.sort(returnList, (a, b) -> a.getPartNumber().compareToIgnoreCase(b.getPartNumber()));
      
      return returnList;
   }
   
   
   public void fileDownAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
      FileInputStream fis= null;
      BufferedInputStream bis = null;
      ServletOutputStream so = null;
      BufferedOutputStream bos = null;
      try {
         String fileList = request.getParameter("fileList");
         String[] list = fileList.split(",");
         
         long time = System.currentTimeMillis();
         String wtHome = wt.util.WTProperties.getLocalProperties().getProperty("wt.home", "");
         String downPath = wtHome + "\\temp\\zipFile\\Drawing\\";
         String path = downPath + time ;
         
         List<ApplicationData> appList = new ArrayList<ApplicationData>();
         for(String oid : list) {
               
            List<ApplicationData> appDataList = ContentUtil.getDistributeFile((ContentHolder)CommonUtil.getObject(oid));
               
            for(ApplicationData appData :appDataList) {
               appList.add(appData);
            }
         }
            
         for(ApplicationData appData :appList) {

            FileUtil.copyFiles(appData, path, appData.getFileName());
         
         }
         
         
         String fileName = "DrawingList(" + list.length + ")_" + time + ".zip";
         
         boolean isZip = ZipUtil.compress(path, downPath, fileName);
         
         
         if(isZip) {
            File file = new File(downPath + "/" + fileName);
            response.reset();
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition","attachment;filename=\"" + fileName +"\";");
            
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            so = response.getOutputStream();
            bos = new BufferedOutputStream(so);
              
            byte[] data=new byte[2048];
            int input=0;
            while((input=bis.read(data))!=-1){
               bos.write(data, 0, input);
               bos.flush();
            }
         }
         CookieGenerator cg = new CookieGenerator();
         cg.setCookieName("fileDownload");
         cg.addCookie(response, "TRUE");
      } catch(Exception e) {
         e.printStackTrace();
      }catch(Throwable e) {
         e.printStackTrace();
      }finally {
         if(bos!=null) bos.close();
         if(bis!=null) bis.close();
         if(so!=null) so.close();
         if(fis!=null) fis.close();
      }
   }

   

   /**
    * @methodName : searchRegistrationApproval
    * @author : shjeong
    * @param b 
    * @date : 2023.07.20
    * @return : Map<String,Object>
    * @description : 배포 조회 로직
    */
   public Map<String, Object> searchDistributeRegistrationScrollAction(Map<String, Object> reqMap) throws Exception {
      Map<String, Object> map = new HashMap<>();

      List<DistributeRegistrationData> list = new ArrayList<>();

      int page = (Integer) reqMap.get("page");
      int rows = (Integer) reqMap.get("rows");
      String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));

      PagingQueryResult result = null;

      if (sessionId.length() > 0) {
         result = PagingSessionHelper.fetchPagingSession((page - 1) * rows, rows, Long.valueOf(sessionId));
      } else {
         QuerySpec query = getDistributeRegistrationListQuery(reqMap);

         result = PageQueryBroker.openPagingSession((page - 1) * rows, rows, query, true);
      }

      int totalSize = result.getTotalSize();
      
      while (result.hasMoreElements()) {
         Object[] obj = (Object[]) result.nextElement();
         DistributeRegistration distReg = (DistributeRegistration) obj[0];
         if(distReg != null) {
        	 DistributeRegistrationData data = new DistributeRegistrationData(distReg);
            list.add(data);
         }
      }

      map.put("list", list);
      map.put("totalSize", totalSize);
      map.put("sessionId", result.getSessionId() == 0 ? "" : result.getSessionId());

      return map;
   }
   
   /** 미완성
    * @methodName : getDistributeRegistrationListQuery
    * @author : shjeong
    * @date : 2023.07.20
    * @return : Map<String,Object>
    * @description : 배포 조회 쿼리
    */
   public QuerySpec getDistributeRegistrationListQuery(Map<String, Object> reqMap) throws Exception {

      String distName = StringUtil.checkNull((String) reqMap.get("distName"));
      distName = distName.replace("[", "[[]");
      String distNumber = StringUtil.checkNull((String) reqMap.get("distNumber"));
      String distributeCompany = StringUtil.checkNull((String) reqMap.get("distributeCompany"));
      String distributeTarget = StringUtil.checkNull((String) reqMap.get("distributeTarget"));
      
      List<String> creator = StringUtil.checkReplaceArray(reqMap.get("creator"));
      String pre_createDate = StringUtil.checkNull((String) reqMap.get("pre_createDate"));
      String post_createDate = StringUtil.checkNull((String) reqMap.get("post_createDate"));
      
      List<String> state = StringUtil.checkReplaceArray(reqMap.get("state"));
      String pre_modifyDate = StringUtil.checkNull((String) reqMap.get("pre_modifyDate"));
      String post_modifyDate = StringUtil.checkNull((String) reqMap.get("post_modifyDate"));
      
      String pre_distDate = StringUtil.checkNull((String) reqMap.get("pre_distDate"));
      String post_distDate = StringUtil.checkNull((String) reqMap.get("post_distDate"));
      
      
      List<String> relatedPart = StringUtil.checkReplaceArray(reqMap.get("relatedPart"));
      
      
      QuerySpec qs = new QuerySpec();
      SearchCondition sc = null;
      
      int idx1 = qs.appendClassList(DistributeRegistration.class, true);
      int idx2 = qs.appendClassList(DistributeDocument.class, false);
      
      //Equal Join
      sc = new SearchCondition(new ClassAttribute(DistributeRegistration.class,DistributeRegistration.DISTRIBUTE_REFERENCE+"."+WTAttributeNameIfc.REF_OBJECT_ID),"=",new ClassAttribute(DistributeDocument.class,WTAttributeNameIfc.ID_NAME));
      qs.appendWhere(sc, new int[] {idx1, idx2});
      
      
      if(distName.length() > 0) {
         if(qs.getConditionCount()>0) {
            qs.appendAnd();
         }
         qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.DISTRIBUTE_NAME, SearchCondition.LIKE,"%"+distName.trim()+"%" , false), new int[] {idx2});
      }
      
      if(distNumber.length()>0) {
    	  if(qs.getConditionCount()>0) {
    		  qs.appendAnd();
    	  }
    	  qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.DISTRIBUTE_NUMBER, SearchCondition.LIKE, "%" + distNumber.toUpperCase() + "%", true), new int[] {idx2});
      }
      
      if(distributeCompany.length() > 0) {
         if(qs.getConditionCount()>0) {
            qs.appendAnd();
         }
         qs.appendWhere(new SearchCondition(DistributeRegistration.class, DistributeRegistration.DISTRIBUTE_COMPANY, SearchCondition.EQUAL, distributeCompany, false), new int[] {idx1});
      }
      
      if(distributeTarget.length() > 0) {
          if(qs.getConditionCount()>0) {
             qs.appendAnd();
          }
          qs.appendWhere(new SearchCondition(DistributeRegistration.class, DistributeRegistration.DISTRIBUTE_COMPANY, SearchCondition.EQUAL, distributeTarget, false), new int[] {idx1});
       }
      
      if(pre_distDate.length() > 0) {
          if(qs.getConditionCount()>0) {
             qs.appendAnd();
          }
          qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.DISTRIBUTE_DATE, SearchCondition.GREATER_THAN_OR_EQUAL, DateUtil.convertStartDate(pre_distDate)), new int[] {idx2});
       }
       
       if(post_distDate.length() > 0) {
          if(qs.getConditionCount()>0) {
             qs.appendAnd();
          }
          qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.DISTRIBUTE_DATE, SearchCondition.LESS_THAN, DateUtil.convertStartDate(post_distDate)), new int[] {idx2});
       }
      
      
      ///////////////// 상세검색 ////////////////////////
      
      // 상태
      if (state.size() > 0) {
          if (qs.getConditionCount() > 0) {
             qs.appendAnd();
          }
          sc = new SearchCondition(
                new ClassAttribute(DistributeRegistration.class, DistributeRegistration.LIFE_CYCLE_STATE), SearchCondition.IN,
                new ArrayExpression(state.toArray()));
          qs.appendWhere(sc, new int[] { idx1 });
       }
      
      // 작성자
      if (creator.size() > 0) {
         List<Long> userOidLongValueList = new ArrayList<>();
         
         for(String pp : creator) {
            People people = (People) CommonUtil.getObject(pp);
            WTUser user = people.getUser();
            
            userOidLongValueList.add(CommonUtil.getOIDLongValue(user));
         }
         
         if (qs.getConditionCount() > 0) {
            qs.appendAnd();
         }
         qs.appendWhere(new SearchCondition(new ClassAttribute(DistributeRegistration.class, DistributeRegistration.CREATOR + "." + "key.id"), SearchCondition.IN, new ArrayExpression(userOidLongValueList.toArray())), new int[] { idx1 });
      }
      
      // 작성일
      if(pre_createDate.length() > 0) {
         if(qs.getConditionCount()>0) {
            qs.appendAnd();
         }
         qs.appendWhere(new SearchCondition(DistributeRegistration.class, DistributeRegistration.CREATE_TIMESTAMP, SearchCondition.GREATER_THAN_OR_EQUAL, DateUtil.convertStartDate(pre_createDate)), new int[] {idx1});
      }
      
      if(post_createDate.length() > 0) {
         if(qs.getConditionCount()>0) {
            qs.appendAnd();
         }
         qs.appendWhere(new SearchCondition(DistributeRegistration.class, DistributeRegistration.CREATE_TIMESTAMP, SearchCondition.LESS_THAN, DateUtil.convertStartDate(post_createDate)), new int[] {idx1});
      }
      
      // 수정일
      if(pre_modifyDate.length() > 0) {
         if(qs.getConditionCount()>0) {
            qs.appendAnd();
         }
         qs.appendWhere(new SearchCondition(DistributeRegistration.class, DistributeRegistration.MODIFY_TIMESTAMP, SearchCondition.GREATER_THAN_OR_EQUAL, DateUtil.convertStartDate(pre_modifyDate)), new int[] {idx1});
      }
      
      if(post_modifyDate.length() > 0) {
         if(qs.getConditionCount()>0) {
            qs.appendAnd();
         }
         qs.appendWhere(new SearchCondition(DistributeRegistration.class, DistributeRegistration.MODIFY_TIMESTAMP, SearchCondition.LESS_THAN, DateUtil.convertStartDate(post_modifyDate)), new int[] {idx1});
      }
      
      // 품목
      if (relatedPart.size() > 0) {
         qs.setAdvancedQueryEnabled(true); //최상위 쿼리 고급쿼리 설정
         
         List<Long> partOidLongValueList = new ArrayList<>();
         
         for(String partNumber : relatedPart) {
            List<WTPart> partList = PartHelper.manager.getPartAllVersion(partNumber);
            for(WTPart part : partList) {
               partOidLongValueList.add(CommonUtil.getOIDLongValue(part));
            }
         }
         
         QuerySpec subQuery = new QuerySpec(); 
         int idx3 = subQuery.appendClassList(DistributeRegToPartLink.class, false);
         subQuery.getFromClause().setAliasPrefix("link"); // PurchaseRequestPartLink 테이블 Alias link로 변경
         subQuery.setAdvancedQueryEnabled(true); // 고급쿼리 설정

         ClassAttribute purchaseOidColumn = new ClassAttribute(DistributeRegToPartLink.class,DistributeRegToPartLink.ROLE_AOBJECT_REF + ".key.id"); // 구매요청 oid 컬럼 
         subQuery.setDistinct(true); // distinct 설정으로 중복되어 나오는 oid 값 제거
         subQuery.appendSelect(purchaseOidColumn, new int[] { idx3 }, false); // select distinct link.[구매요청oid] from PurchaseRequestPartLink link;
         sc = new SearchCondition(new ClassAttribute(DistributeRegToPartLink.class, DistributeRegToPartLink.ROLE_BOBJECT_REF +  ".key.id"), SearchCondition.IN, new ArrayExpression(partOidLongValueList.toArray()));
         subQuery.appendWhere(sc, new int[] { idx3 });
         
         if(qs.getConditionCount()>0) {
            qs.appendAnd();
         }
         // and A0.ida2a2 in( select distinct link.[구매요청oid] from PurchaseRequestPartLink link );
         sc = new SearchCondition(new ClassAttribute(DistributeRegistration.class, WTAttributeNameIfc.ID_NAME), SearchCondition.IN, new SubSelectExpression(subQuery));
         qs.appendWhere(sc, new int[] { idx1 });
         
      }
      
      qs.appendOrderBy(new OrderBy(new ClassAttribute(DistributeRegistration.class, DistributeRegistration.CREATE_TIMESTAMP), true), new int[] { idx1 });
      
      return qs;
   }

	public Map<String, Object> searchDistributeRereptionScroll(Map<String, Object> reqMap) throws Exception {
		 
		Map<String, Object> map = new HashMap<>();
	
//	     List<DistributeData> list = new ArrayList<>();
	     List<ApprovalListData> list = new ArrayList<ApprovalListData>();
	
	     int page = (Integer) reqMap.get("page");
	     int rows = (Integer) reqMap.get("rows");
	     String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));
	
	     PagingQueryResult result = null;
	
	     if (sessionId.length() > 0) {
	        result = PagingSessionHelper.fetchPagingSession((page - 1) * rows, rows, Long.valueOf(sessionId));
	     } else {
	        QuerySpec query = getDistributeRereptionListQuery(reqMap);
	
	        result = PageQueryBroker.openPagingSession((page - 1) * rows, rows, query, true);
	     }
	
	     int totalSize = result.getTotalSize();
	     
//	     while (result.hasMoreElements()) {
//	        Object[] obj = (Object[]) result.nextElement();
//	        DistributeDocument dist = (DistributeDocument) obj[0];
//	        if(dist != null) {
//	           DistributeData data = new DistributeData(dist);
//	           list.add(data);
//	        }
//	     }
	     while (result.hasMoreElements()) {
			Object[] objects = (Object[]) result.nextElement();
			ApprovalObjectLink link = (ApprovalObjectLink) objects[0];
			ApprovalLine line = (ApprovalLine) objects[1];
			ApprovalMaster master = (ApprovalMaster) link.getRoleBObject();
			String state = master.getState().toString();
			ApprovalListData data = ApprovalUtil.setApprovalObject(line, link, state);
			if(data!=null) list.add(data);

		}
	
	     map.put("list", list);
	     map.put("totalSize", totalSize);
	     map.put("sessionId", result.getSessionId() == 0 ? "" : result.getSessionId());
	
	     return map;
	}

	/** 미완성
	 * @methodName : getDistributeRegistrationListQuery
	 * @author : shjeong
	 * @date : 2023.07.20
	 * @return : Map<String,Object>
	 * @description : 배포 조회 쿼리
	 */
	public QuerySpec getDistributeRereptionListQuery(Map<String, Object> reqMap) throws Exception {
	
		List<String> creatorList = StringUtil.checkReplaceArray(reqMap.get("creator"));
		List<String> stateList = StringUtil.checkReplaceArray(reqMap.get("state"));
		List<String> objectTypeList = StringUtil.checkReplaceArray(reqMap.get("objectType"));

		String predate = StringUtil.checkNull((String) reqMap.get("predate"));
		String postdate = StringUtil.checkNull((String) reqMap.get("postdate"));
		String predate_completeDate = StringUtil.checkNull((String) reqMap.get("predate_completeDate"));
		String postdate_completeDate = StringUtil.checkNull((String) reqMap.get("postdate_completeDate"));

		String title = StringUtil.checkNull((String) reqMap.get("title"));

		QuerySpec qs = new QuerySpec();
		WTUser user = (WTUser) SessionHelper.getPrincipal();

		int idx0 = qs.addClassList(ApprovalMaster.class, false);
		int idx1 = qs.addClassList(ApprovalObjectLink.class, true);
		int idx2 = qs.addClassList(ApprovalLine.class, true);
		SearchCondition sc = null;
		// Join
		getApprovalJoinQuery(qs);

		// 로그인 유저 및 최신 결재 라인
		getOwnerApprovalLine(qs, idx2, true);

		// master owner
		if (!CommonUtil.isAdmin()) {
			qs.appendAnd();
			sc = new SearchCondition(ApprovalMaster.class, "owner.key.id", SearchCondition.EQUAL,
					CommonUtil.getOIDLongValue(user));
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 기안자
		qs.appendAnd();
		sc = new SearchCondition(ApprovalLine.class, ApprovalLine.ROLE, SearchCondition.EQUAL, ApprovalUtil.ROLE_DRAFT);
		qs.appendWhere(sc, new int[] { idx2 });
		
		if (qs.getConditionCount() > 0) {
			qs.appendAnd();
		}
	    qs.appendWhere(new SearchCondition(ApprovalMaster.class, ApprovalMaster.OBJECT_TYPE, SearchCondition.EQUAL, "DISTRIBUTEREG", false), new int[] {idx0});
		
	    if (qs.getConditionCount() > 0) {
			qs.appendAnd();
		}
	    qs.appendWhere(new SearchCondition(ApprovalMaster.class, ApprovalMaster.STATE, SearchCondition.EQUAL, "COMPLETED", false), new int[] {idx0});

		// 타이틀
		if (title.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.TITLE, SearchCondition.LIKE,
					"%" + title + "%", false);
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 등록자
		if (creatorList.size() > 0) {
			List<Long> userOidLongValueList = new ArrayList<>();

			for (String pp : creatorList) {
				People people = (People) CommonUtil.getObject(pp);
				WTUser searchUser = people.getUser();

				userOidLongValueList.add(CommonUtil.getOIDLongValue(searchUser));
			}

			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(ApprovalMaster.class, ApprovalMaster.OWNER + "." + "key.id"),
					SearchCondition.IN, new ArrayExpression(userOidLongValueList.toArray()));
			qs.appendWhere(sc, new int[] { idx0 });
		}


		// 마스터 상태 조회
		if (stateList.size() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(new ClassAttribute(ApprovalMaster.class, ApprovalMaster.STATE), SearchCondition.IN,
					new ArrayExpression(stateList.toArray()));
			qs.appendWhere(sc, new int[] { idx0 });
		} else {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.STATE, SearchCondition.NOT_EQUAL,
					ApprovalUtil.STATE_MASTER_TEMP_STORAGE);
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 마스터 생성일
		if (predate.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalMaster.class, WTAttributeNameIfc.CREATE_STAMP_NAME,
					SearchCondition.GREATER_THAN, DateUtil.convertStartDate(predate));
			qs.appendWhere(sc, new int[] { idx0 });
		}
		if (postdate.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalMaster.class, WTAttributeNameIfc.CREATE_STAMP_NAME,
					SearchCondition.LESS_THAN, DateUtil.convertEndDate(postdate));
			qs.appendWhere(sc, new int[] { idx0 });
		}

		// 마스터 완료일
		if (predate_completeDate.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.COMPLETE_DATE, SearchCondition.GREATER_THAN,
					DateUtil.convertStartDate(predate_completeDate));
			qs.appendWhere(sc, new int[] { idx0 });
		}
		if (postdate_completeDate.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalMaster.class, ApprovalMaster.COMPLETE_DATE, SearchCondition.LESS_THAN,
					DateUtil.convertEndDate(postdate_completeDate));
			qs.appendWhere(sc, new int[] { idx0 });
		}

		qs.appendOrderBy(new OrderBy(new ClassAttribute(ApprovalLine.class, ApprovalLine.START_DATE), true),
				new int[] { idx2 });
		
		return qs;
	}
	
	public void getApprovalJoinQuery(QuerySpec qs) throws Exception {

		SearchCondition outerJoinSc = new SearchCondition(ApprovalMaster.class, "thePersistInfo.theObjectIdentifier.id",
				ApprovalObjectLink.class, "roleBObjectRef.key.id");
		outerJoinSc.setOuterJoin(SearchCondition.NO_OUTER_JOIN);
		qs.appendWhere(outerJoinSc, new int[] { 0, 1 });

		qs.appendAnd();
		outerJoinSc = new SearchCondition(ApprovalMaster.class, "thePersistInfo.theObjectIdentifier.id",
				ApprovalLine.class, "masterReference.key.id");
		outerJoinSc.setOuterJoin(SearchCondition.NO_OUTER_JOIN);
		qs.appendWhere(outerJoinSc, new int[] { 0, 2 });

	}
	
	public void getOwnerApprovalLine(QuerySpec qs, int idx, boolean isLast) throws Exception {

		WTUser user = (WTUser) SessionHelper.getPrincipal();
		SearchCondition sc = null;
		/* 로그인 유저 */
		if (!CommonUtil.isAdmin()) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalLine.class, "owner.key.id", SearchCondition.EQUAL,
					CommonUtil.getOIDLongValue(user));
			qs.appendWhere(sc, new int[] { idx });
		}

		/* 최신 결재 라인 */
		if (isLast) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(ApprovalLine.class, ApprovalLine.LAST, SearchCondition.IS_TRUE);
			qs.appendWhere(sc, new int[] { idx });
		}

	}
	
	/** 미완성
	 * @methodName : getDistributeRegistrationListQuery
	 * @author : shjeong
	 * @date : 2023.07.20
	 * @return : Map<String,Object>
	 * @description : 배포 조회 쿼리
	 */
	public QuerySpec getDistributeRereptionListQuery222(Map<String, Object> reqMap) throws Exception {
	
	   String distName = StringUtil.checkNull((String) reqMap.get("distName"));
	   String distNumber = StringUtil.checkNull((String) reqMap.get("distNumber"));
	   String distType = StringUtil.checkNull((String) reqMap.get("distType"));
	   String company = StringUtil.checkNull((String) reqMap.get("company"));
	   
	   List<String> creator = StringUtil.checkReplaceArray(reqMap.get("creator"));
	   String pre_createDate = StringUtil.checkNull((String) reqMap.get("pre_createDate"));
	   String post_createDate = StringUtil.checkNull((String) reqMap.get("post_createDate"));
	   
	   List<String> state = StringUtil.checkReplaceArray(reqMap.get("state"));
	   String pre_modifyDate = StringUtil.checkNull((String) reqMap.get("pre_modifyDate"));
	   String post_modifyDate = StringUtil.checkNull((String) reqMap.get("post_modifyDate"));
	   
	   
	   List<String> relatedPart = StringUtil.checkReplaceArray(reqMap.get("relatedPart"));
	   
	   
	   QuerySpec qs = new QuerySpec();
	   SearchCondition sc = null;
	   
	   int idx1 = qs.appendClassList(DistributeDocument.class, true);
	   
	   
	   // 구매요청 명
	   if(distName.length() > 0) {
	      if(qs.getConditionCount()>0) {
	         qs.appendAnd();
	      }
	      qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.DISTRIBUTE_NAME, SearchCondition.LIKE,"%"+distName.trim()+"%" , false), new int[] {idx1});
	   }
	   
	   if(distType.length() > 0) {
	      if(qs.getConditionCount()>0) {
	         qs.appendAnd();
	      }
	      qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.DISTRIBUTE_TYPE, SearchCondition.EQUAL, distType, false), new int[] {idx1});
	   }
	   
	   
	   if(company.length() > 0) {
	      if(qs.getConditionCount()>0) {
	         qs.appendAnd();
	      }
	      qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.COMPANY_ID, SearchCondition.EQUAL, company, false), new int[] {idx1});
	   }
	   
	   
	   //배포요청 번호
	   if(distNumber.length()>0) {
	      if(qs.getConditionCount()>0) {
	         qs.appendAnd();
	      }
	      //SQLFunction upper = SQLFunction.newSQLFunction("UPPER", new ClassAttribute(PurchaseRequest.class, PurchaseRequest.PURCHASE_NUMBER));
	      //String number = (String)reqMap.get("distNumber");
	//      qs.appendWhere(new SearchCondition(upper, SearchCondition.LIKE, new ConstantExpression("%" + number.toUpperCase() + "%")), new int[] {idx1});
	      qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.DISTRIBUTE_NUMBER, SearchCondition.LIKE, "%" + distNumber.toUpperCase() + "%", true), new int[] {idx1});
	   }
	   
	   
	   ///////////////// 상세검색 ////////////////////////
	   
	   // 상태
	  if (qs.getConditionCount() > 0) {
	     qs.appendAnd();
	  }
	  qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.LIFE_CYCLE_STATE, SearchCondition.EQUAL, "COMPLETED", false), new int[] {idx1});
	   
	   // 작성자
	   if (creator.size() > 0) {
	      List<Long> userOidLongValueList = new ArrayList<>();
	      
	      for(String pp : creator) {
	         People people = (People) CommonUtil.getObject(pp);
	         WTUser user = people.getUser();
	         
	         userOidLongValueList.add(CommonUtil.getOIDLongValue(user));
	      }
	      
	      if (qs.getConditionCount() > 0) {
	         qs.appendAnd();
	      }
	      qs.appendWhere(new SearchCondition(new ClassAttribute(DistributeDocument.class, DistributeDocument.CREATOR + "." + "key.id"), SearchCondition.IN, new ArrayExpression(userOidLongValueList.toArray())), new int[] { idx1 });
	   }
	   
	   // 작성일
	   if(pre_createDate.length() > 0) {
	      if(qs.getConditionCount()>0) {
	         qs.appendAnd();
	      }
	      qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.CREATE_TIMESTAMP, SearchCondition.GREATER_THAN_OR_EQUAL, DateUtil.convertStartDate(pre_createDate)), new int[] {idx1});
	   }
	   
	   if(post_createDate.length() > 0) {
	      if(qs.getConditionCount()>0) {
	         qs.appendAnd();
	      }
	      qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.CREATE_TIMESTAMP, SearchCondition.LESS_THAN, DateUtil.convertStartDate(post_createDate)), new int[] {idx1});
	   }
	   
	   // 수정일
	   if(pre_modifyDate.length() > 0) {
	      if(qs.getConditionCount()>0) {
	         qs.appendAnd();
	      }
	      qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.MODIFY_TIMESTAMP, SearchCondition.GREATER_THAN_OR_EQUAL, DateUtil.convertStartDate(pre_modifyDate)), new int[] {idx1});
	   }
	   
	   if(post_modifyDate.length() > 0) {
	      if(qs.getConditionCount()>0) {
	         qs.appendAnd();
	      }
	      qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.MODIFY_TIMESTAMP, SearchCondition.LESS_THAN, DateUtil.convertStartDate(post_modifyDate)), new int[] {idx1});
	   }
	   
	   // 품목
	   if (relatedPart.size() > 0) {
	      qs.setAdvancedQueryEnabled(true); //최상위 쿼리 고급쿼리 설정
	      
	      List<Long> partOidLongValueList = new ArrayList<>();
	      
	      for(String partNumber : relatedPart) {
	         List<WTPart> partList = PartHelper.manager.getPartAllVersion(partNumber);
	         for(WTPart part : partList) {
	            partOidLongValueList.add(CommonUtil.getOIDLongValue(part));
	         }
	      }
	      
	      QuerySpec subQuery = new QuerySpec(); 
	      int idx2 = subQuery.appendClassList(DistributeToPartLink.class, false);
	      subQuery.getFromClause().setAliasPrefix("link"); // PurchaseRequestPartLink 테이블 Alias link로 변경
	      subQuery.setAdvancedQueryEnabled(true); // 고급쿼리 설정
	
	      ClassAttribute purchaseOidColumn = new ClassAttribute(DistributeToPartLink.class,DistributeToPartLink.ROLE_AOBJECT_REF + ".key.id"); // 구매요청 oid 컬럼 
	      subQuery.setDistinct(true); // distinct 설정으로 중복되어 나오는 oid 값 제거
	      subQuery.appendSelect(purchaseOidColumn, new int[] { idx2 }, false); // select distinct link.[구매요청oid] from PurchaseRequestPartLink link;
	      sc = new SearchCondition(new ClassAttribute(DistributeToPartLink.class, DistributeToPartLink.ROLE_BOBJECT_REF +  ".key.id"), SearchCondition.IN, new ArrayExpression(partOidLongValueList.toArray()));
	      subQuery.appendWhere(sc, new int[] { idx2 });
	      
	      if(qs.getConditionCount()>0) {
	         qs.appendAnd();
	      }
	      // and A0.ida2a2 in( select distinct link.[구매요청oid] from PurchaseRequestPartLink link );
	      sc = new SearchCondition(new ClassAttribute(DistributeDocument.class, WTAttributeNameIfc.ID_NAME), SearchCondition.IN, new SubSelectExpression(subQuery));
	      qs.appendWhere(sc, new int[] { idx1 });
	      
	   }
	   
	   qs.appendOrderBy(new OrderBy(new ClassAttribute(DistributeDocument.class, DistributeDocument.MODIFY_TIMESTAMP), true), new int[] { idx1 });
	   
	   return qs;
	}

	public DistributeRegistration getDistributeRegistrationByDistributeDocument(String oid) throws WTException {

		QuerySpec qs = new QuerySpec();
		SearchCondition sc = null;
		   
		int idx2 = qs.appendClassList(DistributeRegistration.class, true);
		sc = new SearchCondition(DistributeRegistration.class, DistributeRegistration.DISTRIBUTE_REFERENCE + ".key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(oid));
	    qs.appendWhere(sc, new int[] {idx2});
		
	    QueryResult result = PersistenceHelper.manager.find(qs);
	    
	    if (result.hasMoreElements()) {
	        Object[] o = (Object[]) result.nextElement();
	        return (DistributeRegistration) o[0];
	     }
	    
		return null;
	}
	
	public DistributeRegToPartLink getDistributeRegToPartLinkOid (DistributeRegistration distributeReg) throws WTException {
		
		QuerySpec qs = new QuerySpec();
		SearchCondition sc = null;
		
		int idx = qs.appendClassList(DistributeRegToPartLink.class, true);
		int idx1 = qs.appendClassList(DistributeRegistration.class, false);
		
		sc = new SearchCondition(DistributeRegistration.class, DistributeRegistration.PERSIST_INFO + ".theObjectIdentifier.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(distributeReg));
	    qs.appendWhere(sc, new int[] {idx1});
		
		if (qs.getConditionCount() > 0) {
			qs.appendAnd();
		}
		sc = new SearchCondition(new ClassAttribute(DistributeRegToPartLink.class, DistributeRegToPartLink.ROLE_AOBJECT_REF + ".key.id"), SearchCondition.EQUAL, new ClassAttribute(DistributeRegistration.class, DistributeRegistration.PERSIST_INFO + ".theObjectIdentifier.id"));
		qs.appendWhere(sc, new int[] { idx, idx1 });
		
		QueryResult result = PersistenceHelper.manager.find(qs);
		
		if (result.hasMoreElements()) {
	        Object[] o = (Object[]) result.nextElement();
	        return (DistributeRegToPartLink) o[0];
	     }
		
		
		return null;
		
	}
	
	public DistributeRegistration getDistributeRegistrationToPartLink (DistributeRegToPartLink distributeRegPart) throws WTException{
		
		QuerySpec qs = new QuerySpec();
		SearchCondition sc = null;
		//	thePersistInfo.theObjectIdentifier.id
		
		int idx = qs.appendClassList(DistributeRegistration.class, true);
		int idx1 = qs.appendClassList(DistributeRegToPartLink.class, true);
		
		sc = new SearchCondition(DistributeRegToPartLink.class, DistributeRegToPartLink.PERSIST_INFO + ".theObjectIdentifier.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(distributeRegPart));
	    qs.appendWhere(sc, new int[] {idx1});
		
		if (qs.getConditionCount() > 0) {
			qs.appendAnd();
		}
		sc = new SearchCondition(new ClassAttribute(DistributeRegToPartLink.class, DistributeRegToPartLink.ROLE_AOBJECT_REF + ".key.id"), SearchCondition.EQUAL, new ClassAttribute(DistributeRegistration.class, DistributeRegistration.PERSIST_INFO + ".theObjectIdentifier.id"));
		qs.appendWhere(sc, new int[] { idx1, idx });
		
		QueryResult result = PersistenceHelper.manager.find(qs);
		
		if (result.hasMoreElements()) {
	        Object[] o = (Object[]) result.nextElement();
	        return (DistributeRegistration) o[0];
	     }
		
		return null;
		
	}
	
	/**
	 * @methodName : getDistributeRegToParts_By_DistributeRegistration
	 * @author : shjeong - hgkang
	 * @date : 2022.11.11
	 * @return : Map<String,Object>
	 * @description : 배포요청 객체로 부품들 가져오기
	 */
	public List<DistributeRegToPartLink> getDistributeRegToParts_By_DistributeRegistration(DistributeRegistration distributeReg) throws Exception{
		
		List<DistributeRegToPartLink> resultList = new ArrayList<DistributeRegToPartLink>();
		
		QueryResult result = PersistenceHelper.manager.navigate(distributeReg, DistributeRegToPartLink.PART_ROLE, DistributeRegToPartLink.class, false);
		
		while(result.hasMoreElements()) {
			DistributeRegToPartLink link = (DistributeRegToPartLink) result.nextElement();
	         resultList.add(link);
	      }
		
		return resultList;
	}
	
	/** 미완성
	 * @methodName : getDistributeAutoSearchQuery
	 * @author : shjeong
	 * @date : 2023.12.14
	 * @return : Map<String,Object>
	 * @description : 도면 출도 의뢰서 자동 검색 쿼리
	 */
	public QuerySpec getDistributeAutoSearchQuery(Map<String, Object> reqMap) throws Exception {
	

		  String keyword = StringUtil.checkNull((String) reqMap.get("keyword"));
		
	      QuerySpec qs = new QuerySpec();
	      SearchCondition sc = null;
	      
	      int idx1 = qs.appendClassList(DistributeDocument.class, true);
	      int idx2 = qs.appendClassList(DistributeRegistration.class, false);
	      
	      sc = new SearchCondition(new ClassAttribute(DistributeDocument.class, WTAttributeNameIfc.ID_NAME), SearchCondition.EQUAL, new ClassAttribute(DistributeRegistration.class, DistributeRegistration.DISTRIBUTE_REFERENCE+".key.id"));
	      sc.setOuterJoin(SearchCondition.RIGHT_OUTER_JOIN);
	      qs.appendWhere(sc, new int[] {idx1, idx2});
	      
	      if(keyword.length() > 0) {
	    	 if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			 }
	    	 qs.appendOpenParen();
	         qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.DISTRIBUTE_NAME, SearchCondition.LIKE,"%"+keyword.trim().toUpperCase()+"%" , false), new int[] {idx1});
	         qs.appendOr();
	         qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.DISTRIBUTE_NUMBER, SearchCondition.LIKE, "%" + keyword.trim().toUpperCase() + "%", false), new int[] {idx1});
	         qs.appendCloseParen();
	      }
	      
	      if (qs.getConditionCount() > 0) {
				qs.appendAnd();
		  }
	      qs.appendWhere(new SearchCondition(DistributeDocument.class, DistributeDocument.LIFE_CYCLE_STATE, SearchCondition.EQUAL, "APPROVED", false), new int[] {idx1});
	      
	      if (qs.getConditionCount() > 0) {
				qs.appendAnd();
		  }
	      qs.appendWhere(new SearchCondition(DistributeRegistration.class, WTAttributeNameIfc.ID_NAME, SearchCondition.IS_NULL, false), new int[] {idx2});
	      
	      qs.appendOrderBy(new OrderBy(new ClassAttribute(DistributeDocument.class, DistributeDocument.CREATE_TIMESTAMP), true), new int[] { idx1 });
	      
	      return qs;
	}


}