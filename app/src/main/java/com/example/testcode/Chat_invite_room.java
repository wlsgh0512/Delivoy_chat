package com.example.testcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testcode.model.Chat_invite_main_Response;
import com.example.testcode.model.ErrorDto;
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
 * 채팅창 화면 (Chat)에서 우측 상단 새로운 채팅방 만들기 버튼.
 * 내비게이션 드로어를 넣어서 카카오톡 방식으로 서랍식 구현 할 예정.
 */

public class Chat_invite_room extends AppCompatActivity {
    String hostname = "222.239.254.253";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_invite);

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("대화상대 초대");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

//        Frag1 frag1 = new Frag1();
//
//        frag1.getContext();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat_invite_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.create_chat:
                Chat_invite_room();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void Chat_invite_room() {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = String.format("http://%s/chatt/app/groups/group_post.php", hostname);

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("ulRoomNo", ((TextView) findViewById(R.id.write_room_name)).getText().toString())
                    .addFormDataPart("ucAreaNo", ((TextView) findViewById(R.id.write_room_name)).getText().toString())
                    .addFormDataPart("ucDistribId", ((TextView) findViewById(R.id.write_room_name)).getText().toString())
                    .addFormDataPart("ucAgencyId", ((TextView) findViewById(R.id.write_room_name)).getText().toString())
                    .addFormDataPart("ucMemCourId", ((TextView) findViewById(R.id.write_room_name)).getText().toString())
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
                        final Chat_invite_main_Response chat_invite_main_response = new Gson().fromJson(responseData, Chat_invite_main_Response.class);
                        runOnUiThread(() -> {
                            try {
                                Toast.makeText(getApplicationContext(), "채팅방이 생성되었습니다." + chat_invite_main_response.acRoomTitle, Toast.LENGTH_SHORT).show();
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