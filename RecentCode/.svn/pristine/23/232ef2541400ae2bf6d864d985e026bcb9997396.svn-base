package com.e3ps.common.folder;

import java.util.Vector;

import wt.access.AccessPermission;
import wt.admin.AdminDomainRef;
import wt.clients.folder.FolderTaskLogic;
import wt.fc.IdentityCollationKeyFactory;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.folder.Cabinet;
import wt.folder.CabinetBased;
import wt.folder.Folder;
import wt.folder.FolderEntry;
import wt.folder.FolderHelper;
import wt.folder.FolderNotFoundException;
import wt.folder.SubFolder;
import wt.inf.container.ExchangeContainer;
import wt.inf.container.WTContainerHelper;
import wt.query.QueryException;
import wt.session.SessionHelper;
import wt.util.CollationKeyFactory;
import wt.util.SortedEnumeration;
import wt.util.WTException;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.WCUtil;

public class FolderUtil
{

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
    public static Cabinet createCabinet(String name)
    {
        Cabinet cabinet = null;
        try
        {
            cabinet = FolderHelper.service.createCabinet(name, getRootDomain(), "", WTContainerHelper.getClassicRef());
        }
        catch (WTException e)
        {
            e.printStackTrace();
        }
        return cabinet;
    }
    
    public static Cabinet createCabinet(String name, AdminDomainRef ref)
    {
        Cabinet cabinet = null;
        try
        {
            cabinet = FolderHelper.service.createCabinet(name, ref, "", WTContainerHelper.getClassicRef());
        }
        catch (WTException e)
        {
            e.printStackTrace();
        }
        return cabinet;
    }

    public static AdminDomainRef getRootDomain()
    {
        AdminDomainRef rootDomain = null;
        try
        {
            ExchangeContainer container = WTContainerHelper.service.getExchangeContainer();
            rootDomain = container.getDomainRef();
        }
        catch (WTException e)
        {
            e.printStackTrace();
        }

        return rootDomain;
    }

    public static Folder createFolder(Folder parentFolder, String name) throws Exception
    {
        SubFolder subfolder = SubFolder.newSubFolder(name);
//        subfolder.setInheritedDomain(false);
//        DomainAdministeredHelper.setAdminDomain(subfolder, getRootDomain());
        FolderHelper.assignLocation((FolderEntry) subfolder, parentFolder);
        return (Folder) PersistenceHelper.manager.save(subfolder);
    }

    // here
    public static Folder getFolder(String folderPath)
    {
        Folder folder = null;
        if (!availableFolder(folderPath))
        {
            folder = createFolder(folderPath);
        }
        else
        {
            try
            {
                folder = FolderHelper.service.getFolder(folderPath,WCUtil.getWTContainerRef());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return folder;
    }

    public static Folder createFolder(String s)
    {
        Folder folder = null;
        //folder = FolderHelper.service.createSubFolder(s);
		try {
			folder = FolderHelper.service.createSubFolder(s, WCUtil.getWTContainerRef());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return folder;
    }

    public static boolean availableFolder(String s)
    {
        boolean exist = false;
        Folder folder = null;
        try
        {
            //folder = FolderHelper.service.getFolder(s);
        	folder = FolderHelper.service.getFolder(s, WCUtil.getWTContainerRef());
            if (folder != null)
                exist = true;
            else
                return false;
        }
        catch (Exception e)
        {}
        return exist;
    }

    public static Cabinet getPersonalCabinet() throws WTException
    {
        return FolderTaskLogic.getPersonalCabinet(SessionHelper.manager.getPrincipal());
    }

    public static Folder getMyCheckoutFolder() throws Exception
    {
        Cabinet cabinet = FolderTaskLogic.getPersonalCabinet(SessionHelper.manager.getPrincipal());
        String checkoutFolder = cabinet.getFolderPath() + "/Checked Out";

        Folder folder = null;
        try
        {
            folder = FolderHelper.service.getFolder(checkoutFolder);
        }
        catch (FolderNotFoundException e)
        {
            FolderHelper.service.createSubFolder(checkoutFolder);
            folder = FolderHelper.service.getFolder(checkoutFolder,WCUtil.getWTContainerRef());
        }

        return folder;
    }

    public static SortedEnumeration getSubFolders(Folder folder) throws WTException
    {
        return getSubFolders(folder, new IdentityCollationKeyFactory());
    }

    public static SortedEnumeration getSubFolders(Folder folder, CollationKeyFactory collationkeyfactory)
            throws WTException
    {
        SortedEnumeration sortedenumeration = null;
        QueryResult queryresult = FolderHelper.service.findSubFolders(folder);
        if (queryresult != null)
            sortedenumeration = new SortedEnumeration(queryresult.getEnumeration(), collationkeyfactory);
        return sortedenumeration;
    }

    public static Folder getSelectFolder(String name) throws QueryException, WTException
    {
        Folder folder = null;
		try {
			folder = FolderTaskLogic.getFolder(name,WCUtil.getWTContainerRef());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return folder;
    }

    public static FolderEntry moveSharedObject(Folder folder, FolderEntry folderEntry) throws WTException
    {
        if (FolderHelper.inPersonalCabinet((CabinetBased) folderEntry))
        {
            FolderEntry entry = (FolderEntry) FolderHelper.service.changeFolder(folderEntry, folder);
            return entry;
        }
        else
        {
            return null;
        }
    }

    public static FolderEntry moveSharedObject(String folderPath, FolderEntry folderEntry) throws WTException
    {
        if (FolderHelper.inPersonalCabinet((CabinetBased) folderEntry))
        {
            String folderName = folderPath.trim();

            Folder folder = null;
            try
            {
                folder = FolderHelper.service.getFolder(folderName);
            }
            catch (FolderNotFoundException e)
            {
                FolderHelper.service.createSubFolder(folderName);
                folder = FolderHelper.service.getFolder(folderName);
            }
            FolderEntry entry = (FolderEntry) FolderHelper.service.changeFolder(folderEntry, folder);
            return entry;
        }
        else
        {
            return null;
        }
    }


    public static SortedEnumeration getAllCabinets(boolean isIncludePersonal) throws QueryException, WTException
    {
    	SortedEnumeration sortedenumeration = null;
    	try {
	        QueryResult queryresult;
				queryresult = FolderHelper.service.findCabinets(AccessPermission.READ, !isIncludePersonal, WCUtil.getWTContainerRef());
	        if (queryresult != null)
	            sortedenumeration = new SortedEnumeration(queryresult.getEnumeration(), new IdentityCollationKeyFactory());
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
        return sortedenumeration;
    }
    

    public static Vector getSubFolderList(Folder folder) throws WTException {
    	Vector subs = new Vector();
    	try {
    		getSubFolderList(folder, subs);
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	return subs;
    }
    
    public static void getSubFolderList(Folder folder, Vector vec) {
		try {
			SortedEnumeration en = getSubFolders(folder);
			while(en.hasMoreElements()) {
				SubFolder sub = (SubFolder)en.nextElement();
				if(vec == null)
					vec = new Vector();

				vec.add(sub);

				getSubFolderList(sub, vec);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
