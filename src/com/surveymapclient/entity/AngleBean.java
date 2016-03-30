package com.surveymapclient.entity;

import java.io.Serializable;

import android.graphics.PointF;

public class AngleBean implements Serializable{
	private static final long serialVersionUID = -7060210544600432481L;
	
	private float startX;
	private float startY;
	private float endX;
	private float endY;
	private float angleX;
	private float angleY;
	
	private String name;
	private String descripte;
	private double angle;
	
	private float paintWidth;
	private boolean paintIsFull;
	private int paintColor;
	
	
	public double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}
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
	public float getAngleX() {
		return angleX;
	}
	public void setAngleX(float angleX) {
		this.angleX = angleX;
	}
	public float getAngleY() {
		return angleY;
	}
	public void setAngleY(float angleY) {
		this.angleY = angleY;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescripte() {
		return descripte;
	}
	public void setDescripte(String descripte) {
		this.descripte = descripte;
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
}
