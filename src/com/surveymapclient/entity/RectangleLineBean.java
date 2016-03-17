package com.surveymapclient.entity;

import android.graphics.PointF;

public class RectangleLineBean {

	private PointF startpoint;
	private PointF endpoint;
	private float width;
	private boolean isFull;
	private String descripte;
	private String name;
	
	
	public RectangleLineBean(PointF startpoint, PointF endpoint) {
		super();
		this.startpoint = startpoint;
		this.endpoint = endpoint;
	}
	public PointF getStartpoint() {
		return startpoint;
	}
	public void setStartpoint(PointF startpoint) {
		this.startpoint = startpoint;
	}
	public PointF getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(PointF endpoint) {
		this.endpoint = endpoint;
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
