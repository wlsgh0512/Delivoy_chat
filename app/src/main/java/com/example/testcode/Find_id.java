package com.example.testcode;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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

public class Find_id extends AppCompatActivity {
    String hostname = "222.239.254.253";
    EditText input_phone_number, input_real_name;
    ImageButton btn_find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);
        input_phone_number = (EditText) findViewById(R.id.input_phone_number);
        input_real_name = (EditText)findViewById(R.id.input_real_name);
        btn_find = (ImageButton) findViewById(R.id.btn_find);

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("아이디 찾기");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

    }

    public void onClick_find_id(View view) {
        // tv_phone_number, input_phone_number , btn_find
        Find_id();

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setTitle("회원님의 아이디는").setMessage("xxxxxxxxxxxxx입니다.");
//
//        AlertDialog alertDialog = builder.create();
//
//        alertDialog.show();
    }

    public void Find_id() {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = String.format("http://%s/chatt/app/login/find_id_get.php", hostname);

            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            urlBuilder.addQueryParameter("acRealName", ((TextView) findViewById(R.id.input_real_name)).getText().toString());
            urlBuilder.addQueryParameter("acCellNo", ((TextView) findViewById(R.id.input_phone_number)).getText().toString());


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
                        // 응답 실패
                        Log.i("tag", "응답실패");
                    } else {

                        // 응답 성공
                        Log.i("tag", "응답 성공");
                        final String responseData = response.body().string();
                        final LoginResponse loginResponse = new Gson().fromJson(responseData, LoginResponse.class);
                        runOnUiThread(() -> {
                            try {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Find_id.this);

                                builder.setTitle("회원님의 아이디는").setMessage("코드 방식 : " + loginResponse.ucAreaNo + "-" +
                                        loginResponse.ucDistribId + "-" +
                                        loginResponse.ucAgencyId + "-" +
                                        loginResponse.ucMemCourId + " ,\n" +
                                        "아이디 방식 : " + loginResponse.acUserId + " 입니다.");

                                AlertDialog alertDialog = builder.create();

                                alertDialog.show();
//                                Toast.makeText(getApplicationContext(), "응답" + findidResponse.ucAreaNo, Toast.LENGTH_SHORT).show();
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

//    public void showIdDialog(String userId) {
//        // response를 받아서 getUserId?
//        // showIdDialog(response.userId);
//        new AlertDialog.Builder(this)
//                .setTitle("회원님의 아이디는")
//                .setMessage(String.format("%s 입니다", userId))
//                .show();
//    }
}