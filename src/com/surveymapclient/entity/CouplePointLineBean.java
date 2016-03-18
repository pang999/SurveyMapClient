package com.surveymapclient.entity;

import java.io.Serializable;

import android.graphics.PointF;

public class CouplePointLineBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private PointF startPoint;
	private PointF stopPoint;
	
	private String name;
	private double length;
	private double angle;
	private int color;
	private float width;
	private boolean isFull;
	private String descripte;
	

	public boolean isFull() {
		return isFull;
	}

	public void setFull(boolean isFull) {
		this.isFull = isFull;
	}

	public CouplePointLineBean(PointF startPoint, PointF stopPoint) {
		super();
		this.startPoint = startPoint;
		this.stopPoint = stopPoint;
	}

	public PointF getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(PointF startPoint) {
		this.startPoint = startPoint;
	}

	public PointF getStopPoint() {
		return stopPoint;
	}

	public void setStopPoint(PointF stopPoint) {
		this.stopPoint = stopPoint;
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

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}



	public String getDescripte() {
		return descripte;
	}

	public void setDescripte(String descripte) {
		this.descripte = descripte;
	}

	@Override
	public String toString() {
		return "CouplePointLine [startPoint=" + startPoint + ", stopPoint=" + stopPoint + ", name=" + name + ", length="
				+ length + ", angle=" + angle + ", color=" + color + ", width=" + width + ", isFull=" + isFull
				+ ", descripte=" + descripte + "]";
	}


	

	
}
