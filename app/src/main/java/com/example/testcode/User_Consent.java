package com.example.testcode;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testcode.model.ErrorDto;
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
 * 구현 목표 -> 전체 동의 후 확인 버튼을 눌렀을 때 user_consent_response 확인하고 다음 화면으로.
 * 저번에 오셔서 수정해주신 POST 코드랑 구글링 참조해서 나름 작성해보았는데
 * 잘 해결이 안되서 코드를 이렇게 작성하면 되는지 문제가 있는 코드인지가 제일 궁금합니다..
 */

public class User_Consent extends AppCompatActivity {
    CheckBox checkBox, checkBox2, checkBox3;
    String hostname = "222.239.254.253";
    private String TAG = "이용약관 닫기";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("이용약관 동의");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기


        setContentView(R.layout.activity_user_consent);
        // 전체동의
        checkBox = findViewById(R.id.checkbox);
        // 서비스
        checkBox2 = findViewById(R.id.checkbox2);
        // 위치기반
        checkBox3 = findViewById(R.id.checkbox3);
        // 오픈뱅킹

        // 이용약관 버튼1 - 서비스
        Button btn_agr = findViewById(R.id.btn_agr);
        btn_agr.setText(R.string.underlined_text);

        // 이용약관 버튼2 - 제3자 정보 제공 동의
        Button btn_agr2 = findViewById(R.id.btn_agr2);
        btn_agr2.setText(R.string.underlined_text);

        if(checkBox.isChecked()) {
        }


        // 전체동의 클릭시
        // 전체 true / 전체 false 로 변경
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    checkBox2.setChecked(true);
                    checkBox3.setChecked(true);
                } else {
                    checkBox2.setChecked(false);
                    checkBox3.setChecked(false);
                }
            }
        });

        // 2번째 체크박스 클릭
        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //만약 전체 클릭이 true 라면 false로 변경
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                    //각 체크박스 체크 여부 확인해서  전체동의 체크박스 변경
                } else if (checkBox2.isChecked() && checkBox3.isChecked()) {
                    checkBox.setChecked(true);
                }
            }
        });

        // 3번째 체크박스 클릭
        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                } else if (checkBox2.isChecked() && checkBox3.isChecked()) {
                    checkBox.setChecked(true);
                }
            }
        });
    }

    private boolean ischecked(CheckBox checkBox, CheckBox checkBox2, CheckBox checkBox3) {

        if (checkBox.isChecked() || checkBox2.isChecked() && checkBox3.isChecked()) {
            return true;
        }
        return false;
    }

    public void Onclick1(View view) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setTitle("서비스 이용약관").setMessage("서비스 이용약관");
//
//        AlertDialog alertDialog = builder.create();
//
//        alertDialog.show();
        Intent intent1 = new Intent(getApplicationContext(), WebViewActivity1.class);
        startActivity(intent1);

    }

    public void Onclick2(View view) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setTitle("위치기반 서비스 이용약관").setMessage("위치기반 서비스 이용약관");
//
//        AlertDialog alertDialog = builder.create();
//
//        alertDialog.show();
        Intent intent2 = new Intent(getApplicationContext(), WebViewActivity2.class);
        startActivity(intent2);
    }

    public void Onclick3(View view) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setTitle("오픈뱅킹 서비스 이용약관").setMessage("오픈뱅킹 서비스 이용약관");
//
//        AlertDialog alertDialog = builder.create();
//
//        alertDialog.show();
        Intent intent3 = new Intent(getApplicationContext(), WebViewActivity3.class);
        startActivity(intent3);
    }

    // 다음 번에 Login 할 때는 해당 동의서에 표시가 되어 있으면 Skip 하고
    // 동의서에 동의가 안되어 있으면 동의할때까지 계속 띄워줘야함
    // -> 어차피 동의를 받아야 서비스를 이용할 수 있다면 체크가 안되면 안넘어가게 ?
    public void onClick_user_consent(View view) {
        // 어차피 체크박스 2,3 중 하나라도 체크가 되어있지 않으면 체크박스1은 체크 x.
        if (!checkBox.isChecked()) {
            return;
        } else {
//            agreement();

            Intent intent = new Intent(User_Consent.this, ListActivity.class);
            startActivity(intent);

        }
    }

        public void agreement() {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = String.format("http://%s/chatt/app/users/user_put.php", hostname);

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("ucAreaNo","88")
                    .addFormDataPart("ucDistribId","17")
                    .addFormDataPart("ucAgencyId","1")
                    .addFormDataPart("ucMemCourId","1")
                    .addFormDataPart("ucAgreeOption","1")
                    .addFormDataPart("ucThirdPartyOption","1")
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", "x-www-form-urlencoded")
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final ErrorDto error = new Gson().fromJson(response.body().string(), ErrorDto.class);
                        Log.i("tag", error.message);
                        // 응답 실패
                        Log.i("tag", "응답실패");
                    } else {
                        // 응답 성공
                        Log.i("tag", "응답 성공");
                        final String responseData = response.body().string();
                        final User_Consent_Response user_consent_response = new Gson().fromJson(responseData, User_Consent_Response.class);

                        runOnUiThread(() -> {
                            try {
                                Toast.makeText(getApplicationContext(), "응답" + user_consent_response.ucAgreeOption, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            });

        } catch (Exception e) {}
    }
}