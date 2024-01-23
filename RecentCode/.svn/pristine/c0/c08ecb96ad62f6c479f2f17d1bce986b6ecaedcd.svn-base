riskTableElement = document.querySelector("table.risk > tbody");

seq = 1;
signalOptions = [
  { title: "",  classname: "none" },
  { title: "G", classname: "signal_G" },
  { title: "Y", classname: "signal_Y" },
  { title: "R", classname: "signal_R" },
];
reviews = [
  { title: "",  classname: "" },
  { title: "Phase1",  classname: "Phase1" },
  { title: "Phase2",  classname: "Phase2" },
  { title: "Phase3",  classname: "Phase3" },
  { title: "Phase4",  classname: "Phase4" },
  { title: "Phase5",  classname: "Phase5" },
  { title: "Phase6",  classname: "Phase6" },
  { title: "Phase7",  classname: "Phase7" },
];

// textarea element 생성
createTextAreaElement = (name, value, disabled) => {
  let textArea = document.createElement("textArea");
  textArea.name = name;
  textArea.rows = 3;
  textArea.classList.add("demo-textArea");
  textArea.value = value || "";
  textArea.disabled = disabled;
  return textArea;
};

// text 타입의 input element 생성
createInputTextElement = (name, value, disabled) => {
  let inputText = document.createElement("input");
  inputText.name = name;
  inputText.type = "text";
  inputText.addEventListener("keypress", textEnterPress);
  inputText.value = value || "";
  inputText.disabled = disabled;
  return inputText;
};

// hidden 타입의 input element 생성
createInputHiddenElement = (name, value) => {
  let inputText = document.createElement("input");
  inputText.name = name;
  inputText.type = "hidden";
  inputText.value = value;
  return inputText;
};

// numbering을 위한 span element 생성
createSpanElement = (classname) => {
  let span = document.createElement("span");
  span.classList.add(classname);
  return span;
};

// 줄 삭제를 위한 button element 생성
createDeleteButton = (deleteTR) => {
  let button = document.createElement("button");
  button.classList.add("row_delete_button");
  button.innerText = "-";
  button.addEventListener("click", (e) => {
    const rowIndex = e.target.parentNode.parentNode.rowIndex - 2;
    riskTableElement.deleteRow(rowIndex);
  });
  return button;
};

// select의 option들을 추가한다.
createSelectOptions = (selectEle, options, value) => {
  options.forEach(({ title, classname }) => {
    let optionEle = document.createElement("option");
    optionEle.innerText = title;
    if (classname) {
      optionEle.classList.add(classname);
      optionEle.value = classname;
    }
	
	if(value === classname){
		optionEle.setAttribute('selected', 'selected');
	}

    selectEle.appendChild(optionEle);
  });
};

// select 값을 변경했을 때 발생 이벤트
selectClassChange = (e) => {
  let select = e.target;
  select.classList.remove(select.classList.item(0));
  select.classList.add(select.value);
};

// reivew select element 생성
createReviewSelect = (name, value, disabled) => {
	let selectEle = document.createElement("select");
	selectEle.name = name;
	createSelectOptions(selectEle, reviews, value);
	selectEle.classList.add("signal_normal");
	selectEle.disabled = disabled;
	
	return selectEle;
};

// status select element 생성
createSignalSelect = (name, value, disabled) => {
  let selectEle = document.createElement("select");
  selectEle.name = name;
  createSelectOptions(selectEle, signalOptions, value);
  selectEle.addEventListener("change", selectClassChange);
  selectEle.disabled = disabled;
  return selectEle;
};

// value에 따른 줄 삭제 버튼의 숨기기 /  보이기
rowDeleteButtonVisibleControl = (value) => {
  document.querySelectorAll("button.row_delete_button").forEach((element) => {
    if (value) element.classList.remove("visibility_hidden");
    else element.classList.add("visibility_hidden");
  });
};

