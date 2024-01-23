package com.e3ps.groupware.service;

import java.util.concurrent.ConcurrentHashMap;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.org.WTUser;
import wt.pom.Transaction;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.StandardManager;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.web.ParamUtil;
import com.e3ps.groupware.favorite.FavoritePage;

public class StandardFavoriteService extends StandardManager implements FavoriteService {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.GROUPWARE.getName());
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default factory for the class.
	 * @return
	 * @throws WTException
	 */
	public static StandardFavoriteService newStandardFavoriteService() throws WTException {
		StandardFavoriteService instance = new StandardFavoriteService();
		instance.initialize();
		return instance;
	}


	@Override
	public FavoritePage createFavoritePage(ConcurrentHashMap hash) throws WTException {
		FavoritePage page = null;

		Transaction trx = new Transaction();
		try {
			trx.start();

			String name = ParamUtil.get(hash, "name");
			String url = ParamUtil.get(hash, "url");
			page = FavoritePage.newFavoritePage();
			page.setName(name);
			page.setUrl(url);
			page.setOwner(SessionHelper.manager.getPrincipalReference());
			page = (FavoritePage)PersistenceHelper.manager.save(page);
			
			trx.commit();
			trx = null;
		} catch (WTPropertyVetoException e) {
			logger.error("ERROR", e);
			throw new WTException(e.getLocalizedMessage());
		} catch (WTException e) {
			logger.error("ERROR", e);
			throw new WTException(e.getLocalizedMessage());
		} catch (Exception e) {
			logger.error("ERROR", e);
			throw new WTException(e.getLocalizedMessage());
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return page;
	}

	@Override
	public FavoritePage deleteFavoritePage(ConcurrentHashMap hash)  throws WTException {
		FavoritePage page = null;

		Transaction trx = new Transaction();
		try {
			trx.start();

			String oid = ParamUtil.get(hash, "oid");
			
			ReferenceFactory rf = new ReferenceFactory();
			page = (FavoritePage)rf.getReference(oid).getObject();
			page = (FavoritePage)PersistenceHelper.manager.delete(page);
			
			trx.commit();
			trx = null;
			
		} catch (WTException e) {
			logger.error("ERROR", e);
			throw new WTException(e.getLocalizedMessage());
		} catch (Exception e) {
			logger.error("ERROR", e);
			throw new WTException(e.getLocalizedMessage());
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return page;
	}

	@Override
	public QueryResult searchMyFavorite() throws WTException {
		WTUser user = (WTUser)SessionHelper.manager.getPrincipal();
		QuerySpec qs = new QuerySpec(FavoritePage.class);
		qs.appendWhere(new SearchCondition(FavoritePage.class,"owner.key.id","=",user.getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
		qs.appendOrderBy(new OrderBy(new ClassAttribute(FavoritePage.class,"thePersistInfo.createStamp"),false),new int[]{0});
		return PersistenceHelper.manager.find(qs);
	}
}
