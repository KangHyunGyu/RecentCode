// 펼치기 - 접기 이미지 클릭시 이벤트 처리
toggleImgBinding = () => {
  const imgs = document.querySelectorAll("img.content--toggle");
  imgs.forEach((img) => {
    img.addEventListener("click", function (e) {
      const target = e.target.dataset.target;
      if (!target) return;
      const toggle = document.querySelector("." + target).style.display;
      if (toggle) {
        e.target.src = "/Windchill/jsp/portal/images/minus_icon.png";
        document.querySelector("." + target).style.display = "";
      } else {
        e.target.src = "/Windchill/jsp/portal/images/add_icon.png";
        document.querySelector("." + target).style.display = "none";
      }
    });
  });
};

// tap 메뉴 클릭시 이벤트
document.querySelectorAll(".tap.pt15 ul li").forEach((tapElement) => {
  tapElement.addEventListener("click", function (e) {
    const module = e.target.dataset.module;
    const navigate = document.createElement("a");
    navigate.href = `./modules/${module}/${module}.html`;
    document.querySelector("body").appendChild(navigate);
    navigate.click();
  });
});

// 저장 <-> 수정 버튼 클릭시 해당하는 element 들의 disabled 값을 toggle 한다.
documentInputTextsControll = (target) => {
  const texts = document.querySelectorAll(
    `
    ${target ? "." + target + " " : ""}input[type="text"],
    ${target ? "." + target + " " : ""}input[type="number"],
  	${target ? "." + target + " " : ""}select,
    ${target ? "." + target + " " : ""}textArea
    `
  );
  //disabled 조건
  texts.forEach((element) => {
	if(!element.classList.contains("ds") || !element.classList.contains("signal_NA")){
	    element.toggleAttribute("disabled");
	}
  });
};

// enter를 눌렀을 때 해당 element 에서 focus를 해제
textEnterPress = (e) => {
  if (e.charCode === 13) {
    e.target.blur();
  }
};

//저장 <-> 수정 버튼
toggleUpdateBtn = (e, btnText) => {
	const button = e.target;
	const target = button.dataset.target;
	//수정 버튼
	if (button.classList.contains("property_update")) {
		button.classList.remove("property_update");
		button.classList.add("property_submit");
	} 
	//저장버튼
	else {
		button.classList.add("property_update");
		button.classList.remove("property_submit");
	}
	button.innerText = btnText;
	button.value =btnText;
	documentInputTextsControll(target);
};

//테이블 저장 시 데이터 가져오기
getTableData = (tableID, mergeIdx) => {
	let tdArr = [];
	let trList = document.querySelectorAll("#" + tableID + " tbody tr");
	let oriTr = [];
	let rowspan = 0;
	//row
	for(let row=0; row<trList.length; row++){
		let tr = trList[row];
		
		if(mergeIdx){
			let tdList = tr.querySelectorAll("td");
			let td = tdList[mergeIdx];
			if(td){
				rowspan = td.getAttribute("rowspan");
				if(rowspan > 1){
					oriTr = tr;
				}
			}
		}
		
		//getTdData
		tdArr = getTdData(tdArr, row, tr);
		
		//getTdData rowspan
		if(rowspan > 1){
			tdArr = getTdData(tdArr, row, oriTr);
		}
	}
	return tdArr;
}

//td 데이터 가져오기
getTdData = (tdArr, row, tr) => {
	let tdList = tr.querySelectorAll("th, td");
	//console.log("tdList", tdList);
	for(let ti=0; ti<tdList.length; ti++){
		let td = tdList[ti];
		let eleItemList = getEleDataList(td);
		
		for(eleItem of eleItemList){
			let selTD = tdArr[row];
			if(selTD){
				if(!selTD[eleItem.name]){
					selTD[eleItem.name] = eleItem.value;
				}
			}else{
				let item = new Object();
				item[eleItem.name] = eleItem.value;
				tdArr[row] = item;
			}
		}
	}
	return tdArr;
}

//Element Name, Value 가져오기
getEleData = (td) => {
	let item = new Object();
	let input = td.querySelector("input[type='text'], textarea");
	let sel = td.querySelector("select");
	if(input){
		item.name = input.name;
		item.value = input.value;
	}else if(sel){
		let selOpt = sel.options[sel.selectedIndex];
		item.name = sel.name;
		item.value = selOpt.value;
		
	}
	return item;
}

//Element Name, Value 가져오기
getEleDataList = (td) => {
	let itemList = [];
	let inputList = td.querySelectorAll("input[type='text'], input[type='hidden'], textarea");
	let selList = td.querySelectorAll("select");
	for(let ii=0; ii<inputList.length; ii++){
		let item = new Object();
		let input = inputList[ii];
		item.name = input.name;
		item.value = input.value;
		itemList.push(item);
	}
	for(let si=0; si<selList.length; si++){
		let item = new Object();
		let sel = selList[si];
		let selOpt = sel.options[sel.selectedIndex];
		item.name = sel.name;
		item.value = selOpt.value;
		itemList.push(item);
	}
	return itemList;
}

//수정 중 보이지 않도록 함.
toggleImportBtn = (diplay) =>{
	let importBtn = document.getElementById("import");
	if(diplay){
		importBtn.style = "";
	}else{
		importBtn.style = "display: none;";
	}
}