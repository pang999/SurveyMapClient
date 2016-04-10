package com.surveymapclient.entity;

import java.io.Serializable;

import android.graphics.PointF;

public class LineBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private float startX;
	private float startY;
	private float endX;
	private float endY;
	
	private String name;
	private double length;
	private double angle;
	
	private int paintColor;
	private float paintWidth;
	private boolean paintIsFull;	
	private String descripte;
	
	public float getStartX() {
		return startX;
	}
	public void setStartX(float startX) {
		this.startX = startX;
	}
	public float getStartY() {
		return startY;
	}
	public void setStartY(float startY) {
		this.startY = startY;
	}
	public float getEndX() {
		return endX;
	}
	public void setEndX(float endX) {
		this.endX = endX;
	}
	public float getEndY() {
		return endY;
	}
	public void setEndY(float endY) {
		this.endY = endY;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	public double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}
	public int getPaintColor() {
		return paintColor;
	}
	public void setPaintColor(int paintColor) {
		this.paintColor = paintColor;
	}
	public float getPaintWidth() {
		return paintWidth;
	}
	public void setPaintWidth(float paintWidth) {
		this.paintWidth = paintWidth;
	}
	public boolean isPaintIsFull() {
		return paintIsFull;
	}
	public void setPaintIsFull(boolean paintIsFull) {
		this.paintIsFull = paintIsFull;
	}
	public String getDescripte() {
		return descripte;
	}
	public void setDescripte(String descripte) {
		this.descripte = descripte;
	}
	@Override
	public String toString() {
		return "CouplePointLineBean [startX=" + startX + ", startY=" + startY + ", endX=" + endX + ", endY=" + endY
				+ ", name=" + name + ", length=" + length + ", angle=" + angle + ", paintColor=" + paintColor
				+ ", paintWidth=" + paintWidth + ", paintIsFull=" + paintIsFull + ", descripte=" + descripte + "]";
	}
	
	
	
}
