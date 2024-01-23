package com.e3ps.stagegate.util;

import java.util.List;
import java.util.Map;

import com.e3ps.common.content.service.CommonContentHelper;
import com.e3ps.common.util.StringUtil;
import com.e3ps.stagegate.SGObjectValue;

import wt.content.ContentHolder;

public class StageGateUtil {
	
	public static boolean isSubTitle(String objType) {
		boolean result = false;
		
		switch (objType) {
		case "QUALITY":
			result = true;
			break;
		case "DS":
			result = true;
			break;
		case "REVIEWS":
			result = true;
			break;
		default:
			result = false;
			break;
		}
		
		return result;
		
	}
	
	public static String getChartColor(String code) {
		String result = "";
		
		switch (code) {
		case "CHART1":
			result = "#ED0000";
			break;
		case "CHART2":
			result = "#0100FF";
			break;
		case "CHART3":
			result = "#1DDB16";
			break;
		case "CHART4":
			result = "#FFBB00";
			break;
		case "CHART5":
			result = "#5F00FF";
			break;
		default:
			result = "#ED0000";
			break;
		}
		
		return result;
		
	}

	public static void attachFile(Map<String, Object> reqMap, SGObjectValue objValue) throws Exception {
		String primary = StringUtil.checkNull((String) reqMap.get("PRIMARY"));
		List<String> secondary = StringUtil.checkReplaceArray(reqMap.get("SECONDARY"));
		List<String> delocIds		= StringUtil.checkReplaceArray(reqMap.get("delocIds"));
		
		//attach files
		CommonContentHelper.service.attach((ContentHolder)objValue, primary, secondary);
		
		//주첨부파일 드래그 변경으로 인해 첨부파일 관련 함수 추가
//		if(secondary.size() > 0) {
//			CommonContentHelper.service.delete(objValue, ContentRoleType.SECONDARY);
//			for (int i = 0; i < secondary.size(); i++) {
//				String secondCacheId = secondary.get(i).split("/")[0];
//				String fileName = secondary.get(i).split("/")[1];
//				CachedContentDescriptor cacheDs = new CachedContentDescriptor(secondCacheId);
//				CommonContentHelper.service.attach(objValue, cacheDs, fileName, "", ContentRoleType.SECONDARY);
//		    }
//			if(delocIds.size() > 0) {
//				for (int i = 0; i < delocIds.size(); i++) {
//					ApplicationData appData = (ApplicationData) CommonUtil.getObject(delocIds.get(i));
//					if(ContentRoleType.SECONDARY.equals(appData.getRole())) {
//						CommonContentHelper.service.attach(objValue, appData, false);
//					}
//				}
//			}
//		}else {
//			if(delocIds.size() > 0) {
//				CommonContentHelper.service.delete(objValue, ContentRoleType.SECONDARY);
//				for (int i = 0; i < delocIds.size(); i++) {
//					ApplicationData appData = (ApplicationData) CommonUtil.getObject(delocIds.get(i));
//					if(ContentRoleType.SECONDARY.equals(appData.getRole())) {
//						CommonContentHelper.service.attach(objValue, appData, false);
//					}
//				}
//			}
//		}
	}
//	public static String objectType(Persistable object) {
//		String result = "";
//		if(object instanceof SGSummary) {
//			result = "SGSummary";
//		}else if(object instanceof SGQuality) {
//			result = "SGQuality";
//		}else if(object instanceof SGCStop) {
//			result = "SGCStop";
//		}else if(object instanceof SGRisk) {
//			result = "SGRisk";
//		}else if(object instanceof SGDeliverable) {
//			result = "SGDeliverable";
//		}else if(object instanceof SGReview) {
//			result = "SGReview";
//		}else if(object instanceof SGProductDev) {
//			result = "SGProductDev";
//		}
//		return result;
//	}
}
