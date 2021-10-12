package com.example.testcode;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.databinding.ActivityFindIdBinding;
import com.example.testcode.databinding.ActivityUserConsentBinding;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.FriendsResponse;
import com.example.testcode.model.LoginResponse;
import com.example.testcode.model.User_Consent_Response;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 이용약관 동의
 * 필수 약관 동의 두개가 모두 체크(전체 동의)되어야 확인 버튼이 눌리도록,
 * MainActivity에서 ucAgreeOption , ucThirdPartyOption 값이 0인 경우 약관 동의 화면.
 * 체크박스 체크가 완료된 상태로 확인을 눌렀을 때 agreement() -> runOnUiThread에서
 * ucAgreeOption, ucThirdPartyOption 값을 0 -> 1로 바꾼다 ?
 * error code 500, Logcat은 Expected BEGIN_OBJECT but was STRING at line 8 column 1 path $
 */

public class User_Consent extends AppCompatActivity {
    CheckBox checkBox, checkBox2, checkBox3;
    private String TAG = "이용약관 닫기";

    String ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId, AgreeOption, ThirdPartyOption;

    ActivityUserConsentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserConsentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("이용약관 동의");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        // 이용약관 버튼1 - 서비스
        Button btn_agr = findViewById(R.id.btn_agr);
        btn_agr.setText(R.string.underlined_text);

        // 이용약관 버튼2 - 제3자 정보 제공 동의
        Button btn_agr2 = findViewById(R.id.btn_agr2);
        btn_agr2.setText(R.string.underlined_text);

        SharedPreferences sharedPreferences = getSharedPreferences("test", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
        ucAreaNo = sharedPreferences.getString("ar", "");
        ucDistribId = sharedPreferences.getString("di", "");
        ucAgencyId = sharedPreferences.getString("ag", "");
        ucMemCourId = sharedPreferences.getString("me", "");
        AgreeOption = sharedPreferences.getString("ao", "");
        ThirdPartyOption = sharedPreferences.getString("tpo", "");

        // 전체동의 클릭시
        // 전체 true / 전체 false 로 변경
        binding.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.checkbox.isChecked()) {
                    binding.checkbox2.setChecked(true);
                    binding.checkbox3.setChecked(true);
                } else {
                    binding.checkbox2.setChecked(false);
                    binding.checkbox3.setChecked(false);
                }
            }
        });

        // 2번째 체크박스 클릭
        binding.checkbox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //만약 전체 클릭이 true 라면 false로 변경
                if (binding.checkbox.isChecked()) {
                    binding.checkbox.setChecked(false);
                    //각 체크박스 체크 여부 확인해서  전체동의 체크박스 변경
                } else if (binding.checkbox2.isChecked() && binding.checkbox3.isChecked()) {
                    binding.checkbox.setChecked(true);
                }
            }
        });

        // 3번째 체크박스 클릭
        binding.checkbox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.checkbox.isChecked()) {
                    binding.checkbox.setChecked(false);
                } else if (binding.checkbox2.isChecked() && binding.checkbox3.isChecked()) {
                    binding.checkbox.setChecked(true);
                }
            }
        });
    }

    private boolean ischecked(CheckBox checkBox, CheckBox checkBox2, CheckBox checkBox3) {

        if (binding.checkbox.isChecked() || binding.checkbox2.isChecked() && binding.checkbox3.isChecked()) {
            return true;
        }
        return false;
    }

    public void Onclick1(View view) {
        Intent intent1 = new Intent(getApplicationContext(), WebViewActivity1.class);
        startActivity(intent1);

    }

    public void Onclick2(View view) {
        Intent intent2 = new Intent(getApplicationContext(), WebViewActivity2.class);
        startActivity(intent2);
    }

    public void Onclick3(View view) {
        Intent intent3 = new Intent(getApplicationContext(), WebViewActivity3.class);
        startActivity(intent3);
    }

    // 다음 번에 Login 할 때는 해당 동의서에 표시가 되어 있으면 Skip 하고
    // 동의서에 동의가 안되어 있으면 동의할때까지 계속 띄워줘야함
    // -> 어차피 동의를 받아야 서비스를 이용할 수 있다면 체크가 안되면 안넘어가게 ?
    public void onClick_user_consent(View view) {
        // 어차피 체크박스 2,3 중 하나라도 체크가 되어있지 않으면 체크박스1은 체크 x.

        if (!binding.checkbox.isChecked()) {
            Toast.makeText(getApplicationContext(), "이용 약관 동의가 필요합니다.", Toast.LENGTH_SHORT).show();
        } else {
//            agreement();

            Intent intent = new Intent(User_Consent.this, ListActivity.class);
            startActivity(intent);
        }
    }

    public void agreement() {
        try {
            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);

            service.agree(ucAreaNo,
                    ucDistribId,
                    ucAgencyId,
                    ucMemCourId,
                    AgreeOption,
                    ThirdPartyOption)
                    .enqueue(new retrofit2.Callback<User_Consent_Response>() {
                        @Override
                        public void onResponse(retrofit2.Call<User_Consent_Response> call,
                                               retrofit2.Response<User_Consent_Response> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final User_Consent_Response user_consent_response = response.body();
//                                    Intent intent = new Intent(User_Consent.this, ListActivity.class);
//                                    startActivity(intent);
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
                        public void onFailure(retrofit2.Call<User_Consent_Response> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}