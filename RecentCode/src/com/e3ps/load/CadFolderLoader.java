package com.e3ps.load;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.WCUtil;

import wt.epm.EPMDocumentMaster;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.folder.Folder;
import wt.folder.FolderEntry;
import wt.folder.FolderHelper;
import wt.inf.container.WTContainerRef;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.vc.VersionControlHelper;

public class CadFolderLoader implements RemoteAccess, Serializable{

	/**
	 * windchill com.e3ps.load.CadFolderLoader D:\TESTDIR wcadmin wcadmin
	 */
	private static final long serialVersionUID = 1L;
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.LOAD.getName());
	
	public static void main(String[] args) throws Exception {

		String rootFolderPath = "D:\\TEMPDIR";
		
		if (args != null && args.length > 0 && args[0].trim().length() > 0) {
			rootFolderPath = args[0];
		}
		
		String userName = "wcadmin";
		String password = "wcadmin";
		
		if(args != null && args.length > 2) {
			userName = args[1];
			password = args[2];
		}
		
		setUser(userName, password);

		new CadFolderLoader().load(rootFolderPath);
		
	}

	public static void setUser(final String id, final String pw) {
		RemoteMethodServer.getDefault().setUserName(id);
		RemoteMethodServer.getDefault().setPassword(pw);
	}

	public void load(String rootFolderPath) throws RemoteException, InvocationTargetException {
		Class argTypes[] = new Class[]{ String.class };
		Object args[] = new Object[]{ rootFolderPath };
		RemoteMethodServer.getDefault().invoke("_load", null, this, argTypes, args);
	}
	
	public void _load(String rootFolderPath) {
		try {

			String defaultPath = "Default/";
			
			int lastSeparator = rootFolderPath.lastIndexOf("\\");
			String rootPath = rootFolderPath.substring(lastSeparator + 1);
			String localPath = rootFolderPath.substring(0, lastSeparator);
			
			List<String> folderList = new ArrayList<>();
			List<String> fileList = new ArrayList<>();
			
			List<Path> dirs = Files.walk(Paths.get(rootFolderPath), Integer.MAX_VALUE)
	                .collect(Collectors.toList());
			
			for(Path path : dirs) {
				File file = path.toFile();
				
				if(file.isDirectory()) {
					folderList.add(file.toString());
				} else {
					fileList.add(file.toString());
				}
			}
			
			WTContainerRef container = WCUtil.getWTContainerRef();
			
			
			Map<String ,Folder> folderMap = new HashMap<>();
			for(String path : folderList) {
				String folderPath = defaultPath + path.replace(localPath, "").replace("\\", "/");
				
				Folder folder = null;
				try {
					folder = FolderHelper.service.getFolder(folderPath, container);
				}catch(Exception e) {
					folder = FolderHelper.service.createSubFolder(folderPath, container);
				}
				
				folderMap.put(folderPath, folder);
			}
			
			QuerySpec qs = new QuerySpec();
			int idx = qs.appendClassList(EPMDocumentMaster.class, true);
			
			Map<String ,EPMDocumentMaster> fileMap = new HashMap<>();
			Map<String, String> fileFolderPathMap = new HashMap<>();
			for(String path : fileList) {
				int last = path.lastIndexOf("\\");
				String fileName = path.substring(last + 1);
				String folderPath = defaultPath + path.substring(0, last).replace(localPath, "").replace("\\", "/");
				
				fileFolderPathMap.put(fileName, folderPath);
				
				if(qs.getConditionCount() > 0) {
					qs.appendAnd();
				}
				qs.appendWhere(new SearchCondition(EPMDocumentMaster.class, EPMDocumentMaster.NUMBER, SearchCondition.EQUAL, fileName), new int[] { idx });
			}

			QueryResult qr = PersistenceHelper.manager.find(qs);
			
			while(qr.hasMoreElements()) {
				Object[] obj = (Object[]) qr.nextElement();
				EPMDocumentMaster master = (EPMDocumentMaster) obj[0];
				
				fileMap.put(master.getNumber(), master);
			}
			
			
			Iterator<Entry<String, EPMDocumentMaster>> it = fileMap.entrySet().iterator();			
			while(it.hasNext()) {
				Entry<String, EPMDocumentMaster> entry = it.next();
				String fileName = entry.getKey();
				EPMDocumentMaster master = entry.getValue();
				
				QueryResult resultSet = VersionControlHelper.service.allVersionsOf(master);
				
				while(resultSet.hasMoreElements()) {
					FolderHelper.service.changeFolder((FolderEntry) resultSet.nextElement(), folderMap.get(fileFolderPathMap.get(fileName)));					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
