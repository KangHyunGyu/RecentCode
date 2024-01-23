/**
 * 모듈명             	: com.e3ps.common.util
 * 프로그램 명       	: WCUtil
 * 기능설명           	: Windchill 관련 유틸
 * 프로그램 타입   	: Util
 * 비고 / 특이사항	: CommonUtil 과 기능 중복 체크 필요
 */

package com.e3ps.common.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.e3ps.common.jdf.config.Config;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.log4j.Log4jPackages;

import wt.admin.AdministrativeDomain;
import wt.admin.AdministrativeDomainHelper;
import wt.content.ApplicationData;
import wt.fc.ObjectIdentifier;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.fc.WTReference;
import wt.inf.container.ExchangeContainer;
import wt.inf.container.OrgContainer;
import wt.inf.container.WTContainer;
import wt.inf.container.WTContainerHelper;
import wt.inf.container.WTContainerRef;
import wt.inf.library.WTLibrary;
import wt.org.OrganizationServicesHelper;
import wt.org.WTGroup;
import wt.org.WTPrincipal;
import wt.org.WTPrincipalReference;
import wt.org.WTUser;
import wt.pdmlink.PDMLinkProduct;
import wt.pds.StatementSpec;
import wt.query.ClassAttribute;
import wt.query.CompoundQuerySpec;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.query.SetOperator;
import wt.util.WTException;
import wt.util.WTProperties;
import wt.util.WTRuntimeException;

