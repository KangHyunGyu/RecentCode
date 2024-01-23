
var gridLength = [1,5,10,50,100];

function AUIGrid_paging(pagingDIV, vArr) {
	
	var totalCount = vArr[0]
	var length = vArr[1];
	var page = vArr[2];
	
	var totalPage;
	
	if((totalCount % length) == 0) {
		totalPage = Math.floor((totalCount / length));
	}else if((totalCount % length) > 0) {
		totalPage = Math.floor((totalCount / length)) + 1;
	}
	
	var html = "";
	html += "<table style='width: 100%'>";
	
	html += "	<colgroup>";
	html += "		<col width='15%'>";
	html += "		<col width='70%'>";
	html += "		<col width='15%'>";
	html += "	</colgroup>";

	html += "	<tbody>";
	html += "		<tr>";
	
	/*
	 *  페이징 좌측 처리 ( 총 갯수 (현재페이지 / 총 페이지) )
	 */
	html += "			<td style='text-align: left;'>";
	html += 				totalCount + " ( " + page + " / " + totalPage + " page ) ";
	html += "			</td>";
	
	/*
	 *  페이징 중간 처리 ( 페이지 이동 )
	 */
	html += "			<td style='text-align: center;'>";
	html += "				<input type='text' name='page' id='page' style='display: none;' value=''>";
	
	/*
	 *  페이지 이동( 처음으로 )
	 */
	var firstEvent = "onclick='movePage(1)' ";
	var firstCursor = "pointer";
	if(page == 1 ) {
		firstEvent = "";
		firstCursor = "text";
	}
	html += "				<span style='display: inline-block; background-color: #ffffff; color : #555; border: 1px solid black; min-width: 2em; line-height: 2em; border-radius: 1em; font-weight: bold; cursor: " + firstCursor + "' " + firstEvent + " >";
	html += "<<";
	html += "				</span>";

	/*
	 *  페이지 이동( 이전 )
	 */
	var before = ((Math.floor((page-1) / 10) - 1) * 10) + 1;
	var beforeEvent = "onclick='movePage(" + before + ")' ";
	var beforeCursor = "pointer";
	if(Math.floor((page-1) / 10) <= 0) {
		beforeEvent = "";
		beforeCursor = "";
	}
	html += "				<span style='display: inline-block; background-color: #ffffff; color : #555; border: 1px solid black; min-width: 2em; line-height: 2em; border-radius: 1em; font-weight: bold; cursor: " + beforeCursor + "' " + beforeEvent + " >";
	html += "<";
	html += "				</span>";
	/*
	 *  페이지 이동( Index )
	 */
	var index = Math.floor((page-1) / 10);
	for(var i=1; i<11; i++) {
		
		var pageCount = (index * 10) + i;
		var color = "#ffffff";
		var textColor = "#555";
		var clickEvent = "onclick='movePage(" + pageCount + ")' ";
		var cursor = "pointer";

		if(pageCount <= totalPage){
			
			if(pageCount == page) {
				color = "#555";
				textColor = "#ffffff";
				clickEvent = "";
				cursor = "text";
			}
			html += "				<span style='display: inline-block; background-color: " + color + "; color : " + textColor + "; border: 1px solid black; min-width: 2em; line-height: 2em; border-radius: 1em; font-weight: bold; cursor: " + cursor + " ' " + clickEvent + " >";
			html += pageCount;
			html += "				</span>";
		}
	}
	
	/*
	 *  페이지 이동( 다음 )
	 */
	var after = ((Math.floor((page-1) / 10) + 1) * 10) + 1;
	var afterEvent = "onclick='movePage(" + after + ")' ";
	var afterCursor = "pointer";
	if(Math.floor((page-1) / 10) >= Math.floor((totalPage-1) / 10)) {
		afterEvent = "";
		afterCursor = "";
	}
	html += "				<span style='display: inline-block; background-color: #ffffff; color : #555; border: 1px solid black; min-width: 2em; line-height: 2em; border-radius: 1em; font-weight: bold; cursor: " + afterCursor + "' " + afterEvent + " >";
	html += ">";
	html += "				</span>";
	
	/*
	 *  페이지 이동( 마지막 으로 )
	 */
	var lastEvent = "onclick='movePage(" + totalPage + ")' ";
	var lastCursor = "pointer";
	if(page == totalPage ) {
		lastEvent = "";
		lastCursor = "text";
	}
	html += "				<span style='display: inline-block; background-color: #ffffff; color : #555; border: 1px solid black; min-width: 2em; line-height: 2em; border-radius: 1em; font-weight: bold; cursor: " + lastCursor + "' " + lastEvent + " >";
	html += ">>";
	html += "				</span>";
	
	html += "			</td>";
		
	/*
	 *  페이징 우측 처리 ( select )
	 */
	html += "			<td style='text-align: right;'>";
	html += "				<select name='length' id='length' style='width: 100px; height: 25px;' onchange='changeLength()'>";
	for(var i=0; i<gridLength.length; i++) {
		html += "					<option value='" + gridLength[i] + "'"  + (length==gridLength[i]? "selected" : "") +  "> " + gridLength[i] + " </option>";
	}
	html += "				</select>";
	html += "			</td>";
		
	html += "		</tr>";
	
	html += "	</tbody>";
	html += "</table>";
	
	$("#" + pagingDIV).html(html);
	
}

function changeLength() {
	if (window.myGridID){
		search();
	}
}

function movePage(count) {
	if($("#page")) {
		$($("#page")).val(count);
		if (window.myGridID){
			search();
		}
	}
}
