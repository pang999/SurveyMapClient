package com.surveymapclient.impl;

import com.surveymapclient.entity.AngleBean;
import com.surveymapclient.entity.CoordinateBean;
import com.surveymapclient.entity.LineBean;
import com.surveymapclient.entity.PolygonBean;
import com.surveymapclient.entity.RectangleBean;

public interface DialogCallBack {

	void onDialogCallBack(LineBean couplePoint,int i);
	void onDialogCallBack(PolygonBean polygon,int i);
	void onDialogCallBack(RectangleBean rectangle,int i);
	void onDialogCallBack(CoordinateBean coordinate,int i);
	void onDialogCallBack(AngleBean angleLine,int i);
}
