document.querySelectorAll(".property_update").forEach((element) => {
  element.addEventListener("click", (e) => {
    const button = e.target;
    const target = button.dataset.target;
    if (button.classList.contains("property_update")) {
      button.classList.remove("property_update");
      button.classList.add("property_submit");
      documentInputTextsControllQuality(target);
      setDisplayCalButton(true, target, button);
    } else {
      button.classList.add("property_update");
      button.classList.remove("property_submit");
      documentInputTextsControllQuality(target);
      setDisplayCalButton(false, target, button);
    }
  });
});

documentInputTextsControllQuality = (target) => {
  const texts = document.querySelectorAll(
    `
    ${target ? "." + target + " " : ""}input[type="text"],
    ${target ? "." + target + " " : ""}input[type="number"],
    ${target ? "." + target + " " : ""}.qualitySelect,
    ${target ? "." + target + " " : ""}textArea
    `
  );
  texts.forEach((element) => {
    element.toggleAttribute("disabled");
  });
};

//공통 : doubleCalendar
drawCal = () => {
	$(".datePicker").each(function(){
		var id = $(this).attr("id");
	
//		$(this).after("<img class='pointer' src='/Windchill/jsp/portal/images/calendar_icon.png' name='" + id + "Btn' id='" + id + "Btn'>");
		
		/*$("#" + id + "Btn").after("<span class='resetDate pointer' data-remove-target='" + id + "'><img class='verticalMiddle' src='/Windchill/jsp/portal/images/delete_icon.png'></span>");*/
	    
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
}

setDisplayCalButton = (isDisplay, target, button) => {
  if(isDisplay){
  	setDisplayCalButton2(true, target);
  	button.value = "저장";
  }else{
  	setDisplayCalButton2(false, target);
  	button.value = "수정";
    modifyQuality(target);
  }
  
}

setDisplayCalButton2 = (isDisplay, target) => {
  	texts = document.querySelectorAll(
	    `
	    ${target ? "." + target + " " : ""}.dateCalendar
	    `
	  );
  	
	texts.forEach((element) => {
		if(isDisplay){
			element.style.display = "";
		}else{
			element.style.display = "none";
		}
	});
}

var signal_info = document.querySelector(".signal_info");

document.querySelectorAll(".help_icon").forEach((element) => {
  element.addEventListener("mouseover", (e) => {
    let windowHeight = globalThis.innerHeight;
    let signalHeight = signal_info.offsetHeight;
    let { clientX, clientY } = e;
    signal_info.classList.remove("visibility_hidden");

    signal_info.style.setProperty(
      "top",
      signalHeight + clientY > windowHeight
        ? `${clientY - signalHeight}`
        : `${clientY}px`
    );
    signal_info.style.setProperty("left", `${clientX}px`);
  });

  element.addEventListener("mouseout", (e) => {
    signal_info.classList.add("visibility_hidden");
  });
});

drawCal();
setDisplayCalButton2(false);