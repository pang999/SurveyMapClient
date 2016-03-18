package com.surveymapclient.entity;

import android.graphics.PointF;

public class CoordinateLineBean {

	private PointF centerPoint;
	private PointF xPoint;
	private PointF yPoint;
	private PointF zPoint;
	private float width;
	private boolean isFull;
	private String descripte;
	private String name;
	
	
	
	public CoordinateLineBean(PointF centerPoint, PointF xPoint, PointF yPoint, PointF zPoint) {
		super();
		this.centerPoint = centerPoint;
		this.xPoint = xPoint;
		this.yPoint = yPoint;
		this.zPoint = zPoint;
	}
	public PointF getCenterPoint() {
		return centerPoint;
	}
	public void setCenterPoint(PointF centerPoint) {
		this.centerPoint = centerPoint;
	}
	public PointF getxPoint() {
		return xPoint;
	}
	public void setxPoint(PointF xPoint) {
		this.xPoint = xPoint;
	}
	public PointF getyPoint() {
		return yPoint;
	}
	public void setyPoint(PointF yPoint) {
		this.yPoint = yPoint;
	}
	public PointF getzPoint() {
		return zPoint;
	}
	public void setzPoint(PointF zPoint) {
		this.zPoint = zPoint;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public boolean isFull() {
		return isFull;
	}
	public void setFull(boolean isFull) {
		this.isFull = isFull;
	}
	public String getDescripte() {
		return descripte;
	}
	public void setDescripte(String descripte) {
		this.descripte = descripte;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
