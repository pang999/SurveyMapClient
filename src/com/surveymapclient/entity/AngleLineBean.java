package com.surveymapclient.entity;

import android.graphics.PointF;

public class AngleLineBean {

	private PointF startPoint;
	private PointF endPoint;
	private PointF anglePoint;
	
	private float width;
	private boolean isFull;
	private String descripte;
	private String name;
	
	
	public AngleLineBean(PointF startPoint, PointF endPoint, PointF anglePoint) {
		super();
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.anglePoint = anglePoint;
	}
	public PointF getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(PointF startPoint) {
		this.startPoint = startPoint;
	}
	public PointF getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(PointF endPoint) {
		this.endPoint = endPoint;
	}
	public PointF getAnglePoint() {
		return anglePoint;
	}
	public void setAnglePoint(PointF anglePoint) {
		this.anglePoint = anglePoint;
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
