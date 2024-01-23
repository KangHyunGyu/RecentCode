package com.e3ps.common.log4j;

public enum Log4jPackages {
	
	ADMIN("log_admin"),
	APPROVAL("log_approval"),
	BENCHMARKING("log_benchmarking"),
	CHANGE("log_change"),
	COMMON("log_common"),
	DISTRIBUTE("log_distribute"),
	DOC("log_doc"),
	EPM("log_epm"),
	ERP("log_erp"),
	GROUPWARE("log_groupware"),
	LISTENER("log_listener"),
	LOAD("log_load"),
	OLDCAR("log_oldcar"),
	ORG("log_org"),
	PART("log_part"),
	PORTAL("log_portal"),
	PROJECT("log_project"),
	QUEUE("log_queue"),
	SCHEDULE("log_schedule"),
	STATISTICS("log_part"),
	WORKSPACE("log_workspace")
	;
	
	private final String value;
	
	Log4jPackages(String value){
		this.value = value;
	}
	
	public String getName() {
		return value;
	}
	
}
