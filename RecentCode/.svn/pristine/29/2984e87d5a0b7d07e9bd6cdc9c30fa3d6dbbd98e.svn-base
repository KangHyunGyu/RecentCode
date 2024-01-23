<%@page import="com.e3ps.doc.bean.DocData"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
  $(document).ready(function () {
// 	  searchCustomerSR("#customerNameSR");
// 	  searchUserSR("#managerSR");
  });

//   function searchCustomerSR() {
// 	  var searchCustomerSR = $("#customerNameSR");
	  
// 	  var width = searchCustomerSR.data("width");
// 	  var type = searchCustomerSR.data("type");

// 	  if (width == null) {
// 	    width = "100%";
// 	  }

// 	  var url = getURLString("/common/searchCustomerListAction");

// 	  searchCustomerSR.select2({
// 	    width: width,
// 	    ajax: {
// 	      type: "POST",
// 	      url: url,
// 	      allowClear: true,
// 	      dataType: "JSON",
// 	      contentType: "application/json; charset=UTF-8",
// 	      data: function (params) {
// 	        var param = new Object();

// 	        param["name"] = params.term;
// 	        if (type) param["type"] = type;

// 	        param = JSON.stringify(param);
// 	        return param;
// 	      },
// 	      processResults: function (data) {
// 	        return {
// 	          results: $.map(data.list, function (item) {
// 	            var name = item.name;
// 	            var display = item.displayOptionTag;
// 	            return {
// 	              id: name,
// 	              text: display,
// 	            };
// 	          }),
// 	        };
// 	      },
// 	      cache: true,
// 	    },
// 	    placeholder: "Insert user Info (ID or NAME)",
// 	    minimumInputLength: 1,
// 	    templateResult: function (item) {
// 	      return item.text;
// 	    },
// 	    templateSelection: function (item) {
// 	      return item.text;
// 	    },
// 	    escapeMarkup: function (m) {
// 	      return m;
// 	    },
// 	  });
// 	}
  
//   function searchUserSR() {
// 	  var searchUserSR = $("#managerSR");

// 	  var width = searchUserSR.data("width");
// 	  var type = searchUserSR.data("type");

// 	  if (width == null) {
// 	    width = "100%";
// 	  }

// 	  var url = getURLString("/common/searchUserAction");

// 	  searchUserSR.select2({
// 	    width: width,
// 	    ajax: {
// 	      type: "POST",
// 	      url: url,
// 	      allowClear: true,
// 	      dataType: "JSON",
// 	      contentType: "application/json; charset=UTF-8",
// 	      data: function (params) {
// 	        var param = new Object();

// 	        param["name"] = params.term;
// 	        if (type) param["type"] = type;

// 	        param = JSON.stringify(param);
// 	        return param;
// 	      },
// 	      processResults: function (data) {
// 	        return {
// 	          results: $.map(data.list, function (item) {
// 	            var oid = item.oid;
// 	            var name = item.name;
// 	            var erpid = item.erpid;
// 	            var returnText = name;
// 	            var departmentName = item.departmentName;
// 	            if (type == "erp" && erpid != null) {
// 	              returnText = name + "[" + departmentName + "](" + erpid + ")";
// 	            } else {
// 	              returnText = name + "[" + departmentName + "]";
// 	            }

// 	            if (searchUserSR.attr("id") == "managerSR") oid = item.id;
// 	            return {
// 	              id: oid,
// 	              text: returnText,
// 	            };
// 	          }),
// 	        };
// 	      },
// 	      cache: true,
// 	    },
// 	    placeholder: "Insert user Info (ID or NAME)",
// 	    minimumInputLength: 1,
// 	    templateResult: function (item) {
// 	      return item.text;
// 	    },
// 	    templateSelection: function (item) {
// 	      return item.text;
// 	    },
// 	    escapeMarkup: function (m) {
// 	      return m;
// 	    },
// 	  });
// 	}

  //구분
  function selectedDivisionSR(element) {
   var divisionSR = $("input[name=divisionSR]:checked").val();
  }
  //용도
  function selectedUsageSR(element) {
   var usageSR = $("input[name=usageSR]:checked").val();
  }
  
  function etcEventHandle(element){
		
		const elementName = element.getAttribute('name');
		
		Array.prototype.slice.call(document.querySelectorAll('label')).forEach((item) => {
			let labelChildArray = Array.prototype.slice.call(item.childNodes)
			let textExist = labelChildArray.some((l)=>l.nodeName='INPUT' && l.type == 'text'  && l.name == elementName )
			labelChildArray.forEach((x)=> {
				if(x.nodeName=='INPUT' && x.type == 'radio' && x.name == elementName && textExist){
					element = x;
				}
			})
		})
		
		let textNode = document.querySelector('input[name='+elementName+'][type=text]');
		
		if(element.checked){
			textNode.disabled = false;
		}else{
			textNode.disabled = true;
		}
		
	}
  
