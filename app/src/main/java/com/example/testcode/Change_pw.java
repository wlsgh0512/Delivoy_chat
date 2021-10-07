package com.example.testcode;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.Join_Response;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 채팅 메인 -> 3dot -> 설정 -> 비밀번호 변경
 * postman에서 실제로 데이터가 바뀌는거 확인.
 * 여기서도 dialog가 안뜸.
 * Chanege_info와 동일하게 휴대전화인증 미구현.
 */

public class Change_pw extends AppCompatActivity {
    TextView member_num;
    EditText input_now_pw, input_new_pw, input_new_pw_check;
    Button change_success, key_check;
    String hostname = "222.239.254.253";
    String ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);

        member_num = (TextView)findViewById(R.id.member_num);
        input_now_pw = (EditText)findViewById(R.id.input_now_pw);
        input_new_pw = (EditText)findViewById(R.id.input_new_pw);
        input_new_pw_check = (EditText)findViewById(R.id.input_new_pw_check);

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("비밀번호 변경");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        SharedPreferences sharedPreferences= getSharedPreferences("test", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
        ucAreaNo = sharedPreferences.getString("ar","");
        ucDistribId = sharedPreferences.getString("di","");
        ucAgencyId = sharedPreferences.getString("ag","");
        ucMemCourId = sharedPreferences.getString("me","");

        member_num.setText(ucAreaNo + "-" + ucDistribId + "-" + ucAgencyId + "-" + ucMemCourId);
    }

    public void onClick_change_pw(View view) {
        switch (view.getId()) {
            case R.id.key_check:
                Toast.makeText(this, "인증이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            // 휴대전화번호 인증 ~ 부분
                break;
            case R.id.change_success:
                if(!input_new_pw.getText().toString().equals(input_new_pw_check.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                else if (input_new_pw.getText().toString().equals(input_now_pw.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "현재 비밀번호와 일치합니다.", Toast.LENGTH_SHORT).show();
                }

                Change_pw();
                break;

        }

    }

    public void Change_pw() {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = String.format("http://%s/chatt/app/users/user_put.php", hostname);

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("ucAreaNo", ucAreaNo)
                    .addFormDataPart("ucDistribId",ucDistribId)
                    .addFormDataPart("ucAgencyId",ucAgencyId)
                    .addFormDataPart("ucMemCourId",ucMemCourId)
                    .addFormDataPart("acPassword",input_new_pw.getText().toString())
                    .addFormDataPart("ucAccessFlag","0")
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
                    if (!response.isSuccessful()) {
                        final ErrorDto error = new Gson().fromJson(response.body().string(), ErrorDto.class);
                        Log.i("tag", error.message);
                        // 응답 실패
                        Log.i("tag", "응답실패");
                    } else {
                        // 응답 성공
                        Log.i("tag", "응답 성공");
                        final String responseData = response.body().string();
                        final Join_Response join_response = new Gson().fromJson(responseData, Join_Response.class);
                        runOnUiThread(() -> {
                            try {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Change_pw.this);

                                builder.setTitle("비밀번호 변경이 완료되었습니다.");

                                AlertDialog alertDialog = builder.create();

                                alertDialog.show();

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