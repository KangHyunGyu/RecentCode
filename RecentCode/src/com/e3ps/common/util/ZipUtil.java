package com.e3ps.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;

import org.springframework.web.util.CookieGenerator;

import com.e3ps.common.content.service.CommonContentHelper;

import wt.content.ApplicationData;
import wt.content.ContentHolder;
import wt.content.ContentRoleType;
import wt.content.ContentServerHelper;
import wt.epm.EPMDocument;
import wt.fc.PersistenceHelper;

public class ZipUtil {

	public static void eventListener(Object obj, String event) {

		// System.out.println(obj +" event =" + event);
		if (event.equals("PUBLISH_SUCCESSFUL") && obj instanceof EPMDocument) {

			EPMDocument epm = (EPMDocument) obj;

			boolean isInventor = epm.getAuthoringApplication().toString().equals("INVENTOR");
			boolean isDrawing = epm.getDocType().toString().equals("CADDRAWING");

			System.out.println("isInventor =" + isInventor + ",isDrawing =" + isDrawing + "," + epm.getNumber());
			if (isInventor && isDrawing) {
				publishZipFileSplit(epm);
			}
		}

	} 

	/**
	 * Inventor Drawing 파일 Zip인 경우 분할 등록
	 * 
	 * @param epm
	 */
	public static void publishZipFileSplit(EPMDocument epm) {

		try {

			boolean isDrawing = epm.getDocType().toString().equals("CADDRAWING");

			/*
			 * List<ApplicationData> list = EpmHelper.manager.getDrawingPublishFile(epm);
			 * System.out.println("list =" + list.size()); ApplicationData appData = null;
			 * for(ApplicationData app : list) {
			 * 
			 * String role = app.getRole().toString(); String fileName = app.getFileName();
			 * 
			 * boolean isDWG = isDrawing && role.equals("ADDITIONAL_FILES") &&
			 * fileName.toLowerCase().indexOf("zip") >= 0;
			 * 
			 * System.out.println("publishZipFileSplit isDrawing =" + isDrawing);
			 * System.out.println("publishZipFileSplit role =" +
			 * role.equals("ADDITIONAL_FILES"));
			 * System.out.println("publishZipFileSplit indexOf =" +
			 * (fileName.toLowerCase().indexOf("zip") >= 0) );
			 * System.out.println("publishZipFileSplit isDWG =" + isDWG); if(isDWG) {
			 * appData = app; break; } }
			 */
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void decompress(File zipFile, String directory) throws Exception {

		FileInputStream fis = null;
		ZipInputStream zis = null;
		ZipEntry zipentry = null;

		try {
			// 파일 스트림
			// fis =
			// (FileInputStream)FileUtil.findContentStream(appData);//EpmHelper.service.getApplicationDataStream(appData);//(FileInputStream)
			// ContentServerHelper.service.findContentStream(appData);
			fis = new FileInputStream(zipFile);
			System.out.println("fis = " + fis);
			// Zip 파일 스트림
			zis = new ZipInputStream(fis);
			// entry가 없을때까지 뽑기
			File folder = new File(directory);
			if (folder.isDirectory()) {
				folder.mkdirs();
			}

			System.out.println("decompress zipFile = " + zis.getNextEntry());
			while ((zipentry = zis.getNextEntry()) != null) {
				String filename = zipentry.getName();
				System.out.println("decompress zipFile = " + filename);
				File file = new File(directory, filename);
				// entiry가 폴더면 폴더 생성
				// 파일이면 파일 만들기
				createFile(file, zis);
				/*
				 * if (zipentry.isDirectory()) { file.mkdirs(); } else { //파일이면 파일 만들기
				 * createFile(file, zis); }
				 */
			}

		} catch (Exception e) {
			throw e;
		} finally {
			if (zis != null)
				zis.close();
			if (fis != null)
				fis.close();

		}
		System.out.println("decompress zipFile END");

	}

	public static void decompress(String zipFileName, String directory) throws Exception {
		File zipFile = new File(zipFileName);
		FileInputStream fis = null;
		ZipInputStream zis = null;
		ZipEntry zipentry = null;
		System.out.println("decompress zipFileName START");
		try {
			// 파일 스트림
			fis = new FileInputStream(zipFile);
			// Zip 파일 스트림
			zis = new ZipInputStream(fis);
			// entry가 없을때까지 뽑기
			while ((zipentry = zis.getNextEntry()) != null) {
				String filename = zipentry.getName();
				filename = filename.replace("시트", "sheet");
				File file = new File(directory, filename);

				System.out.println("decompress zipFileName filename =" + filename);
				// entiry가 폴더면 폴더 생성
				if (zipentry.isDirectory()) {
					file.mkdirs();
				} else {
					// 파일이면 파일 만들기
					createFile(file, zis);
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (zis != null)
				zis.close();
			if (fis != null)
				fis.close();
		}
		System.out.println("decompress zipFileName END");
	}

	private static void createFile(File file, ZipInputStream zis) throws Exception {
		// 디렉토리 확인
		File parentDir = new File(file.getParent());
		// 디렉토리가 없으면 생성하자
		if (!parentDir.exists()) {
			parentDir.mkdirs();
		}

		System.out.println("createFile filename =" + file.getName());
		// 파일 스트림 선언
		try (FileOutputStream fos = new FileOutputStream(file)) {
			byte[] buffer = new byte[256];
			int size = 0;
			// Zip스트림으로부터 byte뽑아내기
			while ((size = zis.read(buffer)) > 0) {
				// byte로 파일 만들기
				fos.write(buffer, 0, size);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private static List<File> getFileList(String compressUrl) {

		File folder = new File(compressUrl);

		File[] fileList = folder.listFiles();
		List<File> list = new ArrayList<File>();
		for (File file : fileList) {

			String fileName = file.getName();

			if (fileName.indexOf("zip") > 0) {
				continue;
			}

			System.out.println("fileName =" + fileName);
			list.add(file);
		}

		return list;

	}

	public static void main(String[] args) {
		try {
			System.out.println("START Main");
			String fileUrl = "D:\\ptc\\Windchill_11.2\\Windchill\\temp\\zipFile\\dwg_2006-0RD10-013_dwg.zip\\1618820558797\\dwg_2006-0RD10-013_dwg.zip";

			File zipFile = new File(fileUrl);
			decompress(fileUrl, "D:\\E3PS\\tsuam\\cc");

			getFileList("D:\\E3PS\\tsuam\\cc");

			System.out.println("START END");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/** * 
	 * @description 압축 메소드
	 * @param path 압축할 폴더 경로 
	 * @param outputFileName 출력파일명 
	 * */ 
	
	public static boolean compress(String path, String outputPath, String outputFileName) throws Throwable { 
		// 파일 압축 성공 여부 
		boolean isChk = false; 
		File file = new File(path); // 파일의 .zip이 없는 경우, .zip 을 붙여준다. 
		int pos = outputFileName.lastIndexOf(".") == -1 ? outputFileName.length() : outputFileName.lastIndexOf("."); 
		
		// outputFileName .zip이 없는 경우 
		if (!outputFileName.substring(pos).equalsIgnoreCase(".zip")) { 
			outputFileName += ".zip"; 
		} 
		// 압축 경로 체크 
		if (!file.exists()) { 
			throw new Exception("Not File!"); 
		} 
		// 출력 스트림 
		FileOutputStream fos = null; 
		
		// 압축 스트림 
		ZipOutputStream zos = null; 
		
		try { 
			fos = new FileOutputStream(new File(outputPath + outputFileName)); 
			zos = new ZipOutputStream(fos); 
			// 디렉토리 검색를 통한 하위 파일과 폴더 검색 
			searchDirectory(file, file.getPath(), zos); 
			// 압축 성공. 
			isChk = true; 
		} catch (Throwable e) { 
			throw e; 
		} finally { 
			if (zos != null) zos.close(); 
			if (fos != null) fos.close(); 
		} return isChk; 
	}
	
	/** * 
	 * @description 디렉토리 탐색 
	 * @param file 현재 파일 
	 * @param root 루트 경로 
	 * @param zos 압축 스트림 
	 * */ 
	private static void searchDirectory(File file, String root, ZipOutputStream zos) throws Exception { 
		// 지정된 파일이 디렉토리인지 파일인지 검색 
		if (file.isDirectory()) { 
			// 디렉토리일 경우 재탐색(재귀) 
			File[] files = file.listFiles(); 
			for (File f : files) { 
				System.out.println("file = > " + f); 
				searchDirectory(f, root, zos); 
			} 
		} else { 
			// 파일일 경우 압축을 한다. 
			try { 
				compressZip(file, root, zos); 
			} catch (Throwable e) { 
				// TODO Auto-generated catch block 
				e.printStackTrace(); 
			} 
		} 
	}
	
	/** 
	 * @description압축 메소드 
	 * @param file 
	 * @param root 
	 * @param zos 
	 * @throws Throwable 
	 * */
	private static  void compressZip(File file, String root, ZipOutputStream zos) throws Throwable { 
		FileInputStream fis = null; 
		try { 
			String zipName = file.getPath().replace(root + "\\", ""); 
			// 파일을 읽어드림 
			fis = new FileInputStream(file); 
			// Zip엔트리 생성(한글 깨짐 버그) 
			ZipEntry zipentry = new ZipEntry(zipName); 
			// 스트림에 밀어넣기(자동 오픈) 
			zos.putNextEntry(zipentry); 
			int length = (int) file.length(); 
			byte[] buffer = new byte[length]; 
			// 스트림 읽어드리기 
			fis.read(buffer, 0, length); 
			// 스트림 작성 
			zos.write(buffer, 0, length); 
			// 스트림 닫기 
			zos.closeEntry(); 
		} catch (Throwable e) {
			throw e; 
		} finally { 
			if (fis != null) fis.close(); 
		} 
	}
}