// row 추가 시 td 및 data 설정
setTableRows = (addRowTR, data) => {
	// no
	let addNoTD = document.createElement("td");
	let deleteButton = createDeleteButton(addRowTR);
	addNoTD.appendChild(createSpanElement("numbering_label"));
	addNoTD.appendChild(deleteButton);
	addRowTR.appendChild(addNoTD);
	
	// Risk Description - Oid, name, code
	let riskAreaTD = document.createElement("td");
	let riskTextArea = createTextAreaElement("value0", data && data.value0 || "", data);
	let inputOid = createInputHiddenElement("oid", data && data.oid || "");
	let inputName = createInputHiddenElement("name", data && data.code || "RISK");
	let inputCode = createInputHiddenElement("code", data && data.name || "RISK");
	riskAreaTD.appendChild(riskTextArea);
	riskAreaTD.appendChild(inputOid);
	riskAreaTD.appendChild(inputName);
	riskAreaTD.appendChild(inputCode);
	addRowTR.appendChild(riskAreaTD);
	
	// Program Review Type
	let reviewTD = document.createElement("td");
	const value1 = data && data.value1 || "";
	let reviewSelect = createReviewSelect("value1", value1, data);
	if(value1){
		reviewSelect.classList.add(value1);
	}
	reviewTD.appendChild(reviewSelect);
	addRowTR.appendChild(reviewTD);
	
	// file
	let fileTD = document.createElement("td");
	let imgArea = document.createElement("img");
	imgArea.src = "/Windchill/netmarkets/images/doc_document.gif";
	imgArea.style.cursor = 'pointer';
	imgArea.classList.add('file');
	imgArea.addEventListener('click', function(event){
        filePopup(data.oid);
    });
	fileTD.appendChild(imgArea);
	addRowTR.appendChild(fileTD);
	
	// Customer
	let customerTD = document.createElement("td");
	const value2 = data && data.value2 || "";
	let customerSelect = createSignalSelect("value2", value2, data);
	if(value2){
		customerSelect.classList.add(value2);
	}
	customerTD.appendChild(customerSelect);
	addRowTR.appendChild(customerTD);
	
	// Budget
	let budgetTD = document.createElement("td");
	const value3 = data && data.value3 || "";
	let budgetSelect = createSignalSelect("value3", value3 || "", data);
	if(value3){
		budgetSelect.classList.add(value3);
	}
	budgetTD.appendChild(budgetSelect);
	addRowTR.appendChild(budgetTD);
	
	// Timing
	let timingTD = document.createElement("td");
	const value4 = data && data.value4 || "";
	let timingSelect = createSignalSelect("value4", value4, data);
	if(value4){
		timingSelect.classList.add(value4);
	}
	timingTD.appendChild(timingSelect);
	addRowTR.appendChild(timingTD);
	
	// Perform
	let performTD = document.createElement("td");
	const value5 = data && data.value5 || "";
	let performSelect = createSignalSelect("value5", value5, data);
	if(value5){
		performSelect.classList.add(value5);
	}
	performTD.appendChild(performSelect);
	addRowTR.appendChild(performTD);
	
	// proposed Action
	let proposedAreaTD = document.createElement("td");
	const value6 = data && data.value6 || "";
	let proposedTextArea = createTextAreaElement("value6", value6, data);
	proposedAreaTD.appendChild(proposedTextArea);
	addRowTR.appendChild(proposedAreaTD);
	
	// Leader
	let leaderTD = document.createElement("td");
	const value7 = data && data.value7 || "";
	leaderTD.appendChild(createInputTextElement("value7", value7, data));
	addRowTR.appendChild(leaderTD);
	
	// Target completion date
	let targetTD = document.createElement("td");
	let targetID = "target" + seq;
	const value8 = data && data.value8 || "";
	targetTD = createDatePicker("value8", targetID, targetTD, value8, data);
	addRowTR.appendChild(targetTD);
	
	// Actual completion date
	let actualTD = document.createElement("td");
	const compID = "completion" + seq;
	const value9 = data && data.value9 || "";
	actualTD = createDatePicker("value9", compID, actualTD, value9, data);
	addRowTR.appendChild(actualTD);
	
	//DatePickerAction
	setDatePickerAction(targetID);
	setDatePickerAction(compID);

	//set view
	if(data){
		setNumberingLabelValue();
		styleVisibleControl(false);
		rowDeleteButtonInVisible();
	}
	
	seq++;
};

// 수정 버튼을 클릭했을때 row의 numbering을 숨긴다.
deleteumberingLableValue = () => {
  document.querySelectorAll("span.numbering_label").forEach((element) => {
    element.innerText = "";
  });
};

// 각 row의 삭제 버튼을 보여준다.
rowDeleteButtonVisible = () => {
  rowDeleteButtonVisibleControl(true);
};

// 수정 버튼을 클릭했을때 줄 추가 버튼이 있는 row가 생성된다.
addAddButtonRow = () => {
	deleteumberingLableValue();
	rowDeleteButtonVisible();
	let addRowTR = riskTableElement.insertRow(-1);
	let addRowTD = document.createElement("td");
	addRowTD.classList.add("sub_menu");
	
	let addButton = document.createElement("button");
	addButton.innerText = "+";
	addButton.classList.add("row_add_button");
	addButton.addEventListener("click", addButtonAction);
	
	addRowTD.appendChild(addButton);
	
	addRowTR.appendChild(addRowTD);
	styleVisibleControl(true);
};

