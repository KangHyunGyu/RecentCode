/**
 * 
 */
package com.e3ps.common.excelDown.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.e3ps.common.excelDown.service.ExcelDownHelper;
import com.e3ps.common.log4j.Log4jPackages;

@Controller
@RequestMapping("/excel")
public class ExcelDownController {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	/**
	 * @desc	: 문서 엑셀 다운로드
	 * @author	: mnyu
	 * @date	: 2019. 11. 21.
	 * @method	: excelDownDoc
	 * @return void
	 */
	@RequestMapping("/excelDownDoc")
	public void excelDownDoc(HttpServletRequest request, HttpServletResponse response) {
		try {
			ExcelDownHelper.service.excelDownDoc(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @desc	: 도면 엑셀 다운로드
	 * @author	: mnyu
	 * @date	: 2019. 11. 21.
	 * @method	: excelDownEpm
	 * @return void
	 */
	@RequestMapping("/excelDownEpm")
	public void excelDownEpm(HttpServletRequest request, HttpServletResponse response) {
		try {
			ExcelDownHelper.service.excelDownEpm(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping("/excelDownEpmPartState")
	public void excelDownEpmPartState(HttpServletRequest request, HttpServletResponse response) {
		try {
			ExcelDownHelper.service.excelDownEpmPartState(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @desc	: 부품 엑셀 다운로드
	 * @author	: mnyu
	 * @date	: 2019. 11. 22.
	 * @method	: excelDownPart
	 * @return void
	 */
	@RequestMapping("/excelDownPart")
	public void excelDownPart(HttpServletRequest request, HttpServletResponse response) {
		try {
			ExcelDownHelper.service.excelDownPart(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @desc	: 배포 엑셀 다운로드
	 * @author	: mnyu
	 * @date	: 2019. 11. 22.
	 * @method	: excelDownDistribute
	 * @return void
	 */
	@RequestMapping("/excelDownDistribute")
	public void excelDownDistribute(HttpServletRequest request, HttpServletResponse response) {
		try {
			ExcelDownHelper.service.excelDownDistribute(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
//	/**
//	 * @desc	: 접수 엑셀 다운로드
//	 * @author	: mnyu
//	 * @date	: 2019. 11. 25.
//	 * @method	: excelDownReceipt
//	 * @return void
//	 */
//	@RequestMapping("/excelDownReceipt")
//	public void excelDownReceipt(ReceiptData rec, HttpServletRequest request, HttpServletResponse response) {
//		try {
//			ExcelDownHelper.service.excelDownReceipt(rec, request, response);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
	/**
	 * @desc	: 업체 목록 엑셀 다운로드
	 * @author	: mnyu
	 * @date	: 2019. 11. 25.
	 * @method	: excelDownSupplier
	 * @return void
	 */
	@RequestMapping("/excelDownSupplier")
	public void excelDownSupplier(HttpServletRequest request, HttpServletResponse response) {
		try {
			ExcelDownHelper.service.excelDownSupplier(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @desc	: 배포 대상 item List 엑셀 다운로드
	 * @author	: mnyu
	 * @date	: 2020. 1. 3.
	 * @method	: excelDownDistributeItem
	 * @return void
	 */
	@RequestMapping("/excelDownDistributeItem")
	public void excelDownDistributeItem(HttpServletRequest request, HttpServletResponse response) {
		try {
			//ExcelDownHelper.service.excelDownDistributeItem(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @desc	: 배포 부품 엑셀 다운로드
	 * @author	: mnyu
	 * @date	: 2020. 1. 7.
	 * @method	: excelDownDistributePart
	 * @return void
	 */
	@RequestMapping("/excelDownDistributePart")
	public void excelDownDistributePart(HttpServletRequest request, HttpServletResponse response) {
		try {
			ExcelDownHelper.service.excelDownDistributePart(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @desc	: 정규 배포 엑셀 다운로드
	 * @author	: mnyu
	 * @date	: 2020. 1. 15.
	 * @method	: excelDownRegularDistribute
	 * @return void
	 */
	@RequestMapping("/excelDownRegularDistribute")
	public void excelDownRegularDistribute(HttpServletRequest request, HttpServletResponse response) {
		try {
			ExcelDownHelper.service.excelDownRegularDistribute(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @desc	: NumberCode 엑셀 다운로드
	 * @author	: mnyu
	 * @date	: 2020. 2. 21.
	 * @method	: excelDownNumberCode
	 * @return	: void
	 * @param request
	 * @param response
	 */
	@RequestMapping("/excelDownNumberCode")
	public void excelDownNumberCode(HttpServletRequest request, HttpServletResponse response) {
		try {
			ExcelDownHelper.service.excelDownNumberCode(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @desc	: ECR 엑셀 다운로드
	 * @author	: tsjeong
	 * @date	: 2020. 10. 07.
	 * @method	: excelDownECR
	 * @return	: void
	 * @param request
	 * @param response
	 */
	@RequestMapping("/excelDownECR")
	public void excelDownECR(HttpServletRequest req, HttpServletResponse res) {
		try {
			ExcelDownHelper.service.excelDownECR(req, res);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @desc	: ECO 엑셀 다운로드
	 * @author	: tsjeong
	 * @date	: 2020. 10. 07.
	 * @method	: excelDownECO
	 * @return	: void
	 * @param request
	 * @param response
	 */
	@RequestMapping("/excelDownECO")
	public void excelDownECO(HttpServletRequest req, HttpServletResponse res) {
		try {
			ExcelDownHelper.service.excelDownECO(req, res);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @desc	: Proejct 엑셀 다운로드
	 * @author	: tsjeong
	 * @date	: 2020. 10. 07.
	 * @method	: excelDownProject
	 * @return	: void
	 * @param request
	 * @param response
	 */
	@RequestMapping("/excelDownProject")
	public void excelDownProject(HttpServletRequest req, HttpServletResponse res) {
		try {
			ExcelDownHelper.service.excelDownProject(req, res);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @desc	: user 엑셀 다운로드
	 * @author	: tsjeong
	 * @date	: 2020. 10. 07.
	 * @method	: excelDownUser
	 * @return	: void
	 * @param request
	 * @param response
	 */
	@RequestMapping("/excelDownUser")
	public void excelDownUser(HttpServletRequest req, HttpServletResponse res) {
		try {
			ExcelDownHelper.service.excelDownUser(req, res);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @desc	: 엑셀 업로드 템플릿 다운로드
	 * @author	: shkim
	 * @date	: 2020. 10. 28.
	 * @method	: excelDownEpmUploadTemplate
	 * @param   : req, res
	 * @return  : void
	 */
	@RequestMapping("/excelDownEpmUploadTemplate")
	public void excelDownEpmUploadTemplate(HttpServletRequest req, HttpServletResponse res) {
		try {
			ExcelDownHelper.service.excelDownEpmUploadTemplate(req, res);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @desc	: 엑셀 간트 Template Download
	 * @author	: tsjeong
	 * @date	: 2020. 12. 04.
	 * @method	: excelDownEpmUploadTemplate
	 * @param   : req, res
	 * @return  : void
	 */
	@RequestMapping("/excelDownGantt")
	public void excelDownTemplate(HttpServletRequest req, HttpServletResponse res) {
		try {
			ExcelDownHelper.service.excelDownTemplate(req, res);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 프로젝트 산출물 리스트 엑셀 Export
	 * @author hckim
	 * @date 2021. 01. 18
	 * @param req
	 * @param res
	 */
	@RequestMapping("/excelDownProjectOutput")
	public void excelDownProjectOutput(HttpServletRequest req, HttpServletResponse res) {
		try {
			ExcelDownHelper.service.excelDownProjectOutput(req, res);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 엑셀 간트 Excel Download
	 * @author hckim
	 * @date 2021. 01. 27
	 * @param req
	 * @param res
	 */
	@RequestMapping("/excelDownGanttExcel")
	public void excelDownGanttExcel(HttpServletRequest req, HttpServletResponse res) {
		try {
//			String[] arrayTemp = req.getParameterValues("excelProjectList");
//			System.out.println(Arrays.toString(arrayTemp));
			
			ExcelDownHelper.service.excelDownGanttExcel(req, res);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * WBS 엑셀 Import /Export 기능 
	 * @author gs
	 * @date 2021. 05. 24
	 * @param req
	 * @param res
	 */
	@RequestMapping("/excelDownGanttExcel2")
	public void excelDownGanttExcel2(HttpServletRequest req, HttpServletResponse res) {
		try {
//			String[] arrayTemp = req.getParameterValues("excelProjectList");
//			System.out.println(Arrays.toString(arrayTemp));
			
			ExcelDownHelper.service.excelDownGanttExcel2(req, res);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
