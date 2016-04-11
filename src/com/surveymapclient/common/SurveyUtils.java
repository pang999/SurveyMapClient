package com.surveymapclient.common;

import android.graphics.Color;

public class SurveyUtils {

	public static String getColor(int color){
		if (color==Color.RED) {
			return "红色";
		}else if (color==Color.BLUE) {
			return "蓝色";
		}else if (color==Color.GREEN) {
			return "绿色";
		}else{
			return "黑色";
		}		
	}
	public static String getStyle(boolean isfull){
		if (isfull) {
			return "实线";
		}else {
			return "虚线";
		}
	}
}
