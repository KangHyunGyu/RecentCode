package com.e3ps.common.bean;

import java.util.Enumeration;
import java.util.List;

import com.e3ps.admin.FolderAuthGroup;
import com.e3ps.admin.service.AdminHelper;
import com.e3ps.common.util.CommonUtil;

import wt.folder.SubFolder;
import wt.org.WTGroup;
import wt.org.WTPrincipalReference;

public class FolderData {
	
	private String id;
	private String oid;
	private String name;
	private String path;
	private int depth;
	private int sort;
	private String parent;
	private String parentGroupOid;
	private String permissionGroupOid;
	private String readGroupOid;
	
	private List<FolderData> data;
	
	
	public FolderData(String oid) throws Exception {
		SubFolder folder = (SubFolder)CommonUtil.getObject(oid);
		initialize(folder);
	}
	
	public FolderData(SubFolder folder) throws Exception {
		initialize(folder);
	}
	
	private void initialize(SubFolder folder) throws Exception {
		this.id = oid;
		this.oid = CommonUtil.getOIDString(folder);
		this.name = folder.getName();
		this.path = folder.getFolderPath();
		this.parent = CommonUtil.getOIDString(folder.getParentFolder().getObject());
		FolderAuthGroup auth_permission = AdminHelper.manager.getFolderAuthGroupBySubFolder(folder, "PERMISSION");
		FolderAuthGroup auth_read = AdminHelper.manager.getFolderAuthGroupBySubFolder(folder, "READ");
		if(auth_permission != null) this.permissionGroupOid = auth_permission.getAuthFoldergroup().getPersistInfo().getObjectIdentifier().getStringValue();
		if(auth_read != null) this.readGroupOid = auth_read.getAuthFoldergroup().getPersistInfo().getObjectIdentifier().getStringValue();
		if(auth_permission!=null) {
			Enumeration<?> parentGroups = auth_permission.getAuthFoldergroup().parentGroups();
			
			//무조건 부모 그룹 있어야 함. 로더 참조.
			WTGroup parentGroup = null;
			while(parentGroups.hasMoreElements()){
				Object o = (Object)parentGroups.nextElement();
				
				if(o instanceof WTPrincipalReference){
					WTPrincipalReference u = (WTPrincipalReference)o;
					parentGroup = (WTGroup)u.getPrincipal();
				}
			}
			this.parentGroupOid = parentGroup!=null?parentGroup.getPersistInfo().getObjectIdentifier().getStringValue():"";
		}else {
			this.parentGroupOid = "";
		}
	}


	public String getOid() {
		return oid;
	}


	public void setOid(String oid) {
		this.oid = oid;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public int getDepth() {
		return depth;
	}


	public void setDepth(int depth) {
		this.depth = depth;
	}


	public int getSort() {
		return sort;
	}


	public void setSort(int sort) {
		this.sort = sort;
	}


	public String getParent() {
		return parent;
	}


	public void setParent(String parent) {
		this.parent = parent;
	}
	
	public List<FolderData> getData() {
		return data;
	}


	public void setData(List<FolderData> data) {
		this.data = data;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public String getPermissionGroupOid() {
		return permissionGroupOid;
	}

	public void setPermissionGroupOid(String permissionGroupOid) {
		this.permissionGroupOid = permissionGroupOid;
	}

	public String getReadGroupOid() {
		return readGroupOid;
	}

	public void setReadGroupOid(String readGroupOid) {
		this.readGroupOid = readGroupOid;
	}

	public String getParentGroupOid() {
		return parentGroupOid;
	}

	public void setParentGroupOid(String parentGroupOid) {
		this.parentGroupOid = parentGroupOid;
	}


}
