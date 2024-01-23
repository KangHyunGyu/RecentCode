// 수정 <-> 저장 버튼 이벤트
document.querySelector(".property_update").addEventListener("click", (e) => {
  const button = e.target;
  if (button.classList.contains("property_update")) {
    button.classList.remove("property_update");
    button.classList.add("property_submit");
    documentInputTextsControll(false);
    setDisplayCalButton(true);
  } else {
    button.classList.add("property_update");
    button.classList.remove("property_submit");
    documentInputTextsControll(true);
    setDisplayCalButton(false);
  }
});



// label 과 값을 출력
const setLabels = ({ labels }) => {
  for (let id in labels) {
    const element = document.querySelector(`#${id}`);
    if (element.tagName === "SPAN")
      document.querySelector(`#${id}`).innerHTML = labels[id];
    else if (element.tagName === "INPUT")
      document.querySelector(`#${id}`).value = labels[id];
  }
};

// webix chart 생성
const renderChart = ({ charts }) => {
  const chartCols = [];
  const chartsPanel = document.querySelector(".chartsPanel");
  for (let data of charts) {
    chartCols.push({
      rows: [
        {
          template: data.title,
          height: 30,
        },
        {
          view: "chart",
          data: data.values,
          height: 140,
          xAxis: {
            template: "#label#",
          },
          yAxis: {
          	start:data.ystart,
	        end:data.yend,
	        step:data.ystep,
          	template: function(value){
          		if(data.yaxis_tem!=null && data.yaxis_tem.length > 0){
          			value = value + data.yaxis_tem;
          		}
			    return value;
			}
            //step: 10,
          },
          series: [
            {
            //data
              type: "line",
              value: "#value#",
              tooltip: {
                template: "#value#",
              },
              item: {
              	borderColor: data.target_color
              },
              line: {
              	width: 5,
              	color: data.target_color
              }
            },
            {
            //target
              type: "line",
              value: data.target,
              
              item: { 
              	radius: 0
              },
              line: {
              	color: data.target_color
              },
              tooltip: {
                template: data.plot,
              },
            },
          ],
        },
      ],
    });
  }

  webix.ui({
    container: "chartsPanel",
    cols: chartCols,
  });
};

// type 타입의 input element 에서 keypress 시에 이벤트 처리
document.querySelectorAll("input[type=text]").forEach((element) => {
  element.addEventListener("keypress", textEnterPress);
});

//공통 : doubleCalendar
$(".datePicker").each(function(){
	var id = $(this).attr("id");

	$(this).after("<img class='pointer' src='/Windchill/jsp/portal/images/calendar_icon.png' name='" + id + "Btn' id='" + id + "Btn'>");
	
	/*$("#" + id + "Btn").after("<span class='resetDate pointer' data-remove-target='" + id + "'><img class='verticalMiddle' src='/Windchill/jsp/portal/images/delete_icon.png'></span>");*/
    
	$(".resetDate").click(function(){
		
		var target = $(this).data("remove-target");
		
		$("#" + target).val("");
	});
	
	var locale = {
		ko: {
			monthShort: ["1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"],
			months: ["1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"],
			daysShort: ["일","월","화","수","목","금","토"],
			days: ["일","월","화","수","목","금","토"]
		},
		en: {
			monthShort: ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"],
			months: ["January","February","March","April","May","Jun","July","August","September","October","November","December"],
			daysShort: ["Sun","Mon","Tue","Wed","Thu","Fri","Sat"],
			days: ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"]
		}
	}

	var myCalendar = new dhtmlXCalendarObject({
		input:id,
		button:id+"Btn"
	});
	
	myCalendar.setDateFormat("%Y/%m/%d");
	myCalendar.setWeekStartDay(7);
	
	if("ENDDATE" == id) {
		var date = new Date();
		date.setDate(date.getDate() + 1);
		myCalendar.setInsensitiveRange(date, null);
	}
});

const setDisplayCalButton = (isDisplay) => {
	if(isDisplay){
		$(".pointer").show();
	}else{
		$(".pointer").hide();
	}
}

const inputCalendarTextElement = (classname) => {
	const inputText = document.createElement("input");
  	inputText.type = "text";
  	inputText.classList.add("datePicker");
  	inputText.classList.add("w125");
  	if (classname) inputText.classList.add(classname);
  	inputText.setAttribute("disabled", "disabled");
 	inputText.addEventListener("keypress", textEnterPress);
  	return inputText;

}

// 기본정보, 구성원, Project main objectives 의 값을 설정
setLabels(datas);

// chart를 그린다.
renderChart(datas);

$(".pointer").hide();