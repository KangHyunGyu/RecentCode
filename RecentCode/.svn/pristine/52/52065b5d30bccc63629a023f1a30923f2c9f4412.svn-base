package com.e3ps.groupware.service;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.e3ps.groupware.favorite.FavoritePage;

import wt.fc.QueryResult;
import wt.method.RemoteInterface;
import wt.util.WTException;

@RemoteInterface
public interface FavoriteService {
	
	public static final Logger logger = LoggerFactory.getLogger(FavoriteService.class);
	
	FavoritePage createFavoritePage(ConcurrentHashMap hash) throws WTException;
	
	FavoritePage deleteFavoritePage(ConcurrentHashMap hash) throws WTException ;
	
	QueryResult searchMyFavorite() throws WTException;
}
