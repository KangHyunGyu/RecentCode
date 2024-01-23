package com.e3ps.common.history.service;

import java.util.Map;

import wt.method.RemoteInterface;

@RemoteInterface
public interface HistoryService {

	 public abstract String createDownloadHistory(Map<String, String> map) throws Exception;

	void createLoginHistory(String id, String browser, String ip);

}
