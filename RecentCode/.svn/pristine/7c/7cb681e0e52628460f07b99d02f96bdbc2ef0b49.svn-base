/**
 * jqGrid에서 사용할 페이저를 정의한다
 * @param gridRenderTo
 * @param totalSize
 * @param currentPage
 * @param sessionid
 * @param labels totalPageLabel, totalCountLabel, listSizeLabel 속성의 라벨 객체
 */
function initPaginate(gridRenderTo, totalSize, currentPage, sessionid, labels) {
	// #. 국제화를 위한 라벨 적용
	var totalPageLabel = "Total Page";
	var totalCountLabel = "Total Count";
	var listSizeLabel = "/ Count";
	if (labels != null) {
		if (labels.totalPageLabel != null && labels.totalPageLabel.length > 0) {
			totalPageLabel = labels.totalPageLabel;
		}
		if (labels.totalCountLabel != null && labels.totalCountLabel.length > 0) {
			totalCountLabel = labels.totalCountLabel;
		}
		if (labels.listSizeLabel != null && labels.listSizeLabel.length > 0) {
			listSizeLabel = labels.listSizeLabel;
		}
	}
	
	if(currentPage==""){
	   var currentPage = $('#'+gridRenderTo).getGridParam('page');
	}
	var pageCount = 10;
	var totalPage = Math.ceil(totalSize/$('#'+gridRenderTo).getGridParam('rowNum'));
	if (isNaN(totalPage)) {
		totalPage = 0;
	}
	
	var totalPageList = Math.ceil(totalPage/pageCount);
	var pageList=Math.ceil(currentPage/pageCount);
	if(pageList<1) pageList=1;
	if(pageList>totalPageList) pageList = totalPageList;
	var startPageList=((pageList-1)*pageCount)+1;
	var endPageList=startPageList+pageCount-1;
	if(startPageList<1) startPageList=1;
	if(endPageList>totalPage) endPageList=totalPage;
	if(endPageList<1) endPageList=1;
	var pageInner="<table border='0' cellspacing='1' cellpadding='2' width='100%' align='center' bgcolor='white'><tr bgcolor='white'>";
	pageInner += "<td class='small' width='300'><span class='small'>[" + totalPageLabel + ":"+totalPage+"][" + totalCountLabel + ":"+totalSize+"]</span></td>";
	pageInner +="  <td>";
	pageInner +="		<table border='0' align='center' cellpadding='0' cellspacing='0'  bgcolor='white'>";
	pageInner +="			<tr bgcolor='white'>";
	pageInner +="				<td width='30' align='center'>";
	if(pageList<2){
		pageInner+=" <img src='/Windchill/jsp/project/images/BBS_start.gif'></td>";
		pageInner+=" <td width='1' bgcolor='#dddddd'></td>";
		pageInner+=" <td width='30' class='quick' align='center'><img src='/Windchill/jsp/project/images/BBS_prev.gif'></td>";
		pageInner+=" <td width='1' bgcolor='#dddddd'></td>";
		
	}
	if(pageList>1){
		pageInner+="<a class='first' href=\"javascript:firstPage('" + gridRenderTo + "', "+sessionid+");\"><img src='/Windchill/jsp/project/images/BBS_start.gif'></a>";
		pageInner+="</td>";
		pageInner+="				<td width='1' bgcolor='#dddddd'></td>";
		pageInner+="				<td width='30' class='quick' align='center'><a class='pre' href=\"javascript:prePage('" + gridRenderTo + "', "+totalSize+","+sessionid+");\" class='smallblue'><img src='/Windchill/jsp/project/images/BBS_prev.gif' border='0' align='middle'></a></td>";
		pageInner+="				<td width='1' bgcolor='#dddddd'></td>";
	}
	for(var i=startPageList; i<=endPageList; i++){
		pageInner += "				<td style='padding:2 8 0 7;cursor:pointer' onMouseOver='this.style.background=\"#ECECEC\"' OnMouseOut='this.style.background=\"\"' class='nav_on' OnClick=\"javascript:goPage('" + gridRenderTo + "', '"+(i)+"','"+sessionid+"')\" >";

		if(i==currentPage){
			pageInner += "&nbsp;<b>"+(i)+"</b>&nbsp;";
		}else{
			pageInner += "&nbsp;" + i + "&nbsp;";
		}
		pageInner += "</td>";
	}
	if(totalPageList>pageList){
		pageInner+="				<td width='1' bgcolor='#dddddd'></td>";
		pageInner+="				<td width='30' align='center'><a href=\"javascript:nextPage('" + gridRenderTo + "', "+totalSize+","+sessionid+");\" class='smallblue'><img src='/Windchill/jsp/project/images/BBS_next.gif' border='0' align='middle'></a></td>";
		pageInner+="				<td width='1' bgcolor='#dddddd'></td>";
		pageInner+="				<td width='30'align='center'>";
		pageInner+="<a href=\"javascript:lastPage('" + gridRenderTo + "', "+totalPage+","+sessionid+");\" class='small'><img src='/Windchill/jsp/project/images/BBS_end.gif' border='0' align='middle'></a>";
	}
	if(totalPageList==pageList){
		pageInner+="				<td width='1' bgcolor='#dddddd'></td>";
		pageInner+="				<td width='30' align='center'><img src='/Windchill/jsp/project/images/BBS_next.gif'></td>";
		pageInner+="				<td width='1' bgcolor='#dddddd'></td>";
		pageInner+="				<td width='30'align='center'><img src='/Windchill/jsp/project/images/BBS_end.gif'></a>";
	}
	pageInner +="				</td>";
	pageInner +="			</tr>";
	pageInner +="		</table>";
	pageInner +="  </td>";
	pageInner +=" <td class='small' align='right'  width='200'><select id='pSizeSelect' onchange=\"javascript:selectpSize('" + gridRenderTo + "')\">";
	pageInner +=" <option value='10' >10</option>";
	pageInner +=" <option value='15' >15</option>";
	pageInner +=" <option value='20' >20</option>";
	pageInner +=" <option value='30' >30</option>";
	pageInner +=" <option value='50' >50</option>";
	pageInner +=" <option value='100' >100</option>";
	pageInner +="</select>" + listSizeLabel + "</td>";
	pageInner +="</tr>";
	pageInner +="</table>";
	$("#paginate").html("");
	$("#paginate").append(pageInner);
   
	var rowNum = $('#'+gridRenderTo).getGridParam('rowNum');
	$("option[value="+rowNum+"]").attr('selected','selected');
}

