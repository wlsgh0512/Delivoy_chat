package com.example.testcode.config;

import dagger.Component;
import retrofit2.Retrofit;

public class RetrofitConfig {

    private Retrofit retrofit;

    public RetrofitConfig() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://222.239.254.253/chatt/app")
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

}
