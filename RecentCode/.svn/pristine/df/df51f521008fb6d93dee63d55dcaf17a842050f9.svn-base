/**
 * 
 */
package com.e3ps.common.favorite.bean;

import com.e3ps.common.favorite.Favorite;
import com.e3ps.common.util.CommonUtil;

public class FavoriteData {
	private String url;
	private String name;
	private String oid;
	
	public FavoriteData(String oid) throws Exception {
		Favorite fav = (Favorite) CommonUtil.getObject(oid);
		
		_FavoriteData(fav);
	}
	
	public FavoriteData(Favorite fav) throws Exception {
		
		_FavoriteData(fav);
	}
	
	public void _FavoriteData(Favorite fav) throws Exception {
		this.url = fav.getUrl();
		this.name = fav.getName();
		this.oid = CommonUtil.getOIDString(fav);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}
}
