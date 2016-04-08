package com.surveymapclient.db.greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table TEXT_NOTE.
 */
public class TextNote {

    private Long id;
    private Long key;
    private String content;
    private Float tx;
    private Float ty;

    public TextNote() {
    }

    public TextNote(Long id) {
        this.id = id;
    }

    public TextNote(Long id, Long key, String content, Float tx, Float ty) {
        this.id = id;
        this.key = key;
        this.content = content;
        this.tx = tx;
        this.ty = ty;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Float getTx() {
        return tx;
    }

    public void setTx(Float tx) {
        this.tx = tx;
    }

    public Float getTy() {
        return ty;
    }

    public void setTy(Float ty) {
        this.ty = ty;
    }

}
