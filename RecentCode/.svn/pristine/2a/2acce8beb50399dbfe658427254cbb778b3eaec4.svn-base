/**
 *  AUIGrid Column js 기능
 */

const e3psGrid_props_basicSearch = {
	
	
}

	//document 이벤트 수정
	$(document).on("click", function (event) {
		if ($(event.target).attr("aria-haspopup")) { // 서브 메뉴 아이템 클릭 한 경우
			return;
		}
		hideMenu();
	});
	// 헤더  메뉴가 현재 보이고 있는지 여부
	var nowHeaderMenuVisible = false;
	
	// 헤더 클릭 여부
	var headerClicked = false;
	
	// 헤더  메뉴 생성된 곳의 dataField 보관 변수
	var currentDataField;
	
	//메뉴 감추기
	function hideMenu() {
		if (nowHeaderMenuVisible && !headerClicked) { // 메뉴 감추기
			$("#headerMenu").menu("destroy");
			$("#headerMenu").hide();
			nowHeaderMenuVisible = false;
		}
		headerClicked = false;
	};

	//그리드 헤더 클릭 이벤트 핸들러
	function auiGridHeaderClickHandler(event) {

		if (nowHeaderMenuVisible) {
			hideMenu();
		}
		// 헤더 그룹 클릭 한 경우
		if (event.item.isBranch) {
			currentDataField = "headerGroup";
		} else {
			//  메뉴 생성된 dataField 보관.
			currentDataField = event.dataField;
		}
		if (event.dataField == "id") { // ID 칼럼은 숨기기 못하게 설정
			$("#h_item_4").addClass("ui-state-disabled");
		} else {
			$("#h_item_4").removeClass("ui-state-disabled");
		}
		// 헤더 에서 사용할 메뉴 위젯 구성
		$("#headerMenu").menu({
			select: headerMenuSelectHandler
		});
		$("#headerMenu").css({
			left: event.pageX,
			top: event.pageY
		}).show();

		nowHeaderMenuVisible = true;
		headerClicked = true;

		return false; // 기본 행위(소팅) 하지 않기.
	};

	//헤더  메뉴 아이템 선택 핸들러
	function headerMenuSelectHandler(event, ui) {
		
		var selectedId = ui.item.prop("id");

		switch (selectedId) {
			case "h_item_1": // 오름 차순 정렬
				AUIGrid.setSorting(myGridID, [{ "dataField": currentDataField, "sortType": 1 }]);
				break;
			case "h_item_2": // 내림 차순 정렬
				AUIGrid.setSorting(myGridID, [{ "dataField": currentDataField, "sortType": -1 }]);
				break;
			case "h_item_3": // 정렬 초기화
				AUIGrid.clearSortingAll(myGridID);
				break;
			case "h_item_4": // 현재 칼럼 숨기기
				AUIGrid.hideColumnByDataField(myGridID, currentDataField);
				$("#h_item_ul span.ui-icon[data=" + currentDataField + "]").removeClass("ui-icon-check")
					.addClass("ui-icon-blank");
				break;
			case "h_item_6": // 모든 칼럼 보이기
				AUIGrid.showAllColumns(myGridID);
				$("#h_item_ul span.ui-icon[data]").addClass("ui-icon-check")
					.removeClass("ui-icon-blank");
				break;
			default: // 헤더 보이기 / 숨기기
				var kids = ui.item.children();
				var dataField = kids.attr("data"); // data 속성에서 dataField 얻기
				if (typeof dataField != "undefined") {
					var checked = kids.hasClass("ui-icon-check");
					if (checked) {
						AUIGrid.hideColumnByDataField(myGridID, dataField);
						kids.removeClass("ui-icon-check")
							.addClass("ui-icon-blank");
					} else {
						AUIGrid.showColumnByDataField(myGridID, dataField);
						kids.addClass("ui-icon-check");
						kids.removeClass("ui-icon-blank");
					}
				}
				break;
		}
	};

	// AUIGrid 칼럼 레이아웃을 기반으로  메뉴 구성 위해 html 문자 만들어 반환.
	function genColumnHtml(columns) {
		var arr = [];
		for (var i = 0, len = columns.length; i < len; i++) {
			recursiveParse(columns[i]);
		}
		return arr.join('');

		// 재귀함수
		function recursiveParse(column) {
			if (typeof column.children != "undefined") {
				arr.push('<li>' + column.headerText + '<ul>');
				for (var i = 0, l = column.children.length; i < l; i++) {
					recursiveParse(column.children[i]);
				}
				arr.push('</ul></li>');
			} else {
				if (column.dataField == "id") { // ID 칼럼은 숨기기 못하게 설정
					arr.push('<li class="ui-state-disabled"><span class="ui-icon ui-icon-check"/>' + column.headerText + '</li>');
				} else {
					arr.push('<li><span class="ui-icon ui-icon-check" data="' + column.dataField + '"/>' + column.headerText + '</li>');
				}
			}
		};
	};





