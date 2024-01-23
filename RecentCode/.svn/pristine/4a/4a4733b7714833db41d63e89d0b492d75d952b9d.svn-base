/**
 * @(#)	CompanyState.java
 * Copyright (c) e3ps. All rights reserverd
 * 
 * @version 1.00
 * @since jdk 1.4.2
 * @author Cho Sung Ok, jerred@e3ps.com
 */

package com.e3ps.org.bean;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import wt.util.WTProperties;

public class CompanyState {
	public static Map<String, String> dutyTable = new HashMap<>();
	public static Map<String, String> nameTable = new HashMap<>();
	public static List<String> dutyNameList = new ArrayList<>();
	public static List<String> dutyCodeList = new ArrayList<>();
	public static URL defaultURL;
	public static String companyName;
	public static String ldapAdapter;
	public static String ldapUser;
	public static String ldapPassword;
	public static String ldapDirectoryInfo;
	static {
		try {
			defaultURL = new URL(WTProperties.getServerCodebase().toString() + "lgchem/portal/images/icon/noimage.gif");	
		} catch ( Exception e ) {}
		
//		companyName = "iljin";
//		String dutyNameStr = "대표이사;부사장;전무;상무;이사;상무보;수석연구원;부장;책임연구원갑;책임연구원을;차장;선임연구원;과장;전임연구원;대리;연구원;주임;사원;실장";
//		String dutyCodeStr = "DC_01;DC_02;DC_03;DC_04;DC_05;DC_06;DC_07;DC_08;DC_09;DC_10;DC_11;DC_12;DC_13;DC_14;DC_15;DC_16;DC_17;DC_18;DC_19";
		
		companyName = "worldex";
		String dutyNameStr = "대표이사;전무이사;이사;부장;차장;과장;대리;사원;wcadmin";
		String dutyCodeStr = "DC_01;DC_02;DC_03;DC_04;DC_05;DC_06;DC_07;DC_08;DC_09";
		
		StringTokenizer nameToken = new StringTokenizer(dutyNameStr,";");
		StringTokenizer codeToken = new StringTokenizer(dutyCodeStr,";");
		while(nameToken.hasMoreTokens()){
		    String name = (String)nameToken.nextElement();
		    String code = (String)codeToken.nextElement();
		    dutyTable.put(code,name);
		    nameTable.put(name,code);
			dutyNameList.add(name); 
			dutyCodeList.add(code);
		}
		
		ldapAdapter = "com.e3ps.Ldap";
		ldapUser = "cn=Manager";
		ldapPassword = "ldapadmin";
//		ldapDirectoryInfo = "ou=people,cn=AdministrativeLdap,cn=Windchill_11.0,o=ptc";
		ldapDirectoryInfo = "ou=people,cn=AdministrativeLdap,cn=Windchill_12.1,o=ptc";
	}
}
