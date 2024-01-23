package com.e3ps.distribute.util;

import org.joda.time.DateTime;

import com.e3ps.common.util.SequenceDao;

public class DistributeUtil {
	/**
	 * 
	 * @desc : 도면출도의뢰서 번호 채번
	 * @author : shjeong
	 * @date : 2023. 07. 20.
	 * @method : createNumber
	 * @param order_no
	 * @return
	 * @throws Exception String
	 */
	public static String createDistributeNumber() throws Exception {
		
		DateTime dt = DateTime.now();
		String seqName = String.format("DIST-%d-", dt.getYear());
		String serial = SequenceDao.manager.getSeqNo(seqName, "0000", "DistributeDocument", "distributeNumber");
		return seqName + serial;
	}
	
//	/**
//	 * 
//	 * @desc : 배포요청 번호 채번
//	 * @author : shjeong
//	 * @date : 2023. 07. 20.
//	 * @method : createDistributeRegNumber
//	 * @param order_no
//	 * @return
//	 * @throws Exception String
//	 */
//	public static String createDistributeRegNumber() throws Exception {
//		
//		DateTime dt = DateTime.now();
//		String seqName = String.format("DISTIBUTE-%d-", dt.getYear());
//		String serial = SequenceDao.manager.getSeqNo(seqName, "0000", "DistributeDocument", "distributeNumber");
//		return seqName + serial;
//	}
}
