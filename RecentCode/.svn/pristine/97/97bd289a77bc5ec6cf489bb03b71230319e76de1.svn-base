//#################### 공통 - 체크박스 라디오 관련 ####################

/**
 * checkboxName 에 해당하는 체크박스 전체 토글
 */
function toggleCheckbox(checkboxName, eventFiring) {
	if (eventFiring !== false) {
		eventFiring = true;
	}
	var controlCheckbox = event.srcElement;

	var checkboxes = document.getElementsByName(checkboxName);
	for ( var i = 0; i < checkboxes.length; i++) {
		var checkbox = checkboxes[i];
		checkbox.checked = controlCheckbox.checked;
		if (eventFiring) {
			try {
				checkbox.fireEvent("onclick");
			} catch (e) { }
		}
	}
}

/**
 * 체크된 체크박스 갯수를 얻는다
 * 
 * @param checkboxName
 * @returns
 */
function getCheckedCheckboxCount(checkboxName) {
	return getCheckedCheckboxes(checkboxName).length;
}

/**
 * 선택되어진 체크박스들를 얻는다
 * 
 * @param checkboxName
 */
function getCheckedCheckboxes(checkboxName) {
	var checkedCheckboxes = [];

	var checkboxes = document.getElementsByName(checkboxName);
	for ( var i = 0; i < checkboxes.length; i++) {
		if (checkboxes[i].checked) {
			checkedCheckboxes.push(checkboxes[i]);
		}
	}
	return checkedCheckboxes;
}

function getAllCheckboxes(checkboxName) {
	var checkedCheckboxes = [];

	var checkboxes = document.getElementsByName(checkboxName);
	for ( var i = 0; i < checkboxes.length; i++) {
			checkedCheckboxes.push(checkboxes[i]);
	}
	return checkedCheckboxes;
}

/**
 * 주어진 값에 해당하는 체크박스 찾기
 * 
 * @param checkboxName
 * @param checkboxValue
 */
function getCheckboxByValue(checkboxName, checkboxValue) {
	var checkbox = null;

	var checkboxes = document.getElementsByName(checkboxName);
	for ( var i = 0; i < checkboxes.length; i++) {
		if (checkboxes[i].value == checkboxValue) {
			checkbox = checkboxes[i];
			break;
		}
	}
	return checkbox;

}

/**
 * 선택되어진 라디오를 얻는다. 체크된 것이 없다면 null
 * 
 * @param radioName
 */
function getCheckedRadio(radioName) {
	var radios = document.getElementsByName(radioName);
	for ( var i = 0; i < radios.length; i++) {
		var radio = radios[i];
		if (radio.checked) {
			return radio;
		}
	}
	return null;
}

/**
 * 선택되어진 라디오의 값을 조회한다
 * @param radioName
 * @returns 체크되지 않았다면 null
 */
function getCheckedRadioValue(radioName) {
	var checkedRadio = getCheckedRadio(radioName);
	if (checkedRadio != null) {
		return checkedRadio.value;
	} else {
		return null;
	}
}