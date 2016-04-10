package com.surveymapclient.entity;

import java.io.Serializable;

public class TextBean implements Serializable{
	private static final long serialVersionUID = -7060212344600432481L;
	private String text;
	private float tx;
	private float ty;
	
	public TextBean() {
	}
	

	public float getTx() {
		return tx;
	}


	public void setTx(float tx) {
		this.tx = tx;
	}


	public float getTy() {
		return ty;
	}


	public void setTy(float ty) {
		this.ty = ty;
	}


	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
