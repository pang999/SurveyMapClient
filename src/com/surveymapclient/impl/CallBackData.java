package com.surveymapclient.impl;

import com.surveymapclient.entity.AudioBean;
import com.surveymapclient.entity.LineBean;
import com.surveymapclient.entity.PolygonBean;
import com.surveymapclient.entity.TextBean;

public interface CallBackData {
	void onCallBackDown(float x,float y);
	void onCallBackMove(float x,float y);
	void onCallBackUp(float x,float y);
	void onCallBackLong(boolean islong,boolean isyes);
	void onCallBackDeleteLine(int i);
	void onCallBackMoveLine(LineBean lineBean);
	void onCallBackDeletePolygon(int i);
	void onCallBackMovePolygon(PolygonBean polygonBean);
	void onCallBackDeletePolygonLine(int n,int i);
	void onCallBackLineAttribute(int index,LineBean lineBean);
	void onCallBaclPolygonLineAttribute(int n, int index,LineBean line);
	void onCallBackText(float x,float y);
	void onCallBackAudio(float x,float y,String url,int len);
	void onCallBackDeleteText(int i);
	void onCallBackDeleteAudio(int i);
	void onCallBackMoveTextUp(TextBean textBean);
	void onCallBackMoveAudioUp(AudioBean audioBean);
	void onCallBackChangeTextContent(int i,String text);
}
