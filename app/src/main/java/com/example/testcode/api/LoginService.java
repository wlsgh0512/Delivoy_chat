package com.example.testcode.api;

import com.example.testcode.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LoginService {
    @GET("login/login_no_get.php")
    Call<LoginResponse> login(@Path("ucAreaNo") String ucAreaNo,
                              @Path("ucDistribId") String ucDistribId,
                              @Path("ucAgencyId") String ucAgencyId,
                              @Path("ucMemCourId") String ucMemCourId,
                              @Path("acPassword") String acPassword);
}
