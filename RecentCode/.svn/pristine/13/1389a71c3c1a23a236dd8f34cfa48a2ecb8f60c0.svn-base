package com.e3ps.epm.bean;

import java.io.File;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

import wt.epm.EPMDocument;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.iba.definition.litedefinition.AttributeDefDefaultView;
import wt.iba.definition.service.IBADefinitionHelper;
import wt.iba.value.StringValue;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.vc.VersionControlHelper;
import wt.vc.config.ConfigSpec;

import com.e3ps.common.content.service.CommonContentHelper;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.ptc.wvs.common.util.WVSProperties;
import com.ptc.wvs.server.publish.Publish;

public class EpmPublishUtil implements RemoteAccess{
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.EPM.getName());
	private static Method filterEPMDocumentPublishMethod;
	
	static {
		try{
			String s2 = WVSProperties.getPropertyValue("publish.service.filterepmdocumentpublishmethod");
            if(s2 != null)
            {
                StringTokenizer stringtokenizer = new StringTokenizer(s2.trim(), "/");
                if(stringtokenizer.countTokens() == 2)
                {
                    Class class1 = Class.forName(stringtokenizer.nextToken());
                    Class aclass[] = {
                        wt.epm.EPMDocument.class
                    };
                    filterEPMDocumentPublishMethod = class1.getMethod(stringtokenizer.nextToken(), aclass);
                   
                }
            }
		}catch(Exception ex){
			
		}
	}	
	
	public static void publish(EPMDocument epm)throws Exception{
		
		LOGGER.info("<<<<<<<<<<<<<<<publishEPM >>>>>>>>>>>");
        
		if (!RemoteMethodServer.ServerFlag) {
            try {
                Class argTypes[] = { EPMDocument.class };
                Object argValues[] = { epm };
                RemoteMethodServer.getDefault().invoke("publish", "com.e3ps.drawing.beans.EpmPublishUtil", null, argTypes, argValues);
                return;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
		
		
		boolean flag1 = true;
		   
        if(filterEPMDocumentPublishMethod != null)
            try {
                Object aobj[] = {
                		epm
                };
                flag1 = ((Boolean)filterEPMDocumentPublishMethod.invoke(null, aobj)).booleanValue();
            } catch(Exception exception) {
                exception.printStackTrace();
                flag1 = true;
            }
        if(flag1) {
        	ConfigSpec configspec = null;
       		ConfigSpec configspec1 = null;
       		Publish.doPublish(false, true, epm, configspec, null, false, null, null, 1, null, 2, null);
        }
		
	}
	/**
	 * 입력한 기간에 등록된 도면의 Publishing (변환) 요청
	 * @param predate
	 * @param postdate
	 * @param islastversion
	 */
	public static void multiPublish(String predate, String postdate, String islastversion) {
		try{
			QuerySpec qs = new QuerySpec();
			int idx = qs.appendClassList(EPMDocument.class, true);
			
			if(predate.length() > 0) {
				if(qs.getConditionCount() > 0) { qs.appendAnd(); }
				qs.appendWhere(new SearchCondition(EPMDocument.class, "thePersistInfo.createStamp", 
						SearchCondition.GREATER_THAN, DateUtil.convertStartDate(predate)), new int[] {idx});
			}
			if(postdate.length() > 0) {
				if(qs.getConditionCount() > 0) { qs.appendAnd(); }
				qs.appendWhere(new SearchCondition(EPMDocument.class, "thePersistInfo.createStamp", 
						SearchCondition.LESS_THAN, DateUtil.convertEndDate(postdate)), new int[] {idx});
			}
			
			//최신 이터레이션
			if(qs.getConditionCount() > 0) { qs.appendAnd(); }
			qs.appendWhere(VersionControlHelper.getSearchCondition(EPMDocument.class, true), new int[]{idx});
			//버전검색
			if(!StringUtil.checkString(islastversion)) islastversion = "true";
			if("true".equals(islastversion)) {
				AttributeDefDefaultView aview = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath("LatestVersionFlag");
			    if(aview != null) {
			        if(qs.getConditionCount() > 0) { qs.appendAnd(); }
			        int _idx = qs.appendClassList(StringValue.class, false);
			        qs.appendWhere(new SearchCondition(StringValue.class, "theIBAHolderReference.key.id",  EPMDocument.class, "thePersistInfo.theObjectIdentifier.id"),  new int[]{_idx, idx});
			        qs.appendAnd();
			        qs.appendWhere(new SearchCondition(StringValue.class, "definitionReference.key.id",  SearchCondition.EQUAL, aview.getObjectID().getId()), new int[]{_idx});
			        qs.appendAnd();
			        qs.appendWhere(new SearchCondition(StringValue.class, "value", SearchCondition.EQUAL, "TRUE"), new int[]{_idx});
			    }
			}
			
			QueryResult qr = PersistenceHelper.manager.find(qs);
			
			if(qr != null) {
				LOGGER.info(String.valueOf(qr.size()));
				
				while(qr.hasMoreElements()) {
					Object[] obj = (Object[])qr.nextElement();
					EPMDocument epm = (EPMDocument)obj[0];
					EpmPublishUtil.publish(epm);
				}
			}
						
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}

