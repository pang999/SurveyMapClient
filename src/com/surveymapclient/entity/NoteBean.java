package com.surveymapclient.entity;

import java.io.Serializable;

public class NoteBean implements Serializable {
	private static final long serialVersionUID = -7060212344600432532L;
	
	private String text;
	private String audioUrl;
	private String audioTime;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getAudioUrl() {
		return audioUrl;
	}
	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}
	public String getAudioTime() {
		return audioTime;
	}
	public void setAudioTime(String audioTime) {
		this.audioTime = audioTime;
	}
	

}
