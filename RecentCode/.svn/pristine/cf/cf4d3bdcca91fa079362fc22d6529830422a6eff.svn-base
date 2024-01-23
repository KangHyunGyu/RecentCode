/**
 * 
 */
package com.e3ps.common.favorite.service;

import java.util.Map;

import com.e3ps.common.favorite.Favorite;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;

import wt.fc.PersistenceHelper;
import wt.pom.Transaction;
import wt.services.StandardManager;
import wt.session.SessionHelper;

@SuppressWarnings("serial")
public class StandardFavoriteService extends StandardManager implements FavoriteService{
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	public static StandardFavoriteService newStandardFavoriteService() throws Exception{
		final StandardFavoriteService instance = new StandardFavoriteService();
		instance.initialize();
		return instance;
	}

	@Override
	public String createFavoriteAction(Map<String, Object> reqMap) throws Exception{
		Transaction trx = new Transaction();
		String oid = "";
		try {
			trx.start();
			
			String url = StringUtil.checkNull((String) reqMap.get("url"));
			String name = StringUtil.checkNull((String) reqMap.get("name")); 
			
			Favorite fav = Favorite.newFavorite();
			fav.setUrl(url);
			fav.setName(name);
			fav.setOwner(SessionHelper.manager.getPrincipalReference());
			fav = (Favorite)PersistenceHelper.manager.save(fav);
			oid = CommonUtil.getOIDString(fav);
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return oid;
	}

	@Override
	public void deleteFavoriteAction(Map<String, Object> reqMap) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			Favorite fav = (Favorite) CommonUtil.getObject(oid);
			PersistenceHelper.manager.delete(fav);
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}
	
	
}
