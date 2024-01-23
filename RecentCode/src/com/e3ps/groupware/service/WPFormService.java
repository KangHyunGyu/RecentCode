package com.e3ps.groupware.service;

import com.e3ps.groupware.workprocess.WorkProcessForm;

import wt.method.RemoteInterface;

@RemoteInterface
public interface WPFormService {
	
	/**
	 * WorkProcessForm객체를 받아서 수신처에 메일을 발송하는 메소드
	 * @param form WorkProcessForm
	 */
	void sendMail(WorkProcessForm form);
	
}
