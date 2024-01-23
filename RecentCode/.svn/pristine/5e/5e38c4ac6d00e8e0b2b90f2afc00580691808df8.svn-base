/**
 * 모듈명 또는 시스템명       :  com.e3ps.common.content.service
 * 프로그램명 또는 클래스 명    :  ContentService
 * 기능 설명             :  Content 서비스 
 * 프로그램 타입           : Interface
 * 비고 / 특이 사항           :   
 */

package com.e3ps.common.folder.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wt.folder.Folder;
import wt.inf.container.WTContainerRef;
import wt.method.RemoteInterface;
import wt.util.WTException;

@RemoteInterface
public interface CommonFolderService {	
   
   public static final Logger logger = LoggerFactory.getLogger(CommonFolderService.class);
   
   public ArrayList getFolderSortTree(final Folder obj) throws Exception;
  
   public ArrayList getFolderTree(final Folder obj) throws WTException;
   
   public ArrayList getFolderDTree(final Folder obj) throws WTException;
   
   public int compare(String object1, String object2);
   	
   public Folder getFolder(String location, WTContainerRef wtContainerRef)throws Exception;

void eventListener(Object obj, String event);
}
