package com.e3ps.part.bomLoader.service;


import com.e3ps.common.log4j.Log4jPackages;

import wt.fc.PersistenceHelper;
import wt.services.ServiceFactory;

public class BomLoaderHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PART.getName());
	public static final BomLoaderService service = ServiceFactory.getService(BomLoaderService.class);
	
	public static final BomLoaderHelper manager = new BomLoaderHelper();
	
	public boolean checkStoredLength(String value, int length, boolean flag) {
		
		try {
			return PersistenceHelper.checkStoredLength(value, length, flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	public boolean isPartValidate(String number) {
		
		if(number.trim().length() == 0) {
    		return false;
    	}
    	
		//J, R : 프로젝트
    	if(number.startsWith("J") || number.startsWith("R")) {
			if(number.length() == 10) {
				return true;
			}
    	}
    	
    	//T : 사용안함 SUB SYSTEM 
    	/*if(number.startsWith("T")) {
			if(number.length() == 12) {
				return true;
			}
    	}*/
    	
    	// 사용 안함 팀 코드
    	/*if(number.startsWith("S") || number.startsWith("D") || number.startsWith("E")) {
    		if(number.length() == 10 || number.length() == 12) {
				return true;
			}
    	} else if( number.startsWith("M") || number.startsWith("U")) {
    		//REVISION 번호 여부에 따라 16~18자리(REVISION 번호는 2자리)
    		
			if(number.length() == 16 || number.length() == 18) {
				return true;
			}
		}*/
    	
    	if(number.startsWith("M") || number.startsWith("U")) {
    		//REVISION 번호 여부에 따라 15~17자리(REVISION 번호는 2자리) 팀코드(2번째자리 삭제되어서 기존 16~18에서 15~17로 변경)
    		
			if(number.length() == 15 || number.length() == 17) {
				return true;
			}
		}
    	
		if(number.startsWith("A") || number.startsWith("P")) {
			//Ass'y와 Part는 9~13자리
			//REVISION 번호 : 2자리
			//Repair 번호 : (구)revision번호((현)00:2자리) + repair(2자리)
			
			if(number.length() == 9 || number.length() == 11 || number.length() == 13) {
				return true;
			}
		}
				
    	return false;
	}
	
	public boolean isBomValidate(String parent, String child) {
    	
		if((parent.startsWith("J") || parent.startsWith("R"))
				&& (child.startsWith("M"))) {
			
			String p = "";
			String e = "";
			
			p = parent.substring(2);
			e = child.substring(6,14);
			
			return e.equals(p);
		} else if(parent.startsWith("M") && child.startsWith("U")) {
			String p = "";
			String e = "";
			
			//Project 번호 일치 여부 확인.
			p = parent.substring(6,14);
			e = child.substring(6,14);
			
			if(!(e.equals(p))) {
				return false;
			}
			
			//PM번호 일치 여부 확인
			p = parent.substring(14,15);
			e = child.substring(14,15);
    				
			if(!(e.equals(p))) {
				return false;
			}
			
			//Sub Code 체크
			if((parent.length() == 17)) {
				if(child.length() != 17) {
					return false;
				}
				
				p = parent.substring(15);
    			e = child.substring(15);
    			
    			if(!(e.equals(p))) {
    				return false;
    			}
    			
    			return true;
			} else {
				if(child.length() == 15) {
					return true;
				}
			}
			
			return e.equals(p);
		} else if(parent.startsWith("U") && (child.startsWith("A") || child.startsWith("P"))) {
			return true;
		} else if(parent.startsWith("A") && (child.startsWith("A") || child.startsWith("P"))) {
			return true;
		}
		
    	/*if((parent.startsWith("J") || parent.startsWith("R"))
    			&& (child.startsWith("S") || child.startsWith("D") || child.startsWith("E"))) {
    		
    		String p = "";
			String e = "";
			
			//Project 번호 일치 여부 확인.
			p = parent.substring(2);
			e = child.substring(2);
			
    		return e.equals(p);
    	} else if((parent.startsWith("J") || parent.startsWith("R"))
    			&& (child.startsWith("T"))){
    		
    		String p = "";
			String e = "";
			
			//Project 번호 일치 여부 확인.
			p = parent.substring(2);
			e = child.substring(2,10);
			
    		return e.equals(p);
    	} else if((parent.startsWith("T"))
    			&& (child.startsWith("S") || child.startsWith("D") || child.startsWith("E"))) {
    		
    		String p = "";
			String e = "";
			
			//Project 번호 일치 여부 확인.
			p = parent.substring(2,10);
			e = child.substring(2,10);
			
			if(!(e.equals(p))) {
				return false;
			}
			
			//Sub Code 체크
			p = parent.substring(10);
			e = child.substring(10);
			
    		return e.equals(p);
    	} else if((parent.startsWith("S") || parent.startsWith("D") || parent.startsWith("E"))
    			&& child.startsWith("M") ) {
    		
    		String p = "";
			String e = "";
			
			//Project 번호 일치 여부 확인.
			p = parent.substring(2,10);
			e = child.substring(7,15);
			
			if(!(e.equals(p))) {
				return false;
			}
			
			//Team 구분번호 일치 여부 확인(PLM에서만 확인)
			p = parent.substring(1,2);
			e = child.substring(1,2);
				
			if(!(e.equals(p))) {
				return false;
			}
			
			//Sub Code 체크
			if((parent.length() == 12)) {
				if(child.length() != 18) {
					return false;
				}
				
				p = parent.substring(10);
    			e = child.substring(16);
    			
    			if(!(e.equals(p))) {
    				return false;
    			}
    			
    			return true;
			} else {
				if(child.length() == 16) {
					return true;
				}
			}
    	} else if( parent.startsWith("M") && child.startsWith("U") ) {
    		
    		String p = "";
			String e = "";
			
			//Project 번호 일치 여부 확인.
			p = parent.substring(7,15);
			e = child.substring(7,15);
			
			if(!(e.equals(p))) {
				return false;
			}
			
			//Team 구분번호 일치 여부 확인(PLM에서만 확인)
			p = parent.substring(1,2);
			e = child.substring(1,2);
				
			if(!(e.equals(p))) {
				return false;
			}
			
			//PM번호 일치 여부 확인
			p = parent.substring(15,16);
			e = child.substring(15,16);
    				
			if(!(e.equals(p))) {
				return false;
			}
			
			//Sub Code 체크
			if((parent.length() == 18)) {
				if(child.length() != 18) {
					return false;
				}
				
				p = parent.substring(16);
    			e = child.substring(16);
    			
    			if(!(e.equals(p))) {
    				return false;
    			}
    			
    			return true;
			} else {
				if(child.length() == 16) {
					return true;
				}
			}
		}
    	
		if(parent.startsWith("U") && (child.startsWith("A") || child.startsWith("P"))) {
			return true;
		} else if( parent.startsWith("A") && (child.startsWith("A") || child.startsWith("P"))) {
			return true;
		}*/
    	
    	return false;
    }
}
