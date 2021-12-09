package com.example.testcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.databinding.ActivityCreateChatroomBinding;
import com.example.testcode.databinding.FragmentFrag1Binding;
import com.example.testcode.databinding.FriendListBinding;
import com.example.testcode.model.Chat_invite_room_Response;
import com.example.testcode.model.Create_chat_Response;
import com.example.testcode.model.DataItem2;
import com.example.testcode.model.ErrorDto;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 채팅 메인 화면 (ListActivity)에서 우측 상단 새로운 채팅방 만들기 버튼.
 */

public class Create_chatroom extends AppCompatActivity {

    ActivityCreateChatroomBinding binding;

    String ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId;

    public ArrayList<String> roomList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateChatroomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("새로운 채팅방 만들기");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        SharedPreferences sharedPreferences= getSharedPreferences("test", MODE_PRIVATE);
        ucAreaNo = sharedPreferences.getString("ar","");
        ucDistribId = sharedPreferences.getString("di","");
        ucAgencyId = sharedPreferences.getString("ag","");
        ucMemCourId = sharedPreferences.getString("me","");

        SharedPreferences prefs = getSharedPreferences("roomName", MODE_PRIVATE);
        int size = prefs.getInt("Status_size", 0);
        for(int i=0;i<size;i++)
        {
            roomList.add(prefs.getString("Status_" + i, null));
        }


//        binding.friendList.fetchFriendList(ucAreaNo,ucDistribId,ucAgencyId,ucMemCourId);


        binding.roomList.fetchRoomList(ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId);

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
            final String roomName = binding.writeRoomName.getText().toString();
            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.Create_chat(roomName,
                    "",
                    ucAreaNo,
                    ucDistribId,
                    ucAgencyId,
                    ucMemCourId
            )
                    .enqueue(new retrofit2.Callback<Create_chat_Response>() {
                        @Override
                        public void onResponse(retrofit2.Call<Create_chat_Response> call,
                                               retrofit2.Response<Create_chat_Response> response) { if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final Create_chat_Response create_chat_response = response.body();

                                    if(roomList.contains(roomName)) {
                                        Toast.makeText(getApplicationContext(),
                                                "기존에 생성된 채팅방과 이름이 같습니다. 채팅방 이름을 다시 입력하세요.",
                                                Toast.LENGTH_SHORT).show();
                                        return;
                                    } else {

                                        Toast.makeText(getApplicationContext(), create_chat_response.acRoomTitle +
                                                " 채팅방을 생성했습니다.", Toast.LENGTH_SHORT).show();
                                        binding.writeRoomName.setText("");
                                    }
//                                    SharedPreferences sharedPreferences = getSharedPreferences("create", MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                                    editor.putString("uiroomno", create_chat_response.uiRoomNo);
//                                    editor.putString("roomtitle", create_chat_response.acRoomTitle);
//                                    editor.commit();


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

//    public void cir() {
//        try {
//            SharedPreferences sharedPreferences = getSharedPreferences("create", MODE_PRIVATE);
//            String rn = sharedPreferences.getString("uiroomno", "");
//            String rt = sharedPreferences.getString("roomtitle", "");
//
////            final String newUser = binding.findList.getText().toString();
//            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
//            service.room_invite(
//                    rn,
//                    ucAreaNo,
//                    ucDistribId,
//                    ucAgencyId,
//                    ucMemCourId)
//                    .enqueue(new retrofit2.Callback<Chat_invite_room_Response>() {
//                        @Override
//                        public void onResponse(retrofit2.Call<Chat_invite_room_Response> call,
//                                               retrofit2.Response<Chat_invite_room_Response> response) {
//                            if (response.isSuccessful()) {
//                                // 응답 성공
//                                Log.i("tag", "응답 성공");
//                                try {
//                                    final Chat_invite_room_Response chat_invite_room_response = response.body();
//
//                                    Toast.makeText(getApplicationContext(), rt + "를" + rn + "에" + "초대 했습니다.", Toast.LENGTH_SHORT).show();
//
//
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                final ErrorDto error;
//                                try {
//                                    error = new Gson().fromJson(response.errorBody().string(),
//                                            ErrorDto.class);
//                                    Log.i("tag", error.message);
//                                    // 응답 실패
//                                    Log.i("tag", "응답실패");
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(retrofit2.Call<Chat_invite_room_Response> call, Throwable t) {
//
//                        }
//                    });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}