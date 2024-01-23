/**
 * 
 */
package com.e3ps.common.favorite.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.e3ps.common.favorite.Favorite;
import com.e3ps.common.favorite.bean.FavoriteData;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.org.WTUser;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.ServiceFactory;
import wt.session.SessionHelper;

public class FavoriteHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	public static final FavoriteService service = ServiceFactory.getService(FavoriteService.class);
	public static final FavoriteHelper manager = new FavoriteHelper();
	
	/**
	 * @desc	: 즐겨찾기 여부
	 * @author	: mnyu
	 * @date	: 2019. 12. 16.
	 * @method	: isFavorite
	 * @return	: void
	 * @param reqMap
	 */
	public String isFavorite(Map<String, Object> reqMap) throws Exception{
		String oid = "";
		String url = StringUtil.checkNull((String) reqMap.get("url"));
		
		QuerySpec qs = new QuerySpec();
		qs.appendClassList(Favorite.class, true);
		
		qs.appendWhere(new SearchCondition(Favorite.class, Favorite.URL, "=", url), new int[]{ 0 });
		qs.appendAnd();
		
		WTUser user = (WTUser)SessionHelper.manager.getPrincipal();
		qs.appendWhere(new SearchCondition(Favorite.class, "owner.key.id", "=", CommonUtil.getOIDLongValue(user)), new int[]{ 0 });
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		if(qr.hasMoreElements()){
			Object[] obj = (Object[]) qr.nextElement();
			oid = CommonUtil.getOIDString((Favorite)obj[0]);
		}
		
		return oid;
	}

	/**
	 * @desc	: 즐겨찾기 목록
	 * @author	: mnyu
	 * @date	: 2019. 12. 17.
	 * @method	: getFavoriteList
	 * @return	: List<FavoriteData>
	 * @return
	 * @throws Exception 
	 */
	public List<FavoriteData> getFavoriteList() throws Exception {
		List<FavoriteData> list = new ArrayList<FavoriteData>();
		
		QuerySpec qs = new QuerySpec();
		qs.appendClassList(Favorite.class, true);
		WTUser user = (WTUser)SessionHelper.manager.getPrincipal();
		qs.appendWhere(new SearchCondition(Favorite.class, "owner.key.id", "=", CommonUtil.getOIDLongValue(user)), new int[] {0});
		
		qs.appendOrderBy(new OrderBy(new ClassAttribute(Favorite.class, "thePersistInfo.createStamp"), true), new int[] { 0 });
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		while(qr.hasMoreElements()){
			Object[] obj = (Object[]) qr.nextElement();
			FavoriteData data = new FavoriteData((Favorite)obj[0]);
			list.add(data);
		}
		return list;
	}
}
