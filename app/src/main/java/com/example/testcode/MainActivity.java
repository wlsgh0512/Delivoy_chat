package com.example.testcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.databinding.ActivityMainBinding;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.LoginResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 메인 화면.
 * 로그인 방식 두개, 아이디 찾기, 비밀번호 찾기 완료.
 * 로그인 방식 라디오 버튼 선택하면 아이디 입력하는 곳에 다른 형식의 EditText 나오게.
 * 현재는 위치 각자 잡고 setVisibility
 */

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private boolean mBackPressedToExitOneMore;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rbOldLogin.setChecked(true);

        binding.rgLoginMethod.setOnCheckedChangeListener((radioGroup, id) -> {
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.rb_old_login: {
                    binding.llOldUserIdWrap.setVisibility(View.VISIBLE);
                    binding.userId.setVisibility(View.INVISIBLE);
                    break;
                }
                case R.id.rb_user_id_login: {
                    binding.llOldUserIdWrap.setVisibility(View.INVISIBLE);
                    binding.userId.setVisibility(View.VISIBLE);
                    break;
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            // 로그인 버튼을 눌렀을 때
            case R.id.btn_login:
                switch (binding.rgLoginMethod.getCheckedRadioButtonId()) {
                    case R.id.rb_user_id_login:
                        getUserIdLogin();
                        break;
                    case R.id.rb_old_login:
                    default:
                        getOldLogin();
                        break;
                }
                break;

            // 아이디 찾기 눌렀을 때
            case R.id.btnFindUserId:
                Intent intent0 = new Intent(MainActivity.this, Find_id.class);
                startActivity(intent0);
                break;
            // 비밀번호 찾기 눌렀을 때
            case R.id.btnFindUserPw:
                Intent intent1 = new Intent(MainActivity.this, Find_pw.class);
                startActivity(intent1);
                break;
            // 회원가입 눌렀을 때
            case R.id.join_member:
                Intent intent2 = new Intent(MainActivity.this, Join_Member.class);
                startActivity(intent2);
                break;
        }
    }

    public void getOldLogin() {
        try {
            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.oldLogin(binding.edtUserAreaNo.getText().toString(),
                    binding.edtUserDistribId.getText().toString(),
                    binding.edtUserAgencyId.getText().toString(),
                    binding.edtUserCourId.getText().toString(),
                    binding.userPw.getText().toString())
                    .enqueue(new retrofit2.Callback<LoginResponse>() {
                        @Override
                        public void onResponse(retrofit2.Call<LoginResponse> call,
                                               retrofit2.Response<LoginResponse> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final LoginResponse loginResponse = response.body();
                                    SharedPreferences sharedPreferences = getSharedPreferences(
                                            "test"
                                            , MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("ar", loginResponse.ucAreaNo);
                                    editor.putString("di", loginResponse.ucDistribId);
                                    editor.putString("ag", loginResponse.ucAgencyId);
                                    editor.putString("me", loginResponse.ucMemCourId);
                                    editor.putString("id", loginResponse.acUserId);
                                    editor.putString("name", loginResponse.acRealName);
                                    editor.putString("ao", loginResponse.ucAgreeOption);
                                    editor.putString("tpo", loginResponse.ucThirdPartyOption);
                                    editor.putString("ac", loginResponse.ucAccessFlag);
                                    editor.putString("pw", binding.userPw.getText().toString());
                                    editor.commit();

                                    // Option 두 개의 Value값이 1이라면 이미 동의를 한 것.
                                    // 약관 동의를 마치면 Option이 1이 되게 하여 약관 동의 페이지를 생략하도록.
                                    if (loginResponse.ucAgreeOption.equals("1") && loginResponse.ucThirdPartyOption.equals("1")) {
                                        Intent intent = new Intent(MainActivity.this,
                                                ListActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(), "로그인에 성공했습니다.",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent intent = new Intent(MainActivity.this,
                                                User_Consent.class);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(), "첫 로그인 시 약관 동의가 " +
                                                "필요합니다" +
                                                ".", Toast.LENGTH_SHORT).show();
                                    }
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

    public void getUserIdLogin() {
        try {
            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.idLogin(binding.userId.getText().toString(),
                    binding.userPw.getText().toString())
                    .enqueue(new retrofit2.Callback<LoginResponse>() {
                        @Override
                        public void onResponse(retrofit2.Call<LoginResponse> call,
                                               retrofit2.Response<LoginResponse> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final LoginResponse loginResponse = response.body();
                                    SharedPreferences sharedPreferences = getSharedPreferences(
                                            "test"
                                            , MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    // json으로
                                    editor.putString("loginResponse", new Gson().toJson(loginResponse));
                                    editor.commit();

                                    // Option 두 개의 Value값이 1이라면 이미 동의를 한 것.
                                    // 약관 동의를 마치면 Option이 1이 되게 하여 약관 동의 페이지를 생략하도록.
                                    if (loginResponse.ucAgreeOption.equals("1") && loginResponse.ucThirdPartyOption.equals("1")) {
                                        Intent intent = new Intent(MainActivity.this,
                                                ListActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(), "로그인에 성공했습니다.",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent intent = new Intent(MainActivity.this,
                                                User_Consent.class);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(), "첫 로그인 시 약관 동의가 " +
                                                "필요합니다" +
                                                ".", Toast.LENGTH_SHORT).show();
                                    }
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


