//데이터로 테이블 추가
getTableRows = (list) => {
	let tbody = document.querySelector("#cstopTable > tbody");
	let isHeader = true;
	for (let data of list) {
		let addRowTR = document.createElement("tr");
		tbody.appendChild(addRowTR);
		
		setTableRows(addRowTR, data, isHeader);
		isHeader = false;
	}
};

// textarea element 생성
createTextAreaElement = (name, value, disabled) => {
  let textArea = document.createElement("textArea");
  textArea.name = name;
  textArea.value = value || "";
  textArea.disabled = disabled;
  return textArea;
};
//row 추가 시 td 및 data 설정
setTableRows = (addRowTR, data, isHeader) => {
	// KRW
	let krwTD = document.createElement("th");
	
	const code = data && data.code || "";
	if(code.includes("CHART")){
		krwTD.style = "background-color: #e2e0e0; font-size: 14px;";
	}
	let inputOid = createInputHiddenElement("oid", data && data.oid || "");
	let inputName = createInputHiddenElement("name", data && data.name || "");
	let inputCode = createInputHiddenElement("code", data && data.code || "");
	
	//krwTD.innerText = data.name;
	krwTD.innerHTML = "<span onclick='viewObject(\""+data.oid+"\")'>"+data.name+"</span>";
	krwTD.appendChild(inputOid);
	krwTD.appendChild(inputName);
	krwTD.appendChild(inputCode);
	
	addRowTR.appendChild(krwTD);
	
	// value0~6
	for(let vi=0; vi<=6; vi++){
		if(code.includes("DESC")){
			if(vi != 6){
				let valueTD = document.createElement("td");
				valueTD.colSpan = 2;
				let value = data && data["value" + vi] || "";
				let valueName = "value" + vi;
				let input = createTextAreaElement(valueName, value, data);
				console.log(input.span);
				valueTD.appendChild(input);
				addRowTR.appendChild(valueTD);
			}
		}else{
		
			let valueTD = document.createElement("td");
			let value = data && data["value" + vi] || "";
			let valueName = "value" + vi;
			let input = createInputTextElement(valueName, value, data);
			input.oninput = function(){
				this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');
			};
			
			let span = createSpanTextElement(valueName, value, data);
			valueTD.appendChild(input);
			valueTD.appendChild(span);
			setInputEvent(input, isHeader);
			addRowTR.appendChild(valueTD);
			if(vi != 6){
				let td = document.createElement("td");
				td.classList.add(valueName + "NTD");
				const afValue = data && (data["value" + (vi + 1)] || "");
				let tmpValue = "";
				if((afValue && value) && (!isNaN(afValue) && !isNaN(value))){
					tmpValue = (afValue - value);
					tmpValue = setNumberFormat(tmpValue, data.code);
				}
				td.innerText = tmpValue;
				addRowTR.appendChild(td);
			}
		}
	}
}

// hidden 타입의 input element 생성
createInputHiddenElement = (name, value) => {
  let inputText = document.createElement("input");
  inputText.name = name;
  inputText.type = "hidden";
  inputText.value = value;
  return inputText;
};

// text 타입의 input element 생성
createInputTextElement = (name, value, data) => {
	let inputText = document.createElement("input");
	inputText.classList.add("c_stop_inputView");
	inputText.style.display = "none";
	inputText.name = name;
	inputText.type = "text";
	inputText.addEventListener("keypress", textEnterPress);
	inputText.value = value || "";
	inputText.disabled = data;
	return inputText;
};

// text를 넣을 수 있도록 span 생성
createSpanTextElement = (name, value, data) => {
	let span = document.createElement("span");
	span.classList.add("c_stop_spanView");
	span.style.display = "";
	span.name = name;
	let number = value || "";
	span.innerText = setNumberFormat(Number(number), data.code);
	return span;
};

//input 이벤트 : set Span Text, GR 간 차이 계산
setInputEvent = (input, isHeader) => {
	input.addEventListener("change", (e) => {
		const searchStr = "value";
		let input = e.target;
		let inputName = input.name;
		let inputNum = inputName.substring(inputName.indexOf(searchStr) + searchStr.length, inputName.length);
		
		//set Span Text
		let td = e.target.parentNode;
		let tr = td.parentNode;
		let code = tr.querySelector("th > input[name='code']").value;
		let span = td.querySelector("span");
		span.innerText = setNumberFormat(input.value, code);
		
		//GR 간 차이 계산
		let bfInput = tr.querySelector("input[name=" + searchStr + (Number(inputNum) - 1) + "]");
		let afInput = tr.querySelector("input[name=" + searchStr + (Number(inputNum) + 1) + "]");
		
		if(input.value){
			if(bfInput){
				let bf_result = "";
				let bf_td = tr.querySelector("td." + bfInput.name + "NTD");
				if(bfInput.value && (!isNaN(bfInput.value) && !isNaN(input.value))){
					bf_result = input.value - bfInput.value;
					bf_result = setNumberFormat(bf_result, code);
				}
				bf_td.innerText = bf_result;
			}
			if(afInput){
				let result = "";
				let cur_td = tr.querySelector("td." + input.name + "NTD");
				if(afInput.value && (isNaN(afInput.value) && isNaN(input.value))){
					result = afInput.value - input.value;
					bf_result = setNumberFormat(result, code);
				}
				cur_td.innerText = result;
			}
		}else{
			if(bfInput){
				let bf_td = tr.querySelector("td." + bfInput.name + "NTD");
				bf_td.innerText = "";
			}
			if(afInput){
				let cur_td = tr.querySelector("td." + input.name + "NTD");
				cur_td.innerText = "";
			}
		}
	});
	if(!isHeader){
		input.onkeypress = (e) => {
			let code = e.which? e.which : e.keyCode;
			if(code < 48 || code > 57){
				if(code != 45 && code != 46){
					return false;
				}
			}
		}
	}
}

//toggle display spanView, inputView
toggleSpanInputView = () => {
	let ilist = document.querySelectorAll(".c_stop_inputView");
	let slist = document.querySelectorAll(".c_stop_spanView");
	
	for(let elem of ilist){
		elem.style.display = elem.style.display === "none"? "" : "none"; 
	}
	for(let elem of slist){
		elem.style.display = elem.style.display === "none"? "" : "none"; 
	}
}

//Number Format 변경
setNumberFormat = (num, code) => {
	let str = "";
	if(num === 0){
		str = "";
	}
	//0,000.0
	else if(code === "CSTOP1" || code === "CSTOP14"){
		str = numeral(num).format('0,0.0');
	}
	//0,000.00
	else if(code === "CSTOP12"
	 || code === "CSTOP13"
	 || code === "CHART2"
	 || code === "CSTOP15"
	 || code === "CSTOP16"){
		str = numeral(num).format('0,0.00');
	}
	//0,000.0%
	else if(code === "CHART3" || code === "CSTOP18"){
		str = numeral(num).format('0,0.0%');
	}
	//0,000
	else{
		str = numeral(num).format('0,0');
	}
	//-0.0, 0.0
	if(str === "-0.0" || str === "0.0"){
		str = "0";
	}
	return str;
}