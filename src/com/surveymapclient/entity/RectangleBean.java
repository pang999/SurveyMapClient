package com.surveymapclient.entity;

import java.io.Serializable;

public class RectangleBean implements Serializable{

	private static final long serialVersionUID = -7060210544600464481L;
	private float startX;
	private float startY;
	private float endX;
	private float endY;
	
	private float paintWidth;
	private boolean isFull;
	private int paintColor;
	private String descripte;
	
	private String rectName;
	private float rectLenght;
	private float rectWidth;
	private double rectArea;
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
	public float getPaintWidth() {
		return paintWidth;
	}
	public void setPaintWidth(float paintWidth) {
		this.paintWidth = paintWidth;
	}
	public boolean isFull() {
		return isFull;
	}
	public void setFull(boolean isFull) {
		this.isFull = isFull;
	}
	public int getPaintColor() {
		return paintColor;
	}
	public void setPaintColor(int paintColor) {
		this.paintColor = paintColor;
	}
	public String getDescripte() {
		return descripte;
	}
	public void setDescripte(String descripte) {
		this.descripte = descripte;
	}
	public String getRectName() {
		return rectName;
	}
	public void setRectName(String rectName) {
		this.rectName = rectName;
	}
	public float getRectLenght() {
		return rectLenght;
	}
	public void setRectLenght(float rectLenght) {
		this.rectLenght = rectLenght;
	}
	public float getRectWidth() {
		return rectWidth;
	}
	public void setRectWidth(float rectWidth) {
		this.rectWidth = rectWidth;
	}
	public double getRectArea() {
		return rectArea;
	}
	public void setRectArea(double rectArea) {
		this.rectArea = rectArea;
	}


	
}
