package com.surveymapclient.common;

import android.graphics.Color;

public class SurveyUtils {

	public static String getColor(int color){
		if (color==Color.RED) {
			return "��ɫ";
		}else if (color==Color.BLUE) {
			return "��ɫ";
		}else if (color==Color.GREEN) {
			return "��ɫ";
		}else{
			return "��ɫ";
		}		
	}
	public static String getStyle(boolean isfull){
		if (isfull) {
			return "ʵ��";
		}else {
			return "����";
		}
	}
}
