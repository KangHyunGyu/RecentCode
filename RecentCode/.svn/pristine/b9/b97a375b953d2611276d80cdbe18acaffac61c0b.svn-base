package com.e3ps.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.e3ps.common.bean.FolderData;
import com.e3ps.common.log4j.Log4jPackages;

import wt.access.AccessPermission;
import wt.admin.AdminDomainRef;
import wt.clients.folder.FolderTaskLogic;
import wt.fc.IdentityCollationKeyFactory;
import wt.fc.IdentityHelper;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.folder.Cabinet;
import wt.folder.CabinetBased;
import wt.folder.Folder;
import wt.folder.FolderEntry;
import wt.folder.FolderHelper;
import wt.folder.FolderNotFoundException;
import wt.folder.SubFolder;
import wt.folder.SubFolderIdentity;
import wt.inf.container.ExchangeContainer;
import wt.inf.container.WTContainerHelper;
import wt.pom.Transaction;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SQLFunction;
import wt.query.SearchCondition;
import wt.session.SessionHelper;
import wt.util.CollationKeyFactory;
import wt.util.SortedEnumeration;
import wt.util.WTAttributeNameIfc;
import wt.util.WTException;

/**
 * @dsec.module e3ps.common.folder
 * @dsec.program FolderUtil.java
 * @dsec.desc 공통 유틸리티
 * @dsec.type Service
 * @dsec.etc -
 * @dsec.history
 * 
 *               <pre>
 *------------------------------------------------------------------------------------------------------
 * 변경이력
 *------------------------------------------------------------------------------------------------------
 * 일자           작성자   CSR#                                        변경내역
 *--------------  -------- ------------------------------------------- -------------------------------------
 * 2015. 7. 3.  Administrator   중국확산프로젝트                            최초개발
 *               </pre>
 */
