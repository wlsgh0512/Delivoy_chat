package com.example.testcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.LoginResponse;
import com.example.testcode.model.Login_id_Response;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

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
 * 메인 화면.
 * 로그인 방식 두개, 아이디 찾기, 비밀번호 찾기 완료.
 * 로그인 방식 라디오 버튼 선택하면 아이디 입력하는 곳에 다른 형식의 EditText 나오게.
 * 현재는 구현 방법을 모르겠어서 위치는 각자 잡고 setVisibility로 했습니다..
 * 자동 로그인, 회원가입 미구현.
 */

public class MainActivity extends AppCompatActivity {
    String hostname = "222.239.254.253";
    String port = "80";
    String url = "http://" + hostname + ":" + port + "0x0201";
    String oldid = "";
    String ar = "";
    String di = "";
    String ag = "";
    String me = "";
    String id = "";
    String pw = "";
    Button btn_login;
    TextView btnFindUserId, btnFindUserPw, join_member;
    EditText user_id, user_pw;
    ImageButton btn_find;
    RadioButton rb_old_login, rb_user_id_login;
    RadioGroup rg_login_method;
    LinearLayout ll_old_user_id_wrap;
    AppCompatEditText edtUserAreaNo, edtUserDistribId, edtUserAgencyId, edtUserCourId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_login = (Button) findViewById(R.id.btn_login);
        btnFindUserId = (TextView) findViewById(R.id.btnFindUserId);
        btnFindUserPw = (TextView) findViewById(R.id.btnFindUserPw);
        join_member = (TextView) findViewById(R.id.join_member);
        user_id = (EditText) findViewById(R.id.user_id);
        user_pw = (EditText) findViewById(R.id.user_pw);
        btn_find = (ImageButton) findViewById(R.id.btn_find);
        rb_old_login = (RadioButton) findViewById(R.id.rb_old_login);
        rb_user_id_login = (RadioButton) findViewById(R.id.rb_user_id_login);
        rg_login_method = (RadioGroup) findViewById(R.id.rg_login_method);
        ll_old_user_id_wrap = (LinearLayout) findViewById(R.id.ll_old_user_id_wrap);

        edtUserAreaNo = (AppCompatEditText) findViewById(R.id.edtUserAreaNo);
        edtUserDistribId = (AppCompatEditText) findViewById(R.id.edtUserDistribId);
        edtUserAgencyId = (AppCompatEditText) findViewById(R.id.edtUserAgencyId);
        edtUserCourId = (AppCompatEditText) findViewById(R.id.edtUserCourId);

        rg_login_method.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                RadioButton select = (RadioButton) findViewById(id);
                if (select == rb_old_login) {
                    ll_old_user_id_wrap.setVisibility(View.VISIBLE);
                    user_id.setVisibility(View.INVISIBLE);
                } else if (select == rb_user_id_login) {
                    ll_old_user_id_wrap.setVisibility(View.INVISIBLE);
                    user_id.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            // 로그인 버튼을 눌렀을 때
            case R.id.btn_login:
                RadioGroup radioGroup = findViewById(R.id.rg_login_method);
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_old_login:
                        getOldLogin();
                        break;
                    case R.id.rb_user_id_login:
                        getUserIdLogin();
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
            OkHttpClient client = new OkHttpClient();
            String url = String.format("http://%s/chatt/app/login/login_no_get.php", hostname);

            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            urlBuilder.addQueryParameter("ucAreaNo", ((TextView) findViewById(R.id.edtUserAreaNo)).getText().toString());
            urlBuilder.addQueryParameter("ucDistribId", ((TextView) findViewById(R.id.edtUserDistribId)).getText().toString());
            urlBuilder.addQueryParameter("ucAgencyId", ((TextView) findViewById(R.id.edtUserAgencyId)).getText().toString());
            urlBuilder.addQueryParameter("ucMemCourId", ((TextView) findViewById(R.id.edtUserCourId)).getText().toString());
            urlBuilder.addQueryParameter("acPassword", ((TextView) findViewById(R.id.user_pw)).getText().toString());

            Request request = new Request.Builder()
                    .url(urlBuilder.build().toString())
                    .get()
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        final ErrorDto error = new Gson().fromJson(response.body().string(), ErrorDto.class);
                        Log.i("tag", error.message);
                        // 응답 실패
                        Log.i("tag", "응답실패");
                    } else {
                        ar = edtUserAreaNo.getText().toString();
                        di = edtUserDistribId.getText().toString();
                        ag = edtUserAgencyId.getText().toString();
                        me = edtUserCourId.getText().toString();
                        oldid = ar + di + ag + me;
                        String asd = "1";
                        // 응답 성공
                        Log.i("tag", "응답 성공");
                        final String responseData = response.body().string();
                        final LoginResponse loginResponse = new Gson().fromJson(responseData, LoginResponse.class);
                        runOnUiThread(() -> {
                            try {
                                // 아이디 비밀번호 일치 시 로그인 성공하게 하는 조건?
//                                if (oldid.equals(loginResponse.ucAreaNo + loginResponse.ucDistribId + loginResponse.ucAgencyId + loginResponse.ucMemCourId)) {
//                                    Intent intent = new Intent(MainActivity.this, User_Consent.class);
//                                    startActivity(intent);
//                                    Toast.makeText(getApplicationContext(), "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();
//                                }

                                // Option 두 개의 Value값이 1이라면 이미 동의를 한 것.
                                // 약관 동의를 마치면 Option이 1이 되게 하여 약관 동의 페이지를 생략하도록.
                                if(loginResponse.ucAgreeOption.equals("1") && loginResponse.ucThirdPartyOption.equals("1") ) {
                                    Intent intent = new Intent(MainActivity.this, ListActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent intent = new Intent(MainActivity.this, User_Consent.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "첫 로그인 시 약관 동의가 필요합니다.", Toast.LENGTH_SHORT).show();
                                }


//                                Toast.makeText(getApplicationContext(), "응답" + loginResponse.ucAreaNo + loginResponse.ucDistribId + loginResponse.ucAgencyId + loginResponse.ucMemCourId , Toast.LENGTH_SHORT).show();
//                                Log.d("tag", loginResponse.ucAgencyId);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            });

        } catch (Exception e) {

        }
    }

    public void getUserIdLogin() {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = String.format("http://%s/chatt/app/login/login_id_get.php", hostname);

            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            urlBuilder.addQueryParameter("acUserId", ((TextView) findViewById(R.id.user_id)).getText().toString());
            urlBuilder.addQueryParameter("acPassword", ((TextView) findViewById(R.id.user_pw)).getText().toString());


            Request request = new Request.Builder()
                    .url(urlBuilder.build().toString())
                    .get()
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        final ErrorDto error = new Gson().fromJson(response.body().string(), ErrorDto.class);
                        Log.i("tag", error.message);
                        // 응답 실패
                        Log.i("tag", "응답실패");
                    } else {
                        id = user_id.getText().toString();
                        pw = user_pw.getText().toString();
                        // 응답 성공
                        Log.i("tag", "응답 성공");
                        final String responseData = response.body().string();
                        final LoginResponse login_id_Response = new Gson().fromJson(responseData, LoginResponse.class);
                        runOnUiThread(() -> {
                            try {
                                if(id.equals(login_id_Response.acUserId)) {
                                    Intent intent = new Intent(MainActivity.this, User_Consent.class);
                                    startActivity(intent);
                                }
////                                    Toast.makeText(getApplicationContext(), "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();

                                Toast.makeText(getApplicationContext(), "" + login_id_Response.ucAreaNo, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {

        }
    }
}


