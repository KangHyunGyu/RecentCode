const signal_info = document.querySelector(".signal_info");

// help 아이콘에 마우스를 올렸을때 발생하는 이벤트
document.querySelectorAll(".help_icon").forEach((element) => {
  element.addEventListener("mouseover", (e) => {
    const windowHeight = globalThis.innerHeight;
    const signalHeight = signal_info.offsetHeight;
    const { clientX, clientY } = e;
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

// 수정 <-> 저장 버튼 이벤트
document.querySelectorAll(".property_update").forEach((element) => {
  element.addEventListener("click", (e) => {
    const button = e.target;
    const target = button.dataset.target;
    if (button.classList.contains("property_update")) {
      button.classList.remove("property_update");
      button.classList.add("property_submit");
      documentInputTextsControll(false, target);
      setDisplayCalButton(true, target);
    } else {
      button.classList.add("property_update");
      button.classList.remove("property_submit");
      documentInputTextsControll(true, target);
      setDisplayCalButton(false, target);
    }
  });
});

// select의 값을 변경할때 발생되는 이벤트
const selectClassChange = (e) => {
  const select = e.target;
  select.classList.remove(select.classList.item(0));
  select.classList.add(select.value);
};

// select element를 생성
const selectElement = () => {
  const select = document.createElement("select");
  let title, classname;
  for ({ title, classname } of selectOptions) {
    const option = document.createElement("option");
    option.classList.add(classname);
    option.value = classname;
    option.innerText = title;
    select.appendChild(option);
  }
  select.setAttribute("disabled", "disabled");
  select.addEventListener("change", selectClassChange);

  return select;
};

// text타입의 input element를 생성
const inputTextElement = (classname) => {
  const inputText = document.createElement("input");
  inputText.type = "text";
  if (classname) inputText.classList.add(classname);
  inputText.setAttribute("disabled", "disabled");
  inputText.addEventListener("keypress", textEnterPress);
  return inputText;
};

// text타입의 input element를 생성
const inputTextElementId = (classname, uniqueIdx1, uniqueIdx2) => {
  const inputText = document.createElement("input");
  inputText.type = "text";
  if (classname) inputText.classList.add(classname);
  inputText.setAttribute("disabled", "disabled");
  inputText.addEventListener("keypress", textEnterPress);
  inputText.id = "datepick" + uniqueIdx1 + "" + uniqueIdx2;
  return inputText;
};

// table의 header를 추가
const addHeader = (id) => {
  const table = document.querySelector(`.${id} > tbody`);
  const tableTR = document.createElement("tr");
  for (let title of header) {
    const tableTH = document.createElement("th");
    tableTH.classList.add("tac");
    tableTH.innerText = title;

    tableTR.appendChild(tableTH);
  }
  table.appendChild(tableTR);
};

// table의 data row를 추가
const addRow = (id, data, uniqueIdx1) => {
  const table = document.querySelector(`.${id} > tbody`);
  
  var uniqueIdx2 = 1;
  for (let title of data) {
    const tableTR = document.createElement("tr");

    // 구분
    const td01 = document.createElement("td");
    td01.innerText = title;
    tableTR.appendChild(td01);

    // what
    const td02 = document.createElement("td");
    td02.appendChild(inputTextElement("textLF"));
    tableTR.appendChild(td02);

    // Gate Index
    for (let i = 0; i < 6; i++) {
      const gateTD = document.createElement("td");
      gateTD.appendChild(selectElement());
      tableTR.appendChild(gateTD);
    }

    // Action
    const td04 = document.createElement("td");
    td04.appendChild(inputTextElement("textLF"));
    tableTR.appendChild(td04);

    // who
    const td05 = document.createElement("td");
    td05.appendChild(inputTextElement());
    tableTR.appendChild(td05);

    // date
    const td06 = document.createElement("td");
    td06.appendChild(inputTextElementId("datePicker", uniqueIdx1, uniqueIdx2++));
    tableTR.appendChild(td06);

    table.appendChild(tableTR);
  }
};

//공통 : doubleCalendar
const drawCal = () => {
	$(".datePicker").each(function(){
		var id = $(this).attr("id");
	
		$(this).after("<img class='pointer' src='/Windchill/jsp/portal/images/calendar_icon.png' name='" + id + "Btn' id='" + id + "Btn'>");
		
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
		
		if("ENDDATE" == id) {
			var date = new Date();
			date.setDate(date.getDate() + 1);
			myCalendar.setInsensitiveRange(date, null);
		}
	});
}

const setDisplayCalButton = (isDisplay, target) => {

	const texts = document.querySelectorAll(
	    `
	    ${target ? "." + target + " " : ""}img
	    `
  	);
  	
  	//element.style.display = "none";
	texts.forEach((element) => {
		if(isDisplay){
			element.style.display = "";
		}else{
			element.style.display = "none";
		}
	});
}

const setDisplayCalButton2 = (isDisplay) => {
	const texts = document.querySelectorAll(
		".pointer"
  	);
  	
  	//element.style.display = "none";
	texts.forEach((element) => {
		if(isDisplay){
			element.style.display = "";
		}else{
			element.style.display = "none";
		}
	});
}

// table들에 datas를 기반으로 데이터를 추가
const addTableRows = (datas) => {
	var uniqueIdx1 = 1;
  for (let id in datas) {
    addHeader(id);
    addRow(id, datas[id], uniqueIdx1++);
  }
};

// data.js > datas를 보낸다.
addTableRows(datas);
drawCal();
setDisplayCalButton2(false);