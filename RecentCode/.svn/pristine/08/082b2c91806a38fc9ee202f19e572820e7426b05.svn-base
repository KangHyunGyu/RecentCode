package com.e3ps.project.beans;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import wt.method.MethodContext;
import wt.method.RemoteMethodServer;
import wt.pds.DatabaseInfoUtilities;
import wt.pom.DBProperties;
import wt.pom.WTConnection;
import wt.util.WTException;
import wt.util.WTProperties;

import com.e3ps.project.ETaskNode;

public class ProjectDao implements wt.method.RemoteAccess, java.io.Serializable {

	static final boolean SERVER = wt.method.RemoteMethodServer.ServerFlag;
	public static final ProjectDao manager = new ProjectDao();

	static String dataStore = "Oracle"; //SQLServer ....
	static {
		try{
			dataStore = WTProperties.getLocalProperties().getProperty("wt.db.dataStore");
		}catch(Exception ex){
			dataStore = "Oracle";
		}
	}

	protected ProjectDao() {
	}
	
	
	public ArrayList getStructure(long oid) throws Exception{
		return getStructure(oid,0);
	}
	
	public ArrayList getStructure(long oid,int depth) throws Exception{
		return getStructure(oid,depth,null);
	}
	
	public ArrayList getStructure(long oid,int depth,String[] fields) throws Exception{


		if (!SERVER) {

			try {
				Class argTypes[] = new Class[]{long.class,int.class,String[].class};
				Object args[] = new Object[]{new Long(oid),new Integer(depth),fields};
				return (ArrayList)RemoteMethodServer.getDefault().invoke("getStructure", null, this, argTypes, args);
			} catch (RemoteException e) {
				e.printStackTrace();
				throw new WTException(e);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new WTException(e);
			}
		}

		MethodContext methodcontext = null;
		WTConnection wtconnection = null;

        PreparedStatement st = null;
        ResultSet rs = null;
        
		try {
			methodcontext = MethodContext.getContext();
			wtconnection = (WTConnection) methodcontext.getConnection();
			Connection con = wtconnection.getConnection();
			
			wt.introspection.ClassInfo classinfo = wt.introspection.WTIntrospector.getClassInfo(ETaskNode.class);
			wt.introspection.ClassInfo[] arry = classinfo.getDescendentInfos();
			
			String[] tableName = new String[arry.length];
			for(int i=0; i< arry.length; i++){			
				if (DatabaseInfoUtilities.isAutoNavigate(arry[i])) {
					tableName[i] = DatabaseInfoUtilities.getBaseTableName(arry[i]);
				}else{
					tableName[i] = DatabaseInfoUtilities.getValidTableName(arry[i]);
				}
			}
			
			StringBuffer sb = new StringBuffer();
			
			
		if("Oracle".equals(dataStore)){

			/**

SELECT LEVEL,OID,IDA3PARENTREFERENCE  FROM (
	SELECT CLASSNAMEA2A2||':'||IDA2A2 OID ,SORT,IDA2A2,IDA3PARENTREFERENCE FROM ETASK
) STR  
START WITH IDA3PARENTREFERENCE=0
CONNECT BY PRIOR IDA2A2=IDA3PARENTREFERENCE 
ORDER SIBLINGS BY SEQ

			**/
			
			
			sb.append(" SELECT LEVEL,NAME,OID,IDA3PARENTREFERENCE,DESCRIPTION ");
			
			for(int j=0; fields!=null && j< fields.length; j++){
				sb.append(",");
				sb.append(fields[j]);
			}
			sb.append(" FROM (");
			
			for(int i=0; i< tableName.length; i++){
			
				sb.append(" SELECT NAME,CLASSNAMEA2A2||':'||IDA2A2 OID,DESCRIPTION,SORT,IDA2A2,IDA3PARENTREFERENCE ");
				for(int j=0; fields!=null && j< fields.length; j++){
					sb.append(",");
					sb.append(fields[j]);
				}
				sb.append(" from ")
				.append(tableName[i]);
				
				if(tableName.length > i+1){
					sb.append(" UNION ALL ");
				}
			}
			
			sb.append(") STR ")
			.append(" START WITH IDA3PARENTREFERENCE=? ")
			.append(" CONNECT BY PRIOR IDA2A2=IDA3PARENTREFERENCE ");
			
			if(depth>0){
				sb.append(" AND LEVEL <= "+depth);
			}
			sb.append(" ORDER SIBLINGS BY SORT,IDA2A2");
			
			
		}else{
			/*SELECT LEVEL,NAME,OID,IDA3PARENTREFERENCE,DESCRIPTION
			 * 
			with cte 
			(classnameA2A2, idA2A2, classnamekeyparentReference, idA3parentReference, sort, level ,name, description) 
			as( select classnameA2A2, idA2A2, classnamekeyparentReference, idA3parentReference, sort, 1 as level ,name, description
			from ETask 
			where idA3parentReference = ? 
			union all 
			select a.classnameA2A2, a.idA2A2, a.classnamekeyparentReference, a.idA3parentReference, a.sort, level+1, a.name, a.description
			from ETask a, cte b 
			where a.idA3parentReference = b.idA2A2)
			select level,name,classnameA2A2+':'+convert(nvarchar, idA2A2), convert(nvarchar, idA3parentReference),description
			from cte
			 */
			
			sb.append(" with cte ");
			sb.append(" (classnameA2A2, idA2A2, classnamekeyparentReference, idA3parentReference, sort, level ,name, description) ");
			for(int j=0; fields!=null && j< fields.length; j++){
				sb.append(",");
				sb.append(fields[j]);
			}
			sb.append(" as( select classnameA2A2, idA2A2, classnamekeyparentReference, idA3parentReference, sort, 1 as level ,name, description ");
			for(int j=0; fields!=null && j< fields.length; j++){
				sb.append(",");
				sb.append(fields[j]);
			}
			sb.append(" from ETask ");
			sb.append(" where idA3parentReference = ? ");
			sb.append(" union all");
			sb.append(" select a.classnameA2A2, a.idA2A2, a.classnamekeyparentReference, a.idA3parentReference, a.sort, level+1, a.name, a.description ");
			for(int j=0; fields!=null && j< fields.length; j++){
				sb.append(",a.");
				sb.append(fields[j]);
			}
			sb.append(" from ETask a, cte b ");
			sb.append(" where a.idA3parentReference = b.idA2A2) ");
			sb.append(" select level,name,classnameA2A2+':'+convert(nvarchar, idA2A2), convert(nvarchar, idA3parentReference),description ");
			for(int j=0; fields!=null && j< fields.length; j++){
				sb.append(",");
				sb.append(fields[j]);
			}
			sb.append(" from cte ");
			if(depth>0){
				sb.append(" where level <= "+depth);
			}
			sb.append(" order by sort ");
			
		}
		
			st = con.prepareStatement(sb.toString());
			st.setLong(1, oid);

			rs = st.executeQuery();

			ArrayList list = new ArrayList();
			
			int resultSize = 5;
			if(fields!=null){
				resultSize += fields.length;
			}
			
			if("Oracle".equals(dataStore)){
			int a= 1;
				while (rs.next()) {
				
					String[] rr = new String[resultSize];
					for(int i=0; i< rr.length; i++){
						rr[i] = rs.getString(i+1);
					}
					list.add(rr);
				}
			}else{
				
				HashMap temp = new HashMap();
				
				ArrayList root = new ArrayList();
				ArrayList idList = new ArrayList();
				
				while (rs.next()) {
					String[] rr = new String[resultSize];
					for(int i=0; i< rr.length; i++){
						rr[i] = rs.getString(i+1);
					}
					
					SQLTreeData sd = new SQLTreeData(rr);
					
					temp.put(sd.id, sd);
					idList.add(sd.id);
				}
				
				for(int i=0; i< idList.size(); i++){
					SQLTreeData sd = (SQLTreeData)temp.get(idList.get(i));
					SQLTreeData pp = (SQLTreeData)temp.get(sd.parent);
					if(pp!=null){
						pp.children.add(sd);
					}else{
						root.add(sd);
					}
				}

				for(int i=0; i< root.size(); i++){
					SQLTreeData rr = (SQLTreeData)root.get(i);
					rr.getList(list);
				}
			}
		
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
            if ( rs != null ) {
                rs.close();
            }
            if ( st != null ) {
                st.close();
            }
			if (DBProperties.FREE_CONNECTION_IMMEDIATE
					&& !wtconnection.isTransactionActive()) {
				MethodContext.getContext().freeConnection();
			}
		}
	}
	
