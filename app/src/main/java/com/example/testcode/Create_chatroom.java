package com.example.testcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.model.Chat_invite_room_Response;
import com.example.testcode.model.Create_chat_Response;
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
 * 채팅 메인 화면 (ListActivity)에서 우측 상단 새로운 채팅방 만들기 버튼.
 */

public class Create_chatroom extends AppCompatActivity {
    String hostname = "222.239.254.253";
    EditText write_room_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_invite_main);

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("새로운 채팅방 만들기");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        write_room_name = (EditText) findViewById(R.id.write_room_name);

//        Frag1 frag1 = new Frag1();

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
                create_chat();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void create_chat() {
        try {
            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.Create_chat("1", "")
                    .enqueue(new retrofit2.Callback<Create_chat_Response>() {
                        @Override
                        public void onResponse(retrofit2.Call<Create_chat_Response> call,
                                               retrofit2.Response<Create_chat_Response> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final Create_chat_Response create_chat_response = response.body();

                                    Toast.makeText(getApplicationContext(), "채팅방이 생성되었습니다.", Toast.LENGTH_SHORT).show();


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
                        public void onFailure(retrofit2.Call<Create_chat_Response> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}