</script>
<br />
<div class="seach_arm2 pb5">
  <div class="leftbt">
    <span class="title">
				<img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
			${title}
		</span>
  </div>
  <div class="rightbt"></div>
</div>
<!-- pro_table -->
<div class="pro_table">
	<table class="mainTable">
		<colgroup>
			<col style="width: 20%" />
			<col style="width: 30%" />
			<col style="width: 20%" />
			<col style="width: 30%" />
		</colgroup>
		<tbody>
			<tr>
				<th>${e3ps:getMessage('거래처명')}</th>
				<td><input type="text" class="w50" id="customerNameSR" name="customerNameSR">
<!-- 					<div class="pro_view"> -->
<!-- 						<select id="customerNameSR" name="customerNameSR" data-width="70%"></select> -->
<!-- 						<span class="pointer verticalMiddle" onclick="javascript:deleteUser('customerNameSR');"> -->
<!-- 							<img class="verticalMiddle" src="/Windchill/jsp/portal/images/delete_icon.png" /> -->
<!-- 						</span> -->
<!-- 					</div> -->
				</td>
				<th>${e3ps:getMessage('담당자')}</th>
				<td>
					<div class="pro_view"> 
						<select class="searchUser" id="managerSR" name="managerSR" data-width="70%" data-valuetoname="true"></select> 
						<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('managerSR', 'single');">
							<img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png" />
						</span>
						<span class="pointer verticalMiddle" onclick="javascript:deleteUser('managerSR');">
							<img class="verticalMiddle" src="/Windchill/jsp/portal/images/delete_icon.png" />
						</span>
					</div>
				</td>
			</tr>

			<tr>
				<th>${e3ps:getMessage('PartNumber')}</th>
				<td>
					<input type="text" class="w50" id="partNumberSR" name="partNumberSR">
				</td>
				<th>${e3ps:getMessage('품명')}</th>
				<td>
					<input type="text" class="w50" id="productNameSR" name="productNameSR">
				</td>
			</tr>

			<tr>
				<th>${e3ps:getMessage('도번')}</th>
				<td>
					<input type="text" class="w50" id="drawNumberSR" name="drawNumberSR">
				</td>
				<th>${e3ps:getMessage('BarcodeNumber')}</th>
				<td>
					<input type="text" class="w50" id="barcodeNumberSR" name="barcodeNumberSR">
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('MarkingNumber')}</th>
				<td>
					<input type="text" class="w50" id="markNumberSR" name="markNumberSR">
				</td>
				<th>${e3ps:getMessage('수량')}</th>
				<td>
					<input type="text" class="w50" id="quanSR" name="quanSR">
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('구분')}</th>
				<td>
					<input type="radio" id="paid" name="divisionSR" value="paid" onchange="selectedDivisionSR(this)" checked /> 
					<label for="paid">${e3ps:getMessage('유상')}</label>
					<input type="radio" id="free" name="divisionSR" value="free" onchange="selectedDivisionSR(this)" /> 
					<label for="free">${e3ps:getMessage('무상')}</label>
				</td>
				<th>${e3ps:getMessage('용도')}</th>
				<td>
					<input type="radio" id="custReq" name="usageSR" value="custReq" onchange="selectedUsageSR(this)" onclick="etcEventHandle(this)" checked /> 
					<label for="custReq">${e3ps:getMessage('고객요청')}</label>
					<input type="radio" id="selfDefect" name="usageSR" value="selfDefect" onchange="selectedUsageSR(this)" onclick="etcEventHandle(this)"/> 
					<label for="selfDefect">${e3ps:getMessage('자사불량')}</label>
					<input type="radio" id="rework" name="usageSR" value="rework" onchange="selectedUsageSR(this)" onclick="etcEventHandle(this)"/> 
					<label for="rework">${e3ps:getMessage('RE')}</label>
					<label for="usageEtc">
						<input type="radio" id="usageEtc" name="usageSR" value="" onchange="selectedUsageSR(this)" onclick="etcEventHandle(this)"/>
						${e3ps:getMessage('기타')} ( <input type="text" id="usageSR" name="usageSR" class="w20" disabled /> )
					</label>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('설명')}</th>
				<td class="pd15" colspan="3">
					<div class="textarea_autoSize">
						<textarea name="descriptionSR" id="descriptionSR"
							onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
