selectOptions = [
  { title: "", classname: "none" },
  { title: "G", classname: "signal_G" },
  { title: "Y", classname: "signal_Y" },
  { title: "R", classname: "signal_R" },
  { title: "N/A", classname: "signal_NA" },
];

// select의 값을 변경할때 발생되는 이벤트
selectClassChange = (e) => {
	let select = e.target;
	for(let si=0; si<selectOptions.length; si++){
		const className = selectOptions[si].classname;
		if(select.classList.contains(className)){
			select.classList.remove(className);
		}
	}
	select.classList.add(select.value);
};

// Option element 생성
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
	
	if("signal_NA" === classname){
		optionEle.style = "display:none;"
	}

    selectEle.appendChild(optionEle);
  });
};

// Select element 생성
createSelect = (name, value) => {
	let selectEle = document.createElement("select");
	selectEle.name = name;
	selectEle.classList.add("ds");
	createSelectOptions(selectEle, selectOptions, value);
	selectEle.addEventListener("change", selectClassChange);
	selectEle.disabled = "disabled";
	
	return selectEle;
};

// table들에 datas를 기반으로 데이터를 추가
addTableRows = (tableID, data) => {
	let tbody = document.querySelector("#" + tableID + " tbody");
   	//add Child
	let childTitle = "";
	let parentCnt = 0;
	let rowSpan = 0;
    let bfText = "";
	let oTDList = [];
 	for (let child of data.child) {
		//add Title
		let parent = data.parent[parentCnt];
		let parentCode = parent? parent.code : "";
		childTitle = child.parentCode;
		if(parentCode === childTitle){
			let titleTR = document.createElement("tr");
	   		let titleTD = document.createElement("td");
			titleTD.innerHTML = parent.name;
	   		titleTD.colSpan = 8;
	   		titleTD.classList.add("subdivision");
	
	   		titleTR.appendChild(titleTD);
	   		tbody.appendChild(titleTR);
			
			parentCnt++;
		}
		
		//add Child
		let childTR = document.createElement("tr");
		
		//code
		let codeTD = document.createElement("td");
		codeTD.innerHTML = child.code;
		codeTD.classList.add("borderGrey");
		let inputOid = createHiddenInput("oid", child.oid);
		let inputCode = createHiddenInput("code", child.code);
		let inputName = createHiddenInput("name", child.name);
		let inputSeq = createHiddenInput("seq", child.seq);
		codeTD.appendChild(inputOid);
		codeTD.appendChild(inputCode);
		codeTD.appendChild(inputName);
		codeTD.appendChild(inputSeq);
		childTR.appendChild(codeTD);
		
		//name -- colspan
		// 다를 때
        if (bfText != child.name) {
			//전에 같다가 현재 다름
			for(let ori_td_idx=0; ori_td_idx<oTDList.length; ori_td_idx++){
				const ori_td = oTDList[ori_td_idx];
				ori_td.setAttribute("rowspan", rowSpan+1);
			}
			createTDNameToSelect(childTR, child, oTDList = []);
			bfText = child.name;
			rowSpan = 0;
        }
		//같을 때
		else {			
            rowSpan++;
        }		
		//add tr
		tbody.appendChild(childTR);
	}
};
// hidden 타입의 input element 생성
createHiddenInput = (name, value) => {
  let inputText = document.createElement("input");
  inputText.name = name;
  inputText.value = value;
  inputText.type = "hidden";
  inputText.addEventListener("keypress", textEnterPress);
  return inputText;
};

//Name, 0~5 Select TD 생성
createTDNameToSelect = (childTR, child, oTDList) => {
	let nameTD = document.createElement("td");
	nameTD.innerHTML = child.name;
	childTR.appendChild(nameTD);
	oTDList.push(nameTD);
	
	//0~5
	const subTitleList = ["value0", "value1", "value2", "value3", "value4", "value5"];
	for(let i=0; i<subTitleList.length; i++){
		let selectTD = document.createElement("td");
		const value = child[subTitleList[i]];
		let select = createSelect(subTitleList[i], value);
		if(value){
			select.classList.add(value);
		}
		selectTD.appendChild(select);
		childTR.appendChild(selectTD);
		oTDList.push(selectTD);
	}
}