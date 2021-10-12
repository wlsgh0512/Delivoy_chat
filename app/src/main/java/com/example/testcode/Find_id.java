package com.example.testcode;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.databinding.ActivityFindIdBinding;
import com.example.testcode.databinding.ActivityMainBinding;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.LoginResponse;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * EditText가 비어있을 때, EditText 값과 받는 값이 다를 때 조건 달아주기.
 */
public class Find_id extends AppCompatActivity {
    String hostname = "222.239.254.253";

    private ActivityFindIdBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindIdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("아이디 찾기");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

    }

    public void onClick_find_id(View view) {
        if (binding.inputRealName.getText().toString().isEmpty() || binding.inputPhoneNumber.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "비어있는 항목을 작성해주세요.", Toast.LENGTH_SHORT).show();
        }

        find_Id();

    }

    public void find_Id() {
        try {
            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.findId(binding.inputRealName.getText().toString(),
                    binding.inputPhoneNumber.getText().toString())
                    .enqueue(new retrofit2.Callback<LoginResponse>() {
                        @Override
                        public void onResponse(retrofit2.Call<LoginResponse> call,
                                               retrofit2.Response<LoginResponse> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final LoginResponse loginResponse = response.body();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(Find_id.this);

                                    builder.setTitle("회원님의 아이디는").setMessage("코드 방식 : "
                                            + loginResponse.ucAreaNo + "-"
                                            + loginResponse.ucDistribId + "-"
                                            + loginResponse.ucAgencyId + "-"
                                            + loginResponse.ucMemCourId + " ,\n"
                                            + "아이디 방식 : " + loginResponse.acUserId + " 입니다.");

                                    AlertDialog alertDialog = builder.create();

                                    alertDialog.show();



                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                final ErrorDto error;
                                try {
                                    error = new Gson().fromJson(response.errorBody().string(),
                                            ErrorDto.class);
                                    Log.i("tag", error.message);
                                    // 응답 실패
                                    Log.i("tag", "응답실패");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        @Override
                        public void onFailure(retrofit2.Call<LoginResponse> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}