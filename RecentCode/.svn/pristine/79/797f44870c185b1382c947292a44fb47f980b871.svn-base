package com.e3ps.load;

import com.duruan.shadowcube.SecureStreamCreator;
import com.duruan.shadowcube.SecureStreamException;
import com.duruan.shadowcube.SecureStreamReader;

public class Test {
	public static void main(String[] args)
	  { 
	    // Step 1. ShadowCube login 정보 설정(초기화) 과정
	    SecureStreamCreator.Init("100334", "qwe123!@#", "D:/certi");
	    // serial: 인증서 번호
	    // passphrase: 인증서 비밀번호
	    // home: 인증서 파일 경로 (null 인 경우에는 ShadowCube 홈폴더가 자동으로 지정됩니다.)
	    //  - ShadowCube 홈폴더  
	    //     XP : C:\Documents and Settings\{사용자계정이름}\Local Settings\Application Data\ShadowCube
	    //     Vista 이상 : C:\Users\{사용자계정이름}\AppData\Local\ShadowCube
	    //     (Service 하위로 생성된 프로세스가 아닌 곳에서 CSDL_LOCAL_APPDATA 등의 방법으로 해당경로를 생성할 수 있습니다.)

	    String srcFileName = "D:/testFile/NEW DRAW_NODRM.dwg";
	    String encFileName = "D:/testFile/NEW DRAW_DRM.dwg";
	    //String decFileName = "D:/testFile/testMi__.xls";

	    try {
	      // Step 2. 암호화
	      SecureStreamReader ssr = new SecureStreamCreator().CreateEncryptReader(srcFileName, 0, 0);
	      if (ssr != null) {
	    	  ssr.ReadAllToFile(encFileName, null);
	        ssr.Close();
	      }
	      
	      /*
	      //복
	      SecureStreamReader ss = new SecureStreamCreator().CreateDecryptReader("D:/testFile/drmokfile.xls");
	      if(ss != null) {
	    	  ss.ReadAllToFile("D:/testFile/drmnookfile.xls", null);
	    	  ss.close();
	      }
	      */
	      
	    } catch (SecureStreamException sse)  {
	    	System.out.println(sse.getMessage());
	    	sse.printStackTrace();
	    } catch (Exception e) {
	    	System.out.println(e.getMessage());
	    	e.printStackTrace();
	    }
	     
	  }
}
