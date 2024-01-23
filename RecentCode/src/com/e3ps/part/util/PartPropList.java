package com.e3ps.part.util;

import java.util.AbstractMap;
import java.util.Map;

//문서 유형에 따른 IBA 속성 키 리스트 뽑아오는 Enum 클래스
//사용 예제
//DocTypePropList obj = Stream.of(DocTypePropList.values()).filter(h -> h.getDocTypeCode().equals("D001")).findFirst().orElseThrow(() -> new Exception("문서 유형 코드가 존재하지 않음"));
//Map<String,String> propMap = obj.getProps();
//propMap.forEach( (jspName, ibaKey) -> { IBAUtil.changeIBAValue(part, ibaKey, StringUtil.checkNull((String)reqMap.get(jspName)), "string") });
public enum PartPropList {
	
	PART_CREATION_DATA(
				Map.ofEntries(
				  new AbstractMap.SimpleEntry<String, String>("Default","/Default/Drawing_Part"),		
				  new AbstractMap.SimpleEntry<String, String>("A","/Default/Drawing_Part/1.ASS'Y"),
				  new AbstractMap.SimpleEntry<String, String>("B","/Default/Drawing_Part/2.Phantom"),
				  new AbstractMap.SimpleEntry<String, String>("P","/Default/Drawing_Part/3.제품"),
				  new AbstractMap.SimpleEntry<String, String>("H","/Default/Drawing_Part/4.반제품"),
				  new AbstractMap.SimpleEntry<String, String>("U","/Default/Drawing_Part/5.구매품"),
				  new AbstractMap.SimpleEntry<String, String>("R","/Default/Drawing_Part/6.R＆D"),
				  new AbstractMap.SimpleEntry<String, String>("M","/Default/Drawing_Part/7.원자재"),
				  new AbstractMap.SimpleEntry<String, String>("L","/Default/Drawing_Part/8.원료"),
				  new AbstractMap.SimpleEntry<String, String>("K","/Default/Drawing_Part/9.첨가제"),
				  new AbstractMap.SimpleEntry<String, String>("X","/Default/Drawing_Part/10.기타")
				)
				, Map.ofEntries(
				  new AbstractMap.SimpleEntry<String, String>("p_unit","P_UNIT"),
				  new AbstractMap.SimpleEntry<String, String>("p_material","P_MATERIAL"),
				  new AbstractMap.SimpleEntry<String, String>("p_equipment","P_EQUIPMENT"),
				  new AbstractMap.SimpleEntry<String, String>("p_product","P_PRODUCT"),
				  new AbstractMap.SimpleEntry<String, String>("p_inch","P_INCH"),
				  new AbstractMap.SimpleEntry<String, String>("approved","APPROVED"),
				  new AbstractMap.SimpleEntry<String, String>("checked1","CHECKED1"),
				  new AbstractMap.SimpleEntry<String, String>("checked2","CHECKED2"),
				  new AbstractMap.SimpleEntry<String, String>("customer","CUSTOMER"),
				  new AbstractMap.SimpleEntry<String, String>("customer_no","CUSTOMER_NO"),
				  new AbstractMap.SimpleEntry<String, String>("date","DATE"),
				  new AbstractMap.SimpleEntry<String, String>("draw_no","DRAW_NO"),
				  new AbstractMap.SimpleEntry<String, String>("drawn","DRAWN"),
				  new AbstractMap.SimpleEntry<String, String>("equipment","EQUIPMENT"),
				  new AbstractMap.SimpleEntry<String, String>("material","MATERIAL"),
				  new AbstractMap.SimpleEntry<String, String>("oem_part_no","OEM_PART_NO"),
				  new AbstractMap.SimpleEntry<String, String>("old_pn","OLD_PN"),
				  new AbstractMap.SimpleEntry<String, String>("resistivity","RESISTIVITY"),
				  new AbstractMap.SimpleEntry<String, String>("surfaces","SURFACES"),
				  new AbstractMap.SimpleEntry<String, String>("title","TITLE"),
				  new AbstractMap.SimpleEntry<String, String>("external_diameter","EXTERNAL_DIAMETER"),
				  new AbstractMap.SimpleEntry<String, String>("internal_diameter","INTERNAL_DIAMETER"),
				  new AbstractMap.SimpleEntry<String, String>("thickness","THICKNESS"),
				  new AbstractMap.SimpleEntry<String, String>("hall_dia","HALL_DIA"),
				  new AbstractMap.SimpleEntry<String, String>("hall_count","HALL_COUNT"),
				  new AbstractMap.SimpleEntry<String, String>("remark","REMARK"),
				  new AbstractMap.SimpleEntry<String, String>("description","DESCRIPTION"),
				  new AbstractMap.SimpleEntry<String, String>("migData","MIGDATA")
				  
				)
			);
	
	private final Map<String,String> locations;
	private final Map<String,String> props;
	
	private PartPropList(final Map<String,String> locations, final Map<String,String> props){
		this.props = props;
		this.locations = locations;
	}
	
	public Map<String,String> getLocations() {
		return locations;
	}

	public Map<String,String> getProps() {
		return this.props;
	}
	
}
