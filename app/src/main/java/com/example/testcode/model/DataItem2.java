package com.example.testcode.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class DataItem2 implements Serializable {

    private String content;
    private String rno;
//    private String tno;

    public DataItem2(String content, String rno) {
        this.content = content;
        this.rno = rno;
//        this.tno = tno;
    }

    public String getContent() {
        return content;
    }
    public String getRno() {
        return rno;
    }
//    public String getTno() {
//        return tno;
//    }


    @NonNull
    @Override
    public String toString() {
        return content;
    }
}