//만약 jQuery 사용한다면, $(document).ready(function() {}); 사용하세요.
function auiGridOption {	
	var auiGridProps = {
			// 칼럼 레이아웃 작성 시 칼럼의 width 를 퍼센티지(%) 로 설정한 경우 퍼센티지 적용 대상이 명시적 칼럼 사이즈를 제외하고 남는 width를 대상으로 할지 여부를 지정합니다.
			// 기본값(default) : false.
			applyRestPercentWidth = false;
			
			// 터치가 지원되는 모바일 기기(아이폰, 아이패드, 안드로이드 기기 등)로 접속 시 셀 편집을 탭(tap)하여 수정하게 만들지 여부를 지정합니다.
			// 기본값(default) : false.
			autoEditBeginMode = false;
	
			// 그리드 셀을 수정 중일 때 그리드 외의 HTML 바탕을 mouseDown 한 경우 수정을 종료 시킬지 여부를 지정합니다.
			// 기본값(default) : true.
			autoEditCompleteMode = true;
			
			// 터치가 지원되는 모바일 기기(아이폰, 아이패드, 안드로이드 기기 등)에서 스크롤 높이(수직 스크롤인 경우 넓이)를 자동으로 작게 만들지 여부를 지정합니다.
			// 기본값(default) : false.
			autoScrollSize = false;
	};
	
	
	// 실제로 #grid_wrap 에 그리드 생성
	myGridID = AUIGrid.create("#grid_wrap", columnLayout, auiGridProps);
	
	// 그리드 선택 모드
	AUIGrid.setSelectionMode(myGridID, "singleRow"); 
	
	// 푸터 객체 세팅
	AUIGrid.setFooter(myGridID, footerObject);
	
	AUIGrid.bind(myGridID, "cellEditEndBefore", function( event ) {
		var index = event.columnIndex;
		var returnValue = event.value;
		if(index == 8 || index == 9) {
			if(!$.isNumeric(returnValue)) {
				alert("숫자만 입력 가능합니다.");
				returnValue = "";
			}
		}
		return returnValue;
	})
}