function firstPage(gridRenderTo, sessionid){
	$("#" + gridRenderTo).jqGrid('setGridParam', {
		postData:{
		tpage:1,
		sessionid : sessionid,
		searchChk : "false"
		}
	}).trigger("reloadGrid");
}
//그리드 이전페이지 이동
function prePage(gridRenderTo, totalSize, sessionid){
	var currentPage = parseInt($('#' + gridRenderTo).getGridParam('page'));
	$("#currentPage").val(currentPage);
	var pageCount = 10;
	currentPage-=pageCount;
	pageList=Math.ceil(currentPage/pageCount);
	currentPage=(pageList-1)*pageCount+pageCount;
	initPaginate(gridRenderTo, totalSize, currentPage, sessionid);
	$("#" + gridRenderTo).jqGrid('setGridParam', {
		postData:{
		tpage:currentPage,
		sessionid : sessionid,
		searchChk : "false"
		}
	}).trigger("reloadGrid");

}
//그리드 다음페이지 이동    
function nextPage(gridRenderTo, totalSize, sessionid){
	var currentPage = parseInt($('#' + gridRenderTo).getGridParam('page'));

	$("#currentPage").val(currentPage);
	var pageCount = 10;
	currentPage+=pageCount;
	pageList=Math.ceil(currentPage/pageCount);
	$("#pageList").val(pageList);
	currentPage=(pageList-1)*pageCount+1;
	initPaginate(gridRenderTo, totalSize, currentPage, sessionid);
	$("#" + gridRenderTo).jqGrid('setGridParam', {
		postData:{
		tpage : currentPage,
		sessionid : sessionid,
		searchChk : "false"
		}
	}).trigger("reloadGrid");
}
//그리드 마지막페이지 이동
function lastPage(gridRenderTo, totalSize, sessionid){
	var currentPage = $('#' + gridRenderTo).getGridParam('page');
	$("#currentPage").val(currentPage);
	$("#" + gridRenderTo).jqGrid('setGridParam', {
		postData:{
			tpage : totalSize,
			sessionid : sessionid,
			searchChk : "false"
		}
	}).trigger("reloadGrid");
}
//그리드 페이지 이동
function goPage(gridRenderTo, num, sessionid){
	var currentPage = $('#' + gridRenderTo).getGridParam('page');
	$("#" + gridRenderTo).setGridParam({
		postData:{
			tpage : num,
			sessionid : sessionid,
			searchChk : "false"
	   }
	}).trigger("reloadGrid");
	$("#page").val(num);
}

