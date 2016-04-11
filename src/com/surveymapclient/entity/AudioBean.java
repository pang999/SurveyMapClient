package com.surveymapclient.entity;

import java.io.Serializable;

public class AudioBean implements Serializable {
	
	private static final long serialVersionUID = -7045330544600432481L;
	private String url;
	private int lenght;
	private float ax;
	private float ay;
	
	
	public int getLenght() {
		return lenght;
	}
	public void setLenght(int lenght) {
		this.lenght = lenght;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public float getAx() {
		return ax;
	}
	public void setAx(float ax) {
		this.ax = ax;
	}
	public float getAy() {
		return ay;
	}
	public void setAy(float ay) {
		this.ay = ay;
	}
	
	
}