public class FolderUtil {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());

	/**
	 * name 이란 이름으로 Cabinet을 생성한다.
	 * 
	 * @param name
	 * @return name이란 이름의 Cabinet 반환
	 */
	public static Cabinet createCabinet(String name) throws WTException {
		Cabinet cabinet = null;
		cabinet = FolderHelper.service.createCabinet(name, getRootDomain(), "", WTContainerHelper.getClassicRef());
		return cabinet;
	}

	/**
	 * @desc
	 * @author
	 * @date 2015. 7. 3.
	 *
	 * @param name
	 * @param ref
	 * @return
	 * @throws WTException
	 */
	public static Cabinet createCabinet(String name, AdminDomainRef ref) throws WTException {
		Cabinet cabinet = null;
		cabinet = FolderHelper.service.createCabinet(name, ref, "", WTContainerHelper.getClassicRef());
		return cabinet;
	}

	/**
	 * @desc
	 * @author
	 * @date 2015. 7. 3.
	 *
	 * @return
	 */
	public static AdminDomainRef getRootDomain() throws WTException {
		AdminDomainRef rootDomain = null;
		ExchangeContainer container = WTContainerHelper.service.getExchangeContainer();
		rootDomain = container.getDomainRef();
		return rootDomain;
	}

	/**
	 * SubFolder를 생성한다
	 *
	 * @param parentFolder 부모폴더
	 * @param name         생성할 폴더의 이름
	 * @throws Exception
	 */
	public static Folder createFolder(Folder parentFolder, String name) throws Exception {

		Folder folder = null;
		Transaction trx = new Transaction();
		try {
			trx.start();
			SubFolder subfolder = SubFolder.newSubFolder(name);
			FolderHelper.assignLocation((FolderEntry) subfolder, parentFolder);
			folder = (Folder) PersistenceHelper.manager.save(subfolder);
			trx.commit();
			trx = null;
		} catch (WTException e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
				trx = null;
			}
		}

		return folder;
	}

	// here
	/**
	 * @desc
	 * @author
	 * @date 2015. 7. 3.
	 *
	 * @param folderPath
	 * @return
	 */
	public static Folder getFolder(String folderPath) throws WTException {
		Folder folder = null;
		if (!availableFolder(folderPath)) {
			folder = createFolder(folderPath);
		} else {
			folder = FolderHelper.service.getFolder(folderPath, WCUtil.getWTContainerRef());
		}
		return folder;
	}

	/**
	 * @desc
	 * @author
	 * @date 2015. 7. 3.
	 *
	 * @param s
	 * @return
	 * @throws WTException
	 */
	public static Folder createFolder(String s) throws WTException {
		Folder folder = null;
		folder = FolderHelper.service.createSubFolder(s, WCUtil.getWTContainerRef());
		return folder;
	}

	/**
	 * @desc
	 * @author
	 * @date 2015. 7. 3.
	 *
	 * @param s
	 * @return
	 */
	public static boolean availableFolder(String s) throws WTException {
		boolean exist = false;
		Folder folder = null;
		folder = FolderHelper.service.getFolder(s, WCUtil.getWTContainerRef());
		if (folder != null)
			exist = true;
		else
			return false;
		return exist;
	}

	/**
	 * 개인 캐비넷을 가져온다.
	 *
	 * @return Personal Cabinet
	 * @throws Exception
	 */
	public static Cabinet getPersonalCabinet() throws WTException {
		return FolderTaskLogic.getPersonalCabinet(SessionHelper.manager.getPrincipal());
	}

	/**
	 * 개인의 체크아웃 폴더를 가져온다.
	 *
	 * @return Personal CheckOut Folder
	 * @throws Exception
	 */
	public static Folder getMyCheckoutFolder() throws Exception {
		Cabinet cabinet = FolderTaskLogic.getPersonalCabinet(SessionHelper.manager.getPrincipal());
		String checkoutFolder = cabinet.getFolderPath() + "/Checked Out";

		Folder folder = null;
		try {
			folder = FolderHelper.service.getFolder(checkoutFolder);
		} catch (FolderNotFoundException e) {
			FolderHelper.service.createSubFolder(checkoutFolder);
			folder = FolderHelper.service.getFolder(checkoutFolder, WCUtil.getWTContainerRef());
		}

		return folder;
	}

	/**
	 * @desc
	 * @author
	 * @date 2015. 7. 3.
	 *
	 * @param folder
	 * @return
	 * @throws WTException
	 */
	public static SortedEnumeration getSubFolders(Folder folder) throws WTException {
		return getSubFolders(folder, new IdentityCollationKeyFactory());
	}

	/**
	 * @desc
	 * @author
	 * @date 2015. 7. 3.
	 *
	 * @param folder
	 * @param collationkeyfactory
	 * @return
	 * @throws WTException
	 */
	public static SortedEnumeration getSubFolders(Folder folder, CollationKeyFactory collationkeyfactory)
			throws WTException {
		SortedEnumeration sortedenumeration = null;
		QueryResult queryresult = FolderHelper.service.findSubFolders(folder);
		if (queryresult != null)
			sortedenumeration = new SortedEnumeration(queryresult.getEnumeration(), collationkeyfactory);
		return sortedenumeration;
	}

	/**
	 * @desc
	 * @author
	 * @date 2015. 7. 3.
	 *
	 * @param name
	 * @return
	 * @throws QueryException
	 * @throws WTException
	 */
	public static Folder getSelectFolder(String name) throws QueryException, WTException {
		Folder folder = null;
		folder = FolderTaskLogic.getFolder(name, WCUtil.getWTContainerRef());
		return folder;
	}

	/**
	 * @desc
	 * @author
	 * @date 2015. 7. 3.
	 *
	 * @param folder
	 * @param folderEntry
	 * @return
	 * @throws WTException
	 */
	public static FolderEntry moveSharedObject(Folder folder, FolderEntry folderEntry) throws WTException {
		if (FolderHelper.inPersonalCabinet((CabinetBased) folderEntry)) {
			FolderEntry entry = (FolderEntry) FolderHelper.service.changeFolder(folderEntry, folder);
			return entry;
		} else {
			return null;
		}
	}

	/**
	 * @desc
	 * @author
	 * @date 2015. 7. 3.
	 *
	 * @param folderPath
	 * @param folderEntry
	 * @return
	 * @throws WTException
	 */
	public static FolderEntry moveSharedObject(String folderPath, FolderEntry folderEntry) throws WTException {
		if (FolderHelper.inPersonalCabinet((CabinetBased) folderEntry)) {
			String folderName = folderPath.trim();

			Folder folder = null;
			try {
				folder = FolderHelper.service.getFolder(folderName);
			} catch (FolderNotFoundException e) {
				FolderHelper.service.createSubFolder(folderName);
				folder = FolderHelper.service.getFolder(folderName);
			}
			FolderEntry entry = (FolderEntry) FolderHelper.service.changeFolder(folderEntry, folder);
			return entry;
		} else {
			return null;
		}
	}

	/**
	 * 모든 캐비넷을 가져온다.
	 * 
	 * @param isIncludePersonal true:개인캐비넷포함 false:공용캐비넷만
	 * @return
	 * @throws QueryException
	 * @throws WTException
	 */
	public static SortedEnumeration getAllCabinets(boolean isIncludePersonal) throws QueryException, WTException {
		SortedEnumeration sortedenumeration = null;
		QueryResult queryresult;
		queryresult = FolderHelper.service.findCabinets(AccessPermission.READ, !isIncludePersonal,
				WCUtil.getWTContainerRef());
		if (queryresult != null)
			sortedenumeration = new SortedEnumeration(queryresult.getEnumeration(), new IdentityCollationKeyFactory());
		return sortedenumeration;
	}

	/**
	 * @desc
	 * @author
	 * @date 2015. 7. 3.
	 *
	 * @param folder
	 * @param vec
	 * @throws WTException
	 */
	public static void getSubFolderList(Folder folder, List vec) throws WTException {
		SortedEnumeration en = getSubFolders(folder);
		while (en.hasMoreElements()) {
			SubFolder sub = (SubFolder) en.nextElement();
			if (vec == null)
				vec = new ArrayList();
			vec.add(sub);
			getSubFolderList(sub, vec);
		}
	}

	@SuppressWarnings("unchecked")
	public static List<FolderData> getFolderTree(Folder folder, List list) throws Exception {
		FolderData data = new FolderData((SubFolder) folder);
		data.setData(getSubFolderList(folder));
		list.add(data);

		list = getFolderTreeData(folder, list);
		return list;
	}

	@SuppressWarnings("unchecked")
	public static List<FolderData> getFolderTreeData(Folder folder, List list) throws Exception {

		long longOid = CommonUtil.getOIDLongValue(folder);

		QuerySpec qs = new QuerySpec();
		int idx = qs.addClassList(SubFolder.class, true);

		SearchCondition sc = new SearchCondition(SubFolder.class, "folderingInfo.parentFolder.key.id",
				SearchCondition.EQUAL, longOid);
		qs.appendWhere(sc, new int[] { idx });

		qs.appendOrderBy(new OrderBy(new ClassAttribute(SubFolder.class, WTAttributeNameIfc.ID_NAME), false), new int[] { idx });
		QueryResult qr = PersistenceHelper.manager.find(qs);
		while (qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			SubFolder sb = (SubFolder) obj[0];
			FolderData data = new FolderData(sb);
			list.add(data);

			Folder children = FolderUtil.getFolderOid(data.getOid());
			if (children != null) {
				list = getFolderTreeData(children, list);
			}
		}
		return list;
	}

	/*
	 * public static void getFolderTree(Folder folder, List list) throws WTException
	 * {
	 * 
	 * FolderData data = new FolderData((SubFolder) folder);
	 * data.setData(getSubFolderList(folder)); list.add(data);
	 * 
	 * if(list.size() > 0) { for(int i=0; i<list.size(); i++) {
	 * System.out.println("list++++++++++ : " + list.get(i)); FolderData fd =
	 * (FolderData)list.get(i); System.out.println("FD NAME : " + fd.getName()); } }
	 * 
	 * SortedEnumeration en = getSubFolders(folder); while (en.hasMoreElements()) {
	 * SubFolder sub = (SubFolder) en.nextElement(); getFolderTree(sub, list); }
	 * 
	 * }
	 */

	public static void getSubFolderListForJsTree(Folder folder, Map<String, Object> folderMap) throws WTException {

		folderMap.put("text", folder.getName());
		folderMap.put("path", folder.getFolderPath());

		SortedEnumeration en = getSubFolders(folder);

		List<Map<String, Object>> childrenList = new ArrayList<>();

		while (en.hasMoreElements()) {
			Map<String, Object> childFolderMap = new HashMap<>();
			SubFolder sub = (SubFolder) en.nextElement();

			getSubFolderListForJsTree(sub, childFolderMap);

			childrenList.add(childFolderMap);
		}

		folderMap.put("children", childrenList);
	}

	/**
	 * 
	 * @desc : cabinet 의 subFoder List
	 * @author : plmadmin
	 * @date : 2019. 9. 10.
	 * @method : getCabinetSubFolder
	 * @return : void
	 * @param folder
	 */
	public static List<Folder> getCabinetSubFolder(Folder folder) {

		List<Folder> list = new ArrayList<Folder>();

		try {

			long longOid = CommonUtil.getOIDLongValue(folder);

			QuerySpec qs = new QuerySpec();
			int idx = qs.addClassList(SubFolder.class, true);

			SearchCondition sc = new SearchCondition(SubFolder.class, SubFolder.CABINET + ".key.id",
					SearchCondition.EQUAL, longOid);
			qs.appendWhere(sc, new int[] { idx });

			qs.appendAnd();
			sc = new SearchCondition(SubFolder.class, SubFolder.PARENT_FOLDER + ".key.id", SearchCondition.EQUAL,
					Long.parseLong("0"));
			qs.appendWhere(sc, new int[] { idx });

			qs.appendOrderBy(new OrderBy(new ClassAttribute(SubFolder.class, SubFolder.NAME), false),
					new int[] { idx });

			LOGGER.info(qs.toString());

			QueryResult rt = PersistenceHelper.manager.find(qs);
			int i = 0;
			while (rt.hasMoreElements()) {
				Object[] obj = (Object[]) rt.nextElement();
				Folder subFolder = (Folder) obj[0];

				list.add(subFolder);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	// 바로 아래 sub 폴더만 갖고오기
	public static List<FolderData> geSubFolderList(Folder folder) throws WTException {
		List<FolderData> list = new ArrayList<FolderData>();

		try {
			long longOid = CommonUtil.getOIDLongValue(folder);

			QuerySpec qs = new QuerySpec();
			int idx = qs.addClassList(SubFolder.class, true);

			SearchCondition sc = new SearchCondition(SubFolder.class, "folderingInfo.parentFolder.key.id",
					SearchCondition.EQUAL, longOid);
			qs.appendWhere(sc, new int[] { idx });

			qs.appendOrderBy(new OrderBy(new ClassAttribute(SubFolder.class, SubFolder.NAME), false),
					new int[] { idx });

			QueryResult qr = PersistenceHelper.manager.find(qs);
			while (qr.hasMoreElements()) {
				Object[] obj = (Object[]) qr.nextElement();
				FolderData data = new FolderData((SubFolder) obj[0]);
				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	// 바로 아래 sub 폴더만 갖고오기 + children setting -- tsjeong
	public static List<FolderData> getSubFolderList(Folder folder) throws WTException {
		List<FolderData> list = new ArrayList<FolderData>();

		try {
			long longOid = CommonUtil.getOIDLongValue(folder);

			QuerySpec qs = new QuerySpec();
			int idx = qs.addClassList(SubFolder.class, true);

			SearchCondition sc = new SearchCondition(SubFolder.class, "folderingInfo.parentFolder.key.id",
					SearchCondition.EQUAL, longOid);
			qs.appendWhere(sc, new int[] { idx });

			qs.appendOrderBy(new OrderBy(new ClassAttribute(SubFolder.class, SubFolder.NAME), false),
					new int[] { idx });

			QueryResult qr = PersistenceHelper.manager.find(qs);
			while (qr.hasMoreElements()) {
				Object[] obj = (Object[]) qr.nextElement();
				FolderData data = new FolderData((SubFolder) obj[0]);
				Folder children = FolderUtil.getFolderOid(data.getOid());
				List<FolderData> subList = geSubFolderList(children);
				if (subList.size() > 0) {
					data.setData(getSubFolderList(children));
				}

				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	// Library 바로 아래 sub 폴더만 갖고오기 + children setting -- tsjeong
	public static List<FolderData> getLibrarySubFolderList(Folder folder) throws WTException {
		List<FolderData> list = new ArrayList<FolderData>();

		try {
			long longOid = 0;
			if (folder != null) {
				longOid = CommonUtil.getOIDLongValue(folder);
			}

			QuerySpec qs = new QuerySpec();
			int idx = qs.addClassList(SubFolder.class, true);

			SearchCondition sc = new SearchCondition(SubFolder.class, "folderingInfo.parentFolder.key.id",
					SearchCondition.EQUAL, longOid);
			qs.appendWhere(sc, new int[] { idx });

			qs.appendAnd();
			sc = new SearchCondition(SubFolder.class, "containerReference.key.classname", SearchCondition.EQUAL,
					"wt.inf.library.WTLibrary");
			qs.appendWhere(sc, new int[] { idx });

			qs.appendOrderBy(new OrderBy(new ClassAttribute(SubFolder.class, SubFolder.NAME), false),
					new int[] { idx });

			QueryResult qr = PersistenceHelper.manager.find(qs);
			while (qr.hasMoreElements()) {
				Object[] obj = (Object[]) qr.nextElement();
				FolderData data = new FolderData((SubFolder) obj[0]);
				Folder children = FolderUtil.getFolderOid(data.getOid());

				List<FolderData> subList = geSubFolderList(children);

				if (subList.size() > 0) {
					data.setData(geSubFolderList(children));
				}

				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * @desc : oid로 folder 가져오기
	 * @author : mnyu
	 * @date : 2020. 2. 10.
	 * @method : getFolderOid
	 * @return : Folder
	 * @param fOid
	 * @return
	 */
	public static Folder getFolderOid(String fOid) {
		Folder folder = null;
		try {
			long longOid = CommonUtil.getOIDLongValue(fOid);

			QuerySpec qs = new QuerySpec();
			int idx = qs.addClassList(SubFolder.class, true);

			SearchCondition sc = new SearchCondition(SubFolder.class, "thePersistInfo.theObjectIdentifier.id",
					SearchCondition.EQUAL, longOid);
			qs.appendWhere(sc, new int[] { idx });

			QueryResult qr = PersistenceHelper.manager.find(qs);
			while (qr.hasMoreElements()) {
				Object[] obj = (Object[]) qr.nextElement();
				folder = (SubFolder) obj[0];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return folder;
	}

	/**
	 * @desc : folder Name 수정
	 * @author : mnyu
	 * @date : 2020. 2. 10.
	 * @method : modifyFolder
	 * @return : void
	 * @param oid
	 * @param name
	 * @throws Exception
	 */
	public static void modifyFolder(String oid, String name) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();

			SubFolder folder = (SubFolder) getFolderOid(oid);
			if (folder != null) {
				SubFolderIdentity identity = (SubFolderIdentity) folder.getIdentificationObject();
				identity.setName(name);
				IdentityHelper.service.changeIdentity(folder, identity);
			}

			trx.commit();
			trx = null;
		} catch (WTException e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
				trx = null;
			}
		}
	}

	/**
	 * @desc : folder 삭제
	 * @author : mnyu
	 * @date : 2020. 2. 10.
	 * @method : deleteFolder
	 * @return : void
	 * @param oid
	 */
	public static void deleteFolder(String oid) throws Exception {
		SubFolder folder = (SubFolder) getFolderOid(oid);
		if (folder != null) {
			PersistenceHelper.manager.delete(folder);
		}
	}
}