function selectpSize(gridRenderTo){
	
	var pSize = $("select[id=pSizeSelect]").val();
	
	// #. 폼의 pSize hidden에 값 셋팅
	var pSizeInputs = document.getElementsByName("pSize");
	if (pSizeInputs != null && pSizeInputs.length > 0) {
		pSizeInputs[0].value = pSize;
	}
	
	$("#" + gridRenderTo).jqGrid('setGridParam', {
		rowNum : pSize,
		postData:{
			psize : pSize,
			searchChk : "false",
			tpage : 1
		}
	}).trigger("reloadGrid");
	
	$("option[value="+pSize+"]").attr('selected','selected');
}

/**
 * jqGrid 현재 page에서 해당 column으로 정렬
 * @param divId
 * @param index
 * @param columnIndex
 * @param sortOrder
 */
function handleOnSortColumn(index, columnIndex, sortOrder){
	var currentPage = $(this).getGridParam("page");
	$(this).setGridParam({
		postData:{
			sortValue : index,
			sortCheck : sortOrder,
			searchChk : "true",
			tpage : currentPage
		}
	}).trigger("reloadGrid");
}

/**
 * loadError 핸들러
 * @param xhr
 * @param st
 * @param err
 */
function handleLoadError(xhr, st, err){
	//아무것도 안함
	//jQuery("#rsperror").html("Type: "+ st + "; Response: " + xhr.status + " " + xhr.statusText);
}

/**
 * 페이징을 갱신한다
 * @param gridSelector Grid 셀렉터
 */
function refreshPaginate(gridSelector) {
	// #. paging 갱신
	var totalCount = $(gridSelector).jqGrid('getGridParam','records');
	if (totalCount == null || isNaN(totalCount)) {
		totalCount = 0;
	}
	var userData = $(gridSelector).jqGrid('getGridParam', 'userData');
	var sessionid = 0;
	if (userData != null) {
		sessionid = userData.sessionid;
	}
	var gridRenderTo = $(gridSelector).attr("id");
	initPaginate(gridRenderTo, totalCount, "", sessionid);
}

/**
 * Grid에서 체크되어진 객체를 반환한다
 * @param gridRenderTo Grid가 그려진 DIV 식별자
 * @param gridRows 임시저장된 Grid JSON 데이터
 */
function getCheckedRowsInGrid(gridRenderTo, gridRows) {
	var rows = [];
	var rowIds = $("#" + gridRenderTo).jqGrid("getGridParam", "selarrrow");
	for (var i = 0; i < rowIds.length; i++) {
		var rowId = rowIds[i];
		if (gridRows != null) {
			for (var j = 0; j < gridRows.length; j++) {
				if (gridRows[j].id == rowId) {
					rows.push(gridRows[j]);
					break;
				}
			}
		}
	}
	return rows;
}

/*
 * @param string grid_id 사이즈를 변경할 그리드의 아이디
 * @param string div_id 그리드의 사이즈의 기준을 제시할 div 의 아이디
 */
function addJqGridResizeListener(gridId, divId){
	$(window).bind('resize', function() {
		resizeGrid(gridId, divId);
	}).trigger('resize');
}

/**
 * Grid의 폭을 0으로 좁혔다가 다시 리사이즈한다.
 * 한 페이지에 grid 다 여러개인 경우 한꺼번에 줄여야만 함 !!!
 * @param gridIds grid 의 table id. 여러개인 경우 array를 입력
 * @param divIds grid를 wrapping 하는 div id. 여러개인 경우 array를 입력
 */
function resizeGrid(gridIds, divIds){
	// #. 문자열 인자를 array로 변환
	if (!$.isArray(gridIds)) {
		gridIds = [ gridIds ];
	}
	if (!$.isArray(divIds)) {
		divIds = [ divIds ];
	}
	
	// #. 모든 grid 넓이를 한꺼번에 0으로 초기화
	for (var i = 0; i < gridIds.length; i++) {
		$('#' + gridIds[i]).setGridWidth(0);
	}
	
	// #. 모든 grid의 넓이를 한꺼번에 div 넓이와 동기화
	for (var i = 0; i < gridIds.length; i++) {
		var currentFit = $('#' + gridIds[i]).getGridParam("shrinkToFit");
		$('#' + gridIds[i]).setGridWidth($('#' + divIds[i]).width() , currentFit);
	}
}

/**
 * Tree와 Grid 사이 Spliter 마우스 핸들러 등록
 * @param splitterId 구분자 ID
 * @param treeId Tree ID
 * @param gridTableId 그리드 Table ID
 * @param gridDivId 그리드 Div ID
 */
