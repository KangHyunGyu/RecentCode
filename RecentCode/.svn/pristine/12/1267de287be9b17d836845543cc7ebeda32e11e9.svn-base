/**
 * FileSaver.min.js와 xlsx.full.min.js가 import되어야 함.
 * StageGate 용 Excel Export
 */

function s2ab(s) { 
    var buf = new ArrayBuffer(s.length); //convert s to arrayBuffer
    var view = new Uint8Array(buf);  //create uint8array as viewer
    for (var i=0; i<s.length; i++) view[i] = s.charCodeAt(i) & 0xFF; //convert to octet
    return buf;    
}
function exportExcel(tableId, fileName, sheetName){ 
    // step 1. workbook 생성
    var wb = XLSX.utils.book_new();

    // step 2. 시트 만들기 
	let exportTable = convertTable(tableId);
    var newWorksheet = XLSX.utils.table_to_sheet(exportTable);
    
    // step 3. workbook에 새로만든 워크시트에 이름을 주고 붙인다.  
    XLSX.utils.book_append_sheet(wb, newWorksheet, sheetName);

    // step 4. 엑셀 파일 만들기 
    var wbout = XLSX.write(wb, {bookType:'xlsx',  type: 'binary'});

    // step 5. 엑셀 파일 내보내기 
    saveAs(new Blob([s2ab(wbout)],{type:"application/octet-stream"}), fileName);
}

//테이블을 기본형태로 변환한다.
function convertTable(tableId){ 
	const table = document.getElementById(tableId);
	let exportTable = table.cloneNode(true);
	//input[type=hidden] 제외
	for(let elem of exportTable.querySelectorAll('input[type=hidden]')) { elem.remove(); };
	//input[type=text], textarea 변경
	for(let elem of exportTable.querySelectorAll('input[type=text], textarea')) { elem.replaceWith(elem.value); };
	//select 변경
	for(let sel of exportTable.querySelectorAll('select')) {
		let value = sel.options[sel.selectedIndex].value;
		 sel.replaceWith(value);
	};
	return exportTable;
}

//테이블을 Json 형태로 변환한다.
function tableToJson(tableId){ 
	let table = convertTable(tableId);
	let list = [];
	//row
	for(let tr of table.querySelectorAll('tr')) {
		let row = [];
		//col
		for(let td of tr.querySelectorAll('th, td')) {
			let col = new Object();
			col.rowspan = td.getAttribute("rowspan");
			col.colspan =  td.getAttribute("colspan");
			col.value = td.innerText;
			row.push(col);
		}
		list.push(row);
	}
	return list;
}

function exportExcelForm(params, url) {
	let form = document.createElement("form");
	form.setAttribute("method", "post");
    form.setAttribute("action", url);
	
	for(let key of Object.keys(params)){
		let inputOid = document.createElement("input");
		
		inputOid.setAttribute("type", "hidden");
		inputOid.setAttribute("name", key);
		inputOid.setAttribute("value", params[key]);
		form.appendChild(inputOid);
	}
	
	document.body.appendChild(form);
	form.submit();
}