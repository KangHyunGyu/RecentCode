package com.e3ps.change.beans;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import wt.method.MethodContext;
import wt.method.RemoteMethodServer;
import wt.pom.DBProperties;
import wt.pom.WTConnection;
import wt.util.WTException;
import wt.util.WTProperties;

public class ChangeDao implements wt.method.RemoteAccess, java.io.Serializable {

	static final boolean SERVER = wt.method.RemoteMethodServer.ServerFlag;
	public static final ChangeDao manager = new ChangeDao();

	static String dataStore = "Oracle"; //SQLServer ....
	static {
		try{
			dataStore = WTProperties.getLocalProperties().getProperty("wt.db.dataStore");
		}catch(Exception ex){
			dataStore = "Oracle";
		}
	}

	
	public ArrayList getEcrMonthData(String start, String end, long product) throws Exception{


		if (!SERVER) {

			try {
				Class argTypes[] = new Class[]{String.class,String.class,long.class};
				Object args[] = new Object[]{start,end,product};
				return (ArrayList)RemoteMethodServer.getDefault().invoke("getEcrMonthData", null, this, argTypes, args);
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

			/**
				
SELECT TO_CHAR(CREATESTAMPA2,'YYYY-MM'),COUNT(*) FROM ECHANGEREQUEST2 WHERE TO_CHAR(CREATESTAMPA2,'YYYY-MM') >= '2009-13'  AND TO_CHAR(CREATESTAMPA2,'YYYY-MM') <= '2010-02' 	
AND IDA3A4 IN (
SELECT PRODUCT.IDA2A2 FROM (
SELECT IDA2A2,IDA3PARENTREFERENCE FROM (
SELECT IDA2A2,IDA3PARENTREFERENCE FROM LCProductstructure
union all
SELECT IDA2A2,IDA3PARENTREFERENCE FROM LCProduct
) str 
START WITH IDA3PARENTREFERENCE = 0 
CONNECT BY PRIOR IDA2A2=IDA3PARENTREFERENCE
) PRODUCT) 
GROUP BY TO_CHAR(CREATESTAMPA2,'YYYY-MM')
/

			**/
			
			StringBuffer sb = new StringBuffer()
			.append("SELECT TO_CHAR(CREATESTAMPA2,'YYYY-MM'),COUNT(*) ")
			.append("FROM ECHANGEREQUEST2 ")
			.append("WHERE TO_CHAR(CREATESTAMPA2,'YYYY-MM') >= ?  ")
			.append("AND TO_CHAR(CREATESTAMPA2,'YYYY-MM') <= ? 	")
			.append("AND IDA3A4 IN (")
			.append("SELECT PRODUCT.IDA2A2 FROM ( ")
			.append("SELECT IDA2A2,IDA3PARENTREFERENCE FROM ( ")
			.append("SELECT IDA2A2,IDA3PARENTREFERENCE FROM LCProductstructure ")
			.append("UNION ALL ")
			.append("SELECT IDA2A2,IDA3PARENTREFERENCE FROM LCProduct ")
			.append(") STR ")
			.append("START WITH IDA3PARENTREFERENCE = ? ")
			.append("CONNECT BY PRIOR IDA2A2=IDA3PARENTREFERENCE ")
			.append(") PRODUCT) ")
			.append("GROUP BY TO_CHAR(CREATESTAMPA2,'YYYY-MM')");


			st = con.prepareStatement(sb.toString());
			st.setString(1, start);
			st.setString(2, end);
			st.setLong(3, product);

			rs = st.executeQuery();

			ArrayList list = new ArrayList();

			while (rs.next()) {
				String date = rs.getString(1);
				String value = rs.getString(2);

				list.add(new String[]{date,value});
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
	
	

	public ArrayList getEcoMonthData(String start, String end,long product) throws Exception{


		if (!SERVER) {

			try {
				Class argTypes[] = new Class[]{String.class,String.class,long.class};
				Object args[] = new Object[]{start,end,product};
				return (ArrayList)RemoteMethodServer.getDefault().invoke("getEcoMonthData", null, this, argTypes, args);
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

			/**
				
				SELECT TO_CHAR(CREATESTAMPA2,'YYYY-MM'),COUNT(*) FROM ECHANGEORDER2 WHERE TO_CHAR(CREATESTAMPA2,'YYYY-MM') >= '2009-13'  AND TO_CHAR(CREATESTAMPA2,'YYYY-MM') <= '2010-02' 	GROUP BY TO_CHAR(CREATESTAMPA2,'YYYY-MM')

			**/
			
			StringBuffer sb = new StringBuffer()
			.append("SELECT TO_CHAR(CREATESTAMPA2,'YYYY-MM'),COUNT(*) ")
			.append("FROM ECHANGEORDER2 ")
			.append("WHERE TO_CHAR(CREATESTAMPA2,'YYYY-MM') >= ?  ")
			.append("AND TO_CHAR(CREATESTAMPA2,'YYYY-MM') <= ? 	")
			.append("AND IDA3A4 IN (")
			.append("SELECT PRODUCT.IDA2A2 FROM (")
			.append("SELECT IDA2A2,IDA3PARENTREFERENCE FROM ( ")
			.append("SELECT IDA2A2,IDA3PARENTREFERENCE FROM LCProductstructure ")
			.append("UNION ALL ")
			.append("SELECT IDA2A2,IDA3PARENTREFERENCE FROM LCProduct ")
			.append(") str ")
			.append("START WITH IDA3PARENTREFERENCE = ? ")
			.append("CONNECT BY PRIOR IDA2A2=IDA3PARENTREFERENCE ")
			.append(") PRODUCT) ")
			.append("GROUP BY TO_CHAR(CREATESTAMPA2,'YYYY-MM')");


			st = con.prepareStatement(sb.toString());
			st.setString(1, start);
			st.setString(2, end);
			st.setLong(3, product);
			rs = st.executeQuery();

			ArrayList list = new ArrayList();

			while (rs.next()) {
				String date = rs.getString(1);
				String value = rs.getString(2);

				list.add(new String[]{date,value});
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
	
	
	public ArrayList getEcoTypeData(String start,String end, long product) throws Exception{


		if (!SERVER) {

			try {
				Class argTypes[] = new Class[]{String.class,String.class,long.class};
				Object args[] = new Object[]{start,end,product};
				return (ArrayList)RemoteMethodServer.getDefault().invoke("getEcoTypeData", null, this, argTypes, args);
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

			/**
				SELECT TO_CHAR(CREATESTAMPA2,'YYYY-MM'),ECOTYPE,COUNT(*) 
				FROM ECHANGEORDER2 
				WHERE 
				TO_CHAR(CREATESTAMPA2,'YYYY-MM') >= '2009-01'
				AND
				TO_CHAR(CREATESTAMPA2,'YYYY-MM') <= '2010-01'  
				GROUP BY 
				TO_CHAR(CREATESTAMPA2,'YYYY-MM'),ECOTYPE

			**/
			
			StringBuffer sb = new StringBuffer()
			.append("SELECT TO_CHAR(CREATESTAMPA2,'YYYY-MM'),ECOTYPE,COUNT(*)  ")
			.append("FROM ECHANGEORDER2 ")
			.append("WHERE TO_CHAR(CREATESTAMPA2,'YYYY-MM') >=  ?  ")
			.append("AND ")
			.append("TO_CHAR(CREATESTAMPA2,'YYYY-MM') <=  ?  ")
			.append("AND IDA3A4 IN (")
			.append("SELECT PRODUCT.IDA2A2 FROM (")
			.append("SELECT IDA2A2,IDA3PARENTREFERENCE FROM ( ")
			.append("SELECT IDA2A2,IDA3PARENTREFERENCE FROM LCProductstructure ")
			.append("UNION ALL ")
			.append("SELECT IDA2A2,IDA3PARENTREFERENCE FROM LCProduct ")
			.append(") str ")
			.append("START WITH IDA3PARENTREFERENCE = ? ")
			.append("CONNECT BY PRIOR IDA2A2=IDA3PARENTREFERENCE ")
			.append(") PRODUCT) ")
			.append("GROUP BY TO_CHAR(CREATESTAMPA2,'YYYY-MM'),ECOTYPE");


			st = con.prepareStatement(sb.toString());
			st.setString(1, start);
			st.setString(2, end);
			st.setLong(3, product);
			rs = st.executeQuery();

			ArrayList list = new ArrayList();

			while (rs.next()) {
				String yyyy = rs.getString(1);
				String ecotype = rs.getString(2);
				String value = rs.getString(3);

				list.add(new String[]{yyyy,ecotype,value});
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


}