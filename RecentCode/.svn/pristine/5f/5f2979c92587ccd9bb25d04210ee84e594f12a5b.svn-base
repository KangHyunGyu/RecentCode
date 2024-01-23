package com.e3ps.admin.bean;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.e3ps.admin.AuthorityGroup;
import com.e3ps.admin.util.AuthKey;
import com.e3ps.admin.util.ObjectKey;
import com.e3ps.common.util.StringUtil;
import com.e3ps.doc.util.DocTypePropList;
import com.fasterxml.jackson.annotation.JsonIgnore;

import wt.org.WTGroup;
import wt.util.WTContext;

public class AuthorityGroupData {

	private String oid;
	private String name;
	private String displayName;
	private String groupName;
	private String auth;
	private String authDisplayName;
	private String authObjectType;
	private String authObjectTypeName;
	private String authGroupType;
	private String authGroupTypeDisplayName;
	private String domainPath;
	private String domainPathDisplayName;
	private String description;
	
	private AuthorityGroup authGroup;
	
	public AuthorityGroupData(AuthorityGroup ag) throws Exception {
		this.authGroup = ag;
		this.oid = ag.getPersistInfo().getObjectIdentifier().getStringValue();
		this.name = StringUtil.checkNull(ag.getName());
		this.groupName = StringUtil.checkNull(ag.getGroup().getName());
		this.displayName = name + " [ " + groupName + " ]";
		this.auth = StringUtil.checkNull(ag.getAuth());
		this.authDisplayName = StringUtil.checkNull(AuthKey.AUTHKEY.getKeyMap().get(ag.getAuth()));
		this.authObjectType = StringUtil.checkNull(ag.getAuthObjectType());
		this.authObjectTypeName = StringUtil.checkNull((String)ObjectKey.OBJECTKEY.getKeyList().stream().filter(item -> ((String)item.get("code")).equals(ag.getAuthObjectType())).findFirst().orElseThrow(()-> new Exception("ENUM 클래스에 코드가 존재하지 않음")).get("name"));
		this.authGroupType = StringUtil.checkNull(ag.getCodeType().toString());
		this.authGroupTypeDisplayName = StringUtil.checkNull(ag.getCodeType().getDisplay(WTContext.getContext().getLocale()));
		this.domainPath = StringUtil.checkNull(ag.getDomainPath());
		this.domainPathDisplayName = StringUtil.checkNull(ag.getDomain().getDescription());
		this.description = StringUtil.checkNull(ag.getDescription());
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	public String getAuthGroupType() {
		return authGroupType;
	}
	public void setAuthGroupType(String authGroupType) {
		this.authGroupType = authGroupType;
	}
	public String getAuthGroupTypeDisplayName() {
		return authGroupTypeDisplayName;
	}
	public void setAuthGroupTypeDisplayName(String authGroupTypeDisplayName) {
		this.authGroupTypeDisplayName = authGroupTypeDisplayName;
	}
	public String getAuthDisplayName() {
		return authDisplayName;
	}
	public void setAuthDisplayName(String authDisplayName) {
		this.authDisplayName = authDisplayName;
	}
	public String getAuthObjectType() {
		return authObjectType;
	}
	public void setAuthObjectType(String authObjectType) {
		this.authObjectType = authObjectType;
	}
	public String getAuthObjectTypeName() {
		return authObjectTypeName;
	}
	public void setAuthObjectTypeName(String authObjectTypeName) {
		this.authObjectTypeName = authObjectTypeName;
	}
	public String getDomainPath() {
		return domainPath;
	}
	public void setDomainPath(String domainPath) {
		this.domainPath = domainPath;
	}
	public String getDomainPathDisplayName() {
		return domainPathDisplayName;
	}
	public void setDomainPathDisplayName(String domainPathDisplayName) {
		this.domainPathDisplayName = domainPathDisplayName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@JsonIgnore
	public WTGroup getGroup() {
		return this.authGroup.getGroup();
	}
	
	
	
}
