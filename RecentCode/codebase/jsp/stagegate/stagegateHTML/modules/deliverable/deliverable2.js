const deliverableTableElement = document.querySelector("table.deliverable_status > tbody");
const cookieId = "deliverable_status";

// 수정 <-> 저장 버튼 이벤트
document.querySelectorAll(".property_update").forEach((element) => {
  element.addEventListener("click", (e) => {
    const button = e.target;
    const target = button.dataset.target;
    if (button.classList.contains("property_update")) {
      button.classList.remove("property_update");
      button.classList.add("property_submit");
      documentInputTextsControll(false, target);
      addAddButtonRow();
    } else {
      button.classList.add("property_update");
      button.classList.remove("property_submit");
      documentInputTextsControll(true, target);
      deleteAddButtonRow();
      saveAsCookie();
    }
  });
});

const saveAsCookie = () => {
	const inputTextList = document.querySelectorAll(".mainTable > tbody > tr > td > input[type='text']");
	
	//const table = document.querySelector(`.${id} > tbody`);
	var keyIdx = 0; 
	inputTextList.forEach(function(inputText){
		
		setCookie(cookieId + keyIdx.toString(), inputText.value, 1);
		
		keyIdx++;
	});
}

const setCookie = (key, value, expire) => {
	var expireDate = new Date();
	expireDate.setDate(expireDate.getDate() + expire);
	cookies = key + '=' + escape(value) + '; path=/ ';
	if(typeof expire != 'undefined') cookies += ';expires=' + expireDate.toGMTString() + ';';
	document.cookie = cookies;
}

const getCookie = (key) => {
	key = key + '=';
	var cookieData = document.cookie;
	var start = cookieData.indexOf(key);
	var value = '';
	if(start != -1){
		start += key.length;
		var end = cookieData.indexOf(';', start);
		if(end == -1)end = cookieData.length;
		value = cookieData.substring(start, end);
	}
	return unescape(value);
}

const loadCookies = () => {
	const table = document.querySelector("."+cookieId+" > tbody");
	
	var cookieData = document.cookie;
	var start = cookieData.indexOf(cookieId);
	
	const reg = new RegExp(cookieId, 'g');
	const cntColumn = table.rows[0].cells.length;
	
	if(start != -1){
		var cookieLength = cookieData.match(reg).length;
		
		
		for(var idxTR = 0; idxTR < cookieLength/cntColumn; idxTR++){
			var tableTR = document.createElement("tr");
		
			for(var idxTD = 0; idxTD < cntColumn; idxTD++){
				
				var tableTD = document.createElement("td");
				var inputText = inputTextElement("textLF");
				
				inputText.value = getCookie(cookieId+(idxTD+(idxTR)*cntColumn).toString());
			    tableTD.appendChild(inputText);
			    tableTR.appendChild(tableTD);
				
				var cookieValue = getCookie(cookieId + idxTD.toString());
			}	
			
			table.appendChild(tableTR);
		}
		
	}
}


