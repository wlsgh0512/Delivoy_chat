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

import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.Join_Response;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 채팅 메인 -> 3dot 정보수정.
 * postman에서 실제로 데이터가 바뀌는거 확인.
 * 여기서도 dialog 뜨기전에 죽는데 일단 데이터는 바뀌어서 다른거부터.
 * 1개라도 변경된 것이 있어야 확인 버튼 눌렀을 때 server로 전송되게 할 것. (변경 x -> 종료)
 * 변경은 되지만 휴대전화번호 인증키 미구현.
 * 다른 거 작업하고 다시 구현하기.
 * logcat에는 Expected BEGIN_OBJECT but was STRING at line 8 column 1 path $ 출력.
 */

public class Change_info extends AppCompatActivity {
    String hostname = "222.239.254.253";
    TextView input_member_num, input_user_id,  real_name, authority;
    EditText input_nickname, input_phone_number, input_email_address;
    String ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId, user_id, name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("정보 수정");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        input_member_num = (TextView)findViewById(R.id.input_member_num);
        input_user_id = (TextView)findViewById(R.id.input_user_id);
        real_name = (TextView)findViewById(R.id.real_name);

        input_nickname = (EditText) findViewById(R.id.input_nickname);
        input_phone_number = (EditText) findViewById(R.id.input_phone_number);
        input_email_address = (EditText) findViewById(R.id.input_email_address);

        SharedPreferences sharedPreferences= getSharedPreferences("test", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
        ucAreaNo = sharedPreferences.getString("ar","");
        ucDistribId = sharedPreferences.getString("di","");
        ucAgencyId = sharedPreferences.getString("ag","");
        ucMemCourId = sharedPreferences.getString("me","");
        user_id = sharedPreferences.getString("id","");
        name = sharedPreferences.getString("name","");

        input_member_num.setText(ucAreaNo + "-" + ucDistribId + "-" + ucAgencyId + "-" + ucMemCourId);
        input_user_id.setText(user_id);
        real_name.setText(name);
    }

    // 휴대전화 인증
    public void onClick_check(View view) {

    }

    // 확인 버튼
    public void onClick_finish(View view) {
        Change_user_info();
    }



    public void Change_user_info() {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = String.format("http://%s/chatt/app/users/user_put.php", hostname);

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("ucAreaNo", ucAreaNo)
                    .addFormDataPart("ucDistribId",ucDistribId)
                    .addFormDataPart("ucAgencyId",ucAgencyId)
                    .addFormDataPart("ucMemCourId",ucMemCourId)
                    .addFormDataPart("acNickName",input_nickname.getText().toString())
                    .addFormDataPart("acCellNo",input_phone_number.getText().toString())
                    .addFormDataPart("acEmailAddress",input_email_address.getText().toString())
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(Change_info.this);

                                builder.setTitle("정보 수정이 완료되었습니다.");

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