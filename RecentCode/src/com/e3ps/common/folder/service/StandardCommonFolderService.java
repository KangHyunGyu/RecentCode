package com.e3ps.common.folder.service;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.e3ps.admin.AuthorityGroupType;
import com.e3ps.admin.FolderAuthGroup;
import com.e3ps.admin.service.AdminHelper;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;

import wt.access.AccessControlHelper;
import wt.admin.AdminDomainRef;
import wt.admin.AdministrativeDomain;
import wt.admin.AdministrativeDomainHelper;
import wt.admin.AdministrativeDomainServerHelper;
import wt.admin.PolicyRuleAlreadyExistsException;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceManagerEvent;
import wt.fc.QueryResult;
import wt.fc.collections.WTArrayList;
import wt.fc.collections.WTCollection;
import wt.fc.collections.WTHashSet;
import wt.fc.collections.WTSet;
import wt.folder.Folder;
import wt.folder.FolderHelper;
import wt.folder.SubFolder;
import wt.inf.container.WTContainerHelper;
import wt.inf.container.WTContainerRef;
import wt.method.MethodContext;
import wt.method.RemoteMethodServer;
import wt.org.OrganizationServicesHelper;
import wt.org.WTGroup;
import wt.org.WTPrincipal;
import wt.org.WTPrincipalReference;
import wt.pom.DBProperties;
import wt.pom.Transaction;
import wt.pom.WTConnection;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.StandardManager;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.util.WTProperties;

public class StandardCommonFolderService extends StandardManager implements CommonFolderService {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	private static final long serialVersionUID = 8710369108840395774L;
	
	static String dataStore = "Oracle"; //SQLServer ....
    static {
        try {
            dataStore = WTProperties.getLocalProperties().getProperty("wt.db.dataStore");
        }
        catch ( Exception ex ) {
            dataStore = "Oracle";
        }
    }

    /**
	 * Default factory for the class.
	 * @return
	 * @throws WTException
	 */
	public static StandardCommonFolderService newStandardCommonFolderService() throws WTException {
		StandardCommonFolderService instance = new StandardCommonFolderService();
		instance.initialize();
		return instance;
	}
    
	@Override
	public ArrayList getFolderSortTree(final Folder obj) throws Exception
    {

        ArrayList list = CommonFolderHelper.service.getFolderTree(obj);
        
        HashMap temp = new HashMap();

        TempNode root = new TempNode(null, null);
        temp.put("0", root);
        LOGGER.info("getFolder size = " + list.size());
        for ( int i = 0; i < list.size(); i++ ) {
            String[] s = (String[]) list.get(i);
            String level = s[0];
            int depth = Integer.parseInt(level);
            TempNode parent = (TempNode) temp.get(Integer.toString(depth - 1));
            TempNode newNode = new TempNode(s, parent);
            temp.put(level, newNode);
        }

        ArrayList result = new ArrayList();
        root.getList(result);
        return result;
    }

    class TempNode
    {
        public String[] obj;
        public TempNode parent;
        public ArrayList children = new ArrayList();

        public TempNode(final String[] obj, final TempNode parent) throws Exception {
            this.obj = obj;
            this.parent = parent;
            if ( parent != null ) {
                boolean flag = true;
                for ( int i = 0; i < parent.children.size(); i++ ) {
                    TempNode node = (TempNode) parent.children.get(i);
                    String name = node.obj[1];
                    if ( name.compareTo(obj[1]) > 0 ) {
                        flag = false;
                        parent.children.add(i, this);
                        break;
                    }
                }
                if ( flag ) {
                    parent.children.add(this);
                }
            }
        }

        public void getList(final ArrayList result) throws Exception
        {
            for ( int i = 0; i < children.size(); i++ ) {
                TempNode node = (TempNode) children.get(i);
                result.add(node.obj);
                node.getList(result);
            }
        }
    };

