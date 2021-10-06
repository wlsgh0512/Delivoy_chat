package com.example.testcode;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    EditText input_name, input_phone_number;
    Button btn_search_pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        input_name = (EditText)findViewById(R.id.input_name);
        input_phone_number = (EditText)findViewById(R.id.input_phone_number);
        btn_search_pw = (Button)findViewById(R.id.btn_search_pw);

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("비밀번호 찾기");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기
    }

    public void onClick_find_pw(View view) {
        Find_pw();
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setTitle("회원님의 비밀번호는").setMessage("xxxxxxxxxxxxx입니다.");
//
//        AlertDialog alertDialog = builder.create();
//
//        alertDialog.show();
    }

    public void Find_pw() {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = String.format("http://%s/chatt/app/login/find_pw_get.php", hostname);

            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            urlBuilder.addQueryParameter("ucAreaNo", ((TextView) findViewById(R.id.edtUserAreaNo)).getText().toString());
            urlBuilder.addQueryParameter("ucDistribId", ((TextView) findViewById(R.id.edtUserDistribId)).getText().toString());
            urlBuilder.addQueryParameter("ucAgencyId", ((TextView) findViewById(R.id.edtUserAgencyId)).getText().toString());
            urlBuilder.addQueryParameter("ucMemCourId", ((TextView) findViewById(R.id.edtUserCourId)).getText().toString());
            urlBuilder.addQueryParameter("acUserId", ((TextView) findViewById(R.id.edtUserCourId)).getText().toString());
            urlBuilder.addQueryParameter("AcRealName", ((TextView) findViewById(R.id.input_name)).getText().toString());
            urlBuilder.addQueryParameter("acEmailAddress", ((TextView) findViewById(R.id.input_email)).getText().toString());
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
                        // 응답 실패
                        Log.i("tag", "응답실패");
                    } else {

                        // 응답 성공
                        Log.i("tag", "응답 성공");
                        final String responseData = response.body().string();
                        final LoginResponse loginResponse = new Gson().fromJson(responseData, LoginResponse.class);
                        runOnUiThread(() -> {
                            try {
                                Toast.makeText(getApplicationContext(), "임시 비밀번호가 메일로 전송되었습니다.", Toast.LENGTH_SHORT).show();
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
