package com.e3ps.admin.bean;

import java.util.List;
import java.util.Locale;

import com.e3ps.admin.EsolutionMenu;
import com.e3ps.common.code.NumberCode;
import com.e3ps.common.code.bean.NumberCodeData;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import wt.org.WTGroup;
import wt.session.SessionHelper;
import wt.util.WTException;

public class EsolutionMenuData {
	
	private String oid;
	private String code;
	private String name;
	private String name_en;
	private String displayName;
	private String href;
	private String imgsrc;
	private boolean active_yn;
	private boolean disabled;
	private int sort;
	private int menuLevel;
	private String parentCode;
	private String parentName;
	private String groupOid;
	private String moduleName;
	
	private List<EsolutionMenuData> children;
	
	public EsolutionMenuData(EsolutionMenu eMenu) throws WTException {
		this.oid = eMenu.getPersistInfo().getObjectIdentifier().getStringValue();
		this.code = StringUtil.checkNull(eMenu.getCode());
		this.name = StringUtil.checkNull(eMenu.getName());
		this.name_en = StringUtil.checkNull(eMenu.getName_en());
		this.displayName = getDisplayLanguageName(eMenu);
		this.href = StringUtil.checkNull(eMenu.getHref());
		this.imgsrc = StringUtil.checkNull(eMenu.getImgsrc());
		this.disabled = eMenu.isDisabled();
		this.active_yn = !eMenu.isDisabled();
		this.sort = eMenu.getSort();
		this.menuLevel = eMenu.getMenuLevel();
		EsolutionMenu parent = eMenu.getParent();
		this.parentCode = parent==null?"":parent.getCode();
		this.parentName = parent==null?"":parent.getName();
		WTGroup group = eMenu.getGroup();
		this.groupOid = group==null?"":group.getPersistInfo().getObjectIdentifier().getStringValue();
		this.moduleName = eMenu.getAlias();
	}
	
	@JsonIgnore
	private String getDisplayLanguageName(EsolutionMenu eMenu) throws WTException {
		String value = eMenu.getName();
		String locale = SessionHelper.manager.getLocale().toString();
		if(!Locale.KOREA.toString().contains(locale)){
			value = eMenu.getName_en();
			if(!StringUtil.checkString(value)){
				value = eMenu.getName();
			}
		}
		return value;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName_en() {
		return name_en;
	}

	public void setName_en(String name_en) {
		this.name_en = name_en;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getImgsrc() {
		return imgsrc;
	}

	public void setImgsrc(String imgsrc) {
		this.imgsrc = imgsrc;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(int menuLevel) {
		this.menuLevel = menuLevel;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public List<EsolutionMenuData> getChildren() {
		return children;
	}

	public void setChildren(List<EsolutionMenuData> children) {
		this.children = children;
	}

	public boolean isActive_yn() {
		return active_yn;
	}

	public void setActive_yn(boolean active_yn) {
		this.active_yn = active_yn;
	}

	public String getGroupOid() {
		return groupOid;
	}

	public void setGroupOid(String groupOid) {
		this.groupOid = groupOid;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	
}
