/*
 * @(#) ManageSequence.java
 * Copyright (c) e3ps. All rights reserverd
 */
package com.e3ps.common.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.e3ps.common.log4j.Log4jPackages;

import wt.fc.PersistenceHelper;
import wt.method.MethodContext;
import wt.pds.oracle81.OraclePds81;
import wt.pom.DBProperties;
import wt.pom.WTConnection;

/**
 * 데이터베이스의 시퀀스를 가져온다.
 * 
 * @version 1.00
 * @since jdk 1.4.2
 * @author Seung-hwan Choi, skyprda@e3ps.com
 */

public class ManageSequence
{
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
    /**
     * Sequence 를 가져온다
     * 
     * @param seqName
     *            가져올 시퀀스 이름
     * @param format
     *            필요한 자릿수 ex) "000"
     */

    public static String getSeqNo(String seqName, String format)
    {
        String seqNo = format;
        String temp = seqName;

        try
        {
            temp = temp.replace('-', '_');
            temp = temp.replace(' ', '_');
            seqNo = PersistenceHelper.manager.getNextSequence(temp);
        }
        catch (Exception exception)
        {
            try
            {
            	createNewSequence(temp, 1, 1);
                seqNo = PersistenceHelper.manager.getNextSequence(temp);
            }
            catch (Exception exception1)
            {
                LOGGER.info("Error!! 시퀀스를 가져오는중에 에러가 발생했습니다.");
                exception1.printStackTrace();
            }
        }
        DecimalFormat decimalformat = new DecimalFormat(format);
        return decimalformat.format(Long.parseLong(seqNo));
    }

    /**
     * 현재 sequence중 가장 큰 sequence의 다음값을 반환한다.
     * 
     * @param seqName
     *            sequence name
     * @param format
     *            sequence format
     * @param tabName
     *            DB table name
     * @param colName
     *            DB column name
     * @return
     */
    public static String getSeqNo(String seqName, String format, String tabName, String colName)
    {
        String result = "";
        String serial = "";

        OraclePds81 dds = new OraclePds81();
        Connection con = null;
        ResultSet rs = null;
        try
        {  /*
			ORACLE : select to_char(  TO_NUMBER(SUBSTR(   NVL(   MAX(WTDOCUMENTNUMBER),'DOC-0'),5))+1,'00000000')  FROM WTDOCUMENTMASTER WHERE WTDOCUMENTNUMBER LIKE 'DOC-%';
			MSSQL : SELECT  STR(      DATEPART( SUBSTRING(ISNULL(MAX(WTDOCUMENTNUMBER),'DOC-0'),5))+1,'00000000')  FROM WTDOCUMENTMASTER WHERE WTDOCUMENTNUMBER LIKE 'DOC-%';
		*/
        	Pattern pattern = Pattern.compile("0");
        	Matcher matcher = pattern.matcher(format);
        	
        	while (matcher.find()) {
        		serial += "_";
        	}
        	
            String sql = "SELECT MAX(" + colName + ") no FROM " + tabName + " WHERE " + colName + " LIKE '" + seqName
                    + serial + "'";
            
            con = dds.getDataSource().getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                if (rs.getString("no") == null) result = "1";
                else 
                {
                	String no = rs.getString("no");
                	LOGGER.info("ManageSequence::getSeqNo:no.substring() = "+no.substring(seqName.length()));
            		result = "" + (Integer.parseInt(no.substring(seqName.length())) + 1);
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (rs != null) rs.close();
                if (con != null) con.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        DecimalFormat decimalformat = new DecimalFormat(format);
        return decimalformat.format(Long.parseLong(result));
    }

    /**
     * interval에 지정된 간격만큼 늘어나는 시퀀스를 반환한다.
     * 
     * @param seqName
     *            시퀀스 이름
     * @param interval
     *            시퀀스 간격
     * @param format
     * @return
     */
    public static String getSeqNo(String seqName, int interval, String format)
    {
        String seqNo = format;
        String temp = seqName;

        try
        {
            temp = temp.replace('-', '_');
            temp = temp.replace(' ', '_');
            seqNo = PersistenceHelper.manager.getNextSequence(temp);
        }
        catch (Exception exception)
        {
            
            try
            {
            	createNewSequence(temp, interval, interval);
                seqNo = PersistenceHelper.manager.getNextSequence(temp);
            }
            catch (Exception exception1)
            {
                LOGGER.info("Error!! 시퀀스를 가져오는중에 에러가 발생했습니다.");
            }
        }

        DecimalFormat decimalformat = new DecimalFormat(format);
        return decimalformat.format(Long.parseLong(seqNo));
    }

    /**
     * 시퀀스이름이 길 경우 시퀀스 길이를 제한한다.
     * 
     * @param seqName
     *            가져올 시퀀스 이름
     * @param format
     *            필요한 자릿수 ex) "000"
     * @param position
     *            시퀀스 이름의 길이
     * @return 주어진 format의 시퀀스를 반환
     */
    public static String getSeqNo(String seqName, String format, int position)
    {
        String temp = seqName.replace('-', '_');
        temp = temp.replace(' ', '_');

        byte[] arry = temp.getBytes();
        if (arry.length >= position) temp = new String(arry, 0, position);

        return getSeqNo(temp, format);
    }

    /**
     * Sequence 를 생성해주는 Method
     */
    private static void createNewSequence(String s, int i, int j) throws Exception
    {
    	
    	MethodContext methodcontext = null;
		WTConnection wtconnection = null;

        PreparedStatement st = null;
        ResultSet rs = null;
        
		try {
			methodcontext = MethodContext.getContext();
			wtconnection = (WTConnection) methodcontext.getConnection();
			Connection con = wtconnection.getConnection();
			Statement stmt = con.createStatement();
			LOGGER.info("create sequence " + s + " increment by " + j + " start with " + i);
            stmt.execute("create sequence " + s + " increment by " + j + " start with " + i);
		}
        catch (Exception exception)
        {
            LOGGER.info("Error!! 시퀀스 생성시 에러가 발생했습니다.");
            throw exception;
        
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