package com.example.testcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.example.testcode.databinding.ActivityChatFriendsBinding;
import com.example.testcode.databinding.ActivityChatInviteBinding;
import com.example.testcode.model.Chat_invite_room_Response;
import com.example.testcode.model.DataItem2;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.FriendsResponse;
import com.example.testcode.model.User_Response;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 채팅창 화면 (Chat)에서 우측 상단 새로운 채팅방 만들기 버튼.
 * 내비게이션 드로어를 넣어서 카카오톡 방식으로 서랍식 구현 할 예정.
 */

public class Chat_invite_room extends AppCompatActivity {

    ActivityChatInviteBinding binding;
    String testUiRoomNo, ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId,
            ucFriAreaNo, ucFriDistribId, ucFriAgencyId,ucFriMemCourId, invite_mem;
    ArrayList<String> room_invite_list = new ArrayList<String>();
    public List<FriendsResponse.Items.Rooms> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatInviteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
//        actionBar.setTitle("대화상대 초대");  //액션바 제목설정
//        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        testUiRoomNo = getIntent().getStringExtra("RoomNo");

        SharedPreferences sharedPreferences = getSharedPreferences("test", MODE_PRIVATE);
        ucAreaNo = sharedPreferences.getString("ar", "");
        ucDistribId = sharedPreferences.getString("di", "");
        ucAgencyId = sharedPreferences.getString("ag", "");
        ucMemCourId = sharedPreferences.getString("me", "");


        SharedPreferences sharedPreferences1 = getSharedPreferences("zzzz", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
        ucFriAreaNo = sharedPreferences1.getString("far", "");
        ucFriDistribId = sharedPreferences1.getString("fd", "");
        ucFriAgencyId = sharedPreferences1.getString("fag", "");
        ucFriMemCourId = sharedPreferences1.getString("fm", "");

//        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, room_invite_list);
//        binding.inviteFriendsList.setAdapter(adapter);

        binding.friendList.fetchFriendList(ucAreaNo,ucDistribId,ucAgencyId,ucMemCourId);

        binding.friendList.setOnItemClickListener(new f1Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
//                String aaa = items.get(position).ucMemCourId;

                String qqw = binding.friendList.items.get(position).acRealName;
                String wwq = binding.friendList.items.get(position).ucMemCourId;
                binding.inviteUser.setText(qqw);

//                String select = String.valueOf(binding.friendList.friends_name.get(position));
//                binding.inviteUser.setText(select);

                SharedPreferences spf = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor edi = spf.edit();
                edi.putString("select", wwq);
                edi.commit();
            }
        });

//        binding.friendList.setOnItemClickListener((v, position) -> {
//
//            v.setBackgroundColor(Color.LTGRAY);
//
//            binding.inviteUser.setText("asd");
//
//        });
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
            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
            invite_mem = sharedPreferences.getString("select", "");

            final String newUser = binding.findList.getText().toString();
            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.room_invite(
                    testUiRoomNo,
                    ucAreaNo,
                    ucDistribId,
                    ucAgencyId,
                    invite_mem)
                    .enqueue(new retrofit2.Callback<Chat_invite_room_Response>() {
                        @Override
                        public void onResponse(retrofit2.Call<Chat_invite_room_Response> call,
                                               retrofit2.Response<Chat_invite_room_Response> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final Chat_invite_room_Response chat_invite_room_response = response.body();

                                    Toast.makeText(getApplicationContext(), "대화상대 초대 되었습니다.", Toast.LENGTH_SHORT).show();



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