// 수정 <-> 저장 버튼 이벤트 처리
document.querySelectorAll(".property_update").forEach((element) => {
  element.addEventListener("click", (e) => {
    const button = e.target;
    const target = button.dataset.target;

    if (button.classList.contains("property_update")) {
      button.classList.remove("property_update");
      button.classList.add("property_submit");
      documentInputTextsControll(false, target);
    } else {
      button.classList.add("property_update");
      button.classList.remove("property_submit");
      documentInputTextsControll(true, target);
    }
  });
});

// element 에 class 들을 추가한다.
const addClassnames = (element, classnames) => {
  for (const classname of classnames) {
    element.classList.add(classname);
  }
};

// target이 되는 number타입의 input element 값을 수정할때 발생하는 이벤트
const calculationByTarget = (value, row, column) => {
  for (let temp = 1; temp <= 3; temp++) {
    const source = document.querySelector(`#source_${row + temp}_${column}`);
    const result = (source.value / value) * 100;
    console.log(source.value, value, result);

    document.querySelector(
      `#result_${row + temp}_${column + 1}`
    ).innerText = isNaN(result) ? 0 : result;
  }
};

// source가 되는 number타입의 input element 값을 수정할때 발생하는 이벤트
const calculationBySource = (value, row, column) => {
  for (let temp = 1; temp <= 3; temp++) {
    const target = document.querySelector(`#target_${row - temp}_${column}`);
    if (target) {
      const result = (value / target.value) * 100;
      console.log(value, target.value, result);
      document.querySelector(`#result_${row}_${column + 1}`).innerText = isNaN(
        result
      )
        ? 0
        : result;
    }
  }
};

// number 타입의 input element에 대한 이벤트 처리
const validateNumberInput = (e) => {
  if (e.target.validationMessage) {
    alert(e.target.validationMessage);
    e.target.focus();
  } else {
    const row = +e.target.dataset.row;
    const column = +e.target.dataset.column;
    const value = +e.target.value;

    if (e.target.id === `target_${row}_${column}`) {
      calculationByTarget(value, row, column);
    } else if (e.target.id === `source_${row}_${column}`) {
      calculationBySource(value, row, column);
    }
  }
};

// text 값을 출력해주는 span element를 생성한다.
const createLabelElement = (column) => {
  const span = document.createElement("span");
  span.innerText = column.text;
  if (column?.classnames) addClassnames(span, column.classnames);
  return span;
};

// text 타입의 input element를 생성한다.
const createTextElement = (column) => {
  const input = document.createElement("input");
  input.type = "text";
  input.disabled = "disabled";
  if (column?.classnames) addClassnames(input, column.classnames);
  if (column?.id) input.id = column.id;
  return input;
};

// 계산에 사용되는 number 타입의 input element를 생성한다.
const createNumberElement = (rowIndex, columnIndex, column) => {
  const input = document.createElement("input");
  input.type = "number";
  input.disabled = "disabled";
  if (column?.classnames) addClassnames(input, column.classnames);
  if (column?.id) input.id = `${column.id}_${rowIndex}_${columnIndex}`;
  input.dataset.row = rowIndex;
  input.dataset.column = columnIndex;
  input.addEventListener("keypress", textEnterPress);
  input.addEventListener("focusout", validateNumberInput);
  return input;
};

// 계산된 결과를 출력하는 span element를 생성
const createResultElement = (rowIndex, columnIndex, column) => {
  const span = document.createElement("span");
  span.innerText = 0;
  if (column?.classnames) addClassnames(span, column.classnames);
  span.id = `result_${rowIndex}_${columnIndex}`;
  span.dataset.row = rowIndex;
  span.dataset.column = columnIndex;
  return span;
};

// rows안의 각 칼럼 정의에 따른 tr element를 생성한다.
const createTableRow = (rowIndex, row) => {
  const tr = document.createElement("tr");

  row.forEach((column, columnIndex) => {
    const td = document.createElement("td");
    if (column?.colspan) td.colSpan = column.colspan;
    if (column?.rowspan) td.rowSpan = column.rowspan;
    switch (column.type) {
      case "label":
        td.appendChild(createLabelElement(column));
        break;
      case "text":
        td.appendChild(createTextElement(column));
        break;
      case "number":
        td.appendChild(createNumberElement(rowIndex, columnIndex, column));
        break;
      case "result":
        td.append(createResultElement(rowIndex, columnIndex, column));
        td.classList.add("tac");
        break;
    }

    if (column.type !== "none") tr.appendChild(td);
  });

  return tr;
};

// body element에 data.js > rows 의 길이만큼 추가한다.
const createTableBody = () => {
  const body = document.createElement("tbody");

  rows.forEach((row, rowIndex) => {
    body.appendChild(createTableRow(rowIndex, row));
  });
  return body;
};

// table에 생성된 body element를 추가
const addTableRows = () => {
  const table = document.querySelector("table.productDev");
  table.appendChild(createTableBody());
};

//////////////////이미지 드래그 시작
var uploadFiles = [];
var $drop = $("#drop");
$drop.on("dragenter", function(e) { //드래그 요소가 들어왔을떄
	$(this).addClass('drag-over');
}).on("dragleave", function(e) { //드래그 요소가 나갔을때
	$(this).removeClass('drag-over');
}).on("dragover", function(e) {
	e.stopPropagation();
	e.preventDefault();
}).on('drop', function(e) { //드래그한 항목을 떨어뜨렸을때
	e.preventDefault();
	$(this).removeClass('drag-over');
	var files = e.originalEvent.dataTransfer.files; //드래그&드랍 항목
	for(var i = 0; i < files.length; i++) {
		var file = files[i];
		var size = uploadFiles.push(file); //업로드 목록에 추가
		preview(file, size - 1); //미리보기 만들기
	}
	
	$("#preImage").hide();
});

function preview(file, idx) {
	var reader = new FileReader();
	reader.onload = (function(f, idx) {
		return function(e) {
			var div = '<div class="thumb"> \
				<div class="close" data-idx="' + idx + '">X</div> \
				<img src="' + e.target.result + '" title="' + escape(f.name) + '"/> \
				</div>';
			$("#thumbnails").append(div);
		};
	})(file, idx);
	
	reader.readAsDataURL(file);
}

$("#thumbnails").on("click", ".close", function(e) {
	var $target = $(e.target);
	var idx = $target.attr('data-idx');
	uploadFiles[idx].upload = 'disable'; //삭제된 항목은 업로드하지 않기 위해 플래그 생성
	$target.parent().remove(); //프리뷰 삭제
});

//////////////////이미지 드래그 끝

addTableRows();
