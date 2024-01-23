package com.e3ps.common.util;

import java.util.Comparator;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.part.bean.BomTreeData;
import com.e3ps.part.bean.BomTreeExcelData;

import wt.part.WTPart;


public class ObjectSort implements Comparator{
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	public ObjectSort(){
		
		
	}
	@Override
	public int compare(Object o1, Object o2) {
		
		int result = 0;
		if(o1 instanceof BomTreeData){
			
			BomTreeData data1 =(BomTreeData)o1;
			BomTreeData data2 =(BomTreeData)o2;
			
			String number1 = data1.getNumber();
			String number2 = data2.getNumber();
			result = number1.compareTo(number2);
		
		}else if(o1 instanceof WTPart){
			
			WTPart supart1 = (WTPart)o1;
			WTPart supart2 = (WTPart)o2;
			
			String number1 = supart1.getNumber();
			String number2 = supart2.getNumber();
			result = number1.compareTo(number2);
						
		}else if(o1 instanceof BomTreeExcelData){
			
			BomTreeExcelData supart1 = (BomTreeExcelData)o1;
			BomTreeExcelData supart2 = (BomTreeExcelData)o2;
			
			String number1 = supart1.getNumber();
			String number2 = supart2.getNumber();
			result = number1.compareTo(number2);
						
		}
		
		return result;
	}

}