public class WCUtil {
	private static ReferenceFactory rf;

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());

	/**
	 * @since 2007/05/27
	 * @author kang ho chul
	 * @see
	 * @return WTPrincipalReference - description: WTPrincipalReference 를 반환한다
	 * @throws WTException
	 * 
	 */
	public static WTPrincipalReference getWTPrincipalReference(String id) throws WTException {
		@SuppressWarnings("deprecation")
		WTPrincipalReference wtpr = OrganizationServicesHelper.manager.getPrincipalReference(id,
				OrganizationServicesHelper.manager.getDefaultDirectoryService());
		return wtpr;
	}

	/**
	 * @since 2007/05/27
	 * @author kang ho chul
	 * @see
	 * @return WTContainerRef - description: WTContainerRef 를 반환한다
	 * 
	 */
	public static WTContainerRef getWTContainerRef() throws WTException {

		PDMLinkProduct wtProduct;
		WTContainerRef wtContainerRef = null;
		try {
			wtProduct = getPDMLinkProduct();

			if (wtProduct != null) {
				wtContainerRef = WTContainerRef.newWTContainerRef(wtProduct);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return wtContainerRef;
	}

	/**
	 * @since 2007/05/27
	 * @author kang ho chul
	 * @see
	 * @return WTContainerRef - description: WTContainerRef 를 반환한다
	 * 
	 */
	public static WTContainerRef getWTContainerRef(String container) throws WTException {

		PDMLinkProduct wtProduct = getPDMLinkProduct(container);
		WTContainerRef wtContainerRef = null;
		if (wtProduct != null) {
			wtContainerRef = WTContainerRef.newWTContainerRef(wtProduct);
		}

		return wtContainerRef;
	}

	/**
	 * @since 2007/05/27
	 * @author kang ho chul
	 * @see
	 * @return PDMLinkProduct - description: PDMLinkProduct 를 반환한다
	 * 
	 */

	public static PDMLinkProduct getPDMLinkProduct(String container) throws WTException {
		QuerySpec qs = new QuerySpec(PDMLinkProduct.class);
		qs.appendWhere(new SearchCondition(PDMLinkProduct.class, PDMLinkProduct.NAME, SearchCondition.EQUAL,
				container), new int[] { 0 });
		QueryResult results = (QueryResult) PersistenceHelper.manager.find((StatementSpec) qs);
		PDMLinkProduct wtProduct = null;
		if (results.hasMoreElements()) {
			wtProduct = (PDMLinkProduct) results.nextElement();
		}

		return wtProduct;
	}

	/**
	 * @since 2007/05/27
	 * @author kang ho chul
	 * @see
	 * @return PDMLinkProduct - description: PDMLinkProduct 를 반환한다
	 * 
	 */

	public static PDMLinkProduct getPDMLinkProduct() throws WTException {
		Config conf = ConfigImpl.getInstance();
		String productName = conf.getString("product.context.name");
		QuerySpec qs = new QuerySpec(PDMLinkProduct.class);
		SearchCondition sc1 = new SearchCondition(PDMLinkProduct.class, PDMLinkProduct.NAME,
				SearchCondition.EQUAL, productName);
		qs.appendSearchCondition(sc1);
		QueryResult results = (QueryResult) PersistenceHelper.manager.find(qs);
		PDMLinkProduct wtProduct = null;
		if (results.hasMoreElements()) {
			wtProduct = (PDMLinkProduct) results.nextElement();
		}

		return wtProduct;
	}

	/**
	 * @since 2007/05/27
	 * @author kang ho chul
	 * @see
	 * @return PDMLinkProduct - description: PDMLinkProduct 를 반환한다
	 * 
	 */

	public static ExchangeContainer getSiteContainer() throws Exception {
		QuerySpec qs = new QuerySpec(ExchangeContainer.class);
		QueryResult results = (QueryResult) PersistenceHelper.manager.find((StatementSpec) qs);
		ExchangeContainer ec = null;
		if (results.hasMoreElements()) {
			ec = (ExchangeContainer) results.nextElement();
		}

		return ec;
	}

	/**
	 * 컨테이너의 레퍼런스를 얻는다
	 * @param container 컨테이너
	 * @return
	 * @throws WTException
	 */
	public static WTContainerRef getContainerRef(WTContainer container) throws WTException {
		if (container != null) {
			return WTContainerRef.newWTContainerRef(container);
		}
		return null;
	}
	
	/**
	 * 
	 * @desc	: Library
	 * @author	: tsuam
	 * @date	: 2019. 9. 17.
	 * @method	: getLibrary
	 * @return	: WTLibrary
	 * @param libraryName
	 * @return
	 * @throws WTException
	 */
	public static WTLibrary getLibrary(String libraryName)throws WTException{
		WTLibrary library = null;
		QuerySpec qs = new QuerySpec(WTLibrary.class);
		SearchCondition sc1 = new SearchCondition(WTLibrary.class, WTLibrary.NAME,
				SearchCondition.EQUAL, libraryName);
		qs.appendSearchCondition(sc1);
		QueryResult results = (QueryResult) PersistenceHelper.manager.find(qs);
		PDMLinkProduct wtProduct = null;
		if (results.hasMoreElements()) {
			library = (WTLibrary) results.nextElement();
		}
		return library;
	}
	
	/**
	 * @desc	:PLM host 정보
	 * @author	: tsuam  
	 * @date	: 2019. 9. 5.
	 * @method	: getPlmHost
	 * @return	: String
	 * @return
	 */
	public static String getPlmHost(){
		String hostName ="";
		try{
			hostName = WTProperties.getServerProperties().getProperty("wt.rmi.server.hostname");
		}catch(Exception e){
			e.printStackTrace();
		}
		return hostName;
	}
	
	/**
	 * 
	 * @desc	: Windchill wt.home D:\ptc\Windchill_10.2\Windchill
	 * @author	: tsuam
	 * @date	: 2019. 9. 5.
	 * @method	: getWTHome
	 * @return	: String
	 * @return
	 */
	public static String getWTHome(){
		String wtHome = "";
		try{
			wtHome = WTProperties.getServerProperties().getProperty("wt.home");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return wtHome;
	}
	
	public static String getWTTemp(){
		String wtHome = "";
		try{
			wtHome = WTProperties.getServerProperties().getProperty("wt.temp");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return wtHome;
	}
	
	public static String getWTLogFolder(){
		
		
		String logFolder = "";
		try{
			logFolder = WTProperties.getServerProperties().getProperty("wt.logs.dir");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return logFolder;
	}
	
	/**
	 * 
	 * @desc	: 그룹의 유저 리스트 
	 * @author	: tsuam
	 * @date	: 2019. 10. 11.
	 * @method	: getGroupMembers
	 * @return	: List<WTUser>
	 * @param groupName
	 * @return
	 */
	public static List<WTUser> getGroupMembers(String groupName) {
		
		List<WTUser> list = new ArrayList<WTUser>();
		
		try{
			
			WTGroup group = getGroup(groupName);
			
			Enumeration  aa = group.members();
			
			while(aa.hasMoreElements()){
				
				WTPrincipal principl = (WTPrincipal)aa.nextElement();
				WTUser user = (WTUser)principl;
				
				list.add(user);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * 
	 * @desc	: 그룹명 으로 WTGroup 찾기
	 * @author	: tsuam
	 * @date	: 2019. 10. 11.
	 * @method	: getGroup
	 * @return	: WTGroup
	 * @param groupID
	 * @return
	 * @throws WTException
	 */
	public static WTGroup getGroup (String groupName) throws WTException {
		 
		 if(groupName == null) return null;
		 
		 WTGroup grp = null;
		 
		 String targetOrgName = ConfigImpl.getInstance().getString("org.context.name");
		 QuerySpec qs = new QuerySpec();
		 int orgIndex = qs.appendClassList( OrgContainer.class, true );
		 SearchCondition sc = new SearchCondition( OrgContainer.class, "containerInfo.name", SearchCondition.EQUAL, targetOrgName );
		 qs.appendWhere( sc );
		 QueryResult qr = PersistenceHelper.manager.find( qs );
		 if( qr.hasMoreElements() ){
			 Persistable [] p = ( Persistable [] ) qr.nextElement();
			 OrgContainer org = ( OrgContainer ) p[0];
			 grp = OrganizationServicesHelper.manager.getGroup(groupName, org.getContextProvider() );
		 }
		 
		 return grp;
	 }
	/**
	 * 
	 * @desc	: product, library 가져오기
	 * @author	: mnyu
	 * @date	: 2019. 12. 23.
	 * @method	: getProductLibraryList
	 * @throws Exception 
	 */
	public static List<Map<String, String>> getProductLibraryList() throws Exception{
		List<Map<String, String>> list = new ArrayList<>();
		QuerySpec pro = new QuerySpec();
		pro.appendClassList(PDMLinkProduct.class, false);
		pro.appendSelect(new ClassAttribute(PDMLinkProduct.class, "containerInfo.name"), false);
		//pro.appendSelect(new ClassAttribute(PDMLinkProduct.class, "thePersistInfo.theObjectIdentifier.id"), false);
		
		QuerySpec lib = new QuerySpec();
		lib.appendClassList(WTLibrary.class, false);
		lib.appendSelect(new ClassAttribute(WTLibrary.class, "containerInfo.name"), false);
		//lib.appendSelect(new ClassAttribute(WTLibrary.class, "thePersistInfo.theObjectIdentifier.id"), false);
		
		CompoundQuerySpec compound = new CompoundQuerySpec();
		compound.setSetOperator(SetOperator.UNION_ALL);
		compound.addComponent(pro);
		compound.addComponent(lib);
		
		QueryResult qr = PersistenceHelper.manager.find(compound);
		while(qr.hasMoreElements()) {
			Map<String, String> map = new HashMap<>();
			Object[] obj = (Object[]) qr.nextElement();
			map.put("name", obj[0].toString());
			//map.put("oid", obj[1].toString());
			list.add(map);
		}
		return list;
	}
	public static Persistable getPersistable(String oid) {
		if (oid != null) {
			oid = oid.trim();
			if (oid.trim().length() > 0) {
				try {
					WTReference ref = (new ReferenceFactory()).getReference(oid);
					if (ref != null) {
						return ref.getObject();
					}
				} catch (Exception e) {
					LOGGER.error(e.toString());
				}
			}
		}
		return null;
	}
	public static void main(String[] args) {
		
		try{
			
			WTLibrary library = getLibrary("LIBRARY");
			
			LOGGER.info("library =" + library.getName());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static String getOid(Persistable persistable) {
		return getOid(persistable, false);
	}

	/**
	 * 객체식별자를 얻는다
	 * @param persistable Windchill 저장객체
	 * @param ignoreBranch VR형태의 OID를 무시하려면 true
	 * @return 문자열 객체 식별자
	 * @throws WTException
	 */
	public static String getOid(Persistable persistable, boolean ignoreBranch) {
		if (persistable != null) {
			try {
				ObjectIdentifier objectIdentifier = PersistenceHelper.getObjectIdentifier(persistable);
				if (ignoreBranch) {
					if (objectIdentifier != null) {
						return objectIdentifier.getStringValue();
					}
				} else {
					if (objectIdentifier != null) {
						return (new ReferenceFactory()).getReferenceString(persistable);
					}
				}
			} catch (Exception e) {
				return persistable.toString();
			}
		}
		return null;
	}
	public static Persistable getObject(String oid) throws WTRuntimeException, WTException {
		Persistable result = null;
		if (oid != null && !"null".equals(oid)) {
			if (rf == null) {
				rf = new ReferenceFactory();
			}
			result = rf.getReference(oid).getObject();
		}
		return result;
	}
	
	public static String getDownloadURL(String choid, ApplicationData appData) throws WTException {
		return "/Windchill/worldex/portal/downloadContent"
				+ "?holderOid=" + choid
				+ "&appOid=" + WCUtil.getOid(appData);
	}
	
	public static long getLongOid(Persistable persistable) {
		if (persistable != null) {
			ObjectIdentifier objectIdentifier = PersistenceHelper.getObjectIdentifier(persistable);
			if (objectIdentifier != null) {
				return objectIdentifier.getId();
			}
		}
		return 0L;
	}
	
	/**
	 * 
	  * @desc : 제품 및 Library
	  * @author : tsuam
	  * @date : 2022. 10. 25.
	  * @method : getWTContainer
	  * @param container
	  * @return
	  * @throws Exception WTContainer
	 */
	public static WTContainer getWTContainer(String container) throws Exception{
		
		
		WTContainer wtcontainer = WCUtil.getPDMLinkProduct(container);
		if(wtcontainer == null) {
			wtcontainer = WCUtil.getLibrary(container);
		}
		
		return wtcontainer;
	}
	
	/**
	 * 
	  * @desc : 정책 도메인 가져오기
	  * @author : shjeong
	  * @date : 2023. 09. 19.
	  * @method : getAdministrativeDomain
	  * @param String path
	  * @return
	  * @throws Exception WTContainer
	 */
	public static AdministrativeDomain getAdministrativeDomain(String path) throws Exception{
		
		String orgName = ConfigImpl.getInstance().getString("org.context.name");
		String productName = ConfigImpl.getInstance().getString("product.context.name");
		WTContainerRef container_ref = WTContainerHelper.service.getByPath("/wt.inf.container.OrgContainer=" + orgName + "/wt.pdmlink.PDMLinkProduct=" + productName);
		AdministrativeDomain domain = AdministrativeDomainHelper.manager.getDomain(path, container_ref);
		
		return domain;
	}
}