function addLayoutSplitMouseListener(splitterId, treeId, gridTableId, gridDivId) {
	// document.getElementById(splitterId).onmousedown = function() {
	$("#" + splitterId).mousedown( function () {
		var tdiv = document.getElementById(treeId);
		var position = tdiv.getBoundingClientRect();
		// document.onmousemove = function(e) {
		$(document).mousemove( function (e) {
			e = e || event
			var w = e.pageX-position.left;
			if(w>=200){
				tdiv.style.width = (e.pageX-position.left) +'px';
				resizeGrid(gridTableId, gridDivId);
			}
		});
	});
	// document.onmouseup = function() {
	$(document).mouseup( function () {
		//document.onmousemove = null;
		$(document).off("mousemove");
	});
}

/**
 * checkbox header를 disabled true로 처리
 * checkbox header ID : "cb_" + gridTableId
 * @param gridTableId 그리드 Table ID
 */
function disabledCheckboxHeader(gridTableId) {
	var checkboxHeader = document.getElementById("cb_" + gridTableId);
	checkboxHeader.disabled = true;
}

/**
 * singleSelect가 true인경우 beforeSelectRow 에 css 및 grid reset
 * @param singleSelect
 * @param gridTableId
 */
function handleBeforeSelectRow(singleSelect, gridTableId) {
	$(gridTableId).find(".ui-state-highlight").css("background", "");
	$(gridTableId).find(".ui-state-highlight").css("border", "");
	if (singleSelect) {
		$(gridTableId).jqGrid("resetSelection");
	}
}

/**
 * OnSelectRow 에 css를 설정한다
 * @param gridTableId
 */
function handleOnSelectRow(gridTableId) {
	$(gridTableId).find(".ui-state-highlight").css("background", "#80BFFF");
	$(gridTableId).find(".ui-state-highlight").css("border", "#80BFFF");
}

/**
 * 더블클릭 붙이기
 * @param gridTableId
 * @param listener 리스너 인자는 rowIndex (0~n)
 */
function addDblClickRowListener(gridTableId, listener) {
	var grid = $("#" + gridTableId);
	grid.dblclick(function(e) {
		var td = e.target;
		var ptr = $(td,grid[0].rows).closest("tr.jqgrow");
		if ($(ptr).length === 0 ) {
			return false;
		}
		var ri = ptr[0].rowIndex;
		var ci = $.jgrid.getCellIndex(td);
		var rowId = $(ptr).attr("id");
		//alert("ri=" + ri + ", ci=" + ci + ", rowId=" + rowId);
		
		var dataIndex = ri - 1;
		if (listener != null) {
			listener.call(this, dataIndex);
		}
		
		return false;
	});
}
/**
 * 페이징을 갱신한다
 * @param gridSelector Grid 셀렉터
 */
