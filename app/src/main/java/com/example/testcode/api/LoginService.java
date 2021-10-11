package com.example.testcode.api;

import com.example.testcode.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LoginService {
    @GET("login/login_no_get.php")
    Call<LoginResponse> login(@Query("ucAreaNo") String ucAreaNo,
                              @Query("ucDistribId") String ucDistribId,
                              @Query("ucAgencyId") String ucAgencyId,
                              @Query("ucMemCourId") String ucMemCourId,
                              @Query("acPassword") String acPassword);
}
