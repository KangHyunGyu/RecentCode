package com.e3ps.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.e3ps.common.log4j.Log4jPackages;

import wt.lifecycle.LifeCycleHelper;
import wt.lifecycle.LifeCycleTemplate;
import wt.lifecycle.PhaseTemplate;
import wt.lifecycle.State;
import wt.util.WTException;

public class WFItemUtil {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	/**
	 * <pre>
	 * &#64;description 
	 * &#64;author dhkim
	 * &#64;date 2016. 7. 13. 오전 10:15:39
	 * &#64;method getLifeCycleStateList
	 * &#64;param lifeCycle
	 * &#64;return List<Map<String,String>>
	 * &#64;throws WTException
	 * </pre>
	 */
	public static List<Map<String, String>> getLifeCycleStateList(String lifeCycle) throws WTException {
		List<String> filterState = new ArrayList<String>();
		return getLifeCycleStateList(lifeCycle, filterState);
	}

	/**
	 * <pre>
	 * &#64;description 
	 * &#64;author dhkim
	 * &#64;date 2016. 7. 13. 오전 10:15:41
	 * &#64;method getLifeCycleStateList
	 * &#64;param lifeCycle
	 * &#64;param filterStateStr
	 * &#64;return List<Map<String,String>> 
	 * &#64;throws WTException
	 * </pre>
	 */
	public static List<Map<String, String>> getLifeCycleStateList(String lifeCycle, String filterStateStr) throws WTException {
		List<String> filterState = StringUtil.checkReplaceList(filterStateStr);
		return getLifeCycleStateList(lifeCycle, filterState);
	}

	/**
	 * <pre>
	 * &#64;description 
	 * &#64;author dhkim
	 * &#64;date 2016. 7. 13. 오전 10:15:44
	 * &#64;method getLifeCycleStateList
	 * &#64;param lifeCycle
	 * &#64;param filterState
	 * &#64;return List<Map<String,String>>
	 * &#64;throws WTException
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getLifeCycleStateList(String lifeCycle, List<String> filterState) throws WTException {

		List<Map<String, String>> stateList = new ArrayList<Map<String, String>>();

		try {

			LifeCycleTemplate lct = LifeCycleHelper.service.getLifeCycleTemplate(lifeCycle, WCUtil.getSiteContainer().getContainerReference());
			Vector<PhaseTemplate> states = new Vector<PhaseTemplate>();
			if(lct != null) {
				states = LifeCycleHelper.service.getPhaseTemplates(lct);
			}
			PhaseTemplate pt = null;
			State lcState = null;
			for (int i = 0; i < states.size(); i++) {

				pt = (PhaseTemplate) states.get(i);

				lcState = pt.getPhaseState();

				Map<String, String> map = new HashMap<String, String>();

				map.put("key", lcState.toString());
				map.put("value", lcState.getDisplay());

				if (filterState.contains(lcState.toString())) {
					continue;
				}

				stateList.add(map);
			}

		} catch (Exception e) {
			throw new WTException(e.getLocalizedMessage());
		}

		return stateList;
	}
}
