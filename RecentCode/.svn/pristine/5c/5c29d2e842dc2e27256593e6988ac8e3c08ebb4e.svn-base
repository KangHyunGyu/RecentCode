package com.e3ps.epm.dnc;

public enum CadAttributeDNC {

	AN_ENABLE("PLM_autoNumberingEnable"),//자동 채번 여부, Parameter -> iProperties
	AN_PRODUCT_SERIALNO("PLM_productSerialNo"),//장비 시리얼 번호, iProperties
	AN_MODELING_TYPE("PLM_modelingType"),//도면 구분
	AN_UNIT_NUMBER("PLM_unitNo"),//유닛 번호
	
	ASSEMBLE_TYPE("PLM_assembleType"),//조립 타입(1. 일반, 2. 구매품, 3. 분리불가)
	
	ATT_MATERIAL("PLM_material"),//재질
	ATT_TREATMENT("PLM_treatment"),//후처리
	ATT_CATEGORY("PLM_category"),//분류
	
	NULL(null);
	
	private final String key;
	
	private CadAttributeDNC(final String key){
		this.key = (String)key;
	}
	
	public String getKey() {
		return this.key;
	}
	
}