    @Override
    public ArrayList getFolderTree(final Folder obj) throws WTException
    {

        MethodContext methodcontext = null;
        WTConnection wtconnection = null;

        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {

            ArrayList list = new ArrayList();

            methodcontext = MethodContext.getContext();
            wtconnection = (WTConnection) methodcontext.getConnection();
            Connection con = wtconnection.getConnection();

            StringBuffer sql = null;

            if ( "Oracle".equals(dataStore) ) {

 

                sql = new StringBuffer().append("SELECT LEVEL,T.CNAME,T.CLASSNAME||':'||T.CID FROM ")
                		.append("(SELECT L.IDA3A5 PID,L.IDA3B5 CID,C.NAME CNAME,C.CLASSNAMEA2A2 CLASSNAME ")
                		.append("FROM SUBFOLDERLINK L, SUBFOLDER C  ")
                		.append("where C.IDA2A2=L.IDA3B5) T START WITH PID=? ")
                		.append("CONNECT BY PRIOR CID=PID ORDER SIBLINGS BY CNAME");

            } else {


                sql = new StringBuffer()
                        .append("with cte (idA3B5, level) as ( ")
                        .append("select idA3B5, 1 as level ")
                        .append("from SubFolderLink ")
                        .append("where idA3A5=? ")
                        .append("union all ")
                        .append("select a.idA3B5, level+1 ")
                        .append("from SubFolderLink a, cte b ")
                        .append("where a.idA3A5=b.idA3B5 ) ")
                        .append("select c.level, s.name, s.classnameA2A2+':'+convert(varchar, s.idA2A2) from SubFolder s, cte c ")
                        .append("where c.idA3B5=s.idA2A2");

            }
            LOGGER.info("obj.getPersistInfo().getObjectIdentifier().getId() = " + obj.getPersistInfo().getObjectIdentifier().getId());
            LOGGER.info(sql.toString());
            st = con.prepareStatement(sql.toString());
            st.setLong(1, obj.getPersistInfo().getObjectIdentifier().getId());

            rs = st.executeQuery();

            while ( rs.next() ) {
                String level = rs.getString(1);
                String name = rs.getString(2);
                String oid = rs.getString(3);
                oid = oid.trim();
                
                list.add(new String[] { level, name, oid });

            }
           /* 
           LOGGER.info("list.size	==	"+list.size());
           for(int i=0; i < list.size();i++){
        	   LOGGER.info("list1	="+((String[])list.get(i))[0]+"	/	"+((String[])list.get(i))[1]);
           }
           */
           
            /*
           Comparator<String[]> comparator = new Comparator<String[]>(){
                public int compare(String[] o1, String[] o2) {
                     return o2[2].compareTo(o1[2]);
                }
           };
       
           Collections.sort(list, comparator);
           */
            
           /*
           for(int i=0; i < list.size();i++){
        	   LOGGER.info("list2	="+((String[])list.get(i))[0]+"	/	"+((String[])list.get(i))[1]);
        	   
           }
           */
            return list;
        }
        catch ( Exception e ) {
            e.printStackTrace();
            throw new WTException(e);
        }
        finally {
            try {
                if ( rs != null ) {
                    rs.close();
                }
                if ( st != null ) {
                    st.close();
                }
            } catch(Exception e) {
                throw new WTException(e);
            }
            if ( DBProperties.FREE_CONNECTION_IMMEDIATE && !wtconnection.isTransactionActive() ) {
                MethodContext.getContext().freeConnection();
            }
        }

    }

