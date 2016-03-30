package com.surveymapclient.entity;

import java.io.Serializable;

import android.graphics.PointF;

public class CoordinateBean implements Serializable{
	
	private static final long serialVersionUID = -7060210544600460861L;
	
	private float centerX;
	private float centerY;
	private float xaxisX;
	private float xaxisY;
	private float yaxisX;
	private float yaxisY;
	private float zaxisX;
	private float zaxisY;
	private String name;
	private double volum;
	private float lenght;
	private float width;
	private float height;
	private float paintWidth;
	private boolean paintIsFull;
	private int paintColor;	
	private String descripte;
	
	
	public float getLenght() {
		return lenght;
	}
	public void setLenght(float lenght) {
		this.lenght = lenght;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public float getCenterX() {
		return centerX;
	}
	public void setCenterX(float centerX) {
		this.centerX = centerX;
	}
	public float getCenterY() {
		return centerY;
	}
	public void setCenterY(float centerY) {
		this.centerY = centerY;
	}
	public float getXaxisX() {
		return xaxisX;
	}
	public void setXaxisX(float xaxisX) {
		this.xaxisX = xaxisX;
	}
	public float getXaxisY() {
		return xaxisY;
	}
	public void setXaxisY(float xaxisY) {
		this.xaxisY = xaxisY;
	}
	public float getYaxisX() {
		return yaxisX;
	}
	public void setYaxisX(float yaxisX) {
		this.yaxisX = yaxisX;
	}
	public float getYaxisY() {
		return yaxisY;
	}
	public void setYaxisY(float yaxisY) {
		this.yaxisY = yaxisY;
	}
	public float getZaxisX() {
		return zaxisX;
	}
	public void setZaxisX(float zaxisX) {
		this.zaxisX = zaxisX;
	}
	public float getZaxisY() {
		return zaxisY;
	}
	public void setZaxisY(float zaxisY) {
		this.zaxisY = zaxisY;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public int getPaintColor() {
		return paintColor;
	}
	public void setPaintColor(int paintColor) {
		this.paintColor = paintColor;
	}
	public double getVolum() {
		return volum;
	}
	public void setVolum(double volum) {
		this.volum = volum;
	}
	public String getDescripte() {
		return descripte;
	}
	public void setDescripte(String descripte) {
		this.descripte = descripte;
	}
	
	
}