function refreshPaginates(gridSelector) {
	// #. paging 갱신
	var totalCount = $(gridSelector).jqGrid('getGridParam','records');
	if (totalCount == null || isNaN(totalCount)) {
		totalCount = 0;
	}
	var userData = $(gridSelector).jqGrid('getGridParam', 'userData');
	var sessionid = 0;
	if (userData != null) {
		sessionid = userData.sessionid;
	}
	var gridRenderTo = $(gridSelector).attr("id");
	initPaginates(gridRenderTo, totalCount, "", sessionid);
}
function initPaginates(gridRenderTo, totalSize, currentPage, sessionid, labels) {
	// #. 국제화를 위한 라벨 적용
	var totalPageLabel = "Total Page";
	var totalCountLabel = "Total Count";
	var listSizeLabel = "/ Count";
	if (labels != null) {
		if (labels.totalPageLabel != null && labels.totalPageLabel.length > 0) {
			totalPageLabel = labels.totalPageLabel;
		}
		if (labels.totalCountLabel != null && labels.totalCountLabel.length > 0) {
			totalCountLabel = labels.totalCountLabel;
		}
		if (labels.listSizeLabel != null && labels.listSizeLabel.length > 0) {
			listSizeLabel = labels.listSizeLabel;
		}
	}
	
	if(currentPage==""){
	   var currentPage = $('#'+gridRenderTo).getGridParam('page');
	}
	var pageCount = 10;
	var totalPage = Math.ceil(totalSize/$('#'+gridRenderTo).getGridParam('rowNum'));
	if (isNaN(totalPage)) {
		totalPage = 0;
	}
	
	var totalPageList = Math.ceil(totalPage/pageCount);
	var pageList=Math.ceil(currentPage/pageCount);
	if(pageList<1) pageList=1;
	if(pageList>totalPageList) pageList = totalPageList;
	var startPageList=((pageList-1)*pageCount)+1;
	var endPageList=startPageList+pageCount-1;
	if(startPageList<1) startPageList=1;
	if(endPageList>totalPage) endPageList=totalPage;
	if(endPageList<1) endPageList=1;
	var pageInner="<table border='0' cellspacing='1' cellpadding='2' width='100%' align='center' bgcolor='white'><tr bgcolor='white'>";
	pageInner += "<td class='small' width='300'><span class='small'>[" + totalPageLabel + ":"+totalPage+"][" + totalCountLabel + ":"+totalSize+"]</span></td>";
	pageInner +="  <td>";
	pageInner +="		<table border='0' align='center' cellpadding='0' cellspacing='0'  bgcolor='white'>";
	pageInner +="			<tr bgcolor='white'>";
	pageInner +="				<td width='30' align='center'>";
	if(pageList<2){
		pageInner+=" <img src='/Windchill/jsp/project/images/BBS_start.gif'></td>";
		pageInner+=" <td width='1' bgcolor='#dddddd'></td>";
		pageInner+=" <td width='30' class='quick' align='center'><img src='/Windchill/jsp/project/images/BBS_prev.gif'></td>";
		pageInner+=" <td width='1' bgcolor='#dddddd'></td>";
		
	}
	if(pageList>1){
		pageInner+="<a class='first' href=\"javascript:firstPage('" + gridRenderTo + "', "+sessionid+");\"><img src='/Windchill/jsp/project/images/BBS_start.gif'></a>";
		pageInner+="</td>";
		pageInner+="				<td width='1' bgcolor='#dddddd'></td>";
		pageInner+="				<td width='30' class='quick' align='center'><a class='pre' href=\"javascript:prePage('" + gridRenderTo + "', "+totalSize+","+sessionid+");\" class='smallblue'><img src='/Windchill/jsp/project/images/BBS_prev.gif' border='0' align='middle'></a></td>";
		pageInner+="				<td width='1' bgcolor='#dddddd'></td>";
	}
	for(var i=startPageList; i<=endPageList; i++){
		pageInner += "				<td style='padding:2 8 0 7;cursor:pointer' onMouseOver='this.style.background=\"#ECECEC\"' OnMouseOut='this.style.background=\"\"' class='nav_on' OnClick=\"javascript:goPage('" + gridRenderTo + "', '"+(i)+"','"+sessionid+"')\" >";

		if(i==currentPage){
			pageInner += "&nbsp;<b>"+(i)+"</b>&nbsp;";
		}else{
			pageInner += "&nbsp;" + i + "&nbsp;";
		}
		pageInner += "</td>";
	}
	if(totalPageList>pageList){
		pageInner+="				<td width='1' bgcolor='#dddddd'></td>";
		pageInner+="				<td width='30' align='center'><a href=\"javascript:nextPage('" + gridRenderTo + "', "+totalSize+","+sessionid+");\" class='smallblue'><img src='/Windchill/jsp/project/images/BBS_next.gif' border='0' align='middle'></a></td>";
		pageInner+="				<td width='1' bgcolor='#dddddd'></td>";
		pageInner+="				<td width='30'align='center'>";
		pageInner+="<a href=\"javascript:lastPage('" + gridRenderTo + "', "+totalPage+","+sessionid+");\" class='small'><img src='/Windchill/jsp/project/images/BBS_end.gif' border='0' align='middle'></a>";
	}
	if(totalPageList==pageList){
		pageInner+="				<td width='1' bgcolor='#dddddd'></td>";
		pageInner+="				<td width='30' align='center'><img src='/Windchill/jsp/project/images/BBS_next.gif'></td>";
		pageInner+="				<td width='1' bgcolor='#dddddd'></td>";
		pageInner+="				<td width='30'align='center'><img src='/Windchill/jsp/project/images/BBS_end.gif'></a>";
	}
	pageInner +="				</td>";
	pageInner +="			</tr>";
	pageInner +="		</table>";
	pageInner +="  </td>";
	pageInner +=" <td class='small' align='right'  width='200'><select id='pSizeSelect' onchange=\"javascript:selectpSize('" + gridRenderTo + "')\">";
	pageInner +=" <option value='10' >10</option>";
	pageInner +=" <option value='15' >15</option>";
	pageInner +=" <option value='20' >20</option>";
	pageInner +=" <option value='30' >30</option>";
	pageInner +=" <option value='50' >50</option>";
	pageInner +=" <option value='100' >100</option>";
	pageInner +="</select>" + listSizeLabel + "</td>";
	pageInner +="</tr>";
	pageInner +="</table>";
	$("#paginates").html("");
	$("#paginates").append(pageInner);
   
	var rowNum = $('#'+gridRenderTo).getGridParam('rowNum');
	$("option[value="+rowNum+"]").attr('selected','selected');
}