	class SQLTreeData{
		public String[] rr;
		public String id;
		public String parent;
		public ArrayList children = new ArrayList();
		public SQLTreeData(String[] rr){
			this.rr = rr;
			parent = rr[3];
			id = rr[2].substring(rr[2].lastIndexOf(":")+1);
		}
		
		public void getList(ArrayList list){
			list.add(rr);
			for(int i=0; i< children.size(); i++){
				SQLTreeData cd = (SQLTreeData)children.get(i);
				cd.getList(list);
			}
		}
	}
	
	public ArrayList getParents(long oid) throws Exception{
		return getStructure(oid,0);
	}
	
	public ArrayList getParents(long oid,int depth) throws Exception{


		if (!SERVER) {

			try {
				Class argTypes[] = new Class[]{long.class,int.class};
				Object args[] = new Object[]{new Long(oid),new Integer(depth)};
				return (ArrayList)RemoteMethodServer.getDefault().invoke("getParents", null, this, argTypes, args);
			} catch (RemoteException e) {
				e.printStackTrace();
				throw new WTException(e);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new WTException(e);
			}
		}


		MethodContext methodcontext = null;
		WTConnection wtconnection = null;

        PreparedStatement st = null;
        ResultSet rs = null;
        
		try {
			methodcontext = MethodContext.getContext();
			wtconnection = (WTConnection) methodcontext.getConnection();
			Connection con = wtconnection.getConnection();
			
			
			wt.introspection.ClassInfo classinfo = wt.introspection.WTIntrospector.getClassInfo(ETaskNode.class);
			wt.introspection.ClassInfo[] arry = classinfo.getDescendentInfos();
			
			String[] tableName = new String[arry.length];
			for(int i=0; i< arry.length; i++){			
				if (DatabaseInfoUtilities.isAutoNavigate(arry[i])) {
					tableName[i] = DatabaseInfoUtilities.getBaseTableName(arry[i]);
				}else{
					tableName[i] = DatabaseInfoUtilities.getValidTableName(arry[i]);
				}
			}

			StringBuffer sb = new StringBuffer();
			
		
		if("Oracle".equals(dataStore)){
			
			sb.append("SELECT LEVEL,NAME,OID,IDA3PARENTREFERENCE,DESCRIPTION  FROM (");
			
			for(int i=0; i< tableName.length; i++){
			
				sb.append(" SELECT NAME,CLASSNAMEA2A2||':'||IDA2A2 OID,DESCRIPTION,SORT,IDA2A2,IDA3PARENTREFERENCE from ")
				.append(tableName[i]);
				
				if(tableName.length > i+1){
					sb.append(" UNION ALL");
				}
			}
			
			sb.append(") STR ")
			.append(" START WITH IDA2A2=? ")
			.append(" CONNECT BY PRIOR IDA3PARENTREFERENCE=IDA2A2 ");
			
			if(depth>0){
				sb.append(" AND LEVEL <= "+depth);
			}
			sb.append(" ORDER SIBLINGS BY SORT");
		}else{
			
			sb.append(" with cte");
			sb.append(" (classnameA2A2, idA2A2, classnamekeyparentReference, idA3parentReference, sort, level ,name, description)");
			sb.append(" as( select classnameA2A2, idA2A2, classnamekeyparentReference, idA3parentReference, sort, 1 as level ,name, description");
			sb.append(" from ETask ");
			sb.append(" where idA2A2 = ?");
			sb.append(" union all");
			sb.append(" select a.classnameA2A2, a.idA2A2, a.classnamekeyparentReference, a.idA3parentReference, a.sort, level+1, a.name, a.description");
			sb.append(" from ETask a, cte b ");
			sb.append(" where b.idA2A2=a.idA3parentReference)");
			sb.append(" select level,name,classnameA2A2+':'+convert(nvarchar, idA2A2), convert(nvarchar, idA3parentReference),description");
			sb.append(" from cte");
			if(depth>0){
				sb.append(" where level <= "+depth);
			}
			sb.append("order by sort");
		}

			st = con.prepareStatement(sb.toString());
			st.setLong(1, oid);

			rs = st.executeQuery();

			ArrayList list = new ArrayList();
			int resultSize = 5;
			
			
			if("Oracle".equals(dataStore)){
				
				while (rs.next()) {
				
					String[] rr = new String[resultSize];
					for(int i=0; i< rr.length; i++){
						rr[i] = rs.getString(i+1);
					}
					list.add(rr);
				}
			}else{
				
				HashMap temp = new HashMap();
				
				ArrayList root = new ArrayList();
				ArrayList idList = new ArrayList();
				
				while (rs.next()) {
					String[] rr = new String[resultSize];
					for(int i=0; i< rr.length; i++){
						rr[i] = rs.getString(i+1);
					}
					
					SQLTreeData sd = new SQLTreeData(rr);
					
					temp.put(sd.id, sd);
					idList.add(sd.id);
				}
				
				for(int i=0; i< idList.size(); i++){
					SQLTreeData sd = (SQLTreeData)temp.get(idList.get(i));
					SQLTreeData pp = (SQLTreeData)temp.get(sd.parent);
					if(pp!=null){
						pp.children.add(sd);
					}else{
						root.add(sd);
					}
				}

				for(int i=0; i< root.size(); i++){
					SQLTreeData rr = (SQLTreeData)root.get(i);
					rr.getList(list);
				}
			}

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
            if ( rs != null ) {
                rs.close();
            }
            if ( st != null ) {
                st.close();
            }
			if (DBProperties.FREE_CONNECTION_IMMEDIATE
					&& !wtconnection.isTransactionActive()) {
				MethodContext.getContext().freeConnection();
			}
		}
	}
	
	
	public ArrayList getStepTree(long oid,String typeId) throws Exception{

		if (!SERVER) {

			try {
				Class argTypes[] = new Class[]{long.class,long.class};
				Object args[] = new Object[]{oid,typeId};
				return (ArrayList)RemoteMethodServer.getDefault().invoke("getStepTree", null, this, argTypes, args);
			} catch (RemoteException e) {
				e.printStackTrace();
				throw new WTException(e);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new WTException(e);
			}
		}

		MethodContext methodcontext = null;
		WTConnection wtconnection = null;

        PreparedStatement st = null;
        ResultSet rs = null;

		try {
			methodcontext = MethodContext.getContext();
			wtconnection = (WTConnection) methodcontext.getConnection();
			Connection con = wtconnection.getConnection();

			wt.introspection.ClassInfo classinfo = wt.introspection.WTIntrospector.getClassInfo(ETaskNode.class);
			wt.introspection.ClassInfo[] arry = classinfo.getDescendentInfos();

			String[] tableName = new String[arry.length];
			for(int i=0; i< arry.length; i++){			
				if (DatabaseInfoUtilities.isAutoNavigate(arry[i])) {
					tableName[i] = DatabaseInfoUtilities.getBaseTableName(arry[i]);
				}else{
					tableName[i] = DatabaseInfoUtilities.getValidTableName(arry[i]);
				}
			}
			
			/*
			 *
				SELECT S.NAME,S.IDA2A2,T.NAME,T.OID
				FROM OUTPUTTYPESTEP S,
				(
				SELECT B.NAME NAME,B.IDA3C3 RID,A.CLASSNAMEA2A2||':'||A.IDA2A2 OID 
				FROM ETASK A, EOUTPUT B
				WHERE A.IDA2A2=B.IDA3A3 AND B.IDA3C3>0 AND A.IDA3PROJECTREFERENCE=30513
				) T 
				WHERE S.IDA3A3=30034 AND S.IDA2A2  = T.RID(+) ORDER BY S.SORT
				
				MODIFY =>> wslee 
				
				SELECT LEVEL, S.NAME,S.IDA2A2,T.BID,T.NAME,T.OID 
				FROM OUTPUTTYPESTEP S, 
				  ( SELECT B.NAME NAME,B.IDA2A2 BID,B.IDA3C3 RID,A.CLASSNAMEA2A2||':'||A.IDA2A2 OID 
				  FROM ETask A, EOUTPUT B  
				  WHERE A.IDA2A2=B.IDA3A3 
				  AND B.IDA3C3>0 
				  AND A.IDA3PROJECTREFERENCE=67005
				  ) T  
				WHERE S.OUTPUTTYPE='GATE' 
				AND S.IDA2A2  = T.RID(+) 
				START WITH IDA3A3 is null
				CONNECT BY PRIOR IDA2A2 = IDA3A3;
			 */

			
			StringBuffer sb = new StringBuffer()
			.append("SELECT LEVEL,S.NAME,S.IDA2A2,T.BID,T.NAME,T.OID,S.IDA3A3 ")
			.append("FROM OUTPUTTYPESTEP S, (");
			
			for(int i=0; i< tableName.length; i++){
			
				sb.append(" SELECT B.NAME NAME,B.IDA2A2 BID,B.IDA3C3 RID,A.CLASSNAMEA2A2||':'||A.IDA2A2 OID  FROM ")
				.append(tableName[i])
				.append(" A, EOUTPUT B ")
				.append(" WHERE A.IDA2A2=B.IDA3A3 AND B.IDA3C3>0 AND A.IDA3PROJECTREFERENCE=")
				.append(oid);
				
				if(tableName.length > i+1){
					sb.append(" UNION ALL");
				}
			}
			
			sb.append(") T ");

			sb.append(" WHERE S.OUTPUTTYPE=");
			sb.append("'"+typeId+"'");
			sb.append(" AND S.IDA2A2  = T.RID(+)"); // ORDER BY S.SORT");
			
			sb.append(" START WITH IDA3A3 is not null");//sb.append(" START WITH IDA3A3 <> 0"); //sb.append(" START WITH IDA3A3 is null");
			sb.append(" CONNECT BY PRIOR IDA2A2 = IDA3A3");
			
			st = con.prepareStatement(sb.toString());
			
			rs = st.executeQuery();

			ArrayList list = new ArrayList();
			int resultSize = 7;
			while (rs.next()) {
				String[] rr = new String[resultSize];
				for(int i=0; i< rr.length; i++){
					rr[i] = rs.getString(i+1);
				}
				list.add(rr);
			}

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
            if ( rs != null ) {
                rs.close();
            }
            if ( st != null ) {
                st.close();
            }
			if (DBProperties.FREE_CONNECTION_IMMEDIATE
					&& !wtconnection.isTransactionActive()) {
				MethodContext.getContext().freeConnection();
			}
		}
	}



	public static void main(String[] args)throws Exception{
		
	  	ArrayList list = ProjectDao.manager.getParents(330012L);
		for(int i=0; i< list.size(); i++){
			String[] s = (String[])list.get(i);
		}

	}
}