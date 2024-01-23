

//저장 <-> 수정 버튼
toggleUpdateBtn_GR= (e, btnText) => {
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
	documentInputTextsControll_GR(target);
};

// 저장 <-> 수정 버튼 클릭시 해당하는 element 들의 disabled 값을 toggle 한다.
documentInputTextsControll_GR = (target) => {
		
  const texts = document.querySelectorAll(
    `
    ${target ? "." + target + " " : ""}input[type="text"],
    ${target ? "." + target + " " : ""}input[type="number"],
  	${target ? "." + target + " " : ""}select,
    ${target ? "." + target + " " : ""}textArea
    `
  );
  texts.forEach((element) => {
    element.toggleAttribute("disabled");
  });
};

