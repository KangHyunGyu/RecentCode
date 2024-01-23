// 수정 <-> 저장 버튼 이벤트
document.querySelector(".property_update").addEventListener("click", (e) => {
  const button = e.target;
  if (button.classList.contains("property_update")) {
    button.classList.remove("property_update");
    button.classList.add("property_submit");
    documentInputTextsControllSummary(false);
    setDisplayCalButton(true);
  } else {
    button.classList.add("property_update");
    button.classList.remove("property_submit");
    documentInputTextsControllSummary(true);
    setDisplayCalButton(false);
  }
});

documentInputTextsControllSummary = (target) => {
  const texts = document.querySelectorAll(
    `
    ${target ? "." + target + " " : ""}input[type="text"],
    ${target ? "." + target + " " : ""}input[type="number"],
    ${target ? "." + target + " " : ""}textArea
    `
  );
  texts.forEach((element) => {
    element.toggleAttribute("disabled");
  });
};

// type 타입의 input element 에서 keypress 시에 이벤트 처리
document.querySelectorAll("input[type=text]").forEach((element) => {
  element.addEventListener("keypress", textEnterPress);
});

//공통 : doubleCalendar
$(".datePicker").each(function(){
	var id = $(this).attr("id");

	$(this).after("<img class='dateCalendar' src='/Windchill/jsp/portal/images/calendar_icon.png' name='" + id + "Btn' id='" + id + "Btn'>");
	
	/*$("#" + id + "Btn").after("<span class='resetDate dateCalendar' data-remove-target='" + id + "'><img class='verticalMiddle' src='/Windchill/jsp/portal/images/delete_icon.png'></span>");*/
    
	$(".resetDate").click(function(){
		
		var target = $(this).data("remove-target");
		
		$("#" + target).val("");
	});
	
	var locale = {
		ko: {
			monthShort: ["1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"],
			months: ["1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"],
			daysShort: ["일","월","화","수","목","금","토"],
			days: ["일","월","화","수","목","금","토"]
		},
		en: {
			monthShort: ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"],
			months: ["January","February","March","April","May","Jun","July","August","September","October","November","December"],
			daysShort: ["Sun","Mon","Tue","Wed","Thu","Fri","Sat"],
			days: ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"]
		}
	}

	var myCalendar = new dhtmlXCalendarObject({
		input:id,
		button:id+"Btn"
	});
	
	myCalendar.setDateFormat("%Y/%m/%d");
	myCalendar.setWeekStartDay(7);
	
	let input = document.getElementById(id);
	let rect = input.getBoundingClientRect();
	
	myCalendar.attachEvent("onShow", function(){
	   myCalendar.base.style.left = rect.left - input.offsetWidth - 21+"px";
	});
		
	if("ENDDATE" == id) {
		var date = new Date();
		date.setDate(date.getDate() + 1);
		myCalendar.setInsensitiveRange(date, null);
	}
});

setDisplayCalButton = (isDisplay) => {
	if(isDisplay){
		$("#saveSummary").val("저장");
		$(".dateCalendar").show();
		$("#imgDiv").hide();
		$("#uploader_container").show();
		
	}else{
		$("#saveSummary").val("수정");
		$(".dateCalendar").hide();
		$("#imgDiv").show();
		$("#uploader_container").hide();
		modifySummary();
	}
}

inputCalendarTextElement = (classname) => {
	const inputText = document.createElement("input");
  	inputText.type = "text";
  	inputText.classList.add("datePicker");
  	inputText.classList.add("w125");
  	if (classname) inputText.classList.add(classname);
  	inputText.setAttribute("disabled", "disabled");
 	inputText.addEventListener("keypress", textEnterPress);
  	return inputText;

}

$(".dateCalendar").hide();