// 각 row의 numbering을 한다.
setNumberingLabelValue = () => {
  document.querySelectorAll("span.numbering_label").forEach((element, index) => {
	  const idx = index + 1;
      element.innerText = idx;

	  let inputSeq = createInputHiddenElement("seq", idx);
	  element.appendChild(inputSeq);
  });
};

// 각 row의 삭제 버튼을 숨긴다.
rowDeleteButtonInVisible = () => {
  rowDeleteButtonVisibleControl(false);
};

// 저장 버튼을 클릭했을때 numbering, 삭제버튼 숨기기, 줄 추가 row 삭제
deleteAddButtonRow = () => {
	setNumberingLabelValue();
	styleVisibleControl(false);
	rowDeleteButtonInVisible();
	riskTableElement.deleteRow(-1);
};

// 숨기기/보이기 : input border, textarea resize, img
styleVisibleControl = (viewable) => {
	let trList = riskTableElement.querySelectorAll("tr");
	//row
	for(let row=0; row<trList.length; row++){
		let tr = trList[row];
		
		let inputTextList = tr.querySelectorAll("input[type='text']");
		for(let ii=0; ii<inputTextList.length; ii++){
			let input = inputTextList[ii];
			if(viewable){
				input.style = "width:60px;";
			}else{
				input.style = "border-style: none;";
			}
		}
		let textareaList = tr.querySelectorAll("textarea");
		for(let ti=0; ti<textareaList.length; ti++){
			let textarea = textareaList[ti];
			if(viewable){
				textarea.style = "resize: vertical; font-size: 12px;";
			}else{
				textarea.style = "resize: none;";
			}
		}
		let imgList = tr.querySelectorAll("img");
		for(let bi=0; bi<imgList.length; bi++){
			let img = imgList[bi];
			if(viewable){
				img.style = "vertical-align: middle;";
			}else{
				img.style = "display: none;";
			}
			
			if(img.classList.contains('file')){
				img.style = "vertical-align: middle;";
			}
			
		}
	}
};

//달력 추가
createDatePicker = (name, id, targetTD, value, disabled) => {
	let inputText = createInputTextElement(name, value, disabled);
	inputText.classList.add("datePicker");
	inputText.id = id;
	inputText.style = "width:60px;";
	inputText.setAttribute("readonly", "readonly");
	
	let pointerImg = document.createElement("img");
	pointerImg.classList.add("pointer");
	pointerImg.src = "/Windchill/jsp/portal/images/calendar_icon.png";
	pointerImg.name = id + "Btn";
	pointerImg.id = id + "Btn";
	pointerImg.style = "vertical-align: middle;";
	
	let resetSpan = document.createElement("span");
	resetSpan.classList.add("resetDate");
	resetSpan.classList.add("pointer");
	resetSpan.addEventListener("click", (e) => {
	let inputDate = document.getElementById(e.target.dataset.removeTarget);
	inputDate.value = "";
	});
	
	let delImg = document.createElement("img");
	delImg.classList.add("verticalMiddle");
	delImg.src = "/Windchill/jsp/portal/images/delete_icon.png";
	delImg.dataset.removeTarget = id;
	resetSpan.appendChild(delImg);
	
	targetTD.appendChild(inputText);
	targetTD.appendChild(pointerImg);
	targetTD.appendChild(resetSpan);
	
	return targetTD;
}

//달력 액션 추가
setDatePickerAction = (id) => {
  let myCalendar = new dhtmlXCalendarObject({
  	input: id,
  	button: id+"Btn"
  });
  myCalendar.setDateFormat("%Y/%m/%d");
  myCalendar.setWeekStartDay(7);
  let input = document.getElementById(id);
  let rect = input.getBoundingClientRect();
  myCalendar.attachEvent("onShow", function(){
	myCalendar.base.style.left = rect.left - input.offsetWidth - 21+"px";
  });
  //myCalendar.setPosition(rect.left - input.offsetWidth - 20, rect.top + input.offsetHeight);
}

// 데이터로 테이블 추가
getTableRows = (list) => {
	for (let data of list) {
		let addRowTR = document.createElement("tr");
		riskTableElement.appendChild(addRowTR);
		
		setTableRows(addRowTR, data);
	}
};

// row 추가 버튼
addButtonAction = () => {
	const addRowTR = riskTableElement.insertRow(
		riskTableElement.rows.length - 1,
		riskTableElement.rows.length
	);
	setTableRows(addRowTR);
};