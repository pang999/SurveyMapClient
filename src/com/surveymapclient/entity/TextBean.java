package com.surveymapclient.entity;

import android.graphics.Paint;
import android.graphics.PointF;

public class TextBean {
	
	private PointF locaPoint;
	private String text;
	private Paint paint;
	
	
	public Paint getPaint() {
		return paint;
	}
	public void setPaint(Paint paint) {
		this.paint = paint;
	}
	public TextBean() {
	}
	public TextBean(PointF locaPoint) {
		super();
		this.locaPoint = locaPoint;
	}
	public PointF getLocaPoint() {
		return locaPoint;
	}
	public void setLocaPoint(PointF locaPoint) {
		this.locaPoint = locaPoint;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
