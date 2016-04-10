package com.surveymapclient.db.greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table MODULE.
 */
public class Module {

    private Long id;
    private Long key;
    private String name;
    private Integer type;

    public Module() {
    }

    public Module(Long id) {
        this.id = id;
    }

    public Module(Long id, Long key, String name, Integer type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.type = type;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
