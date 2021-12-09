package com.example.testcode.model;

import androidx.annotation.NonNull;

public class NonFriendsData {
    private String name;
    private String memCour;

    public NonFriendsData(String name, String memCour) {
        this.name = name;
        this.memCour = memCour;
    }

    public String getName() {
        return name;
    }
    public String getMemCour() {
        return memCour;
    }

}