    @Override
    public ArrayList getFolderDTree(final Folder obj) throws WTException
    {
        MethodContext methodcontext = null;
        WTConnection wtconnection = null;

        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {

            ArrayList list = new ArrayList();

            methodcontext = MethodContext.getContext();
            wtconnection = (WTConnection) methodcontext.getConnection();
            Connection con = wtconnection.getConnection();

            StringBuffer sql = null;

            sql = new StringBuffer();
            sql.append(" SELECT T.RN ID, NVL(S.RN, 0) PID, T.OID, T.NAME, T.L  ");
            sql.append("   FROM (                                       ");
            sql.append("          SELECT ROWNUM RN,                     ");
            sql.append("                 LEVEL L,                       ");
            sql.append("                 TB.NAME NAME,                  ");
            sql.append("                 TB.CLASSNAMEA2A2||':'||TB.IDA2A2 OID, ");
            sql.append("                 TB.IDA2A2 ID,                  ");
            sql.append("                 TA.IDA3A5 PID                  ");
            sql.append("            FROM SUBFOLDERLINK TA, SUBFOLDER TB ");
            sql.append("           WHERE TA.IDA3B5 = TB.IDA2A2          ");
            sql.append("           START WITH TA.IDA3A5 = ?             ");
            sql.append("         CONNECT BY PRIOR TA.IDA3B5 = TA.IDA3A5 ");
            sql.append("           ORDER SIBLINGS BY TB.NAME ASC        ");
            sql.append("        ) T,                                    ");
            sql.append("        (                                       ");
            sql.append("          SELECT ROWNUM RN,                     ");
            sql.append("                 SB.IDA2A2 ID                   ");
            sql.append("            FROM SUBFOLDERLINK SA, SUBFOLDER SB ");
            sql.append("           WHERE SA.IDA3B5 = SB.IDA2A2          ");
            sql.append("           START WITH SA.IDA3A5 = ?             ");
            sql.append("         CONNECT BY PRIOR SA.IDA3B5=SA.IDA3A5   ");
            sql.append("           ORDER SIBLINGS BY SB.NAME ASC        ");
            sql.append("        ) S                                     ");
            sql.append("  WHERE T.PID = S.ID(+)                         ");
            sql.append("  ORDER BY T.RN                                 ");

            st = con.prepareStatement(sql.toString());
            st.setLong(1, obj.getPersistInfo().getObjectIdentifier().getId());
            st.setLong(2, obj.getPersistInfo().getObjectIdentifier().getId());
            LOGGER.info(sql.toString());
            rs = st.executeQuery();

            while ( rs.next() ) {
                list.add(new String[] { rs.getString(1), rs.getString(2),   // DTree�� ID, DTree�� PID
                                        StringUtil.checkNull(rs.getString(3)).trim(),   //OID
                                        rs.getString(4), rs.getString(5) }); // �����, Level

            }

            return list;
        }
        catch ( Exception e ) {
            e.printStackTrace();
            throw new WTException(e);
        }
        finally {
            try {
                if ( rs != null ) {
                    rs.close();
                }
                if ( st != null ) {
                    st.close();
                }
            } catch(Exception e) {
                throw new WTException(e);
            }
            if ( DBProperties.FREE_CONNECTION_IMMEDIATE && !wtconnection.isTransactionActive() ) {
                MethodContext.getContext().freeConnection();
            }
        }

    }
    
    public int compare(String object1, String object2) {

    	return object1.compareToIgnoreCase(object2);

	}
    
    public Folder getFolder(String location, WTContainerRef wtContainerRef)throws Exception{
    	Transaction trx = new Transaction();
        Folder folder = null;
        try {
        	trx.start();
        	
        	String[] locationArray = location.split("/");
            String subLocation = "/Default";
        	for(int i=2; i < locationArray.length; i++){
        		String locationValue = locationArray[i];
        		
        		subLocation += "/" + locationValue;
        		
        		WTContainerRef[] wtContainerRefArray = {wtContainerRef};
        		
        		//QueryResult qr = FolderHelper.service.findFolderContents(subLocation, wtContainerRefArray, FolderEntry.class);
        		
        		//QueryResult qr  = FolderHelper.service.findSubFolders(subLocation, AccessPermission.ALL, wtContainerRef);
        		try{
        			folder = FolderHelper.service.getFolder(subLocation, wtContainerRef);
        		}catch(Exception e ){
        			folder = null;
        		}
        		
        		//LOGGER.info("subLocation = "+subLocation + " - ");
        		
        		if( folder == null){
        			folder = FolderHelper.service.createSubFolder(subLocation, wtContainerRef);
        		}
        		
        		/*
        		if( qr.hasMoreElements()){
        			//folder = (Folder)qr.nextElement();
        			Object[] o = (Object[])qr.nextElement();
        			LOGGER.info("### object = " + o[0].getClass() );
        			
        		}else{
        			folder = FolderHelper.service.createSubFolder(subLocation, wtContainerRef);
        		}
        		*/
        		
        		
        	}
    		
            trx.commit();
            trx = null;

       } catch(Exception e) {
           e.printStackTrace();
       } finally {
           if(trx!=null){
                trx.rollback();
           }
       }
        return folder;
    }
    
