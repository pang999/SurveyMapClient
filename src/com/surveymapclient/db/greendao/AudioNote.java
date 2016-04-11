package com.surveymapclient.db.greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table AUDIO_NOTE.
 */
public class AudioNote {

    private Long id;
    private Long key;
    private Integer lenght;
    private String url;
    private Float ax;
    private Float ay;

    public AudioNote() {
    }

    public AudioNote(Long id) {
        this.id = id;
    }

    public AudioNote(Long id, Long key, Integer lenght, String url, Float ax, Float ay) {
        this.id = id;
        this.key = key;
        this.lenght = lenght;
        this.url = url;
        this.ax = ax;
        this.ay = ay;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public Integer getLenght() {
        return lenght;
    }

    public void setLenght(Integer lenght) {
        this.lenght = lenght;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Float getAx() {
        return ax;
    }

    public void setAx(Float ax) {
        this.ax = ax;
    }

    public Float getAy() {
        return ay;
    }

    public void setAy(Float ay) {
        this.ay = ay;
    }

}