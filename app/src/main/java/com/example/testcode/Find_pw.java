package com.example.testcode;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.databinding.ActivityFindIdBinding;
import com.example.testcode.databinding.ActivityFindPwBinding;
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

public class Find_pw extends AppCompatActivity {
    String hostname = "222.239.254.253";
    String ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId, user_id, name, asd;

    private ActivityFindPwBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindPwBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("비밀번호 찾기");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

//        loadPreference();

        SharedPreferences sharedPreferences = getSharedPreferences("test", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
        ucAreaNo = sharedPreferences.getString("ar", "");
        ucDistribId = sharedPreferences.getString("di", "");
        ucAgencyId = sharedPreferences.getString("ag", "");
        ucMemCourId = sharedPreferences.getString("me", "");
        user_id = sharedPreferences.getString("id", "");
        name = sharedPreferences.getString("name", "");
    }

    private void loadPreference() {
        SharedPreferences sharedPreferences = getSharedPreferences("test", MODE_PRIVATE);
        final String data = sharedPreferences.getString("loginResponse", null);
        if (data == null || data.isEmpty()) {
            Toast.makeText(getApplicationContext(), "데이터없음", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            final LoginResponse loginResponse = new Gson().fromJson(data, LoginResponse.class);
            Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
        }


    }

    public void onClick_find_pw(View view) {
        find_Pw();
    }

    public void find_Pw() {
        try {
            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);

            service.findPw(ucAreaNo,
                    ucDistribId,
                    ucAgencyId,
                    ucMemCourId,
                    user_id,
                    binding.inputName.getText().toString(),
                    binding.inputEmail.getText().toString(),
                    binding.inputPhoneNumber.getText().toString())
                    .enqueue(new retrofit2.Callback<LoginResponse>() {
                        @Override
                        public void onResponse(retrofit2.Call<LoginResponse> call,
                                               retrofit2.Response<LoginResponse> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
//                                    final LoginResponse loginResponse = response.body();
                                    Toast.makeText(getApplicationContext(),
                                            "임시 비밀번호가 메일로 전송되었습니다.",
                                            Toast.LENGTH_SHORT).show();
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
