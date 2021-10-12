package com.example.testcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.model.Chat_invite_room_Response;
import com.example.testcode.model.ErrorDto;
import com.google.gson.Gson;

import java.io.IOException;

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
            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.room_invite("1", "88", "17", "1", "1")
                    .enqueue(new retrofit2.Callback<Chat_invite_room_Response>() {
                        @Override
                        public void onResponse(retrofit2.Call<Chat_invite_room_Response> call,
                                               retrofit2.Response<Chat_invite_room_Response> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final Chat_invite_room_Response chat_invite_room_response = response.body();

                                    Toast.makeText(getApplicationContext(), "초대가 완료되었습니다.", Toast.LENGTH_SHORT).show();


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
                        public void onFailure(retrofit2.Call<Chat_invite_room_Response> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}