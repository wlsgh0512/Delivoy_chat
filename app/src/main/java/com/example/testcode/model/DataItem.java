package com.example.testcode.model;

public class DataItem {

    private int talkId;
    private String content;
    private String name;
    private int viewType;

    public DataItem(int talkId, String content, String name ,int viewType) {
        this.talkId = talkId;
        this.content = content;
        this.viewType = viewType;
        this.name = name;
    }

//public DataItem(String content, String name ,int viewType) {
//    this.content = content;
//    this.viewType = viewType;
//    this.name = name;
//}

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
}