package com.e3ps.admin.bean;

import java.util.Enumeration;
import java.util.List;

import com.e3ps.admin.AuthorityGroupType;
import com.e3ps.admin.AuthorityObjectType;
import com.e3ps.admin.ObjectAuthGroup;
import com.e3ps.admin.service.AdminHelper;
import com.e3ps.common.message.util.MessageUtil;

import wt.org.WTGroup;
import wt.org.WTPrincipalReference;

public class ObjectAuthGroupData {
	
	private String permissionGroupOid;
	private String readGroupOid;
	private String parentGroupOid;
	private String display;
	private String objTypeKey;
	private String objTypeValue;
	
	public ObjectAuthGroupData(AuthorityObjectType authObjectType) throws Exception {
		
		List<ObjectAuthGroup> list = AdminHelper.manager.getObjectAuthGroup(authObjectType);
		if(list.size()>0) {
			for (ObjectAuthGroup objectAuthGroup : list) {
				if("PERMISSION".equals(objectAuthGroup.getAuthGroupType().toString())) this.permissionGroupOid = objectAuthGroup.getObjectGroup().getPersistInfo().getObjectIdentifier().getStringValue();
				if("READ".equals(objectAuthGroup.getAuthGroupType().toString())) this.readGroupOid = objectAuthGroup.getObjectGroup().getPersistInfo().getObjectIdentifier().getStringValue();
			}
			
			Enumeration<?> parentGroups = list.get(0).getObjectGroup().parentGroups();
			
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
		}
		
		this.display = authObjectType.getFullDisplay();
		this.objTypeKey = authObjectType.toString();
		this.objTypeValue = authObjectType.getDisplay();
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getObjTypeKey() {
		return objTypeKey;
	}

	public void setObjTypeKey(String objTypeKey) {
		this.objTypeKey = objTypeKey;
	}

	public String getObjTypeValue() {
		return objTypeValue;
	}

	public void setObjTypeValue(String objTypeValue) {
		this.objTypeValue = objTypeValue;
	}

	public String getParentGroupOid() {
		return parentGroupOid;
	}

	public void setParentGroupOid(String parentGroupOid) {
		this.parentGroupOid = parentGroupOid;
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

	
	
}
