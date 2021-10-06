package com.example.testcode;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.FriendsResponse;
import com.example.testcode.model.Join_Response;
import com.example.testcode.model.User_Consent_Response;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 회원 가입
 * 맨 위에 회원번호(자동 생성)이 있는데 없애고 회원 가입을 완료하면
 * Toast 메시지로 생성 된 회원번호를 보여주는게 맞을 것 같아 수정할 예정.
 * 10/06 error code 416 발견 Response{protocol=http/1.1, code=416, message=Requested Range Not Satisfiable, url=http://222.239.254.253/chatt/app/users/user_post.php}
 *
 */

public class Join_Member extends AppCompatActivity {
    String hostname = "222.239.254.253";
    TextView member_number, authority;
    EditText join_id, join_pw, join_name, join_nickname, join_phone_number, join_mail;
    Button btn_join;
    Spinner spinner;
    static final String[] USER_CHOICE = {"일반 사용자, 관리자, 본사"};
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_member);

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("회원가입");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        //업버튼이 되려면 눌렀을 때 돌아갈 Activity를 지정해줘야 함

        //이 작업은 매니패스트에서 함

        authority = (TextView) findViewById(R.id.authority);
        join_id = (EditText) findViewById(R.id.join_id);
        join_pw = (EditText) findViewById(R.id.join_pw);
        join_name = (EditText) findViewById(R.id.join_name);
        join_nickname = (EditText) findViewById(R.id.join_nickname);
        join_phone_number = (EditText) findViewById(R.id.join_phone_number);
        join_mail = (EditText) findViewById(R.id.join_mail);
        btn_join = (Button) findViewById(R.id.btn_join);



    }

    public void onclick_join_member(View view) {
//        Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
//
        Intent intent = new Intent(Join_Member.this, MainActivity.class);
        startActivity(intent);

        Join();

    }

    public void Join() {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = String.format("http://%s/chatt/app/users/user_post.php", hostname);

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("acUserId", ((TextView) findViewById(R.id.join_id)).getText().toString())
                    .addFormDataPart("acPassword",((TextView) findViewById(R.id.join_pw)).getText().toString())
                    .addFormDataPart("acRealName",((TextView) findViewById(R.id.join_name)).getText().toString())
                    .addFormDataPart("acNickName",((TextView) findViewById(R.id.join_nickname)).getText().toString())
                    .addFormDataPart("acCellNo",((TextView) findViewById(R.id.join_phone_number)).getText().toString())
                    .addFormDataPart("acEmailAddress",((TextView) findViewById(R.id.join_mail)).getText().toString())
                    .addFormDataPart("ucAccessFlag","0")
                    .build();

//            RequestBody requestBody = new FormBody.Builder()
//                    .add("acUserId", ((EditText) findViewById(R.id.join_id)).getText().toString())
//                    .add("acPassword",((EditText) findViewById(R.id.join_pw)).getText().toString())
//                    .add("acRealName",((EditText) findViewById(R.id.join_name)).getText().toString())
//                    .add("acNickName",((EditText) findViewById(R.id.join_nickname)).getText().toString())
//                    .add("acCellNo",((EditText) findViewById(R.id.join_phone_number)).getText().toString())
//                    .add("acEmailAddress",((EditText) findViewById(R.id.join_mail)).getText().toString())
//                    .add("ucAccessFlag","0")
//                    .build();


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
                                AlertDialog.Builder builder = new AlertDialog.Builder(Join_Member.this);

                                builder.setTitle("회원가입이 완료되었습니다.").setMessage("코드 방식 : " + join_response.ucAreaNo + "-" +
                                        join_response.ucDistribId + "-" +
                                        join_response.ucAgencyId + "-" +
                                        join_response.ucMemCourId + " ,\n" +
                                        "아이디 방식 : " + join_response.acUserId + " 입니다.");

                                AlertDialog alertDialog = builder.create();

                                alertDialog.show();

                                        Toast.makeText(getApplicationContext(), "첫 로그인 시 약관 동의가 필요합니다.", Toast.LENGTH_SHORT).show();
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