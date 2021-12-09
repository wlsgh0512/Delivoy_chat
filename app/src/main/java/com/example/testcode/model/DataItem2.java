package com.example.testcode.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class DataItem2 implements Serializable {

    private String content;
    private String rno;
    private String memCour;

    public DataItem2(String content, String rno, String memCour) {
        this.content = content;
        this.rno = rno;
        this.memCour = memCour;
    }

    public String getContent() {
        return content;
    }
    public String getRno() {
        return rno;
    }
    public String getMemCour() {
        return memCour;
    }

    public int getItemViewType(int position) {
        return position;
    }
    @NonNull
    @Override
    public String toString() {
        return content;
    }
}