// text타입의 input element를 생성
const inputTextElement = (classname) => {
  const inputText = document.createElement("input");
  inputText.type = "text";
  if (classname) inputText.classList.add(classname);
  inputText.setAttribute("disabled", "disabled");
  inputText.addEventListener("keypress", textEnterPress);
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
const addRow = (id, data) => {
  const table = document.querySelector(`.${id} > tbody`);
  for (let title of data) {
    const tableTR = document.createElement("tr");

    // NO.
    const td01 = document.createElement("td");
    var textTd01 = td01.appendChild(inputTextElement(""));
    textTd01.value = title; 
    //td01.innerText = title;
    tableTR.appendChild(td01);
    
    // 태스크명
    const td02 = document.createElement("td");
    td02.appendChild(inputTextElement("textLF"));
    tableTR.appendChild(td02);
    
    // 산출물
    const td03 = document.createElement("td");
    td03.appendChild(inputTextElement("textLF"));
    tableTR.appendChild(td03);
    
    // 프로젝트 롤
    const td04 = document.createElement("td");
    td04.appendChild(inputTextElement("textLF"));
    tableTR.appendChild(td04);
    
    // 인증타입
    const td05 = document.createElement("td");
    td05.appendChild(inputTextElement("textLF"));
    tableTR.appendChild(td05);
    
    // Phase
    const td06 = document.createElement("td");
    td06.appendChild(inputTextElement("textLF"));
    tableTR.appendChild(td06);
    
    // 작업 현황
    const td07 = document.createElement("td");
    td07.appendChild(inputTextElement("textLF"));
    tableTR.appendChild(td07);
    
    // 문서
    const td08 = document.createElement("td");
    td08.appendChild(inputTextElement("textLF"));
    tableTR.appendChild(td08);
    
    // 최초 등록일
    const td09 = document.createElement("td");
    td09.appendChild(inputTextElement("textLF"));
    tableTR.appendChild(td09);

    table.appendChild(tableTR);
  }
};

// table들에 datas를 기반으로 데이터를 추가
const addTableRows = (datas) => {
  for (let id in datas) {
    addHeader(id);
    addRow(id, datas[id]);
  }
};

//hckim

// textarea element 생성
const createTextAreaElement = () => {
  const textArea = document.createElement("textArea");
  textArea.rows = 3;
  textArea.classList.add("demo-textArea");
  return textArea;
};


// text 타입의 input element 생성
const createInputTextElement = (classname) => {
  const inputText = document.createElement("input");
  inputText.type = "text";
  
  if (classname) inputText.classList.add(classname);
  
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
    deliverableTableElement.deleteRow(rowIndex);
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
  const addRowTR = deliverableTableElement.insertRow(
    deliverableTableElement.rows.length - 1,
    deliverableTableElement.rows.length
  );


    // NO.
    const td01 = document.createElement("td");
    td01.appendChild(createInputTextElement(""));
    addRowTR.appendChild(td01);
    
    // 태스크명
    const td02 = document.createElement("td");
    td02.appendChild(createInputTextElement("textLF"));
    addRowTR.appendChild(td02);
    
    // 산출물
    const td03 = document.createElement("td");
    td03.appendChild(createInputTextElement("textLF"));
    addRowTR.appendChild(td03);
    
    // 프로젝트 롤
    const td04 = document.createElement("td");
    td04.appendChild(createInputTextElement("textLF"));
    addRowTR.appendChild(td04);
    
    // 인증타입
    const td05 = document.createElement("td");
    td05.appendChild(createInputTextElement("textLF"));
    addRowTR.appendChild(td05);
    
    // Phase
    const td06 = document.createElement("td");
    td06.appendChild(createInputTextElement("textLF"));
    addRowTR.appendChild(td06);
    
    // 작업 현황
    const td07 = document.createElement("td");
    td07.appendChild(createInputTextElement("textLF"));
    addRowTR.appendChild(td07);
    
    // 문서
    const td08 = document.createElement("td");
    td08.appendChild(createInputTextElement("textLF"));
    addRowTR.appendChild(td08);
    
    // 최초 등록일
    const td09 = document.createElement("td");
    td09.appendChild(createInputTextElement("textLF"));
    addRowTR.appendChild(td09);

    //table.appendChild(tableTR);

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
  
  const addRowTR = deliverableTableElement.insertRow(-1);
  const addRowTD = document.createElement("td");
  addRowTD.classList.add("sub_menu");

  const addButton = document.createElement("button");
  addButton.innerText = "+";
  addButton.classList.add("row_add_button");
  addButton.addEventListener("click", addDataRow);
  
  addRowTD.appendChild(addButton);
  addRowTR.appendChild(addRowTD);
  
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
  deliverableTableElement.deleteRow(-1);
};

// data.js > datas를 보낸다.
addTableRows(datas);
loadCookies();