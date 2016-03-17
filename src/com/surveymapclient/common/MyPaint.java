package com.surveymapclient.common;

import android.graphics.Paint;

public class MyPaint extends Paint {

	private int color;
	private int width;
	private int paintstyle;
	
	
	
	public MyPaint(int color, int width, int paintstyle) {
		super();
		this.color = color;
		this.width = width;
		this.paintstyle = paintstyle;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getPaintstyle() {
		return paintstyle;
	}
	public void setPaintstyle(int paintstyle) {
		this.paintstyle = paintstyle;
	}
	
	
}
