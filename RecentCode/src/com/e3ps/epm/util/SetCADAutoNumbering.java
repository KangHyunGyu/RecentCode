package com.e3ps.epm.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.e3ps.common.util.SequenceDao;

/**
 * @author hckim
 *
 */
public class SetCADAutoNumbering {

	private static SetCADAutoNumbering instance;

	// 가져온 파트넘버 체계 값을 Key로 갖습니다.
	private static Map<String, String> numberingHash;

	private SetCADAutoNumbering() {
		numberingHash = new HashMap<>();
	}

	public static SetCADAutoNumbering getInstance() {

		if (instance == null) {
			instance = new SetCADAutoNumbering();
		}
		return instance;
	}

	public void consolePrint() {
		for (Entry<String, String> entry : numberingHash.entrySet()) {
			System.out.println("KEY :: " + entry.getKey());
			System.out.println("VALUE :: " + entry.getValue());
		}
	}
	
	public void modifyHash(String key, String value) {
		if(numberingHash.containsKey(key)) {
			numberingHash.put(key, value);
		}
	}

	/**
	 * 파트 넘버 체계에서 사용 가능한 format값을 받습니다.
	 * 
	 * @param number ex) format이 제외된 번호(ASM-AAA-)
	 * @param format ex) ###
	 */
	public String getSequence(String number, String format) throws Exception {

		String returnSequence = null;

		if (number == null || format == null) {
			throw new Exception("자동채번을 위한 조건이 불충분합니다.");
		}

		String numberFormat = number + format;

		// 마지막으로 사용된 넘버
		if (numberingHash.containsKey(numberFormat)) {

			String lastNumber = numberingHash.get(numberFormat);
			if (lastNumber == null) {
				registNumberSequence(number, format);
				//getSequence(number, format);
			}

			String newVal = String.format("%0" + format.length() + "d", Integer.parseInt(lastNumber) + 1);
			numberingHash.put(numberFormat, newVal);

			returnSequence = newVal;

			// 서버 부팅이후 처음 사용된 넘버
		} else {

			registNumberSequence(number, format);
			returnSequence = numberingHash.get(numberFormat);
		}

		return returnSequence;
	}

	public void registNumberSequence(String number, String format) throws Exception {
		String seq = SequenceDao.manager.getSeqNo(number, format, "EPMDocumentMaster", "documentNumber");
		numberingHash.put(number + format, seq);
	}
}