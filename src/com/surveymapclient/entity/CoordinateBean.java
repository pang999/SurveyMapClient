package com.surveymapclient.entity;

import java.io.Serializable;

public class CoordinateBean implements Serializable{
	
	private static final long serialVersionUID = -7060210544600460861L;
	
//	private float centerX;
//	private float centerY;
//	private float xaxisX;
//	private float xaxisY;
//	private float yaxisX;
//	private float yaxisY;
//	private float zaxisX;
//	private float zaxisY;
	private String name;
	private double volum;	
	private String descripte;
	private LineBean xLine;
	private LineBean yLine;
	private LineBean zLine;
	public LineBean getxLine() {
		return xLine;
	}
	public void setxLine(LineBean xLine) {
		this.xLine = xLine;
	}
	public LineBean getyLine() {
		return yLine;
	}
	public void setyLine(LineBean yLine) {
		this.yLine = yLine;
	}
	public LineBean getzLine() {
		return zLine;
	}
	public void setzLine(LineBean zLine) {
		this.zLine = zLine;
	}
//	public float getCenterX() {
//		return centerX;
//	}
//	public void setCenterX(float centerX) {
//		this.centerX = centerX;
//	}
//	public float getCenterY() {
//		return centerY;
//	}
//	public void setCenterY(float centerY) {
//		this.centerY = centerY;
//	}
//	public float getXaxisX() {
//		return xaxisX;
//	}
//	public void setXaxisX(float xaxisX) {
//		this.xaxisX = xaxisX;
//	}
//	public float getXaxisY() {
//		return xaxisY;
//	}
//	public void setXaxisY(float xaxisY) {
//		this.xaxisY = xaxisY;
//	}
//	public float getYaxisX() {
//		return yaxisX;
//	}
//	public void setYaxisX(float yaxisX) {
//		this.yaxisX = yaxisX;
//	}
//	public float getYaxisY() {
//		return yaxisY;
//	}
//	public void setYaxisY(float yaxisY) {
//		this.yaxisY = yaxisY;
//	}
//	public float getZaxisX() {
//		return zaxisX;
//	}
//	public void setZaxisX(float zaxisX) {
//		this.zaxisX = zaxisX;
//	}
//	public float getZaxisY() {
//		return zaxisY;
//	}
//	public void setZaxisY(float zaxisY) {
//		this.zaxisY = zaxisY;
//	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