    /**
	 * 
	  * @desc : 폴더 생성 시 도메인, 그룹, 정책 설정
	  * @author : shjeong
	  * @date : 2023. 11. 14.
	  * @method : eventListener
	  * @param container
	  * @return
	  * @throws Exception WTContainer
	 */
    @Override
	public void eventListener(Object obj, String event) {
		try {
			if(obj instanceof SubFolder){
				SubFolder folder = (SubFolder)obj;
				if(folder.getFolderPath().contains("/Default/Document/")) {
					
					Map<String, Object> reqMap = new HashMap<String,Object>();
					
					String orgName = ConfigImpl.getInstance().getString("org.context.name");
					String productName = ConfigImpl.getInstance().getString("product.context.name");
					WTContainerRef container_ref = WTContainerHelper.service.getByPath("/wt.inf.container.OrgContainer="+orgName+"/wt.pdmlink.PDMLinkProduct="+productName);
					
					if (PersistenceManagerEvent.POST_STORE.equals(event)) {
						if(folder.getFolderPath().contains("/Default/Document")) {
							WTPrincipal principal = SessionHelper.manager.getPrincipal();
							SessionHelper.manager.setAdministrator();
							
							String identityName = "D"+CommonUtil.getOIDLongValue(folder);
							AdministrativeDomain existDomain = AdministrativeDomainHelper.manager.getDomain("/Default/DOCFOLDER/"+identityName, container_ref);
							AdminDomainRef domainRef = null;
							if(existDomain==null) {
								//도메인 생성
								domainRef = AdminHelper.service.createDomain(identityName, folder.getName());
							}else {
								domainRef = AdminDomainRef.newAdminDomainRef(existDomain);
							}
							
							//생성한 폴더에 생성한 도메인 적용
							AdministrativeDomainHelper.manager.changeAdministrativeDomain(folder, domainRef, false);
							
							WTGroup authParentGroup = null;
							WTGroup permissionGroup = null;
							WTGroup readGroup = null;
							
							authParentGroup = WCUtil.getGroup(identityName+"_AUTH");
							if(authParentGroup==null) {
								authParentGroup = AdminHelper.service.createWTGroup(identityName+"_AUTH", folder.getName()+" folder auth management parent group");
							}
							
							permissionGroup = WCUtil.getGroup(identityName+"_PERMISSION");
							if(permissionGroup==null) {
								permissionGroup = AdminHelper.service.createWTGroup(identityName+"_PERMISSION", folder.getName()+" folder permission");
							}
							
							readGroup = WCUtil.getGroup(identityName+"_READ");
							if(readGroup==null) {
								readGroup = AdminHelper.service.createWTGroup(identityName+"_READ", folder.getName()+" folder read and download");
							}
							
							
							List<String> addGroupList = new ArrayList<String>();
							
							//parent group 추가
							reqMap.put("oid", CommonUtil.getOIDString(authParentGroup));
							reqMap.put("edit", true);
							
							boolean existPermissionGroupParent = false;
							
							Enumeration<?> permissionGroupParents = permissionGroup.parentGroups();
							
							while(permissionGroupParents.hasMoreElements()){
								Object o = (Object)permissionGroupParents.nextElement();
								
								if(o instanceof WTPrincipalReference){
									WTPrincipalReference u = (WTPrincipalReference)o;
									WTGroup g = (WTGroup)u.getPrincipal();
									if(g.getName().equals(authParentGroup.getName())) existPermissionGroupParent = true;
								}
							}
							
							if(!existPermissionGroupParent) {
								addGroupList.add(CommonUtil.getOIDString(permissionGroup));
							}
							
							
							boolean existReadGroupParent = false;
							
							Enumeration<?> readGroupParents = readGroup.parentGroups();
							
							while(readGroupParents.hasMoreElements()){
								Object o = (Object)readGroupParents.nextElement();
								
								if(o instanceof WTPrincipalReference){
									WTPrincipalReference u = (WTPrincipalReference)o;
									WTGroup g = (WTGroup)u.getPrincipal();
									if(g.getName().equals(authParentGroup.getName())) existReadGroupParent = true;
								}
							}
							
							if(!existReadGroupParent) {
								addGroupList.add(CommonUtil.getOIDString(readGroup));
							}
							
							reqMap.put("addGroupList", addGroupList);
							AdminHelper.service.editGroupUser(reqMap);
							
							AdminHelper.service.editACL(domainRef, "DOC", authParentGroup, "DENY", "create", true);
							AdminHelper.service.editACL(domainRef, "DOC", permissionGroup, "PERMISSION", "create", false);
							AdminHelper.service.editACL(domainRef, "DOC", readGroup, "READ", "create", false);
							
							WTCollection faList = new WTArrayList();
							
							FolderAuthGroup link_permission = AdminHelper.manager.getFolderAuthGroupBySubFolder(folder, "PERMISSION");
							if(link_permission==null) {
								//폴더, 그룹 링크 생성
								link_permission = FolderAuthGroup.newFolderAuthGroup(folder, permissionGroup);
								link_permission.setAuthFolderGroupType(AuthorityGroupType.toAuthorityGroupType("PERMISSION"));
								faList.add(link_permission);
							}
							
							FolderAuthGroup link_read = AdminHelper.manager.getFolderAuthGroupBySubFolder(folder, "READ");
							if(link_read==null) {
								//폴더, 그룹 링크 생성
								link_read = FolderAuthGroup.newFolderAuthGroup(folder, readGroup);
								link_read.setAuthFolderGroupType(AuthorityGroupType.toAuthorityGroupType("READ"));
								faList.add(link_read);
							}
							
							if(faList.size() > 0) PersistenceHelper.manager.save(faList);
							
							SessionHelper.manager.setPrincipal(principal.getName());
						}
						
					}else if(PersistenceManagerEvent.PRE_DELETE.equals(event)) {
						//도메인 제대로 적용되어있는지 체크
						if(folder.getDomainRef().getName().equals("D"+CommonUtil.getOIDLongValue(folder))) {
							
							WTPrincipal principal = SessionHelper.manager.getPrincipal();
							SessionHelper.manager.setAdministrator();
							
							//그룹 명 변수 저장
							String groupName = folder.getDomainRef().getName();
							
							//타겟 도메인 변수 저장
							AdminDomainRef targetRef = folder.getDomainRef();
							
							//삭제 전 폴더 도메인 루트로 변경 (도메인 삭제처리를 위해)
							AdminDomainRef tempRef = AdministrativeDomainHelper.getAdminDomainRef("/Default");
							AdministrativeDomainHelper.manager.changeAdministrativeDomain(folder, tempRef, true);
							
							//도메인에 적용되어있는 정책 삭제
							WTGroup authParentGroup =  WCUtil.getGroup(groupName+"_AUTH");
							WTGroup permissionGroup = WCUtil.getGroup(groupName+"_PERMISSION");
							WTGroup readGroup = WCUtil.getGroup(groupName+"_READ");
							
							//정책 삭제
							if(AdminHelper.service.existACL(targetRef, "DOC", authParentGroup, true)) {
								AdminHelper.service.editACL(targetRef, "DOC", authParentGroup, "", "delete", true);
							}
							if(AdminHelper.service.existACL(targetRef, "DOC", permissionGroup, false)) {
								AdminHelper.service.editACL(targetRef, "DOC", permissionGroup, "", "delete", false);
							}
							if(AdminHelper.service.existACL(targetRef, "DOC", readGroup, false)) {
								AdminHelper.service.editACL(targetRef, "DOC", readGroup, "", "delete", false);
							}
							
							WTSet set = new WTHashSet();
							//FolderAuthGroup 객체 삭제 처리
							FolderAuthGroup link_permission = AdminHelper.manager.getFolderAuthGroupBySubFolder(folder, "PERMISSION");
							FolderAuthGroup link_read = AdminHelper.manager.getFolderAuthGroupBySubFolder(folder, "READ");
							if(link_permission!=null)set.add(link_permission);
							if(link_read!=null)set.add(link_read);
							if(set.size() > 0) PersistenceHelper.manager.delete(set);
							
							set = new WTHashSet();
							if(authParentGroup!=null)set.add(authParentGroup);
							if(permissionGroup!=null)set.add(permissionGroup);
							if(readGroup!=null)set.add(readGroup);
							//그룹 삭제
							OrganizationServicesHelper.manager.deleteGroups(set);
							
							//도메인 삭제
							AdminHelper.service.deleteDomain(targetRef);
							
							SessionHelper.manager.setPrincipal(principal.getName());
						}
					}else if(PersistenceManagerEvent.POST_MODIFY.equals(event)) {
						if(folder.getFolderPath().contains("/Default/Document")) {
							WTPrincipal principal = SessionHelper.manager.getPrincipal();
							SessionHelper.manager.setAdministrator();
							
							String identityName = "D"+CommonUtil.getOIDLongValue(folder);
							AdminDomainRef domainRef = null;
							AdministrativeDomain existDomain = AdministrativeDomainHelper.manager.getDomain("/Default/DOCFOLDER/"+identityName, container_ref);
							if(!folder.getDomainRef().getName().equals(identityName)) {
								
								if(existDomain==null) {
									//도메인 생성
									domainRef = AdminHelper.service.createDomain(identityName, folder.getName());
								}else {
									domainRef = AdminDomainRef.newAdminDomainRef(existDomain);
								}
								
								//생성한 폴더에 생성한 도메인 적용
								AdministrativeDomainHelper.manager.changeAdministrativeDomain(folder, domainRef, false);
								
							}else {
								domainRef = folder.getDomainRef();
							}
							
							
							WTGroup authParentGroup = null;
							WTGroup permissionGroup = null;
							WTGroup readGroup = null;
							
							authParentGroup = WCUtil.getGroup(identityName+"_AUTH");
							if(authParentGroup==null) {
								authParentGroup = AdminHelper.service.createWTGroup(identityName+"_AUTH", folder.getName()+" folder auth management parent group");
							}
							
							permissionGroup = WCUtil.getGroup(identityName+"_PERMISSION");
							if(permissionGroup==null) {
								permissionGroup = AdminHelper.service.createWTGroup(identityName+"_PERMISSION", folder.getName()+" folder permission");
							}
							
							readGroup = WCUtil.getGroup(identityName+"_READ");
							if(readGroup==null) {
								readGroup = AdminHelper.service.createWTGroup(identityName+"_READ", folder.getName()+" folder read and download");
							}
							
							
							List<String> addGroupList = new ArrayList<String>();
							
							//parent group 추가
							reqMap.put("oid", CommonUtil.getOIDString(authParentGroup));
							reqMap.put("edit", true);
							
							boolean existPermissionGroupParent = false;
							
							Enumeration<?> permissionGroupParents = permissionGroup.parentGroups();
							
							while(permissionGroupParents.hasMoreElements()){
								Object o = (Object)permissionGroupParents.nextElement();
								
								if(o instanceof WTPrincipalReference){
									WTPrincipalReference u = (WTPrincipalReference)o;
									WTGroup g = (WTGroup)u.getPrincipal();
									if(g.getName().equals(authParentGroup.getName())) existPermissionGroupParent = true;
								}
							}
							
							if(!existPermissionGroupParent) {
								addGroupList.add(CommonUtil.getOIDString(permissionGroup));
							}
							
							
							boolean existReadGroupParent = false;
							
							Enumeration<?> readGroupParents = readGroup.parentGroups();
							
							while(readGroupParents.hasMoreElements()){
								Object o = (Object)readGroupParents.nextElement();
								
								if(o instanceof WTPrincipalReference){
									WTPrincipalReference u = (WTPrincipalReference)o;
									WTGroup g = (WTGroup)u.getPrincipal();
									if(g.getName().equals(authParentGroup.getName())) existReadGroupParent = true;
								}
							}
							
							if(!existReadGroupParent) {
								addGroupList.add(CommonUtil.getOIDString(readGroup));
							}
							
							reqMap.put("addGroupList", addGroupList);
							AdminHelper.service.editGroupUser(reqMap);
							
							//그룹에 정책 적용
							if(!AdminHelper.service.existACL(domainRef, "DOC", authParentGroup, true)) {
								AdminHelper.service.editACL(domainRef, "DOC", authParentGroup, "DENY", "create", true);
							}
							if(!AdminHelper.service.existACL(domainRef, "DOC", permissionGroup, false)) {
								AdminHelper.service.editACL(domainRef, "DOC", permissionGroup, "PERMISSION", "create", false);
							}
							if(!AdminHelper.service.existACL(domainRef, "DOC", readGroup, false)) {
								AdminHelper.service.editACL(domainRef, "DOC", readGroup, "READ", "create", false);
							}
							
							WTGroup beforeGroup = WCUtil.getGroup(identityName);
							
							if(beforeGroup!= null) {
								
								QuerySpec qs = new QuerySpec();
								int idx = qs.addClassList(FolderAuthGroup.class, true);
								qs.appendWhere(new SearchCondition(FolderAuthGroup.class, FolderAuthGroup.ROLE_BOBJECT_REF+".key.id", "=", CommonUtil.getOIDLongValue(beforeGroup)), new int[] {idx});
								QueryResult qr = PersistenceHelper.manager.find(qs);
								WTSet deletSet = new WTHashSet(qr);
								PersistenceHelper.manager.delete(deletSet);
								
								if(AdminHelper.service.existACL(domainRef, "DOC", beforeGroup, false)) {
									AdminHelper.service.editACL(domainRef, "DOC", beforeGroup, "", "delete", false);
								}
								if(AdminHelper.service.existACL(domainRef, "DOC", beforeGroup, true)) {
									AdminHelper.service.editACL(domainRef, "DOC", beforeGroup, "", "delete", true);
								}
								
								OrganizationServicesHelper.manager.delete(beforeGroup);
							}
							
							
							WTCollection faList = new WTArrayList();
							
							FolderAuthGroup link_permission = AdminHelper.manager.getFolderAuthGroupBySubFolder(folder, "PERMISSION");
							if(link_permission==null) {
								//폴더, 그룹 링크 생성
								link_permission = FolderAuthGroup.newFolderAuthGroup(folder, permissionGroup);
								link_permission.setAuthFolderGroupType(AuthorityGroupType.toAuthorityGroupType("PERMISSION"));
								faList.add(link_permission);
							}
							
							FolderAuthGroup link_read = AdminHelper.manager.getFolderAuthGroupBySubFolder(folder, "READ");
							if(link_read==null) {
								//폴더, 그룹 링크 생성
								link_read = FolderAuthGroup.newFolderAuthGroup(folder, readGroup);
								link_read.setAuthFolderGroupType(AuthorityGroupType.toAuthorityGroupType("READ"));
								faList.add(link_read);
							}
							
							if(faList.size() > 0) PersistenceHelper.manager.save(faList);
							
							SessionHelper.manager.setPrincipal(principal.getName());
						}
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
    
}