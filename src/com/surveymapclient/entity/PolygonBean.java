package com.surveymapclient.entity;

import java.io.Serializable;
import java.util.List;

public class PolygonBean implements Serializable{
	
	private static final long serialVersionUID = 7L;
	private String polyName;
	private List<LineBean> polyLine;
	private double polyArea;
	private int polyColor;
	private String descripe;
	
	
	public String getPolyName() {
		return polyName;
	}
	public void setPolyName(String polyName) {
		this.polyName = polyName;
	}
	public String getDescripe() {
		return descripe;
	}
	public void setDescripe(String descripe) {
		this.descripe = descripe;
	}
	public int getPolyColor() {
		return polyColor;
	}
	public void setPolyColor(int polyColor) {
		this.polyColor = polyColor;
	}
	public List<LineBean> getPolyLine() {
		return polyLine;
	}
	public void setPolyLine(List<LineBean> polyLine) {
		this.polyLine = polyLine;
	}
	public double getPolyArea() {
		return polyArea;
	}
	public void setPolyArea(double polyArea) {
		this.polyArea = polyArea;
	}
	

}
