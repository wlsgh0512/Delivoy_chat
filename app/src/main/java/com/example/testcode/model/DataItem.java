package com.example.testcode.model;

public class DataItem {

    private int talkId;
    private String content;
    private String name;
    private int viewType;
    private String time;

    public DataItem(int talkId, String content, String name ,int viewType, String time) {
        this.talkId = talkId;
        this.content = content;
        this.viewType = viewType;
        this.name = name;
        this.time = time;
    }

    public int getTalkId() {return talkId;}

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public int getViewType() {
        return viewType;
    }

    public String getTime() {
        return time;
    }

}