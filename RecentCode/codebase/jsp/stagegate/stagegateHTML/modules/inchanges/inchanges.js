const ecrTableElement = document.querySelector("table.ecrTable > tbody");
const ecoTableElement = document.querySelector("table.ecoTable > tbody");

// 수정 <-> 저장 버튼 이벤트
document.querySelector(".property_update").addEventListener("click", (e) => {
  const button = e.target;
  if (button.classList.contains("property_update")) {
    button.classList.remove("property_update");
    button.classList.add("property_submit");
    documentInputTextsControll(false);
    //addAddButtonRow();
  } else {
    button.classList.add("property_update");
    button.classList.remove("property_submit");
    documentInputTextsControll(true);
    //deleteAddButtonRow();
  }
});


// textarea element 생성
const createTextAreaElement = () => {
  const textArea = document.createElement("textArea");
  textArea.rows = 3;
  textArea.classList.add("demo-textArea");
  return textArea;
};

// text 타입의 input element 생성
const createInputTextElement = () => {
  const inputText = document.createElement("input");
  inputText.type = "text";
  inputText.addEventListener("keypress", textEnterPress);
  return inputText;
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

// numbering을 위한 span element 생성
const createSpanElement = (classname) => {
  const span = document.createElement("span");
  span.classList.add(classname);
  return span;
};

// 줄 삭제를 위한 button element 생성
const createDeleteButton = (deleteTR) => {
  const button = document.createElement("button");
  button.classList.add("row_delete_button");
  button.innerText = "-";
  button.addEventListener("click", (e) => {
    const rowIndex = e.target.parentNode.parentNode.rowIndex - 2;
    ecrTableElement.deleteRow(rowIndex);
  });
  return button;
};

// select의 option들을 추가한다.
const createSelectOptions = (selectEle, options) => {
  options.forEach(({ title, classname }) => {
    const optionEle = document.createElement("option");
    optionEle.innerText = title;
    if (classname) {
      optionEle.classList.add(classname);
      optionEle.value = classname;
    }

    selectEle.appendChild(optionEle);
  });
};

// select 값을 변경했을 때 발생 이벤트
const selectClassChange = (e) => {
  const select = e.target;
  select.classList.remove(select.classList.item(0));
  select.classList.add(select.value);
};

// reivew select element 생성
const createReviewSelect = () => {
  const selectEle = document.createElement("select");
  createSelectOptions(selectEle, reviews);
  selectEle.classList.add("signal_normal");
  return selectEle;
};

// status select element 생성
const createSignalSelect = () => {
  const selectEle = document.createElement("select");
  createSelectOptions(selectEle, signalOptions);
  selectEle.addEventListener("change", selectClassChange);
  return selectEle;
};

// value에 따른 줄 삭제 버튼의 숨기기 /  보이기
const rowDeleteButtonVisibleControl = (value) => {
  document.querySelectorAll("button.row_delete_button").forEach((element) => {
    if (value) element.classList.remove("visibility_hidden");
    else element.classList.add("visibility_hidden");
  });
};

// 줄 추가 버튼을 클릭했을때, row가 하나 추가된다.
const addDataRow = () => {
  const addRowTR = ecrTableElement.insertRow(
    ecrTableElement.rows.length - 1,
    ecrTableElement.rows.length
  );


  // ECR 번호
  const ecrNumberTD = document.createElement("td");
  //const deleteButton = createDeleteButton(addRowTR);
  ecrNumberTD.appendChild(createInputTextElement());
  //ecrNumberTD.appendChild(deleteButton);
  addRowTR.appendChild(ecrNumberTD);

  // ECR 이름
  const ecrNameTD = document.createElement("td");
  ecrNameTD.appendChild(createInputTextElement());
  addRowTR.appendChild(ecrNameTD);

};

// 줄 추가 버튼을 클릭했을때, row가 하나 추가된다.
const ecoAddDataRow = () => {
  const addRowTR = ecoTableElement.insertRow(
    ecoTableElement.rows.length - 1,
    ecoTableElement.rows.length
  );


  // ECO 번호
  const ecoNumberTD = document.createElement("td");
  //const deleteButton = createDeleteButton(addRowTR);
  ecoNumberTD.appendChild(createInputTextElement());
  //ecrNumberTD.appendChild(deleteButton);
  addRowTR.appendChild(ecoNumberTD);

  // ECO 이름
  const ecoNameTD = document.createElement("td");
  ecoNameTD.appendChild(createInputTextElement());
  addRowTR.appendChild(ecoNameTD);

};

// 수정 버튼을 클릭했을때 row의 numbering을 숨긴다.
const deleteumberingLableValue = () => {
  document.querySelectorAll("span.numbering_label").forEach((element) => {
    element.innerText = "";
  });
};

// 각 row의 삭제 버튼을 보여준다.
const rowDeleteButtonVisible = () => {
  rowDeleteButtonVisibleControl(true);
};

// 수정 버튼을 클릭했을때 줄 추가 버튼이 있는 row가 생성된다.
const addAddButtonRow = () => {
  deleteumberingLableValue();
  rowDeleteButtonVisible();
  
  const addRowTR = ecrTableElement.insertRow(-1);
  const addRowTD = document.createElement("td");
  addRowTD.classList.add("sub_menu");

  const addButton = document.createElement("button");
  addButton.innerText = "+";
  addButton.classList.add("row_add_button");
  addButton.addEventListener("click", addDataRow);
  
  addRowTD.appendChild(addButton);
  addRowTR.appendChild(addRowTD);
  
  ///////////////////////////
  const ecoAddRowTR = ecoTableElement.insertRow(-1);
  const ecoAddRowTD = document.createElement("td");
  ecoAddRowTD.classList.add("sub_menu");
  
  const ecoAddButton = document.createElement("button");
  ecoAddButton.innerText = "+";
  ecoAddButton.classList.add("row_add_button");
  ecoAddButton.addEventListener("click", ecoAddDataRow);
  
  ecoAddRowTD.appendChild(ecoAddButton);
  ecoAddRowTR.appendChild(ecoAddRowTD);
};

// 각 row의 numbering을 한다.
const setNumberingLabelValue = () => {
  document
    .querySelectorAll("span.numbering_label")
    .forEach((element, index) => {
      element.innerText = index + 1;
    });
};

// 각 row의 삭제 버튼을 숨긴다.
const rowDeleteButtonInVisible = () => {
  rowDeleteButtonVisibleControl(false);
};

// 저장 버튼을 클릭했을때 numbering, 삭제버튼 숨기기, 줄 추가 row 삭제
const deleteAddButtonRow = () => {
  setNumberingLabelValue();
  rowDeleteButtonInVisible();
  ecrTableElement.deleteRow(-1);
  ecoTableElement.deleteRow(-1);
};

// table의 data row를 추가
const addRow = (id, data) => {
  
	const table = document.querySelector(`#${id}`);
	
	for(let rowVal of Object.values(data)){

  		const tableTR = document.createElement("tr");
  		
		const td01 = document.createElement("td");
	    const inputA = document.createElement("a");
	    inputA.innerText = rowVal.number;
	    inputA.href = "javascript:openPopup('"+id+"', '"+ rowVal.oid+ "');";
	    td01.appendChild(inputA);
	    tableTR.appendChild(td01);
  		
		const td02 = document.createElement("td");//hckim
		const inputText= inputTextElement("textLF");
		inputText.value = rowVal.name;
		inputText.setAttribute("disabled", "disabled");
    	td02.appendChild(inputText);
    	tableTR.appendChild(td02);
    	
		table.appendChild(tableTR);
	}
	
};

const openPopup = (id, oid) => {
	const option = "top=50, left=200, width=1600, height=800, status=no, menubar=no, toolbar=no";
	var url = ""
	if(id == "ecrMainBody"){
		url = "http://"+window.location.hostname+"/Windchill/worldex/change/viewECR?oid=OR:"+oid;
	}else{
		url = "http://"+window.location.hostname+"/Windchill/worldex/change/viewECO?oid=OR:"+oid;
	}
	
	window.open(url, "", option);
};

// table들에 datas를 기반으로 데이터를 추가
const addTableRows = (datas) => {

	for (let id in datas) {
		addRow(id, datas[id]);
	}

};

addTableRows(datas);