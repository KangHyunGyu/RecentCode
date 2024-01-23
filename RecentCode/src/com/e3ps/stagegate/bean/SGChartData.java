package com.e3ps.stagegate.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.e3ps.common.util.StringUtil;
import com.e3ps.stagegate.SGObjectValue;
import com.e3ps.stagegate.util.StageGateUtil;

public class SGChartData {
	private String title;
	private String values;
	private int ystart;
	private int yend;
	private int ystep;
	private double target;
	private String target_color;
	private int plot;
	
	public SGChartData(SGObjectValue objValue) throws JSONException {
		JSONObject jObj = new JSONObject(StringUtil.checkNull(objValue.getDivision()));
		String code = jObj.getString("code");
		String value0 = jObj.getString("value0");
		String value1 = jObj.getString("value1");
		String value2 = jObj.getString("value2");
		String value3 = jObj.getString("value3");
		String value4 = jObj.getString("value4");
		String value5 = jObj.getString("value5");
		String targetValue = jObj.getString("value6");
		
		String[] stringValues = {value0, value1, value2,
				value3, value4, value5};
		
		double intValue0 = 0;
		double intValue1 = 0;
		double intValue2 = 0;
		double intValue3 = 0;
		double intValue4 = 0;
		double intValue5 = 0;
		double targetIntValue = 0;
		
		if(!targetValue.isEmpty()) {
			targetIntValue = Double.parseDouble(targetValue);
		}
		
		double[] intValues = {intValue0,intValue1,intValue2,
				intValue3,intValue4,intValue5,targetIntValue};
		
		JSONArray jArray = new JSONArray();
		for(int i=0; i < stringValues.length; i++) {
			JSONObject listObject = new JSONObject();
			String label = String.valueOf(i+1);
			if(!stringValues[i].isEmpty()) {
				intValues[i] = Double.parseDouble(stringValues[i]);
				listObject.put("id", i+1);
				listObject.put("value", stringValues[i]);
				listObject.put("label", label);
			}else {
				listObject.put("label", label);
			}
			jArray.put(listObject);
			
		}
		
		double maxVal = max(intValues);
		double step = step(maxVal);
		
		this.title = jObj.getString("name");
		this.values = jArray.toString();
	    this.yend = (int) maxVal;
	    this.ystep = (int) step;
	    this.target = targetIntValue;
	    this.target_color = StageGateUtil.getChartColor(code);
	    this.plot = 15;
	    if("CHART1".equals(code)) {
	    	this.plot = 50;
	    }
		
	}
	
	public static double max(double[] data){
		 
        if( data == null || data.length == 0){
            return 1;
        }
 
        double maxValue = data[0];
        for (int i = 0; i < data.length; i++){
 
            if(maxValue < data[i]){
                maxValue = data[i];
            }
        }
        maxValue = Math.ceil(maxValue / 100) * 100;
        return maxValue;
    }
	
	public static double step(double maxVal){
		maxVal = Math.ceil(maxVal / 5);
        return maxVal;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

	public int getYstart() {
		return ystart;
	}

	public void setYstart(int ystart) {
		this.ystart = ystart;
	}

	public int getYend() {
		return yend;
	}

	public void setYend(int yend) {
		this.yend = yend;
	}

	public int getYstep() {
		return ystep;
	}

	public void setYstep(int ystep) {
		this.ystep = ystep;
	}

	public double getTarget() {
		return target;
	}

	public void setTarget(double target) {
		this.target = target;
	}

	public String getTarget_color() {
		return target_color;
	}

	public void setTarget_color(String target_color) {
		this.target_color = target_color;
	}

	public int getPlot() {
		return plot;
	}

	public void setPlot(int plot) {
		this.plot = plot;
	}

}
