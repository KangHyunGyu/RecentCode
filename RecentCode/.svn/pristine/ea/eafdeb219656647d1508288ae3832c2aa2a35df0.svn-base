package com.e3ps.epm.dnc;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.epm.util.SetCADAutoNumbering;
import com.ptc.windchill.uwgm.proesrv.c11n.DocIdentifier;

public class NumberRuleDNC {

	private DocIdentifier docIdentifier;
	
	public NumberRuleDNC(DocIdentifier docIdentifier) {
		this.docIdentifier = docIdentifier;
		this.execute();
	}
	
	private void execute() {
		
		if(checkAttributeKey(CadAttributeDNC.AN_ENABLE.getKey()) && "true".equals(getAttributeValue(CadAttributeDNC.AN_ENABLE.getKey()))   ) {
			
			String modelingType = "";
			String productSerialNo = "";
			String unitNo = "";
			String extension = "";
			try {
				modelingType = getAttributeValue(CadAttributeDNC.AN_MODELING_TYPE.getKey()).toUpperCase();
				productSerialNo = getAttributeValue(CadAttributeDNC.AN_PRODUCT_SERIALNO.getKey()).toUpperCase();
				unitNo = getAttributeValue(CadAttributeDNC.AN_UNIT_NUMBER.getKey()).toUpperCase();
				extension = docIdentifier.getModelName().lastIndexOf(".") > 0 ? CommonUtil.getExtension(docIdentifier.getModelName()) : "";
			}catch(NullPointerException npe) {
				System.out.println(docIdentifier.getDocNumber());
				System.out.println(docIdentifier.getDocName());
				System.out.println(docIdentifier.getModelName());
				npe.printStackTrace();
				throw npe;
			}
			
			
			
			String finalNumber = "";
			if(modelingType.indexOf("TOTAL ASSEMBLY") > -1 ) {
				finalNumber = productSerialNo;
			}else if(modelingType.indexOf("UNIT") > -1) {
				finalNumber = productSerialNo + "-" + unitNo;
			}else {
				String modelingCharacter = "";
				if(modelingType.indexOf("ASSY") > -1) {
					modelingCharacter = "A";
				}else if(modelingType.indexOf("가공품") > -1) {
					modelingCharacter = "M";
				}else if(modelingType.indexOf("판금품") > -1) {
					modelingCharacter = "P";
				}
				
				String numberFormat = productSerialNo + "-" + unitNo + "-" + modelingCharacter;
				
				try {
					finalNumber = numberFormat + SetCADAutoNumbering.getInstance().getSequence(numberFormat, "000");
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
			
			if(finalNumber.length() > 0) {
				
				if(extension.length() > 0) {
					finalNumber += "."+extension;
				}
				
				docIdentifier.setDocNumber(finalNumber);
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private void consolePrint() {
		
		System.out.println("===================");
		
		System.out.println("모델 네임 : "+docIdentifier.getModelName());
		System.out.println("문서 네임 : "+docIdentifier.getDocName());
		System.out.println("문서 번호 : "+docIdentifier.getDocNumber());
		
		
		if(docIdentifier.getParameters() == null) {
			System.out.println("PARAM IS NULL");
		}else {
			Map hashMap = docIdentifier.getParameters();
			System.out.println("HASH SIZE : "+hashMap.size());
			
			Set set = hashMap.keySet();
			Iterator iter = set.iterator();
			
			while(iter.hasNext()) {
				Object obj = iter.next();
				
				String key = obj.toString();
				String value = hashMap.get(obj).toString();
				System.out.println("KEY : "+key);
				System.out.println("VALUE : "+value);
				System.out.println("DT : "+value.getClass().getSimpleName());
				
			}
			
		}
		
		System.out.println("=====LOG END=====");
	}
	
	
	private boolean checkAttributeKey(String key) {
		return docIdentifier.getParameters().containsKey(key);
	}
	
	private String getAttributeValue(String key) {
		
		if(checkAttributeKey(key)) {
			return docIdentifier.getParameters().get(key).toString();
		}else {
			return null;
		}
	}
	
}
